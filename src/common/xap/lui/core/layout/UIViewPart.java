package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.render.PCViewLayOutRender;

@XmlRootElement(name = "ViewPart")
@XmlAccessorType(XmlAccessType.NONE)
public class UIViewPart extends UILayout {
	private static final long serialVersionUID = -6203854872529543007L;
	public static final String AUTO_FILL = "autoFill";
	private Integer autoFill;
	
	/**
	 * 該註解
	 */

	private UIPartMeta parent = null;

	public UIPartMeta getParent() {
		return parent;
	}

	public void setParent(UIPartMeta parent) {
		this.parent = parent;
	}

	public UIViewPart() {
		setAutoFill(UIConstant.FALSE);
	}

	public UIPartMeta getUimeta() {
		if (getPanelList().size() == 0)
			return null;
		return (UIPartMeta) getPanelList().get(0);
	}

	public void setUimeta(UIPartMeta uimeta) {
		addPanel(uimeta);
	}

	@Override
	public void removePanel(UILayoutPanel ele) {
		if (!(ele instanceof UIPartMeta))
			throw new LuiRuntimeException("不能删除非UIMeta元素");
		// throw new
		// LuiRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
		// "UIWidget-000000")/*不能删除非UIMeta元素*/);
		if (getPanelList().size() == 0)
			throw new LuiRuntimeException("不存在UIMeta元素");
		// throw new
		// LuiRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
		// "UIWidget-000001")/*不存在UIMeta元素*/);
		super.removePanel(ele);
	}

	@Override
	public void addPanel(UILayoutPanel panel) {
		if (!(panel instanceof UIPartMeta))
			throw new LuiRuntimeException("不能添加非UIMeta元素");
		if (getPanelList().size() > 0)
			throw new LuiRuntimeException("已经存在UIMeta元素");
		panel.setViewId(this.getId());
		if (panel.getId() == null)
			panel.setId(this.getId() + "_um");
		super.addPanel(panel);
	}

	public Integer getAutoFill() {
		return autoFill;
	}

	public void setAutoFill(Integer autoFill) {
		this.autoFill=autoFill;
	}

	@Override
	public UIViewPart doClone() {
		return (UIViewPart) super.doClone();
	}

	@Override
	public PCViewLayOutRender getRender() {
		if(render==null){
			render= new PCViewLayOutRender(this);
		}
		return (PCViewLayOutRender)render;
		
	}

}
