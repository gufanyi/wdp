package xap.lui.core.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.GenericChartData;
import xap.lui.core.dataset.MDComboDataConf;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.refmodel.BaseRefModel;
import xap.lui.core.refmodel.GridRefModel;
import xap.lui.core.refmodel.TreeGridRefModel;
import xap.lui.core.refmodel.TreeRefModel;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefNodeRelations;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.render.PCViewLayOutRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "DataModels")
@XmlAccessorType(XmlAccessType.NONE)
public class DataModels implements Cloneable, Serializable {
	private static final long serialVersionUID = 3628459738668095893L;
	@XmlElement(name = "DatasetRelations")
	private DatasetRelations dsrelations;
	private RefNodeRelations refnodeRelations;
	@XmlElementRefs({
		@XmlElementRef(name = "MdDataset", type = MdDataset.class),//
		@XmlElementRef(name = "RefMdDataset", type = RefMdDataset.class),//
		@XmlElementRef(name = "Dataset", type = Dataset.class) })
	private LuiSet<Dataset> datasetsList = new LuiHashSet<Dataset>();
	@XmlElementRefs({
		@XmlElementRef(name = "MDDataList", type = MDComboDataConf.class),//
		@XmlElementRef(name = "DataList", type = DataList.class)//
		//@XmlElementRef(name = "DyDataList", type = DynamicComboDataConf.class)
		})
	private LuiSet<ComboData> comboDataList = new LuiHashSet<ComboData>();
	@XmlElementWrapper(name = "RefNodes")
	@XmlElementRefs({
		@XmlElementRef(name = "GenericRefNode", type = GenericRefNode.class),//
		@XmlElementRef(name = "SelfRefNode", type = SelfDefRefNode.class) 
		})
	private LuiSet<IRefNode> refNodeList = new LuiHashSet<IRefNode>();
	
	@XmlElementWrapper(name="RefModels")
	@XmlElementRefs({
		@XmlElementRef(name = "GridRefModel", type = GridRefModel.class),
		@XmlElementRef(name = "TreeRefModel", type = TreeRefModel.class),
		@XmlElementRef(name = "TreeGridRefModel", type = TreeGridRefModel.class)
	})
	private LuiSet<BaseRefModel> refModelList = new LuiHashSet<BaseRefModel>();
	
	@XmlElementWrapper(name = "ChartModels")
	@XmlElementRefs({ 
		@XmlElementRef(name = "GenericChartData", type = GenericChartData.class)//
		})
	private LuiSet<GenericChartData> chartDataList = new LuiHashSet<GenericChartData>();
	
	private PCViewLayOutRender render = null;
	public PCViewLayOutRender getRender() {
		if (render == null) {
			ViewPartMeta webElement = this.getWidget();
			UIPartMeta uiPartMeta = LuiRenderContext.current().getUiPartMeta();
			UIViewPart uiEle = UIElementFinder.findUIWidget(uiPartMeta, webElement.getId());
			render = RenderProxy.getRender(new PCViewLayOutRender(uiEle));
		}
		return render;
	}

	public LuiSet<IRefNode> getRefNodeList() {
		return refNodeList;
	}

	public void setRefNodeList(LuiSet<IRefNode> refNodeList) {
		this.refNodeList = refNodeList;
	}

	private ViewPartMeta widget;

	public DataModels() {
	}

	public DataModels(ViewPartMeta widget) {
		super();
		this.widget = widget;
	}

	public DatasetRelations getDsrelations() {
		return dsrelations;
	}

	public void setDsrelations(DatasetRelations dsrelations) {
		dsrelations.setWidget(this.widget);
		this.dsrelations = dsrelations;
	}

	public Dataset getDataset(String id) {
		return this.datasetsList.find(id);
	}

	public Dataset[] getDatasets() {
		return this.datasetsList.toArray(new Dataset[0]);
	}

	public LifeCyclePhase getPhase() {
		return RequestLifeCycleContext.get().getPhase();
	}

	public Object clone() {
		try {
			DataModels viewModel = (DataModels) super.clone();
			if (this.dsrelations != null) {
				viewModel.dsrelations = (DatasetRelations) this.dsrelations.clone();
			}
			viewModel.datasetsList = new LuiHashSet<Dataset>();
			for (Dataset inner : this.datasetsList) {
				viewModel.datasetsList.add((Dataset) inner.clone());
			}
			viewModel.comboDataList = new LuiHashSet<ComboData>();
			if (this.comboDataList != null) {
				for (ComboData inner : this.comboDataList) {
					viewModel.comboDataList.add((ComboData) inner.clone());
				}
			}
			viewModel.refNodeList = new LuiHashSet<IRefNode>();
			if (this.refNodeList != null) {
				for (IRefNode inner : this.refNodeList) {
					viewModel.refNodeList.add((IRefNode) inner.clone());
				}
			}
			viewModel.refModelList = new LuiHashSet<BaseRefModel>();
			if (this.refModelList != null) {
				for (BaseRefModel inner : this.refModelList) {
					viewModel.refModelList.add((BaseRefModel) inner.clone());
				}
			}
			return viewModel;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}

	public void addDatasets(Dataset[] datasets) {
		for (Dataset inner : datasets) {
			this.addDataset(inner);
		}
	}

	public void addDataset(Dataset ds) {
		ds.setWidget(this.widget);
		this.datasetsList.add(ds);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addDataset(ds);
		}
	}
	
	public String addDataset(Dataset ds, boolean needRender) {
		ds.setWidget(this.widget);
		this.datasetsList.add(ds);
		if(needRender) {
			return this.getRender().generateDatasetScript(this.widget,ds);
		}
		return null;
	}

	public void merge(DataModels viewModel) {
		this.datasetsList.addAll(viewModel.getDatasetsList());
		this.dsrelations.addDsRelations(viewModel.getDsrelations());
		this.comboDataList.addAll(viewModel.comboDataList);
		this.refNodeList.addAll(viewModel.refNodeList);
	}

	public RefNodeRelations getRefNodeRelations() {
		return refnodeRelations;
	}

	public void setRefnodeRelations(RefNodeRelations refnodeRelations) {
		this.refnodeRelations = refnodeRelations;
	}

	public ComboData getComboData(String id) {
		return this.comboDataList.find(id);
	}
	
	public BaseRefModel getRefModel(String id) {
		return this.refModelList.find(id);
	}

	public ComboData[] getComboDatas() {
		return this.comboDataList.toArray(new ComboData[0]);
	}

	public void addComboData(ComboData comboData) {
		comboData.setWidget(this.widget);
		this.comboDataList.add(comboData);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addDataList(comboData);
		}
	}

	public void addRefNode(IRefNode refNode) {
		refNode.setWidget(this.widget);
		this.refNodeList.add(refNode);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addRefNode(refNode);
		}
	}
	
	public void addRefModel(BaseRefModel refModel) {
		refModel.setWidget(this.widget);
		this.refModelList.add(refModel);
	}

	public IRefNode getRefNode(String id) {
		return this.refNodeList.find(id);
	}

	public IRefNode[] getRefNodes() {
		return this.refNodeList.toArray(new IRefNode[0]);
	}

//	public void addRefModel(BaseRefModel refModel) {
//		refModel.setWidget(this.widget);
//		this.refModelList.add(refModel);
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			this.getRender().addRefNode(refModel);
//		}
//	}

	public LuiSet<BaseRefModel> getRefModelList() {
		return refModelList;
	}

	public void setRefModelList(LuiSet<BaseRefModel> refModelList) {
		this.refModelList = refModelList;
	}

	public void removeDataset(String id) {
		this.datasetsList.remove(id);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().removeDataset(id);
		}

	}

	public void removeComboData(String id) {
		ComboData com = this.comboDataList.find(id);
		this.comboDataList.remove(id);
		// this.getRender();
		// this.notify("notifyRemoveComboDataScript", com);
	}

	public void removeRefNode(String id) {
		this.refNodeList.remove(id);
	}

	public ViewPartMeta getWidget() {
		return widget;
	}

	public void setWidget(ViewPartMeta widget) {
		this.widget = widget;
		IRefNode[] refnodes = getRefNodes();
		for (int i = 0; i < refnodes.length; i++) {
			refnodes[i].setWidget(widget);
		}
		Dataset[] dss = getDatasets();
		for (int i = 0; i < dss.length; i++) {
			dss[i].setWidget(widget);
		}
		ComboData[] combs = getComboDatas();
		for (int i = 0; i < combs.length; i++) {
			combs[i].setWidget(widget);
		}
	}

	// private void notify(String type, Object obj) {
	// if (LifeCyclePhase.ajax.equals(getPhase())) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// String widgetId = this.getWidget().getId();
	// map.put("widgetId", widgetId);
	// map.put("type", type);
	// if (type.equals("notifyAddRefNode"))
	// map.put("iRefNode", obj);
	// else if (type.equals("notifyAddComboDataScript"))
	// map.put("comboData", obj);
	// else if (type.equals("notifyAddDataset"))
	// map.put("ds", obj);
	// else if (type.equals("notifyRemoveComboDataScript")) {
	// map.put("comboData", obj);
	// }
	// this.getWidget().notifyChange(UIElement.UPDATE, map);
	// }
	// }
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Dataset[] dss = this.getDatasets();
		if (dss != null) {
			buf.append("dataset:{");
			for (int i = 0; i < dss.length; i++) {
				buf.append(dss[i].getId());
				buf.append(",");
			}
			buf.append("},");
		}
		ComboData[] cbs = this.getComboDatas();
		if (cbs != null) {
			buf.append("combo:{");
			for (int i = 0; i < cbs.length; i++) {
				buf.append(cbs[i].getId());
				buf.append(",");
			}
			buf.append("},");
		}
		IRefNode[] rfs = this.getRefNodes();
		if (rfs != null) {
			buf.append("refnode:{");
			for (int i = 0; i < rfs.length; i++) {
				buf.append(rfs[i].getId());
				buf.append(",");
			}
			buf.append("}");
		}
		return buf.toString();
	}

	public LuiSet<Dataset> getDatasetsList() {
		return datasetsList;
	}

	public void setDatasetsList(LuiSet<Dataset> datasetsList) {
		this.datasetsList = datasetsList;
	}

	public LuiSet<ComboData> getComboDataList() {
		return comboDataList;
	}

	public void setComboDataList(LuiSet<ComboData> comboDataList) {
		this.comboDataList = comboDataList;
	}
}
