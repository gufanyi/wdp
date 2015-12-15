package xap.sys.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapListHandler extends BaseHandler {
	private static final long serialVersionUID = 1L;

	@Override
	public Object processRs(ResultSet rs) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			results.add(HandlerUtils.toMap(rs));
		}
		return results;
	}

}
