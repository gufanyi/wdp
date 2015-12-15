package xap.lui.core.event;

/**
 * @author guoweic
 *
 */
public class SimpleEvent<T> extends AbstractServerEvent<T>{

	public SimpleEvent(T webElement) {
		super(webElement);
	}

	@Override
	public String getJsClazz() {
		return null;
	}

}
