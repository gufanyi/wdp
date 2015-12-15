package xap.lui.core.builder;

import java.util.Set;

public interface LuiSet<T extends LuiObj> extends Set<T> {
	 boolean remove(String objId);
	 T  find(String objId);
}
