package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import xap.lui.core.dataset.FieldRelation;

public class MapFieldRelationAdapter extends XmlAdapter<MapFieldRelationAdapter.FieldRelationsMap, Map<String, FieldRelation>> {

	public static class FieldRelationsMap {

		@XmlElement(name = "FieldRelation")
		private FieldRelation[] FieldRelation;

		public FieldRelation[] getFieldRelation() {
			return FieldRelation;
		}

		public void setFieldRelation(FieldRelation[] fieldRelation) {
			FieldRelation = fieldRelation;
		}

		
		
	}

	@Override
	public MapFieldRelationAdapter.FieldRelationsMap marshal(Map<String, FieldRelation> map)
			throws Exception {

		FieldRelation[] Beans = new FieldRelation[map.size()];
		Iterator<Entry<String, FieldRelation>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, FieldRelation> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		FieldRelationsMap rtb = new FieldRelationsMap();
		rtb.setFieldRelation(Beans);
		return rtb;
	}

	@Override
	public Map<String, FieldRelation> unmarshal(FieldRelationsMap v) throws Exception {
		Map<String, FieldRelation> map = new HashMap<String, FieldRelation>();
		for (int i = 0; i < v.getFieldRelation().length; i++) {
			map.put(v.getFieldRelation()[i].getId(), v.getFieldRelation()[i]);
		}
		return map;
	}

}

