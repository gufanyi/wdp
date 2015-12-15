package xap.lui.core.render;

import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.logger.LuiLogger;

/**
 * @author renxh
 * 非容器类控件渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public abstract class UINormalComponentRender<T extends UIComponent, K extends WebComp> extends UIComponentRender<T, K> {

	protected String align;

	// 父节点为panel 或者容器类组件
	public UINormalComponentRender(K webEle) {
		super(webEle);
		UIComponent uiEle = this.getUiElement();
		if(uiEle != null){
			this.align = uiEle.getAlign();
		}
		if(this.align == null)
			this.align = UIComponent.ALIGN_LEFT;
	}


	
	
	public String create(){
		StringBuffer buf = new StringBuffer();
		String script = (String) getAttribute(BEFORE_SCRIPT);// 暂时还不知道如何处理
		if (script != null)
			buf.append(script);
		buf.append(this.createDesignHead());
		
		buf.append(this.createBody());

		WebComp comp = (WebComp) this.getWebElement();
		
		String varId = getVarId();
		
		if(comp instanceof FormElement) {
			buf.append(this.addEventSupport(comp, getViewId(), null, ((FormElement) comp).getParent().getId()));
		} else {
			String compStr = "pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + getId() + "')";
			buf.append(this.addEventSupport(comp, getViewId(), compStr, null));
		}
		
		if (comp.getContextMenu() != null && !comp.getContextMenu().equals("")) {
			script = addContextMenu(comp.getContextMenu(), varId);
			buf.append(script);
		}

		script = this.createDsBinding();
		if (script != null){
			StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
			if(dsScript!=null){
				dsScript.append(script);
			}else{
				dsScript = new StringBuilder();
				dsScript.append(script);
				this.setContextAttribute(DS_SCRIPT, dsScript);
			}
		}
		buf.append(this.creatBodyTail());
		comp.setRendered(true);
		
		String wstr = setWidgetToComponent();
		if(wstr != null)
			buf.toString();
		
		if(this.getWebElement() instanceof SelfDefComp){// 自定义组件特殊处理
			String id = getVarId();
			script = id + ".oncreate();\n";
			buf.append(script);
		}
		
		buf.append(this.createDesignTail());
		script = (String) getAttribute(AFTER_SCRIPT);
		if (script != null)
			buf.append(script);

		//return wrapByRequired("", buf.toString());
		
		return buf.toString();
	}
	
	protected String creatBodyTail() {
		return "";
	}




	/**
	 * 将Widget属性绑定到控件上
	 */
	protected String setWidgetToComponent() {
		if (this.getViewId() != null){
			StringBuffer buf = new StringBuffer();
			buf.append("pageUI.getViewPart('").append(this.getViewId()).append("').getComponent('").append(getId()).append("').viewpart = " + WIDGET_PRE + this.getViewId()).append("\n");
			return buf.toString();
		}
		return "";
	}

	/* (non-Javadoc)
	 * 创建渲染脚本
	 */
	public String createDesignHead() {
		return "";
	}
	public String createDesignTail() {
		StringBuilder buf = new StringBuilder();
		if(this.isEditMode()){
			if(this.isGenEditableTail()){
				return "";
			}
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement() == null ? "" : this.getWebElement().getId();
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";
			
			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {widgetid:'"+widgetId+"',uiid:'"+uiid+"',eleid:'"+eleid+"',type:'"+type+"'};\n");
				buf.append("$.design.getObj({divObj:$('#" + getDivId() + "')[0],params:params,objType:'component'});\n");
			}
			if(this.getDivId() != null){
				buf.append("$('#").append(this.getDivId()).append("').css('padding','0px');\n");
			}
		}
		return buf.toString();
	}



	protected abstract String getSourceType(IEventSupport ele);

	public abstract String createBody();

	/**
	 * 提供给子类覆盖的方法
	 */
	public String createDsBinding() {
		return "";
	}
	

	
	/**
	 * 2011-10-10 下午01:28:18 renxh
	 * des：利用js脚本创建DIV
	 * @return
	 */
	public String place(){
		StringBuilder buf = new StringBuilder();
		UIComponent comp = this.getUiElement();
		buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("').css({");
		String width = comp.getWidth();
		if(width != null && !width.equals("100%")){
			width = width.indexOf("%") != -1 ? width : width + "px";
			buf.append("'width':'").append(width).append("',");
		}
		String height = comp.getHeight();
		if(height != null){
			height = height.indexOf("%") != -1 ? height : height + "px";
			buf.append("'height':'").append(height).append("',");
		}
		if(this.align.equals(UIComponent.ALIGN_RIGHT))
			buf.append("'float':'right',");
		buf.append("'top':'").append(comp.getTop() + "px',");
		buf.append("'left':'").append(comp.getLeft() + "px',");
		buf.append("'position':'").append(comp.getPosition()).append("'});");
//		buf.append(getDivId()).append(".style.overflow = 'hidden';");
//		buf.append(getDivId()).append(".style.margin = '0 auto';");
		return buf.toString();
	}
	

	@Override
	public void destroy(){
		String divId = this.getDivId();
		UIComponent uiEle = this.getUiElement();
		
		StringBuilder buf = new StringBuilder();
		if (divId != null) {
			buf.append("window.execDynamicScript2RemoveComponent('" + divId + "','" + uiEle.getViewId() + "','" + uiEle.getId() + "');");
			this.removeComponent(uiEle.getViewId(), uiEle.getId(), isMenu(uiEle));
		} else {
			buf.append("alert('删除控件失败！未获得divId')");
		}
		addDynamicScript(buf.toString());
	}
	
	
	
	public void setVisible(boolean isVisible){
		StringBuffer buf = new StringBuffer();
		buf.append("debugger;\nvar text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setVisible) text.setVisible("+ isVisible +");\n");
		addDynamicScript(buf.toString());
	}

	public void setAlign(String align){
		if(align == null){
			return;
		}
		String divId = getDivId();
		StringBuffer buf = new StringBuffer();
		buf.append("$('#"+divId+"').css('float','").append(align).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	
	public void setWidth(String width){
		String divId = getDivId();
		StringBuffer buf = new StringBuffer();
		buf.append("$('#"+divId+"').css('width','");
		buf.append(width);
		if(width.endsWith("%")) {
			buf.append("');");
		} else {
			buf.append("px');");
		}
		addDynamicScript(buf.toString());
	}
	
	public void setHeight(String height){
		String divId = getDivId();
		StringBuffer buf = new StringBuffer();
		buf.append("$('#"+divId+"').css('height','");
		buf.append(height);
		if(height.endsWith("%")) {
			buf.append("');");
		} else {
			buf.append("px');");
		}
		addDynamicScript(buf.toString());
	}
	
	public void setClassName(String className){
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(widget).append(" = pageUI.getViewPart('"+this.getCurrWidget().getId()+"');\n");
		buf.append("var comp = ").append(widget).append(".getComponent('" + id + "');\n");
		buf.append("comp.changeClass('").append(className).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setValue(String value){
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		//String value = ((TextComp)this.getWebElement()).getValue();
		if(value == null)
			buf.append("if(text.setValue){ text.setValue(null);}\n");
		else
			buf.append("if(text.setValue){ text.setValue('").append(value).append("');}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setChecked(boolean isChecked){
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");		
		buf.append("if(text.setChecked){ text.setChecked("+ isChecked+");}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setEnable(boolean isEnable){
		StringBuffer buf = new StringBuffer();
		buf.append(" var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");		
		buf.append("if(text!=null&&text.setActive){ text.setActive("+ isEnable+");}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setText(String text){
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setLabelText) text.setLabelText('"+ text +"');\n");
		addDynamicScript(buf.toString());
	}
	
	public void clearValue(){
		StringBuffer buf = new StringBuffer();
		buf.append("var text = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if(text.setValue) text.setValue('');\n");
		buf.append("if(text.setShowValue) text.setShowValue('');\n");
		addDynamicScript(buf.toString());
	}
	
	/**
	 * ID更新操作
	 */
	protected void notifyUpdateId() {
		
	}

	/**
	 * 获得新的divId，因为对编辑态时，需要将最外层的div改变id
	 * 
	 * @return
	 */
	public String getNewDivId() {
		return this.getDivId();
	}
	
	
	@Override
	public void removeChild(UIElement obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addChild(UIElement obj) {
		// TODO Auto-generated method stub
		
	}
}
