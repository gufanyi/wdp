package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;
import xap.lui.psn.refence.CssStylePageModel;
import xap.lui.psn.refence.CssStyleViewController;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public class LayoutPanelInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	
	public LayoutPanelInfo(){
		super();
		StringPropertyInfo widgetid = new StringPropertyInfo();
		widgetid.setId("widgetId");
		widgetid.setVisible(false);
		widgetid.setEditable(true);
		widgetid.setDsField("string_ext2");
		widgetid.setLabel("widgetId");
		widgetid.setVoField("widgetid");
		list.add(widgetid);
		
		StringPropertyInfo topPadding = new StringPropertyInfo();
		topPadding.setId("topPadding");
		topPadding.setVisible(true);
		topPadding.setEditable(true);
		topPadding.setDsField("string_ext16");
		topPadding.setLabel("上边距");
		topPadding.setVoField("toppadding");
		list.add(topPadding);
		
		StringPropertyInfo bottomPadding = new StringPropertyInfo();
		bottomPadding.setId("bottomPadding");
		bottomPadding.setVisible(true);
		bottomPadding.setEditable(true);
		bottomPadding.setDsField("string_ext17");
		bottomPadding.setLabel("下边距");
		bottomPadding.setVoField("bottompadding");
		list.add(bottomPadding);
		
		StringPropertyInfo leftPadding = new StringPropertyInfo();
		leftPadding.setId("leftPadding");
		leftPadding.setVisible(true);
		leftPadding.setEditable(true);
		leftPadding.setDsField("string_ext18");
		leftPadding.setLabel("左边距");
		leftPadding.setVoField("leftpadding");
		list.add(leftPadding);
		
		StringPropertyInfo rightPadding = new StringPropertyInfo();
		rightPadding.setId("rightPadding");
		rightPadding.setVisible(true);
		rightPadding.setEditable(true);
		rightPadding.setDsField("string_ext19");
		rightPadding.setLabel("右边距");
		rightPadding.setVoField("rightpadding");
		list.add(rightPadding);
		
		

		
		SelfDefRefPropertyInfo cssStyleRef = new SelfDefRefPropertyInfo();
		cssStyleRef.setId("cssStyle");
		cssStyleRef.setVisible(true);
		cssStyleRef.setEditable(true);
		cssStyleRef.setType(StringDataTypeConst.STRING);
		cssStyleRef.setDsField("ref_ext6");
		cssStyleRef.setLabel("自定义CSS样式");
		cssStyleRef.setUrl("app/mockapp/cdref?model="+CssStylePageModel.class.getName()+"&ctrl="+CssStyleViewController.class.getName());
		cssStyleRef.setVoField("cssstyle");
		cssStyleRef.setWidth("560");
		cssStyleRef.setHeight("400");
		list.add(cssStyleRef);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(new ModePhase[]{ModePhase.eclipse, ModePhase.persona},true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
		StringPropertyInfo childtype = new StringPropertyInfo();
		childtype.setId("");
		childtype.setVisible(false);
		childtype.setEditable(true);
		childtype.setDsField("childtype");
		childtype.setLabel("子类型");
		childtype.setVoField("childtype");
		list.add(childtype);
		
		StringPropertyInfo childid = new StringPropertyInfo();
		childid.setId("");
		childid.setVisible(false);
		childid.setEditable(true);
		childid.setDsField("childid");
		childid.setLabel("子ID");
		childid.setVoField("childid");
		list.add(childid);
	}
}
