/**
 *	
 * Password类型的Text输入控件  
 * 
 * @author lxl
 * @version lui 1.0
 */
(function($) {
	/**
	 * 密码控件构造函数
	 * @class 密码控件 
	 */
	$.widget("lui.pswtext" , $.lui.textfield , {
		options : {
			dataType : 'P'
		},
		_create : function() {
			this.componentType = "PSWTEXT";
			this.parentOwner = this.element.parent()[0];
			var oThis = this;
			this._super();
			this.input.off("mouseover").on('mouseover',function(e){
		    	oThis._trigger('onmouseover',e);
		    	oThis.input.attr('title','');
			});
		},
		/**
		 * 创建默认格式化器
		 * @private
		 */
		createDefaultFormater : function() {	
			return $.stringformater.getObj(this.options.maxSize);
		},
		/**
		 * 设值，设置值时要检测类型
		 */
		setValue : function(text) {
			this._super(text);
		}
	});
	
})(jQuery);
