package xap.lui.psn.cmd;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;


public class LuiOpenWindowCmd extends LuiCommand{
	private String winId;
	private String height = "600";
	private String width = "400";
	private String title = "TITLE";
	
	public LuiOpenWindowCmd(String winId){
		this.winId = winId;
	}
	
	public LuiOpenWindowCmd(String winId, String width, String height){
		this(winId, width, height, null);
	}
	
	public LuiOpenWindowCmd(String winId, String width, String height, String title){
		this(winId);
		if(StringUtils.isNotBlank(height))
			this.height = height;
		if(StringUtils.isNotBlank(width))
			this.width = width;
		if(StringUtils.isNotBlank(title))
			this.title = title;
	}
	
	@Override
	public void execute() {
		getLifeCycleContext().getAppContext().navgateTo(winId, title, width, height, getWindowParam());
	}

	protected Map<String, String> getWindowParam() {
		return null;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

}
