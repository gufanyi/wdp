/**
 * @fileoverview Label组件
 *
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 标签控件
	 * @class LabelComp控件的构造函数
	 * @constructor LabelComp控件的构造函数
	 */
	$.widget("lui.label" , $.lui.base , {
		options : {
			text : '',
			position : 'absolute',
			className : 'normallabel',
			textAlign : 'left',
			//event
			onmouseover : null,
			onmouseout : null
		},
		_initParam : function() {
			this.componentType = "LABEL";
			this.parentOwner = this.element.parent()[0];
			this._super();
		},
		_create : function() {
			var oThis = this, opts = this.options, ele = this.element;
			ele.addClass(opts.className).attr({
				'title' : opts.text
			}).css({
				'position' : opts.position,
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'text-overflow' : 'ellipsis',
				'overflow' : 'hidden',
				'white-space' : 'nowrap',
				'cursor' : 'default',
				'text-align' : opts.textAlign
			}).html(opts.text).on('mouseover',function(e){
				oThis._trigger('onmouseover',e);
			}).on('mouseout',function(e){
				oThis._trigger('onmouseout',e);
			});
		},
		/**
		 * 设置文字位置，左、中、右
		 * @param {} textAlign
		 */
		setTextAlign : function(textAlign){
			this.options.textAlign = textAlign;
			this.element.css('text-align',this.options.textAlign);
		},
		setHeight : function(height){
			this.options.height = height;
			this.element.css('height',$.argumentutils.getString($.measures.convertHeight(this.options.height), '100%'));
		},
		/**
		 * 设置文字颜色
		 */
		setColor : function(color) {
			this.color = color;
			this.element.css('color',this.color);
			this.ctxChanged = true;
		},
		/**
		 * 设置文字样式（normal, italic, oblique）
		 */
		setStyle : function(style) {
			this.style = style;
			this.element.css('font-style',this.style);
		},
		/**
		 * 设置文字字体粗细（normal, bold, bolder, lighter, 100-900）
		 */
		setWeight : function(weight) {
			this.weight = weight;
			this.element.css('font-weight',this.weight);
		},
		/**
		 * 设置文字字体大小
		 */
		setSize : function(size) {
			this.size = size;
			this.element.css('font-size',this.size + "px");
		},
		/**
		 * 设置文字字体
		 */
		setFamily : function(family) {
			this.family = family;
			this.element.css('font-family',this.family);
		},
		/**
		 * 设置宽度
		 */
		setMaxWidth : function(maxWidth){
			this.maxWidth = maxWidth;
			if(this.maxWidth && this.maxWidth.indexOf("%") == -1){
				var maxWidth = parseInt(this.maxWidth, 10);
				this.element.css('max-width',maxWidth + "px");
			}
		},
		/**
		 * 设置外观
		 */
		setDecoration : function(decoration) {
			this.decoration = decoration;
			this.element.css('text-decoration',decoration);
		},
		/**
		 * 改变label组件的文字
		 */
		changeText : function(text) {
			this.element.html('');
			this.options.text = text;
			// 重新计算宽度
			var labelWidth = $.measures.getTextWidth(text);
			this.element.css('width',labelWidth+2 + "px").html(this.options.text);
			this.ctxChanged = true;
		},
		/**
		 * 改变label组件的内容
		 */
		setInnerHTML : function(html) {
			this.element.html(html);
			if(this.maxWidth && this.maxWidth.indexOf("%") == -1){
				var maxWidth = parseInt(this.maxWidth, 10);
//				if($.browsersupport.IS_IE && maxWidth < this.element.outerWidth()){
				if(maxWidth < this.element.outerWidth()){
					this.element.css('width',maxWidth + "px");
				}
			}
		},
		/**
		 * 禁用LabelComp控件
		 */
		inactive : function() {
			this.element.attr('class','label_inactive');
			this.disabled = true;
			this.ctxChanged = true;
		},
		/**
		 * 激活LabelComp控件
		 */
		active : function() {
			this.element.attr('class',this.options.className);
			this.disabled = false;
			this.ctxChanged = true;
		},
		changeClass : function(className){
			this.element.attr('class',className);
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			if(this.ctxChanged == false)
				return null;
			return {
				c : "LabelContext",
				id : this.element.attr("id"),
				enabled : !this.disabled,
				text : "" + this.options.text,
				color : this.color,
				visible : this.visible
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled  == true)
				this.active();
			else if (context.enabled == false)
				this.inactive();
			if (context.text != null && context.text != this.options.text)
				this.changeText(context.text);
			if (context.innerHTML != null){
				//IE8不支持marquee，如果innerHTML中包含此标签，过滤掉,待完善，可以js模拟实现marquee
//				if($.browsersupport.IS_IE8){
//					context.innerHTML = context.innerHTML.replace(/\<marquee>/g, "");
//					context.innerHTML = context.innerHTML.replace(/\<\/marquee>/g, "");
//				}
				this.setInnerHTML(context.innerHTML);
			}
			if (context.color != null)
				this.setColor(context.color);
			if (context.style != null)
				this.setStyle(context.style);
			if (context.weight != null)
				this.setWeight(context.weight);
			if (context.size != null)
				this.setSize(context.size);
			if (context.family != null)
				this.setFamily(context.family);
			if (context.visible != this.visible) {
				if (context.visible == true)
					this.show();
				else
					this.hide();	
			}
			this.ctxChanged = false;
		},
		show : function() {
			this._super();
		},
		hide : function() {
			this._super();
		}
	});
	
})(jQuery);
