package xap.dp.dmengine.d;

import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.d.FBoolean;

public class ClassDO extends BaseDO {
	private static final long serialVersionUID = -510057484619688952L;
	public static final String ID = "Id";
	public static final String NAME = "Name";
	public static final String DISPLAYNAME = "Displayname";
	public static final String DESCRIPTION = "Description";
	public static final String CLASSTYPE = "Classtype";
	public static final String FULLCLASSNAME = "Fullclassname";
	public static final String COMPONENTID = "Componentid";
	public static final String EXTTAG = "Exttag";
	public static final String ISMAINCLASS = "Ismainclass";
	public static final String DEFAULTTBLNAME = "Defaulttblname";
	public static final String REFMODELNAME = "Refmodelname";
	public static final String MODINFOCLASSNAME = "Modinfoclassname";
	public static final String BIZITFIMPLCLASSNAME = "Bizitfimplclassname";
	public static final String PARENTCLASSID = "Parentclassid";
	public static final String RETURNTYPE = "Returntype";
	public static final String ISFIXEDLEN = "Isfixedlen";
	public static final String PRECISON = "Precison";
	public static final String ISCREATESQL = "Iscreatesql";
	public static final String ISACTIVE = "Isactive";
	public static final String ISAUTHEN = "Isauthen";
	public static final String ISEXTENDBEAN = "Isextendbean";
	public static final String STEREOTYPE = "Stereotype";
	public static final String USERDEFCLASSNAME = "Userdefclassname";
	public static final String ACCESSORCLASSNAME = "Accessorclassname";
	public static final String INDUSTRY = "Industry";
	public static final String RESID = "Resid";
	public static final String VERSIONTYPE = "Versiontype";
	public static final String CREATEDBY = "Createdby";
	public static final String CREATEDTIME = "Createdtime";
	public static final String MODIFIEDBY = "Modifiedby";
	public static final String MODIFIEDTIME = "Modifiedtime";
	public static final String KEYATTRNAME = "Keyattrname";
	public static final String KEYATTR = "Keyattr";
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

	public String getClasstype() {
		return ((String) getAttrVal("Classtype"));
	}

	public void setClasstype(String Classtype) {
		setAttrVal("Classtype", Classtype);
	}

	public String getFullclassname() {
		return ((String) getAttrVal("Fullclassname"));
	}

	public void setFullclassname(String Fullclassname) {
		setAttrVal("Fullclassname", Fullclassname);
	}

	public String getComponentid() {
		return ((String) getAttrVal("Componentid"));
	}

	public void setComponentid(String Componentid) {
		setAttrVal("Componentid", Componentid);
	}

	public String getExttag() {
		return ((String) getAttrVal("Exttag"));
	}

	public void setExttag(String Exttag) {
		setAttrVal("Exttag", Exttag);
	}

	public FBoolean getIsmainclass() {
		return ((FBoolean) getAttrVal("Ismainclass"));
	}

	public void setIsmainclass(FBoolean Ismainclass) {
		setAttrVal("Ismainclass", Ismainclass);
	}

	public String getDefaulttblname() {
		return ((String) getAttrVal("Defaulttblname"));
	}

	public void setDefaulttblname(String Defaulttblname) {
		setAttrVal("Defaulttblname", Defaulttblname);
	}

	public String getRefmodelname() {
		return ((String) getAttrVal("Refmodelname"));
	}

	public void setRefmodelname(String Refmodelname) {
		setAttrVal("Refmodelname", Refmodelname);
	}

	public String getModinfoclassname() {
		return ((String) getAttrVal("Modinfoclassname"));
	}

	public void setModinfoclassname(String Modinfoclassname) {
		setAttrVal("Modinfoclassname", Modinfoclassname);
	}

	public String getBizitfimplclassname() {
		return ((String) getAttrVal("Bizitfimplclassname"));
	}

	public void setBizitfimplclassname(String Bizitfimplclassname) {
		setAttrVal("Bizitfimplclassname", Bizitfimplclassname);
	}

	public String getParentclassid() {
		return ((String) getAttrVal("Parentclassid"));
	}

	public void setParentclassid(String Parentclassid) {
		setAttrVal("Parentclassid", Parentclassid);
	}

	public String getReturntype() {
		return ((String) getAttrVal("Returntype"));
	}

	public void setReturntype(String Returntype) {
		setAttrVal("Returntype", Returntype);
	}

	public FBoolean getIsfixedlen() {
		return ((FBoolean) getAttrVal("Isfixedlen"));
	}

	public void setIsfixedlen(FBoolean Isfixedlen) {
		setAttrVal("Isfixedlen", Isfixedlen);
	}

	public String getPrecison() {
		return ((String) getAttrVal("Precison"));
	}

	public void setPrecison(String Precison) {
		setAttrVal("Precison", Precison);
	}

	public FBoolean getIscreatesql() {
		return ((FBoolean) getAttrVal("Iscreatesql"));
	}

	public void setIscreatesql(FBoolean Iscreatesql) {
		setAttrVal("Iscreatesql", Iscreatesql);
	}

	public FBoolean getIsactive() {
		return ((FBoolean) getAttrVal("Isactive"));
	}

	public void setIsactive(FBoolean Isactive) {
		setAttrVal("Isactive", Isactive);
	}

	public FBoolean getIsauthen() {
		return ((FBoolean) getAttrVal("Isauthen"));
	}

	public void setIsauthen(FBoolean Isauthen) {
		setAttrVal("Isauthen", Isauthen);
	}

	public FBoolean getIsextendbean() {
		return ((FBoolean) getAttrVal("Isextendbean"));
	}

	public void setIsextendbean(FBoolean Isextendbean) {
		setAttrVal("Isextendbean", Isextendbean);
	}

	public String getStereotype() {
		return ((String) getAttrVal("Stereotype"));
	}

	public void setStereotype(String Stereotype) {
		setAttrVal("Stereotype", Stereotype);
	}

	public String getUserdefclassname() {
		return ((String) getAttrVal("Userdefclassname"));
	}

	public void setUserdefclassname(String Userdefclassname) {
		setAttrVal("Userdefclassname", Userdefclassname);
	}

	public String getAccessorclassname() {
		return ((String) getAttrVal("Accessorclassname"));
	}

	public void setAccessorclassname(String Accessorclassname) {
		setAttrVal("Accessorclassname", Accessorclassname);
	}

	public String getIndustry() {
		return ((String) getAttrVal("Industry"));
	}

	public void setIndustry(String Industry) {
		setAttrVal("Industry", Industry);
	}

	public String getResid() {
		return ((String) getAttrVal("Resid"));
	}

	public void setResid(String Resid) {
		setAttrVal("Resid", Resid);
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

	public String getKeyattrname() {
		return ((String) getAttrVal("Keyattrname"));
	}

	public void setKeyattrname(String Keyattrname) {
		setAttrVal("Keyattrname", Keyattrname);
	}

	public String getKeyattr() {
		return ((String) getAttrVal("Keyattr"));
	}

	public void setKeyattr(String Keyattr) {
		setAttrVal("Keyattr", Keyattr);
	}

	@Override
	public String getPkVal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTableName() {
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

	@Override
	public String getPKFieldName() {
		// TODO Auto-generated method stub
		return null;
	}

}