(function($) {
	
	$.widget("lui.htmlcontent", $.lui.base, {
		options : {
			position : 'relative',
			className : ''
		},
		_initParam : function() {
			this.componentType = "HTMLCONTENT";
			this._super();
		},
		_create : function() {
			this._initParam();
		},
		/**
		 * 设置内容
		 */
		setContent : function(html) {
//			if($.browsersupport.IS_IE){
//				this.element.html("<span style='display: none'>uapweb</span>" + html);
//			}else{
				this.element.html(decodeURIComponent(html));
//			}
		},
		/**
		 * 获得内容
		 * @return {}
		 */
		getContent : function() {
			return this.element.html();
		},
		/**
		 * 清空内容
		 * @return {}
		 */
		clearContent : function() {
			return this.element.empty();
		},
		addContent : function(obj) {
			this.element.append(obj);
		},
		removeContent : function(obj) {
			$(obj).remove();
		},
		
		getContext : function() {
			return {
				c : "PartCompContext",
				id : this.element.attr("id")
			};
		},
		/**
		 * @private
		 */
		setContext : function(context) {
			if(context.innerHTML){
				this.setContent(context.innerHTML);
			}
		}
	});
})(jQuery);
