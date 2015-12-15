/**
 *@fileoverview 分割面板控件,将布局分为div1和div2两部分.
 * 
 * @author lxl
 * @version lui 1.0
 *
 **/
(function($) {
	/**
	 * 分割面板构造函数
	 * @class 分割面板控件，将布局分为prop和center两部分。
	 */
	$.widget("lui.dividlayout", $.lui.base, {
		options : {
			position : 'absolute',
			prop : 200,
			animate : true,						// 动画效果
			orientation : 'h',    				// 分割方向
			isInverse : false,					// 是否反向设置分割大小
			className : 'dividlayout_div'
		},
		_initParam : function() {
			this.componentType = "DIVIDLAYOUT";
			this.EXPAND_SPEED = 200;
			this.isExpand = true;
			this.inverse = this.options.isInverse ? "_inverse" : "";
		    this._super();
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element,oThis = this;
			ele.addClass(opts.className);
			
			this.propDiv = $("<div>").addClass("prop_div_" + opts.orientation + this.inverse).appendTo(ele);
			this.spliterDiv = $("<div>").addClass("spliter_" + opts.orientation + this.inverse).appendTo(ele);
			this.centerDiv = $("<div>").addClass("center_div_" + opts.orientation + this.inverse).appendTo(ele);
			
			this.spliterBtn = $("<div>").addClass("spliter_btn_" + opts.orientation + this.inverse)
				.appendTo(this.spliterDiv)
				.on('click',function() {
				if(oThis.isExpand) {
					oThis.setProp(0,oThis.EXPAND_SPEED);
				} else {
					oThis.setProp(opts.prop,oThis.EXPAND_SPEED);
				}
				oThis.isExpand = !oThis.isExpand;
			});
			
			$(window).on('resize',function(){oThis.setProp((oThis.isExpand?opts.prop:0),0);});
			this.setProp(opts.prop,0);
		},
		setProp : function(prop,animateSpeed,changeOptions) {
			var opts = this.options,ele = this.element,oThis = this;
			if(changeOptions) {
				opts.prop = prop;
			}
			if(opts.orientation === 'h') {
				var ceneterWidth = ele.parent().outerWidth() - this.spliterDiv.outerWidth() - prop;
				if(animateSpeed == 0) {
					this.propDiv.css({'width':prop+"px"});
					this.centerDiv.css({'width':ceneterWidth+"px"});
				} else {
					this.propDiv.animate({'width':prop+"px"},animateSpeed);
					this.centerDiv.animate({'width':ceneterWidth+"px"},{
						queue:false, 
						duration:animateSpeed,
						complete:function(){
							$(window).triggerHandler("resize");
						}
					});
				}
			} else {
				this.propDiv.animate({'height':prop+"px"},animateSpeed);
				var ceneterHeight = ele.parent().outerHeight() - this.spliterDiv.outerHeight() - prop;
				this.centerDiv.animate({'height':ceneterHeight+"px"},animateSpeed);
			}
		},
		setPropDiv : function(propId) {
			this.propDiv.empty().append($("#"+propId).show());
		},
		setCenterDiv : function(centerId) {
			this.centerDiv.empty().append($("#"+centerId).show());
		}
	});
})(jQuery);
