(function($) {
    /**
     * 扩展String方法
     */
    $.extend(String.prototype, {
        /**
         * 字符串去掉左右空格
         */
        trim : function() {
            return this.replace(/^\s*(\b.*\b|)\s*$/, "$1");
        },
        /**
         * 字符串替换
         */
        replaceStr : function(strFind, strRemp) {
            var tab = this.split(strFind);
            return new String(tab.join(strRemp));
        },
        /**
         * 获得字符串的字节长度
         */
        lengthb : function() {
            return this.replace(/[^\x00-\xff]/g, "**").length;
        },
        /**
         * 将AFindText全部替换为ARepText
         */
        replaceAll : function(AFindText, ARepText) {
            // 自定义String对象的方法
            raRegExp = new RegExp(AFindText, "g");
            return this.replace(raRegExp, ARepText);
        },
        /**
         * 按字节数截取字符串 例:"e我是d".nLen(4)将返回"e我"
         */
        substrCH : function(nLen) {
            var i = 0;
            var j = 0;
            while (i < nLen && j < this.length) {// 循环检查制定的结束字符串位置是否存在中文字符
                if (this.charCodeAt(j) > 256 && i == nLen - 1) {
                    break;
                } else if (this.charCodeAt(j) > 256) {// 返回指定下标字符编码，大于265表示是中文字符
                    i = i + 2;
                }// 是中文字符，那计数增加2
                else {
                    i = i + 1;
                }
                // 是英文字符，那计数增加1
                j = j + 1;
            }
            ;
            return this.substr(0, j);
        },
        /**
         * 校验字符串是否以指定内容开始
         */
        startWith : function(strChild) {
            return this.indexOf(strChild) == 0;
        },
        /**
         * 判断字符串是否以指定参数的字符串结尾
         *
         * @param strChild
         */
        endWith : function(strChild) {
            var index = this.indexOf(strChild);
            if (index == -1)
                return;
            else
                return index == this.length - strChild.length;
        }
    });

    /**
     * 扩展Date方法
     */
    $.extend(Date.prototype, {
        /**
         * 获取AAAAMMJJ类型字符串
         */
        getAAAAMMJJ : function() {
            // date du jour
            var jour = this.getDate();
            if (jour < 10)
                ( jour = "0" + jour);
            var mois = this.getMonth() + 1;
            if (mois < 10)
                ( mois = "0" + mois);
            var annee = this.getYear();
            return annee + "" + mois + "" + jour;
        },
        /**
         * 获取YYYY-MM-DD类型字符串
         */
        getFomatDate : function() {
            var year = this.getFullYear();
            var month = this.getMonth() + 1;
            if (month < 10)
                month = "0" + month;
            var day = this.getDate();
            if (day < 10)
                day = "0" + day;
            return year + "-" + month + "-" + day;
        },
        /**
         * 获取YYYY-MM-DD HH:MM:SS类型字符串
         */
        getFomatDateTime : function() {
            var year = this.getFullYear();
            var month = this.getMonth() + 1;
            if (month < 10)
                month = "0" + month;
            var day = this.getDate();
            if (day < 10)
                day = "0" + day;
            var hours = this.getHours();
            if (hours < 10)
                hours = "0" + hours;
            var minutes = this.getMinutes();
            if (minutes < 10)
                minutes = "0" + minutes;
            var seconds = this.getSeconds();
            if (seconds < 10)
                seconds = "0" + seconds;
            return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
        }
    });

    /**
     * 扩展Array方法
     */
    $.extend(Array.prototype, {
        /**
         * 返回obj在数组中的位置
         */
        indexOf : function(obj) {
            for (var i = 0; i < this.length; i++) {
                if (this[i] == obj)
                    return i;
            }
            return -1;
        },
        /**
         * 按照index remove
         */
        remove : function(index) {
            if (index < 0 || index > this.length) {
                alert("index out of bound");
                return;
            }
            this.splice(index, 1);
        },
        /**
         * 按照数组的元素remove
         */
        removeEle : function(ele) {
            for (var i = 0, count = this.length; i < count; i++) {
                if (this[i] == ele) {
                    this.splice(i, 1);
                    return;
                }
            }
        },
        /**
         * 将指定值ele插入到index处
         */
        insert : function(index, ele) {
            if (index < 0 || index > this.length) {
                alert("index out of bound");
                return;
            }
            this.splice(index, 0, ele);
        },
        /**
         * 得到和索引相对应的数组中的值
         */
        values : function(indices) {
            if (indices == null)
                return null;
            var varr = [];
            for (var i = 0; i < indices.length; i++) {
                varr.push(this[indices[i]]);
            }
            return varr;
        },
        /**
         * 清空数组
         */
        clear : function() {
            this.splice(0, this.length);
        },
        pushAll : function(arr) {
        	if(arr != null) {
        		for(var i=0 ;i<arr.length;i++) {
        			this.push(arr[i]);
        		}
        	}
        }
    });
    /**
     * 生成UUID
     */
    Math.UUID = function() {
        return ((new Date()).getTime() + "").substr(9);
    };
    String.UUID = function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };

    /**
     * @description 模拟HashMap
     * @class 模拟HashMap
     * @constructor
     */
    $.hashmap = function() {
        this.length = 0;
        this.prefix = "js_hashmap_pre_";
        this.trueObj = {};
        this.keyObj = {};
    };

    $.hashmap.getObj = function() {
        return new $.hashmap();
    };

    $.extend($.hashmap.prototype, {
        /**
         * @description 向HashMap中添加键值对
         * @param {String}
         *            key 键
         * @param {Object}
         *            value 值
         * @public
         */
        put : function(key, value) {
            if (this.keyObj[key] != null)
                this.trueObj[key] = value;
            else {
                this.trueObj[key] = value;
                this.keyObj[key] = 1;
                this.length++;
            }
        },
        /**
         * @description 从HashMap中获取value值
         * @param {String}
         *            key 键
         * @return {Object} 键对应的值
         * @public
         */
        get : function(key) {
            return this.trueObj[key];
        },
        /**
         * @description 从HashMap中获取所有key的集合，以数组形式返回
         * @return {Array} HashMap中key的集合
         * @public
         */
        keySet : function() {
            var arrKeySet = [];
            var index = 0;
            for (var strKey in this.keyObj) {
                arrKeySet[index++] = strKey;
            }
            return arrKeySet;
        },
        /**
         * @description 从HashMap中获取value的集合，以数组形式返回
         * @return {Array} HashMap中value的集合
         * @public
         */
        values : function() {
            var arrValues = [];
            var index = 0;
            for (var strKey in this.keyObj) {
                arrValues[index++] = this.trueObj[strKey];
            }
            return arrValues;
        },
        /**
         * @description 获取HashMap的键值对的数量
         * @return {int} HashMap中键值对的数量
         * @public
         */
        size : function() {
            return this.length;
        },
        /**
         * @description 删除指定的值
         * @param {String}
         *            key 要删除的键
         * @return {Object} key对应的value，如果没找到key对应的value，返回null
         * @public
         */
        remove : function(key) {
            if (this.keyObj[key] != null) {
                var obj = this.trueObj[key];
                delete this.keyObj[key];
                delete this.trueObj[key];
                this.length--;
                return obj;
            }
            return null;
        },
        /**
         * @description 清空HashMap
         * @public
         */
        clear : function() {
            this.keyObj = {};
            this.trueObj = {};
            this.length = 0;
        },
        /**
         * @description 判断HashMap是否为空
         * @return {Boolean} 不为空返回true 否则返回false
         * @public
         */
        isEmpty : function() {
            return this.length == 0;
        },
        /**
         * @description 判断HashMap是否存在某个key
         * @param {String}
         *            key 要判断的键
         * @return {Boolean} HashMap中含有key返回true 否则返回false
         * @public
         */
        containsKey : function(key) {
            return this.keyObj[key] != null;
        },
        /**
         * @description 判断HashMap是否存在某个value
         * @param {Object}
         *            value 要判断的值
         * @return {Boolean} HashMap中含有value返回true 否则返回false
         * @public
         */
        containsValue : function(value) {
            for (var strKey in this.trueObj) {
                if (this.trueObj[strKey] == value)
                    return true;
            }
            return false;
        },
        /**
         * @description 把一个HashMap的值加入到另一个HashMap中，参数必须是HashMap
         * @param {HashMap}
         *            map 要加入的HashMap
         * @public
         */
        putAll : function(map) {
            if (map == null)
                return;
            if (map.constructor != HashMap)
                return;
            var arrKey = map.keySet();
            var arrValue = map.values();
            for (var i in arrKey) {
                this.put(arrKey[i], arrValue[i]);
            }
        },
        /**
         * @description toString()方法
         * @return {String} HashMap的toString字符串值
         * @public
         */
        toString : function() {
            var str = "";
            for (var strKey in this.keyObj) {
                str += strKey + " : " + this.trueObj[strKey] + "\r\n";
            }
            return str;
        }
    });

    /**
     * 格式化器
     * @class 格式化器基类
     * @return
     */
    $.formater = function() {
    };

    $.formater.getObj = function() {
        return new $.formater();
    };

    $.extend($.formater.prototype, {
        /**
         * 格式化
         */
        format : function(value, direction) {
            if (direction == null || direction == true)
                return $.argumentutils.getString(value, "");
            return value;
        },
        /**
         * 校验
         */
        valid : function(presssKey, aggValue) {
            return true;
        }
    });

    /**
     * Boolean格式化器
     * @class Boolean格式化器
     * 将传入值解析，返回true或者false
     */
    $.booleanformater = function(trueArr, falseArr, defaultValue) {
        if (trueArr == null)
            this.trueArr = $.booleanformater.trueArr;
        else
            this.trueArr = trueArr;

        if (falseArr == null)
            this.falseArr = $.booleanformater.falseArr;
        else
            this.falseArr = falseArr;

        this.defaultValue = $.argumentutils.getBoolean(defaultValue, false);
    };

    $.booleanformater.getObj = function(trueArr, falseArr, defaultValue) {
        return new $.booleanformater(trueArr, falseArr, defaultValue);
    };

    $.booleanformater.trueArr = [1, 'Y', 'y', true, 'true'];
    $.booleanformater.falseArr = [0, 'N', 'n', false, 'false'];

    $.extend($.booleanformater.prototype, {
        /**
         * 格式化
         */
        format : function(value, direction) {
            if (direction == null || direction == true) {
                if ($.booleanformater.falseArr.indexOf(value) != -1)
                    return false;
                if ($.booleanformater.trueArr.indexOf(value) != -1)
                    return true;
                return this.defaultValue;
            }
            return value;
        }
    });

    /**
     * 字符格式化器
     * @class 字符格式化器
     * @param maxSize 最大允许长度
     */
    $.stringformater = function(maxSize) {
        if (maxSize == null || parseInt(maxSize) < 0)
            this.maxSize = -1;
        else
            this.maxSize = maxSize;
    };

    $.stringformater.getObj = function(maxSize) {
        return new $.stringformater(maxSize);
    };

    $.extend($.stringformater.prototype, {
        /**
         * 格式化
         */
        format : function(value) {
            if (value == null || value == "")
                return "";
            if (this.maxSize > 0) {
                // 若输入字节长度大于指定长度,则截去超过指定的最大长度的部分
                if (value.lengthb() > this.maxSize)
                    value = value.substrCH(this.maxSize);
            }
            return value;
        },
        /**
         * 校验
         */
        valid : function(key, aggValue, currValue) {
            if (this.maxSize != -1) {
                if (aggValue.lengthb() > this.maxSize)
                    return false;
            }
        }
    });

    /**
     * 浮点数格式化器
     * @class 浮点数格式化器。
     * 判断传入的是否时浮点数，不足精度的自动补全。不是浮点数的返回空值 如果指定了最大值或者最小值则校验大小范围。
     */
    $.dicimalformater = function(precision, minValue, maxValue) {
        this.precision = $.argumentutils.getInteger(precision, 2);
        // 默认精度为2
        this.minValue = minValue;
        this.maxValue = maxValue;
    };

    $.dicimalformater.getObj = function(precision, minValue, maxValue) {
        return new $.dicimalformater(precision, minValue, maxValue);
    };

    $.extend($.dicimalformater.prototype, {
        /**
         * 格式化
         */
        format : function(value) {
            if (value == null || value == "")
                return "";
            if (this.minValue != null && parseFloat(value) < this.minValue)
                return "";
            if (this.maxValue != null && parseFloat(value) > this.maxValue)
                return "";
            value = value + "";

            for (var i = 0; i < value.length; i++) {
                if ("-0123456789.".indexOf(value.charAt(i)) == -1)
                    return "";
            }

            return $.formater.checkDicimalInvalid(value, this.precision);
        },
        /**
         * 校验
         */
        valid : function(key, aggValue, currValue) {
            //检测输入的是否为字符
            if ("0123456789.-".indexOf(key) == -1) {
                return false;
            }
            //非IE浏览器，aggvalue中没有把key拼入
            if (!$.browsersupport.IS_IE) {
                if ( typeof (aggValue) == 'string' && currValue.indexOf(".") != -1) {
                    if (key == ".")  //防止输入多个"."
                        return false;
                }

                if ( typeof (aggValue) == 'string' && currValue.indexOf("-") != -1) {
                    if (key == "-")
                        return false;
                }
                return true;
            }
            //IE浏览器
            return $.formater.checkInputDicimal(aggValue, this.precision);
        }
    });

    /**
     * 浮点型输入框的实时检测
     */
    $.formater.checkInputDicimal = function(value, precision) {
        if (value == null || value == "")
            return false;
        value = value + "";

        //若value是有"."数，则判断其是否为合法浮点数
        var freg = new RegExp(/^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$/);
        if (freg.test(value)) {
            // "."前面的整数位数不能超过15 - precision位
            if (value.indexOf(".") != -1) {
                var intNumber = value.substr(0, value.indexOf("."));
                if (intNumber.length > 15 - precision)
                    return false;
            }

            //"."后面的小数位数不能超过精度
            var num = 0, start = 0;
            if (( start = value.indexOf(".")) != -1) {
                if ((value.substring(start + 1)).length > parseInt(precision))
                    return false;
            }
            return true;
        }

        //若value是没"."整数，则判断其是否为合法整数
        var nreg = new RegExp(/^(-|\+)?\d+$/);
        if (nreg.test(value)) {
            // 没"."整数位数不能超过15 - precision位
            if (value.length > 15 - precision)
                return false;

            return true;
        }

        if (value == "-") {
            return true;
        }

        if (value == "0." || value == "-0.")
            return true;

        return false;
    };
    /**
     * 校验浮点类型。对于浮点型输入框帮助用户完成他的无意失误。
     */
    $.formater.checkDicimalInvalid = function(str, precision) {
        if (str == null || isNaN(str))
   //     if (str == null)
            return "";
    //    if (isNaN(str-'.') || str=="" || str==false)
    //        return "";
        // 浮点数总位数不能超过10位
        if (str.length > 15)
            str = str.substring(0, 15);

        // 默认2位精度
        if (precision == null || !$.argumentutils.isNumberOnly(precision))
            precision = 2;
        else
            precision = parseInt(precision);
        var digit = parseFloat(str);
        var result = (digit * Math.pow(10, precision) / Math.pow(10, precision)).toFixed(precision);
        if (result == "NaN")
            return "";
        return result;
    };

    /**
     * 整数格式化器
     * @class 整数格式化器。判断传入的是否是整数，是否介于设定的上下界直接。不符合返回空值。
     */
    $.integerformater = function(minValue, maxValue) {
        if (!$.argumentutils.isNumber(minValue))
            this.minValue = -9007199254740992;
        else
            this.minValue = parseInt(minValue);

        if (!$.argumentutils.isNumber(maxValue))
            this.maxValue = 9007199254740992;
        else
            this.maxValue = parseInt(maxValue);
    };

    $.integerformater.getObj = function(minValue, maxValue) {
        return new $.integerformater(minValue, maxValue);
    };

    $.extend($.integerformater.prototype, {
        /**
         * 格式化
         */
        format : function(value) {
            if (value == null || value == "")
                return "";
            else if (!$.argumentutils.isNumber(value)) {
                return "";
            }

            if ($.formater.checkIntegerIsValid(value, this.minValue, this.maxValue) == true)
                return value;
            else
                return "";
        },
        /**
         * 整数类型校验
         */
        valid : function(key, aggValue, currValue) {
            var isInvalid = false;
            // 检测输入的是否为数字
            if ("-0123456789".indexOf(key) == -1)
                isInvalid = false;
            else
                isInvalid = true;

            if (isInvalid == true) {
                // 只输入一个负号时认为合法
                if (aggValue == "-")
                    return true;
                else {
                    /*modify by zuopf 2012.10.10  添加不能超过最大值和小于最小值*/
                    if ($.argumentutils.isNumber(aggValue)) {
                        return $.formater.checkIntegerIsValid(aggValue, this.minValue, this.maxValue);
                    }
                    return false;
                }
            } else
                return false;
            return true;
        }
    });

    /**
     * 校验整数类型
     * @param value
     * @param minValue
     * @param maxValue
     * @return
     */
    $.formater.checkIntegerIsValid = function(value, minValue, maxValue) {
        // 确定传入参数的合法性
        if (!$.argumentutils.isNumber(value))
            return false;

        //整数最多能输入15位
        if (value.length > 15)
            return false;

        value = parseInt(value);
        if (!$.argumentutils.isNumber(minValue)) {
            minValue = -9007199254740992;
        }

        if (!$.argumentutils.isNumber(maxValue)) {
            maxValue = 9007199254740992;
        }
        // 整数不能超过范围
        if (value > maxValue || value < minValue)
            return false;
        else
            return true;
    };

    /**
     * 日期格式化器
     * @class 日期格式化器。将传入值解析，若传入正确，则返回正确格式的日期，否则返回空值。
     */
    $.dateformater = function() {
    };

    $.dateformater.getObj = function() {
        return new $.dateformater();
    };

    $.extend($.dateformater.prototype, {
        valid : function(key, aggValue, currValue) {
            return true;
        },

        getMaskerFormatValue : function(value) {
            if ( typeof (value) == "undefined" || value == "" || value == null) {
                return "";
            }
            var type = "DATETEXT";
            if ( typeof (DateTextComp) != "undefined" && typeof (DateTextComp.prototype.componentType) != "undefined") {
                type = DateTextComp.prototype.componentType;
            }
            var maskerType = this.showTimeBar ? "DateTimeText" : type;
            var masker = getMasker(maskerType);
            if (masker != null)
                return masker.format(value).value;
            else
                return value;
        },
        /**,
         * 格式化。传入java中new Date()输出的字符串。
         */
        format : function(value) {
            if ( typeof (value) == "undefined" || value == null || value == "") {
                return "";
            }
            if (value.length > 10) {//使用formatDateTime
                var dateTimeValue = this.formatDateTime(value);
                if (dateTimeValue != "") {
                    return dateTimeValue;
                }
            } else if (value.length >= 8) {//使用formatDate
                var dateTimeValue = this.formatDate(value);
                if (dateTimeValue != "") {
                    return dateTimeValue;
                }
            } else {
                return "";
            }

            var changeValue = value.replace(/-/ig, "/");
            if (!isNaN(Date.parse(changeValue))) {
                var d = new Date(changeValue);
                var nowDate = new Date();
                var y = d.getFullYear();
                // 得到年份

                var m = d.getMonth() + 1;
                // 得到月份
                if (parseInt(m) < 10) {
                    m = "0" + m;
                }

                var d = d.getDate();
                // 得到日期
                if (d < 10) {
                    d = "0" + d;
                }
                hour = nowDate.getHours();
                min = nowDate.getMinutes();
                sec = nowDate.getSeconds();
                return y + "-" + m + "-" + d + " " + hour + ":" + min + ":" + sec;
            } else {
                return "";
            }
        },
        /**
         * 格式化日期：年-月-日 时:分:秒
         */
        formatDateTime : function(value) {
            if ( typeof (value) == "undefined" || value == null || value == "")
                return "";
            var reg = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/;
            var r = value.match(reg);
            if (r == null)
                return "";
            var d = new Date(r[1], r[3] - 1, r[4], r[5], r[6], r[7]);
            if (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4] && d.getHours() == r[5] && d.getMinutes() == r[6] && d.getSeconds() == r[7])
                return r[1] + "-" + r[3] + "-" + r[4] + " " + r[5] + ":" + r[6] + ":" + r[7];
            else
                return "";
        },
        /**
         * 格式化日期：年-月
         * @param {} value
         * @return {String}
         */
        formatYearAndMonth : function(value) {
            var strDate = value;
            if ( typeof (strDate) == "undefined" || strDate == null || strDate == "")
                return "";
            if (strDate.length > 7) {
                strDate = strDate.substring(0, 7);
            }
            var reg = /^(\d{1,4})(-|\/)(([0]{0,1}[1-9]{1})|([1]{1}[0-2]{1}))$/;
            var r = strDate.match(reg);

            if (r == null) {
                return "";
            } else if (r == false) {
                return "";
            }

            var year = parseInt(r[1], 10);
            var month = parseInt(r[3], 10);
            var date = 1;
            var dateObj = new Date(year, month - 1, date);
            if (dateObj.getFullYear() == year && (dateObj.getMonth() + 1) == month && dateObj.getDate() == date) {
                var m = dateObj.getMonth() + 1;
                // 得到月份
                if (m < 10) {
                    m = "0" + m;
                }
                value = dateObj.getFullYear() + "-" + m;
            } else {
                value = "";
            }
            return value;
        },
        /**
         * 格式化日期：年-月-日
         * @param value
         * @return
         */
        formatDate : function(value) {
            var strDate = value;
            if ( typeof (strDate) == "undefined" || strDate == null || strDate == "")
                return "";
            if (strDate.length > 10) {
                strDate = strDate.substring(0, 10);
            }
            var reg = /^(\d{1,4})(-|\/)(([0]{0,1}[1-9]{1})|([1]{1}[0-2]{1}))(-|\/)(([0]{0,1}[1-9]{1})|([1-2]{1}[0-9]{1})|([3]{1}[0-1]{1}))$/;
            ///^(\d{1,4})(-|\/)(\d{1,2})(-|\/)(\d{1,2})$/;
            var r = strDate.match(reg);

            if (r == null) {
                return "";
            } else if (r == false) {
                return "";
            }

            var year = parseInt(r[1], 10);
            var month = parseInt(r[3], 10);
            var date = parseInt(r[7], 10);
            var dateObj = new Date(year, month - 1, date);
            if (dateObj.getFullYear() == year && (dateObj.getMonth() + 1) == month && dateObj.getDate() == date) {
                var m = dateObj.getMonth() + 1;
                // 得到月份
                if (m < 10) {
                    m = "0" + m;
                }

                var d = dateObj.getDate();
                // 得到日期
                if (d < 10) {
                    d = "0" + d;
                }

                value = dateObj.getFullYear() + "-" + m + "-" + d;
            } else {
                value = "";
            }
            return value;
        },
        /**
         * 格式化Date对象为字符串
         * @param {} date
         */
        formatDateToString : function(date) {
            if ( typeof (date) != "object" || !( date instanceof Date)) {
                date = new Date();
            }
            var year = date.getFullYear();
            var month = date.getMonth() + 1;
            if (month < 10) {
                month = new String("0" + month);
            }
            var day = date.getDate();
            if (day < 10) {
                day = new String("0" + day);
            }
            var hour = date.getHours();
            if (hour < 10) {
                hour = new String("0" + hour);
            }
            var min = date.getMinutes();
            if (min < 10) {
                min = new String("0" + min);
            }
            var sec = date.getSeconds();
            if (sec < 10) {
                sec = new String("0" + sec);
            }
            return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
        },
        /**
         * 格式化输入值为日期字符串
         * @param {} inputValue
         */
        formatInputValueToDateString : function(inputValue) {
            var value = this.formatDateTime(inputValue);
            if (value != "") {
                return value;
            }
            value = this.formatDate(inputValue);
            if (value != "") {
                var date = new Date();
                var hour = date.getHours();
                if (hour < 10) {
                    hour = new String("0" + hour);
                }
                var min = date.getMinutes();
                if (min < 10) {
                    min = new String("0" + min);
                }
                var sec = date.getSeconds();
                if (sec < 10) {
                    sec = new String("0" + sec);
                }
                return value + " " + hour + ":" + min + ":" + sec;
            }
            return value;
        }
    });

    /**
     * 显示错误提示浮动条
     */
    $.formater.showVerifyMessage = function(obj, msg) {
    	return;
        if (window.sys_verifyMessageDiv == null) {
            var div = $("<DIV>").css({
                'background' : "#D8E3E8",
                'z-index' : $.measures.getZIndex(),
                'position' : "absolute"
            }).html("").hide();
            window.sys_verifyMessageDiv = div;
            //TODO:先注释，以后处理错误条显示方式
            //$("body").append(window.sys_verifyMessageDiv);
        }

        window.sys_verifyCount = 0;
        var top = obj.offset().top + obj.outerHeight();
        var left = obj.offset().left;

        window.sys_verifyMessageDiv.html(msg).css({
            'top' : top + 4 + "px",
            'left' : left + "px"
        }).show();
        setTimeout("$.formater.startVerifyMessageFloating()", 100);
    };

    $.formater.startVerifyMessageFloating = function() {
        if (window.sys_verifyCount < 10) {
            window.sys_verifyMessageDiv.css('top', window.sys_verifyMessageDiv.offset().top - 4 + "px");
            window.sys_verifyCount++;
            setTimeout("$.formater.startVerifyMessageFloating()", 100);
        } else {
            window.sys_verifyMessageDiv.hide();
        }
    };

    /**
     * ajax请求封装
     *
     * @param path 请求url，不能为空
     * @param queryStr 请求参数,如 a=1&b=2&c=3
     * @param returnFunc 回调函数。ajax请求完毕后会调用此函数
     * @param returnArgs 回调函数传入的参数
     * @param method 请求方式，"GET" or "POST",默认POST
     * @param asyn 是否异步，默认异步
     * @param format 是否是格式化数据，如果是，返回xml，否则返回text.默认为text
     * @class
     */
    $.ajaxutils = function(opts) {
        opts = opts || {};
        this.path = opts.path;
        this.queryStr = opts.queryStr;
        this.format = opts.format || "json";
        this.params = opts.params || {};
    };

    $.ajaxutils.getObj = function(opts) {
        return new $.ajaxutils(opts);
    };

    $.extend($.ajaxutils.prototype, {
        setPath : function(path) {
            this.path = path;
        },
        setQueryStr : function(qryStr) {
            this.queryStr = qryStr;
        },
        addParam : function(key, value) {
            this.params[key] = value;
        },
        setFormat : function(format) {
            this.format = format;
        },
        clone : function() {
            var ajax = $.ajaxutils.getObj();
            ajax.path = this.path;
            ajax.queryStr = this.queryStr;
            ajax.format = this.format;
            ajax.params = this.params;
            return ajax;
        },
        get : function(asyn) {
            var oThis = this;
            if ( typeof ($.pageutils.getStickString) != "undefined") {
                var stickStr = $.pageutils.getStickString();
                if (stickStr != null && stickStr != "")
                    this.queryStr = $.argumentutils.mergeParameter(this.queryStr, stickStr);
            }

            // 是否异步，默认异步
            asyn = $.argumentutils.getBoolean(asyn, true);
            if (this.queryStr == null) {
                this.queryStr = "";
            }

            try {
                // 标明是ajax请求,应为ajax请求传递的中文参数在后台不需要转化编码会正确获得
                var urlParam = this.path + (this.path.indexOf("?") == -1 ? "?" : "") + this.queryStr + "&isAjax=1";
                if (this.queryStr != "" && this.queryStr[this.queryStr.length - 1] != "&")
                    urlParam += "&";
                else
                    urlParam += "&";
                $.each(this.params, function(key, val) {
                    urlParam += key + "=" + encodeURIComponent(val);
                    urlParam += "&";
                });

                // 由于get方法对同一个网址不会做第二次返回，所以为了请求正常进行，在网址后加随机参数
                urlParam += "tmprandid=" + Math.random();

                var ajaxDeferred = $.ajax({
                    asyn : asyn,
                    type : "GET",
                    url : urlParam,
                    dataType : this.format
                });

                // 同步，直接返回结果
                if (!asyn) {
                    var result = "";
                    ajaxDeferred.done(function(data) {
                        result = data;
                    });
                    return result;
                }
                ajaxDeferred.always(function(xhr, ts) {
                    oThis.destroySelf();
                    oThis = null;
                });
                ajaxDeferred.ref = this;
                return ajaxDeferred;
            } catch(e) {
                if ($.browsersupport.IS_IE)
                    $.pageutils.showErrorDialog("Ajax request error:" + e.name + " " + e.message);
                else
                    $.pageutils.showErrorDialog("Ajax request error:" + e);
            }
        },
        post : function(asyn) {
            var oThis = this;
            if ( typeof ($.pageutils.getStickString) != "undefined") {
                var stickStr = $.pageutils.getStickString();
                if (stickStr != null && stickStr != "")
                    this.queryStr = $.argumentutils.mergeParameter(this.queryStr, stickStr);
            }
            // 是否异步，默认异步
            asyn = $.argumentutils.getBoolean(asyn, true);
            if (this.queryStr == null) {
                this.queryStr = "";
            }
            var paramStr = "";
            $.each(this.params, function(key, val) {
                paramStr += key + "=" + encodeURIComponent(val);
                paramStr += "&";
            });

            var qryStr = this.queryStr;
            if (paramStr != "")
                qryStr += "&" + paramStr;
            var bodyStr = qryStr + "&isAjax=1";

            var ajaxOpts = {
                async : asyn,
                type : "POST",
                url : this.path,
                data : bodyStr,
                dataType : this.format
            };
            // 同步，直接返回结果
            if (!asyn) {
                var result = "";
                $.ajax($.extend({}, ajaxOpts, {
                    dataType : this.format,
                    success : function(data) {
                        result = data;
                    }
                }));
                return {
                    result : result,
                    ref : this
                };
            } else {
                var ajaxDeferred = $.ajax(ajaxOpts);
                ajaxDeferred.ref = this;
                return ajaxDeferred;
            }
        },
        destroySelf : function() {
        }
    });
    
    
    /**
     * masker类及方法 
     */
    $.abstractmasker = function() {
    };
    $.abstractmasker.getObj = function() {
        return new $.abstractmasker();
    };
    $.extend($.abstractmasker.prototype, {
        format : function(obj) {
            if (obj == null)
                return null;

            var fObj = this.formatArgument(obj);
            return this.innerFormat(fObj);
        },
        /**
         * 统一被格式化对象结构
         * 
         * @param obj
         * @return
         */
        formatArgument : function(obj) {
        },
        /**
         * 格式化
         * 
         * @param obj
         * @return
         */
        innerFormat : function(obj) {
        }
    });

    /**
     * 拆分算法格式化虚基类
     */
    $.abstractsplitmasker = function() {
    };
    $.abstractsplitmasker.getObj = function() {
        return new $.abstractsplitmasker();
    };
    $.extend($.abstractsplitmasker.prototype, $.abstractmasker.getObj(), {
        elements : [],
        format : function(obj) {
            if (obj == null)
                return null;

            var fObj = this.formatArgument(obj);
            return this.innerFormat(fObj);
        },
        /**
         * 统一被格式化对象结构
         * 
         * @param obj
         * @return
         */
        formatArgument : function(obj) {
            return obj;
        },
        /**
         * 格式化
         * 
         * @param obj
         * @return
         */
        innerFormat : function(obj) {
            if (obj == null || obj == "")
                return $.formatresult.getObj(obj);
            this.doSplit();
            var result = "";
            // dingrf 去掉concat合并数组的方式，换用多维数组来实现 提高效率
            result = this.getElementsValue(this.elements, obj);
            // for(var i = 0; i < this.elements.length ; i++){
            // if(i != undefined){
            // var element = this.elements[i];
            // var elementValue = element.getValue(obj);
            // if(elementValue != undefined)
            // result = result + elementValue;
            // }
            // }
            return $.formatresult.getObj(result);
        },
        /**
         * 合并多维数组中的elementValue
         * 
         * @param {}
         *            element
         * @param {}
         *            obj
         * @return {}
         */
        getElementsValue : function(element, obj) {
            var result = "";
            if (element instanceof Array) {
                for (var i = 0; i < element.length; i++) {
                    result = result + this.getElementsValue(element[i], obj);
                }
            } else {
                if (element.getValue)
                    result = element.getValue(obj);
            }
            return result;
        },

        getExpress : function() {
        },

        doSplit : function() {
            var express = this.getExpress();
            if (this.elements == null || this.elements.length == 0)
                this.elements = this.doQuotation(express, this.getSeperators(),
                        this.getReplaceds(), 0);
        },
        /**
         * 处理引号
         * 
         * @param express
         * @param seperators
         * @param replaced
         * @param curSeperator
         * @param obj
         * @param result
         */
        doQuotation : function(express, seperators, replaced, curSeperator) {
            if (express.length == 0)
                return null;
            var elements = new Array();
            var pattern = new RegExp('".*?"', "g");
            var fromIndex = 0;
            var result;
            do {
                result = pattern.exec(express);
                if (result != null) {
                    var i = result.index;
                    var j = pattern.lastIndex;
                    if (i != j) {
                        if (fromIndex < i) {
                            var childElements = this.doSeperator(express
                                    .substring(fromIndex, i), seperators,
                                    replaced, curSeperator);
                            if (childElements != null
                                    && childElements.length > 0) {
                                // elements = elements.concat(childElements);
                                elements.push(childElements);
                            }
                        }
                    }
                    elements.push($.stringelement.getObj(express.substring(i + 1,
                            j - 1)));
                    fromIndex = j;
                }
            } while (result != null);

            if (fromIndex < express.length) {
                var childElements = this.doSeperator(express.substring(
                        fromIndex, express.length), seperators, replaced,
                        curSeperator);
                if (childElements != null && childElements.length > 0)
                    // elements = elements.concat(childElements);
                    elements.push(childElements);
            }
            return elements;
        },
        /**
         * 处理其它分隔符
         * 
         * @param express
         * @param seperators
         * @param replaced
         * @param curSeperator
         * @param obj
         * @param result
         */
        doSeperator : function(express, seperators, replaced, curSeperator) {
            if (curSeperator >= seperators.length) {
                var elements = new Array;
                elements.push(this.getVarElement(express));
                return elements;
            }

            if (express.length == 0)
                return null;
            var fromIndex = 0;
            var elements = new Array();
            var pattern = new RegExp(seperators[curSeperator], "g");
            var result;
            do {
                result = pattern.exec(express);
                if (result != null) {
                    var i = result.index;
                    var j = pattern.lastIndex;
                    if (i != j) {
                        if (fromIndex < i) {
                            var childElements = this.doSeperator(express
                                    .substring(fromIndex, i), seperators,
                                    replaced, curSeperator + 1);
                            if (childElements != null
                                    && childElements.length > 0)
                                // elements = elements.concat(childElements);
                                elements.push(childElements);
                        }

                        if (replaced[curSeperator] != null) {
                            elements.push($.stringelement.getObj(
                                    replaced[curSeperator]));
                        } else {
                            elements.push($.stringelement.getObj(express.substring(
                                    i, j)));
                        }
                        fromIndex = j;
                    }
                }
            } while (result != null);

            if (fromIndex < express.length) {
                var childElements = this.doSeperator(express.substring(
                        fromIndex, express.length), seperators, replaced,
                        curSeperator + 1);
                if (childElements != null && childElements.length > 0)
                    // elements = elements.concat(childElements);
                    elements.push(childElements);
            }
            return elements;
        }
    });

    /**
     * 
     * @type {AbstractSplitMasker}
     */
    $.addressmasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.addressmasker.getObj = function(formatMeta) {
        return new $.addressmasker(formatMeta);
    };
    $.extend($.addressmasker.prototype, $.abstractsplitmasker.getObj(), {
        getExpress : function() {
            return this.formatMeta.express;
        },
        getReplaceds : function() {
            return [ this.formatMeta.separator ];
        },
        getSeperators : function() {
            return [ "(\\s)+?" ];
        },
        getVarElement : function(express) {
            var ex = {};

            if (express == ("C"))
                ex.getValue = function(obj) {
                    return obj.country;
                };

            if (express == ("S"))
                ex.getValue = function(obj) {
                    return obj.state;
                };

            if (express == ("T"))
                ex.getValue = function(obj) {
                    return obj.city;
                };

            if (express == ("D"))
                ex.getValue = function(obj) {
                    return obj.section;
                };

            if (express == ("R"))
                ex.getValue = function(obj) {
                    return obj.road;
                };

            if (express == ("P"))
                ex.getValue = function(obj) {
                    return obj.postcode;
                };

            if (typeof (ex.getValue) == undefined)
                return $.stringelement.getObj(express);
            else
                return ex;
        },
        formatArgument : function(obj) {
            return obj;
        }
    });

    /**
     * 数字格式化 格式化数字 Create at 2009-3-20 上午08:50:32
     */
    /**
     * 构造方法
     */
    $.numbermasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.numbermasker.getObj = function(formatMeta) {
        return new $.numbermasker(formatMeta);
    };
    $.extend($.numbermasker.prototype, $.abstractmasker.getObj(), {
        formatMeta : null,
        /**
         * 格式化对象
         */
        innerFormat : function(obj) {
            var dValue, express, seperatorIndex, strValue;
            dValue = obj.value;
            if (dValue > 0) {
                express = this.formatMeta.positiveFormat;
                strValue = dValue + '';
            } else if (dValue < 0) {
                express = this.formatMeta.negativeFormat;
                strValue = (dValue + '').substr(1, (dValue + '').length - 1);
            } else {
                express = this.formatMeta.positiveFormat;
                strValue = dValue + '';
            }
            seperatorIndex = strValue.indexOf('.');
            strValue = this.setTheSeperator(strValue, seperatorIndex);
            strValue = this.setTheMark(strValue, seperatorIndex);
            var color = null;
            if (dValue < 0 && this.formatMeta.isNegRed) {
                color = "FF0000";
            }
            return $.formatresult.getObj(express.replaceAll('n', strValue), color);

        },
        /**
         * 设置标记
         */
        setTheMark : function(str, seperatorIndex) {
            var endIndex, first, index;
            if (!this.formatMeta.isMarkEnable)
                return str;
            if (seperatorIndex <= 0)
                seperatorIndex = str.length;
            first = str.charCodeAt(0);
            endIndex = 0;
            if (first == 45)
                endIndex = 1;
            index = seperatorIndex - 3;
            while (index > endIndex) {
                str = str.substr(0, index - 0) + this.formatMeta.markSymbol
                        + str.substr(index, str.length - index);
                index = index - 3;
            }
            return str;
        },

        setTheSeperator : function(str, seperatorIndex) {
            var ca;
            if (seperatorIndex > 0) {
                ca = $.numbermasker.toCharArray(str);
                ca[seperatorIndex] = $.numbermasker
                        .toCharArray(this.formatMeta.pointSymbol)[0];
                str = ca.join('');
            }
            return str;
        },
        /**
         * 默认构造方法
         */
        formatArgument : function(obj) {
            var numberObj = {};
            numberObj.value = obj;
            return numberObj;
        }
    });
    /**
     * 将字符串转换成char数组
     * 
     * @param {}
     *            str
     * @return {}
     * 
     * 静态方法
     */
    $.numbermasker.toCharArray = function(str) {
        var str = str.split("");
        var charArray = new Array();
        for (var i = 0; i < str.length; i++) {
            charArray.push(str[i]);
        }
        return charArray;
    };

    /**
     * 货币格式
     */
    $.currencymasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.currencymasker.getObj = function(formatMeta) {
        return new $.currencymasker(formatMeta);
    };
    $.extend($.currencymasker.prototype, $.numbermasker.getObj(), {
        formatMeta : null,
        /**
         * 重载格式方法
         * 
         * @param {}
         *            obj
         * @return {}
         */
        innerFormat : function(obj) {
            var fo = (new NumberFormat(this.formatMeta)).innerFormat(obj);
            fo.value = fo.value.replace("$", this.formatMeta.curSymbol);
            return fo;
        }
    });

    $.datetimemasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.datetimemasker.getObj = function(formatMeta) {
        return new $.datetimemasker(formatMeta);
    };
    $.extend($.datetimemasker.prototype, $.abstractsplitmasker.getObj(), {
        formatMeta : null,
        doOne : function(express) {
            if (express.length == 0)
                return "";
            var obj = new Object;
            if (express == "yyyy") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getyyyy(o);
                    ;
                };
            }
            if (express == "yy") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getyy(o);
                };
            }
            if (express == "MMMM") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getMMMM(o);
                };
            }

            if (express == "MMM") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getMMM(o);
                };
            }

            if (express == "MM") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getMM(o);
                };
            }

            if (express == "M") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getM(o);
                };
            }

            if (express == "dd") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getdd(o);
                };
            }

            if (express == "d") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getd(o);
                };
            }

            if (express == "hh") {
                obj.getValue = function(o) {
                    return $.datetimemasker.gethh(o);
                };
            }

            if (express == "h") {
                obj.getValue = function(o) {
                    return $.datetimemasker.geth(o);
                };
            }

            if (express == "mm") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getmm(o);
                };
            }

            if (express == "m") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getm(o);
                };
            }

            if (express == "ss") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getss(o);
                };
            }

            if (express == "s") {
                obj.getValue = function(o) {
                    return $.datetimemasker.gets(o);
                };
            }

            if (express == "HH") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getHH(o);
                };
            }

            if (express == "H") {
                obj.getValue = function(o) {
                    return $.datetimemasker.getH(o);
                };
            }
            if (express == "t") {
                obj.getValue = function(o) {
                    return $.datetimemasker.gett(o);
                };
            }
            if (typeof (obj.getValue) == undefined)
                return express;
            else
                return obj;
        },

        getExpress : function() {
            return this.formatMeta.format;
        },

        getReplaceds : function() {
            return [ " ", this.formatMeta.speratorSymbol, ":" ];
        },

        getSeperators : function() {
            return [ "(\\s)+?", "-", ":" ];
        },

        getVarElement : function(express) {
            return this.doOne(express);
        },

        formatArgument : function(obj) {
            if (obj == 0)
                return "";
            if (obj == null || obj == "")
                return obj;
            if ((typeof obj) == "string") {
                var dateArr = obj.split(" ");
                if (dateArr.length > 0) {
                    var arr0 = dateArr[0].split("-");
                    var date = new Date();
                    // 先把日期设置为1日，解决bug:当前日期为2011-08-31时，选择日期为2011-09-X，会把日期格式化为2011-10-X
                    date.setDate(1);
                    date.setFullYear(parseInt(arr0[0], 10));
                    date.setMonth(parseInt(arr0[1], 10) - 1);
                    date.setDate(parseInt(arr0[2], 10));
                    if (dateArr.length == 2 && dateArr[1] != undefined) {
                        var arr1 = dateArr[1].split(":");
                        date.setHours(parseInt(arr1[0], 10));
                        date.setMinutes(parseInt(arr1[1], 10));
                        date.setSeconds(parseInt(arr1[2], 10));
                        if (arr1.length > 3)
                            date.setMilliseconds(parseInt(arr1[3], 10));
                    }
                }
                return date;
            }
            return (obj);
        }
    });
    /**
     * 英文短日期
     */
    $.datetimemasker.enShortMonth = [ "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" ];
    /**
     * 英文长日期
     */
    $.datetimemasker.enLongMonth = [ "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October",
            "November", "December" ];
    $.datetimemasker.getyyyy = function(date) {
        return date.getFullYear();
    };
    $.datetimemasker.getyy = function(date) {
        return ("" + date.getFullYear()).substring(2);
    };
    $.datetimemasker.getM = function(date) {
        return "" + (date.getMonth() + 1);
    };
    $.datetimemasker.getMM = function(date) {
        var month = date.getMonth() + 1;
        if (month < 10)
            return "0" + month;
        return month;
    };
    $.datetimemasker.getMMM = function(date) {
        return this.enShortMonth[date.getMonth()];
    };
    $.datetimemasker.getMMMM = function(date) {
        return this.enLongMonth[date.getMonth()];
    };
    $.datetimemasker.getdd = function(date) {
        var day = date.getDate();
        if (day < 10)
            return "0" + day;
        return date.getDate() + "";
    };
    $.datetimemasker.getd = function(date) {
        return date.getDate() + "";
    };
    $.datetimemasker.gethh = function(date) {
        var hh = date.getHours();
        if (hh < 10)
            return "0" + hh;

        return (date.getHours()) + "";
    };
    $.datetimemasker.geth = function(date) {
        return (date.getHours()) + "";
    };
    $.datetimemasker.getHH = function(date) {
        var HH = date.getHours();

        if (HH >= 12)
            HH = HH - 12;

        if (HH < 10)
            return "0" + HH;
        return (HH) + "";
    };
    $.datetimemasker.getH = function(date) {
        var HH = date.getHours();

        if (HH >= 12)
            HH = HH - 12;

        return (HH) + "";
    };
    $.datetimemasker.getmm = function(date) {
        var mm = date.getMinutes();
        if (mm < 10)
            return "0" + mm;
        return (date.getMinutes()) + "";
    };
    $.datetimemasker.getm = function(date) {
        return "" + (date.getMinutes());
    };
    $.datetimemasker.getss = function(date) {
        var ss = date.getSeconds();
        if (ss < 10)
            return "0" + ss;

        return (ss) + "";
    };
    $.datetimemasker.gets = function(date) {
        return (date.getSeconds()) + "";
    };
    $.datetimemasker.gett = function(date) {
        var hh = date.getHours();
        if (hh <= 12)
            return "AM";
        else
            return "PM";
    };

    /**
     * 日期格式化
     */
    $.datemasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.datemasker.getObj = function(formatMeta) {
        return new $.datemasker(formatMeta);
    };
    $.extend($.datemasker.prototype, $.datetimemasker.getObj());

    /**
     * 时间格式化
     */
    $.timemasker = function(formatMeta) {
        this.formatMeta = formatMeta;
    };
    $.timemasker.getObj = function() {
        return new $.timemasker();
    };
    $.extend($.timemasker.prototype, $.datetimemasker.getObj());
    
    /**
     *  MaskerUtil.js
     */
    /**
     * 格式解析后形成的单个格式单元 适用于基于拆分算法的AbstractSplitFormat，表示拆分后的变量单元
     */
    $.stringelement = function(value){
        this.value = value;
    };
    $.stringelement.getObj = function(value){
        return new $.stringelement(value);
    };
    $.extend($.stringelement.prototype,{
        value : "",
        getValue : function(obj) {
            return this.value;
        }
    });
    
    /**
     * 格式结果
     */
    $.formatresult = function(value, color){
        this.value = value;
        this.color = color;
    };
    $.formatresult.getObj = function(value, color){
        return new $.formatresult(value, color);
    };
    
    $.maskerutil = {
            /**
             * 将结果输出成HTML代码
             * 
             * @param {}
             *            result
             * @return {String}
             */
            toColorfulString : function(result) {
                var color;
                if (!result) {
                    return '';
                }
                if (result.color == null) {
                    return result.value;
                }
                color = result.color;
                return '<font color="' + color + '">' + result.value + '<\/font>';
            }
        };

    /**
     *  MaskerMeta.js
     */
    /**
     * @fileoverview 数字格式化信息
     * @author licza
     * @version NC6.0
     * 
     */
    $.numberformatmeta = function(){};
    
    $.numberformatmeta.getObj = function() {
        return new $.numberformatmeta();
    };
    
    $.extend($.numberformatmeta.prototype, {
        metaType : "NumberFormatMeta",
        /**
        *是否负数红字
        */
        isNegRed : true,
        isMarkEnable : true,
        markSymbol : ",",
        pointSymbol : ".",
        positiveFormat : "n",
        negativeFormat : "-n"       
    });
    
    /**
     * 货币格式信息
     * @author licza
     * @version NC6.0
     * 
     */
    /**
     * 构造方法
     */
    $.currencyformatmeta = function(){
        this.positiveFormat = "$n"; 
        this.negativeFormat = "$-n";
    };
    $.currencyformatmeta.getObj = function() {
        return new $.currencyformatmeta();
    };
    $.extend($.currencyformatmeta.prototype, {
        metaType : "CurrencyFormatMeta",
        curSymbol : ""      
    });
    
    /**
     * 时间格式化信息
     */
    $.datetimeformatmeta = function(){};
    $.datetimeformatmeta.getObj = function(){
        return new $.datetimeformatmeta();
    };
    $.extend($.datetimeformatmeta.prototype, {
        metaType : "DateTimeFormatMeta",
        format : "yyyy-MM-dd hh:mm:ss",
        speratorSymbol : "-"
        
    });
    
    /**
     * 日期格式化信息
     */
    $.dateformatmeta = function (){
        this.format = "yyyy-MM-dd";
    };
    $.dateformatmeta.getObj = function(){
        return new $.dateformatmeta();
    };
//  $.extend($.dateformatmeta.prototype, {
//      
//  });
    
    $.timeformatmeta = function(){
        this.format = "hh:mm:ss";
    };
    $.timeformatmeta.getObj = function(){
        return new $.timeformatmeta();
    };
    
    /**
     * 地址格式化信息
     */
    $.addressformatmeta = function(){
        this.express = "C S T R P";
        this.separator = " ";
    };
    $.addressformatmeta.getObj = function(){
        return new $.addressformatmeta();
    };
})(jQuery);
