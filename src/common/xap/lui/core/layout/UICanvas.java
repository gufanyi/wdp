package xap.lui.core.layout;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCCanvasLayoutRender;
import xap.lui.core.render.notify.RenderProxy;


/**
 * panel UI
 * @author zhangxya
 *
 */
public class UICanvas extends UILayout {
	private static final long serialVersionUID = 1L;
	public static final String NULLCANVAS = "nonecanvas";
	public static final String LEFTCANVAS = "leftcanvas";
	public static final String RIGHTCANVAS = "rightcanvas";
	public static final String FULLCANVAS = "fullcanvas";
	public static final String CENTERCANVAS = "centercanvas";
	public static final String TITLE = "title";
	public static final String I18NNAME = "i18nName";
	public static final String LANGDIRF = "langDir";
	
	private String title;
	private String i18nName;
	private String langDir;
	public UICanvas(){
		setClassName(FULLCANVAS);
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String i18nName) {
		this.i18nName = i18nName;
	}
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	@Override
	public ILuiRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCCanvasLayoutRender(this));
		}
		return render;
	}
	
	
	
}
