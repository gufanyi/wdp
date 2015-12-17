package xap.dp.dmengine.d;

import xap.mw.core.data.BaseDO;

public class ComponentDO extends BaseDO {

	private static final long serialVersionUID = -6019863149161424171L;
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String DISPLAYNAME = "Displayname";
	public static final String DESCRIPTION = "Description";
	public static final String NAMESPACE = "Namespace";
	public static final String MODULEID = "Moduleid";
	public static final String EXTTAG = "Exttag";
	public static final String FROMSRCDMF = "Fromsrcdmf";
	public static final String PRELOAD = "Preload";
	public static final String ISBIZMODEL = "Isbizmodel";
	public static final String INDUSTRYINCREASE = "Industryincrease";
	public static final String INDUSTRY = "Industry";
	public static final String VERSION = "Version";
	public static final String RESID = "Resid";
	public static final String RESMODULE = "Resmodule";
	public static final String VERSIONTYPE = "Versiontype";
	public static final String CREATEDBY = "Createdby";
	public static final String CREATEDTIME = "Createdtime";
	public static final String MODIFIEDBY = "Modifiedby";
	public static final String MODIFIEDTIME = "Modifiedtime";
	public static final String DS = "Ds";
	public static final String SV = "Sv";

	public String getId() {
		return ((String) getAttrVal("Id"));
	}

	public void setId(String Id) {
		setAttrVal("Id", Id);
	}

	public String getName() {
		return ((String) getAttrVal("Name"));
	}

	public void setName(String Name) {
		setAttrVal("Name", Name);
	}

	public String getDisplayname() {
		return ((String) getAttrVal("Displayname"));
	}

	public void setDisplayname(String Displayname) {
		setAttrVal("Displayname", Displayname);
	}

	public String getDescription() {
		return ((String) getAttrVal("Description"));
	}

	public void setDescription(String Description) {
		setAttrVal("Description", Description);
	}

	public String getNamespace() {
		return ((String) getAttrVal("Namespace"));
	}

	public void setNamespace(String Namespace) {
		setAttrVal("Namespace", Namespace);
	}

	public String getModuleid() {
		return ((String) getAttrVal("Moduleid"));
	}

	public void setModuleid(String Moduleid) {
		setAttrVal("Moduleid", Moduleid);
	}

	public String getExttag() {
		return ((String) getAttrVal("Exttag"));
	}

	public void setExttag(String Exttag) {
		setAttrVal("Exttag", Exttag);
	}

	public String getFromsrcdmf() {
		return ((String) getAttrVal("Fromsrcdmf"));
	}

	public void setFromsrcdmf(String Fromsrcdmf) {
		setAttrVal("Fromsrcdmf", Fromsrcdmf);
	}

	public String getPreload() {
		return ((String) getAttrVal("Preload"));
	}

	public void setPreload(String Preload) {
		setAttrVal("Preload", Preload);
	}

	public String getIsbizmodel() {
		return ((String) getAttrVal("Isbizmodel"));
	}

	public void setIsbizmodel(String Isbizmodel) {
		setAttrVal("Isbizmodel", Isbizmodel);
	}

	public String getIndustryincrease() {
		return ((String) getAttrVal("Industryincrease"));
	}

	public void setIndustryincrease(String Industryincrease) {
		setAttrVal("Industryincrease", Industryincrease);
	}

	public String getIndustry() {
		return ((String) getAttrVal("Industry"));
	}

	public void setIndustry(String Industry) {
		setAttrVal("Industry", Industry);
	}

	public String getVersion() {
		return ((String) getAttrVal("Version"));
	}

	public void setVersion(String Version) {
		setAttrVal("Version", Version);
	}

	public String getResid() {
		return ((String) getAttrVal("Resid"));
	}

	public void setResid(String Resid) {
		setAttrVal("Resid", Resid);
	}

	public String getResmodule() {
		return ((String) getAttrVal("Resmodule"));
	}

	public void setResmodule(String Resmodule) {
		setAttrVal("Resmodule", Resmodule);
	}

	public Integer getVersiontype() {
		return ((Integer) getAttrVal("Versiontype"));
	}

	public void setVersiontype(Integer Versiontype) {
		setAttrVal("Versiontype", Versiontype);
	}

	public String getCreatedby() {
		return ((String) getAttrVal("Createdby"));
	}

	public void setCreatedby(String Createdby) {
		setAttrVal("Createdby", Createdby);
	}

	public String getCreatedtime() {
		return ((String) getAttrVal("Createdtime"));
	}

	public void setCreatedtime(String Createdtime) {
		setAttrVal("Createdtime", Createdtime);
	}

	public String getModifiedby() {
		return ((String) getAttrVal("Modifiedby"));
	}

	public void setModifiedby(String Modifiedby) {
		setAttrVal("Modifiedby", Modifiedby);
	}

	public String getModifiedtime() {
		return ((String) getAttrVal("Modifiedtime"));
	}

	public void setModifiedtime(String Modifiedtime) {
		setAttrVal("Modifiedtime", Modifiedtime);
	}

	// @Override
	// public java.lang.String getParentPKFieldName() {
	// return null;
	// }

	@Override
	public String getPKFieldName() {
		return "Id";
	}

	@Override
	public String getTableName() {
		return "dm_component";
	}

	@Override
	public String getPkVal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttrVal(String attrName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttrVal(String attrName, Object attrVal) {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getAttrNames() {
		// TODO Auto-generated method stub
		return null;
	}

}