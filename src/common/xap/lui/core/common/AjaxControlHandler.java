package xap.lui.core.common;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.exception.InteractionInfo;
import xap.lui.core.exception.LuiInteractionException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.processor.IRequestProcessor;
import com.alibaba.fastjson.JSON;
/**
 * 处理Ajax请求的核心处理逻辑
 */
public class AjaxControlHandler implements ControlHandler {
	private static final String ENCODING = "UTF-8";
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception {
		// 构造返回串
		String[] results = null;
		// 传回客户端的友好信息。
		String expText = "";
		// 传回客户端的真是错误信息
		String expMsg = "";
		// 传回客户端的异常栈
		String expStack = "";
		// 操作是否成功的表示。便于客户端统一异常处理
		String success = "true";
		Map<String, Object> reusltMap = new HashMap<String, Object>();
		try {
			req.setCharacterEncoding(ENCODING);
			res.setCharacterEncoding(ENCODING);
			RequestLifeCycleContext appCtx = new RequestLifeCycleContext();
			RequestLifeCycleContext.set(appCtx);
			appCtx.setPhase(LifeCyclePhase.ajax);
			PagePartMeta pageMeta = LuiRuntimeContext.getWebContext().getPageMeta();
			if (pageMeta == null) {
				return;
			}
			String processClazz = pageMeta.getProcessorClazz();
			if (processClazz == null || processClazz.trim().equals("")) {
				processClazz = AppRequestProcessor.class.getName();
			}
			LuiRenderContext.current().setPhase(LifeCyclePhase.ajax);
			Class<?> c = Class.forName(processClazz);
			IRequestProcessor processor = (IRequestProcessor) c.newInstance();
			results = processor.doProcess();
		} catch (LuiInteractionException e) {
			InteractionInfo info = e.getInfo();
			String infoJson = JSON.toJSONString(info);
			results = new String[] { infoJson };
			success = "interaction";
		} catch (LuiValidateException e) {
			expText = e.getMessage();
			results = e.getErrors();
			success = "validator";
			if (e.getViewId() != null) {
				reusltMap.put("exp-view", e.getViewId());
			}
			if (e.getComponentIds() != null && e.getComponentIds().size() > 0) {
				List<Map<String, Object>> expComponents = new ArrayList<Map<String, Object>>();
				reusltMap.put("exp-components", expComponents);
				for (int i = 0; i < e.getComponentIds().size(); i++) {
					Map<String, Object> expComponentNode = new HashMap<String, Object>();
					expComponentNode.put("nodeId", e.getComponentIds().get(i));
					if (i == 0 && e.getMessage() != null && e.getMessage().trim().length() > 0) {
						expComponentNode.put("errorMsg", e.getMessage().trim());
					}
					expComponents.add(expComponentNode);
					List<Map<String, Object>> expElementNodes = new ArrayList<Map<String, Object>>();
					expComponentNode.put("elements", expElementNodes);
					if (e.getElementMap() != null && !e.getElementMap().isEmpty()) {
						Iterator<String> keys = e.getElementMap().keySet().iterator();
						String key = null;
						while (keys.hasNext()) {
							key = keys.next();
							if (e.getElementMap().get(key) == null || e.getElementMap().get(key).trim().length() == 0) {
								continue;
							}
							Map<String, Object> expElementNode = new HashMap<String, Object>();
							expElementNode.put("nodeId", key);
							expElementNode.put("errorMsg", e.getElementMap().get(key).trim());
							expElementNodes.add(expElementNode);
						}
					}
				}
			}
		} catch (LuiRuntimeException e) {
			Throwable th = e.getCause();
			if (th != null && th instanceof InvocationTargetException) {
				InvocationTargetException inexp = (InvocationTargetException) th;
				Throwable trueexp = inexp.getCause();
				if (trueexp != null) {
					expText = trueexp.getMessage();
					expMsg = trueexp.getMessage();
				} else {
					expText = inexp.getMessage();
					expMsg = e.getHint();
				}
				StringWriter sw = new StringWriter();
				expStack = sw.toString();
			} else {
				expText = e.getMessage();
				expMsg = e.getHint();
				StringWriter sw = new StringWriter();
				expStack = sw.toString();
			}
			success = "false";
		}
		// 捕捉所有异常。封装成固定xml，便于客户端统一异常处理
		catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
			expText = e.getMessage();
			expMsg = expText;
			StringWriter sw = new StringWriter();
			expStack = sw.toString();
			success = "false";
		} finally {
			RequestLifeCycleContext.set(null);
		}
		reusltMap.put("exp-text", expText);
		reusltMap.put("show-message", expMsg == null ? "操作过程出现异常" : expMsg);
		reusltMap.put("exp-stack", expStack);
		reusltMap.put("success", success);
		List<Object> contents = new ArrayList<Object>();
		reusltMap.put("contents", contents);
		if (results != null) {
			for (int i = 0; i < results.length; i++) {
				contents.add(JSON.parse(results[i]));
			}
		}
		// 直接以指定编码写出到输出流
		res.getWriter().write(JSON.toJSONString(reusltMap));
	}
}
