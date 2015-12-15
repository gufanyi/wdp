(function($) {
	window.onkeydown=keyDownHandler;
	$.design = function(opitons) {
		this.options= opitons|| {};
		if (arguments.length == 0)
			return;
		this.divObj = opitons.divObj;
		if (!this.divObj) {
			return;
		}
		this.params = opitons.params;
		this.initObjAttribute();
		this.objType = $.argumentutils.getString(opitons.objType,$.design.Constant.LAYOUT_TYPE);
		this.divObj.objType = this.objType;
		this.toEditableObj();
		if (this.objType == $.design.Constant.COMPOMENT_TYPE) {
			this.toDragableObj();//控件可以拖动
		}
		//TODO:
		if(this.params.type == "tree") {
			var _viewPart = pageUI.getViewPart(this.params.widgetid);
			var _tree =_viewPart.getComponent(this.params.uiid);
			var _treeDs = $.dataset.getObj({
				id : 'designTreeDs',
				meta:[
					{key:"id",value:"id",dataType:"String"},
					{key:"pid",value:"pid",dataType:"String"},
					{key:"name",value:"name",dataType:"String"}
					],
				editable : true,
				pageSize : 0
			});
			_treeDs.viewpart = _viewPart;
			_treeDs.hasComp = true;
	        _treeDs.setData({
				"allPage": {
					"pageDatas": [
						{
							"changed": true,
	                        "onePage": {
	                            "entryRows": [
	                                {
	                                    "ch": [0,2],
	                                    "content": ["id1","。","根节点"],
	                                    "id": "desginTreeRow1",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id2","id1","子节点1"],
	                                    "id": "desginTreeRow2",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id3","id1","子节点2"],
	                                    "id": "desginTreeRow3",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id4","id1","子节点3"],
	                                    "id": "desginTreeRow4",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id5","id1","子节点4"],
	                                    "id": "desginTreeRow5",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id6","id3","子节点5"],
	                                    "id": "desginTreeRow6",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id7","id3","子节点6"],
	                                    "id": "desginTreeRow7",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id8","id3","子节点7"],
	                                    "id": "desginTreeRow8",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id9","id3","子节点8"],
	                                    "id": "desginTreeRow9",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id10","id3","子节点9"],
	                                    "id": "desginTreeRow10",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id11","id3","子节点10"],
	                                    "id": "desginTreeRow11",
	                                    "isEdit": true,
	                                    "state": "add"
	                                },
	                                {
	                                    "ch": [0,1,2],
	                                    "content": ["id12","id3","子节点11"],
	                                    "id": "desginTreeRow12",
	                                    "isEdit": true,
	                                    "state": "add"
	                                }
	                            ]
	                        },
	                        "pageindex": 0
	                    }
	                ],
	                "pagecount": 1,
	                "pageindex": 0,
	                "pagesize": -1,
	                "recordcount": 0
	            },
	            "editable": true,
	            "focusIndex": -1,
	            "id": "designTreePagesetDs",
	            "isCleared": true,
	            "randomRowIndex": 18
	        });
	        _tree.setTreeLevel(new TreeLevel("desginTreeLevel", _treeDs,"Recursive","id","pid",["name"],"id","null",null,""));
		}
		this.addContextMenu();
	};
	
	$.design.getObj = function(opts) {
		return new $.design(opts);
	}
	
	$.extend($.design, {
		
		Constant : {
			COMPOMENT_TYPE : "component",
			LAYOUT_TYPE : "layout",
			PANEL_TYPE : "panel",
			PAGEUIMETA_TYPE : "pageuimeta",
			GRID_LAYOUT_TYPEL : "grid_layout",
			GRID_PANEL_TYPE : "grid_panel",
			EDIT_LAYOUT_PANEL : "editable",
			EDIT_LAYOUT_VPANEL : "editable_vPanel",
			EDIT_LAYOUT_HPANEL : "editable_hPanel",
			EDIT_COMPONENT : "edit_component",
			EDIT_COMPONENT_SELECTED : "edit_component_selected",
			EDIT_SELECTED : "edit_selected",
			EDIT_GRIDLAYOUT : "edit_gridLayout",
			EDIT_GRIDLAYOUT_SELECTED : "edit_gridLayout_selected",
			
			/**
			 * 操作类型常量，对应右键菜单的项
			 * 
			 * @type {String}
			 */
			CONTEXTMENUADD : "add",
			CONTEXTMENUDELETE : "delete",
			CONTEXTMENUSETTING : "setting",
			UPDATEID : "updateid",
			CONTEXTMENUCREATECOMBO : "createcombo",
			CONTEXTMENUNEXTPAGE : "nextPage",
			CONTEXTMENULASTPAGE : "lastPage"
		},
		variable : {
			editingObj : null, // 正在被编辑的对象，一次只能编辑一个对象
			editingObjP : null,
			
			// 定义两个右键菜单，一个为布局使用，一个为控件使用
			allContextMenus : [],
			compContextMenu : null, // 控件右键菜单
			gridCompContextMenu : null, //grid控件右键菜单
			formCompContextMenu : null, //form控件右键菜单
			menuCompContextMenu : null, //menu控件右键菜单
			toolbarCompContextMenu : null, //menu控件右键菜单
			layoutContextMenu : null, // 布局右键菜单
			panelContextMenu : null, // panel右键菜单
			
			/**
			 * 纵向布局Panel右键菜单
			 */
			flowvPanelContextMenu : null,
			/**
			 * 横向布局Panel右键菜单
			 */
			flowhPanelContextMenu : null,
			/**
			 * 页签布局item右键菜单
			 */
			tabItemContextMenu : null,
			/**
			 * 页签布局右键菜单
			 */
			tabContextMenu : null,
			/**
			 * 卡片布局panel右键菜单
			 */
			cardPanelContextMenu : null,
			/**
			 * 百叶窗panel右键菜单
			 */
			outlookbarItemContextMenu : null,
			/**
			 * gridColumn右键菜单
			 * @type 
			 */
			gridColumnContextMenu : null,
			/**
			 * 表格Panel右键菜单
			 * @type 
			 */
			gridPanelContextMenu : null,
			/**
			 * 卡片布局的右键，需要添加上一页 下一页
			 * 
			 * @type {}
			 */
			cardContextMenu : null,
			/**
			 *  自由布局的右键
			 *  
			 *  @type {}
			 */
			absLayoutContextMenu : null
		},
		dragStart : function() {
			var e = EventUtil.getEvent();
			// 针对点击的源和事件，判断是否能进行拖动操作
			var allowDrag = true;
			allowDrag = $.design.checkDragElement(e);
			if (!allowDrag) {
				return;
			}
			// 右键，返回
			if (e.button == 2)
				return;
			this.bDragStart = true;
			if (window.dragObj == null) {
				var sourceObj = e.target.parentNode.parentNode;
				var dragObj = sourceObj.cloneNode(true);
				window.dragSourceObj=sourceObj;
				dragObj.style.position = "absolute";
				dragObj.style.height = sourceObj.offsetHeight + "px";
				dragObj.style.width = sourceObj.offsetWidth + "px";
				dragObj.style.border = "1px solid red";
				if(sourceObj.parentNode.type==="absolutelayout"){
					sourceObj.parentNode.appendChild(dragObj);
					dragObj.style.display = "none";
					window.dragObj = dragObj;
					window.currentDropObj = this.eleid;
					var flagStyle = window.dragObj.style;
					flagStyle.left = (e.offsetX +e.target.parentNode.parentNode.offsetX+ 3) + "px";
					flagStyle.top = (e.offsetY +e.target.parentNode.parentNode.offsetY+ 3) + "px";
					dragObj.onkeydown=keyDownHandler;
				}else{
					document.body.appendChild(dragObj);
					dragObj.style.display = "none";
					window.dragObj = dragObj;
					window.currentDropObj = this.eleid;
					var flagStyle = window.dragObj.style;
					flagStyle.left = (e.clientX + 3) + "px";
					flagStyle.top = (e.clientY + 3) + "px";
				}
			}
		},
		dragMove : function() {
			if (window.dragObj == null)
				return;
			window.dragObj.style.display = "block";
			var event = EventUtil.getEvent();
			var flagStyle = window.dragObj.style;
			if(window.dragObj.parentNode.type==="absolutelayout"){
				var $panel=$(window.dragObj.parentNode);
				var scrollTop=$panel.scrollTop();
				var scrollLeft=$panel.scrollLeft();
				var layoutPosition=$panel.offset();
				flagStyle.left = (event.clientX-layoutPosition.left+scrollLeft + 10) + "px";
				flagStyle.top = (event.clientY-layoutPosition.top+scrollTop+ 10) + "px";
				flagStyle.display = "block";
				flagStyle.zIndex = 647;
				var trueObj = document.getElementById(window.dragObj.id);
				trueObj.style.display = "none";
			// 这里没有处理IE兼容性 喜海到时候处理下 方法是 pageX + scrollLeft - clientLeft
			}else{
				flagStyle.left = (event.clientX + 10) + "px";
				flagStyle.top = (event.clientY + 10) + "px";
				flagStyle.display = "block";
				flagStyle.zIndex = 647;
				var trueObj = document.getElementById(window.dragObj.id);
				trueObj.style.display = "none";
			}
			
		},
		dragEnd : function(e) {
			this.dragStart = false;
			if (window.dragObj) {
				var trueObj = document.getElementById(window.dragObj.id);
				trueObj.style.display = "";
				
				var panel = $.design.getSelectPanel(e);
				if(dragObj.parentNode.type === "absolutelayout" ){
					var $layoutElem=$(window.dragObj.parentNode);
					var layoutPosition=$layoutElem.offset();
					var scrollLeft=$layoutElem.scrollLeft();
					var scrollTop=$layoutElem.scrollTop();
					trueObj.style.top=(e.clientY-layoutPosition.top+scrollTop+10) + "px";
					trueObj.style.left=(e.clientX-layoutPosition.left+scrollLeft+10) + "px";
					var obj = window.dragObj.parentNode;
//					if(trueObj.type=="form_element") {
//						obj.currentDropObj = trueObj.subeleid;
//					} else {
						obj.currentDropObj = isCompmont ? trueObj.eleid : trueObj.uiid;
//					}
					obj.currentDropDsId = trueObj.widgetid;
					obj.currentDropObjType = trueObj.type;
					obj.layoutType=trueObj.parentNode.type;
					obj.left=e.clientX-layoutPosition.left+scrollLeft+10;
					obj.top=e.clientY-layoutPosition.top+scrollTop+10;
					$.design.toServer(obj, "move");
				}else{
					if (!isChildOfObj(panel, trueObj)&&panel.type !=="absolutelayout") {
						var obj = $.design.toDragSelected(e);
						var isCompmont = trueObj.eleid != "";
						if(trueObj.type=="form_element") {
							obj.currentDropObj = trueObj.subeleid;
						} else {
							obj.currentDropObj = isCompmont ? trueObj.eleid : trueObj.uiid;
						}
						obj.currentDropDsId = trueObj.widgetid;
						obj.currentDropObjType = trueObj.type;
						// 触发后台移动命令
						if (isCompmont) {
							execDynamicScript2RemoveComponent(trueObj.id,
									trueObj.widgetid, trueObj.eleid);
						} else {
							execDynamicScript2RemoveLayout(trueObj.id);
						}
						$.design.toServer(obj, "move");
					}
					// 选中拖放对象
					callParent(trueObj, "init");
				}
				
				// 释放页面图片
				var event = {
					type : "release"
				};
				window.dropEventHandler(event);
				window.currentDropObj = null;
				stopEvent(e);
				// 删除事件对象（用于清除依赖关系）
				// clearEventSimply(e);
			}
			try {
				if(window.dragObj.parentNode.type==="absolutelayout")
					window.dragObj.parentNode.removeChild(window.dragObj);
				else
					document.body.removeChild(window.dragObj);
			} catch (error) {
			}
			window.dragObj = null;
		
		},
		/**
		 * 取消拖动控件
		 * @param {} id
		 */
		dragCancel : function(id) {
		    $("#" + id + "_hand").hide();
		},
		/**
		 * 为不同的类型添加不同的右键事件
		 * 
		 * @param {} e
		 */
		contextMenuRender : function(e) {
		    e = EventUtil.getEvent();
		    if (window.currentDropObj != null) return;
		    var obj = $.design.toEdit(e, false); // 先选中
		    if (obj.objType == $.design.Constant.COMPOMENT_TYPE) {
		        if (obj.type == 'grid_header') {
		            $.design.gridColumnContextMenuRender(e, obj);
		        } else if (obj.type == "grid") {
		            $.design.gridCompContextMenuRender(e, obj);
		        } else if (obj.type == "formcomp") {
		            $.design.formCompContextMenuRender(e, obj);
		        } else if (obj.type == "menubar") {
		            $.design.menuCompContextMenuRender(e, obj);
		        } else if (obj.type == "toolbar_button") {
		            $.design.toolbarCompContextMenuRender(e, obj);
		        } else {
		            $.design.compContextMenuRender(e, obj);
		        }
		    } else if (obj.objType == $.design.Constant.PANEL_TYPE) {
		        if (obj.type == 'uimeta') {
		            $.design.uimetaContextMenuRender(e, obj);
		        } else {
		            $.design.panelContextMenuRender(e, obj);
		        }
		    } else if (obj.objType == "outLookItem" || obj.objType == "menubar_menuitem") {
		        return;
		    } else {
		        if (obj.type == "cardlayout") {
		            $.design.cardContextMenuRender(e, obj);
		        } else if (obj.type == "gridpanel") {
		            $.design.gridPanelContextMenuRender(e, obj);
		        } else if (obj.type == "flowhpanel") {
		            $.design.flowhPanelContextMenuRender(e, obj);
		        } else if (obj.type == "flowvpanel") {
		            $.design.flowvPanelContextMenuRender(e, obj);
		        } else if (obj.type == "tabitem") {
		            $.design.tabItemContextMenuRender(e, obj);
		        } else if (obj.type == "tag") {
		            $.design.tabContextMenuRender(e, obj);
		        } else if (obj.type == "cardpanel") {
		            $.design.cardPanelContextMenuRender(e, obj);
		        } else if (obj.type == "outlookbar_item") {
		            $.design.outlookbarPanelContextMenuRender(e, obj);
		        } else if (obj.type == "spliteronepanel") {
		            return;
		        } else if (obj.type == "flowvlayout" || obj.type == "flowhlayout") {
		            $.design.flowLayoutContextMenuRender(e, obj);
		        } else if (obj.type === "absolutelayout") {
		        	$.design.absoluteLayoutContextMenuRender(e,obj);
		        } else {
		            $.design.layoutContextMenuRender(e, obj);
		        }
		    }
		    stopEvent(e);
		    // 删除事件对象（用于清除依赖关系）
		//    clearEventSimply(e);
		},
		/**
		 * 检查当前点击的元素是否允许拖动操作
		 */
		checkDragElement : function(e) {
		    if ($.browsersupport.IS_IE) {
		        //针对IE：e.srcElement.parentNode.type或者e.srcElement.type如果为form_element，则返回
		        var srcObj = e.srcElement;
		        if (srcObj.type != null && typeof(srcObj.type) != "undefined" && srcObj.type == "form_element") {
		            return false;
		        } else if (srcObj.parentNode != null && typeof(srcObj.parentNode) != "undefined") {
		            var srcParentObj = srcObj.parentNode;
		            if (srcParentObj.type != null && typeof(srcParentObj.type) != "undefined" && srcParentObj.type == "form_element") {
		                return false;
		            }
		        }
		    } else {
		        //针对火狐：formelement点击时候explicitOriginalTarget和originalTarget不相等，如果是formelement，则返回
		        if (e.explicitOriginalTarget != e.originalTarget) return false;
		    }
		
		    return true;
		},
		/**
		 * 点击设计界面上的控件触发的编辑方法
		 * 
		 * @param {} noParent:true表示不进行服务器请求，false表示进行服务器请求
		 */
		toEdit : function(e, noParent) {
		    e = EventUtil.getEvent();
		    if (window.currentDropObj != null) return;
		    var obj = e.target;
		    if (obj == $.design.lastObj) {
		        return obj;
		    }
		
		    while (obj != null) {
		        if (obj.id == "ToRight" || obj.id == "ToLeft") {
		            break;
		        }
		        if (obj.objType) {
		            if (obj.objType == "tabItem") {
		                break;
		            } else if (obj.objType == "outLookItem") {
		                triggerEditorEvent("showPropertiesView", obj);
		                break;
		            } else {
		                $.design.toEditSelected(obj, noParent);
		                stopEvent(e);
		                break;
		            }
		
		        } else {
		            if (obj.parentNode) {
		                obj = obj.parentNode;
		            } else {
		                break;
		            }
		        }
		
		    }
		    $.design.hideAllContextMenu(e);
		    $.design.lastObj = obj;
		    return obj;
		},
		/**
		 * 选择控件的父控件
		 * @param {} obj
		 */
		selectParent : function(obj) {
		    if ($.design.variable.editingObjP != null) {
		        $.design.toEditAbleClass($.design.variable.editingObjP);
		        $.design.variable.editingObjP = null;
		    }
		
		    if (obj.objType == 'panel') {
		        var parent = obj.parentNode;
		        while (parent) {
		            if (parent.objType) {
		                parent.className = 'editable_parent';
		                $.design.variable.editingObjP = parent;
		                break;
		            } else {
		                parent = parent.parentNode;
		            }
		        }
		
		    }
		},
		/**
		 * 选择容器
		 * @param {} e
		 * @return {}
		 */
		getSelectPanel : function(e) {
		    e = EventUtil.getEvent();
		    var obj = e.target;
		    while (true) {
		        if (obj.objType && obj.isContainer) {
		            break;
		        } else {
		            if (obj.parentNode) {
		                obj = obj.parentNode;
		            } else {
		                break;
		            }
		        }
		
		    }
		
		    return obj;
		},
		/**
		 * 选择编辑对性
		 * @param {} obj
		 * @param {} noParent true表示只选中，不向服务器发送
		 */
		toEditSelected : function(obj, noParent) {
		    if (obj) {
		        if ($.design.variable.editingObj) { // 之前编辑的与现在选中的不同
		            $.design.toEditAbleClass($.design.variable.editingObj);
		        }
		        $.design.selectParent(obj);
		        if (obj.objType == $.design.Constant.GRID_LAYOUT_TYPE) {
		            removeClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT);
		            addClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT_SELECTED);
		        } else if (obj.objType == $.design.Constant.GRID_PANEL_TYPE) { // 表格布局的特殊处理table实现，和div的样式有区别
		            removeClassName(obj, 'edit_gridLayout_cell');
		            addClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT_SELECTED);
		        } else if (obj.objType == 'menubar_menuitem') { // MenuBarItem的编辑态样式
		            addClassName(obj, 'edit_menubar_menuitem');
		        } else if (obj.objType == $.design.Constant.COMPOMENT_TYPE) { // 控件布局的特殊处理table实现，和div的样式有区别
		            obj.className = $.design.Constant.EDIT_COMPONENT_SELECTED;
		            //if (obj.type != "grid_header" && obj.type != "form_element" && obj.uiid != "" && obj.id != "") {
		            if ((obj.type != "grid_header" && obj.uiid != "" && obj.id != "") || (obj.type == 'form_element' && obj.renderType==4)) {
		                var handId = obj.id + "_hand";
		                var handler =$("#"+handId);
		                if (handler.size()!=0) {
		                    handler.css("display","block");
		                } else {
		                    var handDiv = $("<div>").attr('id',handId);
		                    var handImg = $("<img>").attr({'src':window.themePath + "/global/images/move.png","id":handId + "_img"})
		                    .css("cursor","move");
		                    handDiv.append(handImg)
		                    .css({'position':'absolute','left':'0px','top':'0px','cursor':'move','zIndex':'64700'})
		                    .attr({'haswidth':'1','hasheight':'1'});
		                    handImg.on("mousedown",$.design.dragStart);
		                    obj.appendChild(handDiv[0]);
		                }
		            }
		        } else {
		            $.design.setClassName(obj, $.design.Constant.EDIT_SELECTED); // 对于div的统一处理
		        }
		        if (obj.type == "tabitem" || obj.type == "flowhpanel" || obj.type == "flowvpanel" || obj.type == "gridpanel" || obj.type.indexOf("layout") != -1 || obj.type == "outlookbar") {
		            var _handler = getPanelHanler(obj, true);
		            _handler.style.display = "block";
		        }
		        $.design.variable.editingObj = obj;
		        // 调用其父窗口的方法
		        if (noParent == true) return;
		        callParent(obj, "init");
		    }
		},
		// 将对象置为 编辑态 对布局和layout
		setClassName : function(obj, defaultClassName) {
		    if (defaultClassName) {
		        if (obj.className && obj.className.length > 0) {
		
		            if (defaultClassName == $.design.Constant.EDIT_SELECTED) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_LAYOUT_PANEL, $.design.Constant.EDIT_SELECTED);
		            } else if (defaultClassName == $.design.Constant.EDIT_LAYOUT_PANEL) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_SELECTED, $.design.Constant.EDIT_LAYOUT_PANEL);
		            } else if (defaultClassName == $.design.Constant.EDIT_LAYOUT_HPANEL) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_SELECTED, $.design.Constant.EDIT_LAYOUT_HPANEL);
		            } else if (defaultClassName == $.design.Constant.EDIT_LAYOUT_VPANEL) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_SELECTED, $.design.Constant.EDIT_LAYOUT_VPANEL);
		            } else if (defaultClassName == $.design.Constant.EDIT_COMPONENT_SELECTED) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_COMPONENT, $.design.Constant.EDIT_COMPONENT_SELECTED);
		            } else if (efaultClassName == $.design.Constant.EDIT_COMPONENT) {
		                obj.className = obj.className.replace($.design.Constant.EDIT_COMPONENT_SELECTED, $.design.Constant.EDIT_COMPONENT);
		            } else if (obj.className.indexOf(defaultClassName) == -1) {
		                obj.className = defaultClassName + " " + obj.className;
		            }
		        } else {
		            obj.className = defaultClassName;
		        }
		    }
		
		},
		/**
		 * 将对象设置为编辑状态，即显示黑线
		 * 
		 * @param {}
		 *            obj
		 */
		toEditAbleClass : function(obj) {
		    if (obj && obj.objType) {
		        var handler = getPanelHanler(obj, false);
		        if (handler) handler.style.display = "none";
		        if (obj.objType == $.design.Constant.COMPOMENT_TYPE) { // 控件类型
		            obj.className = $.design.Constant.EDIT_COMPONENT;
		            $.design.dragCancel(obj.id);
		        } else if (obj.objType == $.design.Constant.GRID_LAYOUT_TYPE) { // 表格布局
		            removeClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT_SELECTED);
		            addClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT);
		        } else if (obj.objType == $.design.Constant.GRID_PANEL_TYPE) { // 表格的cell
		            removeClassName(obj, $.design.Constant.EDIT_GRIDLAYOUT_SELECTED);
		            addClassName(obj, 'edit_gridLayout_cell');
		        } else if (obj.objType == "menubar_menuitem") { //MenuBarComp的菜单项
		            removeClassName(obj, 'edit_menubar_menuitem');
		            //addClassName(obj, 'menu_div');
		        } else if (obj.objType == $.design.Constant.PANEL_TYPE) { // panel
		            if (obj.type == 'flowhpanel') {
		                obj.className = $.design.Constant.EDIT_LAYOUT_HPANEL;
		            } else if (obj.type == 'flowvpanel') {
		                obj.className = $.design.Constant.EDIT_LAYOUT_VPANEL;
		            } else {
		                obj.className = $.design.Constant.EDIT_LAYOUT_PANEL;
		            }
		        } else { // layout
		            $.design.setClassName(obj, $.design.Constant.EDIT_LAYOUT_PANEL);
		        }
		    }
		},
		// 删除该节点和子节点的onclick事件,当遇到是可编辑的div时，则停止，当没有子节点时停止
		removeOnclick : function(obj) {
		    // return;
		    if (obj) {
		        if (obj.className == "tab_img_div") {
		            return;
		        }
		        try {
		            if (!obj.objType) {
		                $(obj).off();
		            }
		
		            if (obj.childNodes) {
		
		                for (var i = 0; i < obj.childNodes.length; i++) {
		                    var child = obj.childNodes[i];
		                    if (child.objType) {
		                        return;
		                    }
		                    $.design.removeOnclick(child);
		                }
		            }
		        } catch(error) {
		            Logger.error(error.name + ":" + error.message);
		        }
		    }
		},
		/**
		 * 右键菜单某一项被点击时，触发此事件
		 * 
		 * @param {}
		 *            contextMenu
		 * @param {}
		 *            name
		 * @param {}
		 *            caption
		 * @param {}
		 *            operationType
		 * @return {}
		 */
		contextMenuOnClick : function(contextMenu, name, caption, operationType) {
		    var menuItem = contextMenu.addMenu(name, caption);
		    menuItem.element.on("menuitemonclick",function(){
		     if (contextMenu.triggerObj) {
		            $.design.toServer(contextMenu.triggerObj, operationType);
		        } else {
		            alert("错误的方法调用，为指定调用对象！");
		        }
		    });
		    return menuItem;
		},
		/**
		 * 隐藏所有的右键菜单，左键单击时隐藏
		 */
		hideAllContextMenu : function(e) {
		    for (var i = 0; i < $.design.variable.allContextMenus.length; i++) {
		        var contextMenu = $.design.variable.allContextMenus[i];
		        if (contextMenu && contextMenu.hide) {
		            contextMenu.hide();
		        }
		    }
		},
		flowvPanelContextMenuRender : function(e, obj) {
		    if ($.design.variable.flowvPanelContextMenu == null) {
		        $.design.variable.flowvPanelContextMenu = $('<div id="flowvPanelContextMenu">').contextmenu().contextmenu('instance');
		        // $.design.variable.layoutContextMenu.settingMenu = $.design.contextMenuOnClick($.design.variable.layoutContextMenu,"setting", "设置", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.flowvPanelContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.variable.flowvPanelContextMenu, "addUp", "向上增加", "addLeft");
		        $.design.variable.flowvPanelContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.flowvPanelContextMenu, "addDown", "向下增加", "addDown");
		        $.design.variable.flowvPanelContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.flowvPanelContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.flowvPanelContextMenu);
		    }
		    $.design.variable.flowvPanelContextMenu.triggerObj = obj;
		    $.design.variable.flowvPanelContextMenu.show(e);
		},
		flowhPanelContextMenuRender : function(e, obj) {
		    if ($.design.variable.flowhPanelContextMenu == null) {
		        $.design.variable.flowhPanelContextMenu = $('<div id="flowhPanelContextMenu">').contextmenu().contextmenu('instance');
		        // $.design.variable.layoutContextMenu.settingMenu = $.design.contextMenuOnClick($.design.variable.layoutContextMenu, "setting", "设置", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.flowhPanelContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.variable.flowhPanelContextMenu, "addLeft", "向左增加", "addLeft");
		        $.design.variable.flowhPanelContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.flowhPanelContextMenu, "addRight", "向右增加", "addRight");
		        $.design.variable.flowhPanelContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.flowhPanelContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.flowhPanelContextMenu);
		    }
		    $.design.variable.flowhPanelContextMenu.triggerObj = obj;
		    $.design.variable.flowhPanelContextMenu.show(e);
		
		},
		tabItemContextMenuRender : function(e, obj) {
		    if ($.design.variable.tabItemContextMenu == null) {
		        $.design.variable.tabItemContextMenu = $('<div id="tabItemContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.tabItemContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.variable.tabItemContextMenu, "addLeft", "向左增加", "addLeft");
		        $.design.variable.tabItemContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.tabItemContextMenu, "addRight", "向右增加", "addRight");
		        $.design.variable.tabItemContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.tabItemContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.tabItemContextMenu);
		    }
		    $.design.variable.tabItemContextMenu.triggerObj = obj;
		    $.design.variable.tabItemContextMenu.show(e);
		
		},
		tabContextMenuRender : function(e, obj) {
		    if ($.design.variable.tabContextMenu == null) {
		        $.design.variable.tabContextMenu = $('<div id="tabContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.tabContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.tabContextMenu, "addTabRight", "添加页签右侧容器", "addTabRight");
		        $.design.variable.tabContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.tabContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.tabContextMenu);
		    }
		    $.design.variable.tabContextMenu.triggerObj = obj;
		    $.design.variable.tabContextMenu.show(e);
		
		},
		cardPanelContextMenuRender : function(e, obj) {
		    if ($.design.variable.cardPanelContextMenu == null) {
		        $.design.variable.cardPanelContextMenu = $('<div id="cardPanelContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.cardPanelContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.variable.cardPanelContextMenu, "addLeft", "向前增加", "addLeft");
		        $.design.variable.cardPanelContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.cardPanelContextMenu, "addRight", "向后增加", "addRight");
		        $.design.variable.cardPanelContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.cardPanelContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.cardPanelContextMenu);
		    }
		    $.design.variable.cardPanelContextMenu.triggerObj = obj;
		    $.design.variable.cardPanelContextMenu.show(e);
		},
		outlookbarPanelContextMenuRender : function(e, obj) {
		    if ($.design.variable.outlookbarItemContextMenu == null) {
		        $.design.variable.outlookbarItemContextMenu = $('<div id="outlookbarItemContextMenu">').contextmenu().contextmenu('instance');
		        //$.design.outlookbarItemContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.outlookbarItemContextMenu, "addUp","向上增加", "addUp");
		        //$.design.outlookbarItemContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.outlookbarItemContextMenu, "addDown","向下增加", "addDown");
		        $.design.variable.outlookbarItemContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.outlookbarItemContextMenu, "addDown", "增加", "addDown");
		        $.design.variable.outlookbarItemContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.outlookbarItemContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.outlookbarItemContextMenu);
		    }
		    $.design.variable.outlookbarItemContextMenu.triggerObj = obj;
		    $.design.variable.outlookbarItemContextMenu.show(e);
		},
		gridColumnContextMenuRender : function(e, obj) {
		    if ($.design.variable.gridColumnContextMenu == null) {
		        $.design.variable.gridColumnContextMenu = $('<div id="gridColumnContextMenu">').contextmenu().contextmenu('instance');
		        // $.design.variable.layoutContextMenu.settingMenu = $.design.contextMenuOnClick($.design.variable.layoutContextMenu,"setting", "设置", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.gridColumnContextMenu.addLeftMenu = $.design.contextMenuOnClick($.design.variable.gridColumnContextMenu, "addLeft", "增加项", "addLeft");
		        //$.design.variable.gridColumnContextMenu.addRightMenu = $.design.contextMenuOnClick($.design.variable.gridColumnContextMenu, "addRight","向右增列", "addRight");
		        $.design.variable.gridColumnContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.gridColumnContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.gridColumnContextMenu);
		    }
		    $.design.variable.gridColumnContextMenu.triggerObj = obj;
		    $.design.variable.gridColumnContextMenu.show(e);
		
		},
		gridPanelContextMenuRender : function(e, obj) {
		    if ($.design.variable.gridPanelContextMenu == null) {
		        $.design.variable.gridPanelContextMenu = $('<div id="gridPanelContextMenu">').contextmenu().contextmenu('instance');
		
		        $.design.variable.gridPanelContextMenu.cell = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu, "expansion", "合并单元格", "expansion");
		        $.design.variable.gridPanelContextMenu.expansionLeft = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.cell, "expansionLeft", "向左", "expansionLeft");
		        $.design.variable.gridPanelContextMenu.expansionRight = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.cell, "expansionRight", "向右", "expansionRight");
		        $.design.variable.gridPanelContextMenu.expansionUp = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.cell, "expansionUp", "向上", "expansionUp");
		        $.design.variable.gridPanelContextMenu.expansionDown = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.cell, "expansionDown", "向下", "expansionDown");
		
		        $.design.variable.gridPanelContextMenu.row = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu, "row", "行", "row");
		        $.design.variable.gridPanelContextMenu.addRowUp = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.row, "addRowUp", "向上增行", "addRowUp");
		
		        $.design.variable.gridPanelContextMenu.addRowDown = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.row, "addRowDown", "向下增行", "addRowDown");
		
		        $.design.variable.gridPanelContextMenu.expansionDown = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.row, "delRow", "删除行", "delRow");
		
		        $.design.variable.gridPanelContextMenu.column = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu, "column", "列", "column");
		        $.design.variable.gridPanelContextMenu.addColumnLeft = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.column, "addColumnLeft", "向左增列", "addColumnLeft");
		        $.design.variable.gridPanelContextMenu.expansionRight = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.column, "addColumnRight", "向右增列", "addColumnRight");
		        $.design.variable.gridPanelContextMenu.expansionDown = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu.column, "delColumn", "删除列", "delColumn");
		
		        $.design.variable.gridPanelContextMenu.delCell = $.design.contextMenuOnClick($.design.variable.gridPanelContextMenu, "delete", "删除单元格", "delete");
		
		        $.design.variable.allContextMenus.push($.design.variable.gridPanelContextMenu);
		    }
		    $.design.variable.gridPanelContextMenu.column.triggerObj = obj;
		    $.design.variable.gridPanelContextMenu.row.triggerObj = obj;
		    $.design.variable.gridPanelContextMenu.cell.triggerObj = obj;
		    $.design.variable.gridPanelContextMenu.triggerObj = obj;
		    $.design.variable.gridPanelContextMenu.show(e);
		},
		/**
		 * 布局右键菜单触发事件
		 */
		flowLayoutContextMenuRender : function(e, obj) {
		    // 布局的右键菜单
		    if ($.design.variable.layoutContextMenu == null) {
		        $.design.variable.layoutContextMenu = $('<div id="layoutContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.layoutContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.layoutContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.layoutContextMenu.BorderStyle = $.design.contextMenuOnClick($.design.variable.layoutContextMenu, "BorderStyle", "边框设置", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.layoutContextMenu.createcombo = $.design.contextMenuOnClick($.design.variable.layoutContextMenu, "createcombo", "创建组合", $.design.Constant.CONTEXTMENUCREATECOMBO);
		
		        $.design.variable.allContextMenus.push($.design.variable.layoutContextMenu);
		    }
		    obj.currentDropObjType2 = "isLayout";
		    $.design.variable.layoutContextMenu.triggerObj = obj;
		    $.design.variable.layoutContextMenu.show(e);
		
		},
		/**
		 * 绝对布局容器右键菜单触发事件
		 */
		absoluteLayoutContextMenuRender : function(e, obj ){
			 if($.design.variable.absoluteLayoutContextMenu == null) {
				 $.design.variable.absoluteLayoutContextMenu = $('<div id="absoluteLayoutContextMenu">').contextmenu().contextmenu('instance');
				 $.design.variable.absoluteLayoutContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.absoluteLayoutContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		         $.design.variable.allContextMenus.push($.design.variable.layoutContextMenu);
			 }
			 obj.currentDropObjType2 = "absolutelayout";
			 $.design.variable.absoluteLayoutContextMenu.triggerObj = obj;
			 $.design.variable.absoluteLayoutContextMenu.show(e);
		},
		
		/**
		 * 布局右键菜单触发事件
		 */
		layoutContextMenuRender : function(e, obj) {
		    // 布局的右键菜单
		    if ($.design.variable.layoutContextMenu == null) {
		        $.design.variable.layoutContextMenu = $('<div id="layoutContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.layoutContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.layoutContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.layoutContextMenu);
		    }
		    obj.currentDropObjType2 = "isLayout";
		    $.design.variable.layoutContextMenu.triggerObj = obj;
		    $.design.variable.layoutContextMenu.show(e);
		
		},
		/**
		 * 控件右键菜单触发事件
		 */
		compContextMenuRender : function(e, obj) {
		    // 控件的右键菜单
		    if ($.design.variable.compContextMenu == null) {
		        $.design.variable.compContextMenu = $('<div id="compContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.compContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.compContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.compContextMenu);
		    }
		    $.design.variable.compContextMenu.triggerObj = obj;
		    $.design.variable.compContextMenu.show(e);
		},
		/**
		 * grid控件的右键菜单触发事件
		 * @param {} e
		 * @param {} obj
		 */
		gridCompContextMenuRender : function(e, obj) {
		    if ($.design.variable.gridCompContextMenu == null) {
		        $.design.variable.gridCompContextMenu = $('<div id="gridCompContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.gridCompContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.gridCompContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.gridCompContextMenu.editMenu = $.design.contextMenuOnClick($.design.variable.gridCompContextMenu, "edit", "编辑", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.allContextMenus.push($.design.variable.gridCompContextMenu);
		    }
		    $.design.variable.gridCompContextMenu.triggerObj = obj;
		    $.design.variable.gridCompContextMenu.show(e);
		},
		/**
		 * 表单控件的右键菜单触发事件
		 * @param {} e
		 * @param {} obj
		 */
		formCompContextMenuRender : function(e, obj) {
		    if ($.design.variable.formCompContextMenu == null) {
		        $.design.variable.formCompContextMenu = $('<div id="formCompContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.formCompContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.formCompContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.formCompContextMenu.editMenu = $.design.contextMenuOnClick($.design.variable.formCompContextMenu, "edit", "编辑", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.formCompContextMenu.addElementMenu = $.design.contextMenuOnClick($.design.variable.formCompContextMenu, "addRight", "增加项", "addRight");
		        $.design.variable.allContextMenus.push($.design.variable.formCompContextMenu);
		    }
		    $.design.variable.formCompContextMenu.triggerObj = obj;
		    $.design.variable.formCompContextMenu.show(e);
		},
		/**
		 * panel的右键菜单触发事件
		 * @param {} e
		 * @param {} obj
		 */
		panelContextMenuRender : function(e, obj) {
		    if ($.design.variable.panelContextMenu == null) {
		        $.design.variable.panelContextMenu = $('<div id="panelContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.panelContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.panelContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.panelContextMenu);
		    }
		    obj.currentDropObjType2 = "isPanel";
		    $.design.variable.panelContextMenu.triggerObj = obj;
		    $.design.variable.panelContextMenu.show(e);
		},
		uimetaContextMenuRender : function(e, obj) {
		    // 控件的右键菜单
		    stopEvent(e);
		    // 删除事件对象（用于清除依赖关系）
		//    clearEventSimply(e);
		},
		cardContextMenuRender : function(e, obj) {
		    // 控件的右键菜单
		    if ($.design.variable.cardContextMenu == null) {
		        $.design.variable.cardContextMenu = $('<div id="cardContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.cardContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.cardContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.allContextMenus.push($.design.variable.cardContextMenu);
		    }
		    $.design.variable.cardContextMenu.triggerObj = obj;
		    $.design.variable.cardContextMenu.show(e);
		},
		/**
		 * 菜单控件的右键菜单触发事件
		 * @param {} e
		 * @param {} obj
		 */
		menuCompContextMenuRender : function(e, obj) {
		    if ($.design.variable.menuCompContextMenu == null) {
		        $.design.variable.menuCompContextMenu = $('<div id="menuCompContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.menuCompContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.menuCompContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.menuCompContextMenu.editMenu = $.design.contextMenuOnClick($.design.variable.menuCompContextMenu, "edit", "编辑", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.allContextMenus.push($.design.variable.menuCompContextMenu);
		    }
		    $.design.variable.menuCompContextMenu.triggerObj = obj;
		    $.design.variable.menuCompContextMenu.show(e);
		},
		/**
		 * 工具栏控件的右键菜单触发事件
		 * @param {} e
		 * @param {} obj
		 */
		toolbarCompContextMenuRender : function(e, obj) {
		    if ($.design.variable.toolbarCompContextMenu == null) {
		        $.design.variable.toolbarCompContextMenu = $('<div id="toolbarCompContextMenu">').contextmenu().contextmenu('instance');
		        $.design.variable.toolbarCompContextMenu.deleteMenu = $.design.contextMenuOnClick($.design.variable.toolbarCompContextMenu, "delete", "删除", $.design.Constant.CONTEXTMENUDELETE);
		        $.design.variable.toolbarCompContextMenu.editMenu = $.design.contextMenuOnClick($.design.variable.toolbarCompContextMenu, "edit", "编辑", $.design.Constant.CONTEXTMENUSETTING);
		        $.design.variable.allContextMenus.push($.design.variable.toolbarCompContextMenu);
		    }
		    $.design.variable.toolbarCompContextMenu.triggerObj = obj;
		    $.design.variable.toolbarCompContextMenu.show(e);
		},
		/**
		 * ------------------------------------------------------------------
		 * 接收拖拽事件，消息通知，参数为类型，整个页面的布局和容器接收onmourse事件。
		 * 前提，没有子元素的容器才可以，
		 * ------------------------------------------------------------------
		 */
		toDragSelected : function(e) {
		    e = EventUtil.getEvent();
		    var obj = e.target;
		    while (true) {
		        if (obj.objType && obj.isContainer) {
		            $.design.toEditSelected(obj, true);
		            stopEvent(e);
		            break;
		        } else {
		            if (obj.parentNode) {
		                obj = obj.parentNode;
		            } else {
		                break;
		            }
		        }
		
		    }
		    $.design.hideAllContextMenu(e);
		    return obj;
		},
		/**
		 * 
		 * 先调用wupeng1的listener进行数据持久化 然后调用renxh的listener,进行页面的响应
		 * 
		 * @param {} obj
		 * @param {} oper
		 */
		toServerProxy : function(obj, oper) {
		    if (oper == $.design.Constant.CONTEXTMENUDELETE) {
		        if (!confirm("确定要删除吗?")) {
		            return;
		        }
		    }
		
		    if (oper == $.design.Constant.CONTEXTMENUADD) {
		        window.isRenderDone = false;
		        var div = $("#"+obj.id)[0];
		        var children = div.childNodes;
		        $.design.toServer(obj, oper);
		        callParent(obj, oper);
		    }
		},
		/**
		 * 
		 * 进行uimeta操作响应的listener，同时会更新页面的脚本
		 */
		toServer : function(obj, oper) {
		    if (oper == "editEvent") {
		        triggerEditorEvent("showPropertiesView", obj);
		    }
		    var operateActions = [];
		    //一般操作
		    operateActions.push("move,delete,setting,update,updateid");
		    //合并单元格
		    operateActions.push("expansionLeft,expansionRight,expansionUp,expansionDown");
		    //grid行列操作
		    operateActions.push("addRowUp,addRowDown,delRow,addColumnLeft,addColumnRight,delColumn");
		    //添加操作
		    operateActions.push("add,addLeft,addRight,addUp,addDown,addTabRight");
		    //模型的操作
		    operateActions.push("addModel,deleteModel,editModel,copyModel");
		    //gird和form的重画操作
		    operateActions.push("repaintFormComp,repaintGridComp,repaintMenuBarComp,repaintToolbarComp");
		    operateActions.push($.design.Constant.CONTEXTMENUCREATECOMBO);
		    //允许的命令操作串
		    var operAllow = operateActions.join(",");
		    if (operAllow.split(",").indexOf(oper) == -1) return;
		    var proxy = $.serverproxy.getObj({async:true});
		    //添加参数	
		    proxy.addParam({
		    	'clc': 'xap.lui.core.render.RaDynamicScriptListener',
			    'el': '2',
			    'm_n': 'handlerEvent',
			    "oper": oper,
			    "uiid": obj.uiid,
			    "eleid": obj.eleid,
			    "widgetid": obj.widgetid,
			    "type": obj.type,
			    "objtype": obj.objtype,
			    "rendertype": obj.rendertype,
			    "subeleid": obj.subeleid,
			    "subuiid": obj.subuiid,
			    "divid": obj.id,
			    "rowindex": obj.rowindex,
			    "colindex": obj.colindex,
			    "currentDropObj": obj.currentDropObj,
			    "currentDropObjType": obj.currentDropObjType,
			    "currentDropObjType2": obj.currentDropObjType2,
			    "currentDropDsId": obj.currentDropDsId,
			    "currentDropPid": obj.currentDropPid,
			    "query_keyvalue": obj.query_keyvalue,
			    // 更新属性传递的参数
			    "compid": obj.compid, // 前台组件ID
			    "prtid": obj.prtid, //父节点ID
			    "viewid": obj.viewid,
			    "comptype": obj.type, // 前台组件类型
			    "attr": obj.attr, // 前台属性名
			    "attrtype": obj.attrtype, // 属性类型
			    "oldvalue": obj.oldvalue, // 修改前的值
			    "newvalue": obj.newvalue,
			    //鼠标焦点位置
			    "offsetX" : obj.offsetX,
			    "offsetY" : obj.offsetY,
			    "pageX" :obj.pageX,
			    "pageY" : obj.pageY,
			    "clientX" : obj.clientX,
			    "clientY" : obj.clientY
			}); // 修改后的值
		    if(oper==="move"){
		    	if(window.dragObj){
			    	proxy.addParam({
			    		dropX:window.dragObj.offsetLeft,
			    		dropY:window.dragObj.offsetTop
			    	});
	    		}else{
	    			proxy.addParam({
			    		dropX:obj.left,
			    		dropY:obj.top
			    	});
    			}
	    		
	    	}
		    
		    proxy.execute();
		}
		//TODO : class function end
	});
	


	$.extend($.design.prototype, {
//		addContextMenu : function() {
//			if (this.divObj) {
//				var obj = this.divObj;
//				$(obj).on('rclick',$.design.contextMenuRender);
//			}
//		},
		/**
		 * 拖动控件方法
		 */
		toDragableObj : function() {
			var obj = this.divObj;
			$(document).off(".toDragableObj").on('mousemove.toDragableObj', $.design.dragMove).on('mouseup.toDragableObj', $.design.dragEnd);
		},
		initObjAttribute : function() {
		    if (this.divObj && this.params) {
		        for (var i in this.params) this.divObj[i] = this.params[i];
		    }
		},
		toEditableObj : function() {
		    if (this.divObj) {
		        var obj = this.divObj;
		        $.design.toEditAbleClass(obj);
		        $.design.removeOnclick(obj);
		        $(obj).on("click",$.design.toEdit);
		    }
		},
		// 添加右键菜单
		addContextMenu : function() {
		    if (this.divObj) {
		        var obj = this.divObj;
		        $(obj).on("contextmenu",$.design.contextMenuRender);
		    }
		}
		
		//TODO :object function end
	});
	

	$.draglistener = function(obj) {
	    obj.isContainer = true;
	    this.obj = obj;
	    this.create();
	};
	
	
	$.draglistener.getObj=function(obj){
		return new $.draglistener(obj);
	};
	
	$.extend($.draglistener.prototype,{
		create : function() {
		    var obj = this.obj;
		    $(obj).on("click",this.onclick).on("mouseover",this.onmouseover).on("rclick",this.oncontextmenu);
		},
		onclick : function(e) {
		    if (window.dragObj != null) return;
		    if (window.currentDropObj != null) {
		        var obj = $.design.toDragSelected(e);
		        obj.currentDropObj = window.currentDropObj; // 保存变量
		        obj.currentDropObjType = window.currentDropObjType; // 保存变量
		        obj.currentDropObjType2 = window.currentDropObjType2;
		        obj.currentDropDsId = window.currentDropDsId;
		        obj.currentDropPid = window.currentDropPid;
		        try{
			        obj.offsetX = e.offsetX;
			        obj.offsetY = e.offsetY;
			        obj.pageX = e.pageX;
			        obj.pageY = e.pageY;
			        obj.clientX = e.clientX;
			        obj.clientY = e.clientY;
			        obj.offsetX+=obj.scrollLeft;
			        obj.offsetY+=obj.scrollTop;
		        }catch(ex){
		        	console.log(ex);
	        	}
		        
		        obj.query_keyvalue = window.query_keyvalue;
		        // 释放页面图片
		        var event = {
		            type: "release"
		        };
		        window.dropEventHandler(event);
		        if (obj.currentDropObj != "" && document.getElementById(obj.currentDropObj)) {
		        	$.pageutils.showErrorDialog("当前控件" + obj.currentDropObj + "在View中已存在！");
		        } else {
		            $.design.toServer(obj, "add");
		        }
		        stopEvent(e);
		    }
		},
		/**
		 * 拖动mouseover事件
		 * @param {} e
		 */
		onmouseover : function(e) {
		    if (window.currentDropObj != null) {
		        $.design.toDragSelected(e);
		        stopEvent(e);
		    }
		},
		/**
		 * 拖动右键事件
		 * @param {} e
		 * @return {Boolean}
		 */
		oncontextmenu : function(e) {
		    if (window.currentDropObj != null) {
		        e = EventUtil.getEvent();
		        var event = {
		            type: "release"
		        };
		        window.dropEventHandler(event);
		        // stopEvent(e);
		        // 删除事件对象（用于清除依赖关系）
		        // clearEventSimply(e);
		        return false;
		    }
		}
	});
	
})(jQuery);



function isChildOfObj(parent, child) {
    if (parent.children.length > 0) {
        for (var i = 0; i < parent.children.length; i++) {
            if (parent.children[i] == child) return true;
            if (isChildOfObj(parent.children[i], child)) return true;
        }
    }
    return false;
}


/**
 * 增加样式
 * @param {} obj
 * @param {} className
 */
function addClassName(obj, className) {
    if (obj.className) {
        if (obj.className.indexOf(className) >= 0) {
            return;
        } else {
            obj.className = obj.className + ' ' + className;
        }

    } else {
        obj.className = className;
    }

}
/**
 * 移除样式
 * @param {} obj
 * @param {} className
 */
function removeClassName(obj, className) {
    if (obj.className) {
        var c = obj.className;
        var array = c.split(' ');
        var a = new Array();
        for (var i = 0; i < array.length; i++) {
            if (array[i] != className) {
                a.push(array[i]);
            }
        }
        obj.className = a.join(' ');
    }
    return;

}

/**
 * 获得Panel的手柄
 */
function getPanelHanler(obj, create) {
    var handId = obj.id + "_hand";
    var handler = $("#"+handId)[0];
    if (handler != null && typeof(handler) != "undefined") {} else {
        if (create) {
            var handId = obj.id + "_hand";
         
            var handImg = $("<img>").attr({"src":window.themePath + "/global/images/icon/24/edit.png","id":handId + "_img"})
        	.css({"cursor":"pointer","width":"24px","height":"24px"});
             var handDiv = $("<div>").attr({"id":handId,"haswidth":"1","hasheight":"1"})
        	.append(handImg)
        	.css({"position":"absolute","left":"0px","top":"0px","width":"20px","height":"20px","cursor":"pointer","zIndex":"64700"});
            obj.appendChild(handDiv[0]);
            handler = handDiv[0];
        }

    }
    return handler;
}


/**
 * 控件拖放事件处理
 * 
 * @param event
 */
window.dropEventHandler = function(event) {
    if (event.type == "start") {
        window.currentDropObj = event.componentId;
        window.currentDropObjType = event.compType;
        window.currentDropObjType2 = event.compType2;
        window.currentDropDsId = event.text;
        window.currentDropPid = event.pid;
        window.query_keyvalue = event.query_keyvalue;
        //创建拖拽面板
        var dropFlagPanel = this.getDropFlagPanel();
        dropFlagPanel.innerHTML = event.text;
    }
    if (event.type == "release") {
        window.getDropFlagPanel().style.display = "none";
        window.currentDropObj = null;
    }
};

//
function dropPanelMouseEventHandler(event) {
    if (!window.currentDropObj){
    	return;
    } 
    if (window.dragObj){
    	return;
    } 
    var dropFlagPanel = window.getDropFlagPanel();
    var flagStyle = dropFlagPanel.style;
    // 这里没有处理IE兼容性 喜海到时候处理下 方法是 pageX + scrollLeft - clientLeft
    flagStyle.left = (event.clientX + 10) + "px";
    flagStyle.top = (event.clientY + 10) + "px";
    flagStyle.display = "block";
    flagStyle.zIndex = 2147483647;
}

/**
 * 获得拖放的面板，不存在则创建
 */
window.getDropFlagPanel = function() {
    var dropFlagPanel = $("#dropFlagPanel");
    if (dropFlagPanel.size()==0) {
        var dropFlagPanel = $("<div id='dropFlagPanel'>")
        .css({"position":"absolute","width":"250px","height":"60px","border":"solid 1px #0992C1","background-color":"#C6E9EE","display":"none"});
        document.body.appendChild(dropFlagPanel[0]);
        $(document).on("mousemove",dropPanelMouseEventHandler);
    }
    return dropFlagPanel[0];
};

/**
 * 更新属性值，触发后台事件
 */
window.updateProperty = function(obj) {
    if (obj.updateid) {
        //删除原始对象
        var dvId = "$d_" + obj.widgetid + "_" + obj.oldvalue;
        var trueObj = document.getElementById(dvId);
        //通知服务器修改对象
        $.design.toServer(obj, $.design.Constant.UPDATEID);
    } else {
        $.design.toServer(obj, "update");
    }
};

window.datasetOper = function(obj,oper) {
	$.design.toServer(obj, oper);
};

function callParent(obj, oper) {
    var parent = window.parent;
    if (parent && parent.callBack) {
        parent.callBack(obj, oper);
    }
}


/**
 * 点击选择事件
 * @param {} id
 */
window.toClickSelected = function(id) {
    var obj = document.getElementById(id);
    if ($.browsersupport.IS_STANDARD) {
        try {
            var evt = document.createEvent("MouseEvents");
            evt.initEvent("click", true, true);
            var element = document.getElementById(id);
            if (element != null && typeof(element) != "undefined") {
               $("#"+id)[0].dispatchEvent(evt);
            }
        } catch(error) {
            Logger.error(error.name + ":" + error.message);
        }
    } else {
        if (obj != null) {
            try {
                obj.click();
            } catch(e) {

}
        }
    }
};
/**
 * 点击删除事件
 * @param {} id
 */
window.toDeleteSelected = function(id) {
    var obj = $("#"+id)[0];
    $.design.toServer(obj, "delete");
};

window.toAddSelected = function(obj) {
    var trueObj = $("#"+obj.id)[0];
    trueObj.currentDropObjType2 = obj.currentDropObjType2;
    $.design.toServerProxy(trueObj, "add");
};

/**
 * 模型的右键菜单触发跳转方法
 * 
 * @param {} obj:传递的具体参数
 * @param {} oper:操作类型
 */
window.toOperate = function(obj, oper) {
    var trueObj = $("#"+obj.id + "_raw")[0];
    if (trueObj) {
        execDynamicScript2RemoveComponent(obj.id + "_raw", obj.widgetid, obj.eleid);
    } else {
       execDynamicScript2RemoveComponent(obj.id, obj.widgetid, obj.eleid);
    }
    $.design.toServer(obj, oper);
};

/**
 * 修改控件ID通知
 */
function triggerChangerId(oldId, newId) {
    var obj = {};
    obj.oldId = oldId;
    obj.newId = newId;
    triggerEditorEvent("triggerChangerId", obj);
}

/**
 * 创建一个Eclipse交互事件
 * @param {} type
 * @param {} source
 * @return {}
 */
function triggerEditorEvent(type, source) {
    var eventContext = "event:";
    eventContext += type;
    for (i in source) {
        eventContext += ",,,";
        eventContext += i;
        eventContext += ":";
        eventContext += source[i];
    }
    window.status = eventContext;
    return true;
}

/**
 * 刷新数据集
 */
function refreshDS(viewId, dsId, masterKey) {
    var window_obj = window;
    var page_UI = window_obj.pageUI;
    while (page_UI) {
        var widget = page_UI.getViewPart(viewId);
        if (widget) {
            var dataset = widget.getDataset(dsId);
            if (dataset) {
                dataset.setCurrentPage(0, null, null, true);
                break;
            }
        }
        window_obj = window_obj.parent;
        page_UI = window_obj.pageUI;
    }
}

function reNewModel(id, type, widgetid) {
    var proxy = $.serverproxy.getObj({async:false});
    //添加参数  
    proxy.addParam('clc', 'xap.lui.psn.pamgr.PaModelReNewController');
    proxy.addParam('el', '2');
    proxy.addParam('m_n', 'handlerEvent');
    proxy.addParam('id', id);
    proxy.addParam('typo', type);
    proxy.addParam('wid', widgetid);
    proxy.execute();
}

var ARROW = {
	UP: 38,
	DOWN: 40,
	LEFT: 37,
	RIGHT: 39
};

/**
 * 键盘事件Handler
 */
function keyDownHandler(e){
	var event=e||window.event;
	if(window.selectedArray&&window.selectedArray.length>0){
		switch(event.keyCode){
			case ARROW.UP:
				e.ctrlKey?alignOp.top():alignOp.move(event.keyCode,1);
				break;
			case ARROW.DOWN:
				e.ctrlKey?alignOp.bottom():alignOp.move(event.keyCode,1);
				break;
			case ARROW.LEFT:
				e.ctrlKey?alignOp.left():alignOp.move(event.keyCode,1);
				break;
			case ARROW.RIGHT:
				e.ctrlKey?alignOp.right():alignOp.move(event.keyCode,1);
				break;
		}
		e.preventDefault();
		stopEvent(e);
	}else if($(".edit_component_selected").length>0){
		var currentEditObj=$(".edit_component_selected");
		currentEditObj=currentEditObj[0];
		if(currentEditObj.parentNode.type!=="absolutelayout")
			return;
		var dragObj=currentEditObj;
		var dragObjLeft=+dragObj.style.left.replace("px","");
		var dragObjTop=+dragObj.style.top.replace("px","");
		var moveFlag=false;
		if(event.keyCode==ARROW.UP){
			dragObj.style.top=dragObjTop-1+"px";
			moveFlag=true;
		}
		if(event.keyCode==ARROW.DOWN){
			dragObj.style.top=dragObjTop+1+"px";
			moveFlag=true;
		}
		if(event.keyCode==ARROW.LEFT){
			dragObj.style.left=dragObjLeft-1+"px";
			moveFlag=true;
		}
		if(event.keyCode==ARROW.RIGHT){
			dragObj.style.left=dragObjLeft+1+"px";
			moveFlag=true;
		}
		if(moveFlag){
			var obj = dragObj.parentNode;
			obj.currentDropObj =dragObj.uiid;
			obj.currentDropDsId = dragObj.widgetid;
			obj.currentDropObjType = dragObj.type;
			obj.layoutType=dragObj.parentNode.type;
			obj.left=dragObj.style.left.replace("px","");
			obj.top=dragObj.style.top.replace("px","");
			$.design.toServer(obj, "move");
		}
		e.preventDefault();
		stopEvent(e);
	}else if(e.keyCode== 83&&e.ctrlKey){
		$(window.parentWindow.document).find("#save_file_toolbarItemDiv").children().click();
		e.preventDefault();
		stopEvent(e);
	}
	
}

/**
 * 自由布局加载时绑定鼠标事件
 */
function bindAbsoluteLayoutEvent(absoluteLayoutId){
	$('#'+absoluteLayoutId).mousedown(absoluteLayoutMouseDownHandler);
}

/**
 * 自由布局区域鼠标按下事件
 * 做框选功能
 */
function absoluteLayoutMouseDownHandler(e) {
	var event = e || window.event;
	var $targetElem = $(event.target);
	var scrollTop=$targetElem.scrollTop();
	var scrollLeft=$targetElem.scrollLeft();
	if (e.target.type==="absolutelayout"){
		var dropX = event.offsetX+scrollLeft-10;
		var dropY = event.offsetY+scrollTop-10;
		window.selectAreaStartPoint = { x: event.pageX+scrollLeft-10, y: event.pageY+scrollTop-10 ,top:dropX,left:dropY};
		if(window.selectedArray&&window.selectedArray.length>0){
			if(!e.ctrlKey){
				window.selectedArray.forEach(function(cNode){
					cNode.classList.remove("item-selected");
				});
			}
		}
		
		var selectAreaHtml = [];
		selectAreaHtml.push('<div id="absoluteSelectedArea" class="absolute-selected-area" style="top:');
		selectAreaHtml.push((dropY) + 'px;left:');
		selectAreaHtml.push((dropX ) + 'px;');
		selectAreaHtml.push('"></div>');
	
		$targetElem.append(selectAreaHtml.join(''));
		$targetElem.mousemove(absoluteLayoutMouseMoveHandler);
		$targetElem.mouseup(absoluteLayoutMouseUpHandler);
	}
}

/**
 * 空白区移动鼠标，绘制选中区域
 * @param e
 */
function absoluteLayoutMouseMoveHandler(e) {
	var $layoutElem=$("#absoluteSelectedArea").parent();
	var scrollLeft=$layoutElem.scrollLeft();
	var scrollTop=$layoutElem.scrollTop();
	var event = e || window.event;
	var dropX=event.pageX+scrollLeft-10;
	var dropY = event.pageY+scrollTop-10;
	var height = dropY - selectAreaStartPoint.y;
	var width = dropX - selectAreaStartPoint.x;
	$("#absoluteSelectedArea").css("height", Math.abs(height) + "px").css("width", Math.abs(width) + "px");
	if (height > 0) {
		if (width > 0) {
			$("#absoluteSelectedArea").css("left", (selectAreaStartPoint.top ) + "px");
			$("#absoluteSelectedArea").css("top", (selectAreaStartPoint.left ) + "px");
		} else {
			$("#absoluteSelectedArea").css("left", (selectAreaStartPoint.top - Math.abs(width) ) + "px");
			$("#absoluteSelectedArea").css("top", (selectAreaStartPoint.left ) + "px");
		}
	} else {
		if (width > 0) {
			$("#absoluteSelectedArea").css("left", (selectAreaStartPoint.top ) + "px");
			$("#absoluteSelectedArea").css("top", (selectAreaStartPoint.left - Math.abs(height)) + "px");
		} else {
			$("#absoluteSelectedArea").css("left", (selectAreaStartPoint.top - Math.abs(width) ) + "px");
			$("#absoluteSelectedArea").css("top", (selectAreaStartPoint.left - Math.abs(height) ) + "px");
		}
	}
}

/**
 * 空白区域释放，删除选中区域，处理框选动作
 * @param e
 */
function absoluteLayoutMouseUpHandler(e) {
	var absoluteLayout=$(".absolute-selected-area").parent().unbind("mousemove").unbind("mouseup")[0];
	$(".absolute-selected-area").remove();
	if (!selectAreaStartPoint)
		return;
	var event = e || window.event;
	var $targeElem=$(e.target);
//	stopEvent(event);
	var scrollLeft=$(absoluteLayout).scrollLeft();
	var scrollTop=$(absoluteLayout).scrollTop();
	var dropX = event.pageX+scrollLeft-10;
	var dropY = event.pageY+scrollTop-10;
	if(window.selectedArray==null||!e.ctrlKey)
		window.selectedArray=[];
	var selectAreaRect = {
		x: selectAreaStartPoint.x,
		y: selectAreaStartPoint.y,
		width: dropX - selectAreaStartPoint.x,
		height: dropY - selectAreaStartPoint.y
	};
	selectAreaStartPoint = null;
	setTimeout(function () {
		[].forEach.call(absoluteLayout.childNodes,function(cNode){
			if(cNode.classList.contains("edit_component")||cNode.classList.contains("edit_component_selected")){
				if (isOverlaped({
						x:cNode.offsetLeft,
						y:cNode.offsetTop,
						width:$(cNode).width(),
						height:$(cNode).height()
					},selectAreaRect))
				{
					cNode.classList.add("item-selected");
					selectedArray.push(cNode);
				}
			}
			
		});
	});
}
/**
 * 判断两个矩形是否重叠
 * @param {x:x,y:y,width:width,height:height}
 * @param {x:x,y:y,width:width,height:height}
 */
function isOverlaped(rect1, rect2) {
	var rect1CP = getCenterPointPos(rect1.x, rect1.y, rect1.width, rect1.height);
	var rect2CP = getCenterPointPos(rect2.x, rect2.y, rect2.width, rect2.height);
	if (Math.abs(rect1CP.x - rect2CP.x) < (Math.abs(rect1.width) + Math.abs(rect2.width)) / 2 && Math.abs(rect1CP.y - rect2CP.y) < (Math.abs(rect1.height) + Math.abs(rect2.height)) / 2)
		return true;
	return false;
}

/**
 * 获取矩形中心坐标
 * @param x
 * @param y
 * @param width
 * @param height
 * @returns {___anonymous40668_40693}
 */
function getCenterPointPos(x, y, width, height) {
	return { x: x + width / 2, y: y + height / 2 };
}

var alignOp={
     top:function(){
		//获取最小的top值
		var _minTop=Number.MAX_SAFE_INTEGER;
		window.selectedArray.forEach(function(node){
			_minTop=Math.min(_minTop,node.offsetTop);
		});
		window.selectedArray.forEach(function(node){
			node.style.top=_minTop+'px';
		});
		alignOp._syncMove();
	 },
	 bottom:function(){
		 //获取最大的bottom值
		 var _maxBottom=Number.MIN_SAFE_INTEGER;
		 window.selectedArray.forEach(function(node){
			var _nodeBottom=node.offsetTop+$(node).height();
			_maxBottom=Math.max(_maxBottom,_nodeBottom);
		 });
		 window.selectedArray.forEach(function(node){
			 node.style.top=_maxBottom-$(node).height()+'px';
		 });
		 alignOp._syncMove();
	 },
	 left:function(){
		 //获取最小的left值
		 var _minLeft=Number.MAX_SAFE_INTEGER;
		 window.selectedArray.forEach(function(node){
			 _minLeft=Math.min(_minLeft,node.offsetLeft);
		 });
		 window.selectedArray.forEach(function(node){
			 node.style.left=_minLeft+'px';
		 });
		 alignOp._syncMove();
	 },
	 right:function(){
		 //获取最大的right值
		 var _maxRight=Number.MIN_SAFE_INTEGER;
		 window.selectedArray.forEach(function(node){
			var _nodeRight=node.offsetLeft+$(node).width();
			_maxRight=Math.max(_maxRight,_nodeRight);
		 });
		 window.selectedArray.forEach(function(node){
			 node.style.left=_maxRight-$(node).width()+'px';
		 });
		 alignOp._syncMove();
	 },
	 move:function(forward,step){
		 window.selectedArray.forEach(function(node){
			switch(forward){
			case ARROW.UP:
				node.style.top=node.offsetTop-step+'px';
				break;
			case ARROW.DOWN:
				node.style.top=node.offsetTop+step+'px';
				break;
			case ARROW.LEFT:
				node.style.left=node.offsetLeft-step+'px';
				break;
			case ARROW.RIGHT:
				node.style.left=node.offsetLeft+step+'px';
				break;
			}
		 });
		 alignOp._syncMove();
	 },
	 _syncMove:function(){
		var obj = null;
		var uiidArr=[];
		var leftArr=[];
		var topArr=[];
		var typeArr=[];
		var widgetidArr=[];
		window.selectedArray.forEach(function(node){
			if(obj==null){
				obj=node.parentNode;
				obj.layoutType=node.parentNode.type;
			}
			
			uiidArr.push(node.uiid);
			widgetidArr.push(node.widgetid);
			typeArr.push(node.type);
			leftArr.push(node.offsetLeft);
			topArr.push(node.offsetTop);
		});
		obj.currentDropObj=uiidArr.join(',');
		obj.currentDropDsId=widgetidArr.join(',');
		obj.currentDropObjType=typeArr.join(',');
		obj.left=leftArr.join(',');
		obj.top=topArr.join(',');
		$.design.toServer(obj, "move");
	 }
};
