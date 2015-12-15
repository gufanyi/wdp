package xap.lui.core.builder;

import java.util.Iterator;
import xap.lui.core.comps.CodeTreeLevel;
import xap.lui.core.comps.TreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartComps;



public class CodeRuleAdjuster implements IRuntimeAdjuster {

	@Override
	public void adjust(PagePartMeta pm) {
		//为编码树对应的Dataset设置标识
		processCodeRuleDataset(pm);
	}

	/**
	 * 如果是编码树，则为此Dataset设置标识，以便于每次输出到前台之前，转换成PK树
	 */
	protected void processCodeRuleDataset(PagePartMeta pm) {
		ViewPartMeta[] widgets = pm.getWidgets();
		for (int i = 0; i < widgets.length; i++) {
			ViewPartMeta widget = widgets[i];
			ViewPartComps comps = widget.getViewComponents();
			Iterator<WebComp> cit = comps.getComponents().iterator();
			while(cit.hasNext())
			{
				WebComp comp = cit.next();
				if(comp instanceof TreeViewComp){
					TreeViewComp tree = (TreeViewComp) comp;
					TreeLevel level = tree.getTopLevel();
					if(level != null)
						setRuleSignByLevelType(widget, level);
				}
			}	
		}
	}

	protected void setRuleSignByLevelType(ViewPartMeta widget, TreeLevel level) {
		if(level instanceof CodeTreeLevel){
			Dataset ds = widget.getViewModels().getDataset(level.getDataset());
			ds.setExtendAttribute(Dataset.CODE_LEVEL_CLAZZ, CodeRuleDsUtil.class.getName());
			CodeTreeLevel treeLevel = (CodeTreeLevel) level;
			String codeRule = treeLevel.getCodeRule();
			String codeField = treeLevel.getCodeField();
			String keyField =  treeLevel.getMasterField();
			
			String parentPkField = treeLevel.getRecursiveParentField();
			if(parentPkField == null)
				parentPkField = Dataset.PARENT_PK_COLUMN;
			ds.setExtendAttribute(Dataset.CODE_LEVEL_CODEFIELD, codeField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_PPK, parentPkField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_PK, keyField);
			ds.setExtendAttribute(Dataset.CODE_LEVEL_RULE, codeRule);
			
			if(ds.getField(parentPkField) == null){
				Field field = new Field(parentPkField);
				ds.addField(field);
			}
		}
		TreeLevel clevel = level.getChildTreeLevel();
		if(clevel != null)
			setRuleSignByLevelType(widget, clevel);
	}
}
