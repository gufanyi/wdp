/**
 *
 * 超链接组件
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 链接控件的构造函数
	 * @class 链接控件
	 * @param href   链接的目的文件
	 * @param hasImg 是否在链接前显示图片
	 * @param srcImg 显示在超链接前的图片路径,若hasImg为true且没有提供srcImg则显示默认图片
	 * @param target 超文本链接的目标窗口 "_blank", "_self" , "_parent", "_top"
	 * @param text   链接中可见部分	
	 */
	$.widget("lui.link", $.lui.base, {
		options : {
			href : '#',
			text : '',
			hasImg : false,
			srcImg : '',
			target : '_self',
			position : 'absolute',
			className : 'link_div',
			//event
			onclick : null
		},
		_initParam : function() {
			this.componentType = "A";
			this.parentOwner = this.element.parent()[0];
			this._super();
			if (this.options.hasImg == true) {
				this.options.srcImg = $.argumentutils.getString(this.options.srcImg, window.themePath + "/images/a/aimg.gif");
			};
			this.visible = true;
			this.disabled = false;
		},
		_create : function() {
			var oThis = this,opts = this.options,ele = this.element;
			var _width = '';
			//增加对百分比的判断，如果LinkComp设置成百分比，则用百分数设置宽度
			if(opts.width && opts.width.indexOf("%")>-1){
				_width = opts.width;
			}else{
				_width = 25 + $.measures.getTextWidth(opts.text) + "px";
			}
			ele.addClass(opts.className).attr({
				'title' : opts.text
			}).css({
				'position' : opts.position,
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : _width,
				'height' : '16px',
				'overflow' : 'hidden'
			});
			
			//创建图片
			this.aImg = $("<div></div>").css({
				'position' : 'absolute',
				'left' : '0px',
				'width' : '16px',
				'height' : '16px'
			});
			if (opts.hasImg == true){
				ele.append(this.aImg);
				this.img = $("<div></div>").css({
					'background' : 'url("'+opts.srcImg+'")',
					'width' : '16px',
					'height' : '16px'
				}).appendTo(this.aImg);
			}
			
			//创建超链接文字
			var _left = '';
			if (opts.hasImg == true)
				_left = "20px";
			else
				_left = "0px";
			this.textDiv = $("<div></div>").css({
				'position' : 'absolute',
				'top' : '0px',
				'left' : _left,
				'height' : '20px'
			}).appendTo(ele);

			this.a = $("<a></a>").addClass('link_normal').css({
				'left' : '20px'
			}).html(opts.text).appendTo(this.textDiv);

			if(window.editMode == false){
				this.a.attr('href',opts.href);
			}
			
			this.a.on('click',function(e){
				if (oThis.disabled == true) {
					e.stopPropagation();
					return;
				}
				if (oThis._trigger("onclick",e,this) == false || opts.href == "#") {
					e.stopPropagation();
					return;
				}
				if (opts.target == "_blank") {
					window.open(opts.href);
				}
				else if (opts.target == "_self")
					window.location.href = opts.href;
				else if (opts.target == "_parent")
					window.parent.location.href = opts.href;
				else if (opts.target == "_top")
					window.top.location.href = opts.href;
				e.stopPropagation();
			}).on('mouseover',function(e){
				if (this.disabled == false )
					oThis.a.attr('class','link_over');
			}).on('mouseout',function(e){
				if (this.disabled == false  )
					oThis.a.attr('class','link_normal');
			});
		},
		/**
		 * 设置显示状态
		 */
		setVisible : function(visible) {
			if (visible != null && this.visible != visible) {
				this.element.css('visibility',(visible ? '' : 'hidden'));
				this.visible = visible;
			}
			this.ctxChanged = true;
		},
		/**
		 * 设置激活状态
		 */
		setActive : function(visible) {
			var isActive = $.argumentutils.getBoolean(visible, false);
			// 控件处于激活状态变为非激活状态
			if (this.disabled == false && isActive == false) {
				this.a.attr({
					'disabled' :true,
					'class' : 'link_disable'
				});
				this.disabled = true;
			}
			// 控件处于禁用状态变为激活状态
			else if (this.disabled == true && isActive == true) {
				this.a.attr({
					'disabled' :false,
					'class' : 'link_normal'
				});
				this.disabled = false;
			}
			this.ctxChanged = true;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			if(this.ctxChanged == false)
				return null;
			return {
				c : "LinkContext",
				id : this.element.attr("id"),
				visible : this.visible,
				text : this.options.text,
				enabled : !this.disabled
			};
		},
		/**
		 * 设置显示值
		 */
		setText : function(text) {
			this.options.text = text;
			this.element.attr('title',this.options.text);
			this.a.html(text);
			this.ctxChanged = true;
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.visible != null)
				this.setVisible(context.visible);
			if (context.text != null)
				this.setText(context.text);
			if (context.enabled == this.disabled)
				this.setActive(context.enabled);
			this.ctxChanged = false;
		}
	});
})(jQuery);
