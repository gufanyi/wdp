package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UISplitter;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 分割面板布局渲染器
 */
@SuppressWarnings("unchecked")
public class PCSpliterLayoutRender extends UILayoutRender<UISplitter, LuiElement> {
	private String divideSize = "0.5";
	private String orientation = "v";
	private String boundMode = "%";
	private boolean oneTouch = true;
	private int spliterWidth = 1;
	private boolean inverse = false;
	private boolean inverseFlowPanel = false;
	// 是否隐藏拖动条
	private boolean hideBar = true;

	// 隐藏方向（true-向左/向上；false-向右/向下）
	private boolean hideDirection = true;

	public PCSpliterLayoutRender(UISplitter uiEle) {
		super(uiEle);
		UISplitter spliter = this.getUiElement();
		this.orientation = "h";
		if (UISplitter.ORIENTATION_H.equals(spliter.getOrientation())) {
			this.orientation = "h";
		} else {
			this.orientation = "v";
		}

		this.boundMode = "%";
		if (spliter.getBoundMode() != null) {
			if (UISplitter.BOUNDMODE_PERC.equals(spliter.getBoundMode()))
				this.boundMode = "%";
			else if (spliter.getBoundMode() == 1) {
				this.boundMode = "px";
			}
		}

		if (spliter.getDivideSize() != null) {
			if (this.boundMode.equals("%"))
				this.divideSize = "0." + spliter.getDivideSize();
			else
				this.divideSize = spliter.getDivideSize();
		}
		if (spliter.getInverse() != null)
			this.inverse = UIConstant.TRUE.equals(spliter.getInverse());
		this.inverseFlowPanel = UIConstant.TRUE.equals(spliter.getInverseFlowPanel());
		if (UIConstant.TRUE.equals(spliter.getOneTouch())) {
			this.oneTouch = true;
		} else {
			this.oneTouch = false;
		}

		if (!StringUtils.isEmpty(uiEle.getHideBar())) {
			this.hideBar = Boolean.parseBoolean(uiEle.getHideBar());
		}

		this.divId = DIV_PRE + this.getId(); // + getUniqueId(DIV_INDEX);
		this.varId = COMP_PRE + this.viewId + "_" + getId();
	}


	private String generateParam() {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("position", "relative");
		param.put("prop", divideSize);
		param.put("orientation", orientation);
		param.put("oneTouch", oneTouch);
		param.put("spliterWidth", spliterWidth);
		param.put("boundMode", boundMode);
		param.put("isInverse", inverse);
		param.put("isInverseFlowPanel", inverseFlowPanel);
		if (!isHideDirection())
			param.put("hideDirection", false);
		if (!isHideBar())
			param.put("hideBar", false);
		boolean isRunMode = false;
		isRunMode = !LuiRuntimeContext.isEditMode();
		param.put("isRunMode", isRunMode);
		return JSON.toJSONString(param);
	}

	public String createHead() {
		StringBuilder buf = new StringBuilder();
		String divId = getDivId();
		String spliterId = divId + "_spliter";
		buf.append("window.").append(getVarId()).append(" = $(\"<div id='" + spliterId + "'></div>\")");
		buf.append(".appendTo($('#" + divId + "'))");
		buf.append(".spliter(\n");
		buf.append(generateParam());
		buf.append("\n).spliter('instance');\n");
		return buf.toString();
	}


	public String getDivideSize() {
		return divideSize;
	}

	public void setDivideSize(String divideSize) {
		this.divideSize = divideSize;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getBoundMode() {
		return boundMode;
	}

	public void setBoundMode(String boundMode) {
		this.boundMode = boundMode;
	}

	public boolean isOneTouch() {
		return oneTouch;
	}

	public void setOneTouch(boolean oneTouch) {
		this.oneTouch = oneTouch;
	}

	public int getSpliterWidth() {
		return spliterWidth;
	}

	public void setSpliterWidth(int spliterWidth) {
		this.spliterWidth = spliterWidth;
	}

	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	public boolean isHideBar() {
		return hideBar;
	}

	public void setHideBar(boolean hideBar) {
		this.hideBar = hideBar;
	}

	public boolean isHideDirection() {
		return hideDirection;
	}

	public void setHideDirection(boolean hideDirection) {
		this.hideDirection = hideDirection;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_SPLITERLAYOUT;
	}

	@Override
	public String placeSelf() {

		StringBuilder buf = new StringBuilder();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $('<div>').attr('id','").append(newDivId).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%'});\n");
		// buf.append(newDivId).append(".overflow = 'hidden';\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(newDivId).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void resizeSpliter() {
		StringBuilder buf = new StringBuilder();

		if (this.viewId != null) {
			buf.append("var curWidget = pageUI.getViewPart('").append(this.getViewId()).append("');\n");
			buf.append("var ").append(getVarId()).append(" = curWidget.getSplit('").append(this.getDivId()).append("');\n");
		} else {
			buf.append("var ").append(getVarId()).append(" = pageUI.getSplit('").append(this.getDivId()).append("');\n");
		}
		buf.append("if(" + getVarId() + "){\n");

		buf.append("SpliterComp.spliterCompResize(").append(getVarId()).append(");\n");
		buf.append("};\n");
		addBeforeExeScript(buf.toString());
	}

}
