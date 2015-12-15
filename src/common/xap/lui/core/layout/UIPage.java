package xap.lui.core.layout;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UIPage extends UILayoutPanel {
	private static final long serialVersionUID = 5927522368179835547L;
	private String folderPath;
	private Map<String, UIPortalLayout> dialogMap = new HashMap<String, UIPortalLayout>();
	public static String TEMPLATE = "template";
	public static String TITLE = "title";
	public static String VERSION = "version";
	public static String SKIN = "skin";
	public static String ISDEFAULT = "isdefault";
	public static String PAGENAME = "pagename";

	private Integer isdefault;
	private String pagename;
	private String version;
	private String skin;
	private String template;
	private String title;

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public Map<String, UIPortalLayout> getDialogMap() {
		return dialogMap;
	}

	public void setDialogMap(Map<String, UIPortalLayout> dialogMap) {
		this.dialogMap = dialogMap;
	}

	public UIPortalLayout getDialog(String id) {
		return dialogMap.get(id);
	}

	public void setDialog(String id, UIPortalLayout dialog) {
		dialogMap.put(id, dialog);
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
	this.version=version;
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin=skin;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
	this.template=template;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title=title;
	}

	protected Map<String, Serializable> createAttrMap() {
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put(ISDEFAULT, 0);
		return map;
	}

	@Override
	public UIPage doClone() {
		UIPage page = (UIPage) super.doClone();
		if (this.dialogMap != null) {
			page.dialogMap = new HashMap<String, UIPortalLayout>();
			Iterator<String> keys = this.dialogMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				UIPortalLayout layout = this.dialogMap.get(key);
				page.dialogMap.put(key, (UIPortalLayout) layout.doClone());
			}
		}

		return page;
	}

}
