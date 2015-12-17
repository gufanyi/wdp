package xap.lui.core.refrence;

import java.io.Serializable;
import java.util.List;

public class RefResultSet    implements Serializable {
	private static final long serialVersionUID = 4362546800597354571L;
	public int totalCount;
	public List<List<Object>> data;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<List<Object>> getData() {
		return data;
	}
	public void setData(List<List<Object>> data) {
		this.data = data;
	}
}
