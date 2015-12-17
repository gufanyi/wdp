package xap.mw.core.data;

import java.io.Serializable;

public abstract class BaseDO implements Serializable {
	private static final long serialVersionUID = 8545363400279851414L;
	protected int status = DOStatus.UNCHANGED;

	public BaseDO() {
	}

	public abstract String getPkVal();

	public abstract String getTableName();

	public abstract Object getAttrVal(String attrName);

	public abstract void setAttrVal(String attrName, Object attrVal);

	public abstract String[] getAttrNames();

	public abstract String getPKFieldName();

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPkVal(String pkVal) {
	}

}