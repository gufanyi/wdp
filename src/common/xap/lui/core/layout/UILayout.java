package xap.lui.core.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UILayout extends UIElement {
	private static final long serialVersionUID = 8154264148721825061L;
	protected List<UILayoutPanel> panelList = new ArrayList<UILayoutPanel>();
	public static final String CSSSTYLE = "cssStyle";
	private String cssStyle;
	public List<UILayoutPanel> getPanelList() {
		return panelList;
	}

	public void addPanel(UILayoutPanel panel) {
		panelList.add(panel);
		panel.setLayout(this);
		super.addElement(panel);
	}

	public void addPanel(UILayoutPanel panel, UILayoutPanel target, boolean after) {
		int idx = panelList.indexOf(target);
		if(after && idx + 1 != panelList.size()){
			idx++;
		}
		panelList.add(idx, panel);
		panel.setLayout(this);
		super.addElement(panel);
	}
	
	
	public void removePanel(UILayoutPanel ele) {
		if (ele != null) {
			if (panelList != null) {
				super.removeElement(ele);
				if (ele != null) {
					for (UILayoutPanel panel : panelList) {
						if (panel.getId().equals(ele.getId())) {
							panelList.remove(panel);
							break;
						}
					}
				}
			}
		}

	}
	
	public void setCssStyle(String style){
		this.cssStyle=style;
	}
	
	public String getCssStyle(){
		return cssStyle;
	}

	@Override
	public UILayout doClone() {
		UILayout layout = (UILayout) super.doClone();
		if (this.panelList != null) {
			layout.panelList = new ArrayList<UILayoutPanel>();
			Iterator<UILayoutPanel> panels = this.panelList.iterator();
			while (panels.hasNext()) {
				UILayoutPanel panel = panels.next();
				layout.panelList.add((UILayoutPanel) panel.doClone());
			}
		}
		return layout;
	}

}
