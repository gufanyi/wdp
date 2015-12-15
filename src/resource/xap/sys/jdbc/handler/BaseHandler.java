package xap.sys.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 抽象结果集处理对象，用于自动关闭ResultSet对象，子类需要实现processResultSet方法
 */
public abstract class BaseHandler implements RsHandler {
	private static final long serialVersionUID = 1L;

	public Object handleRs(ResultSet rs) throws SQLException {
		if (rs == null)
			throw new IllegalArgumentException("resultset parameter can't be null");
		try {
			return processRs(rs);
		} catch (SQLException e) {
			throw new SQLException("the resultsetProcessor error!" + e.getMessage(), e.getSQLState());
		}
	}

	/**
	 * 处理结果集返回需要的对象
	 * 
	 * @param rs
	 *            结果�?
	 * @return �?��的对�?
	 * @throws SQLException如果发生错误
	 */
	public abstract Object processRs(ResultSet rs) throws SQLException;

}
