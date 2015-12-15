package xap.lui.psn.cmd;

import java.util.Map;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.model.WindowContext;


public class LuiParentPlugoutCmd extends LuiCommand {
	private String plugoutId;
	private String widgetId;
	private Map<String, Object> paramMap;
	
	
	public LuiParentPlugoutCmd(String widgetId, String plugoutId){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	
	
	public LuiParentPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	@Override
	public void execute() {
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		getLifeCycleContext().getAppContext().addPlug(windowctx.getId() + "_" + widgetId + "_" + plugoutId, this.paramMap);
	
		StringBuffer scriptBuf = new StringBuffer();
		scriptBuf.append("parent.triggerPlugout('").append(this.widgetId).append("','").append(this.plugoutId).append("');\n");
		getLifeCycleContext().getAppContext().addExecScript(scriptBuf.toString());
	}
}
