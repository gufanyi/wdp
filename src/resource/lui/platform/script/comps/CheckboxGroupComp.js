/**
 * @fileoverview Checkbox集合组件
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * Checkbox集合控件构造函数
	 * @class Checkbox集合类 
	 * 
	 * @constructor CheckboxGroupComp的构造函数
	 * @param parent 父控件
	 * @param name 此控件的名字,是对此控件的引用
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param width 控件宽度
	 * @param position 控件的定位属性
	 * @param attrArr 属性对象
	 * @param className css文件的名字
	 */
	$.widget("lui.checkboxgroup", $.lui.base, {
		options : {
			position : 'relative',
			disabled : false,
			readOnly : false,
			tabIndex : 0,
			inputAssistant : '',
			labelText : '',
			labelAlign : 'left',
			labelWidth : 0,
			changeLine : false,
			className : 'checkboxgroup_div',
			//event
			valuechanged : null,
			onfocus : null,
			onblur : null,
			onenter : null,
			onclick : null
		},
		_initParam : function() {
			this.componentType = "RADIOGROUP";
			this.parentOwner = this.element.parent()[0];
			this._super();
			
			this.hasLabel = false;
			if ("" != this.options.labelText)
				this.hasLabel = true;
			if (0 == this.options.labelWidth && "" != this.options.labelText)
				this.options.labelWidth = $.measures.getTextWidth(this.options.labelText) + 5;

			this.groupId = this.element.prop("id") + "_checkbox_group";
			this.checkboxs = new Array();
			this.focusIndex = -1;
			this.isFocus = false;
			this.oldValue = null;
			
			this.ITEM_HEIGHT = "20px";
			this.IMAGE_SIDE_LENGTH = 15;
		},
		_create : function() {
			this._initParam();
			
			var oThis = this,opts = this.options,ele = this.element;
			ele.addClass(opts.className).css({
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'position' : opts.position,
				'width' : opts.width == null ? '120px' : opts.width,
				'height' : '100%'
			}).prop('tabindex',opts.tabIndex == null ? 0 : opts.tabIndex)
			.on('focus',function(e){
				oThis.isFocus = true;
				oThis._trigger('onfocus',e);

			}).on('blur',function(e){
				oThis.isFocus = false;
				oThis._trigger('onblur',e);

			}).on('keypress',function(e){
				if (opts.disabled != true) {
					// 获取输入字符
					var keyCode = e.keyCode;
					//处理回车
					if (keyCode == 13) {
						oThis._trigger('onenter',e);
						return;
					}
				}
				return;
			}).on('keydown',function(e){
				if (opts.disabled != true) {
					// 获取输入字符
					var keyCode = e.keyCode;
					// 左方向键、右方向键
					if (keyCode == 37) {
						if (oThis.focusIndex <= 0)
							return;
						else
							oThis.setFocusItem(oThis.focusIndex - 1);
						return;
					} else if (keyCode == 39) {
						if (oThis.focusIndex >= oThis.checkboxs.length - 1)
							return;
						else
							oThis.setFocusItem(oThis.focusIndex + 1);
						return;
					}
				}
			});
			
			var _width = '';
			if (this.hasLabel)
				_width = ele.outerWidth() - opts.labelWidth - 4 + "px";
			else
				_width = "100%";

			this.Div_text = $("<div></div>").addClass('div_blur').prop({
				'id' : ele.prop("id") + "_textdiv"
			}).css({
				'position' : 'relative',
				'width' : _width,
				'overflow' : 'hidden'
			}).appendTo(ele);

			if (this.hasLabel) {  // 有标签
				this.labelDiv = $("<div></div>").css({
					'width' : opts.labelWidth - 2 + "px",
					'position' : 'relative',
					'top' : '2px'
				}).appendTo(ele);

				if (opts.labelAlign == "left") {
					this.labelDiv.css('float','left');
					this.Div_text.css('float','right');
				} else if (opts.labelAlign == "right") {
					this.labelDiv.css('float','right');
					this.Div_text.css('float','left');
				}
				
				this.label = $("<div id='"+ ele.prop("id") +"_label'></div>").label({
		        	left : 0,
		        	top : 0,
		        	text : opts.labelText,
		        	position : 'relative',
		        	className : 'textcomp_normallabel'
		        }).appendTo(this.labelDiv);
			}
		},
		setValgin : function(valgin) {
			this.valgin = valgin;
			this.element.css({
				'display' : 'table-cell',
				'vertical-align' : 'middle'
			});
		},
		/**
		 * 增加checkbox子项
		 * 
		 * @param text 子项的显示值
		 * @param value 子项的真实值
		 * @param checked 设置是否为初始选中项
		 * @param sepWidth checkbox的间隔宽度
		 */
		addCheckbox : function(text, value, checked, sepWidth, imageSrc) {
			var oThis = this;
			// 索引值
			var index = this.checkboxs.length;

			var realWidth = $.argumentutils.getInteger(sepWidth, 0);
			if (text && text != "")
				realWidth += $.measures.getTextWidth(text) + 25;
			
			var imgsrc = '';
			if(imageSrc){
				imgsrc = imageSrc;
				realWidth += this.IMAGE_SIDE_LENGTH;
			}
			if(this.element.parent().outerWidth() < realWidth){
				this.element.parent().css('width',realWidth + "px");
			}
			
			var checkboxDiv = $("<div></div>");
			if (!this.options.changeLine) {
				checkboxDiv.css('float','left');
			}
			checkboxDiv.css({
				'height' : '100%',
				'width' : realWidth + "px",
				'margin-top' : '2px',
				'margin-bottom' : '2px'
			}).appendTo(this.Div_text);

			var checkbox = $("<div id='"+(this.element.prop("id") + "_checkbox_" + index)+"'></div>").checkbox({
				left : 0,
				top : 0,
				width : realWidth - 2,
				text : text,
				checked : checked,
				position : 'relative',
				imgsrc : imgsrc,
				onclick : function(e) {
					oThis.element.focus();
					oThis._trigger("onclick",e);
				},
				valuechanged : function(e,data) {
					var newValue = oThis.getValue();
			   		oThis._trigger("valuechanged",e,{oldValue:oThis.oldValue,newValue:newValue});
			   		oThis.oldValue = newValue;
				}
			}).appendTo(checkboxDiv);
			checkbox.data("value",value);
			this.checkboxs.push(checkbox);
			checkbox.data("index",index);
			this.focusIndex = 0;
		},
		setWidth : function(width) {
        	this.setBounds(null,null,width,null);
        },
		/**
		 * 设置位置
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options,ele = this.element;
			opts.left = left;
			opts.top = top;
			opts.width = $.argumentutils.getString($.measures.convertWidth(width), ele.outerWidth() + "px");
			opts.height = $.argumentutils.getString($.measures.convertHeight(height), ele.outerHeight() + "px");

			// 设置最外层的大小
			ele.css({
				'left' : left + "px",
				'top' : top + "px",
				'width' : opts.width,
				'height' : opts.height
			});

			var tempWidth = 0;
			if ($.argumentutils.isPercent(opts.width))
				tempWidth = ele.outerWidth();
			else
				tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);
			
			this.Div_text.css('width',tempWidth - 4 + "px");
			if (this.hasLabel)
				this.Div_text.css('width',tempWidth - opts.labelWidth - 4 + "px");
		},
		/**
		 * 示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
			this.visible = true;
		},
		/**
		 * 隐藏控件(显示属性是visibility)
		 */
		hideV : function() {
			this._super();
		},
		/**
		 * 得到所有的checkbox项
		 */
		getCheckboxs : function() {
			if (this.checkboxs != null)
				return this.checkboxs;
			else
				return null;
		},
		/**
		 * 清除所有的checkbox
		 */
		clearCheckboxs : function() {
			if (this.checkboxs == null)
				return;
			this.Div_text.empty();

			this.checkboxs = new Array();
			this.focusIndex = -1;
		},
		/**
		 * 清除参数指定的checkbox
		 * 
		 * @param value checkbox的真实值
		 */
		clearCheckbox : function(value) {
			if (value == null || value == "")
				return;
			var checkboxs = this.checkboxs;
			if (checkboxs != null && checkboxs.length > 0) {
				for (var i = 0; i < checkboxs.length; i++) {
					if (checkboxs[i].data("value") == value) {
						checkboxs[i].parent().remove();
						this.checkboxs.splice(i, 0, 1);
						return;
					}
				}
			}
		},
		/**
		 * 设置数据
		 * 
		 * @param comboData 数据内容
		 * @param sepWidth checkbox的间隔宽度
		 */
		setComboData : function(comboData, sepWidth) {
			this.clearCheckboxs();
			if (!comboData)
				return;
			var nameArr = comboData.getNameArray();
			var valueArr = comboData.getValueArray();
			var imageArr = comboData.getImageArray();
			if (nameArr != null) {
				for (var i = 0; i < nameArr.length; i++) {
					var checked = false;
					if (i == (nameArr.length - 1))
						sepWidth = 0;
					this.addCheckbox(nameArr[i], valueArr[i], checked, sepWidth, imageArr[i]);
				}
			}
			this.comboData = comboData;
			if (this.options.disabled == true)  // 设置不可编辑
				this.setActive(false);
		},
		/**
		 * 设置聚焦项
		 * 
		 * @param index 要设置聚焦项的索引值,index小于0表示不设置任何checkbox为聚焦状态
		 */
		setFocusItem : function(index) {
			index = parseInt(index);
			if (isNaN(index) || index > this.checkboxs.length)
				return;

			// index小于0表示不改变任何checkbox为聚焦状态
			if (index < 0) {
				return;
			}

			var checkbox = this.checkboxs[index];
			for (var i = 0; i < this.checkboxs.length; i++) {
				if (i == index)
					this.checkboxs[i].setDivFocus(true);
				else
					this.checkboxs[i].setDivFocus(false);
			}
			this.focusIndex = checkbox.data("index");

//			this.valueChanged(checkbox, this.checkboxs[oldSelectedIndex]);
			
		},
		/**
		 * 获取聚焦项
		 */
		getFocusItem : function() {
			if (this.focusIndex == -1)
				return null;
			return this.checkboxs[this.focusIndex].checkbox('instance');
		},
		/**
		 * 设置value，如果text不是任何一个checkboxs的text，则忽略
		 * 
		 * @param values 要设置的值
		 */
		setValue : function(values) {
			if (values != null && values != "") {
				var valueArr = values.split(",");
				for (var i = 0; i < this.checkboxs.length; i++) {
					var checked = false;
					for (var j = 0; j < valueArr.length; j++) {
						if (valueArr[j].trim() == this.checkboxs[i].data("value")) {
							this.checkboxs[i].checkbox("setChecked",true);
							checked = true;
						}
					}
					if (checked == false)
						this.checkboxs[i].checkbox("setChecked",false);
				}
			}
			else{
				for (var i = 0; i < this.checkboxs.length; i++) {
					this.checkboxs[i].checkbox("setChecked",false);
				}
			}
			var newValue = this.getValue();
			this._trigger('valueChanged',null,{oldValue: this.oldValue,newValue : newValue});
			this.oldValue = this.getValue();
			return true;
		},
		/**
		 * 得到选择项的value值
		 */
		getValue : function() {
			var value = "";
			for (var i = 0; i < this.checkboxs.length; i++) {
				if (this.checkboxs[i].checkbox("getChecked"))
					value += this.checkboxs[i].data("value") + ",";
			}
			if (value.length > 0)
				value = value.substr(0, value.length - 1);
			return value;
		},
		/**
		 * 得到选择项的Text值
		 */
		getText : function() {
			var text = "";
			for (var i = 0; i < this.checkboxs.length; i++) {
				if (this.checkboxs[i].checkbox("getChecked"))
					text += this.checkboxs[i].checkbox("getText") + ",";
			}
			if (text.length > 0)
				text = text.substr(0, text.length - 1);
			return text;
		},
		/**
		 * 得到指定value的索引值
		 * 
		 * @param value 指定的value
		 */
		getValueIndex : function(value) {
			if (value != null) {
				for ( var i = 0; i < this.checkboxs.length; i++) {
					if (this.checkboxs[i].data("value") == value)
						return i;
				}
			}
			return -1;
		},
		/**
		 * 得到聚焦的checkbox的index值
		 */
		getFocusIndex : function() {
			return this.focusIndex;
		},
		/**
		 * 得到选择的checkbox的text
		 */
		getFocusText : function() {
			if (this.focusIndex == -1)
				return null;
			return this.checkboxs[this.focusIndex].checkbox("getText");
		},
		setEditFormular : function(editFormular) {
			this.editFormular = editFormular;
		},
		setValidateFormular : function(validateFormular) {
			this.validateFormular = validateFormular;
		},
		/**
		 * 设置控件的激活状态
		 * 
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			for (var i = 0, n = this.checkboxs.length; i < n; i++) {
				this.checkboxs[i].checkbox("setActive",isActive);
			}
			this.options.disabled = !isActive;
		},
		/**
		 * 设置聚焦
		 */
		setDivFocus : function(isFocus) {
			if (isFocus == false) {
				this.element.blur();
				if (this.checkboxs.length > 0) {
					for (var i = 0; i < this.checkboxs.length; i++) {
						this.checkboxs[i].checkbox("setDivFocus",false);
					}
				}
			} else {
				this.element.focus();
				if (this.checkboxs.length > 0) {
					if (this.focusIndex != -1)
						this.checkboxs[this.focusIndex].checkbox("setDivFocus",true);
					else
						this.checkboxs[0].checkbox("setDivFocus",true);
				}
			}
			return true;
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			if (this.options.disabled){
				this.setActive(true);
			}
			for (var j = 0, m = this.checkboxs.length; j < m; j++) {
				this.checkboxs[j].checkbox("setReadOnly",readOnly);
			}
			this.options.readOnly = readOnly;
		},
		/**
		 * 设置出错时样式
		 * @private
		 */
		setErrorStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text)
					this.Div_text.prop('class','div_error');
			}
		},
		/**
		 * 设置聚焦时样式
		 * @private
		 */
		setFocusStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text && this.Div_text.prop('div_error') != "div_error")
					this.Div_text.prop('class','div_focus');
			}
		},
		/**
		 * 设置焦点移出时样式
		 * @private
		 */
		setBlurStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text && this.Div_text.prop('class') != "div_error")
					this.Div_text.prop('class','div_blur');
			}
		},
		/**
		 * 设置普通样式
		 * @private
		 */
		setNormalStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text)
					this.Div_text.prop('class','div_blur');
			}
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			var context = {
				c : "CheckboxGroupContext",
				id : this.element.prop("id"),
				enabled : !this.options.disabled,
				visible : this.visible,
				comboDataId : (this.comboData == null ? null : this.comboData.id),
				value : this.getValue(),
				readOnly : this.options.readOnly
			};
			// 获取子项Context
			var checkboxs = this.checkboxs;
			if (checkboxs.length > 0) {
				context.checkboxContexts = [];
				for (var i = 0, n = checkboxs.length; i < n; i++) {
					context.checkboxContexts.push(checkboxs[i].checkbox("getContext"));
				}
			}
			return context;
		},
		setVisible : function(isVisible) {
			if(isVisible) {
				this.element.show();
			} else {
				this.element.hide();
			}
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.value && context.value != this.getValue())
				this.setValue(context.value);
			if (context.visible != this.visible) {
				if (context.visible)
					this.showV();
				else
					this.hideV();
			}
			if (context.readOnly != null && this.options.readOnly != context.readOnly){
				this.setReadOnly(context.readOnly);
			}
			// 为子项设置Context
			if (context.checkboxContexts) {
				for (var i = 0, n = context.checkboxContexts.length; i < n; i++) {
					var checkboxContext = context.checkboxContexts[i];
					for (var j = 0, m = this.checkboxs.length; j < m; j++) {
						if (this.checkboxs[j].prop("id") == context.checkboxContexts[i].id)
							this.checkboxs[j].checkbox("setContext",checkboxContext);
					}
				}
			}
		}
	});
})(jQuery);
