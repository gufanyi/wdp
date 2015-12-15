var BASE = {
	SCROLLWIDTH : 18,
	ELEMENT_ERROR : "error",
	ELEMENT_WARNING : "warning",
	ELEMENT_NORMAL : "normal",
	ELEMENT_SUCCESS : "success"
};
(function($) {
	$.widget("lui.base", {
		options: {
			left : 0,
			top : 0,
			width : '100%',
			height : '100%'
		},
		_initParam : function() {
			this.hotKey = null;
			this.visible = true;
			this.ctxChanged = false;
			this.contextMenu = null;
			if(this.id && this.id!=""){
				 window.objects[this.id] = this;
			}else{
			     window.objects[this.element.attr('id')] = this;
			}
			
			this.allChildObjects = [];
			this.element[0].owner = this;
		},
		_create : function() {
			this._initParam();
		},
		add : function(ObjHtml) {
			if (ObjHtml.owner) {
				// 添加到子控件数组中
				this.allChildObjects.push(ObjHtml.owner);
				ObjHtml.owner.parentOwner = this;
			}
			this.element.append($(ObjHtml));
		},
		/** 
		 * 设置控件位置
		 * @param left 控件左侧X坐标
		 * @param top  控件顶部Y坐标
		 */
		setPosition : function(left, top) {	
			// 改变数据对象的值
			this.options.left = $.argumentutils.getInteger(left, 0);
			this.options.top = $.argumentutils.getInteger(top, 0);
			// 改变显示对象的值
			this.element.css({
				'left' : this.left + "px",
				'top' : this.top + "px"
			});
		},
		/**
		 * 设置控件大小
		 * @param width  控件的宽度
		 * @param height 控件的高度
		 */
		setSize : function(width, height) {
			this.options.width = $.argumentutils.getString(width, "100%");
			this.options.height = $.argumentutils.getString(height, "100%");
			if(width != -1)
				this.element.css('width',this.width);
			if(height != -1)
				this.element.css('height', this.height);
		},
		/**
		 * 设置控件边界值.子控件可根据实际情况覆盖此函数
		 * @param left   控件左侧X坐标
		 * @param top    控件顶部Y坐标
		 * @param width  控件的宽度
		 * @param height 控件的高度
		 */
		setBounds : function(left, top, width, height) {	
			// 改变数据对象的值
			this.options.left = $.argumentutils.getInteger(left, 0);
			this.options.top = $.argumentutils.getInteger(top, 0);
			this.options.width = $.argumentutils.getString($.measures.convertWidth(width), "100%");
			this.options.height = $.argumentutils.getString($.measures.convertHeight(height), "100%");
			// 改变显示对象的值
			this.element.css({
				'left' : this.left + "px",
				'top' : this.top + "px",
				'width' : this.width,
				'height' : this.height
			});
		},
		/**
		 * 得到组件的width属性
		 * @return 控件的宽度
		 */
		getWidth : function() {
			return this.options.width;
		},
		/**
		 * 得到组件的height属性
		 * @return 控件的高度
		 */
		getHeight : function() {
		 	return this.options.height;
		},
		convertSize : function(size) {
			if(size) {
				size += "";
				if(size.indexOf("%")>=0) {
					return size;
				}
				if(size.indexOf("px")>=0) {
					return size;
				}
				return size + "px";
			}
			
		},
		/**
		 * 设置控件立体高度
		 * @param zIndex 控件的第三维高度
		 */
		setZIndex : function(zIndex) {
			this.element.css('z-index',zIndex);
		},
		/** 
		 * 隐藏控件(显示属性是display)
		 */
		hide : function() {                
		    this.element.hide();
		    this.visible = false;
		    this.ctxChanged = true;
		},
		/** 
		 * 显示控件(显示属性是display)
		 */
		show : function() {                    
			this.element.show();
		    this.visible = true;
		    this.ctxChanged = true;
		},
		/**
		 * 隐藏控件(显示属性是visibility)
		 */
		hideV : function() {
			this.element.css('visibility','hidden');
		    this.visible = false;
		    this.ctxChanged = true;
		},
		/**
		 * 显示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
		    this.visible = true;
		    this.ctxChanged = true;
		},
		/**
		 * 设置右键菜单
		 */
		setContextMenu : function(menu){
			this.contextMenu = menu;
		},
		/**
		 * 获取右键菜单
		 */
		getContextMenu : function() {
			return this.contextMenu;
		},
		/**
		 * 显示右键菜单
		 */
		showMenu : function(e) {
			if (this.contextMenu != null)
				this.contextMenu.show(e);
		},
		/**
		 * 响应快捷键
		 */
		handleHotKey : function(key) {
			if (this.hotKey != null && key == this.hotKey) {
				this._trigger('onclick');
			}
		},
		/**
		 * 设置快捷键
		 */
		setHotKey : function(hotKey) {
			this.hotKey = hotKey;
		},
		/**
		 * 获取快捷键
		 */
		getHotKey : function() {
			return this.hotKey;
		},
		/**
		 * 获取对象信息
		 */
		getContext : function() {
			return {
				c : ''
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {},
		_destroy : function() {
			this.element.empty();
		},
		destroySelf : function() {
			this._destroy();
		}

	});
})(jQuery);

