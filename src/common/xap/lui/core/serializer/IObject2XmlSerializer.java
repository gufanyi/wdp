package xap.lui.core.serializer;

public interface IObject2XmlSerializer<T> {
	public String[] serialize(T obj);
}
