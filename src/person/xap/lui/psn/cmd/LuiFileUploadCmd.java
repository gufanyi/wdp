package xap.lui.psn.cmd;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.model.AppSession;


public class LuiFileUploadCmd extends LuiCommand {
	private Map<String, String> paramMap;
	private String title;
	public LuiFileUploadCmd(String title, Map<String, String> paramMap){
		this.paramMap = paramMap;
		this.title = title;
	}
	@Override
	public void execute() {
		String url =  LuiRuntimeContext.getRootPath() + "/core/file.jsp?pageId=file&";
		if(paramMap != null){
			Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				url += entry.getKey() + "=" + entry.getValue();
				if(it.hasNext())
					url += "&";
			}
		}
		
	    //AppSession.current().getAppContext().showModalDialog(url, title, "450", "425", "pictureupload", ApplicationContext.FACTORY_BEAN_PREFIX);
	}

}
