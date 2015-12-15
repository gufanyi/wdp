package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIDivid;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh 分割面板布局渲染器
 */
@SuppressWarnings("unchecked")
public class PCDividLayoutRender extends UILayoutRender<UIDivid, LuiElement> {
	private String orientation = "h";
	private int prop = 200;
	private boolean inverse = false;
	private boolean animate = true;

	public PCDividLayoutRender(UIDivid uiEle) {
		super(uiEle);
		UIDivid divid = this.getUiElement();
		this.orientation = divid.getOrientation();
		if (divid.getInverse() != null)
			this.inverse = UIConstant.TRUE.equals(divid.getInverse());
		if(divid.getProp() != null) {
			this.prop = divid.getProp();
		}
		if (divid.getIsAnimate() != null)
			this.animate = UIConstant.TRUE.equals(divid.getIsAnimate());

		this.divId = DIV_PRE + this.getId(); 
		this.varId = COMP_PRE + this.viewId + "_" + getId();
	}


//	public String generalHeadHtml() {
//		StringBuilder buf = new StringBuilder();
//		buf.append("<div id=\"" + getNewDivId() + "\" style=\"width:100%;height:100%;position:relative;\">\n");
//		buf.append(this.generalEditableHeadHtml());
//		return buf.toString();
//	}
	
	private String generateParam() {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("position", "relative");
		param.put("prop", this.prop);
		param.put("orientation", this.orientation);
		param.put("isInverse", this.inverse);
		param.put("animate", this.animate);
		return JSON.toJSONString(param);
	}

	public String createHead() {
		StringBuilder buf = new StringBuilder();
		String divId = getDivId();
		String dividId = divId + "_spliter";
		buf.append("window.").append(getVarId()).append(" = $(\"<div id='"+dividId+"'></div>\")");
		buf.append(".appendTo($('#"+divId+"'))");
		buf.append(".dividlayout(\n");
		buf.append(generateParam());
		buf.append("\n).dividlayout('instance');\n");
		return buf.toString();
	}
//
//	public String generalTailHtml() {
//		StringBuilder buf = new StringBuilder();
//		buf.append(this.generalEditableTailHtml());
//		buf.append("</div>\n");
//		return buf.toString();
//	}

	public String generalTailScript() {
		return "";
	}


	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public int getProp() {
		return prop;
	}


	public void setProp(int prop) {
		this.prop = prop;
	}


	public boolean isAnimate() {
		return animate;
	}


	public void setAnimate(boolean animate) {
		this.animate = animate;
	}


	public boolean isInverse() {
		return inverse;
	}

	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_DIVIDLAYOUT;
	}

	@Override
	public String placeSelf() {
		
		StringBuilder buf = new StringBuilder();
		String newDivId = getNewDivId();
		buf.append("var ").append(newDivId).append(" = $('<div>')").append(".attr('id','").append(newDivId).append("').css({");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%'})\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(newDivId).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}

}
