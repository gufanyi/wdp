package xap.lui.core.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;

public class MapMenubarCompAdapter extends XmlAdapter<MapMenubarCompAdapter.MenubarComps, Map<String, MenubarComp>> {

	public static class MenubarComps {

		@XmlElement(name="MenuBar")
		private MenubarComp[] MenubarComp;
		@XmlElement(name = "MenuItem")
		private MenuItem[] MenuItem;

		public MenubarComp[] getMenubarComp() {
			return MenubarComp;
		}

		public void setMenubarComp(MenubarComp[] menubarComp) {
			MenubarComp = menubarComp;
		}

		public MenuItem[] getMenuItem() {
			return MenuItem;
		}

		public void setMenuItem(MenuItem[] menuItem) {
			MenuItem = menuItem;
		}
		
	}
	

	@Override
	public Map<String, MenubarComp> unmarshal(MenubarComps v) throws Exception {
		MenuItem[]  menuItem =  v.getMenuItem();
		List<MenuItem> menuList = new ArrayList<MenuItem>();
		
		for (int i = 0; i < menuItem.length; i++) {
			menuList.add(menuItem[i]);
		}
		MenubarComp nenubarComp = new MenubarComp();
		nenubarComp.setMenuList(menuList);
		Map<String, MenubarComp> map = new HashMap<String, MenubarComp>();
		map.put("ittit", nenubarComp);
		return map;
	}

	@Override
	public MenubarComps marshal(Map<String, MenubarComp> map) throws Exception {
		MenubarComp[] Beans = new MenubarComp[map.size()];
		
		Iterator<Entry<String, MenubarComp>> it = map.entrySet().iterator();
		int i = 0;
		MenuItem[] menuItem =null;
		while (it.hasNext()) {
			Entry<String, MenubarComp> entry = it.next();
			Beans[i] = entry.getValue();
			List<MenuItem> mlist = Beans[i].getMenuList();
			menuItem = new MenuItem[Beans[i].getMenuList().size()];
			for(int j=0;j<mlist.size();j++){
				menuItem[j]=(MenuItem)mlist.get(j);
			}
			i++;
		}
		MenubarComps mc = new MenubarComps();
		mc.setMenuItem(menuItem);
		return mc;
	}

}

