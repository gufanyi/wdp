package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCOutlookItemRender;
import xap.lui.core.render.PCOutlookbarCompRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIShutterItem extends UILayoutPanel {
	private static final long serialVersionUID = 1498487520155539622L;
	public static final String TEXT = "text";
	public static final String I18NNAME = "i18nName";
	
	public static final String LANGDIRF = "langDir";
	
	private String id;
	private String text;
	private String i18nName;
	private String langDir;
	
	public void setId(String id) {
	this.id=id;
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		setI18nName(null);
		this.text=text;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
	this.i18nName=name;
	}
	
	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir=langDir;
	}

	@Override
	public ILuiRender getRender() {
		if(render==null){
			PCOutlookbarCompRender parentRender=(PCOutlookbarCompRender)this.getLayout().getRender();
			render = RenderProxy.getRender(new PCOutlookItemRender(this,parentRender));
		}
		return render;
	}
	
	

}
