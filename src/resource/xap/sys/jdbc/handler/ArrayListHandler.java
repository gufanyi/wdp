package xap.sys.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArrayListHandler extends BaseHandler {
    private static final long serialVersionUID = 1L;

    public Object processRs(ResultSet rs) throws SQLException {

	List result = new ArrayList();

	while (rs.next()) {
	    result.add(HandlerUtils.toArray(rs));
	}
	return result;
    }

}
