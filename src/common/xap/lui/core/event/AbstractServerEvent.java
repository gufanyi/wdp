package xap.lui.core.event;
import xap.lui.core.listener.LuiListenerConf;
public abstract class AbstractServerEvent<T> extends LuiListenerConf implements IServerEvent<T> {
	private T element;
	private boolean stop = false;
	public AbstractServerEvent(T element) {
		this.element = element;
	}
	public AbstractServerEvent() {}
	public T getSource() {
		return element;
	}
	public boolean isStop() {
		return stop;
	}
	public void stop() {
		this.stop = true;
	}
}
