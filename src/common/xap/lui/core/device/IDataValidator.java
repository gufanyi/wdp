package xap.lui.core.device;

import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.GridComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.ViewPartMeta;

public interface IDataValidator {

	/**
	 * 验证片段
	 * 
	 * @param ds
	 * @param widget
	 */
	public void validate(Dataset ds, ViewPartMeta widget);

	/**
	 * 验证From
	 * 
	 * @param ds
	 * @param formcomp
	 */
	public void validate(Dataset ds, FormComp formcomp);

	/**
	 * 验证Grid
	 * 
	 * @param ds
	 * @param gridcomp
	 */
	public void validate(Dataset ds, GridComp gridcomp);
	
	/**
	 * 获取要校验的数据行Row集合
	 * @param ds
	 * @return
	 */
	public Row[] getValidatorRows(Dataset ds);

}
