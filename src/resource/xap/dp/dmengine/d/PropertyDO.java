package xap.dp.dmengine.d;

import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.d.FBoolean;

public class PropertyDO extends BaseDO {
	private static final long serialVersionUID = -2299830317287472534L;
	public static final String ID= "Id";
	public static final String SEQNO= "Seqno";
	public static final String NAME= "Name";
	public static final String DISPLAYNAME= "Displayname";
	public static final String DATATYPESTYLE= "Datatypestyle";
	public static final String DATATYPE= "Datatype";
	public static final String REFMODELNAME= "Refmodelname";
	public static final String DESCRIPTION= "Description";
	public static final String CLASSID= "Classid";
	public static final String ISHIDE= "Ishide";
	public static final String ISNULLABLE= "Isnullable";
	public static final String ISREADONLY= "Isreadonly";
	public static final String DEFAULTVALUE= "Defaultvalue";
	public static final String ATTRMAXVALUE= "Attrmaxvalue";
	public static final String ATTRMINVALUE= "Attrminvalue";
	public static final String ATTRLENGTH= "Attrlength";
	public static final String PRECISION= "Precision";
	public static final String ISFIEXEDLEN= "Isfiexedlen";
	public static final String ISCALCULATION= "Iscalculation";
	public static final String ISACTIVE= "Isactive";
	public static final String ISAUTHEN= "Isauthen";
	public static final String ISNOTSERIALIZE= "Isnotserialize";
	public static final String EXTTAG= "Exttag";
	public static final String ISCUSTOMATTR= "Iscustomattr";
	public static final String ISDYNAMIC= "Isdynamic";
	public static final String ISACCESSPOWER= "Isaccesspower";
	public static final String ACCESSGROUP= "Accessgroup";
	public static final String ACCESSORCLASSNAME= "Accessorclassname";
	public static final String VISIBILITY= "Visibility";
	public static final String INDUSTRY= "Industry";
	public static final String RESID= "Resid";
	public static final String VERSIONTYPE= "Versiontype";
	public static final String CREATEDBY= "Createdby";
	public static final String CREATEDTIME= "Createdtime";
	public static final String MODIFIEDBY= "Modifiedby";
	public static final String MODIFIEDTIME= "Modifiedtime";
	public static final String EXTENDTABLE= "Extendtable";
	public static final String REFRELFLDSDATA= "Refrelfldsdata";
	public static final String PROPTYPE= "Proptype";
	public static final String DATAFROM= "Datafrom";
	public static final String DECODE= "Decode";
	public static final String VERSION= "Version";
	public static final String DATAFROMREFINFO= "Datafromrefinfo";
	public static final String CLASSNAME= "Classname";
	public static final String CLASSDISPLAYNAME= "Classdisplayname";
	public static final String CLASSTYPE= "Classtype";
	public static final String FULLCLASSNAME= "Fullclassname";
	public static final String DS = "Ds";
	public static final String SV = "Sv";
	
	public String getId() {
		return ((String) getAttrVal("Id"));
	}	
	public void setId(String Id) {
		setAttrVal("Id", Id);
	}
	public Integer getSeqno() {
		return ((Integer) getAttrVal("Seqno"));
	}	
	public void setSeqno(Integer Seqno) {
		setAttrVal("Seqno", Seqno);
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
	public Integer getDatatypestyle() {
		return ((Integer) getAttrVal("Datatypestyle"));
	}	
	public void setDatatypestyle(Integer Datatypestyle) {
		setAttrVal("Datatypestyle", Datatypestyle);
	}
	public String getDatatype() {
		return ((String) getAttrVal("Datatype"));
	}	
	public void setDatatype(String Datatype) {
		setAttrVal("Datatype", Datatype);
	}
	public String getRefmodelname() {
		return ((String) getAttrVal("Refmodelname"));
	}	
	public void setRefmodelname(String Refmodelname) {
		setAttrVal("Refmodelname", Refmodelname);
	}
	public String getDescription() {
		return ((String) getAttrVal("Description"));
	}	
	public void setDescription(String Description) {
		setAttrVal("Description", Description);
	}
	public String getClassid() {
		return ((String) getAttrVal("Classid"));
	}	
	public void setClassid(String Classid) {
		setAttrVal("Classid", Classid);
	}
	public FBoolean getIshide() {
		return ((FBoolean) getAttrVal("Ishide"));
	}	
	public void setIshide(FBoolean Ishide) {
		setAttrVal("Ishide", Ishide);
	}
	public FBoolean getIsnullable() {
		return ((FBoolean) getAttrVal("Isnullable"));
	}	
	public void setIsnullable(FBoolean Isnullable) {
		setAttrVal("Isnullable", Isnullable);
	}
	public FBoolean getIsreadonly() {
		return ((FBoolean) getAttrVal("Isreadonly"));
	}	
	public void setIsreadonly(FBoolean Isreadonly) {
		setAttrVal("Isreadonly", Isreadonly);
	}
	public String getDefaultvalue() {
		return ((String) getAttrVal("Defaultvalue"));
	}	
	public void setDefaultvalue(String Defaultvalue) {
		setAttrVal("Defaultvalue", Defaultvalue);
	}
	public String getAttrmaxvalue() {
		return ((String) getAttrVal("Attrmaxvalue"));
	}	
	public void setAttrmaxvalue(String Attrmaxvalue) {
		setAttrVal("Attrmaxvalue", Attrmaxvalue);
	}
	public String getAttrminvalue() {
		return ((String) getAttrVal("Attrminvalue"));
	}	
	public void setAttrminvalue(String Attrminvalue) {
		setAttrVal("Attrminvalue", Attrminvalue);
	}
	public Integer getAttrlength() {
		return ((Integer) getAttrVal("Attrlength"));
	}	
	public void setAttrlength(Integer Attrlength) {
		setAttrVal("Attrlength", Attrlength);
	}
	public Integer getPrecision() {
		return ((Integer) getAttrVal("Precision"));
	}	
	public void setPrecision(Integer Precision) {
		setAttrVal("Precision", Precision);
	}
	public FBoolean getIsfiexedlen() {
		return ((FBoolean) getAttrVal("Isfiexedlen"));
	}	
	public void setIsfiexedlen(FBoolean Isfiexedlen) {
		setAttrVal("Isfiexedlen", Isfiexedlen);
	}
	public FBoolean getIscalculation() {
		return ((FBoolean) getAttrVal("Iscalculation"));
	}	
	public void setIscalculation(FBoolean Iscalculation) {
		setAttrVal("Iscalculation", Iscalculation);
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
	public FBoolean getIsnotserialize() {
		return ((FBoolean) getAttrVal("Isnotserialize"));
	}	
	public void setIsnotserialize(FBoolean Isnotserialize) {
		setAttrVal("Isnotserialize", Isnotserialize);
	}
	public String getExttag() {
		return ((String) getAttrVal("Exttag"));
	}	
	public void setExttag(String Exttag) {
		setAttrVal("Exttag", Exttag);
	}
	public FBoolean getIscustomattr() {
		return ((FBoolean) getAttrVal("Iscustomattr"));
	}	
	public void setIscustomattr(FBoolean Iscustomattr) {
		setAttrVal("Iscustomattr", Iscustomattr);
	}
	public FBoolean getIsdynamic() {
		return ((FBoolean) getAttrVal("Isdynamic"));
	}	
	public void setIsdynamic(FBoolean Isdynamic) {
		setAttrVal("Isdynamic", Isdynamic);
	}
	public FBoolean getIsaccesspower() {
		return ((FBoolean) getAttrVal("Isaccesspower"));
	}	
	public void setIsaccesspower(FBoolean Isaccesspower) {
		setAttrVal("Isaccesspower", Isaccesspower);
	}
	public String getAccessgroup() {
		return ((String) getAttrVal("Accessgroup"));
	}	
	public void setAccessgroup(String Accessgroup) {
		setAttrVal("Accessgroup", Accessgroup);
	}
	public String getAccessorclassname() {
		return ((String) getAttrVal("Accessorclassname"));
	}	
	public void setAccessorclassname(String Accessorclassname) {
		setAttrVal("Accessorclassname", Accessorclassname);
	}
	public Integer getVisibility() {
		return ((Integer) getAttrVal("Visibility"));
	}	
	public void setVisibility(Integer Visibility) {
		setAttrVal("Visibility", Visibility);
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
	public String getExtendtable() {
		return ((String) getAttrVal("Extendtable"));
	}	
	public void setExtendtable(String Extendtable) {
		setAttrVal("Extendtable", Extendtable);
	}
	public String getRefrelfldsdata() {
		return ((String) getAttrVal("Refrelfldsdata"));
	}	
	public void setRefrelfldsdata(String Refrelfldsdata) {
		setAttrVal("Refrelfldsdata", Refrelfldsdata);
	}
	public String getProptype() {
		return ((String) getAttrVal("Proptype"));
	}	
	public void setProptype(String Proptype) {
		setAttrVal("Proptype", Proptype);
	}
	public String getDatafrom() {
		return ((String) getAttrVal("Datafrom"));
	}	
	public void setDatafrom(String Datafrom) {
		setAttrVal("Datafrom", Datafrom);
	}
	public String getDecode() {
		return ((String) getAttrVal("Decode"));
	}	
	public void setDecode(String Decode) {
		setAttrVal("Decode", Decode);
	}
	public String getVersion() {
		return ((String) getAttrVal("Version"));
	}	
	public void setVersion(String Version) {
		setAttrVal("Version", Version);
	}
	public String getDatafromrefinfo() {
		return ((String) getAttrVal("Datafromrefinfo"));
	}	
	public void setDatafromrefinfo(String Datafromrefinfo) {
		setAttrVal("Datafromrefinfo", Datafromrefinfo);
	}
	public String getClassname() {
		return ((String) getAttrVal("Classname"));
	}	
	public void setClassname(String Classname) {
		setAttrVal("Classname", Classname);
	}
	public String getClassdisplayname() {
		return ((String) getAttrVal("Classdisplayname"));
	}	
	public void setClassdisplayname(String Classdisplayname) {
		setAttrVal("Classdisplayname", Classdisplayname);
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

  
	@Override
	public String getPKFieldName() {
		return "Id";
	}
	
	@Override
	public String getTableName() {	  
		return "dm_property";
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