package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import xap.lui.core.listener.WidgetRule;

public class MapViewPartRuleAdapter extends XmlAdapter<MapViewPartRuleAdapter.SubmitRule, Map<String, WidgetRule>> {

	@XmlAccessorType(XmlAccessType.NONE)
	public static class SubmitRule {
		@XmlElement(name="ViewPartRule")
		private WidgetRule[] widgetRuless;

		public WidgetRule[] getWidgetRuless() {
			return widgetRuless;
		}

		public void setWidgetRuless(WidgetRule[] widgetRuless) {
			this.widgetRuless = widgetRuless;
		}



	}

	@Override
	public MapViewPartRuleAdapter.SubmitRule marshal(Map<String, WidgetRule> map)
			throws Exception {

		WidgetRule[] Beans = new WidgetRule[map.size()];
		Iterator<Entry<String, WidgetRule>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, WidgetRule> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		SubmitRule rtb = new SubmitRule();
		rtb.setWidgetRuless(Beans);
		return rtb;
	}

	@Override
	public Map<String, WidgetRule> unmarshal(SubmitRule v) throws Exception {
		Map<String, WidgetRule> map = new HashMap<String, WidgetRule>();
		for (int i = 0; i < v.getWidgetRuless().length; i++) {
			map.put(v.getWidgetRuless()[i].getId(), v.getWidgetRuless()[i]);
		}
		return map;
	}

}
