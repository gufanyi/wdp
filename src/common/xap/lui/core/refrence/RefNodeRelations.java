package xap.lui.core.refrence;

import java.util.HashMap;
import java.util.Map;
import xap.lui.core.comps.LuiElement;


/**
 * DataSetRelation的集合对象
 * 
 * @author dengjt
 *
 */
public class RefNodeRelations extends LuiElement{
	
	private static final long serialVersionUID = 1L;
	
	private Map<String, RefNodeRelation> refnodeRelations = new HashMap<String, RefNodeRelation>();
	
	public Map<String, RefNodeRelation> getRefnodeRelations() {
		return refnodeRelations;
	}

	public void setRefnodeRelations(Map<String, RefNodeRelation> refnodeRelations) {
		this.refnodeRelations = refnodeRelations;
	}

	/**
	 * 添加数据集关系对象
	 * @param relation
	 */
	public void addRefNodeRelation(RefNodeRelation relation)
	{
		this.refnodeRelations.put(relation.getId(), relation);
	}

	public void removeRefNodeRelation(String relId){
		refnodeRelations.remove(relId);
	}
	
	public RefNodeRelation getRefNodeRelation(String relationId) {
		return refnodeRelations.get(relationId);
	}

}
