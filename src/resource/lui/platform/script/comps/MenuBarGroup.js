(function($) {
	$.widget("lui.menubargroup", {
		options : {
			id : ''
		},
		/**
		 * MenuBarGroup构造函数
		 * @class 菜单组控件
		 */
		_initParam : function() {
			this.componentType = "MENUBARGROUP";
			this.id = this.options.id;
			this.menubars = $.hashmap.getObj();
		},
		_create : function() {
			this._initParam();
		},
		/**
		 * 添加子项
		 * @param state 状态
		 * @param menubar 该状态下显示的menubar
		 */
		addItem : function(state, menubar) {
			menubar.element.parent().css('position','absolute');
			menubar.hideV();
//			if (!$.browsersupport.IS_IE6)
				menubar.element.parent().css('visibility','hidden');
			this.menubars.put(state, menubar);
		},
		/**
		 * 移除某状态对应的menubar
		 */
		removeItem : function(state) {
			this.menubars.remove(state);
		},
		/**
		 * 设置菜单组的当前状态
		 */
		setState : function(state) {
			var bar = this.menubars.get(state);
			if (this.nowMenubar != null) {
				this.nowMenubar.hideV();
//				if (!$.browsersupport.IS_IE6)
					this.nowMenubar.element.parent().css('visibility','hidden');
			}

			if (bar != null) {
//				if (!$.browsersupport.IS_IE6)
					bar.element.parent().css('visibility','');
				bar.showV();
				this.nowMenubar = bar;
			}
		}
	});
})(jQuery);
