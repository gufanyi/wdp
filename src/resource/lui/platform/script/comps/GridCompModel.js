/**
 * Grid模型（数组类型）
 * @class GridCompModel定义 GridCompModel是grid的数据模型，此模型独立于gird。
 *        model适配dataset，无需了解dataset的实现细节，对dataset是
 *        透明化的，所有的数据插入，更新,修改均是对GridCompModel进行操作。
 * @author lxl
 * @version lui 1.0
 */
(function($){
	$.gridcompmodel=function(){}
	$.gridcompmodel.getObj=function(){
		return new $.gridcompmodel();
	}
	$.extend($.gridcompmodel.prototype,{
		/**
		 * 设置headers
		 */
		setHeaders:function(headers) {
			this.headers = headers;
		},
		/**
 		  * 得到model中的headers数据
 		  */
		getHeaders :function() {
			return this.headers;
		},
		removeHeader : function(keyName) {
		    for (var i = 0; i < this.headers.length; i++) {
		        if (this.headers[i].keyName == keyName) {
		            var header = this.headers[i];
		            this.headers.splice(i, 1);
		            return header;
		        }
		    }
		    return null;
		},
		/**
		 * 得到指定数据行
		 */
		getRow:function(index) {
		    var tempIndex = parseInt(index);
		    var row = this.rows[index];
		    return row;
		},
		/**
		 * 得到指定数据行
		 */
		getRowIndexById : function(id) {
		    for (var i = 0, n = this.rows.length; i < n; i++) {
		        if (this.rows[i].pageData.rowId == id) 
					return i;
		    }
		    return - 1;
		},
		getRowIndexByValue : function(field, value) {
		    for (var i = 0, n = this.rows.length; i < n; i++) {
		        if (this.rows[i].getCellValueByFieldName(field) == value) 
		        	return i;
		    }
		    return - 1;
		},
		/**
		 * 得到grid指定行列的cell值
		 * @param rowIndex 行索引
		 * @param colIndex 列索引
		 */
		getCellValueByIndex : function(rowIndex, colIndex) {
		    var tempRowIndex = parseInt(rowIndex);
		    var tempColIndex = parseInt(colIndex);
		    if (tempRowIndex < 0 || tempRowIndex >= this.dataset.getRowCount()) 
		    	return null;
		    if (colIndex > this.basicHeaders.length - 1) 
		    	return null;
		    return this.rows[rowIndex].getCellValue(colIndex);
		},
		/**
		 * 得到所有的数据行
		 * @return{$.gridrow[]} $.gridrow的数组,若没有任何数据返回null
		 */
		getRows : function(key) {
		    if (key != null) {
		        return this.rowsMap[key];
		    }
		    if (this.rows == null || this.rows.length == 0) 
		    	return null;
		    return this.rows;
		},
		/**
		 * 对model中的gridrow进行排序，如果没有传入sortFuncs数组，则按照每列的数据类型调用默认的排序函数
		 * 如果没有传入sortDirecs数组则默认按照升序排序
		 * @param sortHeaders{Array} 要排序的表头
		 * @param sortFuncs{Array} 每一列排序的排序算法(指定的排序算法都按升序)
		 * @param ascending{Array} 每一列排序的方向(升序-1, 降序1)
		 */
		 sortable : function(sortHeaders, sortFuncs, ascendings) {
		    // 没有数据不进行排序
		    if (this.rows == null || this.rows.length == 0) 
		    	return;
		    if (sortHeaders == null || sortHeaders.length == 0)
		    	return;
		    if (sortFuncs != null && sortHeaders.length != sortFunsc.length) 
		    	return;
		    // 设置默认排序函数
		    if (sortFuncs == null || ascendings.length == 0) {
		        sortFuncs = new Array(sortHeaders.length);
		        var dataType = null;
		        for (var i = 0, count = sortHeaders.length; i < count; i++) {
		            dataType = sortHeaders[i].dataType;
		            if (dataType == $.datatype.INTEGER || dataType == $.datatype.INT) 
		            	sortFuncs[i] = $.gridcompmodel.sortRowsByIntergerColum;
		            else if (dataType == $.datatype.dOUBLE || dataType == $.datatype.FDOUBLE || dataType == $.datatype.DOUBLE) 
		            	sortFuncs[i] = $.gridcompmodel.sortRowsByDecimalColum;
		            else 
		            	sortFuncs[i] =$.gridcompmodel.defaultSortRows;
		        }
		    }
		    // 设置默认排序方向(默认升序)
		    if (ascendings == null || ascendings.length == 0) {
		        ascendings = new Array(sortHeaders.length);
		        for (var i = 0, count = ascendings.length; i < count; i++) 
		        	ascendings[i] = -1;
		    }
		
		    if (sortHeaders.length == 1) {
		        // 存在sortHeader,说明已经排序过,则此时只需要reverse即可
		        sortFuncs[0].index = this.basicHeaders.indexOf(sortHeaders[0]);
		        sortFuncs[0].ascending = ascendings[0];
		        var row1 = this.rows[0];
		        var row2 = null;
		        var needSort = false;
		        for (var i = 1; i < this.rows.length; i++) {
		            row2 = this.rows[i];
		            if (row1.getCellValue(sortFuncs[0].index) != row2.getCellValue(sortFuncs[0].index)) {
		                needSort = true;
		                break;
		            }
		            row1 = this.rows[i];
		        }
		        if (needSort == true) {
		            this.rows.sort(sortFuncs[0]);
		            if (sortFuncs[0].ascending != sortHeaders[0].ascending) {
		                this.rows.reverse();
		            }
		        }
		    } else {
		        // 按照指定的多列排序
		        for (var i = 0, count = sortHeaders.length; i < count; i++) {
		            sortFuncs[i].index = this.basicHeaders.indexOf(sortHeaders[i]);
		            sortFuncs[i].ascending = ascendings[i];
		            this.rows.sort(sortFuncs[i]);
		        }
		    }
		    this.owner.paintRows(true);
		},
		/**
		 * 该参数是为了前后台分页准备的，initUIRows时需要去获取ds相应的行初始化grid rows
		 */
		 initUIRows : function(startIndex, count, key, level) {
		    // 保存UI rows的数组
		    if (this.rows == null) {
		        this.rows = [];
		    }
		    var dsRows = null;
		    if (startIndex == null) 
		    	startIndex = 0;
		    //	// 否则只有startIndex,说明是后台分页,获取相应页码的数据集
		    if (this.treeLevel != null) 
		    	dsRows = this.filterRows(this.dataset, key);
		    else 
		    	dsRows = this.dataset.getRows();
		    if (dsRows != null) {
		        // 将dataset的rows放入grid的UI rows中
		        var bindRelation = this.bindRelation;
		        var mrows = [];
		        for (var i = 0, count = dsRows.length; i < count; i++) {
		            // 对dataset中的行进行适配包装(内部使用尽量采用属性设置,避免方法调用)
		            var row = $.gridrow.getObj();
		            row.pageData = dsRows[i];
		            row.relation = bindRelation;
		            this.rows.splice(startIndex + i, 0, row);
		            mrows[i] = row;
		            mrows[i].hasChildren = this.hasChildren(this.dataset, dsRows[i]);
		            if (level != null) 
		            	mrows[i].level = level + 1;
		        }
		        if (key != null) {
		            if (this.rowsMap == null) 
		            	this.rowsMap = {};
		            this.rowsMap[key] = mrows;
		        }
		    } else {
		        if (key != null) {
		            if (this.rowsMap == null) 
		            	this.rowsMap = {};
		            this.rowsMap[key] = [];
		        }
		    }
		},
		/**
		 * 得到dataset row在grid上的rowIndex
		 * @param dsRow dateset行数据
		 * @return 没有找到返回-1
		 */
		getUIRowIndex : function(dsRow) {
		    if (!dsRow) 
		    	return - 1;
		    var rows = this.rows;
		    for (var i = 0, count = rows.length; i < count; i++) {
		        if (rows[i].pageData.rowId == dsRow.rowId) 
		        	return i;
		    }
		    return - 1;
		},
		/**
		 * 设置row、col所对应元素的值
		 * @param rowIndex 行索引
		 * @param colIndex 列索引
		 * @param value 新值
		 */
		setValueAt : function(rowIndex, colIndex, newValue) {
		    if (this.dataset == null) 
		    	$.pageutils.showErrorDialog("dataset is null!");
		    else {
		        var dsRow = this.rows[rowIndex].pageData;
		        var oldValue = dsRow.getCellValue(this.bindRelation[colIndex]);
		        if (oldValue != newValue) 
		        	this.dataset.setValueAt(this.dataset.getRowIndex(dsRow), this.bindRelation[colIndex], newValue);
		    }
		},
		/**
		 * 设置row、col所对应元素的值,不派发事件
		 * @param rowIndex 行索引
		 * @param colIndex 列索引
		 * @param value 新值
		 */
		setCellValueAt: function(rowIndex, colIndex, newValue, oldValue) {
		    if (this.dataset == null) 
		    	$.pageutils.showErrorDialog("dataset is null!");
		    else {
		        var dsRow = this.rows[rowIndex].pageData;
		        var index = this.dataset.getRowIndex(dsRow);
		        var row = this.dataset.getRow(index);
		        row.setCellValue(this.bindRelation[colIndex], newValue);
		        this.owner.cellValueChangedFunc(rowIndex, colIndex, newValue, oldValue);
		    }
		},
		/**
		 * Model发生变化时的回调函数
		 */
		onModelChanged : function(event) {
		    var g = this.owner;
		    if (!g) 
		    	return;
		    // 行选中时
		    if (event.type == 'RowSelectEvent') {
		        if (event.currentRow == null) 
		        	return;
		        var index = this.getUIRowIndex(event.currentRow);
		        if (index == -1) {
		            // 如果发现该dsRow不属于该grid,并且该grid的选中数组中有记录,则清除该记录. gd 2008-11-20
		            // 只处理单选的情况,对于多选暂不考虑
		            if (g.selectedRowIndice != null && g.selectedRowIndice.length == 1) 
		            	g.clearAllUISelRows();
		            return;
		        }
		        // 单选的情况
		        if (g.isMultiSelWithBox == false) {
		            // 如果处于多选状态
		            if (event.isAdd) {
		                // 追加行选中
		                g.rowSelected(index, true);
		            } else {
		                // 如果选中行不唯一则清除所有选中行(清除外观,从选中行数组中去除)
		                if (g.selectedRowIndice != null && g.selectedRowIndice.length > 0) 
		                	g.clearAllUISelRows();
		                g.rowSelected(index);
		                // 如果已经选中的cell和当前选中行不在同一行,则把已经选中的cell的边框去掉
		                if (g.oldCell != null && g.oldCell.data('rowIndex') != index) {
		                    g.oldCell.attr('class',g.oldClassName);
		                }
		            }
		            // 单选模式下，如果当前聚焦行不是选中行，则设置当前聚焦行为该选中行
		            if (g.getFocusIndex() != index) 
		            	g.setFocusIndex(index);
		        }
		        // 多选模式为checkbox的情况
		        else if (g.isMultiSelWithBox) {
		            if (event.isMultiSelect) {
		                // 得到所有选中行
		                var selRows = this.dataset.getSelectedRows();
		                if (selRows != null && selRows.length > 0) {
		                    for (var i = 0, count = selRows.length; i < count; i++) {
		                        var rowIndex = this.getUIRowIndex(selRows[i]);
		                        if (rowIndex != -1) {
		                            var cell = g.selectColumDiv.data('cells')[rowIndex];
		                            if (cell != null && cell.children(':first').size() > 0 && cell.children(':first').prop('checked') != true) {
		                                cell.children(':first').prop('checked',true);
		                                //cell.firstChild.tempChecked = null;
		                                g.setCheckBoxChecked(true, rowIndex);
		                            }
		                        }
		                    }
		                }
		                //清除掉所有样式
		                g.clearAllUISelRows();
		                //设置焦点行为空
		                this.setFocusIndex( - 1);
		            } else {
		                var cell = g.selectColumDiv.data('cells')[index];
		                if (cell != null && cell.children(':first').size() > 0 && cell.children(':first').prop('checked') != true) {
		                    cell.children(':first').prop('checked',true);
		                    //cell.firstChild.tempChecked = null;
		                    g.setCheckBoxChecked(true, index);
		                }
		                //设置选中行
		                g.rowSelected(index);
		            }
		            var tempCheck = true;
		            var tempCells = g.selectColumDiv.data('cells');
		            if (tempCells) {
		                var tempLen = tempCells.length;
		                for (var k = 0; k < tempLen; k++) {
		                    if (tempCells[k] && tempCells[k].children(':first').size() > 0 && tempCells[k].children(':first').prop('checked') == false) {
		                        tempCheck = false;
		                        break;
		                    }
		                }
		                if (tempCheck) {
		                    // 选中全选按钮的选中勾
		                    g.selectAllBox.prop('checked',true);
		                }
		            }
		        }
		    }
		    // 行选中撤销事件
		    else if (event.type == 'RowUnSelectEvent') {
		        var index = this.getUIRowIndex(this.dataset.getRow(event.currentRowIndex));
		        if (index == -1) 
		        	return;
		        // ctrl模式多选
		        if (g.isMultiSelWithBox) {
		            var cell = this.owner.selectColumDiv.data('cells')[index];
		            if (cell != null && cell.children(':first').size() > 0 && cell.children(':first').prop('checked') != false) {
		                cell.children(':first').prop('checked',false).data('tempChecked',null);
		                g.setCheckBoxChecked(false, index);
		            }
		            // 取消全选按钮的选中勾
		            g.selectAllBox.prop('checked',false);
		        }
		        var headerLength = g.basicHeaders.length, selIndice = g.selectedRowIndice;
		        if (selIndice != null && selIndice.length > 0) {
		            var header = null, cell = null;
		            for (var i = 0; i < selIndice.length; i++) {
		                if (index == selIndice[i]) {
		                    for (var j = 0; j < headerLength; j++) {
		                        header = this.basicHeaders[j];
		                        if (header.isHidden == false) {
		                            cell = header.dataDiv.data('cells')[index];
		                            if (cell != null) {
		                                var isOdd = g.isOdd(index);
		                                if (header.isFixedHeader) {
		                                    if (cell.data('isErrorCell')) 
		                                    	cell.attr('class',isOdd ? "fixed_gridcell_odd cell_error": "fixed_gridcell_even cell_error");
		                                    else 
		                                    	cell.attr('class',isOdd ? "fixed_gridcell_odd": "fixed_gridcell_even");
		                                } else {
		                                    if (cell.data('isErrorCell')) 
		                                    	cell.attr('class',isOdd ? "gridcell_odd cell_error": "gridcell_even cell_error");
		                                    else 
		                                    	cell.attr('class',isOdd ? "gridcell_odd": "gridcell_even");
		                                }
		                            }
		                        }
		                    }
		
		                    // 设置此行状态div的背景
		                    var node = g.lineStateColumDiv.data('cells')[selIndice[i]];
		                    if (node.attr('class') != "row_state_div row_update_state" && node.attr('class') != "row_state_div row_add_state") {
		                        node.attr('class','row_state_div');
		                    }
		                    // 从选择数组中将此选择行删除
		                    g.selectedRowIndice.splice(i, 1);
		                    // 没有选中行将选中数组置空
		                    if (g.selectedRowIndice.length == 0) 
		                    	g.selectedRowIndice = null;
		                    break;
		                }
		            }
		        }
		        // 单选的情况，清除当前聚焦行
		        if (g.isMultiSelWithBox == false) {
		            g.setFocusIndex( - 1);
		        }
		    }
		    // cell数据改变时
		    else if (event.type=='DataChangeEvent') {
		        var rowIndex = this.getUIRowIndex(event.currentRow);
		        if (rowIndex == -1) 
		        	return;
		        // 由绑定关系得到dataset中的列索引对应的grid中的列索引
		        var colIndex = this.bindRelation.indexOf(event.cellColIndex);
		        if (colIndex != -1) {
		            g.cellValueChangedFunc(rowIndex, colIndex, event.currentValue, event.oldValue);
		            // 如果改变的是合计列,则计算合计列的值
		            if (g.isShowSumRow && this.basicHeaders[colIndex].isSumCol) {
		                this.setSumColValue(colIndex, this.basicHeaders[colIndex].keyName);
		            }
		        }
		        //如果是更改父节点属性，要同时更改树表结构
		        if (this.treeLevel) {
		            var ppkField = this.treeLevel.recursivePKeyField;
		            var parentIndex = this.dataset.nameToIndex(ppkField);
		
		            if (event.cellColIndex == parentIndex) {
		                var pageData = event.currentRow;
		                var gridRow = $.gridrow.getObj();
		                gridRow.setPageData(pageData);
		                gridRow.setBindRelation(this.bindRelation);
		                var oldpk = event.oldValue;
		                var newpk = event.currentValue;
		                var pkField = this.treeLevel.recursiveKeyField;
		                var oldrowIndex = this.getRowIndexByValue(pkField, oldpk);
		                var oldpRow = this.getRow(oldrowIndex);
		                if (oldrowIndex == -1 || (oldpRow.loadedChild != null && oldpRow.loadedChild == true)) {
		                    this.rows.splice(rowIndex, 1);
		                    g.fireRowDeleted([rowIndex]);
		                } else {
		                    oldpRow.loadImg.triggerHandler('click');
		                }
		
		                var newrowIndex = this.getRowIndexByValue(pkField, newpk);
		                if (newrowIndex == -1) { //父节点不存在
		                    gridIndex = this.getRowsCount();
		                    this.rows.splice(gridIndex, 0, gridRow);
		                    g.fireRowInserted(gridIndex);
		                } else {
		                    var newpRow = this.getRow(newrowIndex);
		                    var newlevel = this.getRow(newrowIndex).level == null ? 0 : this.getRow(newrowIndex).level;
		                    if (newpRow.loadedChild != null && newpRow.loadedChild == true) {
		                        g.showChildRows(newrowIndex);
		                        gridIndex = this.getChildrenCount(newrowIndex) + newrowIndex + 1;
		                        this.rows.splice(gridIndex, 0, gridRow);
		                        g.fireRowInserted(gridIndex, newlevel, newrowIndex);
		                    } else {
		                        newpRow.loadImg.triggerHandler('click');
		                    }
		                }
		            }
		        }
		    }
		    // 某列的多个cell数据改变时
		    else if (event.type=='DataColSingleChangeEvent') {
		        for (var i = 0; i < event.cellRowIndices.length; i++) {
		            var rowIndex = this.getUIRowIndex(event.currentRows[i]);
		            if (rowIndex == -1) 
		            	continue;
		            var currentValue = event.currentValues[i];
		            var oldValue = event.oldValues[i];
		            // 由绑定关系得到dataset中的列索引对应的grid中的列索引
		            var colIndex = this.bindRelation.indexOf(event.cellColIndex);
		            if (colIndex != -1) {
		                g.cellValueChangedFunc(rowIndex, colIndex, currentValue, oldValue);
		                // 如果改变的是合计列,则计算合计列的值
		                if (g.isShowSumRow && this.basicHeaders[colIndex].isSumCol) {
		                    this.setSumColValue(colIndex, this.basicHeaders[colIndex].keyName);
		                }
		            }
		        }
		    }
		    // 违反校验规则的事件
		    else if (event.type == 'DataCheckEvent') {
		        var row = event.currentRow;
		        var colIndice = event.cellColIndices;
		        var rowIndex = this.getUIRowIndex(row);
		        if (rowIndex == -1) 
		        	return;
		        // grid中需要纠正输入的列
		        var colIndices = [];
		        // 由绑定关系得到dataset中的列索引对应的grid中的列索引
		        for (var i = 0, count = event.cellColIndices.length; i < count; i++) {
		            // 对于grid没有绑定的列为空了grid不负责显示
		            var index = this.bindRelation.indexOf(event.cellColIndices[i]);
		            if (index != -1) 
		            	colIndices.push(this.bindRelation.indexOf(event.cellColIndices[i]));
		        }
		        for (var i = 0, count = colIndices.length; i < count; i++) {
		            var header = g.basicHeaders[colIndices[i]];
		            if (header.isHidden) 
		            	return;
		            var cell = header.dataDiv.data('cells')[rowIndex];
		            if (cell != null) {
		                if (event.rulesDescribe[i] != "") { // 校验失败
		                    cell.data('isErrorCell',true);
		                    var isOdd = g.isOdd(rowIndex);
		                    if (header.isFixedHeader) 
		                    	cell.attr('class',isOdd ? "fixed_gridcell_odd cell_error": "fixed_gridcell_even cell_error");
		                    else 
		                    	cell.attr('class',isOdd ? "gridcell_odd cell_error": "gridcell_even cell_error");
		                    cell.data('tip',event.rulesDescribe[i]);
		                } else { // 校验成功
		                    cell.data('isErrorCell',false);
		                    var isOdd = g.isOdd(rowIndex);
		                    if (header.isFixedHeader) 
		                    	cell.attr('class',isOdd ? "fixed_gridcell_odd": "fixed_gridcell_even");
		                    else
		                    	cell.attr('class',isOdd ? "gridcell_odd": "gridcell_even")
		                    // 设置提示信息
		                    var title = row.getCellValue(colIndice);
		                    var editorType = header.editorType;
		                    if ("ComboBox" == editorType) {
		                        if (header.comboData != null && header.comboData.getValueArray() != null) {
		                            var varr = header.comboData.getValueArray();
		                            var vs = (title == null) ? [] : title.split(",");
		                            var indices = [];
		                            for (var i = 0; i < vs.length; i++) {
		                                var index = varr.indexOf(vs[i]);
		                                indices.push(index);
		                            }
		                            var parsedValueArr =[];
		                            if (indices.length > 0) {
		                                var narr = header.comboData.getNameArray();
		                                for (var i = 0; i < indices.length; i++) {
		                                    if (indices[i] != null && indices[i] != -1) 
		                                    	parsedValueArr.push(narr[indices[i]]);
		                                    else 
		                                    	parsedValueArr.push("");
		                                }
		                            }
		                            title = parsedValueArr.join(",");
		                        }
		                    }
		                    cell.data('tip',title);
		                }
		            }
		        }
		    }
		    // 整页数据更新
		    else if (event.type=='PageChangeEvent') {
		        // 对于分页,如果多选先将多选按钮变为非选中状态
		        if (g.isMultiSelWithBox && g.selectAllBox.prop('checked')) {
		            g.selectAllBox.prop('checked',false);
		        }
		        g.paintRows(false, event.pageIndex); // this.owner.setModel(this);
		        if (g.isShowSumRow) 
		        	this.setSumColValue(null, null);
		        // 设置分页信息
		        g.setPaginationInfo();
		        //如果有排序，先删除排序
		        if (g.basicHeaders) {
		            for (var i = 0, count = g.basicHeaders.length; i < count; i++) {
		                var headerDiv = g.basicHeaders[i].contentDiv;
		                if (headerDiv && headerDiv.find('.sort_img').size()>0) {
		                	headerDiv.find('.sort_img').remove();
		                }
		            }
		        }
		    }
		    // 插入新数据行
		    else if (event.type=='RowInsertEvent') {
		        if (g.isMultiSelWithBox && g.selectAllBox.prop('checked')) {
		            g.selectAllBox.triggerHandler('click');
		        }
		
		        var rows = event.insertedRows;
		        if (rows == null || rows.length == 0) 
		        	return;
		        var rowIndices = this.dataset.getRowIndices(rows);
		        if (rowIndices != null) {
		            var insertIndex = event.insertedIndex;
		            var gridIndex = insertIndex;
		            if (this.treeLevel != null) {
		                var pkField = this.treeLevel.recursiveKeyField;
		                for (var i = 0, count = rows.length; i < count; i++) {
		                    var pageData = rows[i];
		                    var gridRow = $.gridrow.getObj();
		                    gridRow.setPageData(pageData);
		                    gridRow.setBindRelation(this.bindRelation);
		                    var parentKey = this.getParentKey(gridRow);
		                    //parentKey为空时，直接插到最后
		                    if (parentKey == null || parentKey == "") {
		                        gridIndex = this.getRowsCount();
		                        this.rows.splice(gridIndex, 0, gridRow);
		                        g.fireRowInserted(gridIndex);
		                    } else {
		                        var pRowIndex = this.getRowIndexByValue(pkField, parentKey);
		                        //没找到父row时，通过ds找到最上级的父，逐级展开
		                        if (pRowIndex == -1) {
		                            var parentKeys = this.getParentKeys(this.dataset, parentKey);
		                            parentKeys.push(parentKey);
		                            while (parentKeys.length > 0) {
		                                var pKey = parentKeys[0];
		                                var index = this.getRowIndexByValue(pkField, pKey);
		                                if (index == -1) {
		                                    gridIndex = this.getRowsCount();
		                                    this.rows.splice(gridIndex, 0, gridRow);
		                                    g.fireRowInserted(gridIndex);
		                                    break;
		                                } else {
		                                    var pRow = this.getRow(index);
		                                    if (pRow.loadedChild != null && pRow.loadedChild == true) {
		                                        g.showChildRows(pRowIndex);
		                                    } else {
		                                        pRow.loadImg.triggerHandler('click');
		                                    }
		                                }
		                                parentKeys.splice(0, 1);
		                            }
		                        } else {
		                            var pRow = this.getRow(pRowIndex);
		                            var level = pRow.level == null ? 0 : pRow.level;
		                            if (pRow.loadedChild != null && pRow.loadedChild == true) {
		                                g.showChildRows(pRowIndex);
		                                gridIndex = this.getChildrenCount(pRowIndex) + pRowIndex + 1;
		                                this.rows.splice(gridIndex, 0, gridRow);
		                                g.fireRowInserted(gridIndex, level, pRowIndex);
		                            } else {
		                                pRow.loadImg.triggerHandler('click');
		                            }
		                        }
		                    }
		                }
		            } else {
		                for (var i = 0, count = rows.length; i < count; i++) {
		                    var pageData = rows[i];
		                    var row = $.gridrow.getObj();
		                    row.setPageData(pageData);
		                    row.setBindRelation(this.bindRelation);
		                    this.rows.splice(gridIndex, 0, row);
		                    g.fireRowInserted(gridIndex);
		                    gridIndex++;
		                }
		            }
		            if (g.isShowSumRow) 
		            	this.setSumColValue(null, null);
		        }
		        // 增行后增加分页条上的记录数
		        if (g.sumRowCountSpan) {
		            var count = parseInt(g.sumRowCountSpan.text()) + rowIndices.length;
		            g.sumRowCountSpan.html(count);
		        }
		    }
		    // 删除行
		    else if (event.type=='RowDeleteEvent') {
		        if (event.deleteAll == false) {
		            var indice = [];
		            for (var i = 0, count = event.deletedIndices.length; i < count; i++) {
		                var index = this.getUIRowIndex(event.deletedRows[i]);
		                if (index != -1) {
		                    this.rows.splice(index, 1);
		                    indice.push(index);
		                }
		            }
		            g.fireRowDeleted(indice);
		            // 删行后减少分页条上的记录数
		            if (g.sumRowCountSpan) {
		                var count = parseInt(g.sumRowCountSpan.text()) - indice.length;
		                g.sumRowCountSpan.html(count);
		            }
		        }
		        // 删除所有行数据
		        else if (event.deleteAll) {
		            var indice = [];
		            for (var i = 0, count = event.deletedIndices.length; i < count; i++) {
		                var index = this.getUIRowIndex(event.deletedRows[i]);
		                if (index != -1) {
		                    this.rows.splice(index, 1);
		                    indice.push(index);
		                }
		            }
		            g.fireRowDeleted(indice);
		            // 删行后减少分页条上的记录数
		            if (g.sumRowCountSpan) {
		                g.sumRowCountSpan.html(0);
		            }
		        }
		        if (g.isShowSumRow) 
		        	this.setSumColValue(null, null);
		    }
		    // undo操作
		    else if (event.type=='DatasetUndoEvent') {
		        g.paintRows();
		        var indice = [];
		        var selectedRows = this.dataset.getSelectedRows();
		        var UIIndex = -1;
		        if (selectedRows != null && selectedRows.length > 0) {
		            for (var i = 0, count = selectedRows.length; i < count; i++) {
		                UIIndex = this.getUIRowIndex(selectedRows[i]);
		                if (UIIndex != -1) 
		                	indice.push(UIIndex);
		            }
		            g.selectedRowIndice = indice;
		            g.rowSelected(indice[0]);
		        } else if (selectedRows != null && selectedRows.length == 0) {
		            // 没有选中行将选中数组置空
		            if (g.selectedRowIndice != null && g.selectedRowIndice.length == 0) 
		            	g.selectedRowIndice = null;
		        }
		        if (g.isShowSumRow) 
		        	this.setSumColValue(null, null);
		    }
		    // 接收此事件清除行状态图标
		    else if (event.type == 'StateClearEvent') {
		        for (var i = 0, count = g.lineStateColumDiv.children().length; i < count; i++) {
		            g.lineStateColumDiv.children(':eq('+i+')').attr('class','row_state_div');
		        }
		    }
		    // fieldList changeg 事件
		    else if (event.type=='MetaChangeEvent') {
		        //处理精度
		        if (event.precision != null) {
		            var index = this.bindRelation.indexOf(event.colIndex);
		            if (index != -1) {
		                this.basicHeaders[index].setPrecision(event.precision, true);
		            }
		        }
		    }
		    // 数据假删除 事件
		    else if (event.type=='DataFalseDelEvent') {
		        var index = event.delRowIndex;
		        var indice = [];
		        indice.push(index);
		        g.fireRowDeleted(indice);
		        // 删行后减少分页条上的记录数
		        if (g.sumRowCountSpan) {
		            var count = parseInt(g.sumRowCountSpan.text()) - indice.length;
		            g.sumRowCountSpan.html(count);
		        }
		    }
		
		    if (g.editable) { //编辑态增加*号
		        var headerLength = g.basicHeaders.length, header = null, cell = null;
		        var rowsNum = g.getRowsNum();
		        for (var i = 0; i < rowsNum; i++) {
		            for (var j = 0; j < headerLength; j++) {
		                header = this.basicHeaders[j];
		                if (!header.isHidden && header.required) {
		                    cell = header.dataDiv.data('cells')[i];
		                    var isShowRequired = false;
		                    //移除cell中存在的必输项SPAN
		                    if (cell.children() && cell.children().length > 0) {
		                        var length = cell.children().length;
		                        if (length > 0) {
		                            //移除cell中存在的必输项SPAN
		                            for (var count = length - 1; count >= 0; count--) {
		                            	var _cell = cell.children(':eq('+count+')');
		                                if (_cell) {
		                                    if (_cell.is('span') && _cell.attr('class') && _cell.attr('class').toLowerCase() == 'requiredstyle') {
		                                        _cell.remove();
		                                        continue;
		                                    }
		                                }
		                            }
		                        }
		                    }
		                    var childNodes = cell.children();
		                    if (childNodes && childNodes.length > 0) {
		                        var len = childNodes.length;
		                        for (var count = 0; count < len; count++) {
		                            if (childNodes[count]) {
		                                var innerText = $(childNodes[count]).text();
		                                if (innerText == '') {
		                                    isShowRequired = true;
		                                } else {
		                                    isShowRequired = false;
		                                    break;
		                                }
		                            }
		                        }
		                    } else {
		                        isShowRequired = true;
		                    }
		                    if (isShowRequired) {
		                        var requiredStar = $("<span>").addClass('requiredstyle').html("*");
		                        cell.append(requiredStar);
		                    }
		                }
		            }
		        }
		    }
		
		    if (g.errorMsgDiv && g.errorMsgDiv.css('display') == "block") {
		        g.errorMsgDiv.hide();
		    }
		},
		/**
		 * 根据后台生成的js脚本直接设置合计行的值
		 */
		setSumColValueByExecuteJs : function(gridColIndex, dsColName, sum) {
		    if (dsColName == null || sum == null || gridColIndex == null) 
		    	return;
		    //获取对应的列号,在grid未渲染完时this.rows为空，不能调用getCellValueByFieldName(),需要传入gridColIndex
		    //var gridColIndex = this.rows[0].getCellValueByFieldName(dsColName);
		    // 如果此列是合计行
		    if (this.basicHeaders[gridColIndex].isSumCol) {
		        var header = this.basicHeaders[gridColIndex];
		        var sumCells = this.owner.dynSumPageDataDiv.children();
		        for (var i = 0; i < sumCells.length; i++) {
		            if ($(sumCells[i]).data('headKey') == dsColName) {
		                if (header.textAlign != null) 
		                	textAlign = header.textAlign;
		                if (header.sumColRenderFunc) {
		                    header.sumColRenderFunc.call(window, this.owner, this, $(sumCells[i]), sum);
		                } else {
		                    if (isNaN(sum)) 
		                    	$(sumCells[i]).html(sum);
		                    else {
		                        var colorSum = $.maskerutil.toColorfulString($.numbermasker.getObj(window.$maskerMeta.NumberFormatMeta).format(sum));
		                        $(sumCells[i]).html(colorSum);
		                    }
		                }
		            }
		        }
		    }
		},
		/**
		 * 计算合计列的值,如果gridColIndex为null,dsColIndex也为null,则计算所有合计列的值
		 * @param gridColIndex grid中列索引
		 * @param dsColName ds列名
		 */
		setSumColValue : function(gridColIndex, dsColName) {
		    var textAlign = "center";
		    if (gridColIndex == null && dsColName == null) {
		        for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		            var header = this.basicHeaders[i];
		            if (header.isHidden == false && header.isSumCol) {
		                var sum = this.dataset.totalSum([header.keyName], null, null, header.precision);
		                var sumCells = this.owner.dynSumPageDataDiv.children();
		                // 格式化结果数据
		                sum = GridComp.parseData(header, sum, true);
		                for (var j = 0, count1 = sumCells.length; j < count1; j++) {
		                    if ($(sumCells[j]).data('headKey') == header.keyName) {
		                        if (header.textAlign != null) 
		                        	textAlign = header.textAlign;
		                        if (header.sumColRenderFunc) {
		                            //this.owner为grid,this为当前的header，this,sumCells[j]为合计单元格
		                            header.sumColRenderFunc.call(window, this.owner, this, $(sumCells[j]), sum);
		                        } else {
		                            if (isNaN(sum)) 
		                            	$(sumCells[i]).html(sum);
		                            else {
		                                var colorSum = $.maskerutil.toColorfulString($.numbermasker.getObj(window.$maskerMeta.NumberFormatMeta).format(sum));
		                                $(sumCells[j]).html(colorSum);
		                            }
		                        }
		                    }
		                }
		            }
		        }
		    } else {
		        // 如果此列是合计行,则向ds取合计列数据
		        if (this.basicHeaders[gridColIndex].isSumCol) {
		            var header = this.basicHeaders[gridColIndex];
		            var sum = this.dataset.totalSum([dsColName], null, null, header.precision);
		            var sumCells = this.owner.dynSumPageDataDiv.children();
		            // 格式化结果数据
		            sum = GridComp.parseData(header, sum, true);
		            for (var i = 0, count = sumCells.length; i < count; i++) {
		                if ($(sumCells[i]).data('headKey') == header.keyName) {
		                    if (header.textAlign != null) 
		                    	textAlign = header.textAlign;
		                    if (header.sumColRenderFunc) {
		                        //this为当前的header，this,sumCells[j]为合计单元格
		                        header.sumColRenderFunc.call(window, this.owner, this, $(sumCells[i]), sum);
		                    } else {
		                        if (isNaN(sum)) 
		                        	$(sumCells[i]).html(sum);
		                        else {
		                            var colorSum = $.maskerutil.toColorfulString($.numbermasker.getObj(window.$maskerMeta.NumberFormatMeta).format(sum));
		                            $(sumCells[i]).html(colorSum);
		                        }
		                    }
		                }
		            }
		        }
		    }
		},
		/**
		 * 将指定header加入Model的headers数组
		 * @param header 为GridHeader的实例
		 */
		addHeader : function(header) {
		    if (this.headers == null) 
		    	this.headers = [];
		    this.headers.push(header);
		},
		/**
		 * 设置model中的数据 不通过dataSet设置数据时使用
		 */
		setRows:function(rows) {
		    this.initBasicHeaders();
		    this.rows = rows;
		},
		/**
		 * 设置dataSet，此model适配dataset 注意：setDataSet之前必须保证this.headers已经初始化
		 */
		 setDataSet : function(dataset) {
		    if (this.headers == null) {
		    	$.pageutils.showErrorDialog("You must init headers before setDataSet!");
		        return;
		    }
		    // 此model所适配的dataset
		    this.dataset = dataset;
		    // 解析出grid列和dataSet的绑定关系
		    this.bindRelation = [];
		    // 初始化basicHeaders
		    this.initBasicHeaders();
		    for (var i = 0, count = this.basicHeaders.length; i < count; i++) {
		        var index = dataset.nameToIndex(this.basicHeaders[i].keyName);
		        this.bindRelation.push(index);
		        //设置精度
		        var fieldList = dataset.fieldList[index];
		        if (fieldList && fieldList.precision != null) 
		        	this.basicHeaders[i].setPrecision(fieldList.precision, true);
		    }
		    dataset.bindComponent(this);
		
		    // 将model的rows置为null,需要重新取新数据
		    this.rows = null;
		    this.initUIRows();
		},
		/**
		 * 获取页数
		 */
		getPageCount: function() {
		    var pageInfo = this.dataset.getPageCount();
		    return pageInfo;
		},
		/**
		 * 根据headers初始化basicHeaders
		 */
		initBasicHeaders : function() {
		    var header = null;
		    var basics = null;
		    this.basicHeaders = [];
		    for (var i = 0, count = this.headers.length; i < count; i++) {
		        header = this.headers[i];
		        if (header.children == null) 
		        	this.basicHeaders.push(header);
		        else {
		            // 得到basicHeader
		            basics = header.getBasicHeaders();
		            // 将包含真正有用信息的header放入顶层header的basicHeaders数组中
		            header.basicHeaders = basics;
		            for (var j = 0; j < basics.length; j++) {
		                // 将header下包含真正有用信息的header放入basicHeaders中
		                this.basicHeaders.push(basics[j]);
		                // 保存此header的顶层header
		                basics[j].topHeader = header;
		            }
		        }
		    }
		},
		/**
		 * 得到basicHeaders
		 */
		getBasicHeaders : function() {
		    return this.basicHeaders;
		},
		/**
		 * 得到模型中数据行数，如果没有数据返回0
		 */
		getRowsCount : function() {
		    if (this.dataset == null) 
		    	return 0;
		    else 
		    	return this.rows == null ? 0 : this.rows.length;
		},
		/**
		 * 得到新增行数据
		 * @return rows GridRow数组
		 */
		getNewAddedRows:function() {
		    if (this.dataset != null) {
		        var newAdded = this.dataset.getNewAddedRows();
		        if (newAdded != null && newAdded.length > 0) {
		            var rows = new Array(newAdded.length);
		            for (var i = 0, count = newAdded.length; i < count; i++) {
		                // 对dataset中的行进行适配包装
		                var row = $.gridrow.getObj();
		                row.pageData = newAdded[i];
		                rows[i] = row;
		            }
		            return rows;
		        } else 
		        	return null;
		    }
		    return null;
		},
		/**
		 * 得到更新行数据
		 * @return 更新行数组
		 */
		getUpdatedRows : function() {
		    if (this.dataset != null) {
		        var updateRows = this.dataset.getUpdatedRows();
		        if (updateRows != null && updateRows.length > 0) {
		            var rows = new Array(updateRows.length);
		            for (var i = 0, count = updateRows.length; i < count; i++) {
		                // 对dataset中的行进行适配包装
		                var row = $.gridrow.getObj();
		                row.pageData = updateRows[i];
		                rows.push(row);
		            }
		            return rows;
		        } else 
		        	return null;
		    }
		    return null;
		},
		/**
		 * 删除model一行
		 * @param index 删除行索引
		 */
		deleteRow : function(index) {
		    if (this.dataset != null) 
		    	this.dataset.deleteRows([index]);
		},
		/**
		 * 删除model中的多行
		 * @param indice 删除行索引数组
		 */
		deleteRows: function(indice) {
		    if (this.dataset != null) 
		    	this.dataset.deleteRows(indice);
		},
		/**
		 * 得到model中删除的行
		 */
		getDeletedRows : function() {
		    if (this.dataset != null) 
		    	return this.dataset.getDeletedRows();
		},
		/**
		 * 得到选中行
		 */
		getSelectedRows : function() {
		    if (this.dataset != null) {
		        // var selectedRows = this.dataset.getSelectedRows(null,
		        // this.rowFilter);
		        var selectedRowsIndice = this.owner.selectedRowIndice, selectedRows = [];
		        if (selectedRowsIndice != null && selectedRowsIndice.length > 0) {
		            for (var i = 0; i < selectedRowsIndice.length; i++) {
		                selectedRows.push(this.rows[selectedRowsIndice[i]]);
		            }
		        }
		
		        if (selectedRows != null && selectedRows.length > 0) {
		            var rows = [];
		            for (var i = 0, count = selectedRows.length; i < count; i++) {
		                // 对dataset中的行进行适配包装
		                var row = $.gridrow.getObj();
		                row.setPageData(selectedRows[i].getPageData());
		                row.setBindRelation(selectedRows[i].relation);
		                rows.push(row);
		            }
		            return rows;
		        } else 
		        	return null;
		    }
		    return null;
		},
		/**
		 * 判断行是否选中
		 */
		isRowSelected : function(index) {
		    // 根据dataset上的选中状态来判断
		    var selRows = this.dataset.getSelectedRows();
		    if (selRows != null && selRows.length > 0) {
		        for (var i = 0, count = selRows.length; i < count; i++) {
		            var rowIndex = this.getUIRowIndex(selRows[i]);
		            if (rowIndex == index) 
		            	return true;
		        }
		    }
		    return false;
		},
		/**
		 * 设置当前聚焦行（鼠标点中行）
		 */
		setFocusIndex:function(index) {
		    if (this.dataset != null) {
		        if (index == -1) {
		            this.dataset.setFocusRowIndex( - 1);
		        } else {
		            var gridRow = this.getRow(index);
		            if (gridRow != null) {
		                var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		                this.dataset.setFocusRowIndex(realIndex);
		            }
		        }
		    }
		},
		/**
		 * 设置行选中
		 * @param index UI上的选中行索引
		 */
		setRowSelected : function(index) {
		    if (this.dataset != null) {
		        var gridRow = this.getRow(index);
		        if (gridRow != null) {
		            var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		            this.dataset.setRowSelected(realIndex);
		        }
		    }
		},
		/**
		 * 设置多行选中
		 * @param index UI上的选中行索引
		 */
		addRowSelected : function(indices) {
		    if (this.dataset != null) {
		        if (indices instanceof Array) {
		            var realIndices = [];
		            for (var i = 0; i < indices.length; i++) {
		                var index = indices[i];
		                var gridRow = this.getRow(index);
		                var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		                realIndices.push(realIndex);
		            }
		            this.dataset.addRowSelected(realIndices);
		        } else {
		            var gridRow = this.getRow(indices);
		            var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		            this.dataset.addRowSelected(realIndex);
		        }
		    }
		},
		/**
		 * 设置行反选
		 */
		setRowUnSelected : function(indices) {
		    if (this.dataset != null) {
		        if (indices instanceof Array) {
		            var realIndices = [];
		            for (var i = 0; i < indices.length; i++) {
		                var gridRow = this.getRow(indices[i]);
		                var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		                realIndices.push(realIndex);
		            }
		            this.dataset.setRowUnSelected(realIndices);
		        } else {
		            var gridRow = this.getRow(indices);
		            var realIndex = this.dataset.getRowIndex(gridRow.pageData);
		            this.dataset.setRowUnSelected(realIndex);
		        }
		    }
		},
		/**
		 * 在model指定位置插入数据行。model插入数据后会通知grid控件。
		 * @param row 所需插入行的数据
		 * @param index 当前插入的位置.此位置将被参数的行代替,此行后的所有行将移动.
		 */
		insertRow :function(row, index) {
		    if (this.dataset != null) {
		        var newRow = $.gridrow.getObj();
		        var headers = this.basicHeaders;
		        if (row == null) {
		            var dsRow = this.dataset.insertEmptyRow(index);
		            newRow.pageData = dsRow;
		            newRow.relation = this.bindRelation;
		        } else {
		            var dsRow = $.datasetrow.getObj();
		            for (var i = 0, count = headers.length; i < count; i++) 
		            	dsRow.setCellValue(this.bindRelation[i], row.getCellValue(i));
		            this.dataset.insertRow(index, dsRow);
		            newRow.pageData = dsRow;
		            newRow.relation = this.bindRelation;
		        }
		        return newRow;
		    }
		},
		/**
		 * 在model最后插入数据行。model插入数据后会通知grid控件。
		 * @param row 所需插入行的数据
		 */
		addRow : function(row) {
		    var ds = this.dataset;
		    if (this.basicHeaders == null || this.basicHeaders.length == 0) {
		    	$.pageutils.showErrorDialog("basicHeaders为null!");
		        return;
		    }
		    var headers = this.basicHeaders;
		    // 调用dataset的方法插入数据
		    if (this.dataset != null) {
		        var newRow = $.gridrow.getObj();
		        if (row == null) {
		            // 在dataset中插入新行
		            var dsRow = this.dataset.addEmptyRow();
		            // 用gridrow包装dsRow返回(默认值的设置已由webdataset完成)
		            newRow.pageData = dsRow;
		            newRow.relation = this.bindRelation;
		        } else {
		            if (!$.gridrow.prototype.isPrototypeOf(row)) {
		            	$.pageutils.showErrorDialog("the parameter 'row' must be the instance of gridrow");
		                return;
		            }
		            var dsRow = $.datasetrow.getObj();
		            for (var i = 0, count = headers.length; i < count; i++) 
		            	dsRow.setCellValue(this.bindRelation[i], row.getCellValue(i));
		            newRow.pageData = dsRow;
		            newRow.relation = this.bindRelation;
		        }
		        return newRow;
		    }
		},
		/**
		 * 设置grid的编辑属性
		 * @param isEditable  设置grid可否编辑
		 */
		setEditable : function(isEditable) {
		    this.owner.setEditable(isEditable);
		},
		/**
		 * 销毁model
		 */
		destroySelf : function() {
		    this.bindRelation = null;
		    this.dataset = null;
		    this.rows = null;
		    this.headers = null;
		    this.owner = null;
//		    this.splice(0, this.length);
		},
		/**
		 * 获取选中所有行索引
		 */
		getSelectedIndices : function() {
		    if (this.owner != null) 
		    	return this.owner.selectedRowIndice;
		},
		/**
		 * 获取选中行索引
		 */
		getSelectedIndex :function() {
		    var indices = this.getSelectedIndices();
		    if (indices == null || indices.length == 0) 
		    	return - 1;
		    return indices[0];
		},
		/**
		 * 获取当前聚焦行（鼠标点中行）
		 */
		getFocusIndex : function() {
		    if (this.dataset != null) 
		    	return this.dataset.getFocusRowIndex();
		},
		/**
		 * treeLevel 树表相关方法
		 */
		setTreeLevel : function(level) {
		    this.treeLevel = level;
		},
		hasChildren : function(ds, row) {
		    if (!this.treeLevel) 
		    	return false;
		    var ppkField = this.treeLevel.recursivePKeyField;
		    var parentIndex = ds.nameToIndex(ppkField);
		
		    var pkField = this.treeLevel.recursiveKeyField;
		    var index = ds.nameToIndex(pkField);
		
		    var rows = this.dataset.getRows();
		    if (rows == null) 
		    	return false;
		    var rowArr = [];
		
		    var pk_parent = row.getCellValue(index);
		    for (var i = 0; i < rows.length; i++) {
		        var ppkValue = rows[i].getCellValue(parentIndex);
		        if (ppkValue == pk_parent) 
		        	return true;
		    }
		    return false;
		},
		getParentKey : function(gridRow) {
		    if (!this.treeLevel) 
		    	return null;
		    var ppkField = this.treeLevel.recursivePKeyField;
		    return gridRow.getCellValueByFieldName(ppkField);
		},
		getChildrenCount : function(parentRowIndex) {
		    var ppkField = this.treeLevel.recursivePKeyField;
		    var pkField = this.treeLevel.recursiveKeyField;
		    var pGridRow = this.getRow(parentRowIndex);
		    var parentKey = pGridRow.getCellValueByFieldName(pkField);
		    var count = 0;
		    for (var i = 0, n = this.rows.length; i < n; i++) {
		        if (this.rows[i].getCellValueByFieldName(ppkField) == parentKey) {
		            count++;
		            var count2 = 0;
		            count2 = this.getChildrenCount(i);
		            count = count + count2;
		        }
		    };
		    return count;
		},
		filterRows :function(ds, key) {
		    if (this.treeLevel == null) {
		        alert("不是树表，不支持此方法");
		        return;
		    }
		    if (key == null) 
		    	key = "";
		    var ppkField = this.treeLevel.recursivePKeyField;
		    var index = ds.nameToIndex(ppkField);
		    //主健
		    var pkField = this.treeLevel.recursiveKeyField;
		    var pkIndex = ds.nameToIndex(pkField);
		    var rows = this.dataset.getRows();
		    if (rows == null) 
		    	return null;
		    //所有的pk的Map
		    var allPks = [];
		    for (var i = 0; i < rows.length; i++) {
		        var pkValue = rows[i].getCellValue(pkIndex);
		        allPks.push(pkValue);
		    }
		    //如果某行的父不在页面上，则将此行的父置成null
		    for (var i = 0; i < rows.length; i++) {
		        var ppkValue = rows[i].getCellValue(index);
		        if (ppkValue == null) 
		        	continue;
		        if (allPks.indexOf(ppkValue) == -1) {
		            rows[i].setCellValue(index, null);
		        }
		    }
		
		    var rowArr = [];
		    for (var i = 0; i < rows.length; i++) {
		        var ppkValue = rows[i].getCellValue(index);
		        if (ppkValue == null) 
		        	ppkValue = "";
		        if (key == ppkValue) {
		            rowArr.push(rows[i]);
		        }
		    }
		    return rowArr;
		},
		/**
		 * 得到所有的父数组
		 * @param {} ds
		 * @param {} key
		 * @return {}
		 */
		getParentKeys : function(ds, key) {
		    var parentKeys = [];
		    var parentKey = this.getParentKeyFromDs(ds, key);
		    while (parentKey != null) {
		        parentKeys.splice(0, 0, parentKey);
		        parentKey = this.getParentKeyFromDs(ds, parentKey);
		    }
		    return parentKeys;
		},
		getParentKeyFromDs : function(ds, key) {
		    var ppkField = this.treeLevel.recursivePKeyField;
		    var parentIndex = ds.nameToIndex(ppkField);
		    //主健
		    var pkField = this.treeLevel.recursiveKeyField;
		    var pkIndex = ds.nameToIndex(pkField);
		    var rows = this.dataset.getRows();
		    if (rows == null) 
		    	return null;
		    for (var i = 0; i < rows.length; i++) {
		        var pkValue = rows[i].getCellValue(pkIndex);
		        if (pkValue == key) 
		        	return rows[i].getCellValue(parentIndex);
		    }
		    return null;
		}
	});
	
	$.extend($.gridcompmodel,{
		/**
		 * 默认排序
		 */
		defaultSortRows:function(row1, row2) {
		    var index = $.gridcompmodel.defaultSortRows.index;
		    var ascending = $.gridcompmodel.defaultSortRows.ascending;
		    //默认排序改为按拼音排
		    var value1 = row1.getCellValue(index) || "";
		    var value2 = row2.getCellValue(index) || "";
		    return value1.localeCompare(value2);
		
		},
		/**
		 * 按整数列排序行(升序)
		 */
		sortRowsByIntergerColum:function (row1, row2) {
		    var index = $.gridcompmodel.sortRowsByIntergerColum.index;
		    var ascending = $.gridcompmodel.sortRowsByIntergerColum.ascending;
		    if (parseInt(row1.getCellValue(index)) < parseInt(row2.getCellValue(index))) 
		    	return ascending == -1 ? -1 : 1;
		    else if (parseInt(row1.getCellValue(index)) > parseInt(row2.getCellValue(index))) 
		    	return ascending == -1 ? 1 : -1;
		    else 
		    	return 0;
		},
		/**
		 * 按浮点数列排序行(升序)
		 */
		sortRowsByDecimalColum:function (row1, row2) {
		    var index = $.gridcompmodel.sortRowsByDecimalColum.index;
		    var ascending = $.gridcompmodel.sortRowsByDecimalColum.ascending;
		    if (parseFloat(row1.getCellValue(index)) < parseFloat(row2.getCellValue(index))) 
		    	return (ascending == -1) ? -1 : 1;
		    else if (parseFloat(row1.getCellValue(index)) > parseFloat(row2.getCellValue(index))) 
		    	return (ascending == -1) ? 1 : -1;
		    else 
		    	return 0;
		}		
	} );
})(jQuery)
