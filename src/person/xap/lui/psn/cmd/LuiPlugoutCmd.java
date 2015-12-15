package xap.lui.psn.cmd;

import java.util.Map;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.model.WindowContext;


public class LuiPlugoutCmd extends LuiCommand {
	private String plugoutId;
	private String widgetId;
	private Map<String, Object> paramMap;
	private boolean isBeforeExec = false;
	
	
	public LuiPlugoutCmd(String widgetId, String plugoutId){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	public LuiPlugoutCmd(String widgetId, String plugoutId, boolean isBeforeExec){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.isBeforeExec = isBeforeExec;
	}
	
	
	
	public LuiPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	public LuiPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap, boolean isBeforeExec){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
		this.isBeforeExec = isBeforeExec;
	}
	
	
	@Override
	public void execute() {
		
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		getLifeCycleContext().getAppContext().addPlug(windowctx.getId() + "_" + widgetId + "_" + plugoutId, this.paramMap);
	
		
		StringBuffer scriptBuf = new StringBuffer();
		scriptBuf.append("triggerPlugout('").append(this.widgetId).append("','").append(this.plugoutId).append("');\n");
		if (this.isBeforeExec)
			getLifeCycleContext().getAppContext().addBeforeExecScript(scriptBuf.toString());
		else
			getLifeCycleContext().getAppContext().addExecScript(scriptBuf.toString());
	}

	
}
