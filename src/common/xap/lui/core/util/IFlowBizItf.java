package xap.lui.core.util;

import java.util.Date;

/**
 * 流程平台业务接口-获取、回写流程相关信息
 * <li>由业务单据对象元模型实现
 * 
 * @author leijun 2008-3
 * @modifier leijun 2009-3 适配V6多组织
 * 
 * @since 5.5
 */
public interface IFlowBizItf {
	static final String ATTRIBUTE_BILLMAKER = "billmaker";

	static final String ATTRIBUTE_APPROVER = "approver";

	static final String ATTRIBUTE_BILLID = "billid";

	static final String ATTRIBUTE_BILLNO = "billno";

	static final String ATTRIBUTE_PKORG = "pkorg";

	static final String ATTRIBUTE_BUSITYPE = "busitype";

	static final String ATTRIBUTE_APPROVESTATUS = "approvestatus";

	static final String ATTRIBUTE_APPROVENOTE = "approvenote";

	static final String ATTRIBUTE_APPROVEDATE = "approvedate";

	static final String ATTRIBUTE_BILLTYPE = "billtype";
	
	static final String ATTRIBUTE_SRCBILLTYPE = "srcbilltype";
	
	static final String ATTRIBUTE_SRCBILLID = "srcbillid";
	
	static final String ATTRIBUTE_TRANSTYPE = "transtype";
	
	static final String ATTRIBUTE_TRANSTYPEPK = "transtypepk";
	
	//是否修订单据
	static final String ATTRIBUTE_EMEND="emendenum";
	//联查修订审批流时使用
	static final String ATTRIBUTE_BILLVERSIONPK="billversionpk";
	
	
	String getBillVersionPK();
	
	void setBillVersionPK(String billVersionPK);
	
	/**
	 * 
	 * */
	Integer getEmendEnum();
	
	/**
	 * 回写是否修订单据
	 * */
	void setEmendEnum(Integer emendEnum);
	
	/**
	 * 返回交易类型
	 * @return
	 */
	String getTranstype();

	/**
	 * 回写交易类型
	 * @param transtype
	 */
	void setTranstype(String transtype);

	/**
	 * 返回单据类型
	 * @return
	 */
	String getBilltype();

	/**
	 * 回写单据类型
	 * @param billtype
	 */
	void setBilltype(String billtype);

	/**
	 * 返回制单人PK
	 * @return
	 */
	String getBillMaker();

	/**
	 * 返回审批人PK
	 * @return
	 */
	String getApprover();

	/**
	 * 返回单据PK
	 * @return
	 */
	String getBillId();

	/**
	 * 返回单据号
	 * @return
	 */
	String getBillNo();

	/**
	 * 返回组织PK
	 * @return
	 */
	String getPkorg();

	/**
	 * 返回业务类型PK
	 * @return
	 */
	String getBusitype();

	/**
	 * 返回接口属性在单据实体中 对应的数据库字段 
	 * @return
	 */
	String getColumnName(String itfAttrName);

	/**
	 * 返回单据的审批状态
	 * @return
	 */
	Integer getApproveStatus();

	/**
	 * 返回单据的审批批语
	 * @return
	 */
	String getApproveNote();

	/**
	 * 返回单据的审批时间
	 * @return
	 */
	Date getApproveDate();

	/**
	 * 回写单据的审批时间
	 * @return
	 */
	void setApproveDate(Date approveDate);

	/**
	 * 回写单据的审批批语
	 * @return
	 */
	void setApproveNote(String approveNote);

	/**
	 * 回写单据的审批状态
	 * @return
	 */
	void setApproveStatus(Integer iStatus);

	/**
	 * 回写组织PK
	 * @return
	 */
	void setPkorg(String pkorg);

	/**
	 * 回写单据的业务类型
	 * @return
	 */
	void setBusitype(String busitype);

	/**
	 * 回写单据的审批人
	 * @return
	 */
	void setApprover(String approver);

	/**
	 * 回写单据号
	 * @return
	 */
	void setBillNo(String billNo);

	/**
	 * 回写单据PK
	 * @return
	 */
	void setBillId(String billId);

	/**
	 * 回写单据的制单人
	 * @return
	 */
	void setBillMaker(String maker);
	
	String getSourceBillType();
	
	String getSourceBillID();
	
	void setSourceBillType(String srcBillType);
	
	void setSourceBillID(String srcBillId);
	
	String getTranstypePk();
	void setTranstypePk(String transtypePk);
}
