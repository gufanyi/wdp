/**
 * 
 */
package xap.lui.psn.setting;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.constant.PaConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetCellEvent;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.Connector;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.context.BaseInfo;
import xap.lui.psn.context.IPropertyInfo;
import xap.lui.psn.context.InfoCategory;
import xap.lui.psn.java.Command2TplCache;
import xap.lui.psn.java.ExtAttrConf;
import xap.lui.psn.pamgr.LuiFinder;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;
public class PaSettingDsListener {
	
	public boolean validateInputString(String inputStr) {
		String regex = "^[a-zA-Z0-9_]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	public void onAfterDataChange(DatasetCellEvent e) {
		Dataset ds = e.getSource();
		// 获取前后的值
		String oldValue = (String) e.getOldValue();
		if (oldValue != null) {
			try {
				oldValue = URLEncoder.encode(oldValue, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		String newValue = (String) e.getNewValue();
		// 获取修改的row
		Row row = ds.getCurrentPageData().getSelectedRow();
		if (row == null)
			return;
		// 获取修改的索引和对应的ds中的code：dsCode
		int changeIndex = e.getColIndex();
		Field field = ds.getField(changeIndex);
		String dsCode = field.getId();
		if (newValue != null) {
			try {
				// 校验ID格式
				if (changeIndex == 0 && "string_ext1".equals(dsCode)) {
					if (!validateInputString(newValue)) {
						throw new LuiRuntimeException("请使用非中文的字符串命名ID!");
					}
				}
				newValue = URLEncoder.encode(newValue, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		// 获取修改的元素的ID
		int idIndex = ds.nameToIndex("string_ext1");
		// String compId = (String) row.getValue(idIndex);
		int itemIdIndex = ds.nameToIndex(PaPropertyDatasetListener.FIELD_CHILDID);
		String itemId = (String) row.getValue(itemIdIndex);
		int viewIndex = ds.nameToIndex("string_ext2");
		String viewId = (String) row.getValue(viewIndex);
		int prtIndex = ds.nameToIndex("parentid");
		String prtId = (String) row.getValue(prtIndex);
		// 获取修改元素的类型，已经前台对应的属性值
		int stateIndex = ds.nameToIndex("ds_state");
		String state = (String) row.getValue(stateIndex);
		if (state.equals(PaConstant.DS_DEL))
			return;
		int typeIndex = ds.nameToIndex("comptype");
		String type = (String) row.getValue(typeIndex);
		BaseInfo bpi = InfoCategory.getInfo(type);
		IPropertyInfo[] ipis = bpi.getPropertyInfos();
		IPropertyInfo pi = null;
		for (int i = 0; i < ipis.length; i++) {
			pi = ipis[i];
			if (pi.getDsField().equals(dsCode))
				break;
			pi = null;
		}
		if (pi != null && row != null) {
			AppContext ctx = AppSession.current().getAppContext();
			FormComp fc = (FormComp) AppSession.current().getViewContext().getView().getViewComponents().getComponent("adhintform");
			if (pi.getChangeMonitor() != null) {
				pi.getChangeMonitor().on(fc, pi, newValue);
			}
			//ctx.addExecScript("setEditorState();");
			row.setValue(stateIndex, PaConstant.DS_UPDATE);
			ctx.addExecScript("var obj ={widgetid :'" + viewId + "',updateid:" + (changeIndex == idIndex) + ", parentid : '" + prtId + "', compid:'" + itemId + "', type:'" + type + "', attr:'" + pi.getId() + "', attrtype:'" + pi.getType() + "', oldvalue:'" + oldValue + "', newvalue:'" + newValue + "'};");
			ctx.addExecScript("document.getElementById('iframe_tmp').contentWindow.updateProperty(obj);");
		}
	}
	public void onAfterChartOptionDataChange(DatasetCellEvent e) {
		Dataset ds = e.getSource();
		// 获取前后的值
		String oldValue = (String) e.getOldValue();
		if (oldValue != null) {
			try {
				oldValue = URLEncoder.encode(oldValue, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		Row row = ds.getSelectedRow();
		if (row == null) {
			return;
		}
		String newValue=(String)e.getNewValue();
		if(StringUtils.isBlank(newValue)){
			return ;
		}
		String Id=(String)row.getValue(ds.nameToIndex("Id"));
		MethodCacheInfo methodCacheInfo=PaCache.getInstance().getCacheMethod(Id);
		if(methodCacheInfo==null){
			return;
		}
		methodCacheInfo.invoke(newValue);
		AppContext ctx = AppSession.current().getAppContext();
		//ctx.addExecScript("var obj ={widgetid :'" + viewId + "',updateid:" + (changeIndex == idIndex) + ", parentid : '" + prtId + "', compid:'" + itemId + "', type:'" + type + "', attr:'" + pi.getId() + "', attrtype:'" + pi.getType() + "', oldvalue:'" + oldValue + "', newvalue:'" + newValue + "'};");
		//ctx.addExecScript("document.getElementById('iframe_tmp').contentWindow.updateProperty(obj);");
		
//		String webEleId=(String) row.getValue(ds.nameToIndex("eleId"));
//		ViewPartMeta viewPartMeta = PaCache.getEditorViewPartMeta();
//		ChartBaseComp chartComp = (ChartBaseComp) viewPartMeta.getViewComponents().getComponent(webEleId);
//		chartComp.updateOption();
		
	}
	
	
	// 事件
	public void onAfterEventDataChange(DatasetCellEvent e) {
		Dataset ds = e.getSource();
		String oldValue = (String) e.getOldValue();
		if (oldValue != null) {
			try {
				oldValue = URLEncoder.encode(oldValue, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		Row row = ds.getSelectedRow();
		if (row == null) {
			return;
		}
		String newVavlue = (String) e.getNewValue();
		if (StringUtils.isBlank(newVavlue)) {
			return;
		}
		IEventSupport editor = this.getEditorComp(ds);
		LuiEventConf eventConf = builderEventConf(ds);
		String methodName=eventConf.getMethod();
		String eventName=eventConf.getName();
		String innerIdValue = (String) row.getValue(ds.nameToIndex("Id"));
		if(innerIdValue.endsWith("_Method")){
			methodName = oldValue;
		}
		if(StringUtils.isNotBlank(methodName)&&StringUtils.isNotBlank(eventName)&&editor!=null){
			editor.removeEventConf(eventName, methodName);
			String subRuleId = (String) row.getValue(ds.nameToIndex("ParamValue"));
			if(ds.getExtendAttribute(subRuleId) != null){
				EventSubmitRule submitRule = (EventSubmitRule)ds.getExtendAttribute(subRuleId).getValue();
				eventConf.setSubmitRule(submitRule);
			}
			editor.addEventConf(eventConf);
		}
		//genJavaFile(eventConf);
	}

	public IEventSupport getEditorComp(Dataset ds) {
		Row row = ds.getSelectedRow();
		//IEventSupport editor = null;
		String eleId = (String)row.getValue(ds.nameToIndex("eleId"));
		String uiId = (String)row.getValue(ds.nameToIndex("uiId"));
		String subuiId = (String)row.getValue(ds.nameToIndex("subuiId"));
		String subEleId = (String)row.getValue(ds.nameToIndex("subEleId"));
		String widgetId = (String)row.getValue(ds.nameToIndex("widgetId"));
		String colIndex = (String)row.getValue(ds.nameToIndex("colIndex"));
		String rowIndex = (String)row.getValue(ds.nameToIndex("rowIndex"));
		
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		UIPartMeta uimeta = PaCache.getEditorUIPartMeta();
		UIElement uiEle = null;
		LuiElement webEle = null;
		// 表格数据
		if (rowIndex != null) {
			uiEle = LuiFinder.getGridElement(uimeta, uiId, rowIndex, colIndex);
		} else {
			if (subuiId != null) {
				uiEle = UIElementFinder.findElementById(uimeta, uiId, subuiId);
			} else {
				uiEle = UIElementFinder.findElementById(uimeta, uiId);
			}
			if (eleId != null) {
				if (subEleId != null)
					webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId, subEleId);
				else
					webEle = LuiFinder.getWebElement(pagemeta, widgetId, eleId);
			}
		}
		if(webEle!=null){
		  return(IEventSupport)	webEle;
		}
		if(uiEle!=null){
			return (IEventSupport)	uiEle;
		}
		return null;
	}
	private LuiEventConf builderEventConf(Dataset ds) {
		// 正在编辑的属性
		Row row = ds.getSelectedRow();
		String eventProp = (String) row.getValue(ds.nameToIndex("Id"));
		Row[] rows = ds.getCurrentPageData().getRows();
		String eventName = null;
		if(eventProp.endsWith("_Exattr")){
			String commond = (String) row.getValue(ds.nameToIndex("Pid"));
			for (Row inner : rows) {
				String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
				if (commond.equalsIgnoreCase(innerIdValue)) {
					eventName = (String) inner.getValue(ds.nameToIndex("Pid"));
				}
			}
		}else{
			eventName = (String) row.getValue(ds.nameToIndex("Pid"));
		}
		LuiEventConf eventConf = new LuiEventConf();
		eventConf.setId(eventName);
		eventConf.setEventName(eventName);
		Row parentRow=null;
		for (Row inner : rows) {
			String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
			String pIdValue = (String) inner.getValue(ds.nameToIndex("Pid"));
			if(innerIdValue.equals(eventName)){
				parentRow=inner;
			}
			String paramValue = (String) inner.getValue(ds.nameToIndex("ParamValue"));
			if (eventName.equalsIgnoreCase(pIdValue)) {
				if (innerIdValue.endsWith("_EventType")) {
					eventConf.setEventType(paramValue);
				} else if (innerIdValue.endsWith("_Method")) {
					eventConf.setMethod(paramValue);
				} else if (innerIdValue.endsWith("_Controller")) {
					eventConf.setControllerClazz(paramValue);
				} else if (innerIdValue.endsWith("_Onserver")) {
					eventConf.setOnserver(Boolean.valueOf(paramValue));
				} else if (innerIdValue.endsWith("_Async")) {
					eventConf.setAsync(Boolean.valueOf(paramValue));
				}else if(innerIdValue.endsWith("_Command")){
					eventConf.setModelCmd(paramValue);
				}else if(innerIdValue.endsWith("_State")){
					eventConf.setuIStateId(paramValue);
				}else if(innerIdValue.endsWith("_Lang")){
					eventConf.setEventLang(paramValue);
				}
			}else if(pIdValue != null && pIdValue.endsWith("_Command") && innerIdValue.endsWith("_Exattr")){
					LuiParameter para=new LuiParameter();
					para.setName(innerIdValue);
					para.setValue(paramValue);
					eventConf.addExtendParam(para);
			}else if (pIdValue != null && pIdValue.endsWith("_Lang") && innerIdValue.endsWith("_LangExattr")) {
					eventConf.setScript(paramValue);
			}
		}
	    //如果onserver为false，模式化和执行脚本都不显示
		if (eventProp.endsWith("_Onserver")) {
			String onserveValue = (String) row.getValue(ds.nameToIndex("ParamValue")).toString();
			if(StringUtils.equals(onserveValue, "false")){
				for (Row inner : rows) {
					String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
					if(StringUtils.equals(innerIdValue, eventName + "_LangExattr")){//移除执行脚本行
						ds.removeRow(inner);
					}
					//将模式化命令行移除
					if(StringUtils.equals(innerIdValue, eventName + "_Command")){
						for(Row inner2 : rows){
							String value2 = (String) inner2.getValue(ds.nameToIndex("Pid"));
							if(innerIdValue.equalsIgnoreCase(value2)){
								ds.removeRow(inner2);
							}
						}
						ds.removeRow(inner);
					}
				}
			}else{
				if(eventConf.getEventLang().equalsIgnoreCase("Java语言")){
					//添加模式化命令行
					addCommRow(ds, eventName, eventConf, parentRow);
				}else{
					//添加执行脚本
					this.GenRowsByEventLang(ds, "mvel脚本", eventName + "_Lang", parentRow, eventName);
				}
				
			}
		}
		if (eventProp.endsWith("_Command")) {
			String commandName = (String) row.getValue(ds.nameToIndex("ParamValue"));
			for (Row inner : rows) {
				String value = (String) inner.getValue(ds.nameToIndex("Pid"));
				String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
				if (eventProp.equalsIgnoreCase(value)) {
					if (innerIdValue.endsWith("_Exattr")) {
						ds.removeRow(inner);
					}
				}
				//将执行脚本行移除
				if(StringUtils.equals(innerIdValue, eventName + "_LangExattr")){
					ds.removeRow(inner);
				}
			}
			this.GenRowsByCommand(ds, commandName, eventProp, parentRow);
//			this.GenRowsByCommand(ds, commandName, eventName, parentRow);
			eventConf.setModelCmd(commandName);
		}
		if(eventProp.endsWith("_Lang")){//脚本语言
			String langValue = (String) row.getValue(ds.nameToIndex("ParamValue"));
			if(langValue.equalsIgnoreCase("Java语言")){
				for (Row inner : rows) {
					String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
					//将执行脚本行移除
					if(StringUtils.equals(innerIdValue, eventName + "_LangExattr")){
						ds.removeRow(inner);
					}
				}
				//添加模式化命令行
				{
					addCommRow(ds, eventName, eventConf, parentRow);
				}
			}else{
				for(Row inner : rows){
					String value = (String) inner.getValue(ds.nameToIndex("Pid"));
					String innerIdValue = (String) inner.getValue(ds.nameToIndex("Id"));
					if(eventProp.equalsIgnoreCase(value)){
						if (innerIdValue.endsWith("_LangExattr")) {//移除原来的
							ds.removeRow(inner);
						}
					}
					//将模式化命令行移除
					if(StringUtils.equals(innerIdValue, eventName + "_Command")){
						for(Row inner2 : rows){
							String value2 = (String) inner2.getValue(ds.nameToIndex("Pid"));
							if(innerIdValue.equalsIgnoreCase(value2)){
								ds.removeRow(inner2);
							}
						}
						ds.removeRow(inner);
					}
				}
				this.GenRowsByEventLang(ds, langValue, eventProp, parentRow, eventName);
			}
			eventConf.setEventLang(langValue);
		}
		return eventConf;
	}
	private void addCommRow(Dataset ds, String eventName, LuiEventConf eventConf, Row parentRow) {
		Row enpRow = ds.getEmptyRow();
		enpRow.setValue(ds.nameToIndex("Id"), eventName + "_Command");
		enpRow.setValue(ds.nameToIndex("Pid"), eventName);
		enpRow.setValue(ds.nameToIndex("Name"), "模式化命令");
		if(eventConf!=null){
			enpRow.setValue(ds.nameToIndex("ParamValue"), eventConf.getModelCmd());
		}
		ds.addRow(enpRow);
		this.addEditorAttr(ds, parentRow, enpRow);
	}
	
	public void addEditorAttr(Dataset ds,Row oldRow,Row newRow){
		String eleId =(String) oldRow.getValue(ds.nameToIndex("eleId"));
		String uiId =(String)oldRow.getValue(ds.nameToIndex("uiId"));
		String subuiId =(String) oldRow.getValue(ds.nameToIndex("subuiId"));
		String subEleId =(String)oldRow.getValue(ds.nameToIndex("subEleId"));
		String widgetId = (String)oldRow.getValue(ds.nameToIndex("widgetId"));
		String colIndex =(String) oldRow.getValue(ds.nameToIndex("colIndex"));
		String rowIndex =(String) oldRow.getValue(ds.nameToIndex("rowIndex"));
		String typeIndex = (String) oldRow.getValue(ds.nameToIndex("type"));
		newRow.setValue(ds.nameToIndex("eleId"), eleId);
		newRow.setValue(ds.nameToIndex("uiId"), uiId);
		newRow.setValue(ds.nameToIndex("subuiId"), subuiId);
		newRow.setValue(ds.nameToIndex("subEleId"), subEleId);
		newRow.setValue(ds.nameToIndex("widgetId"), widgetId);
		newRow.setValue(ds.nameToIndex("colIndex"), colIndex);
		newRow.setValue(ds.nameToIndex("rowIndex"), rowIndex);
		newRow.setValue(ds.nameToIndex("type"), typeIndex);
	}
	public void GenRowsByCommand(Dataset dataset, String commandName, String pid,Row parentRow) {
		if (StringUtils.isBlank(commandName)) {
			return;
		}
		ExtAttrConf[] extAttrConfs = Command2TplCache.getExtAttrConf(commandName);
		if(extAttrConfs==null){
			return ;
		}
		for (ExtAttrConf inner : extAttrConfs) {
			Row emptyRow = dataset.getEmptyRow();
			emptyRow.setValue(dataset.nameToIndex("Id"), inner.getAttrId()+"_Exattr");
			emptyRow.setValue(dataset.nameToIndex("Pid"), pid);
			emptyRow.setValue(dataset.nameToIndex("Name"), inner.getAttrName());
			this.addEditorAttr(dataset, parentRow, emptyRow);
			dataset.addRow(emptyRow);
			
		}
	}
	private void GenRowsByEventLang(Dataset ds, String langValue, String pid, Row parentRow, String eventName) {
		if (StringUtils.isBlank(langValue)) {
			return;
		}
		Row emptyRow = ds.getEmptyRow();
		emptyRow.setValue(ds.nameToIndex("Id"), eventName+"_LangExattr");
		emptyRow.setValue(ds.nameToIndex("Pid"), pid);
		emptyRow.setValue(ds.nameToIndex("Name"), "执行脚本");
		this.addEditorAttr(ds, parentRow, emptyRow);
		ds.addRow(emptyRow);
	}

	//管道添加按钮
	public void onclickAddPipeSetting(MouseEvent e){
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		ViewPartMeta viewPartMeta = getViewPartMeta(pagePart);
		if(viewPartMeta == null && pagePart == null)
			return;
		UITabComp tab = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta().findChildById("pipetag");
		int curItem = tab.getCurrentItem();
		if(0 == curItem){//输入管道
			LuiAppUtil.addAppAttr("pipein_status", "add");
			LuiAppUtil.getCntWindowCtx().popView("pipein", "400", "300", "添加输入管道");
		}else if(1 == curItem){//输出管道
			LuiAppUtil.addAppAttr("pipeout_status", "add");
			LuiAppUtil.getCntWindowCtx().popView("pipeout", "600", "500", "添加输出管道");
		}else{//连接器
			LuiAppUtil.addAppAttr("conn_status", "add");
			LuiAppUtil.getCntWindowCtx().popView("connector", "500", "400", "添加连接器");
		}
	}
	//判断是行选中的view还是当前编辑的view
	public ViewPartMeta getViewPartMeta(PagePartMeta pagePart) {
		ViewPartMeta viewPartMeta = null;
		Dataset currDs = LuiAppUtil.getCntWindowCtx().getViewContext("data").getView().getViewModels().getDataset("currds");
		Row curRow = currDs.getSelectedRow();
		if(curRow != null){
			String pId = curRow.getString(currDs.nameToIndex("pid"));
			if(pId != null)
				viewPartMeta = pagePart.getWidget((String)curRow.getValue(currDs.nameToIndex("id")));
		}else{
			viewPartMeta = PaCache.getEditorViewPartMeta();
		}
		return viewPartMeta;
	}
	
	//管道编辑按钮
	public void onclickEditPipeSetting(MouseEvent e){
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaSettingDsListener psetting = new PaSettingDsListener();
		ViewPartMeta viewPartMeta = psetting.getViewPartMeta(pagePart);
		if(viewPartMeta == null && pagePart == null)
			return;
		
		UITabComp tab = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta().findChildById("pipetag");
		int curItem = tab.getCurrentItem();
		if(0 == curItem){//输入管道
			Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipein");
			Row selRow = inDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要編輯的數據！");
			String pid = (String) selRow.getValue(inDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则输入管道行！");
			LuiAppUtil.addAppAttr("pipein_status", "edit");
			LuiAppUtil.getCntWindowCtx().popView("pipein", "400", "300", "修改输入管道");
		}else if(1 == curItem){//输出管道
			Dataset outDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipeout");
			Row selRow = outDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要編輯的數據！");
			String pid = (String) selRow.getValue(outDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则输出管道行！");
			LuiAppUtil.addAppAttr("pipeout_status", "edit");
			LuiAppUtil.getCntWindowCtx().popView("pipeout", "600", "500", "修改输出管道");
		}else{//连接器
			Dataset connDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_connector");
			Row selRow = connDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要編輯的數據！");
			String pid = (String) selRow.getValue(connDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则连接器行！");
			LuiAppUtil.addAppAttr("conn_status", "edit");
			LuiAppUtil.getCntWindowCtx().popView("connector", "600", "500", "修改连接器");
		}
	}
	//管道删除按钮
	public void onclickDelPipeSetting(MouseEvent e){
		UITabComp tab = (UITabComp) LuiAppUtil.getCntWindowCtx().getViewContext("settings").getUIMeta().findChildById("pipetag");
		int curItem = tab.getCurrentItem();
		
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		PaSettingDsListener psetting = new PaSettingDsListener();
		ViewPartMeta viewPartMeta = psetting.getViewPartMeta(pagePart);
		if(viewPartMeta == null && pagePart == null)
			return;
		
		if(0 == curItem){//输入管道
			Dataset inDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipein");
			Row selRow = inDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要删除的數據！");
			String pid = (String) selRow.getValue(inDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则输入管道行！");
			if(InteractionUtil.showConfirmDialog("确认", "确定删除？")){
				String pipeInId = (String) selRow.getValue(inDs.nameToIndex("Value"));
				PipeIn pipeIn = null;
				if(viewPartMeta != null){
					pipeIn = viewPartMeta.getPipeIn(pipeInId);
					viewPartMeta.removePipeIns(pipeIn);
				}else{
					pipeIn = pagePart.getPipeIn(pipeInId);
					pagePart.removePipeIns(pipeIn);
				}
				removeSelectRow(inDs, selRow);
			}
		}else if(1 == curItem){//输出管道
			Dataset outDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_pipeout");
			Row selRow = outDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要編輯的數據！");
			String pid = (String) selRow.getValue(outDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则输出管道行！");
			if(InteractionUtil.showConfirmDialog("确认", "确定删除？")){
				String pipeOutId = (String) selRow.getValue(outDs.nameToIndex("Value"));
				PipeOut pipeOut = null;
				if(viewPartMeta != null){
					pipeOut = viewPartMeta.getPipeOut(pipeOutId);
					viewPartMeta.removePipeOuts(pipeOut);
				}else{
					pipeOut = pagePart.getPipeOut(pipeOutId);
					pagePart.removePipeOuts(pipeOut);
				}
				removeSelectRow(outDs, selRow);
			}
		}else{//连接器
			Dataset connDs = LuiRuntimeContext.getWebContext().getPageMeta().getWidget("settings").getViewModels().getDataset("ds_connector");
			Row selRow = connDs.getSelectedRow();
			if(selRow == null)
				throw new LuiRuntimeException("請選中要删除的數據！");
			String pid = (String) selRow.getValue(connDs.nameToIndex("Pid"));
			if(pid != null)
				throw new LuiRuntimeException("请选则连接器行！");
			if(InteractionUtil.showConfirmDialog("确认", "确定删除？")){
				String connId = (String) selRow.getValue(connDs.nameToIndex("Value"));
				Application app = (Application) PaCache.getInstance().get("_editApp");
				if(app != null){//app
					List<Connector> connmap = app.getConnectorList();
					if(connmap != null && connmap.size() > 0){
						for(Connector conn : connmap){
							if(StringUtils.equals(conn.getId(), connId)){
								app.removeConnector(conn);
								removeSelectRow(connDs, selRow);
							}
						}
					}
				}else{
					LuiSet<Connector> connmap = pagePart.getConnectorMap();
					Connector connector = connmap.find(connId);
					if(connector != null){
						pagePart.removeConnector(connector);
						removeSelectRow(connDs, selRow);
					}
				}
			}
		}
	}
	
	// 移除行及其子行
	private void removeSelectRow(Dataset ds, Row row) {
		ds.removeRow(row);
		String id = (String) row.getValue(ds.nameToIndex("Id"));
		PageData[] pageDatas = ds.getAllPageData();
		if (pageDatas != null && pageDatas.length > 0) {
			for (PageData pageData : pageDatas) {
				Row[] rows = pageData.getRows();
				if (rows != null && rows.length > 0) {
					for (int i = 0; i < rows.length; i++) {
						Row r = rows[i];
						String pid = (String) r.getValue(ds.nameToIndex("Pid"));
						if (pid != null && pid.equals(id)) {
							removeSelectRow(ds, r);
						}
					}
				}
			}
		}
	}
	
}
