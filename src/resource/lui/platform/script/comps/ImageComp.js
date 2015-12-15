/**
 * @fileoverview ImagaComp控件.
 * 用图片取代按钮的外观,功能完全等同于按钮.	
 * 
 * @author lxl
 * @version lui 1.0
 *
 */
(function($) {
	/**
	 * ImageComp控件构造函数
	 * @class 图片控件
	 * @param refImg1 图片处于未选中效果样式图片的绝对路径
	 * @param refImg2 图片处于选中效果的样式图片的绝对路径
	 * @param attrObj.inactiveImg 图片处于禁用效果的样式图片的绝对路径
	 * 
	 */
	$.widget("lui.image", $.lui.base, {
		options : {
			refImg1 : window.themePath + "/images/default/default.gif",
			refImg2 : '',
			alt : '',
			position : 'relative',
			disabled : false,
			visible : true,
			boolFloatRight : false,
			boolFloatLeft : false,
			inactiveImg : null,
			maxShow : false,
			//event
			onmouseout : null,
			onmouseover : null,
			onclick : null
		},
		_initParam : function() {
			this.componentType = "IMAGE";
			this.parentOwner = this.element.parent()[0];
			var opts = this.options;
			this._super();
			
			// 如果没有传入的width或者height则按照实际图片的大小显示
			if (opts.width == '' || opts.width == null || isNaN(parseInt(opts.width)))
				opts.width = "auto";		
			if (opts.height == '' || opts.height == null || isNaN(parseInt(opts.height)))
				opts.height = "auto";		
			
			opts.refImg2 = $.argumentutils.getString(opts.refImg2, opts.refImg1);
			
			if (opts.boolFloatRight != null && opts.boolFloatRight == true) {
				opts.position = "relative";
			}
			if (opts.boolFloatLeft != null && opts.boolFloatLeft == true) {
				opts.position = "relative";			
			}
			opts.inactiveImg = $.argumentutils.getString(opts.inactiveImg, opts.refImg1);
		},
		_create : function() {
			var oThis = this,opts = this.options,ele = this.element;
			ele.css({
				'position' : opts.position,
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'float' : (opts.boolFloatLeft ? 'left' : 'right')
			});

			if(!opts.visible){
				this.hideV();
			}

			ele[0].onselectstart = function() { 
				return false; 
			};
			
			//创建普通状态图片	
			this.img1 = $('<img/>').attr({
				'src' : opts.refImg1
			}).appendTo(ele);

			ele.css({
				'width' : (opts.width == "auto" ? (this.img1.width() + "px") : opts.width),
				'height' : (opts.height == "auto" ? (this.img1.height() + "px") : opts.height)
			});

			// 如果设置了width,height,则按照指定大小显示
			if (opts.width != "auto" && opts.width != "100%" || opts.height != "auto" && opts.height != "100%"){
		  		if(opts.width != "auto" && opts.width != "100%")
		   			this.img1.width('100%');
		     	if(opts.height != "auto" && opts.height != "100%")
		      		this.img1.height('100%');
		 	}

		 	if(opts.maxShow){
		 		this.img1.width('100%').height('100%');
		 	}
			// 设置鼠标移动到图片上的提示信息
			if (opts.alt != null) 
				ele.attr('title',opts.alt);

		 	var mouseoutHandler = function(e) {
				if (!oThis.disabled) {
					oThis.img1.attr('src',opts.refImg1);
					$(e.target).css('cursor','default');
					oThis._trigger("onmouseout",e);
				}
			};

			var mouseoverHandler = function(e) {

				if (!oThis.disabled) {
					$(e.target).css('cursor','pointer');
					// 更换图片为激活状态图片
					if (opts.refImg2) {
						oThis.img1.attr('src',opts.refImg2);
					}
				} else {
					$(e.target).css('cursor','default');
				}
				oThis._trigger("onmouseover",e);
				e.stopPropagation();
			};

			var clickHandler = function(e) {
				if (!oThis.disabled) {
					oThis._trigger("onclick",e);
					e.stopPropagation();
				}
			};

			ele.data('mouseoutEvent',mouseoutHandler)
				.data('mouseoverEvent',mouseoverHandler)
				.data('clickEvent',clickHandler);

			if (opts.refImg2 != "") {
				ele.off("mouseout").on('mouseout',function(e){
					mouseoutHandler(e);
				});
			}

			ele.off("mouseover click").on('mouseover',function(e){
				mouseoverHandler(e);
			}).on('click',function(e){
				clickHandler(e);
			});

			ele[0].oncontextmenu = function(e) {
				//oThis.oncontextmenu(e);
			};
			
		},
		/**
		 * 设置控件边界值.子控件可根据实际情况覆盖此函数
		 * @param left   控件左侧X坐标
		 * @param top    控件顶部Y坐标
		 * @param width  控件的宽度
		 * @param height 控件的高度
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options;
			// 改变数据对象的值
			opts.left = $.argumentutils.getInteger(left, 0);
			opts.top = $.argumentutils.getInteger(top, 0);
			if (width == "auto")
				opts.width = "auto"; 
			else		
				opts.width = $.argumentutils.getString($.measures.convertWidth(width), "100%");
			if (height == "auto")
				opts.height = "auto";
			else	
				opts.height = $.argumentutils.getString($.measures.convertHeight(height), "100%");
			// 改变显示对象的值
			ele.css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : opts.width,
				'height' : opts.height
			});
		},
		/**
		 * 改变显示图片
		 */
		changeImage : function(src1, src2) {
			var opts = this.options;
			opts.refImg1 = src1;   
			this.img1.attr('src',opts.refImg1);
			if (opts.refImg2 != '') 
				opts.refImg2 = src2;
			this.img1.removeAttr("width");
			this.img1.removeAttr("height");
		},
		/**
		 * 改变提示信息
		 */
		changeAlt : function(alt) {
			ele.attr('title',alt);
		},
		/**
		 * 设置此图片控件的激活状态
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			var opts = this.options,ele = this.element;
			var isActive = $.argumentutils.getBoolean(isActive, false);
			// 控件处于激活状态变为非激活状态
			if (!opts.disabled && !isActive) {
				this.contextmenuFunc = ele[0].oncontextmenu;
				ele.off('mouseout mouseover click');
				ele[0].oncontextmenu = function(){};
				opts.disabled = true;
				//变换图片风格为禁用状态
				this.img1.attr('src',opts.inactiveImg);
			}
			// 控件处于禁用状态变为激活状态
			else if (opts.disabled && isActive) {
				ele.off("mouseout mouseover click").on('mouseout',function(e){ele.data('mouseoutEvent')(e);})
					.on('mouseover',function(e){ele.data('mouseoverEvent')(e);})
					.on('click',function(e){ele.data('clickEvent')(e);});
				ele[0].oncontextmenu = this.contextmenuFunc;
				opts.disabled = false;
				this.img1.attr('src',opts.refImg1);
			}
		},
		/**
		 * 得到输入框的激活状态
		 */
		isActive : function() {
			return !this.options.disabled;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "ImageContext",
				id : this.element.attr("id"),
				enabled : !this.options.disabled,
				visible : this.options.visible
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
			var opts = this.options;
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.visible != null && context.visible != opts.visible){
				opts.visible = context.visible;
				if (opts.visible)		
					this.showV();
				else
					this.hideV();
			}	
			if (context.image1 != null && context.image1 != opts.refImg1) {
				opts.refImg1 = context.image1;
				this.img1.attr('src',opts.refImg1);
			}
			if (context.image2 != null && context.image2 != opts.refImg2) {
				opts.refImg2 = context.image2;
			}
		}
	});
	
})(jQuery);
