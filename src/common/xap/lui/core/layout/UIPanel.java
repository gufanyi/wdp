package xap.lui.core.layout;

import java.util.List;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCPanelLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * panel UI
 * 
 * @author zhangxya
 *
 */
public class UIPanel extends UILayout {

	private static final long serialVersionUID = 1L;
	public static final String TITLE = "title";
	public static final String I18NNAME = "i18nName";
	public static final String RENDERTYPE = "renderType";
	public static final String EXPAND = "expand";
	public static final String ISCANEXPAND = "isCanExpand";
	public static final String LANGDIRF = "langDir";
	private String title;
	private String i18nName;
	private String renderType;
	private Boolean expand = true;
	private Boolean isCanExpand = true;
	private String langDir;
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		setI18nName(null);
		this.title=title;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setTitle(title);
		}
	}
	

	public Boolean getIsCanExpand() {
		return isCanExpand;
	}

	public void setIsCanExpand(Boolean isCanExpand) {
		this.isCanExpand = isCanExpand;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		this.i18nName=i18nName;
	}

	public void setLangDir(String langDir) {
		this.langDir=langDir;
	}

	public String getLangDir() {
		return langDir;
	}

	public String getRenderType() {
		return renderType;
	}

	public void setRenderType(String renderType) {
		Object oldRenderType = String.valueOf(this.renderType);
		// 当新的renderType不等于原来的renderType时候
		if (oldRenderType != null && renderType != null
				&& !renderType.equals(oldRenderType)) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setRenderTypeFunc(renderType);
			}
		}
		this.renderType=renderType;
	}

	public Boolean isExpand() {
		return expand;
	}

	public void setExpand(Boolean expand) {
		this.expand=expand;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setExpand(expand);
		}

	}

	public UIPanelPanel addElementToPanel(UIElement uigrid) {
		List<UILayoutPanel> list = getPanelList();
		UIPanelPanel panel = null;
		if (list != null && list.size() > 0) {
			panel = (UIPanelPanel) list.get(0);
		} else {
			panel = new UIPanelPanel();
			panel.setId("panel");
			addPanel(panel);
		}
		panel.setElement(uigrid);
		return panel;
	}

	@Override
	public PCPanelLayoutRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCPanelLayoutRender(this));
		}
		return (PCPanelLayoutRender) render;
	}

}
