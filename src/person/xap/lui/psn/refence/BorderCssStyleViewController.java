package xap.lui.psn.refence;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;


@SuppressWarnings("restriction")
public class BorderCssStyleViewController extends CssStyleViewController {
	
	@Override
	public void onOkEvent(ScriptEvent e) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String sourceWinId = session.getOriginalParameter("sourceWinId");
		IPaEditorService pes =  new PaEditorServiceImpl();
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		UIPartMeta uiMeta = pes.getOriUIMeta(sourceWinId, sessionId);
		AppSession ctx = AppSession.current();
		String borderstyle = ctx.getParameter("borderstyle");
		String selectedIdsStr = ctx.getParameter("selectedids");
		if(!StringUtils.isBlank(selectedIdsStr)){
			String idsArray[] = selectedIdsStr.split(",");
			LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			for (int i = 0; i < idsArray.length; i++) {
				String id = idsArray[i];
				String strArray[] = id.split("_");
				String panelId = strArray[0];
				String direction = strArray[2];
				UIElement uiEle = uiMeta.findChildById(panelId);
				if(uiEle instanceof UILayoutPanel){
					UILayoutPanel panel = (UILayoutPanel)uiEle;
					if("top".equals(direction)){
						panel.setTopBorder(borderstyle);
					}else if("bottom".equals(direction)){
						panel.setBottomBorder(borderstyle);
					}else if("left".equals(direction)){
						panel.setLeftBorder(borderstyle);
					}else if("right".equals(direction)){
						panel.setRightBorder(borderstyle);
					}
					
				}
			}
			RequestLifeCycleContext.get().setPhase(oriPhase);
		}
		AppSession.current().getAppContext().closeWinDialog();
	}

}
