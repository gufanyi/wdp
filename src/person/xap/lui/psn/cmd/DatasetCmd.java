package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.exception.LuiBusinessException;
import xap.mw.core.data.BaseDO;

public abstract class DatasetCmd extends LuiCommand {

	// 分页查询
	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, String orderPart) throws LuiBusinessException {
		BaseDO[] vos = null;
		try {
			vos = (BaseDO[]) CRUDHelper.getCRUDService().getBeansByWherePart(vo.getClass(), wherePart, pinfo, orderPart, null);
		} catch (Throwable e) {
			throw new LuiBusinessException(e.getMessage());
		}
		return vos;
	}

	// 普通查询
	protected BaseDO[] queryVOs(Class<?> className, String condition) throws LuiBusinessException {
		try {
			return  (BaseDO[]) CRUDHelper.getCRUDService().getBeansByWherePart(className, condition);
		} catch (Throwable e) {
			throw new LuiBusinessException(e.getMessage());
		}
	}

	protected BaseDO[] queryChildVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, boolean isNewMaster) throws LuiBusinessException {
		if (!isNewMaster) {
			BaseDO[] vos = (BaseDO[]) CRUDHelper.getCRUDService().getBeansByWherePart(vo.getClass(), wherePart, pinfo,  null, null);
			return vos;
		}
		return null;
	}

	protected BaseDO[] queryChildVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, boolean isNewMaster, String orderPart) throws LuiBusinessException {
		if (!isNewMaster) {
			BaseDO[] vos = (BaseDO[]) CRUDHelper.getCRUDService().getBeansByWherePart(vo.getClass(), wherePart, pinfo,  orderPart,null );
			return vos;
		}
		return null;
	}

	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String wherePart) throws LuiBusinessException {
		return queryVOs(pinfo, vo, wherePart, null);
	}

	protected void postProcessRowSelect(Dataset ds) {
		if (ds.getCurrentPageRowCount() > 0) {
			ds.setRowSelectIndex(0);
		}
	}

	protected String postProcessQueryVO(Dataset ds) {
		return ds.getLastCondition();
	}

	protected String postProcessRowSelectVO(Dataset ds) {
		return ds.getLastCondition();
	}

	protected String postOrderPart(Dataset ds) {
		if (ds.getExtendAttribute("ORDER_PART") != null)
			return (String) ds.getExtendAttribute("ORDER_PART").getValue();
		else
			return null;
	}

	protected void postProcessChildRowSelect(Dataset ds) {
		if (ds.getCurrentPageRowCount() > 0)
			ds.setRowSelectIndex(0);
	}

}
