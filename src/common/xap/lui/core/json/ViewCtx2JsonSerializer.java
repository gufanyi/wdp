package xap.lui.core.json;
import java.util.ArrayList;
import java.util.List;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.IDetachable;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EventContextConstant;
import xap.lui.core.context.BaseContext;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.IRefDataset;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import com.alibaba.fastjson.JSONObject;
public class ViewCtx2JsonSerializer implements IObject2JsonSerializer<ViewPartContext> {
	@Override
	public JSONObject serialize(ViewPartContext ctx) {
		ViewPartMeta widget =  ctx.getView();
		com.alibaba.fastjson.JSONObject viewJsonObj = new com.alibaba.fastjson.JSONObject();
		viewJsonObj.put("id", widget.getId());
		if (widget.isCtxChanged()) {
			viewJsonObj.put(EventContextConstant.context, widget.getContext());
		}
		JSONObject comsJsonObj = this.genCompsJsonObj(widget);
		if (comsJsonObj != null) {
			viewJsonObj.put("componets", comsJsonObj);
		}
		viewJsonObj.put("datasets", this.addModels(widget));
		return viewJsonObj;
	}
	private void sortDss(Dataset ds, DatasetRelations dsRels, ViewPartMeta widget, List<Dataset> dsList) {
		if (dsList.contains(ds)) {
			return;
		}
		DatasetRelation[] masterRels = dsRels.getDsRelations(ds.getId());
		if (masterRels != null && masterRels.length > 0) {
			for (int i = 0; i < masterRels.length; i++) {
				DatasetRelation dr = masterRels[i];
				Dataset detailDs = widget.getViewModels().getDataset(dr.getDetailDataset());
				sortDss(detailDs, dsRels, widget, dsList);
			}
		}
		dsList.add(ds);
	}
	protected List<JSONObject> addModels(ViewPartMeta widget) {
		Dataset[] dss = widget.getViewModels().getDatasets();
		DatasetRelations dsRels = widget.getViewModels().getDsrelations();
		//过滤数据集
		if (dsRels != null) {
			List<Dataset> dsList = new ArrayList<Dataset>();
			for (int i = 0; i < dss.length; i++) {
				if (dss[i] instanceof IRefDataset)
					continue;
				sortDss(dss[i], dsRels, widget, dsList);
			}
			dss = dsList.toArray(new Dataset[0]);
		}
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			if (ds.isCtxChanged()) {
				JSONObject dsJsonObj = new Dataset2JsonSerializer(ds).serialize(ds);
				list.add(dsJsonObj);
			}
			ds.detach();
		}
		return list;
	}
	protected JSONObject genCompsJsonObj(ViewPartMeta widget) {
		JSONObject object = null;
		WebComp[] comps = widget.getViewComponents().getComps();
		if (comps != null) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = 0; i < comps.length; i++) {
				WebComp comp = comps[i];
				if (comp.isCtxChanged()) {
					BaseContext ctx = comp.getContext();
					if (ctx != null) {
						JSONObject compJsonObj = new JSONObject();
						compJsonObj.put("id", comp.getId());
						compJsonObj.put(EventContextConstant.context, ctx);
						list.add(compJsonObj);
					}
					comp.setCtxChanged(false);
				}
				if (comp instanceof IDetachable) {
					((IDetachable) comp).detach();
				}
			}
			if (list.size() != 0) {
				if (object == null) {
					object = new JSONObject();
				}
				object.put("generalcomps", list);
			}
		}
		ContextMenuComp[] contextMenus = widget.getViewMenus().getContextMenus();
		if (contextMenus != null && contextMenus.length > 0) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = 0; i < contextMenus.length; i++) {
				ContextMenuComp ctxMenu = contextMenus[i];
				if (ctxMenu.isCtxChanged()) {
					BaseContext ctx = ctxMenu.getContext();
					if (ctx != null) {
						JSONObject menuJsonObj = new JSONObject();
						menuJsonObj.put("id", ctxMenu.getId());
						menuJsonObj.put(EventContextConstant.context, ctxMenu.getContext());
						list.add(menuJsonObj);
						ctxMenu.setCtxChanged(false);
					}
				}
			}
			if (list.size() != 0) {
				if (object == null) {
					object = new JSONObject();
				}
				object.put("contextmenus", list);
			}
		}
		MenubarComp[] menubars = widget.getViewMenus().getMenuBars();
		if (menubars != null && menubars.length > 0) {
			List<JSONObject> list = new ArrayList<JSONObject>();
			for (int i = 0; i < menubars.length; i++) {
				MenubarComp menubar = menubars[i];
				if (menubar.isCtxChanged()) {
					JSONObject menubarJsonObj = new JSONObject();
					menubarJsonObj.put("id", menubar.getId());
					menubarJsonObj.put(EventContextConstant.context, menubar.getContext());
					list.add(menubarJsonObj);
					menubar.setCtxChanged(false);
				}
			}
			if (list.size() != 0) {
				if (object == null) {
					object = new JSONObject();
				}
				object.put("menubars", list);
			}
		}
		return object;
	}
}
