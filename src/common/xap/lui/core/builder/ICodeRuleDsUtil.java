package xap.lui.core.builder;

import xap.lui.core.dataset.Dataset;

public interface ICodeRuleDsUtil {
	public void buildCodeRuleDataset(Dataset ds, String codeRule, int codeIndex, int pkIndex, int ppkIndex);
}
