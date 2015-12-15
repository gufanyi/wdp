package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;


public class WidgetInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	public WidgetInfo(){
		super();
		
		StringPropertyInfo refId = new StringPropertyInfo();
		refId.setId("refId");
		refId.setVisible(true);
		refId.setEditable(false);
		refId.setDsField("string_ext4");
		refId.setVoField("refId");
		refId.setLabel("refId");
		list.add(refId);
		
		StringPropertyInfo controller = new StringPropertyInfo();
		controller.setId("controller");
		controller.setVisible(true);
		controller.setEditable(false);
		controller.setDsField("string_ext5");
		controller.setVoField("controller");
		controller.setLabel("窗体控制类");
		list.add(controller);

		StringPropertyInfo srcFolder = new StringPropertyInfo();
		srcFolder.setId("srcFolder");
		srcFolder.setVisible(true);
		srcFolder.setEditable(false);
		srcFolder.setDsField("string_ext6");
		srcFolder.setVoField("srcFolder");
		srcFolder.setLabel("源代码文件夹");
		list.add(srcFolder);
		
		ComboPropertyInfo isDialog = new ComboPropertyInfo();
		isDialog.setId("dialog");
		isDialog.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		isDialog.setEditable(true);
		isDialog.setType(StringDataTypeConst.BOOLEAN);
		isDialog.setKeys(new String[]{"是", "否"});
		isDialog.setValues(new String[]{"Y", "N"});
		isDialog.setDsField("combo_ext1");
		isDialog.setVoField("dialog");
		isDialog.setLabel("是否是对话框");
		list.add(isDialog);

	}
}
