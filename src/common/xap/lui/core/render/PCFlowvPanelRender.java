package xap.lui.core.render;

import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh
 * 纵向布局的panel渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCFlowvPanelRender extends UILayoutPanelRender<UIFlowvPanel, LuiElement> {
	// 高度
	private String height;
	// 锚点名称
	private String anchor;

	public PCFlowvPanelRender(UIFlowvPanel uiEle) {
		super(uiEle);
		UIFlowvPanel flowvPanel = this.getUiElement();
		this.height = this.getFormatSize(flowvPanel.getHeight());
		if(this.height.equals("0px"))
			this.height = null;
		this.anchor = flowvPanel.getAnchor();
		//this.className = uiEle.getClassName();
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
//		buf.append(getNewDivId()).append(".style.width = '100%';\n");
		buf.append("'left':'0px',\n");
		buf.append("'position':'relative'})");
//		buf.append(getNewDivId()).append(".style.overflow = 'auto';\n");
		buf.append(".addClass('").append(this.getUiElement().getClassName()).append("');\n");
		
		getBorderScript(buf, getNewDivId());
		getPaddingLeftScript(buf, getNewDivId());
		getPaddingRightScript(buf, getNewDivId());
		getPaddingTopScript(buf, getNewDivId());
		getPaddingBottomScript(buf, getNewDivId());
		getCssStylesScript(buf, getNewDivId());
		
		if (height != null) {
			buf.append(getNewDivId()).append(".css('height','"+height+"');\n");
			buf.append(getNewDivId()).append(".attr('hasheight','1');\n");
		} else {
			buf.append(getNewDivId()).append(".attr('hasheight','0');\n");
//			buf.append(getNewDivId()).append(".style.minHeight = '30px';\n");
		}
		if (anchor != null)
			buf.append(getNewDivId()).append(".prop('anchor','").append(anchor).append("');\n");
		
		if(this.isEditMode()){
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
	


	/**
	 * 2011-8-2 下午07:02:06 renxh des：编辑态时，需加入此div
	 * 
	 * @return
	 */
	public String placeDesign() {
		if (isEditMode()) {

			StringBuilder buf = new StringBuilder("");
			buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({\n");
			buf.append("'position':'relative',\n");
			buf.append("'height':'100%',\n");
			buf.append("'overflow-x':'hidden'});\n");
			if(isFlowMode()){
				buf.append(getDivId()).append(".css('min-height','" + MIN_HEIGHT + "');\n");
				buf.append(getDivId()).append(".attr('flowmode',true);\n");
			}
			return buf.toString();
		}
		return "";
	}


	public String createTail() {
		return "";
	}

	public String getHeight() {
		return height;
	}


	public String getAnchor() {
		return anchor;
	}

	public void setAnchor(String anchor) {
		this.anchor = anchor;
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_FLOWVPANEL;
	}


	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setHeight(String height) {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('#"+getNewDivId()+"');\n");
		if (StringUtils.isNotBlank(height)) {
			if(height.endsWith("%")) {
				buf.append(getNewDivId()).append(".css('height','" + height + "');\n");
			} else {
				buf.append(getNewDivId()).append(".css('height','" + height + "px');\n");
			}
			
			buf.append(getNewDivId()).append(".attr('hasheight','1');\n");
		} else {
			buf.append(getNewDivId()).append(".attr('hasheight','0');\n");
		}
		if (anchor != null)
			buf.append(getNewDivId()).append(".prop('anchor','").append(anchor).append("');\n");
		
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}
	

}
