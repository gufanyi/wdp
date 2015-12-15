/**
 * 
 */
package xap.lui.core.serializer;

import xap.lui.core.design.UIMetaToXml;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.Application;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

/**
 * 
 * 转换WebElement到XML
 * @author chouhl
 *
 */
public class AMCWebElementToXML {

	
//	public static void modelToXml(String filePath, String fileName, String projectPath, Model model){
//		ModelToXml.toXml(filePath, fileName, projectPath, model);
//	}
	
	public static void viewToXml(String filePath, String fileName, String projectPath, String refId){
		//WidgetToXml.toXml(filePath, fileName, projectPath, refId);
	}
	
//	public static void viewToXml(String filePath, String fileName, String projectPath, ViewConf viewConf){
//		WidgetToXml.toXml(filePath, fileName, projectPath, viewConf);
//	}
	
	public static void widgetToXml(String filePath, String fileName, String projectPath, ViewPartMeta widget){
		
		//WidgetToXml.toXml(filePath, fileName, projectPath, widget);
	}
	
	public static void windowToXml(String filePath, String fileName, String projectPath, PagePartMeta pageMeta){
		PageMetaToXml.toXml(filePath, fileName, projectPath, pageMeta);
	}
	
	public static void createUIMeta(String folderPath, UIPartMeta meta){
		String fp = folderPath.replaceAll("\\\\", "/");
		String id = fp.substring(fp.lastIndexOf("/") + 1) + "_um";
		if(meta == null){
			meta = new UIPartMeta();
//			meta.setAttribute(UIMeta.ISCHART, 0);
//			meta.setAttribute(UIMeta.ISJQUERY, 0);
//			meta.setAttribute(UIMeta.ISEXCEL, 0);
//			meta.setAttribute(UIMeta.JSEDITOR, 0);
			meta.setAttribute(UIPartMeta.ID, id);
		} 
		if(meta.getId() == null || meta.getId().trim().length() == 0){
			meta.setId(id);
		}
		//UIMetaToXml.toXml(meta, folderPath);
	}
	
}
