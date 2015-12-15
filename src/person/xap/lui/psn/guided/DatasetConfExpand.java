package xap.lui.psn.guided;

import xap.lui.core.model.ViewPartMeta;
import xap.lui.psn.dsmgr.DatasetConfigController.DatasetConf;

public class DatasetConfExpand extends DatasetConf{

	
	//用于分页
	private String pageSize;
	//是否配置 AfterRowSelect 事件，true为配置，false 为不配置
	private Boolean isDataLoad;//是否配置ds的关系

	private Boolean isAfterSelRowEvent;
	private Boolean isParTabStatus;
	private ViewPartMeta widget;
	private String masterDs;//主表的主键
	private String treePTabDs;//树表表的 主表dsid
	private Boolean isNORelation;//是否配置ds的关系
	
	
	public Boolean getIsDataLoad() {
		return isDataLoad;
	}
	public void setIsDataLoad(Boolean isDataLoad) {
		this.isDataLoad = isDataLoad;
	}
	public Boolean getIsNORelation() {
		return isNORelation;
	}
	public Boolean getIsParTabStatus() {
		return isParTabStatus;
	}
	public void setIsParTabStatus(Boolean isParTabStatus) {
		this.isParTabStatus = isParTabStatus;
	}
	public void setIsNORelation(Boolean isNORelation) {
		this.isNORelation = isNORelation;
	}
	public String getTreePTabDs() {
		return treePTabDs;
	}
	public void setTreePTabDs(String treePTabDs) {
		this.treePTabDs = treePTabDs;
	}
	public String getMasterDs() {
		return masterDs;
	}
	public void setMasterDs(String masterDs) {
		this.masterDs = masterDs;
	}
	private String masterKey;//主表的主键

	public String getMasterKey() {
		return masterKey;
	}
	public void setMasterKey(String masterKey) {
		this.masterKey = masterKey;
	}
	/**
	 * 获取分页大小
	 */
	public String getPageSize() {
		return pageSize;
	}
	/**
	 * 设置分页
	 */
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public Boolean getIsAfterSelRowEvent() {
		return isAfterSelRowEvent;
	}
	/**
	 * 设置是否配置 AfterRowSelect 事件
	 * @param isAfterSelRowEvent 为Boolean值，True为配置，False为不配置
	 */
	public void setIsAfterSelRowEvent(Boolean isAfterSelRowEvent) {
		this.isAfterSelRowEvent = isAfterSelRowEvent;
	}
	public ViewPartMeta getWidget() {
		return widget;
	}
	public void setWidget(ViewPartMeta widget) {
		this.widget = widget;
	}
	
	
}
