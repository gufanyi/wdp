package xap.lui.core.control;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mvel2.MVEL;
import org.mvel2.integration.impl.DefaultLocalVariableResolverFactory;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartContext;


public class DftAppCtrl {
	public void onPageClosed(PageEvent e){
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	
	public void onViewClosed(DialogEvent e){
		AppSession ctx = AppSession.current();
		String widgetId = ctx.getViewContext().getId();
		UIPartMeta um = (UIPartMeta)ctx.getWindowContext().getUIPartMeta();
		um.getDialogMap().remove(widgetId);
	}
	
	public void onFormula(ScriptEvent e) {
		AppSession session = AppSession.current();
		String widgetType = session.getParameter("widgetType");
		String viewPartId = session.getParameter("widgetId");
		String fieldId = session.getParameter("fieldId");
		ViewPartContext currentView = session.getWindowContext().getViewContext(viewPartId);
		session.getWindowContext().setCurrentViewContext(currentView);
		WebComp comp = null;
		if(StringUtils.equals(widgetType, "formElement")) {
			String formId = session.getParameter("formId");
			String eleId = session.getParameter("eleId");
			FormComp form = (FormComp)LuiAppUtil.getControl(formId);
			if(form !=null) {
				comp = form.getElementById(eleId);
			}
		} else{
			comp = LuiAppUtil.getControl(fieldId);
		}
		if(comp != null) {
			LuiEventConf validateEvent = getCompEvent(comp,"validate_method");
			if(validateEvent != null) {
				String script = validateEvent.getScript();
				Serializable c = MVEL.compileExpression(script);
				String message = (String) MVEL.executeExpression(c, new DefaultLocalVariableResolverFactory());
				if (StringUtils.isNotBlank(message)) {
					LuiValidateException exception = null;
					if(StringUtils.equals(widgetType, "formElement")) {
						exception = new LuiValidateException("");
						exception.addComponentId(((FormElement)comp).getParent().getId());
						Map<String,String> map = exception.getElementMap();
						map.put(comp.getId(), message);
					} else {
						exception = new LuiValidateException(message);
						exception.addComponentId(comp.getId());
					}
					exception.setViewId(viewPartId);
					throw exception;
				}
			}
			LuiEventConf editorEvent = getCompEvent(comp,"editor_method");
			if(editorEvent != null) {
				String script = editorEvent.getScript();
				Serializable c = MVEL.compileExpression(script);
				MVEL.executeExpression(c, new DefaultLocalVariableResolverFactory());
			}
		}
	}
	
	private LuiEventConf getCompEvent(WebComp comp, String method) {
		List<LuiEventConf> eventList = comp.getEventConfList();
		if(CollectionUtils.isNotEmpty(eventList)) {
			for(LuiEventConf event : eventList) {
				if(StringUtils.equals(event.getMethod(), method)) {
					return event;
				}
			}
		}
		return null;
	}
	
//	public void onFormular(ScriptEvent e){
//		AppLifeCycleContext ctx = AppLifeCycleContext.current();
//		String widgetId = ctx.getParameter("widgetId");
//		String datasetId = ctx.getParameter("dsId");
//		String fieldId = ctx.getParameter("fieldId");
//		//ctx.getApplicationContext().addAppAttribute(AbstractLuiViewFormula.FORMULA_WIDGET_ID, widgetId);
//		//ctx.getApplicationContext().addAppAttribute(AbstractLuiViewFormula.FORMULA_DATASET_ID, datasetId);
//		//ctx.getApplicationContext().addAppAttribute(AbstractLuiViewFormula.FORMULA_FIELD_ID, fieldId);
//		LUiWidget widget = AppLifeCycleContext.current().getWindowContext().getViewContext(widgetId).getView();
//		if(datasetId != null && !"".equals(datasetId)){
//			Dataset ds = widget.getViewModels().getDataset(datasetId);
//			Row row = ds.getSelectedRow();
//			row.setRowChanged(true);
//		}
//		
//		WebComponent  wc = widget.getViewComponents().getComponent(fieldId);
//		/**
//		 * 文本输入控件.使用公式
//		 */
//		Map<String, TextComp> textMap = new HashMap<String, TextComp>();
//		Map<String, String> textValMap = new HashMap<String, String>();
//		if(wc instanceof TextComp){
//			TextComp t = ((TextComp) wc);
//			if(t.getValidateFormula() != null && t.getValidateFormula().length() > 0){
//				processValFormular(t, t.getValue());
//			}
//			if(t.getEditFormular() != null && t.getEditFormular().length() > 0){
//				WebComponent[] wcs = widget.getViewComponents().getComponents();
//				if(wcs != null && wcs.length > 0){
//					for(WebComponent comp : wcs){
//						if(comp instanceof TextComp){
//							textMap.put(comp.getId(), (TextComp) comp);
//							textValMap.put(comp.getId(), ((TextComp) comp).getValue());
//						}
//					}
//				}
//				processFormular(textMap, textValMap);
//			}
//		}
		
		
		
	//}
//	private void processValFormular(TextComp field, String value){
//		String formular = field.getValidateFormula();
//		FormulaParse fp = LuiFormulaParser.getInstance();
// 
//		
//		String[] expArr = formular.split(";");
//		fp.setExpressArray(expArr);
//		fp.addVariable(field.getId(), value);
//		fp.getValueOArray();
//	}
	
//	private void processFormular(Map<String, TextComp> textMap, Map<String, String> textValMap) {
//		List<TextComp> textlist = new ArrayList<TextComp>();
//		textlist.addAll(textMap.values());
//		if(textlist == null || textlist.isEmpty())
//			return;
//		List<String> executedFpList = new ArrayList<String>();
//		int fieldCount = textlist.size();
//		FormulaParse fp = LuiFormulaParser.getInstance();
//		for(int i = 0; i < fieldCount; i ++){
//			try{
//				TextComp field = textlist.get(i);
//				List<String> fieldFormulars = new ArrayList<String>();
//				String formular = field.getEditFormular();
//				if(formular != null)
//					fieldFormulars.add(formular);
//				Iterator<String> fit = fieldFormulars.iterator();
//				while(fit.hasNext()){
//					formular = fit.next();
//					String exp = formular;
//					if(exp == null)
//						continue;
//					
//					if(executedFpList.contains(exp))
//						continue;
//					executedFpList.add(exp);
//					String[] expArr = exp.split(";");
//						
//					fp.setExpressArray(expArr);
//					VarryVO[] varryVOs = fp.getVarryArray();
//					if(varryVOs != null && varryVOs.length > 0){
//						String[] formularNames = new String[varryVOs.length];
//						for(int j = 0; j < varryVOs.length; j ++){
//							String[] keys = varryVOs[j].getVarry();
//							if(keys != null){
//								for(String key : keys){
//									List<Object> valueList = new ArrayList<Object>();
//										Object value = textValMap.get(key);
//										valueList.add( value );
//									fp.addVariable(key, valueList);
//								}
//								formularNames[j] = varryVOs[j].getFormulaName();
//							}
//						}
//						Object[][] result = fp.getValueOArray();
//						if(result != null){
//							for (int l = 0; l < formularNames.length; l++) {
//								TextComp tc = textMap.get(formularNames[l]);
//								if(tc == null){
//									LuiLogger.error("can not find TextComp:" + formularNames[l]  );
//									continue;
//								}
//								tc.setValue((result[l][0]).toString());
//							}
//						}
//					}
//					else{
//						 fp.getValueOArray();
//					}
//				}
//			}
//			catch(Throwable e){
//				if(e instanceof LuiInteractionException)
//					throw (LuiInteractionException)e;
//				Logger.error(e.getMessage(), e);
//			}
//		}
//	}
	
}
