package xap.lui.psn.setting;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.cache.PaCache;
import xap.lui.core.command.CmdInvoker;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.Application;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.psn.cmd.LuiAddRowCmd;
import xap.lui.psn.cmd.LuiRemoveRowCmd;
import xap.lui.psn.pamgr.PaPropertyDatasetListener;
public class PaParamDsListener {
	private static final String DS_PARAM = "ds_param";
	
	public void onDataLoad_ds(DatasetEvent e){
		new PaPropertyDatasetListener().paramSetting();
	}
	//添加
	public void onclickAddParam(MouseEvent e){
		CmdInvoker.invoke(new LuiAddRowCmd(DS_PARAM));
	}
	public void onclickEditParam(MouseEvent e){
		Dataset ds = getDs();
		ds.setEdit(true);
	}
	//删除
	public void onclickDelParam(MouseEvent e){
		CmdInvoker.invoke(new LuiRemoveRowCmd(DS_PARAM));
	}
	//保存
	public void onClickSave(MouseEvent e) {
		Dataset ds = getDs();
		Row[] rows = ds.getCurrentPageData().getRows();
		if (rows != null && rows.length > 0) {
			String appId = (String) PaCache.getInstance().get("_appId");
			String filePath = "";
			Properties props = new Properties();
			if (StringUtils.isBlank(appId)) {// 窗口集
				PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
				if (pagemeta == null)
					return;
				filePath = getFilePath(pagemeta.getId(), "/lui/nodes/", "/node.properties");
				props = genProp(ds, rows, filePath);
				// 保存到内存模型
				if (!props.isEmpty())
					pagemeta.getWindow().setProps(props);
			} else {// 应用集
				filePath = getFilePath(appId, "/lui/apps/", "/app.properties"); 
				props = genProp(ds, rows, filePath);
				// 保存到内存模型
				Application app = (Application) PaCache.getInstance().get("_editApp");
				if (!props.isEmpty())
					app.setProps(props);
			}
			ds.setEdit(false);
		}
	}
	private Properties genProp(Dataset ds, Row[] rows, String filePath) {
		Properties props = new Properties();
		File file = new File(filePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				LuiLogger.error(e1.getMessage(), e1);
			}
		}
		String str = "";
		int keyIndex = ds.nameToIndex("Name");
		int valueIndex = ds.nameToIndex("Value");
		for (Row row : rows) {
			String key = row.getString(keyIndex);
			String value = row.getString(valueIndex);
			str = str + key + "=" + value + "\n";
			props.setProperty(key, value);
		}
		try {
			FileUtils.write(file, str, "utf-8");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return props;
	}
	private String getFilePath(String id, String luipath, String propname) {
		String resourceFolder = (String) PaCache.getInstance().get("_resourceFolder");
		resourceFolder = resourceFolder + luipath;
		String filePath = resourceFolder + "/" + id + propname;
		return filePath;
	}
	private Dataset getDs() {
		Dataset ds = LuiAppUtil.getCntView().getViewModels().getDataset(DS_PARAM);
		return ds;
	}
}
