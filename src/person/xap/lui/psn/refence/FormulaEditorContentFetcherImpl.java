package xap.lui.psn.refence;

import java.util.UUID;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IWebPartContentFetcher;
import xap.lui.core.comps.WebComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.JsURLEncoder;
import xap.lui.core.xml.StringUtils;
import xap.lui.core.model.AppSession;

public class FormulaEditorContentFetcherImpl implements IWebPartContentFetcher {

	/**
	 * 获取页面内容
	 */
	@Override
	@SuppressWarnings("restriction")
	public String fetchHtml(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String eleid = session.getOriginalParameter("eleid");
		String type = session.getOriginalParameter("type");
		String subeleId = session.getOriginalParameter("subeleid");
		String wherefromValue = session.getOriginalParameter("wherefrom");
		String dsId4Form = "";
		String winId = null;
		String viewId = null;
		
		ViewPartMeta sourceWidget = null;//源window
		PagePartMeta sourceWin = null;//源widget
		if(StringUtils.equals(wherefromValue, "designer")){
			sourceWin = PaCache.getEditorPagePartMeta();
			sourceWidget = PaCache.getEditorViewPartMeta();
		}else{
			
			winId = session.getOriginalParameter("sourcewinId");
			viewId = session.getOriginalParameter("sourceviewId");
			sourceWin = LuiRuntimeContext.getWebContext().getOriginalPageMeta(winId);
			sourceWidget = sourceWin.getWidget(viewId);
		}
			
			
			WebComp comp = null;
			if(sourceWidget != null){
				if(subeleId != null){
					WebComp pComp = sourceWidget.getViewComponents().getComponent(eleid);
					if(type!=null && ("form_element".equals(type) || "grid_header".equals(type))){
						if(pComp instanceof FormComp){
							FormComp form = (FormComp) pComp;
							dsId4Form = form.getDataset();
							comp =form.getElementById(subeleId);
						}
						if(pComp instanceof GridComp){
							GridComp grid = (GridComp) pComp;
							comp = grid;
							dsId4Form = grid.getDataset();
//							comp = (WebComp) grid.getColumnById(subeleId);
						}
					}
				}else {
					comp = sourceWidget.getViewComponents().getComponent(eleid);
				}
			}
			
			
			String formulaOldStr = 	"";	//原始值
			if(comp != null){
				String formulatype = session.getOriginalParameter("formulatype");
				LuiEventConf eventConf = null;
				if(StringUtils.equals("ref_ext2", formulatype)){
					eventConf = getEventConf(comp,"validate_method");
				}else if(StringUtils.equals("ref_ext3", formulatype)){
					eventConf = getEventConf(comp,"editor_method");
				}
				if(eventConf != null) {
					formulaOldStr = JsURLEncoder.encode(eventConf.getScript(), "UTF-8");
					if(formulaOldStr.contains("\"")){
						formulaOldStr = formulaOldStr.replace("\"", "\\\"");
					}
				}
			}
			
	    String sourceWinId = "";
		if(sourceWin!=null){
			 sourceWinId = sourceWin.getId();
		}
		
		String sourceViewId = ""; 
		if(sourceWidget!=null){
			  sourceWinId = sourceWidget.getId();
		}
		
		String otherParam = "&sourcewinId="+winId + "&sourceviewId="+viewId;
		//构造参数串
		String paramUrl = "?model="+FormulaPageModel.class.getName()+"&sourceWin="+sourceWinId+"&sourceView="+sourceViewId+"&eleid="+eleid+"&type="+type+"&dsId4Form="+dsId4Form + "&wherefrom=" + wherefromValue + "&formulaStr=" + formulaOldStr 
				+ otherParam + "&pi=" + UUID.randomUUID().toString();
		return "\"<iframe id='formulaEditor' style='width:940px;height:620px;overflow:auto;border:1px solid #EEEEEE;' src='/portal/app/mockapp/formula"+paramUrl+"'></iframe>\""; 
	}

	private LuiEventConf getEventConf(WebComp comp, String method) {
		LuiEventConf eventConf = comp.getEventConf("valueChanged", method);
		return eventConf;
	}

	/**
	 * 生成页面的执行脚本
	 */
	@Override
	public String fetchBodyScript(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		
		//设置初始值等可在此实现
		return "";
	}

}