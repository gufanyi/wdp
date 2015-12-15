(function($){
	/**
	 * 数据集Relation客户端定义
	 * @class 数据集Relation客户端定义
	 * @constructor
	 * @param id relation Id
	 * @param masterDataset 主表ID
	 * @param masterKeyFields 主表关联keys
	 * @param detailDataset 子表ID
	 * @param detailForeignkeys 子表外键
	 *
	 * @author lxl
	 */
	$.dsrelation = function(opts) {
		opts = opts || {};
	    this.id = opts.id;
	    this.masterDataset = opts.masterDataset;
	    this.masterKeyField = opts.masterKeyField;
	    this.detailDataset = opts.detailDataset;
	    this.detailForeignkey = opts.detailForeignkey;
	};
	$.dsrelation.getObj = function(opts) {
		return new $.dsrelation(opts);
	};
	
	/**
	 * Dataset关联关系
	 * @class 当前页面所有数据集Relation的包装，每个页面有唯一实例。通过它可方便的获取查询相应的Relation。
	 * @constructor
	 * @author dengjt
	 */
	$.dsrelations = function() {
	    //关系对照表，key为master dataset id, value 为relation
	    this.relationMap = $.hashmap.getObj();
	};
	$.dsrelations.getObj = function() {
		return new $.dsrelations();
	};
	$.extend($.dsrelations.prototype,{
		/**
		 * 向页面Relations中添加一个Relation。此方法一般只被Web Frame调用
		 *
		 * @param relation 数据集Relation
		 */
		addRelation : function(relation) {
		    var relationArr;
		    if ((relationArr = this.relationMap.get(relation.masterDataset)) == null) {
		        relationArr = [];
		        this.relationMap.put(relation.masterDataset, relationArr);
		    }
		    relationArr.push(relation);
		},
		/**
		 * 根据relation id删除相应的relation。 <b>注意，必须保证所有的relation id不重复</b>
		 *
		 * @param id
		 */
		removeRelation : function(id) {
		    var masterArr = this.relationMap.values();
		    for (var i = 0; i < masterArr.length; i++) {
		        for (var j = 0; j < masterArr[i].length; j++) {
		            if (masterArr[i][j].id == id) break;
		        }
		    }
		},
		/**
		 * 根据主Dataset获取relation
		 */
		getRelationsByMasterDataset : function(masterDsId) {
		    return this.relationMap.get(masterDsId);
		},
		/**
		 * 根据关联Dataset获取relation
		 */
		getRelationsBySlaveDataset : function(slaveDsId) {
		    var relArr = null;
		    var relations = this.relationMap.values();
		    if (relations != null && relations.length > 0) {
		        for (var i = 0; i < relations.length; i++) {
		            for (var j = 0; j < relations[i].length; j++) {
		                var relation = relations[i][j];
		                if (relation.detailDataset == slaveDsId) {
		                    if (relArr == null) relArr = [];
		                    relArr.push(relation);
		                }
		            }
		        }
		    }
		    return relArr;
		},
		destroySelf : function() {
		    this.relationMap.clear();
		    this.relationMap = null;
		}
	});
	
	/**
	 * 聚合数据前台对应类
	 * @class 聚合数据前台对应类。可通过getComboData(id)获取。
	 * @constructor
	 * @param id 聚合数据id
	 * @author dengjt
	 */
	$.datalist = function(id) {
	    this.id = id;
	    this.dataItems = [];
	};
	$.datalist.getObj = function(id) {
		return new $.datalist(id);
	};
	$.extend($.datalist.prototype,{
		/**
		 * 获得值数组
		 *
		 * @return 值String数组
		 */
		getValueArray : function() {
		    if (this.dataItems.length == 0) return [];
		    var result = [];
		    for (var i = 0; i < this.dataItems.length; i++) result.push(this.dataItems[i].value);
		    return result;
		},
		/**
		 * 获得名称数组
		 *
		 * @return 名称String数组
		 */
		getNameArray : function() {
		    if (this.dataItems.length == 0) return [];
		    var result = [];
		    for (var i = 0; i < this.dataItems.length; i++) {
		        result.push(this.dataItems[i].i18nName);
		    }
		    return result;
		},
		/**
		 * 获得图片路径数组
		 *
		 * @return 名称String数组
		 */
		getImageArray : function() {
		    if (this.dataItems.length == 0) return [];
		    var result = [];
		    for (var i = 0; i < this.dataItems.length; i++) {
		        result.push(this.dataItems[i].image);
		    }
		    return result;
		},
		/**
		 * 根据值获得名称
		 */
		getNameByValue : function(value) {
		    if (this.dataItems.length == 0) return [];
		    for (var i = 0; i < this.dataItems.length; i++) {
		        if (this.dataItems[i].value == value) return this.dataItems[i].i18nName;
		    }
		    return null;
		},
		/**
		 * 根据名称获得值
		 */
		getValueByName : function(name) {
		    if (this.dataItems.length == 0) return [];
		    for (var i = 0; i < this.dataItems.length; i++) {
		        if (this.dataItems[i].i18nName == name) return this.dataItems[i].value;
		    }
		    return null;
		},
		/**
		 * 向ComboData中增加一个item,此方法应该只被web frame调用。除非手工构造一ComboData
		 *
		 * @param item ComboItem对象
		 */
		addItem : function(item) {
		    this.dataItems.push(item);
		}
	});
	
	/**
	 * 聚合数据项
	 * @class 聚合数据项
	 * @constructor ComboItem构造函数
	 * @param name 名称
	 * @param value 值
	 * @author dengjt
	 */
	$.dataitem = function(name, value, image) {
	    this.i18nName = name;
	    if (typeof(value) == "string") {
	        value = value.replace(/&#92;/g, "\\");
	    }
	    this.value = value;
	    this.image = image;
	};
	$.dataitem.getObj = function(name, value, image) {
		return new $.dataitem(name, value, image);
	};
	
	/**
	 * 参照信息
	 * @class 参照信息
	 */
	$.refnodeinfo = function(opts) {
	    opts = opts || {};
		this.id = opts.id;
	    this.name = opts.name;
	    this.pageMeta = opts.pageMeta;
	    this.readDs = opts.readDs;
	    this.writeDs = opts.writeDs;
	    this.readFields = opts.readFields;
	    this.writeFields = opts.writeFields;
	    this.filterSql = opts.filterSql;
	    this.userObj = opts.userObj;
	    this.multiSel = opts.multiSel;
	    this.usePower = opts.usePower;
	    this.selLeafOnly = opts.selLeafOnly;
	    //	this.refreshRefPage = $.argumentutils.getBoolean(refreshRefPage, false);
	    //	this.isDialog = $.argumentutils.getBoolean(isDialog, false);
	    // 是否允许存在参照之外的值
	    this.allowExtendValue = $.argumentutils.getBoolean(opts.allowExtendValue, false);
	    this.dialogWidth = opts.dialogWidth;
	    this.dialogHeight = opts.dialogHeight;
	    this.dataListener = opts.dataListener;
	    this.isRead = $.argumentutils.getBoolean(opts.isRead, false);
	    this.refType = opts.refType;
	    this.referenceList = [];
	};
	$.refnodeinfo.getObj = function(opts) {
		return new $.refnodeinfo(opts);
	};
	$.extend($.refnodeinfo.prototype,{
		/**
		 * 设置过滤SQL语句
		 */
		setFilterSql : function(filterSql) {
		    this.filterSql = filterSql;
		    for (var i = 0; i < this.referenceList.length; i++) {
		        this.referenceList[i].setFilterSql(filterSql);
		    }
		},
		/**
		 * 绑定参照
		 */
		bindReference : function(reference) {
		    if (this.referenceList.indexOf(reference) == -1) this.referenceList.push(reference);
		},
		setDialogWidth : function(dialogWidth) {
		    this.dialogWidth = dialogWidth;
		},
		//设置dataListener
		setDataListener : function(dataListener) {
		    this.dataListener = dataListener;
		},
		setDialogHight : function(dialogHeight) {
		    this.dialogHeight = dialogHeight;
		},
		setName : function(name) {
		    this.name = name;
		}
	});
	
	$.selfrefnodeinfo = function(opts) {
		opts = opts || {};
	    this.id = opts.id;
	    this.name = opts.text;
	    this.url = opts.url;
	    this.dialogWidth = opts.dialogWidth;
	    this.dialogHeight = opts.dialogHeight;
	    this.isRead = $.argumentutils.getBoolean(opts.isRead, false);
	    this.allowExtendValue = false;
	     this.referenceList = [];
	};
	$.selfrefnodeinfo.getObj = function(opts) {
		return new $.selfrefnodeinfo(opts);
	};
	$.extend($.selfrefnodeinfo.prototype,$.refnodeinfo.prototype,{
		setPath : function(path) {
		    this.url = path;
		},
		setName : function(name) {
		    this.name = name;
		}
	});
	
	/**
	 * 参照关联关系构造函数
	 * @class 参照关联关系类
	 * @return
	 */
	$.refnoderelations = function() {
	    this.relationMap = $.hashmap.getObj();
	};
	$.refnoderelations.getObj = function() {
		return new $.refnoderelations();
	};
	$.extend($.refnoderelations.prototype,{
		/**
		 * 向页面Relations中添加一个Relation。此方法一般只被Web Frame调用
		 *
		 * @param relation 数据集Relation
		 */
		addRelation : function(relation) {
		    this.relationMap.put(relation.id, relation);
		},
		/**
		 * 根据relation id删除相应的relation。 <b>注意，必须保证所有的relation id不重复</b>
		 *
		 * @param id
		 */
		removeRelation : function(id) {
		    if (this.relationMap.containsKey(id)) this.relationMap.remove(id);
		},
		/**
		 * 根据主字段ID获取relation
		 */
		getRelationsByMasterFieldId : function(masterFieldId, dsId) {
		    var relArr = [];
		    var relations = this.relationMap.values();
		    for (var i = 0,
		             n = relations.length; i < n; i++) {
		        var relation = relations[i];
		        var masterFields = relation.masterFieldInfos;
		        for (var j = 0,
		                 m = masterFields.length; j < m; j++) {
		            var masterField = masterFields[j];
		            if (masterField.dsId == dsId && masterField.fieldId == masterFieldId) relArr.push(relation);
		        }
		    }
		    return relArr;
		},
		/**
		 * 根据关联字段ID获取relation
		 */
		getRelationBySlaveRefNode : function(slaveRefNode, dsId) {
		    var relations = this.relationMap.values();
		    if (relations != null && relations.length > 0) {
		        for (var i = 0,
		                 n = relations.length; i < n; i++) {
		            var relation = relations[i];
		            // TODO
		            if ((relation.targetDsId == null || relation.targetDsId == "null" || relation.targetDsId == dsId) && relation.detailRefNode == slaveRefNode) {
		                return relation;
		            }
		        }
		    }
		    return null;
		}
	});
	
	/**
	 * 参照关联关系
	 * @class 参照关联关系
	 * @param id
	 * @param masterFieldInfos ：主字段信息对象数组，对象对应后台类：MasterFieldInfo，包含内容：fieldId、filterSql、nullProcess
	 * @param masterKeyField
	 * @param detailRefNode
	 * @param targetDsId
	 * @param clearDetail
	 * @return
	 */
	$.refnoderelation = function(opts) {
		opts = opts || {};
	    this.id = opts.id;
	    this.masterFieldInfos = eval(opts.masterFieldInfos);
	    this.detailRefNode = opts.detailRefNode;
	    this.targetDsId = opts.targetDsId;
	    this.clearDetail = opts.clearDetail;
	};
	$.refnoderelation.getObj = function(opts) {
		return new $.refnoderelation(opts);
	};
	
	$.viewmodel = {
		/**
		 * 将RefNodeRelations绑定到对应的Dataset的事件上
		 *
		 * @return
		 */
		bindRefNode2Dataset : function() {
		    if (window.$refNodeRelations != null) {
		        var widget = window.$refNodeRelations.viewpart;
		        var relations = window.$refNodeRelations.relationMap.values();
		        // Dataset的onAfterDataChange事件的相关字段
		        var acceptFieldMap = $.hashmap.getObj();
		        for (var i = 0, n = relations.length; i < n; i++) {
		            var relation = relations[i];
		            for (var j = 0, m = relation.masterFieldInfos.length; j < m; j++) {
		                var dsId = relation.masterFieldInfos[j].dsId;
		                var arr = acceptFieldMap.get(dsId);
		                if (arr == null) {
		                    arr = [];
		                    acceptFieldMap.put(dsId, arr);
		                }
		                arr.push(relation.masterFieldInfos[j].fieldId);
		            }
		        }
		        // 为Dataset增加数据改变事件
		        var keys = acceptFieldMap.keySet();
		        for (var i = 0, n = keys.length; i < n; i++) {
		            var dsId = keys[i];
		            var ds = widget.getDataset(dsId);
		            ds.$temp_af = acceptFieldMap.get(dsId);
		            ds.element.on('onAfterDataChange',function(e,dataChangeData){
		            	var fields = this.$temp_af;
		                var colIndex = dataChangeData.cellColIndex;
		                var currentFieldName = this.fieldList[colIndex].key;
		                var find = false;
		                for (var i = 0,
		                         n = fields.length; i < n; i++) {
		                    if (fields[i] == currentFieldName) {
		                        find = true;
		                        break;
		                    }
		                }
		                if (!find) return;
		                $.viewmodel.doProcessFieldRelation(dataChangeData);
		            });
		        }
		    }
		},
		/**
		 * 执行字段关联操作
		 * @return
		 */
		doProcessFieldRelation : function(dataChangeEvent) {
		    var newValue = dataChangeEvent.currentValue;
		    var oldValue = dataChangeEvent.oldValue;
		    if (window.$refNodeRelations != null && newValue != oldValue) { // 数据改变
		        var colIndex = dataChangeEvent.cellColIndex;
		        var widget = window.$refNodeRelations.viewpart;
		        var fieldId = widget.getDataset(dataChangeEvent.datasetId).fieldList[colIndex].key;
		        var relations = window.$refNodeRelations.relationMap.values();
		        for (var i = 0, n = relations.length; i < n; i++) {
		            var relation = relations[i];
		            var masterFields = relation.masterFieldInfos;
		            for (var j = 0, m = masterFields.length; j < m; j++) {
		                var masterField = masterFields[j];
		                if (masterField.dsId == dataChangeEvent.datasetId && masterField.fieldId == fieldId) {
		                    // 设置绑定字段值为空
		                    var refNodeId = relation.detailRefNode;
		                    var refNode = widget.getRefNode(refNodeId);
		                    for (var k = 0, l = refNode.referenceList.length; k < l; k++) {
		                        refNode.referenceList[k].clearValue();
		                    }
		                }
		            }
		        }
		    }
		}
	};
})(jQuery);
