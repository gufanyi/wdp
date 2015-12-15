package xap.lui.core.layout;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCSpliterLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "Splitter")
@XmlAccessorType(XmlAccessType.NONE)
public class UISplitter extends UILayout {
	private static final long serialVersionUID = -634167934328017861L;

	public static final Integer ORIENTATION_H = 1;
	public static final Integer ORIENTATION_V = 0;
	public static final Integer BOUNDMODE_PX = 1;
	public static final Integer BOUNDMODE_PERC = 0;
	public static final Integer ONETOUCH_TRUE = 1;
	public static final Integer ONETOUCH_FALSE = 0;
	public static final Integer INVERSE_FALSE = 0;
	public static final Integer INVERSE_TRUE = 1;
	public static final Integer HIDEDIRECTION_R = 1;
	public static final Integer HIDEDIRECTION_L = 0;

	public static final String DIVIDE_SIZE = "divideSize";
	public static final String ORIENTATION = "orientation";
	public static final String BOUNDMODE = "boundMode";
	public static final String ISONETOUCH = "isOneTouch";
	// public static final String SPLITERWIDTH = "spliterWidth";
	public static final String ISINVERSE = "isInverse";
	public static final String ISINVERSEFLOWPANEL = "isInverseFlowPanel";
	// public static final String HIDEBAR = "hideBar";
	public static final String HIDEDIRECTION = "hideDirection";
	public static final String HIDEBAR = "hideBar";
	public static final String ID = "id";

	@XmlElementRef(name = "SplitterOne", type = UISplitterOne.class)
	private UISplitterOne splitterOne;
	@XmlElementRef(name = "SplitterOne", type = UISplitterTwo.class)
	private UISplitterTwo splitterTwo;
	private String id;
	private String divideSize;
	private Integer orientation;
	private Integer boundMode;
	private Integer isOneTouch;
	private Integer isInverse;
	private String hideBar;
	private Integer isInverseFlowPanel;
	private Integer hideDirection;
	private String widgetId;


	public UISplitter() {
		this.divideSize="30";
		this.orientation=ORIENTATION_H;
		this.boundMode=BOUNDMODE_PERC;
		this.isOneTouch=UIConstant.FALSE;
		// map.put(SPLITERWIDTH, "4");
		this.isInverse=UIConstant.FALSE;
		this.isInverseFlowPanel=UIConstant.FALSE;
		// map.put(HIDEBAR, 1);
		this.hideDirection=HIDEDIRECTION_R;// 1 纵向 0横向
		
	}

	public UISplitterOne getSplitterOne() {
		return splitterOne;
	}

	private void setSplitterOne(UISplitterOne splitterOne) {
		this.splitterOne = splitterOne;
	}

	public UISplitterTwo getSplitterTwo() {
		return splitterTwo;
	}

	private void setSplitterTwo(UISplitterTwo splitterTwo) {
		this.splitterTwo = splitterTwo;
	}

	public void addPanel(UILayoutPanel panel) {
		if (panel instanceof UISplitterTwo)
			this.setSplitterTwo((UISplitterTwo) panel);
		else
			this.setSplitterOne((UISplitterOne) panel);
		super.addPanel(panel);
	}

	public String getId() {
		return id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public String getDivideSize() {
		return divideSize;
	}

	public void setDivideSize(String divideSize) {
		this.divideSize = divideSize;
		// notifyChange(UPDATE, DIVIDE_SIZE);
	}

	public Integer getOrientation() {
		return orientation;
	}

	public void setOrientation(Integer orientation) {
		this.orientation = orientation;
		// notifyChange(UPDATE, ORIENTATION);
	}

	public Integer getBoundMode() {
		return boundMode;
	}

	public void setBoundMode(Integer boundMode) {
		this.boundMode = boundMode;
		// notifyChange(UPDATE, BOUNDMODE);
	}

	public Integer getOneTouch() {
		return isOneTouch;
	}

	public void setOneTouch(Integer oneTouch) {
		this.isOneTouch = oneTouch;
		// notifyChange(UPDATE, ONETOUCH);
	}

	// public String getSpliterWidth() {
	// return (String) getAttribute(SPLITERWIDTH);
	// }
	//
	// public void setSpliterWidth(String spliterWidth) {
	// setAttribute(SPLITERWIDTH, spliterWidth);
	// // notifyChange(UPDATE, SPLITERWIDTH);
	// }

	public Integer getInverse() {
		return isInverse;
	}

	public void setInverse(Integer inverse) {
		this.isInverse = inverse;
		// notifyChange(UPDATE, INVERSE);
	}

	public String getHideBar() {
		return hideBar;
	}

	public void setHideBar(String hideBar) {
		this.hideBar = hideBar;
		// notifyChange(UPDATE, INVERSE);
	}

	public Integer getInverseFlowPanel() {
		return isInverseFlowPanel;
	}

	public void setInverseFlowPanel(Integer inverseFlowPanel) {
		this.isInverseFlowPanel = inverseFlowPanel;
	}

	// public Integer getHideBar() {
	// return (Integer) getAttribute(HIDEBAR);
	// }
	//
	// public void setHideBar(Integer hideBar) {
	// setAttribute(HIDEBAR, hideBar);
	// // notifyChange(UPDATE, HIDEBAR);
	// }

	public Integer getHideDirection() {
		return hideDirection;
	}

	public void setHideDirection(Integer hideDirection) {
		this.hideDirection = hideDirection;
		;
		// notifyChange(UPDATE, HIDEDIRECTION);
	}

	public String getViewId() {
		return widgetId;
	}

	public void setViewId(String widgetId) {
		this.widgetId = widgetId;
	}

	// @Override
	// protected Map<String, Serializable> createAttrMap() {
	// Map<String, Serializable> map = new HashMap<String, Serializable>();
	// map.put(DIVIDE_SIZE, "50");
	// map.put(ORIENTATION, 1); // 0 纵向， 1横向
	// map.put(BOUNDMODE, 0); // % 0, px 1
	// map.put(ONETOUCH, 0);
	// // map.put(SPLITERWIDTH, "4");
	// map.put(INVERSE, 0);
	// // map.put(HIDEBAR, 1);
	// map.put(HIDEDIRECTION, 1); // 1 纵向 0横向
	// return map;
	// }

	@Override
	public UISplitter doClone() {
		UISplitter uiSplitter = (UISplitter) super.doClone();
		if (this.splitterOne != null) {
			uiSplitter.splitterOne = (UISplitterOne) this.splitterOne.doClone();
		}

		if (this.splitterTwo != null) {
			uiSplitter.splitterTwo = (UISplitterTwo) this.splitterTwo.doClone();
		}
		return uiSplitter;
	}

	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCSpliterLayoutRender(this));
		}
		return render;
	}

}
