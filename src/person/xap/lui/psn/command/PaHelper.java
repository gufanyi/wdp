package xap.lui.psn.command;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.control.ModePhase;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
/**
 * Personalized Tools
 * 
 * @author licza
 * @since UAP 6.1
 */
public class PaHelper {
	private static final String $PA = "$pa";
	/**
	 * Create an Personalize URL with dimensions. Open the URL will not
	 * immediately create a template, just on click the Save button.
	 * 
	 * @param dimensions
	 * @param mode
	 */
	public static String createPaURL(LinkedHashMap<String, String> dimensions, ModePhase mode) {
		return createPaURL(dimensions, mode, null);
	}
	public static String createPaURL(LinkedHashMap<String, String> dimensions, ModePhase mode, String dftTemplatePK) {
		if (dimensions == null || dimensions.isEmpty()) {
			throw new IllegalArgumentException("Illegal Argument:  'dimensions', (argument should not be null or empty)");
		}
		if (!dimensions.containsKey("windowid")) {
			throw new IllegalArgumentException("Illegal Argument: 'dimensions[windowid]', (argument should not be null or empty)");
		}
		String winId = dimensions.get("windowid");
		String urlPrefix = LuiRuntimeContext.getRootPath() + "/app/mockapp/pa";
		urlPrefix = urlPrefix + "?model=" + PaEditorPageModel.class.getName();
		urlPrefix = urlPrefix + "&from=1&" + LuiRuntimeContext.DESIGNWINID + "=" + winId + "&" + LuiRuntimeContext.MODEPHASE + "=" + mode.toString();
		if (dftTemplatePK == null || dftTemplatePK.length() == 0) {
			urlPrefix += "&dftTemplatePK=" + dftTemplatePK;
		}
		StringBuffer urlBuf = bindPaParameter(dimensions, urlPrefix, mode);
		return urlBuf.toString();
	}
	/**
	 * Bind Personalize Parameter with dimensions.
	 * 
	 * @param dimensions
	 * @param urlPrefix
	 * @return
	 */
	public static StringBuffer bindPaParameter(LinkedHashMap<String, String> dimensions, String urlPrefix, ModePhase mode) {
		StringBuffer urlBuf = new StringBuffer();
		urlBuf.append(urlPrefix);
		Iterator<String> iterator = dimensions.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			urlBuf.append("&").append($PA).append("_").append(i).append("_").append(key).append("=").append(dimensions.get(key));
			i++;
		}
		urlBuf.append("&" + LuiRuntimeContext.MODEPHASE + "=").append(mode.toString());
		return urlBuf;
	}
	/**
	 * Restore the dimensions from an Disorder Map.
	 * 
	 * @param paramMap
	 * @return
	 */
	public static LinkedHashMap<String, String> restoreDimensions(Map<String, String> paramMap) {
		LinkedHashMap<String, String> dimensions = new LinkedHashMap<String, String>();
		Iterator<String> iterator = paramMap.keySet().iterator();
		String[][] dim = new String[20][2];
		while (iterator.hasNext()) {
			String key = iterator.next();
			if (key.startsWith($PA)) {
				String[] kes = key.split("_");
				if (kes.length > 2) {
					String value = paramMap.get(key);
					if (value != null && value.length() > 0) {
						String prefix = kes[0] + "_" + kes[1] + "_";
						int idx = Integer.parseInt(kes[1]);
						String trueKey = key.replace(prefix, "");
						dim[idx] = new String[] { trueKey, value };
					}
				}
			}
		}
		for (int i = 0; i < dim.length; i++) {
			String[] di = dim[i];
			if (di != null && di[0] != null) {
				dimensions.put(di[0], di[1]);
			}
		}
		return dimensions;
	}
	/**
	 * Create a Neat parameter Map.
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> tranlateRequestParamMap(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		Map<String, String> paramMap = new HashMap<String, String>();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			paramMap.put(key, request.getParameter(key));
		}
		return paramMap;
	}
	/**
	 * get edit window id
	 * 
	 * @return
	 */
	public static String getCurrentEditWindowId() {
		String windowid = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter(LuiRuntimeContext.DESIGNWINID );
		if (windowid == null)
			windowid = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("windowId");
		return windowid;
	}
	public static PagePartMeta getPageMeta() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		return ipaService.getOriPageMeta(pageId, sessionId);
	}
	public static UIPartMeta getUIMeta() {
		IPaEditorService ipaService = new PaEditorServiceImpl();
		String pageId = (String) PaCache.getInstance().get("_pageId");
		String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
		return ipaService.getOriUIMeta(pageId, sessionId);
	}

}
