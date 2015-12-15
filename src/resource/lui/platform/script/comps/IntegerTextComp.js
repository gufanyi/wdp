/**
 * @fileoverview   
 *	
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 整形输入框
	 * @class Integer类型的Text输入控件
	 * 	
	 * @constructor integerText构造函数
	 * @author gd
	 * @version 1.2
	 */
	$.widget("lui.integertext" , $.lui.textfield , {
		options : {
			dataType : "I",
			maxValue : 999999999999999,
			minValue : -999999999999999,
			tip : null
		},
		_initParam : function() {
			this.componentType = "INTEGERTEXT";
			this.parentOwner = this.element.parent()[0];
			var opts = this.options;
			if (opts.tip == null || opts.tip == "") {
				if (opts.minValue != null && opts.maxValue != null)
					opts.tip = opts.minValue + "～" + opts.maxValue;
				else if (opts.minValue != null)
					opts.tip = ">=" + opts.minValue;
				else if (opts.maxValue != null)
					opts.tip = "<=" + opts.maxValue;
			}
			this._super();
		},
		_create : function() {
			this._super();
		},
		getValue : function() {
			return this._super();
		},
		getFormater : function() {
			return this._super();
		},
		setFocus : function() {
			this._super();
		},
		setMessage : function(inputValue) {
			this._super(inputValue);
		},
		/**
		 * 处理回车事件
		 * @private
		 */
		processEnter : function() {
			 var inputValue = this.getValue().trim();
			 if (inputValue != "") {	 
				 inputValue = this.getFormater().format(inputValue);
				 if (inputValue == "") {
//					$.formater. showVerifyMessage(this, trans("ml_integermustbetween") + this.minValue + trans("ml_and") + this.maxValue + trans("ml_between"));
					 $.formater.showVerifyMessage(this.element, "只能输入整形数字");
					 this.input.val(''); 
					 this.setFocus();
				 } else {
					this.setMessage(inputValue);
					this.input.val(inputValue);
				 }
			 }	 
		},
		/**
		 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
		 * @private
		 */
		createDefaultFormater : function() {
			return $.integerformater.getObj(this.options.minValue, this.options.maxValue);
		},
		/**
		 * 失去焦点时进行检测
		 * @private
		 */
		verify : function(oldValue) {
			if (this.newValue == "" && this.input.value != "") {
				$.formater.showVerifyMessage(this.element, trans("ml_integermustbetween") + this.options.minValue + trans("ml_and") + this.options.maxValue + trans("ml_between"));
				this.input.val('');
				this.setMessage("");
			} 
		},
		/**
			得到context信息
		**/
		getContext : function() {
			var context = this._super();
			if (context == null) return null;
	//		var context = new Object;
			context.id = this.element.attr("id");
			context.maxValue = this.options.maxValue;
			context.minValue = this.options.minValue;
			return context;
		},
		/**
			设置context信息
		**/
		setContext : function(context) {
			this._super(context);
			if (context.maxValue != null){
				this.setIntegerMaxValue(context.maxValue);
			}
			if (context.minValue != null){
				this.setIntegerMinValue(context.minValue);
			}
		},
		/**
		 * 设置最大值
		 */
		setIntegerMaxValue : function(maxValue) {
			if (maxValue != null) {	
				// 判断maxValue是否是数字
				if ($.argumentutils.isNumber(maxValue)) {	
					if (parseInt(maxValue) >= -999999999999999 && parseInt(maxValue) <= 999999999999999) {
						this.options.maxValue = maxValue;
					}
				}	
			}
		},
		/**
		 * 设置最小值
		 */
		setIntegerMinValue : function(minValue) {
			if (minValue != null) {	
				// 判断minValue是否是数字
				if ($.argumentutils.isNumber(minValue)) {	
					if ((parseInt(minValue) >= -999999999999999) && (parseInt(minValue) <= 999999999999999)) {  
						this.options.minValue = minValue;
					}
				}
			}
		},
		/**
		 * 设置值
		 */
		setValue : function(text) {
			if (!$.formater.checkIntegerIsValid(text, null, null)) {
				text = "";
				this.input.val('');
				this.setMessage("");
			}
			this._super(text + "");
		}

	});
})(jQuery);
