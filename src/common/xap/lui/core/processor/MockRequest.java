package xap.lui.core.processor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
/**
 * HttpRequest 包装
 * @author dengjt
 *
 */
public class MockRequest extends HttpServletRequestWrapper {
	private Map<String, String> paramMap = new HashMap<String, String>();
	public MockRequest(HttpServletRequest request){
		super(request);
	}


	@Override
	public String getParameter(String key) {
		String value = paramMap.get(key);
		return value;
	}

	public void addParameter(String key, String value) {
		paramMap.put(key, value);
	}

}
