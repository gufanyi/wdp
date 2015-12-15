package xap.lui.core.builder;
import java.util.AbstractSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class LuiHashSet<T extends LuiObj> extends AbstractSet implements LuiSet {
	private Map<String, LuiObj> cotainer = new HashMap<String, LuiObj>();
	@Override
	public boolean add(Object e) {
		if (e instanceof LuiObj) {
			LuiObj obj = (LuiObj) e;
			cotainer.put(obj.getId(), obj);
		}
		return true;
	}
	@Override
	public boolean remove(String objId) {
		cotainer.remove(objId);
		return true;
	}
	@Override
	public LuiObj find(String objId) {
		// TODO Auto-generated method stub
		return cotainer.get(objId);
	}
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		return cotainer.values().iterator();
	}
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return cotainer.size();
	}
}
