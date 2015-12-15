/**
 * 
 * 多选框控件
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * CheckBox控件的构造函数
	 * @class 复选框控件
	 * @param left 控件左坐标
	 * @param top 控件顶部坐标
	 * @param text 控件显示的名字
	 * @param checked 是否被选中
	 * @param position 定位属性
	 */
	$.widget("lui.checkbox", $.lui.textfield, {
		options : {
			dataType : 'C',
			text : '',
			checked : false,
			imgsrc : '',
			position : 'relative',
			className : 'checkbox_div',
			//event
			onenter : null,
			onclick : null,
			valuechanged : null
		},
		_initParam : function() {
			this.componentType = "CHECKBOX";
			this.parentOwner = this.element.parent()[0];
			this.IMAGE_SIDE_LENGTH = 15;
			this.inactivate = "inactivate";
			this.readonly = "readonly";
			this.valuePair = [ "true", "false" ];
			this.checkboxImgSrc = $.argumentutils.getString(this.options.imgsrc, '');
			this._super();
		},
		_create : function() {
			this._initParam();
			
			var oThis = this,opts = this.options,ele = this.element;
			// 设置tabindex属性，以便在Firefox下获取聚焦事件
			ele.prop('tabindex',this.tabIndex == null ? 0 : this.tabIndex)
			 .css('position', opts.position);
			
			this.input = $("<input type=\"checkbox\"/>").prop({
				'name' : ele.prop("id"),
				'checked' : opts.checked
			}).addClass('checkbox_box').appendTo(ele);

			if (opts.disabled) {
				this.input.prop('disabled',true);
				ele.prop('class',opts.className + " " + this.inactivate);
			}

			this.input.val(this.input.is(':checked'));
			
			ele.on('blur',function(e){
				$(this).css('border','0px');
				oThis._trigger("onblur",e);
			}).on('keypress',function(e){
				if (opts.disabled != true) {
					// 获取输入字符
					var keyCode = e.keyCode;
					// 回车键
					if (keyCode == 13) {
						oThis._trigger('onenter',e);
						return true;
					}
				}
			   	return true;
			}).on('keydown',function(e){
				if (opts.disabled != true) {
					// 获取输入字符
					var keyCode = e.keyCode;
					// 空格键
					if (keyCode == 32) {
						oThis.input.triggerHandler('click');
						ele.focus();
						e.stopPropagation();
					}
				}
			});
			
			this.input.off('click').on('click',function(e){
				ele.focus();
				
				// 获取新值
				var newValue = oThis.input.is(':checked');
				opts.checked = oThis.input.is(':checked');
				oThis.ctxChanged = true;
				oThis._trigger('valuechanged',e,{oldValue:newValue,newValue:!newValue});
				oThis._trigger('onclick',e);
			});

			// 显示文字的div
			this.textLabel = $("<div></div>").css({
				'left' : '20px',
				'position' : 'relative'
			}).html(opts.text).appendTo(ele);
			this.textLabel.get(0).htmlFor = this.input.prop('id');
		//	if($.browsersupport.IS_IE7){//解决IE7,checkbox不能完全显示
	//			this.textLabel.css('margin-top','5px');
		//	}
		//	ele.css('margin-top','4px');
			var realWidth = $.measures.getTextWidth(opts.text) + 25;
			
			if(this.checkboxImgSrc){
				var imgDiv = $("<div></div>").css({
					'margin-right' : '1px',
					'width' : this.IMAGE_SIDE_LENGTH + "px",
					'height' : this.IMAGE_SIDE_LENGTH + "px",
					'float' : 'left'
				});
				var img = $("<img/>").prop({
					'src' : window.themePath + this.checkboxImgSrc
				}).appendTo(imgDiv);
				
				//var div = $("<div></div>").append(imgDiv);
				//this.textLabel.innerHTML = div.innerHTML + this.textLabel.innerHTML;
				this.textLabel.prepend(imgDiv);
				realWidth += this.IMAGE_SIDE_LENGTH;
			}
			
			if(!ele.parent().is("body")){
				//ele.parent().css('width',realWidth + "px");
			}
			
		//	if (!$.browsersupport.IS_IE) {  // firefox下的label点击绑定特别处理
				this.textLabel.on('click',function(e){
					oThis.input.triggerHandler('click');
				});
		//	}
		},
		setLabelText : function(textLabel) {
			this.options.text = textLabel;
			this.textLabel.html(textLabel);
		},
		/**
		 * 设置此checkbox状态
		 * @param{boolean} checked
		 */
		setChecked : function(checked) {
			if (checked == null || checked == "")
				this.options.checked = false;
			else
				this.options.checked = checked;
			this.input.prop('checked',this.options.checked);
			this.input.val(this.options.checked);
			this.ctxChanged = true;
		},
		/**
		 * 得到指定按钮状态
		 */
		getChecked : function() {
			return this.input.is(':checked');
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			if (this.options.readOnly==readOnly)
				return;
			if (this.options.disabled){
				this.setActive(true);
			}
			// 控件处于只读状态变为非只读状态
			if (this.options.readOnly && !readOnly) {
				this.input.prop('disabled',false);
				this.options.readOnly = false;
				this.element.prop('class',this.options.className);
			}
			// 控件处于非只读状态变为只读状态
			else if (!this.options.readOnly && readOnly) {
				this.input.prop('disabled',true);
				this.options.readOnly = true;
				this.element.prop('class',this.options.className + " " + this.readOnly);
			}
		},
		/**
		 * 设置值对
		 * @param{Array} arr
		 */
		setValuePair : function(arr) {
			this.valuePair = arr;
		},
		/**
		 * 获取当前值
		 */
		getValue : function() {
			return this.getChecked() ? this.valuePair[0] : this.valuePair[1];
		},
		/**
		 * 设值
		 */
		setValue : function(value) {
			this.setChecked(value == this.valuePair[0]);
		},
		/**
		 * 设置此checkbox控件的激活状态
		 * 
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			var isActive = $.argumentutils.getBoolean(isActive, false);
			// 控件处于激活状态变为非激活状态
			if (!this.options.disabled && !isActive) {
				this.input.prop('disabled',true);
				this.options.disabled = true;
				this.element.prop('class',this.options.className + " " + this.inactivate);
			}
			// 控件处于禁用状态变为激活状态
			else if (this.options.disabled && isActive) {
				this.input.prop('disabled',false);
				this.options.disabled = false;
				this.element.prop('class',this.options.className);
			}
		},
		/**
		 * 设置聚焦
		 */
		setDivFocus : function(isFocus) {
			if (!isFocus)
				this.element.blur();
			else {
				this.element.focus();
			}
			return true;
		},
		/**
		 * 获取显示值
		 */
		getText : function() {
			return this.options.text;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			var context = this._super();
			if (context == null)
				return null;
			context.c = "CheckBoxContext";
			context.checked = this.input.is(':checked');
			return context;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			this._super(context);
			if (context.checked != null)
				this.setChecked(context.checked);
			this.ctxChanged = false;
		}

	});
})(jQuery);
