package xap.lui.core.refrence;

import java.util.List;

public class RefDftListItem extends AbstractRefListItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RefDftListItem(List<Object> record, int codeIndex, int nameIndex) {
		super(record, codeIndex, nameIndex);
	}

	@Override
	public String getShowName() {
		String code = getCode();
		String name = getName();

		if (codeIndex == nameIndex) {
			return (name == null ? "" : name);
		}

		return (code == null ? "" : code) + " " + (name == null ? "" : name);
	}

}
