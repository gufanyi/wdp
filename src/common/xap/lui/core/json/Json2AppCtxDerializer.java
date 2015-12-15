package xap.lui.core.json;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.PageUIContext;
import xap.lui.core.context.TabContext;
import xap.lui.core.context.WidgetUIContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.WindowContext;
import xap.lui.core.parser.UIMetaParserUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
public class Json2AppCtxDerializer implements IJson2ObjectDeserialize<AppSession> {
	private AppSession lifeCycleCtx;
	@Override
	public AppSession derialize(String json) {
		LifeCyclePhase currentPhase = RequestLifeCycleContext.get().getPhase();
		try {
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			LuiWebContext webCtx = LuiRuntimeContext.getWebContext();
			PagePartMeta pagemeta = webCtx.getPageMeta();
			IUIPartMeta uimeta = webCtx.getUIMeta();
			LuiRenderContext.current().setPagePartMeta(pagemeta);
			LuiRenderContext.current().setUiPartMeta((UIPartMeta)uimeta);
			lifeCycleCtx = new AppSession();
			AppContext appCtx = new AppContext();
			lifeCycleCtx.setAppContext(appCtx);
			String compress = webCtx.getRequest().getParameter("compress");
			if (compress != null) {
				int compressLength = Integer.parseInt(webCtx.getRequest().getParameter("compressl"));
				json = decompressRequest(json, compressLength);
			}
			serialize(json, pagemeta, uimeta);
			return lifeCycleCtx;
		} catch (Exception e) {
			throw new LuiRuntimeException(e);
		} finally {
			RequestLifeCycleContext.get().setPhase(currentPhase);
		}
	}
	private String decompressRequest(String xml, int length) throws IOException {
		try {
			BASE64Decoder base64 = new BASE64Decoder();
			Inflater decompressor = new Inflater();
			byte[] rawbytes = base64.decodeBuffer(xml);
			decompressor.setInput(rawbytes, 0, rawbytes.length);
			byte[] buffer = new byte[length * 2 + 2048];
			int resultLength = decompressor.inflate(buffer);
			decompressor.end();
			String resultString = new String(buffer, 0, resultLength, "UTF-8");
			return resultString;
		} catch (DataFormatException e) {
			LuiLogger.error(e);
			throw new LuiRuntimeException("error occurred while decompressing");
		}
	}
	protected AppSession serialize(String json, PagePartMeta pagemeta, IUIPartMeta uimeta) {
		try {
			JSONObject object = (JSONObject) JSON.parse(json);
			return serialize(object, pagemeta, uimeta);
		} catch (Exception e) {
			e.printStackTrace();
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	private AppSession serialize(JSONObject jsonObject, PagePartMeta pagemeta, IUIPartMeta uimeta) throws SAXException, IOException {
		AppContext appCtx = lifeCycleCtx.getAppContext();
		String winId = jsonObject.getString("id");
		WindowContext winCtx = new WindowContext();
		winCtx.setId(winId);
		winCtx.setPagePartMeta(pagemeta);
		winCtx.setUIPartMeta(uimeta);
		appCtx.addWindowContext(winCtx);
		JSONArray groupParamsNode = (JSONArray) jsonObject.getJSONArray("reqparas");
		if (groupParamsNode != null) {
			if (groupParamsNode.size() > 0) {
				List<Map<String, String>> gplist = new ArrayList<Map<String, String>>();
				lifeCycleCtx.setGroupParam(gplist);
				for (int i = 0; i < groupParamsNode.size(); i++) {
					JSONObject paramsEle = groupParamsNode.getJSONObject(i);
					Map<String, String> paramMap = parseParams(paramsEle);
					gplist.add(paramMap);
				}
			}
		}
		JSONObject pageMetaJson = jsonObject.getJSONObject("pagemeta");
		PageUIContext context = pageMetaJson.getObject("context", PageUIContext.class);
		if (context != null) {
			pagemeta.setContext(context);
		}
		JSONArray widgets = pageMetaJson.getJSONArray("views");
		if(widgets!=null){
			for (int i = 0; i < widgets.size(); i++) {
				JSONObject widgetEle = widgets.getJSONObject(i);
				this.processwidget(winCtx, pagemeta, (UIPartMeta) uimeta, widgetEle);
			}
		}
		{
			JSONArray tabJsons = pageMetaJson.getJSONArray("tabcomps");
			if (tabJsons != null) {
				for (int i = 0; i < tabJsons.size(); i++) {
					JSONObject tabJson = tabJsons.getJSONObject(i);
					try {
						TabContext baseCtx = (TabContext) tabJson.getObject("context", TabContext.class);
						UITabComp tab = (UITabComp) UIElementFinder.findElementById((UIPartMeta) uimeta, baseCtx.getId());
						tab.setCurrentItem(baseCtx.getCurrentIndex());
					} catch (Throwable e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
		JSONObject pcontextNode = jsonObject.getJSONObject("parentpe");
		if (pcontextNode != null) {
			String pId = pcontextNode.getString("id");
 			PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getOriginalPageMeta(pId);
			IUIPartMeta parentUm = LuiRuntimeContext.getWebContext().getOriginalUm(pId);
			serialize(pcontextNode, parentPm, parentUm);
		}
		return lifeCycleCtx;
	}
	private Map<String, String> parseParams(JSONObject paramsNode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		Set<String> keys = paramsNode.keySet();
		for (Iterator<String> iter = keys.iterator(); iter.hasNext();) {
			String key = iter.next();
			String value = paramsNode.getString(key);
			if (value != null) {
				value = value.replaceAll("&lt;", "<");
				value = value.replaceAll("&amp;", "&");
			}
			paramMap.put(key, value);
		}
		return paramMap;
	}
	protected void processwidget(WindowContext winCtx, PagePartMeta pagemeta, UIPartMeta um, JSONObject widgetEle) throws SAXException, IOException {
		String widgetId = widgetEle.getString("id");
		ViewPartMeta widget = pagemeta.getWidget(widgetId);
		ViewPartConfig wcf = pagemeta.getViewPartConf(widgetId);
		if (widget == null) {
			throw new LuiRuntimeException("根据ID没有找到对应的widget配置," + widgetId);
		}
		ViewPartContext vCtx = new ViewPartContext();
		vCtx.setId(widgetId);
		UIViewPart uiWidget = (UIViewPart) UIElementFinder.findUIWidget(um, widgetId);
		if (uiWidget != null)
			vCtx.setUIMeta(uiWidget.getUimeta());
		else {
			String appPath = ContextResourceUtil.getCurrentAppPath();
			String folderPath = pagemeta.getFoldPath();
			String refId = widget.getRefId();
			if (refId == null)
				refId = wcf.getRefId();
			String path = appPath + folderPath + "/" + widgetId;
			if (refId != null && refId.startsWith("../")) {
				folderPath = "pagemeta/public/widgetpool";
				path = appPath + folderPath + "/" + refId.substring(3);
			}
			UIPartMeta viewUIMeta = (UIPartMeta) LuiRuntimeContext.getWebContext().getPageWebSession().getAttribute("$TMP_UM_" + widgetId);
			if (viewUIMeta == null) {
				viewUIMeta = new UIMetaParserUtil(false).parseUIMeta(path+"/"+pagemeta.getId(),pagemeta.getId(), widgetId);
			}
			vCtx.setUIMeta(viewUIMeta);
		}
		vCtx.setView(widget);
		winCtx.addViewContext(vCtx);
		String init = widgetEle.getString("init");
		if (init != null && init.equals("false")) {
			return;
		}
		{
			WidgetUIContext newWidget = widgetEle.getObject("context", WidgetUIContext.class);
			widget.setContext(newWidget);
		}
		JSONArray datasetJsons = widgetEle.getJSONArray("datasets");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pagemeta", pagemeta);
		if(datasetJsons!=null){
			new Json2DatasetDesirializer().serialize(datasetJsons, paramMap);
		}
		
		{
			JSONArray componentJsons = widgetEle.getJSONArray("componets");
			if (componentJsons != null) {
				for (int i = 0; i < componentJsons.size(); i++) {
					JSONObject jsonObject = componentJsons.getJSONObject(i);
					ViewPartComps viewComponent = widget.getViewComponents();
					WebComp webcomp = (WebComp) viewComponent.getComponent(jsonObject.getString("id"));
					//JSONObject webCtx = jsonObject.getJSONObject("context");
					String clazzName = jsonObject.getString("c");
					try {
						Class<?> clazz = Class.forName("xap.lui.core.context."+clazzName);
						BaseContext	 baseCtx=(BaseContext)TypeUtils.cast(jsonObject, clazz, null);
						if (webcomp != null)
							webcomp.setContext(baseCtx);
					} catch (Throwable e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
		{
			JSONArray menubarJsons = widgetEle.getJSONArray("menubars");
			if (menubarJsons != null) {
				for (int i = 0; i < menubarJsons.size(); i++) {
					JSONObject jsonObject = menubarJsons.getJSONObject(i);
					MenubarComp menubar = widget.getViewMenus().getMenuBar(jsonObject.getString("id"));
					JSONObject webCtx = jsonObject.getJSONObject("context");
					String clazzName = webCtx.getString("clc");
					try {
						Class<?> clazz = Class.forName(clazzName);
						BaseContext baseCtx = (BaseContext) jsonObject.getObject("context", clazz);
						if (baseCtx != null)
							menubar.setContext(baseCtx);
					} catch (Throwable e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
		{
			JSONArray tabJsons = widgetEle.getJSONArray("tabcomps");
			if (tabJsons != null) {
				for (int i = 0; i < tabJsons.size(); i++) {
					JSONObject jsonObject = tabJsons.getJSONObject(i);
					try {
						TabContext	 baseCtx=(TabContext)TypeUtils.cast(jsonObject, TabContext.class, null);
						UIPartMeta uimeta = vCtx.getUIMeta();
						UITabComp tab = (UITabComp) UIElementFinder.findElementById(uimeta, baseCtx.getId());
						tab.setCurrentItem(baseCtx.getCurrentIndex());
					} catch (Throwable e) {
						throw new RuntimeException(e.getMessage());
					}
				}
			}
		}
	}
}
