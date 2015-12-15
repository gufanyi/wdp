package xap.lui.psn.refence;
import java.util.HashMap;
import java.util.Map;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.mock.MockTreeViewController;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.SelfDefRefNode;

public class MockSelfBasicDocRefController extends MockTreeViewController {
	private static final String CURRENT_WIDGET = "CURRENT_WIDGET";
	@Override
	public void dataLoad(DatasetEvent e) {
//		WebSession webSession = LuiRuntimeEnvironment.getWebContext().getWebSession();
//		String code = webSession.getOriginalParameter("basedoccode");
//		Dataset ds = e.getSource();
//		ds.clear();
//		Row row = ds.getEmptyRow();
//		row.setValue(0, CURRENT_WIDGET);
//		row.setValue(1, "");
//		row.setValue(2, "自定义档案 ");
//		ds.addRow(row);
//		IDefdocQryService service = NCLocator.getInstance().lookup(IDefdocQryService.class);
//		String pk_org = "";
//		String pk_group = "";
//		try {
//			IDefdoclistQryService ser = NCLocator.getInstance().lookup(IDefdoclistQryService.class);
//			DefdoclistVO docListVo = ser.queryDefdoclistVOByPk(code);
//			int mng = docListVo.getMngctlmode();
//			if (mng == 0) {
//				pk_org = "GLOBLE00000000000000";
//				pk_group = "GLOBLE00000000000000";
//			} else if (mng == 1) {
//				pk_org = LuiRuntimeEnvironment.getLuiSessionBean().getPk_unit();
//				pk_group = LuiRuntimeEnvironment.getLuiSessionBean().getPk_unit();
//			} else if (mng == 2 || mng == 3) {
//				pk_org = (String) AppLifeCycleContext.current().getApplicationContext().getAppAttribute("Mode_MainOrg");
//				pk_group = LuiRuntimeEnvironment.getLuiSessionBean().getPk_unit();
//			}
//			DefdocVO[] vos = service.queryDefdocVOsByDoclistPk(code, pk_org, pk_group);
//			if (vos != null && vos.length > 0) {
//				for (int i = 0; i < vos.length; i++) {
//					DefdocVO vo = vos[i];
//					row = ds.getEmptyRow();
//					row.setValue(0, vo.getPk_defdoc());
//					row.setValue(1, CURRENT_WIDGET);
//					row.setValue(2, vo.getName());
//					ds.addRow(row);
//				}
//			}
//		} catch (BusinessException e1) {
//			LuiLogger.error(e1.getMessage(), e1);
//		}
	}
	public void onAfterRowSelect(DatasetEvent e) {}
	@Override
	public void okButtonClick(MouseEvent e) {
		String owner = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("owner");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer valueBuff = new StringBuffer();
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("widgetId");
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("nodeId");
		String parentPageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("otherPageId");
		PagePartMeta parentPm = AppSession.current().getAppContext().getWindowContext(parentPageId).getPagePartMeta();
		SelfDefRefNode rfnode = (SelfDefRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		String writeFields = rfnode.getWriteFields();
		if (writeFields != null && writeFields.length() > 0) {
			paramMap.put("type", "Dataset");
			paramMap.put("id", rfnode.getWriteDs());
		} else {
			paramMap.put("type", "Text");
			paramMap.put("id", owner);
		}
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset currentDs = widget.getViewModels().getDataset("masterDs");;
		Row[] currRows = null;
		// 支持多选
		currRows = currentDs.getAllSelectedRows();
		String value = "";
		if (currRows != null) {
			String showValue = "";
			for (int i = 0; i < currRows.length; i++) {
				Row currRow = currRows[i];
				if (i != currRows.length - 1) {
					value += (String) currRow.getValue(currentDs.nameToIndex("id")) + ",";
					showValue += (String) currRow.getValue(currentDs.nameToIndex("label")) + ",";
				} else {
					value += (String) currRow.getValue(currentDs.nameToIndex("id"));
					showValue += (String) currRow.getValue(currentDs.nameToIndex("label"));
				}
			}
			if (currRows.length > 1) {
				showValue = " 多项 ";
			}
			if (writeFields != null) {
				Map<String, String> valueMap = new HashMap<String, String>();
				String[] wf = writeFields.split(",");
				if (wf.length > 1) {
					valueMap.put(wf[0], showValue);
					valueMap.put(wf[1], value);
				}
				paramMap.put("writeFields", valueMap);
			} else {
				paramMap.put("key", value);
				paramMap.put("value", showValue);
			}
			// paramMap.put("writeFields", valueMap);
		}
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd("main", "refOkPlugout", paramMap);
		uifPluOutCmd.execute();
	}
}
