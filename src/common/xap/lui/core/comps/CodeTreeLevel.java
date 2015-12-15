package xap.lui.core.comps;

import xap.lui.core.dataset.Dataset;

/**
 * 编码树Level
 * 编码树Level指树Level所对应的dataset的行数据通过编码决定层次关系。从本质上是一种递归树
 * @author dengjt
 *
 */
public class CodeTreeLevel extends RecursiveTreeLevel {
	
	private static final long serialVersionUID = -2149518856890487561L;
	
	// 编码字段
	private String codeField = "2/2/2";
	
	//编码字段的编码规则
	private String codeRule;
	
	public String getCodeField() {
		return codeField;
	}
	public void setCodeField(String codeField) {
		this.codeField = codeField;
	}
	
	public String getCodeRule() {
		return codeRule;
	}
	public void setCodeRule(String codeRule) {
		this.codeRule = codeRule;
	}
	
	public String getRecursiveField() {
		if(recursiveField != null)
			return recursiveField;
		return getCodeField();
	}
	@Override
	public String getRecursiveParentField() {
		if(recursiveParentField != null)
			return recursiveParentField;
		return Dataset.PARENT_PK_COLUMN;
	}
	
}
