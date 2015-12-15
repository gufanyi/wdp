/**
 * @fileoverview 右键菜单控件
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * ContextMenuComp的构造函数
	 * 
	 * @class 1、item的parentOwner属性表示此item所在的contextMenu<br>
	 *        2、item的childMenu属性表示此item的子menu<br>
	 *        3、子menu可以通过parentMenu属性访问父menu<br>
	 * 
	 * @constructor
	 * @param posFix
	 *            是否为固定显示位置的contextMenu
	 */
	$.widget("lui.contextmenu", $.lui.base, {
		options : {
			posFix : false,
			// event
			onbeforeclose : null,
			onclose : null,
			onbeforeshow : null,
			onshow : null,
			onmouseout : null,
			onmouseover : null,
			onclick : null
		},
		_initParam : function() {
			this.componentType = "CONTEXTMENU";
			this.id = this.element.attr('id');
			this.parentOwner = this.element.parent()[0];
			this.SEP_HEIGHT = 10;
			this.ITEM_WIDTH = 170;
			this.MAX_ZINDEX = 2147483647;

			this._super();
			this.width = this.ITEM_WIDTH;
			this.visible = false;
			// 标示此menu是否是某一个item的下一级menu
			this.isChildMenu = false;
			// 此菜单的所有的子项
			this.childItems = [];
			// 此菜单所有的子菜单
			this.childMenus = [];
			// 分隔条集合
			this.seps = [];
			// 分隔条索引
			this.sepIndex = 0;
		},
		_create : function() {
			this._initParam();
			var opts = this.options, ele = this.element;
			ele.addClass('contextmenu_div').css({
						'width' : this.width + "px",
						'position' : 'absolute',
						'z-index' : this.MAX_ZINDEX
					}).appendTo('body').hide();

			this.centerContent = $("<div></div>").appendTo(ele)
					.addClass('center_div');

			// 注册外部回掉函数
			window.clickHolders.push(this);
		},
		/**
		 * 销毁控件
		 * 
		 * @private
		 */
		destroySelf : function() {
			this.removeChildrenMenu();
			// this.destroy();
			this.element.remove();
		},
		removeChildrenMenu : function() {
			// 销毁子项
			for (var i = 0, n = this.childItems.length; i < n; i++) {
				var item = this.childItems[i];
				item.destroySelf();
			}
			// 销毁子菜单
			for (var i = 0, n = this.childMenus.length; i < n; i++) {
				var menu = this.childMenus[i];
				menu.destroySelf();
			}
			
			// 清空子项列表
			this.childItems = [];
			this.childMenus = [];
		},
		/**
		 * 外部click事件发生时的回调函数,用来隐藏当前显示的空件
		 * 
		 * @private
		 */
		outsideClick : function(e) {
			if (e != null && 2 == e.button)// 鼠标右键
				return;
			if (this.visible)
				this.hide();
		},
		/**
		 * 增加zIndex
		 * 
		 * @private
		 */
		addZIndex : function(e) {
			this.element.css('z-index', $.argumentutils.getInteger(this.element
							.css('z-index'))
							+ 1);
		},
		setPosFix : function(isPosFix) {
			this.options.posFix = isPosFix;
		},
		/**
		 * 设置菜单的left值
		 * 
		 * @param left
		 *            设置left属性
		 */
		setPosLeft : function(left) {
			this.options.left = $.argumentutils.getInteger(left, 0) - 3;
			// 3左边图片阴影宽度
			this.element.css('left', this.options.left + "px");
		},
		/**
		 * 设置菜单的top值
		 */
		setPosTop : function(top) {
			this.options.top = $.argumentutils.getInteger(top, 0) - 5;
			this.element.css('top', this.options.top + "px");
		},
		/**
		 * 设置菜单的宽度
		 */
		setWidth : function(width) {
			this.width = $.argumentutils.getString(width, "100%");
			if (this.width.toString().indexOf("%") == -1)
				this.element.css('width', this.width + "px");
			// 左右图片宽度和
			else
				this.element.css('width', this.width);
		},
		/**
		 * 设置菜单的高度
		 */
		setHeight : function(height) {
			this.options.height = height;
			this.element.css('height', height + "px");
		},
		/**
		 * 菜单子条目宽度扩大时通知此菜单
		 * 
		 * @private
		 */
		childItemWidthUpdated : function(width) {
			if (this.width < width) {
				// 改变父菜单的宽度
				this.width = width;
				this.element.css('width', this.width + "px");
				// 左右图片宽度和
				// 通知所有子扩大宽度
				for (var i = 0; i < this.childItems.length; i++)
					this.childItems[i].setItemWidth(width);
				// 通知所有分隔条扩大宽度
				for (var i = 0; i < this.seps.length; i++)
					this.seps[i].setItemWidth(width);
			}
		},
		/**
		 * 向菜单中增加子项
		 * 
		 * @param isCheckBoxItem
		 *            是否是checkboxitem
		 * @param isCheckBoxSelected
		 *            此item初始是否选中
		 */
		addMenu : function(name, caption, tip, refImg, isCheckBoxItem,
				isCheckBoxSelected, attrObj) {
			attrObj = attrObj || {};
			var oThis = this;
			var item = $("<div id=\"" + name + "\"></div>")
					.appendTo(this.centerContent).menuitem({//声明一个menuitem对象，并取得实例（menuitem对象在文件底下）
								parent : this,
								caption : caption,
								tip : tip,
								refImg : refImg,
								className : 'menuitem_div',
								isCheckBoxItem : isCheckBoxItem,
								isCheckBoxSelected : isCheckBoxSelected,
								imgIconOn : attrObj.imgIconOn,
								imgIconDisable : attrObj.imgIconDisable
							}).menuitem("instance");//执行instance后返回的才是menuitem对象，如果没有后面，则返回的是jQuery对象

			// 存储此菜单的所有子项
			var index = this.childItems.push(item);

			// 保存此item的父菜单
			item.parentOwner = this;
			item.index = index - 1;
			this.addItemHtml(item);
			item.preMenuItem = this.lastAddItem;
			this.lastAddItem = item;
			return item;
		},

		/**
		 * 向菜单中增加分割线
		 * 
		 * @param isCheckBoxItem
		 *            是否是checkboxitem
		 * @param isCheckBoxSelected
		 *            此item初始是否选中
		 */
		addSep : function(attrObj) {
			var id = this.element.attr("id") + "_sep_" + this.sepIndex;
			var sep = $("<div id=\"" + id + "\"></div>")
					.appendTo(this.centerContent).menusep().menusep("instance");

			// 存储此菜单的所有分隔条
			this.sepIndex = this.seps.push(sep);

			// 保存此item的父菜单
			sep.parentOwner = this;
			sep.index = this.sepIndex - 1;
			this.addItemHtml(sep);

			return sep;
		},
		/**
		 * 获取子菜单
		 */
		getMenu : function(id) {
			for (var i = 0; i < this.childItems.length; i++) {
				if (id == this.childItems[i].name)
					return this.childItems[i];
			}
			return null;
		},
		/**
		 * 向菜单中增加子项的显示对象 在内部方法addMenu()中使用.
		 * 
		 * @private
		 */
		addItemHtml : function(item) {
			// 如果此条目最宽,则通知父菜单
			var itemWidth = item.getItemWidth();
			if (itemWidth > this.width - 2)
				this.childItemWidthUpdated(itemWidth);
			// 否则,根据父菜单宽度调整此条目宽度
			else
				item.setItemWidth(this.width - 2);

			// this.centerContent.append(item.element);
		},
		/**
		 * 增加菜单条目的子菜单 在内部addItem()方法使用.
		 * 
		 * @private
		 */
		addItemChildMenu : function(childMenu, item) {
			childMenu.isChildMenu = true;
			childMenu.menuParent = this;
			this.childMenus.push(childMenu);
			this.childMenus = this.childMenus.concat(childMenu.childMenus);
			childMenu.option.posFix = true;
			document.body.appendChild(childMenu.element[0]);
			if (childMenu.childMenus != null) {
				for (var i = 0; i < childMenu.childMenus.length; i++)
					document.body
							.appendChild(childMenu.childMenus[i].element[0]);
			}
			if (item != null)
				item.setChildMenu(childMenu);
		},
		/**
		 * 添加行分隔符
		 */
		addSeparator : function() {
			var oThis = this;
			var sep = $("<div></div>").css({
						'position' : 'relative',
						'width' : '100%',
						'height' : this.SEP_HEIGHT + "px",
						'background' : '#fff'
					}).html("<hr/>").on('mouseover', function(e) {
						if (oThis.nowActiveMenu) {
							oThis.nowActiveMenu.fermezoneMenu();
							oThis.nowActiveMenu = "";
						}
						e.stopPropagation();
					});
			this.add(sep);
		},
		/**
		 * 隐藏菜单
		 */
		hide : function() {
			if (!this.visible)
				return;
			// 隐藏之前调用用户的方法
			if (this._trigger("onbeforeclose", null) == false)
				return;

			for (var i = 0, count = this.childMenus.length; i < count; i++) {
				if (this.childMenus[i].visible)
					this.childMenus[i].hide();
			}

			// 清空触发项
			this.triggerObjHtml = null;
			// 如果是子菜单，清空父菜单的当前激活子项
			if (this.isChildMenu)
				this.menuParent.nowActiveMenu = null;

			this.element.hide();
			this.visible = false;
			// 隐藏之后调用用户的方法
			this._trigger("onclose", null);
		},
		/**
		 * 调用此方法显示菜单
		 */
		show : function(e) {
			if (arguments != null && arguments.length == 2) {
				this.triggerObjHtml = arguments[1];
			}
			
			// 菜单列表为空，不显示
			if(0 == this.childItems.length && 0==this.childMenus.length) return;

			// 在菜单显示之前调用用户的方法,用户返回false则不显示菜单
			if (this._trigger("onbeforeshow", e) == false) {
				return;
			}

			this.visible = true;
			this.element.show();
			// 如果是显示子菜单,记录当前激活的子菜单
			if (this.isChildMenu)
				this.menuParent.nowActiveMenu = this;

			if (!this.options.posFix)
				$.measures.positionneSelonEvent(this.element, e);
			else {
				$.measures.positionneSelonPosFournie(this.element,
						this.options.top + 5, this.options.left + 2);
			}
			e.preventDefault();
			// 显示之后调用用户的方法
			this._trigger("onshow", e);
		},
		/**
		 * 将触发菜单显示的控件对象绑定到事件对象上
		 * 
		 * @private
		 */
		mouseover : function(e) {
			this._trigger("onmouseover", e);
		},
		/**
		 * @private
		 */
		mouseout : function(item) {
			this._trigger("onmouseout", e, item);
		},
		/**
		 * 默认子菜单事件处理。 子条目被点击后会掉用这个方法。
		 * 
		 * @private
		 */
		click : function(e) {
			// var menu = e.triggerItem.parentOwner;
			var menu = e.targetItem.options.parent;
			while (menu.isChildMenu)
				menu = menu.menuParent;
			if (menu.menubar)
				menu.menubar.click(e);
			else
				menu._trigger("onclick", e);
			menu.hide();
		},
		/**
		 * 获取选中的项
		 */
		getSelectedItems : function() {
			var items = this.childItems;
			var selItems = [];
			if (items != null && items.length > 0) {
				for (var i = 0, count = items.length; i < count; i++) {
					if (items[i].checkbox && items[i].checkbox.checked)
						selItems.push(items[i]);
				}
			}
			return selItems;
		},
		/**
		 * 获取当前的显示项
		 */
		getVisibleItems : function() {
			var items = this.childItems;
			var visibleItems = [];
			if (items != null && items.length > 0) {
				for (var i = 0, count = items.length; i < count; i++) {
					if (items[i].visible == true)
						visibleItems.push(items[i]);
				}
			}
			return visibleItems;
		},
		/**
		 * 此方法暂时为portal页面定制。更加通用的方法在以后补充
		 * 
		 * @private
		 */
		hideItems : function(indice) {
			if (indice == null)
				return;
			for (var i = 0, count = indice.length; i < count; i++) {
				var index = indice[i];
				var item = this.childItems[index];
				item.element.hide();
				item.visible = false;
			}
		},
		/**
		 * 此方法暂时为portal页面定制,更加通用的方法在以后补充
		 * 
		 * @private
		 */
		showItems : function(indice) {
			if (indice == null)
				return;
			for (var i = 0, count = indice.length; i < count; i++) {
				var index = indice[i];
				var item = this.childItems[index];
				item.element.show();
				item.visible = true;
			}
		},
		/**
		 * 响应快捷键（只响应第一个匹配子项的点击事件）
		 * 
		 * @private
		 */
		handleHotKey : function(key) {
			if (this.element.css('display') == "none"
					|| this.element.css('visibility') == "hidden")
				return null;
			// 匹配子项快捷键
			var childItems = this.childItems;
			if (childItems.length > 0) {
				for (var i = 0, n = childItems.length; i < n; i++) {
					var obj = childItems[i].handleHotKey(key);
					if (obj != null)
						return childItems[i];
				}
			}
			return null;
		},
		/**
		 * 根据索引获取子项对象
		 */
		getItemByIndex : function(index) {
			return this.childItems[index];
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function() {
			var context = {};
			context.c = "ContextMenuContext";
			context.id = this.element.attr("id");
			if (this.triggerObj != null)
				context.triggerId = this.triggerObj.id;
			// 获取子项Context
			if (this.childItems.length > 0) {
				context.childItemContexts = [];
				for (var i = 0, n = this.childItems.length; i < n; i++) {
					var ctx = this.childItems[i].getContext();
					if (ctx == null)
						continue;
					context.childItemContexts.push(this.childItems[i]
							.getContext());
				}
			}

			return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
			// 为子项设置Context
			if (context.childItemContexts) {
				for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
					for (var j = 0, m = this.childItems.length; j < m; j++) {
						if (this.childItems[j].id == context.childItemContexts[i].id) {
							this.childItems[j]
									.setContext(context.childItemContexts[i]);
							break;
						}
					}
				}
			}
		}
	});

	var MenuConst = {
		ITEM_HEIGHT : 24,
		IMAGE_WIDTH : 27
	};

	/**
	 * 
	 * @fileoverview MenuItem控件,是ContextMenuComp和MenuBarComp的子项
	 * @author lxl
	 * @version lui 1.0
	 * 
	 */
	$.widget("lui.menuitem", $.lui.base, {
		/**
		 * MenuItemComp的构造函数
		 * 
		 * @param tip
		 *            鼠标悬停时的提示信息
		 * @param childMenu
		 *            MenuItem子项的子菜单
		 * @param isMenuBarItem
		 *            如果是menubar中的item,则item不显示左侧的leftDiv,有子menu则显示向下的箭头;
		 *            如果是contextMenu中的item则显示左侧leftDiv,有子显示像右的箭头
		 * @param isCheckBoxGroup
		 *            如果isMenuBarItem为true,则此参数表示此菜单是否checkbox组,如果是,每个子item的左侧创建checkbox控件
		 * @param isCheckBoxSelected{boolean}
		 *            如果isMenuBarItem为false,则表示该item项左侧的checkbox初始是否选中
		 * @param isCheckBoxItem{boolean}
		 *            如果isMenuBarItem为false,则表示该item是否是checkbox项
		 * @class
		 */
		options : {
			parent : null,
			height : 24,
			caption : '',
			tip : '',
			refImg : '',
			className : 'menuitem_div',
			isMenuBarItem : false,
			isCheckBoxGroup : false,
			isCheckBoxItem : false,
			isCheckBoxSelected : false,
			imgIconOn : null,
			imgIconDisable : null,
			// event
			onContainerCreate : null,
			onclick : null,
			onmouseover : null
		},
		_initParam : function() {
			this.componentType = "MENUITEM";
			this._super();

			this.refImgOn = null;
			this.refImgDisable = null;
			if (this.options.imgIconOn)
				this.refImgOn = this.options.imgIconOn;
			if (this.options.imgIconDisable)
				this.refImgDisable = this.options.imgIconDisable;

			this.name = this.element.attr("id");
			this.id = this.element.attr("id");
			this.childMenu = null;
			// 是否禁止此item
			this.disabled = false;
			// 是否是checkbox item
			this.options.isCheckBoxItem = $.argumentutils.getBoolean(
					this.options.isCheckBoxItem, false);
			if (this.options.isCheckBoxItem)
				this.options.isCheckBoxSelected = $.argumentutils.getBoolean(
						this.options.isCheckBoxSelected, false);
			// 此item的显示属性
			this.visible = true;
			// 如果是menubaritem并且是checkbox组,则此属性记录当前处于选中状态的子菜单
			if (this.options.isCheckBoxGroup)
				this.selectedItem = null;
			this.options.tip = $.argumentutils.getString(this.options.tip,
					this.options.caption);
		},
		_create : function() {
			this._initParam();
			var oThis = this, opts = this.options, ele = this.element;
			// item的整体div
			ele.addClass(opts.className).attr({
						'id' : "menu_div_" + this.id
					}).css({
						'overflow' : 'hidden',
						'position' : 'relative',
						'width' : 'auto',
						'height' : opts.height + "px"
					});

			ele.data("owner", this);

			var content = this.createContent();
			if (null != content)
				ele.append(content);

			var clickHandler = function(e) {
				e.targetItem = oThis;
				var _menuParent = opts.parent;
				// 显示Menubar第一层按钮的子菜单
				if (oThis.childMenu != null && oThis.childMenu != "") {
					e.stopPropagation();
					return;
				}
				if (oThis._trigger("onclick", e)) {
					_menuParent.click(e);
				}
			};

			ele.data('clickEvent', clickHandler);
			ele.off("click").on('click', function(e) {
						clickHandler(e);
					});

			// 右键菜单事件
			ele[0].oncontextmenu = function(e) {
				oThis._trigger("onBeforeShowMenu", e);
				oThis._trigger("oncontextmenu", e);
			};
			this.ctxChanged = false;
		},
		createContent : function() {
			var oThis = this, opts = this.options, ele = this.element;

			this.leftDiv = $("<div></div>")
					.addClass('contextmenu_item_left_div').appendTo(ele);
			// 创建checkbox控件
			if (opts.isCheckBoxItem) {
				this.checkbox = $("<input type=\"checkbox\"/>")
						.addClass('checkbox_box').appendTo(this.leftDiv);
				// 声明在默认情况下是否被选中了
				this.checkbox.prop('checked', opts.isCheckBoxSelected);
				this.checkbox.on('click', function(e) {
							e.stopPropagation();
						});
			}

			// 显示文字的Div
			this.divCaption = $("<div></div>")
					.addClass('contextmenu_item_caption').css({
								'line-height' : MenuConst.ITEM_HEIGHT + "px"
							}).appendTo(ele);
			this.rightDiv = $("<div></div>")
					.addClass('contextmenu_item_right_div').appendTo(ele);

			if (opts.refImg) {// 是Menubar子项并且有图片
				// 显示图片
				//this.changeImg(opts.refImg);
				$("<div>").css({
					'background' : 'url('+opts.refImg+')',
					'width' : '12px',
					'height' : '12px',
					'margin-left' : '5.5px',
					'margin-top' : '6px'
				}).appendTo(this.leftDiv);
			}
			this.divCaption.html(opts.caption);
			this.divCaption.attr('title', opts.tip);

			if (this.childMenu != null && this.childMenu != "") {
				this.createRightDiv();
			}

			var mouseoverHandler = function(e) {
				e.targetItem = oThis;
				ele.addClass("menuitem_div_on");
				oThis._trigger("mouseover", e);

				var _menuParent = opts.parent;

				// 隐藏不该显示的子菜单
				if (_menuParent.nowActiveMenu != null)
					oThis.hideChildMenus(_menuParent.nowActiveMenu);

				// 如果有子菜单则显示子菜单
				if (oThis.childMenu != null && oThis.childMenu != "") {
					oThis.showChildMenu(e);
				}

				// 将此item对象传给父组件
				if (_menuParent.mouseover != null)
					_menuParent.mouseover(e);
				e.stopPropagation();
			};

			var mouseoutHandler = function(e) {
				ele.removeClass("menuitem_div_on");
				e.stopPropagation();
			};

			ele.data('mouseoverEvent', mouseoverHandler).data('mouseoutEvent',
					mouseoutHandler);
			// 鼠标移动到item上
			ele.off("mouseover mouseout").on('mouseover', function(e) {
						mouseoverHandler(e);
					}).on('mouseout', function(e) {
						mouseoutHandler(e);
					});
		},
		/**
		 * 销毁控件
		 * 
		 * @private
		 */
		destroySelf : function() {
			// 销毁子菜单
			this.element.remove();
			if (this.childMenu)
				this.childMenu.destroySelf();
			// this.destroy();
		},
		/**
		 * 显示子菜单
		 * 
		 * @private
		 */
		showChildMenu : function(e) {
			// 记录当前激活子菜单
			var _menuParent = this.options.parent;
			_menuParent.nowActiveMenu = this.childMenu;
			this.childMenu.setPosLeft(this.element.offset().left
					+ this.options.width);
			this.childMenu.setPosTop(this.element.offset().top);
			this.childMenu.show(e);
		},
		/**
		 * 隐藏菜单当前显示的所有子菜单
		 * 
		 * @private
		 */
		hideChildMenus : function(menu) {
			if (menu.nowActiveMenu != null) {
				this.hideChildMenus(menu.nowActiveMenu);
				menu.nowActiveMenu = null;
			}
			menu.hide();
		},
		/**
		 * 如果此item是menubaritem,并且此菜单项是checkgroup, 则通过此方法设置哪个子菜单item选中
		 * 
		 * @param menuItem
		 *            子菜单中要选中的item
		 */
		setSelectedItem : function(menuItem) {
			if (menuItem == null)
				return;
			if (this.options.isCheckBoxGroup) {
				// 设置选中项
				var items = this.options.parent.childMenu.childItems;
				if (items != null && items.length > 0) {
					for (var i = 0, count = items.length; i < count; i++) {
						items[i].checkbox.prop('checked',
								items[i].name == menuItem.name);
					}
				}
				// 记录选中项
				this.selectedItem = menuItem;
			}
		},
		/**
		 * 获得菜单条目宽度,包括图片宽度
		 */
		getItemWidth : function() {
			return this.element.width();
		},
		/**
		 * 设置item宽度
		 */
		setItemWidth : function(eleWidth) {
			var width = eleWidth;
			eleWidth = eleWidth + "";
			if (eleWidth.indexOf("%") != -1) {
				width = this.element.innerWidth();
			}
			this.options.width = width;
			this.element.css('width', width + "px");

			// 改变caption的宽度,auto不会自动变化改变自己宽度
			this.divCaption.css('width', width - MenuConst.IMAGE_WIDTH - 27
							+ "px");
		},
		/**
		 * 设置item子菜单
		 */
		setChildMenu : function(childMenu) {
			if ($.argumentutils.isNull(childMenu, false)) {
				throw Error("can't add an empty menu to item " + this.name);
				return;
			}

			// 如果已经添加过子菜单，则不需要改此条目。只需要设置上就行了
			if ($.argumentutils.isNotNull(this.childMenu, false)) {
				this.childMenu = childMenu;
				childMenu.parentMenu = this.options.parent;
			} else {
				this.createRightDiv();
				this.childMenu = childMenu;
				childMenu.parentMenu = this.options.parent;
				if (this.options.width != null)
					this.setItemWidth(this.options.width);
			}
		},
		/**
		 * 根据ID获取子菜单
		 */
		getMenu : function(id) {
			if (this.childMenu != null)
				return this.childMenu.getMenu(id);
		},
		/**
		 * 增加子菜单
		 */
		addMenu : function(itemName, itemCapiton, itemTip, itemRefImg,
				isCheckBoxItem, isCheckBoxSelected, attrObj) {
			if (this.childMenu == null || this.childMenu == "") {
				var cMenu = $("<div>").contextmenu({
							posFix : true
						}).contextmenu('instance');
				// 保存是谁触发的此contextMenu对象
				this.setChildMenu(cMenu);
			}
			var item = this.childMenu.addMenu(itemName, itemCapiton, itemTip,
					itemRefImg, isCheckBoxItem, isCheckBoxSelected, attrObj);
			return item;
		},
		/**
		 * 向菜单中增加分割线
		 * 
		 * @param isCheckBoxItem
		 *            是否是checkboxitem
		 * @param isCheckBoxSelected
		 *            此item初始是否选中
		 */
		addSep : function(attrObj) {
			var sep = this.childMenu.addSep(attrObj);
			return sep;
		},
		/**
		 * 获取子项容器（用户自定义）
		 * 
		 * @private
		 */
		doCreateContainer : function() {
			return this._trigger("onContainerCreate", null);
		},
		/**
		 * 创建右侧div
		 * 
		 * @private
		 */
		createRightDiv : function() {
			var rightImgDiv = $("<div>").addClass("right_btn_img");
			this.rightDiv.append(rightImgDiv);
		},
		/**
		 * 删除子菜单
		 */
		removeChildMenu : function() {
			if ($.argumentutils.isNull(this.childMenu, false)) {
				log("the item" + this.name + "'s child menu is already null");
				return;
			}

			this.childMenu = "";
			this.rightDiv.empty();
		},
		/**
		 * 设置MenuItem控件的激活状态.
		 * 
		 * @param isActive
		 *            true表示处于激活状态,否则表示禁用状态
		 * @private
		 */
		setActive : function(isActive) {
			var oThis = this;
			var isActive = $.argumentutils.getBoolean(isActive, false);
			// 控件处于激活状态变为非激活状态
			if (!this.disabled && !isActive) {
				this.divCaption
						.attr('class', 'contextmenu_item_caption_banned');
				this.element.off('mouseout mouseover click');
				this.disabled = true;
			}
			// 控件处于禁用状态变为激活状态
			else if (this.disabled && isActive) {
				this.element.off("mouseout mouseover click").on('mouseout',
						function(e) {
							oThis.element.data('mouseoutEvent')(e);
						}).on('mouseover', function(e) {
							oThis.element.data('mouseoverEvent')(e);
						}).on('click', function(e) {
							oThis.element.data('clickEvent')(e);
						});
				this.divCaption.attr('class', 'contextmenu_item_caption');
				this.disabled = false;
			}
			this.ctxChanged = true;
		},
		/**
		 * 隐藏控件(显示属性是display)
		 */
		hide : function() {
			this.element.hide();
			if (this.sep)
				this.sep.hide();
			this.visible = false;

			// 如果当前菜单项的前一项存在,并且显示,则将menubar.lastAddItem置为当前菜单项的前一项
			// 这样可以保证菜单初始化（调用MenuBarComp.prototype.addMenu）时,lastAddItem是一个显示的菜单项
			if (this.preMenuItem
					&& this.preMenuItem.element.css("display") != "none") {
				if(this.menuParent)
					this.menuParent.lastShowItem = this.preMenuItem;
			}

			// 如果一组菜单全部隐藏了，隐藏分组栏
			var allHide = true;
			var preMenuItem = this.preMenuItem;
			while (preMenuItem != null) {
				if (preMenuItem.visible) {
					allHide = false;
					break;
				}
				preMenuItem = preMenuItem.preMenuItem;
			}
			if (allHide == true) {
				var nextMenuItem = this.nextMenuItem;
				while (nextMenuItem != null) {
					if (nextMenuItem.visible) {
						allHide = false;
						break;
					}
					nextMenuItem = nextMenuItem.nextMenuItem;
				}
			}
			if (allHide == true) {
				var lastMenuItem = this.getLastMenuItem();
				if (lastMenuItem.sepDiv)
					lastMenuItem.sepDiv.hide();
			}

			this.ctxChanged = true;
			// this.parentOwner.resetMenuItemsWidth();
		},
		/**
		 * 显示控件(显示属性是display)
		 */
		show : function() {
			if (this.visible == true) return;
			
			this.element.show();
			if (this.sep)
				this.sep.show();
			this.visible = true;

			// 后面有显示出来的item时,隐藏自身右侧div
			// if (this.hideNextItemLeftDiv()){
			if (this.sep)
				this.sep.show();

			// 存在分组栏时，显示分组栏
			var lastMenuItem = this.getLastMenuItem();
			if (lastMenuItem.sepDiv)
				lastMenuItem.sepDiv.show();

			this.ctxChanged = true;
			// this.parentOwner.resetMenuItemsWidth();
		},
		getLastMenuItem : function() {
			var menuItem = this;
			while (menuItem.nextMenuItem != null) {
				menuItem = menuItem.nextMenuItem;
			}
			return menuItem;
		},
		/**
		 * 得到激活状态
		 */
		isActive : function() {
			return !this.disabled;
		},
		/**
		 * 更改显示字符串
		 */
		changeCaption : function(caption) {
			caption = $.argumentutils.getString(caption, "");
			if (this.options.caption != caption) {
				this.options.caption = caption;
			}
			this.divCaption.html(this.options.caption);
			this.ctxChanged = true;
		},
		/**
		 * 更改显示图片
		 */
		changeImg : function(refImg) {
			this.leftDiv.children().css('background','url('+refImg+')');
			this.ctxChanged = true;
		},
		/**
		 * 返回MenuItemComp的显示对象
		 */
		getObjHtml : function() {
			return this.element;
		},
		/**
		 * 响应快捷键（只响应第一个匹配子项的点击事件）
		 * 
		 * @private
		 */
		handleHotKey : function(key) {
			if (this.isActive() == false)
				return null;
			if (this.hotKey != null) {
				if (key == this.hotKey) {
					this._trigger("onclick", null);
					return this;
				}
			}
			// 匹配子项快捷键
			if (this.childMenu) {
				var childItems = this.childMenu.childItems;
				if (childItems != null && childItems.length > 0) {
					for (var i = 0, n = childItems.length; i < n; i++) {
						var obj = childItems[i].handleHotKey(key);
						if (obj != null)
							return obj;
					}
				}
			}
			return null;
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function() {
			if (this.ctxChanged == false)
				return null;
			var context = {
				c : "MenuItemContext",
				enabled : !this.disabled,
				visible : this.visible,
				refImg : this.refImg,
				text : this.options.caption,
				id : this.id
			};

			// 获取子项Context
			if (this.childMenu) {
				var childItems = this.childMenu.childItems;
				if (childItems != null && childItems.length > 0) {
					context.childItemContexts = new Array();
					for (var i = 0, n = childItems.length; i < n; i++) {
						context.childItemContexts.push(childItems[i]
								.getContext());
					}
				}
			}
			return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
			if (context.refImg != null && "" != context.refImg
					&& context.refImg != this.refImg)
				this.changeImg(context.refImg);
			if (context.text != null && "" != context.text
					&& context.text != this.options.caption)
				this.changeCaption(context.text);
			if (context.enabled != null && context.enabled == this.disabled)
				this.setActive(context.enabled);
			if (context.visible != null && context.visible != this.visible) {
				if (context.visible == false)
					this.hide();
				else
					this.show();
			}
			// 为子项设置Context
			if (context.childItemContexts) {
				for (var i = 0, n = context.childItemContexts.length; i < n; i++) {
					if(this.childMenu)
						for (var j = 0, m = this.childMenu.childItems.length; j < m; j++) {
							if (this.childMenu.childItems[j].id == context.childItemContexts[i].id) {
								this.childMenu.childItems[j]
										.setContext(context.childItemContexts[i]);
								break;
							}
						}
				}
			}
			this.ctxChanged = false;
		}
	});

	/**
	 * MenuSepComp的构造函数，分隔条
	 */
	$.widget("lui.menusep", $.lui.base, {
		options : {
			className : 'contextmenu_sep'
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
						'position' : 'relative',
						'width' : 'auto'
					});
		},

		/**
		 * 设置item宽度
		 */
		setItemWidth : function(eleWidth) {
			var width = new String(eleWidth);
			if (width.indexOf("%") != -1)
				width = this.element.outerWidth();
			this.width = width - 1;
			this.element.css('width', width - MenuConst.IMAGE_WIDTH - 5 + "px");
		},
		/**
		 * 获得分割条宽度,包括图片宽度
		 */
		getItemWidth : function() {
			return this.width;
		}
	});
	$.widget("lui.menubaritem", $.lui.menuitem, {
		options : {
			className : 'white_menu_div2',
			tip : ''
		},
		_initParam : function() {
			this._super();
		},
		_create : function() {
			this._initParam();
			var oThis = this, opts = this.options, ele = this.element;
			// item的整体div
			ele.addClass(opts.className).attr({
						'id' : "menu_div_" + this.id,
						'title' : opts.tip
					}).css({
						'overflow' : 'hidden',
						'position' : 'relative',
						'height' : opts.height + "px"
					});

			ele.data("owner", this);

			var content = this.createContent();
			if (null != content)
				ele.append(content);

			var clickHandler = function(e) {
				$(document).triggerHandler("click");
				if (oThis.childMenu) {
					oThis.childMenu.setPosLeft(ele.offset().left);
					oThis.childMenu.setPosTop(ele.offset().top
							+ ele.outerHeight());
					oThis.childMenu.show(e);
				} else {
					ele.triggerHandler("menuitemonclick");
				}
				e.stopPropagation();
			};

			ele.data('clickEvent', clickHandler);
			ele.off("click").on('click', function(e) {
						clickHandler(e);
					});

			this.ctxChanged = false;
		},
		createContent : function() {
			var oThis = this, opts = this.options, ele = this.element;

			this.leftDiv = $("<div></div>").appendTo(ele).css({
				'float' : 'left'
			});
			if (opts.refImg) {
				this.leftDiv.css({
					'width':'20px',
					'height' :  opts.height + "px"
				});
				$("<div>").css({
					'background' : 'url("' + opts.refImg + '")',
					'width' : '16px',
					'height' : '16px',
					'margin-top' : ((opts.height -14) / 2) +"px"
				}).appendTo(this.leftDiv)
			}else{//若无图片则改变宽度
				this.capDivWidth = (20 + 16) + "px";
			}

			// 显示文字的Div
			this.divCaption = $("<div></div>").appendTo(ele).css({
						'float' : 'left'
					}).html(opts.caption);
			if(this.capDivWidth){
				this.divCaption.css({'width':this.capDivWidth, 'text-align':'center'});
				
			}

			var mouseoverHandler = function(e) {
				e.targetItem = oThis;
				if(!opts.refImg)
					ele.addClass("white_menu_div2_on2");
				else
					ele.addClass("white_menu_div2_on");
				if (opts.refImg) {
					oThis.leftDiv.children().css('background','url("' + $.argumentutils.addImgSuffix(opts.refImg,"_on") + '")')
				}
				oThis._trigger("mouseover", e);
//				ele.triggerHandler("click");
				e.stopPropagation();//防止冒泡
			};

			var mouseoutHandler = function(e) {
				if(!opts.refImg)
					ele.removeClass("white_menu_div2_on2");
				else
					ele.removeClass("white_menu_div2_on");
				if (opts.refImg) {
					oThis.leftDiv.children().css('background','url("' + opts.refImg + '")')
				}
				e.stopPropagation();
			};

			ele.data('mouseoverEvent', mouseoverHandler).data(
					'mouseoutEvent', mouseoutHandler);
			// 鼠标移动到item上
			ele.off("mouseover mouseout").on('mouseover', function(e) {
						mouseoverHandler(e);
					}).on('mouseout', function(e) {
						mouseoutHandler(e);
					});
		},
		addMenu : function(itemName, itemCapiton, itemTip, itemRefImg,
				isCheckBoxItem, isCheckBoxSelected, attrObj) {
			if (this.childMenu == null || this.childMenu == "") {
				this.childMenu = $("<div>").contextmenu({
							posFix : true
						}).contextmenu('instance');
			}
			var item = this.childMenu.addMenu(itemName, itemCapiton,
					itemTip, itemRefImg, isCheckBoxItem,
					isCheckBoxSelected, attrObj);
			return item;
		}
	});

})(jQuery);