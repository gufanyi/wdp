package xap.lui.core.context;

public class ChartContext extends BaseContext {
	
	private static final long serialVersionUID = 4506405692681980421L;
	
	//是否可见
	private boolean visible = true;
	
	//是否可用
	private boolean enabled = true;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
