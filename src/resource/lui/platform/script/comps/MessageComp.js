/**
 * 提示信息控件
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 提示信息控件构造方法
	 * 
	 * @class 提示信息控件
	 * 
	 * @param name 名称
	 * 
	 * @param left
	 * @param top
	 * @param width 宽度
	 * @param height 高度
	 * @param text 显示文字
	 * @param showPosition 显示位置类型 ("top-left","top-center","top-right","center","bottom-left","bottom-center","bottom-right")
	 * @param x 横向偏差
	 * @param y 纵向偏差
	 * @param className 类名
	 */
	$.widget("lui.message", $.lui.base, {
		options : {
			text : '',
			showPosition : '',
			x : null,
			y : null,
			className : 'message_div'
		},
		_initParam : function() {
			this.componentType = "MESSAGE";
			this.parentOwner = this.element.parent()[0];
			this.DEFAULT_X = 30;
			this.DEFAULT_Y = 30;
			this.HEIGHT = 180;
			this.WIDTH = 350;
			
			this._super();
			var height = $.measures.getCssHeight(this.options.className + "_DEFAULT_HEIGHT");
			width = $.measures.getCssHeight(this.options.className + "_DEFAULT_WIDTH");
		},
		_create : function() {
			var oThis = this,ele = this.element,opts = this.options;
		    var frmstr = '<iframe src="" style="position:absolute; visibility:inherit; top:0px; left:0px; width:100%;height:100%; z-index:-1; border:none;" frameborder="0"></iframe>';
			ele.attr.css({
				'z-index' : $.measures.getZIndex()
			}).appendTo("body").html(frmstr).hide();

			this.messageDiv = $("<div></div>").addClass(opts.className).appendTo(ele);
//			if($.browsersupport.IS_IE7)
//				this.messageDiv.css('left','0px');
			// 设置显示大小和位置
			this.setStyle();

			// 显示文字
			this.textDiv = $("<div></div>").addClass('message_text_div').html(opts.text);
			if (opts.text != null) {
				this.messageDiv.append(this.textDiv);
			}

			// 关闭按钮
			this.closeImg = $("<div></div>").addClass('message_close_img').on('click',function(e){
				oThis.hideMsg();
			}).on('mouseover',function(){
				$(this).attr('class','message_close_img_over');
			}).on('mouseout',function(){
				$(this).attr('class','message_close_img');
			}).appendTo(this.messageDiv);

			this.bottomMsg = $("<div></div>").addClass('bottom_msg_div').appendTo(this.messageDiv);
			this.bottomTime = $("<span></span>").html("3").appendTo(this.bottomMsg);
			this.bottomText = $("<span></span>").html($.argumentutils.getString(trans("ml_messagecomp_tip1"),"秒钟后自动消失")).appendTo(this.bottomMsg);

		},
		/**
		 * 设置显示大小和位置
		 * @private
		 */
		setStyle : function() {
			var oThis = this, opts = this.options, ele = this.element;
			ele.attr('class',opts.className).css({
				'filter' : 'alpha(opacity=0)'
			});
//			if (!$.browsersupport.IS_IE)
				ele.css('height',opts.height);
			var left = 0;
			var top = 0;
			// 显示位置
			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = ele.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
			var tempHeight = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempHeight = ele.outerWidth();
			else
				tempHeight = $.argumentutils.getInteger(parseInt(opts.height), 120);
			
			if (opts.showPosition == "top-left") {
				left = this.DEFAULT_X + (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x));
				top = this.DEFAULT_Y + (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			} else if (opts.showPosition == "top-center") {
				left = (screen.width - tempWidth - (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x)))/2;
				top = this.DEFAULT_Y + (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			} else if (opts.showPosition == "top-right") {
				left = screen.width - tempWidth - this.DEFAULT_X - (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x));
				top = this.DEFAULT_Y + (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			} else if (opts.showPosition == "center") {
				left = (screen.width - tempWidth - (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x)))/2;
				top = (screen.height - tempHeight - this.DEFAULT_Y - (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y)))/2;
			} else if (opts.showPosition == "bottom-left") {
				left = this.DEFAULT_X + (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x));
				top = screen.height - tempHeight - this.DEFAULT_Y - (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			} else if (opts.showPosition == "bottom-center") {
				left = (screen.width - tempWidth - (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x)))/2;
				top = screen.height - tempHeight - this.DEFAULT_Y - (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			} else { // bottom-right
				left = screen.width - tempWidth - this.DEFAULT_X - (opts.x == null ? 0 : $.argumentutils.getInteger(opts.x));
				top = screen.height - tempHeight - this.DEFAULT_Y - (opts.y == null ? 0 : $.argumentutils.getInteger(opts.y));
			}
			ele.css({
				'left' : left + "px",
				'top' : top + "px"
			});
		},
		/**
		 * 设置显示文字
		 * @private
		 */
		setText : function(text) {
			this.options.text = text;
			if (text != null)
				this.textDiv.html(this.options.text);
		},
		/**
		 * 用setTimeout循环减少透明度
		 * @private
		 */
		changeOpacity : function(startValue, endValue, step, speed) {
			var oThis = this;
			if (this.element.css('display') == "none") {
				this.element.show();
			}
			if (startValue <= 0) {  // 隐藏控件
				this.bottomTime.html('0');
				this.element.hide();
				return;
			}else if(startValue <= 40){
				this.bottomTime.html('1');
			}else if(startValue <= 70){
				this.bottomTime.html('2');
			}
			setOpacity(this.element[0], startValue);
			if (this.timeoutFunc)
				clearTimeout(this.timeoutFunc);
			this.timeoutFunc = setTimeout( function() {
				oThis.changeOpacity(startValue - step, endValue, step, speed);
			}, speed);
		},
		/**
		 * 显示提示信息（先显示后隐藏）
		 * @private
		 */
		showMsg : function() {
			this.element.css('z-index',$.measures.getZIndex());
			this.step = 5;
			this.speed = 150;
			this.bottomTime.html('3');
			this.changeOpacity(100, 0, this.step, this.speed);
		},
		/**
		 * 直接隐藏提示信息
		 * @private
		 */
		hideMsg : function() {
			if (this.timeoutFunc)
				clearTimeout(this.timeoutFunc);
			this.element.hide();
		},
		/**
		 * @private
		 */
		getContext : function() {
			return {
				c : "",
				id : this.element.attr("id")
			};
		},
		/**
		 * @private
		 */
		setContext : function(context) {
		}
	});
	
	$.messageComp = {
		/**
		 * 显示提示信息（外部调用）
		 */
		"showMessage" : function(showPosition, text, x, y, className) {
			var width = $.measures.getTextWidth(text,className);
			width = width + 100;
			if (!window.$_messageComp) {
				window.$_messageComp = $("<div id=\""+name+"\"></div>").message({
					width : width,
					text : text,
					showPosition : showPosition,
					x : x,
					y : y,
					className : className
				}).message("instance");
			} else {
				window.$_messageComp.option.showPosition = showPosition;
				//window.$_messageComp.width = width;
				window.$_messageComp.option.x = x;
				window.$_messageComp.option.y = y;
				window.$_messageComp.option.className = $.argumentutils.getString(className, window.$_messageComp.className);
				window.$_messageComp.setStyle();
				window.$_messageComp.setText(text);
			}
			window.$_messageComp.showMsg();
		}
	};
})(jQuery);

/**
 * 设置透明度
 * @private
 */
function setOpacity(obj, value) {
	if (document.all) {
		if (value == 100) {
			$(obj).css('filter','');
		} else {
			$(obj).css('filter',"alpha(opacity=" + value + ")");
		}
	} else {
		$(obj).css('opacity',value / 100);
	}
};
