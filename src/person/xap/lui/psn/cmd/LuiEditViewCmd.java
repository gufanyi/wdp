package xap.lui.psn.cmd;



public class LuiEditViewCmd extends LuiOpenViewCmd {

	public LuiEditViewCmd(String viewId){
		super(viewId);
	}
	
	public LuiEditViewCmd(String viewId, String width, String height){
		super(viewId, width, height);
	}
	
	public LuiEditViewCmd(String viewId, String width, String height, String title) {
		super(viewId, width, height, title);
	}
	public LuiEditViewCmd(String viewId, String width, String height, String title, boolean isclose){
		super(viewId, width, height, title, isclose);
	}
}
