package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.RadioGroupContext;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCRadioGroupCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * @author guoweic
 *
 */
@XmlRootElement(name = "RadioGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class RadioGroupComp extends TextComp {

	private static final long serialVersionUID = 8517132806746485488L;
	public static final String WIDGET_NAME = "radiogroup";

	private List<RadioComp> elementList = new ArrayList<RadioComp>();
	@XmlAttribute
	private String dataListId;

	// 选中项索引
	@XmlAttribute
	private int index;
	@XmlAttribute
	private int tabIndex;

	// 每个子项的间隔
	@XmlAttribute
	private int sepWidth;

	// 是否每个子项占一行
	@XmlAttribute
	private boolean changeLine = false;
//	@XmlAttribute
//	private boolean readOnly = false;

	// 引用参照
	@XmlAttribute
	private String refData = null;
	@JSONField(serialize=false)
	private PCRadioGroupCompRender render = null;

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

	public List<RadioComp> getElementList() {
		return elementList;
	}

	public void setElementList(List<RadioComp> elementList) {
		this.elementList = elementList;
	}

	public String getDataListId() {
		return dataListId;
	}

	public void setDataListId(String dataListId) {
		this.dataListId = dataListId;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().bindDataList(dataListId);
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void addElement(RadioComp ele) {
		this.elementList.add(ele);
	}

	public int getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public int getSepWidth() {
		return sepWidth;
	}

	public void setSepWidth(int sepWidth) {
		this.sepWidth = sepWidth;
	}

//	public boolean isReadOnly() {
//		return readOnly;
//	}
//
//	public void setReadOnly(boolean readOnly) {
//		this.readOnly = readOnly;
//		if (LifeCyclePhase.ajax.equals(getPhase())) {
//			this.getRender().setEnable(readOnly);
//		}
//	}

	public Object clone() {
		RadioGroupComp comp = (RadioGroupComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<RadioComp>();
			Iterator<RadioComp> it = this.elementList.iterator();
			while (it.hasNext()) {
				comp.addElement((RadioComp) it.next().clone());
			}
		}
		return comp;
	}

	@Override
	public boolean isCtxChanged() {
		for (int i = 0; i < elementList.size(); i++) {
			RadioComp radio = elementList.get(i);
			if (radio.isCtxChanged())
				return true;
		}
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		RadioGroupContext ctx = new RadioGroupContext();
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		RadioGroupContext rgCtx = (RadioGroupContext) ctx;
		this.setEnabled(rgCtx.isEnabled());
		this.setDataListId(rgCtx.getComboDataId());
		this.setIndex(rgCtx.getIndex());
		this.setValue(rgCtx.getValue());
		setCtxChanged(false);
	}

	@Override
	public String getEditorType() {
		return EditorTypeConst.RADIOGROUP;
	}

	@Override
	public PCRadioGroupCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCRadioGroupCompRender(this));
		}
		return render;
	}

}
