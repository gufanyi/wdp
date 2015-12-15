package xap.lui.core.model;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import xap.lui.core.builder.ViewPartProvider;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.ContextResourceUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.exception.LuiRuntimeException;
/**
 * 从公共池中获取相应类型对象
 * 
 * @author gd 2010-1-4
 * @version NC6.0
 * @since NC6.0
 */
public class PoolObjectManager {
	private static final String DS_0000 = "0000";
	private static final String DS_DEVELOP = "develop";
	public static final String MAIN_PATH = "/pagemeta/public/";
	public static final String CMD_PATH = "commands";
	public static final String REF_PATH = "refnodes";
	public static final String DS_PATH = "dspool";
	public static final String PUB_WIDGET_PATH = "widgetpool";
	public static final String PAGE_PATH = "pagepool";
	// ds的缓存key值
	private static final String PUB_WIDGET_CACHE_KEY = "PUB_WIDGET_CACHE_KEY";
	/**
	 * 刷新widget缓存
	 * 
	 * @param ctx
	 * @param ds
	 * @return
	 */
	public static Map<String, Map<String, ViewPartMeta>> refreshWidgetPool(String ctx, ViewPartMeta widget) {
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, ViewPartMeta>> widgets = (HashMap<String, Map<String, ViewPartMeta>>) cache.get(cacheKey);
		if (widgets == null) {
			widgets = new HashMap<String, Map<String, ViewPartMeta>>();
			cache.put(cacheKey, widgets);
		}
		Map<String, ViewPartMeta> widgetMap = widgets.get(ctx);
		if (widgetMap == null) {
			widgetMap = new HashMap<String, ViewPartMeta>();
			widgets.put(ctx, widgetMap);
		}
		widgetMap.put(widget.getId(), widget);
		return widgets;
	}
	/**
	 * 从缓存中删除widget
	 * 
	 * @param ctx
	 * @param widget
	 * @return
	 */
	public static Map<String, Map<String, ViewPartMeta>> removeWidgetFromPool(String ctx, ViewPartMeta widget) {
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, ViewPartMeta>> widgets = (HashMap<String, Map<String, ViewPartMeta>>) cache.get(cacheKey);
		if (widgets != null) {
			Map<String, ViewPartMeta> widgetMap = widgets.get(ctx);
			if (widgetMap != null) {
				widgetMap.remove(widget.getId());
			}
		}
		return widgets;
	}
	/**
	 * 从公共池中获得widget池
	 * 
	 * @param ctx
	 * @return
	 */
	public static Map<String, Map<String, ViewPartMeta>> getWidgetsFromPool(String ctx, String datasource) {
		ensureDatasource();
		if (LuiRuntimeContext.getRootPath() == null) {
			LuiRuntimeContext.setRootPath(ctx);
		}
		LUICache cache = CacheMgr.getStrongCache(WebConstant.LUI_CORE_CACHE, null);
		String cacheKey = PUB_WIDGET_CACHE_KEY;
		HashMap<String, Map<String, Map<String, ViewPartMeta>>> allwidgets = (HashMap<String, Map<String, Map<String, ViewPartMeta>>>) cache.get(cacheKey);
		if (allwidgets == null) {
			allwidgets = new HashMap<String, Map<String, Map<String, ViewPartMeta>>>();
			cache.put(cacheKey, allwidgets);
		}
		Map<String, Map<String, ViewPartMeta>> widgets = allwidgets.get(datasource);
		if (widgets == null) {
			widgets = new HashMap<String, Map<String, ViewPartMeta>>();
			allwidgets.put(datasource, widgets);
		}
		if(ctx==null){
			ctx="/portal";
		}
		Map<String, ViewPartMeta> ctxwidets = widgets.get(ctx);
		ctxwidets = null;
		if (ctxwidets == null) {
			ctxwidets = new HashMap<String, ViewPartMeta>();
			widgets.put(ctx, ctxwidets);
		}
		Map<String, Map<String, ViewPartMeta>> result = new HashMap<String, Map<String, ViewPartMeta>>();
		result.put(ctx, ctxwidets);
		return result;
	}
	/**
	 * 确保Datasource
	 */
	private static void ensureDatasource() {
		String ds = LuiRuntimeContext.getDatasource();
		LuiRuntimeContext.setDatasource(ds == null ? "design" : ds);
		/*
		 * BusiCenterVO[] busicenters = BusicenterHelper.getBusiCenters(); if
		 * (ds != null && !ds.equals("")) { if (!dsExist(ds, busicenters)) ds =
		 * null; } if (ds == null) { try { for (int i = 0; i <
		 * busicenters.length; i++) { // 设置默认数据源 BusiCenterVO busi =
		 * busicenters[i]; if (busi.getCode() != null &&
		 * (DS_DEVELOP.equals(busi.getCode())) ||
		 * DS_0000.equals(busi.getCode())) { continue; } else {
		 * LuiRuntimeEnvironment.setDatasource(busi.getDataSourceName()); break;
		 * } } if (LuiRuntimeEnvironment.getDatasource() == null ||
		 * "".equals(LuiRuntimeEnvironment.getDatasource()))
		 * LuiRuntimeEnvironment
		 * .setDatasource(busicenters[0].getDataSourceName()); } catch
		 * (Exception e) { LuiLogger.error(e); } } else
		 * LuiRuntimeEnvironment.setDatasource
		 * (LuiRuntimeEnvironment.getDatasource());
		 */
	}
	/*
	 * private static boolean dsExist(String ds, BusiCenterVO[] busicenters) {
	 * if (ds.equals(DS_DEVELOP) || ds.equals(DS_0000)) return false; for (int i
	 * = 0; i < busicenters.length; i++) { // 设置默认数据源 BusiCenterVO busi =
	 * busicenters[i]; if (ds.equals(busi.getDataSourceName())) return true; }
	 * return false; }
	 */
	/**
	 * 根据ID从公共池中取得widget
	 * 
	 * @param id
	 * @return
	 */
	public static ViewPartMeta getWidget(String id) {
		String datasource = LuiRuntimeContext.getDatasource();
		if (datasource == null)
			datasource = "design";
		Map<String, Map<String, ViewPartMeta>> widgetMap = getWidgetsFromPool(null, datasource);
		Iterator<Map<String, ViewPartMeta>> it = widgetMap.values().iterator();
		while (it.hasNext()) {
			Map<String, ViewPartMeta> widgetmap = it.next();
			ViewPartMeta widget = widgetmap.get(id);
			if (widget != null) {
				return (ViewPartMeta) widget.clone();
			}
		}
		String path = "lui/views/" + id + "/" + id + ".view.xml";
		try {
			ViewPartMeta widget = ViewPartMeta.parse(ContextResourceUtil.getResourceAsStream(path));
			widget.setId(widget.getId());
			widgetMap.get(LuiRuntimeContext.getRootPath()).put(widget.getId(), widget);
			return (ViewPartMeta) widget.clone();
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public static ViewPartMeta rebuiltWidget(ViewPartMeta widget, String providerStr) {
		try {
			widget = (ViewPartMeta) widget.clone();
			Class<ViewPartProvider> providerClass = (Class<ViewPartProvider>) Class.forName(providerStr);
			ViewPartProvider d = providerClass.newInstance();
			widget = d.buildWidget(null, widget, null, widget.getId());
			widget.setIsCustom(false);
		} catch (Exception e) {
			throw new LuiRuntimeException(e.getMessage());
		}
		return widget;
	}
}
