/**
 * 
 * @fileoverview 加载提示条
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 加载提示工具条控件构造函数
	 * @class 加载提示工具条控件
	 * 
	 * @param align 横向对齐方式（center, left, right）
	 * @param valign 纵向对齐方式（center, top, bottom）
	 * @param zIndex 显示层级
	 */
	$.widget("lui.loading", $.lui.base, {
		options : {
			position : 'absolute',
			align : '',
			valign : '',
			zIndex : null,
			className : 'panel_div_alpha',
			fixed : false
		},
		_initParam : function() {
			this.componentType = "LOADING";
			this.parentOwner = this.element.parent()[0];
			this._super();
			this.overflow = "hidden";
			this.options.zIndex = $.measures.getZIndex();
			this.innerHTML = null;
			this.visible = false;
		},
		_create : function() {
			this._initParam();
			var opts = this.options,ele = this.element;
			if (opts.fixed == true){
				this.positionElement();
				ele.css({
					'width' : opts.width,
					'height' : opts.height
				});
			}
			else{
				ele.css({
					'left' : '0px',
					'top' : '0px',
					'width' : '100%',
					'height' : '100%'
				});
			}
			ele.addClass(opts.className).css({
				'position' : opts.position,
				'overflow' : this.overflow,
				'z-index' : opts.zIndex
			}).hide();
			
			if (!ele.parent().is("body")) {
				ele.css('z-index',ele.parent().css("z-index")+1);
			}
			this.imgDiv = $("<div></div>").css({
				'width' : '100%',
				'height' : '100%'
			}).appendTo(ele);
			
			if (this.innerHTML != null) {  // 用户自定义显示内容
				ele.html(this.innerHTML);
			}
		},
		/**
		 * 计算显示位置
		 * @private
		 */
		positionElement : function() {
			var opts = this.options,ele = this.element;
			if (opts.align != null && opts.align != "") {
				var parentWidth = 0;
				if (ele.parent().is("body")) {
					parentWidth = document.body.clientWidth;
				} else {
					parentWidth = ele.parent().outerWidth();
				}
				var left = 0;
				var eleWidth = opts.width;
				if ($.argumentutils.isPercent(opts.width)) {
					left = 0;
				} else if (parentWidth <= parseInt(opts.width))
					left = 0;
				else {
					if (opts.align == "center") {
						left = (parentWidth - parseInt(opts.width)) / 2;
						left = parseInt(left);
					} else if (opts.align == "left") {
						left = 0;
					} else if (opts.align == "right") {
						left = parentWidth - parseInt(opts.width);
					}
						
				}
				ele.css('left' , left + "px");
			}
			if (opts.valign != null && opts.valign != "") {
				var parentHeight = 0;
				if (ele.parent().is("body")) {
					parentHeight = document.body.clientHeight;
				} else {
					parentHeight = ele.outerHeight();
				}
				var top = 0;
				var eleHeight = opts.height;
				if ($.argumentutils.isPercent(opts.height)) {
					top = 0;
				} else if (parentHeight <= parseInt(opts.height))
					top = 0;
				else {
					if (opts.align == "center") {
						top = (parentHeight - parseInt(opts.height)) / 2;
						top = parseInt(top);
					} else if (opts.align == "top") {
						
					} 
				}
				ele.css('top' , top + "px");
			}
			if (opts.align == null || opts.align == "") {
				ele.css('left' , opts.left + "px");
			}
			if (opts.valign == null || opts.valign == "") {
				ele.css('top' , opts.top + "px");
			}
		},
		/**
		 * 设置自定义内容
		 */
		setInnerHTML : function(innerHTML) {
			if (innerHTML != null) {
				this.innerHTML = innerHTML;
				this.imgDiv.html(innerHTML);
			}
		},
		/**
		 * 显示加载提示条
		 */
		show : function() {
			this.element.show();
			this.imgDiv.show();
			this.visible = true;
		},
		/**
		 * 隐藏加载提示条
		 */
		hide : function() {
			this.element.hide();
			this.imgDiv.hide();
			this.visible = false;
		},
		showDivgen : function() {
			 if (window.showLoadingBar != true) return;	
			 this.element.show();
		}
	});
})(jQuery);

/**
 * 设置页面默认的加载提示条
 * @return
 */
function initDefaultLoadingBar(left, top, width, height, align, valign, zIndex, innerHTML) {
	window.loadingBar = $("<div id=\"__loadingBar\"></div>").loading({
		left : left,
		top :top,
		width : width,
		height : height,
		align : align,
		valign : valign,
		zIndex : zIndex
	}).appendTo("body").loading("instance");
	window.loadingBar.setInnerHTML(innerHTML);
};

/**
 * 显示页面默认的加载提示条
 */
function showDefaultLoadingBar() {
	if(window.editMode)
		return;
	if (window.loadingBar == null) {
		var imgName = 'loading.gif';
		var imgSrc = window.themePath + "/comps/loading/images/" + imgName;
		var innerHTML = "<table width=100% height=100%><tr><td align='center'><img class='panel_vertical_middle_div' src='" + imgSrc + "'/></td></tr></table>";
		window.loadingBar = $("<div id=\"__loadingBar\"></div>").appendTo("body").loading({
			left : 0,
			top : 0,
			width: 39,
			height : 39,
			align : 'center',
			valign : 'center',
			fixed : true
		}).loading("instance");
		window.loadingBar.setInnerHTML(innerHTML);
		var bgImgSrc = window.themePath + "/global/images/transparent.gif";
		window.loadingBar.element.css("background-image","url("+ bgImgSrc +")");
	}
	if (window.showLoadingBarTimeOutFunc)
		clearTimeout(window.showLoadingBarTimeOutFunc);
//	if ($.browsersupport.IS_IE){
//		window.showLoadingBar = true;
//		window.loadingBar.showDivgen();
//	}	
//	else	
	window.loadingBar.show();
};

/**
 * 隐藏页面默认的加载提示条
 */
function hideDefaultLoadingBar() {
	window.showLoadingBar = false;
	if (window.showLoadingBarDivTimeOutFunc)
		clearTimeout(window.showLoadingBarDivTimeOutFunc);
	if (window.showLoadingBarTimeOutFunc)
		clearTimeout(window.showLoadingBarTimeOutFunc);
	if (window.loadingBar != null)
		window.loadingBar.hide();
};
