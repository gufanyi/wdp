package xap.lui.psn.context;

import xap.lui.core.control.ModePhase;

/**
 * PagePartMeta的个性化和表单设置的信息类
 */
public class PagePartMetaInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	
	public PagePartMetaInfo(){
		super();
		
		StringPropertyInfo caption = new StringPropertyInfo();
		caption.setId("caption");
		caption.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		caption.setEditable(true);
		caption.setDsField("string_ext4");
		caption.setVoField("caption");
		caption.setLabel("标题描述");
		list.add(caption);
		
		StringPropertyInfo controller = new StringPropertyInfo();
		controller.setId("controller");
		controller.setVisible(true);
		controller.setEditable(false);
		controller.setDsField("string_ext5");
		controller.setVoField("controller");
		controller.setLabel("页面控制类");
		list.add(controller);
		
		StringPropertyInfo srcFolder = new StringPropertyInfo();
		srcFolder.setId("srcFolder");
		srcFolder.setVisible(true);
		srcFolder.setEditable(false);
		srcFolder.setDsField("string_ext6");
		srcFolder.setVoField("srcFolder");
		srcFolder.setLabel("源代码文件夹");
		list.add(srcFolder);
		
		StringPropertyInfo windowType = new StringPropertyInfo();
		windowType.setId("windowType");
		windowType.setVisible(true);
		windowType.setEditable(false);
		windowType.setDsField("string_ext7");
		windowType.setVoField("windowType");
		windowType.setLabel("window类型");
		list.add(windowType);
	}
}
