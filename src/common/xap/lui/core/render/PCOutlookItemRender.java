package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIShutterItem;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh
 * PCOutlookItemRender
 */
@SuppressWarnings("unchecked")
public class PCOutlookItemRender extends UILayoutPanelRender<UIShutterItem, LuiElement> {

//	public PCOutlookItemRender(UIShutterItem uiEle) {
//		super(uiEle);
//		UIShutterItem item = this.getUiElement();
//		this.text = item.getText();
//		this.i18nName = item.getI18nName();
//		this.langDir = item.getLangDir();
//	}
	
	
	public PCOutlookItemRender(UIShutterItem uiEle,PCOutlookbarCompRender parentRender) {
		super(uiEle);
		UIShutterItem item = this.getUiElement();
		this.text = item.getText();
		this.i18nName = item.getI18nName();
		this.langDir = item.getLangDir();
		this.parentRender=parentRender;
	}

	private String text;
	private String i18nName;
	private String image;
	private String langDir;

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_OUTLOOKBAR_ITEM;
	}


	public String createHead() {
		PCOutlookbarCompRender parent = (PCOutlookbarCompRender)this.getParentRender();
		String outlookId = parent.getId();
		String outlookShowId = parent.getVarId();
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getId()).append(" = ");
		buf.append(outlookShowId).append(".addItem(\"").append(getId());
		
		String showValue = translate(i18nName, text, langDir);
		buf.append("\",\"").append(showValue).append("\",");
		if (image != null)
			buf.append("\"");
		buf.append(image);
		if (image != null)
			buf.append("\"");
		buf.append(",\"").append(text == null ? i18nName : text).append("\");\n");

		buf.append(getId()).append(".add($(\""+"#").append(getNewDivId()).append("\"));\n");

		buf.append("window.$").append(outlookId).append("_").append(getId()).append("_init = function(){\n");

		// 将已有的脚本暂存在临时变量中
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuilder();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		this.setContextAttribute("$item_" + getId() + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());

		return buf.toString();
	}

	public String createTail() {

		PCOutlookbarCompRender parent = (PCOutlookbarCompRender) this.getParentRender();
		StringBuilder tmpBuf = new StringBuilder();
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuilder();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		
		String tmpScript = (String) this.getContextAttribute("$item_" + getId() + "$tmpScript");
		// 如果是当前显示项目
		if (parent.getCurrentIndex() != parent.getItemList().indexOf(getId())) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			dsScript.append(tmpScript);
		} else {
			dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$item_" + getId() + "$tmpScript");
		tmpBuf.append("\n};\n");
		return tmpBuf.toString();
	}	
	
	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width':'100%',\n");
		buf.append("'height':'100%',\n");
		buf.append("'overflow':'hidden'});\n");
		if(this.isEditMode()){
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append("+getDivId()+");\n");
		}
		return buf.toString();
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		StringBuilder buf = new StringBuilder();
		String divId = this.getDivId();
		UILayoutPanel panel = this.getUiElement();
		if (divId != null) {
			UIElement child = panel.getElement();
			if (child != null) {
				child.getRender().destroy();
			}
			buf.append("window.execDynamicScript2RemovePanel('"+divId+"');\n");
			buf.append("$(window).triggerHandler('resize');\n");
		} else {
		//	buf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("ra", "UILayoutPanelRender-000000"));
		}
		addDynamicScript(buf.toString());		
	}
}
