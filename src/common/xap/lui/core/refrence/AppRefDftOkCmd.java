package xap.lui.core.refrence;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.PropertyGridComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.comps.WebTreeNode;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPlugoutCmd;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.refmodel.BaseRefModel;
public class AppRefDftOkCmd extends LuiCommand {
	private Dataset currentDs;
	public AppRefDftOkCmd(Dataset ds) {
		super();
		this.currentDs = ds;
	}
	@Override
	public void execute() {
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("widgetId");
		if (StringUtils.isBlank(widgetId)) {
			widgetId = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter(LuiWebContext.WIDGET_ID);
		}
		String refNodeId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("refNodeId");
		if (StringUtils.isBlank(refNodeId)) {
			refNodeId = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter(LuiWebContext.REF_NODE_ID);
		}
		String parentPageId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("otherPageId");
		PagePartMeta parentPm = AppSession.current().getAppContext().getWindowContext(parentPageId).getPagePartMeta();
		GenericRefNode rfnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refNodeId);
		Row[] currRows = null;
		if (rfnode.isMultiple()) {
			currRows = currentDs.getAllSelectedRows();
		} else {
			Row currRowOnly = currentDs.getSelectedRow();
			if (currRowOnly == null) {
				AppSession.current().getAppContext().closeWinDialog();
				return;
			}
			currRows = new Row[] {
				currRowOnly
			};
		}
		boolean selLeafOnly = rfnode.isOnlyLeaf();
		WebComp tree = LuiAppUtil.getCntView().getViewComponents().getComponent("reftree");
		if (currRows == null) {
			return;
		}
		if (tree != null && selLeafOnly) {
			for (int i = 0; i < currRows.length; i++) {
				Row currRow = currRows[i];
				if (((TreeViewComp) tree).getTreeModel() != null) {
					WebTreeNode node = ((TreeViewComp) tree).getTreeModel().getTreeNodeByRowId(currRow.getRowId());
					if (node != null) {
						if (node.getChildNodeList() != null && node.getChildNodeList().size() > 0) // 非叶子节点
							return;
					}
				}
			}
		}
		//处理propertyGrid
		if(StringUtils.isNotBlank(rfnode.getPropertyGridId())) {
			rfnode.setWriteDataset(PropertyGridComp.PROPERTYDATASET_PREFIX + rfnode.getPropertyGridId());
		}
		
		if (rfnode.getWriteDataset() == null || "".equals(rfnode.getWriteDataset())) { // 未绑定dataset
			processTextOk(widgetId, parentPm, rfnode, currRows);
		} else {
			processDatasetOk(widgetId, parentPm, rfnode, currRows);
		}
		clearRefRelationData(widgetId, parentPm, rfnode);
		refClose();
	}
	protected void clearRefRelationData(String widgetId, PagePartMeta parentPm, GenericRefNode rfnode) {
		if (parentPm.getWidget(widgetId).getViewModels().getRefNodeRelations() != null) {
			String writeFieldStr = rfnode.getWriteFields();
			String[] writeFields = writeFieldStr.split(",");
			Map<String, RefNodeRelation> refNodeRelMap = parentPm.getWidget(widgetId).getViewModels().getRefNodeRelations().getRefnodeRelations();
			for (Iterator<String> itwd = refNodeRelMap.keySet().iterator(); itwd.hasNext();) {
				String id = itwd.next();
				RefNodeRelation refNodeRel = refNodeRelMap.get(id);
				List<MasterFieldInfo> masterFieldInfoList = refNodeRel.getMasterFieldInfos();
				for (int i = 0; i < masterFieldInfoList.size(); i++) {
					MasterFieldInfo masterFieldInfo = masterFieldInfoList.get(i);
					if (masterFieldInfo.getDsId() == null || masterFieldInfo.getFieldId() == null)
						continue;
					if (masterFieldInfo.getDsId().equals(rfnode.getWriteDataset()) && masterFieldInfo.getFieldId().equals(writeFields[0])) {
						String detailRefNodeid = refNodeRel.getDetailRefNode();
						GenericRefNode relatedRefNode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(detailRefNodeid);
						if (relatedRefNode != null) {
							Dataset relatedWriteDs = parentPm.getWidget(widgetId).getViewModels().getDataset(relatedRefNode.getWriteDataset());
							String relatedwriteFieldStr = relatedRefNode.getWriteFields();
							String[] relatedwriteFields = relatedwriteFieldStr.split(",");
							Row relatedRow = relatedWriteDs.getSelectedRow();
							if (relatedRow != null) {
								for (int j = 0; j < relatedwriteFields.length; j++) {
									relatedRow.setValue(relatedWriteDs.nameToIndex(relatedwriteFields[j]), "");
								}
							}
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 处理数据集之上的参照
	 * 
	 * @param widgetId
	 * @param parentPm
	 * @param rfnode
	 * @param currRow
	 */
	protected void processDatasetOk(String widgetId, PagePartMeta parentPm, GenericRefNode rfnode, Row[] currRows) {
		String readFieldStr = rfnode.getReadFields();
		if (readFieldStr == null || readFieldStr.length() == 0) {
			if(rfnode.isDefModel()){
				BaseRefModel model = RefSelfUtil.getDefRefModel(rfnode.getRefcode());
				readFieldStr = model.getPkField();
			}else{
				IRefModel rm = RefSelfUtil.getRefModel(rfnode);
				readFieldStr = rm.getPkFieldCode();
			}
		}
		String[] readFields = readFieldStr.split(",");
		if (readFieldStr.indexOf("(") != -1 || readFieldStr.indexOf(")") != -1) {
			if (readFields != null && readFields.length > 2) {
				if (readFields.length == 3) {
					int lastSplitIndex = readFieldStr.lastIndexOf(",");
					String lastStr = readFieldStr.substring(lastSplitIndex + 1);
					if (lastStr.indexOf(")") != -1) {
						int firstSplitIndex = readFieldStr.indexOf(",");
						readFields[0] = readFieldStr.substring(0, firstSplitIndex);
						readFields[1] = readFieldStr.substring(firstSplitIndex + 1);
					} else {
						readFields[0] = readFieldStr.substring(0, lastSplitIndex);
						readFields[1] = readFieldStr.substring(lastSplitIndex + 1);
					}
					readFields[2] = null;
				}
			}
		}
		String writeFieldStr = rfnode.getWriteFields();
		String[] writeFields = writeFieldStr.split(",");
		// 绑定了dataset
		Dataset wds = parentPm.getWidget(widgetId).getViewModels().getDataset(rfnode.getWriteDataset());
		// Row row = wds.getSelectedRow();
		Row row = wds.getSelectedRow();
		if (row == null)
			row = wds.getFocusRow();
		Map<String, String> valueMap = new LinkedHashMap<String, String>();// 顺序放入父子参照
		for (int i = 0; i < writeFields.length; i++) {
			String value = "";
			for (int j = 0; j < currRows.length; j++) {
				Row currRow = currRows[j];
				if (currRow != null) {
					String readField = readFields[i];
					if (readField != null && readField.indexOf(" as ") != -1) {
						int asIndex = readField.indexOf(" as ");
						readField = readField.substring(asIndex + 4).replaceAll("\\.", "_");
					} else if (readField != null && readField.indexOf(".") != -1) {
						readField = readField.split("\\.")[1].replaceAll("\\.", "_");
					}
					int index = currentDs.nameToIndex(readField);
					if (index == -1) {
						if (currentDs != null) {
							Field[] fields = currentDs.getFields();
							if (fields != null) {
								StringBuffer fs = new StringBuffer();
								for (Field f : fields) {
									fs.append(f.getId() + " || ");
								}
								LuiLogger.error("All Fields Id : " + fs.toString());
							}
						}
						throw new LuiRuntimeException("没有找到列:" + readFields[i] + ":" + readField);
					}
					if (currRow.getValue(index) == null)
						continue;
					if (j != currRows.length - 1)
						value += currRow.getValue(index) + ",";
					else
						value += currRow.getValue(index);
				}
			}
			valueMap.put(writeFields[i], value);
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("type", "Dataset");
		paramMap.put("id", rfnode.getWriteDataset());
		paramMap.put("writeFields", valueMap);
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd("main", "refOkPlugout", paramMap);
		uifPluOutCmd.execute();
	}
	/**
	 * 处理单输入框的参照
	 * 
	 * @param widgetId
	 * @param parentPm
	 * @param rfnode
	 * @param currRow
	 */
	protected void processTextOk(String widgetId, PagePartMeta parentPm, GenericRefNode rfnode, Row[] currRows) {
		String owner = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("owner");
		// 获取显示值和真实值
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String rfStr = null;
		String[] rfs = null;
		String returnField = null;
		String pkField = null;
		String codeField = null;
		String nameField = null;
		if(rfnode.isDefModel()){
			BaseRefModel rm = RefSelfUtil.getDefRefModel(rfnode.getRefcode());
			rfStr = rfnode.getReadFields();
			rfs = rfStr.split(",");
			pkField = rm.getPkField();
			codeField = rm.getCodeField();
			nameField = rm.getNameField();
		}else{
			IRefModel rm = RefSelfUtil.getRefModel(rfnode);
			rfStr = rfnode.getReadFields();
			rfs = rfStr.split(",");
			pkField = rm.getPkFieldCode();
			codeField = rm.getRefCodeField();
			nameField = rm.getRefNameField();
		}
		for (int i = 0; i < rfs.length; i++) {
			if (!rfs[i].equals(pkField)) {
				if (rfs[i].equals(codeField) || rfs[i].equals(nameField)) {
					returnField = rfs[i];
					break;
				}
			}
		}
		if (returnField == null)
			returnField = nameField;
		paramMap.put("type", "Text");
		paramMap.put("id", owner);
		if (currRows != null) {
			String value = "";
			String showValue = "";
			if (pkField.indexOf(".") != -1) {
				pkField = pkField.split("\\.")[1].replaceAll("\\.", "_");
			}
			if (returnField.indexOf(".") != -1) {
				returnField = returnField.split("\\.")[1].replaceAll("\\.", "_");
			}
			for (int i = 0; i < currRows.length; i++) {
				Row currRow = currRows[i];
				if (currRow == null)
					continue;
				if (i != currRows.length - 1) {
					value += (String) currRow.getValue(currentDs.fieldToIndex(pkField)) + ",";
					showValue += (String) currRow.getValue(currentDs.fieldToIndex(returnField)) + ",";
				} else {
					value += (String) currRow.getValue(currentDs.nameToIndex(pkField));
					showValue += (String) currRow.getValue(currentDs.nameToIndex(returnField));
				}
			}
			paramMap.put("key", value);
			paramMap.put("value", showValue);
		}
		LuiPlugoutCmd uifPluOutCmd = new LuiPlugoutCmd("main", "refOkPlugout", paramMap);
		uifPluOutCmd.execute();
	}
	protected void refClose() {
		// 隐藏Div
		// getGlobalContext().addExecScript("parent.document.onclick();");
		// AppLifeCycleContext.current().getApplicationContext().addExecScript("if (parent.window.currentReferenceWithDivOpened) parent.window.currentReferenceWithDivOpened.hideRefDiv();");
		// 关闭Dialog
		// AppLifeCycleContext.current().getApplicationContext().closeWinDialog();
		// 关闭Excel控件打开的参照对话框
		// getGlobalContext().addExecScript("if (parent.window.parentPage.excelComp) parent.window.parentPage.ExcelComp.closeRefDialog();");
	}
}
