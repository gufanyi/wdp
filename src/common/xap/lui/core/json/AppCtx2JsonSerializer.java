package xap.lui.core.json;
import java.util.ArrayList;
import java.util.List;
import xap.lui.core.constant.EventContextConstant;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.WindowContext;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
public class AppCtx2JsonSerializer implements IObject2JsonSerializer<AppSession> {
	@Override
	public String serialize(AppSession obj) {
		com.alibaba.fastjson.JSONObject appJsonObj = new com.alibaba.fastjson.JSONObject();
		AppContext appCtx = obj.getAppContext();
		if (appCtx.getClientSession().getAttributeMap().size() > 0) {
			appJsonObj.put(EventContextConstant.attributes, appCtx.getClientSession());
		}
		if (appCtx.getAfterExecScript() != null) {
			appJsonObj.put("afterExec", appCtx.getAfterExecScript());
		}
		{
			appJsonObj.put("isPlug", obj.getParameter(AppSession.PLUGOUT_SIGN) == null ? false : true);
		}
		if (appCtx.getCurrentEvent() != null && appCtx.getCurrentEvent().isStop()) {
			appJsonObj.put("result", false);
		}
		WindowContext windowCtx = appCtx.getCurrentWindowContext();
		//返回结果
		JSONObject pageJsonObj = new WinCtx2JsonSerializer().serialize(windowCtx);
		appJsonObj.put("pagemeta", pageJsonObj);
		
		WindowContext[] ctxs = appCtx.getWindowCtxs();
		if (ctxs != null && ctxs.length > 0) {
			List<JSONObject> list=new ArrayList<JSONObject>();
			for (int i = 0; i < ctxs.length; i++) {
				if (ctxs[i].equals(appCtx.getCurrentWindowContext())){
					continue;
				}
				JSONObject paretPageJsonObj = new WinCtx2JsonSerializer().serialize(ctxs[i]);
				JSONObject  innerObj=new JSONObject();
				innerObj.put("pagemeta", paretPageJsonObj);
				list.add(innerObj);
			}
			
			appJsonObj.put("parentpe", list);
		}
		String str = JSON.toJSONString(appJsonObj);
		//java.util.logging.Logger.getAnonymousLogger().info(str);
		return str;
	}
}
