package xap.lui.psn.context;

/**
 * 自定义参照属性
 * @author licza
 *
 */
public class SelfDefRefPropertyInfo extends BasePropertyInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	 
	String url;
	String width;
	String height;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	 
	
}
