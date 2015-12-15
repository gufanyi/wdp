/**
 * 日期控件。控件状态分为弹出状态和静态状态。对于弹出状态， * 日期控件为一单例.对于静态状态,每个控件对应一个实例, 并 *
 * 且创建时应传入一个true参数 * *
 * 
 * @author lxl
 * @version lui 1.0
 * 
*/
(function($) {
	var MONTHS = ["January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"];
	var WEEKDAYS_FULL = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday"];
	var WEEKDAYS = null;
	var MONTH_DAY_COUNT = ["31", "28", "31", "30", "31", "30", "31", "31",
			"30", "31", "30", "31"];
	var currDate = new Date();
	var DimanchePaques = false;
	
	$.widget("lui.calendar", $.lui.base , {
		options : {
			isState : false,
			//event
			onclick : null
		},
		_initParam : function() {
			this.componentType = "CALENDAR";
			this.parentOwner = this.element.parent()[0];
			if (WEEKDAYS == null)
				WEEKDAYS = [trans("ml_seven"), trans("ml_one"), trans("ml_two"),
						trans("ml_three"), trans("ml_four"), trans("ml_five"),
						trans("ml_six")];
			this._super();
			this.dateObject = new Date();
			this.day = this.dateObject.getDate();
			this.month = this.dateObject.getMonth() + 1;
			this.year = this.dateObject.getFullYear();
			this.weekDay = this.dateObject.getDay();
			this.oldDayCell = null;
			this.oldDayCellColor = null;
			// 注册外部回掉函数
			//window.clickHolders.push(this);
		},
		_create : function() {
			this._initParam();
			var oThis = this,ele = this.element;
			
			ele.addClass('calendar_div').css('z-index',$.measures.getZIndex()).on('click',function(e){
				e.stopPropagation();
			});
			this.bgDiv = $("<div></div>");
		    var frmstr = '<iframe src="" style="position:absolute; visibility:inherit; top:0px; left:0px; width:100%;height:100%; z-index:-1; border:none;" frameborder="0"></iframe>';
		    this.bgDiv.html(frmstr).addClass('background_div').appendTo(ele);
			
			this.bgCenterDiv = $("<div></div>").addClass('bg_center_div').appendTo(this.bgDiv);
			this._managerSelf();
		},
		_managerSelf : function() {
			var oThis = this,opts = this.options,ele = this.element;
			// 操作菜单栏
			this.opBar = $("<div></div>").addClass('opBar').appendTo(this.bgCenterDiv);
			
			this.weekBar = $("<div></div>").addClass('weekBar');
			if(!this.hiddenDayBar){
				this.bgCenterDiv.append(this.weekBar);
			}
			
			var marginLeft = 30;
			// 年份选择框
			this.preYearDiv = $("<div></div>").addClass('preDiv_nm').css({
				'left' : marginLeft + "px"
			}).on('mouseover',function(e){
				$(this).attr('class','preDiv_on');
			}).on('mouseout',function(e){	
				$(this).attr('class','preDiv_nm');
			}).on('click',function(e){
				oThis.changeDate(oThis.year - 1, oThis.month);
				e.stopPropagation();
			}).appendTo(this.opBar);
			
			this.yearInput = $("<input/>").addClass('calendar_bar_input').css({
				'left' : marginLeft + 10 + "px"
			}).on('change',function(e){
				var currYear=this.value.replace(trans("ml_year"),"");
				oThis.changeDate(parseInt(currYear, 10), oThis.month);
				e.stopPropagation();
				
			}).on('mousedown',function(e){
				e.stopPropagation();
			}).appendTo(this.opBar);
			
			this.nextYearDiv = $("<div></div>").addClass('nextDiv_nm').css({
				'left' : marginLeft + 70 + "px"
			}).on('mouseover',function(e){
				$(this).attr('class','nextDiv_on');
			}).on('mouseout',function(e){
				$(this).attr('class','nextDiv_nm');
			}).on('click',function(e){
				// 首先调用用户函数获取任务数据
				oThis.changeDate(oThis.year + 1, oThis.month);
				e.stopPropagation();
			}).appendTo(this.opBar);
			
			
			// 创建上一月按钮
			this.preMonButt = $("<div></div>").addClass('preDiv_nm').css({
				'left' : marginLeft + 105 + "px"
			}).on('mouseover',function(e){
				$(this).attr('class','preDiv_on');
			}).on('mouseout',function(e){
				$(this).attr('class','preDiv_nm');
			}).on('click',function(e){
				oThis.changeDate(oThis.year, oThis.month - 1);
			}).appendTo(this.opBar);
			
			this.monthInput = $("<input/>").addClass('calendar_bar_input').css({
				'left' : marginLeft + 115 + "px",
				'width' : '20px'
			}).on('mousedown',function(e){
				e.stopPropagation();
			}).on('change',function(e){
				var currMonth = this.value.replace(trans("ml_month"),"");
				oThis.changeDate(oThis.year, parseInt(currMonth, 10));
				e.stopPropagation();
				
			}).appendTo(this.opBar);
			
			// 创建下一月按钮
			this.nextMonButt = $("<div></div>").addClass('nextDiv_nm').css({
				'left' : marginLeft + 158 + "px"
			}).on('mouseover',function(e){
				$(this).attr('class','nextDiv_on');
			}).on('mouseout',function(e){
				$(this).attr('class','nextDiv_nm');
			}).on('click',function(e){
				oThis.changeDate(oThis.year, oThis.month + 1);
			}).appendTo(this.opBar);
			
			if(!this.hiddenDayBar){
				// 创建星期显示的每个div
				for (j = 0; j < 7; j++) {
					var divWeekday = $("<div></div>").addClass('calendar_day').css({
						'left' : (j * 30)+7 + "px"
					}).html(WEEKDAYS[j]).appendTo(this.weekBar);
					if(j == this.weekDay){
						divWeekday.addClass('calendar_current_day');
					}
				}
			}

			var sepBelowDivWeek = $("<hr/>").css({
				'position' : 'absolute',
				'width' : this.opBar.outerWidth() - 40 + "px",
				'left' : '18px',
				'color' : '#c6d6e9'
			}).attr('size',1);
		//	if (!$.browsersupport.IS_IE || $.browsersupport.IS_IE8)
				sepBelowDivWeek.css('top',55 + "px");
		//	else
		//		sepBelowDivWeek.css('top',60 + "px");
			
			
			//this.opBar.appendChild(sepBelowDivWeek);
			
			this.element.draggable({
				cancel: "input , .preDiv_nm , .nextDiv_nm , .preDiv_on , .nextDiv_on",
				handle: ".opBar",
				containment: "document"
			});

			this.div_Calendar = $("<div></div>").addClass('calendarDiv');
			if(!this.hiddenDayBar){
				this.bgCenterDiv.append(this.div_Calendar);
			}

			this.resetCalendar();
		},
		/**
		 * 外部mouseclick事件发生时的回调函数.用来隐藏弹出的calendar控件
		 * @private
		 */
		outsideClick : function(e) {
			this.hide();
		},
		outsideMouseWheelClick : function(e) {
			this.hide();
		},
		/**
		 * 显示日历控件
		 */
		show : function(left, top, showTimeBar, dateValue, hiddenDayBar) {
			
			var opts = this.options, ele = this.element,oThis = this;
			opts.left = left;
			opts.top = top;
			ele.show().offset({top:opts.top,left:opts.left});
			
			if (showTimeBar){
				this.showTimeBar = true;
			}else{
				this.showTimeBar = false;
			}
			if(hiddenDayBar){
				this.hiddenDayBar = true;
			}else{
				this.hiddenDayBar = false;
			}
			
			this.dateValue = dateValue;
			
			var oldDateObject = this.dateObject;	
			if (this.dateValue != null && this.dateValue != ""){
				if (this.dateValue.indexOf("-") > -1)
					this.dateValue = this.dateValue.replace(/\-/g,"/");
				this.dateObject = new Date(this.dateValue);
				if(isNaN(this.dateObject.getFullYear())){
					var values = dateValue.split("-");
					if(values && values.length == 3){
						var year = parseInt(values[0],10);
						var month = parseInt(values[1],10) - 1;
						var day = parseInt(values[2],10);
						if(isNaN(year) || isNaN(month) || isNaN(day)){
							this.dateObject = new Date();		
						}else{
							this.dateObject = new Date(year,month,day);			
						}
					}else{
						this.dateObject = new Date();
					}
				}
			}else{	
				this.dateObject = new Date();	
			}
			
			if (this.dateObject.getAAAAMMJJ() != oldDateObject.getAAAAMMJJ()) {
				this.day = this.dateObject.getDate();
				this.month = this.dateObject.getMonth() + 1;
				this.year = this.dateObject.getFullYear();
//				this.resetCalendar();
			}
			
			// 显示时间框
			if (this.showTimeBar) {
				if (this.timeBar == null) {
					this.timeBar = $("<div></div>").css({
						'position' : 'absolute',
						'width' : '100%',
						'height' : '55px',
						'top' : (ele.outerHeight() - 75) + "px",
						'left' : '0px'
					}).hide();
					if(!this.hiddenDayBar){
						this.bgCenterDiv.append(this.timeBar);
					}

					var marginLeft = 10;
					
					//生成时间输入框
					var createInput = function(offsetLeft) {
						return $("<input/>").addClass('calendar_bar_input').css({
								'width' : '20px',
								'left' : marginLeft + offsetLeft + "px"				
							}).on('click',function(e){
								e.stopPropagation();
							});
					};
					
					//生成向上向下按钮位置包裹
					var createTimeBtnWrap = function(offsetLeft) {
						return $("<div></div>").addClass('calendar_bar_img_div').css({
								'position' : 'absolute',
								'left' : marginLeft + offsetLeft + "px"
							});
					};
					
					//生成向上向下按钮
					var createTimeBtn = function(direction,changeItem,type) {
						var perTime = (type == 0 ? 23 : 59);
						return $("<div></div>").addClass('calendar_bar_img' + direction).on('click',function(e){
								var value = changeItem.val();
								if (value == null || value == "")
									value = 0;
								else
									value = parseInt(value,10);
								if(direction=="up")
									value++;
								else
									value--;
								if (value > perTime)
									value = perTime;
								else if (value < 0)
									value = 0;
				
								changeItem.val(value);
								e.stopPropagation();
							});
					};
					
					//创建显示文字
					var createTimeText = function(offsetLeft,text) {
						return $("<div></div>").css({
								'position' : 'absolute',
								'width' : '10px',
								'left' : marginLeft + offsetLeft + "px",
								'top' : '8px'				
							}).html(text);
					};
					
					/* 小时输入框 */
					this.hourInput = createInput(0).appendTo(this.timeBar);
					this.hourDiv = createTimeBtnWrap(36).appendTo(this.timeBar);  // 小时上下移动图标
					createTimeBtn('up',oThis.hourInput,0).appendTo(this.hourDiv);  // 上一小时
					createTimeBtn('down',oThis.hourInput,0).appendTo(this.hourDiv);  // 下一小时
					createTimeText(53,trans("ml_hour")).appendTo(this.timeBar);  //显示小时

					/* 分钟输入框  */
					this.minInput = createInput(68).appendTo(this.timeBar);
					this.minDiv = createTimeBtnWrap(104).appendTo(this.timeBar);			
					createTimeBtn('up',oThis.minInput,1).appendTo(this.minDiv);  // 上一分钟
					createTimeBtn('down',oThis.minInput,1).appendTo(this.minDiv);  // 下一分钟
					createTimeText(121,trans("ml_min")).appendTo(this.timeBar);  //显示分钟

					/* 秒输入框  */
					this.secInput = createInput(136).appendTo(this.timeBar);
					this.secDiv = createTimeBtnWrap(172).appendTo(this.timeBar);			
					createTimeBtn('up',oThis.secInput,1).appendTo(this.secDiv);  // 上一秒钟			
					createTimeBtn('down',oThis.secInput,1).appendTo(this.secDiv);  // 下一秒钟			
					createTimeText(190,trans("ml_sec")).appendTo(this.timeBar);  //显示秒
					
					//确定取消按扭
					var btnDiv = $("<div></div>").css({
						'height' : '30px',
						'width' : '100%',
						'top' : '30px',
						'position' : 'absolute'
					}).appendTo(this.timeBar);
					
					if(this.hiddenDayBar){
						this.bgCenterDiv.append(btnDiv);
						btnDiv.css('top','40px');
					}
					
					this.cancelBtDiv = $("<div></div>").css({
						'float' : 'right',
						'margin-right' : '8px'
					}).appendTo(btnDiv);
					
					this.okBtDiv = $("<div></div>").css({
						'float' : 'right',
						'margin-right' : '8px'
					}).appendTo(btnDiv);
				//	if (!$.browsersupport.IS_IE) {
						this.okBtDiv.css('text-align','-moz-right');
						this.cancelBtDiv.css('text-align','-moz-left');
				//	}
					
					this.okText = trans("ml_ok");
					this.cancelText = trans("ml_cancel");
					this.okBt = $("<div id='okBt'></div>").appendTo(this.okBtDiv).button({
						width : 60,
						text : (this.okText == null? trans("ml_ok") : this.okText),
						position : 'relative',
						className : 'blue_button_div',
						onclick : function(e) {
							oThis.onClick(oThis.day);
						}
					});
					this.cancelBt = $("<div id='cancelBt'></div>").appendTo(this.cancelBtDiv).button({
						width : 60,
						text : (this.cancelText == null? trans("ml_cancel"):this.cancelText),
						position : 'relative',
						className : 'button_div',
						onclick : function(e) {
							oThis.hide();
						}
					});
						
				}
				this.hourInput.val(this.dateObject.getHours());
				this.minInput.val(this.dateObject.getMinutes());	
				this.secInput.val(this.dateObject.getSeconds());
				if (typeof calendarChange != "undefined"){
					calendarChange.call(this, oThis);
				}

			}
			if(this.hiddenDayBar){
				if(this.timeBar==null){
					this.timeBar = $("<div></div>").css({
						'position' : 'absolute',
						'width' : '100%',
						'height' : '55px',
						'top' : (ele.outerHeight() - 75) + "px",
						'left' : '0px'
					}).hide().appendTo(this.bgCenterDiv);
					
					
					//确定取消按扭
					var btnDiv = $("<div></div>").css({
						'height' : '30px',
						'width' : '100%',
						'top' : '30px',
						'position' : 'absolute'
					}).appendTo(this.timeBar);
					
					this.cancelBtDiv = $("<div></div>").css({
						'float' : 'right',
						'margin-right' : '8px'
					}).appendTo(btnDiv);
					
					this.okBtDiv = $("<div></div>").css({
						'float' : 'right',
						'margin-right' : '8px'
					}).appendTo(btnDiv);
				//	if (!$.browsersupport.IS_IE) {
						this.okBtDiv.css('text-align','-moz-right');
						this.cancelBtDiv.css('text-align','-moz-left');
				//	}
					
					this.okText = trans("ml_ok");
					this.cancelText = trans("ml_cancel");
					
					this.okBt = $("<div id='okBt'></div>").button({
						width : 60,
						text : (this.okText == null? trans("ml_ok") : this.okText),
						position : 'relative',
						className : 'blue_button_div',
						onclick : function(e) {
							oThis.onClick(oThis.day);
						}
					});
					this.cancelBt = $("<div id='cancelBt'></div>").button({
						width : 60,
						text : (this.cancelText == null? trans("ml_cancel"):this.cancelText),
						position : 'relative',
						className : 'button_div',
						onclick : function(e) {
							oThis.hide();
						}
					});
				}
			}
			if(this.hiddenDayBar){
				this.timeBar.show();
			}else{
				if (this.showTimeBar)
					this.timeBar.show();
				else {
					if (this.timeBar != null)
						this.timeBar.hide();
				}
			}
			this.resetCalendar();
			
		},
		/**
		 * 设置日期
		 */
		setDate : function(y, m, d) {
			this.day = d;
			this.month = m;
			this.year = y;
			this.dateObject = new Date(y, m - 1, d);
		},
		/**
		 * 改变日期,年,月
		 */
		changeDate : function(y, m) {
			if (this.dateObject == null)
				this.dateObject = currDate; 
			var d = this.dateObject.getDate();
			if (m == 13) {
				m = 1;
				y++;
			}
			if (m == 0) {
				m = 12;
				y--;
			}
			this.setDate(y, m, d);
			this.resetCalendar();
		},
		/**
		 * 处理点击事件
		 * @private
		 */
		onClick : function(day) {
			var tmpDay = day;
			var tmpMonth = 0 + this.month;
			var tmpYear = this.year;
			if (tmpDay < 10) {
				tmpDay = "0" + tmpDay;
			}

			if (tmpMonth < 10) {
				tmpMonth = "0" + tmpMonth;
			}

			if(this.hiddenDayBar){
				this._trigger('onclick',null,tmpYear + "-" + tmpMonth);
			}else{
				if (this.showTimeBar) {
					var hour = this.hourInput.val();
					if (hour != null) {
						hour = parseInt(hour,10);
						if (!$.argumentutils.isNumber(hour) || hour < 0 || hour >= 24)
							hour = "00";
						else if (hour < 10)
							hour = "0" + hour;
					} else {
						hour = "00";
					}
					var min = this.minInput.val();
					if (min != null) {
						min = parseInt(min,10);
						if (!$.argumentutils.isNumber(min) || min < 0 || min >= 60)
							min = "00";
						else if (min < 10)
							min = "0" + min;
					} else {
						min = "00";
					}
					var sec = this.secInput.val();
					if (sec != null) {
						sec = parseInt(sec,10);
						if (!$.argumentutils.isNumber(sec) || sec < 0 || sec >= 60)
							sec = "00";
						else if (sec < 10)
							sec = "0" + sec;
					} else {
						sec = "00";
					}
					this._trigger("onclick",null,tmpYear + "-" + tmpMonth + "-" + tmpDay + " " + hour + ":" + min + ":" + sec);
					this.hourInput.val(hour);
					this.minInput.val(min);
					this.secInput.val(sec);
				} else {
					var d = new Date();
					hour = d.getHours();
					min = d.getMinutes();
					sec = d.getSeconds();
					this._trigger("onclick",null,tmpYear + "-" + tmpMonth + "-" + tmpDay + " " + hour + ":" + min + ":" + sec);
				}
			}
			
			if (!this.options.isState) {
				this.hide();
			}
		},
		/**
		 * 隐藏日历控件
		 */
		hide : function() {
			this.element.css({left:'0px',top:'0px'}).hide();
			if (this.timeBar)
				this.timeBar.hide();
		},
		/**
		 * 重置日历控件
		 */
		resetCalendar : function() {
			this.div_Calendar.empty();
			this.yearInput.val(this.year);
			this.monthInput.val(this.month<10?("0"+this.month):this.month);
			
			var tmpDate = new Date(this.year, (this.month) - 1);
			this.currDay = tmpDate.getDay();
			if (this.currDay == 0) {
				this.currDay = 7;
			}

			if (isRunNian(this.year)) {
				MONTH_DAY_COUNT[1] = 29;
			} else {
				MONTH_DAY_COUNT[1] = 28;
			}

			var day = 0;

			if(this.hiddenDayBar){
				this.realHeight = 90;
				this.element.css('height',this.realHeight + "px");
			}else{
				var dayLine = 0;
				if(((MONTH_DAY_COUNT[(this.month) - 1]) == 31 && tmpDate.getDay() >= 5) || ((MONTH_DAY_COUNT[(this.month) - 1]) == 30 && tmpDate.getDay() >= 6)){//6行
					dayLine = 6;
				}else if(((MONTH_DAY_COUNT[(this.month) - 1]) == 28 && tmpDate.getDay() == 0)){//4行
					dayLine = 4;
				} else {//5行
					dayLine = 5;
				}
				if (this.showTimeBar){
					this.realHeight = this.opBar.outerHeight() + this.weekBar.outerHeight() + dayLine * 31 + (this.timeBar ? this.timeBar.outerHeight():0);
					this.element.css('height',this.realHeight + "px");
				}else{
					this.realHeight = this.opBar.outerHeight() + this.weekBar.outerHeight() + dayLine * 31;
					this.element.css('height',this.realHeight + "px");
				}
			}
			
			if (this.timeBar)
				this.timeBar.css('top',(this.element.outerHeight() - 75) + "px");

			var oThis = this;
			for (s = 0; s < 6; s++) {
				for (j = 0; j < 7; j++) {
					day = 7 * s + j - this.currDay + 1;
					
					var _top = '';
					if(7 - this.currDay > 0){
						_top = (s * 24)+7 + "px";	
					}else{
						_top = ((s-1) * 24)+7 + "px";
					}
					var dayCell = $("<div></div>").addClass('calendar_day_cell').attr({
						'id' : 'dayCell'
					}).css({
						'left' : (j * 30)+7 + "px",
						'top' : _top
					});
					
					if (isWeekEnd(j)) {
						dayCell.addClass('calendar_rest_day_cell');
					}

					if (day > 0 && (day <= MONTH_DAY_COUNT[(this.month) - 1])) {
						this.div_Calendar.append(dayCell);
						dayCell.html(day).on('click',function(e){
							if (oThis.options.isState) {
								if (oThis.oldDayCell) {
									$(oThis.oldDayCell).css('color',oThis.oldDayCellColor);
								}
								oThis.oldDayCell = $(this);
								oThis.oldDayCellColor = $(this).css('color');
								$(this).css('color','yellow');
							}
							if (oThis.showTimeBar){
								oThis.currentDayCell.removeClass('calendar_current_day_cell');
								oThis.currentDayCell = $(this);
								oThis.currentDayCell.addClass('calendar_current_day_cell');
								oThis.day = $(this).text();
								if (typeof calendarChange != "undefined"){
									calendarChange.call(this, oThis);
								}
							}
							else{
								oThis.onClick($(this).text());
							}
							e.stopPropagation();
						});
					}
					if (this.dateObject == null)
						this.dateObject = currDate; 
					if ((day == (this.dateObject.getDate()))
							&& (this.month == (this.dateObject.getMonth() + 1))
							&& (this.year == (this.dateObject.getFullYear()))) {
						dayCell.addClass('calendar_current_day_cell');
						this.currentDayCell = dayCell; 
					}
				}
				if (day >= MONTH_DAY_COUNT[(this.month) - 1]) {
					break;
				}
			}
			
			var top = this.element.get(0).offsetTop;
			if(top > 0){
				//超过下边界
				if (top + this.realHeight > document.body.clientHeight) {
					if((top - 26) > this.realHeight){
						//在日期框上面显示
						top = top - 24 - this.realHeight-4;
					}else{
						top = document.body.clientHeight - this.realHeight-4;
					}
					this.element.offset({top: top});
				}
			}
			var left = this.element.get(0).offsetLeft;
			if(left > 0){
				//超过右边界
				if (left + this.element.outerWidth() > document.body.clientWidth){
					left = document.body.clientWidth - this.element.outerWidth();
				}
				this.element.offset({left: left});
			}
		},
		getZIndex : function() {
			return this.element.css("z-index");
		},
		setZIndex : function(zIndex) {
			this.element.css("z-index",zIndex);
		}
		
	});
})(jQuery);

/**
 * 判断是否是润年
 * @private
 */
function isRunNian(year) {
	return (((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) ? true
			: false);
};

/**
 * 判断是否是星期六和星期日
 * @private
 */
function isWeekEnd(day) {
	return (((day == 0) || (day == 6)) ? true : false);
};

