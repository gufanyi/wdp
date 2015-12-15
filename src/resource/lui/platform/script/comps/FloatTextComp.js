/**
 * @fileoverview float类型的Text输入控件.  
 *	
 * @author lxl
 * @version lui 1.0
 *	
 */
(function($) {
	/**
	 * 浮点型输入框构造函数
	 * @class 浮点型输入框
	 * @constructor floatText构造函数
	 */
	$.widget("lui.floattext" , $.lui.textfield , {
		options : {
			dataType : 'N',
			precision : '2',
			maxValue : 10000000000000000,
			minValue : -10000000000000000,
			tip : null
		},
		_initParam : function() {
			this.componentType = "FLOATTEXT";
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
		_managerSelf : function() {
			this._super();
			var oThis = this,opts = this.options; 
			this.input.off('blur').on('blur',function(e) {
				if(opts.readOnly){
			      return ;
			    }
				if(oThis.Div_text.children().length == 3){
					var children = oThis.Div_text.children();
					$.each(children,function(index,item){
						$(item).attr('class',$(item).attr('class').replaceStr('input_highlight','input_normal'));
					});
					oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
				}
				oThis.blur(e);
				if(!(typeof(oThis.input.val()) == "string" && oThis.input.val().indexOf(",") != -1)){
					oThis.setValue(oThis.input.val());
				}
				oThis.showTip();
			}).off('focus').on('focus',function(e){
				//保证光标在最后
				var length = oThis.input.val().length;
				if(oThis.input.get(0).createTextRange){//IE
					var r = oThis.input.get(0).createTextRange();
					r.collapse(true);
					r.moveStart('character',length);
					r.select();
				}else if(oThis.input.get(0).setSelectionRange){//Firefox
					oThis.input.get(0).setSelectionRange(length,length);
				}
				oThis.focus(e);
			});
		},
		showTip : function() {
			this._super();
		},
		getValue : function() {
			return this._super();
		},
		getFormater : function() {
			return this._super();
		},
		setMessage : function(val) {
			this._super(val);
		},
		/**
		 * 处理回车事件
		 * @private
		 */
		processEnter : function() {
			 var inputValue = this.getValue().trim();
			 if (inputValue != "") {
				 inputValue = this.getFormater().format(inputValue, this.options.minValue, this.options.maxValue);
				 if (inputValue == "") {
					 this.input.val('');
					 this.setMessage("");
					 this.setFocus();
				 } else {
					 this.setCorrectValue(inputValue);
				 }
			 }	 
		},
		/**
		 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
		 * @private
		 */
		createDefaultFormater : function() {
			return $.dicimalformater.getObj(this.options.precision, this.options.minValue, this.options.maxValue);
		},
		/**
		 * 失去焦点时进行检测
		 * @private
		 */
		blur : function() {	
			if(this.visible == false) return;
			var value = this.input.val().trim();
			this.newValue = value;
			if (this.options.dataType == 'N' && value == "") {
				this.setCorrectValue("");
			}	
			if (this.options.dataType == 'N' && value != "") {
				if(this.oldValue != this.newValue)
					value = this.getFormater().format(value, this.options.minValue, this.options.maxValue);
				// 检测输入的是否是浮点数,若不是则设置焦点要求用户重新输入
				if (value == "") {
					$.formater.showVerifyMessage(this.element, trans("ml_decimalmustbetween") + this.options.minValue + trans("ml_and") + this.options.maxValue + trans("ml_between"));
					this.input.val('');
					this.setMessage("");
					this.setFocus();
				} 
			}
			this._trigger("onblur",null);
		},
		hideTip : function() {
			this._super();
		},
		focus : function (e) {
			if(this.options.readOnly){
			   return ;
			}
			this.warnIcon.hide();
			if(this.isError && typeof(this.errorMessage) == 'string' && this.errorMessage != ''){
				this.errorCenterDiv.html(this.errorMessage);
				this.errorMsgDiv.show();
			}
			if(this.Div_text.children().length == 3){
				var children = this.Div_text.children();
				$.each(children,function(index,item){
					$(item).attr('class',$(item).attr('class').replaceStr('input_normal','input_highlight'));
				});
				this.input.removeClass('input_normal_center_bg').addClass('input_highlight_center_bg');
			}
			this.input.css('color','black').val(this.newValue);
			this.oldValue = this.newValue;
			if (this.visible == true) {
				this._trigger("onfocus",e);
			}
			this.hideTip();	
		},
		/**
		 * 获得输入焦点
		 */
		setFocus : function() {
			if(this.options.readOnly){
			   return ;
			}
			var oThis = this;
			if (this.visible == true){
				if(this.options.disabled){
					this.mayFocus=true;
				}else{
//					this.focus();
//					if ($.browsersupport.IS_IE) {
//						this.input.focus();
//						this.input.select();
//					} else {  // firefox等浏览器不能及时执行focus方法
						window.setTimeout(function(){oThis.input.focus();oThis.input.select();}, 50); 
//					}
				}
			}
			this.ctxChanged = true;
		},
		/**
		 * 设置精度
		 */
		setPrecision : function(precision,fromDs) {
			fromDs = (fromDs == null) ? false : fromDs;
			if (fromDs == true){
				this.precisionFromDs = true;
			}
			//以ds设置的精度为准
			if (this.precisionFromDs != null && this.precisionFromDs == true && fromDs == false)
				return;
			this.options.precision = parseInt(precision);
			this.getFormater().precision = this.options.precision;
			var text = this.getValue();
			if(text != ""){
				text = this.getFormater().format(text);
				this.setValue(text);
			}
		},
		checkTip : function() {
			this._super();
		},
		/**
		 * 设置值
		 */
		setValue : function(text) {
			var textValue = parseFloat(text);
			if (isNaN(textValue)){
				textValue = "";
				text = "";
			}
//			if(window.pageUI.isReturn){
//				this.newValue = text;
//			}else{
				if(this.oldValue != this.newValue)
			    	text = this.getFormater().format(text);
				this.newValue = text;		
//			}	
			
//			var masker = $.pageutils.getMasker("FLOATTEXT");
//			if(masker != null) {
//				this.showValue = masker.format(this.newValue).value;
//				if(masker.format(this.newValue).color != null)
//					this.input.css('color','red');
//				else
//					this.input.css('color','black');  
//			}
//			if (this.showValue == "") {
//				this.input.val('');
//				this.setMessage("");
//			}else{
//				this.input.val(this.showValue); 
//				this.setMessage(this.showValue);
//			}
			
			if (this.checkTip()) {
				if (this.input.val() == "")
					this.showTip();
				else
					this.input.css('color','black');
			}
			
			if(!this.options.disabled){
				if(this.newValue != this.oldValue){
					this._trigger("valueChanged",null,{oldValue:this.oldValue,newValue:this.newValue});
				}else if(typeof(this.oldValue) == "string" && this.oldValue.trim() == "" && this.newValue == 0){
					this._trigger("valueChanged",null,{oldValue:this.oldValue,newValue:this.newValue});
				}else if(this.oldValue == 0 && typeof(this.newValue) == "string" && this.newValue.trim() == ""){
					this._trigger("valueChanged",null,{oldValue:this.oldValue,newValue:this.newValue});
				}
			}
			this.oldValue = this.newValue;		
			this.ctxChanged = true;
		},
		setTitle : function(title) {
			this.input.attr('title',title);
			this.element.attr('title',title);
		},
		/**
		 * 这个方法和上面的方法的唯一区别是这个方法设置的值一定保证正确
		 * @private 私有方法 
		 */
		setCorrectValue : function(text) {
			this.setMessage(text);
			this.input.val(text);
		},
		/**
			得到context信息
		**/
		getContext : function() {
			var context = this._super();
			if(context == null)
				return null;
	//		var context = new Object;
			context.c = "FloatTextContext";
			context.id = this.element.attr("id");
			context.precision = this.options.precision;
			return context;
		},
		/**
			设置context信息
		**/
		setContext : function(context) {
			this._super(context);
			if (context.precision != null){
				this.setPrecision(context.precision);
			}
		},
		/**
		 * 设置最大值
		 */
		setMaxValue : function(maxValue) {
			if (!isNaN(parseFloat(maxValue))) {
				this.options.maxValue = parseFloat(maxValue);
			}
			else
				this.options.maxValue = null;
		},
		/**
		 * 设置最小值
		 */
		setMinValue : function(minValue) {
			if (!isNaN(parseFloat(minValue))) {
				this.options.minValue = parseFloat(minValue);
			} 
			else
				this.options.minValue = null;
		}
	});
	
})(jQuery);
