package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.CheckBoxContext;
import xap.lui.core.context.CheckboxGroupContext;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCCheckboxGroupCompRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "CheckBoxGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class CheckboxGroupComp extends TextComp {

	private static final long serialVersionUID = 7116611611792966304L;

	public static final String WIDGET_NAME = "checkboxgroup";

	private List<CheckBoxComp> elementList = new ArrayList<CheckBoxComp>();
	@XmlAttribute
	private String dataListId;

	// 选中的值（逗号,分隔的字符串）
	@XmlAttribute
	private String value;
	@XmlAttribute
	private int tabIndex;

	// 每个子项的间隔
	@XmlAttribute
	private int sepWidth;

	// 是否每个子项占一行
	@XmlAttribute
	private boolean changeLine = false;
	@XmlAttribute
	private boolean readOnly = false;

	// 引用参照
	@XmlAttribute
	private String refData = null;

	private PCCheckboxGroupCompRender render;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getRefData() {
		return refData;
	}

	public void setRefData(String refData) {
		this.refData = refData;
	}

	public boolean isChangeLine() {
		return changeLine;
	}

	public void setChangeLine(boolean changeLine) {
		this.changeLine = changeLine;
	}

	public List<CheckBoxComp> getElementList() {
		return elementList;
	}

	public void setElementList(List<CheckBoxComp> elementList) {
		this.elementList = elementList;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
			this.value = value;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setValue(value);
			}
		}
	}

	public String getDataListId() {
		return dataListId;
	}

	public void setDataListId(String dataListId) {
		if (this.dataListId != dataListId) {
			this.dataListId = dataListId;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setDataList(dataListId);
			}
		}
	}

	public void addElement(CheckBoxComp ele) {
		this.elementList.add(ele);
	}

	public int getSepWidth() {
		return sepWidth;
	}

	public void setSepWidth(int sepWidth) {
		this.sepWidth = sepWidth;
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if (this.readOnly != readOnly) {
			this.readOnly = readOnly;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setEnable(!readOnly);
			}
		}
	}

	public Object clone() {
		CheckboxGroupComp comp = (CheckboxGroupComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<CheckBoxComp>();
			Iterator<CheckBoxComp> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((CheckBoxComp) it.next().clone());
			}
		}
		return comp;
	}

	@Override
	public boolean isCtxChanged() {
		for (int i = 0; i < elementList.size(); i++) {
			CheckBoxComp checkbox = elementList.get(i);
			if (checkbox.isCtxChanged())
				return true;
		}
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		CheckboxGroupContext ctx = new CheckboxGroupContext();
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		CheckboxGroupContext ckgCtx = (CheckboxGroupContext) ctx;
		this.setEnabled(ckgCtx.isEnabled());
		this.setDataListId(ckgCtx.getComboDataId());
		this.setValue(ckgCtx.getValue());
		this.setVisible(ckgCtx.isVisible());
		// 子元素
		CheckBoxContext[] checkboxContexts = ckgCtx.getCheckboxContexts();
		if (checkboxContexts != null) {
			for (int i = 0, n = checkboxContexts.length; i < n; i++) {
				CheckBoxContext cbCtx = checkboxContexts[i];
				for (int j = 0, m = elementList.size(); j < m; j++) {
					if (elementList.get(j).getId().equals(cbCtx.getId())) {
						elementList.get(j).setContext(cbCtx);
						break;
					}
				}
			}
		}
		setCtxChanged(false);
	}

	@Override
	public PCCheckboxGroupCompRender getRender() {
		if (this.render == null) {
			render = RenderProxy.getRender(new PCCheckboxGroupCompRender(this));
		}
		return render;
	}

}
