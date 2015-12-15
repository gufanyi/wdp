/**
 * @version lui 1.0
 */
(function($) {
	$.widget("lui.panel", $.lui.base, {
		options : {
			parent : null,
			position : 'absolute',
			title : '',
			topPadding : 0,
			bottomPadding : 0,
			flowmode : '',
			className : 'panel_div',
			selfDefRender : '',
			expand : true,
			isCanExpand : true
		},
		_initParam : function() {
			this.componentType = "PANEL";
		    this._super();
		    this.id = this.element.attr("id");
		    if(this.options.parent)
		    	this.parentOwner = $("#"+this.options.parent)[0];
		    if (this.options.className == 'small_panel_div') {
		        this.options.topPadding = 15;
		        this.options.bottomPadding = 0;
		    }
		},
		_create : function() {
			this._initParam();
			var oThis = this,ele = this.element,opts = this.options;
		    var _height = '';
		    if (!opts.flowmode)
		        _height = "100%"; //this.height;
		    else
		        _height = "100%";//20px;
		    ele.addClass(opts.className).attr({
		        'id' : ele.attr("id")
		    }).css({
		        'left' : opts.left + "px",
		        'top' : opts.top + "px",
		        'width' : '100%',
		        'height' : _height,
		        'position' : opts.position
		    });

		    this.titleDiv = $("<div></div>").addClass('title_div');

		    this.dottaDiv = $("<div></div>").addClass('panel_dotta');

		    this.contentDiv = $("<div></div>").addClass('content_div').attr({
		        'id' : ele.attr("id") + "_content"
		    });
		    if (opts.className == 'small_panel_div') {
		        this.contentDiv.css({
		            'position' : 'relative',
		            'width' : '100%',
		            'float' : 'left'
		        });
		    }

		    this.titleDiv_img_left = $("<div></div>");
		    if (opts.expand) {
		        this.titleDiv_img_left.addClass('img_left_div_col');
		        this.titleDiv_img_left.data("expand",true);
		        this.contentDiv.show();
		    } else {
		        this.titleDiv_img_left.addClass('img_left_div_expand');
		        this.titleDiv_img_left.data("expand",false);
		        this.contentDiv.hide();
		    }

		    /**
			 * 左侧图片点击事件
			 */
		    if(opts.isCanExpand) {
		    	this.titleDiv_img_left.on('click',function(){
			        if (opts.expand) {
			            $(this).attr('class','img_left_div_expand');
			            opts.expand = false;
			            oThis.contentDiv.hide();
			            oThis.titleDiv.attr('class','title_div_off');
			            if (opts.selfDefRender) {
			            	opts.selfDefRender.call(oThis, oThis.customDiv, false);
			            }
			        } else {
			            $(this).attr('class','img_left_div_col');
			            opts.expand = true;
			            oThis.contentDiv.show();
			            oThis.titleDiv.attr('class','title_div');
			            oThis.initByExpandState();
			            if (opts.selfDefRender) {
			            	opts.selfDefRender.call(oThis, oThis.customDiv, true);
			            }
			        }
			    });
		    }

		    this.titleDiv_title_left = $("<div></div>").addClass('panel_transparent_title_left_div').html(opts.title);

		    //右侧预留的DIV，开发用户实现render
		    this.customDiv = $("<div></div>").addClass('panel_custom_right_div');

		    this.titleDiv.append(this.titleDiv_img_left);
		    this.titleDiv.append(this.titleDiv_title_left);
		    this.titleDiv.append(this.customDiv);

		    ele.append(this.titleDiv);
		    if (this.titleDiv_title_left.text()) {
		        this.titleDiv.attr('title',this.titleDiv_title_left.text());
		    //} else if (this.titleDiv_title_left.textContent) {
		    //    this.titleDiv.title = this.titleDiv_title_left.textContent;
		    } else {
		        this.titleDiv.attr('title',opts.title);
		    }
		    //	var width = this.titleDiv.offsetWidth;
		    //	this.titleDiv_title_left.style.maxWidth = width + "px";
		    //this.Div_gen.appendChild(this.dottaDiv);
		    ele.append(this.contentDiv);
		    
		    if(this.parentOwner) {
		    	var fc = $(this.parentOwner).children().first();
		        if (fc.size()>0) {
		            this.contentDiv.append($(fc));
		        }
		        ele.appendTo($(this.parentOwner));
		    }
	       

		    if (opts.flowmode) {
		        if (window.editMode)
		            this.contentDiv.css('min-height','28px');
		        else
		            this.contentDiv.css('min-height','2px');
		    } else {
		    	$(window).on("resize",function(){
		    		$.panelComp.panelResize(ele.attr("id"));
		    	});
//		        addResizeEvent(this.element[0],
//		        function() {
//		            $.panelComp.panelResize(ele.attr("id"));
//		        });
		        var currHeight = ele.outerHeight() - 30;
		        if (currHeight > 0)
		            this.contentDiv.css('height',currHeight + "px");
		    }

		    if (opts.topPadding != null)
		        ele.css('margin-top',opts.topPadding + "px");
		    if (opts.bottomPadding != null)
		        ele.css('margin-bottom',opts.bottomPadding + "px");
		    if (opts.className == 'small_panel_div' && (this.titleDiv_img_left.outerWidth() > 0 || this.titleDiv_title_left.outerWidth() > 0)) {
		        this.titleDiv.css('width',this.titleDiv_img_left.outerWidth() + this.titleDiv_title_left.outerWidth() + 20 + "px"); //15左侧图片距左右间距和
		        //this.dottaDiv.style.width = this.Div_gen.offsetWidth - this.titleDiv_img_left.offsetWidth - this.titleDiv_title_left.offsetWidth - 20 - 5 + "px";//5分割线距左间距
		    }

		    //判断用户是否定义右侧预留区域的渲染类型,如果有责渲染,在include.js中执行
		    if (opts.selfDefRender) {
		    	opts.selfDefRender.call(this, this.customDiv.get(0), opts.expand);
		    }
		    //内容太长时，显示省略号
		    var width = this.titleDiv.outerWidth();
		    var customWidth = this.customDiv.outerWidth();
		    //前面的显示隐藏符号的宽是30
		    if (width - 30 - customWidth - 1 > 0) {
		        this.titleDiv_title_left.css({
		            'max-width' : (width - 30 - customWidth - 1) + "px",
		            'white-space' : 'nowrap',
		            'overflow' : 'hidden',
		            'text-overflow' : 'ellipsis'
		        });
		    }
		},
		setTitle : function(title) {
		    this.titleDiv_title_left.html(title);
		    if (this.options.className == 'small_panel_div' && (this.titleDiv_img_left.outerWidth() > 0 || this.titleDiv_title_left.outerWidth() > 0)) {
		        this.titleDiv.css('width',this.titleDiv_img_left.outerWidth() + this.titleDiv_title_left.outerWidth() + 20 + "px");
		        //this.dottaDiv.style.width = this.Div_gen.offsetWidth - this.titleDiv_img_left.offsetWidth - this.titleDiv_title_left.offsetWidth - 20 - 5 + "px";
		    }
		},
		setExpand : function(expand) {
		    if (expand && typeof(expand) == "boolean") {
		        this.titleDiv_img_left.data("expand",!expand);
		        this.titleDiv_img_left.triggerHandler('click');
		    } else {
		        this.titleDiv_img_left.data("expand",true);
		        this.titleDiv_img_left.triggerHandler('click');
		    }
		},
		/**
		 * 后台改变renderType后触发的renderType
		 */
		setRenderTypeFunc : function(selfDefRender) {
		    if (selfDefRender) {
		        selfDefRender.call(this, this.customDiv, this.options.expand);
		    }
		},
		getContentDiv : function() {
		    return this.contentDiv;
		},
		setContent : function(obj) {
		    this.contentDiv.append($(obj));
		},
		removeContent : function(obj) {
		    $(obj).remove();
		    //this.contentDiv.removeChild(obj);
		},
		setDisplay : function(display) {
		    if(display) {
		    	$(this.parentOwner).show();
		    } else {
		    	$(this.parentOwner).hide();
		    }
		    this.display = display;
		},
		setVisible : function(visible) {
			$(this.parentOwner).css('visibility',visible ? "visible": "hidden");
		    this.visible = visible;
		},
		getContext : function() {
		    return {
		    	c : "PanelContext",
		    	id : this.element.attr("id"),
		    	display : this.display,
		    	visible : this.visible
		    };
		},
		setContext : function(context) {
		    if (context.display != null && context.display != this.display) this.setDisplay(context.display);
		    if (context.visible != null && context.visible != this.visible) this.setVisible(context.visible);
		},
		adjustSelf : function() {
		    if (this.titleDiv.css('display') == "none") {
		        this.contentDiv.css('height',this.element.outerHeight() + 'px');
		    } else {
		        //this.contentDiv.style.height = "100%";
		        var barHeight = 30;
		        if (this.element.outerHeight() != null && this.element.outerHeight() > parseInt(barHeight)) {
		            var height = this.element.outerHeight() - parseInt(barHeight);
//		            if ($.browsersupport.IS_IE7 || $.browsersupport.IS_IE8) //避免死循环，多减5个像素
//		            height -= 5;
		            if (height > 0) this.contentDiv.css('height',height + 'px');
		        }
		    }
		}
	});
	
	$.panelComp={
		panelResize : function(id) {
		    var panel = window.objects[id];
		    panel.adjustSelf();
		}
	};
})(jQuery);
