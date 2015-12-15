package xap.lui.core.util;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.Theme;

/**
 * LUI EL表达式自定义类
 * @author dengjt
 *
 */
public class LuiElTool {
	/**
	 * 获取infoName对应的值
	 * @param infoName
	 * @return
	 */
	public static String getInfo(String infoName) {
		if(infoName == null)
			return null;
		else if(infoName.equals("background"))
			return getThemeElement(Theme.LUI_BACKGROUND_COLOR);
		else if(infoName.equals("compBackground"))
			return getThemeElement(Theme.LUI_COMP_BACKGROUND_COLOR);
		else if(infoName.equals("borderColor"))
			return getThemeElement(Theme.LUI_BORDER_COLOR);
		else if(infoName.equals("spliterZoneBorderColor"))
			return getThemeElement(Theme.LUI_SPLITER_BORDER_COLOR);
//		else if(infoName.equals("accountName"))
//			return LuiRuntimeEnvironment.getAccountName();
		else if(infoName.equals("themeKey"))
			return LuiRuntimeContext.getTheme().getThemeElement(infoName);
		
//		else if(infoName.equals("modelClazz")) {
//			String modelClazz = LuiRuntimeEnvironment.getWebContext().getParameter("model");
//			if(modelClazz == null)
//				modelClazz = BillTemplatePageModel.class.getName();
//			return modelClazz;
//		}
		return null;
	}
	
	public static String getThemeElement(String key){
		return LuiRuntimeContext.getTheme().getThemeElement(key);
	}
	
	public static String getBackground(){
		return getThemeElement(Theme.LUI_BACKGROUND_COLOR);
	}
	
	public static String getCompBackground(){
		return getThemeElement(Theme.LUI_COMP_BACKGROUND_COLOR);
	}
	
	public static String getBorderColor() {
		return getThemeElement(Theme.LUI_BORDER_COLOR);
	}
	
	public static String getSpliterZoneBorderColor() {
		return getThemeElement(Theme.LUI_SPLITER_BORDER_COLOR);
	}
}
