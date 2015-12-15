package xap.lui.core.constant;

import java.util.HashMap;
import java.util.Map;


/**
 * 编辑器类型常量
 * @author dengjt
 *
 */
public final class EditorTypeConst 
{
	private EditorTypeConst(){}
	
	public static final String CHECKBOX = "CheckBox";
	public static final String STRINGTEXT = "StringText";
	public static final String INTEGERTEXT = "IntegerText";
	public static final String DECIMALTEXT = "DecimalText";
	public static final String PWDTEXT = "PwdText";
	public static final String DATETEXT = "DateText";
	public static final String MULTIDATETEXT = "MultiDateText";
	public static final String DATETIMETEXT = "DateTimeText";
	public static final String MULTIDATETIMETEXT = "MultiDateTimeText";
	public static final String FILEUPLOAD = "FileUpload";
	public static final String RADIOGROUP = "RadioGroup";
	public static final String CHECKBOXGROUP = "CheckboxGroup";
	public static final String REFERENCE = "Reference";
	public static final String LANGUAGECOMBODATA = "LanguageComboBox";
	public static final String SHORTDATETEXT = "ShortDateText";
	public static final String COMBODATA = "ComboBox";
	public static final String MULTICOMBOBOX = "MultiComboBox";
	public static final String LIST = "List";
	public static final String RADIOCOMP = "Radio";
	public static final String TEXTAREA = "TextArea";
	public static final String IMAGECOMP = "Image";
	public static final String RICHEDITOR = "RichEditor";
	public static final String FILECOMP = "File";
	// 自定义类型，此类型只用于FormElement，指明此元素来自于页面定义的其它控件
	public static final String SELFDEF = "SelfDef";
	
	public static final String LITERALDATE = "FLiteralDate";
	
	
	private static final Map<String, String> pac2etMap = new HashMap<String, String>();
	private static final Map<String, String> dt2etMap = new HashMap<String, String>();
	
	static{
		dt2etMap.put(StringDataTypeConst.BIGDECIMAL, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.bOOLEAN, EditorTypeConst.CHECKBOX);
		dt2etMap.put(StringDataTypeConst.BOOLEAN, EditorTypeConst.CHECKBOX);
		dt2etMap.put(StringDataTypeConst.bYTE, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.BYTE, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.CHAR, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.CHARACTER, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.DATE, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.Decimal, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.dOUBLE, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.DOUBLE, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.fLOATE, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.FLOATE, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.INT, EditorTypeConst.INTEGERTEXT);
		dt2etMap.put(StringDataTypeConst.INTEGER, EditorTypeConst.INTEGERTEXT);
		dt2etMap.put(StringDataTypeConst.lONG, EditorTypeConst.INTEGERTEXT);
		dt2etMap.put(StringDataTypeConst.LONG, EditorTypeConst.INTEGERTEXT);
		dt2etMap.put(StringDataTypeConst.STRING, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.FBOOLEAN, EditorTypeConst.CHECKBOX);
		dt2etMap.put(StringDataTypeConst.FDATE, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.FDATETIME, EditorTypeConst.DATETIMETEXT);
		dt2etMap.put(StringDataTypeConst.FDOUBLE, EditorTypeConst.DECIMALTEXT);
		dt2etMap.put(StringDataTypeConst.FNUMBERFORMAT, EditorTypeConst.INTEGERTEXT);
		dt2etMap.put(StringDataTypeConst.FTIME, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.MEMO, EditorTypeConst.TEXTAREA);
		dt2etMap.put(StringDataTypeConst.CUSTOM, EditorTypeConst.STRINGTEXT);
		dt2etMap.put(StringDataTypeConst.FLiteralDate, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.FDate_begin, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.FDate_end, EditorTypeConst.DATETEXT);
		dt2etMap.put(StringDataTypeConst.IMAGE, EditorTypeConst.IMAGECOMP);
		
		dt2etMap.put(StringDataTypeConst.ENTITY, EditorTypeConst.TEXTAREA);

		pac2etMap.put(PaConstant.CTRL_T_INT, INTEGERTEXT);
		pac2etMap.put(PaConstant.CTRL_T_FLOAT, DECIMALTEXT);
		pac2etMap.put(PaConstant.CTRL_T_DATE, DATETEXT);
		pac2etMap.put(PaConstant.CTRL_T_STRING, STRINGTEXT);
		pac2etMap.put(PaConstant.CTRL_T_COMB, COMBODATA);
		pac2etMap.put(PaConstant.CTRL_T_REF, REFERENCE);
		pac2etMap.put(PaConstant.CTRL_T_DATETIME, DATETIMETEXT);
		pac2etMap.put(PaConstant.CTRL_T_PWD, PWDTEXT);
	}
	
	public static String getEditorTypeByString(String strType) {
		return dt2etMap.get(strType);
	}
	
	public static String getEditorTypeByPaConstant(String strType) {
		return pac2etMap.get(strType);
	}
}
