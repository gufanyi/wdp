/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6	
 */
public class ControlInfo extends BaseInfo {
	
	private static final long serialVersionUID = 1L;
	
	public static String[] TEXTTYPE = {
		EditorTypeConst.STRINGTEXT,EditorTypeConst.INTEGERTEXT, EditorTypeConst.CHECKBOX, 
		EditorTypeConst.CHECKBOXGROUP,EditorTypeConst.RADIOCOMP,EditorTypeConst.RADIOGROUP, 
		EditorTypeConst.DECIMALTEXT, EditorTypeConst.PWDTEXT, EditorTypeConst.DATETEXT,
		EditorTypeConst.DATETIMETEXT, EditorTypeConst.FILEUPLOAD,EditorTypeConst.REFERENCE, 
		EditorTypeConst.COMBODATA, EditorTypeConst.LANGUAGECOMBODATA, EditorTypeConst.TEXTAREA,
		EditorTypeConst.RICHEDITOR,
		EditorTypeConst.SELFDEF, EditorTypeConst.FILECOMP,
		EditorTypeConst.LIST
	};
	
	public String[] getValue(){
		return new String[]{
				"字符",
				"整型", 
				"复选框",
				"复选框组",
				"单选按钮",
				"单选按钮组",
				"小数", 
				"密文", 
				"日期",
				"日期时间", 
				"文件上传",
				"参照",
				"下拉框",
				"多语下拉框",
				"文本域",
				"富文本编辑", 
				"自定义", 
				"文件", 
				"列表"
		};
	}
	
	public ControlInfo(){
	
		StringPropertyInfo sinfo = new StringPropertyInfo();
		sinfo.setId("id");
		sinfo.setVisible(true);
		sinfo.setEditable(false);
		sinfo.setDsField("string_ext1");
		sinfo.setVoField("id");
		sinfo.setLabel("ID");
		list.add(sinfo);
		
		StringPropertyInfo widgetId = new StringPropertyInfo();
		widgetId.setId("");
		widgetId.setEditable(true);
		widgetId.setVisible(false);
		widgetId.setDsField("string_ext2");
		widgetId.setVoField("widgetid");
		widgetId.setLabel("View ID");
		list.add(widgetId);
		
		StringPropertyInfo sinfo2 = new StringPropertyInfo();
		sinfo2.setId("name");
		sinfo2.setEditable(false);
		sinfo2.setVisible(true);
		sinfo2.setDsField("string_ext3");
		sinfo2.setVoField("name");
		sinfo2.setLabel("名称");
		
		ComboPropertyInfo align = new ComboPropertyInfo();
		align.setId("align");
		align.setKeys(new String[]{"左","右"});
		align.setValues(new String[]{"left", "right"});
		align.setVisible(true);
		align.setEditable(true);
		align.setDsField("combo_ext15");
		align.setType(StringDataTypeConst.STRING);
		align.setVoField("align");
		align.setLabel("位置");
		list.add(align);
		
	//	list.add(sinfo2);
	}
	
 

}
