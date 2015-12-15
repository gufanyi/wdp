package xap.lui.core.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.design.UIMetaToXml;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCUIMetaPanelRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "UIPart")
@XmlAccessorType(XmlAccessType.NONE)
public class UIPartMeta extends UILayoutPanel implements IUIPartMeta {
	private static final long serialVersionUID = 5927522368179835547L;
	private Map<String, UIViewPart> dialogMap = new HashMap<String, UIViewPart>();
	public static String ISJQUERY = "jquery";
	public static String INCLUDEJS = "includejs";
	public static String INCLUDEID = "includeid";
	public static String INCLUDECSS = "includecss";
	public static String LUIINCLUDEJS = "includejs";
	public static String LUIINCLUDECSS = "includecss";
	public static String ISREFERENCE = "isReference";
	public static String ISFLOW = "isFlow";
	// 页面级样式表，直接添加在head区域
	public static String PageCss = "pagecss";
	// 页面级JS，直接添加在head区域
	public static String Pagejs = "pagejs";
	// 动态生成uimeta的class类
	public static String GENERATECLASS = "generateclass";
	// 是否在tab页签生成tabfolder
	public static String TABBODY = "tabBody";
	@XmlAttribute(name = "isFlow")
	public Boolean isflow;
	public static String UIPROVIDER = "uiprovider";

	private Boolean tabBody;
	private Boolean isFlow;
	private String uiprovider;
	private String folderPath;
	private String id;
	private Integer jquery;
	private String includeid;
	private String includecss;
	private String includejs;
	private String luiincludejs;
	private String luiincludecss;
	private String pagecss;
	private String pagejs;
	private Integer isReference;

	public Boolean isFlow() {
		return isflow;
	}

	public  void setIsFlow(Boolean isflow) {
		this.isflow = isflow;
	}

	public UIPartMeta() {
		this.isFlow = Boolean.FALSE;
	}

	public Boolean getTabBody() {
		return this.tabBody;
	}

	public void setTabBody(Boolean tabBody) {
		this.tabBody = tabBody;
	}

	public Boolean getFlowmode() {
		return isFlow;
	}

	public void setFlowmode(Boolean flowMode) {
		this.isFlow = flowMode;
	}

	public String getUiprovider() {
		return uiprovider;
	}

	public void setUiprovider(String uiprovider) {
		this.uiprovider = uiprovider;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Map<String, UIViewPart> getDialogMap() {
		return dialogMap;
	}

	public void setDialogMap(Map<String, UIViewPart> dialogMap) {
		this.dialogMap = dialogMap;
	}

	public UIViewPart getDialog(String id) {
		return dialogMap.get(id);
	}

	public void setDialog(String id, UIViewPart dialog) {
		dialogMap.put(id, dialog);
		if (LifeCyclePhase.render.equals(getPhase())
				|| LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addChild(dialog);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getJquery() {
		return jquery;
	}

	public void setJquery(Integer jquery) {
		this.jquery = jquery;
	}

	public void setIncludejs(String includejs) {
		this.includejs = includejs;
	}

	public void setIncludecss(String includecss) {
		this.includecss = includecss;
	}

	public void setLuiIncludejs(String luiincludejs) {
		this.luiincludejs = luiincludejs;
	}

	public void setLuiIncludecss(String luiincludecss) {
		this.luiincludecss = luiincludecss;
	}

	public String getPagecss() {
		return pagecss;
	}

	public void setPagecss(String pagecss) {
		this.pagecss = pagecss;
	}

	public String getPagejs() {
		return pagejs;
	}

	public void setPagejs(String pagejs) {
		this.pagejs = pagejs;
	}

	public String getIncludejs() {
		return includejs;
	}

	public String getIncludeId() {
		return includeid;
	}

	public void setIncludeId(String ids) {
		this.includeid = ids;
	}

	public String getLuiIncudejs() {
		return luiincludejs;
	}

	public String getLuiIncudecss() {
		return luiincludecss;
	}

	public String getIncludecss() {
		return includecss;
	}

	public Integer getIsReference() {
		return isReference;
	}

	public void setIsReference(Integer isReference) {
		this.isReference = isReference;
	}

	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(ISJQUERY, 0);
		return map;
	}

	public void adjustForRuntime() {
	}

	@Override
	public UIPartMeta doClone() {
		UIPartMeta uimeta = (UIPartMeta) super.doClone();
		if (this.dialogMap != null) {
			uimeta.dialogMap = new HashMap<String, UIViewPart>();
			Iterator<String> keys = this.dialogMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				UIViewPart widget = this.dialogMap.get(key);
				uimeta.dialogMap.put(key, (UIViewPart) widget.doClone());
			}
		}
		return uimeta;
	}

	/**
	 * 返回找到的第一个VIEW
	 * 
	 * @return
	 */
	public UIViewPart getFirstView() {
		UIElement ele = this.getElement();
		if (ele instanceof UIViewPart)
			return (UIViewPart) ele;
		else if (ele instanceof UILayout) {
			return getFirstView((UILayout) ele);
		}
		return null;
	}

	private UIViewPart getFirstView(UILayout layout) {
		List<UILayoutPanel> list = layout.getPanelList();
		Iterator<UILayoutPanel> it = list.iterator();
		while (it.hasNext()) {
			UILayoutPanel panel = it.next();
			UIElement ele = panel.getElement();
			if (ele instanceof UIViewPart)
				return (UIViewPart) ele;
			else if (ele instanceof UILayout)
				return getFirstView((UILayout) ele);
		}
		return null;
	}

	@Override
	public void removeElement(UIElement ele) {
		super.removeElement(ele);
	}

	/**
	 * 调整UI必须属性
	 */
	public void adjustUI(String widgetId) {
		setViewId(widgetId);
		modifyWidgetUIId(this.getElement(), widgetId);
	}

	private void modifyWidgetUIId(UIElement ele, String widgetId) {
		if (ele == null)
			return;
		ele.setViewId(widgetId);
		if (ele instanceof UIViewPart) {
			widgetId = ele.getId();
		}
		if (ele instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout) ele).getPanelList()
					.iterator();
			while (it.hasNext()) {
				modifyWidgetUIId(it.next(), widgetId);
			}
		} else if (ele instanceof UILayoutPanel) {
			UIElement newEle = ((UILayoutPanel) ele).getElement();
			modifyWidgetUIId(newEle, widgetId);
		}
	}

	public String getEtag() {
		return null;
	}

	public String toXml() {
		return UIMetaToXml.toString(this);
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCUIMetaPanelRender(this));
		}
		return render;
	}

}
