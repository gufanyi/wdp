package xap.lui.core.vos;

import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDateTime;

public class FileTypeVO {
	private String pk_filetype;
	private String name;
	private String mime;
	private String edittype;
	private FBoolean iscanflash;
	private String ext2;
	private String ext1;
	private String ext3;
	private String ext4;
	private String ext5;
	private String ext6;
	private String ext7;
	private FDateTime ts;
	private java.lang.Integer dr;
	
	public FileTypeVO() {
		super();
	}
	public void setPk_filetype(String pk_filetype) {
		this.pk_filetype = pk_filetype;
	}

	public String getPk_filetype() {
		return pk_filetype;
	}
	public void setMime(String mime) {
		this.mime = mime;
	}
	public String getMime() {
		return mime;
	}
	public void setEdittype(String edittype) {
		this.edittype = edittype;
	}
	public String getEdittype() {
		return edittype;
	}
	public void setIscanflash(FBoolean iscanflash) {
		this.iscanflash = iscanflash;
	}
	public FBoolean getIscanflash() {
		return iscanflash;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}
	public String getExt4() {
		return ext4;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	public String getExt5() {
		return ext5;
	}
	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}
	public String getExt6() {
		return ext6;
	}
	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}
	public String getExt7() {
		return ext7;
	}
	public void setDr(java.lang.Integer dr) {
		this.dr = dr;
	}
	public java.lang.Integer getDr() {
		return dr;
	}
	public FDateTime getTs() {
		return ts;
	}

	public void setTs(FDateTime ts) {
		this.ts = ts;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	
}
