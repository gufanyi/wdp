package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCDividLayoutRender;
import xap.lui.core.render.notify.RenderProxy;


@XmlRootElement(name="Divid")
@XmlAccessorType(XmlAccessType.NONE)
public class UIDivid extends UILayout {
	private static final long serialVersionUID = -634167934328017861L;
	
	public static final String ORIENTATION_H = "h";
	public static final String ORIENTATION_V = "v";
	
	public static final String PROP = "prop";
	public static final String ISINVERSE = "isInverse";
	public static final String ANIMATE = "isAnimate";
	public static final String ORIENTATION = "orientation";
	public static final String ID = "id";

	@XmlElementRef(name="DividProp",type=UIDividProp.class)
	private UIDividProp dividProp;
	@XmlElementRef(name="DividCenter",type=UIDividCenter.class)
	private UIDividCenter dividCenter;
	private String orientation;
	private Integer isInverse;
	private String id;
	private String widgetId;
	private Integer prop;
    private Integer isAnimate;
	
	public UIDivid() {
		this.orientation = ORIENTATION_H;
		this.isInverse=UIConstant.FALSE;
		this.prop=200;
		this.isAnimate=UIConstant.TRUE;
	}

	public UIDividProp getDividProp() {
		return dividProp;
	}

	public void setDividProp(UIDividProp dividProp) {
		this.dividProp = dividProp;
	}

	public UIDividCenter getDividCenter() {
		return dividCenter;
	}

	public void setDividCenter(UIDividCenter dividCenter) {
		this.dividCenter = dividCenter;
	}

	public void addPanel(UILayoutPanel panel) {
		if (panel instanceof UIDividProp)
			this.setDividProp((UIDividProp) panel);
		else
			this.setDividCenter((UIDividCenter) panel);
		super.addPanel(panel);
	}

	public Integer getIsInverse() {
		return isInverse;
	}

	public void setIsInverse(Integer isInverse) {
		this.isInverse = isInverse;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public Integer getInverse() {
		return isInverse;
	}

	public void setInverse(Integer inverse) {
		this.isInverse=inverse;
	}
	
	public String getViewId() {
		return  widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId=widgetId;
	}

	public Integer getProp() {
		return prop;
	}

	public void setProp(Integer prop) {
	this.prop=prop;
	}

	public Integer getIsAnimate() {
		return isAnimate;
	}

	public void setIsAnimate(Integer isAnimate) {
		this.isAnimate=isAnimate;
	}

	@Override
	public UIDivid doClone() {
		UIDivid uiDivid = (UIDivid) super.doClone();
		if (this.dividProp != null) {
			uiDivid.dividProp = (UIDividProp) this.dividProp.doClone();
		}

		if (this.dividCenter != null) {
			uiDivid.dividCenter = (UIDividCenter) this.dividCenter.doClone();
		}
		return uiDivid;
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCDividLayoutRender(this));
		}
		// TODO Auto-generated method stub
		return render;
	}
	
	

}
