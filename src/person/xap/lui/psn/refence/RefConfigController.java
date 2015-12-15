package xap.lui.psn.refence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.mw.coreitf.d.FBoolean;

/**
 * 个性化设置中，编辑模型中参照配置的控制类
 * 
 * @author wupeng1
 */
public class RefConfigController implements IWindowCtrl, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;
	public static final String PARAMS_SRCWINDOW_ID = "sourceWinId";
	public static final String PARAMS_SRCVIEW_ID = "sourceView";

	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}

	/**
	 * 编辑完后，点击确定按钮，从数据集中取得参照的参数，更新参照属性
	 * 
	 * @param mouseEvent
	 */
	public void okBtnEvent(MouseEvent<MenuItem> mouseEvent) {
		BaseRefNode refNode = getOriginalRefNode();
		if(refNode==null){
			refNode=new SelfDefRefNode();
		}
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		Dataset ds = widget.getViewModels().getDataset("refcfgds");
		Row row = ds.getSelectedRow();
		int idIndex = ds.nameToIndex("refNodeId");
		// int dialogIndex = ds.nameToIndex("isDialog");
		int titleIndex = ds.nameToIndex("title");
		int refCodeIndex = ds.nameToIndex("refNodeCode");
		int wtDsIndex = ds.nameToIndex("writeDs");
		int wtfdsIndex = ds.nameToIndex("writeFields");
		int dlIndex = ds.nameToIndex("dataListener");
		int rdDsIndex = ds.nameToIndex("readDs");
		int rdfdsIndex = ds.nameToIndex("readFields");
		int pmIndex = ds.nameToIndex("pageModel");
		int pathIndex = ds.nameToIndex("pagePath");
		int widthIndex = ds.nameToIndex("dialogWidth");
		int heightIndex = ds.nameToIndex("dialogHeight");
		int orgsIndex = ds.nameToIndex("orgs");
		int fltRefNodeNames = ds.nameToIndex("filterRefNodeNames");
		// int refTypeIndex = ds.nameToIndex("refType");
		if (row != null) {
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			// 将ajax的状态置为nullstatus
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			if (refNode instanceof GenericRefNode) {
				GenericRefNode ncRefNode = (GenericRefNode) refNode;
				ncRefNode.setId((String) row.getValue(idIndex));
				ncRefNode.setController((String) row.getValue(dlIndex));
				ncRefNode.setReadDataset((String) row.getValue(rdDsIndex));
				ncRefNode.setReadFields((String) row.getValue(rdfdsIndex));
				ncRefNode.setWriteDataset((String) row.getValue(wtDsIndex));
				ncRefNode.setWriteFields((String) row.getValue(wtfdsIndex));
				ncRefNode.setTitle((String) row.getValue(titleIndex));
				ncRefNode.setPagemeta((String) row.getValue(pmIndex));
				ncRefNode.setRefcode((String) row.getValue(refCodeIndex));
				ncRefNode.setHeight((String) row.getValue(heightIndex));
				ncRefNode.setWidth((String) row.getValue(widthIndex));
				// ncRefNode.setOrgs(
				// row.getFBoolean(orgsIndex).booleanValue());
				// ncRefNode.setFilterRefNodeNames(row.getFBoolean(fltRefNodeNames).booleanValue());
			} else if (refNode instanceof SelfDefRefNode) {
				SelfDefRefNode sdRefNode = (SelfDefRefNode) refNode;
				sdRefNode.setId((String) row.getValue(idIndex));
				sdRefNode.setTitle((String) row.getValue(titleIndex));
				sdRefNode.setHeight((String) row.getValue(heightIndex));
				sdRefNode.setWidth((String) row.getValue(widthIndex));
				sdRefNode.setPath((String) row.getValue(pathIndex));
				sdRefNode.setWriteDs((String) row.getValue(wtDsIndex));
				sdRefNode.setWriteFields((String) row.getValue(wtfdsIndex));
			}
			{
				ViewPartMeta editorView = PaCache.getEditorViewPartMeta();
				editorView.getViewModels().addRefNode(refNode);
			}
			RequestLifeCycleContext.get().setPhase(phase);
		}
		AppSession.current().getAppContext().closeWinDialog();
	}

	public void cancelBtnEvent(MouseEvent<MenuItem> mouseEvent) {
		AppSession.current().getAppContext().closeWinDialog();
	}

	/**
	 * 加载参照配置的属性项
	 * 
	 * @param DatasetEvent
	 */
	public void onDataLoad_refcfgds(DatasetEvent DatasetEvent) {
		Dataset ds = DatasetEvent.getSource();
		BaseRefNode refNode = getOriginalRefNode();
		InitFormElement(refNode);
		Row row = ds.getEmptyRow();
		if (refNode != null) {
			row.setValue(ds.nameToIndex("refNodeId"), refNode.getId());
			row.setValue(ds.nameToIndex("isdialog"), FBoolean.TRUE);
			row.setValue(ds.nameToIndex("title"), refNode.getTitle());
			if (refNode instanceof GenericRefNode) {
				GenericRefNode ncRefNode = (GenericRefNode) refNode;
				row.setValue(ds.nameToIndex("refNodeCode"), ncRefNode.getRefcode());
				row.setValue(ds.nameToIndex("writeDs"), ncRefNode.getWriteDataset());
				row.setValue(ds.nameToIndex("refType"), "ncrefnode");
				row.setValue(ds.nameToIndex("writeFields"), ncRefNode.getWriteFields());
				row.setValue(ds.nameToIndex("dataListener"), ncRefNode.getController());
				row.setValue(ds.nameToIndex("readDs"), ncRefNode.getReadDataset());
				row.setValue(ds.nameToIndex("readFields"), ncRefNode.getReadFields());
				row.setValue(ds.nameToIndex("pageModel"), ncRefNode.getWinModel());
				row.setValue(ds.nameToIndex("filterRefNodeNames"), FBoolean.valueOf(ncRefNode.isFilterNames()));
			} else if (refNode instanceof SelfDefRefNode) {
				SelfDefRefNode sdRefNode = (SelfDefRefNode) refNode;
				row.setValue(ds.nameToIndex("pagePath"), sdRefNode.getPath());
				row.setValue(ds.nameToIndex("dialogHeight"), sdRefNode.getHeight());
				row.setValue(ds.nameToIndex("dialogWidth"), sdRefNode.getWidth());
				row.setValue(ds.nameToIndex("refType"), "selfrefnode");
				row.setValue(ds.nameToIndex("writeDs"), sdRefNode.getWriteDs());
				row.setValue(ds.nameToIndex("writeFields"), sdRefNode.getWriteFields());
			}
		}
		{
			row.setValue(ds.nameToIndex("refType"), "selfrefnode");
		}
		ds.addRow(row);
		ds.setEdit(true);
		ds.setRowSelectIndex(0);
	}

	// 根据和用户交互所得的参照类型，选择显示不同的属性
	private void InitFormElement(BaseRefNode refNode) {
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		FormComp form = (FormComp) widget.getViewComponents().getComponent("refcfgform");
		if (refNode == null) {
			FormElement formElement = form.getElementById("refNodeId");
			formElement.setEnabled(true);

			FormElement formElementCode = form.getElementById("refNodeCode");
			formElementCode.setVisible(false);
		}
		String[] selfFilter = { "refNodeId", "isdialog", "dialogWidth", "dialogHeight", "refType", "title", "pagePath", "writeDs", "writeFields" };
		List<String> selfProps = new ArrayList<String>();
		for (int i = 0; i < selfFilter.length; i++) {
			selfProps.add(selfFilter[i]);
		}
		List<FormElement> eleList = form.getElementList();
		if (refNode instanceof SelfDefRefNode) {
			for (int i = 0; i < eleList.size(); i++) {
				FormElement ele = eleList.get(i);
				if (!selfProps.contains(ele.getField()))
					ele.setVisible(false);
			}
		}
	}

	/**
	 * 获取OriginalRefNode
	 */
	public BaseRefNode getOriginalRefNode() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String refId = session.getOriginalParameter("refId");
		// PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		ViewPartMeta widget = PaCache.getEditorViewPartMeta();
		BaseRefNode refNode = (BaseRefNode) widget.getViewModels().getRefNode(refId);
		return refNode;
	}

	public ViewPartMeta getOriginalWidget() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		@SuppressWarnings("restriction")
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		IPaEditorService pes = new PaEditorServiceImpl();
		String pageId = session.getOriginalParamMap().get(PARAMS_SRCWINDOW_ID);
		String viewId = session.getOriginalParamMap().get(PARAMS_SRCVIEW_ID);
		PagePartMeta pagemeta = pes.getOriPageMeta(pageId, sessionId);
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		return widget;
	}
}
