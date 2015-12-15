package xap.lui.core.json;
import java.util.ArrayList;
import java.util.List;
import xap.lui.core.constant.EventContextConstant;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.WindowContext;
import com.alibaba.fastjson.JSONObject;
public class WinCtx2JsonSerializer implements IObject2JsonSerializer<WindowContext> {
	@Override
	public JSONObject serialize(WindowContext obj) {
		com.alibaba.fastjson.JSONObject pageJsonObj = new com.alibaba.fastjson.JSONObject();
		PagePartMeta meta = obj.getPagePartMeta();
		if (meta == null) {
			return null;
		}
		if (meta.isCtxChanged()) {
			pageJsonObj.put(EventContextConstant.context, meta.getContext());
		}
		ViewPartContext[] ctxs = obj.getViewContexts();
		if(ctxs!=null){
			List<JSONObject> views = new ArrayList<>(ctxs.length);
			for (int i = 0; i < ctxs.length; i++) {
				JSONObject object = new ViewCtx2JsonSerializer().serialize(ctxs[i]);
				views.add(object);
			}
			pageJsonObj.put("views", views);
		}
		if (obj.getBeforeExecScript() != null) {
			List<String> sList = obj.getBeforeExecScript();
			pageJsonObj.put("beforeExec", sList);
		}
		if (obj.getExecScript() != null) {
			List<String> sList = obj.getExecScript();
			pageJsonObj.put("exec", sList);
		}
		return pageJsonObj;
	}
}
