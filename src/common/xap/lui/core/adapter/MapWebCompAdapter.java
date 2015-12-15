package xap.lui.core.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapWebCompAdapter extends XmlAdapter<Object[], Map<String, Object>> {

	
	@Override
	public Map<String, Object> unmarshal(Object[] v) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < v.length; i++) {
			map.put("sdsd"+i, v[i]);
		}
		return map;
	}

	@Override
	public Object[] marshal(Map<String, Object> v) throws Exception {
		Object[] Beans = new Object[v.size()];
		Iterator<Entry<String, Object>> it = v.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			Beans[i] = entry.getValue();
			i++;
		}
		
		return Beans;
	}
	


	

}
