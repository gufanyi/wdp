package xap.lui.core.render;
public interface IUIElementObserver {
	public void notifyChange(String type, Object ele, Object userObject);
}
