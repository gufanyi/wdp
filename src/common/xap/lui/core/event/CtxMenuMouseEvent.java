package xap.lui.core.event;
import xap.lui.core.comps.MenuItem;
public class CtxMenuMouseEvent extends MouseEvent<MenuItem> {
	private String triggerId;
	public CtxMenuMouseEvent(MenuItem webElement) {
		super(webElement);
	}
	public CtxMenuMouseEvent() {}
	public String getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(String triggerId) {
		this.triggerId = triggerId;
	}
}
