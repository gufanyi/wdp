package xap.lui.core.event;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.AbstractServerEvent;
public class ScriptEvent extends AbstractServerEvent<LuiElement> {
	public ScriptEvent(LuiElement webElement) {
		super(webElement);
	}
	public ScriptEvent() {}
	@Override
	public String getJsClazz() {
		return null;
	}
}
