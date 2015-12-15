package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dao.LuiCRUDService;
import xap.lui.core.dao.LuiCRUDServiceImpl;
import xap.lui.core.dao.PtBaseDAO;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.exception.LuiBusinessException;
import xap.mw.core.data.BaseDO;
import xap.sys.jdbc.facade.DAException;

public abstract class DatasetCmd extends LuiCommand {

	// 分页查询
	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, String orderPart) throws LuiBusinessException {
		LuiCRUDService service = new LuiCRUDServiceImpl();
		BaseDO[] vos = null;
		try {
			vos = (BaseDO[]) service.queryVOs(vo, pinfo, wherePart, null, orderPart);
		} catch (Throwable e) {
			throw new LuiBusinessException(e.getMessage());
		}
		return vos;
	}

	// 普通查询
	protected BaseDO[] queryVOs(Class<?> className, String condition) throws LuiBusinessException {
		try {
			return PtBaseDAO.getIns().queryByCondition(className, condition);
		} catch (DAException e) {
			throw new LuiBusinessException(e.getMessage());
		}
	}

	protected BaseDO[] queryChildVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, boolean isNewMaster) throws LuiBusinessException {
		if (!isNewMaster) {
			BaseDO[] vos =(BaseDO[]) CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, null);
			return vos;
		}
		return null;
	}

	protected BaseDO[] queryChildVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, boolean isNewMaster, String orderPart) throws LuiBusinessException {
		if (!isNewMaster) {
			BaseDO[] vos =(BaseDO[]) CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, orderPart);
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

//	protected void processTabCode(ViewPartMeta widget, Dataset detailDs, BaseDO vo) {
//		Object datasetMetaId = detailDs.getExtendAttributeValue(ExtAttrConstants.DATASET_METAID);
//		if (datasetMetaId != null) {
//			String metaId = datasetMetaId.toString();
//			try {
//				// IBusinessEntity entity =
//				// MDQueryService.lookupMDQueryService().getBusinessEntityByFullName(metaId);
//				// boolean tabCodeItf = false;
//				// if(entity != null)
//				// tabCodeItf =
//				// entity.isImplementBizInterface(LuiTabcodeItf.class.getName());
//				// if(tabCodeItf){
//				// String tabcode = LuiMdUtil.getMdItfAttr(entity,
//				// LuiTabcodeItf.class.getName(), "tabcode");
//				// if(tabcode == null || tabcode.equals(""))
//				// throw new LuiRuntimeException("Dataset:" + detailDs.getId()
//				// +"实现了LuiTabcodeItf业务接口，但没有设置属性对照");
//				// IBodyInfo bodyInfo = (IBodyInfo)
//				// widget.getExtendAttributeValue(ViewPartMeta.BODYINFO);
//				// if(bodyInfo != null){
//				// if(bodyInfo instanceof MultiBodyInfo){
//				// MultiBodyInfo multiBodyInfo = (MultiBodyInfo) bodyInfo;
//				// Map<String, String> tabDsMap = multiBodyInfo.getItemDsMap();
//				// for (Iterator<String> itwd = tabDsMap.keySet().iterator();
//				// itwd.hasNext();) {
//				// String tabId = (String) itwd.next();
//				// if(tabDsMap.get(tabId).equals(detailDs.getId())){
//				// vo.setAttributeValue(tabcode, tabId);
//				// break;
//				// }
//				// }
//				// }
//				// }
//				// }
//			} catch (Exception e1) {
//				LuiLogger.error(e1.getMessage(), e1);
//			}
//		}
//	}
}
