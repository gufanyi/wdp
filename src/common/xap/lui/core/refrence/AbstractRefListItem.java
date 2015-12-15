package xap.lui.core.refrence;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public abstract class AbstractRefListItem implements Serializable {

	private static final long serialVersionUID = 1116684011483504195L;
	private List<Object> record = null;
	int codeIndex = 0;
	int nameIndex = 0;

	public String getCode() {
		if (getRecord() != null) {
			Object code = getRecord().get(codeIndex);
			return (code == null ? null : code.toString());
		}
		return null;
	}

	public String getName() {
		if (getRecord() != null) {
			Object name = getRecord().get(nameIndex);
			return (name == null ? null : name.toString());
		}
		return null;
	}

	public AbstractRefListItem(List<Object> record, int codeIndex, int nameIndex) {

		this.record = record;
		this.codeIndex = codeIndex;
		this.nameIndex = nameIndex;
	}

	public List<Object> getRecord() {
		return record;
	}

	public void setRecord(Vector record) {
		this.record = record;
	}

	public String toString() {
		// TODO Auto-generated method stub
		return getShowName();
	}

	public abstract String getShowName();

}
