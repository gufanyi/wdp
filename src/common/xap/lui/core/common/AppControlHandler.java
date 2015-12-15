package xap.lui.core.common;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import xap.lui.core.builder.ApplicationParser;
import xap.lui.core.constant.AppConsts;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Application;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.processor.MockRequest;
import xap.lui.core.util.ApplicationNodeUtil;
/**
 * App 类型节点控制器
 * 
 * @author dengjt
 *
 */
public class AppControlHandler implements ControlHandler {
	public static final String NODECODE = "nodecode";
	public static final String DSName = "dsname";
	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, String path) throws Exception {
		String appPath = req.getPathInfo();
		String appId = appPath.substring(appPath.indexOf("/") + 1, appPath.lastIndexOf("."));
		String realPath = null;
		if (appId != null) {
			try {
				realPath = ApplicationNodeUtil.getApplicationNodeDir(appId);
			} catch (LuiRuntimeException e) {
				return;
			}
			if (realPath == null)
				realPath = appId;
		}
		// 创建AppWebSession
		LuiWebSession webSes = (LuiWebSession) LuiRuntimeContext.getWebContext().getWebSessionByAppId(appId);
		webSes.setAttribute(LuiWebContext.APP_ID, appId);
		Application app = (Application) webSes.getAttribute(LuiWebContext.APP_CONF);
		String winid = req.getParameter(AppConsts.PARAM_WIN_ID);
		if (app == null) {
			if (!appId.equals("mockapp")) {
				InputStream inputStream = ContextResourceUtil.getResourceAsStream("/lui/apps/" + realPath + "/"+appId+".app.xml",false);
				app = ApplicationParser.parse(inputStream);
			} else {
				if (winid == null || winid.equals("")) {
					throw new LuiRuntimeException("Mock app 必须输入window id");
				}
				PagePartMeta pm = new PagePartMeta();
				pm.setId(winid);
				app = new Application();
				app.addWindow(pm);
			}
			webSes.setAttribute(LuiWebContext.APP_CONF, app);
		}
		if (app == null)
			throw new LuiRuntimeException("没有找到APP," + appId);
		String winOpe = req.getParameter(AppConsts.PARAM_WIN_OPE);
		if (winid == null || winid.isEmpty() || winid.equals("null")) {
			winid = app.getDefaultWindowId();
		}
		if (winid == null)
			throw new LuiRuntimeException("没有设置window id");
		String url = null;
		int index = winid.indexOf(".");
		if (index != -1) {
			url = "/core/uimeta.jsp";
			winid = winid.split("\\.")[0];
		} else {
			url = "/core/uimeta.ra";
		}
		if (winOpe != null && winOpe.equals("add")) {
			if (app.getWindowConf(winid) == null) {
				PagePartMeta win = new PagePartMeta();
				win.setId(winid);
				app.addWindow(win);
			}
		}
		MockRequest mockReq = new MockRequest(req);
		Map paramMap = req.getParameterMap();
		Iterator it = paramMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			String[] value = (String[]) entry.getValue();
			if (!key.equals(AppConsts.PARAM_WIN_ID)) {
				mockReq.addParameter((String) entry.getKey(), value[0]);
			}
		}
		mockReq.addParameter("pageId", winid);
		mockReq.addParameter(ParamConstant.APP_UNIQUE_ID, LuiRuntimeContext.getWebContext().getAppUniqueId());
		LuiWebContext webCtx = new LuiWebContext(mockReq);
		LuiRuntimeContext.setWebContext(webCtx);
		try {
			AppSession lifeCtx = new AppSession();
			AppContext appCtx = new AppContext();
			String nodecode = req.getParameter(NODECODE);
			if (nodecode != null && !"".equals(nodecode)) {
				appCtx.addAppAttribute(NODECODE, nodecode);
			}
			appCtx.addAppAttribute(LuiWebContext.Http_Session_ID, req.getSession().getId());
			appCtx.addAppAttribute(DSName, LuiRuntimeContext.getDatasource());
			lifeCtx.setAppContext(appCtx);
			AppSession.current(lifeCtx);
			RequestDispatcher dispatcher = LuiRuntimeContext.getServletContext().getRequestDispatcher(url);
			dispatcher.forward(mockReq, res);
		} finally {
			AppSession.reset();
		}
	}
}
