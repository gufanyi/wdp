package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import xap.lui.core.listener.DatasetRule;

public class MapDatasetRuleAdapter extends XmlAdapter<MapDatasetRuleAdapter.WidgetRules, Map<String, DatasetRule>> {

	public static class WidgetRules {
		@XmlElement(name="DatasetRule")
		private DatasetRule[] datasetRule;

		public DatasetRule[] getDatasetRule() {
			return datasetRule;
		}

		public void setDatasetRule(DatasetRule[] datasetRule) {
			this.datasetRule = datasetRule;
		}
	}

	@Override
	public MapDatasetRuleAdapter.WidgetRules marshal(Map<String, DatasetRule> map)
			throws Exception {

		DatasetRule[] Beans = new DatasetRule[map.size()];
		Iterator<Entry<String, DatasetRule>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, DatasetRule> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		WidgetRules rtb = new WidgetRules();
		rtb.setDatasetRule(Beans);
		return rtb;
	}

	@Override
	public Map<String, DatasetRule> unmarshal(WidgetRules v) throws Exception {
		Map<String, DatasetRule> map = new HashMap<String, DatasetRule>();
		for (int i = 0; i < v.getDatasetRule().length; i++) {
			map.put(v.getDatasetRule()[i].getId(), v.getDatasetRule()[i]);
		}
		return map;
	}
}
