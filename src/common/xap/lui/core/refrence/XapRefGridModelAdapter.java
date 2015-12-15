package xap.lui.core.refrence;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.exception.LuiRuntimeException;

/**
 * grid适配器
 */
public class XapRefGridModelAdapter implements IRefGridModel {

	private xap.sys.appfw.refinfo.IRefGridModel xapRefModel=null;

	public xap.sys.appfw.refinfo.IRefGridModel getXapRefModel() {
		return xapRefModel;
	}

	public void setXapRefModel(xap.sys.appfw.refinfo.IRefGridModel xapRefModel) {
		this.xapRefModel = xapRefModel;
	}

	@Override
	public String[] getAllFields() {
		return xapRefModel.getAllFields();
	}

	@Override
	public int getAllFieldCount() {
		return xapRefModel.getAllFieldCount();
	}

	@Override
	public int getFieldIndex(String filedName) {
		return xapRefModel.getFieldIndex(filedName);
	}

	@Override
	public String[] getShowFieldCode() {
		return xapRefModel.getShowFieldCode();
	}

	@Override
	public String[] getHiddenFieldCode() {
		return xapRefModel.getHiddenFieldCode();
	}

	@Override
	public String[] getShowFieldName() {
		return xapRefModel.getShowFieldName();
	}

	@Override
	public List<String[]> getWhereFieldCode() {
		return xapRefModel.getWhereFieldCode();
	}

	@Override
	public List<String> getWriteFieldCode() {
		return xapRefModel.getWriteFieldCode();
	}

	@Override
	public String[] getBlurFields() {
		return xapRefModel.getBlurFields();
	}

	@Override
	public String getTableName() {
		return xapRefModel.getTableName();
	}

	@Override
	public List<String> getRefTableName() {
		return xapRefModel.getRefTableName();
	}

	@Override
	public String getRefTitle() {
		return xapRefModel.getRefTitle();
	}

	@Override
	public String getPkFieldCode() {
		return xapRefModel.getPkFieldCode();
	}

	@Override
	public String getRefCodeField() {
		return xapRefModel.getRefCodeField();
	}

	@Override
	public String getRefNameField() {
		return xapRefModel.getRefNameField();
	}

	@Override
	public void addWherePart(String wherePart) {
		xapRefModel.addWherePart(wherePart);
	}

	@Override
	public List<String> getWherePart() {
		return xapRefModel.getWherePart();
	}

	@Override
	public List<String> getGroupPart() {
		return xapRefModel.getGroupPart();
	}

	@Override
	public List<String> getOrderPart() {
		return xapRefModel.getOrderPart();
	}

	@Override
	public boolean isUseDataPower() {
		return xapRefModel.isUseDataPower();
	}

	@Override
	public boolean isIncludeBlurPart() {
		return xapRefModel.isIncludeBlurPart();
	}

	@Override
	public boolean isIncludeEnvPart() {
		return xapRefModel.isIncludeEnvPart();
	}

	@Override
	public String afterBuilderSql(String sql) {
		return xapRefModel.afterBuilderSql(sql);
	}

	@Override
	public String value2ShowValue(String value) {
		return xapRefModel.value2ShowValue(value);
	}

	@Override
	public String getPk_group() {
		return xapRefModel.getPk_group();
	}

	@Override
	public void setPk_group(String pk_group) {
		xapRefModel.setPk_group(pk_group);
	}

	@Override
	public String getPk_org() {
		return xapRefModel.getPk_org();
	}

	@Override
	public void setPk_org(String pk_org) {
		xapRefModel.setPk_org(pk_org);
	}

	@Override
	public String getPk_user() {
		return xapRefModel.getPk_user();
	}

	@Override
	public void setPk_user(String pk_user) {
		xapRefModel.setPk_user(pk_user);
	}

	@Override
	public String getRefSql() {
		return xapRefModel.getRefSql();
	}

	@Override
	public RefResultSet getRefData() {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.getRefData();
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	@Override
	public RefResultSet filterRefPks(String[] filterPks) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.filterRefPks(filterPks);
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	@Override
	public RefResultSet filterRefValue(String filterValue) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.filterRefBlurValue(filterValue);
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	@Override
	public RefResultSet filterRefBlurValue(String blurValue) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.filterRefBlurValue(blurValue);
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	@Override
	public int getPageSize() {
		return xapRefModel.getPageSize();
	}

	@Override
	public RefResultSet getRefData(int pageIndex) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.getRefData(pageIndex);
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
		
	}

	@Override
	public RefResultSet filterRefValue(String filterValue, PaginationInfo pInfo) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.filterRefValue(filterValue,pInfo);
			pInfo.setRecordsCount(((Integer)pInfo.getAttrVal("RecordsCount")).intValue());
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		try {
			RefResultSet rtnRef = new RefResultSet();
			xap.sys.appfw.refinfo.RefResultSet refResultSet = xapRefModel.getRefData(pInfo);
			if(pInfo.getAttrVal("RecordsCount")!=null)
					pInfo.setRecordsCount(((Integer)pInfo.getAttrVal("RecordsCount")).intValue());
			BeanUtils.copyProperties(rtnRef,refResultSet);
			return rtnRef;
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new LuiRuntimeException(e+":refResultSet转换错误");
		}
	}

	
	
}
