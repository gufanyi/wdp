package xap.lui.psn.newapp;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.psn.appwinlist.AppWinListCtrl;
import xap.lui.psn.designer.DesignerMainViewController;
import xap.lui.psn.designer.PaProjViewTreeController;
import xap.lui.psn.java.CreateJavaUtil;

public class NewAppCtrl {
	public void pluginplugin_newapp(Map<Object, Object> obj) {
		String dftwinid = (String) obj.get("dftwinid");//(String) LuiAppUtil.getAppAttr("dftwinid");
		ReferenceComp ref = ((ReferenceComp)getComp("ref_dftwin"));
		ref.setValue(dftwinid);
		ref.setShowValue(dftwinid);
	}
	
	public void onOkClick(MouseEvent<ButtonComp> e){
		String appId = ((StringTextComp)getComp("str_id")).getValue();
		String javaFullClassName = ((StringTextComp)getComp("str_name")).getValue();
		String dftwin = ((ReferenceComp)getComp("ref_dftwin")).getValue();
		if(StringUtils.isBlank(appId))
			throw new LuiRuntimeException("请输入app id!");
		if(StringUtils.isBlank(javaFullClassName))
			throw new LuiRuntimeException("请输入控制类!");
		if(StringUtils.isBlank(dftwin))
			throw new LuiRuntimeException("请选择默认窗口!");
		if(StringUtils.isBlank(dftwin))
			dftwin = "daydft";
		PaCache cache = PaCache.getInstance();
		cache.pub("_appId", appId);
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/apps/";
		String nodePath = resourceFolder + "/" + appId;
		File nodefile = new File(nodePath);
		if (!nodefile.exists()) {
			nodefile.mkdirs();
		}
		{
			Application application = new Application();
			application.setId(appId);
			application.setControllerClazz(javaFullClassName);
			application.setDefaultWindowId(dftwin);
			PagePartMeta pagemeta = new AppWinListCtrl().createPageMeta(dftwin);
			application.addWindow(pagemeta);
			{
				String xml0 = application.toXml();
				File file = this.createAppFile(appId);
				try {
					FileUtils.write(file, xml0, "utf-8");
				} catch (IOException e1) {
					LuiLogger.error(e1.getMessage(), e1);
				}
			}
			
			PaCache.getInstance().pub("_editApp", application);
		}
		{
			try {
				String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
				String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
				String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
				File file = PaProjViewTreeController.createJavaFile(javaFullClassName, null);
				FileUtils.write(file, javaContent, "utf-8");
			} catch (IOException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
//		Dataset projectDs = LuiAppUtil.getDataset("projViewTreeDs", "project");
		Dataset projectDs = AppSession.current().getAppContext().getWindowContext("pa").getViewContext("project").getView().getViewModels().getDataset("projViewTreeDs");
		String projectName = (String) cache.get("_projName");
		desinerMainViewController._add_app_as_row(nodefile, projectName + "_ui_apps", projectDs);
		closepage();
	}
	public File createAppFile(String pageId) {
		String compMetaPath =PaCache.getEditorAppXmlPath();
		File file = new File(compMetaPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	private WebComp getComp(String id) {
		return LuiAppUtil.getCntView().getViewComponents().getComponent(id);
	}
	
	public void onCancelClick(MouseEvent<ButtonComp> e){
		closepage();
	}
	
	private void closepage(){
		AppSession.current().getAppContext().closeWinDialog();
	}
}
