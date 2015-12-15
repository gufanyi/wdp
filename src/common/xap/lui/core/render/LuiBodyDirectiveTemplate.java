package xap.lui.core.render;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import xap.lui.core.builder.LuiMaskerUtil;
import xap.lui.core.builder.Window;
import xap.lui.core.common.ClientSession;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.ExtAttribute;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.util.JsURLEncoder;
import xap.lui.core.util.StringUtil;

import com.alibaba.fastjson.JSON;

import freemarker.core.Environment;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
public class LuiBodyDirectiveTemplate extends BaseLuiDirectiveModel {
	protected static final String DS_SCRIPT = "dsScript";
	protected static final String ALL_SCRIPT = "allScript";
	protected StringBuffer scriptBuf = null;
	protected Environment evn = null;
	public Environment getEvn() {
		return evn;
	}
	public void setEvn(Environment evn) {
		this.evn = evn;
	}
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		this.evn = env;
		scriptBuf = new StringBuffer(4096);
		StringModel strModel = (StringModel) env.getGlobalVariables().get("pageModel");
		Window window = (Window) strModel.getWrappedObject();
		this.setWindow(window);
		try {
			PagePartMeta pageMeta = window.getPageMeta();
			scriptBuf.append("function pageBodyScript(){\r\n");
			LuiMaskerUtil.makeMaskerScript(scriptBuf);
			scriptBuf.append(renderPageUI());
			scriptBuf.append("window.$paramsMap = $.hashmap.getObj();\n");
			Iterator it = LuiRuntimeContext.getWebContext().getRequest().getParameterMap().entrySet().iterator();
			{
				while (it.hasNext()) {
					Entry entry = (Entry) it.next();
					scriptBuf.append("$.pageutils.setParameter(\"").append(entry.getKey()).append("\",\"");
					String str = StringUtil.convertToCorrectEncoding(((String[]) entry.getValue())[0]);
					scriptBuf.append(JsURLEncoder.encode(str, "UTF-8")).append("\");\n");
				}
			}
			scriptBuf.append(renderClientSession(window));
			if (pageMeta.getCaption() != null) {
				scriptBuf.append("document.title = \"").append(pageMeta.getCaption()).append("\";\n");
			}
			Map<String, ExtAttribute> attrMap = pageMeta.getExtendMap();
			if (attrMap != null && !attrMap.isEmpty()) {
				Iterator<String> attrIt = attrMap.keySet().iterator();
				while (attrIt.hasNext()) {
					String attrName = attrIt.next();
					scriptBuf.append("pageUI.addAttribute('" + attrName + "', '" + attrMap.get(attrName) + "');\n");
				}
			}
			scriptBuf.append(renderLuiBody(window.getUIMeta(), window.getPageMeta()));
			scriptBuf.append("if(typeof(externalInit) != 'undefined'){\n externalInit();\n}\n");
			scriptBuf.append("pageUI.renderDone = true;\n");
			scriptBuf.append("pageUI.$afterPageInit();\n");
			scriptBuf.append("pageUI.$beforeInitData();\n").append("pageUI.$beforeActive();\n");
			scriptBuf.append("$(window).triggerHandler('resize');\n");
			scriptBuf.append("window.renderDone = true;\n");
			scriptBuf.append("if(window.editMode){document.body.className='unselectable';}\n");
			scriptBuf.append("}\n");
			StringBuilder buf2 = new StringBuilder();
			buf2.append("pageBodyScript();");
			env.getOut().write("<script>\n" + scriptBuf.toString() + buf2.toString() + "\n </script>");
		} catch (Throwable e) {
			LuiLogger.error(e.getMessage(), e);
			if (window != null)
				window.destroy();
			if (e instanceof LuiRuntimeException)
				throw (LuiRuntimeException) e;
			throw new LuiRuntimeException(e);
		}
	}
	private String renderLuiBody(IUIPartMeta meta, PagePartMeta pagemeta) throws IOException {
		StringBuilder builder = new StringBuilder();
		ILuiRender render =((UIPartMeta)meta).getRender();
		if (render == null) {
			return "";
		}
		((IDynamicAttributes) render).setContextAttribute(UIRender.DS_SCRIPT, new StringBuilder());
		this.getEvn().getOut().write("<script>"+render.place()+"</script>");
		String script = render.create();
		builder.append(script);
		return builder.toString();
	}
	private String renderPageUI() {
		StringBuilder buf = new StringBuilder();
		PagePartMeta pm = this.getWindow().getPageMeta();
		String caption = pm.getCaption();
		if (caption == null)
			caption = "";
		buf.append("window.pageUI = $.pagepart.getObj('").append(pm.getId()).append("','").append(pm.getCaption()).append("');\n");
		buf.append("$.application.getObj().addPageUI('").append(pm.getId()).append("', window.pageUI);\n");
		String eventStr = addEventSupport(pm, null, "pageUI", null);
		buf.append(eventStr);
		return buf.toString();
	}
	private String renderClientSession(Window model) {
		try {
			ClientSession cs = model.getClientSession();
			if (cs == null) {
				return "";
			}
			StringBuilder builder = new StringBuilder();
			Map<String, Serializable> map = cs.getAttributeMap();
			if (map != null && map.size() > 0) {
				// LuiJsonSerializer jsonSerialzer =
				// LuiJsonSerializer.getInstance();
				// String jsonStr = jsonSerialzer.toJSON(map);
				String jsonStr = JSON.toJSONString(map);
				builder.append(CS_PRE).append("clientSession = ").append(jsonStr).append(";\n");
			}
			Map<String, Serializable> stickMap = cs.getStickAttributeMap();
			if (stickMap != null && stickMap.size() > 0) {
				Iterator<Entry<String, Serializable>> it = stickMap.entrySet().iterator();
				builder.append(CS_PRE).append("clientStickKeys = '");
				while (it.hasNext()) {
					Entry<String, Serializable> entry = it.next();
					if (entry.getValue() == null) {
						continue;
					}
					builder.append(entry.getKey()).append("=").append(JsURLEncoder.encode(entry.getValue().toString(), "UTF-8"));
					if (it.hasNext())
						builder.append("&");
				}
				builder.append("';\n");
			}
			return builder.toString();
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected String getSourceType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_PAGEMETA;
	}
}
