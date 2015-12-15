package xap.lui.psn.cmd;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.LuiAppUtil;
import xap.mw.core.data.BaseDO;
import xap.sys.appfw.orm.handle.dataobject.paging.PaginationInfo;


public class QueryCmd extends LuiDatasetLoadCmd{
	
	private String whereSql;
	
	public QueryCmd(String viewId,String dsId,String whereSql){
		super(LuiAppUtil.getCntView().getViewModels().getDataset(dsId));
		this.whereSql = whereSql;
	}
	//在父类中进行数据的查询  放入到DataSet中
	public void excute(){
		super.execute();
	}
	
	//
	protected String postProcessQueryVO(BaseDO vo, Dataset ds) {
	  String where = buildWhere(whereSql);
	  PaginationInfo pageInfo = ds.getPaginationInfo();
	  pageInfo.setPageIndex(0);
	  ds.setLastCondition(where);	
	  return where;
	}
	
	@Override
	protected String postProcessQueryVO(Dataset ds) {
		  String where = buildWhere(whereSql);
		  PaginationInfo pageInfo = ds.getPaginationInfo();
		  pageInfo.setPageIndex(0);
		  ds.setLastCondition(where);	
		  return where;
	}
	protected String buildWhere(String whereSql){
		if(whereSql==null||"".equals(whereSql)){
			whereSql = " 1=1 ";
		  }
		return whereSql;
	}
}
