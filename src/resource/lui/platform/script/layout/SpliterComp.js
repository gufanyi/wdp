/**
 *@fileoverview 分割面板控件,将布局分为div1和div2两部分.
 * 
 * @author lxl
 * @version lui 1.0
 *
 **/
var timeoutID;
(function($) {
	/**
	 * 分割面板构造函数
	 * @class 分割面板控件，将布局分为div1和div2两部分。
	 */
	$.widget("lui.spliter", $.lui.base, {
		options : {
			position : 'absolute',
			prop : 100,
			orientation : 'h',    				// 分割方向
			oneTouch : false,					// 是否一键缩进
			boundMode : '%',					// 边界限定模式。默认是%方式.也可是设定为绝对值方式。boundMode值为"px"
			miniProp : 0,
			maxiProp : 1,
			hideDirection : true,				// 点击分隔条时，分隔栏向哪边隐藏。true为左或上。  false为右或下
			spliterWidth : 1,
			hideBar : true,						// 默认是否隐藏拖动bar
			isInverse : false,					// 是否反向设置分割大小
			isRunMode : false,					// 是否运行态
			isInverseFlowPanel : false,			// 是否反向显示悬浮面板
			className : '',
			//event
			onresizediv1 : null,
			onresizediv2 : null
		},
		_initParam : function() {
			this.componentType = "SPLITER";
		    this._super();
		    this.showDragImg = false;
		    this.overflow = "hidden";
		    
	        if (this.options.spliterWidth) this.options.spliterWidth = this.options.spliterWidth + "px";
	        this.options.hideBar = $.argumentutils.getBoolean(this.options.hideBar, !this.options.oneTouch);

		    if (this.options.boundMode == "%") this.options.prop = $.argumentutils.getFloat(this.options.prop, 0.5);
		    else this.options.prop = $.argumentutils.getInteger(this.options.prop, 100);
		    this.redoit = false;
		    // 悬浮层显示位置
		    this.showPos = this.options.isInverseFlowPanel ? " left_pos": " right_pos";
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element,oThis = this;
		    ele.addClass('spliter_div').css({
		        'top' : opts.top + "px",
		        'left' : opts.left + "px",
		        'width' : opts.width,
		        'height' : opts.height,
		        'position' : opts.position
		    });
		    
		    $(window).on('resize',function(){
		    	oThis.fixProp(opts.prop, opts.isInverse);
		    });
//		    addResizeEvent(ele[0],
//		    function() {
//		        oThis.fixProp(opts.prop, opts.isInverse);
//		    });

		    //创建div1
		    this.div1 = $("<div></div>").addClass('spliter_div1').appendTo(ele);
		    this.div1.owner = this;
		    this.div1.add = function(obj) {
		    	oThis.div1.append($(obj).show());
		    };

		    //创建div2
		    this.div2 = $("<div></div>").addClass('spliter_div2').attr({
		        'id' : 'spliter_div2'
		    }).appendTo(ele);
		    this.div2.owner = this;

		    this.div2.add = function(obj) {
		    	oThis.div2.append($(obj).show());
		    };

		    this.divBar = $("<div></div>").addClass("spliter_bar_" + opts.orientation + (opts.hideBar ? "_hide": "")).attr({
		        'id' : ele.attr("id") + "_bar"
		    }).appendTo(ele);

		    if (opts.orientation == 'h') {
		        this.divBar.css('min-height','25px');
		    }

             this.divBar.on('mouseout',function(){
                if (opts.oneTouch)
                    $(this).attr('class','spliter_bar_onetouch');
                else
                    $(this).attr('class',"spliter_bar_" + opts.orientation + (opts.hideBar ? "_hide": ""));
                $(this).css('cursor','default');
            }).on('mouseover',function(){
                if (opts.oneTouch)
                    $(this).attr('class','spliter_bar_onetouch');
                else
                  $(this).attr('class',"spliter_bar_" + opts.orientation);
                if (opts.orientation == 'h')
                    $(this).css('cursor','e-resize');
                else
                    $(this).css('cursor','s-resize');
            }).draggable({
            	axis : opts.orientation == 'h'?'x':'y',
            	drag : function() {
            		if (opts.orientation == 'h') {
		                if (opts.boundMode == '%') oThis.fixProp((oThis.divBar.position().left + (oThis.divBar.outerWidth() / 2)) / ele.outerWidth());
		                else oThis.fixProp(oThis.divBar.position().left);
		            } else {
		                if (oThis.boundMode == '%') oThis.fixProp((oThis.divBar.position().top + (oThis.divBar.outerHeight() / 2)) / ele.outerHeight());
		                else oThis.fixProp(oThis.divBar.position().top);
		            }
            	}
            });

		    if (opts.oneTouch) {
		    	if (opts.orientation == 'h') {
			        this.divBar.css('height','100%');
			        if (opts.spliterWidth)
			            this.divBar.css('width',opts.spliterWidth);
			        else
			            this.divBar.css('width',this.divBar.css('width'));
			        this.div1.css('height','100%');
			        this.div2.css('height','100%');
		    	} else {
		    		this.divBar.css('width','100%');
			        if (opts.spliterWidth)
			            this.divBar.css('height',opts.spliterWidth);
			        else
			            this.divBar.css('height',this.divBar.css('width'));
			        this.div1.css('width','100%');
			        this.div2.css('width','100%');
		    	}

		    }

		    //不显示拖动条
		    else {
		        this.divBar.append($("<div></div>"));
		        if (opts.orientation == 'v') {
		            this.divBar.css('width','100%');
		            if (opts.spliterWidth)
		                this.divBar.css('height',opts.spliterWidth);
		            else
		                this.divBar.css('height',this.divBar.css('height'));
		            this.div1.css('height','100%');
		            this.div2.css('height','100%');
		        } else {
		            this.divBar.css('height','100%');
		            if (opts.spliterWidth)
		                this.divBar.css('height',opts.spliterWidth);
		            else
		                this.divBar.css('width',this.divBar.css('width'));
		            this.div1.css('height','100%');
		            this.div2.css('height','100%');
		        }

		        if (opts.isRunMode) {
		            this.divBar.hide();
		            this.div1.css('width','100%');
		            this.div2.data('placeIn',0);

		            this.div2.attr('class',"spliter_flow_div" + this.showPos);
		            if (opts.prop && opts.prop > 220) {
		                this.div2.css('width',opts.prop + "px");
		            }
		            if ($.pageutils.getCookie("SPLIT_ON_TOUCH")) {
		                this.div2.css({
		                    'visibility' : 'hidden',
		                    'z-index' : '-99999'
		                });
		            }
		            this.div2.css('z-index',$.measures.getZIndex());
		            //阴影
		            this.shadowDiv = $("<div></div>").addClass('spliter_shadow_div').appendTo(this.div2);
		            //关闭按钮
		            this.closeBtn = $("<div></div>").addClass('spliter_btn_div').attr({
		                'id' : 'spliterCloseBtn'
		            }).appendTo(this.div2);
		            //底部提示信息
//		            this.showMsgDiv = $("<div></div>").addClass('spliter_show_msg_div');
//		            this.showMsgCk = $("<input type=\"checkbox\"/>").css({
//		                'margin-top' : '10px'
//		            });
//		            if ($.pageutils.getCookie("SPLIT_ON_TOUCH")) {
//		                this.showMsgCk.val('1');
//		                this.showMsgCk.attr('checked',true);
//		            } else {
//		                this.showMsgCk.val('0');
//		            }
//		            this.showMsgDiv.append(this.showMsgCk);
//		            this.showMsgText = $("<div></div>")
//		                .addClass('spliter_show_msg_text_div')
//		                .html(trans('ml_splitercomp_msg'))
//		                .appendTo(this.showMsgDiv);
//		            this.div2.append(this.showMsgDiv);
		            //感应区
		            this.divFeel = $("<div></div>").addClass("spliter_feel_div" + this.showPos).attr({
		                'id' : 'spliterFeelDiv'
		            }).appendTo(ele).hide();
		            this.divFeel.css({
		                'z-index' : $.measures.getZIndex()
		            });
		            this.divBtn = $("<div></div>").addClass("spliter_btn" + (opts.isInverseFlowPanel ? "_left_pos": "_right_pos")).attr({
		                'id' : 'spliterDivBtn'
		            }).css({
		                'z-index' : $.measures.getZIndex()
		            }).appendTo(ele).hide();

		            this.closeBtn.on('click',function(e){
		            	var _this = this;
		                $("#spliter_div2").data('placeIn',"0");
		                if ($("#spliter_div2").data('placeIn') == "0") {
		                    $("#spliter_div2").animate({
		                    	width : 0
		                    },200);
		                    $(_this).hide();
		                    $("#spliterDivBtn").data('placeIn',"1");
		                    $("#spliterDivBtn").show();
		                    blink();
		                    $("#spliterFeelDiv").show().triggerHandler('mouseout');
		                }
		            });
//		            this.showMsgCk.on('click',function(e){
//		                if (oThis.showMsgCk.val() == "0") {
//		                    var date = new Date();
//		                    var ms = 365 * 3600 * 1000 * 24;
//		                    date.setTime(date.getTime() + ms);
//		                    setCookie("SPLIT_ON_TOUCH", "1", date);
//		                    oThis.showMsgCk.val('1');
//		                    oThis.closeBtn.triggerHandler('click');
//		                } else {
//		                    $.pageutils.deleteCookie("SPLIT_ON_TOUCH");
//		                    oThis.showMsgCk.val('0');
//		                }
//		            });
		            this.divFeel.on('mouseout',function(e){
		                $("#spliterDivBtn").data('placeIn',"0");
		                if (this.timeoutID) {
		                    window.clearTimeout(this.timeoutID);
		                }
		                this.timeoutID = window.setTimeout(function() {
	                        if ($("#spliterDivBtn").data('placeIn') == "0") {
	                            $("#spliterDivBtn").hide();
	                        }
	                    },
	                    3000);
//		                clearEventSimply(e);
		            }).on('mouseover',function(e){
		                $("#spliterDivBtn").data('placeIn',"1");
		                $("#spliterDivBtn").show();
		                blink();
		                if (this.timeoutID) {
		                    window.clearTimeout(this.timeoutID);
		                }
//		                clearEventSimply(e);
		            });

		            this.divBtn.on('mouseover',function(e){
		                $("#spliterDivBtn").data('placeIn',"1");
		                $("#spliterDivBtn").show();
		                window.clearTimeout(timeoutID);
//		                clearEventSimply(e);
		            }).on('mouseout',function(e){
		                blink();
		                $("#spliterFeelDiv").triggerHandler('mouseout');
//		                clearEventSimply(e);
		            }).on('click',function(e){
		                $("#spliterDivBtn").data('placeIn',"0");
		                $("#spliterDivBtn").hide();
		                $("#spliterFeelDiv").hide();
		                $("#spliter_div2").data('placeIn',"1");
		                $('#spliterCloseBtn').show();
		                $("#spliter_div2").css({
		                    'z-index' : '99999'
		                }).animate({
	                    	width : opts.prop + 'px'
	                    },200);
//		                clearEventSimply(e);
		            });

		            this.div2.on('mouseout',function(e){
		                $(this).data('placeIn',"0");
		                if (this.timeoutID) {
		                    window.clearTimeout(this.timeoutID);
		                }
		                this.timeoutID = window.setTimeout(function(e) {
	                        if ($("#spliter_div2").data('placeIn') == "0") {
	                            $("#spliter_div2").animate({
			                    	width : 0
			                    },200);
	                            $("#spliterDivBtn").data('placeIn',"1");
	                            $('#spliterCloseBtn').hide();
	                            $("#spliterDivBtn").show();
	                            blink();
	                            $("#spliterFeelDiv").show();
	                            $("#spliterFeelDiv").triggerHandler('mouseout');
	                        }
	                    },
	                    3000);
		            }).on('mouseover',function(e){
		                $(this).data('placeIn',"1");
		                $(this).css({
		                    'z-index' : '99999'
		                }).show();
		                if (this.timeoutID) {
		                    window.clearTimeout(this.timeoutID);
		                }
		            });
		            window.setTimeout(function(e) {
		                if ($("#spliter_div2").data('placeIn') == "0") {
		                    $("#spliter_div2").animate({
		                    	width : 0
		                    },200);
		                    $('#spliterCloseBtn').hide();
		                    $("#spliterDivBtn").data('placeIn',"1");
		                    $("#spliterDivBtn").show();
		                    blink();
		                    $("#spliterFeelDiv").show();
		                    $("#spliterFeelDiv").triggerHandler('mouseout');
		                }
		            },
		            5000);
		        }
		    }

		    //将分割位置转换为百分比形式
		    //this.changeToProp();
		    this.fixProp();
		    
//		    if (!$.browsersupport.IS_IE) { // 如果是firefox浏览器，增加window大小改变后事件
		        // var oSilderComp = this;
		        // EventUtil.addEventHandler(window, "resize",
		        // function() {
		            // $.spliterComp.spliterCompResize(oSilderComp);
		        // });
//		    }
		},
		fixProp : function(prop, isInverse) {
			var opts = this.options, ele = this.element;
		    if (!opts.oneTouch && opts.isRunMode) {
		        return;
		    }
		    var propNum = $.argumentutils.truncFloat(Math.abs(opts.prop - prop), 2);
		    if (propNum <= 0.01 && opts.isInverse == isInverse) {
		        if (ele[0].oldWidth == ele.outerWidth() && ele[0].oldHeight == ele.outerHeight()) {
		            return;
		        } else {
		            ele[0].oldWidth = ele.outerWidth();
		            ele[0].oldHeight = ele.outerHeight();
		        }
		    }
		    try {
		        if (prop != null && propNum > 0.01) {
		        	opts.prop = prop;
		            if (isInverse != null && isInverse == true) opts.isInverse = true;
		            else opts.isInverse = false;
		        }

		        var nowProp = opts.prop;
		        if (opts.boundMode == '%') {
		            if (opts.isInverse) nowProp = (1 - nowProp);
		            if (nowProp < opts.miniProp) nowProp = opts.miniProp;
		            if (nowProp > opts.maxiProp) nowProp = opts.maxiProp;
		        } else if (opts.isInverse) {
		            if (opts.orientation == 'v') nowProp = ele.outerHeight() - nowProp;
		            else nowProp = ele.outerWidth() - nowProp;
		        }
		        if (opts.orientation == 'v') {
		            var limit = Math.max(0, ele.outerHeight() - this.divBar.outerHeight() - 1);
		            var firstHeight;
		            if (opts.boundMode == '%') firstHeight = nowProp * (ele.outerHeight());
		            else firstHeight = nowProp;
		            this.div1.css('height',Math.floor(Math.min(firstHeight, limit)) + "px");
		            this.divBar.css({
		                'top' : parseFloat(this.div1.css('height')) + "px",
		                'left' : '0px'
		            });
		            this.div2.css({
		                'top' : parseFloat(this.divBar.css('top')) + parseFloat(this.divBar.outerHeight()) + "px",
		                'left' : '0px'
		            });
		            var secondHeight = ele.outerHeight() - parseFloat(this.div1.css('height')) - parseFloat(this.divBar.outerHeight());
		            this.div2.css('height',Math.floor(Math.max(0, secondHeight)) + 'px');
		        } else {
		            var limit = Math.max(0, ele.outerWidth() - this.divBar.outerWidth());
		            var firstWidth;
		            if (opts.boundMode == '%') firstWidth = nowProp * (ele.outerWidth());
		            else firstWidth = nowProp;
		            this.div1.css('width',Math.min(firstWidth, limit) + "px");
		            this.divBar.css('left',parseFloat(this.div1.css('width')) + "px");
		            this.div2.css('left',parseFloat(this.divBar.css('left')) + parseFloat(this.divBar.outerWidth()) + "px");
		            var secondWidth = ele.outerWidth() - parseFloat(this.div1.outerWidth()) - parseFloat(this.divBar.outerWidth());
		            this.div2.css('width',Math.max(0, secondWidth) + 'px');
		        }

		        this._trigger("onresizediv2",null,{
		        	oldH : this.oldHeightDiv2,
		        	oldW : this.oldWidthDiv2,
		        	newH : this.div2.outerHeight(),
		        	newW : this.div2.outerWidth()
		        });
		        this._trigger("onresizediv1",null,{
		        	oldH : this.oldHeightDiv1,
		        	oldW : this.oldWidthDiv1,
		        	newH : this.div1.outerHeight(),
		        	newW : this.div1.outerWidth()
		        });
		    } catch(e) {}

//		    if (!$.browsersupport.IS_IE) {
		        // 重新设置页面布局高度
		    $(window).triggerHandler("resize");
//	    }
		},
		/**
		 * 获取第一个DIV
		 */
		getDiv1 : function() {
		    return this.div1;
		},
		/**
		 * 获取第二个DIV
		 */
		getDiv2 : function() {
		    return this.div2;
		}
	});
	
	$.spliterComp = {
		/**
		 * 组件大小改变后，相应改变内部容器div高度
		 * @private
		 */
		spliterCompResize : function(oSilderComp) {
		    oSilderComp.fixProp();
		}
	};
})(jQuery);

function blink() {
//    if (timeoutID) {
//        window.clearTimeout(timeoutID);
//    }
//    if ($("#spliterDivBtn").css('display') == "" || $("#spliterDivBtn").css('display') == "block") {
//       $("#spliterDivBtn").css('visibility',($("#spliterDivBtn").css('visibility')) ? "visible": "hidden");
//        timeoutID = window.setTimeout("blink()", 500);
//    }
};
