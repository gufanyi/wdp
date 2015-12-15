package xap.lui.psn.refence;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.control.ViewController;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.ViewPartMeta;

/**
 * 边框样式后台监控类
 * @author wupeng1
 */
public class BorderStyleViewController implements ViewController {
	private static final String DS_MIDDLE = "ds_middle";
	private static final String DATASET = "Dataset";
	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	
	/**
	 * 点击确定，获取表单数据，并用plugout写入到对应的数据集中
	 * @param e
	 */
	public void onOkEvent(MouseEvent<ButtonComp> e){
		//LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String callBackId = AppSession.current().getParameter("nodeId");		
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs =  widget.getViewModels().getDataset("masterDs");
		Row currRow =  currentDs.getSelectedRow();
		String borderWidth = (String) currRow.getValue(currentDs.nameToIndex("borderWidth"));
		String borderColor = (String) currRow.getValue(currentDs.nameToIndex("borderColor"));
		String borderStyle = (String) currRow.getValue(currentDs.nameToIndex("borderStyle"));
		String result = borderWidth+"px "+borderColor+" "+borderStyle;
		
		//拼接样式字符串
		String backValue = result;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, DS_MIDDLE);
	
		Map<String, String> writeFields = new HashMap<String, String>();
		writeFields.put(callBackId, backValue);
		paramMap.put(WRITE_FIELDS, writeFields);
		
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();		
		
	}
	
	public void onCancelEvent(MouseEvent<ButtonComp> e){
		AppSession.current().getAppContext().closeWinDialog();
	}
	
	/**
	 * 数据加载方法，将所选控件的方法填充到当前表单中
	 * @param se
	 */
	public void onDataLoad(DatasetEvent se){
		
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		
		String preValue = webSession.getOriginalParameter("param");
		Dataset currentDs =  widget.getViewModels().getDataset("masterDs");
		Row currRow = currentDs.getEmptyRow();
		if(!StringUtils.isBlank(preValue)){
			String[] values = preValue.split(" ");
			for (int i = 0; i < values.length; i++) {
				String attrValue = values[i];
				if(!StringUtils.isBlank(attrValue)){
					if(attrValue.contains("px")){
						currRow.setValue(currentDs.nameToIndex("borderWidth"),attrValue.replace("px", ""));
					}else if("black,red,blue,green,gray".contains(attrValue)){
						currRow.setValue(currentDs.nameToIndex("borderColor"), attrValue);
					}else if("none,dotted,dashed,solid,double".contains(attrValue)){
						currRow.setValue(currentDs.nameToIndex("borderStyle"), attrValue);
					}
				}
			}
			/*
			try {
				currRow.setValue(currentDs.nameToIndex("borderWidth"),values[0]);
				currRow.setValue(currentDs.nameToIndex("borderColor"), values[1]);
				currRow.setValue(currentDs.nameToIndex("borderStyle"), values[2]);
			} catch (Exception e) {
				//忽略越界异常
			}
			*/
		}
		currentDs.addRow(currRow);
		currentDs.setEdit(true);
		currentDs.setRowSelectIndex(0);
	}
}
