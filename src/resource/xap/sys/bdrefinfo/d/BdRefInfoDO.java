package xap.sys.bdrefinfo.d;

import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDateTime;

public class BdRefInfoDO extends BaseDO {

	public static final String ID_REFINFO= "Id_refinfo";
	public static final String CODE= "Code";
	public static final String NAME= "Name";
	public static final String REFTYPE= "Reftype";
	public static final String RESIDPATH= "Residpath";
	public static final String RESID= "Resid";
	public static final String MODULENAME= "Modulename";
	public static final String REFCLASS= "Refclass";
	public static final String ISSPECIALREF= "Isspecialref";
	public static final String ISNEEDPARA= "Isneedpara";
	public static final String PARA1= "Para1";
	public static final String PARA2= "Para2";
	public static final String PARA3= "Para3";
	public static final String REFSYSTEM= "Refsystem";
	public static final String RESERV1= "Reserv1";
	public static final String RESERV2= "Reserv2";
	public static final String RESERV3= "Reserv3";
	public static final String METADATATYPENAME= "Metadatatypename";
	public static final String WHEREPART= "Wherepart";
	public static final String DS = "Ds";
	public static final String SV = "Sv";
	
	public String getId_refinfo() {
		return ((String) getAttrVal("Id_refinfo"));
	}	
	public void setId_refinfo(String Id_refinfo) {
		setAttrVal("Id_refinfo", Id_refinfo);
	}
	public String getCode() {
		return ((String) getAttrVal("Code"));
	}	
	public void setCode(String Code) {
		setAttrVal("Code", Code);
	}
	public String getName() {
		return ((String) getAttrVal("Name"));
	}	
	public void setName(String Name) {
		setAttrVal("Name", Name);
	}
	public Integer getReftype() {
		return ((Integer) getAttrVal("Reftype"));
	}	
	public void setReftype(Integer Reftype) {
		setAttrVal("Reftype", Reftype);
	}
	public String getResidpath() {
		return ((String) getAttrVal("Residpath"));
	}	
	public void setResidpath(String Residpath) {
		setAttrVal("Residpath", Residpath);
	}
	public String getResid() {
		return ((String) getAttrVal("Resid"));
	}	
	public void setResid(String Resid) {
		setAttrVal("Resid", Resid);
	}
	public String getModulename() {
		return ((String) getAttrVal("Modulename"));
	}	
	public void setModulename(String Modulename) {
		setAttrVal("Modulename", Modulename);
	}
	public String getRefclass() {
		return ((String) getAttrVal("Refclass"));
	}	
	public void setRefclass(String Refclass) {
		setAttrVal("Refclass", Refclass);
	}
	public FBoolean getIsspecialref() {
		return ((FBoolean) getAttrVal("Isspecialref"));
	}	
	public void setIsspecialref(FBoolean Isspecialref) {
		setAttrVal("Isspecialref", Isspecialref);
	}
	public FBoolean getIsneedpara() {
		return ((FBoolean) getAttrVal("Isneedpara"));
	}	
	public void setIsneedpara(FBoolean Isneedpara) {
		setAttrVal("Isneedpara", Isneedpara);
	}
	public String getPara1() {
		return ((String) getAttrVal("Para1"));
	}	
	public void setPara1(String Para1) {
		setAttrVal("Para1", Para1);
	}
	public String getPara2() {
		return ((String) getAttrVal("Para2"));
	}	
	public void setPara2(String Para2) {
		setAttrVal("Para2", Para2);
	}
	public String getPara3() {
		return ((String) getAttrVal("Para3"));
	}	
	public void setPara3(String Para3) {
		setAttrVal("Para3", Para3);
	}
	public String getRefsystem() {
		return ((String) getAttrVal("Refsystem"));
	}	
	public void setRefsystem(String Refsystem) {
		setAttrVal("Refsystem", Refsystem);
	}
	public String getReserv1() {
		return ((String) getAttrVal("Reserv1"));
	}	
	public void setReserv1(String Reserv1) {
		setAttrVal("Reserv1", Reserv1);
	}
	public String getReserv2() {
		return ((String) getAttrVal("Reserv2"));
	}	
	public void setReserv2(String Reserv2) {
		setAttrVal("Reserv2", Reserv2);
	}
	public String getReserv3() {
		return ((String) getAttrVal("Reserv3"));
	}	
	public void setReserv3(String Reserv3) {
		setAttrVal("Reserv3", Reserv3);
	}
	public String getMetadatatypename() {
		return ((String) getAttrVal("Metadatatypename"));
	}	
	public void setMetadatatypename(String Metadatatypename) {
		setAttrVal("Metadatatypename", Metadatatypename);
	}
	public String getWherepart() {
		return ((String) getAttrVal("Wherepart"));
	}	
	public void setWherepart(String Wherepart) {
		setAttrVal("Wherepart", Wherepart);
	}

	
  
	@Override
	public String getPKFieldName() {
		return "Id_refinfo";
	}
	
	@Override
	public String getTableName() {	  
		return "sys_refinfo";
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