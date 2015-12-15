package xap.lui.core.refrence;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import xap.sys.jdbc.handler.BaseHandler;
public class RefResultHandler extends BaseHandler {
	private static final long serialVersionUID = -1042896084768780338L;
	int beginIndex = -1;
	int endIndex = -1;
	public RefResultHandler(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}
	public List<List<Object>> processRs(ResultSet rs) throws SQLException {
		List<List<Object>> vecs = new ArrayList<List<Object>>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int nColumnCount = rsmd.getColumnCount();
		int count = 0;
		boolean isRollOverResult = !(beginIndex == -1 && endIndex == -1);
		while (rs.next()) {
			if (isRollOverResult) {
				if (count < beginIndex) {
					count++;
					continue;
				}
				List<Object> record = getARecord(rs, rsmd, nColumnCount);
				vecs.add(record);
				count++;
				if (count >= endIndex) {
					break;
				}
			} else {
				List<Object> record = getARecord(rs, rsmd, nColumnCount);
				vecs.add(record);
			}
		}
		return vecs;
	}
	private List<Object> getARecord(ResultSet rs, ResultSetMetaData rsmd, int nColumnCount) throws SQLException {
		List<Object> tmpV = new ArrayList<Object>();
		for (int j = 1; j <= nColumnCount; j++) {
			Object o = rs.getObject(j);
			tmpV.add(o);
		}
		return tmpV;
	}
}
