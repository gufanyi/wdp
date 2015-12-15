package xap.lui.core.layout;

import java.io.Serializable;

import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTabItemRender;
import xap.lui.core.render.PCTabLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

public class UITabItem extends UILayoutPanel {
	private static final long serialVersionUID = 1835380360211884210L;

	public static final String TEXT = "text";
	public static final String I18NNAME = "i18nName";
	public static final String LANGDIRF = "langDir";
	public static final String STATE = "state";
	public static final String SHOWCLOSEICON = "showCloseIcon";
	public static final String ACTIVE = "active";
	private String id;
	private String text;
	private String i18nName;
	private String langDir;
	private Integer state;
	private Integer showCloseIcon;
	private Integer active;

	public UITabItem() {
		this.active = 0;
		this.showCloseIcon = UIConstant.FALSE;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			setI18nName(null);
		}
		this.text = text;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setItemTitle(text);
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		this.i18nName = name;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer pageState) {
		this.state = pageState;
	}

	public Integer getShowCloseIcon() {
		return showCloseIcon;
	}

	public void setShowCloseIcon(Integer showColseIcon) {
		this.showCloseIcon = showColseIcon;
	}

	public Integer getActive() {
		return active;
	}

	/**
	 * 设置页签是否缓初始化
	 * 
	 * @param active
	 */
	public void setActive(Integer active) {
		this.active = active;
	}

	@Override
	public Serializable getAttribute(String key) {
		Serializable obj = super.getAttribute(key);
		if (key.equals(STATE)) {
			if (obj == null)
				return Integer.valueOf(-1);
		}
		return obj;
	}

	@Override
	public void setVisible(boolean visibility) {
		super.setVisible(visibility);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setItemVisible(visibility);
		}
	}

	@Override
	public PCTabItemRender getRender() {
		if (render == null) {
			PCTabLayoutRender parentRender = (PCTabLayoutRender) this.getLayout().getRender();
			render = RenderProxy.getRender(new PCTabItemRender(this, parentRender));
		}
		return (PCTabItemRender) render;
	}

}
