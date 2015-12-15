function makeDragImage(treeNodeDragEvent) {
    var node = treeNodeDragEvent.sourceNode;
    var row = node.nodeData;
    var div = $('<div>').css({'width':'100px','height':'100px','border':'1px solid red'})[0];
    return div;
}

function onLoadStruct(id) {
        var editorIFrame = $("#iframe_tmp")[0];
  	    editorIFrame.contentWindow.toClickSelected(id);
}

function deleteFromStruct(id) {
    var editorIFrame = $("#iframe_tmp")[0];
    editorIFrame.contentWindow.toDeleteSelected(id);

}

function addFromStruct(obj) {
    var editorIFrame = $("#iframe_tmp")[0];
    editorIFrame.contentWindow.toAddSelected(obj);
}

function callBack(obj, oper) {
    //true为请求同步,防止死循环问题,待查
    var proxy =  $.serverproxy.getObj({async:false});
    proxy.addParam("clc", "xap.lui.psn.pamgr.PaPropertyDatasetListener");
    proxy.addParam("m_n", "handlerEvent");
    proxy.addParam("divid", obj.id);
    proxy.addParam("uiid", obj.uiid);
    proxy.addParam("eleid", obj.eleid);
    proxy.addParam("widgetid", obj.widgetid);
    proxy.addParam("type", obj.type);
    proxy.addParam("objtype", obj.objtype);
    proxy.addParam("oper", oper);
    proxy.addParam("subeleid", obj.subeleid);
    proxy.addParam("subuiid", obj.subuiid);
    proxy.addParam("rendertype", obj.rendertype);
    proxy.addParam("rowindex", obj.rowindex);
    proxy.addParam("colindex", obj.colindex);
    proxy.addParam("direction", obj.direction);
    var sbr = $.submitrule.getObj();
    
    
    //settings widget中的提交规则
    var pWdRule = $.viewpartrule.getObj("settings");
    var pFormRule =$.formrule.getObj("hintform", "all_child");
    pWdRule.addFormRule(pFormRule);
    var dsRule = $.datasetrule.getObj("ds_middle", "ds_all_line");
    pWdRule.addDsRule("ds_middle", dsRule);

    //data widget中的提交规则
    var dataWdRule = $.viewpartrule.getObj("data");
    var treeRule = $.treerule.getObj("entitytree", "tree_current_parent_root_tree");
    var ctrlDsRule =  $.datasetrule.getObj("ctrlds", "ds_all_line");
    dataWdRule.addDsRule("ctrlds", ctrlDsRule);
    dataWdRule.addTreeRule(treeRule);

    //nav widget中的提交规则
    var navWdRule = $.viewpartrule.getObj("nav");
    var structDsRule = $.datasetrule.getObj("ds_struct", "ds_all_line");
    navWdRule.addDsRule("ds_struct", structDsRule)

    sbr.addViewPartRule("settings", pWdRule);
    sbr.addViewPartRule("data", dataWdRule);
    sbr.addViewPartRule("nav", navWdRule);

    proxy.setSubmitRule(sbr);
    pageUI.getViewPart('settings').getDataset('ds_middle').silent = true;
    proxy.execute();
	pageUI.getViewPart('settings').getDataset('ds_middle').silent = false;
    triggerEditorEvent("showPropertiesView", obj);
}

function triggerSaveEvent() {
    try {
        var proxy =  $.serverproxy.getObj({async:true});
        proxy.addParam("clc", "xap.lui.psn.pamgr.PaPropertySaveListener");
        proxy.addParam("m_n", "handlerEvent");
        var sbr =  $.submitrule.getObj();
        var setWdRule = $.viewpartrule.getObj("settings");

        var pFormRule =$.formrule.getObj ("hintform", "all_child");
        setWdRule.addFormRule(pFormRule);

        var dsRule = $.datasetrule.getObj("ds_middle", "ds_all_line");
        setWdRule.addDsRule("ds_middle", dsRule);

        sbr.addViewPartRule("settings", setWdRule);
        proxy.setSubmitRule(sbr);
        proxy.execute();
    } catch(e) {
        alert(e);
    }
}

function triggerRevertEvent() {
    showConfirmDialog("是否还原当前页面",
    function() {
        try {
            var proxy = new ServerProxy(null, null, true);
            proxy.addParam("clc", "xap.lui.psn.pamgr.PaRevertEditViewController");
            proxy.addParam("m_n", "revertView");
            proxy.execute();
        } catch(e) {
            alert(e);
        }
    },
    null, null, null, null, "确定", "取消", "还原当前页面");

}

function triggerSwitchEvent() {
//    showThreeButtonConfirmDialog("是否保存当前view",
//    function() {
//        triggerSaveEvent();
//        rePostOk();
//    },
//    null,
//    function() {
//        rePostOk()
//    },
//    ["保存", "不保存", "取消"], null, null, null, null, null, "保存当前view");
}

function rePostOk() {
    try {
        var proxy = new ServerProxy(null, null, true);
        proxy.addParam("clc", "xap.lui.psn.pamgr.PaSwitchViewController");
        proxy.addParam("m_n", "switchView");
        var sbr = new SubmitRule();
        proxy.execute();
    } catch(e) {
        alert(e);
    }
}

function refreshDs() {
    var dataset = pageUI.getViewPart("data").getDataset("ctrlds");
    var remaindKey = dataset.currentKey;
    dataset.currentKey = "aaa";
    dataset.currentKey = remaindKey;
}

function saveForEclipse() {
    triggerSaveEvent();
}
/**
 * 清除事件状态
 */
function clearEventStatus() {
    try {
        window.status = "stopEvent";
    } catch(e) {
    }
}

/**
 * 创建一个Eclipse交互事件
 * @param {} type
 * @param {} source
 * @return {}
 */
function triggerEditorEvent(type, source) {

    var eventContext = "event:";
    eventContext += type;
    for (i in source) {
        try {
            eventContext += ",,,";
            eventContext += i;
            eventContext += ":";
            eventContext += source[i];
        } catch(e) {}
    }
    window.status = eventContext;
    return true;
}

function setEditorState() {
    try {
        triggerEditorEvent("changeFileStatus", {});
    } catch(e) {
    }
}
document.getEditorState = function() {
    if (window.eclipseState) {
        return window.eclipseState;
    } else {
        return false;
    }
}
document.resetEditorState = function() {
    window.eclipseState = false;
}
function setSaveState() {
    triggerEditorEvent("changeSaveStatus", {})
}
function setSession(sessionId) {
    var param = {};
    param.sessionID = sessionId;
    triggerEditorEvent("setSessionId", param);
};
setSession(window.JSessionID);


function treeImageRender() {
}
treeImageRender = function(imgCell, treeNode) {
    if (treeNode.nodeData) {
        var index = treeNode.level.dataset.nameToIndex("imgtype");
        var type = treeNode.nodeData.getCellValue(index);
        if (!type || type == "") {
            index = treeNode.level.dataset.nameToIndex("type");
            type = treeNode.nodeData.getCellValue(index);
            if (!type || type == "") {
                index = treeNode.level.dataset.nameToIndex("id");
                type = treeNode.nodeData.getCellValue(index);
            }
        }
        if (!type) {
            type = "";
        }
        imgCell.innerHTML = "<img style='width:14px;height:14px;vertical-align:middle;margin-right:6px;' src='" + getImgSrc(type) + "'>";
    }
};

function getImgSrc(type) {
	
    var imgPath = window.themePath + "/comps/tree/images/icon/";
    /*var imgType = "";
	if(type){
		imgType = getImgType(type);
	}
	var imgName = getImgName(imgType);*/
    var imgname = getImgNameByType(type);
    return imgPath + imgname;
}
function getImgNameByType(type) {
    var imgname = "";
    if (type.indexOf("UILayoutPanel") != -1) { //UI类名区分
        imgname = "UILayoutPanel.png";
    } else if (type.indexOf("UILayout") != -1) {
        imgname = "UILayout.png";
    } else if (type.indexOf("UIComponent") != -1 || type.indexOf("WebComponent") != -1) { //UI类名或元素类名
        imgname = "UIComponent.png";
    } else if (type.indexOf("UIButton") != -1 || type.indexOf("ButtonComp") != -1) {
        imgname = "UIButton.png";
    } else if (type.indexOf("UIImageComp") != -1 || type.indexOf("ImageComp") != -1) {
        imgname = "UIImageComp.png";
    } else if (type.indexOf("UILabelComp") != -1 || type.indexOf("LabelComp") != -1) {
        imgname = "UILabelComp.png";
    } else if (type.indexOf("UIMenubarComp") != -1 || type.indexOf("MenubarComp") != -1 || type.indexOf("ContextMenuComp") != -1) {
        imgname = "UIMenubarComp.png";
    } else if (type.indexOf("UILinkComp") != -1 || type.indexOf("LinkComp") != -1) {
        imgname = "UILinkComp.png";
    } else if (type.indexOf("UITextField") != -1 || type.indexOf("TextComp") != -1 || type.indexOf("CheckBoxComp") != -1 || type.indexOf("CheckboxGroupComp") != -1 || type.indexOf("ComboBoxComp") != -1 || type.indexOf("LanguageComboBoxComp") != -1) {
        imgname = "UITextField.png";
    } else if (type.indexOf("RadioComp") != -1 || type.indexOf("RadioGroupComp") != -1 || type.indexOf("ReferenceComp") != -1) {
        imgname = "UITextField.png";
    } else if (type.indexOf("SelfDefElementComp") != -1 || type.indexOf("TextAreaComp") != -1) {
        imgname = "UITextField.png";
    } else if (type.indexOf("UIIFrame") != -1 || type.indexOf("IFrameComp") != -1) {
        imgname = "UIChildWin.png";
    } else if (type.indexOf("UISelfDefComp") != -1 || type.indexOf("SelfDefComp") != -1) {
        imgname = "UISelfDefComp.png";
    } else if (type.indexOf("UIPartComp") != -1 || type.indexOf("WebPartComp") != -1) {
        imgname = "UIHtmlcontent.png";
    } else if (type.indexOf("UIGridComp") != -1 || type.indexOf("GridComp") != -1) {
        imgname = "UIGrid.png";
    }
    if (imgname == "") {
        switch (type) { //类型区分
        case "gridlayout":
            imgname = "UILayout_grid.png";
            break;
            /*网格容器*/
        case "flowhlayout":
            imgname = "UILayout_horizontal.png";
            break;
            /*横向容器*/
        case "flowvlayout":
            imgname = "UILayout_vertical.png";
            break;
            /*纵向容器*/
        case "tag":
            imgname = "UILayout_tab.png";
            break;
            /*页签容器*/
        case "panellayout":
            imgname = "UILayout_titlepanel.png";
            break;
            /*标题面板*/
        case "spliterlayout":
            imgname = "UILayout_compart.png";
            break;
            /*分隔容器*/
        case "outlookbar":
            imgname = "UILayout_shutter.png";
            break;
            /*百叶窗容器*/
        case "cardlayout":
            imgname = "UILayout_card.png";
            break;
            /*卡片容器*/
        case "border":
            imgname = "UILayout_frame.png";
            break;
            /*边框容器*/
        case "canvaslayout":
            imgname = "UILayout_background.png";
            break;
            /*背景容器*/
        case "definedCom":
            imgname = "UIDefinedCom.png";
            break;
            /*已定义控件*/
        case "undefinedCom":
            imgname = "UIUndefinedComp.png";
            break;
            /*未定义控件*/
        case "button":
            imgname = "UIButton.png";
            break;
            /*按钮*/
        case "label":
            imgname = "UILabelComp.png";
            break;
            /*标签*/
        case "menubar":
            imgname = "UIMenubarComp.png";
            break;
            /*菜单*/
        case "image":
            imgname = "UIImageComp.png";
            break;
            /*图片*/
        case "link":
            imgname = "UILinkComp.png";
            break;
            /*超链接*/
        case "progress_bar":
            imgname = "UIProgressbar.png";
            break;
            /*进度条*/
        case "StringText":
            imgname = "UIStringText.png";
            break;
            /*字符输入控件*/
        case "Reference":
            imgname = "UIReference.png";
            break;
            /*参照输入控件*/
        case "ComboBox":
            imgname = "UICombox.png";
            break;
            /*下拉输入控件*/
        case "CheckboxGroup":
            imgname = "UICheckboxGroup.png";
            break;
            /*多选*/
        case "RadioGroup":
            imgname = "UIRadioGroup.png";
            break;
            /*单选*/
        case "DateText":
            imgname = "UIDateText.png";
            break;
            /*日期输入控件*/
        case "DecimalText":
            imgname = "UIDecimalText.png";
            break;
            /*浮点输入控件*/
        case "IntegerText":
            imgname = "UIIntegerText.png";
            break;
            /*整型输入控件*/
        case "TextArea":
            imgname = "UITextArea.png";
            break;
            /*大文本框*/
        case "iframe":
            imgname = "UIChildWin.png";
            break;
            /*子窗口控件*/
        case "self_def_comp":
            imgname = "UISelfDefComp.png";
            break;
            /*自定义控件*/
        case "htmlcontent":
            imgname = "UIHtmlcontent.png";
            break;
            /*WebPart控件*/
        case "grid":
            imgname = "UIGrid.png";
            break;
            /*表格*/
        case "formcomp":
            imgname = "UIFormcomp.png";
            break;
            /*表单*/
        }
    }
    if (imgname == "") { //id区分
        if (type.indexOf("layout") != -1 || type.indexOf("tag") != -1 || type.indexOf("outlookbar") != -1 || type.indexOf("border") != -1) {
            imgname = "UILayout.png";
        } else if (type.indexOf("panel") != -1 || type.indexOf("Item") != -1) {
            imgname = "UILayoutPanel.png";
        } else {
            imgname = "UIComponent.png";
        }
    }
    if (imgname == "") imgname = "xor.png";
    return imgname;
}
/**
 * 打开参照前操作，此处用于拼接参照真实值
 * 
 * @param {} ref 参照对象
 * @return {} param等号后的字符串
 */
function globalBeforeOpenRefDialog(ref) {
    var dataset = pageUI.getViewPart("settings").getDataset("ds_middle");
    var colIndex = dataset.nameToIndex(ref.fieldId);
    var rowIndex = dataset.getSelectedIndex();
    var refValue = dataset.getValueAt(rowIndex, colIndex);
    refValue = encodeURIComponent(refValue);
    return refValue;
}
function selectStruct(id) {
    try {
        var proxy = $.serverproxy.getObj({async:true});
        proxy.addParam("clc", "xap.lui.psn.pamgr.PaStructDsListener");
        proxy.addParam("m_n", "onSelect");
        proxy.addParam("id", id);
        proxy.execute();
    } catch(e) {
        alert(e);
    }
}

/**
 * 刷新实体
 */
function reloadEntityds() {
    var dsName = "entityds";
    var ds = pageUI.getViewPart("data").getDataset(dsName);
    var tab = pageUI.getViewPart("data").getTab("outlookbar1");
    if (ds.currentKey && ds.currentKey != "") {
        if (tab.getSelectedItem().id != "item_entity") {
            tab.activeTabByName("item_entity")
        }
        ds.setCurrentPage(ds.currentKey + "1");
        var selectIdx = ds.getSelectedIndex();
        if (selectIdx != -1) {
            ds.setRowUnSelected(selectIdx);
        }
        ds.setFocusRowIndex( - 1);
    }
}

function switchEditorType(grid,row,cell,header) {
//判断类型(添加控件时)
   if(grid.id == "grid_event"){
	   	var PVvalue = row.getCellValue(0);
		if("模式化命令" == PVvalue){
			cell.data('editorType',$.editortype.COMBOBOX);
			var comboData = pageUI.getViewPart('settings').getComboData('model');
			cell.data('combo',grid.compsMap.get($.editortype.COMBOBOX+"11"));
			if(!cell.data('combo').getComboData())
				cell.data('combo').setComboData(comboData);
			cell.data('comboData',comboData);
		}
		else if("同步(async)" == PVvalue || "服务器上运行(onserver)" == PVvalue){
			cell.data('editorType',$.editortype.COMBOBOX);
			var comboData = pageUI.getViewPart('settings').getComboData('booleanValue');
			cell.data('combo',grid.compsMap.get($.editortype.COMBOBOX+"12"));
			if(!cell.data('combo').getComboData())
				cell.data('combo').setComboData(comboData);
			cell.data('comboData',comboData);
		}else if("提交规则" == PVvalue) {
			cell.data('editorType',$.editortype.REFERENCE);
			var refNode = pageUI.getViewPart('settings').getRefNode("refnode_rule");
			cell.data('refnode',grid.compsMap.get($.editortype.REFERENCE+"13"));
			cell.data('refnode').options.nodeInfo=refNode;
		}else if("目标状态" == PVvalue){
			cell.data('editorType',$.editortype.COMBOBOX);
			var comboData = pageUI.getViewPart('settings').getComboData('stateList');
			cell.data('combo',grid.compsMap.get($.editortype.COMBOBOX+"14"));
			if(!cell.data('combo').getComboData())
				cell.data('combo').setComboData(comboData);
			cell.data('comboData',comboData);
		}
		else if("事件语言" == PVvalue){
			cell.data('editorType',$.editortype.COMBOBOX);
			var comboData = pageUI.getViewPart('settings').getComboData('eventlangList');
			cell.data('combo',grid.compsMap.get($.editortype.COMBOBOX+"15"));
			if(!cell.data('combo').getComboData())
				cell.data('combo').setComboData(comboData);
			cell.data('comboData',comboData);
		}else if("执行脚本" == PVvalue){
			cell.data('editorType',$.editortype.REFERENCE);
			var refNode = pageUI.getViewPart('settings').getRefNode("refnode_mvel");
			cell.data('refnode',grid.compsMap.get($.editortype.REFERENCE+"16"));
			cell.data('refnode').options.nodeInfo=refNode;
		}
		else {
			cell.data('editorType',$.editortype.STRINGTEXT);
			
		}
   }else{
   		cell.data('editorType',header.editorType);
   }
}

window.onbeforeunload = function() {
    if (window.getParameter("ModePhase") != "eclipse") return "";
};