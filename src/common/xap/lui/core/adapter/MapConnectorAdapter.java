package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import xap.lui.core.model.Connector;;

public class MapConnectorAdapter extends XmlAdapter<MapConnectorAdapter.Connectors, Map<String, Connector>> {

	public static class Connectors {

		@XmlElement(name = "Connector")
		private Connector[] Connector;

		public Connector[] getConnector() {
			return Connector;
		}

		public void setConnector(Connector[] connector) {
			Connector = connector;
		}
	}

	@Override
	public MapConnectorAdapter.Connectors marshal(Map<String, Connector> map)
			throws Exception {

		Connector[] Beans = new Connector[map.size()];
		Iterator<Entry<String, Connector>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, Connector> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		Connectors rtb = new Connectors();
		rtb.setConnector(Beans);
		return rtb;
	}

	@Override
	public Map<String, Connector> unmarshal(Connectors v) throws Exception {
		Map<String, Connector> map = new HashMap<String, Connector>();
		if(v.getConnector()==null){
			return map;
		}
		for (int i = 0; i < v.getConnector().length; i++) {
			map.put(v.getConnector()[i].getId(), v.getConnector()[i]);
		}
		return map;
	}

}
