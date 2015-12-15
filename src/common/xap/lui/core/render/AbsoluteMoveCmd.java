package xap.lui.core.render;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIConstant;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;


/**
 * 移动控件命令，由Editable.js中dragEnd()方法触发
 * 
 * @author liujmc
 */
public class AbsoluteMoveCmd extends AbstractRaCommand {

	String oper;
	
	UIElement uiEle;
	
	public AbsoluteMoveCmd(RaParameter rp ,String oper, UIElement uiEle) {
		super(rp);
		this.oper = oper;
		this.uiEle = uiEle;
	}

	@Override
	public void execute() {
		String[] dropObjIdArr=rp.getCurrentDropObj().split(",");
		String[] dropDsIdArr=rp.getCurrentDropDsId().split(",");
		String[] dropObjType=rp.getCurrentDropObjType().split(",");
		String[] leftArr=rp.getDropX().split(",");
		String[] topArr=rp.getDropY().split(",");
		if(dropObjIdArr!=null && dropObjIdArr.length>0){
			for(int i=0;i<dropObjIdArr.length;i++){
				String compId = dropObjIdArr[i];
				if(compId == null){
					compId=rp.getUiId();
				}
				rp.setCurrentDropDsId(dropDsIdArr[i]);
				rp.setCurrentDropObj(compId);
				rp.setCurrentDropObjType(dropObjType[i]);
				UIElement child = UIElementFinder.findElementById(rp.getUiMeta(),compId);
				((UIComponent)child).setLeft(Integer.parseInt(leftArr[i]));
				((UIComponent)child).setTop(Integer.parseInt(topArr[i]));
			}
		}
		
		
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();		
		//将ajax的状态置为nullstatus
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		RequestLifeCycleContext.get().setPhase(phase);		
	}
	
	public void deleteUIElement(UIPartMeta uiMeta, PagePartMeta pageMeta,UIElement uiEle){
		if (rp.getEleId() == null && rp.getUiId() == null && rp.getSubEleId() == null && rp.getSubuiId() == null)
			return;
		UIElement parent = null;
		parent = UIElementFinder.findParent((UIPartMeta) uiMeta, uiEle);
		removeUIElement(parent, uiEle);
		
		//处理导航
		ParamObject param = new ParamObject();
		if(uiEle != null)
			param.subuiId = uiEle.getId();
		if(parent != null)	
			param.uiId = parent.getId();
		param.type = rp.getType();
		param.widgetId = rp.getWidgetId();
		callServer(param, "delete");		
	}
}
