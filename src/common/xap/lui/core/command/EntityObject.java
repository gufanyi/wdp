package xap.lui.core.command;

/**
 * 业务接口映射接口
 * @author wujd
 *
 */
public interface EntityObject {
	
	static final String ATTRIBUTE_ID = "id";
	
	static final String ATTRIBUTE_PID = "pid";

	static final String ATTRIBUTE_PKORG = "pk_org";

	static final String ATTRIBUTE_CODE = "code";
	
	static final String ATTRIBUTE_NAME = "name";
	
	static final String ATTRIBUTE_PKGROUP = "pk_group";


	
	Object getId();
	Object getPId();
	Object getPk_org();
	Object getCode();
	Object getName();
	Object getPk_group();
}
