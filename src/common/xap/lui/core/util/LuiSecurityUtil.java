package xap.lui.core.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpSession;

/**
 * yer安全工具类
 * 
 * @author gd 2008-11-28
 * @version NC5.5
 * @since NC5.5
 * 
 */

public class LuiSecurityUtil {
	private static final ConcurrentMap<String, HttpSession> sessionMap = new ConcurrentHashMap<String, HttpSession>();

	public static void addSessionToQueue(HttpSession session) {
		if (session != null) {
			session.setAttribute("SESSION_EXPIRE_TIME", System.currentTimeMillis());
			sessionMap.put(session.getId(), session);
		}
	}

	public static void deleteSessionFromQueue(String id) {
		if (sessionMap.containsKey(id)) {
			sessionMap.remove(id);
		}
	}

	public static void clearExpiredSession() {
		Iterator<Entry<String, HttpSession>> it = sessionMap.entrySet().iterator();
		long nowTime = System.currentTimeMillis();
		while (it.hasNext()) {
			Entry<String, HttpSession> entry = it.next();
			HttpSession session = entry.getValue();
			try {
				Long savedTime = (Long) session.getAttribute("SESSION_EXPIRE_TIME");
				if (nowTime - savedTime.longValue() > 5000) {
					session.invalidate();
					it.remove();
				}
			} catch (IllegalStateException e) {
				it.remove();
			}
		}
	}
}
