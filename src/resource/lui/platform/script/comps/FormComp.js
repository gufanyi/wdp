/**
 * Form表单控件
 * 
 * @version lui 1.0
 */
(function($) {
	$.autoFormComp = {
		firstForm : null,
		repaintRt : null,
		/**
		 * 只针对type = 4
		 * 
		 * @param {} id
		 */
		resize :function(id) {
		    alert('resize');
		    var ele = window.objects[id];
		    try {
		        $.autoFormComp.type4Resize(ele);
		    } catch(e) {}
		},
		type4Resize : function(ele) {
		    if (ele == null) return;
		    if (ele.outerDiv) {
		        if (ele.formWidth && ele.formWidth.indexOf("%") != -1) {
		            var outerWidth = parseInt(ele.formWidth) * ele.outerDiv.parent().outerWidth() / 100;
		            ele.outerDiv.css('width',outerWidth + "px");
		        }
		    }
		},
		/**
		 * 设置元素的错误信息
		 * 
		 * @param element 元素对象
		 * @param type 类型（"error"、"warning"、"normal"）
		 * @param message 信息
		 * @param renderType 布局类型（1-固定布局；2-流式布局；3-固定提示布局）
		 * 
		 */
		setElementErrorMessage : function(element, type, message, renderType, formcomp) {
		    if (renderType == 3) { // 固定提示布局类型
		        var messageDiv = null;
		        if (element.element.parent().next() && element.element.parent().next().next()) 
		        	messageDiv = element.element.parent().next().next();
		        if (messageDiv == null) return;
		        if (type == BASE.ELEMENT_ERROR) {
		            messageDiv.css('color','red');
		            var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='" + window.themePath + "/images/text/error.gif" + "'/></div>";
//		            if (!$.browsersupport.IS_IE) 
		                 innerHTML = innerHTML + "<div style='float:left;padding-top:1px'>&nbsp;" + message + "</div>";
//		            else innerHTML = innerHTML + "<div style='float:left;padding-top:1px'>&nbsp;&nbsp;&nbsp;" + message + "</div>";
		            messageDiv.html(innerHTML);
		        } else if (type == BASE.ELEMENT_WARNING) {
		            messageDiv.css('color','#F99F0F');
		            var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='" + window.themePath + "/images/text/warning.gif" + "'/></div>";
//		            if (!$.browsersupport.IS_IE) 
		                  innerHTML = innerHTML + "<div style='float:left;padding-top:1px'>&nbsp;" + message + "</div>";
//		            else innerHTML = innerHTML + "<div style='float:left;padding-top:1px'>&nbsp;&nbsp;&nbsp;" + message + "</div>";
		            messageDiv.html(innerHTML);
		        } else if (type == BASE.ELEMENT_SUCCESS) {
		            messageDiv.css('color','#000000');
		            var innerHTML = "<div style='float:left'><img style='height:16px;width:16px;margin-top:0px;' src='" + window.themePath + "/images/text/success.gif" + "'/></div>";
		            messageDiv.html(innerHTML);
		        } else if (type == BASE.ELEMENT_NORMAL) {
		            messageDiv.css('color','gray');
		            messageDiv.html(message);
		        }
		    } else {
		        if (type == BASE.ELEMENT_ERROR) {
		            if (typeof(element) == "object" && typeof(element.errorMsgDiv) != 'undefined') {
		                if (typeof(element.setError) == 'function') {
		                    element.setError(true);
		                }
		                if (typeof(element.setErrorMessage) == 'function') {
		                    element.setErrorMessage(message);
		                }
		                if (typeof(element.setErrorStyle) == 'function') {
		                    element.setErrorStyle();
		                }
		                if (typeof(element.setErrorPosition) == 'function') {
		                    element.setErrorPosition();
		                }
		            }
		        } else if (type == BASE.ELEMENT_WARNING) {
		            if (typeof(element.setError) == 'function') {}
		        } else if (type == BASE.ELEMENT_SUCCESS) {
		            if (typeof(element) == "object") {
		                if (typeof(element.setError) == 'function') {
		                    element.setError(false);
		                }
		                if (typeof(element.setErrorMessage) == 'function') {
		                    element.setErrorMessage("");
		                }
		                if (typeof(element.setNormalStyle) == 'function') {
		                    element.setNormalStyle();
		                }
		            }
		        } else if (type == BASE.ELEMENT_NORMAL) {
		            if (typeof(element.setError) == 'function') {}
		        }
		    }
		},
		/**
		 * 隐藏整体错误信息框
		 */
		hideErrorMsg : function(widgetId, componentId) {
		    var widget = pageUI.getViewPart(widgetId);
		    if (widget) {
		        var component = widget.getComponent(componentId);
		        if (component && 'AUTOFORM' == component.componentType) {
		            if (component.errorMsgDiv) {
		                component.errorMsgDiv.hide();
		            }
		        }
		    }
		},
		repaintSelf : function(widgetId, formid) {
		    var form = pageUI.getViewPart(widgetId).getComponent(formid);
		    form.pLayout.paint(true);
		},
		firstEleFocus : function() {
		    if ($.autoFormComp.focusFirstEle == true) return;
		    if ($.autoFormComp.firstForm == null) return;
		    var form = $.autoFormComp.firstForm;
		    if (form.editable == false) return;

		    for (var i = 0; i < form.eleArr.length; i++) {
		        var ele = form.eleArr[i];
		        if (ele.visible != null && ele.visible == true && ele.disabled != null && ele.disabled != true && ele.componentType != "SELFDEFELEMENT" && ele.componentType != "EDITOR") { // 元素可用
		            if (ele.setDivFocus) // RadioGroup和Checkbox的聚焦
		            ele.setDivFocus(true);
		            else if (ele.input != null) {
		                try { //为了屏蔽撤销注解的脚本错误
		                    ele.input.focus();
		                } catch(e) {}
		            } else if (ele.textArea != null) ele.textArea.focus();
		            break;
		        }
		    }
		}
	};
	
	/**
	 * 表单构造函数 renderType 
	 * 1 固定布局居中 
	 * 2 流式布局 
	 * 3 固定提示布局 
	 * 4 自由表单 
	 * 5 固定布局右对齐 
	 * 6 只读布局 
	 * 
	 * @class 表单控件
	 */
	$.widget("lui.autoform", {
		options : {
			parentDiv : null, //div id
			id : '',
			renderType :null,
			renderHiddenEle : false,
			rowHeight : null,
			columnCount : 2,
			labelPosition : 'left',
			eleWidth : 120,
			isShowLine : true,
			labelMinWidth : 80,
			formRender : null,
			ellipsis : false,
			//event
			onSuccess : null,
			onFailed : null
		},
		_initParam : function() {
			//定义componentType
			this.componentType = "AUTOFORM";
			// 额外的空白高度
			this.SPACE_HEIGHT = 1;
			this.focused = false;
			
		    // 分别保存当前Form中元素对dataset的绑定索引,绑定key和绑定元素,这三者是一一对应的
		    this.indiceArr = null;
		    this.keyArr = [];
		    // 子项集合
		    this.eleArr = [];
		    // 用来记录控件的原始可编辑性
		    this.disableArr = [];
		    // 用来记录控件的原始只读属性
		    this.readOnlyArr = [];
		    this.dataset = null;
		    this.rowIndex = -1;
		    this.editable = true;
		    this.readOnly = false;
		    this.tmpEditable = true;
		    // 记录违反了数据校验规则还没有修改正确的行,列及违反的规则
		    this.dataCheckErrorRows = null;
		    //初始化错误提示对象
		    this.initWholeErrorMsgDiv();
		    var oThis = this;
		    //自由表单
		    if (this.options.renderType == 4) {
		        this.pLayout = {};
		        this.pLayout.paint = function() {};
		    }
		    //流式布局
		    else if (this.options.renderType == 2) {
		        this.pLayout = $("<div id=\"flowGrid_"+this.options.id+"\">").flowgridlayout({
		        	parent : this.options.parentDiv, //parent id
		        	position : 'relative',
		        	scroll : true,
		        	compDefaultWidth : 120,
		        	marginTop : 10
		        }).flowgridlayout('instance');
		        	
		        this.pLayout.element.css('height','100px');
		        this.pLayout.labelPosition = this.options.labelPosition;
		        this.pLayout.ellipsis = this.options.ellipsis;
		        this.pLayout.afterRepaint = function() {
		            oThis.afterRepaint();
		        };
		    }
		    //固定布局
		    else if (this.options.renderType == 1 || this.options.renderType == 3 || this.options.renderType == 5) {
		        this.pLayout = $("<div id=\"fixedGrid_"+this.options.id+"\">").fixedgridlayout({
		        	parent : this.options.parentDiv, //parent id
		        	position : 'relative',
		        	scroll : true,
		        	compDefaultWidth : this.options.eleWidth,
		        	marginTop : 10,
		        	rowHeight : this.options.rowHeight,
		        	columnCount : this.options.columnCount,
		        	renderType : this.options.renderType
		        }).fixedgridlayout('instance');
		        	
		        this.pLayout.labelPosition = this.options.labelPosition;
		    }
		    //只读布局
		    else if (this.options.renderType == 6) {
		        this.pLayout = new ReadGridLayout(this.options.parentDiv, "readGrid_" + id, 0, 0, "100%", "100%", "relative", true, this.options.eleWidth, 10, rowHeight, columnCount, this.options.formRender);
		        this.pLayout.eleWidth = this.options.eleWidth;
		        this.pLayout.labelMinWidth = this.options.labelMinWidth;
		    }
		    this.viewpart = null;
		    if ($.autoFormComp.firstForm == null) $.autoFormComp.firstForm = this;
		},
		_create : function() {
			this._initParam();
		},
		/**
		 * 重新绘制后执行方法
		 * 
		 * @private
		 */
		afterRepaint : function() {
		    var childNodes = $('#'+this.options.parentDiv).children();
		    var currNode = null;
		    if (childNodes != null) {
		        for (var j = 0; j < childNodes.length; j++) {
		            var child = childNodes.eq(j);
//		            if (!$.browsersupport.IS_IE) {
		                if (child[0].nodeName == "#text") continue;
//		            }
		            currNode = child;
		        }
		    }
		    if (currNode == null) {
		        return;
		    }
		    // 设置外层DIV高度
		    currNode.css('height',this.getHeight() + this.SPACE_HEIGHT + "px");
		},
		removeElementById : function(id) {
		    if (this.options.renderType == 4) { // 自由表单删除 renxh
		        var comp = this.getElement(id);
		        if (comp) {
		            var newEles = [];
		            var newKeyEles = [];
		            for (var i = 0; i < this.eleArr.length; i++) {
		                if (this.eleArr[i] != comp) {
		                    newEles.push(this.eleArr[i]);
		                    newKeyEles.push(this.eleArr[i].element.data('eleid'));
		                }
		            }
		            this.eleArr = newEles;
		            this.keyArr = newKeyEles;
		            if (comp.attachedLabel) {
//		                var labelP = comp.attachedLabel.parentNode;
//		                if (labelP) labelP.removeChild(comp.attachedLabel);
		            	$(comp.attachedLabel).remove();
		            }
//		            var Div_gen = comp.getObjHtml();
//		            if (Div_gen) {
//		                var p = Div_gen.parentNode;
//		                p.removeChild(Div_gen);
//		            }
		            //comp.element.remvoe();
		            
		            //TODO:执行了pagePart的execDynamicScript2RemoveFormElement2方法
//		            var autoFormEleDom = comp.element.parent().parent();
//		            if(autoFormEleDom.attr('id').indexOf(id+"_ele")>=0) {
//		            	autoFormEleDom.remove();
//		            }
		        }

		    } else {
		        var comp = this.pLayout.removeCompById(this.options.id + id);
		        if (comp) {
		            var newEles = [];
		            for (var i = 0; i < this.eleArr.length; i++) {
		                if (this.eleArr[i] != comp) {
		                    newEles.push(this.eleArr[i]);
		                }
		            }
		            this.eleArr = newEles;
		            if (comp.attachedLabel) {
//		                var labelP = comp.attachedLabel.parentNode;
//		                if (labelP) labelP.removeChild(comp.attachedLabel);
		            	$(comp.attachedLabel).remove();
		            }
//		            var Div_gen = comp.getObjHtml();
//		            if (Div_gen) {
//		                var p = Div_gen.parentNode;
//		                p.removeChild(Div_gen);
//		            }
		            comp.destroy();
		        }
		    }
		},
		/**
		 * 创建子项
		 * 
		 * eleWidth 控件宽度
		 * width 自由表单时 FormElement整体宽度
		 * 
		 */
		createElement : function(eleId, field, eleWidth, height, rowSpan, colSpan, type, 
				userObject, disabled, readOnly, dataset, labelName, labelColor, labelPos, nextLine, 
				required, tip, inputAssistant, showTip, description, isAttachNext, className, 
				formula, eleDiv, width) {
			var pdiv = {}, position;
		    if (className == null) className = "text_div";
		    if (this.options.renderType == 1 || this.options.renderType == 3 || this.options.renderType == 5) {
		        pdiv = this.pLayout;
		        position = "relative";
		    } else if (this.options.renderType == 4) {
		        pdiv.element = eleDiv;
		        position = "relative";
		    } else if (this.options.renderType == 6) {
		        pdiv.element = eleDiv;
		        position = "relative";
		    } else {
		        pdiv = this.pLayout;
		        position = "absolute";
		    }
		    var ele;
		    if (this.options.renderType == 6) {
		        ele = new ReadElement(eleId, this.pLayout);
		        if (userObject && userObject.visible == false) ele.visible = false;
		        else ele.visible = true;
		        if (type == $.editortype.COMBOBOX) {
		            ele.showImgOnly = userObject.imageOnly;
		            ele.comboData = userObject.comboData;
		            ele.visible = userObject.visible;
		        }
		        ele.editorType = type;
		    } else if (type == $.editortype.CHECKBOX) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).checkbox({
		        	width : eleWidth,
		        	text : labelName,
		        	checked : false,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            inputAssistant : inputAssistant,
		            showTipMessage : showTip
		        }).checkbox("instance");
		        if (userObject && userObject.valuePair) ele.setValuePair(userObject.valuePair);
		        if (userObject && userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.REFERENCE) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).reference({
		        	width : eleWidth,
		        	position : position,
		        	disabled: disabled,
		            readOnly: readOnly,
		            tip: tip,
		            inputAssistant: inputAssistant,
		            refType: userObject.refType,
		            refHeight: userObject.refHeight,
		            refWidth: userObject.refWidth,
		            isShowLine: this.options.isShowLine,
		            refFormId: this.options.id,
		            refFormeleID: eleId,
		            nodeInfo : userObject.refNode,
		            className : className
		        }).reference("instance");
		        	
		        ele.setBindInfo(dataset, field);
		        if (userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.COMBOBOX || type == $.editortype.MULTICOMBOBOX) {
		        var dataDivHeight = userObject.dataDivHeight;
		        var comobOptions = {
		        	width : eleWidth,
		        	position : position,
		        	selectOnly : userObject.selectOnly,
		        	disabled : disabled,
		            readOnly : readOnly,
		            dataDivHeight : dataDivHeight,
		            inputAssistant : inputAssistant,
		            className : className
		        };
		        if(type == $.editortype.MULTICOMBOBOX) {
		        	comobOptions.multiple = true;
		        }
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).combo(comobOptions).combo("instance");
		        ele.setShowImgOnly(userObject.imageOnly);
		        var comboData = userObject.comboData;
		        if (comboData != null) {
		            ele.setComboData(comboData);
		        }
		        if (userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.LANGUAGECOMBOBOX) {
		        var dataDivHeight = userObject.dataDivHeight;
		        ele = new LanguageComboComp(pdiv, eleId, 0, 0, eleWidth, position, userObject.selectOnly, {
		            "disabled": disabled,
		            "readOnly": readOnly,
		            "dataDivHeight": dataDivHeight,
		            "inputAssistant": inputAssistant
		        },
		        className, userObject.currentLangCode);
		        ele.setShowImgOnly(userObject.imageOnly);
		        //不从下拉数据集取数据构造下拉区域，从json数组,json从PCFormCompRender中渲染得到，保存对应的userObj中
		        var langDataArray = userObject.langDataArray;
		        if (langDataArray != null) {
		            ele.setComboOptions(langDataArray);
		        }
		        if (userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.LIST) {
		        var dataDivHeight = userObject.dataDivHeight;
		        ele = new ListComp(pdiv, eleId, 0, 0, eleWidth, dataDivHeight, false, position, className);
		        var comboData = userObject.comboData;
		        if (comboData != null) {
		            ele.setComboData(comboData);
		        }
		        if (userObject.visible == false) ele.visible = false;

		    } else if (type == $.editortype.RADIOGROUP) {
		        var widgetId = this.viewpart.id;
		        // RadioGroup的ID要重新设置，以免不同form中radio的name相同
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).radiogroup({
		        	width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            inputAssistant : inputAssistant
		        }).radiogroup("instance");
		        	
		        var comboData = userObject.comboData;
		        if (comboData != null) {
		            ele.setComboData(comboData);
		        }
		        if (userObject.visible == false) ele.visible = false;

		    } else if (type == $.editortype.CHECKBOXGROUP) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).checkboxgroup({
		        	width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            inputAssistant : inputAssistant
		        }).checkboxgroup("instance");
		        	
		        var comboData = userObject.comboData;
		        if (comboData != null) {
		            ele.setComboData(comboData);
		        }
		        if (userObject.visible == false) ele.visible = false;

		    } else if (type == $.editortype.PWDTEXT) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).pswtext({
		        	width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            inputAssistant : inputAssistant,
		            className : className
		        }).pswtext("instance");
		        	
		    } else if (type == $.editortype.INTEGERTEXT) {
		        var maxValue = userObject.maxValue != null ? userObject.maxValue: null;
		        var minValue = userObject.minValue != null ? userObject.minValue: null;
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).integertext({
		        	width : eleWidth,
		        	position : position,
		        	maxValue : maxValue,
		        	minValue : minValue,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		        }).integertext("instance");
		        	
		        if (userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.DECIMALTEXT) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).floattext({
		        	width : eleWidth,
		        	position : position,
		        	precision : userObject.precision,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		        }).floattext("instance");
		        	
		        if (userObject.visible == false) ele.visible = false;
		    } else if (type == $.editortype.TEXTAREA) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).textarea({
		        	width : eleWidth,
		        	height : height,
		        	position : position,
		            readOnly : readOnly,
		            tip : tip,
		            className : className
		        }).textarea("instance");
		        	
		        var disabled = $.argumentutils.getBoolean(disabled, true);
		        ele.setActive(!disabled);
		        // ele.nextLine = true;
		    } else if (type == $.editortype.DATETEXT || type == $.editortype.MULTIDATETEXT) {
		    	var _dataTextOptions = {
		    		width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		    	};
		    	if(type == $.editortype.MULTIDATETEXT) {
		    		_dataTextOptions.multiple = true;
		    	}
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).datetext(_dataTextOptions).datetext("instance");
		        	
		    } else if (type == $.editortype.DATETIMETEXT) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).datetext({
		        	width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		        }).datetext("instance");
		        	
		        ele.setShowTimeBar(true);
		    } else if (type == $.editortype.SHORTDATETEXT || type == $.editortype.MULTISHORTDATETEXT) {
		    	var _dataTextOptions = {
		    		width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		    	};
		    	if(type == $.editortype.MULTISHORTDATETEXT) {
		    		_dataTextOptions.multiple = true;
		    	}
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).datetext(_dataTextOptions).datetext("instance");
		        ele.setHiddenDayBar(true);
		    } else if (type == $.editortype.IMAGECOMP) {
		        var refImg1 = '';
		        var refImg2 = '';
		        if (userObject.refImage1) {
		            refImg1 = userObject.refImage1;
		        }
		        if (userObject.refImage2) {
		            refImg2 = userObject.refImage2;
		        }
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).image({
		        	refImg1 : refImg1,
		        	width : userObject.eleWidth,
		        	height : userObject.height,
		        	alt : '',
		        	refImg2 : refImg2,
		        	position : "relative",
			        disabled : disabled,
			        visible : userObject.visible
		        }).image("instance");
		        	
		        ele.baseUrl = userObject.url;
		        ele.pkField = userObject.pkfield;
		        if (userObject.refImage1 || userObject.refImage2) {
		            ele.setValue = function() {
		                return;
		            };
		        } else {
		            ele.setValue = function() {
		                var row = oThis.dataset.getSelectedRow();
		                if (row == null) this.changeImage("", "");
		                else {
		                    var pkValue = row.getCellValue(oThis.dataset.nameToIndex(this.pkField));
		                    if (pkValue == null) {
		                        this.changeImage("", "");
		                    } else {
		                        var url = this.baseUrl.replace("$REPLACE$", pkValue);
		                        this.changeImage(url, url);
		                    }
		                }
		            };
		        }
		    } else if (type == $.editortype.RICHEDITOR) {
		        var toolbarType = null;
		        if (userObject && userObject.toolbarType) toolbarType = userObject.toolbarType;
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).editor({
		        	position : "relative",
		        	toolbarType : toolbarType
		        }).editor("instance");
		    }
		    // form的自定义元素
		    else if (type == $.editortype.SELFDEFELE) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).selfdefelement({
		        	width : eleWidth,
		        	height : height,
		        	position : position
		        }).selfdefelement("instance");
		        	
		        var disabled = $.argumentutils.getBoolean(disabled, true);
		        ele.setActive(!disabled);
		    } else if (type == $.editortype.FILECOMP) {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).file({
		        	width : eleWidth,
		        	height : height,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            //sizeLimit : userObject.sizeLimit,
		            //sysid : userObject.sysid,
		            //billtype : userObject.billtype,
		            className : className
		        }).file("instance");
		    } else {
		        ele = $("<div id=\""+eleId+"\"></div>").appendTo(pdiv.element).stringtext({
		        	width : eleWidth,
		        	position : position,
		        	disabled : disabled,
		            readOnly : readOnly,
		            maxSize: userObject.maxLength,
		            tip : tip,
		            inputAssistant : inputAssistant,
		            className : className
		        }).stringtext("instance");
		        	
		        if (userObject.visible == false) ele.visible = false;
		    }
		    if(pdiv.comps) pdiv.comps.push(ele);
		    if (ele.element) {
		    	ele.element.attr("id",this.options.id + eleId);
		    	ele.element.data('eleid',eleId);
		    }
		    ele.fieldId = field;
		    ele.labelName = labelName;
		    ele.labelPos = labelPos;
		    ele.showTipMessage = showTip;
		    if (ele.inputAssistant == null) ele.inputAssistant = inputAssistant;
		    ele.description = description;
		    ele.isAttachNext = isAttachNext;
		    ele.basewidth = eleWidth;
		    ele.readOnly = readOnly;
		    ele.colSpan = colSpan;
		    ele.rowSpan = rowSpan;
		    ele.nextLine = nextLine;
		    ele.isRequired = !required;
		    if (labelColor != "" && labelColor != null) ele.labelColor = labelColor;
		    var oriIndex = -1;
		    for (var xi = 0; xi < this.keyArr.length; xi++) {
		        if (this.keyArr[xi] == eleId) {
		            oriIndex = xi;
		            break;
		        }
		    }
		    if (oriIndex != -1) {
		        this.eleArr[oriIndex] = ele;
		    } else {
		        this.keyArr.push(eleId);
		        this.eleArr.push(ele);
		    }

		    this.disableArr.push(disabled ? disabled: false);
		    this.readOnlyArr.push(readOnly ? readOnly: false);
		    var oThis = this;
		    //如果控件类型为LanguageComboBox，则单独定义textListener，主要是获取对应的下拉选项的fieldId，才能找到数据集中的对应，然后修改值
		    if (type == $.editortype.LANGUAGECOMBOBOX) {
		        var textListener = new TextListener();
		        textListener.valueChanged = function(valueChangeEvent4LanguageComp) {
		            if (oThis.rowIndex == -1) return;
		            var index = oThis.dataset.nameToIndex(valueChangeEvent4LanguageComp.fieldId);
		            if (index == -1) return;
		            oThis.dataset.setValueAt(oThis.rowIndex, index, this.getValue());
		        };
		        ele.addListener(textListener);
		    } else {
		        // 数据改变事件（不包括EditorComp子项）
		    	ele.element.on($.EditorTypeMap[type] + 'valuechanged',function(e,valueChangeEvent){
		            if (oThis.rowIndex == -1) return;
		            var index = oThis.dataset.nameToIndex(ele.fieldId);
		            if (index == -1) {
		            	alert("未找到表单元素"+ele.fieldId);
		            	return;
		            }
		            var newValue = null;
		            var oldValue = null;
		            if(type == $.editortype.REFERENCE) {
		            	var newValue = valueChangeEvent.newValue;
			            var oldValue = valueChangeEvent.oldValue;
		            } else {
		            	newValue = ele.getValue();
		            	oldValue = oThis.dataset.getValueAt(oThis.rowIndex, index);
		            }
		          
		            if (newValue == oldValue) return;
		            oThis.dataset.setValueAt(oThis.rowIndex, index, newValue);
		            ele.element.triggerHandler("formelementvaluechanged",{newValue:newValue,oldValue:oldValue});
		            if(formula && ele.setFormular) {
		            	ele.extendOpts = {
		            		widgetType : 'formElement',
		            		formId : oThis.options.id,
		            		eleId : eleId
		            	};
		            	ele.setFormular(true);
		            }
		        });
		    }
		    // 鼠标移出且内容为空，进行数据校验
		    ele.element.on($.EditorTypeMap[type] + 'onblur',function(focusEvent) {
		        //var obj = focusEvent.obj;
		        if (ele.componentType == "EDITOR") { // EditorComp子项，为dataset设值
		            if (oThis.rowIndex == -1) return;
		            if (this.readOnly == true) return;
		            var index = oThis.dataset.nameToIndex(this.fieldId);
		            if (index == -1) return;
		            // 当前值
		            var newValue = this.getValue();
		            // 旧值
		            var oldValue = oThis.dataset.getValueAt(oThis.rowIndex, index);
		            if (oldValue != newValue) { // 值发生变化
		                // 更新ds内容
		                oThis.dataset.setValueAt(oThis.rowIndex, index, newValue);
		            }
		        } else { // 非EditorComp子项，鼠标移出且内容为空，进行数据校验
		            if (oThis.rowIndex == -1) return;
		            var row = oThis.dataset.getRow(oThis.rowIndex);
		            var colIndex = oThis.dataset.nameToIndex(ele.fieldId);
		            if (colIndex == -1) return;
		            var value = ele.getValue();
		            if (value == null || value == "") oThis.dataset.checkDatasetCell(value, colIndex, row);
		            if (oThis.options.renderType == 1 || oThis.options.renderType == 2) { // 固定布局或流式布局
		                if (ele.hideFloatMessageDiv) {
		                    // 隐藏浮动提示信息
		                	ele.hideFloatMessageDiv();
		                }
		            }
		        }
		    });
		    ele.element.on($.EditorTypeMap[type] + 'onfocus',function(focusEvent) {
		        //var obj = focusEvent.obj;
		        if (oThis.options.renderType == 1 || oThis.options.renderType == 2) { // 固定布局或流式布局
		            if (ele.showFloatMessageDiv) {
		                // 显示浮动提示信息
		            	ele.showFloatMessageDiv();
		            }
		        }
		    });
		    
		    // 增加回车事件，焦点移到下一个元素
		    ele.element.on($.EditorTypeMap[type] + 'onenter',function(keyEvent) {
//		        var obj = keyEvent.obj;
		        for (var i = 0,
		        n = oThis.eleArr.length - 1; i <= n; i++) {
		            var _ele = oThis.eleArr[i];
		            if (ele == _ele) {
		                // // 是否是最后一个可用元素
		                // var isLastEle = true;
		                //做循环
		                var j = i + 1;
		                if (j > n) j = 0;
		                var nextEle = oThis.eleArr[j];
		                while (nextEle != _ele) {
		                    if (nextEle.disabled != null && nextEle.disabled != true && nextEle.visible != null && nextEle.visible == true) {
		                        if (nextEle.setDivFocus && nextEle.setDivFocus(true) == true) {
		                            break;
		                        } else if (nextEle.input != null) {
		                            nextEle.input.focus();
		                            break;
		                        } else if (nextEle.textArea != null) {
		                            nextEle.textArea.focus();
		                            break;
		                        }
		                    }
		                    j++;
		                    if (j > n) j = 0;
		                    nextEle = oThis.eleArr[j];
		                }
		                break;
		            }
		        }
		        if(keyEvent) {
		        	keyEvent.stopPropagation();
		        	keyEvent.preventDefault();
		        }
		    });

		    // 编辑态下的测试
		    if (window.editMode) {
		        if (this.options.renderType != 4) {
		            var params = {
		                uiid: "",
		                eleid: this.options.id,
		                widgetid: this.viewpart ? this.viewpart.id: "",
		                type: "form_element",
		                subeleid: eleId,
		                renderType: this.options.renderType
		            };
		            ele.params = params;
		            if (!window.windowEditorMode) {
		                $.design.getObj({divObj:ele.element[0], params:params, objType:$.design.Constant.COMPOMENT_TYPE});
		            }
		        }
		    }
		    if (this.options.renderType == 4) { // 创建laybel
		        ele.formWidth = width;
		        this.createType4Div(eleDiv, ele, className == "text_div" ? null: className);
		    }
		    if (userObject && userObject != null && typeof(userObject.visible) == "boolean") {
		        ele.visible = userObject.visible;
		    } else {
		        ele.visible = true;
		    }
		    if (!ele.visible && typeof(ele.hide) == "function") {
		        ele.hide();
		        if (ele.attachedLabel) {
		            $(ele.attachedLabel).css('visibility','hidden');
		        }
		    }
		    return ele;
		},
		/**
		 * 更新FormElement的宽度
		 */
		updateType4Size : function(eleId, eleDiv, eleWidth) {
		    var ele = this.getElement(eleId);
		    var divWidth = $(eleDiv).parent().outerWidth();
		    if (eleWidth == null) {
		        eleWidth = ele.element.outerWidth();
		    }
//		    if ((typeof(eleWidth) == "string") && (eleWidth.indexOf("%") != -1)) {
//		        ele.element.css('width',eleWidth);
//		        return;
//		    }
		    if(ele.formWidth) {
		    	divWidth = ele.formWidth;
		    }
		    var labelWidth = divWidth - eleWidth - 5;
		    if (labelWidth < 0) {
		        labelWidth = 20;
		    }
		    if (labelWidth > 120) {
		        labelWidth = 120;
		    }
		    $(ele.attachedLabel).css('width',labelWidth + "px");
		    ele.setBounds(0, 0, eleWidth, ele.height);
		},
		//改变formWidth
		updateFormWidth : function(eleId,eleDiv,width) {
			var ele = this.getElement(eleId);
			ele.formWidth = width;
			ele.element.parent().css('width',width+"px");
			this.updateType4Size(eleId,eleDiv);
		},
		/**
		 * 设置绑定ds特定元素的值(用于EditorComp的情况)
		 * 
		 * @param id 元素绑定控件ID
		 * @param value 值，若为null则取当前元素绑定控件的值
		 * 
		 */
		setDsCellValue : function(id, value) {
		    if (this.rowIndex == -1) return;
		    var index = this.dataset.nameToIndex(id);
		    if (index == -1) return;
		    var ele = this.getElement(id);
		    value = value == null ? ele.getValue() : value;
		    this.dataset.setValueAt(this.rowIndex, index, value);
		},
		/**
		 * 隐藏子项
		 */
		hideElement : function(key) {
		    for (var i = 0; i < this.keyArr.length; i++) {
		        if (this.keyArr[i] == key) {
		            var comp = this.eleArr[i];
		            comp.hide();
		            if (comp.attachedLabel) {
		                $(comp.attachedLabel).css('visibility','hidden');
		            }
		        }
		    }
		},
		/**
		 * 显示子项
		 */
		showElement : function(key) {
		    for (var i = 0; i < this.keyArr.length; i++) {
		        if (this.keyArr[i] == key) {
		            var comp = this.eleArr[i];
		            comp.show();
		            if (comp.attachedLabel) {
		                $(comp.attachedLabel).css('visibility','visible');
		            }
		        }
		    }
		},
		/**
		 * 获取子项
		 */
		getElement : function(key) {
		    for (var i = 0; i < this.keyArr.length; i++) {
		        if (this.keyArr[i] == key) return this.eleArr[i];
		    }
		    return null;
		},
		/**
		 * 根据索引获取子项
		 */
		getElementByIndex : function(index) {
		    return this.eleArr[index];
		},
		/**
		 * 获取所有子项
		 */
		getElements : function() {
		    return this.eleArr;
		},
		/**
		 * 设置绑定的dataset
		 */
		setDataset : function(ds) {
		    this.dataset = ds;
		    var rows = ds.getSelectedRows();
		    var row = rows == null ? null: rows[0];
		    this.indiceArr = [];
		    // 一次性计算绑定列在Dataset中的索引值，便于后面数据读取。
		    for (var i = 0,
		    count = this.eleArr.length; i < count; i++) {
		        this.indiceArr[i] = ds.nameToIndex(this.eleArr[i].fieldId);
		        // 设置精度
		        var fieldList = ds.fieldList[this.indiceArr[i]];
		        if (fieldList && fieldList.precision != null && this.eleArr[i].componentType == "FLOATTEXT") {
		            if (this.eleArr[i] && typeof(this.eleArr[i].setPrecision) == 'function') {
		                this.eleArr[i].setPrecision(fieldList.precision, true);
		            }
		        }
		        // 顺便绑定当前值
		        if (row != null) {
		            if ($.editortype.COMBOBOX == this.eleArr[i].editorType && this.options.renderType == 6) {
		                var value = row.getCellValue(this.indiceArr[i]);
		                if (this.eleArr[i].comboData && this.eleArr[i].comboData != null) {
		                    this.eleArr[i].setValue(this.eleArr[i].comboData.getNameByValue(value));
		                } else {
		                    this.eleArr[i].setValue(value);
		                }
		            } else {
		                this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		            }
		            this.rowIndex = ds.getRowIndex(row);
		        } else this.eleArr[i].setValue(null);
		    }
		    ds.bindComponent(this);
		    this.setEditable(ds.isEditable());
		    this.pLayout.paint();

		    if (typeof globalFormInitialized != "undefined") globalFormInitialized(this);
		},
		/**
		 * 设置控件的可编辑性
		 * 
		 */
		setEditable : function(editable) { // 实际上调用的是disable属性而不是readonly属性
		    var e = editable;
		    if (this.dataset.editable == false) e = false;
		    this.editable = e;
		    this.tmpEditable = e;
		    // 如果没有选中行，不能edit
		    if (this.dataset.editable == false || this.dataset.getSelectedRow() == null) {
		        this.tmpEditable = false;
		    }
		    for (var i = 0,
		    count = this.eleArr.length; i < count; i++) {
		        if (this.readOnlyArr[i] && this.tmpEditable) continue;
		        if(this.disableArr[i]) continue;
		        // 如果原始不可编辑，在设置为可编辑时，不做动作
		        //if (this.disableArr[i] && this.tmpEditable) continue;
		        if (this.tmpEditable) {
		            this.eleArr[i].setActive(true);
		        } else this.eleArr[i].setActive(false);
		    }
		    if (editable != this.pLayout.editable) {
		        this.pLayout.editable = editable;
		        this.pLayout.paint(true);
		    }
		    setTimeout('$.autoFormComp.firstEleFocus()', 200);
		},
		/**
		 * 设置控件的只读属性
		 */
		setReadOnly : function(readOnly) {
		    this.readOnly = readOnly;
		    this.tmpReadOnly = readOnly;
		    // 如果没有选中行，不能ReadOnly
		    if (this.dataset.getSelectedRow() == null) {
		        this.tmpReadOnly = false;
		    }
		    for (var i = 0,
		    count = this.eleArr.length; i < count; i++) {
		        if (this.disableArr[i] && !this.tmpReadOnly) continue;
		        // 如果原始只读，在设置为不只读不做动作
		        if (this.readOnlyArr[i] && !this.tmpReadOnly) continue;
		        if (this.tmpReadOnly) this.eleArr[i].setReadOnly(true);
		        else this.eleArr[i].setReadOnly(false);
		    }
		},
		/**
		 * 获取高度
		 */
		getHeight : function() {
		    if (this.options.renderType == 1 || this.options.renderType == 3) {
		        var pdiv = $("#__d_" + this.options.id);
		        return (pdiv[0].firstChild.rows.length - 1) * 24;
		    } else return this.pLayout.getHeight();
		},
		/**
		 * Dataset改变后回调方法
		 * 
		 * @private
		 */
		onModelChanged : function(event) {
		    if (event.type == 'RowSelectEvent' || event.type=='FocusChangeEvent') {
		        var row = event.currentRow;
		        this.rowIndex = event.currentRowIndex;
		        for (var i = 0; i < this.indiceArr.length; i++) {
		            if (row != null) {
		                if ("LANGUAGECOMBOBOX" == this.eleArr[i].componentType) {
		                    var formComp = this;
		                    this.eleArr[i].setComboDatas(formComp, row);
		                } else if ($.editortype.COMBOBOX == this.eleArr[i].editorType && this.options.renderType == 6) {
		                    var value = row.getCellValue(this.indiceArr[i]);
		                    if (this.eleArr[i].comboData && this.eleArr[i].comboData != null) {
		                        this.eleArr[i].setValue(this.eleArr[i].comboData.getNameByValue(value));
		                    } else {
		                        this.eleArr[i].setValue(value);
		                    }
		                } else {
		                    this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		                }
		            } else this.eleArr[i].setValue(null);
		        }

		        if (this.editable != this.tmpEditable) {
		            this.tmpEditable = this.editable;
		            this.setEditable(this.editable);
		        }
		        // 当前选中行包括未通过检验的字段
		        if (row != null) {
		            // 清除行中所有内容的错误提示
		            for (var i = 0; i < this.indiceArr.length; i++) {
		                var eleHtml = this.eleArr[i].element;
		                // 设置普通样式
		                if (this.eleArr[i].Div_text) {
		                    this.eleArr[i].setNormalStyle();
		                } else if (eleHtml != null && eleHtml.attr('class').indexOf(" checkedborder") != -1) 
		                	eleHtml.attr('class',eleHtml.attr('class').replace(" checkedborder", ""));
		                if (this.eleArr[i].setMessage) {
		                    if (this.eleArr[i].showTipMessage && this.eleArr[i].showTipMessage != null) 
		                    	this.eleArr[i].setMessage(this.eleArr[i].showTipMessage);
		                    else 
		                    	this.eleArr[i].setMessage(row.getCellValue(this.indiceArr[i]));
		                }
		            }
		            if (this.dataCheckErrorRows != null && this.dataCheckErrorRows.containsKey(row.rowId)) {
		                var value = this.dataCheckErrorRows.get(row.rowId);
		                for (var i = 0; i < value[0].length; i++) {
		                    var index = value[0][i];
		                    var eleHtml = this.eleArr[index].element;
		                    if (this.eleArr[index].setError) this.eleArr[index].setError(true);
		                    // 设置错误样式
		                    if (this.eleArr[index].Div_text) {
		                        if (value[2][i] == BASE.ELEMENT_ERROR) this.eleArr[index].setErrorStyle();
		                        else if (value[2][i] == BASE.ELEMENT_WARNING) this.eleArr[index].setWarningStyle();
		                    } else if (eleHtml.attr('class').indexOf("checkedborder") == -1) 
		                    	eleHtml.addClass('checkedborder');
		                    if (this.eleArr[index].setErrorMessage) 
		                    	this.eleArr[index].setErrorMessage(value[1][i]);
		                }
		            }
		        }
		    }
		    else if (event.type=='RowUnSelectEvent') {
		        this.rowIndex = -1;
		        var count = this.indiceArr.length;
		        for (var i = 0; i < count; i++) this.eleArr[i].setValue(null);
		    }
		    else if (event.type=='DataChangeEvent') {
		        var i = this.getIndexByColIndex(event.cellColIndex);
		        if (i != -1) {
		            if (this.eleArr[i].getValue && this.eleArr[i].getValue() != event.currentValue) {
		                var oldValue = this.eleArr[i].getValue();
		                if (event.currentValue != oldValue) this.eleArr[i].setValue(event.currentValue);
		            }
		            // 移除未通过检验的行在map中的相应数据
		            if (this.dataCheckErrorRows != null && this.dataCheckErrorRows.containsKey(event.currentRow.rowId)) {
		                var value = this.dataCheckErrorRows.get(event.currentRow.rowId);
		                var colIndex = value[0].indexOf(event.cellColIndex);
		                if (colIndex != -1) {
		                    value[0].splice(colIndex, 1);
		                    value[1].splice(colIndex, 1);
		                }
		                if (value[0].length == 0) this.dataCheckErrorRows.remove(event.currentRow.rowId);
		            }
		        }
		    } else if (event.type=='DatasetUndoEvent') {
		        var row = this.dataset.getSelectedRow();
		        if (row != null) {
		            this.rowIndex = this.dataset.getRowIndex(row);
		            for (var i = 0,
		            count = this.indiceArr.length; i < count; i++) {
		                if ($.editortype.COMBOBOX == this.eleArr[i].editorType && this.options.renderType == 6) {
		                    var value = row.getCellValue(this.indiceArr[i]);
		                    if (this.eleArr[i].comboData && this.eleArr[i].comboData != null) {
		                        this.eleArr[i].setValue(this.eleArr[i].comboData.getNameByValue(value));
		                    } else {
		                        this.eleArr[i].setValue(value);
		                    }
		                } else {
		                    this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		                }
		            }
		        } else {
		            this.rowIndex = -1;
		            for (var i = 0,
		            count = this.indiceArr.length; i < count; i++)
		            // 此处是否应该设置成默认值
		            this.eleArr[i].setValue("");
		        }
		    } else if (event.type=='RowInsertEvent') {
		        var row = this.dataset.getSelectedRow();
		        if (row != null) {
		            this.rowIndex = this.dataset.getRowIndex(row);
		            for (var i = 0; i < this.indiceArr.length; i++) {
		                if ($.editortype.COMBOBOX == this.eleArr[i].editorType && this.options.renderType == 6) {
		                    var value = row.getCellValue(this.indiceArr[i]);
		                    if (this.eleArr[i].comboData && this.eleArr[i].comboData != null) {
		                        this.eleArr[i].setValue(this.eleArr[i].comboData.getNameByValue(value));
		                    } else {
		                        this.eleArr[i].setValue(value);
		                    }
		                } else {
		                    this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		                }
		                if (this.eleArr[i].errorMsgDiv) {
		                    this.eleArr[i].errorMsgDiv.hide();
		                }
		            }
		            if (this.errorMsgDiv) {
		                this.errorMsgDiv.hide();
		            }
		        } else {
		            for (var i = 0; i < this.indiceArr.length; i++) {
		                if (this.eleArr[i].errorMsgDiv) {
		                    this.eleArr[i].errorMsgDiv.hide();
		                }
		            }
		            if (this.errorMsgDiv) {
		                this.errorMsgDiv.hide();
		            }
		        }
		        if (this == $.autoFormComp.firstForm && this.editable == true) {
		            setTimeout('$.autoFormComp.firstEleFocus()', 200);
		        }
		    } else if (event.type=='PageChangeEvent') {
		        this.rowIndex = this.dataset.getSelectedIndex();
		        var row = this.dataset.getSelectedRow();
		        for (var i = 0; i < this.indiceArr.length; i++) {
		            if (row != null) {
		                if ($.editortype.COMBOBOX == this.eleArr[i].editorType && this.options.renderType == 6) {
		                    var value = row.getCellValue(this.indiceArr[i]);
		                    if (this.eleArr[i].comboData && this.eleArr[i].comboData != null) {
		                        this.eleArr[i].setValue(this.eleArr[i].comboData.getNameByValue(value));
		                    } else {
		                        this.eleArr[i].setValue(value);
		                    }
		                } else {
		                    this.eleArr[i].setValue(row.getCellValue(this.indiceArr[i]));
		                }
		            } else this.eleArr[i].setValue(null);
		        }
		        if (this.editable != this.tmpEditable) {
		            this.tmpEditable = this.editable;
		            this.setEditable(this.editable);
		        }
		    } else if (event.type=='RowDeleteEvent') {
		        this.rowIndex = this.dataset.getSelectedIndex();
		        for (var i = 0; i < this.indiceArr.length; i++) this.eleArr[i].setValue(null);
		    }
		    // 数据校验事件
		    else if (event.type=='DataCheckEvent') {
		        if (this.options.renderType == 6) return;
		        var colIndice = event.cellColIndices;
		        var describes = event.rulesDescribe;
		        var row = event.currentRow;
		        var eleIndex = -1;
		        var success = true;
		        for (var i = 0,
		        count = colIndice.length; i < count; i++) {
		            if ((eleIndex = this.indiceArr.indexOf(colIndice[i])) != -1) {
		                // 获取当前值
		                var value = row.getCellValue(colIndice);
		                // 获取当前元素是否为必填项
		                var eleRequired = this.eleArr[eleIndex].isRequired;
		                // 如果ds校验通过，但当前值为空且form中不允许该值为空，则设置为校验不通过，设置描述信息
		                if (describes[i] == "" && ((value == null || value == "") && eleRequired)) {
		                    describes[i] = trans('ml_thisfieldcannotnull');
		                }
		                var eleHtml = this.eleArr[eleIndex].element;
		                if (describes[i] != "" && ((value != null && value != "") || ((value == null || value == "") && eleRequired))) { // 校验失败（若ds校验是因为非空而报错，而当前form中允许该元素为空，则不认为是错误）
		                    // 错误类型，包括错误BaseComponent.ELEMENT_ERROR和警告BaseComponent.ELEMENT_WARNING
		                    var errorType = BASE.ELEMENT_ERROR;
		                    // 自定义校验结果（包含2个变量：1："type"错误类型，包括BaseComponent.ELEMENT_ERROR和BaseComponent.ELEMENT_WARNING；2："describe"：错误描述信息）
		                    var userCheckResult = null;
		                    if (typeof selfDefDataCheck != "undefined") { // 如果在当前节点下的include.js中定义了自定义数据校验方法，则进行自定义校验
		                        // 自定义数据校验方法：方法名为selfDefDataCheck，变量1为dataset的ID，变量2为列id，变量3为输入值
		                        userCheckResult = selfDefDataCheck(this.dataset.id, this.dataset.fieldList[colIndice[i]].key, value);
		                        if (userCheckResult) errorType = userCheckResult.type;
		                    }
		                    // 设置错误提示信息
		                    if (userCheckResult != null) { // 有自定义校验结果
		                        if (userCheckResult.type == BASE.ELEMENT_ERROR) {
		                            $.autoFormComp.setElementErrorMessage(this.eleArr[eleIndex], BASE.ELEMENT_ERROR, userCheckResult.describe, this.options.renderType, this);
		                        } else if (userCheckResult.type == BASE.ELEMENT_WARNING) {
		                            $.autoFormComp.setElementErrorMessage(this.eleArr[eleIndex], BASE.ELEMENT_WARNING, userCheckResult.describe, this.options.renderType, this);
		                        }
		                    } else { // 默认校验结果
		                        $.autoFormComp.setElementErrorMessage(this.eleArr[eleIndex], BASE.ELEMENT_ERROR, describes[i], this.options.renderType, this);
		                    }
		                    if (this.eleArr[eleIndex].setError) this.eleArr[eleIndex].setError(true);
		                    // 设置错误样式和校验结果类型
		                    if (this.eleArr[eleIndex].Div_text) {
		                        if (userCheckResult != null) { // 有自定义校验结果
		                            if (userCheckResult.type == BASE.ELEMENT_ERROR) {
		                                // 设置错误样式
		                                this.eleArr[eleIndex].setErrorStyle();
		                                // 设置校验结果类型
		                                if (this.eleArr[eleIndex].setCheckResult) this.eleArr[eleIndex].setCheckResult(BASE.ELEMENT_ERROR);
		                            } else if (userCheckResult.type == BASE.ELEMENT_WARNING) {
		                                // 设置错误样式
		                                this.eleArr[eleIndex].setWarningStyle();
		                                // 设置校验结果类型
		                                if (this.eleArr[eleIndex].setCheckResult) this.eleArr[eleIndex].setCheckResult(BASE.ELEMENT_WARNING);
		                            }
		                        } else {
		                            this.eleArr[eleIndex].setErrorStyle();
		                        }
		                    } else if (eleHtml.attr('class').indexOf(" checkedborder") == -1) {
		                        eleHtml.addClass('checkedborder');
		                    }
		                    // 设置鼠标悬停提示
		                    if (this.eleArr[eleIndex].setErrorMessage) {
		                        if (userCheckResult != null) // 有自定义校验结果
		                        this.eleArr[eleIndex].setErrorMessage(userCheckResult.describe);
		                        else this.eleArr[eleIndex].setErrorMessage(describes[i]);
		                    }
		                    if (this.dataCheckErrorRows == null) this.dataCheckErrorRows = $.hashmap.getObj();
		                    // 当前未通过校验的行没有在map中,则在map中添加该数据
		                    if ((this.dataCheckErrorRows.containsKey(row.rowId)) == false) this.dataCheckErrorRows.put(row.rowId, [[eleIndex], [describes[i]], [errorType]]);
		                    else {
		                        var value = this.dataCheckErrorRows.get(row.rowId);
		                        value[0].push(eleIndex);
		                        value[1].push(describes[i]);
		                        value[2].push(errorType);
		                    }
		                    // 失败
		                    success = false;
		                } else { // 校验成功
		                    if (value == null || value == "") {
		                        // 校验成功后去掉失败时的提示，恢复默认提示
		                        $.autoFormComp.setElementErrorMessage(this.eleArr[eleIndex], BASE.ELEMENT_NORMAL, this.eleArr[eleIndex].inputAssistant, this.options.renderType, this);
		                        // 设置校验结果类型
		                        if (this.eleArr[eleIndex].setCheckResult) this.eleArr[eleIndex].setCheckResult(BASE.ELEMENT_NORMAL);
		                    } else {
		                        // 校验成功后设置成功提示
		                        $.autoFormComp.setElementErrorMessage(this.eleArr[eleIndex], BASE.ELEMENT_SUCCESS, "", this.options.renderType, this);
		                        // 设置校验结果类型
		                        if (this.eleArr[eleIndex].setCheckResult) this.eleArr[eleIndex].setCheckResult(this.ELEMENT_SUCESS);
		                    }
		                    // 设置普通样式
		                    if (this.eleArr[eleIndex].Div_text) {
		                        this.eleArr[eleIndex].setNormalStyle();
		                    } else {
		                        var className = eleHtml.attr('class').replace(" checkedborder", "");
		                        eleHtml.attr('class',className);
		                    }
		                    if (this.eleArr[eleIndex].setError) this.eleArr[eleIndex].setError(false);
		                    // 设置鼠标悬停提示
		                    var title = row.getCellValue(colIndice);
		                    if (this.eleArr[eleIndex].setMessage) {
		                        if (this.eleArr[eleIndex].showTipMessage && this.eleArr[eleIndex].showTipMessage != null) 
		                        	this.eleArr[eleIndex].setMessage(this.eleArr[eleIndex].showTipMessage);
		                        else {
		                            if (this.eleArr[eleIndex].componentType == "FLOATTEXT") title = this.eleArr[eleIndex].getFormater().format(title);
		                            this.eleArr[eleIndex].setMessage(title);
		                        }
		                    }
		                    if (this.dataCheckErrorRows == null) this.dataCheckErrorRows = $.hashmap.getObj();
		                    // 当前通过校验的行在map中,则在map中删除该数据
		                    if ((this.dataCheckErrorRows.containsKey(row.rowId)) == true) this.dataCheckErrorRows.remove(row.rowId);
		                }
		            }
		        }
		        // 调用AutoForm整体校验结果事件
		        if (success) {
		            this._trigger('onSuccess',event);
		        } else {
		            this._trigger('onFailed',event);
		        }
		    }
		    // fieldList changeg 事件
		    else if (event.type=='MetaChangeEvent') {
		        // 处理精度
		        if (event.precision != null) {
		            var index = this.getIndexByColIndex(event.colIndex);
		            if (this.eleArr[index] != null && typeof(this.eleArr[index].setPrecision) == 'function') {
		                this.eleArr[index].setPrecision(event.precision, true);
		            }
		        }
		    }
		},
		getIndexByColIndex : function(colIndex) {
		    if (this.indiceArr == null) return - 1;
		    for (var i = 0,
		    count = this.indiceArr.length; i < count; i++) {
		        if (this.indiceArr[i] == colIndex) {
		            return i;
		        }
		    }
		    return - 1;
		},
		/**
		 * 初始化全局错误提示对象
		 */
		initWholeErrorMsgDiv : function(isUp) {
		    var oThis = this;

		    this.errorMsgDiv = $("<div>").attr('id',this.options.id + "_error_whole_msg_id").addClass('form_error_whole_msg_div').hide();

		    var prefix = "down_";
		    if (isUp) {
		        prefix = "up_";
		        this.errorMsgDiv.css('top','30px');
		    }
		    //九宫格
		    this.wholeMsgDiv = $("<div>").addClass('whole_msg_div').appendTo(this.errorMsgDiv);
		    //左上
		    this.leftTopDiv = $("<div>").addClass(prefix + "bg_left_top").appendTo(this.wholeMsgDiv);
		    //上
		    this.topMiddleDiv = $("<div>").addClass(prefix + "bg_top_middle").appendTo(this.wholeMsgDiv);
		    //右上
		    this.rightTopDiv = $("<div>").addClass(prefix + "bg_right_top").appendTo(this.wholeMsgDiv);
		    //右中
		    this.rightMiddleDiv = $("<div>").addClass(prefix + "bg_right_middle").appendTo(this.wholeMsgDiv);
		    //右下
		    this.rightBottomDiv = $("<div>").addClass(prefix + "bg_right_bottom").appendTo(this.wholeMsgDiv);
		    //下
		    this.bottomMiddleDiv = $("<div>").addClass(prefix + "bg_bottom_middle").appendTo(this.wholeMsgDiv);
		    //左下
		    this.leftBottomDiv = $("<div>").addClass(prefix + "bg_left_bottom").appendTo(this.wholeMsgDiv);
		    //左中
		    this.leftMiddleDiv = $("<div>").addClass(prefix + "bg_left_middle").appendTo(this.wholeMsgDiv);

		    this.errorCenterDiv = $("<div>").addClass(prefix + "error_center_div").appendTo(this.wholeMsgDiv);

		    this.errorMsg = $("<div>").addClass("errorMsg").appendTo(this.errorCenterDiv);
//		    if ($.browsersupport.IS_IE && !$.browsersupport.IS_IE9) {
//		    }

		    this.warningIcon = $("<div>").addClass(prefix + "warning").appendTo(this.wholeMsgDiv);

		    this.closeIcon = $("<div>").addClass(prefix + "close_normal").on('mouseover',function(e){
		    	$(this).attr('class',prefix + "close_press");
		    }).on('mouseout',function(e){
		    	$(this).attr('class',prefix + "close_normal");
		    }).on('mouseup',function(e){
		    	$(this).attr('class',prefix + "close_normal");
		        oThis.errorMsgDiv.hide();
		    }).appendTo(this.wholeMsgDiv);

		    $('#'+this.options.parentDiv).append(this.errorMsgDiv);
		},
		/**
		 * 重置全局错误提示对象样式
		 */
		resetWholeErrorMsgDivStyle : function(isUp) {
		    var oThis = this;
		    var prefix = "down_";
		    if (isUp) {
		        prefix = "up_";
		        this.errorMsgDiv.css('top','30px');
		    }
		    //左上
		    this.leftTopDiv.attr('class',prefix + "bg_left_top");
		    //上
		    this.topMiddleDiv.attr('class',prefix + "bg_top_middle");
		    //右上
		    this.rightTopDiv.attr('class',prefix + "bg_right_top");
		    //右中
		    this.rightMiddleDiv.attr('class',prefix + "bg_right_middle");
		    //右下
		    this.rightBottomDiv.attr('class',prefix + "bg_right_bottom");
		    //下
		    this.bottomMiddleDiv.attr('class',prefix + "bg_bottom_middle");
		    //左下
		    this.leftBottomDiv.attr('class',prefix + "bg_left_bottom");
		    //左中
		    this.leftMiddleDiv.attr('class',prefix + "bg_left_middle");
		    this.errorCenterDiv.attr('class',prefix + "error_center_div");
		    this.warningIcon.attr('class',prefix + "warning");
		    this.closeIcon.attr('class',prefix + "close_normal").on('mouseover',function(e){
		    	$(this).attr('class',prefix + "close_press");
		    }).on('mouseout',function(e){
		    	$(this).attr('class',prefix + "close_normal");
		    }).on('mouseup',function(e){
		    	$(this).attr('class',prefix + "close_normal");
		        oThis.errorMsgDiv.hide();
		    });
		},
		/**
		 * 设置总体错误提示位置
		 */
		setWholeErrorPosition : function() {
		    var parentElement = $('#'+this.options.parentDiv).parent();
		    var tempTop = 0;
		    var tempLeft = 0;
		    while (typeof(parentElement) == "object") {
		        if (parentElement.attr('id') && parentElement.attr('id').indexOf('_um') != -1) {
		            parentElement.append(this.errorMsgDiv);
		            if (tempTop > 40) { //当前弹出表单顶部有按钮
		                this.resetWholeErrorMsgDivStyle(true);
		            }
		            this.errorMsgDiv.hide();
		            this.errorMsgDiv.css('visibility','visible');
		            break;
		        }
		        if (typeof(parentElement[0].offsetLeft) == 'number') {
		            tempLeft += parentElement[0].offsetLeft;
		        }
		        if (typeof(parentElement[0].offsetTop) == 'number') {
		            tempTop += parentElement[0].offsetTop;
		        }
		        if (typeof(parentElement[0].parentOwner) == "object") {
		            parentElement = parentElement[0].parentOwner;
		        } else {
		            parentElement = parentElement.parent();
		        }
		    }
		},
		createType4Div : function(pDiv, comp, className) {
		    var oThis = this;
		    var outerWidth = 0;
		    //大小变化时是否需要重算
		    var needResize = false;
		    comp.pDiv = pDiv;
		    comp.labelClassName = className;
		    if (comp.formWidth == null) outerWidth = $(pDiv).outerWidth();
		    else {
		        if (typeof(comp.formWidth) == 'string' && comp.formWidth.indexOf("%") != -1) {
		            outerWidth = parseInt(comp.formWidth) * $(pDiv).outerWidth() / 100;
		            needResize = true;
		        } else outerWidth = comp.formWidth;
		    }
		    var outerDiv = $("<div>").css({
		    	'width' : outerWidth + "px",
		    	'height' : '100%'
		    });
		    if (needResize) {
		        comp.outerDiv = outerDiv;
		        window.objects[this.options.id + "$" + comp.id] = comp;
		        addResizeEvent(pDiv,
		        function() {
		        });
		    }
		    $(pDiv).append(outerDiv);

		    //标签
		    var labelWidth = 0;
		    var label = comp.labelName;
		    var labelDiv = null;
		    //百分比情况下，在控件外包一层div
		    var compOuterDiv = null;
		    if (label != null && label != "") {
		        // checkbox不设置label
		        if (comp.componentType == "CHECKBOX") label = "";
		        labelDiv = $("<div>");
		        if (className) labelDiv.addClass(className);
		        labelDiv.css({
		        	'text-align' : comp.labelPos,
		        	'padding-top' : '2px',
		        	'padding-right' : '5px',
		        	'float' : 'left',
		        	'white-space' : 'nowrap',
		        	'overflow':'hidden',
		        	'position' : 'relative'
		        }).attr('title',label).html(label);
		        //lable颜色
		        if (comp.labelColor) labelDiv.css('color',comp.labelColor);
		        //必填项，加红色 *	
		        if (comp.isRequired && label != null) {
		            var span = $("<span>").css('color','red').html('*').appendTo(labelDiv);
		            labelWidth = $.measures.getTextWidth(label + "*", className);
		        } else labelWidth = $.measures.getTextWidth(label, null);
		        outerDiv.append(labelDiv);
		        //控件宽度在百分比和非百分比情况	
		        if (comp.basewidth.indexOf('%') == -1) {
		            labelWidth = outerWidth - comp.basewidth - 5;
		        } else {
		        	var compWidth = outerWidth - labelWidth - 5;
			        if (compWidth < 0) compWidth = 0;
		            compOuterDiv = $("<div>").attr('id','compOuter').css({
		            	'float' : 'right',
		            	'height' : '100%',
		            	'position' : 'relative',
		            	'width' : compWidth + "px"
		            });
		            //改变控件宽度
//		            if (comp.componentType == "TEXTAREA" && $.browsersupport.IS_IE8) comp.setBounds(0, 0, compWidth, null);
//		            else 
		                comp.setBounds(0, 0, compWidth, comp.height);
		        }

		        labelDiv.css('width',labelWidth + "px");
		        if(comp.visible) {
		        	labelDiv.css('visibility','visible');
		        } else {
		        	labelDiv.css('visibility','hidden');
		        }
		        
		    }

		    //控件
		    var objHtml = comp.element;
		    if (objHtml != null) {
		        if (compOuterDiv != null) {
		            outerDiv.append(compOuterDiv);
		            compOuterDiv.append(objHtml);
		        } else {
		            objHtml.css('float','right');
		            outerDiv.append(objHtml);
		        }
		    }
		    if(labelDiv && labelDiv.size()>0)
		    	comp.attachedLabel = labelDiv[0];
		},
		changeEleLabelPos : function(id,pos) {
			var label = comp.labelName;
			if (label != null && label != "") { 
				var comp = this.getElement(id);
				comp.labelPos = pos;
				repaintType4Div(comp);
			}
		},
		/**
		 * 重画自由表单元素
		 * @param {} comp
		 */
		repaintType4Div : function(comp) {
		    $(comp.pDiv).empty();
		    this.createType4Div(comp.pDiv, comp, comp.labelClassName);
		},
		destroySelf : function() {
		},
		createType4DivTop : function(pDiv, comp) {
		    var label = comp.labelName;
		    var labelDiv = null;
		    if (label != null && label != "") {
		        // checkbox不设置label
		        if (comp.componentType == "CHECKBOX") label = "";
		        labelDiv = $('<div>').css({
		        	'text-align' : 'left',
		        	'padding-top' : '2px',
		        	'position' : 'relative'
		        }).html(label);
		        //lable颜色
		        if (comp.labelColor) labelDiv.css('color',comp.labelColor);
		        //必填项，加红色 *	
		        if (comp.isRequired && label != null) {
		            var span = $("<span>").css('color','red').html('*').appendTo(labelDiv);
		        }
		        $(pDiv).append(labelDiv);
		    }
		    var objHtml = comp.element;
		    if (objHtml != null) {
		        $(pDiv).append(objHtml);
		    }
		    comp.attachedLabel = labelDiv[0];
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function(type) {
		    if (type == null) {
		        return null;
		    }
		    var context = {};
		    context.c = "AutoFormContext";
		    context.id = this.options.id;
		    context.enabled = this.editable;
		    context.readOnly = this.readOnly;

		    if (type == "all_child") {
		        context.elementContexts = [];
		        for (var i = 0,
		        n = this.getElements().length; i < n; i++) {
		            var ctx = this.getElementByIndex(i).getContext();
		            if (ctx == null) continue;
		            ctx.c = "FormElementContext";
		            ctx.label = this.getElementByIndex(i).labelName;
		            ctx.labelColor = this.getElementByIndex(i).labelColor;
		            ctx.tip = this.getElementByIndex(i).tip;
		            ctx.required = this.getElementByIndex(i).isRequired;
		            context.elementContexts.push(ctx);
		        }
		    }
		    return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
		    if (context.enabled != null && (this.editable == null || this.editable != context.enabled)) this.setEditable(context.enabled);
		    if (context.readOnly != null && (this.readOnly == null || this.readOnly != context.readOnly)) this.setReadOnly(context.readOnly);
		    var needRepaint = false;
		    if (context.elementContexts) {
		        for (var i = 0,
		        n = context.elementContexts.length; i < n; i++) {
		            for (var j = 0,
		            m = this.getElements().length; j < m; j++) {
		                if (this.getElementByIndex(j)) {
		                    if (this.getElementByIndex(j).element.attr('id') == context.elementContexts[i].id) {
		                        var ctx = context.elementContexts[i];
		                        var comp = this.getElementByIndex(j);
		                        if (comp.visible != ctx.visible) {
		                            comp.visible = ctx.visible;
		                            needRepaint = true;
		                        }
		                        if (ctx.labelColor != null && ctx.labelColor != comp.labelColor) {
		                            comp.labelColor = ctx.labelColor;
		                            if (comp.attachedLabel != null) comp.attachedLabel = null;
		                            needRepaint = true;
		                        }
		                        ctx.isform = true;
		                        // 表单值以Dataset为准
		                        if (this.editable == false) ctx.enabled = false;
		                        //提示
		                        comp.tip = ctx.tip;
		                        if (ctx.required != null && ctx.required != comp.isRequired) {
		                            comp.isRequired = ctx.required;
		                            needRepaint = true;
		                        }
		                        comp.setContext(ctx);
		                    }
		                }
		            }
		        }
		    }
		    if (needRepaint) {
		        this.pLayout.paint(true);
		    }
		},
		setEleLabelColor : function(id, labelColor) {
		    var comp = this.getElement(id);
		    var needRepaint = false;
		    if (labelColor && comp.labelColor != labelColor) {
		        comp.labelColor = labelColor;
		        if (comp.attachedLabel != null) comp.attachedLabel = null;
		        if (this.options.renderType == 4) {
		            this.repaintType4Div(comp);
		        } else needRepaint = true;
		    }
		    if (needRepaint) {
		        this.toRepaint();
		    }
		},
		setEleEnabled : function(id, enabled) {
		    var comp = this.getElement(id);
		    if (comp == null) return;
		    var needRepaint = false;
		    if (this.editable == false) enabled = false;
		    if (comp.setActive) {
		        comp.setActive(enabled);
		        this.disableArr[this.getElementIndex(id)] = !enabled;
		        
		    }
		},
		getElementIndex :function(id) {
			for(var i=0;i<this.eleArr.length;i++) {
				if(this.keyArr[i] == id) {
					return i;
				}
			}
		},
		setEleVisible : function(id, visible) {
		    var comp = this.getElement(id);
		    var needRepaint = false;
		    if (comp.visible != visible) {
		        comp.visible = visible;
		        if (visible) {
		            if (typeof(comp.show) == "function") {
		                comp.show();
		                if (comp.attachedLabel) {
		                    $(comp.attachedLabel).css('visibility','visible');
		                }
		            }
		        } else {
		            if (typeof(comp.hide) == "function") {
		                comp.hide();
		                if (comp.attachedLabel) {
		                    $(comp.attachedLabel).css('visibility','hidden');
		                }
		            }
		        }
		        if (this.options.renderType == 4) {
		            this.repaintType4Div(comp);
		        } else needRepaint = true;
		    }
		    if (needRepaint) {
		        this.toRepaint();
		    }
		},
		setEleFocus : function(id, focus) {
		    var comp = this.getElement(id);
		    if (focus == true && comp.setFocus) {
		        comp.setFocus();
		    }
		},
		setElePrecision : function(id, precision) {
		    var comp = this.getElement(id);
		    if (comp.setPrecision) {
		        comp.setPrecision(precision);
		    }
		},
		setEleEditable : function(id, editable) {
		    var comp = this.getElement(id);
		    if (this.readOnly == true) editbale = false;
		    if (comp.readOnly != null && comp.readOnly == editable) {
		        comp.setReadOnly(!editable);
		    }
		},
		setEleLabel : function(id, label) {
		    var comp = this.getElement(id);
		    var needRepaint = false;
		    if (comp.labelName != label) {
		        comp.labelName = label;
		        if (this.options.renderType == 4) {
		            this.repaintType4Div(comp);
		        } else needRepaint = true;
		    }
		    if (needRepaint) {
		        this.toRepaint();
		    }
		},
		toRepaint : function() {
		    if ($.autoFormComp.repaintRt != null) clearTimeout($.autoFormComp.repaintRt);
		    $.autoFormComp.repaintRt = setTimeout("$.autoFormComp.repaintSelf('" + this.viewpart.id + "', '" + this.options.id + "')", 200);
		},
		/**
		 * 固定布局时，Context变化后，重绘方法
		 */
		paint : function() {
		    // 如果是流式布局，退出
		    if (this.options.renderType == 2) return;
		    // 取出table的列数
		    var table = $('#'+this.parentDiv).children().first();
		    var cells = table[0].rows.item(0).cells.length;

		    // 删除form中的table
		    //this.parentDiv.removeChild(this.parentDiv.childNodes[0]);
		    $('#'+this.options.parentDiv).children().first().remove();
		    for (var i = 0,
		    n = this.getElements().length; i < n; i++) {
		        var ctx = this.getContext();
		    }
		}
	});
	
	/**
	 * @fileoverview form控件的自定义element,该组件使form包含任意用户自定义控件成为可能,组件本身采用委托模式,该组件发生状态
	 *               变换或者发生事件时,将把该状态变化,事件派发出去,用户可以重载该组件暴露的方法观察该组件的变化,从而改变该组件内部的自定义控件
	 *               的状态,或者处理自己的逻辑
	 * @author lxl
	 * @version lui 1.0
	 * 
	 */
	$.widget("lui.selfdefelement", $.lui.base, {
		/**
		 * 表单的自定义元素
		 * 
		 * @class form控件的自定义element，该组件使form包含任意用户自定义控件成为可能，组件本身采用委托模式，该组件发生状态
		 *        变换或者发生事件时，将把该状态变化用事件派发出去，用户可以重载该组件暴露的方法观察该组件的变化，从而改变该组件内部的自定义控件
		 *        的状态，或者处理自己的逻辑。
		 */
		options : {
			position : 'absolute',
			disabled : false,
			value : '',
			//event
			active : null,
			inactive : null,
			onblur : null,
			onclick : null,
			onmouseover : null,
			onenter : null
		},
		_initParam : function() {
			this.componentType = "SELFDEFELEMENT";
			this.parentOwner = this.element.parent()[0];
			this._super();
		    this.visible = true;
		    this.labelName = "";
		},
		_create : function() {
			this._initParam();
			var oThis = this,opts = this.options,ele = this.element;
		    this.element.css({
		    	'position' : opts.position,
			    'left' : opts.left + "px",
			    'top' : opts.top + "px",
			    'width' : opts.width,
			    'height' : opts.height,
			    'overflow' : "hidden"
		    });
		    
		    // 初始禁用
		    if (opts.disabled) {
		        this.setActive(false);
		    }

		    if (opts.value) this.setValue(opts.value);

		    // 失去键盘焦点时调用
		    this.element.on('blur',function(e){
		    	oThis._trigger('onblur',e);
		    }).on('mouseover',function(e) {
		        oThis._trigger('onmouseover',e);
		    }).on('click',function(e) {
		        oThis._trigger('onclick',e);
		    }).on('keypress',function(e) {
		        var keyCode = e.keyCode;
		        if (keyCode == 13) {
		            oThis._trigger('onenter',e);
		            return true;
		        }
		    });
		},
		/**
		 * 增加内容
		 */
		setContent : function(contentObj) {
		    if (window.editMode != null && window.editMode == true) {
		        this.element.html('自定义控件');
		        return;
		    }
		    //	contentObj.selfDefElement = this;
		    this.content = contentObj;
		    if (contentObj instanceof $.lui.base) {
		        if (contentObj.element.parent()) this.element.append(contentObj.element.parent());
		        else this.element.append(contentObj.element);
		    } else this.element.append(contentObj.element);
		},
		/**
		 * 获取内容
		 */
		getContent : function() {
		    return this.content;
		},
		setError : function(error) {
		},
		/**
		 * 设置普通样式（校验通过后样式）
		 * @private
		 */
		setNormalStyle : function() {
		},
		setDivFocus : function(isFocus) {
		    if (this.content && this.content.contentObj && this.content.contentObj.setDivFocus) {
		        this.content.contentObj.setDivFocus(isFocus);
		        return true;
		    } else return false;
		},
		/**
		 * @private
		 */
		setActive : function(isActive) {
		    var isActive = $.argumentutils.getBoolean(isActive, false);
		    if (isActive) this._trigger('active',null);
		    else this._trigger('inactive',null);
		    this.options.disabled = !isActive;
		    if (this.content && this.content.setActive) this.content.setActive(isActive);
		},
		/**
		 * 设值
		 * 
		 * @private
		 */
		setValue : function(value) {
		    if (this.content != null && this.content.contentObj != null) this.content.contentObj.setValue(value);
		},
		/**
		 * 必须实现,获取控件的值
		 */
		getValue : function() {
		    if (this.content != null && this.content.contentObj != null) return this.content.contentObj.getValue();
		},
		/**
		 * 获取对象信息
		 * 
		 * @private
		 */
		getContext : function() {
		    var context = new Object;
		    context.id = this.element.attr('id');
		    context.enabled = !this.options.disabled;
		    context.readyOnly = this.readyOnly;
		    return context;
		},
		/**
		 * 设置对象信息
		 * 
		 * @private
		 */
		setContext : function(context) {
		    if (context.enabled != null && (this.options.disabled == null || this.options.disabled == context.enabled)) this.setActive(context.enabled);
		    if (context.readyOnly != null && (this.readyOnly == null || this.readyOnly == context.readyOnly)) this.setReadyOnly(context.readyOnly);
		},
		/**
		 * 设置大小及位置
		 * @param width 像素大小
		 * @param height 像素大小
		 */
		setBounds : function(left, top, width, height) {
			var opts = this.options,ele = this.element;
			opts.left = left;
		    this.top = top;

		    opts.width = $.argumentutils.getString($.measures.convertWidth(width), ele.outerWidth() + "px");
		    opts.height = $.argumentutils.getString($.measures.convertHeight(height), ele.outerHeight() + "px");
		    ele.css({
		    	'left' : opts.left + "px",
		    	'top' : opts.top + "px",
		    	'width' : opts.width,
		    	'height' : opts.height
		    });
		}
	});
})(jQuery);
