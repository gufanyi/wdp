package xap.lui.core.adapter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import xap.lui.core.comps.LuiElement;

public class GridColumnListAdapter extends XmlAdapter<GridColumnListAdapter.Columns, List<LuiElement>> {
	
	
	
	
	@XmlAccessorType(XmlAccessType.NONE)
	 public static class Columns {
		@XmlElement(name="Column")
	    private ArrayList<LuiElement> gridList = new ArrayList<LuiElement>();

		public ArrayList<LuiElement> getGridList() {
			return gridList;
		}

		public void setGridList(ArrayList<LuiElement> gridList) {
			this.gridList = gridList;
		}

		

			  
	 }



	@Override
	public List<LuiElement> unmarshal(Columns v) throws Exception {
		ArrayList<LuiElement> ll = new ArrayList<LuiElement>();
		System.out.println("sds");
		return ll;
	}

	@Override
	public Columns marshal(List<LuiElement> v) throws Exception {
		Columns column= new Columns();
		return column;
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
	

	

