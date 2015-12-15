package xap.lui.core.builder;
import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

public class RaSelfWindow extends Window {
	@Override
	protected PagePartMeta createPageMeta() {
		//String widgetId = (String) getWebSesssion().getRequest().getParameter("viewId");
		// if (WebConstant.DEFAULT_WINDOW_ID.equals(pageId)) { // 针对public
		// PagePartMeta pageMeta = new PagePartMeta();
		// pageMeta.setId(pageId);
		// InputStream input;
		// try {
		// String folderPath = "pagemeta/public/widgetpool/" + widgetId +
		// "/widget.wd";
		// String fullPath = ContextResourceUtil.getCurrentAppPath() +
		// folderPath;
		// input = new FileInputStream(fullPath);
		// } catch (FileNotFoundException e) {
		// throw new LuiRuntimeException(e.getMessage());
		// }
		// ViewPartMeta widget = ViewPartMetaParser.parse(input);
		// pageMeta.addWidget(widget);
		// return pageMeta;
		// } else {
		LuiRuntimeContext.setMode(WebConstant.MODE_DESIGN);
		PagePartMeta oldPm = null;
		oldPm = (PagePartMeta) PaCache.getEditorPagePartMeta();
		return oldPm;
	}
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		// if ("defaultWindowId".equals(pageId)) { // 针对public view的特殊处理
		// String folderPath = "pagemeta/public/widgetpool/" + widgetId;
		// String fullPath = ContextResourceUtil.getCurrentAppPath() +
		// folderPath;
		// UIMetaParserUtil parserUtil = new UIMetaParserUtil();
		// parserUtil.setPagemeta(pm);
		// UIPartMeta meta = parserUtil.parseUIMeta(fullPath, pm.getId(), null);
		// UIPartMeta uimeta = new UIPartMeta();
		// UIViewPart uiWidget = new UIViewPart();
		// uiWidget.setId(widgetId);
		// uimeta.setElement(uiWidget);
		// uiWidget.setUimeta(meta);
		// return uimeta;
		// }
		{
			
			IPaEditorService ipaService = new PaEditorServiceImpl();
			String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
			String pageId = (String) PaCache.getInstance().get("_pageId");
			//String viewId = (String) PaCache.getInstance().get("_viewId");
			UIPartMeta uiMeta = ipaService.getOriUIMeta(pageId, sessionId);
			//UIPartMeta uiMeta = (UIPartMeta) PaCache.getEditorUIPartMeta();
			return uiMeta;
		}
	}
}
