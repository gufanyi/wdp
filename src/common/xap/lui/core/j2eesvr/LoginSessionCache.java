package xap.lui.core.j2eesvr;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
public class LoginSessionCache {
	public static LoginSessionCache instace = null;
	public Map<String, Map<String, HttpSession>> cache = new ConcurrentHashMap<String, Map<String, HttpSession>>();
	public static LoginSessionCache getInstacne() {
		if (instace == null) {
			synchronized (LoginSessionCache.class) {
				if (instace == null) {
					synchronized (LoginSessionCache.class) {
						instace = new LoginSessionCache();
					}
				}
			}
		}
		return instace;
	}
	public void addSession(String userId, String ctx, HttpSession session) {
		Map<String, HttpSession> map = cache.get(userId);
		if (map == null) {
			synchronized (LoginSessionCache.class) {
				map = new ConcurrentHashMap<String, HttpSession>();
				map.put(ctx, session);
				cache.put(userId, map);
			}
		} else {
			map.put(ctx, session);
		}
	}
	public HttpSession getSession(String userId, String ctx) {
		if(cache.get(userId)!=null){
			return cache.get(userId).get(ctx);
		}
		return null;
	}
	
	
	public void remove(String userId) {
		 cache.remove(userId);
	}
}
