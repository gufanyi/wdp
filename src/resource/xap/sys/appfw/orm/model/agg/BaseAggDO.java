package xap.sys.appfw.orm.model.agg;

import xap.mw.core.data.BaseDO;

public abstract class BaseAggDO {

	public BaseAggDO() {
		super();
	}

	public abstract BaseDO[] getChildrenDO();

	public abstract BaseDO getParentDO();

	public abstract void setChildrenDO(BaseDO[] children);

	public abstract void setParentDO(BaseDO parent);

	public abstract BaseDO[] getAllChildrenDO();

	public abstract BaseDO[] getChildrenDO(String tabCode);

	public abstract void setChildrenVO(String tabCode, BaseDO[] children);
}
