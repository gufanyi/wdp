package xap.lui.core.util;

import java.util.Iterator;
import java.util.Random;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.refrence.IRefNode;


/**
 * 个性化组合辅助类
 * 
 * @author licza
 *
 */
public class PaComboHelper {
	/**
	 * 处理Combo中的模型控件.
	 * <pre>目前没有看到contextMenu的应用,暂不考虑支持</pre>
	 * @param um
	 * @param widget
	 * @param oriUm
	 * @param oriWidget
	 * @return
	 */
	public static UIElement processCombo(UIPartMeta um, ViewPartMeta widget, UIPartMeta oriUm, ViewPartMeta oriWidget){
		UIElement foo = um.getElement();
		fixCompByUI(foo, widget, oriWidget.getId());
		/**
		 * 处理模型 如数据集,枚举,数据关联关系等
		 */
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.ajax);
		ComboData[]  cds = widget.getViewModels().getComboDatas();
		if(cds != null && cds.length > 0){
			for(ComboData cd : cds){
				oriWidget.getViewModels().addComboData(cd);
			}
		}
		IRefNode[]  rns = widget.getViewModels().getRefNodes();
		if(rns != null && rns.length > 0){
			for(IRefNode rn : rns){
				oriWidget.getViewModels().addRefNode(rn);
			}
		}
		Dataset[] dss = widget.getViewModels().getDatasets();
		if(dss != null && dss.length > 0){
			for(Dataset ds : dss){
				oriWidget.getViewModels().addDataset(ds);
			}
		}
		if(widget.getViewModels().getDsrelations() != null){
			DatasetRelation[] drs = widget.getViewModels().getDsrelations().getDsRelations() ;
			if(drs != null){
				oriWidget.getViewModels().getDsrelations().addDsRelations(widget.getViewModels().getDsrelations());
			} 
		}
		/**
		 * 处理控件
		 */
		WebComp[] comps  = widget.getViewComponents().getComps();
		if(comps != null && comps.length > 0){
			for(WebComp comp : comps){
				oriWidget.getViewComponents().addComponent(comp);
			}
		}
		/**
		 * 兼容MenuBar 
		 */
		MenubarComp[] mbs = widget.getViewMenus().getMenuBars();
		if(mbs != null && mbs.length > 0){
			for(MenubarComp mb : mbs){
				oriWidget.getViewMenus().addMenuBar(mb);
			}
		}
		
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		return foo;
	}
	
	private static String randomString(){
		int i = 10000 - (int) ((new Random()).nextFloat() * 1000);
		return i + "";
		
	}
	/**
	 * 取出UI中的控件，修改名称,并放到widget中
	 * <pre>不对模型的ID修改.一旦在view中拖入Combo.同名的模型将会被Combo中的覆盖.</pre>
	 * @param ele
	 * @param widget
	 * @param viewid
	 */
	private static void fixCompByUI(UIElement ele,ViewPartMeta widget, String viewid) {
		if (ele == null)
			return ;
		String eleid = ele.getId();
		String newId = eleid + randomString();
		ele.setId(newId);
		ele.setViewId(viewid);
		if(ele instanceof UIComponent){
			WebComp comp = null;
			comp = widget.getViewComponents().getComponent(eleid);
			if(comp == null && ele instanceof UIMenubarComp)
				comp = widget.getViewMenus().getMenuBar(eleid);
			if(comp != null)
				comp.setId(newId);
		}
		if(ele instanceof UILayoutPanel){
			fixCompByUI(((UILayoutPanel) ele).getElement(), widget, viewid);
		}
		else if (ele instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)ele).getPanelList().iterator();
			while(it.hasNext()){
				UILayoutPanel panel = it.next();
				fixCompByUI(panel, widget, viewid);
			}
		}
	}
}
