package xap.lui.core.vos;

import xap.mw.coreitf.d.FDateTime;

public class LuiFileVO {

	/**
	 * 
	 */
	
	public static String OAINFO = "oa_info";
	
	/** 主键 **/
	private String pk_luifile;
	/** 文件名 **/
	private String filename;
	/** 显示名称 **/
	private String displayname;
	/** 创建者 **/
	private String creator;
	private String pk_billtype;
	private String pk_billitem;
	private String pk_group;
	/** 文件类型 **/
	private String filetypo;
	/** 文件大小 **/
	private Long   filesize;
	
//	private FBoolean dr;
	private java.lang.Integer dr = 0;
	private FDateTime ts;
	/** 创建时间 **/
	private FDateTime creattime;
	/** 最后修改时间 **/
	private FDateTime lastmodifytime;
	/** 最后修改者 **/
	private String lastmodifyer;
	/** 单据新增状态,默认是1；即正常状态；如果是新增初始单据在新增时需将该状态值为0**/
	private String createstatus;
	/**
	 * 扩展项
	 */
	private String ext1;
	private String ext2;
	private String ext3;
	private String ext4;
	private String ext5;
	
	private String filemgr;

	public String getPk_luifile() {
		return pk_luifile;
	}

	public void setPk_luifile(String pk_luifile) {
		this.pk_luifile = pk_luifile;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_billitem() {
		return pk_billitem;
	}

	public void setPk_billitem(String pk_billitem) {
		this.pk_billitem = pk_billitem;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

//	public FBoolean getDr() {
//		return dr;
//	}
//
//	public void setDr(FBoolean dr) {
//		this.dr = dr;
//	}

	public FDateTime getTs() {
		return ts;
	}

	public void setTs(FDateTime ts) {
		this.ts = ts;
	}


	public String getFiletypo() {
		return filetypo;
	}

	public void setFiletypo(String filetypo) {
		this.filetypo = filetypo;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public FDateTime getCreattime() {
		return creattime;
	}

	public void setCreattime(FDateTime creattime) {
		this.creattime = creattime;
	}

	public FDateTime getLastmodifytime() {
		return lastmodifytime;
	}

	public void setLastmodifytime(FDateTime lastmodifytime) {
		this.lastmodifytime = lastmodifytime;
	}

	public String getLastmodifyer() {
		return lastmodifyer;
	}

	public void setLastmodifyer(String lastmodifyer) {
		this.lastmodifyer = lastmodifyer;
	}
	
	public String getDisplayname() {
		return (displayname != null && displayname.length() > 0) ? displayname : filename;
	}

	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public String getExt4() {
		return ext4;
	}

	public void setExt4(String ext4) {
		this.ext4 = ext4;
	}

	public String getExt5() {
		return ext5;
	}

	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}

	public void setCreatestatus(String createstatus) {
		this.createstatus = createstatus;
	}

	public String getCreatestatus() {
		return createstatus;
	}

	public void setFilemgr(String filemgr) {
		this.filemgr = filemgr;
	}

	public String getFilemgr() {
		return filemgr;
	}
	
	/**
	 * 属性dr的Getter方法.属性名：dr
	 * 创建日期:
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	
}
