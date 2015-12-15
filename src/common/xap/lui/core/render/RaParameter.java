package xap.lui.core.render;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.PagePartMeta;


/**
 * 界面调整参数
 * 
 * @author licza
 */
public class RaParameter implements Serializable, Cloneable {
	private static final long serialVersionUID = -7643567666073469299L;
	
	public static final String PARAM_CURRENT_DROP_DS_ID = "currentDropDsId";
	public static final String PARAM_CURRENT_DROP_PID = "currentDropPid";
	public static final String PARAM_CURRENT_DROP_OBJ_TYPE2 = "currentDropObjType2";
	public static final String PARAM_CURRENT_DROP_OBJ_TYPE = "currentDropObjType";
	public static final String PARAM_CURRENT_DROP_OBJ = "currentDropObj";
	public static final String PARAM_QUERY_KEYVALUE = "query_keyvalue";
	public static final String PARAM_SUBELEID = "subeleid";
	public static final String PARAM_TYPE = "type";
	public static final String PARAM_SUBUIID = "subuiid";
	public static final String PARAM_UIID = "uiid";
	public static final String PARAM_ELEID = "eleid";
	public static final String PARAM_WIDGETID = "widgetid";
	public static final String PARAM_OPER = "oper";
	public static final String PARAM_ROWINDEX = "rowindex";
	public static final String PARAM_COLINDEX = "colindex";
	public static final String INIT = "init";
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String PRTID = "parentid";
	public static final String COMPTYPE = "comptype";
	public static final String ATTR = "attr";
	public static final String NEWVALUE = "newvalue";
	public static final String OLDVALUE = "oldvalue";
	public static final String ATTRTYPE = "attrtype";
	public static final String PARAM_COMPID = "compid";
	public static final String OFFSET_X="offsetX";
	public static final String OFFSET_Y="offsetY";
	public static final String PAGE_X="offsetX";
	public static final String PAGE_Y="offsetY";
	public static final String CLIENT_X="clientX";
	public static final String CLIENT_Y="clientY";
	public static final String DROP_X="dropX";
	public static final String DROP_Y="dropY";
	
	public static final String NAN = "NAN";
	
	private String widgetId;
	private String eleId;
	private String uiId;
	private String subuiId;
	private String type;
	private String subEleId;
	private String currentDropObj;
	private String currentDropObjType;
	private String currentDropObjType2;
	private String currentDropDsId;
	private String currentDropPid;
	private String queryKeyValue;
	private String colIndex;
	private String rowIndex;
	private AppSession lpc;
	private PagePartMeta pageMeta;
	private UIPartMeta uiMeta;
	private UpdateParameter param;
	
	private String offsetX;
	private String offsetY;
	private String pageX;
	private String pageY;
	private String clientX;
	private String clientY;
	private String dropX;
	private String dropY;
	/**
	 * 构造方法
	 * 
	 * @param lpc
	 */
	public RaParameter(AppSession lpc) {
		this.widgetId = replaceNullString(lpc.getParameter(PARAM_WIDGETID));
		this.eleId = replaceNullString(lpc.getParameter(PARAM_ELEID));
		this.uiId = replaceNullString(lpc.getParameter(PARAM_UIID));
		this.subuiId = replaceNullString(lpc.getParameter(PARAM_SUBUIID));
		this.type = replaceNullString(lpc.getParameter(PARAM_TYPE));
		this.subEleId = replaceNullString(lpc.getParameter(PARAM_SUBELEID));
		this.currentDropObj = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ));
		this.currentDropObjType = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ_TYPE));
		this.currentDropObjType2 = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_OBJ_TYPE2));
		this.currentDropDsId = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_DS_ID));
		this.currentDropPid = replaceNullString(lpc.getParameter(PARAM_CURRENT_DROP_PID));
		this.queryKeyValue = replaceNullString(lpc.getParameter(PARAM_QUERY_KEYVALUE));
		this.colIndex = replaceNullString(lpc.getParameter(PARAM_COLINDEX));
		this.rowIndex = replaceNullString(lpc.getParameter(PARAM_ROWINDEX));
		this.pageMeta = LuiRuntimeContext.getWebContext().getPageMeta();
		this.uiMeta = (UIPartMeta) LuiRuntimeContext.getWebContext().getUIMeta();
		this.lpc = lpc;
		
		this.offsetX =replaceNullInteger(lpc.getParameter(OFFSET_X));
		this.offsetY=replaceNullInteger(lpc.getParameter(OFFSET_Y));
		this.pageX=replaceNullInteger(lpc.getParameter(PAGE_X));
		this.pageY=replaceNullInteger(lpc.getParameter(PAGE_Y));
		this.clientX=replaceNullInteger(lpc.getParameter(CLIENT_X));
		this.clientY=replaceNullInteger(lpc.getParameter(CLIENT_Y));
		
		this.dropX=replaceNullInteger(lpc.getParameter(DROP_X));
		this.dropY=replaceNullInteger(lpc.getParameter(DROP_Y));
		
		String prtId = replaceNullString(lpc.getParameter(PRTID));
		String compType = replaceNullString(lpc.getParameter(COMPTYPE));
		String attr = replaceNullString(lpc.getParameter(ATTR));
		String attrType = replaceNullString(lpc.getParameter(ATTRTYPE));
		String oldValue = replaceNullString(lpc.getParameter(OLDVALUE));
		if(oldValue != null){
			try {
				oldValue = URLDecoder.decode(oldValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		String newValue = replaceNullString(lpc.getParameter(NEWVALUE));
		if(newValue != null){
			try {
				newValue = URLDecoder.decode(newValue, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		String compId = replaceNullString(lpc.getParameter(PARAM_COMPID));
		
		/*
		proxy.addParam("compid", obj.compid); // 前台组件ID
		proxy.addParam("prtid", obj.prtid); //父节点ID
		proxy.addParam("viewid", obj.viewid);
		proxy.addParam("comptype", obj.type); // 前台组件类型
		proxy.addParam("attr", obj.attr); // 前台属性名
		proxy.addParam("attrtype", obj.attrtype); // 属性类型
		proxy.addParam("oldvalue", obj.oldvalue); // 修改前的值
		proxy.addParam("newvalue", obj.newvalue); // 修改后的值
		*/
		
		this.param = new UpdateParameter();
		param.setCompId(compId);
		param.setPrtId(prtId);
		param.setCompType(compType);
		param.setAttr(attr);
		param.setAttrType(attrType);
		param.setOldValue(oldValue);
		param.setNewValue(newValue);
		param.setViewId(widgetId);
	}

	public String getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(String widgetId) {
		this.widgetId = widgetId;
	}

	public String getEleId() {
		return eleId;
	}

	public void setEleId(String eleId) {
		this.eleId = eleId;
	}

	public String getUiId() {
		return uiId;
	}

	public void setUiId(String uiId) {
		this.uiId = uiId;
	}

	public String getSubuiId() {
		return subuiId;
	}

	public void setSubuiId(String subuiId) {
		this.subuiId = subuiId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubEleId() {
		return subEleId;
	}

	public void setSubEleId(String subEleId) {
		this.subEleId = subEleId;
	}

	public String getCurrentDropObj() {
		return currentDropObj;
	}

	public void setCurrentDropObj(String currentDropObj) {
		this.currentDropObj = currentDropObj;
	}

	public String getCurrentDropObjType() {
		return currentDropObjType;
	}

	public void setCurrentDropObjType(String currentDropObjType) {
		this.currentDropObjType = currentDropObjType;
	}

	public String getCurrentDropObjType2() {
		return currentDropObjType2;
	}

	public void setCurrentDropObjType2(String currentDropObjType2) {
		this.currentDropObjType2 = currentDropObjType2;
	}
	public String getCurrentDropPid() {
		return currentDropPid;
	}

	public void setCurrentDropPid(String currentDropPid) {
		this.currentDropPid = currentDropPid;
	}
	
	public String getQueryKeyValue() {
		return queryKeyValue;
	}

	public void setQueryKeyValue(String queryKeyValue) {
		this.queryKeyValue = queryKeyValue;
	}

	public String getCurrentDropDsId() {
		return currentDropDsId;
	}

	public void setCurrentDropDsId(String currentDropDsId) {
		this.currentDropDsId = currentDropDsId;
	}

	public String getColIndex() {
		return colIndex;
	}

	public void setColIndex(String colIndex) {
		this.colIndex = colIndex;
	}

	public String getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}

	public AppSession getLpc() {
		return lpc;
	}

	public void setLpc(AppSession lpc) {
		this.lpc = lpc;
	}

	public PagePartMeta getPageMeta() {
		return pageMeta;
	}

	public void setPageMeta(PagePartMeta pageMeta) {
		this.pageMeta = pageMeta;
	}

	public UIPartMeta getUiMeta() {
		return uiMeta;
	}

	public void setUiMeta(UIPartMeta uiMeta) {
		this.uiMeta = uiMeta;
	}

	public UpdateParameter getParam() {
		return param;
	}

	public void setParam(UpdateParameter param) {
		this.param = param;
	}
	
	public String getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(String offsetX) {
		this.offsetX = offsetX;
	}

	public String getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(String offsetY) {
		this.offsetY = offsetY;
	}

	public String getPageX() {
		return pageX;
	}

	public void setPageX(String pageX) {
		this.pageX = pageX;
	}

	public String getPageY() {
		return pageY;
	}

	public void setPageY(String pageY) {
		this.pageY = pageY;
	}

	public String getClientX() {
		return clientX;
	}

	public void setClientX(String clientX) {
		this.clientX = clientX;
	}

	public String getClientY() {
		return clientY;
	}

	public void setClientY(String clientY) {
		this.clientY = clientY;
	}

	public String getDropX() {
		return dropX;
	}

	public void setDropX(String dropX) {
		this.dropX = dropX;
	}

	public String getDropY() {
		return dropY;
	}

	public void setDropY(String dropY) {
		this.dropY = dropY;
	}

	private String replaceNullString(String arg) {
		if (arg != null) {
			if (arg.equals("null") || arg.equals(""))
				arg = null;
		}
		return arg;
	}

	private String replaceNullInteger(String arg){
		if(StringUtils.isNotBlank(arg)&&arg.matches("^[+-]?\\d+(,[+-]?\\d+)*$")){
			return arg;
		}else{
			return NAN;
		}
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
