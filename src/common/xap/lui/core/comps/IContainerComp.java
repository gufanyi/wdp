package xap.lui.core.comps;

public interface IContainerComp<T extends LuiElement> {
	public T getElementById(String id);
}
