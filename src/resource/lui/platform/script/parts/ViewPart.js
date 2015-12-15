(function($) {
    /**
     * 页面片段构造方法
     * @class 页面Widget的类
     */
    $.viewpart = function(opts) {
        this.element = $(this);
        this.id = opts.id;
        this.comps = $.hashmap.getObj();
        this.dss = $.hashmap.getObj();
        this.combodatas = $.hashmap.getObj();
        this.refnodes = $.hashmap.getObj();
        // dsRelation集合
        this.dsRelations = null;
        this.menuMap = $.hashmap.getObj();
        this.cardMap = $.hashmap.getObj();
        this.tabMap = $.hashmap.getObj();
        this.panelMap = $.hashmap.getObj();
        this.outlookMap = $.hashmap.getObj();
        this.splitMap = $.hashmap.getObj();

        this.plugOutMap = $.hashmap.getObj();

        this.visible = opts.visible;
        this.dialog = opts.dialog;
        this.pageState = null;
        this.operateState = null;
    };

    $.viewpart.getObj = function(opts) {
        return new $.viewpart(opts);
    };

    $.extend($.viewpart.prototype, {
        /**
         * 增加split
         */
        addSplit : function(split) {
            this.splitMap.put(split.id, split);
        },
        /**
         * 获取split
         */
        getSplit : function(id) {
            return this.splitMap.get(id);
        },
        /**
         * 增加Tab
         */
        addTab : function(tab) {
            this.tabMap.put(tab.options.name, tab);
        },
        /**
         * 获取Tab
         */
        getTab : function(id) {
            return this.tabMap.get(id);
        },
        removeTab : function(id) {
        	this.tabMap.remove(id);
        },
        /**
         * 增加Card
         */
        addCard : function(card) {
            this.cardMap.put(card.id, card);
        },
        /**
         * 获取Card
         */
        getCard : function(id) {
            return this.cardMap.get(id);
        },
        removeCard : function(id) {
        	this.cardMap.remove(id);
        },
        /**
         * 获取Panel
         */
        getPanel : function(id) {
            return this.panelMap.get(id);
        },
        /**
         * 获取Panel
         */
        getPanels : function(id) {
            return this.panelMap.values();
        },
        /**
         * 增加Panel
         */
        addPanel : function(panel) {
            this.panelMap.put(panel.id, panel);
        },
        /**
         * 增加Outlook
         */
        addOutlook : function(outlook) {
            this.outlookMap.put(outlook.id, outlook);
        },
        /**
         * 获取Outlook
         */
        getOutlook : function(id) {
            return this.outlookMap.get(id);
        },
        removeOutlook : function(id) {
        	this.outlookMap.remove(id);
        },
        removePanel : function(id) {
            this.panelMap.remove(id);
        },
        /**
         * 获取PlugOut
         */
        getPlugOut : function(id) {
            return this.plugOutMap.get(id);
        },
        /**
         * 增加PlugOut
         */
        addPlugOut : function(plugout) {
            this.plugOutMap.put(plugout.id, plugout);
        },
        /**
         * 增加菜单
         */
        addMenu : function(menu) {
            this.menuMap.put(menu.id, menu);
        },
        /**
         * 获取所有菜单
         */
        getMenus : function() {
            return this.menuMap.values();
        },
        /**
         * 获取菜单
         */
        getMenu : function(id) {
            return this.menuMap.get(id);
        },
        /**
         * 设置可见性
         */
        setVisible : function(visible) {
            if (this.visible != visible) {
                this.visible = visible;
                if (this.dialog) {
                    if (visible) {
                        if (this.onBeforeShow() == false) {
                            this.visible = !this.visible;
                            return;
                        }
                        pageUI.getDialog(this.id).show();
                    } else {
                        var result = pageUI.getDialog(this.id).close();
                        if (result == null || result == false)
                            this.visible = !this.visible;
                    }
                }
            }

            var oThis = this;
            pageUI.getDialog(this.id).element.on('modaldialogonafterclose', function() {
                oThis.visible = false;
            });

            if (this.lazyInit) {
                this.lazyInit();
                this.lazyInit = null;
                this.$beforeInitData();
            }
        },
        onBeforeShow : function() {
            if (pageUI.renderDone) {
                var simpleEvent = {
                    "obj" : this
                };
                this.element.triggerHandler("beforeshow", simpleEvent);
            }
        },
        /**
         * 页面关闭后事件
         * @private
         */
        onclose : function() {
            this.element.triggerHandler("onclose");
        },
        /**
         * 更新全部的按钮状态
         * @private
         */
        updateButtons : function() {
            var activedMenubars = this.getActivedMenubars();
            if (activedMenubars != null && activedMenubars.length > 0) {
                for (var n = 0, count = activedMenubars.length; n < count; n++) {
                    var menubar = activedMenubars[n];
                    var menubarItems = menubar.menuItems;

                    if (menubarItems != null) {
                        var menuItems = menubarItems.values();
                        for (var i = 0, count1 = menuItems.length; i < count1; i++) {
                            if (menuItems[i].contextMenu != null) {
                                var cItems = menuItems[i].contextMenu.childItems;
                                for (var j = 0; j < cItems.length; j++)
                                    menuItems.push(cItems[j]);
                            }
                        }

                        //特殊处理状态
                        var userMap = this.getUserStateMap();
                        if (menuItems != null && menuItems.length > 0) {
                            for (var i = 0; i < menuItems.length; i++) {
                                menuItems[i].updateState(this.operateState, this.businessState, this.userState);
                            }
                        }
                    }
                }
            }
        },
        /**
         * 自定义按钮状态(用户指定)
         * @private
         */
        getUserStateMap : function() {
        },
        destroySelf : function() {
            var dss = this.getDatasets();
            for (var i = 0; i < dss.length; i++) {
                var ds = this.removeDataset(dss[i].id);
                ds.destroySelf();
            }
            var comps = this.getComponents();
            for (var i = 0; i < comps.length; i++) {
                var comp = this.removeComponent(comps[i].id ? comps[i].id : comps[i].element.attr('id'));
                if(comp){
                	comp.destroySelf();
                }
            }

            var panels = this.getPanels();
            for (var i = 0; i < panels.length; i++) {
                var panel = panels[i];
                panel.destroySelf();
            }

            var menus = this.getMenus();
            for (var i = 0; i < menus.length; i++) {
                var menu = menus[i];
                menu.destroySelf();
            }

            //this.clearListenerMap();
            this.comps.clear();
            this.comps = null;
            this.dss.clear();
            this.dss = null;
            this.combodatas.clear();
            this.combodatas = null;
            this.refnodes.clear();
            this.refnodes = null;
            if (this.dsRelations != null) {
                this.dsRelations.destroySelf();
                this.dsRelations = null;
            }
            this.menuMap.clear();
            this.menuMap = null;
            this.cardMap.clear();
            this.cardMap = null;
            this.tabMap.clear();
            this.tabMap = null;
            this.panelMap.clear();
            this.panelMap = null;
            this.outlookMap.clear();
            this.outlookMap = null;
            this.splitMap.clear();
            this.splitMap = null;
            this.plugOutMap.clear();
            this.plugOutMap = null;
            for (var i in this) {
                this[i] = null;
            }
        },
        /**
         * 获取当前激活的menubargroup中的menubar和单独显示的menubar
         */
        getActivedMenubars : function() {
            var menubars = [];
            // 当前可见的menubar
            var menus = this.menuMap.values();
            for (var i = 0, count = menus.length; i < count; i++) {
                menubars.push(menus[i]);
            }
            return menubars;
        },
        getContext : function(viewPartRule) {
            var viewCtxJson = {
                context : {
                    "c" : "WidgetUIContext",
                    "visible" : this.visible
                }
            };
            var compObjs = this.comps.values();
            if (compObjs && compObjs.length > 0) {
                var componetCtxJsons = [];
                for (var i = 0; i < compObjs.length; i++) {
                    var innnerComp = compObjs[i];
                    var compType = null;
                    if (innnerComp.componentType) {
                        compType = innnerComp.componentType.toLowerCase();
                    }

                    var ruleType = null;
                    if ( typeof TreeViewComp != "undefined" && innnerComp.componentType == TreeViewComp.prototype.componentType) {
                        var rule = viewPartRule == null ? null : viewPartRule.getTreeRule(innnerComp.id);
                        if (rule != null)
                            ruleType = rule.type;
                    }
                    if ( typeof AutoFormComp != "undefined" && innnerComp.componentType == AutoFormComp.prototype.componentType) {
                        var rule = viewPartRule == null ? null : viewPartRule.getFormRule(innnerComp.id);
                        if (rule != null)
                            ruleType = rule.type;
                    }
                    if ( typeof GridComp != "undefined" && innnerComp.componentType == GridComp.prototype.componentType) {
                        var rule = viewPartRule == null ? null : viewPartRule.getGridRule(innnerComp.id);
                        if (rule != null)
                            ruleType = rule.type;
                    }
                    var innerCtx = innnerComp.getContext(ruleType);
                    if (innerCtx == null)
                        continue;
                    innerCtx.id = innnerComp.id || innnerComp.element.attr('id');
                    innerCtx.compType = compType;
                    componetCtxJsons.push(innerCtx);
                }
                if (componetCtxJsons.length > 0) {
                    viewCtxJson["componets"] = componetCtxJsons;
                }
            }
            var datasetObjs = this.dss.values();
            if (datasetObjs && datasetObjs.length > 0) {
                var datasetCtxJsons = [];
                for (var i = 0; i < datasetObjs.length; i++) {
                    var innerDs = datasetObjs[i];
                    var dsRule = null;
                    var type = null;
                    if (viewPartRule) {
                        dsRule = viewPartRule.getDsRule(innerDs.id);
                        if (dsRule)
                            type = dsRule.type;
                    }
                    var dsContent = $.dataset.searializeDataset(innerDs, type);
                    dsContent.id = innerDs.id;
                    datasetCtxJsons.push(dsContent);
                }
                if (datasetCtxJsons.length > 0) {
                    viewCtxJson["datasets"] = datasetCtxJsons;
                }
            }
            var menuObjs = this.menuMap.values();
            if (menuObjs && menuObjs.length > 0) {
                var menuCtxJsons = [];
                for (var i = 0; i < menuObjs.length; i++) {
                    var menu = menuObjs[i];
                    var innerCtx = menu.getContext();
                    if (innerCtx == null)
                        continue;
                    innerCtx.id = menu.id;
                    innerCtx.compType = menu.componentType;
                    menuCtxJsons.push(innerCtx);
                }
                if (menuCtxJsons.length > 0) {
                    viewCtxJson["menus"] = menuCtxJsons;
                }
            }
            
            var tabObjs = this.tabMap.values();
            if (tabObjs && tabObjs.length > 0) {
                var tabCtxJsons = [];
                for (var i = 0; i < tabObjs.length; i++) {
                    var innerCtx = tabObjs[i].getContext();
                    innerCtx.id = tabObjs[i].id;
                    tabCtxJsons.push(innerCtx);
                }
                viewCtxJson["tabcomps"] = tabCtxJsons;
            }

            if (viewPartRule && viewPartRule.getCardSubmit()) {
                var cardObjs = this.cardMap.values();
                if (cardObjs && cardObjs.length > 0) {
                    var cardCtxJsons = [];
                    for (var i = 0; i < cardObjs.length; i++) {
                        var innerCtx = cardObjs[i].getContext();
                        innerCtx.id = cardObjs[i].id;
                        cardCtxJsons.push(innerCtx);
                    }
                    if (viewCtxJson.length > 0) {
                        viewCtxJson["cards"] = cardCtxJsons;
                    }
                }
            }

            if (viewPartRule && viewPartRule.getPanelSubmit()) {
                var panelObjs = this.panelMap.values();
                if (panelObjs && panelObjs.length > 0) {
                    var panelCtxJsons = [];
                    for (var i = 0; i < panelObjs.length; i++) {
                        var innerCtx = panelObjs[i].getContext();
                        innerCtx.id = panelObjs[i].id;
                        panelCtxJsons.push(innerCtx);
                    }
                    if (panelCtxJsons.length > 0) {
                        viewCtxJson["panels"] = panelCtxJsons;
                    }
                }
            }
            return viewCtxJson;
        },
        /**
         * @private
         */
        setContext : function(context) {
            this.setVisible(context.visible);
            var currOperateState = context.cos;
            var currBusiState = context.cbs;
            var currUserState = context.cus;
            var isUpdBtns = false;
            if (currOperateState != this.operateState) {
                this.setOperateState(currOperateState);
                isUpdBtns = true;
            }
            if (currBusiState != this.businessState) {
                isUpdBtns = true;
                this.setBusinessState(currBusiState);
            }
            if (currUserState != this.userState) {
                isUpdBtns = true;
                this.setUserState(currUserState);
            }
            if (isUpdBtns == true)
                this.updateButtons();
        },
        recordUndo : function() {
            this.oldOperateState = this.operateState;
            this.oldBusinessState = this.businessState;
            this.oldUserState = this.userState;
        },
        undo : function() {
            var isUpdBtns = false;
            if (this.oldOperateState != null && this.oldOperateState != this.operateState) {
                this.setOperateState(this.oldOperateState);
                isUpdBtns = true;
            }
            if (this.oldBusinessState != null && this.oldBusinessState != this.businessState) {
                isUpdBtns = true;
                this.setBusinessState(this.oldBusinessState);
            }
            if (this.oldUserState != null && this.oldUserState != this.userState) {
                isUpdBtns = true;
                this.setUserState(this.oldUserState);
            }
            if (isUpdBtns == true)
                this.updateButtons();
        },
        /**
         * 设置片段的操作状态
         */
        setOperateState : function(state) {
            this.operateState = state;
        },
        /**
         * 设置片段的业务状态
         *
         * @param 单据的审批状态
         */
        setBusinessState : function(state) {
            this.businessState = state;
        },
        /**
         * 设置片段的自定义状态
         *
         */
        setUserState : function(state) {
            this.userState = state;
        },
        /**
         * 增加组件
         */
        addComponent : function(comp) {
            var argLen = arguments.length;
            if (argLen == 1) {
                this.comps.put(arguments[0].id, arguments[0]);
            } else if (argLen == 2) {
                this.comps.put(arguments[0], arguments[1]);
            }
        },
        /**
         * 增加Dataset
         */
        addDataset : function(ds) {
            this.dss.put(ds.id, ds);
        },
        /**
         * 增加ComboData
         */
        addComboData : function(combodata) {
            this.combodatas.put(combodata.id, combodata);
        },
        /**
         * 获取ComboData
         */
        getComboData : function(id) {
            return this.combodatas.get(id);
        },
        /**
         * 增加参照节点
         */
        addRefNode : function(refnode) {
            this.refnodes.put(refnode.id, refnode);
        },
        /**
         * 获取参照节点
         */
        getRefNode : function(id) {
            return this.refnodes.get(id);
        },
        /**
         * 设置Dataset关联关系
         */
        setDsRelations : function(dsRelations) {
            this.dsRelations = dsRelations;
        },
        /**
         * 获取Dataset关联关系
         */
        getDsRelations : function() {
            return this.dsRelations;
        },
        /**
         * 获取所有组件
         */
        getComponents : function() {
            return this.comps.values();
        },
        /**
         * 获取组件
         */
        getComponent : function(id) {
            return this.comps.get(id);
        },
        /**
         * 获取Dataset
         */
        getDataset : function(id) {
            return this.dss.get(id);
        },
        /**
         * 获取所有Dataset
         */
        getDatasets : function() {
            return this.dss.values();
        },
        /**
         * 删除组件
         */
        removeComponent : function(id) {
            return this.comps.remove(id);
        },
        /**
         * 删除Dataset
         */
        removeDataset : function(id) {
            return this.dss.remove(id);
        },
        /**
         * Widget初始化前执行方法
         * @private
         */
        beforeWidgetInit : function() {
            this.element.triggerHandler("beforeWidgetInit");
        },
        /**
         * Widget初始化后执行方法
         * @private
         */
        afterWidgetInit : function() {
        },
        /**
         * Widget激活前执行方法
         * @private
         */
        afterActive : function() {
        },
        /**
         * 数据初始化前执行方法
         * @private
         */
        $beforeInitData : function() {
            this.initialized = true;
            if (this.lazyInit != null)
                return;
            var openBillId = $.pageutils.getParameter("openBillId");
            var openDsId = $.pageutils.getParameter("openDsId");
            var dss = this.getDatasets();
            if (dss != null) {
                for (var j = 0; j < dss.length; j++) {
                    if (openDsId == dss[j].id)
                        dss[j].addReqParameter("openBillId", openBillId);
                    if (openDsId == dss[j].id) {
                        if (dss[j].dsLoaded)
                            continue;
                        dss[j].dsLoaded = true;
                        dss[j].setCurrentPage(0);
                        dss[j].reqParameterMap.remove("openBillId");
                    }
                    if (!dss[j].dsLoaded && !dss[j].lazyLoad) {
                        if (dss[j].compArr.length == 0 && dss[j].hasComp)
                            continue;
                        dss[j].dsLoaded = true;
                        dss[j].setCurrentPage(0);
                    }
                }
            }
        },
        /**
         * 删除ComboData
         */
        replaceComboData : function(cbId, keyArr, valueArr, updateDs) {
            var cb = $.datalist.getObj(cbId);
            if (keyArr != null && keyArr.length > 0) {
                for (var i = 0; i < keyArr.length; i++) {
                    var item = $.dataitem.getObj(keyArr[i], valueArr[i]);
                    cb.addItem(item);
                }
            }
            this.combodatas.put(cb.id, cb);
            var comps = this.comps.values();
            for (var i = 0; i < comps.length; i++) {
                var comp = comps[i];
                if (comp.componentType=='RADIOGROUP'||comp.componentType=='COMBOBOX') {
                    this.replaceCombo(comp, cbId, cb, updateDs);
                } else if ( comp.componentType == 'AUTOFORM') {
                    for (var j = 0; j < comp.eleArr.length; j++) {
                        if (comp.eleArr[j].componentType=='RADIOGROUP'||comp.eleArr[j].componentType== 'COMBOBOX') {
                            this.replaceCombo(comp.eleArr[j], cbId, cb, updateDs);
                        }
                    }
                } else if (comp.componentType =='GRIDCOMP') {
                    var gridcomps = (comp.compsMap == null ? null : comp.compsMap.values());
                    if (gridcomps != null && gridcomps.length > 0) {
                        for (var j = 0; j < gridcomps.length; j++) {
                            if (gridcomps[j].componentType=='RADIOGROUP') {
                                this.replaceCombo(gridcomps[j], cbId, cb, updateDs);
                            }
                        }
//                         for (var k = 0; k < comp.model.basicHeaders.length; k++) {
//                            // console.info(comp.model.basicHeaders[k]);
//                         }
                        
                        for (var k = 0; k < comp.model.basicHeaders.length; k++) {
//                        	if(comp.model.basicHeaders[k].comboData){
//                        	  console.info(comp.model.basicHeaders[k].comboData.id);
//                        	}
                        	
                            if (comp.model.basicHeaders[k].comboData != null && comp.model.basicHeaders[k].comboData.id == cbId) {
                                comp.model.basicHeaders[k].comboData = cb;
                                if(comp.compsMap.get($.editortype.COMBOBOX + k)) {
                                	comp.compsMap.get($.editortype.COMBOBOX + k).setComboData(cb);
                                } else {
                                	comp.compsMap.get($.editortype.MULTICOMBOBOX + k).setComboData(cb);
                                }
                                comp.model.basicHeaders[k].reRender();
                            }
                        }
                    }
                }
            }
        },
        /**
         * 替换ComboData
         */
        replaceCombo : function(comp, cbId, cb, updateDs) {
            if (comp.comboData != null && comp.comboData.id == cbId) {
                comp.setComboData(cb, updateDs);
            }
        },
        /**
         * Widget关闭时执行方法
         * @private
         */
        widgetClosing : function() {
        },
        /**
         * Widget关闭后执行方法
         * @private
         */
        widgetClosed : function() {
        },
        /**
         * 清除当前widget的所有dataset的状态
         */
        callAllDsClearStatus : function() {
            var dss = this.getDatasets();
            if (dss != null && dss.length != 0) {
                for (var i = 0, count = dss.length; i < count; i++)
                    dss[i].clearState();
            }
        },
        /**
         * 清除当前widget的所有dataset的撤销记录
         */
        callAllDsClearUndo : function() {
            var dss = this.getDatasets();
            if (dss != null && dss.length != 0) {
                for (var i = 0; i < dss.length; i++)
                    dss[i].clearUndo();
            }
        },
        /**
         * 分派事件到Dataset
         * @private
         */
        dispatchEvent2Ds : function(event, masterDsId) {
//            if (event == null || masterDsId == null)
//                return;
//            // 分发RowSelectEvent事件进行单独处理
//            if (event.type == 'RowSelectEvent') {
//            } else if (event.type == 'RowUnSelectEvent') {
//            } else if (event.type == 'DataChangeEvent') {
//                if (event.oldValue == event.currentValue) {
//                    return;
//                }
//                var ds = this.getDataset(masterDsId);
//                var field = ds.fieldList[event.cellColIndex];
//                if (field.editFormular || field.validateFormular)
//                    execFormula(this.id, masterDsId, field.key);
//            }
        },
        /**
         * 直接打开参照页面
         *
         * @param param 类似"a=b&c=d"形式的参数串
         */
        openReference : function(refNodeId, returnFuncName, dialogWidth, dialogHeight, param, refresh, filterValue) {
            var refNode = this.getRefNode(refNodeId);
            if (refNode == null) {
                $.pageutils.showErrorDialog("Can not find refnode by id:" + refNodeId);
                return;
            }
            var trueDialogWidth = $.argumentutils.getInteger(dialogWidth, 650);
            var trueDialogHeight = $.argumentutils.getInteger(dialogHeight, 400);
            var trueReadDsName = $.argumentutils.getString(refNode.readDs, "masterDs");
            var url = window.corePath + "/" + refNode.path + "?pageId=" + refNode.pageMeta + "&widgetId=" + this.id + "&otherPageUniqueId=" + $.pageutils.getPageUniqueId() + "&readDs=" + trueReadDsName + "&nodeId=" + refNodeId;
            if (returnFuncName != null)
                url += "&returnFunc=" + returnFuncName;
            if (refNode.multiSel)
                url += "&multiSel=1";
            if (refNode.delegator != null)
                url += "&delegator=" + refNode.delegator;
            if (refNode.fromNc) {
                url += "&fromNc=Y";
            }
            if (param != null)
                url += "&" + param;

            if (refresh)
                url += "&refresh=1";
            $.pageutils.showDialog(url, refNode.name, trueDialogWidth, trueDialogHeight, 0);

            $.viewpart.doFilterRefDialog($.argumentutils.getString(filterValue, ""), 0);
        }
    });
    /**
     * 参照对话框的内容过滤（显示）
     * @private
     */
    $.viewpart.doFilterRefDialog = function(value, id) {
        if ($.viewpart.waitFilterRt != null)
            clearTimeout($.viewpart.waitFilterRt);
        // 判断Div是否完全打开
        if (window["$modalDialogFrame" + id].contentWindow == null || window["$modalDialogFrame" + id].contentWindow.renderDone == null) {
            $.viewpart.waitFilterRt = setTimeout("$.viewpart.doFilterRefDialog('" + value + "','" + id + "');", 50);
            return;
        }
        window["$modalDialogFrame" + id].contentWindow.doFilter(value);
    };

    /**
     * Widget中的plugout
     */
    $.plugout = function(id) {
        this.id = id;
    };

    $.plugout.getObj = function(id) {
        return new $.plugout(id);
    };

    $.extend($.plugout.prototype, {
        getItems : function() {
            return this.items;
        },

        getItem : function(itemId) {
            return this.items.get(id);
        },

        addItem : function(item) {
            if (this.items == null)
                this.items = $.hashmap.getObj();
            this.items.put(item.id, item);
        }
    });

    /**
     * plugoutitem对象
     * @param {} name
     * @param {} type
     * @param {} source
     * @param {} desc
     */

    $.plugoutitem = function(opts) {
        this.id = opts.name;
        this.type = opts.type;
        this.source = opts.source;
        this.desc = opts.desc;
    };

})(jQuery);

/**
 * 创建Dataset数据类型集合
 * @private
 */
function createDsDataTypeMap(ds) {
    var cache = $.pageutils.getFromCache("dstype_key");
    if (cache == null) {
        cache = new Object;
        $.pageutils.putToCache("dstype_key", cache);
    }

    var obj = cache[ds];
    if (obj == null) {
        obj = {};
        obj.javaClass = 'java.util.HashMap';
        var objMap = new Object;
        obj.map = objMap;
        for (var i = 0, len = ds.fieldList.length; i < len; i++) {
            var md = ds.fieldList[i];
            var replaceField = null;
            if (md.field != null && md.field != "")
                replaceField = md.field.replace(".", "_$_");
            if (replaceField == null || replaceField == "")
                replaceField = md.key;
            // var replaceField = md.field.replace(".", "_$_");
            objMap[replaceField] = md.dataType;
        }
    }
    return obj;
}
