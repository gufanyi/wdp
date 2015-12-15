package xap.lui.core.format;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDateTime;

public class FormatDocVO {
	private java.lang.String pk_formatdoc;
	private java.lang.String pk_org;
	private java.lang.String code;
	private java.lang.String name;
	private java.lang.String name2;
	private java.lang.String name3;
	private java.lang.String name4;
	private java.lang.String name5;
	private java.lang.String name6;
	private java.lang.String description;
	private FBoolean is_default;
	private java.lang.Object formatdef;
	private java.lang.String exp_number;
	private java.lang.String exp_currency;
	private java.lang.String exp_date;
	private java.lang.String exp_time;
	private java.lang.String exp_address;
	private java.lang.String creator;
	private FDateTime creationtime;
	private java.lang.String modifier;
	private FDateTime modifiedtime;
	private java.lang.Integer dr = 0;
	private FDateTime ts;

	public static final String PK_FORMATDOC = "pk_formatdoc";
	public static final String PK_ORG = "pk_org";
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String NAME2 = "name2";
	public static final String NAME3 = "name3";
	public static final String NAME4 = "name4";
	public static final String NAME5 = "name5";
	public static final String NAME6 = "name6";
	public static final String DESCRIPTION = "description";
	public static final String IS_DEFAULT = "is_default";
	public static final String FORMATDEF = "formatdef";
	public static final String EXP_NUMBER = "exp_number";
	public static final String EXP_CURRENCY = "exp_currency";
	public static final String EXP_DATE = "exp_date";
	public static final String EXP_TIME = "exp_time";
	public static final String EXP_ADDRESS = "exp_address";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";

	private static final Set<String> preDefinedPKs = new HashSet<String>(Arrays.asList(new String[] { "00000000000000000000", "00000000000000000001", "00000000000000000002", "00000000000000000003",
			"00000000000000000004" }));

	private FormatMeta fm;

	public java.lang.String getPk_formatdoc() {
		return pk_formatdoc;
	}

	public void setPk_formatdoc(java.lang.String newPk_formatdoc) {
		this.pk_formatdoc = newPk_formatdoc;
	}

	public java.lang.String getPk_org() {
		return pk_org;
	}

	public void setPk_org(java.lang.String newPk_org) {
		this.pk_org = newPk_org;
	}

	public java.lang.String getCode() {
		return code;
	}

	public void setCode(java.lang.String newCode) {
		this.code = newCode;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String newName) {
		this.name = newName;
	}

	public java.lang.String getName2() {
		return name2;
	}

	public void setName2(java.lang.String newName2) {
		this.name2 = newName2;
	}

	public java.lang.String getName3() {
		return name3;
	}

	public void setName3(java.lang.String newName3) {
		this.name3 = newName3;
	}

	public java.lang.String getName4() {
		return name4;
	}

	public void setName4(java.lang.String newName4) {
		this.name4 = newName4;
	}

	public java.lang.String getName5() {
		return name5;
	}

	public void setName5(java.lang.String newName5) {
		this.name5 = newName5;
	}

	public java.lang.String getName6() {
		return name6;
	}

	public void setName6(java.lang.String newName6) {
		this.name6 = newName6;
	}

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String newDescription) {
		this.description = newDescription;
	}

	public FBoolean getIs_default() {
		return is_default;
	}

	public void setIs_default(FBoolean newIs_default) {
		this.is_default = newIs_default;
	}

	public java.lang.Object getFormatdef() {
		return formatdef;
	}

	public void setFormatdef(java.lang.Object newFormatdef) {
		this.formatdef = newFormatdef;
	}

	public void setExp_number(java.lang.String newExp_number) {
		this.exp_number = newExp_number;
	}

	public void setExp_currency(java.lang.String newExp_currency) {
		this.exp_currency = newExp_currency;
	}

	public void setExp_date(java.lang.String newExp_date) {
		this.exp_date = newExp_date;
	}

	public void setExp_time(java.lang.String newExp_time) {
		this.exp_time = newExp_time;
	}

	public void setExp_address(java.lang.String newExp_address) {
		this.exp_address = newExp_address;
	}

	public java.lang.String getCreator() {
		return creator;
	}

	public void setCreator(java.lang.String newCreator) {
		this.creator = newCreator;
	}

	public FDateTime getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(FDateTime newCreationtime) {
		this.creationtime = newCreationtime;
	}

	public java.lang.String getModifier() {
		return modifier;
	}

	public void setModifier(java.lang.String newModifier) {
		this.modifier = newModifier;
	}

	public FDateTime getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(FDateTime newModifiedtime) {
		this.modifiedtime = newModifiedtime;
	}

	public java.lang.Integer getDr() {
		return dr;
	}

	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	public FDateTime getTs() {
		return ts;
	}

	public void setTs(FDateTime newTs) {
		this.ts = newTs;
	}

	public java.lang.String getParentPKFieldName() {
		return null;
	}

	public java.lang.String getPKFieldName() {
		return "pk_formatdoc";
	}

	public java.lang.String getTableName() {
		return "bd_formatdoc";
	}

	public static java.lang.String getDefaultTableName() {
		return "bd_formatdoc";
	}

	public FormatDocVO() {
		super();
	}

//	public FormatMeta getFm() {
//		if (this.fm == null) {
//			fm = (FormatMeta) StringSerializer.deserializeObjectByXml(getFormatdef().toString());
//		}
//
//		return fm;
//	}
//
//	public void setFm(FormatMeta fm) {
//		if (fm != null) {
//			setFormatdef(StringSerializer.serializeObjectByXml(fm));
//		}
//
//		this.fm = fm;
//	}


//	public String getExp_number() {
//		return this.getFm().getNfm().getExpText();
//	}
//
//	public java.lang.String getExp_currency() {
//		return null;
//	}
//
//	public java.lang.String getExp_date() {
//		return this.getFm().getDfm().getExpText();
//	}
//
//	public java.lang.String getExp_time() {
//		return this.getFm().getTfm().getExpText();
//	}
//
//	public java.lang.String getExp_address() {
//		try {
//			return this.getFm().getAfm().getExpText();
//		} catch (RuntimeException e) {
//			throw new LuiRuntimeException(e.getMessage());
//		}
//	}

}
