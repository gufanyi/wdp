package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.ExtAttribute;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.exception.LuiBusinessException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.mw.core.data.BaseDO;

public class LuiTreeGridDatasetLoadCmd extends LuiDatasetLoadCmd{

	private String pk_Parent;
	private String pk_Child;
	public static String TREEGRID_DSLOAD_OPER = "treegrid_dsload";
	public static String TREEGRID_DSQUERY_OPER = "treegrid_dsquery";
	public static String OPERATE_STATUS = "OPERATE_STATUS";
	private int pageSize = 10;
	private Dataset dataset=null;
	public LuiTreeGridDatasetLoadCmd(Dataset ds,String pk_Parent,String pk_Child) {
		super(ds);
		dataset=ds;
		this.pk_Parent = pk_Parent;
		this.pk_Child = pk_Child;
	}
	public LuiTreeGridDatasetLoadCmd(String  dsId,String pk_Parent,String pk_Child) {
		super(dsId);
		this.pk_Parent = pk_Parent;
		this.pk_Child = pk_Child;
	}
	protected BaseDO[] queryVOs(PaginationInfo pinfo, BaseDO vo, String wherePart, String orderPart) throws LuiBusinessException {
		pinfo.setPageSize(15);
		if(TREEGRID_DSQUERY_OPER.equals(LuiAppUtil.getAppAttr(OPERATE_STATUS)))
			return super.queryVOs(pinfo,vo,wherePart,orderPart);
		BaseDO[] parentVOs = queryParentVOs(pinfo,vo,wherePart,orderPart);
		int pageCount = 0;
		if((pinfo.getRecordsCount()%pinfo.getPageSize())>=1){
			pageCount=pinfo.getRecordsCount()/pinfo.getPageSize()+1;
		}else{
			pageCount=pinfo.getRecordsCount()/pinfo.getPageSize();
		}
		ExtAttribute attr=new ExtAttribute();
		attr.setKey("pagecount");
		attr.setValue(pageCount);
		if(dataset!=null)
		dataset.addAttribute(attr);
		parentVOs= queryChildren(parentVOs,wherePart,orderPart);
		if(parentVOs!=null&&parentVOs.length>pinfo.getPageSize()){
			pinfo.setPageSize(parentVOs.length);
		}
		pinfo.setPageCount(pageCount==0?1:pageCount);
		return parentVOs;
	}
	
	protected BaseDO[] queryParentVOs(PaginationInfo pinfo, BaseDO vo, String wherePart)throws LuiBusinessException{
		return queryVOs(pinfo, vo, wherePart, null);
	}
	
	private AppSession getCurAppContext() {
		return AppSession.current();
	}
	
	protected BaseDO[] queryParentVOs(PaginationInfo pinfo, BaseDO vo, String wherePart,String orderPart)throws LuiBusinessException{
		if(pk_Parent==null)
			throw new LuiRuntimeException("请设置树表查询父字段");
		StringBuffer buffer = new StringBuffer(" ");
		buffer.append("( isnull(").append(pk_Parent).append(",'~') = '~' OR isnull(").append(pk_Parent)
			  .append(",'~') = '') ");
		if(wherePart == null || StringUtils.isBlank(wherePart))
			wherePart = buffer.toString();
		else wherePart = "("+wherePart+") AND "+buffer.toString();
		BaseDO[] vos = (BaseDO[]) CRUDHelper.getCRUDService().queryVOs(vo, pinfo, wherePart, null, orderPart);
		return vos;
	}
	protected BaseDO[] queryChildren(BaseDO[] parents,String wherePart,String orderPart) throws LuiBusinessException{
		if(parents==null || parents.length==0)
			return null;
		List<BaseDO> list = new ArrayList<BaseDO>();
		while(parents!=null&&parents.length!=0){
			for(BaseDO vo : parents)
				list.add(vo);
			StringBuffer inSqlBuffer = new StringBuffer(pk_Parent).append(" in (");
			int length = parents.length;
			inSqlBuffer.append("'").append(parents[0].getAttrVal(pk_Child)).append("'");
			if(length>1){
				for(int i = 1; i<length;++i)
					inSqlBuffer.append(",'").append(parents[i].getAttrVal(pk_Child)).append("'");
			}
			inSqlBuffer.append(")");
			String whereSql;
			if(wherePart == null || StringUtils.isBlank(wherePart)){
				whereSql = inSqlBuffer.toString();
			}
			else{
				whereSql = "("+wherePart+") AND "+inSqlBuffer.toString();
			}
			parents = queryVOs(parents[0].getClass(),whereSql);
		}
		return list.toArray(new BaseDO [0]);
	}

}
