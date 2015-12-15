/**
 * tree控件
 * @auther liy
 * @version lui 1.0
 *  
 */
 var _TreeNode = {
	NOPAINT : 1,
	// 节点画了一?表示只画了框?
	PAINTHALF : 2,
	// 节点全部画完
	PAINTALL : 3,
	DEFAULT_ID : "DEFAULT_ID",
    IMAGE_WIDTH : 19,
    // 复选状态隐藏复选框
    hideCheckbox : true,
    oldShowHintNode : null
 };
 
(function($) {
	$.widget("lui.tree", $.lui.base, {
		options: {           
            id : '',
            left : '0',
            top : '0',
            width : '100%',
            height : '100%',
            position : 'absolute',
            // 是否有checkbox
		    withCheckBox : false,
		    rootOpen : true,
		    withRoot : true,
		    rootNode : null,
		    className : 'tree_div',
		    flowmode : false,
	        canEdit : false,
	        imgRender : null,
	        treeNodeTextMaxWidth : -1	    
			
		},
		_initParam : function() {
			this.componentType = "TREEVIEW",
			this.id = this.options.id;
			this.imgRender= this.options.imgRender = this.options.attr.imgRender;
			this.className = $.argumentutils.getString(this.options.className, "tree_div");
			this._super();
			this.NODEHEIGHT = 26;
			this.nodeInstance = 0;
			this.RELOADNODE_suffix = "LoadingNode";
			// 复选状态对应的图标 window.themePath + "/images/treeview/" + this.checkArray[0];
			this.checkArray = new Array("iconUncheckAll.gif", "iconCheckAll.gif",
										"iconCheckGray.gif", "iconUncheckDis.gif", "iconCheckDis.gif",
										"iconCheckDis.gif");
		    /**
			 * 树节点各个显示字段的默认分割?
			 */
		    this.DEFAULT_DELIMS = " ";
		    // 复选策?
			// 只设置自?
			this.CHECKBOXMODEL_SELF = 0;
			// 设置自己和子
			this.CHECKBOXMODEL_SELF_SUB = 1;
			// 设置自己和子和父
			this.CHECKBOXMODEL_SELF_SUB_PARENT = 2;
			
			// TreeViewComp.focusNode = null;
			this.SUBCHECK_MAP = $.hashmap.getObj();
			
			// 设置根节点不显示,则rootOpen设为true
			if (this.options.withRoot == false)this.options.rootOpen = true;
			
			// 表示此树是否可以拖拽
			this.isDragEnabled = false;
			// 表示此树是否为激活状?
			this.isTreeActive = true;
			// 临时无父节点的节点map,以父ID为key,以节点数组为value
			this.tmpNodeMap = $.hashmap.getObj();
			// 建立ID和node的映射关?
			this.idNodeMap = $.hashmap.getObj();
			// 记录导致当前树宽度出滚动条的节点
			this.resultScrollNodes = new Array();			
			// 设置默认复选策略
			this.checkBoxModel = 0;
			// 根节点默认显示?
			this.rootCaption = "ROOT";	
			this.focusNode = null;
			// 节点没画
			this.NOPAINT = 1;
			// 节点画了一?表示只画了框?
			this.PAINTHALF = 2;
			// 节点全部画完
			this.PAINTALL = 3;
            this.TreeNode = null;
            this.base = BaseComponent;
	        this.base(this.options.id, this.options.left, this.options.top, this.options.width, this.options.height);
	        if (this.options.attr != null) {
				this.canEdit = $.argumentutils.getBoolean(this.options.attr.canEdit, false);
				this.imgRender = this.options.attr.imgRender;
				this.flowmode = this.options.attr.flowmode;
				this.treeNodeTextMaxWidth = $.argumentutils.getInteger(this.options.attr.treeNodeTextMaxWidth,
						-1);
			}
		},
		_create : function() {
			this._initParam();
			var oThis = this;
			var jq_div_gen = this.element;
	        jq_div_gen.css({
						'top' : this.options.top + "px",
						left : this.options.left + "px",
						width : this.options.width,
						height :  this.options.height,
						position : this.options.position,
						overflow : "auto"
					}).addClass(this.className);
		
			this.Div_gen = jq_div_gen.get(0);	
			var jq_bgDiv = $("<div></div>").addClass("treebg_div");
			this.bgDiv = jq_bgDiv.get(0);
			jq_bgDiv.appendTo(jq_div_gen);
			// 创建树控件显示对?
			var jq_treeDiv_gen = $("<div></div>").addClass("treeview_div").css({'position':this.options.position});
			this.treeDiv_gen = jq_treeDiv_gen.get(0);
			jq_bgDiv.append(jq_treeDiv_gen);
			if (this.options.rootNode)
				this.addRoot(this.options.rootNode);
			
			// 捕获tree滚动事件,采用缓加载技术加载树节点
			this.Div_gen.onscroll = function(e) {
				e = EventUtil.getEvent();
				e.triggerObj = oThis;
				handleTreeScrollEvent(e);
				// 删除事件对象（用于清除依赖关系）
				// clearEvent(e);
				e.triggerObj = null;
//				clearEventSimply(e);
			};
		},
		/**
		 * 添加根节?
		 * @param rootNode
		 * 根节?
		 */
		addRoot : function(rootNode) {
			// 注意:this.options.rootNode始终代表"最?添加的rootNode
			this.options.rootNode = rootNode;
			this.options.rootNode.refTree = this;
			this.options.rootNode.root = true;
		},
		/**
		 * 画显示区域的树节?
		 * @private
		 */
		paintZone : function() {
			var oThis = this;
			// 如果根节点没有画首先画根节点
			if (this.options.rootNode.painted == this.NOPAINT) {
				// 根结点首先画?
				this.options.rootNode.create1();
				this.options.rootNode.create2(0);
		
				if (this.options.withRoot == false)
					// this.options.rootNode.Div_gen.removeChild(this.options.rootNode.divRow);
					this.options.rootNode.wholeTableCell.removeChild(this.options.rootNode.divRow);
				else {
					var row = this.options.rootNode.divRowTable.firstChild.firstChild;
					row.deleteCell(this.options.rootNode.divRowTable.firstChild.firstChild.cells[0]);
					// this.options.rootNode.divContent.style.display = 'block';
					// 设置根节点不显示,则rootOpen设为true
					if (this.options.withRoot == false)
						this.options.rootNode.open = true;
				}
				this.treeDiv_gen.appendChild(this.options.rootNode.Div_gen);
			}
		
			if (this.options.rootNode.open && this.options.rootNode.childrenTreeNodes.length > 0) {
				// 记录画得节点?
				this.nodeCount = 0;
				this.paintNode(this.options.rootNode.childrenTreeNodes, $(this.Div_gen).scrollTop(),
						$(this.Div_gen).outerWidth(), 1);
			}
			// 计算所有打开的节点的个数
			var openedNodeCount = this.calculateOpenedNodesCount();
			// 记录树的高度(避免在树显示出来后又hidden?得到的offsetHeight?的问?
			this.constant_treeHeight = this.NODEHEIGHT * openedNodeCount;
			if (!this.options.flowmode)
				this.treeDiv_gen.style.height = this.constant_treeHeight + "px";
		
			// guoweic: add 修正多出横向滚动条问?start 2009-11-4
//			if (IS_IE6 || IS_IE7) {
//				if (!this.isAdjusted) { // 只第一次页面加载时进行调整
//					// if (this.Div_gen.offsetWidth != this.Div_gen.clientWidth) { //
//					// 出现竖直滚动?
//					if ($.measures.isDivVScroll(this.Div_gen)) { // 出现竖直滚动?
//						this.bgDiv.style.width = this.Div_gen.scrollWidth
//								- BaseComponent.SCROLLWIDTH + "px";
//						this.isAdjusted = true;
//					}
//				}
//			}
			// guoweic: add end
		},
		/**
		 * 画显示区界面上的树节?采用缓画技术展现树节点
		 * 
		 * @private
		 */
		paintNode : function(nodeArr, divGenScrollTop,
				divGenOffsetHeight, levelNum) {
			for (var i = 0; i < nodeArr.length; i++) {
		
				// 如果此节点不是最后一级level,且是文件夹节?并且还没有画?则添?正在下载"子节?
				// 根据该节点下一级level的数量添加几?正在下载"子节?
				if (nodeArr[i].reload == true && !nodeArr[i].isLeaf
						&& nodeArr[i].painted == this.NOPAINT) {
					// 当前节点所处level的子levels
					var childrenLevel = nodeArr[i].level.childrenLevel;
					for (var j = 0; j < childrenLevel.length; j++) {
						// 创建挂于此节点下?加载节点"
						var loadNodeId = nodeArr[i].id + "_" + childrenLevel[j].id
								+ "_" + this.RELOADNODE_suffix;
						// 将上一级level的masterKeyFields放在加载节点的value属性中
						var value = nodeArr[i].level.masterKeyField;
						var attrArr = {};
						attrArr.open = true;
						attrArr.isLeaf = false;
						attrArr.reload = false;
						attrArr.withCheckBox = this.options.withCheckBox;
						attrArr.checkBoxModel = this.checkBoxModel;
						attrArr.canEdit = this.canEdit;
						attrArr.isBold = false;
						attrArr.textMaxWidth = this.treeNodeTextMaxWidth;
						//var reloadNode = new TreeNode(loadNodeId, trans("ml_loading"),value, attrArr, null, this.options.imgRender);
						
						var reloadNode = $("<div>").treenode({id:loadNodeId,caption:trans("ml_loading"),value:value,attr:attrArr,nodeData:null,imgRender:this.options.imgRender}).treenode("instance");
						// 此节点绑定父级level,ds和nodeData
						reloadNode.level = nodeArr[i].level;
						reloadNode.dataset = nodeArr[i].level.dataset;
						reloadNode.nodeData = nodeArr[i].nodeData;
						// 记录该加载节点加载哪个level的数?
						reloadNode.loadLevel = childrenLevel[j];
						reloadNode.treeViewComp = this;
						nodeArr[i].add(reloadNode);
					}
				}
		
				var currLevel = nodeArr[i].level;
				if (currLevel.loadField != null && currLevel.loadField != "") {
					if (nodeArr[i].loadLevel == null
							&& nodeArr[i].painted == this.NOPAINT) {
						var row = nodeArr[i].nodeData;
						var needLoad = row.getCellValue(currLevel.dataset
								.nameToIndex(currLevel.loadField));
						if (needLoad != null && needLoad == "1") {
							// 创建挂于此节点下?加载节点"
							var loadNodeId = nodeArr[i].id + "_" + currLevel.id + "_"
									+ this.RELOADNODE_suffix;
							// 将上一级level的masterKeyFields放在加载节点的value属性中
							var value = currLevel.masterKeyField;
							var attrArr = {};
							attrArr.open = true;
							attrArr.isLeaf = false;
							attrArr.reload = false;
							attrArr.withCheckBox = false;
							attrArr.checkBoxModel = this.checkBoxModel;
							attrArr.canEdit = this.canEdit;
							attrArr.isBold = false;
							attrArr.textMaxWidth = this.treeNodeTextMaxWidth;
							//var reloadNode = new TreeNode(loadNodeId,trans("ml_loading"), value, attrArr, null,this.options.imgRender);
							var reloadNode = $("<div>").treenode({id:loadNodeId,caption:trans("ml_loading"),value:value,attr:attrArr,nodeData:null,imgRender:this.options.imgRender}).treenode("instance");
						
							// 此节点绑定父级level,ds和nodeData
							reloadNode.level = currLevel;
							reloadNode.dataset = currLevel.dataset;
							reloadNode.nodeData = nodeArr[i].nodeData;
							// 记录该加载节点加载哪个level的数?
							reloadNode.loadLevel = currLevel;
							reloadNode.treeViewComp = this;
							nodeArr[i].add(reloadNode);
							nodeArr[i].reload = true;
						}
					}
				}
				if (nodeArr[i].painted == this.NOPAINT)
					nodeArr[i].create1();
		
				// 记录画了的节点数,便于定位当前节点的位?
				if (nodeArr[i].painted != this.NOPAINT)
					this.nodeCount++;
		
				var refTree = nodeArr[i].refTree;
				if (refTree.flowmode) {
					if (nodeArr[i].painted == this.PAINTHALF)
						nodeArr[i].create2(levelNum);
				} else {
					var nodeOffsetTop = this.nodeCount * this.NODEHEIGHT;
					//&& (nodeOffsetTop < divGenScrollTop + divGenOffsetHeight)
					if ((nodeOffsetTop >= divGenScrollTop - this.NODEHEIGHT * 2)) {
						if (nodeArr[i].painted == this.PAINTHALF)
							nodeArr[i].create2(levelNum);
					}
					if (nodeArr[i].nodeSelected && nodeArr[i].nodeSelected && nodeArr[i].divText) {
						nodeArr[i].focusNode();
					}
					//if (nodeOffsetTop > (divGenScrollTop + divGenOffsetHeight)) {
						//break;
					//}
				}
		
				// 文件夹节点若打开则去递归画它孩子
				if (nodeArr[i].open && nodeArr[i].childrenTreeNodes.length > 0)
					this.paintNode(nodeArr[i].childrenTreeNodes, divGenScrollTop,
							divGenOffsetHeight, levelNum + 1);
				// guoweic: add 处理firefox和IE8下divContent宽度不能自动扩展问题 start 2009-11-19
//				if (!IS_IE) {
					if (nodeArr[i].divContent.offsetWidth < (nodeArr[i].DIVT.offsetWidth + nodeArr[i].divChildren.offsetWidth)) {
						nodeArr[i].divContent.style.width = nodeArr[i].DIVT.offsetWidth
								+ nodeArr[i].divChildren.offsetWidth + "px";
					}
//				}
			}
		},
		/**
		 * 计算打开的树节点的数?用于改变树的高度
		 * @private
		 */
		calculateOpenedNodesCount : function() {
			this.count = 0;
			if (this.options.rootNode.open && this.options.rootNode.childrenTreeNodes.length > 0) {
				this.count++;
				return this.calculateCount(this.options.rootNode.childrenTreeNodes, this.count);
			} else
				return ++this.count;
		},		
		/**
		 * @private
		 */
		calculateCount : function(nodes, nodeCount) {
			this.count = nodeCount;
			for (var i = 0; i < nodes.length; i++) {
				this.count++;
				if (!nodes[i].isLeaf && nodes[i].open
						&& nodes[i].childrenTreeNodes.length > 0)
					this.calculateCount(nodes[i].childrenTreeNodes, this.count);
			}
			return this.count;
		},
		/**
		 * 计算当前节点的距root节点的真实高度
		 * @private
		 */
		calculateNodeRealHeight : function(node) {
			var nodeRVHeight = 0;
			while (node.parentTreeNode != null) {
				// 父节点打开
				if (node.parentTreeNode.open) {
					// 兄弟节点个数
					nodeRVHeight += (node.getPreVisibleSiblingCount() + 1)
							* this.NODEHEIGHT;
				} else
					nodeRVHeight += this.NODEHEIGHT;
				node = node.parentTreeNode;
			}
			return nodeRVHeight;
		},
		/**
		 * 把node添加到parentId指定的父节点?
		 * @param node
		 * 要添加的节点对象
		 * @param parantId
		 * 要添加到的节点的id
		 */
		addNode : function(node, parentId) {
			var added = false;
			// 往树上添加节点就是往根节点上添加节点
			if (parentId == this.id) {
				this.addRoot(node);
				added = true;
			}
			// 没有parentId即往根节点上增加
			else if (parentId == null || parentId == "") {
				this.options.rootNode.add(node);
				added = true;
			} else {
				// 查找parentId指定的节?
				var rsNode = this.idNodeMap.get(parentId);
				// 以下两种情况均要把子放到父的临时池中
				// 1.没有查到说明parent节点还没有初始化,把node节点先放到临时池?以parentId为key
				// 2.查到?但是父的父还没有初始?
				if (rsNode == null || rsNode.refTree == null) {
					var nodeArr = this.tmpNodeMap.get(parentId);
					if (nodeArr == null) {
						nodeArr = new Array();
						this.tmpNodeMap.put(parentId, nodeArr);
					}
					nodeArr.push(node);
				} else {
					rsNode.add(node);
					added = true;
				}
			}
			// 一旦node节点挂到parentId?得到池中以node为父节点的所有未挂的子节点列?
			if (added) {
				var cnodeList = this.tmpNodeMap.remove(node.id);
				if (cnodeList != null)
					this.addRelationNode(node, cnodeList);
			}
		},
		/**
		 * 处理孤儿节点
		 * @param pId
		 * 如果pId不为?则最终将孤儿节点均挂于该节点?否则挂于根结点下
		 * @private
		 */
		manageLeastNode : function(pNode) {
			var keyArr = this.tmpNodeMap.keySet();
			if (keyArr != null) {
				for (var i = 0; i < keyArr.length; i++) {
					var nodeArr = this.tmpNodeMap.remove(keyArr[i]);
					if (nodeArr != null) {
						for (var j = 0; j < nodeArr.length; j++) {
							var node = nodeArr[j];
							if (pNode != null)
								pNode.add(node);
							else {
								// 首先判断node的父节点是否已经画出,如果没有最终画出则将该节点挂于根结点下
								var parentNode = this.idNodeMap.get(node.pNodeId);
								if (parentNode != null)
									this.addNode(node, node.pNodeId);
								else
									this.options.rootNode.add(node);
							}
							var cnodeList = this.tmpNodeMap.remove(node.id);
							if (cnodeList != null)
								this.addRelationNode(node, cnodeList);
						}
					}
				}
			}
		},
		/**
		 * 将给定的一组Node节点添加到父节点点
		 * @param pNode父节点
		 * @param cNodeList 要添加的子节点数?
		 * @private
		 */
		addRelationNode : function(pNode, cNodeList) {
			if (cNodeList != null) {
				for (var i = 0; i < cNodeList.length; i++) {
					// 通过.refTree属性判断此节点是否被父节点添加?没添加过才添?
					if (cNodeList[i].refTree == null)
						this.addNode(cNodeList[i], pNode.id);
				}
			}
		},
		/**
		 * 根据节点id查找节点
		 * @param nodeId 要查找的节点id
		 */
		getNodeById : function(nodeId) {
			return this.idNodeMap.get(nodeId);
		},
		/**
		 * 根据数据行的id查找节点
		 * @param rowId ds中row的id
		 */
		getNodeByRowId : function(rowId) {
			if (this.idNodeMap != null) {
				var nodes = this.idNodeMap.values();
				for (var i = 0; i < nodes.length; i++) {
					if (nodes[i].rowId == rowId)
						return nodes[i];
				}
			}
		},
		/**
		 * 得到下一个即将被选中的节点。在点击下一个节点时，如果用户没有阻止下一个节点选中，则可以通过该方法得到下一个将要选中的节?
		 */
		getNextSelectedNode : function() {
			return this.nextSelNode;
		},
		/**
		 * 得到当前选中的节点
		 */
		getSelectedNode : function() {
			return this.selNode;
		},
		/**
		 * 清空选中节点
		 */
		clearSelectedNode : function() {
			if (this.selNode != null && this.selNode.painted == this.PAINTALL)
				this.selNode.deSelNode();
		},
		/**
		 * 删除选中的节?会删除dataset中的数据)
		 */
		deleteSelectedNode : function() {
			var selNode = this.getSelectedNode();
			var index = selNode.level.dataset.getRowIndex(selNode.nodeData);
			selNode.level.dataset.deleteRow(index);
		},
		/**
		 * 让给定的树节点显示
		 * @param node 要显示出来的树节点
		 * @private
		 */
		letNodeVisible : function(node) {
			// 得到此节点的所有祖先节?
			var parents = new Array();
			var parentNode = node.parentTreeNode;
			while (parentNode != null) {
				parents.push(parentNode);
				parentNode = parentNode.parentTreeNode;
			}
			var sTop = 0;
			while (parents.length > 0) {
				var pNode = parents.pop();
				// var nodeRealHeight = this.calculateNodeRealHeight(pNode);
				// var scrollTop = this.Div_gen.scrollTop;
				// // 如果父节点不在显示区域且父节点和该节点的距离小于可见区域高度才滚动到显示区域
				// if(nodeRealHeight > scrollTop + this.Div_gen.offsetHeight &&
				// getPreVisibleSiblingCount)
				// this.Div_gen.scrollTop = nodeRealHeight - this.Div_gen.offsetHeight +
				// 2*this.NODEHEIGHT;
				// else if(nodeRealHeight < scrollTop)
				// this.Div_gen.scrollTop = nodeRealHeight - 2*this.NODEHEIGHT;
				if (pNode.open == false) {
					if (pNode.painted == this.PAINTALL) {
						pNode.toggle();
					} else {
						pNode.open = true;
					}
				}
			}
		
			var nodeRealHeight = this.calculateNodeRealHeight(node);
			var scrollTop = this.Div_gen.scrollTop;
			// guoweic: modify start 2009-11-2
			if (nodeRealHeight > scrollTop + this.Div_gen.offsetHeight)
				// this.Div_gen.scrollTop = nodeRealHeight - this.Div_gen.offsetHeight +
				// 4
				// * this.NODEHEIGHT;
				this.Div_gen.scrollTop = (nodeRealHeight - this.Div_gen.offsetHeight + 4
						* this.NODEHEIGHT)
						+ "px";
			else if (nodeRealHeight < scrollTop)
				// this.Div_gen.scrollTop = nodeRealHeight - 4 *
				// this.NODEHEIGHT;
				this.Div_gen.scrollTop = (nodeRealHeight - 4 * this.NODEHEIGHT)
						+ "px";
			// guoweic: modify end
		},
		/**
		 * 设定树节点的第几级打开
		 * @public 在树节点构件完成后可以调用此方法，如果构建的树没有root，调用此方法无效。root下第一级节点level参数?，依次类?
		 * @private
		 */
		openNodesByLevel : function(level) {
			if (this.options.rootNode == null)
				return;
		
			level = parseInt(level);
		
			// 获的所有的1级节?
			var levelOneNodes = null;
			if (this.options.withRoot) {
				// 根节点处于打开先关闭它
				if (this.options.rootNode.open == true)
					this.options.rootNode.toggle();
				levelOneNodes = [this.options.rootNode];
				this.openNodes(levelOneNodes, level);
			} else {
				levelOneNodes = this.options.rootNode.childrenTreeNodes;
				this.openNodes(levelOneNodes, level - 1);
			}
		},
		/**
		 * @private 递归打开指定级数的所有节点
		 */
		openNodes : function(childNodes, level) {
			if (level < 0)
				return;
			level--;
			for (var i = 0; i < childNodes.length; i++) {
				if (!childNodes[i].isLeafNode()) {
					childNodes[i].toggle();
					this.openNodes(childNodes[i].childrenTreeNodes, level);
				}
			}
		},
		/**
		 * 设置此树节点是否可以拖拽
		 * @param isDragEnabled true表示可以拖拽，否则表示不能拖?
		 */
		setDragEnabled : function(isDragEnabled) {
			this.isDragEnabled = $.argumentutils.getBoolean(isDragEnabled, false);
		},
		/**
		 * 设置此树节点复选模型
		 * @param checkBoxModeln 复选模型
		 */
		setCheckBoxModel : function(checkBoxModel) {
			this.checkBoxModel = parseInt(checkBoxModel);
		},
		/**
		 * 为ds的一致性添加的方法，ds会掉用所有控件的此方法，但目前树本身并没?editable"
		 * @param isEditable
		 * @private
		 */
		setEditable : function(isEditable) {
			isEditable = true;
			$(".tree_mask").remove();
			$(".treenode_text").removeClass("tree_node_disable");
			if(!isEditable) {
				$(".treenode_text").addClass("tree_node_disable");
				$("<div class='tree_mask'>").css({
					'z-index' : $.measures.getZIndex()
				}).appendTo(this.element.parent());
			}
		},
		setVisible : function(isVisible) {
			if(isVisible) {
				this.element.show();
			} else {
				this.element.hide();
			}
		},
		/**
		 * 设置此树是否激活
		 * @param isActive true表示处于激活状?否则表示禁用状?
		 */
		setActive : function(isActive) {
			this.isTreeActive = $.argumentutils.getBoolean(isActive, true);
		},
		/**
		 * 得到树当前是否为激活状态
		 */
		isActive : function() {
			return this.isTreeActive;
		},
		/**
		 * 得到根节点 
		 * @return 树的根节点
		 */
		getRoot : function() {
			return this.options.rootNode;
		},
		/**
		 * 树的选中节点改变前调用此方法
		 * @param newNode 马上要选中的节点
		 * @param oldNode上一个选中的节点
		 * @private
		 */
		beforeSelNodeChange : function(newNode, oldNode) {
			this.onBeforeSelNodeChange(newNode, oldNode);
		},
		/**
		 * 鼠标点击的事件处?
		 * @private
		 */
		click : function(node) {
			this.onclick(node);
			this.onAfterSelNodeChange(node);
		},
		/**
		 * 鼠标双击的事件处理
		 * @private
		 */
		dblclick : function(node) {
			return this.ondbclick(node);
		},
		/**
		 * 根据dataset得到此dataset所属的level
		 * @param ds 当前的dataset
		 */
		getLevelByDataset : function(ds) {
			var levels = this.getAllLevels();
			for (var i = 0; i < levels.length; i++) {
				var levelArr = levels[i];
				for (var j = 0; j < levelArr.length; j++) {
					if (levelArr[j].dataset == ds)
						return levelArr[j];
				}
			}
			return null;
		},
		/**
		 * 得到所有的levels 注意:该结果是数组,数组每个元素是第几级的所有level的数
		 * @return 该树的所有levels
		 */
		getAllLevels : function() {
			// 得到顶层下的所有levels
			var levels = new Array();
			var topLevel = this.topTreeLevel;
			// 将最顶级level放入levels数组?
			levels.push([topLevel]);
			levels = this.getLevelRecursive(topLevel.childrenLevel, levels);
			return levels;
		},
		/**
		 * 得到递归树Level
		 */
		getLevelRecursive : function(nextLevels, levels) {
			if (nextLevels != null) {
				var tempCurLevels = [];
				var tempNextLevels = [];
				for (var i = 0, count = nextLevels.length; i < count; i++) {
					tempCurLevels.push(nextLevels[i]);
					if (nextLevels[i].childrenLevel != null
							&& nextLevels[i].childrenLevel.length > 0)
						tempNextLevels = nextLevels[i].childrenLevel;
				}
				if (tempCurLevels.length > 0) {
					levels.push(tempCurLevels);
					if (tempNextLevels.length > 0)
						this.getLevelRecursive(tempNextLevels, levels);
				}
			}
			return levels;
		},
		/**
		 * 判断给定level是否是最后一?
		 */
		isLastLevel : function(level) {
			var levels = this.getAllLevels();
			var lastLevels = levels[levels.length - 1];
			for (var i = 0; i < lastLevels.length; i++) {
				if (lastLevels[i] == level)
					return true;
				else
					return false;
			}
		},
		isFirstLevel : function(level) {
			var levels = this.getAllLevels();
			var firstLevels = levels[0];
			for (var i = 0; i < firstLevels.length; i++) {
				if (firstLevels[i] == level)
					return true;
				else
					return false;
			}
		},
		/**
		 * 设置dataset数据?
		 * @param dataset  数据集对?
		 */
		setDataset : function(dataset) {
			if (this.topTreeLevel == null)
				throw new Error("must call setTreeLevel() first!");
			// 如果不是最顶层dataset,设置isChild属性为true
			if (this.topTreeLevel.dataset != dataset)
				dataset.isChild = true;
		
			dataset.bindComponent(this);
			var level = this.getLevelByDataset(dataset);
			// 建立rootNode
			if (this.options.rootNode == null) {
				var attrArr = {};
				attrArr.open = this.options.rootOpen;
				attrArr.isLeaf = false;
				attrArr.reload = false;
				attrArr.withCheckBox = this.options.withCheckBox;
				attrArr.checkBoxModel = this.checkBoxModel;
				attrArr.canEdit = this.canEdit;
				attrArr.isBold = this.isFirstLevel(level);
				attrArr.textMaxWidth = this.treeNodeTextMaxWidth;
				//var rootNode = new TreeNode("$root", this.rootCaption, "root", attrArr,null, this.options.imgRender);
				var rootNode = $("<div>").treenode({id:"$root",caption:this.rootCaption,value:"root",attr:attrArr,nodeData:null,imgRender:this.options.imgRender}).treenode("instance");
						
				rootNode.treeViewComp = this;
				this.onRootNodeCreated(rootNode);
				this.addRoot(rootNode);
				this.idNodeMap.put(rootNode.id, rootNode);
			}
			this.createNodes(dataset.getRows(), dataset, level);
			this.manageLeastNode();
			// 树节点构建完成后向界面上画节?
			this.paintZone();
			// 根据dataset编辑属性设置tree的编辑属?
			if (dataset.isEditable() == false)
				this.setEditable(false);
		},
		/**
		 * 根据传入的rows创建节点
		 * @param rows 要创建的所有节点数?
		 * @param dataset  所属的ds
		 * @param currLevel  所属的level
		 * @param parentNodeId 不为null则当前创建的节点均挂于此节点?
		 * @private
		 */
		createNodes : function(rows, dataset, currLevel) {
			var nodeCount = (rows == null ? 0 : rows.length);
			if (nodeCount <= 0)
				return;
			// 此层是递归类型
			// if(currLevel != null && currLevel.type == "Recursive")
			// {
			var parentKeyFieldIndex = dataset.nameToIndex(currLevel.recursivePKeyField);
			var keyFieldIndex = dataset.nameToIndex(currLevel.masterKeyField);
			// 主键字段必须指定
			if (keyFieldIndex == -1) {
				alert("TreeLevel:" + currLevel.id + " must assign masterKeyField!");
				return;
			}
			for (var i = 0; i < nodeCount; i++) {
				var nodeData = rows[i];
				var nodeId = nodeData.getCellValue(keyFieldIndex);
				// 说明ds中增加的是一行空?此时以rowId为节点id
				if (nodeId == null || nodeId == "")
					nodeId = nodeData.rowId;
		
				var pNodeId = null;
				if (parentKeyFieldIndex != -1) {
					pNodeId = nodeData.getCellValue(parentKeyFieldIndex);
				}
				var caption = this.generateCaptionByLabelFields(currLevel, nodeData), value = caption;
				// 树节点加上level标示,只要同一level内的节点不重复即?
				var realNodeId = nodeId + "_" + currLevel.id;
				var realpNodeId = null;
				// 父ID为null,说明该节点并不挂于currLevel的任何节点下,此时该节点肯定挂于上级level?如果无法在上级level中找到该节点则挂于选中节点?
				// 如果父ID为本身ID 将其当做父ID为空处理
				if ((pNodeId == null || pNodeId == "" || pNodeId == nodeId)) {
					if (currLevel.parentLevel != null) {
						var fk = currLevel.detailKeyParameter;
						// 当前层的外键是不是ds中的真实字段
						var detailKeyParameterIndex = dataset.nameToIndex(fk);
						// 当前层节点的外键字段,该字段必须是ds中的真实存在的字?
						if (fk != null && detailKeyParameterIndex != -1) {
							var fkValue = nodeData
									.getCellValue(detailKeyParameterIndex);
							var parentNode = this.idNodeMap.get(fkValue + "_"
									+ currLevel.parentLevel.id);
							if (parentNode != null)
								realpNodeId = parentNode.id;
							else {
								if (this.selNode != null)
									realpNodeId = this.selNode.id;
							}
						} else {
							if (this.selNode != null)
								realpNodeId = this.selNode.id;
						}
					}
				} else {
					realpNodeId = pNodeId + "_" + currLevel.id;
				}
		
				// 给用户参与节点构建的机会(可以个性化树节点的创建)
				var node = this.nodeCreated(realNodeId, caption, value, realpNodeId,
						nodeData, dataset);
				if (node == null)
					continue;
				// node.level = currLevel;
				// 最后一级level中的树节点不用下?正在加载节点",其它各级均要下挂"正在加载节点"
				if (this.isLastLevel(currLevel))
					node.reload = false;
				else
					node.reload = true;
				if (node.isLeaf)
					node.open = false;
				// 重复id的节点不挂在树上
				if (this.idNodeMap.containsKey(realNodeId)) {
					// $.pageutils.showWarningDialog("nodeId:" + nodeId + ",caption:" +
					// this.idNodeMap.get(realNodeId).caption + " duplicated!");
				} else {
					this.idNodeMap.put(realNodeId, node);
					this.addNode(node, realpNodeId);
				}
			}
		},
		/**
		 * 根据labelFields字段数组生成节点caption
		 * @param{TreeeLevel} level
		 * @param nodeData ds中的一条数据
		 * @private
		 */
		generateCaptionByLabelFields : function(level, nodeData) {
			// 构成caption的各个显示字?
			var labelFields = level.labelFields;
			if (labelFields == null || labelFields == "")
				throw new Error("the argument level.labelField is null in method generateCaptionByLabelFields!");
		
			var labelDelims = level.labelDelims;
			var dataset = level.dataset;
			var tempLabel = "", caption = "";
			for (var i = 0; i < labelFields.length; i++) {
				var realLabelFiled = labelFields[i];
				tempLabel = nodeData.getCellValue(dataset.nameToIndex(realLabelFiled));
				if (!tempLabel) {
					var endIndex = realLabelFiled.substring(realLabelFiled.length - 1);
					if (1 < parseInt(endIndex) < 7) {
						realLabelFiled = realLabelFiled.substring(0,
								realLabelFiled.length - 1);
						tempLabel = nodeData.getCellValue(dataset
								.nameToIndex(realLabelFiled));
					}
				}
				if (!tempLabel) {
					tempLabel = "";
				}
				if (tempLabel != null) {
					if (labelDelims != null && labelDelims[i] != null)
						caption += tempLabel + labelDelims[i];
					else
						caption += tempLabel + this.DEFAULT_DELIMS;
				}
			}
			return caption;
		},
		/**
		 * 设置顶层TreeLevel
		 */
		setTreeLevel : function(topLevel) {
				this.topTreeLevel = topLevel;
				this.setDatasetByLevel(topLevel);
		},
		/**
		 * 根据Level设置Dataset
		 * @private
		 */
		setDatasetByLevel : function(level) {
			var ds = level.dataset;
			this.setDataset(ds);
			var cls = level.childrenLevel;
			if (cls != null && cls.length != 0) {
				for (var i = 0; i < cls.length; i++) {
					this.setDatasetByLevel(cls[i]);
				}
			}
		},
		/**
		 * Model发生变化时的回调函数
		 * @private
		 */
		onModelChanged : function(event, dataset) {
			// 行选中事件
			if (event.type == 'RowSelectEvent') {
				// 当前选中节点的dsRow
				var currNodeData = event.currentRow;
				// 没有数据，认为是跟节点选中
				if (currNodeData == null) {
					// 没有数据不选中根结?点击根结点时主动把根结点设置为选中(gd 2009-07-08)
					// this.options.rootNode.selNode();
					// this.click(this.options.rootNode);
					return;
				}
				// 根据节点的rowId来查找node
				var currNode = this.getNodeByRowId(currNodeData.rowId);
		
				if (currNode == null)
					return;
				// 选中节点改变之前都要调用用户的处理方?返回false则阻止选中节点改变
				if (currNode != this.selNode
						&& this.beforeSelNodeChange(currNode, this.selNode) == false)
					return;
				// 选中节点
				currNode.selNode();
		
				// 设置聚焦行
				// dataset.setFocusRowIndex(dataset.getRowIndexById(currNodeData.rowId));
				dataset.setFocusRowIndex(dataset.getCurrentPageData()
						.getRowIndexById(currNodeData.rowId));
				// dataset.setFocusRowIndex(currNode.index - 1);
				// 选中节点的checkbox
				currNode.setChecked(true);
				// 正式处理节点点击的动?
				this.click(currNode);
			}
			// 行反选事?
			else if (event.type == 'RowUnSelectEvent') {
				var rowIndex = event.currentRowIndex;
				var unSeleRow = dataset.getRow(rowIndex);
				if(unSeleRow) {
					var currNode = this.getNodeByRowId(unSeleRow.rowId);
					if (currNode != null) {
						// 不选中节点的checkbox
						currNode.setChecked(false);
						currNode.deSelNode();
					}
				}
				// this.click(currNode);
			}
			// 行插入事?
			else if (event.type == 'RowInsertEvent') {
				var nodeDatas = event.insertedRows;
				var level = this.getLevelByDataset(dataset);
				this.createNodes(nodeDatas, dataset, level);
				this.paintZone();
			}
			// 行删除事?
			else if (event.type == 'RowDeleteEvent') {
				// 要删除的节点数组
				var deleNodeDatas = event.deletedRows;
				if (deleNodeDatas != null && deleNodeDatas.length > 0) {
					var delNodeData = null;
					// 要删除的节点id
					var deleNodeId = null;
					// 要删除的节点
					var deleNode = null;
		
					for (var i = 0; i < deleNodeDatas.length; i++) {
						delNodeData = deleNodeDatas[i];
						deleNode = this.getNodeByRowId(delNodeData.rowId);
		
						if (deleNode != null) {
							deleNodeId = deleNode.id;
							if (deleNode.deleted != null && deleNode.deleted == true) {
								// 真正的删除此对象
								deleNode.parentTreeNode.remove(deleNodeId);
							} else {
								if (!event.deleteAll) {
									// 前序遍历该节点的所有孩?包括它自?
									var children = deleNode.preorderTraversal();
									// 先删除此节点的所有孩?
									if (children != null && children.length > 0) {
										var index = -1;
										// 如果此节点有孩子,则从idNodeMap中将所有孩子节点删?
										for (var i = 0; i < children.length; i++) {
											// 最后一个节点直接删?此节点为指定的要删除的节?
											if (i == children.length - 1)
												deleNode.parentTreeNode
														.remove(deleNodeId);
											else {
												index = children[i].level.dataset
														.getRowIndex(children[i].nodeData);
												if (index == -1)
													return;
												children[i].deleted = true;
												children[i].level.dataset
														.removeRow(index);
											}
										}
									}
								} else {
									var tempLevel = this.topTreeLevel;
									this.removeUINodesByLevel(tempLevel);
								}
								//当节点删除后，其父节点下无其它子节点，将图标移去
								if(deleNode.parentTreeNode && deleNode.parentTreeNode.getChildCount() <= 0){
									deleNode.parentTreeNode.setLeaf(true);
								}
							}
						}
					}
				}
			}
			// 列数据改变时调用此方?
			else if (event.type == 'DataChangeEvent') {
				var row = event.currentRow;
				// 改变值的?
				var cellColIndex = event.cellColIndex;
				// 旧?
				var oldValue = event.oldValue;
				// 新?
				var newValue = event.currentValue;
				// 要改变属性的节点
				var node = this.getNodeByRowId(row.rowId);
				if (node != null) {
					var level = node.level;
					var ds = node.getDataset();
					if (level.type == "Recursive") {
						var pIdIndex = ds.nameToIndex(level.recursivePKeyField);
						var idIndex = ds.nameToIndex(level.recursiveKeyField);
		
						// pId列改变了
						if (pIdIndex != null && cellColIndex == pIdIndex) {
							var pNode = null;
							// 如果 newValue为空，放到根节点?
							if (newValue == null || newValue == "") {
								pNode = this.options.rootNode;
								// if(level.parentLevel != null)
								// {
								// var fk = level.detailKeyParameter;
								// // 当前层的外键是不是ds中的真实字段
								// var detailKeyParameterIndex = ds.nameToIndex(fk);
								// // 当前层节点的外键字段,该字段必须是ds中的真实存在的字?
								// if(fk != null && detailKeyParameterIndex != -1)
								// {
								// var fkValue =
								// node.nodeData.getCellValue(detailKeyParameterIndex);
								// pNode = this.idNodeMap.get(fkValue + "_" +
								// level.parentLevel.id);
								// }
								// }
							} else
								pNode = this.idNodeMap.get(newValue + "_" + level.id);
							if (pNode == null) {
								ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex,
										oldValue, true);
								// $.pageutils.showErrorDialog("pId value changed to:" + newValue +
								// ",but no tree node reference to this id!(error
								// data)");
								return;
							}
		
							// 此节点要挂的父节点是他自?
							if (newValue != null && newValue != ""
									&& newValue == node.nodeData.getCellValue(idIndex)) {
								// $.pageutils.showErrorDialog("the node's parent id is itself!");
								ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex,
										oldValue, true);
								return;
							}
							// 此节点要挂的父节点是他的子节?
							if (this.checkParentNode(newValue, oldValue,
									idIndex, pIdIndex, node, ds) == false) {
								ds.setValueAt(ds.getRowIndex(node.nodeData), pIdIndex,
										oldValue, true);
								return;
							}
							if (newValue != oldValue) { // newValue !=
														// pNode.nodeData.getCellValue(idIndex)
								pNode.insertBefore(node);
								// 对于pId列改变的情况从界面remove时会删除idNodeMap中该节点的信?所以此处必须补?insertBefore方法会删除idNodeMap中的信息)
								if (this.idNodeMap.get(node.id) == null) {
									this.idNodeMap.put(node.id, node);
								}
		
								this.paintZone();
								// 让改变了父节点的node显示出来
								this.letNodeVisible(node);
								// 判断此node节点改变父节点前是否选中,如果选中则置于选中状?gd check 09-06
								var dsSelRows = ds.getSelectedRows();
								if (dsSelRows != null && dsSelRows.length > 0) {
									if (dsSelRows[0].rowId == node.nodeData.rowId)
										node.selNode();
								}
							}
						}
						// id列改变了
						else if (idIndex != null && cellColIndex == idIndex) {
							// oldValue为空说明是新增行
							if (oldValue == null)
								this.idNodeMap.remove(row.rowId + "_" + level.id);
							else
								this.idNodeMap.remove(oldValue + "_" + level.id);
							node.id = newValue + "_" + level.id;
							this.idNodeMap.put(node.id, node);
						}
					}
					// 判断是否组成显示值的某个字段改变
					var labelFields = level.labelFields;
					var isLabelChange = false;
					for (var i = 0; i < labelFields.length; i++) {
						// caption值改?
						if (cellColIndex == ds.nameToIndex(labelFields[i]))
							isLabelChange = true;
					}
					if (isLabelChange == true) {
						var newCaption = this.generateCaptionByLabelFields(level,
								node.nodeData);
						// 调用用户的方?
						var changedCaption = this.onBeforeNodeCaptionChange(
								labelFields, level.labelDelims, node);
						if (changedCaption != null)
							newCaption = changedCaption;
						if (node.divText) {
							node.divText.removeChild(node.divText.childNodes[0]);
							node.divText.appendChild(document
									.createTextNode(newCaption));
							node.divText.title = newCaption;
						}
		
						// 设置节点的caption?
						node.caption = newCaption;
						isLabelChange = false;
					}
				}
			} else if (event.type == 'PageChangeEvent') {
				// 获得当前?下载"节点(?下载"节点的父节点正在加载数据)
				var userObj = event.userObject;
				// 加载下一级节点时的处理逻辑(多级树的缓加载情况
				if (userObj != null && userObj.loadNode != null) {
					var node = userObj.loadNode;
					var pNode = node.parentTreeNode;
					// 获取正在加载节点加载的是哪个level的数据
					var level = node.loadLevel;
					var ds = level.dataset;
					var rows = ds.getRows(event.pageIndex);
					node.parentTreeNode.remove(node.id);
					this.createNodes(rows, ds, level);
					this.manageLeastNode(pNode);
					// 加载完数据后调整树的宽度
					var overWidth = pNode.getChildrenOverParentWidth();
					if (overWidth > 0)
						this.treeDiv_gen.style.width = this.treeDiv_gen.offsetWidth
								+ overWidth + "px";
					this.paintZone();
					if (pNode.prapareCheck == true) {
						pNode._setSubChecked(pNode._state, pNode._isRoot);
						pNode.prapareCheck = null;
					}
				}
				// 其他处理逻辑
				else if (userObj == null) {
					// 获取树有几级level
					if (this.levelCount == null)
						this.levelCount = this.getAllLevels().length;
					// 直接加载第一级树
					var ds = this.topTreeLevel.dataset;
					this.paintNodesByLevel(this.topTreeLevel, ds);
					// 当前选中节点设为null
					this.selNode = null;
					// 让选中行选中
					var selRows = dataset.getSelectedRows();
					if (selRows != null && selRows.length > 0) {
						var selNode = this.getNodeByRowId(selRows[0].rowId);
						if (selNode) {
							selNode.selNode();
						}
						// this.getNodeByRowId(selRows[0].rowId).selNode();
					}
		
				}
			}
			// Dataset Undo事件
			else if (event.type == 'DatasetUndoEvent') {
				this.paintNodesByLevel(this.getLevelByDataset(dataset), dataset);
				// 当前选中节点设为null
				this.selNode = null;
				// 让选中行选中
				var selRows = dataset.getSelectedRows();
				if (selRows != null && selRows.length > 0)
					this.getNodeByRowId(selRows[0].rowId).selNode();
			} else if (event.type == 'FocusChangeEvent') {
				// this.prapareFocusIndex 为解决二级树点击，加载子数据时，会把focusIndex清空
				var rowIndex = event.focusIndex;
				if (rowIndex == -1) {
					if (this.prapareFocusIndex != null) {
						dataset.setFocusRowIndex(this.prapareFocusIndex);
					} else {
						if (this.focusNode != null && this.focusNode == this)
							return;
						if (this.focusNode != null) {
							this.focusNode.unFocusNode();
							this.focusNode = null;
						}
					}
				} else {
					var focusRow = dataset.getRow(rowIndex);
					var focusNode = null;
					if (focusRow && focusRow.rowId)
						focusNode = this.getNodeByRowId(focusRow.rowId);
					if (focusNode != null) {
						focusNode.focusNode();
					}
				}
				this.prapareFocusIndex = null;
			}
		},
		/**
		 * 此节点要挂的父节点是他的子节点，若是，则返回false，否则返回true
		 * @private
		 */
		checkParentNode : function(newValue, oldValue, idIndex, pIdIndex,
				node, ds) {
			if (newValue != null && newValue != "" && node.childrenTreeNodes != null
					&& node.childrenTreeNodes.length > 0) {
				for (var i = 0, n = node.childrenTreeNodes.length; i < n; i++) {
					if (newValue == node.childrenTreeNodes[i].nodeData
							.getCellValue(idIndex)) {
						return false;
					}
					var result = TreeViewComp.checkParentNode(newValue, oldValue,
							idIndex, pIdIndex, node.childrenTreeNodes[i], ds);
					if (result == false)
						return false;
				}
			}
			return true;
		},
		/**
		 * 重画level层的所有节点
		 * @private
		 */
		paintNodesByLevel : function(level, dataset) {
			// 移除掉界面上已经画的此level中的所有节?
			this.removeUINodesByLevel(level);
			var rows = null;
			if (level.parentLevel != null)
				rows = dataset.getAllRows();
			else
				rows = dataset.getRows();
			this.createNodes(rows, dataset, level);
			this.manageLeastNode();
			// 树节点构建完成后向界面上画节?
			this.paintZone();
		},
		/**
		 * 删除指定的level包含的所有树节点(删除UI节点)
		 * @param{TreeLevel} level
		 * @private
		 */
		removeUINodesByLevel : function(level) {
			var nodes = this.idNodeMap.values();
			if (nodes != null && nodes.length > 0) {
				for (var i = 0, count = nodes.length; i < count; i++) {
					if (nodes[i].level == level) {
						nodes[i].parentTreeNode.remove(nodes[i].id, true);
					}
				}
				/* 确保一? */
				if (level == this.topTreeLevel) {
					this.idNodeMap.clear();
				}
			}
		},
		/**
		 * 节点创建时调用此方法
		 * @private
		 */
		nodeCreated : function(nodeId, caption, value, pNodeId,
				nodeData, dataset) {
			if (!this.onBeforeNodeCreate(nodeData))
				return null;
			// 默认实现如下
			var level = this.getLevelByDataset(dataset);
			var isLeaf = null;
		
			// 最后一级level中的树节点isLeaf均为true,然后在add方法中根据条件改变图?
			// 其他级中isLeaf均为false,用户可以覆盖此默认过程改变其他层级的图标显示
			if (this.isLastLevel(level))
				isLeaf = true;
			else
				isLeaf = false;
		
			var attrArr = {};
			attrArr.open = this.options.rootOpen;
			attrArr.isLeaf = isLeaf;
			attrArr.reload = false;
			attrArr.withCheckBox = this.options.withCheckBox;
			attrArr.checkBoxModel = this.checkBoxModel;
			attrArr.canEdit = this.canEdit;
			attrArr.isBold = this.isFirstLevel(level) && !pNodeId;
			attrArr.textMaxWidth = this.treeNodeTextMaxWidth;
			//var node = new TreeNode(nodeId, caption, value, attrArr, nodeData,this.options.imgRender);
			var node = $("<div>").treenode({id:nodeId,caption:caption,value:value,attr:attrArr,nodeData:nodeData,imgRender:this.options.imgRender}).treenode("instance");
				
			node.rowId = nodeData.rowId;
			node.level = level;
			node.pNodeId = pNodeId;
			node.treeViewComp = this;
			// 调用用户的方?
			this.onNodeCreated(node);
			return node;
		},
		/**
		 * 树的中序遍历方法
		 * @return nodes数组
		 */
		inorderTraversal : function() {
			return this.options.rootNode.inorderTraversal();
		},
		/**
		 * 改变根节点caption的显示内?
		 * @param caption 要显示的新?
		 */
		changeRootCaption : function(rootCaption) {
			if (this.options.rootNode && this.options.rootNode.painted == this.PAINTALL) {
				this.rootCaption = rootCaption;
				this.options.rootNode.divText.removeChild(this.options.rootNode.divText.childNodes[0]);
				this.options.rootNode.divText.appendChild(document.createTextNode(rootCaption));
				this.options.rootNode.divText.title = rootCaption;
			}
		},
		/**
		 * 节点创建前事?
		 * @private
		 */
		onBeforeNodeCreate : function(row) {
			var treeRowEvent = {
				"obj" : this,
				"row" : row
			};
			this.doEventFunc("beforeNodeCreate", TreeRowListener.listenerType,
					treeRowEvent);
			return true;
		},		
		/**
		 * 在节点创建的时候调用这个方?
		 * @param node  刚创建的节点
		 * @private
		 */
		onNodeCreated : function(node) {
			var treeNodeEvent = {
				"obj" : this,
				"node" : node
			};
			this.doEventFunc("nodeCreated", TreeNodeListener.listenerType,
					treeNodeEvent);
		},
		/**
		 * 删除节点
		 * @param node
		 * @private
		 */
		onNodeDelete : function(node) {
			var treeNodeEvent = {
				"obj" : this,
				"node" : node
			};
			this.doEventFunc("onNodeDelete", TreeNodeListener.listenerType,
					treeNodeEvent);
		},
		/**
		 * 根节点创建的时候调用这个方?用户可以改变根节点的caption?
		 * @private
		 */
		onRootNodeCreated : function(rootNode) {
			var treeNodeEvent = {
				"obj" : this,
				"node" : rootNode
			};
			this.doEventFunc("rootNodeCreated", TreeNodeListener.listenerType,
					treeNodeEvent);
		},
		/**
		 * 树的当前选中节点改变前调用此方法,用户返回false可以阻止新的节点被选中.
		 * @param newNode  马上要选中的节点
		 * @param oldNode  上一个选中的节点
		 * @private
		 */
		onBeforeSelNodeChange : function(newNode, oldNode) {
			var treeNodeChangeEvent = {
				"obj" : this,
				"newNode" : newNode,
				"oldNode" : oldNode
			};
			this.doEventFunc("beforeSelNodeChange", TreeNodeListener.listenerType,
					treeNodeChangeEvent);
		},
		/**
		 * 在树节点的某个显示字段改变时调用的方法，用户如果自定义了默认显示值需要返回此?
		 * @param{Array} labelFields 显示值各字段
		 * @param{Array} labelDelims 显示值各字段间的分隔?
		 * @param{TreeNode} node 当前的节?
		 * @private
		 */
		onBeforeNodeCaptionChange : function(labelFields,
				labelDelims, node) {
			var treeNodeCaptionChangeEvent = {
				"obj" : this,
				"labelFields" : labelFields,
				"labelDelims" : labelDelims,
				"node" : node
			};
			this.doEventFunc("beforeNodeCaptionChange", TreeNodeListener.listenerType,
					treeNodeCaptionChangeEvent);
		},
		/**
		 * 树的当前选中节点改变后调用此方法
		 * @param node 新的选中的节点
		 * @private
		 */
		onAfterSelNodeChange : function(node) {
			var treeNodeEvent = {
				"obj" : this,
				"node" : node
			};
			this.doEventFunc("afterSelNodeChange", TreeNodeListener.listenerType,
					treeNodeEvent);
		},
		/**
		 * 树节点文字的点击事件，用户覆盖此方法来处理自己的事件 * 
		 * @param node  点击的树节点
		 * @param isClickSameNode 如果该参数不为null且为true，表示是点击树节点且点击的是同一个树节点时调用的该方?
		 * @private
		 */
		onclick : function(node, isClickSameNode) {
			var treeNodeMouseEvent = {
				"obj" : this,
				"node" : node,
				"isClickSameNode" : isClickSameNode
			};
			this.doEventFunc("onclick", TreeNodeListener.listenerType,
					treeNodeMouseEvent);
		},
		/**
		 * 树节点文字的双击事件，用户覆盖此方法来处理自己的事件，返回false会阻止节点默认事件的响应? ?节点双击事件的默认行为是打开或者收缩此节点的所有子节点?
		 * @private
		 */
		ondbclick : function(node) {
			var treeNodeMouseEvent = {
				"obj" : this,
				"node" : node
			};
			this._trigger('ondbclick',null,treeNodeMouseEvent);
//			this.doEventFunc("ondbclick", TreeNodeListener.listenerType,
//					treeNodeMouseEvent);
		},
		/**
		 * 拖拽开始时的调用方法，返回false禁止拖拽
		 * @param sourceNode 要拖动的节点对象
		 * @private
		 */
		onDragStart : function(sourceNode) {
			var treeNodeDragEvent = {
				"obj" : this,
				"sourceNode" : sourceNode
			};
			this.doEventFunc("onDragStart", TreeNodeListener.listenerType,
					treeNodeDragEvent);
		},
		/**
		 * 拖拽结束时的调用方法，如果返回false表示不执行默认的将源节点挂在目标节点下的动作，而由用户决定如何处理，一般情况下不用自己处理?
		 * @param sourceNode 要拖动的节点对象
		 * @param targetNode 要附着到的目标节点对象
		 * @private
		 */
		onDragEnd : function(sourceNode, targetNode) {
			var treeNodeDragEvent = {
				"obj" : this,
				"sourceNode" : sourceNode,
				"targetNode" : targetNode
			};
			this.doEventFunc("onDragEnd", TreeNodeListener.listenerType,
					treeNodeDragEvent);
		},
		/**
		 * 节点的checkbox选中时事?
		 * @private
		 */
		onChecked : function(node) {
			var treeNodeEvent = {
				"obj" : this,
				"node" : node
			};
			this.doEventFunc("onChecked", TreeNodeListener.listenerType, treeNodeEvent);
		},
		/**
		 * 右键菜单显示前事件
		 * @private
		 */
		onBeforeContextMenu : function(node, ds, level) {
			var treeContextMenuEvent = {
				"obj" : this,
				"node" : node,
				"ds" : ds,
				"level" : level
			};
			this.doEventFunc("beforeContextMenu", TreeContextMenuListener.listenerType,
					treeContextMenuEvent);
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function(type) {
			if (type == null) {
				return null;
			}
			var context = new Object;
			context.c = "TreeViewContext";
			context.enabled = this.isTreeActive;
			context.withRoot = this.options.withRoot;
		
			if (type == "tree_current_parent" && this.selNode != null) {
				var treeModel = new Object;
				treeModel.javaClass = "com.haiyou.wfw.common.comp.WebTreeNode";
				var rootNode = this.TreeNode.generateNodeJsonObj(this.selNode, true,
						"no_child");
				treeModel.rootNode = rootNode;
				treeModel.currentNodeId = this.selNode.id;
				context.treeModel = treeModel;
			}
			// 当前节点、所有父节点、当前节点的第一级子节点
			else if (type == "tree_current_parent_children" && this.selNode != null) {
				var treeModel = new Object;
				treeModel.javaClass = "com.haiyou.wfw.common.comp.WebTreeNode";
				var rootNode = this.TreeNode.generateNodeJsonObj(this.selNode, true,
						"child_level1");
				treeModel.rootNode = rootNode;
				treeModel.currentNodeId = this.selNode.id;
				context.treeModel = treeModel;
			}
			// 所有节?
			else if (type == "tree_all") {
				var treeModel = new Object;
				treeModel.javaClass = "com.haiyou.wfw.common.comp.WebTreeModel";
				var rootNode = this.TreeNode.generateNodeJsonObj(this.options.rootNode, false,
						"child_allLevel");
				treeModel.rootNode = rootNode;
				treeModel.currentNodeId = this.selNode ? this.selNode.id : null;
				context.treeModel = treeModel;
			}
		
			context.text = this.rootCaption;
			return context;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.text != null && context.text != this.rootCaption)
				this.changeRootCaption(context.text);
		},
		destroySelf : function() {
			this.options.rootNode.destroySelf(false);
			if (this.idNodeMap != null) {
				var nodes = this.idNodeMap.values();
				for (var i = 0; i < nodes.length; i++) {
					nodes[i].destroySelf(false);
				}
				nodes = null;
			}
			this.topTreeLevel.destroySelf();
			if (this.idNodeMap != null) {
				this.idNodeMap.clear();
				this.idNodeMap = null;
			}
			if (this.tmpNodeMap != null)
				this.tmpNodeMap.clear();
			this.destroy();
		},
		createDragDiv : function(divText) {
			var jq_dragDiv = $("<div style='background:yellow;width:"+divText.offsetWidth+"px;height:"+divText.offsetHeight+"px'></div>");
			var dragDiv = jq_dragDiv.get(0);
			dragDiv.appendChild(document.createTextNode(divText.firstChild.nodeValue));
			return dragDiv;
		},
		/**
		 * 值改变时事件处理
		 * @private
		 */
		compValueChangeFun : function(oThis, valueChangeEvent) {
			var comp = valueChangeEvent.obj;
			var newValue = comp.getValue();
			var rowIndex = oThis.level.dataset.getRowIndex(oThis.nodeData);
			var colIndex = oThis.level.dataset.nameToIndex(oThis.level.labelFields[0]);
			oThis.level.dataset.setValueAt(rowIndex, colIndex, newValue);
		},doEventFunc : function(oThis, valueChangeEvent ,sassa){
		}
				
    });
    
    
    
    $.widget("lui.treenode", $.lui.base, {
		options: {
            componentType : "TREENODE",
            id : '',
            caption : '',
            value : '',
            nodeData : null,
            imgRender : null,
            attr : null,
            canEdit : false,
		    isBold : false
			
		},
		_initParam : function() {
			this._super();
			this.id = this.options.id,
            this.caption = this.options.caption,
            this.value = this.options.value,
            this.nodeData = this.options.nodeData,
            this.imgRender = this.options.imgRender,
            this.attr = this.options.attr,
            this.DEFAULT_ID = "DEFAULT_ID";
			this.IMAGE_WIDTH = 19;
			// 节点没画
			this.NOPAINT = 1;
			// 节点画了一?表示只画了框?
			this.PAINTHALF = 2;
			// 节点全部画完
			this.PAINTALL = 3;
			// 复选状态隐藏复选框
			this.hideCheckbox = true;
			
			this.CHECKBOXMODEL_SELF = 0;
			// 设置自己和子
			this.CHECKBOXMODEL_SELF_SUB = 1;
			// 设置自己和子和父
			this.CHECKBOXMODEL_SELF_SUB_PARENT = 2;
			
			this.open = true;
			this.isLeaf = false;
			this.reload = false;
			this.withCheckBox = false;
			this.checkBoxModel = this.CHECKBOXMODEL_SELF;
			this.canEdit = false;
			this.isBold = $.argumentutils.getBoolean(this.options.isBold,false);
			this.textMaxWidth = -1;
			if (this.attr) {
				this.open = $.argumentutils.getBoolean(this.attr.open, this.options.open);
				this.isLeaf = $.argumentutils.getBoolean(this.attr.isLeaf, this.options.isLeaf);
				this.reload = $.argumentutils.getBoolean(this.attr.reload, this.options.reload);
				this.withCheckBox = $.argumentutils.getBoolean(this.attr.withCheckBox, this.options.withCheckBox);
				this.checkBoxModel = $.argumentutils.getInteger(this.attr.checkBoxModel,this.CHECKBOXMODEL_SELF);
				// 保存此节点的子树节点
				this.canEdit = $.argumentutils.getBoolean(this.attr.canEdit, this.canEdit);
				// 标识该节点文字是否加粗
				this.isBold = $.argumentutils.getBoolean(this.isBold, false);
				// 节点文字显示最大宽度,-1:不限制.
				this.textMaxWidth = $.argumentutils.getInteger(this.attr.textMaxWidth, this.options.textMaxWidth);
			}
			this.root = false;
			this.childrenTreeNodes = new Array();
			// 标识该节点是否被画过
			this.painted = this.NOPAINT;
			// 标识该节点前面得checkbox是否被选中
			this.checked = false;
			// 文件,文件夹关闭时图标
			if (this.isLeaf) {
				this.icon1 = window.themePath + "/comps/tree/images/leaf.gif";
				this.icon2 = this.icon1;
			} else {
				this.icon1 = window.themePath + "/comps/tree/images/folder_off.png";
				this.icon2 = window.themePath + "/comps/tree/images/folder_on.png";
			}
			// 复选状态对应的图标 window.themePath + "/images/treeview/" + this.checkArray[0];
			this.checkArray = new Array("iconUncheckAll.gif", "iconCheckAll.gif",
										"iconCheckGray.gif", "iconUncheckDis.gif", "iconCheckDis.gif",
										"iconCheckDis.gif");
			
		},
		_create : function() {
			this._initParam();
			var oThis = this;
		},
		/**
		 * 设置文件夹图标为叶子图标
		 * @param isLeaf true表示叶子,false表示文件?
		 */
		setLeaf : function(isLeaf) {
			if (this.isLeaf & isLeaf)
				return;
		
			this.isLeaf = isLeaf;
			// 文件,文件夹关闭时图标
			if (this.isLeaf) {
				this.icon1 = window.themePath + "/comps/tree/images/leaf.gif";
				this.icon2 = this.icon1;
			} else {
				this.icon1 = window.themePath + "/comps/tree/images/folder_off.png";
				this.icon2 = window.themePath + "/comps/tree/images/folder_on.png";
			}
			this.changeIcon();
		},
		/**
		 * 创建
		 * @author guoweic 用DIV代替TABLE
		 * @private
		 */
		create1 : function() {
			if (this.painted == this.NOPAINT) {
				var oThis = this;
				// 标示此节点画得状?
				this.painted = this.PAINTHALF;
				// 创建每个节点的总div
				
				var jq_Div_gen = $("<div></div>");
				
				this.Div_gen = jq_Div_gen.get(0);	
				// 设置高度从而撑开高度占位
				// this.Div_gen.style.height = this.NODEHEIGHT;
				jq_Div_gen.attr({
				    'id':this.id,
				    'owner':this
				});
				
				var jq_wholeTable = $("<table></table>");
				jq_wholeTable.attr({
				'cellSpacing' : '0',
				'cellPadding' : '0'
				}).css({'width' : '100%'});
		
				// add by chouhl 2012-3-7 divRow外增加table
				this.wholeTable = jq_wholeTable.get(0);
				jq_Div_gen.append(this.wholeTable);		
				// this.divRowTable.style.marginLeft = (levelNum * 10) + "px";
				var jq_oTBody = $("<tbody></tbody>");
				var oTBody = jq_oTBody.get(0);
				jq_wholeTable.append(jq_oTBody);
				
				var row = oTBody.insertRow(-1);
				this.wholeTableCell = row.insertCell(-1);
				var jq_divRow = $('<div class="" style="position: relative; height: '+this.NODEHEIGHT+'px; width: auto;"></div>');
				this.divRow = jq_divRow.get(0);
				
				this.wholeTableCell.appendChild(this.divRow);
				this.divRowLeft = $("<DIV>").get(0);
				this.divRow.appendChild(this.divRowLeft);
				this.divRowRight = $("<DIV>").get(0);
				this.divRow.appendChild(this.divRowRight);
		        var jq_divContent = $("<DIV style='height:100%;display:none'></DIV>");
				this.divContent = jq_divContent.get(0);
				this.wholeTableCell.appendChild(this.divContent);
			
		        var jq_DIVT = $("<DIV style='float:left;width:19px;height:0px;background:url(" + window.themePath + "/comps/tree/images/I.gif)'></DIV>");
				this.DIVT = jq_DIVT.get(0);
				
		        var jq_divChildren = $("<DIV></DIV>");
				this.divChildren = jq_divChildren.get(0);
				// this.divChildren.style[$.CONST.ATTRFLOAT] = "left";
				// ****this.divChildren.style.width = "100%";
				this.divContent.appendChild(this.divChildren);
		
//				if (!IS_IE6 && !IS_IE7) { // 增加一个空DIV行，使this.divContent高度为auto起作?
					var jq_divSpace = $("<div style='clear:both;display:block;'></div>");			
					this.divSpace = jq_divSpace.get(0);			
					this.divContent.appendChild(this.divSpace);
//				}
		
				if (!this.root) {
					if (this.parentTreeNode.open && !this.parentTreeNode.isLeaf)
						this.parentTreeNode.divContent.style.display = 'block';
					this.parentTreeNode.divChildren.appendChild(this.Div_gen);
		
					var currentNode = this.parentTreeNode;
					while (currentNode) {
						// guoweic: add start 2010-1-7
						if (currentNode.DIVT.offsetHeight < this.NODEHEIGHT
								* currentNode.getPaintedChildNodesSize(0))
							// guoweic: add end
							currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight
									+ this.NODEHEIGHT + "px";
						currentNode = currentNode.parentTreeNode;
					}
					// this.parentTreeNode.DIVT.style.height =
					// this.parentTreeNode.DIVT.offsetHeight + this.NODEHEIGHT +
					// "px";
				}
		
				// guoweic: modify end
			}
		},
		/**
		 * 获得节点下所有已画出的子孙结点数?
		 * @author guoweic
		 * @param size:结点数量
		 */
		getPaintedChildNodesSize : function(size) {
			var childNodes = this.childrenTreeNodes;
			if (childNodes != null) {
				for (var i = 0, n = childNodes.length; i < n; i++) {
					if (childNodes[i].painted != this.NOPAINT) { // 该节点已画出
						size++;
						size = childNodes[i].getPaintedChildNodesSize(size);
					}
				}
			}
			return size;
		},
		/**
		 * @private
		 */
		create2 : function(levelNum) {
			//alert(levelNum);
			if (this.painted == this.PAINTHALF) {
				//alert("this.painted="+this.painted);
				// 标示此节点画得状?
				this.painted = this.PAINTALL;
				// ****
				
				var jq_divRowTable = $("<table cellSpacing=0 cellPadding=0 style='height:100%;margin-left:"+(levelNum * 10)+"px'></table>");
				this.divRowTable = jq_divRowTable.get(0);
				this.divRow.appendChild(this.divRowTable);
				var oTBody = $("<tbody>").get(0);
				this.divRowTable.appendChild(oTBody);
				this.row = oTBody.insertRow(-1);
				// 加号,减号图标的cell
				var oThis = this;
				if (this.withCheckBox) {			
					// 复选框状? 0:全部非选中,1:全部选中,2:部分子非选中,3:只读(当前状态为非选中),4,5:只读(当前状态为选中)
					this.checkstate = 0;
					// 放置checkbox的cell
					this.imgCheckCell = this.row.insertCell(-1);
					if (this.hideCheckbox && false) {
						this.imgCheckCell.style.width = "0px";
					} else {
						this.imgCheckCell.style.width = "19px";
					}
					this.imgCheckCell.style.height = "26px";
					// checkbox
					this.checkbox = $("<img>").get(0);
					if (this.hideCheckbox && false) {
						this.checkbox.style.display = "none";
					}
					this.imgCheckCell.appendChild(this.checkbox);
					// 设置选中状?
					if (!this.root) {
						this._setCheck(this.isNodeDataSelected() ? 1 : 0, true);
						this._correctCheckStates();
					} else {
						this._setCheck(0, true);
					}
					// checkbox选择事件
					this.checkbox.onclick = function() {
						if (oThis.refTree) {
							oThis._setSubChecked(!oThis.checked, oThis.root);
							oThis._correctCheckStates();
						}
					};
				}
				// 放置文件,文件夹图标的cell
				this.img2Cell = this.row.insertCell(-1);
				this.img2Cell.style.width = "13px";
				this.img2Cell.style.height = "26px";
				// 放置自定义图?
				// this.imgRender = ImgRender;
				if (this.imgRender != null) {
					this.selfImgCell = this.row.insertCell(-1);
					this.imgRender.call(this, this.selfImgCell, this);
				}
				// 放置文字的cell
				this.textCell = this.row.insertCell(-1);
				// 文字的宽?
				// 加号,减号箭头图标(初始化为文件,文件夹图标changeIcon方法会替换为加号,减号图标)
				var jq_img1 = $("<IMG src='"+this.icon1+"' style='float:left' class='treenode_img' />"
				               ).on('click',handleTreeNodeImg1Click);
				this.img1 = jq_img1.get(0);
				this.img1.owner = this;		
				// 文件?文件图标
				var jq_img2 = $("<IMG src='"+this.icon1+"' style='float:left' class='treenode_img' />"
				               ).on('click',handleTreeNodeImg2Click
				               ).on('dbclick',handleTreeNodeImg2Dblclick);
				this.img2 = jq_img2.get(0);
				this.img2Cell.appendChild(this.img2);
				this.img2.owner = this;
				
				// 创建树每个节点的文字
				var jq_divText = $("<SPAN class='treenode_text'></SPAN>");
				this.divText = jq_divText.get(0);
				var jq_textMaxWidth = "";
				if(jq_textMaxWidth!=-1)
				{
					jq_textMaxWidth ="width:"+this.textMaxWidth + "px";
				}
				var jq_divTextOut = $("<DIV class='treenode_div' style='"+jq_textMaxWidth+"'></DIV>");
				this.divTextOut = jq_divTextOut.get(0);
				this.divTextOut.appendChild(this.divText);
			
		
				this.textCell.appendChild(this.divTextOut);
				// 保存此节点对?
				this.divText.owner = this;
				
				if (this.isLeaf) {
					//jq_divText.css("color","#000000");
				}
				if (this.isBold) {
					jq_divText.css("font-weight","bold");
				}
				// 创建编辑，删除按钮div
				if (this.canEdit) {
					var jq_divHint = $("<DIV style='width:40px;height:"+this.divText.style.height+";padding-left:15px;visibility:hidden'></DIV>");
					this.divHint = jq_divHint.get(0);
		            var jq_imgEdit = $("<IMG title='编辑' src='"+window.themePath+"/comps/tree/images/edit.png"+"' style='float:left;cursor: pointer;'/>"
		                              ).on('click',handleTreeNodeImgEditClick);
				
					this.imgEdit = jq_imgEdit.get(0);	
					this.imgEdit.owner = this;
					jq_divHint.append(jq_imgEdit);
				
					var jq_imgDelete = $("<IMG title = '删除' src='"+window.themePath	+"/comps/tree/images/delete.png' style='float:left;cursor: pointer;'/>"
					                 ).on('click',handleTreeNodeImgDeleteClick);
					this.imgDelete = $("<img>").get(0);			
					this.imgDelete.owner = this;			
					
					jq_divHint.append(jq_imgDelete);
		
					this.hintCell = this.row.insertCell(-1);
					this.hintCell.appendChild(this.divHint);
				}
				// guoweic: modify end
				this.divText.appendChild(document.createTextNode(this.caption));
				this.divText.title = this.caption;
		
				this.divText.onmousedown = function(e) {
					handleTreeNodeMouseDown(e, this, oThis);
				};
				this.divText.onmousemove = handleTreeNodeMouseMove;
				this.divText.onmouseup = handleTreeNodeMouseUp;
				this.divText.onmouseover = function(e) {
					handleTreeNodeMouseOver(e, oThis);
				};
				this.divText.onmouseout = function(e) {
					handleTreeNodeMouseOut(e, oThis);
				};
				// 树节点的点击事件
				this.divText.onclick = function(e) {
					this.owner.isClickTreeNode = true;
					handleTreeNodeClick(e, this);
					this.owner.isClickTreeNode = false;
				}; // handleTreeNodeClick;
				// 文字双击效果等同于点击文件夹和加号按钮的效果
				this.divText.ondblclick = handleTreeNodeDblclick;
				// 显示右键菜单
				this.divText.oncontextmenu = handleTreeNodeContextMenu;
		
				if (!this.root) {
					if (this.refTree)
						this.changeChildrenIcon();
				}
		
				/* 树节点拖放处理函? */
				this.refTree.oDrag = null;
				this.refTree.iDiffX = 0;
				this.refTree.iDiffY = 0;
				this.refTree.bDragStart = false;
			}
		},
		/**
		 * @desc: 设置节点以及子节点选中
		 * @author: dingrf
		 * @type: private
		 * @param: state - 状?true/false
		 * @param: treeNode - 目标节点
		 * @param: isRoot - 是否为根，为根时，不管哪种复选策略，都全?
		 */
		_setSubChecked : function(state, isRoot) {
			// 点根节点时全?
			if (this.checkBoxModel != this.CHECKBOXMODEL_SELF || isRoot) {
				if (this.open == false) {
					this.toggle();
				}
				if (this.childrenTreeNodes && this.childrenTreeNodes.length > 0) {
					if (this.childrenTreeNodes[this.childrenTreeNodes.length - 1] != null
							&& this.childrenTreeNodes[this.childrenTreeNodes.length - 1].id
									.endWith(this.RELOADNODE_suffix)) {
						this._state = state;
						this._isRoot = isRoot;
						this.prapareCheck = true;
						return;
					} else {
						for (var i = 0; i < this.childrenTreeNodes.length; i++) {
							if (this.childrenTreeNodes[i]) {
								this.childrenTreeNodes[i]._setSubChecked(state, isRoot);
							}
						}
					}
				}
			}
			this.setChecked(state);
		},
		/**
		 * @desc: 设置复选框状?
		 * @author: dingrf
		 * @type: private
		 * @param: state - 状?0/1/2
		 * @param: treeNode - 目标节点
		 */
		_setCheck : function(state, isCreate) {
			this.checkstate = (state != null) ? state : 0;
			if (this.checkbox) {
				if (isCreate) { // 创建节点时调?
					if (this.checkstate == 0
							&& this.checkBoxModel != this.CHECKBOXMODEL_SELF) {
						if (this.parentTreeNode) {
							this.checkstate = this.parentTreeNode.checkstate;
							this.checked = this.parentTreeNode.checked;
						}
					}
				}
				this.checkbox.src = window.themePath + "/comps/tree/images/"
						+ this.checkArray[this.checkstate];
			}
		},
		/**
		 * @desc: 修正父节点的复选框状?
		 * @author: dingrf
		 * @type: private
		 * @param: treeNode - 目标节点
		 */
		_correctCheckStates : function() {
			if (this.root)
				return;
			// 只设置自己时,或者设置自己和子节点时,不做修正
			if (this.checkBoxModel == this.CHECKBOXMODEL_SELF
					|| this.checkBoxModel == this.CHECKBOXMODEL_SELF_SUB)
				return;
		
			var parentNode = this.parentTreeNode;
			if (!parentNode)
				return;
		
			// 记录是否存在状态为完全非选中的子
			var flag1 = 0;
			// 记录是否存在状态为完全选中的子
			var flag2 = 0;
			for (var i = 0; i < parentNode.childrenTreeNodes.length; i++) {
				var node = parentNode.childrenTreeNodes[i];
				if (node.checkstate == null)
					return;
				if (node.checkstate == 0)
					flag1 = 1;
				else if (node.checkstate == 1)
					flag2 = 1;
				else {
					flag1 = 1;
					flag2 = 1;
					break;
				}
			}
		
			if ((flag1) && (flag2)) {
				parentNode.setChecked(true);
				parentNode._setCheck(2);
			} else if (flag1) {
				parentNode.setChecked(false);
				parentNode._setCheck(0);
			} else {
				parentNode.setChecked(true);
				parentNode._setCheck(1);
			}
		
			parentNode._correctCheckStates();
		},
		/**
		 * 打开或关闭节点后，重新计算父节点DIVT的高?
		 * @author guoweic
		 * @private
		 */
		resizeParentDIVT : function() {
			var childNodes = this.childrenTreeNodes;
			if (this.open) { // 关闭节点
				var currentNode = this.parentTreeNode;
				var height = this.oldDivContentHeight;
				while (currentNode) {
					if (currentNode.DIVT.offsetHeight - height > 0) {
						currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight
								- height + "px";
					}
					currentNode = currentNode.parentTreeNode;
				}
			} else { // 打开节点
				if (this.oldDivContentHeight) {
					var currentNode = this.parentTreeNode;
					var height = this.oldDivContentHeight;
					while (currentNode) {
						currentNode.DIVT.style.height = currentNode.DIVT.offsetHeight
								+ height + "px";
						currentNode = currentNode.parentTreeNode;
					}
				}
			}
		},
		/**
		 * 获取节点前的checkbox是否被选中
		 */
		isChecked : function() {
			return this.checked;
		},
		/**
		 * 设置节点前的checkbox选中状态
		 */
		setChecked : function(checked) {
			if (this.withCheckBox == true && this.checked != checked) {
				if (checked)
					this._setCheck(1);
				else
					this._setCheck(0);
				this.checked = checked;
				var tree = this.refTree;
				if (tree.isTreeActive == false)
					return;
				// 增加选中内容
				if (!this.isRoot()) {
					// 当前ds所有选中行索?
					if (this.level != this.refTree.topTreeLevel) {
//						var keyValue = this.parentTreeNode.getMasterKeyValue();
//						if (this.level.dataset.currentKey != keyValue)
						this.level.dataset.setCurrentPage(null, {
									'firstLoad' : true
								});
					}
		
					var selectedRowIndices = this.level.dataset.getSelectedIndices();
					if (selectedRowIndices == null || selectedRowIndices.length == 0) {
						if (checked == true) {
							this.level.dataset.addRowSelected(this.level.dataset
									.getRowIndex(this.nodeData));
						}
					} else {
						// 当前点击节点index
						var currRowIndex = this.level.dataset
								.getRowIndex(this.nodeData);
						// 如果已经为选中节点则反选该节点
						if (selectedRowIndices.indexOf(currRowIndex) != -1) {
							if (checked == false)
								this.level.dataset.setRowUnSelected(currRowIndex);
						}
						// 继续添加选中节点
						else {
							if (checked == true)
								this.level.dataset.addRowSelected(this.level.dataset
										.getRowIndex(this.nodeData));
						}
						this.level.dataset.setFocusRowIndex(currRowIndex);
						
					}
				}
				this.refTree.onChecked(this);
			}
		},
		/**
		 * 判断节点表示的行数据是否是选中状?
		 */
		isNodeDataSelected : function() {
			if (!this.isRoot()) {
				var rows = this.level.dataset.getSelectedRows();
				if (rows != null && rows.length > 0) {
					if (rows.indexOf(this.nodeData) != -1)
						return true;
				}
			}
			return false;
		},
		/**
		 * 销毁自身
		 * @private
		 */
		destroySelf : function(withChildren) {
			if (withChildren == null)
				withChildren = true;
			if (this.img1) {
				$.pageutils.clearHtmlNodeProperties(this.img1);
				this.img1 = null;
			}
			if (this.img2) {
				$.pageutils.clearHtmlNodeProperties(this.img2);
				this.img2 = null;
			}
			if (this.Div_gen) {
				this.Div_gen.owner = null;
				this.Div_gen.innerHTML = "";
			}
			if (this.divText) {
				$.pageutils.clearHtmlNodeProperties(this.divText);
				this.divText = null;
			}
			if (this.checkbox) {
				$.pageutils.clearHtmlNodeProperties(this.checkbox);
				this.checkbox = null;
			}
			if (withChildren) {
				var children = this.childrenTreeNodes;
				if (children != null) {
					for (var i = 0; i < children.length; i++)
						children[i].destroySelf();
				}
				delete this.childrenTreeNodes;
			}
		},
		/**
		 * 显示hint
		 */
		showHint : function() {
			if (this.hintShow == true)
				return;
			if (_TreeNode.oldShowHintNode != null)
				_TreeNode.oldShowHintNode.hideHint();
			_TreeNode.oldShowHintNode = this;			
			this.divHint.style.visibility = "";
			this.hintShow = true;
		},
		/**
		 * 隐藏hint
		 */
		hideHint : function() {			
			this.divHint.style.visibility = "hidden";
			if (this.comp != null && this.comp.visible == true)
				this.comp.hide();
			if (this.divText)
				this.divText.style.visibility = "";
			this.hintShow = false;
		},
		/**
		 * 判断节点是否是叶子节?
		 */
		isLeafNode : function() {
			return this.isLeaf;
		},
		/**
		 * 判断此节点是否是根节?
		 */
		isRoot : function() {
			return this.root;
		},
		/**
		 * treenode节点的鼠标双击处理函?
		 * @private
		 */
		dblclick : function(node) {
			return this.refTree.dblclick(node);
		},
		/**
		 * 根据根节点此时的状态在切换打开和关闭之间的图标
		 * @private
		 */
		toggle : function() {
			if (this.hasChildren()) {
				if (this.painted == this.NOPAINT)
					return;
				if (this.open) {
					// 即将展开的节点的孩子的宽度从而减少树的宽?
					this.oldDivContentHeight = this.divContent.offsetHeight;
					this.divContent.style.display = 'none';
					// guoweic modify firefox、IE8中有问题 start 2009-11-4
//					if (IS_IE6 || IS_IE7)
//						this.adjustTreeWidth(false);
					// guoweic modify end
					// guoweic: add start 2009-12-18
					// 重新计算父节点DIVT的高?
					this.resizeParentDIVT();
					// guoweic: add end
					this.open = false;
		
					this.refTree.paintZone();
				} else {
					// 即将展开的节点的孩子的宽度从而增加树的宽?
					this.divContent.style.display = 'block';
					// guoweic modify firefox、IE8中有问题 start 2009-11-4
//					if (IS_IE6 || IS_IE7)
//						this.adjustTreeWidth(true);
					// this.adjustTreeWidth(false);
					// guoweic: add start 2009-12-18
					// 重新计算父节点DIVT的高?
					this.resizeParentDIVT();
					// guoweic: add end
					this.open = true;
					this.refTree.paintZone();
					// 此节点第一次打开的时候需要动态加载节?
					if (this.reload)
						this.reloadNode();
				}
				this.changeIcon();
				$.pageutils.adjustContainerFramesHeight();
			}
		},
		/**
		 * 此节点展开子节点时调整树的宽度
		 * @param isAddWidth true表示增加树的宽度,false表示减小树的宽度
		 * @private
		 */
		adjustTreeWidth : function(isAddWidth) {
			if (!this.root) {
				if (isAddWidth) {
					var overWidth = this.getChildrenOverParentWidth();
					if (overWidth > 0) {
						this.refTree.treeDiv_gen.style.width = (this.refTree.treeDiv_gen.offsetWidth + overWidth)
								+ "px";
						this.refTree.treeDiv_gen.scrollLeft = overWidth + "px";
					}
				} else {
					return;
				}
			}
		},
		/**
		 * 此节点的展开?子节点比此节点增加的宽度
		 */
		getChildrenOverParentWidth : function() {
			var allLevelNodes = this.getLevelChildrenNodesArray();
			if (allLevelNodes == null)
				return 0;
		
			var maxWidthArr = new Array();
			var maxWidthNodes = new Array();
		
			// 计算每层打开节点中标题文字最长的节点
			for (var i = 0, count = allLevelNodes.length; i < count; i++) {
				var nodes = allLevelNodes[i], maxWidth = 0, tempWidth = 0, maxWidthNode = null;
				for (var j = 0, count1 = nodes.length; j < count1; j++) {
					tempWidth = $.measures.getTextWidth(nodes[j].caption, "treenode_text");
					if (tempWidth > maxWidth) {
						maxWidth = tempWidth;
						maxWidthNode = nodes[j];
					}
				}
				maxWidthNodes.push(maxWidthNode);
				maxWidthArr.push(maxWidth);
			}
		
			// 所有子层中宽度最大?
			var maxNodeWidth = 0;
			var tmpWidth = 0;
			// 宽度过长导致树出滚动条的节点
			var overWidthNode = null;
			for (var i = 0; i < maxWidthArr.length; i++) {
				// 每一层比上层宽度?5px
				tmpWidth = (i + 1) * 25 + maxWidthArr[i];
				if (tmpWidth > maxNodeWidth) {
					maxNodeWidth = tmpWidth;
					overWidthNode = maxWidthNodes[i];
				}
			}
			// TODO 计算超出的宽?
			var overWidth = maxNodeWidth + 6 // 60
					- this.parentTreeNode.Div_gen.offsetWidth;
			if (overWidth > 0) {
				// 保存此节点展开时子节点增加的宽?
				this.overWidth = overWidth;
				this.refTree.resultScrollNodes.push(overWidthNode);
				return overWidth;
			} else
				return 0;
		},
		/**
		 * 得到此节点的所有孩子节?按层返回,每层中包括的节点是已经打开的节?
		 * @return{二级数组} 树组的第0个元素为孩子中第一级的所有打开节点的数??个元素为孩子中第二级所有打开节点的数?
		 * @private
		 */
		getLevelChildrenNodesArray : function() {
			var arr = new Array();
			var tempArr = new Array();
			if (this.hasChildren()) {
				for (var i = 0; i < this.childrenTreeNodes.length; i++) {
					tempArr.push(this.childrenTreeNodes[i]);
				}
				arr.push(tempArr);
				var tArr = this.getChildrenNodes(arr);
				return tArr;
			} else
				return null;
		},
		/**
		 * 得到该节点之前的所有兄弟节点和各个兄弟节点打开的子节点个数
		 */
		getPreVisibleSiblingCount : function() {
			var siblings = this.getPreviousSiblings();
			if (siblings == null || siblings.length == 0)
				return 0;
			var arr = [], tempArr = [];
			for (var i = 0, count = siblings.length; i < count; i++) {
				tempArr.push(siblings[i]);
			}
			arr.push(tempArr);
			var tArr = this.getChildrenNodes(arr);
			var nodesCount = 0;
			for (var i = 0; i < tArr.length; i++)
				nodesCount += tArr[i].length;
			return nodesCount;
		},
		/**
		 * @private 递归获得所有子层中每层打开节点的数?
		 */
		getChildrenNodes : function(arr) {
			var lastLevelNodes = arr[arr.length - 1];
			var node = null;
			var tempArray = new Array();
			for (var i = 0; i < lastLevelNodes.length; i++) {
				node = lastLevelNodes[i];
				// 此节点有孩子且处于打开状?则存储此节点的所有孩子节?
				if (node.hasChildren() && node.open == true) {
					for (var j = 0; j < node.childrenTreeNodes.length; j++)
						tempArray.push(node.childrenTreeNodes[j]);
				}
			}
			if (tempArray.length != 0) {
				arr.push(tempArray);
				return this.getChildrenNodes(arr);
			} else
				return arr;
		},
		/**
		 * 动态请求并加载此节点下的数据
		 * @private
		 */
		reloadNode : function() {
			if (this.reload == false)
				return;
		
			if (this.hasChildren()) {
				// 如果此节点下挂子节点中有"正在加载"节点,说明展开此节点时要去动态加载此节点下面的节?
				var reloadNodes = [];
				for (var i = this.childrenTreeNodes.length - 1; i >= 0; i--) {
					if (this.childrenTreeNodes[i].id
							.endWith(this.RELOADNODE_suffix))
						reloadNodes.push(this.childrenTreeNodes[i]);
				}
		
				var currDs = this.level.dataset;
				for (var i = 0; i < reloadNodes.length; i++) {
					var loadNode = reloadNodes[i];
					var childDataset = loadNode.loadLevel.dataset;
					var parameterMap = childDataset.reqParameterMap;
					parameterMap.clear();
					var dataset = loadNode.level.dataset;
					var key = loadNode.value;
					var keyValue = loadNode.nodeData.getCellValue(dataset
							.nameToIndex(key));
					if (keyValue != null) {
						parameterMap.put($.dataset.CONST.QUERY_PARAM_KEYS,
								loadNode.loadLevel.detailKeyParameter);
						parameterMap.put($.dataset.CONST.QUERY_PARAM_VALUES, keyValue);
		
						// 取父的masterKey的?
						var recursiveKeyField = loadNode.level.recursiveKeyField;
						var recursiveValue = loadNode.nodeData.getCellValue(dataset
								.nameToIndex(recursiveKeyField));
						parameterMap.put($.dataset.CONST.QUERY_RECURSIVEKEYFIELD,
								recursiveValue);
						childDataset.setReqParameterMap(parameterMap);
						if (currDs == childDataset) {
							// 记录当前?数据下载"节点
							childDataset.appendCurrentPage({
										'firstLoad' : false,
										'loadNode' : loadNode
									}, keyValue);
							loadNode.parentTreeNode.remove(loadNode.id);
						} else {
							// 记录当前?数据下载"节点
							childDataset.setCurrentPage(keyValue, null, {
										'firstLoad' : false,
										'loadNode' : loadNode
									});
							// 将ds的当前页的keyValue放在此节点的keyValue属性中
							this.keyValue = keyValue;
						}
					}
				}
			}
		},
		/**
		 * 改变caption的显示内?
		 * @param caption  要显示的新?
		 */
		changeCaption : function(caption) {
			this.caption = caption;
			if (this.painted == this.PAINTALL) {
				this.divText.removeChild(this.divText.childNodes[0]);
				this.divText.appendChild(document.createTextNode(caption));
				this.divText.title = caption;
			}
		},
		selectRoot : function() {
			if (TreeViewComp.currTreeComp == null)
				return;
			var rootNode = TreeViewComp.currTreeComp.rootNode;
			if (rootNode == null)
				return;
			rootNode.selNode();
		},
		/**
		 * 改变选中节点的外?只负责使节点变为选中样式)
		 */
		selNode : function() {
			var tree = this.refTree;
			if (tree == null || tree.Div_gen == null || tree.Div_gen.offsetWidth == 0)
				return;
			if (tree.selNode != null && tree.selNode != this && tree.selNode.level) {
				var selNodeDs = tree.selNode.level.dataset;
				// 当前选中节点和要选中的节点不在一个ds,则要设置上一个选中节点对应的ds的行非选中 gd 2009-09-03
				if (tree.selNode != null)
					tree.selNode.deSelNode();
			}
			// 如果该节点没有显示出来则先显示该节点
			if (this.parentTreeNode != null)
				tree.letNodeVisible(this);
			tree.selNode = this;
			// 改变选中节点外观
			if (!this.withCheckBox)
				this.focusNode();
//			if (!IS_IE || IS_IE8)
//				this.refTree.paintZone();
			// 显示修改删除按钮
			if (this.canEdit == true) {
				this.showHint();
			}
		},
		/**
		 * 当前选中的节点调用此方法会使此节点变为未选中状? 只能是当前树节点中处于选中状态的节点调用此方?
		 */
		deSelNode : function() {
			if (this.refTree != null && this.refTree.selNode != null
					&& this.refTree.selNode == this)
				this.refTree.selNode = null;
			this.nodeSelected = false;
		},
		/**
		 * 焦点行样式
		 */
		focusNode : function() {
			if (this.treeViewComp.focusNode != null
					&& this.treeViewComp.focusNode == this)
				return;
			if (this.treeViewComp.focusNode != null) {
				this.treeViewComp.focusNode.unFocusNode();
				this.treeViewComp.focusNode = null;
			}
			// 改变选中节点外观
			if (this.hasChildren()) {
				if (this.divText) {
					this.divText.className = "treenode_nodesel";
					// if(!(this.withCheckBox && !this.isClickTreeNode)){
					this.divRow.className = "treenode_on";
					//this.divRowLeft.className = "treenode_left_on";
					//this.divRowRight.className = "treenode_right_on";
					this.treeViewComp.focusNode = this;
					// }
					this.nodeSelected = false;
				} else {
					this.nodeSelected = true;
				}
			} else {
				if (this.divText) {
					this.divText.className = "treenode_textsel";
					// if(!(this.withCheckBox && !this.isClickTreeNode)){
					this.divRow.className = "treenode_on";
					//this.divRowLeft.className = "treenode_left_on";
					//this.divRowRight.className = "treenode_right_on";
					this.treeViewComp.focusNode = this;
					// }
					this.nodeSelected = false;
				} else {
					this.nodeSelected = true;
				}
			}
		},
		/**
		 * 取消焦点行样式
		 */
		unFocusNode : function() {
			if (this.divText) {
				this.divText.className = "treenode_text";
				if (this.isBold) {
					this.divText.style.fontWeight = "bold";
				}
				this.divRow.className = "";
				this.divRowLeft.className = "";
				this.divRowRight.className = "";
			}
		},
		/**
		 * 取消焦点行样式
		 */
		unFocusNode : function() {
			if (this.divText) {
				this.divText.className = "treenode_text";
				if (this.isBold) {
					this.divText.style.fontWeight = "bold";
				}
				this.divRow.className = "";
				this.divRowLeft.className = "";
				this.divRowRight.className = "";
			}
		},
		/**
		 * 移除掉此节点下的给定id的子结点
		 */
		remove : function(id) {
			var ind = this.isExist(id);
			if (ind != -1) {
				// 得到删除节点
				var node = this.childrenTreeNodes[ind];
				/*
				 * 得到node的所有子孙节?将此节点的painted属性设为未? 等此节点重新挂到树的某个节点下的时?这些子节点将重新painted自己
				 */
				var nodes = node.inorderTraversal();
				if (nodes != null && nodes.length > 0) {
					// 由于父节点改?这些节点将等到父节点挂接后重新绘?
					var lossParentNodes = "";
					for (var i = nodes.length - 1; i >= 0; i--) {
						nodes[i].painted = this.NOPAINT;
						lossParentNodes += nodes[i].caption + " | ";
					}
				}
				// 得到此节点上一个节?
				var preNode = node.previous();
				// 得到此节点下一个节?
				var nextNode = node.next();
				if (node.painted == this.PAINTALL) {
					// 从map中删除此节点的子节点
					this.removeChildNodeFromIdNodeMap(node);
					this.divChildren.removeChild(node.Div_gen);
					// 减少连接线高?
					if (this.DIVT.offsetHeight >= this.NODEHEIGHT)
						this.DIVT.style.height = this.DIVT.offsetHeight
								- this.NODEHEIGHT + "px";
					// 减少父节点连接线高度
					var parentNode = this.parentTreeNode;
					while (parentNode != null) {
						if (parentNode.DIVT.offsetHeight >= this.NODEHEIGHT)
							parentNode.DIVT.style.height = parentNode.DIVT.offsetHeight
									- this.NODEHEIGHT + "px";
						parentNode = parentNode.parentNode;
					}
		
					this.childrenTreeNodes.splice(ind, 1);
					// 没有孩子了则删除DIVT
					if (this.hasChildren() == false)
						this.divContent.style.display = "none";
		
					if (preNode != null)
						preNode.changeChildrenIcon();
					if (nextNode != null)
						nextNode.changeChildrenIcon();
					this.changeChildrenIcon();
					// 改变树的高度
					this.refTree.paintZone();
				}
		
				// 将该节点从父节点的子节点列表中删?
				var parentNode = node.parentTreeNode;
				if (parentNode) {
					var newChildNodesArray = new Array();
					for (var i = 0, n = parentNode.childrenTreeNodes.length; i < n; i++) {
						if (node != parentNode.childrenTreeNodes[i])
							newChildNodesArray.push(parentNode.childrenTreeNodes[i]);
					}
					parentNode.childrenTreeNodes = newChildNodesArray;
				}
		
				node.destroySelf();
				// 从map中删除此节点
				this.refTree.idNodeMap.remove(id);
		
			}
		},
		/**
		 * 从map中删除此节点的子节点
		 */
		removeChildNodeFromIdNodeMap : function(node) {
			var nodes = node.childrenTreeNodes;
			if (nodes != null) {
				for (var i = 0, n = nodes.length; i < n; i++) {
					var childNode = nodes[i];
					if (node.refTree.idNodeMap.containsKey(childNode.id))
						node.refTree.idNodeMap.remove(childNode.id);
					if (childNode.childrenTreeNodes != null
							&& childNode.painted == this.PAINTALL)
						this.removeChildNodeFromIdNodeMap(childNode);
				}
			}
		},
		/**
		 * 得到节点的所有孩?按前序遍历算?
		 * @return nodes树组
		 */
		preorderTraversal : function() {
			var children = new Array();
			var temp = this.getAllChildrenByPreorder(children);
			temp.push(this);
			return temp;
		},
		/**
		 * @private
		 */
		getAllChildrenByPreorder : function(children) {
			if (this.hasChildren()) {
				for (var i = this.childrenTreeNodes.length - 1; i >= 0; i--) {
					this.childrenTreeNodes[i].getAllChildrenByPreorder(children);
					children.push(this.childrenTreeNodes[i]);
				}
			}
			return children;
		},
		/**
		 * 得到节点的所有孩子，按中序遍历算?
		 */
		inorderTraversal : function() {
			var children = new Array();
			return this.getAllChildrenByInorder(children);
		},
		/**
		 * @private
		 */
		getAllChildrenByInorder : function(children) {
			if (this.hasChildren()) {
				for (var i = 0; i < this.childrenTreeNodes.length; i++) {
					children.push(this.childrenTreeNodes[i]);
					this.childrenTreeNodes[i].getAllChildrenByInorder(children);
				}
			}
			return children;
		},
		/**
		 * 判断指定node是否有孩子节?
		 */
		hasChildren : function() {
			if (this.childrenTreeNodes == null || this.childrenTreeNodes.length == 0)
				return false;
			else
				return true;
		},
		/**
		 * 判断给定节点是否存在于此节点的子节点
		 * @param nodeId 要查找的node的id
		 * @return 找到的节点在此节点下的位?
		 */
		isExist : function(nodeID) {
			if (!this.hasChildren())
				return -1;
			var trouve = false;
			var i = 0;
			var ind = -1; // 保存找到的节点的index?
			while (!trouve && i < this.childrenTreeNodes.length) {
				var node = this.childrenTreeNodes[i];
				if (node.id == nodeID) {
					trouve = true;
					ind = i;
				}
				i++;
			}
			return ind;
		},
		/**
		 * 设置此节点及其子节点的refTree属?
		 * @private
		 */
		setRefTree : function(refTree) {
			for (var i = 0; i < this.childrenTreeNodes.length - 1; i++) {
				var node = this.childrenTreeNodes[i];
				node.setRefTree(refTree);
			}
			this.refTree = refTree;
		},
		/**
		 * 给指定节点添加子结点
		 * @param node 树节点对?
		 */
		add : function(node) {
			this.insertBefore(node, null);
		},
		/**
		 * 将给定节点node插入到本节点下的nodeId节点之前，如果不指定nodeId则插入到此节点孩子节点的最后一?
		 * @param node 要插入的节点
		 * @param nodeId 要插入在此id指定的节点之?
		 */
		insertBefore : function(node, nodeId) {
			if (node == null)
				return;
			if (this.isExist(node.id) != -1)
				return;
			if (node.parentTreeNode)
				node.parentTreeNode.remove(node.id, false);
			node.parentTreeNode = this;
			node.refTree = this.refTree;
			// 把node的painted状态设为false
			node.painted = this.NOPAINT;
		
			// 改变叶子节点为文件夹节点,只有最后一级的节点允许从叶子节点变为文件夹节点
			if (this.isLeafNode() && this.refTree.isLastLevel(this.level))
				this.setLeaf(false);
		
			var ind = -1;
			if (nodeId != null) {
				ind = this.isExist(nodeId);
				if (ind != -1) {
					for (var i = this.childrenTreeNodes.length - 1; i >= ind; i--)
						this.childrenTreeNodes[i + 1] = this.childrenTreeNodes[i];
					this.childrenTreeNodes[ind] = node;
				}
			}
			// 不指定nodeId则插入到最?
			else {
				ind = this.childrenTreeNodes.push(node);
			}
			node.index = ind;
			if (this.painted == this.PAINTALL)
				node.changeChildrenIcon();
		},
		/**
		 * 改变此节点父节点img1的icon和本身img1的icon. 根据不同情况设置加号、减号图标的图片
		 * @private
		 */
		changeChildrenIcon : function() {
		
		},
		changeChildrenIcon_backup : function() {
			// this是根节点则不改变外观
			if (this.id == this.refTree.rootNode.id)
				return;
		
			// 得到此节点的父节?
			var pNode = this.parentTreeNode;
			if (pNode.hasChildren()) {
				/* 改变父节点图? */
				if (pNode.painted == this.PAINTALL) {
					// 父节点是同级节点中的最后一个节?
					if (pNode.isLast()) {
						pNode.srcPlus = window.themePath
								+ "/comps/tree/images/Lplus.gif";
						pNode.srcMoins = window.themePath
								+ "/comps/tree/images/Lminus.gif";
						if (pNode.id == this.refTree.rootNode.id)
							pNode.DIVT.style.display = "none";
						else {
							pNode.DIVT.style.background = "url(" + window.themePath
									+ "/comps/tree/images/I2.gif)";
							pNode.DIVT.style.display = "block";
						}
					} else {
						pNode.srcPlus = window.themePath
								+ "/comps/tree/images/Tplus.gif";
						pNode.srcMoins = window.themePath
								+ "/comps/tree/images/Tminus.gif";
						if (pNode.id == this.refTree.rootNode.id
								&& this.refTree.withRoot == false)
							pNode.DIVT.style.display = "none";
						else {
							pNode.DIVT.style.background = "url(" + window.themePath
									+ "/comps/tree/images/I.gif)";
							pNode.DIVT.style.display = "block";
						}
					}
					pNode.changeIcon();
				}
		
				/* 改变当前节点图标 */
				if (this.painted == this.PAINTALL) {
					if (this.hasChildren()) {
						// 有孩子并且没有下一个兄?
						if (this.next() == null) {
							this.srcPlus = window.themePath
									+ "/comps/tree/images/Lplus.gif";
							this.srcMoins = window.themePath
									+ "/comps/tree/images/Lminus.gif";
							// 变此节点的divT为空?gd check 09-06
							this.DIVT.style.background = "url(" + window.themePath
									+ "/comps/tree/images/I2.gif)";
						}
						// 有孩子并且有下一个兄?
						else {
							this.srcPlus = window.themePath
									+ "/comps/tree/images/Tplus.gif";
							this.srcMoins = window.themePath
									+ "/comps/tree/images/Tminus.gif";
						}
					} else {
						// 没有孩子并且没有下一个兄?
						if (this.next() == null) {
							this.srcPlus = window.themePath
									+ "/comps/tree/images/L.gif";
							this.srcMoins = window.themePath
									+ "/comps/tree/images/L.gif";
						}
						// 没有孩子并且有下一个兄?
						else {
							this.srcPlus = window.themePath
									+ "/comps/tree/images/T.gif";
							this.srcMoins = window.themePath
									+ "/comps/tree/images/T.gif";
						}
					}
					this.changeIcon();
				}
		
				/* 改变此节点的上一个兄弟节点图? */
				var previousNode = this.previous();
				if (previousNode != null) {
					if (previousNode.painted == this.PAINTALL) {
						if (previousNode.childrenTreeNodes != null
								&& previousNode.childrenTreeNodes.length > 0) {
							previousNode.srcPlus = window.themePath
									+ "/comps/tree/images/Tplus.gif";
							previousNode.srcMoins = window.themePath
									+ "/comps/tree/images/Tminus.gif";
							if (previousNode.open == true)
								previousNode.DIVT.style.background = "url("
										+ window.themePath
										+ "/comps/tree/images/I.gif)";
						} else {
							previousNode.srcPlus = window.themePath
									+ "/comps/tree/images/T.gif";
							previousNode.srcMoins = window.themePath
									+ "/comps/tree/images/T.gif";
						}
						previousNode.changeIcon();
					}
				}
			}
		},
		/**
		 * 加号、减号图片和文件夹图片的开关变?根据创建节点时设置的叶子、文件夹图标改变img2,根据changeChildrenIcon中设置的加号、减号改变img1)
		 * @private
		 */
		changeIcon : function() {
			if (this.painted != this.PAINTALL)
				return;
		
			if (this.open) {
				if (this.srcMoins)
					this.img1.src = this.srcMoins;
				this.img2.src = this.icon2;
				// if(!this.isLeaf)
				// this.img2.src = window.themePath + "/images/treeview/folder_on.gif";
			} else {
				if (this.srcPlus)
					this.img1.src = this.srcPlus;
				this.img2.src = this.icon1;
				// if(!this.isLeaf)
				// this.img2.src = window.themePath + "/images/treeview/folder_off.gif";
			}
		},
		/**
		 * 得到此节点之前所有未画的兄弟节点
		 */
		getPreviousNoPaintSiblings : function() {
			var arr = [];
			var allSiblings = this.parentTreeNode.childrenTreeNodes;
			for (var i = 0, count = allSiblings.length; i < count; i++) {
				if (allSiblings[i].id == this.id)
					break;
				else {
					if (allSiblings[i].painted == this.NOPAINT)
						arr.push(allSiblings[i]);
				}
			}
			return arr;
		},
		/**
		 * 得到此节点之前的所有兄弟节?
		 * @return 此节点的之前的所有兄弟节?没有兄弟节点返回null
		 */
		getPreviousSiblings : function() {
			var arr = [];
			var allSiblings = this.parentTreeNode.childrenTreeNodes;
			for (var i = 0, count = allSiblings.length; i < count; i++) {
				if (allSiblings[i].id == this.id)
					break;
				else
					arr.push(allSiblings[i]);
			}
			return arr;
		},
		/**
		 * 返回给定孩子节点在父节点下的索引
		 * @param TreeNode childNode 孩子节点
		 */
		getIndexOfChild : function(childNode) {
			if (!(childNode instanceof TreeNode))
				return;
			if (this.isExist(childNode.id) != -1)
				return this.childrenTreeNodes.indexOf(childNode);
		},
		/**
		 * 返回指定索引处的孩子节点
		 * @param int index 索引
		 */
		getChild : function(index) {
			if (this.hasChildren() && this.childrenTreeNodes.length - 1 >= index)
				return this.childrenTreeNodes[index];
		},
		/**
		 * 返回此节点下的孩子数?
		 * @return 如果没有?或者是叶子节点返回0
		 */
		getChildCount : function() {
			if (this.hasChildren())
				return this.childrenTreeNodes.length;
			else
				return 0;
		},
		/**
		 * 判断此节点是否是当前父节点的最后一个节?
		 */
		isLast : function() {
			if (!this.root)
				return (this.parentTreeNode.childrenTreeNodes[this.parentTreeNode.childrenTreeNodes.length
						- 1] == this);
			else
				return true;
		},
		/**
		 * 判断此节点是否是当前父节点的第一个节?
		 */
		isFirst : function() {
			if (!this.root)
				return (this.parentTreeNode.childrenTreeNodes[0] == this);
			else
				return true;
		},
		/**
		 * 若指定节点有子节点，则返回第一个子节点，否则返回null
		 */
		first : function() {
			if (this.hasChildren())
				return (this.childrenTreeNodes[0]);
			else
				return null;
		},
		/**
		 * 若指定节点有子节点，则返回最后一个子节点，否则返回null
		 */
		last : function() {
			if (this.hasChildren())
				return (this.childrenTreeNodes[this.childrenTreeNodes.length - 1]);
			else
				return null;
		},
		/**
		 * 若指定节点有下一个节点则返回下一个节点，否则返回null
		 */
		next : function() {
			if (!this.root) {
				var index = this.parentTreeNode.childrenTreeNodes.indexOf(this);
				if (index < this.parentTreeNode.childrenTreeNodes.length - 1)
					return this.parentTreeNode.childrenTreeNodes[index + 1];
				else
					return null;
			} else
				return null;
		},
		/**
		 * 若指定节点有上一个节点则返回上一个节点，否则返回null
		 */
		previous : function() {
			if (!this.root) {
				var index = this.parentTreeNode.childrenTreeNodes.indexOf(this);
				if (index > 0)
					return this.parentTreeNode.childrenTreeNodes[index - 1];
				else
					return null;
			} else
				return null;
		},
		/**
		 * 激发树节点的右键菜?
		 * @private
		 */
		contextmenu : function(e) {
			var ds = this.level.dataset;
		
			// 选中该行
			var rowId = this.nodeData.rowId;
			var index = ds.getRowIndexById(rowId);
			ds.setRowSelected(index);
		
			// 从当前节点所属的level中获取contextMenu对象
			var level = this.refTree.getLevelByDataset(ds);
			// 用户可以阻止contextMenu的弹?
			if (this.refTree.onBeforeContextMenu(this, ds, level) == false) {
				stopEvent(e);
				stopDefault(e);
				return;
			}
		
			var contextMenu = level.contextMenu;
			if (contextMenu != null) {
				contextMenu.triggerObj = this;
				contextMenu.left = 0;
				contextMenu.show(e);
				stopEvent(e);
				stopDefault(e);
			}
		},
		/**
		 * @private
		 */
		refreshReloadNode : function(node) {
			this.remove(this.noeudReload.id);
			this.add(node);
		},
		/**
		 * 得到node所在的level对象
		 * @return TreeLevel
		 */
		getTreeLevel : function() {
			return this.level;
		},
		/**
		 * 得到node所在的dataset
		 * @return Dataset
		 */
		getDataset : function() {
			return this.level.dataset;
		},
		getMasterKeyValue : function() {
			var key = this.level.masterKeyField;
			var keyValue = this.nodeData.getCellValue(this.level.dataset
					.nameToIndex(key));
			return keyValue;
		},
		/**
		 * 生成节点的JSON对象及子节点
		 * @private
		 * genChildType: no_child/child_level1/child_allLevel
		 */
		generateNodeJsonObj : function(nodeJs, genParent, genChildType,
				nodeJson) {
			var rootJsonObj = null;
			if (nodeJson == null)
				nodeJson = new Object;
			nodeJson.javaClass = "com.haiyou.wfw.common.comp.WebTreeNode";
			nodeJson.id = nodeJs.id;
			if (nodeJs.nodeData)
				nodeJson.rowId = nodeJs.nodeData.rowId;
			nodeJson.label = nodeJs.caption;
			if (genChildType != "no_child") {
				var childNodeListObj = new Object;
				childNodeListObj.javaClass = "java.util.ArrayList";
				childNodeListObj.list = new Array();
				var childNodes = nodeJs.childrenTreeNodes;
				if (childNodes) {
					for (var i = 0, n = childNodes.length; i < n; i++) {
						var childType = "no_child";
						if (genChildType == "child_allLevel")
							childType = "child_allLevel";
						var childJsonObj = this.generateNodeJsonObj(childNodes[i],
								false, childType);
						childJsonObj.parentNode = nodeJson.id;
						childNodeListObj.list.push(childJsonObj);
					}
				}
				nodeJson.childNodeList = childNodeListObj;
			}
			if (genParent && nodeJs.parentTreeNode != null) {
				var parentJsonObj = this.generateNodeJsonObj(nodeJs.parentTreeNode,
						true, "no_child");
				nodeJson.parentNode = nodeJs.parentTreeNode.id;
				var parentJsonObj = new Object;
				var childNodeListObj = new Object;
				childNodeListObj.javaClass = "java.util.ArrayList";
				childNodeListObj.list = new Array();
				childNodeListObj.list.push(nodeJson);
				parentJsonObj.childNodeList = childNodeListObj;
		
				rootJsonObj = this.generateNodeJsonObj(nodeJs.parentTreeNode, true,
						"no_child", parentJsonObj);
		
			}
			if (rootJsonObj != null)
				return rootJsonObj;
			else
				return nodeJson;
		},
		/**
		 * @private
		 */
		getRootJsonObj : function(nodeJson) {
			var rootJson = nodeJson;
			while (rootJson.parentNode != null) {
				rootJson = rootJson.parentNode;
			}
			return rootJson;
		}
    });
    
})(jQuery);

















function openNodesByLevel(level, treeId) {
	//if (TreeViewComp.currTreeComp != null)
		//TreeViewComp.currTreeComp.openNodesByLevel(level);
};

/**
 * tree面板滚动处理函数。设30毫秒延迟
 * @private
 */
function handleTreeScrollEvent(e) {
	handleTreeScrollEvent.triggerObj = e.triggerObj;
	if (handleTreeScrollEvent.timer != null)
		clearTimeout(handleTreeScrollEvent.timer);
	handleTreeScrollEvent.timer = setTimeout("doTreeScroll()", 30);
};


/**
 * tree面板滚动处理函数。设30毫秒延迟
 * @private
 */
function handleTreeScrollEvent(e) {
	handleTreeScrollEvent.triggerObj = e.triggerObj;
	if (handleTreeScrollEvent.timer != null)
		clearTimeout(handleTreeScrollEvent.timer);
	handleTreeScrollEvent.timer = setTimeout("doTreeScroll()", 30);
};

/**
 * 滚动后画当前显示区域
 * 
 * @private
 */
function doTreeScroll() {
	handleTreeScrollEvent.triggerObj.paintZone();
};

function TreeImgRender(cell, treeNode) {
	if (treeNode.nodeData != null) {
		var img = $("<img>").get(0);
		// this.img1Cell.appendChild(this.img1);
		img.src = this.icon2;
		// img.className = "treenode_img";
		// this.img1.style.position = "relative";
		img.style[$.CONST.ATTRFLOAT] = "left";
		cell.appendChild(img);
	}

}


/**
 * 处理图片1点击方法
 * @private
 */
function handleTreeNodeImg1Click(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
		return;
	}
	oThis.toggle();

	// 在禁止掉事件进一步传播时先掉用系统注册方?
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
	oThis = null;
};
/**
 * 节点文字点击方法
 */
function handleTreeNodeTextClick(e, oThis) {
	e = EventUtil.getEvent();
	if (oThis.owner.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
		return;
	}
	oThis.owner.toggle();

	// 在禁止掉事件进一步传播时先掉用系统注册方?
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
	oThis = null;
};

/**
 * 处理图片2点击方法
 * 
 * @private
 */
function handleTreeNodeImg2Click(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
		return;
	}
	// 使文件夹的点击处理和加号的点击处理效果一?
	oThis.toggle();
	// 在禁止掉事件进一步传播时先掉用系统注册方?
	document.onclick();
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
	oThis = null;
};

/**
 * 处理图片2双击方法
 */
function handleTreeNodeImg2Dblclick(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
		return;
	}
	// 图片的双击事件不应该触发节点的双击事?
	// oThis.dblclick(oThis);
	stopEvent(e);
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
};

/**
 * 处理右键菜单方法
 * 
 * @private
 */
function handleTreeNodeContextMenu(e) {
	e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false) {
		// 删除事件对象（用于清除依赖关系）
//		clearEventSimply(e);
		return;
	}
	if (oThis.root == false) {
		oThis.contextmenu(e);
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
}

/**
 * 处理树节点双击方?
 * 
 * @private
 */
function handleTreeNodeDblclick(e) {
	// e = EventUtil.getEvent();
	var oThis = this.owner;
	if (oThis.refTree.isTreeActive == false)
		return;
	// 用户阻止默认的双击行为执?
	if (oThis.dblclick(oThis) == false)
		return;
	// 使文字的双击处理和文件夹的点击处理效果一?
	else
		oThis.toggle();
	oThis = null;
};

/**
 * 处理树节点单击方?
 * 
 * @private
 */
function handleTreeNodeClick(e, treeNode) {
	var oThis = treeNode.owner;
	var tree = oThis.refTree;
	if (tree.isTreeActive == false)
		return;
	var newNode = oThis, oldNode = tree.selNode;

	e = EventUtil.getEvent();

	// 如果当前点击节点不是选中的节?
	if (newNode != tree.selNode) {
		//var ds = treeNode.level.dataset;
		//ds.setRowUnSelected(ds.getRowIndex(oldNode.nodeData));
		// if (typeof(newNode.treeViewComp.focusNode) != "undefined" &&
		// newNode.treeViewComp.focusNode != null &&
		// newNode.treeViewComp.focusNode != newNode){
		// newNode.treeViewComp.focusNode.unFocusNode();
		// }
		// newNode.focusNode();
		// newNode.treeViewComp.focusNode = newNode;
		// 复选状态隐藏复选框,调用复选框事件改变状?
		if (oThis.withCheckBox) {
			oThis.checkbox.onclick(e);
			return;
		}
		// 在选中节点改变之前调用用户的处理方?返回false则阻止选中节点改变
		if (tree.beforeSelNodeChange(newNode, oldNode) == false) {
			document.onclick();
			stopEvent(e);
			// 删除事件对象（用于清除依赖关系）
//			clearEventSimply(e);
			return;
		}
		// 将即将选中的节点存储在树的即将选中节点属性中
		tree.nextSelNode = newNode;

		// 内置根节点处于显示状态被点击
		if (newNode.root == true && tree.withRoot == true) {
			if (tree.withCheckBox == false) { // 没有checkbox情况?
				// 选中节点从其他有ds的节点跳转到根节点的时候要设置oldNode节点不选中,避免再回到oldNode时不能选中的问?
				if (oldNode != null && oldNode != oThis) {
					var ds = oldNode.level.dataset;
					ds.setRowUnSelected(ds.getRowIndex(oldNode.nodeData));
					ds.setRowSelected(-1);

					// 选中根节?
					tree.rootNode.selNode();
					tree.click(tree.rootNode);
				}
				// 第一次在没有任何节点选中的情况下直接点击根结?
				else if (oldNode == null) {
					tree.rootNode.selNode();
					tree.click(tree.rootNode);
				}
			}
		} else {
			// 清除根节点的选中状?
			if (tree.withRoot == true)
				tree.rootNode.deSelNode();
			// 通知dataset选中节点
			var ds = oThis.level.dataset;
			if (ds != null) {
				// 获取树有几级level
				if (tree.levelCount == null)
					tree.levelCount = tree.getAllLevels().length;
				var lastLevelNode = oThis;
				var index = null;
				// 从本级任何一个节点找到上一级的节点,得到keyValue(一级以上情?
				if (tree.levelCount > 1) {
					while (lastLevelNode.level == oThis.level) {
						// 避免lastLevelNode为根节点
						if (lastLevelNode.parentTreeNode.root == true)
							break;
						lastLevelNode = lastLevelNode.parentTreeNode;
					}

					// 第二级以后节点点击才需要设置keyValue
					if (oThis.level != tree.topTreeLevel) {
						var keyValue = lastLevelNode.keyValue;
						if (keyValue == null) {
							alert(lastLevelNode.caption + " keyValue is null!");
							// 删除事件对象（用于清除依赖关系）
//							clearEventSimply(e);
							return;
						}

						index = ds.getRowIndex(oThis.nodeData, 0, keyValue);

						// 对于一个ds,对于keyvalue的每次切换都要调用setRowUnSelected把上次的选中节点置为不选中,
						// 避免在次选中此keyvalue下的同一个节点时不会选中的情?
						if (oldNode != null && oldNode.level == oThis.level
								&& keyValue != ds.currentKey)
							oldNode.level.dataset.setRowUnSelected(
									oldNode.level.dataset
											.getRowIndex(oldNode.nodeData), 0,
									ds.currentKey);

						if (tree.levelCount > 1 && keyValue != ds.currentKey)
							ds.setCurrentPage(null, {
										'firstLoad' : false
									});
//							ds.setCurrentPage(keyValue, null, {
//										'firstLoad' : false
//									});
						tree.prapareFocusIndex = index;
					} else {
						// 如果第二级树ds的currentKey不等于当前树节点key,更新第二级树的ds.currentkey
						var levels = tree.getAllLevels();
						for (var i = 0; i < levels.length; i++) {
							if (levels[i][0].id == "level2") {
								var subDs = levels[i][0].dataset;
								if (oThis.keyValue == null) {
									// 此节点第一次打开的时候需要动态加载节?
									// (解决点击树节点时，虽然加载了二级树对应ds数据，但是没创建对应的treeNode)
									if (newNode.reload) {
										newNode.reloadNode();
									} else {
										var keyValue = oThis
												.getMasterKeyValue();
										if (keyValue != null) {
											var parameterMap = subDs.reqParameterMap;
											parameterMap.clear();
											parameterMap
													.put(
															$.dataset.CONST.QUERY_PARAM_KEYS,
															levels[i][0].detailKeyParameter);
											parameterMap
													.put(
															$.dataset.CONST.QUERY_PARAM_VALUES,
															keyValue);
											subDs
													.setReqParameterMap(parameterMap);
											subDs.setCurrentPage(
													null, {
														'firstLoad' : false
													});
//											subDs.setCurrentPage(keyValue,
//													null, {
//														'firstLoad' : false
//													});
											oThis.keyValue = keyValue;
										}
									}
								} else if (oThis.keyValue != subDs.currentKey)
								subDs.setCurrentPage(null,
											{
												'firstLoad' : false
											});
//									subDs.setCurrentPage(oThis.keyValue, null,
//											{
//												'firstLoad' : false
//											});
								break;
							}
						}
						index = ds.getRowIndex(oThis.nodeData);
					}

					// 得到上一个选中的节?如果不是同一个ds,要先设置上一个ds的此节点非选中,避免在次选中此ds下的同一个节点时不会选中的情?
					if (oldNode != null) {
						if (oldNode.root) // 如果是根节点
							tree.rootNode.deSelNode();
						else if (oldNode.level && oldNode.level.dataset != null
								&& oThis.level.dataset != null
								&& oldNode.level.dataset != oThis.level.dataset)
							oldNode.level.dataset
									.setRowUnSelected(oldNode.level.dataset
											.getRowIndex(oldNode.nodeData));
					}
				} else
					index = ds.getRowIndex(oThis.nodeData);
				if (tree.withCheckBox == true)
					ds.addRowSelected(index);
				else
					ds.setRowSelected(index);

				// 设置聚焦?
				ds.setFocusRowIndex(index);
				// 二级树可能会发onload请求，把focusIndex清掉，这里预置一个FocusIndex,再下一次FocusEvent中清空;
			}
		}
	}
	// 点击的是同一个节?
	else {
		if (oThis.withCheckBox) { // 复选状态隐藏复选框,调用复选框事件改变状?
			oThis.checkbox.onclick(e);
		} else {
			// true表示点击树节点时点击的是同一个节?
			tree.onclick(newNode, true);
		}
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
}

/**
 * 处理树节点鼠标移入方?
 * 
 * @private
 */
function handleTreeNodeMouseMove(e) {
	var oThis = this.owner;
	if (oThis.refTree.isDragEnabled == false)
		return;

	if (oThis.refTree.isTreeActive == false)
		return;

	e = EventUtil.getEvent();

	// IE?表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if ((e.button == 1 || e.button == 0) && window.dragStart) {
		// if (e.button == 1) {
		var tree = oThis.refTree;
//		if (IS_IE) // 仅IE中有此方?
//			window.oDrag.setCapture();
		oThis.refTree.bDragStart = true;
		tree.dragDiv.style.display = "block";
		// TODO
		// tree.dragDiv.style.left = e.x ? e.x : e.pageX - tree.iDiffX + "px";
		// tree.dragDiv.style.top = e.y ? e.y : e.pageY - tree.iDiffY + "px";
//		if (IS_IE) {
//			tree.dragDiv.style.left = e.x + 10 + "px";
//			tree.dragDiv.style.top = e.y + "px";
//		} else {
			tree.dragDiv.style.left = e.clientX - 185 + "px";
			tree.dragDiv.style.top = e.clientY - 25 + "px";
			if (tree.Div_gen.scrollWidth > tree.Div_gen.clientWidth) { // 出现横向滚动?
				tree.dragDiv.style.left = e.clientX - 185
						+ (tree.Div_gen.scrollWidth - tree.Div_gen.clientWidth)
						+ "px";
			}
			if (tree.Div_gen.scrollHeight > tree.Div_gen.clientHeight) { // 出现纵向滚动?
				tree.dragDiv.style.top = e.clientY
						- 25
						+ (tree.Div_gen.scrollHeight - tree.Div_gen.clientHeight)
						+ "px";
			}
//		}
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
	oThis = null;
}

/**
 * 处理树节点鼠标点下方?
 * 
 * @private
 */
function handleTreeNodeMouseDown(e, divText, oThis) {

	// 设置不能拖动树则返回不处?
	if (divText.owner.refTree.isDragEnabled == false)
		return;

	if (divText.owner.refTree.isTreeActive == false)
		return;

	e = EventUtil.getEvent();

	// IE?表示无按键动作，1为鼠标左键；firefox中无按键动作和按鼠标左键都是0
	if (e.button == 1 || e.button == 0) {
		// if (e.button == 1) {
		/* 处理拖拽初始化事? */
		window.dragStart = true;
		var tree = divText.owner.refTree;
		window.oDrag = getTarget(e);

		var dragDiv = tree.onDragStart(divText.owner);
		// 调用用户的处理方?
		if (dragDiv == false) {
			// 删除事件对象（用于清除依赖关系）
//			clearEventSimply(e);
			return;
		}

		if (dragDiv == null)
			dragDiv = tree.createDragDiv(divText);
		dragDiv.style.position = "absolute";
		dragDiv.style.display = "none";
		document.body.appendChild(dragDiv);
		tree.dragDiv = dragDiv;
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
};


/**
 * 处理树节点鼠标抬起方?
 * 
 * @private
 */
function handleTreeNodeMouseUp(e) {
	window.dragStart = false;
	var tree = this.owner.refTree;
	if (tree.dragDiv) {
		var dragDiv = tree.dragDiv;
		tree.dragDiv = null;
		document.body.removeChild(dragDiv);
	}
	if (this.owner.refTree.isDragEnabled == false)
		return;
	if (this.owner.refTree.isTreeActive == false)
		return;
	e = EventUtil.getEvent();
	if (tree.bDragStart != null && tree.bDragStart == true) {
		// 得到要拖放到的目标位?
		var targetNode = getTarget(e).owner;
		// 要拖放的源节?
		var sourceNode = window.oDrag.owner;
		// 如果目标位置不是node,或者仍然是原来node则返?
		if (!this.isPrototypeOf(targetNode)
				|| targetNode == sourceNode) {
//			if (IS_IE)
//				window.oDrag.releaseCapture();
			// 删除事件对象（用于清除依赖关系）
//			clearEventSimply(e);
			return;
		}
		// 在全局中保存源节点和目标节?
		tree.targetNode = targetNode;
		tree.sourceNode = sourceNode;
		// 返回false表示要组织默认的处理事件,由用户处?
		tree.onDragEnd(sourceNode, targetNode);
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
}

/**
 * 处理树节点鼠标进入方?
 * 
 * @private
 */
function handleTreeNodeMouseOver(e, oThis) {
	if (!(oThis.divRow.className && oThis.divRow.className == 'treenode_on')) {
		oThis.divRow.className = "treenode_over";
		//oThis.divRowLeft.className = "treenode_left_over";
		//oThis.divRowRight.className = "treenode_right_over"
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
}

/**
 * 处理树节点鼠标离开方法
 * 
 * @private
 */
function handleTreeNodeMouseOut(e, oThis) {
	if (!(oThis.divRow.className && oThis.divRow.className == 'treenode_on')) {
		oThis.divRow.className = "";
		oThis.divRowLeft.className = "";
		oThis.divRowRight.className = "";
	}
	// 删除事件对象（用于清除依赖关系）
//	clearEventSimply(e);
}

/**
 * 树节点编辑按?
 * 
 * @param {}
 *            e
 */
function handleTreeNodeImgEditClick(e) {
	var oThis = this.owner;
	e = EventUtil.getEvent();
	oThis.divText.style.visibility = "hidden";
	oThis.divHint.style.visibility = "hidden";
	// 创建编辑输入?absolute relative oThis.divTextOut
	var width = oThis.divText.style.width.replace("px", "");
	width = parseInt(width) + 10;
	width = 120;
	// 当前?
	var rowIndex = oThis.level.dataset.getRowIndex(oThis.nodeData);
	var colIndex = oThis.level.dataset.nameToIndex(oThis.level.labelFields[0]);
	var value = oThis.value; // oThis.level.dataset.getValueAt(rowIndex,
								// colIndex);
	if (value == null) {
		value = "";
	}
	if (oThis.comp == null) {
		oThis.comp = new StringTextComp(oThis.divTextOut, "text", 0, 0, width,
				"absolute", "", "");
		oThis.comp.newValue = value;
		var textListener = new TextListener();
		textListener.valueChanged = function(valueChangeEvent) {
			TreeViewComp.compValueChangeFun(oThis, valueChangeEvent);
			oThis.comp.hide();
			oThis.divText.style.visibility = "";
			oThis.divHint.style.visibility = "";
		};
		oThis.comp.addListener(textListener);
		// 焦点移出事件
		var compFocusListener = new FocusListener();
		compFocusListener.onBlur = function(focusEvent) {
			// 调用选中节点的onblur方法
			selNodeOnblur(oThis);
		};
		oThis.comp.addListener(compFocusListener);
	} else {
		oThis.comp.newValue = value;
		oThis.comp.show();
	}
	oThis.comp.setFocus();
};

/**
 * 处理编辑框失去焦点后的显?
 */
function selNodeOnblur(tree) {
	// 隐藏编辑控件
	tree.comp.hide();
	tree.divText.style.visibility = "";
	tree.divHint.style.visibility = "";
}

function handleTreeNodeImgDeleteClick(e) {
	var oThis = this.owner;
	e = EventUtil.getEvent();	
	oThis.refTree.onNodeDelete(oThis);
}