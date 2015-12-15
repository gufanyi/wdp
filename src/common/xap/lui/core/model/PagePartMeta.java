package xap.lui.core.model;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.io.IOUtils;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.builder.Window;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.PageUIContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.util.JaxbMarshalFactory;

import com.alibaba.fastjson.annotation.JSONField;
@XmlRootElement(name = "PagePart")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "PagePart-Type")
public class PagePartMeta extends LuiElement implements Cloneable, Serializable, Comparable<PagePartMeta> {
	private static final long serialVersionUID = 2275080997090171371L;
	public static final String WIDGET_NAME = "";
	public static final String PROP_SHOWCARDFIRST = "PROP_SHOWCARDFIRST";
	public static final String MODIFY_TS = "$MODIFY_TS";
	public static final String WIN_TYPE_APP = "App";
	// 查询模板标识
	public static final String $QUERYTEMPLATE = "$QueryTemplate";
	// 对应Processor类名
	@XmlElement(name = "Processor")
	private String processorClazz;
	@XmlAttribute
	private String caption = null;
	@XmlTransient
	private Map<String, ViewPartMeta> widgetMap = new LinkedHashMap<String, ViewPartMeta>();
	// 第一次popview时，把widget克隆进去，第二次popview时，从这里取widget，保证拿到的widget是未被修改过的。
	@XmlTransient
	private Map<String, ViewPartMeta> cloneWidgetMap = new LinkedHashMap<String, ViewPartMeta>();
	@XmlElementWrapper(name = "Connectors")
	@XmlElement(name = "Connector")
	private LuiSet<Connector> connectorMap = new LuiHashSet<Connector>(); 
	// 菜单
	@XmlTransient
	private String etag;
	// 节点中存放图片的路径
	@XmlTransient
	private String nodeImagePath = "";
	@XmlTransient
	private String platformImagePath = "";
	@XmlTransient
	private String themePath = "";
	@XmlAttribute(name = "controller")
	private String controller = null;
	@XmlAttribute
	private String windowType;
	@XmlTransient
	private String foldPath;
	public static final String TagName = "PagePart";
	// 片段引用列表
	@XmlElementWrapper(name = "ViewParts")
	@XmlElement(name = "ViewPart")
	private List<ViewPartConfig> viewPartList = new ArrayList<ViewPartConfig>();
	// 输入描述
	@XmlElementWrapper(name = "PipeIns")
	@XmlElement(name = "PipeIn")
	private List<PipeIn> pipeIns;
	// 输出描述
	@XmlElementWrapper(name = "PipeOuts")
	@XmlElement(name = "PipeOut")
	private List<PipeOut> pipeOuts;
	@XmlTransient
	private Boolean hasChanged = false;
	@XmlTransient
	private Window window = null;
	//UI状态
	@XmlElementWrapper(name = "UIStates")
	@XmlElement(name = "UIState")
	private List<UIState> uIStates;
	
	private boolean isParsed = false;
	
	public Window getWindow() {
		return window;
	}
	public void setWindow(Window window) {
		this.window = window;
	}
	public boolean isParsed() {
		return isParsed;
	}
	public void setParsed(boolean isParsed) {
		this.isParsed = isParsed;
	}
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public PagePartMeta() {}
	public String getProcessorClazz() {
		return processorClazz;
	}
	public void setProcessorClazz(String processorClazz) {
		this.processorClazz = processorClazz;
	}
	public Object clone() {
		PagePartMeta meta = (PagePartMeta) super.clone();
		meta.widgetMap = new HashMap<String, ViewPartMeta>();
		meta.cloneWidgetMap = new HashMap<String, ViewPartMeta>();
		Iterator<ViewPartMeta> widgetIt = this.widgetMap.values().iterator();
		while (widgetIt.hasNext()) {
			meta.addWidget((ViewPartMeta) widgetIt.next().clone());
		}
		meta.connectorMap = new LuiHashSet<Connector>();
		Iterator<Connector> connectorIt = this.connectorMap.iterator();
		while (connectorIt.hasNext()) {
			meta.addConnector((Connector) connectorIt.next().clone());
		}
		return meta;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public List<UIState> getuIStates() {
		return uIStates;
	}
	public void setuIStates(List<UIState> uIStates) {
		this.uIStates = uIStates;
	}
	public void addUIStates(UIState uIState) {
		if (this.uIStates == null)
			this.uIStates = new ArrayList<UIState>();
		this.uIStates.add(uIState);
	}
	public void removeUIStates(UIState uIState){
		this.uIStates.remove(uIState);
	}
	public void removeUIStates(String id){
		this.uIStates.remove(this.getUIState(id));
	}
	public UIState getUIState(String id) {
		if (uIStates == null)
			return null;
		for (UIState us : uIStates) {
			if (id.equals(us.getId())) {
				return us;
			}
		}
		return null;
	}
	
	public ViewPartMeta[] getWidgets() {
		return this.widgetMap.values().toArray(new ViewPartMeta[0]);
	}
	public String[] getWidgetIds() {
		return this.widgetMap.keySet().toArray(new String[0]);
	}
	public ViewPartMeta getWidget(String id) {
		return widgetMap.get(id);
	}
	public ViewPartMeta getCloneWidget(String id) {
		return cloneWidgetMap.get(id);
	}
	public ViewPartMeta addCloneWidget(ViewPartMeta widget) {
		return cloneWidgetMap.put(widget.getId(), widget);
	}
	public LuiSet<Connector> getConnectorMap() {
		return connectorMap;
	}
	public Connector[] getConnectors() {
		return (Connector[]) connectorMap.toArray(new Connector[0]);
	}
	public void addConnector(Connector connector) {
		this.connectorMap.add(connector);
	}
	public void removeConnector(Connector connector) {
		this.connectorMap.remove(connector.getId());
	}
	
	public void addWidget(ViewPartMeta widget) {
		if (widgetMap.get(widget.getId()) != null) {
			widgetMap.remove(widget.getId());
		}
		widget.setPagemeta(this);
		widgetMap.put(widget.getId(), widget);
	}
	public void removeWidget(String id) {
		if (widgetMap.containsKey(id))
			widgetMap.remove(id);
	}
	public void adjustForRuntime() {
		ViewPartMeta[] ws = getWidgets();
		for (int i = 0; i < ws.length; i++) {
			ViewPartMeta wd = ws[i];
			wd.adjustForRuntime();
		}
	}
	public BaseContext getContext() {
		PageUIContext ctx = new PageUIContext();
		ctx.setHasChanged(this.hasChanged);
		return ctx;
	}
	public boolean getHasChanged() {
		return hasChanged;
	}
	public void setHasChanged(Boolean hasChanged) {
		if (hasChanged != this.hasChanged) {
			this.hasChanged = hasChanged;
			setCtxChanged(true);
		}
	}
	@Override
	public void setContext(BaseContext ctx) {
		super.setContext(ctx);
		PageUIContext pctx = (PageUIContext) ctx;
		setHasChanged(pctx.getHasChanged());
		setCtxChanged(false);
	}
	@Override
	public int compareTo(PagePartMeta pm) {
		return this.getId().compareToIgnoreCase(pm.getId());
	}
	@JSONField(serialize = false)
	public String getEtag() {
		return etag;
	}
	public void setEtag(String etag) {
		this.etag = etag;
	}
	@JSONField(serialize = false)
	public String getNodeImagePath() {
		return nodeImagePath;
	}
	public void setNodeImagePath(String nodeImagePath) {
		this.nodeImagePath = nodeImagePath;
	}
	@JSONField(serialize = false)
	public String getWindowType() {
		return windowType;
	}
	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}
	@JSONField(serialize = false)
	public String getFoldPath() {
		return foldPath;
	}
	public void setFoldPath(String foldPath) {
		this.foldPath = foldPath;
	}
	public void addViewPartConf(ViewPartConfig widgetConf) {
		viewPartList.add(widgetConf);
	}
	@JSONField(serialize = false)
	public ViewPartConfig[] getViewPartConfs() {
		return viewPartList.toArray(new ViewPartConfig[0]);
	}
	public void setViewPartList(List<ViewPartConfig> viewPartList) {
		this.viewPartList = viewPartList;
	}
	@JSONField(serialize = false)
	public List<ViewPartConfig> getViewPartList() {
		return this.viewPartList;
	}
	public void removeViewPartConf(ViewPartConfig wconf) {
		viewPartList.remove(wconf);
	}
	public ViewPartConfig getViewPartConf(String id) {
		Iterator<ViewPartConfig> it = viewPartList.iterator();
		while (it.hasNext()) {
			ViewPartConfig w = it.next();
			if (w.getId().equals(id))
				return w;
		}
		return null;
	}
	@JSONField(serialize = false)
	public List<PipeIn> getPipeIns() {
		return pipeIns;
	}
	public void setPipeIns(List<PipeIn> pluginDescs) {
		this.pipeIns = pluginDescs;
	}
	public void addPipeIns(PipeIn pluginDesc) {
		if (this.pipeIns == null)
			this.pipeIns = new ArrayList<PipeIn>();
		this.pipeIns.add(pluginDesc);
	}
	public void removePipeIns(PipeIn pipeIn) {
		this.pipeIns.remove(pipeIn);
	}
	@JSONField(serialize = false)
	public List<PipeOut> getPipeOuts() {
		return pipeOuts;
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
	public void setConnectorMap(LuiSet<Connector> connectorMap) {
		this.connectorMap = connectorMap;
	}
	public String getPlatformImagePath() {
		return platformImagePath;
	}
	public void setPlatformImagePath(String platformImagePath) {
		this.platformImagePath = platformImagePath;
	}
	public String getThemePath() {
		return themePath;
	}
	public void setThemePath(String themePath) {
		this.themePath = themePath;
	}
	public String toXml() {
		String xmlstr= JaxbMarshalFactory.newIns().encodeXML(this);
		xmlstr=xmlstr.replace("ns2:","");
		xmlstr=xmlstr.replace("xmlns:ns2", "xmlns");
		return xmlstr;
	}
	public static PagePartMeta parse(String xml) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(PagePartMeta.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			PagePartMeta conf = (PagePartMeta) jaxbUnmarshaller.unmarshal(new StringReader(xml));
			return conf;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e);
		}
	}
	public static PagePartMeta parse(InputStream input) {
		try {
			//JAXBContext jaxbContext = JAXBContext.newInstance(PagePartMeta.class);
			//Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			String xml = ContextResourceUtil.inputStream2String(input);
			PagePartMeta conf =(PagePartMeta)   JaxbMarshalFactory.newIns().decodeXML(PagePartMeta.class, xml);
			
			//PagePartMeta conf = (PagePartMeta) jaxbUnmarshaller.unmarshal(new StringReader(ContextResourceUtil.inputStream2String(input)));
			//System.out.println(conf.toXml());
			return conf;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e);
		}finally{
			IOUtils.closeQuietly(input);
		}
	}
}
