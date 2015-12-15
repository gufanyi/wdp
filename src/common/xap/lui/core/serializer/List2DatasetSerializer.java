package xap.lui.core.serializer;

import java.util.List;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.PageData;


/**
 * Vector到ds的序列化器
 * @author gd 2010-2-24
 * @version NC6.0
 * @since NC6.0
 */
public class List2DatasetSerializer{
	
	public void serialize(List<List<Object>> v, Dataset ds) {
		serialize(null,v,ds);
	}

	public void serialize(PaginationInfo pInfo, List<List<Object>> v, Dataset ds) {
		if(v != null)
		{
			if(ds != null && pInfo!=null)
				ds.setPaginationInfo(pInfo);
//			ds.setRowSetChanged(true);
			PageData pageData = ds.getCurrentPageData();
			pageData.clear();
//			for(int i = 0; i < v.size(); i++)
//			{
//				Row row = ds.getEmptyRow();
//				List<Object> data = v.get(i);
//				pageData.addRow(row);
//				for (int j = 0; j < ds.getFieldCount(); j++) {
//					row.setValue(j, data.get(j));
//				}
//			}
			
			if(pInfo != null && v != null && v.size() > pInfo.getPageSize() && pInfo != null && pInfo.getPageSize() != -1){
				for (int i = 0; i < pInfo.getPageCount(); i++) {
					for (int j = i * pInfo.getPageSize(); j < pInfo.getPageSize() * (i + 1) && j < v.size(); j++) {
						PageData pageDatanew = ds.getCurrentIndexPageData(i);
						if(pageDatanew == null){
							pageDatanew = new PageData(i);
							ds.addPageData(i, pageDatanew);
						}
						Row row = ds.getEmptyRow();
						pageDatanew.addRow(row);
						for (int k = 0; k < ds.getFieldCount() && k < v.get(j).size(); k++) {
							row.setValue(k, v.get(j).get(k));
						}
					}
				}
				
			}
			else{
				if (v != null) {
					for (int i = 0; i < v.size(); i++) {
						Row row = ds.getEmptyRow();
						pageData.addRow(row);
						for (int j = 0; j < ds.getFieldCount() && j < v.get(i).size(); j++) {
							row.setValue(j, v.get(i).get(j));
						
						}
					}
				}
				
			}
		}
	}
}
