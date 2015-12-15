package xap.lui.psn.util;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.model.LuiPageContext;

/**
 * InfoPath控件类型到lui控件类型的转换类
 * 
 * @author liujmc
 * @date 2012-08-20
 */
public class TypeMappingUtil {

	public static String convertType(String type){
		String luiCompType = "";
		if(type.equals("PlainText")){
			luiCompType = EditorTypeConst.STRINGTEXT;
		}else if(type.equals("DTPicker")){
			luiCompType = EditorTypeConst.DATETEXT;
		}else if(type.equals("RichText")){
			luiCompType = EditorTypeConst.TEXTAREA;
		}else if(type.equals("combobox")||type.equals("dropdown")){
			luiCompType = EditorTypeConst.COMBODATA;
		}else if(type.equals("entitypicker")){
			luiCompType = EditorTypeConst.REFERENCE;
		}else if(type.equals("Button")){
			luiCompType = LuiPageContext.SOURCE_TYPE_BUTTON;
		}else if(type.equals("img") || type.equals("InlineImage")){
			luiCompType = EditorTypeConst.IMAGECOMP;
		}else if(type.equals("label")){
			luiCompType = LuiPageContext.SOURCE_TYPE_LABEL;
		}else if(type.equals("CheckBox")){
			luiCompType = EditorTypeConst.CHECKBOX;
		}else if(type.equals("OptionButton")){
			luiCompType = EditorTypeConst.RADIOCOMP;
		}else if(type.equals("CheckboxGroup")){
			luiCompType = EditorTypeConst.CHECKBOXGROUP;
		}else if(type.equals("RadioGroup")){
			luiCompType = EditorTypeConst.RADIOGROUP;
		}else if(type.equals("a")){
			luiCompType = LuiPageContext.SOURCE_TYPE_LINKCOMP;
		}
		return luiCompType;
	}
}
