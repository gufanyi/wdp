package xap.lui.core.constant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;

/**
 * 构建一个UIElement的链接
 * @author licza
 *
 */
public class UIElementTreeBuilder  {
	private static final String _NEW_FRM = "_newFrm";
	Map<String,String> strack = new LinkedHashMap<String, String>();
	/**
	 * 构建一个UI树
	 * @param uimeta
	 * @param parentid
	 * @param id
	 * @return
	 */
	public List<String> buildUITree(UIPartMeta uimeta, String parentid, String id) {
		/**
		 * 是否UIFormElement
		 */
		boolean isUIFormElement = parentid != null && parentid.endsWith(_NEW_FRM);
		/**
		 * 自由Form根据UIFormElement的ID查找.
		 * 其余根据父ID查找
		 */
		UIElement ele = findElementById(uimeta, isUIFormElement ? id : parentid);
		List<String> uiTree = new ArrayList<String>();
		if(ele != null){
			uiTree.addAll(strack.values());
		}
		/**
		 * UIFormElement不显示Form
		 */
		if(!isUIFormElement)
			uiTree.add(parentid);
		
		if(id != null)
			uiTree.add(id);
		return uiTree;
	}
	/**
	 * 查找元素，本元素仅包含控件与布局
	 */
	public  UIElement findElementById(UIElement ele, String id) {
		UIElement uiEle = null;
		if (ele == null || id == null)
			return null;
		
		if(ele.getId() != null && ele.getId().equals(id))
			return ele;
		
		if(ele instanceof UILayoutPanel){
			String key = UUID.randomUUID().toString();
			if(ele.getId() != null && ! (ele instanceof UIGridRowPanel) )
				strack.put(key, ele.getId());
			uiEle = findElementById(((UILayoutPanel) ele).getElement(), id);
			if(uiEle == null )
				strack.remove(key);
		}
		else if (ele instanceof UILayout) {
			Iterator<UILayoutPanel> it = ((UILayout)ele).getPanelList().iterator();
			while(it.hasNext()){
				String key = UUID.randomUUID().toString();
				/**
				 * 这个地方去掉GridRowLayout.以及UIWidget 
				 */
				if(ele.getId() != null && !(ele instanceof UIGridRowLayout) && ! (ele instanceof UIViewPart))
					strack.put(key, ele.getId());
				 
				UILayoutPanel panel = it.next();
				uiEle = findElementById(panel, id);
				if(uiEle != null )
					break;
				else{
					strack.remove(key);
				}
			}
		}
		return uiEle;
	}
	
}
