package xap.lui.core.dataset;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
@XmlAccessorType(XmlAccessType.NONE)
public class FieldRelations implements Serializable {
	private static final long serialVersionUID = 7251502658543636597L;
	// 对应jaxb序列化用
	@SuppressWarnings("unchecked")
	@XmlElement(name = "FieldRelation")
	private LuiSet<FieldRelation> fieldRelationsList = new LuiHashSet<FieldRelation>();
	public void addFieldRelation(FieldRelation rel) {
		this.fieldRelationsList.add(rel);
	}
	public void removeFieldRelation(String relId) {
		this.fieldRelationsList.remove(relId);
	}
	public FieldRelation getFieldRelation(String relationId) {
		return this.fieldRelationsList.find(relationId);
	}
	public FieldRelation[] getFieldRelations() {
		return this.fieldRelationsList.toArray(new FieldRelation[0]);
	}
	public LuiSet<FieldRelation> getFieldRelationsList() {
		return fieldRelationsList;
	}
	public void setFieldRelationsList(LuiSet<FieldRelation> fieldRelationsList) {
		this.fieldRelationsList = fieldRelationsList;
	}
}
