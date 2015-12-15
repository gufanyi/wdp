package xap.lui.core.event;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.Connector;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PlugEventAdjuster;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.WindowContext;
import xap.lui.core.pluginout.IPlugoutType;
import xap.lui.core.pluginout.PlugoutTypeFactory;
import xap.lui.core.refrence.AppRefDftOkCtrl;
import xap.lui.core.util.ClassUtil;


public class PlugEventHandler {
	public void doPlug() throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		AppSession ctx = AppSession.current();
		String plugoutId = ctx.getParameter(AppSession.PLUGOUT_ID);
		String plugoutSource = ctx.getParameter(AppSession.PLUGOUT_SOURCE);
		
		WindowContext winCtx = null;
		winCtx = ctx.getWindowContext();
		ViewPartContext viewCtx = winCtx.getViewContext(plugoutSource);
		ViewPartMeta widget = winCtx.getPagePartMeta().getWidget(plugoutSource);
		PipeOut plugout = widget.getPipeOut(plugoutId);
		
		
		Map<String, Object> paramMap = ctx.getAppContext().getPlug(winCtx.getId() + "_" + plugoutSource + "_" + plugoutId);

		//window内plug调用 
		Connector[] connectors = winCtx.getPagePartMeta().getConnectors();
		if(connectors != null && connectors.length >0){
			Map<String, Object> resultMap = buildPlugContent(plugout, viewCtx, paramMap);
			for (int i = 0; i < connectors.length; i++) {
				Connector conn = connectors[i];
				if (conn.getSource().equals(plugoutSource) && conn.getPipeoutId().equals(plugoutId)){
					eventInvoke(ctx, conn, resultMap);
				}
			}
		}
		
		//app中window间plug调用 
		List<Connector> connectorList = ctx.getAppContext().getApplication().getConnectorList();
		if (connectorList != null){
				Map<String, Object> resultMap = buildPlugContent(plugout, viewCtx, paramMap);
			String sourceWindowId = winCtx.getPagePartMeta().getId();
//			for (Connector conn : connectorList){
//				if (conn.getSourceWindow().equals(sourceWindowId) && conn.getSource().equals(plugoutSource) && conn.getPlugoutId().equals(plugoutId)){
//					eventInvoke(ctx, conn, resultMap);
//				}
//			}
			for (Connector conn : connectorList){
				String pageId = LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter(PlugEventAdjuster.PAGEID);
				if(pageId!=null &&!pageId.equals("null") && !"".equals(pageId) && !"reference".equals(pageId)){
					if(conn.getSourceWindow().equals(pageId) && conn.getSource().equals(plugoutSource) && conn.getPipeoutId().equals(plugoutId)){
						eventInvoke(ctx, conn, resultMap);
					}
				}
				else{
					if (conn.getSourceWindow().equals(sourceWindowId) && conn.getSource().equals(plugoutSource) && conn.getPipeoutId().equals(plugoutId)){
						eventInvoke(ctx, conn, resultMap);
					}
				}
			}
		}
		
		
		
		
	}

	private void eventInvoke(AppSession ctx, Connector conn, Map<String, Object> resultMap) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		ViewPartMeta targetWidget = null;
		WindowContext oriWinCtx = ctx.getAppContext().getCurrentWindowContext();
		ViewPartContext oriViewCtx = ctx.getViewContext();
		if (conn.getTargetWindow() == null || conn.getTargetWindow().equals(""))
			targetWidget = ctx.getWindowContext().getPagePartMeta().getWidget(conn.getTarget());
		else{
			WindowContext targetWinCtex = ctx.getAppContext().getWindowContext(conn.getTargetWindow());
			if (targetWinCtex == null)
				return;
			targetWidget = targetWinCtex.getPagePartMeta().getWidget(conn.getTarget());
			ctx.getAppContext().setCurrentWindowContext(targetWinCtex);
		}

		if (targetWidget == null)
			return;
		
		PipeIn plugin = targetWidget.getPipeIn(conn.getPipeinId());
		String controllerClazz = targetWidget.getController();

		String methodName = "plugin" + plugin.getId();
		Object controller = null;
		Method m = null;
		if (plugin.getId().equals(AppRefDftOkCtrl.PLUGIN_ID)){
			if(StringUtils.isNotBlank(controllerClazz)){
				controller = ClassUtil.newInstance(controllerClazz);
				try{
					m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
				} 
				catch (NoSuchMethodException e){
				}
			}
			if(m == null){
				controller = ClassUtil.newInstance(AppRefDftOkCtrl.class);
				try{
					m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
				} 
				catch (NoSuchMethodException e){
					LuiLogger.error(e);
					throw new LuiRuntimeException(e.getMessage());
				}
			}
		}
		else{
			controller = ClassUtil.newInstance(controllerClazz);
			m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
		}
		
		ViewPartContext pluginViewCtx = ctx.getWindowContext().getViewContext(conn.getTarget());
		ctx.getWindowContext().setCurrentViewContext(pluginViewCtx);
		Map<String, Object> mappingObj = mapping(conn, resultMap);
//		Object controller = ClassUtil.newInstance(controllerClazz);
//		Method m = controller.getClass().getMethod(methodName, new Class[]{Map.class});
		m.invoke(controller, mappingObj);
		
		ctx.getAppContext().setCurrentWindowContext(oriWinCtx);
		ctx.getWindowContext().setCurrentViewContext(oriViewCtx);
	}
	
	private Map<String, Object> mapping(Connector conn, Map<String, Object> resultMap) {
		Map<String, Object> mappingObj = new HashMap<String, Object>();
		if (conn.getMapping() != null){
			Iterator<Entry<String, String>> entryIt = conn.getMapping().entrySet().iterator();
			while(entryIt.hasNext()){
				Entry<String, String> entry = entryIt.next();
				mappingObj.put(entry.getValue(), resultMap.get(entry.getKey()));
				resultMap.remove(entry.getKey());
			}
		}
		
		Iterator<String> resultIt = resultMap.keySet().iterator();
		while(resultIt.hasNext()){
			String key = resultIt.next();
			mappingObj.put(key, resultMap.get(key));
		}
		
		return mappingObj;
	}

	private Map<String, Object> buildPlugContent(PipeOut plugout, ViewPartContext viewCtx, Map<String, Object> paramMap) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<PipeOutItem> itemList = plugout.getItemList();
		
		if(paramMap != null)
			result.putAll(paramMap);
		
		if(itemList != null){
		Iterator<PipeOutItem> itemIt = itemList.iterator();
			while(itemIt.hasNext()){
				PipeOutItem item = itemIt.next();
				String key = item.getName();
				if (!result.containsKey(key)){
					String type = item.getType();
					IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
					if (plugoutType != null){
						Object obj = plugoutType.fetchContent(item, viewCtx);
						result.put(item.getName(), obj);
					}
				}
			}
		}
		return result;
	}
}
