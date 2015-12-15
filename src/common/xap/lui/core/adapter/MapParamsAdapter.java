package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import xap.lui.core.dataset.Parameter;

public class MapParamsAdapter extends XmlAdapter<MapParamsAdapter.Params, Map<String, Parameter>> {

	public static class Params {

		@XmlElement(name = "Param")
		private Parameter[] Param;

		public Parameter[] getParam() {
			return Param;
		}

		public void setParam(Parameter[] param) {
			Param = param;
		}

	}

	@Override
	public MapParamsAdapter.Params marshal(Map<String, Parameter> map)
			throws Exception {

		if(map==null){
			return null;
		}
		Parameter[] Beans = new Parameter[map.size()];
		Iterator<Entry<String, Parameter>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, Parameter> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		Params rtb = new Params();
		rtb.setParam(Beans);
		return rtb;
	}

	@Override
	public Map<String, Parameter> unmarshal(Params v) throws Exception {
		if(v.getParam() == null) return null;
		Map<String, Parameter> map = new HashMap<String, Parameter>();
		for (int i = 0; i < v.getParam().length; i++) {
			map.put(v.getParam()[i].getName(), v.getParam()[i]);
		}
		return map;
	}

}
