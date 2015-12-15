/**
 * @fileoverview 浮动面板
 * @author lxl
 * @version lui 1.0
 */
(function($) {
	/**
	 * 浮动面板
	 * 
	 * @class 浮动面板，可以附着在指定的控件上。根据指定的attachComp获得此控件的实际位置信息。
	 * @constructor
	 * @param floatPos 浮动位置 左上角:leftTop 右上角:rightTop 左下角:leftBottom 右下角:rightBottom
	 * @param vIndent 纵向缩进
	 * @param hIndent 横向缩进
	 * @param dragEnable 浮动面板是否可以拖动
	 */
	$.widget("lui.floatingpanel", {
		options : {
			attachComp : null,
			name : '',
			width : '100%',
			height : '100%',
			floatPos : '',
			vIndent : 0,
			hIndent : 0,
			dragEnable : true,
			className : ''
		},
		_initParam : function() {
		    this.attachCompHtml = this.options.attachComp.element;
		    this.parentHtml = this.element.parent();
		},
		_create : function() {
			this._initParam();
		    var oThis = this,opts = this.options,ele = this.element;
		    var _width = '',_height = '';
		    if (opts.width.toString().indexOf("%") == -1)
		        _width = opts.width + "px";
		    else
		        _width = opts.width;
		    if (opts.height.toString().indexOf("%") == -1)
		        _height = opts.height + "px";
		    else
		        _height = opts.height;
		    ele.css({
		        'position' : 'absolute',
		        'border' : 'solid #99A1B6 1px',
		        'overflow' : 'hidden',
		        'z-index' : $.measures.getZIndex(),
		        'background' : '#EFF1E6'
		    });

		    if (opts.dragEnable == true) {
		        // 左侧的图片
		        this.leftImg = $("<div id=\"leftImg\"></div>").image({
		        	refImg1 :  window.themePath + "/images/floatingpanel/dragsign.gif",
		        	width : 12,
		        	height : opts.height,
		        	refImg2 : window.themePath + "/images/floatingpanel/dragsign.gif"
		        }).appendTo(ele).image("instance");
//		        this.leftImg.Div_gen.parentContainer = this.parentHtml;
		        // 处理拖放
		        ele.draggable({
					handle: "#leftImg",
					containment: "document"
				});
		    }

		    // 内容区
		    this.contentDiv = $("<div></div>").css({
		        'position' : 'absolute',
		        'left' : '12px',
		        'top' : '0px',
		        'height' : opts.height + "px",
		        'width' : opts.width - 12 + "px",
		        'overflow' : 'hidden'
		    }).appendTo(ele);

		    // 得到附属容器的相对于parent的top、left
		    var parentTop = this.attachCompHtml.position().top;
		    var parentLeft = this.attachCompHtml.position().left;

		    var attachCompWidth = this.attachCompHtml.outerWidth();
		    var attachCompHeight = this.attachCompHtml.outerHeight();
		    // 浮动在左上角
		    if (opts.floatPos == "leftTop") {
		        ele.css({
		            'top' : parentTop + opts.vIndent + "px",
		            'left' : parentLeft + opts.hIndent + "px"
		        });
		    }
		    // 浮动在右上角
		    else if (opts.floatPos == "rightTop") {
		        ele.css({
		            'top' : parentTop + opts.vIndent + "px",
		            'left' : parentLeft + attachCompWidth - opts.width - opts.hIndent + "px"
		        });
		    }
		    // 浮动在左下角
		    else if (opts.floatPos == "leftBottom") {
		        ele.css({
		            'top' : parentTop + attachCompHeight - opts.height - opts.vIndent + "px",
		            'left' : parentLeft + opts.hIndent + "px"
		        });
		    }
		    // 浮动在右下角
		    else if (opts.floatPos == "rightBottom") {
		        ele.css({
		            'top' : parentTop + attachCompHeight - opts.height - opts.vIndent + "px",
		            'left' : parentLeft + attachCompWidth - opts.width - opts.hIndent + "px"
		        });
		    }
		},
		/**
		 * 得到内容面板,可以append其他组件
		 */
		getContentPane : function() {
		    return this.contentDiv;
		},
		/**
		 * 隐藏浮动面板
		 */
		hide : function() {
		    this.element.hide();
		},
		/**
		 * 显示浮动面板
		 */
		show : function() {
		    this.element.show();
		},
		/**
		 * 获取浮动面板的显示对象
		 */
		getObjHtml : function() {
		    return this.element;
		},
		/**
		 * 设定浮动面板位于给定控件的某个位置,该方法会自动获取comp的left和top值,将浮动面板 放在该控件的上,下,左,右
		 * 
		 * @param objHtml 控件的显示对象(div)
		 * @param posi 取值为top,bottom,left,right
		 */
		setPositionByComp : function(objHtml, posi) {
		    if (objHtml == null) return;
		    if (posi == "bottom") {
		        this.element.css({
		            'top' : $(objHtml).offset().top + $(objHtml).outerHeight() + "px",
		            'left' : $(objHtml).offset().left + "px"
		        });
		    } else if (posi == "top") {
		    	this.element.css({
		            'top' : $(objHtml).offset().top - this.element.outerHeight() + "px",
		            'left' : $(objHtml).offset().left + "px"
		        });
		    } else if (posi == "right") {
		    	this.element.css({
		            'top' : $(objHtml).offset().top + "px",
		            'left' : $(objHtml).offset().left + this.element.outerWidth() + "px"
		        });
		    } else if (posi == "left") {
		    	this.element.css({
		            'top' : $(objHtml).offset().top + "px",
		            'left' : $(objHtml).offset().left - this.element.outerWidth() + "px"
		        });
		    }
		}

	});
})(jQuery);
