package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapMappingAdapter extends XmlAdapter<MapMappingAdapter.Mapping, Map<String, String>> {

	public static class Mapping {

		private MappingValue[] mapping;

		public MappingValue[] getMapping() {
			return mapping;
		}

		public void setMapping(MappingValue[] mapping) {
			this.mapping = mapping;
		}
	}
	public static class MappingValue {

		private String inValue;
		private String outValue;
		public String getInValue() {
			return inValue;
		}
		public void setInValue(String inValue) {
			this.inValue = inValue;
		}
		public String getOutValue() {
			return outValue;
		}
		public void setOutValue(String outValue) {
			this.outValue = outValue;
		}
	}

	@Override
	public MapMappingAdapter.Mapping marshal(Map<String, String> map)
			throws Exception {
		if(map == null)
			return null;
		MappingValue[] Beans = new MappingValue[map.size()];
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			MappingValue mv = new MappingValue();
			mv.setOutValue(entry.getKey());
			mv.setInValue(entry.getValue());
			Beans[i] = mv;
			i++;
		}
		Mapping rtb = new Mapping();
		rtb.setMapping(Beans);
		return rtb;
	}

	@Override
	public Map<String, String> unmarshal(Mapping v) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(v.getMapping()==null){
			return map;
		}
		for (int i = 0; i < v.getMapping().length; i++) {
			map.put(v.getMapping()[i].getOutValue(), v.getMapping()[i].getInValue());
		}
		return map;
	}

}
