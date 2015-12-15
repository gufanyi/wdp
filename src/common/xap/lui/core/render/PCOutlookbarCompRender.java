package xap.lui.core.render;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;


/**
 * Outlookbar控件渲染器 
 * OutLookBarComp控件,提供类似百叶窗式的树行控件
 * @author renxh 
 */
@SuppressWarnings("unchecked")
public class PCOutlookbarCompRender extends UILayoutRender<UIShutter, LuiElement> {
	private String className;
	private int currentIndex = 0;
	private List<String> itemList = new ArrayList<String>();
	public int getCurrentIndex() {
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	public List<String> getItemList() {
		return itemList;
	}
	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_OUTLOOKBAR;
	}
	
	public PCOutlookbarCompRender(UIShutter uiEle) {
		super(uiEle);
		UIShutter shutter = this.getUiElement();
		this.className = shutter.getClassName();
		if(shutter.getCurrentItem() != null)
			this.currentIndex = shutter.getCurrentItem();
	}


	public String createHead() {
		StringBuilder buf = new StringBuilder();
		String showId = getVarId();
		buf.append("var ").append(showId).append(" = $(\"<div id='"+getId()+"'></div>\").appendTo($('#"+getDivId()+"')).outlookbar({\n");
		if(!StringUtils.isEmpty(className)) {
			buf.append("className : '"+className+"',");
		}
		buf.append("position : 'relative'\n");
		buf.append("}).outlookbar('instance');\n");
		
		if (getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addOutlook(" + getVarId() + ");\n");
		} else {
			buf.append("pageUI.addOutlook(" + getVarId() + ");\n");
		}
		UIShutter shutter = this.getUiElement();
		if (shutter != null)
			buf.append(addEventSupport(shutter, getViewId(), showId, null));
		return buf.toString();
	}



	public String createTail() {
		StringBuilder buf = new StringBuilder();
//		buf.append(getVarId()).append(".afterActivedItemChangeForInternal = function(currItem){\n");
//		buf.append("var tmpFunc = window['$' + currItem.parentOwner.id + '_' + currItem.options.name + '_init'];\n");
//		buf.append("if(tmpFunc){\ntmpFunc();\n");
//		buf.append("window['$' + currItem.parentOwner.id + '_' + currItem.options.name + '_init'] = null;\n}\n");
//		buf.append("};\n");
		buf.append(getVarId()).append(".activeItem(").append(currentIndex).append(");\n");
		return buf.toString();
	}
	
	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'top':'0px',\n");
		buf.append("'left':'0px',\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'padding':'0px',\n");
		buf.append("'overflow':'hidden'});\n");
		if(this.isEditMode()){
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append("+getDivId()+");\n");
		}
		return buf.toString();
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setActiveItem(int itemIndex) {
		StringBuilder buf = new StringBuilder();
		if(this.getViewId() != null){
			buf.append("var curWidget = pageUI.getViewPart('").append(this.getViewId()).append("');\n");
			buf.append("var ").append(getVarId()).append(" = curWidget.getOutlook('" + this.getId() + "');\n");
		}
		else{
			buf.append("var ").append(getVarId()).append(" = pageUI.getOutlook('" + getId() + "');\n");
		}
		buf.append("if("+getVarId()+"){\n");
		buf.append(getVarId()).append(".activeItem(" + itemIndex + ");\n");
		buf.append("};\n");
		buf.append("$(window).triggerHandler('resize');\n");		
		addBeforeExeScript(buf.toString());
	}
	
	/**
	 * @param  obj为子元素
	 */
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild( UIElement obj) {
		StringBuilder buf = new StringBuilder();
		UILayout uilayout = this.getUiElement();
		if (this.getDivId() != null) {
			List<UILayoutPanel> children = uilayout.getPanelList();
			String removeId = "";
			for (int i = 0; i < children.size(); i++) {
				UILayoutPanel panel = children.get(i);
				if (obj == panel) {
					//调用shutitem的销毁方法
					removeId =panel.getId();
					panel.getRender().destroy();
					break;
				}
			}
			if(this.getViewId() != null){
				buf.append("var curWidget = pageUI.getViewPart('").append(this.getViewId()).append("');\n");
				buf.append("var ").append(getVarId()).append(" = curWidget.getOutlook('" + this.getId() + "');\n");
			}
			else{
				buf.append("var ").append(getVarId()).append(" = pageUI.getOutlook('" + getId() + "');\n");
			}
			buf.append("if("+getVarId()+"){\n");
			buf.append(getVarId()).append(".removeItem('"+removeId+"');\n");
			buf.append("};\n");			
		} else {
			//buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("ra", "UILayoutRender-000000"));
		}
		addDynamicScript(buf.toString());
	}	
	
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addChild( UIElement obj) {
		UILayout layout = getUiElement();
		int size = layout.getPanelList().size();
		UILayoutPanel targetPanel = (UILayoutPanel) obj;
		int index = 0;
		for (int i = 0; i < size; i++) {
			UILayoutPanel panel = layout.getPanelList().get(i);
			if(panel.getId().equals(targetPanel.getId())){
				index = i;
				break;
			}
		}
		
		//IControlPlugin plugin = ControlFramework.getInstance().getControlPluginByUIClass(obj.getClass());
		ILuiRender render = targetPanel.getRender();
		
		StringBuilder buf = new StringBuilder();
		String html = render.place();
		buf.append(html);
		
		buf.append("var div = $('#" + divId + "');\n");
		if (index == (size - 1)){
			buf.append("div.append(" + render.getNewDivId() + ");\n");
		} else {
			//childNodes会出错，使用children。childNodes会返回TextNodes等
			buf.append("div.prepend(" + render.getNewDivId() + ");\n");
		}
		buf.append("var curWidget = pageUI.getViewPart('").append(this.getViewId()).append("');\n");
		buf.append("var ").append(getVarId()).append(" = curWidget.getOutlook('" + this.getId() + "');\n");		
		buf.append(render.create());
		
		buf.append("if("+getVarId()+"){\n");
		buf.append(getVarId()).append(".activeItem("+(size-1)+");\n");
		buf.append("};\n");		
		buf.append("$(window).triggerHandler('resize');\n");
		addDynamicScript(buf.toString());
	}	
}
