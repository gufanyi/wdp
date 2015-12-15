/**
 * @fileoverview Radio控件.  
 * 图形化的单选按钮.Radio按钮元素通常在一个选项组中使用,
 * 其中的选项互斥,具有相同的名字.	
 *
 * @author  lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 单选控件构造函数
	 * @class 单选控件
	 * @param value 一个可读可写的字符串,声明了一段文本,如果在提交表单时该单选按钮处于被选中的状态,那么这段文本就会被传递给服务器.此属性
	 *				不是声明当前该单选按钮是否被选中了.
	 * @param group group相同的为一组,此组中只能选中一个radio
	 */
	$.widget("lui.radio", $.lui.textfield, {
		options : {
			dataType : 'RA',
			group : null,
			value : '',
			text : '',
			checked : false,
			imgsrc : '',
			//event
			onclick : null
		},
		_initParam : function() {
			this.componentType = "RADIO";
			this.parentOwner = this.element.parent()[0];
			this.IMAGE_SIDE_LENGTH = 15;
			this.tabIndex = -1;
			this.radioImgSrc = $.argumentutils.getString(this.options.imgsrc, this.radioImgSrc);
			this._super();
		},
		_create : function() {
			this._initParam();
			var oThis=this,opts=this.options,ele=this.element;	
			var htmlContent = '<input style="float:left;" type="radio" id="' + ele.prop("id") + '_radio" name="' + opts.group + '" value="'+ opts.value +'" />';
			ele.css({
				'position' : opts.position,
				'left' : opts.left + "px",
				'top' : opts.top + "px"
			}).html(htmlContent);

			this.input = ele.children().first().prop({
				'checked' : opts.checked
			});
			
			this.label = $("<div id='"+ele.prop("id")+"_label'></div>").label({
				left : 20,
				top : 2,
				text : opts.text,
				position : 'absolute',
				className : 'label_normal'
			}).appendTo(ele);
			
			if(this.radioImgSrc){
				var radioImgDiv = $("<div></div>").css({
					'margin-right' : '1px',
					'width' : this.IMAGE_SIDE_LENGTH + "px",
					'height' : this.IMAGE_SIDE_LENGTH + "px",
					'float' : 'left'
				});
				var radioImg = $("<img/>").prop({
					'src' : window.themePath + this.radioImgSrc
				}).appendTo(radioImgDiv);
				
				ele.prepend(radioImgDiv);	
			}

			ele.on('click',function(e){
				if (!window.editMode) {
					$("#"+ele.prop("id")+"_radio").trigger("click");
				}
//				e.stopPropagation();
			});
			
			if (this.tabIndex != -1)
				this.input.prop('tabindex',this.tabIndex);
			 
			this.input.off('click').on('click',function(e){
//				oThis.input.prop('checked',!opts.checked);
//				opts.checked = !opts.checked;
				oThis._trigger("onclick",e);
				e.stopPropagation();
//				if (!oThis.click(oThis.input.prop('checked')))
//					return false;
//				else	
//					return true;
			});
		},
		/**
		 * 返回value属性的值
		 */
		getValue : function() {
			return this.options.value;
		},
		/**
		 * 返回显示内容
		 */
		getText : function() {
			return this.options.text;
		},
		/**
		 * 获取选中状态
		 */
		getChecked : function() {
			return this.input.prop('checked');
		},
		/**
		 * 设置选中状态
		 */
		setChecked : function(checked) {
//			this.input.trigger("click");
			if (checked == null){
				this.input.prop({
					'checked' : false,
				});
			}
			else{
				this.input.prop({
					'checked' : checked,
				});
			}
		},
		/**
		 * 单击执行方法
		 * @private
		 */
		click : function(isChecked) {
			return true;
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			if (this.options.readOnly==readOnly)
				return;
				
			// 控件处于只读状态变为非只读状态
			if (this.options.readOnly && !readOnly) {
				this.input.prop('disabled',false);
				this.options.readOnly = false;
				this.input.prop('class','checkbox_box');
				this.element.prop('class','checkbox_box');
			}
			// 控件处于只读状态变为只读状态
			else if (!this.options.readOnly && readOnly) {
				this.input.prop('disabled',true);
				this.options.readOnly = true;
				this.input.prop('class','checkbox_box inactive_bgcolor');
				this.element.prop('class','checkbox_box inactive_bgcolor');
			}
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "RadioContext",
				id : this.element.prop("id"),
				enabled : !this.options.disabled,
				checked : this.options.checked
			};
		},
		setActive : function(active) {
			this._super(active);
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.checked != null)
				this.setChecked(context.checked);
		}

	});
})(jQuery);
