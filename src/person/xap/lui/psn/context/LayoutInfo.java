/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.refence.CssStylePageModel;
import xap.lui.psn.refence.CssStyleViewController;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class LayoutInfo extends BaseInfo {
	private static final long serialVersionUID = 1L;

	public LayoutInfo(){
		super();
		StringPropertyInfo widgetId = new StringPropertyInfo();
		widgetId.setId("widgetId");
		widgetId.setVisible(false);
		widgetId.setEditable(true);
		widgetId.setDsField("string_ext2");
		widgetId.setLabel("widgetId");
		widgetId.setVoField("widgetid");
		list.add(widgetId);
		
		SelfDefRefPropertyInfo cssStyleRef = new SelfDefRefPropertyInfo();
		cssStyleRef.setId("cssStyle");
		cssStyleRef.setVisible(true);
		cssStyleRef.setEditable(true);
		cssStyleRef.setType(StringDataTypeConst.STRING);
		cssStyleRef.setDsField("ref_ext2");
		cssStyleRef.setLabel("自定义CSS样式");
		cssStyleRef.setUrl("app/mockapp/cdref?model="+CssStylePageModel.class.getName()+"&ctrl="+CssStyleViewController.class.getName());
		cssStyleRef.setVoField("cssstyle");
		cssStyleRef.setWidth("600");
		cssStyleRef.setHeight("450");
		list.add(cssStyleRef);
		
//		StringPropertyInfo cssStyle = new StringPropertyInfo();
//		cssStyle.setId("cssStyle");
//		cssStyle.setVisible(true);
//		cssStyle.setEditable(true);
//		cssStyle.setDsField("string_ext24");
//		cssStyle.setLabel("cssStyle");
//		cssStyle.setVoField("cssstyle");
//		list.add(cssStyle);
//		
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
	}
	 

}
