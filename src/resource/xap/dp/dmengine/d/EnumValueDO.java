package xap.dp.dmengine.d;

import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.d.FBoolean;

public class EnumValueDO extends BaseDO {

	private static final long serialVersionUID = -9170653278660503635L;
	public static final String ID_ENUMVALUE = "Id_enumvalue";
	public static final String ID = "Id";
	public static final String SEQNO = "Seqno";
	public static final String NAME = "Name";
	public static final String VALUE = "Value";
	public static final String DESCRIPTION = "Description";
	public static final String CODE = "Code";
	public static final String RESID = "Resid";
	public static final String ISSYSTEM = "Issystem";
	public static final String ISHIDE = "Ishide";
	public static final String VERSIONTYPE = "Versiontype";
	public static final String INDUSTRY = "Industry";
	public static final String ClassName = "classname";
	public static final String ClassDisplayName = "classdisplayname";
	public static final String DS = "Ds";
	public static final String SV = "Sv";

	public String getClass_Name() {
		return ((String) getAttrVal("classname"));
	}

	public void setClass_Name(String classname) {
		setAttrVal("classname", classname);
	}

	public String getClass_Display_Name() {
		return ((String) getAttrVal("classdisplayname"));
	}

	public void setClass_Display_Name(String classdisplayname) {
		setAttrVal("classdisplayname", classdisplayname);
	}

	public String getId_enumvalue() {
		return ((String) getAttrVal("Id_enumvalue"));
	}

	public void setId_enumvalue(String Id_enumvalue) {
		setAttrVal("Id_enumvalue", Id_enumvalue);
	}

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

	public String getValue() {
		return ((String) getAttrVal("Value"));
	}

	public void setValue(String Value) {
		setAttrVal("Value", Value);
	}

	public String getDescription() {
		return ((String) getAttrVal("Description"));
	}

	public void setDescription(String Description) {
		setAttrVal("Description", Description);
	}

	public String getCode() {
		return ((String) getAttrVal("Code"));
	}

	public void setCode(String Code) {
		setAttrVal("Code", Code);
	}

	public String getResid() {
		return ((String) getAttrVal("Resid"));
	}

	public void setResid(String Resid) {
		setAttrVal("Resid", Resid);
	}

	public FBoolean getIssystem() {
		return ((FBoolean) getAttrVal("Issystem"));
	}

	public void setIssystem(FBoolean Issystem) {
		setAttrVal("Issystem", Issystem);
	}

	public FBoolean getIshide() {
		return ((FBoolean) getAttrVal("Ishide"));
	}

	public void setIshide(FBoolean Ishide) {
		setAttrVal("Ishide", Ishide);
	}

	public Integer getVersiontype() {
		return ((Integer) getAttrVal("Versiontype"));
	}

	public void setVersiontype(Integer Versiontype) {
		setAttrVal("Versiontype", Versiontype);
	}

	public String getIndustry() {
		return ((String) getAttrVal("Industry"));
	}

	public void setIndustry(String Industry) {
		setAttrVal("Industry", Industry);
	}

	@Override
	public String getPKFieldName() {
		return "Id_enumvalue";
	}

	@Override
	public String getTableName() {
		return "dm_enumvalue";
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