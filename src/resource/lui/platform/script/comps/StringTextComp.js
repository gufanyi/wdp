/**
 *	
 * String类型的Text输入控件.
 *	
 * @author lxl
 * @version lui1.0
 *	
 */

(function($) {
	/**
	 * 字符串输入框构造函数
	 * @class 字符串输入框
	 * @param maxSize 此变量指定的是字节数,中文每个汉字的字节数是2,英文每个字母的字节数是1
	 */
	$.widget("lui.stringtext", $.lui.textfield, {
		options : {
			dataType : 'A'
		},
		_create : function() {
			this.componentType = "STRINGTEXT";
			this.parentOwner = this.element.parent()[0];
			this._super();
		},
		getValue : function() {
			return this._super();
		},
		verify : function(oldValue) {
			var value = this.getValue();
			if(value != null)
				value = value.trim();
			var beforeFormatL = oldValue.length;
			var afterFormatL = this.newValue.length;
			if (beforeFormatL > afterFormatL)
				$.formater.showVerifyMessage(this.element, trans("ml_thevaluebeyondthemaxlength")); 
		},
		/**
		* 创建默认格式化器
		* @private
		*/
		createDefaultFormater : function() {	
			return $.stringformater.getObj(this.options.maxSize);
		},
		getFormater : function() {
			return this._super();
		},
		setMessage : function(value) {
			this._super(value);
		},
		/**
		 * 处理回车事件
		 */
		processEnter : function() {
			 var inputValue = this.getValue();
			 if (inputValue == null)
			 	inputValue = "";
			 else	
			 	inputValue = inputValue.trim();
			 var beforeFormatL = inputValue.length;
			 value = this.getFormater().format(inputValue);
			 var afterFormatL = value.length;
			 if (beforeFormatL > afterFormatL)
				 $.formater.showVerifyMessage(this.element, trans("ml_thevaluebeyondthemaxlength"));
			 this.setMessage(value);
			 this.input.val(value);
		},
		/**
		 * 设置最大值
		 */
		setMaxSize : function(size) {
			this.options.maxSize = parseInt(size);
		}
	});
})(jQuery);
