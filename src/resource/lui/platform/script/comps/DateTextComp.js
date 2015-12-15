/**
 * @fileoverview date类型的Text输入控件.日期格式为 YYYY-MM-DD
 * 此控件输入合法性的校验是在输入框失去焦点或者按下回车键的时候进行一次校验
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	$.widget("lui.datetext" , $.lui.textfield , {
		options : {
			dataType : "D",
			value : '',
			readOnly : false,
			multiple : false,
			multiSplitChar : ','
		},
		_initParam : function() {
			this.componentType = "DATETEXT";
			this.parentOwner = this.element.parent()[0];
			// 默认日期DIV高度
			this.CALANDER_HEIGHT = 298;
			// 默认日期DIV宽度
			this.CALANDER_WIDTH = 235;

//			this.inputClassName_init = $.browsersupport.IS_IE7? "text_input" : "input_normal_center_bg text_input";
//			this.inputClassName_inactive = $.browsersupport.IS_IE7? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
			this.inputClassName_init = "input_normal_center_bg text_input";
			this.inputClassName_inactive = "input_normal_center_bg text_input_inactive";
			//按钮图片宽高
			this.imageWidth = 19;
			this.imageHeight = 22;
			this.options.multiSplitChar = this.options.multiSplitChar =='-'?'—':this.options.multiSplitChar;
			//long转为时间
			if (this.options.value && this.options.value.indexOf("/") == -1 && this.options.value.indexOf("-")){
				var value = parseInt(this.options.value);
				if (value == this.options.value){
					var date = new Date();
					date.setTime(value);
					this.options.value = this.dateTimeFormat(date); 
				}
			}
			this._super();
			// 是否包括时间，默认为不包括
			this.showTimeBar = false;
			// 是否包括天数，默认为包括
			this.hiddenDayBar = false;
		},
		_create : function() {
			this._super();
		},
		_managerSelf : function() {
			this._super();
			var oThis = this,opts = this.options,ele = this.element;
			//加入日期选择控件
			var height = this.Div_text.outerHeight();
			var width = this.Div_text.outerWidth();
//			if ($.browsersupport.IS_IPAD){
//				width = width - 10;
//			}	
			if (width == 0)
				width =  parseInt(opts.width);
			var inputWidth = width - this.imageWidth;
//			if($.browsersupport.IS_IE)
//				inputWidth -= 3;
			this.input.css({
				'width' : inputWidth + "px",
				'position' : 'relative',
				'float' : 'left'
			});
			
//			var divButtonTop = (this.Div_text.outerHeight() - this.imageHeight)/2;
//			if(divButtonTop < 0){
//				divButtonTop = 0;
//			}
			this.divButton = $("<div></div>").addClass("date_text_arrow_nm").attr({
				'id' : ele.attr('id') + "$date_sel_button"
			}).css({
				'position' : 'absolute',
				'cursor' : 'pointer',
				'width' : this.imageWidth + "px",
				'height' : this.imageHeight + "px",
				'right' : '-2px',
				'top' : "1px"
			});
			
			if(this.Div_text.children().length == 3){
				this.Div_text.children().eq(1).append(this.divButton);
			}else{
				this.Div_text.append(this.divButton);
			}
			
			if(this.Div_text.children().length == 3){
				var centerWidth = width - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + "px");
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE){
//					inputWidth -= 3;
//				}
				this.input.css('width',inputWidth + "px");//2*2 input输入框距离左右间距
			}
			
			// 失去键盘焦点时调用
			this.input.off('blur').on('blur',function(e){
				oThis.focused = false;
				//清除只读状态下的失去焦点操作
				if(opts.readOnly)
					return;
				
				if(oThis.Div_text.children().length == 3){
					var children = oThis.Div_text.children();
					$.each(children,function(index,item){
						$(item).attr('class',$(item).attr('class').replaceStr('input_highlight','input_normal'));
					});
					
					oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
				}
				oThis.blur(e);
			});
			
			var divButtMouseoverHandler = function(e){
				$(e.target).removeClass().addClass("date_text_arrow_on");
			};
			
			var divButtMouseoutHandler = function(e) {
				$(e.target).removeClass().addClass("date_text_arrow_nm");
			};
			
			$(document).off(".calendar"+ele.attr('id')).on("click.calendar"+ele.attr('id'),function(e) {
				if (oThis.calendar) {
					oThis.calendar.calendar("hide");
					if(opts.multiple && oThis.nextCalendar) {
						oThis.nextCalendar.calendar("hide");
					}
				}
					
			}); 
			
			var divButtClickHandler = function(e) {
				// 调用该方法是为了点击该日期控件时隐藏别的日期控件使其失去焦点并将输入值设入到model
				oThis.openCalendar(e);
				e.stopPropagation();
			};
			
			this.divButton.data('mouseoverEvent',divButtMouseoverHandler)
				.data('mouseoutEvent',divButtMouseoutHandler)
				.data('clickEvent',divButtClickHandler);
			
			this.divButton.off("mouseover mouseout click").on('mouseover',function(e) {
				divButtMouseoverHandler(e);
			}).on('mouseout',function(e){
				divButtMouseoutHandler(e);
			}).on('click',function(e){
				divButtClickHandler(e);
			});
			
			//初始控件为禁用状态
			if (opts.disabled) {	
				this.setActive(false);
				//将与下拉按钮的各种动作事件保存
				this.divButton.off('click mouseover mouseout').css('cursor','default');
				this.input.attr('class',this.inputClassName_inactive);//"text_input_inactive";
				this.Div_text.attr('class',this.className + " " + this.className + "_inactive_bgcolor");
				this.divButton.removeClass().addClass("date_text_arrow_disable");
			}
		   
			//设置初始值
			if (opts.value)
				this.setValue(opts.value);
		},
		/**
		 * 设置是否包括时间
		 */
		setShowTimeBar : function(showTimeBar) {
			this.showTimeBar = showTimeBar;
		},
		/**
		 * 设置是否包括天数
		 */
		setHiddenDayBar : function(hiddenDayBar) {
			this.hiddenDayBar = hiddenDayBar;
		},
		_destroy : function() {
			this._super();
			if(this.calendar) {
				this.calendar.remove();
			}
			if(this.options.multiple && this.nextCalendar) {
				this.nextCalendar.remove();
			}
			$(document).off(".calendar"+this.element.attr('id'));
		},
		/**
		 * 处理回车事件
		 * @private
		 */
		processEnter : function() {
			var inputValue = this.input.val().trim();
			if(inputValue == ""){
				// 如果没有输入任何值点击回车,则设置今天日期
				inputValue = this.getFormater().formatDateToString();
			}
			if(this.hiddenDayBar){
				inputValue = this.getFormater().formatYearAndMonth(inputValue);
			}else{
				if(this.showTimeBar) {
					inputValue = this.getFormater().formatInputValueToDateString(inputValue);
				} else {
					inputValue = this.getFormater().formatDate(inputValue);
				}
			}
			if(this.newValue == "" || this.newValue == null || this.newValue != inputValue){
				this.newValue = inputValue;
			}
			this.verify();
			this.setMessage(this.newValue);
			if(this.newValue != this.oldValue){
				this._trigger('valuechanged',null,{oldValue : this.oldValue ,newValue : this.newValue});
			}
		},
		getFormater : function() {
			return this._super();
		},
		/**
		 * 创建默认格式化器,子类必须实现此方法提供自己的默认格式化器
		 * @private
		 */
		createDefaultFormater : function() {
			var dateFormater = $.dateformater.getObj();
			dateFormater.showTimeBar = this.showTimeBar;
			return dateFormater;
		},
		/**
		 * 失去焦点时进行输入的类型检查
		 * @private
		 */
		blur : function() {
			if(this.visible){
				var isNeedVerify = true;
				var inputValue = this.input.val().trim();
				if(inputValue == ""){
					isNeedVerify = false;
				}
				if(this.hiddenDayBar){
					if(!this.options.multiple) {
						inputValue = this.getFormater().formatYearAndMonth(inputValue);
					}
				}else{
					if(!this.options.multiple) {
						if(this.showTimeBar) {
							inputValue = this.getFormater().formatInputValueToDateString(inputValue);
						} else {
							inputValue = this.getFormater().formatDate(inputValue);
						}
					}
				}
				if(this.newValue == "" || this.newValue == null || this.newValue != inputValue){
					this.newValue = inputValue;
				}
				if(isNeedVerify){
					this.verify();
				}
				this.setMessage(this.newValue);
				if(this.newValue != this.oldValue){
					this._trigger('valuechanged',null,{oldValue : this.oldValue ,newValue : this.newValue});
				}
				this._trigger('onblur');
			}
		},
		/**
		 * 取对应long值
		 * private
		 */
		getUtcValue : function() {
			var date = this.newValue;
			if (date.indexOf("-") > -1)
				date = date.replace(/\-/g,"/");
			var utcValue = Date.parse(date);
			if (isNaN(utcValue)) 
				return "";
			return utcValue.toString();	
		},
		/**
		 * 把date类型格式化成yyyy-MM-dd HH:Mi:SS
		 * @param {date} date
		 * @return {String}   YYYY-MM-DD HH:Mi:SS
		 * private
		 */
		dateFormat : function(date) {
			var year = date.getFullYear();
			var month = date.getMonth() + 1;
			if (parseInt(month)<10) month = "0" + month;
			var day = date.getDate();
			if (parseInt(day)<10) day = "0" + day;
			var hours = date.getHours();
			if (parseInt(hours)<10) hours = "0" + hours;
			var minutes = date.getMinutes();
			if (parseInt(minutes)<10) minutes = "0" + minutes;
			var seconds = date.getSeconds();
			if (parseInt(seconds)<10) seconds = "0" + seconds;
			var formatString = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
			return formatString;
		},
		/**
			得到context信息
		**/
		getContext : function() {
			var context = this._super();
			if (context == null) 
				return null;
			//context.value = this.getUtcValue();
			context.value = this.newValue;
			return context;
		},
		/**
			设置context信息
		**/
		setContext : function(context) {
			if (context.value && context.value != ""){
				var date = new Date();
				date.setTime(parseInt(context.value));
				context.value = this.dateFormat(date); 
			}
			this._super(context);
		},
		verify : function() {
			if (this.newValue == "") {
				$.formater.showVerifyMessage(this.element, trans("ml_dateisinvalid"));
				return false;
			}
			return true;
		},
		/**
		 * 打开日期控件
		 * @private
		 */
		openCalendar : function(e) {		 
			var oThis = this;		
			var left = this.Div_text.offset().left;
			
			if (!this.calendar){
				this.calendar = $("<div></div>").appendTo("body").calendar({
					isState : false
				});
				if(this.options.multiple) {
					this.nextCalendar = $("<div></div>").appendTo("body").calendar({
						isState : false
					});
				}
			}
			if(this.calendar.calendar("getZIndex") < $.CONST.STANDARD_ZINDEX){
				this.calendar.calendar("setZIndex",$.measures.getZIndex());
			}
			if(this.options.multiple) {
				if(this.nextCalendar.calendar("getZIndex") < $.CONST.STANDARD_ZINDEX){
					this.nextCalendar.calendar("setZIndex",$.measures.getZIndex());
				}
			}
			this.calendar.off("calendaronclick").on("calendaronclick",function(e,date) {
				oThis.isNext = false;
			    oThis.setFocus();        
                oThis.oldValue = oThis.getValue();
                // 通过选择获取日期不通过setValue设置值,避免通过setValue方法每次oldValue均相同的问题 gd:2008-01-29
                oThis.setValue(date);
                oThis.setMessage(date);
                //oThis._trigger("valuechanged",null,{oldValue:oThis.oldValue,newValue:oThis.newValue});
			});
			if(this.options.multiple) {
				this.nextCalendar.off("calendaronclick").on("calendaronclick",function(e,date) {
					oThis.isNext = true;
				    oThis.setFocus();        
	                oThis.oldValue = oThis.getValue();
	                // 通过选择获取日期不通过setValue设置值,避免通过setValue方法每次oldValue均相同的问题 gd:2008-01-29
	                oThis.setValue(date);
	                oThis.setMessage(date);
	                //oThis._trigger("valuechanged",null,{oldValue:oThis.oldValue,newValue:oThis.newValue});
				});
			}
			var top = this.element.offset().top + this.Div_text.outerHeight();
			var dateValue = this.getValue().trim();
			
			if(this.options.multiple) {
				var valArray = dateValue.split(this.options.multiSplitChar);
				var preVal=null,lastVal = null;
				if(valArray.length==2) {
					preVal = valArray[0];
					lastVal = valArray[1];
				}
				this.calendar.calendar("show",left, top, this.showTimeBar, preVal, this.hiddenDayBar);
				this.nextCalendar.calendar("show",(left+this.calendar.outerWidth()-2), top, this.showTimeBar, lastVal, this.hiddenDayBar);
			} else {
				this.calendar.calendar("show",left, top, this.showTimeBar, dateValue, this.hiddenDayBar);
			}
		},
		setFocus : function() {
			this._super();
		},
		getValue : function() {
			return this._super();
		},
		/**
		 * 覆盖基类的此方法,用于隐藏已经打开的日历控件
		 */
		hideV : function() {	
			this.element.css('visibility','hidden');
			this.visible = false;
			if (this.calendar != null)
				this.calendar.calendar("hide");
			if(this.options.multiple) {
				if(this.nextCalendar) {
					this.nextCalendar.calendar("hide");
				}
			}
		},
		/**
		 * 显示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
			this.visible = true;
		},
		/**
		 * 设置值
		 * 日期控件三种显示（年-月-日、年-月-日 时:分:秒、年-月）前两种value包括日期和时间,第三种value只包括年和月
		 */
		setValue : function(text) {
			var tempText = text;
			if(this.hiddenDayBar){
				tempText = this.getFormater().formatYearAndMonth(tempText);
			}else{
				if(this.showTimeBar) {
					tempText = this.getFormater().formatInputValueToDateString(tempText);
				} else {
					tempText = this.getFormater().formatDate(tempText);
				}
			}
			if(this.options.multiple && this.newValue) {
				if(text.indexOf(this.options.multiSplitChar) >= 0 ) {
					tempText = text;
				} else {
					var _newValueArray = this.newValue.split(this.options.multiSplitChar);
					if(_newValueArray.length == 2) {
						if(this.isNext) {
							_newValueArray[1] = tempText;
						} else {
							_newValueArray[0] = tempText;
						}
					} else {
						if(this.isNext) {
							_newValueArray.push(tempText);
						} else {
							_newValueArray[0] = tempText;
						}
					}
					tempText = _newValueArray.join(this.options.multiSplitChar);
				}
			}
			this._super(tempText);
		},
		setTitle : function(title) {
			this._super(title);
		},
		/**
		 * 
		 * @param {} message
		 */
		setMessage : function(message) {
		    if (!this.isError) {
		    	this.maskValue();
		    	
		    	this.message = this.showValue;
		        this.errorMessage = "";
		        this.setTitle(this.message);
		    }
		},
		maskValue : function(){
			if(!this.hiddenDayBar){
				if(this.options.multiple) {
					this.showValue = this.newValue;
				} else {
					var maskerType = this.showTimeBar ? "DateTimeText" : "DATETEXT";
					var masker = $.pageutils.getMasker(maskerType);
					if(masker != null)
						this.showValue = $.maskerutil.toColorfulString(masker.format(this.newValue));
					else
						this.showValue = this.newValue;
				}
			}else{
				this.showValue = this.newValue;
			}
		},
		postProcessNewValue : function(value) { 
			 return value.substring(0,10);
			
		},
		/**
		 * 设置大小和位置
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options, ele = this.element;
			opts.left = left;
			opts.top = top;
			opts.width = $.argumentutils.getString($.measures.convertWidth(width), ele.outerWidth() + "px");
			opts.height = $.argumentutils.getString($.measures.convertHeight(height), ele.outerHeight() + "px");
			// 设置最外层的大小
			ele.css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : opts.width,
				'height' : opts.height
			});

			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = ele.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
			
			// 设置输入区域的大小
			this.Div_text.css('width',tempWidth - 4 + "px");
			if (this.hasLabel)
				this.Div_text.css('width',tempWidth - opts.labelWidth - 10 + "px");
			//this.Div_text.style.height = this.height - 4 + "px";
			
			var pixelHeight = this.Div_text.outerHeight();
			var pixelWidth = this.Div_text.outerWidth();
//			if ($.browsersupport.IS_IPAD){
//				pixelWidth = pixelWidth - 10;
//			}
			this.input.css('width',pixelWidth - this.imageWidth + "px");
			
			if(this.Div_text.children().length == 3){
				var centerWidth = pixelWidth - 3*2;//3*2左右边框图片宽度
				this.Div_text.children().eq(1).css('width',centerWidth + "px");
				var imgWidth = (this.Div_text.children().eq(1).children().length - 1) * this.imageWidth;//与input输入框同一个DIV中图片的总宽度
				var inputWidth = centerWidth - imgWidth;
//				if($.browsersupport.IS_IE){
//					inputWidth -= 3;
//				}
				this.input.css('width',inputWidth + "px");//2*2 input输入框距离左右间距
			}
		},
		/**
		 * 设置日期输入框控件的激活状态.
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			var isActive = $.argumentutils.getBoolean(isActive, false);
			// 控件处于激活状态变为非激活状态
			if (!isActive) {
				this._super(false);
				this.divButton.off('click mouseout mouseover').css('cursor','default').removeClass().addClass('date_text_arrow_disable');
			}
			// 控件处于禁用状态变为激活状态
			else if (isActive) {	
				this._super(true);
				this.divButton.off('click mouseout mouseover').on('click',function(e){$(this).data('clickEvent')(e);})
					.on('mouseout',function(e){$(this).data('mouseoutEvent')(e);})
					.on('mouseover',function(e){$(this).data('mouseoverEvent')(e);})
					.css('cursor','pointer').removeClass().addClass('date_text_arrow_nm');
				this.input.attr('class',this.inputClassName_init);//"text_input";
				this.Div_text.attr('class',this.options.className);
			}
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			this.input.attr('readOnly',readOnly);
			this.options.readOnly=readOnly;
			if (readOnly) {
				this.Div_text.attr('class',this.options.className + " " + this.options.className + "_readonly");
				this.input.attr('class',this.inputClassName_init + " text_input_readonly");//"text_input text_input_readonly";
				//this.divButton.css('visibility','hidden');
			} else {
				this.Div_text.attr('class',this.options.className);
				this.input.attr('class',this.inputClassName_init);//"text_input";
				//this.divButton.css('visibility','');
			}
		},
		/**
		 * 得到输入框的激活状态
		 */
		isActive : function() {
			return !this.options.disabled;
		}
	});
	
})(jQuery);
