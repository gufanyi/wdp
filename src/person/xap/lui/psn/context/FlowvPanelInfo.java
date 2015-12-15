package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.refence.BorderStylePageModel;
import xap.lui.psn.refence.BorderStyleViewController;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 */
public class FlowvPanelInfo extends LayoutPanelInfo {
	private static final long serialVersionUID = 1L;
	
	public FlowvPanelInfo(){
		super();
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext4");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		String refWidth = "400";
		String refHeight = "250";		
		String refUrl = "app/mockapp/cdref?model="+BorderStylePageModel.class.getName()+"&ctrl="+BorderStyleViewController.class.getName();
		
		SelfDefRefPropertyInfo topBorder = new SelfDefRefPropertyInfo();
		topBorder.setId("topBorder");
		topBorder.setVisible(true);
		topBorder.setEditable(true);
		topBorder.setType(StringDataTypeConst.STRING);
		topBorder.setDsField("ref_ext1");
		topBorder.setLabel("上边框");
		topBorder.setVoField("topborder");
		topBorder.setUrl(refUrl);
		topBorder.setWidth(refWidth);
		topBorder.setHeight(refHeight);			
		list.add(topBorder);
		
		SelfDefRefPropertyInfo bottomBorder = new SelfDefRefPropertyInfo();
		bottomBorder.setId("bottomBorder");
		bottomBorder.setVisible(true);
		bottomBorder.setEditable(true);
		bottomBorder.setType(StringDataTypeConst.STRING);
		bottomBorder.setDsField("ref_ext2");
		bottomBorder.setLabel("下边框");
		bottomBorder.setVoField("bottomborder");
		bottomBorder.setUrl(refUrl);
		bottomBorder.setWidth(refWidth);
		bottomBorder.setHeight(refHeight);		
		list.add(bottomBorder);
		
		SelfDefRefPropertyInfo leftBorder = new SelfDefRefPropertyInfo();
		leftBorder.setId("leftBorder");
		leftBorder.setVisible(true);
		leftBorder.setEditable(true);
		leftBorder.setType(StringDataTypeConst.STRING);
		leftBorder.setDsField("ref_ext3");
		leftBorder.setLabel("左边框");
		leftBorder.setVoField("leftborder");
		leftBorder.setUrl(refUrl);
		leftBorder.setWidth(refWidth);
		leftBorder.setHeight(refHeight);			
		list.add(leftBorder);
		
		SelfDefRefPropertyInfo rightBorder = new SelfDefRefPropertyInfo();
		rightBorder.setId("rightBorder");
		rightBorder.setVisible(true);
		rightBorder.setEditable(true);
		rightBorder.setType(StringDataTypeConst.STRING);
		rightBorder.setDsField("ref_ext4");
		rightBorder.setLabel("右边框");
		rightBorder.setVoField("rightborder");
		rightBorder.setUrl(refUrl);
		rightBorder.setWidth(refWidth);
		rightBorder.setHeight(refHeight);		
		list.add(rightBorder);
		
		SelfDefRefPropertyInfo borderRefInfo = new SelfDefRefPropertyInfo();
		borderRefInfo.setId("border");
		borderRefInfo.setVisible(true);
		borderRefInfo.setEditable(true);
		borderRefInfo.setType(StringDataTypeConst.STRING);
		borderRefInfo.setDsField("ref_ext5");
		borderRefInfo.setLabel("边框样式");
		borderRefInfo.setUrl(refUrl);
		borderRefInfo.setVoField("border");
		borderRefInfo.setWidth(refWidth);
		borderRefInfo.setHeight(refHeight);
		list.add(borderRefInfo);
		
		StringPropertyInfo anchor = new StringPropertyInfo();
		anchor.setId("anchor");
		anchor.setVisible(true);
		anchor.setEditable(true);
		anchor.setDsField("string_ext5");
		anchor.setLabel("锚点");
		anchor.setVoField("anchor");
		list.add(anchor);
		
	}
}
