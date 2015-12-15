package xap.sys.jdbc.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * 值对象集合处理器，返回一个ArrayList集合，集合中的每�?��元素是一个javaBean,每个javaBean对应结果集合中一行数据，
 * 其中每个JavaBean中的数据映射关系和BeanProcess同理
 */
public class BeanListHandler extends BaseHandler {
	private static final long serialVersionUID = 1L;
	private Class type = null;

	public BeanListHandler(Class type) {
		this.type = type;
	}

	public Object processRs(ResultSet rs) throws SQLException {
		return HandlerUtils.toBeanList(rs, type);
	}
}
