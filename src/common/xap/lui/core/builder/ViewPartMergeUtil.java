package xap.lui.core.builder;

import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.DataModels;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.RefNodeRelations;

/**
 * 片段组合工具，用来将不同来源的片段按照规则组合一起
 */
public class ViewPartMergeUtil {
	public static void merge(ViewPartMeta targetWidget, ViewPartMeta sourceWidget) {
		targetWidget.setFrom(sourceWidget.getFrom());
		//不以本地TS为准
		sourceWidget.getExtendMap().remove(ViewPartMeta.MODIFY_TS);
		sourceWidget.getExtendMap().remove(ViewPartMeta.UNIQUE_TS);
		sourceWidget.getExtendMap().remove(ViewPartMeta.UNIQUE_ID);
		targetWidget.getExtendMap().putAll(sourceWidget.getExtendMap());
	
		ViewPartComps viewComponents = sourceWidget.getViewComponents();
		mergeViewComponents(targetWidget.getViewComponents(), viewComponents);
		
		DataModels viewModel = sourceWidget.getViewModels();
		mergeViewModel(targetWidget.getViewModels(), viewModel);
		
		ViewPartMenus viewMenus = sourceWidget.getViewMenus();
		mergeViewMenus(targetWidget.getViewMenus(), viewMenus);
	}
	
	private static void mergeViewMenus(ViewPartMenus targetMenus, ViewPartMenus sourceMenus) {
		ContextMenuComp[] menus = sourceMenus.getContextMenus();
		for (ContextMenuComp contextMenuComp : menus) {
			targetMenus.addContextMenu(contextMenuComp);
		}
	}

	private static void mergeViewComponents(ViewPartComps targetComps, ViewPartComps sourceComps){
		
		WebComp[] components = sourceComps.getComps();
		if(components != null && components.length > 0){
			for(WebComp component: components) {
				if(component.getConfType().equals(LuiElement.CONF_ADD))
					targetComps.addComponent(component);
				else if(component.getConfType().equals(LuiElement.CONF_DEL)){
					if(targetComps.getComponents() != null)
						targetComps.getComponents().remove(component.getId());
				}
				else if(component.getConfType().equals(LuiElement.CONF_REF)){
					WebComp sourceComp = targetComps.getComponent(component.getId());
					if(sourceComp == null)
						throw new LuiRuntimeException("merge component对象时出错，找不到{0}"+component.getId());
					sourceComp.mergeProperties(component);
				}
			}
		}
	}
	
	private static void mergeViewModel(DataModels targetModels, DataModels sourceModels){
		Dataset[] dss = sourceModels.getDatasets();
		if(dss.length > 0){
			for (int i = 0; i < dss.length; i++) {
				Dataset ds = dss[i];
				if(ds.getConfType().equals(LuiElement.CONF_ADD))
					targetModels.addDataset(ds);
				else if(ds.getConfType().equals(LuiElement.CONF_DEL)){
					targetModels.removeDataset(ds.getId());
				}
				else if(ds.getConfType().equals(LuiElement.CONF_REF)){
					Dataset sourceDs = targetModels.getDataset(ds.getId());
					if(sourceDs == null)
						throw new LuiRuntimeException("merge dataset对象时出错，找不到"+ds.getId());
					sourceDs.mergeProperties(ds);
				}
			}
		}
		
		DatasetRelations dsRelations = sourceModels.getDsrelations();
		if(dsRelations != null){
			DatasetRelations sourceDsRelations = targetModels.getDsrelations();
			if(sourceDsRelations == null)
				targetModels.setDsrelations(dsRelations);
			else
				targetModels.getDsrelations().addDsRelations(dsRelations);
		}
		
		RefNodeRelations refnodeRels = sourceModels.getRefNodeRelations();
		if(refnodeRels != null){
			//RefNodeRelations sourcedsRefNodeRels = targetModels.getRefNodeRelations();
			targetModels.setRefnodeRelations(refnodeRels);
		}
//		Map<String, PageData> pdMap = sourceModels.getPageDataMap();
//		if(pdMap.size() > 0){
//			Iterator<PageData> it = pdMap.values().iterator();
//			while(it.hasNext()){
//				PageData pd = it.next();
//				if(pd.getConfType().equals(WebElement.CONF_ADD))
//					targetModels.addPageData(pd);
//				else if(pd.getConfType().equals(WebElement.CONF_DEL)){
//					targetModels.getPageDataMap().remove(pd.getId());
//				}
//				else if(pd.getConfType().equals(WebElement.CONF_REF)){
//					PageData sourcePd = targetModels.getPageDataMap().get(pd.getId());
//					if(sourcePd == null)
//						throw new LuiRuntimeException(NCLangRes4VoTransl.getNCLangRes().getStrByID("lui", "LuiPMMergeUtil-000003", null, new String[]{pd.getId()})/*merge pagedata对象时出错，找不到{0}*/);
//					sourcePd.mergeProperties(pd);
//				}
//			}
//		}
		
		IRefNode[] refnodes = sourceModels.getRefNodes();
		if(refnodes != null && refnodes.length > 0){
			for(IRefNode rn: refnodes) {
				if(rn instanceof GenericRefNode){
					GenericRefNode refnode = (GenericRefNode) rn;
					if(refnode.getConfType().equals(LuiElement.CONF_ADD))
						targetModels.addRefNode(refnode);
					else if(refnode.getConfType().equals(LuiElement.CONF_DEL)){
						targetModels.removeRefNode(refnode.getId());
					}
					else if(refnode.getConfType().equals(LuiElement.CONF_REF)){
						GenericRefNode sourceComp = (GenericRefNode) targetModels.getRefNode(refnode.getId());
						if(sourceComp == null)
							throw new LuiRuntimeException("merge refnode对象时出错，找不到");
						sourceComp.mergeProperties(refnode);
					}
				}
			}
		}
	}
}
