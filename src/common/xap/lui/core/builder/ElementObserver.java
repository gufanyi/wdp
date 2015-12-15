package xap.lui.core.builder;

import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.tags.AppDynamicCompUtil;
import xap.lui.core.tags.DatasetMetaUtil;

public final class ElementObserver {
	public static final String OBS_IN = "OBS_IN";
	private static final String GRIDCOLUMN_VISIBLE_SCRIPT = "gridcolumn_visible_script";
	private static final String GRIDCOLUMN_VISIBLE_INDEX = "gridcolumn_visible_index";
	// static final String GRIDCOLUMN_EDITABLE_SCRIPT = "gridcolumn_editable_script";
	////private static final String GRIDCOLUMN_EDITABLE_INDEX = "gridcolumn_editable_index";
	//private static final String GRIDCOLUMN_PRECISION_INDEX = "gridcolumn_precision_index";
	public static void notifyChange(String type, LuiElement ele){
		notifyChange(type, ele, null);
	}
	
	public static void notifyChange(String type, LuiElement ele, Object userObject){
		if(ele.getExtendAttributeValue(OBS_IN) != null)
			return;
		ele.setExtendAttribute(OBS_IN, "1");
		if(ele instanceof ComboData){
			processComboData(ele);
		}
		else if(ele instanceof GridColumn){
			if(type != null && type.equals(GridColumn.COLUMN_VISIBLE)){
				appProcessColumnVisible((GridColumn) ele, (GridComp) userObject);
			}
			else if(type != null && type.equals(GridColumn.COLUMN_EDITABLE)){
			}
			else if(type != null && type.equals(GridColumn.COLUMN_PRECISION)){	
			}
		}else if(ele instanceof Dataset){
			processDataset(type, (Dataset)ele, userObject);
		}
		
		ele.removeExtendAttribute(OBS_IN);
	}
	

	public static void processDataset(String type,Dataset ds,Object userObject){
		if("adjustField".equals(type)){
			StringBuffer buf = new StringBuffer();
			ViewPartMeta widget = ds.getWidget();
			String varDs = "$c_"+ds.getId();
			if(widget == null){
				buf.append("var "+varDs).append(" = pageUI.getDataset('"+ds.getId()+"');\n");
			}else{
				buf.append("var "+varDs).append(" = pageUI.getViewPart('"+widget.getId()+"').getDataset('"+ds.getId()+"');\n");
			}
			Field field = (Field)userObject;
			String varField = "$c_"+field.getId();
			buf.append("var "+varField).append(" = "+DatasetMetaUtil.generateField(field)+";\n");
			buf.append(varDs).append(".addField("+varField+");\n");
			addBeforeDynamicScript(buf.toString());
			
		}
	}
	

	
	/**
	 * app 设置grid列显示属性
	 * 
	 * @param ele
	 * @param grid
	 */
	private static void appProcessColumnVisible(GridColumn ele, GridComp grid) {
		AppContext appCtx = AppSession.current().getAppContext();
		
		
		Integer index = (Integer) appCtx.getAppAttribute(GRIDCOLUMN_VISIBLE_INDEX + grid.getId());
		if(index != null){
			appCtx.removeExecScript(index);
		}
		
		String script = null;
		String visibleScript = (String) appCtx.getAppAttribute(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId());
		if (visibleScript == null || visibleScript.equals("")){
			visibleScript = "[]";
		}
		
		//清空之前的对该列的设置 
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace(",\"" + ele.getId() + ":" +"false\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"true\"", "");
		visibleScript = visibleScript.replace("\"" + ele.getId() + ":" +"false\"", "");
		
		if(ele.isVisible()){
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"true\"]"); 
		}
		else{
			visibleScript = visibleScript.replace("]", ",\"" + ele.getId() + ":" +"false\"]");
		}
		visibleScript = visibleScript.replace("[,", "[");
		script = "pageUI.getViewPart('" + grid.getWidget().getId() + "').getComponent('" + grid.getId() + "').setColumnVisible(" + visibleScript + ")";
		appCtx.addAppAttribute(GRIDCOLUMN_VISIBLE_SCRIPT + grid.getId(), visibleScript);
		
		index = appCtx.addExecScript(script);
		appCtx.addAppAttribute(GRIDCOLUMN_VISIBLE_INDEX + grid.getId(), index);
		
	}
	

	
	private static void processComboData(LuiElement ele) {
		if(((ComboData) ele).getWidget() == null)
			return;
		String widgetId = ((ComboData) ele).getWidget().getId();
		AppContext appCtx = AppSession.current().getAppContext();
		ViewPartContext viewCtx = appCtx.getCurrentWindowContext().getViewContext(widgetId);
		AppDynamicCompUtil util = new AppDynamicCompUtil(appCtx, viewCtx);
		util.replaceComboData(ele.getId(), widgetId, (ComboData) ele.clone());
	}
	
	public static void addDynamicScript(String script){
		if(script == null || script.equals(""))
			return;
		AppSession.current().getAppContext().addExecScript(script);
	}
	
	public static void addBeforeDynamicScript(String script){
		if(script == null || script.equals(""))
			return;
		AppSession.current().getAppContext().addBeforeExecScript(script);
	}
}
