package xap.lui.psn.refence;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.control.ViewController;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPlugoutCmd;


/**
 * 自定义CSS样式的Controller
 * 
 * @author liujmc
 * @date 2012-06-12
 */
public class CssStyleViewController implements ViewController {
	private static final String DS_MIDDLE = "ds_middle";
	private static final String DATASET = "Dataset";
	private static final String ID = "id";
	private static final String TYPE = "type";
	private static final String WRITE_FIELDS = "writeFields";
	private static final String MAIN = "main";
	private static final String REF_OK_PLUGOUT = "refOkPlugout";
	
	/**
	 * 数据加载
	 * @param DatasetEvent se
	 */
	public void onDataLoad(DatasetEvent se){
		
	}
	
	/**
	 * 设计器确定按钮点击事件
	 * @param MouseEvent e
	 */
	public void onOkEvent(ScriptEvent e){
		LuiWebSession webSession = LuiRuntimeContext.getWebContext().getPageWebSession();
		String callBackId = webSession.getOriginalParameter("nodeId");	
		AppSession ctx = AppSession.current();
		String cssStr = ctx.getParameter("cssStr");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(TYPE, DATASET);
		paramMap.put(ID, DS_MIDDLE);
		
	
		Map<String, String> writeFields = new HashMap<String, String>();
		writeFields.put(callBackId, cssStr);
		paramMap.put(WRITE_FIELDS, writeFields);
		
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd(MAIN,REF_OK_PLUGOUT,paramMap);
		uifPluOutCmd.execute();		
	}
	
	/**
	 * 取消按钮点击事件，关闭窗口
	 * @param MouseEvent e
	 */
	public void onCancelEvent(MouseEvent<ButtonComp> e){
		AppSession.current().getAppContext().closeWinDialog();
	}
	
}
