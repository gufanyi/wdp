package xap.lui.core.j2eesvr;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;

import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.Theme;
import xap.lui.core.common.ThemeManager;
import xap.lui.core.model.PoolObjectManager;
import xap.lui.core.util.ApplicationNodeUtil;
import xap.lui.core.util.JaxbMarshalFactory;
import xap.lui.core.util.PageNodeUtil;
public class DftAllServerListener extends BaseServerListener {
	public DftAllServerListener(ServletContext ctx) {
		super(ctx);
	}
	protected void doAfterStarted() {
		try {
			String appPath = ContextResourceUtil.getCurrentAppPath();
			String tempDir = appPath + "/lui/temp";
			File f = new File(tempDir);
			if (f.exists() && f.isDirectory()) {
				removeTempDir(f);
			}
			String userHome=System.getProperty("user.home");
		    copyWebResource(userHome);
		    copyPlatformResource(userHome);
		    loadThemes(appPath);
			loadPoolObjects(appPath);
		} catch (Exception e) {}
	}
	
	
	protected void copyWebResource(String appPath) {
		ClassLoader loader=DftAllServerListener.class.getClassLoader();
		Set<String> resources=ClassScan.resourceScaner("lui/nodes", loader);
		List<String> list = new ArrayList<String> ();
		list.addAll(resources);
		ClassScan.copyResourceToDir(appPath, list, loader);
	}
	
	
	protected void copyPlatformResource(String appPath) {
		ClassLoader loader=DftAllServerListener.class.getClassLoader();
		Set<String> resources=ClassScan.resourceScaner("lui/platform", loader);
		List<String> list = new ArrayList<String> ();
		list.addAll(resources);
		ClassScan.copyResourceToDir(appPath, list, loader);
	}
	protected void loadPoolObjects(String appPath) {
		String nodeDir = appPath + "/lui/nodes";
		PageNodeUtil.refresh(nodeDir);
		ApplicationNodeUtil.refresh(appPath + "/lui/apps");
		// 取出公共池里的widget
		String ds = LuiRuntimeContext.getDatasource();
		if (ds == null || ds.length() == 0) {
			ds = "design";
			LuiRuntimeContext.setDatasource(ds);
		}
		PoolObjectManager.getWidgetsFromPool(LuiRuntimeContext.getRootPath(), ds);
	}
	private void removeTempDir(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				files[i].delete();
			else
				removeTempDir(files[i]);
		}
		dir.delete();
	}
	private void loadThemes(String appPath) {
		File dir = ContextResourceUtil.getCntWebAppFile("lui/platform/theme");
		if (dir.exists()) {
			File[] fs = dir.listFiles();
			if (fs != null && fs.length > 0) {
				for (int i = 0; i < fs.length; i++) {
					tryToLoadTheme(fs[i]);
				}
			}
		}
	}
	private void tryToLoadTheme(File dir) {
		File[] fs = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.equals("theme.xml");
			}
		});
		if (fs != null && fs.length == 1) {
			try {
				Theme theme = JaxbMarshalFactory.newIns().decodeXML(Theme.class, FileUtils.readFileToString(fs[0], "utf-8"));
				theme.setAbsPath(dir.getAbsolutePath());
				String ctx = LuiRuntimeContext.getRootPath();
				theme.setCtxPath(ctx);
				if (ctx.equals(LuiRuntimeContext.getLuiCtx())) {
					ThemeManager.registerTheme(theme);
				} else {
					ThemeManager.registerModelTheme(LuiRuntimeContext.getRootPath(), theme);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
}
