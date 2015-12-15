/**
 * @fileoverview Radio集合组件
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * 单选控件组构造方法
	 * @class Radio集合类 
	 * 
	 * @constructor RadioGroupComp的构造函数
	 * @param parent 父控件
	 * @param name 此控件的名字,是对此控件的引用
	 * @param left 控件左部x坐标
	 * @param top 控件顶部y坐标
	 * @param width 控件宽度
	 * @param position 控件的定位属性
	 * @param attrArr 属性对象
	 * @param className css文件的名字
	 */
	$.widget("lui.radiogroup", $.lui.base, {
		options : {
			position : 'relative',
			className : 'radiogroup_div',
			disabled : false,
			readOnly : false,
			tabIndex : 0,
			inputAssistant : '',
			labelText : '',
			labelAlign : 'left',
			labelWidth : 0,
			changeLine : false,
			visible : true,
			radioValue : null,
			//event 
			valuechanged : null,
			onfocus : null,
			onblur : null,
			onenter : null
		},
		_initParam : function() {
			this.componentType = "RADIOGROUP";
			this.parentOwner = this.element.parent()[0];
			// 每个Radio元素的高度
			this.ITEM_HEIGHT = "20px";
			this.IMAGE_SIDE_LENGTH = 15;
			this._super();
			
			
			if ("" != this.options.labelText)
				this.hasLabel = true;
			if (0 == this.options.labelWidth && "" != this.options.labelText)
				this.options.labelWidth = $.measures.getTextWidth(this.options.labelText) + 5;
			
			this.groupId = this.element.attr("id") + "_radio_group";
			// 保存所有的radio子项
			this.radios = [];
			// 保存当前选中的值索引
			this.selectedIndex = -1;
			// 保存上次选中的旧值索引
			this.lastSelectedIndex = -1;
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options,ele = this.element;
			ele.addClass(opts.className).attr('tabindex',opts.tabIndex).css({
				'position' : opts.position,
				'left' : opts.left + "px",
				'top' : opts.top + "px",
				'width' : opts.width,
				'height' : '100%'
			}).on('focus',function(e){
				oThis.isFocus = true;
				oThis._trigger("onfocus",e);
				oThis.setFocusStyle();

			}).on('blur',function(e){
				oThis.isFocus = false;
				oThis._trigger("onblur",e);
				oThis.setBlurStyle();

			}).on('keypress',function(e){
				if (opts.disabled != true) {
					// 获取输入字符
					var keyCode = e.keyCode;
					// 回车事件
					if (keyCode == 13) {
						oThis._trigger("onenter",e);
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
						if (oThis.selectedIndex <= 0) {
							return;
						}
						else
							oThis.setSelectedItem(oThis.selectedIndex - 1);
						return;
					} else if (keyCode == 39) {
						if (oThis.selectedIndex >= oThis.radios.length - 1) {
							return;
						}
						else
							oThis.setSelectedItem(oThis.selectedIndex + 1);
						return;
					}
				}
			});
			
			var _width = '';
			if (this.hasLabel)
				_width = ele.outerWidth() - opts.labelWidth - 4 + "px";
			else
				_width = "100%";

			this.Div_text = $("<div></div>").addClass('div_blur').attr({
				'id': ele.attr("id") + "_textdiv"
			}).css({
				'position': 'relative',
				'width': _width,
				'overflow': 'hidden'
			}).appendTo(ele);

//			if ($.browsersupport.IS_IE7) {
//				this.Div_text.css('text-align', 'left');
//			}

			if (this.hasLabel) {  // 有标签
				this.labelDiv = $("<div></div>").css({
					'width': opts.labelWidth - 2 + "px",
					'position': 'relative',
					'top': '2px'
				}).appendTo(ele);

				if (opts.labelAlign == "left") {
					this.labelDiv.css('float', 'left');
					this.Div_text.css('float', 'right');
				} else if (opts.labelAlign == "right") {
					this.labelDiv.css('float', 'right');
					this.Div_text.css('float', 'left');
				}

				this.label = $("<div id='"+ele.attr("id")+"_label'></div>").label({
					left : 0,
					top : 0,
					text : opts.labelText,
					position : 'relative'
				}).appendTo(this.labelDiv);
			}
		},
		setFormular: function (formular) {
            this.formular = formular;
        },
        valuechanged : function(event) {
        	this._trigger("valueChanged", null, event);
        	if(this.formular) {
        		execFormula(this.viewpart.id,null,this.element.attr('id'),this.extendOpts);
        	}
        },
		/**
		 * 增加radio子项
		 * 
		 * @param text 子项的显示值e
		 * @param value 子项的真实值
		 * @param checked 设置是否为初始选中项
		 * @param sepWidth radio的间隔宽度
		 */
		addRadio : function(text, value, checked, sepWidth, imageSrc) {
			var oThis = this;
			// 索引值
			var index = this.radios.length;
			
			var realWidth = $.argumentutils.getInteger(sepWidth, 0);
			if (text && text != "")
				realWidth += $.measures.getTextWidth(text) + 25;
			if(realWidth < 25)
				realWidth = 25;
			
			if(imageSrc){
				realWidth += this.IMAGE_SIDE_LENGTH;
			}
			
			if(this.element.parent().outerWidth() < realWidth){
				this.element.parent().css('width',realWidth + "px");
			}
			
			var radioDiv = $("<div></div>").css({
				'height' : this.ITEM_HEIGHT,
				'width' : realWidth + "px",
				'margin-top' : '2px',
				'margin-bottom' : '2px'
			}).appendTo(this.Div_text);

			if (!this.changeLine) {
				radioDiv.css('float','left');
			}
			
			var radio = $("<div id='"+this.element.attr("id")+"_radio_"+index+"'></div>").radio({
				left : 0,
				top : 0,
				group : this.groupId,
				value : value,
				text : text,
				checked : checked,
				position : 'relative',
				imgsrc : imageSrc,
				onclick : function(e) {
					oThis.setSelectedItem(index,true);
				}
			}).appendTo(radioDiv);

			
			this.radios.push(radio);
			radio.data("index",index);
			
			if (checked == true)
				this.setSelectedItem(index);
		},
		setWidth : function(width) {
        	this.setBounds(null,null,width,null);
        },
		/**
		 * 设置位置
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options, ele = this.element;
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
				this.Div_text.css('width',tempWidth - opts.labelWidth - 2 + "px");
		},
		/**
		 * 显示控件(显示属性是visibility)
		 */
		showV : function() {
			this.element.css('visibility','');
		},
		/**
		 * 隐藏控件(显示属性是visibility)
		 */
		hideV : function() {
			this.element.css('visibility','hidden');
		},
		/**
		 * 得到所有的radio项
		 */
		getRadios : function() {
			if (this.radios != null)
				return this.radios;
			else
				return null;
		},
		/**
		 * 清除所有的radio
		 */
		clearRadios : function() {
			if (this.radios == null)
				return;
			this.Div_text.empty();

			this.radios = [];
			this.selectedIndex = -1;
		},
		/**
		 * 清除参数指定的radio
		 * 
		 * @param value radio的真实值
		 */
		clearRadio : function(value) {
			if (value == null || value == "")
				return;
			var radios = this.radios;
			if (radios != null && radios.length > 0) {
				for (var i = 0; i < radios.length; i++) {
					if (radios[i].radio("getValue") == value) {
						radios[i].parent().remove();
						this.radios.splice(i, 0, 1);
						return;
					}
				}
			}
		},
		/**
		 * 设置数据
		 * 
		 * @param comboData 数据内容
		 * @param sepWidth radio的间隔宽度
		 */
		setComboData : function(comboData, sepWidth) {
			this.clearRadios();
			if (!comboData)
				return;
			var nameArr = comboData.getNameArray();
			var valueArr = comboData.getValueArray();
			var imageArr = comboData.getImageArray();
			if (nameArr != null) {
				for (var i = 0; i < nameArr.length; i++) {
					var checked = false;
//					if (i == 0)  // 默认选中第一条
//						checked = true;
					if (i == (nameArr.length - 1))
						sepWidth = 0;
					this.addRadio(nameArr[i], valueArr[i], checked, sepWidth, imageArr[i]);
				}
				if(this.options.radioValue && this.options.radioValue != null){
					this.setValue(this.options.radioValue);
				}
			}
			this.comboData = comboData;
			if (this.options.disabled)  // 设置不可编辑
				this.setActive(false);
		},
		/**
		 * 设置选中项
		 * 
		 * @param index 要设置选中项的索引值,index小于0表示不设置任何radio为选中状态
		 * @param isDefaultEvent 是否是radio onclick事件触发
		 */
		setSelectedItem : function(index,isDefaultEvent) {
			index = parseInt(index);
			if (isNaN(index) || index > this.radios.length)
				return;
			// index小于0表示不改变任何radio为选中状态
			if (index < 0) {
				return;
			}
			var radio = this.radios[index];
			if(!isDefaultEvent) {
				radio.radio("setChecked",true);
			} else {
				var oldSelectedIndex = this.selectedIndex;
				this.selectedIndex = radio.data("index");
				
				radio.radio("setChecked",true);
				if (index != oldSelectedIndex) {
					this.value = radio.radio("getValue");
					this.text = radio.radio("getText");
					// 选中项改变调用用户重载的方法
					this.valuechanged({
						newValue : radio.radio("getValue"),
						oldValue : this.radios[oldSelectedIndex] == null ? null : this.radios[oldSelectedIndex].radio("getValue")
					});
					if(this.formater)
						execFormula("RADIOGROUP", null, this.element.attr("id"));
				}
			}
		},
		/**
		 * 获取选中项
		 */
		getSelectedItem : function() {
			if (this.selectedIndex == -1)
				return null;
			return this.radios[this.selectedIndex];
		},
		/**
		 * 设置value,如果value不是任何一个radios的value,则设置为空
		 * 
		 * @param value 要设置的值
		 */
		setValue : function(value) {
			for (var i = 0; i < this.radios.length; i++) {
				if (value == this.radios[i].radio("getValue")) {
					this.setSelectedItem(i, true);
					return true;
				}
			}
//			var selectItem = this.getSelectedItem();
//			if (selectItem)
//				selectItem.radio("setChecked",false);
//			this.value = value;
			return false;	
		},
		/**
		 * 得到选择项的value值
		 */
		getValue : function() {
			return this.value;
		},
		/**
		 * 得到选择项的Text值
		 */
		getText : function() {
			return this.text;
		},
		/**
		 * 得到指定value的索引值
		 * 
		 * @param value 指定的value
		 */
		getValueIndex : function(value) {
			if (value != null) {
				for ( var i = 0; i < this.radios.length; i++) {
					if (this.radios[i].radio("getValue") == value)
						return i;
				}
			}
			return -1;
		},
		/**
		 * 得到选中的radio的index值
		 */
		getSelectedIndex : function() {
			return this.selectedIndex;
		},
		/**
		 * 得到选择的radio的text
		 */
		getSelectedText : function() {
			if (this.selectedIndex == -1)
				return null;
			return this.radios[this.selectedIndex].radio("getText");
		},
		/**
		 * 设置控件的激活状态.
		 * 
		 * @param isActive true表示处于激活状态,否则表示禁用状态
		 */
		setActive : function(isActive) {
			for (var i = 0, n = this.radios.length; i < n; i++) {
				this.radios[i].radio("setActive",isActive);
			}
			this.options.disabled = !isActive;
		},
		setValgin : function(valgin) {
			this.valgin = valgin;
			this.element.css({
				'display' : 'table-cell',
				'vertical-align' : 'middle'
			});
		},
		/**
		 * 设置聚焦
		 */
		setDivFocus : function(isFocus) {
			if (isFocus == false)
				this.element.blur();
			else {
				this.element.focus();
			}
			return true;
		},
		/**
		 * 设置出错时样式
		 * @private
		 */
		setErrorStyle : function() {
			if(!this.options.readOnly) {
				this.Div_text.attr('class','div_error');
			}
		},
		/**
		 * 设置出错标志
		 * @private
		 */
		setError : function(flag) {
		},

		/**
		 * 设置出错时提示信息
		 * @private
		 */
		setErrorMessage : function(msg) {
		},
		/**
		 * 设置聚焦时样式
		 * @private
		 */
		setFocusStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text && this.Div_text.attr('class') != "div_error")
					this.Div_text.attr('class','div_focus');
			}
		},
		/**
		 * 设置焦点移出时样式
		 * @private
		 */
		setBlurStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text && this.Div_text.attr('class') != "div_error")
					this.Div_text.attr('class','div_blur');
			}
		},
		/**
		 * 设置普通样式
		 * @private
		 */
		setNormalStyle : function() {
			if(!this.options.readOnly) {
				if (this.Div_text)
					this.Div_text.attr('class','div_blur');
			}
		},
		/**
		 * 设置只读状态
		 */
		setReadOnly : function(readOnly) {
			for (var j = 0, m = this.radios.length; j < m; j++) {
				this.radios[j].radio("setReadOnly",readOnly);
			}
			this.options.readOnly = readOnly;
		},
		/**
		 * 设置显示隐藏状态
		 * @param visible
		 */
		setVisible : function(visible) {
			if (visible != null && this.options.visible != visible) {
				if (visible == true)
					this.showV();
				else
					this.hideV();
				this.options.visible = visible;
			}
			this.ctxChanged = true;
		},
		/**
		 * 获取对象信息
		 * @private
		 */
		getContext : function() {
			return {
				c : "RadioGroupContext",
				id : this.element.attr("id"),
				enabled : !this.options.disabled,
				comboDataId : this.comboData == null ? null : this.comboData.id,
				index : this.selectedIndex,
				value : this.getValue(),
				readOnly : this.options.readOnly
			};
		},
		/**
		 * 设置对象信息
		 * @private
		 */
		setContext : function(context) {
			if (context.enabled != null)
				this.setActive(context.enabled);
			if (context.visible != null && context.visible != this.visible)
				this.setVisible(context.visible);
			if (context.value && context.value != this.getValue())
				this.setValue(context.value);
			if (context.readOnly != null && this.options.readOnly != context.readOnly)
				this.setReadOnly(context.readOnly);
		}
	});
})(jQuery);
