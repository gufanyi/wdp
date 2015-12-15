/**
 * @fileoverview 按钮控件
 * 
 * @auther lxl
 * @version lui 1.0
 *  
 */
(function($) {
	$.widget("lui.button", $.lui.base, {
		options: {
			width : 84,
			height : 24,
			text : '',
//			tip : '',
			refImg : '',
			position : 'absolute',
			align : '',
			disabled : false,
			className : 'button_div',
			//event
			onclick : null,
			onmouseout : null,
			onmouseover : null,
			onmousedown : null,
			onmouseup : null
		},
		_initParam : function() {
			this.componentType = "BUTTON";
			this.parentOwner = this.element.parent()[0];
			this._super();
		},
		_create : function() {
			this._initParam();
			// 快捷键
			var oThis = this;
			this.element.attr({
				'title' : this.options.tip
//				'tip' : this.options.tip
			}).css({
				'position' : this.options.position,
				'left' : this.options.left + "px",
				'top' : this.options.top + "px",
				'width' : this.convertSize(this.options.width),
				'height' : this.convertSize(this.options.height)
			}).addClass(this.options.className);
			
			var jq_btnLeftDiv = $("<div></div>")
				.addClass('btn_left_off')
				.appendTo(this.element);
			
			var jq_btnCenterDiv = $("<div></div>")
				.addClass('btn_center')
				.appendTo(this.element);
				
			var jq_btnRightDiv = $("<div></div>")
				.addClass('btn_right_off')
				.appendTo(this.element);
				
			this.butt = $("<button type='button'></button>").css({
				'width' : '100%',
				'height' : this.element.css('height')
			}).addClass('btn_off').appendTo(jq_btnCenterDiv);
			
			// 初始化禁用按钮
			if (this.options.disabled) {
				jq_btnLeftDiv.attr('class','btn_left_disabled');
				jq_btnRightDiv.attr('class','btn_right_disabled');
				this.butt.attr({
					'class' : 'btn_disabled',
					'disabled' : true
				});
			}
			
			function btnEventToggleClass(e,status,event) {
				jq_btnLeftDiv.attr('class','btn_left_' + status);
				jq_btnRightDiv.attr('class','btn_right_' + status);
				oThis.butt.attr('class','btn_' + status);
				oThis._trigger(event, e);
			}
			
			this.element.hover(
				function(e) {
					if(oThis.options.disabled){
						return;
					}
					btnEventToggleClass(e,'on','onmouseover');
				},
				function(e) {
					if(oThis.options.disabled){
						return;
					}
					btnEventToggleClass(e,'off','onmouseout');
				}
			).on('mousedown',function(e) {
				if(oThis.options.disabled){
					return;
				}
				btnEventToggleClass(e,'down','onmousedown');
			}).on('mouseup',function(e) {
				if(oThis.options.disabled){
					return;
				}
				btnEventToggleClass(e,'on','onmouseup');
			}).on('focus',function(e) {
				if(oThis.options.disabled){
					return;
				}
				btnEventToggleClass(e,'focus','onfocus');
			}).on('blur',function(e) {
				if(oThis.options.disabled){
					return;
				}
				btnEventToggleClass(e,'off','onblur');
			}).on('click',function(e) {
				if (oThis.options.disabled)
					return;
				oThis._trigger('onclick', e);
				e.stopPropagation();
			});
			
			this.textNodeDiv = $("<div></div>").css({
				'white-space' : 'nowrap',
				'overflow' : 'hidden',
				'text-overflow' : 'ellipsis'
			}).html(this.options.text);

			if (this.options.refImg) {
				this.imgNode = $("<img/>").attr({
					'src' : this.options.refImg
				});
				
				// 如果文字没有指定则只显示图片
				if (this.options.text == null || this.options.text == "") {
					this.butt.append(this.imgNode);
				} else {
					// 文字位于图片之左
					if (this.options.align == "left") {
						this.butt.append(this.textNodeDiv).append(this.imgNode);
						this.textNodeDiv.css({
							'display' : 'inline'
						});
						this.imgNode.css({
							'display' : 'inline',
							'margin-left' : '5px',
							'vertical-align':'middle'
						});
					}

					// 文字位于图片之右
					if (this.options.align == "right") {
						this.butt.append(this.imgNode).append(this.textNodeDiv);
						this.textNodeDiv.css({
							'margin-left' : '5px',
							'display' : 'inline'
						});
						this.imgNode.css({
							'display':'inline',
							'vertical-align':'middle'
						});
					}

					var brNode = $("<br/>");
					// 文字位于图片之上
					if (this.options.align == "top") {
						this.butt.append(this.textNodeDiv).append(brNode).append(this.imgNode);
					}

					// 文字位于图片之下
					if (this.options.align == "bottom") {
						this.butt.append(this.imgNode).append(brNode).append(this.textNodeDiv);
					}
				}
			} else {
				this.butt.append(this.textNodeDiv);
			}
		},
		/**
		 * 改变该button显示的图片
		 * 
		 * @param imgPath 新图片的路径
		 */
		changeImage : function(imgPath) {
			if (this.options.refImg != null && this.options.refImg != "") {
				this.options.refImg = imgPath;
				this.imgNode.attr('src',imgPath);
			}
		},
		/**
		 * 改变按钮上显示的文字
		 * 
		 * @param text 按钮上要显示的文字
		 */
		changeText : function(text) {
			this.textNodeDiv.html(text);
			this.options.text = text;
			this.element.attr('title',text);
			this.ctxChanged = true;
		},
		/**
		 * 设置此按钮控件的激活状态.
		 * 
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			// 控件处于激活状态变为非激活状态
			if (!this.options.disabled && !isActive) {
				this.butt.attr({
					'disabled' : true,
					'class' : 'btn_disabled'
				});
				this.options.disabled = true;
			}
			// 控件处于禁用状态变为激活状态
			else if (this.options.disabled && isActive) {
				this.butt.attr({
					'disabled' : false,
					'class' : 'btn_off'
				});
				this.options.disabled = false;
			}
			
			if (this.options.refImg) {
				this.imgNode.attr('src',$.argumentutils.addImgSuffix(this.options.refImg,(isActive?"":"_disable")));
			}
			this.ctxChanged = true;
		},
		/**
		 * 得到按钮的激活状态.
		 */
		isActive : function() {
			return !this.options.disabled;
		},
		/**
		 * 响应快捷键
		 * @private
		 */
		handleHotKey : function(key) {
			if (!this.isActive())
				return null;
			if (this.hotKey != null) {
				if (key == this.hotKey) {
					oThis._trigger('click', e);
					e.stopPropagation();
					return this;
				}
			}
			return null;
		},
		setVisible : function(visible) {
			if (visible != null && this.visible != visible) {
				if (visible)
					this.element.show();
				else
					this.element.hide();
				this.visible = visible;
			}
		    if(visible){
		    	this.element.show();
		    }else{
		    	this.element.hide();
		    } 
			this.ctxChanged = true;
		},
		changeWidth : function(width) {
			if (width != null && $.measures.convertWidth(width) != this.options.width){
				this.options.width = ($.measures.convertWidth(width));
				this.element.css('width',this.options.width);
//				if($.browsersupport.IS_IE7){
//					this.parent().css('width',this.options.width);
//				}
				var tempWidth = 0;
				if ($.argumentutils.isPercent(this.options.width))
					tempWidth = this.element.outerWidth();
				else
					tempWidth = $.argumentutils.getInteger(parseInt(this.options.width), 120);
			}

		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
//			if(!this.ctxChanged)
//				return null;
			return {
				'c' : 'ButtonContext',
				'id' : this.element.attr('id'),
				'enabled' : !this.options.disabled,
				'visible' : this.visible,
				'text' : this.options.text
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled == this.options.disabled)
				this.setActive(context.enabled);
			if (context.visible != null && context.visible != this.visible)
				this.setVisible(context.visible);
			if (context.text != null && context.text != this.options.text)
				this.changeText(context.text);
			if (context.height != null && $.measures.convertHeight(context.height) != this.options.height)
				this.options.height = $.measures.convertHeight(context.height);
				this.element.css('height',this.options.height);
			if (context.refImg != null && context.refImg != this.options.width)
				this.changeImage(context.refImg);
			this.ctxChanged = false;
		}
	});
})(jQuery);
