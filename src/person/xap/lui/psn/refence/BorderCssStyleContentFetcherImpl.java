package xap.lui.psn.refence;

import java.util.List;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.IWebPartContentFetcher;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;


@SuppressWarnings("restriction")
public class BorderCssStyleContentFetcherImpl implements IWebPartContentFetcher {

	/**
	 * 获取页面内容
	 */
	@Override
	public String fetchHtml(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String sourceType = session.getOriginalParameter("sourceType");
		String sourceWinId = session.getOriginalParameter("sourceWinId");
		String uiId = session.getOriginalParameter("uiId");
		String sourceViewId = session.getOriginalParameter("sourceViewId");		
		String paramBuf = "uiId=" + uiId + "&sourceWinId=" + sourceWinId + "&sourceViewId=" + sourceViewId + "&sourceType="+sourceType;
		
		
		IPaEditorService pes =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		UIPartMeta uiMeta = null;
		uiMeta = pes.getOriUIMeta(sourceWinId, sessionId);
		UIElement uiEle = uiMeta.findChildById(uiId);
		UILayout layout = (UILayout)uiEle;
		layout.getPanelList();
		
		return "<iframe id=\"bordereditor\" style=\"width:665px;height:265px;overflow-y:hidden;overflow-x:auto;border:1px solid #EEEEEE;\" src='webtemp/html/nodes/pa/bordereditor/bordereditor.jsp?"+paramBuf+"'></iframe>";
	}

	/**
	 * 生成页面的执行脚本
	 */
	@Override
	public String fetchBodyScript(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String sourceWinId = session.getOriginalParameter("sourceWinId");
		String uiId = session.getOriginalParameter("uiId");
		IPaEditorService pes =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		UIPartMeta uiMeta = null;
		uiMeta = pes.getOriUIMeta(sourceWinId, sessionId);
		UIElement uiEle = uiMeta.findChildById(uiId);
		UILayout layout = (UILayout)uiEle;
		StringBuffer jsScript = new StringBuffer();
		List<UILayoutPanel> panelList = layout.getPanelList();
		String ids = "";
		for (int i = 0; i < panelList.size(); i++) {
			UILayoutPanel panel = panelList.get(i);
			String pId = panel.getId();
			if(i<panelList.size()-1){
				ids+=pId+",";
			}else{
				ids+=pId;
			}
			String topBorder = panel.getTopBorder()==null?"":panel.getTopBorder();
			String bottomBorder = panel.getBottomBorder()==null?"":panel.getBottomBorder();
			String leftBorder = panel.getLeftBorder()==null?"":panel.getLeftBorder();
			String rightBorder = panel.getRightBorder()==null?"":panel.getRightBorder();
		}
		return "";
	}

}
