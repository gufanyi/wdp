
package xap.lui.core.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IDataBinding;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.WidgetUIContext;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.MdDataset;
import xap.lui.core.dataset.RefMdDataset;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.util.JaxbMarshalFactory;
import xap.lui.core.util.JsURLDecoder;


@XmlRootElement(name = "ViewPart")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewPartMeta extends WebComp {
	private static final long serialVersionUID = -750733078068300535L;
	// 片段对外输出的时间常量。
	public static final String UNIQUE_TS = ExtAttriSupport.DYN_ATTRIBUTE_KEY + "$UNIQUE_TS";
	// 记录本片段最后修改时间的常量
	public static final String MODIFY_TS = ExtAttriSupport.DYN_ATTRIBUTE_KEY + "$MODIFY_TS";
	// 记录本片段唯一标识的常量
	public static final String UNIQUE_ID = ExtAttriSupport.DYN_ATTRIBUTE_KEY + "$UNIQUE_ID";
	public static final String WIDGET_CAPTION = "WIDGET_CAPTION";
	public static final String BODYINFO = "$BODYINFO";
	public static final String POOLWIDGET = "$POOLWIDGE";
	private String editFormularClazz;
	// 视图模型
	@XmlElement(name = "DataModels")
	private DataModels viewModels = new DataModels(this);

	// 视图控件为对应jaxb序列方式
	@XmlElement(name = "Controls")
	private ViewPartComps viewComponents = new ViewPartComps(this);

	@XmlElement(name = "Menus")
	private ViewPartMenus viewMenus = new ViewPartMenus(this);

	@XmlAttribute(name = "isDialog")
	private boolean isDialog = false;
	private String caption;
	private PagePartMeta pagemeta;
	@XmlAttribute
	private String refId;
	@XmlAttribute(name = "isCustom")
	private boolean isCustom = true;
	@XmlAttribute(name = "controller")
	private String controller = null;
	// 输入描述
	@XmlElementWrapper(name = "PipeIns")
	@XmlElement(name = "PipeIn")
	private List<PipeIn> pipeIns;
	// 输出描述
	@XmlElementWrapper(name = "PipeOuts")
	@XmlElement(name = "PipeOut")
	private List<PipeOut> pipeOuts;
	private String foldPath;
	// 内容构造类
	@XmlAttribute
	private String provider;
	// view描述
	private String desc;
	// 内联window列表
	private List<PagePartMeta> inlineWindowList;
	
	private boolean isParsed = false;

	@Override
	public String getWidgetName() {
		return "";
	}

	public String toXml() {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		ViewPartMeta newViewPartMeta = (ViewPartMeta) this.clone();
		Dataset[] datasets = newViewPartMeta.getViewModels().getDatasets();
		if (datasets != null && datasets.length != 0) {
			for (Dataset inner : datasets) {
				if (inner instanceof MdDataset) {
					inner.getFieldList().clear();
					newViewPartMeta.getViewModels().addDataset(inner);
				}
				if (inner instanceof RefMdDataset) {
					inner.getFieldList().clear();
					newViewPartMeta.getViewModels().addDataset(inner);
				}
			}
		}
	    {
//	    	Dataset  tmp=	newViewPartMeta.getViewModels().getDataset("ds_tree");
//	    	LuiEventConf confg= tmp.getEventConf("onAfterRowSelect","onAfterRowSelect_ds_tree");
//	    	confg.setScript("111111111111");
	    }
		String xmlstr= JaxbMarshalFactory.newIns().encodeXML(newViewPartMeta);
		xmlstr=xmlstr.replace("ns2:","");
		xmlstr=xmlstr.replace("xmlns:ns2", "xmlns");
		//java.util.logging.Logger.getAnonymousLogger().info(xmlstr);
		RequestLifeCycleContext.get().setPhase(phase);
		return xmlstr;
	}



	@Override
	public Object clone() {
		ViewPartMeta widget = (ViewPartMeta) super.clone();
		if (this.viewModels != null) {
			widget.viewModels = (DataModels) this.viewModels.clone();
			widget.viewModels.setWidget(widget);
		}

		if (this.viewComponents != null) {
			widget.viewComponents = (ViewPartComps) this.viewComponents.clone();
			widget.viewComponents.setWidget(widget);
		}

		if (this.viewMenus != null) {
			widget.viewMenus = (ViewPartMenus) this.viewMenus.clone();
			widget.viewMenus.setWidget(widget);
		}

		if (this.pipeIns != null) {
			widget.pipeIns = new ArrayList<PipeIn>();
			for (PipeIn pluginDesc : this.pipeIns) {
				widget.pipeIns.add((PipeIn) pluginDesc.clone());
			}
		}
		if (this.pipeOuts != null) {
			widget.pipeOuts = new ArrayList<PipeOut>();
			for (PipeOut plugoutDesc : this.pipeOuts) {
				widget.pipeOuts.add((PipeOut) plugoutDesc.clone());
			}
		}
		return widget;
	}

	public void clearCtxChanged() {
		this.setCtxChanged(false);
		if (this.getViewComponents() != null && this.getViewComponents().getComps() != null) {
			WebComp[] components = this.getViewComponents().getComps();
			for (WebComp comp : components) {
				comp.setCtxChanged(false);
			}
		}
		if (this.getViewMenus() != null && this.getViewMenus().getContextMenus() != null) {
			ContextMenuComp[] menus = this.getViewMenus().getContextMenus();
			for (ContextMenuComp comp : menus) {
				comp.setCtxChanged(false);
			}
		}
		if (this.getViewMenus() != null && this.getViewMenus().getMenuBars() != null) {
			MenubarComp[] menubars = this.getViewMenus().getMenuBars();
			for (MenubarComp menubar : menubars) {
				menubar.setCtxChanged(false);
				List<MenuItem> list = menubar.getMenuList();
				if (list != null) {
					for (MenuItem item : list) {
						item.setCtxChanged(false);
					}
				}
			}
		}
		if (this.getViewModels() != null && this.getViewModels().getDatasets() != null) {
			Dataset[] dss = this.getViewModels().getDatasets();
			for (Dataset ds : dss) {
				ds.setCtxChanged(false);
			}
		}
	}

	public IDataBinding[] getComponentsByDataset(String dsId) {
		Iterator<WebComp> it = getViewComponents().getComponents().iterator();
		List<IDataBinding> list = new ArrayList<IDataBinding>();
		while (it.hasNext()) {
			WebComp comp = it.next();
			if (!(comp instanceof IDataBinding))
				continue;
			IDataBinding binding = (IDataBinding) comp;
			if (dsId.equals(binding.getDataset()))
				list.add((IDataBinding) comp);
		}
		return list.toArray(new IDataBinding[list.size()]);
	}

//	private void notify(String type, Object obj) {
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			Map<String, Object> map = new HashMap<String, Object>();
//			String widgetId = this.getId();
//			map.put("widgetId", widgetId);
//			map.put("type", type);
//			this.getWidget().notifyChange(UIElement.UPDATE, map);
//		}
//	}

	@Override
	public void setEnabled(boolean enabled) {
		Dataset[] dss = this.getViewModels().getDatasets();
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			ds.setEdit(enabled);
		}
		WebComp[] components = this.getViewComponents().getComps();
		for (int i = 0; i < components.length; i++) {
			WebComp comp = components[i];
			comp.setEnabled(enabled);
		}
		MenubarComp[] mbs = this.getViewMenus().getMenuBars();
		if (mbs != null && mbs.length > 0) {
			for (int i = 0; i < mbs.length; i++) {
				MenubarComp mb = (MenubarComp) mbs[i];
				List<MenuItem> items = mb.getMenuList();
				if (items != null) {
					Iterator<MenuItem> it = items.iterator();
					while (it.hasNext()) {
						MenuItem item = it.next();
						item.setEnabled(enabled);
					}
				}
			}
		}
	}

	public void adjustForRuntime() {
		DsRelationAdjuster adj = new DsRelationAdjuster(this);
		adj.adjust();
	}

	public String toString() {
		return getId() + ":" + getViewModels().toString() + ":" + getViewComponents().toString() + ":" + getViewMenus().toString();
	}

	public String getController() {
		return controller;
	}

	public void setController(String controllerClazz) {
		this.controller = controllerClazz;
	}

	public String getEditFormularClazz() {
		return editFormularClazz;
	}

	public void setEditFormularClazz(String editFormularClazz) {
		this.editFormularClazz = editFormularClazz;
	}

	public void setViewModels(DataModels viewModels) {
		viewModels.setWidget(this);
		this.viewModels = viewModels;
	}

	public DataModels getViewModels() {
		return viewModels;
	}

	public void setViewComponents(ViewPartComps viewComponents) {
		viewComponents.setWidget(this);
		this.viewComponents = viewComponents;
	}

	public ViewPartComps getViewComponents() {
		return viewComponents;
	}

	public ViewPartMenus getViewMenus() {
		return viewMenus;
	}

	public void setViewMenus(ViewPartMenus viewMenus) {
		viewMenus.setWidget(this);
		this.viewMenus = viewMenus;
	}

	public boolean isDialog() {
		return isDialog;
	}

	public void setDialog(boolean isDialog) {
		this.isDialog = isDialog;
	}

	public String getCaption() {
		return caption;
	}

	public boolean isParsed() {
		return isParsed;
	}

	public void setParsed(boolean isParsed) {
		this.isParsed = isParsed;
	}

	public void setCaption(String caption) {
		this.caption = caption;
		//this.getRender()
		//notify(WIDGET_CAPTION, null);
	}

	public BaseContext getContext() {
		WidgetUIContext ctx = new WidgetUIContext();
		ctx.setVisible(this.isVisible);
		return ctx;
	}

	public void setContext(BaseContext ctx) {
		WidgetUIContext cardCtx = (WidgetUIContext) ctx;
		setVisible(cardCtx.isVisible());
		setCtxChanged(false);
		super.setContext(ctx);
	}

	public PagePartMeta getPagemeta() {
		return pagemeta;
	}

	public void setPagemeta(PagePartMeta pagemeta) {
		this.pagemeta = pagemeta;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public List<PipeIn> getPipeIns() {
		return pipeIns;
	}

	public void setPipeIns(List<PipeIn> pluginDescs) {
		this.pipeIns = pluginDescs;
	}

	@JSONField(serialize = false)
	public boolean isCustom() {
		return isCustom;
	}

	public void setIsCustom(boolean isCustom) {
		this.isCustom = isCustom;
	}

	public PipeIn getPipeIn(String id) {
		if (pipeIns == null)
			return null;
		for (PipeIn p : pipeIns) {
			if (id.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}

	public void addPipeIns(PipeIn pluginDesc) {
		if (this.pipeIns == null)
			this.pipeIns = new ArrayList<PipeIn>();
		this.pipeIns.add(pluginDesc);
	}
	
	public void removePipeIns(PipeIn pipeIn) {
		this.pipeIns.remove(pipeIn);
	}

	public List<PipeOut> getPipeOuts() {
		return pipeOuts;
	}

	public PipeOut getPipeOut(String id) {
		if (pipeOuts == null)
			return null;
		for (PipeOut p : pipeOuts) {
			if (id.equals(p.getId())) {
				return p;
			}
		}
		return null;
	}

	public void setPipeOuts(List<PipeOut> plugoutDescs) {
		this.pipeOuts = plugoutDescs;
	}

	public void addPipeOuts(PipeOut plugoutDesc) {
		if (this.pipeOuts == null)
			this.pipeOuts = new ArrayList<PipeOut>();
		this.pipeOuts.add(plugoutDesc);
	}
	
	public void removePipeOuts(PipeOut pipeOut){
		this.pipeOuts.remove(pipeOut);
	}

	public String getFoldPath() {
		return foldPath;
	}

	public void setFoldPath(String foldPath) {
		this.foldPath = foldPath;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String builder) {
		this.provider = builder;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<PagePartMeta> getInlineWindowList() {
		return inlineWindowList;
	}

	public void addInlineWindow(PagePartMeta pm) {
		if (inlineWindowList == null) {
			inlineWindowList = new ArrayList<PagePartMeta>();
		}
		inlineWindowList.add(pm);
	}

	public static void builderMenuItem(MenuItem item, MenubarComp bar, ViewPartMeta widget) {
		item.setMenu(bar);
		item.setWidget(widget);
		List<MenuItem> clist = item.getChildList();
		if (clist != null && clist.size() != 0) {
			for (int i = 0; i < clist.size(); i++) {
				ViewPartMeta.builderMenuItem(clist.get(i), bar, widget);
			}
		}
	}
	public static void builderCtxMenuItem(MenuItem item, ContextMenuComp bar, ViewPartMeta widget) {
		item.setCtxmenu(bar);
		item.setWidget(widget);
		List<MenuItem> clist = item.getChildList();
		if (clist != null && clist.size() != 0) {
			for (int i = 0; i < clist.size(); i++) {
				ViewPartMeta.builderCtxMenuItem(clist.get(i), bar, widget);
			}
		}
	}
	public static ViewPartMeta parse(InputStream input) {
		try {
			String xml = ContextResourceUtil.inputStream2String(input);
			ViewPartMeta viewPart =(ViewPartMeta)   JaxbMarshalFactory.newIns().decodeXML(ViewPartMeta.class, xml);
			MenubarComp[] menuBars = viewPart.getViewMenus().getMenuBars();
			for (int i = 0; i < menuBars.length; i++) {
				MenubarComp inner = menuBars[i];
				inner.setWidget(viewPart);
				List<MenuItem> cList = inner.getMenuList();
				if (cList != null && cList.size() != 0) {
					for (int j = 0; j < cList.size(); j++) {
						ViewPartMeta.builderMenuItem(cList.get(j), inner, viewPart);
					}
				}
			}
			ContextMenuComp[] ctxMenuComps = viewPart.getViewMenus().getContextMenus();
			for (int i = 0; i < ctxMenuComps.length; i++) {
				ContextMenuComp inner = ctxMenuComps[i];
				inner.setWidget(viewPart);
				List<MenuItem> cList = inner.getItemList();
				if (cList != null && cList.size() != 0) {
					for (int j = 0; j < cList.size(); j++) {
						ViewPartMeta.builderCtxMenuItem(cList.get(j), inner, viewPart);
					}
				}
			}
			WebComp[] comps = viewPart.getViewComponents().getComps();
			for (WebComp inner : comps) {
				inner.setWidget(viewPart);
				if (inner instanceof FormComp) {
					FormComp formComp = (FormComp) inner;
					List<FormElement> formElements = formComp.getElementList();
					formComp.setElementList(formElements);
				}
				if (inner instanceof GridComp) {
					GridComp gridComp = (GridComp) inner;
					List<IGridColumn> gridColumn = gridComp.getColumnList();
					gridComp.setColumnList(gridColumn);
				}
			}
			viewPart.getViewModels().setWidget(viewPart);
			Dataset[] datasets = viewPart.getViewModels().getDatasets();
			for (int i = 0; i < datasets.length; i++) {
				Dataset ds = datasets[i];
				ds.setWidget(viewPart);
				if (ds instanceof MdDataset) {
					MdDataset mdds = (MdDataset) ds;
					mdds.load();
				}
				Field[] fields = ds.getFields();
				if(fields!=null){
					for (int j = 0; j < fields.length; j++) {
						Field field = fields[j];
						if (StringUtils.isNotBlank(field.getEditFormular())) {
							field.setEditFormular(JsURLDecoder.decode(field.getEditFormular(), "UTF-8"));
						}
						if (StringUtils.isNotBlank(field.getValidateFormula())) {
							field.setValidateFormula(JsURLDecoder.decode(field.getValidateFormula(), "UTF-8"));
						}
						if (StringUtils.isNotBlank(field.getFormater())) {
							field.setFormater(JsURLDecoder.decode(field.getFormater(), "UTF-8"));
						}
						field.setCtxChanged(false);
					}
				}
			}
			//viewPart.toXml();
			return viewPart;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}finally{
			IOUtils.closeQuietly(input);
		}
	}

}
