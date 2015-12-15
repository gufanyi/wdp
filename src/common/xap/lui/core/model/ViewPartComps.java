package xap.lui.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.builder.LuiHashSet;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.comps.BarChartComp;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.DateTextComp;
import xap.lui.core.comps.DecimalTextComp;
import xap.lui.core.comps.FileComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.ImageComp;
import xap.lui.core.comps.IntegerTextComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.LineChartComp;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.comps.PieChartComp;
import xap.lui.core.comps.ProgressBarComp;
import xap.lui.core.comps.PropertyGridComp;
import xap.lui.core.comps.PwdTextComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.comps.WebPartComp;
import xap.lui.core.exception.LuiRuntimeException;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ViewPartComps implements Cloneable, Serializable {
	private static final long serialVersionUID = 6273467687662465677L;
	@XmlElementRefs({ @XmlElementRef(name = "CheckBox", type = CheckBoxComp.class), //
			@XmlElementRef(name = "CheckBoxGroup", type = CheckboxGroupComp.class), //
			@XmlElementRef(name = "RadioGroup", type = RadioGroupComp.class), //
			@XmlElementRef(name = "TextArea", type = TextAreaComp.class), //
			@XmlElementRef(name = "Label", type = LabelComp.class), //
			@XmlElementRef(name = "Text", type = TextComp.class), //
			@XmlElementRef(name = "Button", type = ButtonComp.class), //
			@XmlElementRef(name = "ComboBox", type = ComboBoxComp.class), //
			@XmlElementRef(name = "DateText", type = DateTextComp.class), //
			@XmlElementRef(name = "StringText", type = StringTextComp.class), //
			@XmlElementRef(name = "IntegerText", type = IntegerTextComp.class), //
			@XmlElementRef(name = "DecimalText", type = DecimalTextComp.class), //
			@XmlElementRef(name = "PwdText", type = PwdTextComp.class), //
			@XmlElementRef(name = "Iframe", type = IFrameComp.class), //
			@XmlElementRef(name = "SelfDefComp", type = SelfDefComp.class), //
			@XmlElementRef(name = "ImageComp", type = ImageComp.class), //
			@XmlElementRef(name = "Link", type = LinkComp.class), //
			@XmlElementRef(name = "ProgressBarComp", type = ProgressBarComp.class), //
			@XmlElementRef(name = "Reference", type = ReferenceComp.class), //
			@XmlElementRef(name = "Grid", type = GridComp.class), //
			@XmlElementRef(name = "Form", type = FormComp.class), //
			@XmlElementRef(name = "Radio", type = RadioComp.class), //
			@XmlElementRef(name = "HtmlContent", type = WebPartComp.class), //
			@XmlElementRef(name = "Tree", type = TreeViewComp.class), //
			@XmlElementRef(name = "BarChart", type = BarChartComp.class), //
			@XmlElementRef(name = "LineChart", type = LineChartComp.class), //
			@XmlElementRef(name = "PieChart", type = PieChartComp.class), //
			@XmlElementRef(name = "ToolBar", type = ToolBarComp.class), //
			//@XmlElementRef(name = "TextButton", type = TextButtonComp.class), //
			@XmlElementRef(name = "File", type = FileComp.class),
			@XmlElementRef(name = "PropertyGrid", type = PropertyGridComp.class)})
	private LuiSet<WebComp> components = new LuiHashSet<WebComp>();

	public void setComponents(LuiSet<WebComp> components) {
		this.components = components;
	}

	public Set<WebComp> getComponents() {
		return components;
	}

	private ViewPartMeta widget;

	public ViewPartComps() {
	}

	public ViewPartComps(ViewPartMeta widget) {
		this();
		this.widget = widget;
	}

	public WebComp getComponent(String id) {
		return components.find(id);
	}

	public void removeComponent(String id) {
		components.remove(id);
	}

	/**
	 * 获取指定Class类型的控件
	 * 
	 * @param c
	 *            Class
	 * @return
	 */
	public WebComp[] getComponentByType(Class<? extends WebComp> c) {
		Iterator<WebComp> it = components.iterator();
		List<WebComp> list = new ArrayList<WebComp>();
		while (it.hasNext()) {
			WebComp comp = it.next();
			if (c.isAssignableFrom(comp.getClass())) {
				list.add(comp);
			}
		}
		return list.toArray(new WebComp[0]);
	}

	public void addComponent(WebComp component) {
		component.setWidget(this.widget);
		components.add(component);
	}

	public void merge(ViewPartComps viewComps) {
		this.components.addAll(viewComps.components);
	}

	public Object clone() {
		try {
			ViewPartComps viewComps = (ViewPartComps) super.clone();
			viewComps.components = new LuiHashSet<WebComp>();
			for (WebComp inner : this.components) {
				viewComps.components.add((WebComp) inner.clone());
			}
			return viewComps;
		} catch (CloneNotSupportedException e) {
			throw new LuiRuntimeException(e.getMessage(), e);
		}
	}

	public void setWidget(ViewPartMeta luiWidget) {
		this.widget = luiWidget;
		for (Iterator<WebComp> iter = this.components.iterator(); iter.hasNext();) {
			WebComp comp = iter.next();
			comp.setWidget(luiWidget);
		}
	}

	public ViewPartMeta getWidget() {
		return widget;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		WebComp[] dss = this.components.toArray(new WebComp[0]);
		if (dss != null) {
			buf.append("components:{");
			for (int i = 0; i < dss.length; i++) {
				buf.append(dss[i].getId());
				buf.append(",");
			}
			buf.append("},");
		}
		return buf.toString();
	}

	public WebComp[] getComps() {
		return this.components.toArray(new WebComp[0]);
	}
}
