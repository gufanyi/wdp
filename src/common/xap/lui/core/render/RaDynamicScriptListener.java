package xap.lui.core.render;

import xap.lui.core.cache.PaCache;
import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.ICommand;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.event.ScriptEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;

/**
 * 动态处理前台脚本的监听类,利用工厂方法添加
 * @author wupeng1
 *
 */
public class RaDynamicScriptListener implements IWindowCtrl {

	public void handlerEvent(ScriptEvent event) {
		AppSession  ctx = AppSession.current();
		
		//根据获取操作类型
		String oper = ctx.getParameter("oper");
		
		//利用工厂方法获取cmd并之行
		
		RaParameter rp=	new RaParameter(ctx);
		ICommand cmd = RaCmdFactory.getCmd(oper,rp );
		CmdInvoker.invoke(cmd);
		{
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
			IPaEditorService ipaService = new PaEditorServiceImpl();
			String sessionId =(String) PaCache.getInstance().get("eclipse_sesionId");
			ipaService.setOriPageMeta(rp.getPageMeta().getId(), sessionId, rp.getPageMeta());
			ipaService.setOriUIMeta(rp.getPageMeta().getId(), sessionId, rp.getUiMeta());
			java.util.logging.Logger.getAnonymousLogger().info(rp.getUiMeta().toXml());
			{
				LuiMeta uimeta=PaCache.luiMeta;
				if(uimeta!=null){
					LuiMeta next =new LuiMeta();
					next.setPagePartMeta((PagePartMeta)rp.getPageMeta().clone());
					next.setuIPartMeta(rp.getUiMeta().doClone());
					uimeta.setNext(next);
					next.setPre(uimeta);
					PaCache.luiMeta=next;
					PaCache.nowMeta=next;
				}
			}
			RequestLifeCycleContext.get().setPhase(phase);
		}
		//LuiAppUtil.getCntAppCtx().addExecScript("window.parent.setEditorState();");
		
	}

}
