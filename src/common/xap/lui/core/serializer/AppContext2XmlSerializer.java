package xap.lui.core.serializer;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.json.AppCtx2JsonSerializer;
import xap.lui.core.model.AppSession;
public class AppContext2XmlSerializer implements IObject2XmlSerializer<AppSession> {
	public String[] serialize(AppSession appSession) {
		String jsonString = null;
		try {
			jsonString = new AppCtx2JsonSerializer().serialize(appSession);
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
		return new String[] {
			jsonString
		};
	}
}
