(function($) {

    /**
     * dataset 构造
     * @param id
     * @param meta
     * @param lazyLoad
     * @param editable
     * @param pageSize
     * @class
     */
    $.dataset = function(opts) {
        this.element = $(this);
        /* 当前Dataset Id */
        this.id = opts.id;
        // Field
        this.fieldList = opts.meta;
        // 是否缓加
        this.lazyLoad = opts.lazyLoad;
        // 存储请求参数。Dataset本身不负责此Parameter的清理工作，因为类似刷新操作会重复利用里面的参数
        this.reqParameterMap = $.hashmap.getObj();
        // 存储响应参数。Dataset在每次请求返回时，清空并重新加载此参
        this.resParameterMap = $.hashmap.getObj();
        // 用于存储当前处理的dataset的参照装载字段，key:参照主字段ID，value:被参照出来的字段的ID数组
        this.sourceRefMap = null;
        // 此Dataset关联的控观察列表.注意,通知控件绑定并不是独立线程级因此Dataset部分无需考虑同步
        this.compArr = [];
        // //重做列表
        this.undoArr = [];
        // 静默状态。主要用于发生数据改变而不进行控件通知，在所有处理完后统一通知。避免控件反复渲
        this.silent = false;
        // 记录当前Dataset对控件的可编辑
        this.editable = opts.editable;
        // 记录此Dataset的编辑属性是否被修改过
        this.editableChanged = false;
        // ds操作状
        this.operateStateArray = null;
        this.pageSize = opts.pageSize;
        // 当前据交行行号
        this.focusRowIndex = -1;
        // 随机RowId计数器
        this.randomRowIndex = 0;
        //pageDatas属性  有关分页的信息
        this.pagecount = 1;
        this.pageindex = 0;
        this.recordcount = 0;
        this.pageDatas = [];
    };

    $.extend($.dataset.prototype, {
        addField : function(field) {
            this.fieldList.push(field);
        },
        removeField : function(keyName) {
            for (var i = 0; i < this.fieldList.length; i++) {
                if (this.fieldList[i].key == keyName) {
                    this.fieldList.splice(i, 1);
                    break;
                }
            }
        },
        destroySelf : function() {
        },
        /**
         * 获得返回参数
         * @param key 参数 return 参数
         */
        getResponseParameter : function(key) {
            return this.resParameterMap.get(key);
        },
        /**
         * 解析响应参数，并清空旧有相应参数
         * @private
         */
        genResParameter : function(rootNode) {
            this.resParameterMap.clear();
            var nodes = rootNode.selectNodes($.EventContextConstant.res_parameters + "/" + $.EventContextConstant.parameter);
            if (nodes != null) {
                for (var i = 0; i < nodes.length; i++)
                    this.resParameterMap.put($.pageutils.getNodeAttribute(nodes[i], "name"), $.pageutils.getNodeValue(nodes[i]));
            }
        },
        /**
         * @private
         */
        modifyStruct : function(newmeta) {
            this.fieldList = newmeta;
        },
        /**
         * 清空Dataset内容
         */
        clear : function() {
	        this.pagecount = 1;
	        this.pageindex = 0;
	        this.recordcount = 0;
	        this.pageDatas = [];
        },
        setData : function(dsJosnObj, userObj) {
            if (!dsJosnObj)
                return;
//            var isCleared = $.argumentutils.getBoolean(dsJosnObj["isCleared"], false);
//            if (isCleared) {
//            	//&& this.pageDatas.length == 0
//	            this.pagecount = 1;
//		        this.pageindex = 0;
//		        this.recordcount = 0;
//		        this.pageDatas = [];
//            }
            this.randomRowIndex = dsJosnObj["randomRowIndex"];
            var allPageJson = dsJosnObj["allPage"];
            if (allPageJson != null) {             
	            this.pageindex = parseInt(allPageJson["pageindex"]);
	            this.pagecount = parseInt(allPageJson["pagecount"]);
	            this.pageSize = parseInt(allPageJson["pageSize"]);
	            this.recordcount = parseInt(allPageJson["recordcount"]);
	            var pageDatasJson = allPageJson["pageDatas"];
	            if (pageDatasJson) {
	                for (var i = 0; i < pageDatasJson.length; i++) {
	                    var onePageJson = pageDatasJson[i];
	                    var pIndex = onePageJson["pageindex"];
	                    var pageData = this.pageDatas[parseInt(pIndex)];
	                    if (pageData != null) {
	                        pageData.setData(onePageJson);
	                    } else {
	                        pageData = $.pageData.getObj({
	                            onePageJson : onePageJson,
	                            dataset : this
	                        });
	                        this.pageDatas[parseInt(pIndex)] = pageData;
	                        pageData.setData(onePageJson, true);
	                    }
	                    if (this.recordcount == 0)
	                        this.recordcount = pageData.getRowCount();
	                }
	            }
            }
            var editable = dsJosnObj["editable"];
            if ((this.editable + "") != editable) {
                this.setEditable(editable);
            }

            var focusIndex = dsJosnObj["focusIndex"];
            this.focusRowIndex = focusIndex;
//            if (!this.silent) {
                var event = {
                    type : 'FocusChangeEvent',
                    focusIndex : this.focusRowIndex,
                    currentRowIndex : this.focusRowIndex,
                    currentRow : this.getRow(this.focusRowIndex)
                };
                this.dispatchEvent(event);
//            }
        },
        /**
         * 处理fieldList相关设置
         */
        setMeta : function(jsonData) {
            if (jsonData && jsonData.precision) {
                var precisions = jsonData.precision;
                for (var field in precisions) {
                    var precision = precisions[field];
                    var index = this.nameToIndex(field);
                    if (index == -1)
                        continue;
                    var fieldList = this.fieldList[index];
                    //精度不存在，或精度发生变化
                    if (fieldList.precision == null || fieldList.precision != precision) {
                        fieldList.precision = precision;
                        var event = {
                            type : 'MetaChangeEvent',
                            colIndex : index,
                            precision : precision
                        };
                        this.dispatchEvent(event);
                    }
                }
            }
        },
        /**
         * 获得keyValue指定子集的分页数
         *
         * @param keyValue 可为空，为空则取当前keyValue
         * @return 分页数目
         */
        getPageCount : function() {
            if (this.pageDatas.length == 0)
                return 0;
            return this.pagecount;
        },
        /**
         * 获取指定页的当前页码
         *
         * @param keyValue 可为空，为空则取当前keyValue
         * @return 当前页索
         */
        getPageIndex : function() {
            if (this.pageDatas.length == 0)
                return 0;
            return this.pageindex;
        },
        /**
         * 此方法仅用于客户端进行Dataset初始化时调用，非客户端初始化，请不要调用。
         * @private
         */
        initialize : function() {
            var event = {
                type : 'PageChangeEvent',
                pageIndex : 0,
                oldPageIndex : (this.pageDatas == null) ? -1 : this.pageindex
            };
            this.dispatchEvent(event);
            this.onAfterPageChange(event);
        },
        /**
         * 设置页面显示数量
         */
        setPageSize : function(pageSize) {
            //当前页面大小
            this.pageSize = pageSize;
		    this.pageDatas = [];
            // 设置显示空白
            this.setCurrentPage(-1, -1);
        },
        /**
         * 清除keyValue页的所有数据
         */
        clearData : function() {
            this.initialize();
        },
        appendCurrentPage : function(userObj) {
            this.onDataLoad(0, userObj);
            return;
        },
        /**
         * 设置当前将通知观察者进行数据重新绑定
         *
         * @param keyValue
         * @param index 页码 -1表示当前数据没有选中
         * @param userObj 用来反传回调用方的参数。用来解决异步调用无法跟踪发起方的问
         */
        setCurrentPage : function(index, userObj, isRefresh, append) {
            if (!index)
                index = 0;
            // 强制成整型，防止其它控件逻辑出错
            index = parseInt(index);
            // 没有选中页情
            if (index == -1) {
                if (this.pageDatas)
                    this.pageindex = -1;
//                if (!this.silent) {
                    var event = {
                        type : 'PageChangeEvent'
                    };
                    this.dispatchEvent(event);
//                }
            }
            //如果页面没有请求过，发送缺页请求。一般缺页处理函数将处理缺页，并再次调用此方
            if (!this.isDataRequested(index) || (isRefresh != null && isRefresh == true) || append) {
                this.reqParameterMap.put($.dataset.CONST.QUERY_PARAM_PAGEINDEX, index);
                this.onDataLoad(index, userObj);
                return;
            }
            if (this.pageDatas) {
                //-1代表特殊页面
                if (index > this.pagecount - 1) {
                    alert("the page index:" + index + " is not right");
                    return;
                }
                if (this.onBeforePageChange(index) == false)
                    return;
                var event = {
                    type : 'PageChangeEvent',
                    pageIndex : index,
                    oldPageIndex : this.pageindex,
                    userObject : userObj
                };

                this.pageindex = index;

//              if (!this.silent)
                this.dispatchEvent(event);
                if (this.onInternalPageChange) {
                    this.onInternalPageChange(event);
                }
                if (!this.silent)
                    this.onAfterPageChange(event);
            }
            var indices = this.getSelectedIndices();
            if (indices != null && indices.length > 0) {
                for (var i = 0; i < indices.length; i++) {
                    this.addRowSelectedInternal(indices[i], true, true);
                }
            }
        },
        /**
         * 设置请求参数Map
         *
         * @param paramMap
         */
        setReqParameterMap : function(paramMap) {
            this.reqParameterMap = paramMap;
        },
        /**
         * 添加请求参数
         *
         * @key 参数
         * @value 参数
         */
        addReqParameter : function(key, value) {
            this.reqParameterMap.put(key, value);
        },
        /**
         * 设置绑定控件，dataset在有变化时将使用适当事件对控件进行通知。要求所有具备绑定dataset功能的控件
         * 必须具备onModelChange(event)的方法实现。
         * @param comp
         */
        bindComponent : function(comp) {
            //如果dataset已经绑定控件comp，则return.否则会出现重复绑定的情况，会出现多行
            if (this.compArr.indexOf(comp) != -1)
                return;
            if (this.lazyLoad == false) {
                //lazyLoadDs 会使tab和outlookbar出问题，看不到数据
                //		if (!this.dsLoaded){
                //			Dataset.lazyLoadDs(this);
                //		}
                if (!this.dsLoaded) {
                    this.dsLoaded = true;
                    this.setCurrentPage(0);
                    this.reqParameterMap.remove("openBillId");
                }
            }
            this.compArr.push(comp);
        },
        /**
         * 解除控件绑定 dataset将去除和此控件的关联
         */
        unbindComponent : function(comp) {
            var compindex = [];
            for (var i = 0; i < this.compArr.length; i++) {
                if (this.compArr[i] == comp)
                    compindex.push(this.compArr[i]);
                //this.compArr[i].splice(i, 1);
            }
            for (var i = 0; i < compindex.length; i++) {
                this.compArr.splice(compindex[i], 1);
            }
        },
        /**
         * 通过Dataset集中控制Dataset的可编辑，所有绑定此dataset的控件将被设置成editable的状态。
         *
         * @param editable{boolean} 是否可编
         */
        setEditable : function(editable) {
            this.editableChanged = true;
            this.editable = editable;
            for (var i = 0; i < this.compArr.length; i++) {
                if (this.compArr[i].setEditable)
                    this.compArr[i].setEditable(editable);
            }
        },
        /**
         * 获取Dataset的可编辑
         */
        isEditable : function(editable) {
            return this.editable;
        },
        /**
         * 获取翻页对象
         */
        getPaginations : function() {
            var compArr = [];
            for (var i = 0; i < this.compArr.length; i++) {
                //TODO:处理完grid分页再修改此处
                if (this.compArr[i] instanceof PaginationComp)
                    compArr.push(this.compArr[i]);
            }
            return compArr;
        },
        /**
         * 设置下一行为当前行
         */
        nextRow : function() {
            this.changeDisplayRow(0);
        },
        /**
         * 设置最后一行为当前行
         */
        lastRow : function() {
            this.changeDisplayRow(3);
        },
        /**
         * 设置第一行为当前行
         */
        firstRow : function() {
            this.changeDisplayRow(2);
        },
        /**
         * 设置上一行为当前行
         */
        preRow : function() {
            this.changeDisplayRow(1);
        },
        /**
         * 设置当前供其它方法调用
         *
         * @private
         * @param type 设置方式
         */
        changeDisplayRow : function(type) {
            if (this.pageDatas.length == 0)
                throw new Error("no data");
            var currPage = this.currPage;
            var oldRow = null;
            if (this.selectArr[currPage].selectedIndices == null)
                this.selectArr[currPage].selectedIndices = [];
            else if (this.selectArr[currPage].selectedIndices.length > 0)
                oldRow = this.getRow(this.selectArr[currPage].selectedIndices[0]);
            var index = -1;
            if (type == 0) {
                if (oldRow != null)
                    index = this.getRowIndex(oldRow);
                if (index == -1)
                    index = 0;
                else if (index >= this.getRows().length - 1)
                    index = 0;
                else
                    index = index + 1;
            }else if (type == 1) {           //pre Row
                if (oldRow != null)
                    index = this.getRowIndex(oldRow);
                if (index == -1)
                    index = 0;
                else if (index == 0)
                    index = this.getRows().length - 1;
                else
                    index = index - 1;
            }else if (type == 2) {           //first Row
                index = 0;
            }else if (type == 3) {           //last Row
                index = this.getRows().length - 1;
            }
            this.addRowSelectedInternal(index, false);
        },
        /**
         * 获取行索引
         */
        getRowIndex : function(row, pageIndex) {
            var indices = this.getRowIndices([row], pageIndex);
            return (indices == null || indices.length == 0) ? -1 : indices[0];
        },
        /**
         * 获取查询行索引
         */
        getRowIndices : function(rows, pageIndex) {
            if (this.pageDatas.length == 0)
                return null;
            var pd = null;
            if (pageIndex == null || pageIndex == -1) {
                pd = this.getCurrentPageData();
            } else {
                pd = this.getPageData(pageIndex);
            }
            return pd.getRowIndices(rows);
        },
        /**
         * 派发事件,包括控件派发和主子逻辑派发
         * @private
         */
        dispatchEvent : function(event) {
            if (this.recordUndoSign) {
                if (event.type && event.type != 'DatasetUndoEvent') {
                    this.undoArr.push(event);
                }
            }
            for (var i = 0; i < this.compArr.length; i++) {
                this.compArr[i].onModelChanged(event, this);
            }
            this.viewpart.dispatchEvent2Ds(event, this.id);
        },
        /**
         * 设置当前聚焦行
         */
        setFocusRowIndex : function(index) {
            if (index < 0)
                this.focusRowIndex = -1;
            else
                this.focusRowIndex = index;
            var event = {
                type : 'FocusChangeEvent',
                focusIndex : this.focusRowIndex,
                currentRowIndex : this.focusRowIndex,
                currentRow : this.getRow(this.focusRowIndex)
            };
            this.dispatchEvent(event);
            this.onFocusChange(event);
        },
        /**
         * 获取当前聚焦行
         */
        getFocusRowIndex : function() {
            return this.focusRowIndex;
        },
        /**
         * 获取当前聚焦行
         */
        getFocusRow : function() {
            return this.getRow(this.focusRowIndex);
        },
        /**
         * 设置行选中
         */
        setRowSelected : function(index) {
            this.addRowSelectedInternal(index, false);
        },
        /**
         * 增加选中行
         */
        addRowSelected : function(index) {
            this.addRowSelectedInternal(index, true);
        },
        /**
         * 增加选中行
         *
         * 在设置currentPage时，需要dispath事件
         *
         * @private
         */
        addRowSelectedInternal : function(indices, isAdd, isSetCurrentPage) {
            isSetCurrentPage = $.argumentutils.getBoolean(isSetCurrentPage, false);
            var index = indices;
            if ( indices instanceof Array) {
                index = indices[0];
            }
            if (this.pageDatas.length == 0)
                throw new Error("no data");

            var pageData = this.getCurrentPageData();
            var selIndices = pageData.getSelectedIndices();
            if (selIndices != null && selIndices.length > 0) {
                //if(selIndices.indexOf(index) != -1)
                //	return;
                if (!isAdd) {
                    // 获取上一个选中且修改的
                    var curSelRow = this.getRow(selIndices[0]);
                    if (curSelRow != null && (curSelRow.state == $.datasetrow.CONST.STATE_UPD || curSelRow.state == $.datasetrow.CONST.STATE_NEW)) {
                        this.checkDatasetRow(curSelRow);
                    }
                }
            }
            if (this.onBeforeRowSelect(index) == false)
                return false;

            // 设置之前选中
            var event = {
                type : 'RowSelectEvent'
            };
            var unSelEvent = {
                type : 'RowUnSelectEvent'
            };
            //dingrf 之前为非选中状态，变为选中状态
            var newSelected = false;
            //if (index == 0)
            //	newSelected = true;
            event.lastSelectedIndices = selIndices;
            if ( indices instanceof Array) {
                event.isMultiSelect = true;
            }

            if (selIndices == null) {
                pageData.selectedIndices = [];
                selIndices = pageData.getSelectedIndices();
            } else if (selIndices.length > 0) {
                unSelEvent = {
                    currentRowIndex : selIndices[0]
                };
                if (!isAdd || index == -1)
                    selIndices.splice(0, selIndices.length);
            }
            if (index != -1) {
                var newRow = this.getRows()[index];
                if ( indices instanceof Array) {
                    for (var i = 0; i < indices.length; i++) {
                        var id = indices[i];
                        var iid = selIndices.indexOf(id);
                        if (iid == -1) {
                            selIndices.push(id);
                            newSelected = true;
                        }
                    }
                } else {
                    var iid = -1;
                    if (selIndices.length > 0)
                        iid = selIndices.indexOf(index);
                    if (iid == -1) {
                        selIndices.push(index);
                        newSelected = true;
                    }
                }

                event.currentRow = newRow;
                event.currentRowIndex = index;
            }
            event.isAdd = isAdd;
//            if (!this.silent && (newSelected || isSetCurrentPage)) {
            if ((newSelected || isSetCurrentPage)) {
                if (!isAdd)
                    this.dispatchEvent(unSelEvent);
                this.dispatchEvent(event);
            }
//            if (!this.silent && (newSelected || isSetCurrentPage)) {
            if ((newSelected || isSetCurrentPage)) {
                this.onAfterRowSelect(event);
            }
        },
        /**
         * 对指定cell进行设置错误信息操作
         * @private
         */
        setError : function(rowIndex, colIndex, msg) {
            this.dispatchEvent({
                type : 'DataCheckEvent',
                cellColIndices : [colIndex],
                currentRow : this.getRow(rowIndex),
                rulesDescribe : [msg]
            });
        },
        /**
         * 清除指定cell的错误信息
         * @private
         */
        clearError : function(rowIndex, colIndex) {
            this.dispatchEvent({
                type : 'DataCheckEvent',
                cellColIndices : [colIndex],
                currentRow : this.getRow(rowIndex),
                rulesDescribe : [""]
            });
        },
        /**
         * 设置指定行的选中状态
         *
         * @param index{int} 指定行索引
         */
        setRowUnSelected : function(index) {
            this.getCurrentPageData().setRowUnSelected(index);
        },
         /**
         * 设置所有行非选中
         *
         */
        setAllRowUnSelected : function() {
            this.getCurrentPageData().setAllRowUnSelected();
        },
        /**
         * 获取选中行索引
         */
        getSelectedIndex : function() {
            return this.getCurrentPageData().getSelectedIndex();
        },
         /**
         * 获取所有选中行索引
         */
        getSelectedIndices : function() {
            return this.getCurrentPageData().getSelectedIndices();
        },
        /**
         * 返回更新如果分页,则仅返回当前页的
         * @return 更新行
         */
        getUpdatedRows : function(pageIndex) {
            if (this.pageDatas.length == 0)
                throw new Error("no data");
            var currPage = pageIndex ? pageIndex : this.currPage;
            var records = this.getRows(currPage);
            if (records == null)
                return null;
            var rsArr = [];
            for (var i = 0; i < records.length; i++) {
                if (records[i].state == $.datasetrow.CONST.STATE_UPD)
                    rsArr.push(records[i]);
            }
            return rsArr;
        },
        /**
         * 返回已删除行，如果分页，则仅返回当前页的删除。
         * 注意:如果已经调用过clearState方法，则删除行将被清空。
         * @return 已删除行
         */
        getDeletedRows : function() {
            if (this.pageDatas.length == 0)
                return null;
            var delArr = this.deletedRows;
            if (delArr == null)
                return null;
            var arr = [];
            // 拷贝并修改为del状态
            for (var i = 0; i < delArr.length; i++) {
                var row = delArr[i].clone();
                row.state = $.datasetrow.CONST.STATE_DEL;
                row.rowId = delArr[i].rowId;
                arr.push(row);
            }

            return arr;
        },
        /**
         * 返回新增如果分页，则仅返回当前页的新增行
         * @return 新增行
         */
        getNewAddedRows : function(pageIndex) {
            if (this.pageDatas.length == 0)
                throw new Error("no data");
            var currPage = pageIndex ? pageIndex : this.currPage;
            var records = this.getRows(currPage);
            if (records == null)
                return records;
            var rsArr = [];
            for (var i = 0; i < records.length; i++) {
                if (records[i].state == $.datasetrow.CONST.STATE_NEW)
                    rsArr.push(records[i]);
            }
            return rsArr;
        },
        /**
         * 清除行状恢复普通状，主要用在操作完成，将新增行等转换为普通行
         */
        clearState : function() {
            if (this.pageDatas) {
                var records = this.getRows(-1);
                if (records != null) {
                    for (var i = 0; i < records.length; i++)
                        records[i].state = $.datasetrow.CONST.STATE_NRM;
                }
                this.deletedRows = null;
            }
            //	this.undoArr.clear();
            this.dispatchEvent({
                type : 'StateClearEvent'
            });
        },
        recordUndo : function() {
            this.clearUndo();
            this.recordUndoSign = true;
        },
        /**
         * undo操作，一次回退到重做链头部
         */
        undo : function() {
            if (this.undoArr != null && this.undoArr.length > 0) {
                var undoEvent = {
                    type : 'DatasetUndoEvent'
                };
                //设置静默状态
                this.silent = true;
                var event = null;
                while (this.undoArr.length > 0) {
                    event = this.undoArr.pop();
                    if (event.type == 'RowSelectEvent') {
                        this.setRowUnSelected(event.currentRowIndex);
                    } else if (event.type == 'RowUnSelectEvent') {
                        this.setRowSelected(event.currentRowIndex);
                    } else if (event.type == 'RowInsertEvent') {
                        //alert("insert:" + event);
                        var indices = [];
                        for (var i = event.insertedRows.length - 1; i >= 0; i--) {
                            indices.push(event.insertedIndex + i);
                        }
                        this.deleteRows(indices);
                    } else if (event.type == 'DataChangeEvent') {
                        //alert("datachange:" + event);
                        this.setValueAt(event.cellRowIndex, event.cellColIndex, event.oldValue);
                    } else if (event.type == 'RowDeleteEvent') {
                        for (var i = 0; i < event.deletedIndices.length; i++) {
                            this.insertRow(event.deletedIndices[i], event.deletedRows[i]);
                        }
                    } else if (event.type == 'PageChangeEvent') {
                        //如果change之前没有key，表明之前是初始化状态的。需要设置成-1,表示恢复到初始化。仍使用null会导致仍然使用当前key
//                        var oldParentKey = event.oldParentKey;
//                        if (oldParentKey == null)
//                            oldParentKey = -1;
                        this.setCurrentPage(event.oldPageIndex);
                    }
                }
                //恢复静默状态
                this.silent = false;
                //派发Undo事件
                this.dispatchEvent(undoEvent);
                this.recordUndoSign = false;
            }
            this.clearState();
        },
        clearUndo : function() {
            this.undoArr.clear();
        },
        /**
         * 判断当前Key值对应的数据是否请求
         */
        isDataRequested : function(index) {
            if (index == -1)
                return true;
            if (this.pageDatas.length == 0)
                return false;
            var pageIndex = index;
            if (!index)
                pageIndex = this.getPageIndex();
            if (pageIndex == -1)
                return true;
            //第一行单独处理，针对第二页返回第一页数据为空的情况
            if (pageIndex == 0 && this.pagecount > 1) {
                return (this.getPageData(pageIndex) != null && this.getPageData(pageIndex).rows.length > 0);
            }
            return this.getPageData(pageIndex) != null;
        },
        /**
         * 获得指定页对应的指定行
         *
         * @param index 行索
         * @param pageIndex 页索可以为空
        */
        getRow : function(index) {
            if (this.pageDatas[this.pageindex] == null)
                return null;
            return this.pageDatas[this.pageindex].getRow(index);
        },
        /**
         * 删除行数据
         */
        deleteRow : function(index) {
            this.deleteRows([index]);
        },
        /**
         * 删除索引指定的多行。
         *
         */
        deleteRows : function(rowIndices) {
            var oldRows = this.getRows();
            var delAll = false;
            //排序
            if (rowIndices == -1) {
                rowIndices = [];
                delAll = true;
                var count = this.getRowCount();
                for (var i = 0; i < count; i++)
                    rowIndices.push(i);
            } else {
                rowIndices = rowIndices.sort($.dataset.defaultIntSort);
            }

            var tmpRows = [];
            var selIndices = this.getSelectedIndices();
            for (var i = rowIndices.length - 1; i >= 0; i--) {
                $.pageutils.removeFromArray(selIndices, rowIndices[i]);
                //删除行之后，大于此行的索引都应该减一
                if (selIndices != null) {
                    for (var j = 0; j < selIndices.length; j++) {
                        if (selIndices[j] > rowIndices[i])
                            selIndices[j]--;
                    }
                }
                var delRow = oldRows[rowIndices[i]];
                if (delRow == null) {
                    continue;
                }
                oldRows.splice(rowIndices[i], 1);
                //如果是新增行不放入删除行列表中(永久删除)
                if (delRow.state != $.datasetrow.CONST.STATE_NEW) {
                    if (this.deletedRows == null)
                        this.deletedRows = [];
                    this.deletedRows.push(delRow);
                }
                tmpRows.push(delRow);
            }

//            if (!this.silent) {
                var event = {
                    type : 'RowDeleteEvent',
                    deletedRows : tmpRows,
                    deletedIndices : rowIndices,
                    deleteAll : delAll
                };
                this.dispatchEvent(event);
                this.onAfterRowDelete(event);
//            }
        },
        /**
         * @private
         */
        getTotalCount : function() {
        },
        /**
         * 获取行大小
         * @private
         */
        getRowSize : function() {
            return this.fieldList.length;
        },
        /**
         * 获取页大小
         * @private
         */
        getPageSize : function() {
            return this.pageSize;
        },
        /**
         * 获得指定页行。
         * 注意，调用此函数时如果当前页数据没有解析过，将先进行解析。
         *
         * @param index 指定页码 return 页行0表示
         */
        getRowCount : function() {
            var rd = this.getCurrentPageData();
            if (rd == null)
                return 0;
            return rd.getRowCount();
        },
        getAllRowCount : function() {
            return this.recordcount;
        },
        /**
         * 设置row,col所对应元素的值 <b>注意，此方法和DatasetRow的setCellValue的区别在于，此方法会触发DataChangeEvent</b>
         *
         * @param rowIndex 行索引
         * @param colIndex 列索引
         * @param value 新值
         * @param withTrigger 是否出发参照列取值行为。此参数往往不需要设置，仅在确定需要设置参照触发情况下使用
         */
        setValueAt : function(rowIndex, colIndex, value) {
            var rd = this.getCurrentPageData();
            if (rd != null)
                rd.setValueAt(rowIndex, colIndex, value);
        },
        /**
         * 设置某一列元素的值，此方法触发DataColSingleChangeEvent
         *
         * @param {} rowIndices 行索引数组
         * @param {} colIndex    列索引
         * @param {} values        值数组
         */
        setValuesAt : function(rowIndices, colIndex, values) {
            var rd = this.getCurrentPageData();
            if (rd != null) {
                var event = {
                    type : 'DataColSingleChangeEvent',
                    cellColIndex : colIndex,
                    datasetId : this.id
                };
                for (var i = 0; i < rowIndices.length; i++) {
                    var row = this.getRow(rowIndices[i]);
                    var oldValue = row.setCellValue(colIndex, values[i]);
                    oldValue = oldValue ? oldValue : " ";
                    if (oldValue != values[i]) {
                        event.currentRows.push(row);
                        event.cellRowIndices.push(rowIndices[i]);
                        event.currentValues.push(values[i]);
                        event.oldValues.push(oldValue[i]);
                    }
                }
                if (!this.silent) {
                	this.element.triggerHandler('onbeforedatachange', null,event);
                    this.dispatchEvent(event);
                    //  校验cell数据
                    //			dataset.checkDatasetCell(value, colIndex, row);
                    // 对外暴露函数
                    this.onAfterDataChange(event);
                }
            }
        },
        /**
         * 设值
         */
        setValueAtByKey : function(rowIndex, key, value, withTrigger) {
            var colIndex = this.nameToIndex(key);
            this.setValueAt(rowIndex, colIndex, value, withTrigger);
        },
        /**
         * 获取row,col所对应元素的
         *
         * @param rowIndex 行索引
         * @param colIndex 列索引
         * @param value 新值
         */
        getValueAt : function(rowIndex, colIndex) {
            var row = this.getRow(rowIndex);
            return row ? row.getCellValue(colIndex) : null;
        },
        /**
         * 用新的Record更新指定
         *
         * @param rowIndex 当前页行
         * @param row 行数(Record)
         */
        setRow : function(rowIndex, row) {
        },
        /**
         * 增加一空行。普通的增行逻辑都应该调用此函数而非addRow(row)。因为此函数会进行默认值处理 新增的行中已经具有前台运算的唯一ID。
         *
         * @return 新增
         */
        addEmptyRow : function() {
            return this.insertEmptyRow(this.getRowCount());
        },
        /**
         * 插入一空行。普通的增行逻辑都应该调用此函数而非isnertRow(rowIndex, row)。因为此函数会进行默认值处理
         * 新增的行中已经具有前台运算的唯一ID
         *
         * @return 新增
         */
        insertEmptyRow : function(rowIndex) {
            if (rowIndex == null) {
                alert("rowIndex is null in function insertEmptyRow");
                return;
            }
            var row = this.getEmptyRow();
            this.insertRow(rowIndex, row);
            return row;
        },
        /**
         * 获取一个空行
         */
        getEmptyRow : function() {
            this.randomRowIndex++;
            var row = $.datasetrow.getObj({
                rowCount : this.fieldList.length
            });
            var rowCount = this.getRowCount(), lastRow = null;
            for (var i = 0, count = this.fieldList.length; i < count; i++) {
                var defValue = this.fieldList[i].dftValue;
                if (defValue != null)
                    defValue = decodeURIComponent(defValue);
                row.setCellValue(i, defValue);
                if (this.fieldList[i].isLock == true && rowCount > 0) {
                    if (lastRow == null)
                        lastRow = this.getRow(rowCount - 1);
                    row.setCellValue(i, lastRow.getCellValue(i));
                }
            }
            //设置需要触发事
            row.triggerRow = true;
            return row;
        },
        /**
         * 在当前显示数据页末尾添加一新行 <b>注意，因为插入行有用户控制，不对出入参数进行默认值处理</b>
         *
         * @param row 行数(Record)
         */
        addRow : function(row) {
            return this.insertRow(this.getRowCount(), row);
        },
        /**
         * 当前页指定位置插入一新行 <b>注意，因为插入行有用户控制，不对出入参数进行默认值处理</b>
         *
         * @param rowIndex 当前页行
         * @param row 行数(Record)
         */
        insertRow : function(rowIndex, row) {
            return this.insertRows(rowIndex, [row]);
        },
        /**
         * 在制定位置插入多行
         */
        insertRows : function(rowIndex, rows) {
            if (this.onBeforeRowInsert(rowIndex, rows) == false)
                return;
            if (rows != null) {
                var oldRows = this.getRows();
                if (oldRows == null) {
                    alert("the current data block is not initialized, key is:" + this.currentKey);
                    return;
                }
                for (var i = 0; i < rows.length; i++) {
                    rows[i].state = $.datasetrow.CONST.STATE_NEW;
                    oldRows.splice(rowIndex + i, 0, rows[i]);
                }
            }
            var event = {
                type : 'RowInsertEvent',
                insertedRows : rows,
                insertedIndex : rowIndex
            };
//            if (!this.silent) {
                this.dispatchEvent(event);
                this.onAfterRowInsert(event);
//            }

            if (rows != null) {
                for (var i = 0; i < rows.length; i++) {
                    var row = rows[i];
                    if (row.triggerRow == null)
                        continue;
                    row.triggerRow = null;
                    var rowSize = row.getSize();
                    for (var j = 0; j < rowSize; j++) {
                        var cellValue = row.getCellValue(j);
                        if (cellValue == null)
                            continue;
                        var triggerRef = false;
                        if (this.fieldList[j].sourceRefField != null) {
                            if (row.getCellValue(j) != null && row.getCellValue(j - 1) == null)
                                triggerRef = true;
                        }
                        //之所以先设置成空，再设置成当前值，是为了触发一次Datachange事件
                        row.setCellValue(j, null);
                        this.setValueAt(rowIndex + i, j, cellValue, triggerRef);
                    }
                }
            }
            return rowIndex;
        },
        /**
         * 删除行数据
         */
        removeRow : function(index) {
            this.removeRows([index]);
        },
        /**
         * 删除索引指定的多行
         */
        removeRows : function(rowIndices) {
            this.getCurrentPageData().removeRows(rowIndices);
        },
        /**
         * 获取元数据信息
         */
        getfieldList : function() {
            return this.fieldList;
        },
        /**
         * 获取主键字段
         */
        getPrimaryKeyField : function() {
            for (var i = 0; i < this.fieldList.length; i++) {
                if (this.fieldList[i].isPrimaryKey)
                    return this.fieldList[i];
            }
        },
        /**
         * 得到key所在的列的索引
         */
        nameToIndex : function(key) {
            for (var i = 0; i < this.fieldList.length; i++) {
                if (this.fieldList[i].key == key)
                    return i;
            }
            return -1;
        },
        /**
         * 根据名称获取字段索引
         */
        nameToIndices : function(keys) {
            var indicesArr = new Array(keys.length);
            for (var i = 0; i < keys.length; i++) {
                indicesArr[i] = this.nameToIndex(keys[i]);
                if (indicesArr[i] == -1) {
                    alert(keys[i] + "在Dataset中没有正确对");
                    return null;
                }
            }
            return indicesArr;
        },
        /**
         * 获取当前选中行一个key所对应的值
         *
         * @param key Dataset列键
         * @return 当前选中行所对应的列值。如果允许选中多行，则为第一个选中行的列没有选中行返回null
         */
        getValue : function(key) {
            var currRow = this.getSelectedRow();
            if (currRow == null)
                return null;
            return currRow.getCellValue(this.nameToIndex(key));
        },
        /**
         * 设置当前选中行key所对应的<b>注意</b>：此设置会出发DataChangeEvent
         *
         * @param key Dataset列键
         * @param value 待设置
         */
        setValue : function(key, value) {
            var rowIndex = this.getSelectedIndex();
            if (rowIndex == -1) {
                log("no selected row when call dataset.setValue, key is:" + key);
                return;
            }
            var colIndex = this.nameToIndex(key);
            this.setValueAt(rowIndex, colIndex, value);
        },
        /**
         * 校验cell数据，校验不合法派发DataCheckEvent
         */
        checkDatasetCell : function(value, colIndex, row) {
            var resultStr = $.dataset.checkDatasetCell(this, value, colIndex, row);
            if (resultStr != null && typeof resultStr == "string") {
                // 派发校验事件
                var event = {
                    type : 'DataCheckEvent',
                    cellColIndices : [colIndex],
                    currentRow : row,
                    rulesDescribe : [resultStr]
                };
                if (!this.silent)
                    this.dispatchEvent(event);
            }
        },
        /**
         * 校验行数据，校验不合法派发DataCheckEvent
         */
        checkDatasetRow : function(row) {
            var resultArray = $.dataset.checkDatasetRow(this, row);
            if (resultArray != null && resultArray.length > 0) {
                // 解析哪些列校验没有通过
                var cellColIndiceAry = [];
                var rulesAry = [];
                var temp = [];
                for (var i = 0, count = resultArray.length; i < count; i++) {
                    temp = resultArray[i].split(";");
                    cellColIndiceAry.push(temp[0]);
                    rulesAry.push(temp[1]);
                }

                var event = {
                    type : 'DataCheckEvent',
                    cellColIndices : cellColIndiceAry,
                    currentRow : row,
                    rulesDescribe : rulesAry
                };
                if (!this.silent)
                    this.dispatchEvent(event);
            }
        },
        /**
         * 根据制定列进行合计。只合计指定页。
         */
        totalSum : function(cols, index, precision, rowFilter) {
            var resultArr = new Array(cols.length);
            var rows = this.getRows(index);
            if (rows != null && rows.length > 0) {
                var indicesArr = this.nameToIndices(cols);
                for (var i = 0; i < rows.length; i++) {
                    for (var j = 0; j < indicesArr.length; j++) {
                        if (resultArr[j] == null)
                            resultArr[j] = 0;
                        var v = null;
                        if (rowFilter) {
                            if (rowFilter(rows[i])) {
                                v = rows[i].getCellValue(indicesArr[j]);
                            }
                        } else
                            v = rows[i].getCellValue(indicesArr[j]);
                        if (v != null && v != "") {
                            var dataType = this.fieldList[indicesArr[j]].dataType;
                            if (dataType == $.datatype.INTEGER || dataType == $.datatype.INT)
                                resultArr[j] += parseInt(v);
                            else
                                resultArr[j] += parseFloat(v);
                        }
                    }
                }
                if (precision != null) {
                    precision = parseInt(precision);
                    for (var i = 0; i < resultArr.length; i++) {
                        resultArr[i] = resultArr[i].toFixed(precision);
                    }
                }
            }
            return resultArr;
        },

        //************************************************* event *************************************************//
        onDataLoad : function(index, userObj) {
            pageUI.exParams.clear();
            if (this.reqParameterMap.size() > 0) {
                var keySet = this.reqParameterMap.keySet();
                var size = this.reqParameterMap.size();
                for (var i = 0; i < size; i++) {
                    var key = keySet[i];
                    var value = this.reqParameterMap.get(key);
                    pageUI.exParams.put(key, value);
                }
            }
            var dataLoadEvent = {
                "pageIndex" : index,
                "userObj" : userObj
            };
            this.element.triggerHandler('ondataload', dataLoadEvent);
        },
        /**
         * 行选中之前通知事件
         *
         * @param 待选中行索引 return boolean值，false表示阻止进一步处理
         *
         * @private
         *
         */
        onBeforeRowSelect : function(index) {
            var dsIndexEvent = {
                "index" : index
            };
            this.element.triggerHandler('onbeforerowselect', dsIndexEvent);
            return true;
        },
        /**
         * 行选中之后通知事件，传入行选中事件
         *
         * @private
         */
        onAfterRowSelect : function(rowSelectEvent) {
            this.element.triggerHandler('onafterrowselect', rowSelectEvent);
            return true;
        },
        /**
         * 行选中之后通知事件，传入行选中事件
         *
         * @private
         */
        onFocusChange : function(focusChangeEvent) {
            this.element.triggerHandler('onfocuschange', focusChangeEvent);
            return true;
        },
        /**
         * 行反选中之后通知事件，传入行反选中事件
         *
         * @private
         */
        onAfterRowUnSelect : function(rowUnSelectEvent) {
            this.element.triggerHandler('onafterrowunselect', rowUnSelectEvent);
        },
        /**
         * 行插入之前通知事件
         *
         * @param index 插入位置
         * @param rows 待插入行 return boolean值，false表示阻止进一步处理
         *
         * @private
         */
        onBeforeRowInsert : function(index, rows) {
            var dsBeforeRowInsertEvent = {
                "index" : index,
                "rows" : rows
            };
            this.element.triggerHandler('onbeforerowinsert', dsBeforeRowInsertEvent);
            return true;
        },
        /**
         * 行选中之后通知事件，传入行选中事件
         *
         * @private
         */
        onAfterRowInsert : function(rowInsertEvent) {
            this.element.triggerHandler('onafterrowinsert', rowInsertEvent);
        },
        /**
         * 数据修改事件
         *
         * @param rowIndex 行坐标
         * @param colIndex 列坐标
         * @param newValue 新值 return boolean 值，false表示阻止进一步处理
         * @private
         */
        onBeforeDataChange : function(rowIndex, colIndex, newValue, dataset) {
            var dsBeforeDataChangeEvent = {
                "rowIndex" : rowIndex,
                "colIndex" : colIndex,
                "newValue" : newValue,
                "dataset" : dataset
            };
            this.element.triggerHandler('onbeforedatachange', dsBeforeDataChangeEvent);
            return true;
        },
        /**
         * 数据修改之后通知事件.
         *
         * @param event 数据修改事件
         * @param pageIndex 如果pageIndex与当前页不一致，则表示跨页更新，逻辑中要特别处理。在涉及到分页的Dataset，并且允许跨页操作的
         *                  界面中特别要注意这一项逻辑.此参数可能为空，如果为空，则表示是当前页更新
         * @private
         */
        onAfterDataChange : function(dataChangeEvent, pageIndex) {
            dataChangeEvent.pageIndex = pageIndex;
            this.element.triggerHandler('onafterdatachange', dataChangeEvent);
        },
        /**
         *
         * @private
         */
        beforeCallEvent : function(eventName, eventObj) {
            if (eventName == "onAfterDataChange") {
                if (this.afterDataChangeAcceptFields == null || this.afterDataChangeAcceptFields == "")
                    return true;
                var fields = this.afterDataChangeAcceptFields;
                var colIndex = eventObj.cellColIndex;
                var currentFieldName = this.fieldList[colIndex].key;
                for (var i = 0, n = fields.length; i < n; i++) {
                    if (fields[i] == currentFieldName)
                        return true;
                }
                return false;
            } else if (eventName == "onBeforeDataChange") {
                if (this.beforeDataChangeAcceptFields == null || this.afterDataChangeAcceptField == "")
                    return true;
                var fields = this.beforeDataChangeAcceptFields;
                var colIndex = eventObj.cellColIndex;
                var currentFieldName = this.fieldList[colIndex].key;
                for (var i = 0, n = fields.length; i < n; i++) {
                    if (fields[i] == currentFieldName)
                        return true;
                }
                return false;
            }
            return true;
        },
        /**
         * 行删除之前通知事件
         *
         * @param indices 待删除行索引。已经从小到大排序 return boolean 值，false表示阻止进一步处理
         * @private
         */
        onBeforeRowDelete : function(indices) {
            var dsBeforeRowDeleteEvent = {
                "indices" : indices
            };
            this.element.triggerHandler('onbeforerowdelete', dsBeforeRowDeleteEvent);
            return true;
        },
        /**
         * 行删除之后通知事件，传入行删除事件
         *
         * @private
         */
        onAfterRowDelete : function(rowDeleteEvent) {
            this.element.triggerHandler('onafterrowdelete', rowDeleteEvent);
        },
        /**
         * 恢复之后通知事件，传入Undo事件
         *
         */
        onAfterUndo : function(datasetUndoEvent) {
            this.element.triggerHandler('onafterundo', datasetUndoEvent);
        },
        /**
         * 显示页修改之前通知事件, 传入页面修改事件 return boolean 值，false表示阻止进一步处理
         *
         * @private
         */
        onBeforePageChange : function(key, index) {
            var dsBeforePageChangeEvent = {
                "key" : key,
                "index" : index
            };
            this.element.triggerHandler('onbeforepagechange', dsBeforePageChangeEvent);
            return true;
        },
        /**
         * 显示页修改之后通知事件，传入页面修改事件
         * @private
         */
        onAfterPageChange : function(pageChangeEvent) {
            this.element.triggerHandler('onafterpagechange', pageChangeEvent);
        },
        /**
         * 获取所有行数
         */
        getrecordcount : function() {
            return this.recordcount;
        },
        /**
         * 获取当前pageData
         */
        getCurrentPageData : function() {
            return this.getPageData(this.pageindex);
        },
        /**
         * 获取pageData
         */
        getPageData : function(pageindex) {
            return this.pageDatas[pageindex];
        },
        /**
         * 获取所有pageData
         */
        getPageDatas : function() {
            return this.pageDatas;
        },       
        /**
         * 获取当前选中页中的选中行
         */
        getSelectedRows : function() {
        	if(this.getCurrentPageData())
            	return this.getCurrentPageData().getSelectedRows();
            return null;
        },
        /**
         * 获取当前选中页中的选中行
         */
        getSelectedRow : function() {
            var rows = this.getSelectedRows();
            return rows ? rows[0] : null;
        },
        /**
         * 获取所有选中行
         */
        getAllSelectedRows : function() {
            var rowArr = [];
            if(this.pageDatas.length !=0) {
            	var selRows = this.getCurrentPageData().getSelectedRows();
	            if (selRows != null) {
	                for (var j = 0; j < selRows.length; j++)
	                    rowArr.push(selRows[j]);
	            }
            }
            return rowArr;
        },
        /**
         * 根据ID获取行索引
         */
        getRowIndexById : function(id) {
            var rows = this.getAllRows();
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].rowId == id)
                    return i;
            }
            return -1;
        },
        /**
         * 获取指定页的数据
         */
        getRows : function() {
            if (this.pageDatas[this.pageindex] == null)
                return null;
            return this.pageDatas[this.pageindex].getRows();
        },
        /**
         * 获取所有行
         */
        getAllRows : function() {
            var rowArr = [];
            if (this.pageDatas.length == 0)
                return rowArr;
            for (var i = 0; i < this.pageDatas.length; i++) {
                var rows = this.pageDatas[i].getRows();
                if (rows != null) {
                    for (var j = 0; j < rows.length; j++) {
                        rowArr.push(rows[j]);
                    }
                }
            }
            return rowArr;
        },
        /**
         * 获取指定范围内的行
         * @param startIndex 开始索引
         * @param count 查询数量
         */
        getRowsByScale : function(startIndex, count) {
            if (this.pageDatas[this.pageindex] == null)
                return null;
            return this.pageDatas[this.pageindex].getRowsByScale();
        },
        /**
         * 获取指定页的数据
         *
         * @param pageIndex,如果1,则表示获取当前所有行
         */
        getRowCount : function() {
            if (this.pageDatas[this.pageindex] == null)
                return null;
            return this.pageDatas[this.pageindex].getRowCount();
        }
        
    });

    $.dataset.lazyLoadDs = function() {
        if (!ds.viewpart.initialized) {
            setTimeout("$.dataset.doLazyLoadDs('" + ds.viewpart.id + "', '" + ds.id + "')", 20);
            return;
        }
    };
    $.dataset.doLazyLoadDs = function(widgetId, dsId) {
        var ds = pageUI.getViewPart(widgetId).getDataset(dsId);
        if (!ds.viewpart.initialized) {
            setTimeout("$.dataset.doLazyLoadDs('" + widgetId + "', '" + dsId + "')", 20);
            return;
        }
        if (!ds.dsLoaded) {
            ds.dsLoaded = true;
            ds.setCurrentPage(0);
            ds.reqParameterMap.remove("openBillId");
        }
    };
    /*
     * 生成用于前后台交互用的record id 规则 0/1 + dsid + random 前台产生的第一位数字是1 后台产生的第一位数字是0
     * @private
     */
    $.dataset.getRandomRowId = function() {
        var id = "1" + (new Date().getTime());
        var random = Math.random();
        var str = (random * 10000000000).toString();
        id += str.substring(0, 10);
        return id;
    };

    $.dataset.CONST = {
        UPDATE_SAVE : 0,
        ALL_SAVE : 1,
        ALL_NOT_SAVE : 2,
        //MASTER_KEY : 'MASTER_KEY',

        PREFIX_SYSTEM_QUERY_PARAM : "$$",
        QUERY_PARAM_KEYS : "query_param_keys",
        QUERY_PARAM_VALUES : "query_param_values",
        QUERY_PARAM_KEYVALUE : "query_param_keyvalue",
        QUERY_KEYVALUE : "query_keyvalue",
        QUERY_PARAM_PAGEINDEX : "query_param_pageindex",
        QUERY_RECURSIVEKEYFIELD : "recursiveKeyField"
    };
    $.extend($.dataset.CONST, {
        /* 此参数存储自拼装查询条件 */
        NORMAL_QUERY_CONDITiON : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "normal_query_condition",
        /*
         * 在前台向后台发送查询请求时，通过该参数确定是否来自查询模版，如果来自查询模版则添加该参数，其值无意义。 如果不来自查询模版，可以不用设置。
         */
        FROM_QUERY_TEMPLATE : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "from_query_model",
        /* 在前台dataset中，通过此key值存储通过查询模版获取的数据信息 */
        QUERY_TEMPLATE_KEYVALUES : "$%%$query_template_keyvalues",
        /* 在查询模版中通过此参数传递被查询的datasetId,放到查询模版左边查询树的response-parameter中。 */
        QUERY_TEMPLATE_TARGET_DSID : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_dsid",
        /* 通过此参数名称，在查询模版的handler处理时，将被查询dataset所在的pageId放到查询模版左边树形结构的response-parameter中。 */
        QUERY_TEMPLATE_TARGET_PAGEID : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "query_template_target_pageId",
        /* 对于根一级的查询模版树记录的parentId设置为该默认值。 */
        QUERY_TEMPLATE_DEFAULT_ROOTPARENTID : "queryTemplateSpecialParentId",
        /* 通过此参数存储通过查询模版生成的sql语句条件 */
        QUERY_TEMPLATE_CONDITION : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "query_template_condition",
        QUERY_TEMPLATE_LOGICCONDITION : $.dataset.CONST.PREFIX_SYSTEM_QUERY_PARAM + "query_template_logiccondition",
        /* 用此参数表明当前发送的装载数据请求是否是装载的其他页面而不是当前处理页面。查询模版中会用到。 */
        IS_LOAD_OTHER_PAGE : "isLoadOtherPage",
        /* 与上一参数配合使用，表明请求装载那个页面的数据 */
        OTHER_PAGE_ID : "otherPageId",
        // 待审核的单据id
        APPROVE_BILLID : "approveid",
        // 单据的审批类型 0:自己的单据，1:审批的单据
        APPROVE_TYPE : "approve_type"
    });

    $.dataset.getObj = function(opts) {
        return new $.dataset(opts);
    };

    /**
     * 通用对Dataset进行序列化的操作,方便对于非保存操作而需要对Dataset进行
     * 前后台传递时对Dataset的序列化.
     * @param dataset
     * @param rows 若rows不为null则仅序列化rows中的,否则根据ds的ajaxSaveType序列化ds的更新行或者所有行
     * @private
     */
    $.dataset.searializeDataset = function(dataset, dsType) {
        var datasetJson = {
            "id" : dataset.id,
            "widgetId" : dataset.viewpart.id,
            "editable" : dataset.editable,
            "randomRowIndex" : dataset.randomRowIndex
        };

        if (dataset.reqParameterMap && dataset.reqParameterMap.keySet().length > 0) {
            var reqParaJson = {};
            var keyset = dataset.reqParameterMap.keySet();
            for (var j = 0; j < keyset.length; j++) {
                reqParaJson[keyset[j]] = dataset.reqParameterMap.get(keyset[j]);
            }
            datasetJson["reqparas"] = reqParaJson;
        }
        if(dataset.pageDatas) {
	        var pageSetsObjJson = {
	            "pagecount" : dataset.pagecount,
	            "pageSize" : dataset.pageSize,
	            "recordcount" : dataset.recordcount,
	            "pageindex" : dataset.pageindex
	        };
	        var pageSetsJson = [];
	        for (var i = 0; i < dataset.pageDatas.length; i++) {
	            var onePageJson = {};
	            var pageData = dataset.pageDatas[i];
	            if (pageData == null) {
	                continue;
	            }
	            onePageJson["pageindex"] = pageData.pageindex;
	            onePageJson["selected"] = pageData.getSelectedIndices();
	            onePageJson["focusRowIndex"] = dataset.getFocusRowIndex();
	            onePageJson["onePage"] = $.dataset.serializeRows(pageData, dsType, dataset);
	            pageSetsJson.push(onePageJson);
	        }
	        if (pageSetsJson.length > 0) {
	            pageSetsObjJson["pageDatas"] = pageSetsJson;
	        }
	        datasetJson["allPage"] = pageSetsObjJson;
        }
        return datasetJson;
    };

    /**
     * 序列化行
     * @param currentKey
     * @param pageData
     * @param dsType
     * @param dataset
     * @return
     * @private
     */
    $.dataset.serializeRows = function(pageData, dsType, dataset) {
        var pageDataJsonObj = null;
        if (dsType == null) {
            dsType = "ds_current_line";
        }
        var entryRows = [];
        //不能分开处理
        if (dsType == "ds_current_line") {
            pageDataJsonObj = {};
            var count = pageData.getRowCount();
            var selIndices = pageData.selectedIndices;
            var rowJson = null;
            for (var i = 0; i < count; i++) {
                var sel = false;
                if (selIndices) {
                    for (var j = 0; j < selIndices.length; j++) {
                        if (i == selIndices[j]) {
                            if (pageData.getRow(i)) {
                                rowJson = pageData.getRow(i).toJson();
                            }
                            rowJson.isEntry = true;
                            entryRows.push(rowJson);
                            sel = true;
                            break;
                        }
                    }
                }
                var focusRowIndex = dataset.getFocusRowIndex();
                if (focusRowIndex != -1 && (!sel) && (i == focusRowIndex)) {
                    rowJson = pageData.getRow(focusRowIndex).toJson();
                    rowJson.isEntry = true;
                    entryRows.push(rowJson);
                    sel = true;
                }
                if (!sel) {
                    if (!(pageData.getRow(i) instanceof $.emptydsrow)) {
                        rowJson = pageData.getRow(i).toJson();
                    } else {
                        rowJson = rowJson || {};
                    }

                    rowJson.isEntry = false;
                    entryRows.push(rowJson);
                }
                if (entryRows.length > 0) {
                    pageDataJsonObj["rows"] = entryRows;
                }
            }
            return pageDataJsonObj;
        }
        if (dsType == "ds_current_page") {
            if (dataset.pageindex == pageData.pageindex) {
                pageDataJsonObj = {};
                var count = pageData.getRowCount();
                for (var i = 0; i < count; i++) {
                	var _row = pageData.getRow(i).toJson();
                	_row.isEntry = true;
                    entryRows.push(_row);
                }
                if (entryRows.length > 0) {
                    pageDataJsonObj["rows"] = entryRows;
                }
            }
            return pageDataJsonObj;
        }
        if (dsType == "ds_all_line") {
            pageDataJsonObj = {};
            var allRows = pageData.getRows();
            var count = allRows.length;
            for (var i = 0; i < count; i++) {
            	var _row = allRows[i].toJson();
            	_row.isEntry = true;
                entryRows.push(_row);
            }
            if (entryRows.length > 0) {
                pageDataJsonObj["rows"] = entryRows;
            }
        }
        return pageDataJsonObj;
    };

    /*****************************************************************
     * @fileoverview 用于完成对Dataset的校验,针对元数据而来的校验规则
     * @author lkp
     * @version 1.0
     * @date 20070718
     * @modified by dengjt 进行优化，去掉大量map的生成与array的引用，可能会在多次保存后
     * 导致内存占用剧增
     * @modified by luoyf 添加字段非空校验
     ******************************************************************/

    //DatasetRuleChecker = new Object;
    //NOT_NULL_RULE = "not_null_rule";
    //DatasetRuleChecker.keyMap = $.hashmap.getObj();
    //DatasetRuleChecker.keyMap.put(DatasetRuleChecker.NOT_NULL_RULE, ml_nextfieldscannotnull);
    /**
     * 对dataset的某行数据进行校验
     *
     * @param dataset
     * @param row
     * @return 如果违反规则返回一个字符串数组，字符串是行号和出错信息拼接成的，中间以分号分隔，例如：3;不能大于5000 否则 返回NULL
     */
    $.dataset.checkDatasetRow = function(dataset, row) {
        return $.dataset.$rowRuleCheck(dataset, row);

    };

    /**
     * 对Dataset 单元格的增加和修改时进行校验
     *
     * @param dataset
     * @param value
     * @param cellIndex
     * @param row
     * @return 如果违反规则返回一个出错信息字符串 否则 返回NULL
     */
    $.dataset.checkDatasetCell = function(dataset, value, cellIndex, row) {
        if ( typeof generalLogicCheck != "undefined")
            generalLogicCheck();
        // 前台的业务校验
        if ( typeof specialCellLogicCheck != "undefined") {
            var result = specialCellLogicCheck(dataset, value, cellIndex, row);
            if (result != null && typeof result == "string")
                return result;
            else
                return null;
        }
        return $.dataset.$cellRuleCheck(dataset, value, cellIndex);
    };

    /**
     * 对给定的dataset进行增加和修改数据的校验。
     * @param dataset,isCheckDsAllRows
     * @return 返回Map，如果没有违反规则的纪录，则内容为空。
     *         如果存在，则key:对应的错误行,value:是由上方法返回的一个Map。
     *
     */
    $.dataset.checkDataset = function(dataset) {
        return $.dataset.$checkDsAllRows(dataset);
    };

    /**
     * 校验Dataset所有行
     * @param ds
     * @return
     * @private
     */
    $.dataset.$checkDsAllRows = function(ds) {
        var allRows = ds.getRows();
        var resultArr = null;
        for (var i = 0, count = allRows.length; i < count; i++) {
            var arr = $.dataset.checkDatasetRow(ds, allRows[i]);
            var rowIndex = ds.getRowIndex(allRows[i], null, null);
            if (arr != null) {
                if (resultArr == null)
                    resultArr = [];
                for (var j = 0; j < arr.length; j++) {
                    if (resultArr.indexOf(arr[j]) == -1)
                        resultArr.push(rowIndex + ";" + arr[j]);
                }
            }
        }
        return resultArr;
    };

    /**
     * 检验cell
     * @private
     */
    $.dataset.$cellRuleCheck = function(dataset, value, cellIndex) {
        var meta = dataset.fieldList[cellIndex];
        // not null check
        var isRequired = meta.isRequired;
        if (isRequired != null && isRequired == true) {
            if ((value == null || value == "") && value != 0) {//js中 0 == "" 结果为true
                return trans('ml_thisfieldcannotnull');
            }
        }
        var result;
        // 格式校验
        var fm = meta.formater;
        if (fm != null) {
            if (value != null)
                result = $.dataset.$$formaterCherk(fm, value);
        }
        if (result != null)
            return result;
        // 范围校验
        var maxValue = meta.maxValue;
        var minValue = meta.minValue;
        if (maxValue != null || minValue != null) {
            if (value != null)
                result = $.dataset.$$scopeCherk(value, meta.dataType, maxValue, minValue);
        }
        if (result != null)
            return result;
        return "";
    };

    /**
     * specialCellLogicCheck返回值规则:
     * 1、返回true表明没有校验错误,进行进一步默认校验。
     * 2、返回false或者不返回均不做默认检验
     * 3、返回字符串则表明出错,打印该错误信息
     * @private
     */
    $.dataset.$rowRuleCheck = function(dataset, row) {
        var meta = dataset.fieldList;
        var errorArray = null;
        var hasSelfChecker = ( typeof specialCellLogicCheck != "undefined");
        for (var i = 0, count = meta.length; i < count; i++) {
            var value = row.getCellValue(i);
            var errMsg = true;
            if (hasSelfChecker) {
                errMsg = specialCellLogicCheck(dataset, value, i, row);
            }
            //返回true，表明没有错误，并需要再进行默认检查
            if (errMsg == true) {
                if ((row.state == $.datasetrow.CONST.STATE_NEW) || (row.state == $.datasetrow.CONST.STATE_UPD)) {
                    if (meta[i].isForeignKey || meta[i].isPrimaryKey)
                        continue;
                }
                //not null check
                var isRequired = meta[i].isRequired;
                if (isRequired != null && isRequired == true) {
                    if ((value == null || value == "") && value != 0)//js中 0 == "" 结果为true
                        errMsg = trans('ml_nextfieldscannotnull');
                }
                //formater check
                var fm = meta[i].formater;
                if (fm != null) {
                    if (value != null && value != "")
                        errMsg = $.dataset.$$formaterCherk(fm, value);
                }
            }
            if (errMsg != null && typeof errMsg == "string") {
                if (errorArray == null)
                    errorArray = new Array;
                errorArray.push(i + ";" + errMsg);
            }
        }
        return errorArray;
    };

    /**
     * formater 校验
     * @param fm
     * @param value
     * @return
     * @private
     */
    $.dataset.$$formaterCherk = function(fm, value) {
        var returnstr = null;
        switch (fm) {
        case "email":
            if (!$.argumentutils.isEmail(value))
                returnstr = trans("ml_rule_email");
            break;
        case "number":
            if (!$.argumentutils.isNumber(value))
                returnstr = trans("ml_rule_number");
            break;
        case "chn":
            if (!$.argumentutils.isChinese(value))
                returnstr = trans("ml_rule_chn");
            break;
        case "variable":
            if (!$.argumentutils.isValidIdentifier(value))
                returnstr = trans("ml_rule_variable");
            break;
        case "phone":
            if (!$.argumentutils.isPhone(value))
                returnstr = trans("ml_rule_phone");
            break;
        default:
            if (fm != null && fm != '') {
                var f = eval(decodeURIComponent(fm));
                if (!f.test(value))
                    returnstr = trans("ml_rule_other");
            }
            break;
        }
        return returnstr;
    };

    /**
     * 范围 校验
     * @param value
     * @param dataType
     * @param maxValue
     * @param minValue
     * @return
     * @private
     */
    $.dataset.$$scopeCherk = function(value, dataType, maxValue, minValue) {
        if (dataType) {
            if (dataType == $.datatype.STRING || dataType == $.datatype.CHAR || dataType == $.datatype.CHARACTER) {// 字符串
                if (maxValue != null && value.length > maxValue)
                    return "长度超出范围，最大值为:" + maxValue;
                if (minValue != null && value.length < minValue)
                    return "长度超出范围，最小值为:" + minValue;
            } else if (dataType == $.datatype.INTEGER || dataType == $.datatype.INT || dataType == $.datatype.DOUBLE || dataType == $.datatype.dOUBLE || dataType == $.datatype.FDOUBLE || dataType == $.datatype.FLOAT || dataType == $.datatype.fLOAT || dataType == $.datatype.BOOLEAN || dataType == $.datatype.bOOLEAN || dataType == $.datatype.FBOOLEAN || dataType == $.datatype.BIGDECIMAL || dataType == $.datatype.LONG || dataType == $.datatype.lONG || dataType == $.datatype.FNUMBERFORMAT || dataType == $.datatype.Decimal || dataType == $.datatype.FDATETIME || dataType == $.datatype.FDATE || dataType == $.datatype.FTIME) {// 数字
                if (maxValue != null && value > maxValue)
                    return "超出范围，最大值为:" + maxValue;
                if (minValue != null && value < minValue)
                    return "超出范围，最小值为:" + minValue;
            }
        }
        return null;
    };

    /**
     * Dataset行数据
     * @class Dataset行数据，可来自后台生成的dom元素，也可手工创建出来 <b>手工创建出来的Row已经带有当前Dataset中的唯一ID。</b>
     * @param ele 对应“数据岛”中的一个element。行数据将从此element中解析获得。如果为空，则表示新建空行。
     */
    $.datasetrow = function(opts) {
        //ele为空一般表示手工NEW出来的行
        if (opts.oneRowJson != null) {
            this.rowId = opts.oneRowJson["id"];
            this.contentJson = opts.oneRowJson["content"];
            this.state = opts.oneRowJson["state"];
            this.isEdit = opts.oneRowJson["isEdit"];
            this.isAllowMouseoverChange = opts.oneRowJson["isAllowMouseoverChange"];
            this.changedArr = opts.oneRowJson["ch"];
            this.dataset = opts.dataset;
            this.initialize();
        } else {
            this.rowId = $.dataset.getRandomRowId();
            if (opts.rowCount != null)
                this.dataArr = new Array(opts.rowCount);
            else
                this.dataArr = [];
            this.state = $.datasetrow.CONST.STATE_NEW;
        }
    };

    $.extend($.datasetrow.prototype, {
        /**
         * 获得此行指定位置单元
         *
         * @param index
         * @return 单元
         */
        getCellValue : function(index) {
            return this.dataArr[index];
        },
        /**
         * 获取状态
         */
        getState : function() {
            return this.state;
        },
        /**
         * 设置状态
         */
        setState : function(state) {
            this.state = state;
        },
        /**
         * 初始化
         */
        initialize : function() {
            this.dataArr = this.contentJson;
            var fieldList = this.dataset.fieldList;
            var length = this.dataArr.length;
            // 解码字符串值。此处是关键性能点，减少方法调用
            for (var i = 0; i < length; i++) {
                //还原null
                if (this.dataArr[i] == null || this.dataArr[i].trim() == "" || this.dataArr[i] == $.EventContextConstant.NULL) {
                    this.dataArr[i] = null;
                    continue;
                }
                this.dataArr[i] = decodeURIComponent(this.dataArr[i]);
                if (fieldList[i]) {
                    if (fieldList[i].dataType == $.datatype.FDATETIME || fieldList[i].dataType == $.datatype.FTIME) {
                        //判断this.dataArr[i]中的日期时间是否为lang类型
                        if (parseInt(this.dataArr[i]) != this.dataArr[i])
                            continue;
                        var date = new Date();
                        date.setTime(this.dataArr[i]);
                        this.dataArr[i] = this.dateTimeFormat(date);
                    } else if (fieldList[i].dataType == $.datatype.FDATE || fieldList[i].dataType == $.datatype.FDATEBEGIN || fieldList[i].dataType == $.datatype.FDATEEND) {
                        //判断this.dataArr[i]中的日期时间是否为lang类型
                        if (parseInt(this.dataArr[i]) != this.dataArr[i])
                            continue;
                        var date = new Date();
                        date.setTime(this.dataArr[i]);
                        this.dataArr[i] = this.dateTimeFormat(date);
                    } else if (fieldList[i].dataType == $.datatype.FLITERALDATE) {
                        if (parseInt(this.dataArr[i]) != this.dataArr[i])
                            continue;
                        var date = new Date();
                        date.setTime(this.dataArr[i]);
                        this.dataArr[i] = this.dateFormat(date);
                    } else if(fieldList[i].dataType == $.datatype.FBOOLEAN){
                    	this.dataArr[i] = (this.dataArr[i]=="true" ? "Y" :(this.dataArr[i]=="Y"?"Y":"N"));
                    }
                    //替换内容中的 &lt; 与 &amp; 符号 为 < 与 &
                    else if ( typeof this.dataArr[i] == "string") {
                        this.dataArr[i] = this.dataArr[i].replace(/\&lt;/g, "<");
                        this.dataArr[i] = this.dataArr[i].replace(/\&amp;/g, "&");
                    }
                }
            }
            switch (this.state) {
            case "nrm" :
                this.state = $.datasetrow.CONST.STATE_NRM;
                break;
            case "upd" :
                this.state = $.datasetrow.CONST.STATE_UPD;
                break;
            case "add" :
                this.state = $.datasetrow.CONST.STATE_NEW;
                break;
            case "del" :
                this.state = $.datasetrow.CONST.STATE_DEL;
                break;
            case "fdel" :
                this.state = $.datasetrow.CONST.STATE_FALSE_DEL;
                break;
            }
        },
        /**
         * 把date类型格式化成yyyy-MM-dd HH:Mi:SS
         * @param {date} datetime
         * @return {String}   YYYY-MM-DD HH:Mi:SS
         * private
         */
        dateTimeFormat : function(date) {
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if (parseInt(month) < 10)
                month = "0" + month;
            var day = date.getDate();
            if (parseInt(day) < 10)
                day = "0" + day;
            var hours = date.getHours();
            if (parseInt(hours) < 10)
                hours = "0" + hours;
            var minutes = date.getMinutes();
            if (parseInt(minutes) < 10)
                minutes = "0" + minutes;
            var seconds = date.getSeconds();
            if (parseInt(seconds) < 10)
                seconds = "0" + seconds;
            var formatString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
            return formatString;
        },
        /**
         * 把date类型格式化成yyyy-MM-dd
         * @param {date} date
         * @return {String}   YYYY-MM-DD
         * private
         */
        dateFormat : function(date) {
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if (parseInt(month) < 10)
                month = "0" + month;
            var day = date.getDate();
            if (parseInt(day) < 10)
                day = "0" + day;
            var formatString = year + "-" + month + "-" + day;
            return formatString;
        },
        /**
         * 设置单元值 <b>注意：设置此单元值不会出发更新事件。可通过dataset.setValueAt进行修改且出发事件</b>
         *
         * @param index{int} 指定列索引
         * @param value 新值
         */
        setCellValue : function(index, value) {
            var oldValue = this.dataArr[index];
            // 确保为字符串
            if (value != null)
                this.dataArr[index] = value + "";
            else
                this.dataArr[index] = null;
            if (this.state != $.datasetrow.CONST.STATE_NEW)
                this.state = $.datasetrow.CONST.STATE_UPD;
            return oldValue;
        },
        /**
         * 获得行cell大小
         *
         * @return {int} cell
         */
        getSize : function() {
            return this.dataArr.length;
        },
        /**
         * 行复制方 <b>注意，由此clone的新行，将具有新的rowId。行状态为New。即，仅仅是数据保持和源行相/b>
         *
         * @return 克隆后的
         *
         * @private
         */
        clone : function() {
            var row = $.datasetrow.getObj();
            for (var i = 0; i < this.dataArr.length; i++)
                row.setCellValue(i, this.dataArr[i]);
            return row;
        },
        toJson : function() {
            var stateVal = null;
            switch (this.state) {
            case $.datasetrow.CONST.STATE_NEW:
                stateVal = "add";
                break;
            case  $.datasetrow.CONST.STATE_DEL:
                stateVal = "del";
                break;
            case  $.datasetrow.CONST.STATE_DEL:
                stateVal = "del";
                break;
            case  $.datasetrow.CONST.STATE_UPD:
                stateVal = "upd";
                break;
            case $.datasetrow.CONST.STATE_FALSE_DEL:
                stateVal = "fdel";
                break;
            default :
                stateVal = "nrm";
                break;

            }
            var rowJsonObj = {};
            rowJsonObj["id"] = this.rowId;
            rowJsonObj["state"] = stateVal;
            rowJsonObj["content"] = this.contentToJson();
            return rowJsonObj;
        },
        contentToJson : function() {
            var contexntJsons = [];
            var length = this.dataArr.length;
            for (var i = 0; i < length; i++) {
                var temp;
                if (this.dataArr[i] != null) {
                    temp = this.dataArr[i];
                    //dingrf 处理日期时间
                    if (this.dataset != null && this.dataset.fieldList[i] != null && (this.dataset.fieldList[i].dataType == $.datatype.FDATETIME || this.dataset.fieldList[i].dataType == $.datatype.FDATE || this.dataset.fieldList[i].dataType == $.datatype.FDATEBEGIN || this.dataset.fieldList[i].dataType == $.datatype.FDATEEND || this.dataset.fieldList[i].dataType == $.datatype.FTIME)) {
                        temp = this.dateToUTCString(temp);
                    } else if (this.dataset != null && this.dataset.fieldList[i] != null && this.dataset.fieldList[i].dataType == $.datatype.FLITERALDATE) {
                        if (temp.indexOf("-") > -1)
                            temp = temp.replace(/\-/g, "/");
                        var date = new Date(temp);
                        temp = this.dateFormat(date);
                        temp = this.dateToUTCString(temp);
                    }
                    //替换内容中的 < 与 & 符号，这两个符号在xml解析时会出错
                    else if ( typeof temp == "string") {
                        temp = temp.replace(/\&/g, "&amp;");
                        temp = temp.replace(/\</g, "&lt;");
                    }
                    contexntJsons.push(encodeURIComponent(temp));
                } else {
                    temp = $.EventContextConstant.NULL;
                    contexntJsons.push(temp);
                }
            }
            return contexntJsons;
        },
        /**
         * 取得date对应的协调世界时
         * @param {String} date
         * private
         */
        dateToUTCString : function(date) {
            if (date.indexOf("-") > -1)
                date = date.replace(/\-/g, "/");
            var utcString = Date.parse(date);
            if (isNaN(utcString))
                return "";
            return utcString;
        },
        /**
         * 重写toString，便于调试。使用时可直接alert(row)来完成调用。
         *
         * @return {String} 行的完整信息
         * @private
         */
        toString : function() {
            return "RowId is:" + this.rowId + " and state is:" + this.state + " and data is:" + this.dataArr;
        }
    });

    $.datasetrow.CONST = {
        STATE_NRM : 0,
        STATE_UPD : 1,
        STATE_NEW : 2,
        STATE_DEL : 3,
        STATE_FALSE_DEL : 4,
        CHANGEED : "ch"
    };

    $.datasetrow.getObj = function(opts) {
        return new $.datasetrow(opts);
    };
 
    
    /***********************************************************************************
     * pageData构造函数   2370------2900
     ************************************************************************************/
    $.pageData = function(opts) {
        if (opts.onePageJson != null)
            this.pageindex = parseInt(opts.onePageJson["pageindex"]);
        else
            this.pageindex = 0;
        this.rows = [];
        this.deleteRows = null;
        this.selectedIndices = null;
        this.dataset = opts.dataset;
    };

    $.pageData.getObj = function(opts) {
        return new $.pageData(opts);
    };

    $.extend($.pageData.prototype, {
        /**
         * 设置数据
         */
        setData : function(onePageObj, isInit) {
            var isCurrent = (this.dataset.pageindex == this.pageindex);
            var selectedIndexs = onePageObj["selected"];
            selectedIndexs = selectedIndexs || [];
            this.selectedIndices = this.selectedIndices || [];
            
            var onePageRows = $.dataset.maskNodesToRecords(onePageObj["onePage"], this.dataset);
            
            //处理行编辑状态
            var _tmpRows = onePageRows;
            if(_tmpRows) {
            	var _tmpOldRows = this.getRows();
            	for(var i =0;i<_tmpOldRows.length;i++) {
            		for(var j=0;j<_tmpRows.length;j++) {
            			if(_tmpOldRows[i].rowId===_tmpRows[j].rowId && _tmpRows[j].isEdit!=null &&  _tmpRows[j].isEdit!=_tmpOldRows[i].isEdit) {
            				_tmpOldRows[i].isEdit = _tmpRows[j].isEdit;
            			}
            			//鼠标悬浮行变色
            			if(_tmpOldRows[i].isAllowMouseoverChange!=null){
            				_tmpOldRows[i].isAllowMouseoverChange = _tmpRows[j].isAllowMouseoverChange;
            			}
            		}
            	}
            }
            
            var selChanged = false;
            if (isCurrent) {
                var flag = (selectedIndexs.length != this.selectedIndices.length);
                if (flag) {
                    selChanged = true;
                } else {
                    selChanged = false;
                }
            }
            if (isCurrent && !selChanged) {
                var oldRows = this.getRows();
                for (var i = 0; i < selectedIndexs.length; i++) {
                    var newIndex = selectedIndexs[i];
                    if (onePageRows[newIndex] == null || onePageRows[newIndex].rowId == null) {
                        selChanged = true;
                        break;
                    }
                    var oldIndex = this.selectedIndices[i];
                    if (oldRows[oldIndex] == null || oldRows[oldIndex].rowId == null) {
                        selChanged = true;
                        break;
                    }
                    if (oldRows[oldIndex].rowId == onePageRows[newIndex].rowId) {
                        continue;
                    }
                    selChanged = true;
                    for (var j = 0; j < this.selectedIndices.length; j++) {
                        oldIndex = this.selectedIndices[j];
                        if (oldRows[oldIndex].rowId == onePageRows[newIndex].rowId) {
                            selChanged = false;
                            break;
                        }
                    }
                    if (selChanged)
                        break;
                }
            }
            if (isCurrent && selChanged && this.selectedIndices) {
                for (var i = this.selectedIndices.length - 1; i >= 0; i--) {
                    this.setRowUnSelected(this.selectedIndices[i]);
                }
            }
            var isChanged = onePageObj["changed"];
            if (isChanged || isInit) {
                delete this.rows;
                delete this.deleteRows;
                delete this.selectedIndices;
                this.rows = onePageRows;
                for (var i = 0; i < this.rows.length; i++) {
                    this.rows[i].pageData = this;
                }
                if (isCurrent) {
                    var pevent = {
                        type : 'PageChangeEvent',
                        pageIndex : this.pageindex
                    };
                    this.dataset.dispatchEvent(pevent);
                    selChanged = true;
                    // 换页之后，重新触发选中
                }
            } else {
                var oldRows = this.getRows();
                var nindex = 0;
                for (var i = 0; i < onePageRows.length; i++) {
                    if (onePageRows[i] instanceof $.emptydsrow) {
                        continue;
                    }
                    var find = false;
                    for (var j = nindex; j < oldRows.length; j++) {
                        if (oldRows[j].rowId && oldRows[j].rowId == onePageRows[i].rowId) {
                            this.$updateRow(j, oldRows[j], onePageRows[i]);
                            nindex++;
                            find = true;
                            break;
                        }
                    }
                    if (!find && onePageRows[i].rowId) {
                        this.insertRow(i, onePageRows[i]);
                    }
                }
                var deleteIdsJson = onePageObj.onePage["deleteRows"];
                if (deleteIdsJson) {
                    for (var i = 0; i < deleteIdsJson.length; i++) {
                        var index = this.getRowIndexById(deleteIdsJson[i]);
                        this.removeRow(index);
                    }
                }
            }

            if (isCurrent) {
                if (selectedIndexs != null && selChanged) {
                    for (var i = 0; i < selectedIndexs.length; i++) {
                        this.dataset.addRowSelected(selectedIndexs[i]);
                    }
                }
            } else {
                this.selectedIndices = selectedIndexs;
            }
        },
        /**
         * 增加行
         */
        addRow : function(row) {
            this.insertRow(this.getRowCount(), row);
        },
        /**
         * 插入行
         */
        insertRow : function(index, row) {
            this.insertRows(index, [row]);
        },
        /**
         * 设值
         */
        setValueAt : function(rowIndex, colIndex, value) {
            var row = this.getRow(rowIndex);
            var oldValue = row.setCellValue(colIndex, value);
            if (value == oldValue)
                return;
            var dataset = this.dataset;
            var event = {
                type : 'DataChangeEvent',
                currentRow : row,
                cellRowIndex : rowIndex,
                cellColIndex : colIndex,
                currentValue : value,
                oldValue : oldValue,
                datasetId : dataset.id
            };

            if (!dataset.silent) {
            	dataset.element.triggerHandler('onbeforedatachange', null,event);
                dataset.dispatchEvent(event);
                // 处理参照编辑公式
                // if(withTrigger == null || withTrigger)
                // this.processRefEditFormula(rowIndex, colIndex, value, withTrigger);
                //  校验cell数据
                dataset.checkDatasetCell(value, colIndex, row);

                // 对外暴露函数
                dataset.onAfterDataChange(event);
            }
        },
        /**
         * 插入多行
         */
        insertRows : function(index, rows) {
            var oldRows = this.getRows();
            if (index > oldRows.length)
                index = oldRows.length;

            var dataset = this.dataset;
            if (dataset.onBeforeRowInsert(index, rows) == false)
                return;
            if (rows != null) {
                if (oldRows == null) {
                    alert("the current data block is not initialized, key is:" + this.currentKey);
                    return;
                }
                for (var i = 0; i < rows.length; i++) {
                    rows[i].state = $.datasetrow.CONST.STATE_NEW;
                    oldRows.splice(index + i, 0, rows[i]);
                }
            }
            var event = {
                type : 'RowInsertEvent',
                insertedRows : rows,
                insertedIndex : index
            };
            if (!dataset.silent) {
                dataset.dispatchEvent(event);
                dataset.onAfterRowInsert(event);
            }
        },
        /**
         * 删除行
         */
        removeRow : function(index) {
            this.removeRows([index]);
        },
        /**
         * 删除索引指定的多行
         *
         */
        removeRows : function(rowIndices) {
            var oldRows = this.getRows();
            var delAll = false;
            // 排序
            if (rowIndices == -1) {
                rowIndices = [];
                delAll = true;
                var count = this.getRowCount();
                for (var i = 0; i < count; i++)
                    rowIndices.push(i);
            } else {
                rowIndices = rowIndices.sort($.dataset.defaultIntSort);
            }

            // 执行删除前的方法(阻止进一步执行需要抛出异
            this.dataset.onBeforeRowDelete(rowIndices);

            var tmpRows = [];
            var selIndices = this.getSelectedIndices();
            for (var i = rowIndices.length - 1; i >= 0; i--) {
                $.pageutils.removeFromArray(selIndices, rowIndices[i]);
                // 删除行之后，大于此行的索引都应该减一
                if (selIndices != null) {
                    for (var j = 0; j < selIndices.length; j++) {
                        if (selIndices[j] > rowIndices[i])
                            selIndices[j]--;
                    }
                }
                var delRow = oldRows[rowIndices[i]];
                if (delRow == null) {
                    continue;
                }
                oldRows.splice(rowIndices[i], 1);
                // 如果是新增行不放入删除行列表永久删除)
                if (delRow.state != $.datasetrow.CONST.STATE_NEW) {
                    if (this.deletedRows == null)
                        this.deletedRows = [];
                    this.deletedRows.push(delRow);
                }
                tmpRows.push(delRow);
            }

            if (!this.dataset.silent) {
                var event = {
                    type : 'RowDeleteEvent',
                    deletedRows : tmpRows,
                    deletedIndices : rowIndices,
                    deleteAll : delAll
                };
                this.dataset.dispatchEvent(event);
                this.dataset.onAfterRowDelete(event);
            }
        },
        /**
         * 更新行
         * @private
         */
        $updateRow : function(index, oldRow, newRow) {
            if (newRow.empty)
                return;
            var ds = this.dataset;
            var mds = ds.fieldList;
            var oldState = oldRow.state;
            if (newRow.changedArr != null && newRow.changedArr.length > 0) {
                for (var i = 0; i < newRow.changedArr.length; i++) {
                    var changedIndex = newRow.changedArr[i];
                    var newValue = newRow.getCellValue(changedIndex);
                    if (newValue == null)
                        newValue = "";
                    var oldValue = oldRow.getCellValue(changedIndex);
                    if (oldValue == null)
                        oldValue = "";
                    if (newValue != oldValue) {
                        this.setValueAt(index, changedIndex, newValue);
                    }
                }
            } else {
                for (var i = 0; i < mds.length; i++) {
                    var newValue = newRow.getCellValue(i);
                    if (newValue == null)
                        newValue = "";
                    var oldValue = oldRow.getCellValue(i);
                    if (oldValue == null)
                        oldValue = "";
                    if (newValue != oldValue) {
                        this.setValueAt(index, i, newValue);
                    }
                }
            }
            //如果oldRow的state有发生变化，则不再赋值
            if (oldRow.state == oldState)
                oldRow.state = newRow.state;
            //对假删除行打标记
            if (newRow.state == $.datasetrow.CONST.STATE_FALSE_DEL) {
                var event = {
                    type : 'DataFalseDelEvent',
                    delRowIndex : index,
                    delRow : newRow
                };
                ds.dispatchEvent(event);

            }
        },
        /**
         * 获取行索引
         */
        getRowIndices : function(rows) {
            var indices = [];
            for (var i = 0; i < rows.length; i++) {
                indices.push(this.getRowIndex(rows[i]));
            }
            return indices;
        },
        /**
         * 获取行索引
         */
        getRowIndex : function(row) {
            var rows = this.getRows();
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].rowId == row.rowId)
                    return i;
            }
            return -1;
        },
        /**
         * 根据ID获取行索引
         */
        getRowIndexById : function(id) {
            var rows = this.getRows();
            for (var i = 0; i < rows.length; i++) {
                if (rows[i].rowId == id)
                    return i;
            }
            return -1;
        },
        /**
         * 获取所有行
         */
        getRows : function() {
            return this.rows;
        },
        /**
         * 获取指定行
         */
        getRow : function(index) {
            return this.rows[index];
        },
        /**
         * 获取指定范围内的行
         * @param startIndex 开始索引
         * @param count 查询数量
         */
        getRowsByScale : function(start, count) {
            var rows = this.getRows();
            if (rows == null)
                return null;
            if (start >= rows.length)
                return null;
            var rowArr = [];
            for (var i = start; i < count && i < rows.length; i++) {
                rowArr.push(rows[i]);
            }
            return rowArr;
        },
        /**
         * 行反选 （废弃）
         */
        setRowUnSelected_ : function(index) {
            var selIndices = this.getSelectedIndices();
            if (selIndices == null)
                return;
            for (var i = 0; i < selIndices.length; i++) {
                if (selIndices[i] == index) {
                    selIndices.splice(i, 1);
                    var event = {
                        type : 'RowUnSelectEvent',
                        currentRowIndex : index
                    };
                    if (!this.dataset.silent) {
                        this.dataset.dispatchEvent(event);
                        this.dataset.onAfterRowUnSelect(event);
                    }
                    //多选时，如果存在已选中行，派发选中事件
                    if (selIndices.length > 0) {
                        var event = {
                            type : 'RowSelectEvent',
                            isMultiSelect : true
                        };
                        var index = selIndices[0];
                        event.currentRow = this.getRows()[index];
                        event.currentRowIndex = index;
                        if (!this.dataset.silent) {
                            this.dataset.dispatchEvent(event);
                        }
                    }
                }
            }
        },
        /**
         * 设置所有行非选中
         *
         */
        setAllRowUnSelected : function() {
            var selIndices = this.getSelectedRows();
            this.setRowUnSelected(selIndices);
        },
        /**
         * 行反选
         */
        setRowUnSelected : function(indices) {
            var selIndices = this.getSelectedIndices();
            if (selIndices == null)
                return;
            var newUnSelected = false;
            if ( indices instanceof Array) {
                var event = {
                    type : 'RowUnSelectEvent'
                };
                for (var i = 0; i < indices.length; i++) {
                    var index = selIndices.indexOf(indices[i]);
                    if (index != -1) {
                        selIndices.splice(index, 1);
                        newUnSelected = true;
                        var event = {
                            type : 'RowUnSelectEvent'
                        };
                        event.currentRowIndex = indices[i];
                        if (!this.dataset.silent) {
                            this.dataset.dispatchEvent(event);
                        }
                    }
                }
                if (!this.dataset.silent) {
                    this.dataset.onAfterRowUnSelect(event);
                }
            } else {
                var index = selIndices.indexOf(indices);
                if (index != -1) {
                    newUnSelected = true;
                    selIndices.splice(index, 1);
                    var event = {
                        type : 'RowUnSelectEvent'
                    };
                    event.currentRowIndex = indices;
                    if (!this.dataset.silent) {
                        this.dataset.dispatchEvent(event);
                        this.dataset.onAfterRowUnSelect(event);
                    }
                }
            }
            //多选时，如果存在已选中行，派发选中事件
            newUnSelected = false;
            if (selIndices.length > 0 && newUnSelected) {
                var event = {
                    type : 'RowSelectEvent',
                    isMultiSelect : true
                };
                var index = selIndices[selIndices.length - 1];
                if (this.dataset.onBeforeRowSelect(index) == false)
                    return false;
                event.currentRow = this.getRows()[index];
                event.currentRowIndex = index;
                if (!this.dataset.silent) {
                    this.dataset.dispatchEvent(event);
                    this.dataset.onAfterRowSelect(event);
                }
            }
        },
        /**
         * 获取行数
         */
        getRowCount : function() {
            var rows = this.getRows();
            if (rows == null)
                return 0;
            return rows.length;
        },
        /**
         * 获取所有选中行索引
         */
        getSelectedIndices : function() {
            return this.selectedIndices;
        },
        /**
         * 获取选中行索引
         */
        getSelectedIndex : function() {
            if (this.selectedIndices == null || this.selectedIndices.length == 0)
                return -1;
            return this.selectedIndices[0];
        },
        /**
         * 获取选中行
         */
        getSelectedRow : function() {
            var rows = this.getSelectedRows();
            if (rows == null)
                return null;
            return rows[0];
        },
        /**
         * 获取所有选中行
         */
        getSelectedRows : function() {
            var indices = this.getSelectedIndices();
            if (indices == null || indices.length == 0) {
                return null;
            }
            var rows = this.getRows();
            var selRows = [];
            for (var i = 0; i < indices.length; i++)
                selRows.push(rows[indices[i]]);
            return selRows;
        }
    });

    /**
     * Dataset空行
     * @class Dataset空行
     * @param rowId
     * @return
     */
    $.emptydsrow = function(rowId) {
        this.rowId = rowId;
        this.empty = true;
    };
    $.emptydsrow.getObj = function(rowId) {
        return new $.emptydsrow(rowId);
    };

    /**
     * @private 转换Node列表到DatasetRow列表。如果传入数据为空，返回空数组而不是null
     * @param node列表
     * @param dataset 数据集
     * @return DatasetRow数组
     * @private
     */
    $.dataset.maskNodesToRecords = function(onePageObj, dataset) {
        var entryRows = onePageObj["entryRows"];
        if (entryRows == null || entryRows.length == 0) {
            return [];
        }
        var emptyRows = onePageObj["emptyRows"];
        var total = 0;
        var count0 = 0;
        if (emptyRows) {
            count0 = emptyRows.length;
        }
        var count1 = 0;
        if (entryRows) {
            count1 = entryRows.length;
        }
        total = count0 + count1;
        var pageRows = new Array(total);
        var count = 0;
        if (entryRows) {
            for (var i = 0; i < entryRows.length; i++) {
                var oneRow = entryRows[i];
                pageRows[i] = $.datasetrow.getObj({
                    oneRowJson : oneRow,
                    dataset : dataset
                });
                count++;
            }
        }
        if (emptyRows) {
            for (var i = 0; i < emptyRows.length; i++) {
                var oneRow = emptyRows[i];
                var rowId = oneRow["id"];
                pageRows[count] = $.emptydsrow.getObj(rowId);
                count++;
            }
        }
        return pageRows;
    };
    /**
     * @private
     */
    $.dataset.defaultRowSorter = function(row1, row2) {
        var keys = this.tempKeys;
        var orders = this.tempOrders;
        var ds = this.tempDs;
        return 0;
    };
    /**
     * @private
     */
    $.dataset.defaultIntSort = function(value1, value2) {
        var intV1 = parseInt(value1);
        var intV2 = parseInt(value2);
        if (intV1 < intV2)
            return -1;
        else if (intV1 == intV2)
            return 0;
        return 1;
    };

    /********************************************************
     *  根据DatasetRelation进行Dataset之间的事件分发及处理。
     *  需要在有主子关系的Dataset上处理事件的业务逻辑，可实现此处暴露的方法
     ******************************************************/
    $.dataset.$countFormular = function(array, ds, event) {
        var formular = array.formular;
        var tmp = [];
        var mathMap = $.hashmap.getObj();
        mathMap.put("min", "Math.min");
        mathMap.put("max", "Math.max");
        while (formular.length != 0) {
            var val = formular.shift();
            if (val.type != 'ASTFunNode')
                tmp.push(val);
            else {
                var two = $.dataset.$convert(tmp.pop(), ds, event);
                var one = $.dataset.$convert(tmp.pop(), ds, event);
                if (one == null || two == null)
                    return;
                try {
                    //防止出现以0开头的数据
                    if (two != null && two != "" && two.indexOf(".") != -1)
                        two = parseFloat(two);
                    else if (two != null && two != "")
                        two = parseInt(two);
                    if (one != null && one != "" && one.indexOf(".") != -1)
                        one = parseFloat(one);
                    else if (one != null && one != "")
                        one = parseInt(one);
                } catch (error) {
                }
                var result = null;
                if (mathMap.containsKey(val.value)) {
                    result = eval(mathMap.get(val.value) + "(" + one + "," + two + ")");
                } else
                    result = eval(one + val.value + "(" + two + ")");
                var v = new Object;
                v.type = 'ASTConstant';
                v.value = result;
                tmp.push(v);
            }
        }
        var cellKey = array.key;
        var result = tmp.pop().value;
        ds.setValueAt(event.cellRowIndex, ds.nameToIndex(cellKey), result, true);
    };

    $.dataset.$convert = function(tmp, ds, event) {
        if (tmp.type == 'ASTConstant')
            return tmp.value;
        else if (tmp.type == 'ASTVarNode') {
            var row = event.currentRow;
            var result = row.getCellValue(ds.nameToIndex(tmp.value));
            if (result == null || result == '')
                result = null;
            return result;
        }
    };

    /**
     * 执行验证公式
     * @param ds 待验证的ds
     * @param fieldArr 需要验证的ds属性，为null则遍历ds
     * @return 如果不通过，返回true
     */
    $.dataset.executeValidateFormular = function(ds) {
        var meta = ds.fieldList;
        // 构造验证公式
        var vfListStr = "{javaClass:'java.util.ArrayList',list:[";
        var vfArr = [];
        var flag1 = false;
        for (var i = 0, len = meta.length; i < len; i++) {
            var f = meta[i].validateFormular;
            if (f != null && f != "") {
                vfArr.push(f);
                flag1 = true;
            }
        }
        if (!flag1)
            return false;
        for (var i = 0, len = vfArr.length; i < len; i++) {
            vfListStr += "'" + vfArr[i] + "'";
            if (i != len - 1)
                vfListStr += ",";
        }

        vfListStr += "]}";
        // 构造值
        var rows = ds.getRows();
        if (rows == null || ds.getRowCount() == 0)
            return false;
        var listStr = "{javaClass:'java.util.ArrayList',list:[";
        for (var k = 0, len = rows.length; k < len; k++) {

            var row = rows[k];
            var obj = {};
            obj.javaClass = 'java.util.HashMap';
            var objMap = {};
            obj.map = objMap;

            var objMap = "{javaClass:'java.util.HashMap',map:{";
            var arr = [];
            for (var i = 0; i < ds.fieldList.length; i++) {
                var md = ds.fieldList[i];
                var value = row.getCellValue(i);
                // 将"."替换成 _$_
                var replaceField = md.field.replace(".", "_$_");
                objMap[replaceField] = value;
                if (value != null) {
                    var dataType = md.dataType;
                    if (dataType == $.datatype.INTEGER || dataType == $.datatype.FDOUBLE || dataType == $.datatype.INT || dataType == $.datatype.DOUBLE || dataType == $.datatype.dOUBLE || dataType == $.datatype.Decimal)
                        arr.push(replaceField + ":" + value);
                    else if (dataType == $.datatype.BOOLEAN || dataType == $.datatype.bOOLEAN) {
                        arr.push(replaceField + ":" + value);
                    } else
                        arr.push(replaceField + ":'" + value + "'");
                } else
                    arr.push(replaceField + ":null");
            }

            objMap += arr.join(",");
            listStr += objMap + "}}";
            if (k != len - 1)
                listStr += ",";
        }
        listStr += "]}";
        var result = $.pageutils.getFormularService().executeVatelitionFormular(vfListStr, listStr);
        return $.dataset.$executeResult(result, ds, event);
    };
    /*********************************************验证*********************************************************/
    /**
     * 编辑公式中的验证
     * @param result
     * @param ds
     * @param event
     * @return
     * @private
     */
    $.dataset.$executeResult = function(result, ds, event) {
        if (result == null)
            return false;
        var fv = result.map["formular_value"];
        if (fv == null || fv == '' || fv == '$NULL$')
            return false;

        var fk = result.map["formular_key"];
        var flag = false;
        switch (fk) {
        case "$Confirm":
            var params = [ds, event];
            $.dataset.$ok4ConfirmDlg.param = params;
            $.dataset.$cancen4ConfirmDlg.param = params;
            flag = true;
            $.pageutils.showConfirmDialog(fv, $.dataset.$ok4ConfirmDlg, $.dataset.$cancen4ConfirmDlg);
            break;
        case "$Error":
            var params = [ds, event];
            $.dataset.$ok4ErrorDlg.param = params;
            flag = true;
            $.pageutils.showWarningDialog(fv, $.dataset.$ok4ErrorDlg);
            break;
        case "$Message":
            flag = true;
            $.pageutils.showMessageDialog(fv);
            break;
        default:
            break;
        }
        return flag;
    };
    $.dataset.$ok4ConfirmDlg = function() {
    };
    $.dataset.$cancen4ConfirmDlg = function() {
        var params = $.dataset.$cancen4ConfirmDlg.param;
        var ds = params[0];
        var event = params[1];
        ds.setValueAt(event.cellRowIndex, event.cellColIndex, 0);
        delete ($.dataset.$cancen4ConfirmDlg.param);
    };
    $.dataset.$ok4ErrorDlg = function() {
        var params = $.dataset.$ok4ErrorDlg.param;
        var ds = params[0];
        var event = params[1];
        ds.setValueAt(event.cellRowIndex, event.cellColIndex, 0);
        delete ($.dataset.$ok4ErrorDlg.param);
    };
})(jQuery);




//        /**
//         * 获取范围内数据行
//         * @param startIndex 开始索引
//         * @param count 查询数量
//         */
//        getRowsByScale : function(startIndex, count) {
//            return this.getRowsByScale(startIndex, count);
//        },
