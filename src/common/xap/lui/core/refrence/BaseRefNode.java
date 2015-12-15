package xap.lui.core.refrence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.PCViewLayOutRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlAccessorType(XmlAccessType.NONE)
public class BaseRefNode extends ViewElement implements IRefNode {
	private static final long serialVersionUID = 4305953060997010652L;
	@XmlAttribute(name = "title")
	private String title;
	@XmlAttribute
	private boolean isLocate = true;// 是否需要定位框
	@XmlAttribute
	private String width;// 对话框体宽度
	@XmlAttribute
	private String height;// 对话宽体高度
	@XmlAttribute
	private boolean isRead = false;
	@XmlAttribute
	private boolean isDefModel = false;

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

	public boolean isLocate() {
		return isLocate;
	}

	public boolean isDefModel() {
		return isDefModel;
	}

	public void setDefModel(boolean isDefModel) {
		this.isDefModel = isDefModel;
	}

	public void setLocate(boolean locate) {
		this.isLocate = locate;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean read) {
		this.isRead = read;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setRefNodeText(null, this);
		}
		// this.getRender()
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			if (this instanceof SelfDefRefNode) {
				this.getRender().setRefNodeWidth(this);
			}
		}
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
		if (this instanceof SelfDefRefNode) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().updateRefNodeHeight(null, this);
			}
		}
	}
}
