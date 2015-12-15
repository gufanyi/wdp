package xap.lui.core.adapter;
import xap.lui.core.common.ExtAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class MapAdapter extends XmlAdapter<MapAdapter.Attributes, Map<String, ExtAttribute>> {
	@XmlRootElement(name="Attributes")
	@XmlAccessorType(XmlAccessType.NONE)
	 public static class Attributes {
		@XmlElement(name="Attribute")
	    private ArrayList<ExtAttribute> extAttributeList = new ArrayList<ExtAttribute>();

		public ArrayList<ExtAttribute> getExtAttributeList() {
			return extAttributeList;
		}

		public void setExtAttributeList(ArrayList<ExtAttribute> extAttributeList) {
			this.extAttributeList = extAttributeList;
		}
	  
	 }

	@Override
	public Map<String, ExtAttribute> unmarshal(Attributes v) throws Exception {
		Map<String, ExtAttribute> map = new HashMap<String, ExtAttribute>();
		List<ExtAttribute> list = v.getExtAttributeList();
		for(int i =0;i<list.size();i++){
			map.put(list.get(i).getKey(),list.get(i));
		}						
		return map;
	}

	@Override
	public MapAdapter.Attributes marshal(Map<String, ExtAttribute> map) throws Exception {
		ArrayList<ExtAttribute> Beans = new ArrayList<ExtAttribute>();			
		if(map==null){
			return null;
		}
		Iterator<Entry<String, ExtAttribute>> it = map.entrySet().iterator();
		while (it.hasNext()) {				
			Entry<String, ExtAttribute> entry = it.next();
			Beans.add(entry.getValue());				
		    
		}
		Attributes attr = new Attributes();
		attr.setExtAttributeList(Beans);
		if(Beans.size()==0){
			return null;
		}else{
			return attr;
		}
	}

	
}




/*public class MapAdapter extends XmlAdapter<MapAdapter.Attributes, Map<String, ExtAttribute>> {
	
	 public static class Attributes {
		   @XmlElement(name="Attribute")
	       private ExtAttribute[] Attribute;	

		public ExtAttribute[] getAttribute() {
			return Attribute;
		}

		public void setAttribute(ExtAttribute[] attribute) {
			Attribute = attribute;
		}     
	  
	 }
	
	@Override
	public MapAdapter.Attributes marshal(Map<String, ExtAttribute> map) throws Exception {
		
		ExtAttribute[] Beans = new ExtAttribute[map.size()];			
		Iterator<Entry<String, ExtAttribute>> it = map.entrySet().iterator();
		int i=0;
		while (it.hasNext()) {				
			Entry<String, ExtAttribute> entry = it.next();
			Beans[i] = entry.getValue();				
		    i++;
		}
		Attributes rtb= new Attributes();
		rtb.setAttribute(Beans);
		if(Beans.length==0){
			return null;
		}else{
			return rtb;
		}
		
	}

	@Override
	public Map<String, ExtAttribute> unmarshal(Attributes v) throws Exception {
		Map<String, ExtAttribute> map = new HashMap();
		for(int i =0;i<v.getAttribute().length;i++){
			map.put(v.getAttribute()[i].getKey(),v.getAttribute()[i]);
		}						
		return map;
	}
}
*/
	

	

