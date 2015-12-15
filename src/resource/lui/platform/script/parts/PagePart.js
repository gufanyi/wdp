/**
 * 连接器对象
 * @class 连接器对象
 * @param id
 * @param source
 * @param target
 * @param signal
 * @param slot
 * @return
 */
function Connector(id, source, target, signal, slot) {
    this.id = id;
    this.source = source;
    this.target = target;
    this.signal = signal;
    this.slot = slot;
};

(function($) {
    /* EventContext常量 */
    $.EventContextConstant = {
        eventcontext : "e",
        parentcontext : "pe",
        params : "ps",
        groupparams : "gps",
        param : "p",
        key : "k",
        value : "v",
        context : "c",
        attributes : "as",
        attribute : "a",
        res_parameters : "dsps",
        req_parameters : "dqps",
        parameter : "dp",
        NULL : "。",
        records : "rs",
        record : "r",
        erecord : "er",
        drecord : "dr"
    };

    /**
     * ******************************************pagePart(pageUI)***********************************
     */
    /**
     * 页面对象构造函数
     * @class 页面对象
     */
    $.pagepart = function(title) {
        this.element = $(this);
        this.title = title;
        this.widgetMap = $.hashmap.getObj();
        this.dialogMap = $.hashmap.getObj();
        // 以source为key进行存储
        this.connMap = $.hashmap.getObj();
        this.attributeMap = $.hashmap.getObj();
        this.cardMap = $.hashmap.getObj();
        this.tabMap = $.hashmap.getObj();
        this.panelMap = $.hashmap.getObj();
        this.outlookMap = $.hashmap.getObj();
        // 页面是否发生变化
        this.hasChanged = false;
        this.exParams = $.hashmap.getObj();
    };

    $.pagepart.getObj = function(title) {
        return new $.pagepart(title);
    };

    $.extend($.pagepart.prototype, {
        getContext : function(submitRule) {
            var pageUICtxJson = {};
            pageUICtxJson["context"] = {
                "c" : "PageUIContext",
                "hasChanged" : this.hasChanged
            };

            if (submitRule) {
                var widgets = this.widgetMap.values();
                if (widgets && widgets.length > 0) {
                    var widgetCtxJsons = new Array();
                    for (var i = 0; i < widgets.length; i++) {
                        var innerWidget = widgets[i];
                        if (innerWidget.id == null)
                            continue;
                        var widgetRule = submitRule.getViewPartRule(innerWidget.id);
                        var innerCtx = innerWidget.getContext(widgetRule);
                        innerCtx.id = innerWidget.id;
                        widgetCtxJsons.push(innerCtx);
                    }
                    pageUICtxJson["views"] = widgetCtxJsons;
                }
                if (submitRule.getTabSubmit()) {
                    var tabObjs = this.tabMap.values();
                    if (tabObjs != null && tabObjs.length > 0) {
                        var tabCtxJsons = new Array();
                        for (var i = 0; i < tabObjs.length; i++) {
                            var innerCtx = tabObjs[i].getContext();
                            innerCtx.id = tabObjs[i].id;
                            tabCtxJsons.push(innerCtx);
                        }
                        pageUICtxJson["tabs"] = tabCtxJsons;
                    }
                }
                if (submitRule.getCardSubmit()) {
                    var cardObjs = this.cardMap.values();
                    if (cardObjs != null && cardObjs.length > 0) {
                        var cardCtxJsons = new Array();
                        for (var i = 0; i < cardObjs.length; i++) {
                            var innerCtx = cardObjs[i].getContext();
                            innerCtx.id = cardObjs[i].id;
                            cardCtxJsons.push(innerCtx);
                        }
                        pageUICtxJson["cards"] = cardCtxJsons;
                    }
                }
                if (submitRule.getPanelSubmit()) {
                    var panelObjs = this.panelMap.values();
                    if (panelObjs != null && panelObjs.length > 0) {
                        var panelCtxJsons = new Array();
                        for (var i = 0; i < panelObjs.length; i++) {
                            var innerCtx = panelObjs[i].getContext();
                            innerCtx.id = panelObjs.id;
                            panelCtxJsons.push(innerCtx);
                        }
                        pageUICtxJson["panels"] = panelCtxJsons;
                    }
                }
            }

            return pageUICtxJson;
        },
        setContext : function(context) {
            var hasChanged = context.hasChanged;
            if (hasChanged != this.hasChanged) {
                this.setChanged(hasChanged);
            }
        },
        /**
         * 增加ViewPart
         * @private
         */
        addViewPart : function(widget) {
            this.widgetMap.put(widget.id, widget);
        },
        removeViewPart : function(id) {
            var widget = this.widgetMap.remove(id);
            if (widget != null) {
                widget.destroySelf();
                widget = null;
            }
        },
        /**
         * 获取ViewPart
         */
        getViewPart : function(widgetId) {
            return this.widgetMap.get(widgetId);
        },
        /**
         * 获取所有Widget
         * @return Widget数组
         */
        getViewParts : function() {
            return this.widgetMap.values();
        },
        /**
         * private
         * @param id
         * @param dialog
         */
        addDialog : function(id, dialog) {
            this.dialogMap.put(id, dialog);
        },
        removeDialog : function(id) {
            var dialog = this.dialogMap.remove(id);
            if (dialog != null) {
                //TODO
                //		widget.destroy();
            }
        },
        /**
         * 获取对话框
         */
        getDialog : function(id) {
            return this.dialogMap.get(id);
        },
        /**
         * 获取所有对话框
         * @return 对话框数组
         */
        getDialogs : function() {
            return this.dialogMap.values();
        },
        /**
         * 增加属性
         * @private
         */
        addAttribute : function(name, value) {
            this.attributeMap.put(name, value);
        },
        /**
         * 获取属性
         */
        getAttribute : function(name) {
            return this.attributeMap.get(name);
        },
        /**
         * 增加连接对象
         * @private
         */
        addConnector : function(conn) {
            var arr = this.connMap.get(conn.source);
            if (arr == null) {
                arr = new Array;
                this.connMap.put(conn.source, arr);
            }
            arr.push(conn);
        },
        /**
         * 获取连接对象
         */
        getConnectors : function(source, signal) {
            var arr = this.connMap.get(source);
            if (arr == null)
                return null;
            var connArr = new Array;
            for (var i = 0; i < arr.length; i++) {
                if (arr[i].signal == signal)
                    connArr.push(arr[i]);
            }
            return connArr;
        },
        /**
         * 页面初始化前执行方法
         * @private
         */
        $beforePageInit : function() {
            this.element.triggerHandler('beforepageinit');
        },
        /**
         * 页面初始话后执行方法
         * @private
         */
        $afterPageInit : function() {
            var widgets = this.getViewParts();
            if (widgets != null && widgets.length > 0) {
                for (var i = 0; i < widgets.length; i++) {
                    widgets[i].onBeforeShow();
                }
            }
            this.element.triggerHandler('afterpageinit');
        },
        /**
         * 初始化数据前执行方法
         * @private
         */
        $beforeInitData : function() {
            var wds = this.getViewParts();
            for (var i = 0; i < wds.length; i++) {
                wds[i].$beforeInitData();
            }
        },
        destroySelf : function() {
            var widgets = pageUI.getViewParts();
            for (var i = 0, n = widgets.length; i < n; i++) {
                widgets[i].destroySelf();
            }
            this.widgetMap.clear();
            this.widgetMap = null;
            this.dialogMap.clear();
            this.dialogMap = null;
            // 以source为key进行存储
            this.connMap.clear();
            this.connMap = null;
            this.attributeMap.clear();
            this.attributeMap = null;
            this.cardMap.clear();
            this.cardMap = null;
            this.tabMap.clear();
            this.tabMap = null;
            this.panelMap.clear();
            this.panelMap = null;
            this.exParams.clear();
            this.exParams = null;
            for (var i in this) {
                this[i] = null;
            }
        },
        /**
         * 更新已加载的Widget
         * @private
         */
        updateInitedWidgets : function() {
            this.$beforeInitData();
        },
        /**
         * @private
         */
        $externalInit : function() {
            this.element.triggerHandler('externalinit');
        },
        /**
         * 激活前事件
         * @private
         */
        $beforeActive : function() {
            this.element.triggerHandler('beforeactive');
        },
        /**
         * 关闭页面事件
         * @private
         */
        $onClosing : function() {
            return this.element.triggerHandler('onclosing');
        },
        /**
         * 页面关闭后事件
         * @private
         */
        $onClosed : function() {
            return this.element.triggerHandler('onclosed');
        },
        /**
         * 出错事件
         * @private
         */
        onerror : function(msg, url, line) {
            $.pageutils.showMessageDialog(msg);
        },
        /**
         * 增加Tab页签
         * @private
         */
        addTab : function(tab) {
            this.tabMap.put(tab.id, tab);
        },
        /**
         * 获取Tab页签
         */
        getTab : function(id) {
            return this.tabMap.get(id);
        },
        addOutlook : function(outlook) {
        	this.outlookMap.put(outlook.id, outlook);
        },
        getOutlook : function(id) {
       		return this.outlookMap.get(id)
        },
        /**
         * 增加卡片
         * @private
         */
        addCard : function(card) {
            this.cardMap.put(card.id, card);
        },
        /**
         * 获取卡片
         */
        getCard : function(id) {
            return this.cardMap.get(id);
        },
        /**
         * 增加Panel
         * @private
         */
        addPanel : function(panel) {
            this.panelMap.put(panel.id, panel);
        },
        /**
         * 获取Panel
         */
        getPanel : function(id) {
            return this.panelMap.get(id);
        },
        removePanel : function(id) {
            this.panelMap.remove(id);
        },
        /**
         * 显示关闭页面的提示信息
         */
        showCloseConfirm : function(obj) {
            if (this.hasChanged == true) {
                var topWin = $.pageutils.getLuiTop();
                if (topWin == null)
                    topWin = $.pageutils.getTrueParent();
                if (!window.SaveAndExit) {
                    $.confirmDialogComp.showDialog(trans("ml_confirm_close_dialog")
                    /*"确定关闭窗口?"*/, $.pagepart.okClose, null, obj, null);
                    return false;
                } else {
                    $.threeButtonsDialog.showDialog(trans("ml_confirm_save_dialog")
                    /*"信息已修改,     是否保存当前操作?"*/
                    , window.SaveAndExit, null, $.pagepart.okClose, [trans("ml_save"), trans("ml_does_not_save"), trans("ml_cancel")], obj);
                    return false;
                }
            } else {
                return true;
            }
        },
        /**
         * 没有提示的关闭
         */
        closeSilent : function(obj) {
            pageUI.hasChanged = false;
            $.pageutils.closeWinWithNoWarn();
        },
        /**
         * 关闭页面
         */
        toClose : function() {
            var result = this.$onClosing();
            if (result == false)
                return false;
            else
                this.$onClosed();
        },
        /**
         * 设置页面是否改变
         * @private
         */
        setChanged : function(hasChanged) {
            this.hasChanged = hasChanged;
        },
        /**
         * 获取页面是否改变
         */
        hasChanged : function() {
            return this.hasChanged;
        },
        handleHotKey : function(key) {
            var widgets = this.widgetMap.values();
            if (widgets != null && widgets.length > 0) {
                for (var i = 0; i < widgets.length; i++) {
                    var widget = widgets[i];
                    // 匹配Widget的Toolbar和Button
                    var comps = widget.getComponents();
                    for (var j = 0, m = comps.length; j < m; j++) {
                        var comp = comps[j];
                        if (comp.componentType == "TOOLBAR" || comp.componentType == "BUTTON") {
                            var obj = comp.handleHotKey(key);
                            if (obj != null)
                                return obj;
                        }
                    }
                    // 匹配Widget的Menubar和ContextMenu
                    var menus = widget.getMenus();
                    for (var j = 0, m = menus.length; j < m; j++) {
                        var menu = menus[j];
                        var obj = menu.handleHotKey(key);
                        if (obj != null)
                            return obj;
                    }
                }
            }
            return null;
        }
    });
    /**
     * 关闭dialog窗口
     */
    $.pagepart.okClose = function(obj) {
        obj._trigger('onclosing', null, obj);
        obj.hide();
        obj._trigger('onafterclose', null, obj);
    };

    /**
     * ******************************************SubmitRule*********************************************************************
     */
    /**
     * 提交规则描述
     * @class 提交规则描述
     */
    $.submitrule = function() {
        this.widgetRuleMap = null;
        this.tabSubmit = false;
        this.cardSubmit = false;
        this.panelSubmit = false;
        this.parentSubmitRule = null;
        this.paramMap = null;
    };

    $.submitrule.getObj = function() {
        return new $.submitrule();
    };

    $.extend($.submitrule.prototype, {
        /**
         * 增加参数
         */
        addParam : function(key, value) {
            if (this.paramMap == null)
                this.paramMap = $.hashmap.getObj();
            this.paramMap.put(key, value);
        },
        /**
         * 获取参数集合
         */
        getParamMap : function() {
            return this.paramMap;
        },
        /**
         * 设置父提交规则
         */
        setParentSubmitRule : function(submitRule) {
            this.parentSubmitRule = submitRule;
        },
        /**
         * 增加要提交的widget
         */
        addViewPartRule : function(id, widgetRule) {
            if (this.widgetRuleMap == null)
                this.widgetRuleMap = $.hashmap.getObj();
            this.widgetRuleMap.put(id, widgetRule);
        },
        /**
         * 获取Widget提交规则
         */
        getViewPartRule : function(id) {
            if (this.widgetRuleMap == null)
                return null;
            return this.widgetRuleMap.get(id);
        },
        /**
         * 设置Tab页签提交规则
         */
        setTabSubmit : function(submit) {
            this.tabSubmit = submit;
        },
        /**
         * 获取Tab页签提交规则
         */
        getTabSubmit : function() {
            return this.tabSubmit;
        },
        /**
         * 设置卡片提交规则
         */
        setCardSubmit : function(submit) {
            this.cardSubmit = submit;
        },
        /**
         * 获取卡片提交规则
         */
        getCardSubmit : function() {
            return this.cardSubmit;
        },
        /**
         * 设置Panel提交规则
         */
        setPanelSubmit : function(submit) {
            this.panelSubmit = submit;
        },
        /**
         * 获取Panel提交规则
         */
        getPanelSubmit : function() {
            return this.panelSubmit;
        }
    });

    /**
     * ***********************************************viewPartRule(WidgetRule)*****************************************************************
     */
    /**
     * ViewPart(Widget)提交规则
     * @class ViewPart(Widget)提交规则
     * @param id
     * @return
     */
    $.viewpartrule = function(id) {
        this.id = id;
        this.dsRuleMap = null;
        this.treeRuleMap = null;
        this.gridRuleMap = null;

        this.tabSubmit = false;
        this.cardSubmit = false;
        this.panelSubmit = false;
    };

    $.viewpartrule.getObj = function(id) {
        return new $.viewpartrule(id);
    };

    $.extend($.viewpartrule.prototype, {
        /**
         * 增加Dataset提交规则
         */
        addDsRule : function(id, dsRule) {
            if (this.dsRuleMap == null)
                this.dsRuleMap = $.hashmap.getObj();
            this.dsRuleMap.put(id, dsRule);
        },
        /**
         * 移出Dataset提交规则
         */
        removeDsRule : function(id) {
            if (this.dsRuleMap == null)
                return;
            this.dsRuleMap.remove(id);
        },
        /**
         * 设置Tab页签提交规则
         */
        setTabSubmit : function(submit) {
            this.tabSubmit = submit;
        },
        /**
         * 获取Tab页签提交规则
         */
        getTabSubmit : function() {
            return this.tabSubmit;
        },
        /**
         * 设置卡片提交规则
         */
        setCardSubmit : function(submit) {
            this.cardSubmit = submit;
        },
        /**
         * 获取卡片提交规则
         */
        getCardSubmit : function() {
            return this.cardSubmit;
        },
        /**
         * 设置Panel提交规则
         */
        setPanelSubmit : function(submit) {
            this.panelSubmit = submit;
        },
        /**
         * 获取Panel提交规则
         */
        getPanelSubmit : function() {
            return this.panelSubmit;
        },
        /**
         * 获取Widget的ID
         */
        getId : function() {
            return this.id;
        },
        /**
         * 增加Grid提交规则
         */
        addGridRule : function(id, grid) {
            if (this.gridRuleMap == null)
                this.gridRuleMap = $.hashmap.getObj();
            this.gridRuleMap.put(id, grid);
        },
        /**
         * 获取Grid提交规则
         */
        getGridRule : function(id) {
            if (this.gridRuleMap == null)
                return null;
            return this.gridRuleMap.get(id);
        },
        /**
         * 增加树提交规则
         */
        addTreeRule : function(id, tree) {
            if (this.treeRuleMap == null)
                this.treeRuleMap = $.hashmap.getObj();
            this.treeRuleMap.put(id, tree);
        },
        /**
         * 获取树提交规则
         */
        getTreeRule : function(id) {
            if (this.treeRuleMap == null)
                return null;
            return this.treeRuleMap.get(id);
        },
        /**
         * 获取Dataset提交规则
         */
        getDsRule : function(id) {
            if (this.dsRuleMap == null)
                return null;
            return this.dsRuleMap.get(id);
        },
        getFormRule : function(id) {
            if (this.formRuleMap == null)
                return null;
            return this.formRuleMap.get(id);
        },
        addFormRule : function(id, form) {
            if (this.formRuleMap == null)
                this.formRuleMap = $.hashmap.getObj();
            this.formRuleMap.put(id, form);
        }
    });

    /**
     * ***********************************datasetRule*************************************************************
     */
    /**
     * Dataset提交规则
     * @class Dataset提交规则
     * @param id
     * @param type
     * @return
     */
    $.datasetrule = function(id, type) {
        this.id = id;
        this.type = type;
    };

    $.datasetrule.getObj = function(id, type) {
        return new $.datasetrule(id, type);
    };

    $.extend($.datasetrule.prototype, {
        /**
         * 获取ID
         */
        getId : function() {
            return this.id;
        },
        /**
         * 获取类型
         */
        getType : function() {
            return this.type;
        }
    });

    /**
     * ***********************************treeRule*************************************************************
     */
    /**
     * 树提交规则
     * @class 树提交规则
     * @param id
     * @param type
     * @return
     */
    $.treerule = function(id, type) {
        this.id = id;
        this.type = type;
    };

    $.treerule.getObj = function(id, type) {
        return new $.treerule();
    };

    $.extend($.treerule.prototype, {
        /**
         * 获取ID
         */
        getId : function() {
            return this.id;
        },
        /**
         * 获取类型
         */
        getType : function() {
            return this.type;
        }
    });

    /**
     * ***********************************GridRule*************************************************************
     */
    /**
     * Grid提交规则
     * @class Grid提交规则
     * @param id
     * @param type
     * @return
     */
    $.gridrule = function(id, type) {
        this.id = id;
        this.type = type;
    };

    $.gridrule.getObj = function(id, type) {
        return new $.gridrule(id, type);
    };

    $.extend($.gridrule.prototype, {
        /**
         * 获取ID
         */
        getId : function() {
            return this.id;
        },
        /**
         * 获取类型
         */
        getType : function() {
            return this.type;
        }
    });

    /**
     * ***********************************FormRule*************************************************************
     */
    /**
     * Form提交规则
     * @class Form提交规则
     * @param id
     * @param type
     * @return
     */
    $.formrule = function(id, type) {
        this.id = id;
        this.type = type;
    };

    $.formrule.getObj = function(id, type) {
        return new $.formrule(id, type);
    };

    $.extend($.formrule.prototype, {
        /**
         * 获取ID
         */
        getId : function() {
            return this.id;
        },
        /**
         * 获取类型
         */
        getType : function() {
            return this.type;
        }
    });

    /**
     * ******************************************serverProxy*********************************************************************
     */
    $.serverproxy = function(obj) {
        obj = obj || {};
        this.params = $.hashmap.getObj();
        this.returnParams = $.hashmap.getObj();
        this.returnFunc = null;
        this.submitRule = null;
        this.eventName = obj.eventName;
        this.listener = obj.listener;
        this.async = obj.async == null ? true : obj.async;
        this.nmc = true;
    };

    $.serverproxy.getObj = function(obj) {
        return new $.serverproxy(obj);
    };
    $.extend($.serverproxy.prototype, {
        destroySelf : function() {
            this.submitRule = null;
            this.params = null;
            this.returnFunc = null;
            this.eventName = null;
            this.listener = null;
        },
        setNmc : function(nmc) {
            this.nmc = nmc;
        },
        setAsync : function(async) {
            this.async = async;
        },
        setParamMap : function(paramsMap) {
            this.params = paramsMap;
        },
        addParam : function(key, value) {
            var argLen = arguments.length;
            if (argLen == 1) {
                var oThis = this;
                $.each(arguments[0], function(key, val) {
                    oThis.params.put(key, val);
                });
            } else if (argLen == 2) {
                this.params.put(arguments[0], arguments[1]);
            }
        },
        setSubmitRule : function(submitRule) {
            this.submitRule = submitRule;
        },
        setReturnArgs : function(returnArgs) {
            this.returnArgs = returnArgs;
        },
        setReturnFunc : function(returnFunc) {
            this.returnFunc = returnFunc;
        },
        getSubmitRule : function() {
            var useSubmitRule = this.submitRule;
            if (useSubmitRule == null && this.listener != null) {
                if (this.listener[this.eventName]) {
                    useSubmitRule = this.listener[this.eventName].submitRule;
                }
            }
            if (useSubmitRule == null)
                useSubmitRule = $.submitrule.getObj();
            this.submitRule = useSubmitRule;
            return this.submitRule;
        },
        getParamString : function(useSubmitRule) {
            var requestJson = new Object();
            if (this.listener) {
                if (this.listener.source_id) {
                    requestJson.source_id = this.listener.source_id;
                }
                if (this.listener.listener_id) {
                    requestJson.listener_id = this.listener.listener_id;
                }
                if (this.listener.widget_id) {
                    requestJson.widget_id = this.listener.widget_id;
                }
                if (this.eventName) {
                    requestJson.event_name = this.eventName;
                }
                if (this.listener.source_type) {
                    requestJson.source_type = this.listener.source_type;
                }
                if (this.listener.parent_source_id) {
                    requestJson.parent_source_id = this.listener.parent_source_id;
                }
            }
            if (this.params) {
                var size = this.params.size();
                if (size == 0) {
                    return requestJson;
                }
                var keySet = this.params.keySet();
                for (var i = 0; i < size; i++) {
                    var key = keySet[i];
                    var value = this.params.get(key);
                    if (value == null)
                        value = "";
                    else if ( typeof value == "string") {
                        value = value.replace(/\&/g, "&amp;");
                        value = value.replace(/\</g, "&lt;");
                    }
                    requestJson[key] = value;
                }
            }
            if (pageUI.hasChanged) {
                requestJson.hasChanged = pageUI.hasChanged;
            }

            var paramMap = useSubmitRule.getParamMap();
            if (paramMap) {
                var keys = paramMap.keySet();
                for (var i = 0; i < keys.length; i++) {
                    var key = keys[i];
                    var value = paramMap.get(key);
                    if (!value)
                        value = $.pageutils.getParameter(key);
                    if (!value)
                        value = $.pageutils.getSessionAttribute(key);
                    if (!value)
                        value = "";
                    requestJson[key] = value;
                }
            }
            return requestJson;
        },
        getPageCtxStr : function(useSubmitRule) {
            var pageJson = pageUI.getContext(useSubmitRule);
            return pageJson;
        },
        /**
         * 外部调用处理
         */
        execute : function() {
            if (pageUI.exParams.size() > 0) {
                var keySet = pageUI.exParams.keySet();
                var size = pageUI.exParams.size();
                for (var i = 0; i < size; i++) {
                    var key = keySet[i];
                    var value = pageUI.exParams.get(key);
                    this.addParam(key, value);
                }
                pageUI.exParams.clear();
            }

            //		    if (getProxyReturnExecuting() > 0) {
            //		        getProxyArray().push(this);
            //		        setTimeout("$.serverproxy.execServerProxyList()", 100);
            //		        return;
            //		    }
            //		    if (getProxyArray().length > 0) {
            //		        getProxyArray().push(this);
            //		        $.serverproxy.execServerProxyList();
            //		        return;
            //		    }
            //		    proxyReturnExecutingAdd();
            var ajaxDefferd = $(window).data('ajaxDefferd');
            if (ajaxDefferd && ajaxDefferd.state() === 'pending') {
                setTimeout("$.serverproxy.aggregateProxyRequest()", 50);
                return;
            }

            if ($(window).data('proxyArr') && $(window).data('proxyArr').length > 0) {
                $(window).data('proxyArr').push(this);
                $.serverproxy.aggregateProxyRequest();
                return;
            }

            var innerArgsList = [[this.returnArgs, this.returnFunc]];

            var jsonStr = JSON.stringify(this.getRequestJson());
            if (this.async) {
                ajaxDefferd = $.serverproxy.proxyCall(jsonStr, this.async, this.nmc);
                $(window).data('ajaxDefferd', ajaxDefferd);
                ajaxDefferd.done(function(xmlData) {
                    $.serverproxy.returnSuccessFun(xmlData, innerArgsList, ajaxDefferd.ref);
                }).fail(function(xhr, textStatus, errorThrown) {
                    $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
                });
            } else {
                var ret = $.serverproxy.proxyCall(jsonStr, this.async, this.nmc);
                $.serverproxy.returnSuccessFun(ret.result, innerArgsList, ret.ref);
            }

        },
        getRequestJson : function() {
            var requestJson = {};
            requestJson["id"] = $.pageutils.getPageId();
            var useSubmitRule = this.getSubmitRule();
            var haha = this.getParamString(useSubmitRule);
            var gpsJson = new Array();
            gpsJson.push(haha);
            requestJson["reqparas"] = gpsJson;
            requestJson["pagemeta"] = pageUI.getContext(useSubmitRule);
            var parentPageJson = {};
            if (useSubmitRule && useSubmitRule.parentSubmitRule) {
                if ( typeof ($.pageutils.getTrueParent().$.pageutils.getPageId) == "function") {
                    parentPageJson.id = $.pageutils.getTrueParent().$.pageutils.getPageId();
                } else {
                    parentPageJson.id = "";
                }
                if ( typeof ($.pageutils.getTrueParent().pageUI) != 'undefined' && $.pageutils.getTrueParent().pageUI) {
                    parentPageJson["pagemeta"] = $.pageutils.getTrueParent().pageUI.getContext(useSubmitRule.parentSubmitRule);
                }
                requestJson["parentpe"] = parentPageJson;
            }
            //requestJson["parentpe"]=this.hideDialog(useSubmitRule);
            return requestJson;
        }
    });

    $.serverproxy.compressRequest = function(contextXml) {
        return $.pageutils.compress(contextXml);
    };

    $.serverproxy.wrapProxy = function(proxy) {
        if (pageUI.exParams.size() > 0) {
            var keySet = pageUI.exParams.keySet();
            var size = pageUI.exParams.size();
            for (var i = 0; i < size; i++) {
                var key = keySet[i];
                var value = pageUI.exParams.get(key);
                proxy.addParam(key, value);
            }
        }
        var proxyArr = $(window).data("proxyArr") || [];
        proxyArr.push(proxy);
        $(window).data("proxyArr", proxyArr);

        $.serverproxy.aggregateProxyRequest();
    };

    $.serverproxy.aggregateProxyRequest = function() {
        var ajaxDeffered = $(window).data('ajaxDefferd');
        if (ajaxDeffered && ajaxDeffered.state() === 'pending') {
            setTimeout("$.serverproxy.aggregateProxyRequest()", 50);
            return;
        }
        if(!$(window).data("proxyArr")){
          return ;
        }
        var count = $(window).data("proxyArr").length;
        if (count == 0) {
            return;
        }
        var submitRules = [], paramList = [], innerArgsList = [], async = true, i = 0, nmc = true;
        while ($(window).data("proxyArr").length) {
            var proxy = $(window).data("proxyArr").shift();
            if (!proxy.nmc)
                nmc = false;
            var rule = proxy.getSubmitRule(), arr = [], args = proxy.returnArgs, func = proxy.returnFunc;
            if (innerArgsList.length == 0 || (args == null && func == null)) {
                submitRules.push(rule);
                paramList[i] = proxy.getParamString(rule);
                arr.push(args);
                arr.push(func);
                innerArgsList[i] = arr;
                async = (async && proxy.async);
                proxy.destroySelf();
            } else {
                break;
            }
            i++;
        }
        var useSubmitRule = $.serverproxy.mergeSubmitRules(submitRules);
        var requestJson = {};
        requestJson["id"] = $.pageutils.getPageId();
        var gpsJson = [];
        for (var i = 0; i < count; i++) {
            gpsJson.push(paramList[i]);
        }
        requestJson["reqparas"] = gpsJson;
        requestJson["pagemeta"] = pageUI.getContext(useSubmitRule);
        var parentPageJson = {};
        if (useSubmitRule && useSubmitRule.parentSubmitRule) {
            if ( typeof ($.pageutils.getTrueParent().$.pageutils.getPageId) == "function") {
                parentPageJson.id = $.pageutils.getTrueParent().$.pageutils.getPageId();
            } else {
                parentPageJson.id = "";
            }
            if ( typeof ($.pageutils.getTrueParent().pageUI) != 'undefined' && $.pageutils.getTrueParent().pageUI) {
                parentPageJson["pagemeta"] = $.pageutils.getTrueParent().pageUI.getContext(useSubmitRule.parentSubmitRule);
            }
            requestJson["parentpe"] = parentPageJson;
        }
        var jsonStr = null;
        try {
            jsonStr = JSON.stringify(requestJson);
        } catch (e) {
            console.error(e);
        }

        showDefaultLoadingBar();
        if ($(window).data("proxyArr").length > 0) {
            $.serverproxy.aggregateProxyRequest();
        }

        if (async) {
            ajaxDefferd = $.serverproxy.proxyCall(jsonStr, async, nmc);
            $(window).data('ajaxDefferd', ajaxDefferd);
            ajaxDefferd.done(function(jsonData) {
                $.serverproxy.returnSuccessFun(jsonData, innerArgsList, ajaxDefferd.ref);
            }).fail(function(xhr, textStatus, errorThrown) {
                $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
            });
        } else {
            var ret = $.serverproxy.proxyCall(jsonStr, async, nmc);
            $.serverproxy.returnSuccessFun(ret.result, innerArgsList, ret.ref);
        }
    };

    $.serverproxy.proxyCall = function(contextJson, async, nmc) {
        var requestJson = "type=processEvent&xml=";
        var result = $.serverproxy.compressRequest(contextJson);
        if (result == null) {
            requestJson += encodeURIComponent(contextJson);
        } else {
            requestJson += encodeURIComponent(result);
            requestJson += "&compress=1&compressl=" + contextJson.length;
        }
        var ajax = $.ajaxutils.getObj();
        ajax.setPath($.pageutils.getCorePath());
        ajax.setQueryStr(requestJson);
        return ajax.post(async == null ? true : async, true);
    };

    /**
     * Ajax返回后调用方法
     * @private
     */
    $.serverproxy.returnFailFun = function(xhr, textStatus, errorThrown) {
        if (xhr != null && xhr.status == 306) {// 如果是会话失效，则跳转到登陆页
            $.pageutils.openLoginPage();
        } else {
            $.pageutils.showErrorDialog(errorThrown);
        }
    };

    /**
     * Ajax返回后调用方法
     * @private
     */
    $.serverproxy.returnSuccessFun = function(data, returnArgsList, ajaxObj) {
        var execList = [];
        try {
            if ($.pageutils.handleException(data, ajaxObj)) {
                var userArgs =null;// 
                if(returnArgsList){
                   userArgs=returnArgsList[0][0];
                }
                var contentNodes = data.contents;
                var result = true;
                if (contentNodes) {
                    for (var i = 0, count = contentNodes.length; i < count; i++) {
                        var content = contentNodes[i];
                        var isPlug = content["isPlug"];
                        var pPageMetas = content["parentpe"];
                        if (pPageMetas && $.pageutils.getTrueParent().$.serverproxy) {
                            for (var k = 0; k < pPageMetas.length; k++) {
                                $.pageutils.getTrueParent().$.serverproxy.$updateCtx(pPageMetas[k], userArgs, false);
                            }
                        }
                        $.serverproxy.$updateCtx(content, userArgs, false);
                        var script = content["afterExec"];
                        if (script != null && script != "") {
                            execList = script;
                        }
                    }
                }
                for (var i = 0; i < returnArgsList.length - 1; i++) {
                    var args = returnArgsList[i];
                    var returnFunc = args[1];
                    var userArg = args[0];
                    if (returnFunc != null)
                        returnFunc.call(this, userArg, true);
                }
                //由于IE 9在执行了div删除之后，会销毁全局变量，需将这段逻辑挪到后面处理
                for (var i = 0; i < execList.length; i++) {
                    eval(execList[i]);
                }
                hideDefaultLoadingBar();
                return result;
            }
            hideDefaultLoadingBar();
        } catch (e) {
            hideDefaultLoadingBar();
            console.error(e);
            Logger.error(e);
        } finally {
            $.pageutils.adjustContainerFramesHeight();
        }
    };

    $.serverproxy.mergeSubmitRules = function(rules) {
        var submitRule = $.submitrule.getObj();
        for (var i = 0; i < rules.length; i++) {
            $.serverproxy.mergeSubmitRule(submitRule, rules[i]);
        }
        return submitRule;
    };

    $.serverproxy.mergeSubmitRule = function(targetSubmitRule, sourceSubmitRule) {
        if (sourceSubmitRule == null)
            return;
        if (sourceSubmitRule.tabSubmit)
            targetSubmitRule.tabSubmit = true;
        if (sourceSubmitRule.cardSubmit)
            targetSubmitRule.cardSubmit = true;
        if (sourceSubmitRule.panelSubmit)
            targetSubmitRule.panelSubmit = true;
        if (targetSubmitRule.widgetRuleMap == null)
            targetSubmitRule.widgetRuleMap = sourceSubmitRule.widgetRuleMap;
        else if (sourceSubmitRule.widgetRuleMap != null) {
            var keys = sourceSubmitRule.widgetRuleMap.keySet();
            if (keys != null) {
                for (var i = 0; i < keys.length; i++) {
                    var sourceWidgetRule = sourceSubmitRule.widgetRuleMap.get(keys[i]);
                    if (sourceWidgetRule == null)
                        continue;
                    var targetWidgetRule = targetSubmitRule.widgetRuleMap.get(keys[i]);
                    if (targetWidgetRule != null)
                        $.serverproxy.mergeWidgetRule(targetWidgetRule, sourceWidgetRule);
                    else
                        targetSubmitRule.addViewPartRule(sourceWidgetRule.getId(), sourceWidgetRule);
                }
            }
        }
        if (targetSubmitRule.parentSubmitRule == null)
            targetSubmitRule.parentSubmitRule = sourceSubmitRule.parentSubmitRule;
        else
            $.serverproxy.mergeSubmitRule(targetSubmitRule.parentSubmitRule, sourceSubmitRule.parentSubmitRule);
    };

    /**
     * 合并widget提交规则
     *
     * @param {} targetWidgetRule
     * @param {} sourceWidgetRule
     */
    $.serverproxy.mergeWidgetRule = function(targetWidgetRule, sourceWidgetRule) {
        if (sourceWidgetRule == null)
            return;
        if (sourceWidgetRule.tabSubmit == true)
            targetWidgetRule.tabSubmit = true;
        if (sourceWidgetRule.cardSubmit == true)
            targetWidgetRule.cardSubmit = true;
        if (sourceWidgetRule.panelSubmit == true)
            targetWidgetRule.panelSubmit = true;
        if (targetWidgetRule.dsRuleMap == null)
            targetWidgetRule.dsRuleMap = sourceWidgetRule.dsRuleMap;
        else if (sourceWidgetRule.dsRuleMap != null) {
            var keys = sourceWidgetRule.dsRuleMap.keySet();
            if (keys != null) {
                for (var i = 0; i < keys.length; i++) {
                    var sourceDsRule = sourceWidgetRule.dsRuleMap.get(keys[i]);
                    if (sourceDsRule == null)
                        continue;
                    var targetDsRule = targetWidgetRule.dsRuleMap.get(keys[i]);
                    if (targetDsRule != null)
                        $.serverproxy.mergeDsRule(targetDsRule, sourceDsRule);
                    else
                        targetWidgetRule.addDsRule(sourceDsRule.getId(), sourceDsRule);
                }
            }
        }
        if (targetWidgetRule.treeRuleMap == null)
            targetWidgetRule.treeRuleMap = sourceWidgetRule.treeRuleMap;
        else if (sourceWidgetRule.treeRuleMap != null) {
            var keys = sourceWidgetRule.treeRuleMap.keySet();
            if (keys != null) {
                for (var i = 0; i < keys.length; i++) {
                    var sourceTreeRule = sourceWidgetRule.treeRuleMap.get(keys[i]);
                    if (sourceTreeRule == null)
                        continue;
                    var targetTreeRule = targetWidgetRule.treeRuleMap.get(keys[i]);
                    if (targetTreeRule != null)
                        $.serverproxy.mergeTreeRule(targetTreeRule, sourceTreeRule);
                    else
                        targetWidgetRule.addTreeRule(sourceTreeRule.getId(), sourceTreeRule);
                }
            }
        }
        if (targetWidgetRule.gridRuleMap == null)
            targetWidgetRule.gridRuleMap = sourceWidgetRule.gridRuleMap;
        else if (sourceWidgetRule.gridRuleMap != null) {
            var keys = sourceWidgetRule.gridRuleMap.keySet();
            if (keys != null) {
                for (var i = 0; i < keys.length; i++) {
                    var sourceGridRule = sourceWidgetRule.gridRuleMap.get(keys[i]);
                    if (sourceGridRule == null)
                        continue;
                    var targetGridRule = targetWidgetRule.gridRuleMap.get(keys[i]);
                    if (targetGridRule != null)
                        $.serverproxy.mergeGridRule(targetGridRule, sourceGridRule);
                    else
                        targetWidgetRule.addGridRule(sourceGridRule.getId(), sourceGridRule);
                }
            }
        }
        if (targetWidgetRule.formRuleMap == null)
            targetWidgetRule.formRuleMap = sourceWidgetRule.formRuleMap;
        else if (sourceWidgetRule.formRuleMap != null) {
            var keys = sourceWidgetRule.formRuleMap.keySet();
            if (keys != null) {
                for (var i = 0; i < keys.length; i++) {
                    var sourceFormRule = sourceWidgetRule.formRuleMap.get(keys[i]);
                    if (sourceFormRule == null)
                        continue;
                    var targetFormRule = targetWidgetRule.formRuleMap.get(keys[i]);
                    if (targetFormRule != null)
                        $.serverproxy.mergeFormRule(targetFormRule, sourceFormRule);
                    else
                        targetWidgetRule.addFormRule(sourceFormRule.getId(), sourceFormRule);
                }
            }
        }
    };

    /**
     * 合并ds提交规则
     *
     * @param {} targetDsRule
     * @param {} sourceDsRule
     */
    $.serverproxy.mergeDsRule = function(targetDsRule, sourceDsRule) {
        var targetRuleValue = $.serverproxy.getDsRuleValue(targetDsRule.type);
        var sourceRuleValue = $.serverproxy.getDsRuleValue(sourceDsRule.type);
        if (targetRuleValue < sourceRuleValue)
            targetDsRule.type = sourceDsRule.type;
        else if (targetRuleValue == sourceRuleValue && targetDsRule.type != sourceDsRule.type)
            targetDsRule.type = "ds_all_line";
    };

    $.serverproxy.getDsRuleValue = function(type) {
        if ( type = "ds_current_line")
            return 0;
        else if ( type = "ds_current_key")
            return 1;
        else if ( type = "ds_current_page")
            return 1;
        else if ( type = "ds_all_sel_line")
            return 1;
        else if ( type = "ds_all_line")
            return 2;
        else
            return -1;
    };

    /**
     * 合并tree提交规则
     *
     * @param {} targetTreeRule
     * @param {} sourceTreeRule
     */
    $.serverproxy.mergeTreeRule = function(targetTreeRule, sourceTreeRule) {
        var targetRuleValue = $.serverproxy.getTreeRuleValue(targetTreeRule.type);
        var sourceRuleValue = $.serverproxy.getTreeRuleValue(sourceTreeRule.type);
        if (targetRuleValue < sourceRuleValue)
            targetTreeRule.type = sourceTreeRule.type;
    };

    $.serverproxy.getTreeRuleValue = function(type) {
        if ( type = "tree_current_parent")
            return 0;
        else if ( type = "tree_current_parent_children")
            return 1;
        else if ( type = "tree_all")
            return 2;
        else
            return -1;
    };

    /**
     * 合并grid提交规则
     *
     * @param {} targetGridRule
     * @param {} sourceGridRule
     */
    $.serverproxy.mergeGridRule = function(targetGridRule, sourceGridRule) {
        var targetRuleValue = $.serverproxy.getGridRuleValue(targetGridRule.type);
        var sourceRuleValue = $.serverproxy.getGridRuleValue(sourceGridRule.type);
        if (targetRuleValue < sourceRuleValue)
            targetGridRule.type = sourceGridRule.type;
    };
    $.serverproxy.getGridRuleValue = function(type) {
        if ( type = "grid_current_row")
            return 0;
        else if ( type = "grid_all_row")
            return 1;
        else
            return -1;
    };
    /**
     * 合并form提交规则
     *
     * @param {} targetFormRule
     * @param {} sourceFormRule
     */
    $.serverproxy.mergeFormRule = function(targetFormRule, sourceFormRule) {
        var targetRuleValue = $.serverproxy.getFormRuleValue(targetFormRule.type);
        var sourceRuleValue = $.serverproxy.getFormRuleValue(sourceFormRule.type);
        if (targetRuleValue < sourceRuleValue)
            targetFormRule.type = sourceFormRule.type;
    };
    $.serverproxy.getFormRuleValue = function(type) {
        if ( type = "no_child")
            return 0;
        else if ( type = "all_child")
            return 1;
        else
            return -1;
    };

    $.serverproxy.$updateCtx = function(jsonObj, userArgs, isParent) {
        if (!window.pageUI) {
            return;
        }
        var nowUI = window.pageUI;
        if (isParent) {
            nowUI = $.pageutils.getTrueParent().pageUI;
        }
        var result = true;
        var resultVal = jsonObj["result"];
        if (resultVal) {
            if (!resultVal) {
                result = false;
            }
        }
        var beforeExecScript = jsonObj["beforeExec"];
        if (beforeExecScript) {
            for (var i = 0; i < beforeExecScript.length; i++) {
                eval(beforeExecScript[i]);
            }
        }
        var attrVals = jsonObj["as"];
        if (attrVals) {
            for (var attr in attrVals) {
                $.pageutils.setSessionAttribute(attr, attrVals[attr]);
            }
        }
        var pagemeta = jsonObj["pagemeta"];
        beforeExecScript = pagemeta["beforeExec"];
        if (beforeExecScript) {
            for (var i = 0; i < beforeExecScript.length; i++) {
                eval(beforeExecScript[i]);
            }
        }

        $.serverproxy.$processContext(pagemeta, nowUI);
        var viewJsonObjs = pagemeta["views"];
        if (viewJsonObjs) {
            for (var k = 0; k < viewJsonObjs.length; k++) {
                var viewJsonObj = viewJsonObjs[k];
                var widgetId = viewJsonObj["id"];
                var viewObj = nowUI.getViewPart(widgetId);
                if (viewObj == null)
                    continue;
                $.serverproxy.$processContext(viewJsonObj, viewObj);
                for (var attr in viewJsonObj) {
                    var attrVal = viewJsonObj[attr];

                    switch (attr) {
                    case $.EventContextConstant.context:
                        break;
                    case "card":
                        var layoutId = attrVal["id"];
                        var layout = viewObj.cardMap.get(layoutId);
                        $.serverproxy.$processContext(attrVal, layout);
                        break;
                    case "tab":
                        var layoutId = attrVal["id"];
                        var layout = viewObj.tabMap.get(layoutId);
                        $.serverproxy.$processContext(attrVal, layout);
                        break;
                    case "panel":
                        var layoutId = attrVal["id"];
                        var layout = viewObj.panelMap.get(layoutId);
                        $.serverproxy.$processContext(attrVal, layout);
                        break;
                    case "datasets":
                        var length = attrVal.length;
                        for (var i = 0; i < length; i++) {
                            var dsId = attrVal[i]["id"];
                            var ds = viewObj.getDataset(dsId);
                            if (ds == null)
                                continue;
                            ds.setData(attrVal[i], userArgs);
                        }
                        break;
                    case "componets":
                        for (var inner in attrVal) {
                            switch (inner) {
                            case "generalcomps":
                                var comps = attrVal[inner];
                                for (var i = 0; i < comps.length; i++) {
                                    var compId = comps[i]["id"];
                                    var comp = viewObj.getComponent(compId);
                                    $.serverproxy.$processContext(comps[i], comp);
                                }
                                break;
                            case "contextmenus":
                                var comps = attrVal[inner];
                                for (var i = 0; i < comps.length; i++) {
                                    var menuId = comps[i]["id"];
                                    var menu = viewObj.getMenu(menuId);
                                    $.serverproxy.$processContext(comps[i], menu);
                                }
                                break;
                            case "menubars":
                                var comps = attrVal[inner];
                                for (var i = 0; i < comps.length; i++) {
                                    var menubarId = comps[i]["id"];
                                    var menubar = viewObj.getMenu(menubarId);
                                    $.serverproxy.$processContext(comps[i], menubar);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        var execScript = pagemeta["exec"];
        if (execScript) {
            for (var i = 0; i < execScript.length; i++) {
            try { 
            	 eval(execScript[i]);
              }catch(e){
              	console.info(e);
              }
            }
        }

        execScript = jsonObj["exec"];
        if (execScript) {
            for (var i = 0; i < execScript.length; i++) {
              try { 
            	eval(execScript[i]);
              }catch(e){
              		console.info(e);
              }
            }
        }
        return result;
    };

    $.serverproxy.$processContext = function(jsonObj, source) {
        if (source) {
            var cxtVal = jsonObj["c"];
            if (cxtVal) {
                var context;
                if ( typeof (cxtVal) == "String") {
                    eval("context = " + cxtVal);
                } else {
                    context = cxtVal;
                }

                source.setContext(context);
            }
        }
    };
    /**
     * *******************************Application*******************************
     */
    $.application = function() {
        this.pageUIMap = {};
    };

    $.application.getObj = function() {
        if (window.application == null)
            window.application = new $.application();
        return window.application;
    };

    $.extend($.application.prototype, {
        addPageUI : function(id, pageUI) {
            this.pageUIMap[id] = pageUI;
        },
        getPageUI : function(id) {
            var pageUI = this.pageUIMap[id];
            if (pageUI == null)
                pageUI = $.pageutils.getPopParent().pageUI;
            return pageUI;
        }
    });

    /**
     * *******************************serviceproxy*******************************
     */
    $.serviceproxy = function(sName) {
        this.name = sName;
    };
    /**
     * 通过此方法，获取json注册服务
     *
     * @param sName 服务名
     */
    $.serviceproxy.getObj = function(sName) {
        return new $.serviceproxy(sName);
    };

    $.extend($.serviceproxy.prototype, {
        execute : function(method, args) {
            var obj = {
                rpcname : this.name,
                method : method
            };
            for (var i = 1; i < arguments.length; i++) {
                obj["params" + (i - 1)] = arguments[i];
            }

            var data = toJSON(obj);
            var ajax = $.ajaxutils.getObj();
            ajax.setPath($.pageutils.getCorePath() + "/rpc");
            ajax.setQueryStr("rpcdata=" + data);
            var responseText = ajax.post(false);
            try {
                eval("var obj = " + responseText + ";");
            } catch(er) {
                eval("var obj = '" + responseText + "';");
            }
            return obj;
        }
    });

    window.__flash__removeCallback = function(e, f) {
        try {
            if (e) {
                e[f] = null;
            }
        } catch(g) {
        }
    };
    // 页面操作脚本 --------------------------------------------------------
    /**
     * 删除布局操作
     * @param {} id
     * @param {} type
     * @param {} params
     * @return {Boolean}
     */
    window.execDynamicScript2RemoveLayout = function(id, type, params) {
        try {
            if (!id)
                return false;
            var obj = ("string" == typeof id) ? $("#" + id) : id;
            if (!obj.size() > 0)
                return false;
            var parent;
            if (obj[0].objType || obj.attr('isViewPart')=='true') {
                parent = $("#" + obj.attr('id') + "_raw");
                if (parent.size() > 0) {
                    obj = parent;
                    parent = obj.parent();
                }
            }

            if (!parent || parent.size()==0) {
                parent = obj.parent();
            }
            if (parent && parent.size() > 0) {
                obj.remove();
                return true;
            }
            return false;
        } catch (e) {
        };
    };
    /**
     * 删除容器操作
     * @param {} id
     * @param {} type
     * @param {} params
     * @return {Boolean}
     */
    window.execDynamicScript2RemovePanel = function(id, type, params) {
        if (!id)
            return false;
        var obj = ("string" == typeof id) ? $ge(id) : id;
        if (!obj)
            return false;
        var parent;
        if (obj.objType) {
            parent = $ge(obj.id + "_raw");
            if (parent) {
                obj = parent;
                parent = obj.parentNode;
            }
        } else {
            parent = obj.parentNode;
        }
        if (parent) {
            parent.removeChild(obj);
            $(window).triggerHandler('resize');
            return true;
        }
        return false;
    };
    /**
     * 删除控件操作
     * @param {} id
     * @param {} widgetId
     * @param {} compId
     * @param {} params
     * @return {Boolean}
     */
    window.execDynamicScript2RemoveComponent = function(id, widgetId, compId, params) {
        if (!id || !widgetId || !compId)
            return false;
        var obj = ("string" == typeof id) ? $ge(id) : id;
        if (!obj)
            return false;
        var parent = obj.parentNode;
        if (parent) {
            pageUI.getViewPart(widgetId).removeComponent(compId);
            parent.removeChild(obj);
            return true;
        }
        return false;
    };

    /**
     * 删除formElement
     *
     * @param {} id
     * @param {} widgetId
     * @param {} formId
     * @param {} feId
     * @return {Boolean}
     */
    window.execDynamicScript2RemoveFormElement2 = function(id, widgetId, formId, feId) {
        if (!id || !widgetId || !formId)
            return false;
        var obj = ("string" == typeof id) ? $("#"+id) : $(id);
        if (obj.size()==0)
            return false;
		else {
			pageUI.getViewPart(widgetId).getComponent(formId).removeElementById(feId);
            obj.remove();
            return true;
		}
    };
    /**
     * 删除gridclumn操作
     * @param {} widgetId
     * @param {} gridId
     * @param {} keyName
     * @param {} params
     */
    window.execDynamicScript2RemoveGridColumn = function(widgetId, gridId, keyName, params) {
        var comp = pageUI.getViewPart(widgetId).getComponent(gridId);
        var header = comp.removeHeader(keyName);
        comp.paintData();
    };
    /**
     * 删除formelement操作
     * @param {} widgetId
     * @param {} formId
     * @param {} keyName
     * @param {} params
     */
    window.execDynamicScript2RemoveFormElement = function(widgetId, formId, keyName, params) {
        try {
            var comp = pageUI.getViewPart(widgetId).getComponent(formId);
            comp.removeElementById(keyName);
            comp.pLayout.paint(true);
        } catch (e) {
            alert(e);
        }
    };
    /**
     * *******************************以下为全局工具类*******************************
     */
    /**
     * 获取编辑公式服务
     */
    $.pageutils = {
        getFormularService : function() {
            return $.serviceproxy.getObj("com.haiyou.wfw.common.model.IEditFormularService");
        },
        /**
         * 获取上传文件服务
         */
        getFileService : function() {
            return getService("com.haiyou.wfw.common.model.IFileService");
        },
        /**
         * 获取页面参数Map表.此参数是从URL中获取
         *
         * @return{HashMap} paramsMap
         */
        getParamsMap : function() {
            return window.$paramsMap;
        },
        /**
         * 根据key获取页面参数
         */
        getParameter : function(key) {
        	var value = null;
        	if(window && window.$paramsMap){
        		value = window.$paramsMap.get(key);
        	}
            return value == null ? null : decodeURIComponent(value);
        },
        /**
         * 设置某一参数
         */
        setParameter : function(key, value) {
            window.$paramsMap.put(key, value);
        },
        /**
         * 获取客户端session属性map
         */
        getSessionAttributeMap : function() {
            if ( typeof $cs_clientSession != "undefined")
                return $cs_clientSession;
        },
        /**
         * 设置Session属性
         * @param key
         * @param value
         * @return
         */
        setSessionAttribute : function(key, value) {
            $cs_clientSession[key] = value;
        },
        /**
         * 根据key获取客户段session中对应的value
         *
         * @param key
         */
        getSessionAttribute : function(key) {
            if ( typeof $cs_clientSession != "undefined")
                return $cs_clientSession[key];
        },
        /**
         * 获取客户端StickString
         */
        getStickString : function() {
            return window.$cs_clientStickKeys;
        },
        winunload : function() {
            if (window.pageUI == null)
                return;
            window.pageUI.$onClosed();
            $.pageutils.removeAllComponent();
        },
        /**
         * 获取对应的数据格式渲染器
         */
        getMasker : function(type) {
            if ( typeof window.$maskerMeta != "undefined" && ( type == 'INTEGERTEXT' || type == 'FLOATTEXT')) {
                return $.numbermasker.getObj(window.$maskerMeta.NumberFormatMeta);
            }
            if ( typeof window.$maskerMeta != "undefined" && type == 'DATETEXT') {
                return $.datemasker.getObj(window.$maskerMeta.DateFormatMeta);
            }
            if ( typeof window.$maskerMeta != "undefined" && type == "DateTimeText")
                return $.datetimemasker.getObj(window.$maskerMeta.DateTimeFormatMeta);
            return null;
        },
        /**
         * 获取页面标识ID
         */
        getPageId : function() {
            return $.pageutils.getSessionAttribute("pageId");
        },
        /**
         * 获取页面唯一标识
         * @return
         */
        getPageUniqueId : function() {
            return $.pageutils.getSessionAttribute("pageUniqueId");
        },
        /**
         * 获取application唯一标识
         */
        getAppUniqueId : function() {
            return $.pageutils.getSessionAttribute("appUniqueId");
        },
        /**
         * 获取web应用context路径
         */
        getRootPath : function() {
            return window.globalPath;
        },
        /**
         * 获取Web应用LfwDispatchServlet配置的路径
         */
        getCorePath : function() {
            return window.corePath;
        },
        /**
         * 获取单据在nodes目录下的路径
         */
        getNodePath : function() {
            return window.globalPath + "/html/nodes/" + window.$pageId;
        },
        /**
         * 显示进度对话框
         *
         * @param message 要显示标题
         * @param attachComp 绑定控件，将显示于指定控件中心位置
         */
        showProgressDialog : function(message, attachComp) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            topWin.ProgressDialogComp.showDialog(message);
        },
        /**
         * 隐藏进度对话框
         */
        hideProgressDialog : function() {
            ProgressDialogComp.hideDialog();
        },
        /**
         * 显示错误对话框
         */
        showErrorDialog : function(msg, func, title, okText) {
            $.errorDialogComp.showDialog(msg, title, okText, func);
        },
        /**
         * 隐藏错误对话框
         */
        hideErrorDialog : function() {
            $.errorDialogComp.hideDialog();
        },
        /**
         * 隐藏信息对话框
         */
        hideMessageDialog : function() {
            $.messageDialogComp.hideDialog();
        },
        /**
         * 在正中位置打开窗口
         */
        openWindowInCenter : function(url, title, height, width) {
            if (!url) {
                return;
            }
            if (url.indexOf("?") > 0) {
                url = url + "&lrid=" + Math.UUID();
            } else {
                url = url + "?lrid=" + Math.UUID();
            }
            var bodyWidth = window.screen.availWidth;
            var bodyHeight = window.screen.availHeight;
            var left = 0;
            if ( typeof (width) == 'number' || width.indexOf("%") == -1) {
                var intWidth = parseInt(width);
                left = bodyWidth > intWidth ? (bodyWidth - intWidth) / 2 : 0;
                width += "px";
            } else if (width.indexOf("%") > -1) {
                var decimal = width.substring(0, width.indexOf("%")) / 100;
                width = parseInt(bodyWidth * decimal) + "px";
            } else {
                //设置一个默认值
                width = "800px";
            }
            var top = 0;
            if ( typeof (height) == 'number' || height.indexOf("%") == -1) {
                var intHeight = parseInt(height);
                top = bodyHeight > intHeight ? (bodyHeight - intHeight) / 2 : 0;
                height += "px";
            } else {
                height = bodyHeight + "px";
            }
            window.showModalDialog(url, self, "status:no;dialogHeight:" + height + ";dialogWidth:" + width + ";dialogLeft:" + left + "px;dialogTop:" + top + "px");
        },
        /**
         * 在正中位置打开窗口(非模态)
         * @param url
         * @param title
         * @param height
         * @param width
         * @param closeParent
         * @return
         */
        openNormalWindowInCenter : function(url, title, height, width, closeParent) {
            if (!url) {
                return;
            }
            if (url.indexOf("?") > 0) {
                url = url + "&lrid=" + Math.UUID();
            } else {
                url = url + "?lrid=" + Math.UUID();
            }

            var bodyWidth = window.screen.availWidth - 30;
            var bodyHeight = window.screen.availHeight - 30;
            var left = bodyWidth > width ? (bodyWidth - width) / 2 : 0;
            var top = bodyHeight > height ? (bodyHeight - height) / 2 : 0;
            var win = window.open(url, title, "modal=yes, height=" + height + ", width=" + width + ", left=" + left + ", top=" + top, true);
            if (closeParent) {
                if (win == window)
                    return;
                $.pageutils.closeWinWithNoWarn();
            }
        },
        /**
         * 最大化显示窗口
         * @param url
         * @param title
         * @param closeParent
         * @return
         */
        showMaxWin : function(url, title, closeParent) {
            //showMaxWin('" + url + "', '" + title + "', " + closeParent + ")");
            if (!url) {
                return;
            }
            if (url.indexOf("?") > 0) {
                url = url + "&lrid=" + Math.UUID();
            } else {
                url = url + "?lrid=" + Math.UUID();
            }

            var win = window.open(url, title, 'resizable=no,scrollbars=yes');
            win.moveTo(-4, -4);
            var width = screen.availWidth + 8;
            var height = screen.availHeight + 7;

            win.resizeTo(width, height);
            if (closeParent) {
                if (win == window)
                    return;
                $.pageutils.closeWinWithNoWarn();
            }
        },
        /**
         * 强行关闭窗口
         * @return
         */
        closeWinWithNoWarn : function() {
            var browserName = navigator.appName;

            //		    if ($.browsersupport.IS_IE) {
            //		        window.opener = null;
            //		        window.open('', '_self');
            //		        window.close();
            //		    }
            //		    //if (browserName=="Netscape") {
            //		    else {
            window.open('', '_parent', '');
            window.close();
            //		    }
        },
        /**
         * 打开一个模式窗体
         */
        showWin : function(pageUrl, width, height) {

            var pos = pageUrl.indexOf("?");
            var randId = (Math.UUID() * 10000).toString().substring(0, 4);
            if (pos == -1)
                pageUrl += "?randid=" + randId;
            else
                pageUrl += "&randid=" + randId;

            pageUrl = pageUrl + "&lrid=" + Math.UUID();

            if (width == null || width == "")
                width = parseInt(window.screen.width) - 200;
            if (height == null || height == "")
                height = parseInt(window.screen.height) - 300;
            window.showModalDialog(pageUrl, self, "dialogHeight:" + height + "px;dialogWidth:" + width + "px;center:yes;resizable:yes;status:no");
        },
        /**
         * 显示导向某一页面的ModalDialog,id用来区分要显示的Dialog
         */
        showDialog : function(pageUrl, title, width, height, id, refDiv, attr, twin) {
            if (pageUrl) {
                if (pageUrl.indexOf("?") > 0) {
                    pageUrl = pageUrl + "&lrid=" + Math.UUID();
                } else {
                    pageUrl = pageUrl + "?lrid=" + Math.UUID();
                }
            }
            id = id || "";
            width = width || 400;
            height = height || 300;
            $.pageutils.showDialog.dialogCount = $.pageutils.showDialog.dialogCount || 0;
            $.pageutils.showDialog.dialogsTrueParent = $.pageutils.showDialog.dialogsTrueParent || [];
            var dialogName = "$modalDialog" + $.pageutils.showDialog.dialogCount;
            var nowWidth = $("body").innerWidth();
            var nowHeight = $("body").innerHeight();
            var oriWidth = width;
            var oriHeight = height;
            if (!$.argumentutils.isPercent(width) && width < 0)
                width = nowWidth + width;
            if (!$.argumentutils.isPercent(height) && height < 0)
                height = nowHeight + height;
            if (title == 'null')
                title = null;
            //title=(title=='null') || null;
            //alert(title)
            twin = (twin == null ? window : twin);
            //if ($.pageutils.showDialog.dialogsTrueParent == null) $.pageutils.showDialog.dialogsTrueParent = new Object();
            var topwin = $.pageutils.getLuiTop();
            if (topwin != null && topwin != window) {
                return topwin.$.pageutils.showDialog(pageUrl, title, width, height, id, null, attr, twin);
            }
            if (((!$.argumentutils.isPercent(width) && nowWidth < (width - 40)) || (!$.argumentutils.isPercent(height) && nowHeight < (height - 40))) && (window != top && parent.$.pageutils.showDialog != null)) {
                $.pageutils.showDialog.showInParent = true;
                if (parent.$.pageutils.showDialog.dialogCount == null)
                    parent.$.pageutils.showDialog.dialogCount = 0;
                return parent.$.pageutils.showDialog(pageUrl, title, width, height, id, null, attr, twin);
            } else {
                if (twin.isPopView && twin.isPopView == true)
                    $.pageutils.showDialog.dialogsTrueParent[$.pageutils.showDialog.dialogCount] = twin.$.pageutils.getTrueParent();
                else
                    $.pageutils.showDialog.dialogsTrueParent[$.pageutils.showDialog.dialogCount] = twin;
            }
            var isShowLine = true;
            var isConfirmClose = false;
            if (attr) {
                isShowLine = $.argumentutils.getBoolean(attr.isShowLine, isShowLine);
                isConfirmClose = $.argumentutils.getBoolean(attr.isConfirmClose, isConfirmClose);
            }
            //弹出窗口比最外层页面大时，设置宽或高为100%
            var topWidth = window.document.body.clientWidth;
            var topHeight = window.document.body.clientHeight;
            if (window[dialogName] == null) {
                window[dialogName] = $("<div></div>").appendTo("body").modaldialog({
                    name : 'g_modalDialog',
                    title : title,
                    left : 0,
                    top : 0,
                    width : width,
                    height : height,
                    isShowLine : isShowLine,
                    onafterclose : function() {
                        var oThis = window[dialogName];
                        if (oThis) {
                            var frame = oThis.getContentPane().get(0).firstChild;
                            if (frame) {
                                var id = oThis.dialogId;
                                if (frame && frame.contentWindow.ServerProxy && (frame.contentWindow.getProxyReturnExecuting() > 0 || frame.contentWindow.getProxyArray().length > 0)) {
                                    if ( typeof (id) != 'undefined' && id != null) {
                                        setTimeout("$.pageutils.destroyDialog('" + id + "')", 100);
                                    } else {
                                        setTimeout("$.pageutils.destroyDialog()", 100);
                                    }
                                    return;
                                }
                                $.pageutils.destroyDialog(id);
                            }
                        }

                    },
                    onbeforeclose : function() {
                        if (isConfirmClose) {
                            if (window["$modalDialogFrame" + id] == null || typeof (window["$modalDialogFrame" + id].contentWindow) == "unknown")
                                return;
                            if (window["$modalDialogFrame" + id].contentWindow == null)
                                return;
                            var pageui = window["$modalDialogFrame" + id].contentWindow.pageUI;
                            if (pageui && window[dialogName])
                                return pageui.showCloseConfirm(window[dialogName]);
                        } else {
                            return true;
                        }

                    }
                }).modaldialog('instance');
            } else {
                window[dialogName].setSize(width, height);
                window[dialogName].setTitle(title);
                window[dialogName].showLine(isShowLine);
            }
            window[dialogName].oriHeight = oriHeight;
            window[dialogName].oriWidth = oriWidth;

            var iframe = $("<iframe>").attr({
                'name' : "in_frame",
                'id' : "in_frame",
                'allowTransparency' : "true",
                'frameBorder' : 0
            }).css({
                'width' : "100%",
                'height' : "100%"
            });
            window["$modalDialogFrame" + id] = iframe[0];
            window[dialogName].getContentPane().append(iframe);
            window[dialogName].dialogId = id;
            var reload = false;
            if (pageUrl != null && pageUrl != "")
                iframe.attr('src', pageUrl);
            else {
                if (window.domain_key != null)
                    iframe.attr('src', "/wfw/setdomain.jsp");
            }
            window[dialogName].closeBt.css('visibility', 'hidden');
            window[dialogName].show(refDiv);
            $.pageutils.showDialogColseIcon(id, dialogName);
            $.pageutils.showDialog.dialogCount++;
            return [window[dialogName], iframe[0]];
        },
        destroyDialog : function(id) {
            var dialog = $.pageutils.getDialog(id);
            if (dialog) {
                try {
                    //		            var frame = dialog.getContentPane()[0].firstChild;
                    //		            if (frame.contentWindow.handleClose) {
                    //		                frame.contentWindow.handleClose();
                    //		            }
                    //		            frame.src = "";
                    //		            frame.isDestroy = true;
                    window["$modalDialogFrame" + id] = null;
                    $.pageutils.showDialog.dialogsTrueParent[$.pageutils.showDialog.dialogCount] = null;
                    $.pageutils.showDialog.dialogCount--;
                    var dialogName = "$modalDialog" + $.pageutils.showDialog.dialogCount;
                    dialog.destroy();
                    dialog.element.remove();
                    window[dialogName] = null;
                    //dialog.getContentPane()[0].removeChild(frame);
                } catch(e) {
                    console.error(e);
                };
                dialog = null;
            }
        },
        lazyRender : function() {
            var iframe = $.pageutils.lazyRender.iframe;
            var templateStr = $.pageutils.lazyRender.templateStr;
            try {
                $.pageutils.lazyRender.iframe = null;
                $.pageutils.lazyRender.templateStr = null;
                iframe.contentWindow.document;
            } catch(error) {
                $.pageutils.lazyRender.iframe = iframe;
                $.pageutils.lazyRender.templateStr = templateStr;
                setTimeout("$.pageutils.lazyRender()", 100);
                return;
            }
            iframe.contentWindow.document.write(templateStr);
        },
        /**
         * 显示弹出窗close图标
         * @param {} id
         * @param {} dialogName
         * @param {} count  setTimeout次数，限制在10次
         */
        showDialogColseIcon : function(id, dialogName, count) {
            if (count == null)
                count = 1;
            if (window["$modalDialogFrame" + id] != null) {
                try {
                    window["$modalDialogFrame" + id].contentWindow.renderDone;
                } catch(error) {
                    setTimeout("$.pageutils.showDialogColseIcon('" + id + "', '" + dialogName + "'," + count + ")", 100);
                    return;
                }
                if (window["$modalDialogFrame" + id].contentWindow.renderDone == true || count >= 10)
                    window[dialogName].closeBt.css('visibility', '');
                else {
                    count += 1;
                    setTimeout("$.pageutils.showDialogColseIcon('" + id + "', '" + dialogName + "'," + count + ")", 100);
                }
            } else {
                count += 1;
                setTimeout("$.pageutils.showDialogColseIcon('" + id + "', '" + dialogName + "'," + count + ")", 100);
            }
        },
        /**
         * 隐藏对话框
         * @param id
         * @param clearState
         * @return
         */
        hideDialog : function(id, hideImmediate) {
            //前台直接hideDialog在IE9，FF下会出错
            if ((hideImmediate == null || hideImmediate == false)) {
                if ( typeof (id) != 'undefined' && id != null) {
                    setTimeout("$.pageutils.hideDialog('" + id + "', true)", 100);
                } else {
                    setTimeout("$.pageutils.hideDialog(null, true)", 100);
                }
                return;
            }
            var dialog = $.pageutils.getDialog(id);
            if (dialog) {
                var frame = dialog.getContentPane()[0].firstChild;
                if (frame && frame.contentWindow.ServerProxy && (frame.contentWindow.getProxyReturnExecuting() > 0 || frame.contentWindow.getProxyArray().length > 0)) {
                    if ( typeof (id) != 'undefined' && id != null) {
                        setTimeout("$.pageutils.hideDialog('" + id + "')", 100);
                    } else {
                        setTimeout("$.pageutils.hideDialog()", 100);
                    }
                    return;
                }
                dialog.close();
            }
        },
        /**
         * 获取对话框，如果id为null获取最上层对话框
         *
         * @param {} id
         */
        getDialog : function(id) {
            for (var i = $.pageutils.showDialog.dialogCount - 1; i >= 0; i--) {
                var dialogName = "$modalDialog" + i;
                var dialog = window[dialogName];
                if (dialog == null)
                    continue;
                var frm = null;
                if (dialog.getContentPane)
                    frm = dialog.getContentPane()[0].firstChild;
                if (frm == null || (frm.isDestroy != null && frm.isDestroy == true))
                    continue;
                if ( typeof (id) == 'undefined' || id == null)
                    return dialog;
                if (dialog.dialogId == null)
                    continue;
                if (dialog.dialogId == id)
                    return dialog;
            }
            return null;
        },
        closeWindow : function() {
            if (window.$.serverproxy) {
                setTimeout("$.pageutils.closeWindow()", 100);
                return;
            }
            try {
                window.close();
            } catch(e) {
            }
        },
        /**
         * 获取当前对话框
         * @param isOpen
         * @return
         */
        getCurrentDialog : function(isOpen) {
            var dialogName = null;
            if (isOpen == null || isOpen == false)
                dialogName = "$modalDialog" + ($.pageutils.showDialog.dialogCount);
            else
                dialogName = "$modalDialog" + ($.pageutils.showDialog.dialogCount - 1);
            return window[dialogName];
        },
        /**
         * 获取真正所属父窗口，并非目前显示的父窗口
         */
        getTrueParent : function() {
            if (parent == window) {
                return window;
            }
            if (parent.$.pageutils && parent.$.pageutils.showDialog) {
                if (parent.$.pageutils.showDialog.dialogsTrueParent == null)
                    return parent;
                else {
                    for (var i = parent.$.pageutils.showDialog.dialogCount - 1; i >= 0; i--) {
                        dialogName = "$modalDialog" + i;
                        dialog = parent[dialogName];
                        if (dialog == null)
                            continue;
                        var frame = parent["$modalDialogFrame" + dialog.dialogId];
                        if (frame != null && frame.contentWindow != null && frame.contentWindow == window) {
                            return parent.$.pageutils.showDialog.dialogsTrueParent[i] != null ? parent.$.pageutils.showDialog.dialogsTrueParent[i] : parent;
                        }
                    }
                    return parent;
                }
            } else {
                if (parent.pageUI)
                    return parent;
                else
                    return window;
            }
        },
        /**
         * 带有lfwtop标识的一层父(portal)
         * @return {}
         */
        getLuiTop : function() {
            var parentWin = window.parent;
            try {
                while (parentWin != null && parentWin != window) {
                    if (parentWin.lfwtop) {
                        window.lfwtopwin = parentWin;
                        break;
                    }
                    if (parentWin == parentWin.parent)
                        break;
                    parentWin = parentWin.parent;

                }
            } catch(error) {
            }
            return window.lfwtopwin;
        },
        /**
         * 获取顶端页面
         * @return
         */
        getPopParent : function() {
            if (window.parentWindow != null)
                return window.parentWindow;
            try {
                if (window.parent != window)
                    return $.pageutils.getTrueParent();
                if (window.dialogArguments != null && window.parent.dialogArguments == window.dialogArguments)
                    return window.dialogArguments;
                if (window.opener)
                    return window.opener;
            } catch(error) {
                return null;
            }
        },
        compress : function(content) {
            var top = $.pageutils.getLuiTop();
            if (top == null)
                return null;
            if (top.compressContent) {
                var result = top.compressContent(content);
                return result;
            }
            return null;
        },
        /**
         * 获取APP宿主窗口
         *
         */
        getAppTopWindow : function() {
            //取app自身的最顶层window
            var wid = $.pageutils.getPopParent();
            var appTopWid = wid.parent;
            return appTopWid;
        },
        /**
         * 为文本编辑器所定制的显示对话框的方法
         */
        showCommonDialog : function(pageUrl, title, width, height, id) {
            if (id == null)
                id = "";
            if (!pageUrl) {
                return;
            }
            if (pageUrl.indexOf("?") > 0) {
                pageUrl = pageUrl + "&lrid=" + Math.UUID();
            } else {
                pageUrl = pageUrl + "?lrid=" + Math.UUID();
            }

            var returnValue = showModalDialog(pageUrl, window, "dialogWidth:" + width + ";dialogHeight:" + height + ";status:0;help:0;");
            return returnValue;
        },
        /**
         * 处理Ajax请求的异常信息
         *
         * @return true 没有后台异常，处理成功；false表示不成功，并在异常对话框中显示后台的异常信息
         */
        handleException : function(data, ajaxObj) {
            return $.pageutils.handleExceptionByDoc(data, ajaxObj);
        },
        /**
         * 进入登录页面
         */
        openLoginPage : function() {
            var url = window.globalPath + "/pt/home/index&randid=" + (new Date()).getTime();
            if (window.top != window) {
                var parentPage = parent;
                while (parentPage) {
                    if (parentPage == parentPage.parent)
                        break;
                    parentPage = parentPage.parent;
                }
                if (parentPage)
                    parentPage.location.href = url;
            } else {
                window.location.href = url;
            }
        },
        /**
         * 如果有异常发生，将后台的异常信息显示在异常对话框中，返回false。否则直接返回true
         */
        handleExceptionByDoc : function(doc, ajaxObj) {
            if (doc == null)
                return;
            success = doc.success;
            if (success == "false") {
                var expText = doc["exp-text"];
                var expStack = doc["exp-stack"];
                var showMessage = doc["show-message"] || "error occurred";
                $.pageutils.showErrorDialog(showMessage);
                return false;
            } else if (success == "validator") {
                var expText = doc["exp-text"];
                var expView = doc["exp-view"];
                expNode = doc["exp-components"];
                if (expNode) {
                    var exceptionMsg = null;
                    for (var k = 0; k < expNode.length; k++) {
                        var expComponent = expNode[k];
                        if ( typeof (expView) == "string" && typeof (expComponent.nodeId) == "string") {
                            var widget = pageUI.getViewPart(expView);
                            if (widget) {
                                var component = widget.getComponent(expComponent.nodeId);
                                if (component) {
                                    if (component.componentType == "AUTOFORM") {
                                        if (component.errorMsg && expComponent.errorMsg) {
                                            component.errorMsg.innerHTML = expComponent.errorMsg;
                                            component.setWholeErrorPosition();
                                            component.errorMsgDiv.style.display = "block";
                                        }

                                        var eleArr = component.eleArr;
                                        if (eleArr && eleArr.length > 0) {
                                            var element;
                                            for (var i = 0; i < eleArr.length; i++) {
                                                element = eleArr[i];
                                                if ( typeof (element) == "object") {
                                                    var _elements = expComponent.elements;
                                                    for (var j = 0; j < _elements.length; j++) {
                                                        if (_elements[j].nodeId == element.element.data("eleid")) {
                                                            if ( typeof (element.setError) == 'function') {
                                                                element.setError(true);
                                                            }
                                                            if ( typeof (element.setErrorMessage) == 'function') {
                                                                element.setErrorMessage(_elements[j].errorMsg);
                                                            }
                                                            if ( typeof (element.setErrorStyle) == 'function') {
                                                                element.setErrorStyle();
                                                            }
                                                            if ( typeof (element.setErrorPosition) == 'function') {
                                                                element.setErrorPosition();
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (component.componentType == "GRIDCOMP") {
                                        if (component.errorMsg && expComponent.errorMsg) {
                                            component.errorMsg.innerHTML = expComponent.errorMsg;
                                            component.setWholeErrorPosition();
                                            component.errorMsgDiv.style.display = "block";
                                        }

                                        var eleArr = component.basicHeaders;
                                        if (eleArr && eleArr.length > 0) {
                                            var cell = null;
                                            for (var i = 0; i < eleArr.length; i++) {
                                                var _elements = expComponent.elements;
                                                for (var j = 0; j < _elements.length; j++) {
                                                    if (_elements[j].id.split("_")[0] == eleArr[i].keyName) {
                                                        if (eleArr[i].dataDiv.cells.length == 1) {
                                                            cell = eleArr[i].dataDiv.cells[0];
                                                        } else {
                                                            if (_elements[j].id.split("_").length > 1) {
                                                                cell = eleArr[i].dataDiv.cells[_elements[j].id.split("_")[1]];
                                                            } else {
                                                                cell = eleArr[i].dataDiv.cells[0];
                                                            }
                                                        }
                                                        //cell.errorMsg = expComponent.elements[j].errorMsg;//grid输入框错误提示显示位置有误，暂时注掉。
                                                        var warningIcon = cell.warningIcon;
                                                        if ( typeof (warningIcon) == 'undefined') {
                                                            var _warningIcon = $("<DIV>").addClass("cellwarning");
                                                            warningIcon = _warningIcon[0];
                                                            cell.warningIcon = warningIcon;
                                                            cell.style.position = "relative";
                                                        }
                                                        cell.appendChild(warningIcon);
                                                        if ( typeof (cell.errorMsg) == "string" && cell.errorMsg != "") {
                                                            warningIcon.style.display = "block";
                                                        } else {
                                                            warningIcon.style.display = "none";
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {// if(component.componentType == "TEXT")
                                        var element = component;
                                        if ( typeof (element.setError) == 'function') {
                                            element.setError(true);
                                        }
                                        if ( typeof (element.setErrorMessage) == 'function') {
                                            element.setErrorMessage(trans("ml_thisfieldcannotnull"));
                                        }
                                        if ( typeof (element.setErrorStyle) == 'function') {
                                            element.setErrorStyle();
                                        }
                                        if ( typeof (element.setErrorPosition) == 'function') {
                                            element.setErrorPosition();
                                            element.noShowErrorMsgDiv = true;
                                        }
                                        if (expComponent.errorMsg) {
                                            exceptionMsg = expComponent.errorMsg;
                                        }
                                    }
                                } else {
                                    //$.pageutils.showErrorDialog('当前Component:'+expComponent.id+'不存在!');
                                }
                            } else {
                                //$.pageutils.showErrorDialog('当前View:'+expView.id+'不存在!');
                            }
                        }
                    }
                    if ( typeof (exceptionMsg) == "string") {
                        $.pageutils.showErrorDialog(exceptionMsg);
                    }
                }
                return false;
            } else if (success == "interaction") {
                var contentNodes = doc["contents"];
                var content = contentNodes[0];
               // content = decodeURIComponent(content);
                eval("var interationInfo = content" );
                $.pageutils.rePostReq.dialogId = interationInfo.id;
                if (interationInfo.type == "OKCANCEL_DIALOG") {
                    var msg = interationInfo.msg;
                    var title = interationInfo.title;
                    var okText = interationInfo.okText;
                    var cancelText = interationInfo.cancelText;
                    $.pageutils.rePostReq.ajaxObj = ajaxObj.clone();
                    $.pageutils.showConfirmDialog(msg, $.pageutils.rePostOk, $.pageutils.rePostCancel, null, null, null, okText, cancelText, title);
                } else if (interationInfo.type == "THREE_BUTTONS_DIALOG") {
                    var msg = interationInfo.msg;
                    var title = interationInfo.title;
                    $.pageutils.rePostReq.ajaxObj = ajaxObj.clone();
                    var topWin = $.pageutils.getLuiTop();
                    if (topWin == null)
                        topWin = $.pageutils.getTrueParent();
                    topWin.require("threebuttondialog", function() {
                        topWin.ThreeButtonsDialog.showDialog(msg, $.pageutils.rePostOk, $.pageutils.rePostCancel, $.pageutils.rePostMiddle, interationInfo.btnTexts, null, null, null, null, null, title);
                    });
                } else if (interationInfo.type == "MESSAGE_DIALOG") {
                    var msg = interationInfo.msg;
                    var title = interationInfo.title;
                    var btnText = interationInfo.btnText;
                    $.pageutils.rePostReq.ajaxObj = ajaxObj.clone();
                    $.pageutils.rePostReq.okReturn = interationInfo.okReturn;
                    $.pageutils.showMessageDialog(msg, $.pageutils.rePostOk, title, btnText, false);
                } else if (interationInfo.type == "ERROR_MESSAGE_DIALOG") {
                    var msg = interationInfo.msg;
                    var title = interationInfo.title;
                    var btnText = interationInfo.btnText;
                    $.pageutils.rePostReq.ajaxObj = ajaxObj.clone();
                    $.pageutils.rePostReq.okReturn = interationInfo.okReturn;
                    $.pageutils.showErrorDialog(msg, $.pageutils.rePostOk, title, btnText);
                } else if (interationInfo.type == "INPUT_DIALOG") {
                    var items = interationInfo.items;
                    if (items != null) {
                        var title = interationInfo.title;
                        var id = $.pageutils.rePostReq.dialogId;
                        ajaxObj = ajaxObj.clone();
                        ajaxObj.addParam(id + "interactflag", "true");
                        $.pageutils.rePostReq.ajaxObj = ajaxObj;
                        $.pageutils.noPostCancel.ajaxObj = ajaxObj;
                        var inputDlg = $("<div></div>").inputdialog({
                            name : 'input_dialog',
                            title : title,
                            okFunc : $.pageutils.rePostReq,
                            cancelFunc : $.pageutils.noPostCancel
                        }).inputdialog('instance');
                        $.pageutils.rePostReq.inputDlg = inputDlg;
                        for (var i = 0; i < items.length; i++) {
                            var item = items[i];
                            var inputId = item.inputId;
                            var labelText = item.labelText;
                            var inputType = item.inputType;
                            var required = item.required;
                            var value = item.value;
                            if (inputType == "string" || inputType == "pswtext") {
                                inputDlg.addItem(labelText, inputId, inputType, required, null, value);
                            } else if (inputType == "int") {
                                var attr = new Object;
                                attr.minValue = item.minValue;
                                attr.maxValue = item.maxValue;
                                inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
                            } else if (inputType == "combo") {
                                var datas = item.comboData.allDataItems;
                                var comboData = $.datalist.getObj();
                                var attr = new Object;
                                attr.selectOnly = item.selectOnly;
                                var combData = $.datalist.getObj();
                                for (var j = 0; j < datas.length; j++) {
                                    var text = datas[j].text;
                                    if (text == null)
                                        text = datas[j].i18nName;
                                    combData.addItem($.dataitem.getObj(text, datas[j].value));
                                }
                                attr.comboData = combData;
                                inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
                            } else if (inputType == "radio") {
                                var datas = item.comboData.allDataItems;
                                var comboData = $.datalist.getObj();
                                var attr = new Object;
                                attr.selectOnly = item.selectOnly;
                                var combData = $.datalist.getObj();
                                for (var j = 0; j < datas.length; j++) {
                                    var text = datas[j].text;
                                    if (text == null)
                                        text = datas[j].i18nName;
                                    combData.addItem($.dataitem.getObj(text, datas[j].value));
                                }
                                attr.comboData = combData;
                                inputDlg.addItem(labelText, inputId, inputType, required, attr, value);
                            }
                        }
                        inputDlg.show();
                    }
                }
                return false;
            }
            return true;
        },
        /**
         * 点OK后执行方法
         */
        rePostOk : function() {
            if ($.pageutils.rePostReq.okReturn == null || $.pageutils.rePostReq.okReturn == true) {
            	
                var ajaxObj = $.pageutils.rePostReq.ajaxObj;
                var id = $.pageutils.rePostReq.dialogId;
                ajaxObj.addParam(id + "interactflag", "true");
                showDefaultLoadingBar();
                var ajaxDefferd= ajaxObj.post();
		        ajaxDefferd.done(function(jsonData) {
		            $.serverproxy.returnSuccessFun(jsonData, [[]], ajaxDefferd.ref);
		        }).fail(function(xhr, textStatus, errorThrown) {
		            $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
		        });
	        }
            $.pageutils.rePostReq.okReturn = null;
        },
        rePostMiddle : function() {
            if ($.pageutils.rePostReq.okReturn == null || $.pageutils.rePostReq.okReturn == true) {
                var ajaxObj = $.pageutils.rePostReq.ajaxObj;
                var id = $.pageutils.rePostReq.dialogId;
                ajaxObj.addParam(id + "interactflag", "middle");
                showDefaultLoadingBar();
                var ajaxDefferd= ajaxObj.post();
			    ajaxDefferd.done(function(jsonData) {
			        $.serverproxy.returnSuccessFun(jsonData, [[]], ajaxDefferd.ref);
			    }).fail(function(xhr, textStatus, errorThrown) {
			        $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
			    });
            }
            $.pageutils.rePostReq.okReturn = null;
        },
        /**
         * 点CANCEL后执行方法
         */
        rePostCancel : function() {
            var ajaxObj = $.pageutils.rePostReq.ajaxObj;
            var id = $.pageutils.rePostReq.dialogId;
            ajaxObj.addParam(id + "interactflag", "false");
            showDefaultLoadingBar();
			var ajaxDefferd= ajaxObj.post();
		    ajaxDefferd.done(function(jsonData) {
		        $.serverproxy.returnSuccessFun(jsonData, [[]], ajaxDefferd.ref);
		    }).fail(function(xhr, textStatus, errorThrown) {
		        $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
		    });
        },
        /**
         * 消息框关闭时不发请求，需要调整 pageUI.PROXYRETURN_EXECUTING
         */
        noPostCancel : function() {
            var ajaxObj = $.pageutils.noPostCancel.ajaxObj;
            //从请求队列中删除当前请求id
            if (ajaxObj != null && ajaxObj.req_id != null) {
                for ( i = 0; i < Ajax.REQ_ARRAY.length; i++) {
                    if (Ajax.REQ_ARRAY[i] == ajaxObj.req_id) {
                        Ajax.REQ_ARRAY.splice(i, 1);
                        break;
                    }
                }
                ajaxObj.destroySelf();
            }
        },
        rePostReq : function() {
            var ajaxObj = $.pageutils.rePostReq.ajaxObj;
            if ($.pageutils.rePostReq.inputDlg) {
                var dlg = $.pageutils.rePostReq.inputDlg;
                var itemsMap = dlg.getItems();
                var resultStr = "";

                var keySet = itemsMap.keySet();
                for (var i = 0, count = keySet.length; i < count; i++) {
                    var inputId = keySet[i];
                    var inputComp = itemsMap.get(inputId);
                    resultStr += inputId + "=" + inputComp.getValue();
                    if (i != count - 1)
                        resultStr += ",";
                }

                ajaxObj.addParam("interactresult", resultStr);
                var key = $.pageutils.rePostReq.dialogId + "interactresult";
                ajaxObj.addParam(key, resultStr);
                $.pageutils.rePostReq.inputDlg = null;
            }
            showDefaultLoadingBar();
             var ajaxDefferd= ajaxObj.post();
	        ajaxDefferd.done(function(jsonData) {
	            $.serverproxy.returnSuccessFun(jsonData, [[]], ajaxDefferd.ref);
	        }).fail(function(xhr, textStatus, errorThrown) {
	            $.serverproxy.returnFailFun(xhr, textStatus, errorThrown);
	        });
        },
        /**
         * 隐藏异常对话框
         */
        hideExceptionDialog : function() {
            ExceptionDialog.hideDialog();
        },
        /**
         * 显示信息对话框
         *
         * @param func 点击对话框确认按钮要执行的函数
         */
        showMessageDialog : function(msg, func, title, okBtnText, isShowSec) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            topWin.$.messageDialogComp.showDialog(msg, title, okBtnText, func, isShowSec);
        },
        /**
         * 显示反馈信息对话框
         * @param title
         * @param width
         * @return
         */
        showMessage : function(title) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            topWin.$.messageComp.showMessage("center", title, "0", "30", "");
        },
        /**
         * 显示警告对话框
         */
        showWarningDialog : function(msg, func) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            var dialog = topWin.$.warningDialogComp.showDialog(msg);
            if (func != null)
                dialog.onclick = func;
            return dialog;
        },
        /**
         * 隐藏警告对话框
         */
        hideWarningDialog : function() {
            $.warningDialogComp.hideDialog();
        },
        /**
         * 显示确认对话框
         */
        showConfirmDialog : function(msg, okFunc, cancelFunc, obj1, obj2, zIndex, okText, cancelText, title) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            topWin.$.confirmDialogComp.showDialog(msg, okFunc, cancelFunc, obj1, obj2, null, null, okText, cancelText, title);
        },
        /**
         * 显示三个按钮的确认对话框
         */
        showThreeButtonConfirmDialog : function(msg, rePostOk, rePostCancel, rePostMiddle, btnTexts, obj1, obj2, obj3, zIndex, another, title) {
            var topWin = $.pageutils.getLuiTop();
            if (topWin == null)
                topWin = $.pageutils.getTrueParent();
            topWin.$.threeButtonsDialog.showDialog(msg, rePostOk, rePostCancel, rePostMiddle, btnTexts, obj1, obj2, obj3, zIndex, another, title);
        },
        /**
         * 隐藏确认对话框
         */
        hideConfirmDialog : function() {
            $.confirmDialogComp.hideDialog();
        },
        /**
         * 获得节点值
         */
        getNodeValue : function(node) {
            var firstNode = node.firstChild;
            if (firstNode == null)
                return null;
            var nextSibling = firstNode.nextSibling;
            if (nextSibling == null)
                return firstNode.data;
            return nextSibling.data;
        },
        /**
         * 获取节点属性
         * @param node
         * @param attrName
         * @return
         */
        getNodeAttribute : function(node, attrName) {
            //		    if ($.browsersupport.IS_IE) return node.getAttribute(attrName);
            var attrs = node.attributes;
            if (attrs == null)
                return null;
            for (var i = 0; i < attrs.length; i++) {
                if (attrs[i].nodeName == attrName)
                    return attrs[i].nodeValue;
            }
            return null;
        },
        /**
         * 从数组中删除子项
         * @param arr
         * @param ele
         * @return
         */
        removeFromArray : function(arr, ele) {
            if (!arr)
                return false;
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == ele) {
                    arr.splice(i, 1);
                    return true;
                }
            }
            return false;
        },
        /**
         * 显示状态信息
         * @param msg
         * @return
         */
        showStatusMsg : function(msg) {
            window.status = msg;
        },
        /**
         * 根据key获取用户配置属性
         */
        getConfigAttribute : function(key) {
            var value = $.pageutils.getConfigFromCookieById(key);
            if (value != null)
                return value;
            else
                return $.pageutils.getSessionAttribute(key);
        },
        /**
         * 从Cookie中获取配置信息
         * @param key
         * @return
         */
        getConfigFromCookieById : function(key) {
            var allCookie = document.cookie;
            var pos = allCookie.indexOf("LFW_CONFIG_KEY=");
            if (pos != -1) {
                var start = pos + 15;
                var end = allCookie.indexOf(";", start);
                if (end == -1)
                    end = allCookie.length;
                var value = allCookie.substring(start, end);
                var v = value.split("$");
                if (key == "connectServerCycle")
                    return v[0];
                else if (key == "theme")
                    return v[1];
                else if (key == "openNodeMode")
                    return v[2];
                else if (key == "noticeRefreshCycle")
                    return v[3];
                else if (key == "jobRefreshCycle")
                    return v[4];
            } else
                return null;
        },
        /**
         * document对象有个cookie特性，是包含给定页面所有可访问cookie的字符串。Cookie特性也很特别，因为将这个cookie特性
         * 设置为新值只会警告对页面可访问的cookie，并不会真正改变cookie(特性)本身。即使制定了cookie的其他特性，如失效时间，
         * document.cookie也只是返回每个cookie的名称和值，并用分号来分隔这些cookie
         */
        /**
         * 设置Cookie
         * @param sName cookie名称,cookie名称本来不区分大小写，但最好认为区分
         * @param sValue 保存在cookie中的值，这个值在存储之前必须用encodeURIComponent编码,以免丢失数据或占用了Cookie.
         *               名称和值加起来不能超过4095字节,4K
         * @param sDomain 域，出于安全考虑，网站不能访问由其他域创建的cookie。创建cookie后，域的信息会作为cookie的一部分
         *                存储起来，不过，虽然不常见，但还是可以覆盖这个设置以允许另一个网站访问这个cookie
         * @param sPath 路径，另一个cookie的安全特征，路径限制了对Web服务器上的特定目录的访问。
         * @param{Object} oExpires Date对象
         * @param bSecure 一个true/false值，用于表示cookie是否只能从安全网站(使用SSL和https协议的网站)中访问。可将这个值设为
         *                true以提供加强的保护，进而确保cookie不被其他网站访问
         */
        setCookie : function(sName, sValue, oExpires, sPath, sDomain, bSecure) {
            var sCookie = sName + "=" + encodeURIComponent(sValue);
            if (oExpires)
                sCookie += "; expires=" + oExpires.toGMTString();
            if (sPath)
                sCookie += "; path=" + sPath;
            if (sDomain)
                sCookie += "; domain=" + sDomain;
            if (bSecure)
                sCookie += "; secure=" + bSecure;
            document.cookie = sCookie;
        },
        /**
         * 获取Cookie
         * @param sName
         * @return
         */
        getCookie : function(sName) {
            var sRE = "(?:; )?" + sName + "=([^;]*);?";
            var oRE = new RegExp(sRE);

            if (oRE.test(document.cookie)) {
                return decodeURIComponent(RegExp["$1"]);
            } else
                return null;
        },
        /**
         * 删除Cookie
         * @param sName
         * @param sPath
         * @param sDomain
         * @return
         */
        deleteCookie : function(sName, sPath, sDomain) {
            $.pageutils.setCookie(sName, "", new Date(0), sPath, sDomain);
        },
        /**
         * 上传成功后执行方法
         * @param data
         * @param targetComp
         * @return
         * @private
         */
        uploadSuccess : function(data, targetComp) {
            // alert("上传成功");
            var comp = getComponent(targetComp);
            comp.onUploaded(data);
        },
        /**
         * 从缓存中获取内容
         * @param key
         * @return
         */
        getFromCache : function(key) {
            return window.globalObject[key];
        },
        /**
         * 向缓存中存入内容
         * @param key
         * @param value
         * @return
         */
        putToCache : function(key, value) {
            window.globalObject[key] = value;
        },
        /**
         * 从页面删除一个控件并销毁所占资源
         */
        removeComponent : function(compId) {
            var comp = window["$c_" + compId];
            if (comp) {
                comp.destroySelf();
                window["$c_" + compId] = null;
            }
        },
        /**
         * 从删除页面所有控件并销毁所占资源
         */
        removeAllComponent : function() {
            for (var i = 0; i < window.clickHolders.length; i++) {
                window.clickHolders[i] = null;
            }

            if (window.pageUI) {
                pageUI.destroySelf();
            }

            for (var i in window.objects) {
                var comp = window.objects[i];
                if (comp && comp.destroySelf) {
                    comp.destroySelf();
                }
                comp = null;
            }
            window.objects = null;
        },
        clearNodeProperties : function(node) {
            for (var i in node) {
                try {
                    node[i] = null;
                } catch(error) {
                }
            }
        },
        clearHtmlNodeProperties : function(node) {
            if (node != null) {
                var nodeName = node.nodeName;
                if (nodeName == "IMG" || nodeName == "img") {
                    return;
                }
                try {
                    node.onclick = null;
                    node.onmouseover = null;
                    node.keypress = null;
                    node.onfocus = null;
                    node.onblur = null;
                    node.owner = null;
                } catch(error) {
                }
            }
        },
        /**
         * 注册样式表
         * @param {} cssString
         */
        addCssByStyle : function(cssString) {
            var doc = document;
            var style = doc.createElement("style");
            style.setAttribute("type", "text/css");
            if (style.styleSheet) {// IE
                style.styleSheet.cssText = cssString;
            } else {// w3c
                var cssText = doc.createTextNode(cssString);
                style.appendChild(cssText);
            }
            var heads = doc.getElementsByTagName("head");
            if (heads.length)
                heads[0].appendChild(style);
            else
                doc.documentElement.appendChild(style);
        },
        /**
         * 调整容器Frame高度
         */
        adjustContainerFramesHeight : function() {
            try {
                if (document._pt_frame_id) {
                    var frame = parent.getParentsContainer(document._pt_frame_id);
                    parent.adjustIFramesHeightOnLoad(frame);
                } else if (parent.document._pt_frame_id) {
                    var frame = parent.parent.getParentsContainer(parent.document._pt_frame_id);
                    parent.parent.adjustIFramesHeightOnLoad(frame);
                } else if (window.opener) {
                    if (window.opener.document._pt_frame_id) {
                        var frame = window.opener.parent.getParentsContainer(window.opener.document._pt_frame_id);
                        window.opener.parent.adjustIFramesHeightOnLoad(frame);
                    }
                }
            } catch(e) {
            }
        },
        restoreContainerFramesHeight : function() {
            try {
                if (document._pt_frame_id) {
                    parent.initFrameMiniHeight(document._pt_frame_id);
                } else if (parent.document._pt_frame_id) {
                    parent.parent.initFrameMiniHeight(parent.document._pt_frame_id);
                } else if (window.opener) {
                    if (window.opener.document._pt_frame_id) {
                        window.opener.parent.initFrameMiniHeight(window.opener.document._pt_frame_id);
                    }
                }
            } catch(e) {
            }
        },
        uploadedExcelFile : function(result) {
            var proxy = $.serverproxy.getObj({
                async : false
            });
            var results = result.split(",");
            proxy.addParam('clc', results[0]);
            var method = results[3];
            if (method == null || method == "")
                method = 'onUploadedExcelFile';
            proxy.addParam({
                'm_n' : method,
                'widget_id' : results[2],
                'el' : '2',
                "excel_imp_path" : results[1]
            });
            proxy.execute();
        },
        sysDownloadFile : function(url) {
            if (window.sys_DownFileFrame == null) {
                var frm = $('<iframe>').attr({
                    frameborder : 0,
                    vspace : 0,
                    hspace : 0
                }).css({
                    width : '1px',
                    height : '0px',
                    display : 'none'
                });
                window.sys_DownFileFrame = frm;
                $("body").append(window.sys_DownFileFrame);
            }
            window.sys_DownFileFrame.attr('src', url);
        },
        /**
         * 设置透明度
         * @private
         */
        _setOpacity : function(obj, value) {
            if (document.all) {
                if (value == 100) {
                    obj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + value + ")";
                } else {
                    obj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + value + ")";
                }
            } else {
                obj.style.opacity = value / 100;
            }
        },
        /**
         *触发plugout
         *
         */
        triggerPlugout : function(widgetId, plugoutId) {
            var plugout = pageUI.getViewPart(widgetId).getPlugOut(plugoutId);
            if (plugout == null)
                return;
            var proxy = $.serverproxy.getObj();
            proxy.addParam("widget_id", widgetId);
            if (plugout.submitRule != null)
                proxy.submitRule = plugout.submitRule;
            proxy.execute();
        }
    };

    window.parentWindow = $.pageutils.getPopParent();
    $(window).on("unload", function() {
        $.pageutils.winunload();
    });
})(jQuery);
