package xap.lui.core.layout;

import java.util.ArrayList;
import java.util.Iterator;

import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.PCGridLayoutRender;
import xap.lui.core.render.PCGridRowLayoutRender;
import xap.lui.core.render.notify.RenderProxy;

public class UIGridRowLayout extends UILayout {
	private static final long serialVersionUID = 4682570170026288924L;

	public static final String ROWHEIGHT = "rowHeight";
	public static final String COLCOUNT = "colcount";
	private UIGridLayout parent;
	private String rowHeight;
	private int colcount;

	public String getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(String rowHeight) {
		this.rowHeight=rowHeight;
	}

	public Integer getColcount() {
		return colcount;
	}

	public void setColcount(int colcount) {
		this.colcount=colcount;
	}

	public UIGridLayout getParent() {
		return parent;
	}

	public void setParent(UIGridLayout parent) {
		this.parent = parent;
		if (parent != null)
			this.setColcount(parent.getColcount());
	}

	@Override
	public void addPanel(UILayoutPanel panel) {
		UIGridPanel cell = (UIGridPanel) panel;
		panelList.add(cell);
		cell.setParent(this);
	}

	public void addPanelWithIndex(UILayoutPanel panel, int index) {
		panelList.add(panel);
		panel.setLayout(this);
		super.addElement(panel);
	}

	@Override
	public UIGridRowLayout doClone() {
		UIGridRowLayout layout = (UIGridRowLayout) super.doClone();
		if (this.panelList != null) {
			layout.panelList = new ArrayList<UILayoutPanel>();
			Iterator<UILayoutPanel> panels = this.panelList.iterator();
			while (panels.hasNext()) {
				UILayoutPanel panel = panels.next();
				layout.addPanel((UILayoutPanel) panel.doClone());
			}
		}
		layout.render = null;
		return layout;
	}
	
	@Override
	public ILuiRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCGridRowLayoutRender(this,(PCGridLayoutRender)this.getParent().getRender()));
		}
		return render;
	}

}
