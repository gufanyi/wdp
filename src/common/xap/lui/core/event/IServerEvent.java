package xap.lui.core.event;


public interface IServerEvent<T>{
	public T getSource();
	public boolean isStop();
}
