/**
 * @fileoverview
 * 
 * @author lxl
 * @version lui 1.0
 * 
 */
(function($) {
	/**
	 * Input对话框构造函数
	 * @class Input对话框类。<br>
	 *        Input对话框是对ModalDialog的封装，在对话框底部添加了OK，
	 *        Cancel按钮。使用者只需要给出ContentPane部分内容信息即可。<br>
	 *        <b>组件皮肤设置说明：</b><br>
	 *        inputdialog.css是对InputDialog中显示字体的外观控制，如字体、大小等。<br>
	 *        modaldialog.css是对InputDialog外观整体的控制，包括标题背景，内容区背景等。<br>
	 *        <br>
	 * 
	 * <b>使用js，直接通过new的用法生成InputDialog实例：</b><br>
	 * inputdialog = InputDialogComp.showDialog(text); // 用需要显示的字符串代替text<br>
	 * inputdialog.onOk = input; //把点击确认按钮后需要执行的函数名赋给onOk属性<br>
	 * inputdialog.onCancel = noinput; //把点击取消需要执行的函数名赋给onCancel属性<br>
	 * 
	 * @constructor InputDialogComp的构造函数
	 * @param name 对话框名字
	 * @param title 对话框标题
	 * @param labelWidth label的宽度
	 * @param okFunc 点击确定按钮时调用的函数名
	 * @param cancelFunc 点击取消按钮时调用的函数名
	 * @param obj1 调用okFunc函数的对象
	 * @param obj2 调用cancelFunc函数的对象
	 * @param{boolean} another 由于确认对话框生命周期的特殊性,特殊时候需要另一个实例
	 */
	$.widget("lui.inputdialog", {
		options : {
			name : '',
			title : 'Dialog',
			left : 0,
			top : 0,
			width : '',
			height : '',
			labelWidth : '',
			okFunc : null,
			cancelFunc : null,
			obj1 : null,
			obj2 : null,
			zIndex : null,
			another : null,
			buttonDivStyle : 'button_div',
			//event
			onok : null,
			oncancel : null
		},
		_initParam : function() {
			this.componentType = "INPUTDIALOG";
			this.HEIGHT = 90;
			this.WIDTH = 348;
			this.LABEL_WIDTH = 120;
			this.ITEM_HEIGHT = 24;
			this.STRING_TYPE = "string";
			this.PSWTEXT_TYPE = "pswtext";
			this.INT_TYPE = "int";
			this.DECIMAL_TYPE = "decimal";
			this.COMBO_TYPE = "combo";
			this.RADIO_TYPE = "radio";
			this.LABEL_TYPE = "label";
			
			this.id = this.options.name || this.element.attr("id");
			this.width = this.options.width ? this.options.width : this.WIDTH;
			this.height = this.options.height ? this.options.height : this.HEIGHT;
			if (this.options.height)  // 固定高度
				this.fixHeight = true;
			this.labelWidth = this.options.labelWidth ? this.options.labelWidth : this.LABEL_WIDTH;
			// 输入项集合
			this.items = $.hashmap.getObj();
		},
		_create : function() {
			this._initParam();
			var oThis = this, opts = this.options, ele = this.element;
			this.modaldialog = $("<div></div>").appendTo("body").modaldialog({
				name : opts.name,
				title : opts.title,
				left : opts.left,
				top : opts.top,
				width : this.width,
				height : this.height
			}).modaldialog("instance");
				
			this.modaldialog.contentDiv.css('overflow','hidden');

			//初始化错误提示对象
			this.initWholeErrorMsgDiv();
			
			this.contentDiv = $("<div></div>").addClass('input_contentdiv').appendTo(this.modaldialog.getContentPane());
			if (this.fixHeight) {
				this.contentDiv.css({
					'height' : this.height - 105 + "px",
					'overflow-y' : 'auto'
				});
			}

			this.bottomDiv = $("<div></div>").addClass('input_bottomdiv').appendTo(this.modaldialog.getContentPane());

			this.cancelBtDiv = $("<div></div>").addClass('input_cancelbtdiv').appendTo(this.bottomDiv);
			this.okBtDiv = $("<div></div>").addClass('input_okbtdiv').appendTo(this.bottomDiv);
			
//			if (!$.browsersupport.IS_IE) {
				this.okBtDiv.css('text-align','-moz-right');
				this.cancelBtDiv.css('text-align','-moz-left');
//			}
//			if ($.browsersupport.IS_IE6) {
//				this.okBtDiv.css('padding-top','3px');
//				this.cancelBtDiv.css('padding-top','3px');
//			}

			// 生成确定按钮
			this.okBt = $("<div id=\"okBt\"></div>").button({
				top : 0,
				width : 74,
				text : trans("ml_ok"),
				position : 'relative',
				className : opts.buttonOKStyle,
				onclick : function(e) {
					var items = oThis.getItems();
					if(items && items.length > 0){
						oThis.allErrorItemNames = '';
						var item = null;
						var keys = items.keySet();
						for(var i=0; i<keys.length; i++){
							item = items.get(keys[i]);
							if(typeof(item) != "undefined" && typeof(item.input) != "undefined"){
								item.input.triggerHandler('blur');
							}
						}
						if(oThis.allErrorItemNames.length > 0){
							oThis.errorMsg.html(trans("ml_inputdialog_save_fail") + oThis.allErrorItemNames.replace(trans("ml_inputdialog_mark1"), ""));
							oThis.errorMsgDiv.show();
							return;
						}else{
							oThis.errorMsgDiv.hide();
							oThis.errorMsg.html('');
						}
					}
					if (opts.okFunc) {
						if (opts.obj1)
							opts.okFunc.call(opts.obj1);
						else
							opts.okFunc();
					}
					oThis.hide();
				}
			}).appendTo(this.okBtDiv);
				
			// 生成取消按钮
			this.cancelBt = $("<div id=\"cancelBt\"></div>").button({
				top : 0,
				width : 74,
				text : trans("ml_cancel"),
				position : 'relative',
				className : 'button_div',
				onclick : function(e) {
					if (opts.cancelFunc) {
						if (opts.obj2)
							opts.cancelFunc.call(opts.obj2);
						else
							opts.cancelFunc();
					}
					oThis.hide();
				}
			}).appendTo(this.cancelBtDiv);
				
			this.modaldialog.close = function(){
				oThis.close();
			};

//			if ($.browsersupport.IS_IE8) {
//				this.okBt.css('float','right');
//			}
			
			if (this.zIndex)
				this.modaldialog.element.css('z-index',this.zIndex);
		},
		/**
		 * 增加输入框
		 * 
		 * @param labelText label显示文字
		 * @param inputId 输入框ID
		 * @param inputType 输入框类型（string, int, combo）
		 * @param isMust 是否必填
		 * @param attrArr 输入框额外属性
		 * @param attrArr.maxValue 整型输入框时的最大值
		 * @param attrArr.minValue 整型输入框时的最小值
		 * @param attrArr.precision 数字型输入框时的精确度
		 * @param attrArr.selectOnly 下拉输入框时是否只能选择（不能手工输入）
		 * @param attrArr.comboData 下拉输入框时的内容
		 */
		addItem : function(labelText, inputId, inputType, isMust, attrArr, value,className) {
			var oThis = this,opts = this.options,ele = this.element;
			// 创建子项
			var itemDiv = $("<div></div>").addClass('input_itemdiv').appendTo(this.contentDiv);
			if (!this.fixHeight) {  // 不是固定高度
				if (inputType == this.RADIO_TYPE){
					if (attrArr && attrArr.comboData) {
						this.height += this.ITEM_HEIGHT * attrArr.comboData.dataItems.length;		
						this.contentDiv.css('height',(this.contentDiv.outerHeight() + this.ITEM_HEIGHT * attrArr.comboData.dataItems.length) + 60 + "px");
					}
				}
				else{
					this.height += this.ITEM_HEIGHT;
					this.contentDiv.css('height',this.contentDiv.outerHeight() + this.ITEM_HEIGHT + "px");
				}
				this.modaldialog.setSize(this.width, this.height);
			}
			
			// 左边Label区域
			itemDiv.leftDiv = $("<div></div>").addClass('input_leftdiv').css({
				'width' : opts.labelWidth + "px"
			}).appendTo(itemDiv);
			if (inputType == this.LABEL_TYPE){
				itemDiv.leftDiv.css('width','0');
			}
			else
				var label = $("<div></div>").label({
					left : 0,
					text : labelText,
					position : 'relative',
					textAlign : 'right'
				}).appendTo(itemDiv.leftDiv);
			// 必输项提示
			if (isMust == true) {
				itemDiv.mustDiv = $("<div></div>").addClass('input_mustdiv').html("<font color='red'>*</font>").appendTo(itemDiv);
			}
			// 右边输入区域
			itemDiv.rightDiv = $("<div></div>").addClass('input_rightdiv').appendTo(itemDiv).css({
				'width' : itemDiv.outerWidth() - itemDiv.leftDiv.outerWidth() - 30 + "px"
			});

			var inputComp;
			var inputWidth = 210;//this.width - this.labelWidth - 60 - 40 + "px";
			var commOpts = {
				top : 0,
				left : 5,
				width : inputWidth,
				position : 'relative'
			};
			if (inputType == this.STRING_TYPE) {  // 字符串输入框
				inputComp = $("<div id=\""+inputId+"\"></div>").stringtext($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).stringtext('instance');
			}else if (inputType == this.PSWTEXT_TYPE) {  // 整型输入框
				inputComp = $("<div id=\""+inputId+"\"></div>").pswtext($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).pswtext('instance');
			} else if (inputType == this.INT_TYPE) {  // 整型输入框
				inputComp = $("<div id=\""+inputId+"\"></div>").integertext($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).integertext('instance');
			}else if (inputType == this.LABEL_TYPE) {  // 标签
				inputComp = $("<div id=\""+inputId+"\"></div>").label({
					top : 7,
					left : 5,
					text : labelText,
					position : 'relative',
					className : className
				}).appendTo(itemDiv.rightDiv).label('instance');
			} else if (inputType == this.DECIMAL_TYPE) {
				inputComp = $("<div id=\""+inputId+"\"></div>").floattext($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).floattext('instance');
			} 
			else if (inputType == this.COMBO_TYPE) {  // 下拉输入框
				inputComp = $("<div id=\""+inputId+"\"></div>").combo($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).combo('instance');
				if (attrArr && attrArr.comboData) {
					inputComp.setComboData(attrArr.comboData);
					if (attrArr.selectIndex)
						inputComp.setSelectedItem(attrArr.selectIndex);
				}
			} 
			else if (inputType == this.RADIO_TYPE){
				attrArr.changeLine = true;
				inputComp = $("<div id=\""+inputId+"\"></div>").radiogroup($.extend({},commOpts,attrArr)).appendTo(itemDiv.rightDiv).radiogroup('instance');
				if (attrArr && attrArr.comboData) {
					inputComp.setComboData(attrArr.comboData);
					if (attrArr.selectIndex)
						inputComp.setSelectedItem(attrArr.selectIndex);
				}
			}
			if (inputComp){
				inputComp.isMust = isMust == true ? true : false;
				inputComp.inputDialogLabelText = labelText;
				if(typeof(inputComp.input) == "object"){
					inputComp.input.on('blur',function(e){
						//inputComp.input.triggerHandler('blur');
						if (inputComp.isMust && (inputComp.input.val() == null || inputComp.input.val() == "")) {
							if(typeof(inputComp.setError) == 'function'){
								inputComp.setError(true);
							}
							if(typeof(inputComp.setErrorMessage) == 'function'){
								inputComp.setErrorMessage(trans('ml_thisfieldcannotnull'));
							}
							if(typeof(inputComp.setErrorStyle) == 'function'){
								inputComp.setErrorStyle();
							}
							if(typeof(inputComp.setErrorPosition) == 'function'){
								inputComp.setErrorPosition(oThis.modaldialog.getContentPane());
							}
							oThis.allErrorItemNames += trans("ml_inputdialog_mark1") + trans("ml_inputdialog_mark2") + inputComp.inputDialogLabelText + trans("ml_inputdialog_mark3");
						}else{
							if(typeof(inputComp.setError) == 'function'){
								inputComp.setError(false);
							}
							if(typeof(inputComp.setErrorMessage) == 'function'){
								inputComp.setErrorMessage('');
							}
							if(typeof(inputComp.setNormalStyle) == 'function'){
								inputComp.setNormalStyle();
							}
						}
					});
				}
				this.items.put(inputId, inputComp);
			}
			
			if(value != null)
				inputComp.setValue(value);
			
			if(!this.fixHeight){
				var sumHeight = this.ITEM_HEIGHT * this.getItems().length;
				this.contentDiv.css('height',sumHeight + "px");
				//40——对话框头部高度 60——中间内容距上下间距和 8——底部按钮距下间距 18——底部高度
				this.modaldialog.setSize(this.width, 40 + sumHeight + 60 + this.bottomDiv.outerHeight());
			}
		},
		/**
		 * 获取所有输入子项集合
		 */
		getItems : function() {
			return this.items;
		},
		/**
		 * 根据ID获取输入子项
		 */
		getItem : function(id) {
			if (this.items.containsKey(id))
				return this.items.get(id);
			else
				return null;
		},
		/**
		 * 关闭对话框
		 * @private
		 */
		close : function() {
			this._trigger("oncancel",null);
			this.hide();
		},
		/**
		 * 得到要添加内容的面板
		 */
		getContentPane : function() {
			return this.contentDiv;
		},
		/**
		 * 显示对话框
		 * 
		 * @private
		 */
		show : function() {
			this.modaldialog.show();
		},
		/**
		 * 隐藏对话框
		 * 
		 * @private
		 */
		hide : function() {
			this.modaldialog.hide();
		},
		initWholeErrorMsgDiv : function(isUp) {
			var oThis = this;
			this.allErrorItemNames = '';
			this.errorMsgDiv = $("<div></div>").addClass('inputdialog_error_whole_msg_div').attr({
				'id' : this.id + "_error_whole_msg_id"
			}).hide();

			var prefix = "down_";
			if(isUp){
				prefix = "up_";
				this.errorMsgDiv.css('top','30px');
			}
			//九宫格
			this.wholeMsgDiv = $("<div></div>").addClass('whole_msg_div').appendTo(this.errorMsgDiv);
			//左上
			this.leftTopDiv = $("<div></div>").addClass(prefix + "bg_left_top").appendTo(this.wholeMsgDiv);
			//上
			this.topMiddleDiv = $("<div></div>").addClass(prefix + "bg_top_middle").appendTo(this.wholeMsgDiv);
			//右上
			this.rightTopDiv = $("<div></div>").addClass(prefix + "bg_right_top").appendTo(this.wholeMsgDiv);
			//右中
			this.rightMiddleDiv = $("<div></div>").addClass(prefix + "bg_right_middle").appendTo(this.wholeMsgDiv);
			//右下
			this.rightBottomDiv = $("<div></div>").addClass(prefix + "bg_right_bottom").appendTo(this.wholeMsgDiv);
			//下
			this.bottomMiddleDiv = $("<div></div>").addClass(prefix + "bg_bottom_middle").appendTo(this.wholeMsgDiv);
			//左下
			this.leftBottomDiv = $("<div></div>").addClass(prefix + "bg_left_bottom").appendTo(this.wholeMsgDiv);
			//左中
			this.leftMiddleDiv = $("<div></div>").addClass(prefix + "bg_left_middle").appendTo(this.wholeMsgDiv);

			this.errorCenterDiv = $("<div></div>").addClass(prefix + "error_center_div").appendTo(this.wholeMsgDiv);

			this.errorMsg = $("<div></div>").addClass('errorMsg').appendTo(this.errorCenterDiv);

			this.warningIcon = $("<div></div>").addClass(prefix + "warning").appendTo(this.wholeMsgDiv);

			this.closeIcon =  $("<div></div>").addClass(prefix + "close_normal").on('mouseover',function(e){
				$(this).attr('class',prefix + "close_press");
			}).on('mouseout',function(e){
				$(this).attr('class',prefix + "close_normal");
			}).on('mouseup',function(e){
				$(this).attr('class',prefix + "close_normal");
				oThis.errorMsgDiv.hide();
			}).appendTo(this.wholeMsgDiv);

			this.modaldialog.getContentPane().append(this.errorMsgDiv);
		}
	});
	
	$.inputDialogComp = {
		hideDialog : function() {
			var dialog = window.globalObject.$c_InputDialog;
			dialog.hide();
		}
	};
})(jQuery);
