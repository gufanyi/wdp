/**
 * @fileoverview 各种工具方法,包括各种表单域的验证方法.
 *
 * @author wlh
 * @version 1.0
 */
(function($) {
    /**
     * 数据类型定义
     * @class 数据类型
     */
    $.datatype = {
        STRING : "String", //STRING类型
        INTEGER : "Integer", //Integer类型
        INT : "int", //int类型
        DOUBLE : "Double", //Double类型
        dOUBLE : "double", //double类型
        FDOUBLE : "FDouble", //FDouble类型
        FLOAT : "Float", //Float类型
        fLOAT : "float", //float类型
        BYTE : "Byte", //Byte类型
        bYTE : "byte", //byte类型
        BOOLEAN : "Boolean", //Boolean类型
        bOOLEAN : "boolean", //boolean类型
        FBOOLEAN : "FBoolean", //FBoolean类型
        DATE : "Date", //Date类型
        BIGDECIMAL : "BigDecimal", //BigDecimal类型
        lONG : "long", //long类型
        CHAR : "char", //char类型
        CHARACTER : "Character", //Character类型
        FDATETIME : "FDateTime", //FDateTime类型
        FDATE : "FDate", //FDate类型
        FTIME : "FTime", //FTime类型
        FLITERALDATE : "FLiteralDate", //FLiteralDate类型
        FDATEBEGIN : "FDate_begin",
        FDATEEND : "FDate_end",
        FNUMBERFORMAT : "FNumberFormat", //FNumberFormat类型
        Decimal : "Decimal", //Decimal类型
        Entity : "Entity"	//Entity类型
    };

    /**
     * 编辑类型定义
     */
    $.editortype = {
        CHECKBOX : "CheckBox", //CheckBox类型
        STRINGTEXT : "StringText", //StringText类型
        INTEGERTEXT : "IntegerText", //IntegerText类型
        DECIMALTEXT : "DecimalText", //DecimalText类型
        SELFDEFELE : "SelfDef", //"自定义"元素类型,是form的一种element
        RADIOGROUP : "RadioGroup", //RadioGroup类型
        CHECKBOXGROUP : "CheckboxGroup", //CheckboxGroup类型
        REFERENCE : "Reference", //Reference类型
        COMBOBOX : "ComboBox", //ComboBox类型
        MULTICOMBOBOX : "MultiComboBox", //多选ComboBox类型
        LANGUAGECOMBOBOX : "LanguageComboBox", //LanguageComboBox类型
        LIST : "List", //List类型
        PWDTEXT : "PwdText", //PwdText类型
        DATETEXT : "DateText", //DateText类型
        MULTIDATETEXT : "MultiDateText", //多选DateText类型
        DATETIMETEXT : "DateTimeText", //DateTimeText类型
        MULTIDATETIMETEXT : "MultiDateTimeText",
        SHORTDATETEXT : "ShortDateText", //ShortDateTimeText类型
        LITERALDATE : "FLiteralDate",
        TEXTAREA : "TextArea", //TextArea类型
        RICHEDITOR : "RichEditor", //RichEditor类型
        IMAGECOMP : "ImageComp",
        FILECOMP : "FileComp"
    };

    $.EditorTypeMap = {
        "CheckBox" : 'checkbox',
        "StringText" : 'stringtext',
        "IntegerText" : 'integertext',
        "DecimalText" : 'floattext',
        "SelfDef" : 'selfdefelement',
        "RadioGroup" : 'radiogroup',
        "CheckboxGroup" : 'checkboxgroup',
        "Reference" : 'reference',
        "ComboBox" : 'combo',
        "MultiComboBox" : 'combo',
        "LanguageComboBox" : 'languageComboBox',
        "List" : 'list',
        "PwdText" : 'pwdtext',
        "DateText" : 'datetext',
        "DateTimeText" : 'datetext',
        "ShortDateText" : 'datetext',
        "MultiDateText" : 'datetext',
        "MultiDateTimeText" : 'datetext',
        "FLiteralDate" : 'FLiteralDate',
        "TextArea" : 'textarea',
        "RichEditor" : 'editor',
        "ImageComp" : 'image',
        "FileComp" : 'file'
    };

    $.browsersupport = {
        IS_IE : false, //是IE浏览器
        IS_FF : false, //是Firefox浏览器
        IS_OPERA : false, //是Opera浏览器
        IS_CHROME : false, //是Chrome浏览器
        IS_SAFARI : false, //是Safari浏览器
        IS_WEBKIT : false, //是Webkit浏览器
        IS_IE6 : false, //是IE6浏览器
        IS_IE7 : false, //是IE7浏览器
        IS_IE8 : false, //是IE8浏览器
        IS_IE9 : false, //是IE9浏览器
        IS_IOS : false,
        IS_IPHONE : false,
        IS_IPAD : false,

        IS_IE8_CORE : false,
        IS_IE9_CORE : false,
        IS_IE10_ABOVE : false,
        /**
         * 标准浏览器(IE9、firefox、chrome)
         * modify by licza 增加对XULRunner的识别
         * @type {Boolean}
         */
        IS_STANDARD : false,
        BROWSER_VERSION : 0
    };

    $.argumentutils = {
        /**
         * 获取布尔值
         * @param  value 输入值
         * @param {Boolean} defaultValue 默认值
         * @return {Boolean} 返回value解析后的Boolean值，value未能正确解析成Boolean值时，返回defalutValue
         */
        getBoolean : function(value, defaultValue) {
            if (value == 'false')
                return false;
            else if (value == 'true')
                return true;
            else if (value != false && value != true)
                return defaultValue;
            else
                return value;
        },
        /**
         * 获取字符串的值
         * @param  value 输入值
         * @param {String} defaultValue 默认值
         * @return {String} 返回输入值, 输入值为null或者为""时，返回defaultValue
         */
        getString : function(value, defaultValue) {
            if (value == null || value == "")
                return defaultValue;
            return value;
        },
        /**
         * 获取整型值
         * @param value 输入值
         * @param {Int} defaultValue 默认值
         * @return {Int} 返回parseInt后的输入值，未能正确解析时，返回defaultValue
         */
        getInteger : function(value, defaultValue) {
            if (isNaN(parseInt(value)))
                return defaultValue;
            return parseInt(value);
        },
        /**
         * 获取浮点值
         * @param value 输入值
         * @param {Float} defaultValue 默认值
         * @return {Float} 返回parseFloat后的输入值，未能正确解析时，返回defalutValue
         */
        getFloat : function(value, defaultValue) {
            if (isNaN(parseFloat(value)))
                return defaultValue;
            return parseFloat(value);
        },
        /**
         * 判断是否为空
         * @param value 输入值
         * @param {Boolean} canBlank ture表示输入值可以为"",不计算为null;false表示输入值不可以为""，计算为null
         * @return {Boolean} 输入值是否为空
         */
        isNull : function(value, canBlank) {
            if (value == null)
                return true;
            if (value == "" && !canBlank)
                return true;
            return false;
        },
        /**
         * 判断是否为非空
         * @param value 输入值
         * @param {Boolean} canBlank ture表示输入值可以为"",不计算为null;false表示输入值不可以为""，计算为null
         * @return {Boolean} 输入值是否为非空,与 {@link isNull} 相反
         * @see isNull
         */
        isNotNull : function(value, canBlank) {
            return !$.argumentutils.isNull(value, canBlank);
        },
        /**
         * 把百分比转成小数
         * @param per 百分比
         * @return {Float} 百分比转化后的小数
         */
        convertPerToDecimal : function(per) {
            if (per.indexOf("%") == -1)
                return;

            var decimal = per.substring(0, per.length - 1);
            decimal = parseInt(decimal) / 100;
            return decimal;
        },

        /**
         * 判断是否为百分数
         * @param value 输入值
         * @return {Boolean} 输入值是否为"xx%"格式的百分数
         */
        isPercent : function(value) {
            return value == null ? false : ("" + value).indexOf("%") != -1;
        },

        /**
         *
         * 验证传入的str是否是数字 alert(validate("1.22")) //true alert(validate("111")) //true
         * alert(validate("1..22")) //false alert(validate("1.2a2")) //false
         * alert(validate("1.")) //false
         * @param str 输入值
         * @return {Boolean}
         */
        isDigital : function(str) {
            var re = /^((-?)([1-9]+[0-9]*|0{1}))(\.\d+)?$/;
            return re.test(str);
        },

        /**
         * 验证传入的字符是否是字母
         * @param str 输入值
         * @return {Boolean}
         */
        isAlpha : function(str) {
            var patrn = /^[A-Za-z]+$/;
            if (!patrn.exec(str))
                return false;
            return true;
        },

        /**
         * 验证是否仅是(0-9)的数字
         * @param str 输入值
         * @return {Boolean}
         */
        isNumberOnly : function(str) {
            var patrn = /^[0-9]+$/;
            if (!patrn.exec(str))
                return false;
            return true;
        },

        /**
         * 验证传入的字符是否是整数(0-9包括"-"),且整数必须介于javascript规定的最大值最小值之间
         * @param str 输入值
         * @return {Boolean}
         */
        isNumber : function(str) {
            // 将输入的str转化为字符串.否则js认为000 == "0"为true
            str = str + "";
            if (str == "0")
                return true;

            var patrn = /(^-[1-9]\d*$)|^([1-9]\d*$)/;
            if (patrn.exec(str) == null)
                return false;
            else {
                if (parseInt(str) >= -9007199254740992 && parseInt(str) <= 9007199254740992)
                    return true;
                else
                    return false;
            }
        },

        /**
         * 验证输入的是否为中文
         * @param s 输入值
         * @return {Boolean}
         */
        isChinese : function(s) {
            var patrn = /^[\u0391-\uFFE5]+$/;
            if (!patrn.exec(s))
                return false;
            return true;
        },

        /**
         *
         * 验证传入串是否是合法的javascript标示符
         * javascript合法标示符:第一个字符必须是字母、下划线(_)、美元符号($),接下来的字符可以是字母、数字或下划线
         * @param str {输入值}
         * @return {Boolean}
         */
        isValidIdentifier : function(str) {
            str = str.trim();
            var flag = true;
            var first = str.charAt(0);
            if (!($.argumentutils.isAlpha(first) || first == "_" || first == "$"))
                return false;

            for (var i = 1; i < str.length; i++) {
                var leta = str.charAt(i);
                if (!($.argumentutils.isAlpha(leta) || $.argumentutils.isDigital(leta) || leta == "_")) {
                    flag = false;
                    break;
                }
            }
            return flag;
        },

        /**
         * 验证传入串是否email格式
         * @param s 输入值
         * @return {Boolean}
         */
        isEmail : function(s) {
            var patrn = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
            if (!patrn.exec(s))
                return false;
            return true;
        },

        /**
         * 是否是电话号码 匹配: 0910-123456 或 029-123456 或029-12345678 或 12345678912
         * @param s 输入值
         * @return {Boolean}
         */
        isPhone : function(s) {
            var patrn = /^(?:0[0-9]{2,3}[-\\s]{1}|\\(0[0-9]{2,4}\\))[0-9]{6,8}$|^[1-9]{1}[0-9]{5,7}$|^[1-9]{1}[0-9]{10}$/;
            if (!patrn.exec(s))
                return false;
            return true;
        },

        /**
         * 转译xml关键字符(& < > ' \等)
         * @param {String} str 输入值
         * @return {String} 转译后的xml字符串
         */
        convertXml : function(str) {
            if (str != null) {
                str = str + "";
                var reg1 = new RegExp("&", "g");
                str = str.replace(reg1, "&amp;");

                var reg2 = new RegExp("<", "g");
                str = str.replace(reg2, "&lt;");

                var reg3 = new RegExp(">", "g");
                str = str.replace(reg3, "&gt;");

                var reg4 = new RegExp("'", "g");
                str = str.replace(reg4, "&apos;");

                var reg5 = new RegExp("\"", "g");
                str = str.replace(reg5, "&quot;");
            }
            return str;
        },

        /**
         * 在光标位置插入数据（暂时只支持IE）
         * @param myField 光标所在输入框对象
         * @param {String} myValue 插入值
         * @return {String} myField.value
         */
        insertAtCursor : function(myField, myValue) {
            if (document.selection) {
                if (document.selection.type == 'Text') {
                    document.selection.clear();
                }
                sel = document.selection.createRange();
                sel.text = myValue;
            }
            return myField.value;
        },
        /**
         * 强制截断浮点数
         *
         * @param floatValue：浮点数
         * @param precision：保留小数点后位数
         * @author guoweic 2009-10-16
         */
        truncFloat : function(floatValue, precision) {
            if (precision) {
                if (!floatValue)
                    floatValue = "0";
                if (floatValue.toString().indexOf(".") == -1 && precision > 0) {
                    floatValue += ".";
                    for (var i = 0; i < precision; i++) {
                        floatValue += "0";
                    }
                } else if (floatValue.toString().indexOf(".") != -1) {
                    if (precision <= 0)
                        floatValue = floatValue.toString().substring(0, floatValue.toString().indexOf(".") + 1);
                    else {
                        floatValue = floatValue.toString();
                        if (floatValue.length < floatValue.indexOf(".") + precision + 1) {
                            for (var i = 0, n = floatValue.indexOf(".") + precision + 1 - floatValue.length; i < n; i++) {
                                floatValue += "0";
                            }
                        } else
                            floatValue = floatValue.substring(0, floatValue.indexOf(".") + precision + 1);
                    }
                }
            }
            return floatValue;
        },
        mergeParameter : function(sourceParam, appendParam) {
            if (sourceParam == null || sourceParam == "")
                return appendParam;
            else if (appendParam == null || appendParam == "")
                return sourceParam;
            else {
                return sourceParam + "&" + appendParam;
            }
        },
        addImgSuffix : function(imgUrl,suffix) {
        	if(imgUrl!=null && suffix!=null) {
        		var _prefix = imgUrl.substring(0,imgUrl.lastIndexOf('.'));
        		var _suffix = imgUrl.substr(imgUrl.lastIndexOf('.'));
        		return _prefix + suffix + _suffix;
        	}
        }
    };

    $.measures = {
        classWidthCache : $.hashmap.getObj(),
        /**
         * 根据css属性计算文本宽度        9/30  zww       
         */
        getTextWidth : function(text, className) {
            var cacheKey = "NULL";
            if (className) {
                cacheKey = className;
            }
            var lengthb = 0;
            if (text) {
                lengthb = text.lengthb();
            }
            var charwidth = $.measures.classWidthCache.get(cacheKey);
            if (!charwidth) {
                var tmpDiv = $("<DIV>").appendTo("body").html("U").attr("class", className).css({
                    'position' : "absolute",
                    'top' : "0px",
                    'left' : "0px",
                    'width' : 'auto',
                    'font-size' : "12px",
                    'visibility' : 'hidden',
                    'white-space' : 'nowrap'
                });
                charwidth = tmpDiv.width()-2;
                tmpDiv.remove();
                $.measures.classWidthCache.put(cacheKey, charwidth);
            }
            return charwidth * lengthb;
        },

        /**
         * 根据css属性计算文本高度
         */
        getTextHeight : function(text, className) {
            var tmpDiv = $("<div>").appendTo("body").html(text).css({
                'position' : "absolute",
                'top' : "0px",
                'left' : "0px",
                'height' : 'auto',
                'visibility' : 'hidden',
                'white-space' : 'nowrap'
            }).addClass(className);
            var height = tmpDiv.outerHeight();
            tmpDiv.remove();
            return height;
        },

        /**
         * 获取css中定义的高度值（用来将JS中定义的常量挪到CSS中定义并获取）
         */
        getCssHeight : function(className) {
            var tmpDiv = $("<div>").appendTo("body").addClass(className);
            var height = tmpDiv.outerHeight();
            tmpDiv.remove();
            return height;
        },

        /**
         * 判断div是否出了水平滚动条
         */
        isDivScroll : function(div) {
        	var di = $(div);
            // IE和oprea比较scrollHeight和clientHeight fireFox比较
            // offsetScroll和clientHeight（guoweic: 该说法不准确）
            // modified by guoweic（IE和firefox都可以用scrollWidth和clientWidth做比较）
            if (di.scrollLeft() > di.innerWidth())
                return true;
            else
                return false;
        },

        /**
         * 判断div是否出竖直滚动条
         */
        isDivVScroll : function(div) {
            var di = $(div);
            if (di.scrollTop() > di.innerHeight())
                return true;
            else
                return false;
        },

        /**
         * 找到第一个滚动偏移
         */
        compFirstScrollTop : function(oHtml) {
            var otml = $(oHtml);
            var offsetTop = 0;
            while (otml.size()) {
                //	alert("循环死了吗");
                offsetTop = otml.scrollTop();
                if (offsetTop != 0)
                    return offsetTop == null ? 0 : offsetTop;
                otml = otml.parent();
            }
            return 0;
        },

        /**
         * 找到第一个滚动对应的clientHeight
         */
        compFirstScrollClientHeight : function(oHtml) {
            var otml = $(oHtml);
            //oHtml是DOM对象，转换成jQuery对象，才可使用如下方法
            var offsetTop = 0;
            while (otml.size()) {
                //	alert("循环死了吗");
                offsetTop = otml.scrollTop();
                if (offsetTop != 0) {
                    var clientHeight = otml.innerHeight();
                    return clientHeight == null ? 0 : clientHeight;
                }
                otml = otml.parent();
            }
            return 0;
        },

        /**
         * 将控件放在视线内
         *
         * @param eleDiv
         *            控件的显示对象(div)
         */
        positionElementInView : function(eleDiv) {
            // if (eleDiv.style.left < 0)
            // eleDiv.style.left = "5px";
            // if (eleDiv.style.pixelTop + eleDiv.offsetHeight > document.body.clientHeight)
            // eleDiv.style.top = document.body.clientHeight - eleDiv.offsetHeight + "px";
            if (parseInt($(eleDiv).css("top")) + $(eleDiv).outerHeight() > $(document.body).innerHeight())
                $(eleDiv).css({
                    top : $(document.body).innerHeight() - $(eleDiv).outerHeight() + "px"
                });
            // if (eleDiv.style.pixelTop < 0)
            // eleDiv.style.top = "0px";
        },

        positionElementToScreenCenter : function(element, sctop, clinetHeight) {
            var ele = $(element);
            var height = ele.outerHeight();
            var width = ele.outerWidth();
            // var screenHeight = window.screen.availHeight - 400;
            // var screenWidth = 1200;//window.screen.availWidth;
            var screenHeight = $(document.body).innerHeight();
            // window.screen.availHeight;
            var screenWidth = $(document.body).innerWidth();
            // window.screen.availWidth;
            if (ele.parent()) {
                screenWidth = ele.parent().outerWidth() < screenWidth ? ele.parent().outerWidth() : screenWidth;
            }
            var top = screenHeight - height;
            if (top < 0)
                top = 60;
            // if (sctop != null)
            // element.style.top = parseInt(top / 2) + parseInt(sctop) + "px";
            // else
            ele.css({
                top : top / 2 + "px"
            });
            var left = screenWidth - width;
            if (left < 0)
                left = 0;
            ele.css({
                left : left / 2 + "px"
            });
        },

        /**
         * 将节点放在父窗口中央
         */
        positionElementToCenter : function(element, reltop, clientHeight) {
            var parentWidth = 0;
            var parentHeight = 0;
            //	if (element.parent().tagName.toUpperCase() == "BODY") {
            if ((element.parent())[0].tagName.toUpperCase() == "BODY") {
                parentWidth = $(document.body).innerWidth();
                parentHeight = $(document.body).innerHeight();
            } else {
                parentWidth = element.parent().outerWidth() < 1200 ? element.parent().outerWidth() : 1200;
                parentHeight = element.parent().outerHeight();
            }
            if (clientHeight != null && clientHeight > 0) {
                parentHeight = clientHeight;
            }
            var eleWidth = element.css("width");
            var eleHeight = element.css("height");
            var index = eleWidth.indexOf("%");
            if (index != -1) {
                left = (parentWidth * (1 - $.measures.translatePercentToFloat(eleWidth))) / 2;
                left = parseInt(left);
            } else if (parentWidth <= parseInt(eleWidth))
                left = 0;
            else {
                left = (parentWidth - parseInt(eleWidth)) / 2;
                left = parseInt(left);
            }
            index = eleHeight.indexOf("%");
            if (index != -1) {
                topb = (parentHeight * (1 - $.measures.translatePercentToFloat(eleHeight))) / 2;
                topb = parseInt(topb);
            } else if (parentHeight <= parseInt(eleHeight))
                topb = 0;
            else {
                topb = (parentHeight - parseInt(eleHeight)) / 2;
                topb = parseInt(topb);
            }
            element.css({
                position : "absolute"
            });
            // 不计算滚动条内隐藏的高度
            if (reltop != null && reltop != 0)
                topb += reltop;
            element.css({
                top : topb + "px"
            });
            element.css({
                left : left + "px"
            });
        },

        /**
         * @private
         */
        positionneSelonEvent : function(eltApos, e) {
            //		alert("进来了");
            //		alert(e.pageX);
            var droite = $(document.body).innerWidth() - e.pageX;
            var bas = $(document.body).innerHeight() - e.pageY;
            var scrollLeft = $(document.body).scrollLeft();

            if (droite < eltApos.outerWidth())
                eltApos.css({
                    left : scrollLeft + e.pageX - eltApos.outerWidth() + "px"
                });
            else
                eltApos.css({
                    left : scrollLeft + e.pageX + "px"
                });

            if (bas < eltApos.outerHeight())
                eltApos.css({
                    top : scrollLeft + e.pageY - eltApos.outerHeight() + "px"
                });
            else
                eltApos.css({
                    top : scrollLeft + e.pageY + "px"
                });
        },

        positionneSelonPosFournie : function(node, top, left) {
            var visibleWidth = $(document.body).innerWidth() - left;
            var bas = $(document.body).innerHeight() - top;
            var scrollLeft = $(document.body).scrollLeft();

            if (visibleWidth < node.outerWidth())
                node.css({
                    left : scrollLeft + left - (node.outerWidth() - visibleWidth) + "px"
                });
            else
                node.css({
                    left : scrollLeft + left + "px"
                });

            if (bas < node.outerHeight()) {
                var realTop = scrollLeft + top - node.outerHeight();
                if (realTop < 0)
                    realTop = 0;
                node.css({
                    top : realTop + "px"
                });
            } else
                node.css({
                    top : scrollLeft + top + "px"
                });
        },

        /**
         * 把百分比转换为符点数
         *
         * @param percent
         *            百分数形式
         */
        translatePercentToFloat : function(percent) {
            var index = percent.indexOf("%");
            if (index == -1) {
                log("Measures.js(translatePercentToFloat), The string: " + percent + " is not in percent format!");
                return 1;
            }
            try {
                return parseInt(percent.substring(0, index)) / 100;
            } catch (exception) {
                log("Measures.js(translatePercentToFloat)," + exception.name + ":" + exception.message);
                return 1;
            }
        },

        /**
         * 获取当前zindex
         */
        getZIndex : function() {
            return ++$.CONST.STANDARD_ZINDEX;
        },
        /**
         * 获取最外层zindex
         */
        getTopZIndex : function(zindex) {
            var z_index = parseInt(zindex, 10);
            if (isNaN(z_index)) {
                z_index = $.measures.getZIndex();
            } else {
                if (z_index < $.CONST.STANDARD_ZINDEX) {
                    z_index = $.measures.getZIndex();
                }
            }
            return z_index;
        },

        /**
         * 将宽度转换成适合的形式
         */
        convertWidth : function(width) {
            if (width == null || width == "")
                return width;
            width = width + "";
            if (width.indexOf('%') != -1)
                return width;
            if (width.indexOf('px') != -1)
                return width;
            return width + "px";
        },

        /**
         * 将高度转换成适合的形式
         *
         * @param {}
         *            height
         */
        convertHeight : function(height) {
            if (height == null || height == "")
                return height;
            height = height + "";
            if (height.indexOf('%') != -1)
                return height;
            if (height.indexOf('px') != -1)
                return height;
            return height + "px";
        }
    };

    (function() {
        var ua = navigator.userAgent.toLowerCase(), s, o = {};
        if ( s = ua.match(/opera.([\d.]+)/)) {
            $.browsersupport.IS_OPERA = true;
        } else if ( s = ua.match(/chrome\/([\d.]+)/)) {
            $.browsersupport.IS_CHROME = true;
            $.browsersupport.IS_STANDARD = true;
        } else if ( s = ua.match(/version\/([\d.]+).*safari/)) {
            $.browsersupport.IS_SAFARI = true;
        } else if ( s = ua.match(/gecko/)) {
            //add by licza : support XULRunner
            $.browsersupport.IS_FF = true;
            $.browsersupport.IS_STANDARD = true;
        } else if ( s = ua.match(/msie ([\d.]+)/)) {
            $.browsersupport.IS_IE = true;
        } else if ( s = ua.match(/iphone/i)) {
            $.browsersupport.IS_IOS = true;
            $.browsersupport.IS_IPHONE = true;
        } else if ( s = ua.match(/ipad/i)) {
            $.browsersupport.IS_IOS = true;
            $.browsersupport.IS_IPAD = true;
        } else if ( s = ua.match(/firefox\/([\d.]+)/)) {
            $.browsersupport.IS_FF = true;
            $.browsersupport.IS_STANDARD = true;
        } else if ( s = ua.match(/webkit\/([\d.]+)/)) {
            $.browsersupport.IS_WEBKIT = true;
        }

        if (s && s[1]) {
            $.browsersupport.BROWSER_VERSION = parseFloat(s[1]);
        } else {
            $.browsersupport.BROWSER_VERSION = 0;
        }
        if ($.browsersupport.IS_IE) {
            var intVersion = parseInt($.browsersupport.BROWSER_VERSION);
            var mode = document.documentMode;
            if (mode == null) {
                if (intVersion == 6) {
                    $.browsersupport.IS_IE6 = true;
                } else if (intVersion == 7) {
                    $.browsersupport.IS_IE7 = true;
                }
            } else {
                if (mode == 7) {
                    $.browsersupport.IS_IE7 = true;
                } else if (mode == 8) {
                    $.browsersupport.IS_IE8 = true;
                } else if (mode == 9) {
                    $.browsersupport.IS_IE9 = true;
                    $.browsersupport.IS_STANDARD = true;
                } else if (mode == 10) {
                    $.browsersupport.IS_IE10 = true;
                    $.browsersupport.IS_STANDARD = true;
                    $.browsersupport.IS_IE10_ABOVE = true;
                } else {
                    $.browsersupport.IS_STANDARD = true;
                }
                if (intVersion == 8) {
                    $.browsersupport.IS_IE8_CORE = true;
                } else if (intVersion == 9) {
                    $.browsersupport.IS_IE9_CORE = true;
                } else {

                }
            }
        }
    })();
    //TODO: 临时处理
    $.CONST = {
        ATTRFLOAT : $.browsersupport.IS_IE ? "styleFloat" : "cssFloat",
        STANDARD_ZINDEX : 10000
    };
})(jQuery);
/**
 * 等同于document.getElementById(id);
 */
function $ge(id) {
    return document.getElementById(id);
}

/**
 * 等同于document.createElement(obj);
 *
 * @param obj
 * @return
 */
function $ce(obj) {
    return document.createElement(obj);
}
