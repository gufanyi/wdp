/**
 * @iframe iframe控件
 * 
 * @auther liy
 * @version lui 1.0
 *  
 */
(function($) {
	$.widget("lui.iframe", $.lui.base, {
		options: {
            componentType : "IFRAME",
			id :  '',
			name :  '',
			src :  '',
			border :  '',
			frameBorder :  '',
			scrolling :  '',
			visible :  true,
			width : '',
		    height : '',
			//event
			onclick : null,
			onmouseout : null,
			onmouseover : null,
			onmousedown : null,
			onmouseup : null
		},
		_initParam : function() {
			this.id = this.options.id;
			this._super();
		},
		_create : function() {
			this._initParam();
			// 快捷键
			var oThis = this;
			this.element.attr({
				'id' : this.options.id + "_div"
			}).css({
				'width' : '100%',
				'height' : '100%',
				'border' : '0px'
			});
			
			this.frame = $("<iframe></iframe>").appendTo(this.element).attr({
				'id' : this.options.id,
				'name' : this.options.name,
				'src' : this.options.src,
				'border' : this.options.border ? this.options.border : "0px",
				'frameBorder' : this.options.frameBorder ? this.options.frameBorder : "0",
				'scrolling' : this.options.scrolling ? this.options.scrolling : "auto"
			}).css({
				'width' : this.options.width ? this.options.width : "100%",
				'height' : this.options.height ? this.options.height : "100%"
			});
		},
		/**
		 * 销毁
		 */
		destroySelf : function() {
			if(this.element) {
				this.element.empty();
			}
			//this.destroy();
		},
		/**
		 * 设值内容地址
		 */
		setSrc : function(src) {
			//if (this.options.src != src) {
			
				this.options.src = src;
				this.frame.attr('src',this.options.src);
			//}
		},
		/**
		 * 设值可见性
		 */
		setVisible : function(visible) {
		    if (this.options.visible != visible) {
					this.options.visible = visible;
					this.element.css('visibility',(visible ? 'visible' : 'hidden'));
			}
		},
		/**
		 * @private
		 */
        getContext : function() {
			var context = new Object;
			context.c = "IFrameContext";
			context.id = this.options.id;
			context.src = this.options.src;
			context.visible = this.options.visible;
			return context;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
         setContext : function(context) {
			if (context.src != null)
				this.setSrc(context.src);
			if (context.visible != null)
				this.setVisible(context.visible);
		}
	});
})(jQuery);
