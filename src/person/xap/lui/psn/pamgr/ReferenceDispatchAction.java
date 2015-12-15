package xap.lui.psn.pamgr;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.j2eesvr.Action;
import xap.lui.core.j2eesvr.BaseAction;
import xap.lui.core.j2eesvr.Servlet;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.refence.MockRefDataRefController;
import xap.lui.psn.refence.MockRefPageModel;
import xap.lui.psn.refence.MockTreeRefPageModel;

/**
 * Dispatch Request to right Reference
 * 
 * @author licza
 * 
 */
@Servlet(path = "/editor/reference")
public class ReferenceDispatchAction extends BaseAction {
	private static final String GRID_HEADER = "grid_header";
	private static final String STRING_EXT15 = "string_ext15";
	private static final String STRING_EXT14 = "string_ext14";
	private static final String OWNER = "owner";
	private static final String NODE_ID = "nodeId";
	private static final String REFERENCE_REFNODE_URL = "app/mockapp/cdref?model=" + MockRefPageModel.class.getName() + "&ctrl=" + MockRefDataRefController.class.getName();
	private static final String COMBODATA_REFNODE_URL = "app/mockapp/cdref?model=" + MockTreeRefPageModel.class.getName() + "&ctrl=" + MockRefDataRefController.class.getName();

	@Action
	public void dispatch() throws IOException {
		String sourceView = request.getParameter("sourceView");
		String sourceWin = request.getParameter(LuiRuntimeContext.DESIGNWINID);
		String eleid = request.getParameter("eleid");
		String subeleid = request.getParameter("subeleid");
		String type = request.getParameter("type");
		String sessionId = request.getSession().getId();
		IPaEditorService editorService =  new PaEditorServiceImpl();
		PagePartMeta pm = editorService.getOriPageMeta(sourceWin, sessionId);
		/**
		 * 删掉默认的写入字段.在程序中重新设置
		 */

		Map<String, String> param = buildParam();

		if (GRID_HEADER.equals(type)) {
			GridComp grid = (GridComp) pm.getWidget(sourceView).getViewComponents().getComponent(eleid);
			GridColumn column = (GridColumn) grid.getColumnById(subeleid);
			if (EditorTypeConst.REFERENCE.equals(column.getEditorType())) {
				setWriteDsField(param, STRING_EXT15);
				param.put("writeDs", grid.getDataset());
				gun(LuiRuntimeContext.getRootPath() + "/" + REFERENCE_REFNODE_URL + pingParam(param));

			} else if (EditorTypeConst.CHECKBOXGROUP.equals(column.getEditorType()) || EditorTypeConst.RADIOGROUP.equals(column.getEditorType())
					|| EditorTypeConst.COMBODATA.equals(column.getEditorType())) {
				setWriteDsField(param, STRING_EXT14);
				param.put("writeDs", grid.getDataset());
				gun(LuiRuntimeContext.getRootPath() + "/" + COMBODATA_REFNODE_URL + pingParam(param));
			} else {
				print("元素编辑类型不是参照或者下拉框!");
			}
		}

		if ("form_element".equals(type)) {
			FormComp form = (FormComp) pm.getWidget(sourceView).getViewComponents().getComponent(eleid);
			FormElement column = (FormElement) form.getElementById(subeleid);
			if (EditorTypeConst.REFERENCE.equals(column.getEditorType())) {
				setWriteDsField(param, "string_ext11");
				param.put("writeDs", form.getDataset());
				gun(LuiRuntimeContext.getRootPath() + "/" + REFERENCE_REFNODE_URL + pingParam(param));

			} else if (EditorTypeConst.CHECKBOXGROUP.equals(column.getEditorType()) || EditorTypeConst.RADIOGROUP.equals(column.getEditorType())
					|| EditorTypeConst.COMBODATA.equals(column.getEditorType())) {
				setWriteDsField(param, "string_ext12");
				param.put("writeDs", form.getDataset());
				gun(LuiRuntimeContext.getRootPath() + "/" + COMBODATA_REFNODE_URL + pingParam(param));

			} else {
				print("元素编辑类型不是参照或者下拉框!");
			}
		}
	}

	private void setWriteDsField(Map<String, String> param, String field) {
		param.put(OWNER, field);
	}

	private String pingParam(Map<String, String> paramMap) {
		StringBuffer sb = new StringBuffer();
		Set<String> keyset = paramMap.keySet();
		if (keyset != null && !keyset.isEmpty()) {
			for (String ke : keyset) {
				String val = paramMap.get(ke);
				if (val != null && !("".equals(val))) {
					sb.append("&").append(ke).append("=").append(val);
				}
			}
		}
		return sb.toString();
	}

	private Map<String, String> buildParam() {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			map.put(paramName, request.getParameter(paramName));
		}
		return map;
	}

}
