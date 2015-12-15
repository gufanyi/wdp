package xap.sys.jdbc.handler;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface RsHandler extends Serializable {
	public Object handleRs(ResultSet rs) throws SQLException;
}
