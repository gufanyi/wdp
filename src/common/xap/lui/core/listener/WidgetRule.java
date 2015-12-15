package xap.lui.core.listener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiObj;
import xap.lui.core.builder.LuiSet;
/**
 * @author guoweic
 * 
 */
@XmlRootElement(name = "ViewPartRule")
@XmlAccessorType(XmlAccessType.NONE)
public class WidgetRule implements Cloneable, Serializable, LuiObj {
	private static final long serialVersionUID = 2083040983378517809L;
	// Widget的ID
	@XmlAttribute
	private String id = "";
	// Dataset提交类型集合
	@XmlElement(name = "DatasetRule")
	private LuiSet<DatasetRule> dsRuleList = new LuiHashSet<DatasetRule>();
	// Tree提交类型集合
	private Map<String, TreeRule> treeRuleMap = null;
	// Grid提交类型集合
	private Map<String, GridRule> gridRuleMap = null;
	private Map<String, FormRule> formRuleMap = null;
	// widget的Tab是否提交
	private boolean tabSubmit = false;
	// widget的Card是否提交
	private boolean cardSubmit = false;
	// widget的Panel是否提交
	private boolean panelSubmit = false;
	public Object clone() {
		WidgetRule widgetRule = null;
		try {
			widgetRule = (WidgetRule) super.clone();
			for (DatasetRule inner : this.dsRuleList) {
				widgetRule.addDsRule((DatasetRule) inner.clone());
			}
			if (treeRuleMap != null) {
				for (String treeId : this.treeRuleMap.keySet()) {
					widgetRule.addTreeRule((TreeRule) this.treeRuleMap.get(treeId).clone());
				}
			}
			if (gridRuleMap != null) {
				for (String gridId : this.gridRuleMap.keySet()) {
					widgetRule.addGridRule((GridRule) this.gridRuleMap.get(gridId).clone());
				}
			}
		} catch (CloneNotSupportedException e) {}
		return widgetRule;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public DatasetRule[] getDatasetRules() {
		return dsRuleList.toArray(new DatasetRule[0]);
	}
	public DatasetRule getDatasetRule(String id) {
		return dsRuleList.find(id);
	}
	/**
	 * 增加Dataset提交类型
	 * 
	 * @param dsRule
	 */
	public void addDsRule(DatasetRule dsRule) {
		this.dsRuleList.add(dsRule);
	}
	/**
	 * 删除Dataset提交规则
	 * 
	 * @param dsRule
	 */
	public void removeDsRule(String dsRuleId) {
		this.dsRuleList.remove(dsRuleId);
	}
	private Map<String, TreeRule> getTreeRuleMap() {
		if (treeRuleMap == null)
			treeRuleMap = new HashMap<String, TreeRule>();
		return treeRuleMap;
	}
	public TreeRule[] getTreeRules() {
		return treeRuleMap == null ? null : treeRuleMap.values().toArray(new TreeRule[0]);
	}
	/**
	 * 增加Tree提交类型
	 * 
	 * @param treeRule
	 */
	public void addTreeRule(TreeRule treeRule) {
		getTreeRuleMap().put(treeRule.getId(), treeRule);
	}
	/**
	 * 删除提交规则
	 * 
	 * @param treeRule
	 */
	public void removeTreeRule(String treeRuleId) {
		getTreeRuleMap().remove(treeRuleId);
	}
	/**
	 * 增加Grid提交类型
	 * 
	 * @param gridRule
	 */
	public void addGridRule(GridRule gridRule) {
		getGridRuleMap().put(gridRule.getId(), gridRule);
	}
	/**
	 * 删除grid提交规则
	 * 
	 * @param gridRule
	 */
	public void removeGridRule(String gridRuleId) {
		getGridRuleMap().remove(gridRuleId);
	}
	/**
	 * 增加Form提交类型
	 * 
	 * @param formRule
	 */
	public void addFormRule(FormRule formRule) {
		getFormRuleMap().put(formRule.getId(), formRule);
	}
	/**
	 * 删除form提交规则
	 * 
	 * @param formRule
	 */
	public void removeFormRule(String formRuleId) {
		getFormRuleMap().remove(formRuleId);
	}
	private Map<String, GridRule> getGridRuleMap() {
		if (gridRuleMap == null)
			gridRuleMap = new HashMap<String, GridRule>();
		return gridRuleMap;
	}
	public GridRule[] getGridRules() {
		return gridRuleMap == null ? null : gridRuleMap.values().toArray(new GridRule[0]);
	}
	public FormRule[] getFormRules() {
		return formRuleMap == null ? null : formRuleMap.values().toArray(new FormRule[0]);
	}
	private Map<String, FormRule> getFormRuleMap() {
		if (formRuleMap == null)
			formRuleMap = new HashMap<String, FormRule>();
		return formRuleMap;
	}
	public boolean isTabSubmit() {
		return tabSubmit;
	}
	public void setTabSubmit(boolean tabSubmit) {
		this.tabSubmit = tabSubmit;
	}
	public boolean isCardSubmit() {
		return cardSubmit;
	}
	public void setCardSubmit(boolean cardSubmit) {
		this.cardSubmit = cardSubmit;
	}
	public boolean isPanelSubmit() {
		return panelSubmit;
	}
	public void setPanelSubmit(boolean panelSubmit) {
		this.panelSubmit = panelSubmit;
	}
}
