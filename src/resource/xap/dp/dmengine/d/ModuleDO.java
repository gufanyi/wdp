package xap.dp.dmengine.d;

import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.d.FBoolean;

public class ModuleDO extends BaseDO {
	private static final long serialVersionUID = 1L;

	public static final String ID= "Id";
	public static final String NAME= "Name";
	public static final String DISPLAYNAME= "Displayname";
	public static final String DESCRIPTION= "Description";
	public static final String PARENTID= "Parentid";
	public static final String ISACTIVE= "Isactive";
	public static final String EXTTAG= "Exttag";
	public static final String RESID= "Resid";
	public static final String RESMODULE= "Resmodule";
	public static final String VERSIONTYPE= "Versiontype";
	public static final String CREATEDBY= "Createdby";
	public static final String CREATEDTIME= "Createdtime";
	public static final String MODIFIEDBY= "Modifiedby";
	public static final String MODIFIEDTIME= "Modifiedtime";
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
	
	public String getParentid() {
		return ((String) getAttrVal("Parentid"));
	}	
	public void setParentid(String Parentid) {
		setAttrVal("Parentid", Parentid);
	}
	
	public FBoolean getIsactive() {
		return ((FBoolean) getAttrVal("Isactive"));
	}	
	public void setIsactive(FBoolean Isactive) {
		setAttrVal("Isactive", Isactive);
	}
	
	public String getExttag() {
		return ((String) getAttrVal("Exttag"));
	}	
	public void setExttag(String Exttag) {
		setAttrVal("Exttag", Exttag);
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
	

	@Override
	public String getPKFieldName() {
		return "Id";
	}
	
	@Override
	public String getTableName() {	  
		return "dm_module";
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