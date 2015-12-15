
package xap.lui.core.format;
	
import xap.mw.coreitf.d.FDateTime;


public class AddressVO  {
	private java.lang.String pk_address;
	private java.lang.String code;
	private java.lang.String detailinfo;
	private java.lang.String postcode;
	private java.lang.String country;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String vsection;
	private java.lang.String creator;
	private FDateTime creationtime;
	private java.lang.String modifier;
	private FDateTime modifiedtime;
	private java.lang.Integer dr = 0;
	private FDateTime ts;
	private java.lang.Integer dataoriginflag = 0;


	public static final String PK_ADDRESS = "pk_address";
	public static final String CODE = "code";
	public static final String DETAILINFO = "detailinfo";
	public static final String POSTCODE = "postcode";
	public static final String COUNTRY = "country";
	public static final String PROVINCE = "province";
	public static final String CITY = "city";
	public static final String VSECTION = "vsection";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
			
	
	public java.lang.String getPk_address () {
		return pk_address;
	}   
	
	public void setPk_address (java.lang.String newPk_address ) {
	 	this.pk_address = newPk_address;
	} 	  
	
	public java.lang.String getCode () {
		return code;
	}   
	
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	  
	
	public java.lang.String getDetailinfo () {
		return detailinfo;
	}   
	
	public void setDetailinfo (java.lang.String newDetailinfo ) {
	 	this.detailinfo = newDetailinfo;
	} 	  
	
	public java.lang.String getPostcode () {
		return postcode;
	}   
	
	public void setPostcode (java.lang.String newPostcode ) {
	 	this.postcode = newPostcode;
	} 	  
	
	public java.lang.String getCountry () {
		return country;
	}   
	
	public void setCountry (java.lang.String newCountry ) {
	 	this.country = newCountry;
	} 	  
	
	public java.lang.String getProvince () {
		return province;
	}   
	
	public void setProvince (java.lang.String newProvince ) {
	 	this.province = newProvince;
	} 	  
	
	public java.lang.String getCity () {
		return city;
	}   
	
	public void setCity (java.lang.String newCity ) {
	 	this.city = newCity;
	} 	  
	
	public java.lang.String getVsection () {
		return vsection;
	}   
	
	public void setVsection (java.lang.String newVsection ) {
	 	this.vsection = newVsection;
	} 	  
	
	public java.lang.String getCreator () {
		return creator;
	}   
	
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	  
	
	public FDateTime getCreationtime () {
		return creationtime;
	}   
	
	public void setCreationtime (FDateTime newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	  
	
	public java.lang.String getModifier () {
		return modifier;
	}   
	
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	  
	
	public FDateTime getModifiedtime () {
		return modifiedtime;
	}   
	
	public void setModifiedtime (FDateTime newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	  
	
	public java.lang.Integer getDr () {
		return dr;
	}   
	
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	  
	
	public FDateTime getTs () {
		return ts;
	}   
	
	public void setTs (FDateTime newTs ) {
	 	this.ts = newTs;
	}
	
	public java.lang.Integer getDataoriginflag() {
		return dataoriginflag;
	}
	public void setDataoriginflag(java.lang.Integer dataoriginflag) {
		this.dataoriginflag = dataoriginflag;
	}
	
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	
	public java.lang.String getPKFieldName() {
	  return "pk_address";
	}
    
	
	public java.lang.String getTableName() {
		return "bd_address";
	}    
	
	
	public static java.lang.String getDefaultTableName() {
		return "bd_address";
	}    
    
    
     public AddressVO() {
		super();	
	}    
} 


