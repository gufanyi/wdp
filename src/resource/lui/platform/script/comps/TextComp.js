/** ***** */
/**
 * 文本输入框控件构造函
 * @class 文本输入框控件基
 * @param maxSize 此变量指定的是字节数,中文每个汉字的字节数,英文每个字母的字节数
 */
(function ($) {
    $.widget("lui.textfield", $.lui.base, {
        options: {
            height: 24,
            width: 120,
            dataType: 'A',
            position: 'absolute',
            maxSize: -1,
            disabled: false,
            value: '',
            readOnly: false,
            tabIndex: -1,
            tip: '',
            showTipMessage: '',
            inputAssistant: '',
            labelText: '',
            labelAlign: '',
            labelWidth: '',
            stopHideDiv: false,
            className: 'text_div',
            //event
            outsidecontextMenuclick: null,
            outsideclick: null,
            onselect: null,
            onblur: null,
            onclick: null,
            onfocus: null,
            haschanged: null,
            onenter: null,
            onkeydown: null,
            onkeyup: null,
            onmouseover: null
        },
        _getDataType: function (dateType) {
            var dataTypeMap = {
                'P': 'password',
                'HI': 'hidden',
                'C': 'checkbox',
                'F': 'file',
                'A': 'text'
            }
            return dataTypeMap[dateType] ? dataTypeMap[dateType] : "text";
        },
        _initParam: function () {
            this._super();

//            this.inputClassName_init = $.browsersupport.IS_IE7 ? "text_input" : "input_normal_center_bg text_input";
//            this.inputClassName_inactive = $.browsersupport.IS_IE7 ? "text_input_inactive" : "input_normal_center_bg text_input_inactive";
            this.inputClassName_init = "input_normal_center_bg text_input";
            this.inputClassName_inactive ="input_normal_center_bg text_input_inactive";
            this.label_input_margin = 2;

            this.inputType = this._getDataType(this.options.dataType);
            // 是否大写
            this.uppercase = false;
            // 准备Focus
            this.mayFocus = false;
            // 当前是否是校验失败状
            this.isError = false;
            this.checkResult = BASE.ELEMENT_NORMAL;
            // 信息
            this.message;
            // 错误信息（包括title和错误提示）
            this.errorMessage;
            this.focused = false;
            this.options.maxSize = this.options.maxSize || -1;

            this.hasLabel = false;
            if (this.options.dataType != "C" && this.options.dataType != "RA") {
                if ("" != this.options.labelText)
                    this.hasLabel = true;
                this.options.labelAlign = $.argumentutils.getString(this.options.labelAlign, "left");
                if (0 == this.options.labelWidth && "" != this.options.labelText)
                    this.options.labelWidth = $.measures.getTextWidth(this.options.labelText);
            }

            // 只有字符串类型才需要此处理
            if (this.options.dataType == 'A' || this.options.dataType == 'P') {
                if (this.options.maxSize != -1) {
                    // 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
                    if (this.options.value.lengthb() > this.options.maxSize)
                        this.options.value = this.options.value.substrCH(this.maxSize);
                }
            }
            this.formular = null;

        },
        setFormular: function (formular) {
            this.formular = formular;
        },
        setTip: function (tip) {
            this.options.tip = tip;
        },
        postProcessNewValue: function (value) {
            return value;
        },
        _create: function () {
            this._initParam();
            this.element.css({
                'position': this.options.position,
                'left': this.options.left + 'px',
                'top': this.options.top + 'px',
                'width': (!this.options.width ? '120px' : this.options.width),
                'height': '100%',
                'overflow': 'hidden'
            });
            this._managerSelf();

            this._initErrorMsg();
            this.setReadOnly(this.options.readOnly);
        },
        _managerSelf: function () {
            var oThis = this, opts = this.options, ele = this.element;
            var width = 0;
            if ($.argumentutils.isPercent(opts.width)) width = ele.outerWidth();
            else width = $.argumentutils.getInteger(parseInt(opts.width), 120);
            if (width == 0) {
                var ownerWidth = ele.parent().outerWidth()+"";//css('width');
                if (ownerWidth && ownerWidth.indexOf("%") == -1) {
                    width = parseInt(ownerWidth, 10);
                }
                if (width == 0) {
                    width = 120;
                }
            }
            width = ((width - 4) < 0 ? 0 : (width - 4));
            if (this.hasLabel)
                width = (width - opts.labelWidth - this.label_input_margin) < 0 ? 0 : (width - opts.labelWidth - this.label_input_margin);

            this.Div_text = $("<div></div>").attr({
                'id': ele.attr("id") + '_textdiv'
            }).css({
                'position': 'relative',
                'top': '0px',
                'width': width +'px',
                'overflow': 'hidden'
            }).addClass(opts.className).appendTo(ele);

            if (this.hasLabel) {
                this.labelDiv = $("<div></div>").css({
                    'width': ((opts.labelWidth) < 0 ? 0 : (opts.labelWidth) + 'px')
                }).appendTo(ele);

                if (opts.labelAlign == "left") {
                    this.labelDiv.css('float', 'left');
                    this.Div_text.css('float', 'right');
                } else if (opts.labelAlign == "right") {
                    this.Div_text.css('float', 'left');
                    this.labelDiv.css('float', 'right');
                }
                this.label = $("<div id='" + ele.attr("id") + "_label'></div>").label({
                    left: 0,
                    top: 3,
                    text: opts.labelText,
                    position: 'relative',
                    className: 'textcomp_normallabel'
                }).appendTo(this.labelDiv);
            }

            width = parseInt(this.Div_text.css('width'), 10);
//            if ($.browsersupport.IS_IPAD) {
//                width = width - 10;
//            }

            this.input = $("<input type=\""+(this.inputType || 'text')+"\"/>");
//            this.input.get(0).type = this.inputType;

            /* 填充文本框顶部和文字间距,使文字垂直居中显 */
//  if (!$.browsersupport.IS_STANDARD && (this.input.attr('type') == 'text' || this.input.attr('type') == 'password')) {
//                this.input.css('padding-top', '4px');
//            }

            var left_div = $("<div></div>").addClass('input_normal_left_bg').appendTo(oThis.Div_text);
            var center_div = $("<div></div>")
                .addClass('input_normal_center_div_bg')
                .css({
                    'width': (width - 3 * 2) + "px"
                })
                .append(this.input).appendTo(oThis.Div_text);

            var right_div = $("<div></div>").addClass('input_normal_right_bg').appendTo(oThis.Div_text);

            // 数字型控件的数字居右显示
            if (opts.dataType == "I" || opts.dataType == "N") {
                this.input.css({
                    'text-align': 'right',
                    'ime-mode': 'disabled' // 禁用输入法
                });
            }
            this.input.prop({
                'name': ele.attr("id"),
                'readOnly': opts.readOnly
            });
            if (opts.tabIndex != -1) this.input.attr('tabindex', opts.tabIndex);
            if (opts.maxSize != -1) this.input.attr('maxlength', opts.maxSize);

            /** Ios * */
//          if ($.browsersupport.IS_IOS) this.input.css('width', (width - 6) + "px");
 //           else
                 this.input.css('width', (width - 10) + "px");

            if (this.Div_text.children().length == 3) {
                var centerWidth = width - 3 * 2; // 3*2左右边框图片宽度
                this.Div_text.children().eq(1).css('width', centerWidth + "px");
                this.input.css('width', (centerWidth - 2 * 2) + "px"); // 2*2
            }
            /** ***** */
 //           if (!$.browsersupport.IS_IE6) 
                  this.input.css('height', '22px');
            this.input.attr('class', this.inputClassName_init); // "input_normal_center_bg
            if (opts.disabled) {
                this.input.attr('class', this.inputClassName_inactive);
                this.Div_text.attr('class', this.options.className + " text_inactive_bgcolor");
            }
            // 在编辑状态将input置为不可见
//            if (window.editMode) {     TODO
//                this.input.css('visibility', 'hidden');
//            }
            this.input.on('keyup', function (e) {
                oThis.ctxChanged = true;
                oThis._trigger('onkeyup', e);
                if (window.pageUI)
                    window.pageUI.setChanged(true);

            }).on('keydown', function (e) {
                oThis.ctxChanged = true;
                var keyCode = e.keyCode;
                if (oThis._trigger('onkeydown', e) == false) {
                    e.stopPropagation()
                    return;
                }
                var con = (keyCode == 13 || (keyCode == 9 && e.shiftKey) || keyCode == 9);
                if (oThis.options.readOnly && !con) {
                }
                if (keyCode == 8 || keyCode == 46) oThis._trigger("haschanged", e);

            }).on('keypress', function (e) {
                oThis.ctxChanged = true;
                var keyCode = e.keyCode;
                var con = (keyCode == 13 || (keyCode == 9 && e.shiftKey) || keyCode == 9);
                // readOnly时不允许输入
                if (oThis.options.readOnly && !con) {
                }
                // 得到格式化器
                var formater = oThis.getFormater();
                // 获取输入字符
                if (keyCode == 13) {
                    // oThis.newValue = oThis.input.value;
                    oThis.newValue = oThis.getFormater().format(oThis.input.val());
                    if (oThis.processEnter) oThis.processEnter();
                    oThis._trigger('onenter', e)
                    // 回车后失去焦点
                    if (typeof(this.fieldId) == 'undefined') oThis.input.blur();
                    return true;
                }
                // for firefox
                else if (keyCode == 8) {
                    oThis._trigger("haschanged", e);
                    return true;
                }
 //             if (!$.browsersupport.IS_IE && (keyCode == 37 || keyCode == 39 || keyCode == 9)) {
                if (keyCode == 37 || keyCode == 39 || keyCode == 9) {
                    return true;
                }
                var key;
                if (keyCode) {
                    // 从字符编码创建一个字符串,字符串中的每个字符串都由单独的数字Unicode编码指定
                    key = String.fromCharCode(keyCode);
                }
                // IE下增加录入时即校验的特性，在firefox下还无法实现（guoweic: 已实现）
                var currValue = oThis.input.val();
                if (opts.dataType != "F") {
                    var aggValue = $.argumentutils.insertAtCursor(this, key);
                }
//                if (!$.browsersupport.IS_IE && aggValue == "") {
                    aggValue = key;
//                }
//                if ($.browsersupport.IS_IE) {
//                    document.execCommand("undo");
//                }
                if (formater.valid(key, aggValue, currValue) == false) {
                    return false;
                }
                if (oThis.afterValid(keyCode, key) == false) {
                    return false;
                }
                // 删除事件对象（用于清除依赖关系）
                if (window.pageUI) window.pageUI.setChanged(true);
                return true;

            }).on('focus', function (e) {
                oThis.focused = true;
                oThis.warnIcon.hide();
                if (oThis.isError && typeof(oThis.errorMessage) == 'string' && oThis.errorMessage != '') {
                    oThis.errorCenterDiv.html(oThis.errorMessage);
                    if (!oThis.noShowErrorMsgDiv) {
                        oThis.errorMsgDiv.show();
                    }
                }
                if (oThis.input.val() != oThis.newValue) {
                    var value = oThis.postProcessNewValue(oThis.newValue);
                    oThis.input.val(value);
                }
                if (oThis.Div_text.children().length == 3) {
                    if (!oThis.input.attr('readOnly')) {
                        var children = oThis.Div_text.children();
                        $.each(children, function (index, item) {
                            $(item).attr('class', $(item).attr('class').replaceStr('input_normal', 'input_highlight'));
                        });
                        oThis.input.removeClass('input_normal_center_bg').addClass('input_highlight_center_bg');
                    }
                }
                // 保证光标在最后
                var length = oThis.input.val().length;
                if (oThis.input.get(0).createTextRange) { // IE
                    var r = oThis.input.get(0).createTextRange();
                    r.collapse(true);
                    r.moveStart('character', length);
                    r.select();
                } else if (oThis.input.get(0).setSelectionRange) { // Firefox
                    oThis.input.get(0).setSelectionRange(length, length);
                }

                oThis.focus(e);
                oThis.hideTip();

            }).on('blur', function (e) {
                oThis.focused = false;
                if (oThis.Div_text.children().length == 3) {
                    var children = oThis.Div_text.children();
                    $.each(children, function (index, item) {
                        if (typeof(item) != "undefined") {
                            $(item).attr('class', $(item).attr('class').replaceStr('input_highlight', 'input_normal'));
                        }
                    });
                    oThis.input.removeClass('input_highlight_center_bg').addClass('input_normal_center_bg');
                }
                oThis.blur(e);
                if (!oThis.ingrid) {
                    oThis.setValue(oThis.input.val());
                }
                oThis.showTip();

            }).on('select', function (e) {
                if (opts.maxSize != -1) oThis.input.attr('maxLength', (opts.maxSize + 1));
                oThis._trigger('onselect', e);

            }).on('mouseover', function (e) {
                oThis._trigger('onmouseover', e);

            }).on('click', function (e) {
                oThis.warnIcon.hide();
                if (oThis.isError && typeof(oThis.errorMessage) == 'string' && oThis.errorMessage != '') {
                    oThis.errorCenterDiv.html(oThis.errorMessage);
                    if (!oThis.noShowErrorMsgDiv) {
                        oThis.errorMsgDiv.show();
                    }
                }
                oThis._trigger('onclick', e);
                e.stopPropagation();

            }).on('paste', function (e) {
                return oThis._trigger('onPaste', e);
                ;
            });

            // 设置初始
            if (opts.value) this.setValue(opts.value);
            else this.showTip();

        },
        setError: function (error) {
            this.isError = error;
        },
        setValgin: function (valgin) {
            this.valgin = valgin;
            this.element.css({
                'display': 'table-cell',
                'vertical-align': 'middle'
            });
        },
        checkTip: function () {
            return this.options.tip != null && this.options.tip != "" && this.input.attr("type") == "text";
        },
        showTip: function () {
            if (this.checkTip()) {
                if (this.input.val() == "") {
                    this.input.val(this.options.tip).css('color', 'gray');
                }
            }
        },
        hideTip: function () {
            if (this.checkTip()) {
                if (this.input.val() == this.options.tip) {
                    this.input.val('').css('color', 'black');
                }
            }
        },
        setFormater: function (formater) {
            this.formater = formater;
        },
        setTabIndex: function (index) {
            this.input.attr('tabIndex', index);
        },
        getFormater: function () {
            if (this.formater == null) return this.createDefaultFormater();
        },
        createDefaultFormater: function () {
            return $.stringformater.getObj(this.options.maxSize);
        },
        afterValid: function (keyCode, key) {
        },
        setWidth : function(width) {
        	this.setBounds(null,null,width,null);
        },
        setBounds: function (left, top, width, height) {
            var opts = this.options;
            opts.left = left;
            opts.top = top;
            opts.width = $.argumentutils.getString($.measures.convertWidth(width), "100%");
            opts.height = $.argumentutils.getString($.measures.convertHeight(height), "100%");
            this.element.css({
                'left': opts.left + "px",
                'top': opts.top + "px",
                'width': opts.width,
                'height': opts.height
            });

            if (this.Div_text != null) {
                var tempWidth = 0;
                if ($.argumentutils.isPercent(opts.width)) tempWidth = this.element.outerWidth();
                else tempWidth = $.argumentutils.getInteger(parseInt(opts.width), 120);

                this.Div_text.css('width', tempWidth - 4 + "px");
                if (this.hasLabel) this.Div_text.css('width', tempWidth - opts.labelWidth - this.label_input_margin + "px");
                var pixelWidth = parseInt(this.Div_text.css('width'), 10);
//                if ($.browsersupport.IS_IPAD) {
//                    pixelWidth = pixelWidth - 10;
//                }
                if (this.Div_text.children().length == 3) {
                    var centerWidth = pixelWidth - 3 * 2; // 3*2左右边框图片宽度
                    this.Div_text.children().eq(1).css('width', centerWidth + "px");
                    this.input.css('width', (centerWidth - 2 * 2) + "px"); // 2*2
                }
            }
        },
        focus: function (e) {
            // 输入框必须先获得焦点然后再调用select()方法(并不是所有的浏览器都要求这样,但安全起最好每次都先调用focus())
            this.oldValue = this.newValue;
            if (this.visible) {
                if (this.options.dataType == "P") {
                    this.input.select();
                }
                this._trigger('onfocus', e);
            }
        },
        valuechanged : function() {
        	this._trigger("valueChanged", null, {oldValue: this.oldValue, newValue: this.newValue});
        	if(this.formular) {
        		execFormula(this.viewpart.id,null,this.element.attr('id'),this.extendOpts);
        	}
        },
        blur: function (e) {
            if (this.visible) {
                var value = this.input.val();
                if (this.options.dataType != 'P') {
                    if (this.showTipMessage && this.showTipMessage != null) this.setMessage(this.showTipMessage);
                    else this.setMessage(value);
                }
                this.newValue = this.getFormater().format(value);
                var verifyR = this.verify(value);
                if (verifyR == null || verifyR) {
                    if (this.newValue != this.oldValue)
                    	this.valuechanged();
                }
                this._trigger('onblur', e);
            }
        },
        verify: function (oldValue) {
            return true;
        },
        /**
         * 设置提示信息
         */
        setMessage: function (message) {
            if (!this.isError) {
                this.message = message;
                this.errorMessage = "";
                this.setTitle(message);
            }
        },
        /**
         * 设置错误提示信息
         */
        setErrorMessage: function (errorMessage) {
            if (this.isError) {
                this.message = "";
                this.errorMessage = errorMessage;
                this.errorCenterDiv.html(this.errorMessage);
            }
        },
        setTitle: function (title) {
            if (title) {
                if (this.input) {
                    this.input.attr('title', title).val(title);
                }
                this.element.attr('title', title);
            } else {
                if (this.input) {
                    this.input.attr('title', '').val('');
                }
                this.element.attr('title', '');
            }
        },
        setLabelText: function (text) {
            var label = this.getLabel();
            if (label) label.label('changeText', text);
        },
        isShowLabel: function (isShowLabel) {
            if (!isShowLabel) {
                this.labelDiv.hide().css('width', '0px');
                this.element.css('width', this.Div_text.css('width'));
            }
        },
        setCheckResult: function (checkResult) {
            this.checkResult = checkResult;
        },
        showFloatMessageDiv: function () {
            // 要显示的信息
            var text = "";
            // 提示信息类型，包括：BaseComponent.ELEMENT_ERROR、BaseComponent.ELEMENT_WARNING、BaseComponent.ELEMENT_NORMAL、BaseComponent.ELEMENT_SUCCESS
            var messageType = this.checkResult;
            if (messageType == BASE.ELEMENT_SUCESS) { // 当前状态为成功，则直接返回
                return;
            } else if (messageType == BASE.ELEMENT_ERROR || messageType == BASE.ELEMENT_WARNING) { // 获取错误提示或警告提
                if (this.isError && this.errorMessage != null && this.errorMessage != "") {
                    text = this.errorMessage;
                } else {
                    return;
                }
            } else if (messageType == BASE.ELEMENT_NORMAL) { // 获取常规输入辅助提示
                if (this.options.inputAssistant != null && this.options.inputAssistant != "") {
                    text = this.options.inputAssistant;
                } else {
                    return;
                }
            }
            // 计算文字宽度
            var textWidth = $.measures.getTextWidth(text, this.options.className + "_FLOAT_MESSAGE_TEXT");
            if (textWidth == null || textWidth < 150) textWidth = 150;
            var floatMessageDiv = null;
            if (!window.floatMessageDiv) {
                var floatMessageDiv = $("<div></div>").hide().css({
                    'position': 'absolute',
                    'z-index': $.measures.getZIndex()
                }).appendTo('body');
                window.floatMessageDiv = floatMessageDiv.get(0);

                // 左侧DIV
                var leftDiv = $("<div></div>").addClass('div_left').appendTo(floatMessageDiv);

                // 右侧DIV
                var rightDiv = $("<div></div>").addClass('div_right').appendTo(floatMessageDiv);

                // 中间DIV
                var centerDiv = $("<div></div>").addClass('div_center').appendTo(floatMessageDiv);
                window.floatMessageDiv.centerDiv = centerDiv.get(0);

                // 箭头DIV
                var arrowDiv = $("<div></div>").addClass('div_arrow').appendTo(floatMessageDiv);

                // 文字DIV
                var textDiv = $("<div></div>").addClass('div_text').appendTo(floatMessageDiv);
                window.floatMessageDiv.textDiv = textDiv.get(0);

            }
            floatMessageDiv = $(window.floatMessageDiv);
            // 重新调整样式、大小、显示位置、显示内
            if (messageType == BASE.ELEMENT_ERROR) // 设置错误提示样式
                floatMessageDiv.attr('class', this.options.className + "_float_message_div_error");
            else if (messageType == BASE.ELEMENT_WARNING) // 设置警告提示样式
                floatMessageDiv.attr('class', this.options.className + "_float_message_div_warning");
            else if (messageType == BASE.ELEMENT_NORMAL) // 设置常规提示样式
                floatMessageDiv.attr('class', this.options.className + "_float_message_div_normal");
            // 重新调整大小
            floatMessageDiv.css({
                'width': (textWidth + $.measures.getCssHeight(this.options.className + "_FLOAT_MESSAGE_LEFT_WIDTH") + $.measures.getCssHeight(this.options.className + "_FLOAT_MESSAGE_RIGHT_WIDTH")) + "px",
                'top': (this.element.offset().top + this.element.outerHeight()) + "px",
                'left': this.element.offset().left + "px"
            }).show();
            centerDiv.css('width', textWidth + "px");
            textDiv.html(text);
        },
        hideFloatMessageDiv: function () {
            if (window.floatMessageDiv) {
                $(window.floatMessageDiv).hide();
            }
        },
        setFocus: function () {
            if (this.visible) {
                if (this.options.disabled) {
                    this.mayFocus = true;
                } else {
                    var oThis = this;
//                    if ($.browsersupport.IS_IE) {
//                        this.input.focus();
//                        this.input.select();
//                    } else { // firefox等浏览器不能及时执行focus方法
                        window.setTimeout(function () {
                                oThis.input.focus();
                                oThis.input.select();
                            },
                            50);
//                    }
                }
            }
            this.ctxChanged = true;
        },
        getValue: function () {
            if (this.newValue) {
                return this.newValue;
            } else if (this.options.value) {
                return this.options.value;
            } else {
                return "";
            }
        },
        /**
         * 设置值时要检测类
         */
        setValue: function (text) {
            var opts = this.options;
            text = $.argumentutils.getString(text, "");
            // 只有字符串和密码类型才需要此处理
            if (opts.dataType == 'A' || opts.dataType == 'P') {
                if (opts.maxSize != -1) {
                    // 初始设定的字符串的长度如果超过了设定的最大字节数,需要截断后显示
                    if (text.lengthb() > opts.maxSize) text = text.substrCH(opts.maxSize);
                }
            }
            // 记录旧
            this.oldValue = this.newValue;
            this.newValue = text;
            
            this.maskValue();
            if (this.focused) this.input.val(this.newValue);
            else this.input.val(this.showValue);
            // 显示格式化后Tip
            if (opts.dataType != 'P') {
                if (this.showTipMessage && this.showTipMessage != null) this.setMessage(this.showTipMessage);
                else this.setMessage(this.showValue);
            }
            if (this.checkTip()) {
                if (this.input.val() == "") this.showTip();
                else this.input.css('color', 'black');
            }
            if (this.textColor != null) this.input.css('color', 'red');
            if (this.widgetName == "floatText") {
                if (this.newValue.indexOf(',') != -1) return;
            }
            if (this.newValue != this.oldValue) if (!opts.disabled)
           		this.valuechanged();
            this.ctxChanged = true;
        },
        /**
         * 在末尾插入字符（包括退格键，用于直接通过虚拟键盘进行设值操作）
         * @param charCode 字符Unicode编码
         */
        addCharCode: function (charCode) {
            if (charCode == null) return;
            var oldValue = this.getValue();
            if (oldValue != null) oldValue = oldValue.toString();
            else oldValue = "";
            var newValue = "";
            // 得到格式化器
            var formater = this.getFormater();
            if (charCode == 8) { // 退格键
                if (oldValue == "") {
                    return;
                } else {
                    newValue = oldValue.substring(0, oldValue.length - 1);
                    this.setValue(newValue);
                }
            } else {
                var charValue = String.fromCharCode(charCode);
                newValue = oldValue + charValue;
                // 获取keyValue,currValue,aggValue,
                if (formater.valid(charValue, newValue, oldValue) == false) return;
                if (this.afterValid(charCode, charValue) == false) return;
                this.setValue(newValue);
            }
        },
        getShowValue: function () {
            return this.showValue;
        },
        setShowValue: function (text) {
            this.showValue = text;
            this.input.val(text);
        },
        maskValue: function () {
            var masker = $.pageutils.getMasker(this.widgetName);
            if (masker != null) {
                this.showValue = masker.format(this.newValue).value;
                this.textColor = masker.format(this.newValue).color;
            } else this.showValue = this.newValue;
        },
        setActive: function (isActive) {
            var isActive = $.argumentutils.getBoolean(isActive, false);
            if (!isActive) {
                this.setReadOnly(true);
                this.input.css('background-color', '#E1E1E1');
                try {
                    if (this.Div_text.children().size() > 0) {
                        this.Div_text.children().css('background-color', '#E1E1E1')
                    }
                } catch (e) {
                    Logger.error(e);
                }
            }
            else if (isActive) {
                this.setReadOnly(false);
                this.input.css('background-color', '#FFFFFF').attr('class', this.inputClassName_init);
                if (this.Div_text != null) this.Div_text.attr('class', this.options.className);
                if (this.mayFocus) {
                    this.input.focus();
                    this.mayFocus = false;
                }
                try {
                    if (this.Div_text.children().size() > 0) {
                        this.Div_text.children().css('background-color', '#FFFFFF')
                    }
                } catch (e) {
                    Logger.error(e);
                }
            }
            this.ctxChanged = true;
        },

        isActive: function () {
            return !this.options.disabled;
        },
        /**
         * 检测是否是整数
         *
         * @private
         */
        checkInteger: function (data) {
            if (data == null || data == "") return false;
            else if (!$.argumentutils.isNumber(data)) return false;
            return true;
        },
        setMaxSize: function (size) {
            this.options.maxSize = parseInt(size, 10);
        },
        setReadOnly: function (readOnly) {
            this.input.prop({
                'readOnly': readOnly,
                'class': this.inputClassName_init
            });
            this.options.readOnly = readOnly;
            this.Div_text.attr('class', this.options.className + (readOnly ? (" " + "text_div_readonly") : ''));
            this.ctxChanged = true;
        },
        setErrorStyle: function () {
            if (!this.options.readOnly && this.isError) {
                if (this.Div_text) {
                    this.Div_text.attr('class', this.options.className + " " + "text_div_error");
                }
                switch (this.widgetName) {
                    case "reference":
                    case "language":
                    case "combo":
                    case "date":
                        this.warnIcon.css('right', '25px');
                        break;
                    default:
                        this.warnIcon.css('right', '10px');
                        break;
                }
//                if ($.browsersupport.IS_IPAD) {
//                    this.warnIcon.css('right', (parseInt(this.warnIcon.css('right'), 10) + 10) + "px");
//                }
                this.errorMsgDiv.hide();
                this.warnIcon.show();
            }
        },
        setWarningStyle: function () {
            if (!this.options.readOnly) {
                if (this.Div_text != null) this.Div_text.attr('class', this.options.className + " " + this.options.className + "_warning_bgcolor");
            }
        },
        setFocusStyle: function () {
            if (!this.options.readOnly) {
                if (this.Div_text != null && -1 == this.Div_text.attr('class').indexOf("_error_bgcolor") && -1 == this.Div_text.attr('class').indexOf("_warning_bgcolor")) this.Div_text.attr('class', this.options.className + " " + this.options.className + "_focus_bgcolor");
            }
        },
        setBlurStyle: function () {
            if (!this.options.readOnly) {
                if (this.Div_text != null && -1 == this.Div_text.attr('class').indexOf("_error_bgcolor") && -1 == this.Div_text.attr('class').indexOf("_warning_bgcolor")) this.Div_text.attr('class', this.options.className);
            }
        },
        setNormalStyle: function () {
            if (!this.options.readOnly) {
                if (this.Div_text != null) {
                    this.Div_text.attr('class', this.options.className);
                }
                if(this.errorMsgDiv){
                	 this.errorMsgDiv.hide();
                }   
                if(this.warnIcon){
                	this.warnIcon.hide()
                }
                
            }
        },
        setErrorPosition: function (parentElement, left, top) {
            if (typeof(parentElement) != 'undefined' && typeof(left) == 'number' && typeof(top) == 'number') {
                $(parentElement).append(this.errorMsgDiv);
                this.errorMsgDiv.css({
                    'top': top + "px",
                    'left': left + "px"
                });
            } else if (typeof(parentElement) != 'undefined') { // inputDialogComp使用
                var currParentElement = this.element.parent();
                var tempTop = 0;
                var tempLeft = 0;
                while (typeof(currParentElement) == "object") {
                    if (currParentElement.attr('class') && currParentElement.attr('class') == $(parentElement).attr('class')) {
                        $(parentElement).append(this.errorMsgDiv);
                        this.errorMsgDiv.css({
                            'visibility': 'visible',
                            'top': (tempTop + 5 - this.errorMsgDiv.outerHeight()) + "px",
                            'left': (tempLeft + 10) + "px"

                        }).hide();
                        break;
                    }
                    if (typeof(currParentElement.get(0).offsetLeft) == "number") {
                        tempLeft += currParentElement.get(0).offsetLeft;
                    }
                    if (typeof(currParentElement.get(0).offsetTop) == "number") {
                        tempTop += currParentElement.get(0).offsetTop;
                    }
                    if (typeof(currParentElement.get(0).offsetParent) == "object") {
                        currParentElement = $(currParentElement.get(0).offsetParent);
                    } else if (typeof(currParentElement.get(0).parentOwner) == "object") {
                        currParentElement = $(currParentElement.get(0).parentOwner);
                    } else {
                        currParentElement = $(currParentElement.get(0).parentNode);
                    }
                }
            } else {
                parentElement = this.element.parent();
                var tempTop = 0;
                var tempLeft = 0;
                while (typeof(parentElement) == "object"&&parentElement.length>0) {
                    if (parentElement.attr('id') && parentElement.attr('id').indexOf('_um') != -1) {
                        parentElement.append(this.errorMsgDiv);
                        this.errorMsgDiv.css({
                            'visibility': 'visible',
                            'top': (tempTop + this.element.get(0).offsetTop - this.errorMsgDiv.outerHeight()) + "px",
                            'left': (tempLeft + this.element.get(0).offsetLeft + 10) + "px"
                        }).hide();
                        break;
                    }
                    if (typeof(parentElement.get(0).offsetLeft) == 'number') {
                        tempLeft += parentElement.get(0).offsetLeft;
                    }
                    if (typeof(parentElement.get(0).offsetTop) == 'number') {
                        tempTop += parentElement.get(0).offsetTop;
                        parentElement = $(parentElement.get(0).offsetParent);
                        continue;
                    }
                    if (typeof(parentElement.get(0).parentOwner) == "object") {
                        parentElement = $(parentElement.get(0).parentOwner);
                    } else {
                        parentElement = $(parentElement.get(0).parentNode);
                    }
                }
            }
        },
        /**
         * 获取Label对象
         */
        getLabel: function () {
            return this.label;
        },
        /**
         * 获取对象信息
         *
         * @private
         */
        getContext: function () {
            if (!this.ctxChanged) return null;
            return {
                c: "TextContext",
                id: this.element.attr("id"),
                enabled: !this.options.disabled,
                value: this.newValue ? encodeURIComponent(this.newValue) : this.newValue,
                readOnly: this.options.readOnly,
                visible: this.visible
            };
        },
        /**
         * 设置对象信息
         *
         * @private
         */
        setContext: function (context) {
            if (!context.isform) {
                if (context.value != null && context.value != this.input.val()) this.setValue(context.value);
            }
            if (context.readOnly != null && this.options.readOnly != context.readOnly) this.setReadOnly(context.readOnly);
            this.ctxChanged = false;

        },
        hideV: function () {
            this._super();
        },
        setVisible: function (visible) {
            if (visible) this.showV();
            else this.hideV();
        },

        _initErrorMsg: function () {
            var oThis = this;
            this.errorMsgDiv = $("<div></div>").addClass('error_msg_div').hide();
            $("<div></div>").addClass('error_left_div').appendTo(this.errorMsgDiv);
            $("<div></div>").addClass('error_center_div').appendTo(this.errorMsgDiv);
            $("<div></div>").addClass('error_right_div').appendTo(this.errorMsgDiv);

            this.errorCenterDiv = $("<div></div>").addClass('error_content_div').appendTo(this.errorMsgDiv);
            this.errorMsgDiv.on('click', function (e) {
                oThis.input.triggerHandler('click');
                oThis.input.focus();
                $(this).hide();
            }).appendTo(this.element);

            this.warnIcon = $("<div></div>").addClass('warn_icon').hide().appendTo(this.element);
        }

    });
})(jQuery);
