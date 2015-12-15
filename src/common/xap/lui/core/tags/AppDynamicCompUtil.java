package xap.lui.core.tags;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;

/**
 * 动态生成组件脚本工具
 * 
 * @author dingrf
 *
 */
public class AppDynamicCompUtil {
//	private static final String DYNAMICFIELD = ExtendAttributeSupport.DYN_ATTRIBUTE_KEY + "_dynamicfield";
	private static final String REPLACE_COMBODATA = "replace_combodata";
	private AppContext appCtx; 
	private ViewPartContext viewCtx;
	public AppDynamicCompUtil(AppContext appCtx, ViewPartContext viewCtx) {
		this.appCtx = appCtx;
		this.viewCtx = viewCtx;
	}

	/**
	 * 刷新数据集
	 * @param dataset
	 */
	public void refreshDataset(Dataset dataset){
		refreshDataset(dataset, null);
	}
	
	/**
	 * 刷新数据集
	 * @param dataset
	 * @param pageIndex
	 */
	public void refreshDataset(Dataset dataset, Integer pageIndex) {
		dataset.clear();
		if(pageIndex != null){
			appCtx.addExecScript("pageUI.getViewPart('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "')" +
					".setCurrentPage(" + pageIndex + ", null, null, true" + ")");
		}
		else{
			appCtx.addExecScript("pageUI.getViewPart('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "')" +
					".setCurrentPage()");
		}
	}
	
//	/**
//	 * 更新grid某一列的显示文字
//	 * 
//	 * @param widgetId
//	 * @param gridId
//	 * @param columnId
//	 * @param text
//	 */
//	@Deprecated
//	public void replaceGridColumnText(String widgetId,String gridId,String columnId,String text){
//		appCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getComponent('" + gridId + "').getBasicHeaderById('" + columnId + "').replaceText('" + text + "')");
//	} 
//	
//	
//	/**
//	 * 激活某项tab页签
//	 * @param tabId
//	 */
//	@Deprecated
//	public void activeTabItem(String widgetId,String tabId, String tabIndex){
//		appCtx.addExecScript("pageUI.getWidget('" + widgetId + "').getTab('" + tabId + "').activeTab('" + tabIndex + "')");
//	}
//	
	/**
	 * 动态修改下拉数据值
	 * @param oriId
	 * @param widgetId
	 * @param cd
	 */
	@Deprecated
	public void replaceComboData(String oriId, String widgetId, ComboData cd){
		StringBuffer buf = new StringBuffer();
		String[] keyValue = getMergedCDkeyValues(cd);
		String pageUI = "pageUI";
		ViewPartMeta widget = LuiRuntimeContext.getWebContext().getPageMeta().getWidget(widgetId);
		if (widget == null && LuiRuntimeContext.getWebContext().getParentPageMeta() != null){
			widget = LuiRuntimeContext.getWebContext().getParentPageMeta().getWidget(widgetId);
			pageUI = "parent.pageUI";
		}
		if (keyValue == null){
			buf.append(pageUI).append(".getViewPart('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("');\n");
		}
		else{
			buf.append(pageUI).append(".getViewPart('")
			.append(widgetId)
			.append("').replaceComboData('")
			.append(oriId)
			.append("',")
			.append(keyValue[1])
			.append(",")
			.append(keyValue[0])
			.append(");\n");
		}
		
		ComboData oriCb = widget.getViewModels().getComboData(oriId);
		//oriCb.setExtendAttribute(ElementObserver.OBS_IN, "1");
		oriCb.removeAllDataItems();
		DataItem[] items = cd.getAllDataItems();
		if(items != null){
			for (int i = 0; i < items.length; i++) {
				oriCb.addDataItem(items[i]);
			}
		}
		//oriCb.removeExtendAttribute(ElementObserver.OBS_IN);
		
		String oldScript = (String) appCtx.getAppAttribute(REPLACE_COMBODATA + widgetId + "_" + oriId);
		if(oldScript != null){
			if(appCtx.getBeforeExecScript() != null){
				for(int i = 0; i < appCtx.getBeforeExecScript().size(); i++){
					String script =(String) appCtx.getBeforeExecScript().get(i);
					if(script.equals(oldScript)){
						appCtx.getBeforeExecScript().remove(i);
						break;
					}
				}
			}
		}
		appCtx.addBeforeExecScript(buf.toString());
		appCtx.addAppAttribute(REPLACE_COMBODATA + widgetId + "_" + oriId, buf.toString());
		
	}
//	
//	/**
//	 * 生成dataset的metaData相关脚本
//	 * 
//	 * @param dataset
//	 */
//	@Deprecated
//	public void generateDsMetaDataScript(Dataset dataset){
//		int fieldCount = dataset.getFieldSet().getFieldCount();
//		//"filed1":"4","filed2":4
//		JSONObject jsonObj = new JSONObject();
//		JSONObject precisionObj = new JSONObject();
//		for(int i = 0; i < fieldCount; i ++){
//			Field field = dataset.getFieldSet().getField(i);
//			if (field.isCtxChanged() && field.checkCtxPropertyChanged("precision")){
//				String precision = field.getPrecision();
//				if (precision != null){
//					precisionObj.put(field.getId(), precision);
//				}
//			}
//		}
//		if (precisionObj.length() > 0)
//			jsonObj.put("precision", precisionObj);
//		if (jsonObj.length() > 0)
//			appCtx.addBeforeExecScript("pageUI.getWidget('" + viewCtx.getId() + "').getDataset('" + dataset.getId() + "').setMeta(" + jsonObj.toString() + ");");
//	}
//
	private static String[] getMergedCDkeyValues(ComboData cd) {
		DataItem[] items = cd.getAllDataItems();
		if (items == null||items.length==0)
			return null;
		StringBuffer keyBuf = new StringBuffer();
		StringBuffer valueBuf = new StringBuffer();
		keyBuf.append("[");
		valueBuf.append("[");
		for (int i = 0; i < items.length; i++) {
			DataItem item = items[i];
			keyBuf.append("'")
			      .append(item.getValue())
			      .append("'");
			
			valueBuf.append("'")
		      .append(item.getText())
		      .append("'");
			
			if(i != (items.length - 1)){
				keyBuf.append(",");
				valueBuf.append(",");
			}
		}
		keyBuf.append("]");
		valueBuf.append("]");
		return new String[]{keyBuf.toString(), valueBuf.toString()};
	}
}
