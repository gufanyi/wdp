package xap.lui.core.dataset;
import java.io.Serializable;
public class PaginationInfo  implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	//每页个数 默认-1代表不分页
	private int pageSize = -1;
	//当前页  从0开始走的是下标
	private int pageIndex = 0;
	//总页数
	private int pageCount = 1;
	//总条数
	private int recordsCount = 0;
		
	public Integer getRecordsCount() {
		return recordsCount;
	}
	//计算总页数和总条数
	public void setRecordsCount(int count) {
		if (count >= 0) {
			this.recordsCount = count;
			if (pageSize > 0) {
				if (recordsCount != 0 && recordsCount % pageSize == 0)
					pageCount = recordsCount / pageSize;
				else
					pageCount = recordsCount / pageSize + 1;
			}
		} else{
			this.recordsCount = -1;
		}
	}
	
	public Integer getPageSize() {
		return this.pageSize;
	}
	
	public void setPageSize(Integer size) {
		if (size > 0) {
			this.pageSize = size;
//			if (recordsCount > 0) {                            TODO  只有在设置总数的时候才设置pageCount
//				if (recordsCount % pageSize == 0)
//					pageCount = recordsCount / pageSize;
//				else
//					pageCount = recordsCount / pageSize + 1;
//			}
		} else{
			this.pageSize = -1;
		}
	}
	
	public Integer getPageIndex() {
		return this.pageIndex;
	}
	
	public void setPageIndex(int index) {
		this.pageIndex = index;
	}
	
	public Integer getPageCount() {
		return this.pageCount;
	}
	
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public Object clone() {
		PaginationInfo p =null;
		return p;
	}
	
	
//	private String pageInfo = null;        分页内容  问老田
//	public String getPageInfo() {
//		return pageInfo;
//	}
//	public void setPageInfo(String pageInfo) {
//		this.pageInfo = pageInfo;
//	}
	
//	private boolean pageClient = false;     是否客户端分页
//	@JSONField(serialize = false)
//	public boolean isPageClient() {
//		return pageClient;
//	}
//	public void setPageClient(boolean pageClient) {
//		this.pageClient = pageClient;
//	}
}
