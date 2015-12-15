package xap.lui.core.refrence;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.refmodel.BaseRefModel;
import xap.lui.core.refmodel.TreeGridRefModel;
import xap.lui.core.refmodel.TreeRefModel;

public class RefSelfUtil {
	public static IRefModel getRefModel(GenericRefNode refnode) {
		String refCode = refnode.getRefcode();
		IRefModel refModel = getRefModel(refCode);
		return refModel;
	}
	public static IRefModel getRefModel(String refCode) {
		IRefModel refModel = RefPubUtil.getRefModel(refCode);
		if (refModel == null) {}
		return refModel;
	}
	
	public static int getRefType(String refCode) {
		return getRefType(getRefModel(refCode));
	}
	public static int getRefType(IRefModel model) {
		return RefPubUtil.getRefType(model);
	}
	
	public static int getRefType(BaseRefModel model) {
		int refType = IRefConst.GRID;
		if (model instanceof TreeGridRefModel) {
			refType = IRefConst.GRIDTREE;
		} else if (model instanceof TreeRefModel) {
			refType = IRefConst.TREE;
		}
		return refType;
	}

	public static BaseRefModel getDefRefModel(String refcode) {
		String widgetId = (String) LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("widgetId");
		if (widgetId == null || widgetId.equals(""))
			throw new LuiRuntimeException("未获取参照所属widget!");
		PagePartMeta parentPm = LuiRuntimeContext.getWebContext().getParentPageMeta();
		DataModels pViewModel = parentPm.getWidget(widgetId).getViewModels();
		BaseRefModel refModel = pViewModel.getRefModel(refcode);
		return refModel;
	}
}
