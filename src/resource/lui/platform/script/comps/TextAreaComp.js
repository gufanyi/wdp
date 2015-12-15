/**
 * @fileoverview 此控件是对html textarea的封装,可以设置rows和cols
 * 
 * @author lxl
 * @version 1.0
 * 
 */
(function($) {
	/**
	 * TextArea控件的构造函数
	 * @class TextArea控件
	 * @param rows 该元素有多少行高
	 * @param cols 列宽(以字符记)
	 * @param readOnly 若为true,则用户不能编辑任何显示的文本
	 */
	$.widget("lui.textarea", $.lui.base, {
		options : {
			rows : 3,
			cols : 0,
			position : 'absolute',
			readOnly : false,
			value : '',
			tip : '',
			className : 'text_div',
			//event
			onfocus : null,
			onblur : null,
			onkeydown : null,
			valuechanged : null,
		},
		_initParam : function() {
			this.componentType = "TEXTAREA";
			this.parentOwner = this.element.parent()[0];
			this._super();
			this.inactivate = "inactivate";
			this.highlight = "highlight";
			this.readonly = "readonly";
			this.disabled = false;
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts=this.options,ele=this.element;
			ele.addClass(opts.className).css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'position' : opts.position,
				'height' : opts.height,
				'width' : opts.width,
				'border-width' : '0px',
				'overflow' : 'hidden'
			});
			
			//创建textarea对象
			this.textArea = $('<textarea></textarea>').addClass('text_area').css({
				'resize' : 'none'
			}).appendTo(ele);
			if (opts.cols){
				this.textArea.attr('cols',opts.cols);
			}
			else{
				var width = 0;
				if ($.argumentutils.isPercent(opts.width)){
					width = ele.outerWidth();
				}
				else{
					width = $.argumentutils.getInteger(parseInt(opts.width), 120);
				}
				this.textArea.css('width',(width - 6) < 0 ? 0 : (width - 6) + "px");
				//this.textArea.style.width="99%";
				//width=this.textArea.offsetWidth;
				//this.textArea.style.width=(width-6)+"px";
			}
			if (opts.rows){
				this.textArea.attr('rows',opts.rows);
			}
			else{
				if (!$.argumentutils.isPercent(opts.height)){
					this.textArea.css('height',(parseInt(opts.height) - 6) + "px");
				}
				else{
//					if ($.browsersupport.IS_IE){
//						this.textArea.css('height','90%');
//					}
//					else{
						this.textArea.css('height','99%');
//					}
				}
			}
			
			if (opts.readOnly){
				this.setReadOnly(opts.readOnly);
			}

			//设置初始值
			if (opts.value != null){
				this.setValue(opts.value);
			}
			else{
				this.showTip();
			}
			
			//获得键盘焦点时调用
			this.textArea.on('focus',function(e){
				oThis.oldValue = oThis.getValue();
				if (!opts.readOnly){
					ele.attr('class',oThis.className + " " + oThis.highlight);
				}
				oThis._trigger('onfocus',e);
				oThis.hideTip();

			}).on('blur',function(e){
				oThis.newValue = oThis.getValue();
				this.value = oThis.newValue;
				oThis.setTitle(this.value);
				if (oThis.newValue != oThis.oldValue)
					oThis._trigger('valuechanged',null,{oldValue:oThis.oldValue,newValue:oThis.newValue});
				if (opts.readOnly == false){
					ele.attr('class',opts.className);
				}
				oThis._trigger('onblur',e);
				oThis.showTip();

			}).on('click',function(e){
				e.stopPropagation();

			}).on('keydown',function(e){
				// readOnly时不允许输入
				if(opts.readOnly)
					return false;
				// 调用用户的方法
				oThis._trigger('onkeydown',e);
			}).on('select',function(e){
				e.stopPropagation();
			});

			this.textArea.get(0).onselectstart = function(e) {
				e.stopPropagation();
			};
		},
		setError : function() {},
		/**
		 * 校验是否有默认提示信息
		 * @private
		 */
		checkTip : function() {
			if (this.options.tip != null && this.options.tip != "")
				return true;
			else
				return false;
		},
		/**
		 * 显示提示信息
		 * @private
		 */
		showTip : function() {
			if (this.checkTip()) {
				if (this.textArea.val() == "") {
					this.textArea.val(this.options.tip);
					this.textArea.css('color','gray');
				}
			}
		},
		/**
		 * 隐藏提示信息
		 * @private
		 */
		hideTip : function() {
			if (this.checkTip()) {
				if (this.textArea.val() == this.options.tip) {
					this.textArea.val('');
					this.textArea.css('color','black');
				}
			}
		},
		/**
		 * 设置大小及位置
		 * @param width 像素大小
		 * @param height 像素大小
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options;
			// 改变数据对象的值
			opts.left = left;
			opts.top = top;
			opts.width = $.argumentutils.getString($.measures.convertWidth(width), this.element.outerWidth() + "px");
			opts.height = $.argumentutils.getString($.measures.convertHeight(height), this.element.outerHeight() + "px");
			
			// 改变显示对象的值
			this.element.css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : opts.width,
				'height' : opts.height
			});
			
			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = this.element.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
//			if ($.browsersupport.IS_IE8)
//				this.textArea.css('width',(tempWidth - 8) < 0 ? 0 : (tempWidth - 8) + "px");
//			else	
				this.textArea.css('width',(tempWidth - 6) < 0 ? 0 : (tempWidth - 6) + "px");

			var tempHeight = 0;
			if ($.argumentutils.isPercent(opts.height)){
				tempHeight = this.element.outerHeight();
				this.textArea.css('height',tempHeight + "px");
			}
			else{
				tempHeight = $.argumentutils.getInteger(parseInt(opts.height), 120);
				this.textArea.css('height',(tempHeight - 6) < 0 ? 0 : (tempHeight - 6) + "px");
			}
		},
		/**
		 * 得到textarea中的文本内容
		 */
		getValue : function() {
			if (this.checkTip()) {
				if (this.textArea.val() == this.options.tip && this.textArea.css('color') != "black")
					return "";
			}
			return this.textArea.val();
		},
		/**
		 * 设置textarea中的文本内容
		 */
		setValue : function(value) {
			this.oldValue = this.getValue();
			value = $.argumentutils.getString(value, "");
			this.textArea.val(value);
			if (this.checkTip()) {
				if (this.textArea.val() == "")
					this.showTip();
				else
					this.textArea.css('color','black');
			}
			this.newValue = value;
			if (this.newValue != this.oldValue)
				this._trigger('valuechanged',null,{oldValue:this.oldValue,newValue:this.newValue});
			this.setTitle(this.newValue);	
		},
		setTitle : function(title) {
			this.textArea.attr('title',title);
		},
		/**
		 * 设置此TextArea控件的激活状态
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			var isActive = $.argumentutils.getBoolean(isActive, false);
			// 控件处于激活状态变为非激活状态
			if (!this.disabled && !isActive) {
				//TODO 临时办法，解决ie下设置禁用后，textarea滚动条不能用问题
//				if ($.browsersupport.IS_IE)
//					this.textArea.attr('readOnly',true);
//				else	
					this.textArea.attr('disabled',true);
				this.disabled = true;
				this.element.attr('class',this.options.className + " " + this.inactivate);
				this.textArea.css('background-color','#E1E1E1');
			}
			// 控件处于禁用状态变为激活状态
			else if (this.disabled && isActive) {
//				if ($.browsersupport.IS_IE)
//					this.textArea.attr('readOnly',false);
//				else
					this.textArea.attr('disabled',false);
				this.disabled = false;
				this.element.attr('class',this.options.className);
				this.textArea.css('background-color','#FFFFFF');
			}
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			this.textArea.attr('readOnly',readOnly);
			this.readOnly = readOnly;
			if (readOnly) {
				this.element.attr('class',this.className + " " + this.readOnly);
			} else {
				this.element.attr('class',this.className);
			}
		},
		/**
		 * 设置聚焦
		 */
		setFocus : function() {
//			if ($.browsersupport.IS_IE) {
//				this.textArea.focus();
//				this.textArea.select();
//			} else {  // firefox等浏览器不能及时执行focus方法
				var oThis = this;
				window.setTimeout(function(){oThis.textArea.focus();oThis.textArea.select();}, 50); 
//			}
		},
		/**
		 * 得到输入框的激活状态
		 */
		isActive : function() {
			return !this.disabled;
		},
		outsideClick : function(e) {
			this.textArea.triggerHandler('blur');
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "TextAreaContext",
				id : this.element.attr("id"),
				enabled : !this.disabled,
				readOnly : this.options.readOnly,
				value : this.getValue(),
				visible : this.visible
			};
		},
		showV : function() {
			this._super();
		},
		hideV : function() {
			this._super();
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.readOnly != null && this.readOnly != context.readOnly)
				this.setReadOnly(context.readOnly);
			if (context.value != null && context.value != this.textArea.value)
				this.setValue(context.value);
//			if (context.visible != this.visible) {
				if (context.visible)
					this.showV();
				else
					this.hideV();
//			}
			if (context.focus)
				this.setFocus();
		}
	});
})(jQuery);
