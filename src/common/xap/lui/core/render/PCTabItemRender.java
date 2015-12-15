package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabItem;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 页签渲染器
 */
@SuppressWarnings("unchecked")
public class PCTabItemRender extends UILayoutPanelRender<UITabItem, LuiElement> {

	public PCTabItemRender(UITabItem uiEle,PCTabLayoutRender parentRender) {
		super(uiEle);
//		TabItem tabItem = (TabItem)this.getWebElement();
		UITabItem tabItem = this.getUiElement();
		this.i18nName = tabItem.getI18nName();
		this.text = tabItem.getText();
		this.langDir = tabItem.getLangDir();
		if (tabItem.getState() != null)
			this.state = tabItem.getState().toString();
		this.showCloseIcon = tabItem.getShowCloseIcon().equals(UIConstant.TRUE) ? "true" : "false";
//		divId = DIV_PRE + parentRender.getId() + getId();
		this.parentRender=parentRender;

	}

	private String text;
	private String i18nName;
	private String langDir;
	// 页面状态
	private String state;
	private String showCloseIcon;



	public String createHead() {
		PCTabLayoutRender tab = (PCTabLayoutRender) this.getParentRender();
		StringBuilder buf = new StringBuilder();
		String tabId = tab.getVarId();
		// name, title, showCloseIcon, isFirstItem, disabled
		buf.append("var ").append(tabId);
		if(this.getViewId() == null || this.getViewId().equals("")){
			buf.append(" = pageUI.getTab('").append(tab.getId()).append("');");
		}
		else
			buf.append(" = pageUI.getViewPart('").append(this.getViewId()).append("').getTab('").append(tab.getId()).append("');");
		buf.append("var ").append(getId()).append(" = ");
		buf.append(tabId).append(".createItem(\"");
		buf.append(getId()).append("\", \"");
		buf.append(translate(i18nName, text, langDir));
		buf.append("\",").append(showCloseIcon); // item.isShowCloseIcon()
		buf.append(",").append(false); // item.isFirstItem()
		buf.append(",").append(false); // item.isDisabled()
		buf.append(",'").append(state == null ? "" : state).append("');\n");
		buf.append("var tmpDiv = $(\"#").append(getNewDivId()).append("\");\n");
		// .append("tmpDiv.style.display = \"\";\n");

		buf.append(getId()).append(".getObjHtml().appendChild(tmpDiv[0]);\n");
		// 再将tmpDiv的定位属性设为相对
		buf.append("tmpDiv.css('position','relative');\n");

		// 此处是防止tmpDiv出现为0的情况(IE渲染的奇怪行为,出现在第三个tab上，具体为什么搞不清楚），遇到这种情况，就把实际的子加到祖父上面。
		buf.append("if(tmpDiv.outerWidth() == 0){tmpDiv.parent().append(tmpDiv.children().first());tmpDiv.remove();}\n");

		buf.append("window.$").append(tab.getId()).append("_").append(getId()).append("_init = function(){\n");

		// 将已有的脚本暂存在临时变量中
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuilder();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		this.setContextAttribute("$tabitem_" + getId() + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());
		
		return buf.toString();
	}


	public String createTail() {
		PCTabLayoutRender tab = (PCTabLayoutRender) this.getParentRender();
		// TabComp tab = (TabComp) tabTag.getComponent();
		StringBuilder tmpBuf = new StringBuilder();
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		
		dsScript.append(PcFormRenderUtil.getAllFormDsScript(this.getViewId()));//自由表单的数据集的设置
		PcFormRenderUtil.removeFormDsScript(this.getViewId());	
		
		String tmpScript = (String) this.getContextAttribute("$tabitem_" + getId() + "$tmpScript");
		tmpBuf.append(dsScript.toString());
		dsScript.delete(0, dsScript.length());
		dsScript.append(tmpScript.toString());
		// 如果是当前显示项目
		if (tab.getCurrentIndex() != tab.getItemList().indexOf(getId())) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$tabitem_" + getId() + "$tmpScript");
		tmpBuf.append("pageUI.updateInitedWidgets();\n");
		tmpBuf.append("};\n");
		return tmpBuf.toString();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TABITEM;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemTitle(String title) {
		StringBuilder buf = new StringBuilder();
		UITabComp tab = (UITabComp) getUiElement().getLayout();
		String showId = createVarId(tab.getViewId(), tab);
		String id = tab.getId();
		String widget = tab.getViewId();
		if (widget != null) {
//			String widget = WIDGET_PRE + luiWidget.getId();
			String widgetShowId = WIDGET_PRE + widget;
			buf.append("var ").append(widgetShowId).append(" = pageUI.getViewPart('" + widget + "');\n");
			buf.append("var "+showId+" = ").append(widgetShowId + ".getTab('" + id + "');\n");
		} else {
			buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
		}
		buf.append("var "+getId()+" = " ).append(showId).append(".getItemByName('"+getId()+"');\n");
		buf.append(getId()).append(".changeTitle('"+title+"');\n");
		buf.append("$(window).triggerHandler('resize');\n");
		this.addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemVisible(boolean visible) {
		StringBuilder buf = new StringBuilder();
		UITabComp tab = (UITabComp) getUiElement().getLayout();
		String showId = createVarId(tab.getViewId(), tab);
		String id = tab.getId();
		String widget = tab.getViewId();
		if (widget != null) {
//			String widget = WIDGET_PRE + luiWidget.getId();
			String widgetShowId = WIDGET_PRE + widget;
			buf.append("var ").append(widgetShowId).append(" = pageUI.getViewPart('" + widget + "');\n");
			buf.append("var "+showId+" = ").append(widgetShowId + ".getTab('" + id + "');\n");
		} else {
			buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
		}
		buf.append("if(").append(showId).append("){");
		if (visible)
			buf.append(showId).append(".showItem('"+getId()+"');\n");
		else
			buf.append(showId).append(".hideItem('"+getId()+"');\n");
		buf.append("}");
		this.addDynamicScript(buf.toString());
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setItemText(String text) {
		StringBuilder buf = new StringBuilder();
		
		UITabItem item = this.getUiElement();
		UITabComp comp = (UITabComp) item.getLayout();
		String showId = createVarId(comp.getViewId(), comp);
		String id = comp.getId();
		String widget = comp.getViewId();
		if (widget != null) {
			String widgetShowId = WIDGET_PRE + widget;
			buf.append("var ").append(widgetShowId).append(" = pageUI.getViewPart('"+ widget + "');\n");
			buf.append("var "+showId+" = ").append(widgetShowId + ".getTab('" + id + "');\n");
		} else {
			buf.append("var "+showId+" = ").append("pageUI.getTab(" + id + ");\n");
		}
		buf.append("var "+getId()+" = " ).append(showId).append(".getItemByName('"+getId()+"');\n");
		buf.append("if("+getId()+")"+getId()).append(".changeTitle('"+text+"');\n");
		buf.append("$(window).triggerHandler('resize');\n");
		this.addDynamicScript(buf.toString());
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		String divId = this.getDivId();
		StringBuilder buf = new StringBuilder();
		UITabItem item = this.getUiElement();
		
		
		if (divId != null) {
			UIElement child = item.getElement();
			if (child != null) {
				child.getRender().destroy();
			}
			
			UITabComp tab = (UITabComp) item.getLayout();
			
			String tabVarId = this.createVarId(viewId, tab);
			String tabId = tab.getId();
			// name, title, showCloseIcon, isFirstItem, disabled
			buf.append("var ").append(tabVarId);
			if(this.getViewId() == null || this.getViewId().equals("")){
				buf.append(" = pageUI.getTab('").append(tabId).append("');\n");
			}
			else
				buf.append(" = pageUI.getViewPart('").append(this.getViewId()).append("').getTab('").append(tabId).append("');\n");
			
			
			buf.append(tabVarId).append(".removeItemTab(").append(tabVarId).append(".currActiveTab);\n");
			buf.append("$(window).triggerHandler('resize');\n");
		} else {
			buf.append("alert('删除panel失败！未获得divId')");
		}

		addDynamicScript(buf.toString());
	}

}
