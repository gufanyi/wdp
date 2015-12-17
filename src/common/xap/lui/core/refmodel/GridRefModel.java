package xap.lui.core.refmodel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.refrence.RefResultHandler;
import xap.lui.core.refrence.RefResultSet;

@XmlRootElement(name="GridRefModel")
@XmlAccessorType(XmlAccessType.NONE)
public class GridRefModel extends BaseRefModel{
	private static final long serialVersionUID = 7902201493963397257L;

	@Override
	public RefResultSet getRefData(PaginationInfo pInfo) {
		String refSql = this.getRefSql();
		List<List<Object>> data = (List<List<Object>>)CRUDHelper.getCRUDService().executeQuery(refSql,pInfo, new RefResultHandler(-1, -1));
		RefResultSet result = new RefResultSet();
		result.setData(data);
		return result;
	}
}
