package xap.lui.core.refrence;

import java.util.Iterator;
import java.util.Map;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.ViewPartMeta;


/**
 * 将参照数据写入到父页面的dataset或是referenceText中
 * @author 
 */
public class AppRefDftOkCtrl implements IWindowCtrl {


	public static final String TYPE = "type";
	public static final String ID = "id";
	public static final String WRITEFIELDS = "writeFields";
//	public static final String VALUES = "values";
	public static final String TYPE_DS = "Dataset";
	public static final String TYPE_TEXT = "Text";
	public static final String PLUGIN_ID = "refOkPlugin";
	public static final String PLUGOUT_ID = "refOkPlugout";

	
	/***
	 * 参照plug返回处理
	 * map中的参数：
	 * type: Dataset/text
	 * id: 对应id
	 * writeFields: 写入字段
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public void pluginrefOkPlugin(Map<String, Object> map) {
		AppContext appCtx = AppSession.current().getAppContext();
		ViewPartMeta currWidget = appCtx.getCurrentWindowContext().getCurrentViewContext().getView(); 
		String type = (String) map.get(TYPE);
		String id = (String) map.get(ID);
//		JSONObject writeFieldsJson = null;
//		writeFieldsJson = (JSONObject) map.get(WRITEFIELDS);
		if (type.equals(TYPE_DS)){
			Map<String, String> valueMap = (Map<String, String>)map.get(WRITEFIELDS);
			Dataset ds = currWidget.getViewModels().getDataset(id);
			Row row = ds.getFocusRow();
			if(row==null){
				row=ds.getSelectedRow();
			}
			Iterator it =   valueMap.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				String value = valueMap.get(key); 
				if(ds.nameToIndex(key) == -1)
					continue;
				row.setValue(ds.nameToIndex(key), value);
			}
			ds.setCtxChanged(true);
		}
		//TODO
		else if (type.equals(TYPE_TEXT)){
			ReferenceComp comp =  (ReferenceComp)currWidget.getViewComponents().getComponent(id);
//			String key = (String)writeFieldsJson.get("key");
//			String value =  (String)writeFieldsJson.get("value");
			String key = (String) map.get("key");
			String value = (String) map.get("value"); 
			comp.setValue(key);
			comp.setShowValue(value);
		}
		//以下拉框方式打开的，关闭下拉框
		AppSession.current().getAppContext().addExecScript("if (parent.window.currentReferenceWithDivOpened) parent.window.currentReferenceWithDivOpened.hideRefDiv();");
		AppSession.current().getAppContext().closeWinDialog();
	}
}
