/**
 *														
 * @fileoverview Menubar实现,其级联子菜单由ContextMenu组成				
 * 事件处理可根据粒度来重写不同级别的方�?比如�?			 
 * 可分别重写ContextMenuItem, ContextMenu, MenuBar 
 * 来进行不同粒度控�?任一方法中返回false，阻止父级方法进一步处�?			   
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * MenuBarComp构造函数
	 * @class Menubar实现,其级联子菜单由ContextMenu组成				
	 *        事件处理可根据粒度来重写不同级别的方�?比如�?			 
	 *        可分别重写ContextMenuItem, ContextMenu, MenuBar 
	 *        来进行不同粒度控�?任一方法中返回false，阻止父级方法进一步处理�?
	 */
	$.widget("lui.menubar", $.lui.base, {
		options : {
			position : 'relative',
			className : 'white_menubar_div2',
			//event
			onmouseover : null,
			onmouseout : null,
			onclick : null
			
		},
		_initParam : function() {
			this.componentType = 'MENUBAR';
			this.parentOwner = this.element.parent()[0];
			this.id = this.element.attr("id");
			this.menuHeight = 24;
			this.opeWidth = 25;
			this._super();
			// 记录当前显示的子菜单
			this.currVisibleChildMenu = null;
			// 记录当前正在点击的菜单项
			this.nowClickedMenu = null;
			// 保存MenuBar中添加的所有menuitem
			this.menuItems = null;
			
			// 分隔条集合
			this.seps = [];
			// 分隔条索引
			this.sepIndex = 0;
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element;
			ele.addClass(opts.className).css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'position' : opts.position,
				'width' : opts.width,
				'height' : this.menuHeight + "px"
			});

			this.centerDiv = $("<div></div>").addClass('center_div').appendTo(ele);
		},
		/** 
		 * 销毁
		 * @private
		 */
		destroySelf : function() {
			// 销毁子菜单
			if(this.menuItems){
				var items = this.menuItems.values();
				for (var i = 0, n = items.length; i < n; i++) {
					var item = items[i];
					//item.destroySelf();
					//item.remove();
				}
			}
			//this.destroy();
			this.element.remove();
		},
		
		removeMenu:function(menuId){
				var opts = this.options,ele = this.element,oThis = this;
				var menuItem=this.menuItems.get(menuId);
				menuItem.destroySelf();
				$("#"+menuId+"_menuItemDiv").remove();
				this.menuItems.remove(menuId);
			
		},
		
		/**
		 * 给MenuBar添加子菜单。每个子菜单是MenuItemComp�?
		 * @param ContextMenuItem 若此项为null,说明要创建MeneBarComp的Menu,否则传入要添加子菜单的item�?
		 * @param isCheckBoxGroup 表示此menu是否是checkboxgroup
		 */
		addMenu : function(menuId, menuCaption, menuTip, menuSrcImg, menuName, isCheckBoxGroup, attrObj) {	
			attrObj = attrObj || {};
			var opts = this.options,ele = this.element,oThis = this;
			if (this.menuItems == null)
				this.menuItems = $.hashmap.getObj();
			var menuItemDiv = $("<div id='"+menuId+"_menuItemDiv'>").appendTo(this.centerDiv).css("float","left");
			var menuItem = $("<div id=\""+menuId+"\"></div>").appendTo(menuItemDiv).menubaritem({
				parent : oThis,
                caption : menuCaption,
                tip : menuTip,
                refImg : menuSrcImg,
                imgIconOn : attrObj.imgIconOn,
                imgIconDisable : attrObj.imgIconDisable
			}).menubaritem('instance');
			// 将添加的menu放入全局数组
			this.menuItems.put(menuId, menuItem);
			
			if (window.editMode) {
	            var params = {
	                uiid: "",
	                eleid: this.element.attr("id"),
	                widgetid: attrObj.viewPartId || "",
	                type: "menubar_menuitem",
	                subeleid: menuId
	            };
	            if (!window.windowEditorMode) {
	                $.design.getObj({divObj:menuItemDiv[0], params:params, objType:$.design.Constant.COMPOMENT_TYPE});
	            }
		    }
			return menuItem;
		},
		/**
		 * 向菜单中增加分割
		 */
		addSep : function() {
			var id = this.element.attr("id") + "_sep_" + this.sepIndex;
			var sep = $("<div id=\"" + id + "\"></div>")
					.appendTo(this.centerDiv).menubarsep().menubarsep("instance");

			// 存储此菜单的所有分隔条
			this.sepIndex = this.seps.push(sep);

			// 保存此item的父菜单
			sep.parentOwner = this;
			sep.index = this.sepIndex - 1;
//			this.addItemHtml(sep);
			return sep;
		},
		/**
		 * 根据menuId得到menu对象
		 * @param menuId
		 */
		getMenu : function(menuId) {
			if (menuId == null || menuId == "")
				return null;
			if (this.menuItems != null){
				return this.menuItems.get(menuId);
//				var menuitem = this.menuItems.get(menuId)
//				if(menuitem)
//					return menuitem;
//				else{
//					var childitems = this.menuItems.values();
//					for(var i=0; i<childitems.length; i++){
//						var childitem = childitems[i];
//						if(childitem.childmenu){
//							var ccit = childitem.childmenu.getMenu(menuId);
//							
//						}
//					}
//					if(recGetMenu(menuId, menuitem.menuItems))
//						return recGetMenu(menuId, menuitem.menuItems);
//				}
			}
			return null;	
		},
		recGetMenu : function(menuId, childMenuItems){
			if(childMenuItems != null){
				var menuitem = this.menuItems.get(menuId)
				if(menuitem)
					return menuitem;
				else{
					if(recGetMenu(menuId, menuitem.menuItems))
						return recGetMenu(menuId, menuitem.menuItems);
				}
			}
		},

		/**
		 * 响应快捷键（只响应第一个匹配子项的点击事件�?
		 * @private
		 */
		handleHotKey : function(key) {
			if (this.element.css("display") == "none" || this.element.css("visibility") == "hidden")
				return null;
			// 匹配子项快捷键
			if(this.menuItems){
				var childItems = this.menuItems.values();
				if (childItems.length > 0) {
					for (var i = 0, n = childItems.length; i < n; i++) {
						var obj = childItems[i].handleHotKey(key);
						if (obj != null)
							return childItems[i];
					}
				}
			}
			return null;
		},
		/**
		 * 增加自定义子�?
		 * @param width 宽度
		 * @param innerHTML 内容代码
		 */
		setSelfDefItem : function(width, innerHTML) {
			var selfDef = $("<div></div>").css({
				'width' : (width.indexOf("%") == -1 ? $.argumentutils.getInteger(width) + "px" : width),
				'height' : '100%',
				'float' : 'right',
				'margin-right' : '5px'
			}).html(innerHTML).appendTo(this.centerDiv);

			return selfDef;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			var context = {};
			context.c = "MenubarContext";
			context.id = this.element.attr("id");
			if (!this.menuItems) {
				if(this.ctxChanged == false)
					return null;
				context.childItemContexts = [];
				return context;
			}
			
			// 获取子项Context
			var childItems = this.menuItems.values();
			if (childItems.length > 0) {
				context.childItemContexts = [];
				var changed = false;
				for (var i = 0, n = childItems.length; i < n; i++) {
					var ctx = childItems[i].getContext();
					if(ctx != null){
						changed = true;
						context.childItemContexts.push(ctx);
					}
				}
				if(!changed && this.ctxChanged == false)
					return null;
			}
			return context;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			// 为子项设置Context
			if (context.childItemContexts) {
				for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
					if(this.menuItems){
						var item = this.menuItems.get(context.childItemContexts[i].id);
						if (item)
							item.setContext(context.childItemContexts[i]);
					}
				}
			}
			this.ctxChanged = false;
		}
	});
	
	/**
	 * MenuSepComp的构造函数，分隔条
	 */
	$.widget("lui.menubarsep", $.lui.base, {
		options : {
			className : 'menuitem_seperator'
		},
		_initParam : function() {
			this._super();

			this.name = this.element.attr("id");
			this.id = this.element.attr("id");
			// 是否禁止此item
			this.disabled = false;
			// 此item的显示属性
			this.visible = true;
		},
		_create : function() {
			this._initParam();
			// item的整体div
			this.element.addClass(this.options.className).css({
						'overflow' : 'hidden',
						'position' : 'relative'
					});
		},

		/**
		 * 设置item高度
		 */
		setItemHeight : function(eleHeight) {
			var height = eleHeight + "";
			if (height.indexOf("%") != -1)
				height = this.element.outerHeight();
			this.height = height - 1;
			this.element.css('height', height + "px");
		},
		/**
		 * 获得分割条宽度,包括图片宽度
		 */
		getItemWidth : function() {
			return this.width;
		}
	});
})(jQuery);
