package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;


public class LuiOpenViewCmd extends LuiCommand {

	private String viewId;
	private String height = "600";
	private String width = "400";
	private String title = "TITLE";
	private boolean isclose = true;
	public LuiOpenViewCmd(String viewId){
		this.viewId = viewId;
	}
	
	public LuiOpenViewCmd(String viewId, String width, String height){
		this(viewId, width, height, null);
	}
	
	public LuiOpenViewCmd(String viewId, String width, String height, String title) {
		this(viewId, width, height, title, true);
		
	}
	public LuiOpenViewCmd(String viewId, String width, String height, String title, Boolean isclose){
		this(viewId);
		if(StringUtils.isNotBlank(height))
			this.height = height;
		if(StringUtils.isNotBlank(width))
			this.width = width;
		if(StringUtils.isNotBlank(title))
			this.title = title;
		if(isclose != null)
			this.isclose = isclose;
		else 
			this.isclose = true;
	}

	@Override
	public void execute() {
		getLifeCycleContext().getWindowContext().popView(viewId, width, height, title, isclose);
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
