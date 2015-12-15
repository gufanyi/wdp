package xap.lui.psn.cmd;

public class LuiAddViewCmd  extends LuiOpenViewCmd {

	public LuiAddViewCmd(String viewId){
		super(viewId);
	}
	
	public LuiAddViewCmd(String viewId, String width, String height){
		super(viewId, width, height);
	}
	
	public LuiAddViewCmd(String viewId, String width, String height, String title) {
		super(viewId, width, height, title);
	}
	public LuiAddViewCmd(String viewId, String width, String height, String title, boolean isclose){
		super(viewId, width, height, title, isclose);
	}
}
