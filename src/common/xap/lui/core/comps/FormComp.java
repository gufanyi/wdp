package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.AutoFormContext;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.FormElementContext;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.render.PCFormCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * Form控件配置
 */
@XmlRootElement(name = "Form")
@XmlAccessorType(XmlAccessType.NONE)
public class FormComp extends WebComp implements IDataBinding, IContainerComp<FormElement> {
	private static final long serialVersionUID = 7651132623606479940L;
	public static final String WIDGET_NAME = "autoform";
	public static final int DEFAULT_WIDTH = 120;
	public static final int FIXED_LAYOUT = 1;
	public static final int FLOW_LAYOUT = 2;
	public static final int FIXED_HINT_LAYOUT = 3;
	@XmlElement(name = "Element")
	private List<FormElement> elementList = new ArrayList<FormElement>();
	@XmlAttribute(name = "column")
	private Integer column = Integer.valueOf(2);
	@XmlAttribute
	private String dataset;
	@XmlAttribute
	private int rowHeight = 24;
	@XmlAttribute
	private int eleWidth = 0;
	// 最小标签宽度
	@XmlAttribute
	private int labelMinWidth = 0;
	@XmlAttribute
	private String caption;
	@XmlAttribute
	private boolean withForm = false;
	// form背景色
	@XmlAttribute
	private String backgroundColor = null;
	@XmlAttribute
	private int renderType = FLOW_LAYOUT;
	@XmlAttribute
	private boolean readOnly = false;
	@XmlAttribute
	private String formRender = null;
	// label文字是否缩略显示
	@XmlAttribute(name = "isEllipsis")
	private boolean isEllipsis = false;

	@JSONField(serialize=false)
	private PCFormCompRender render = null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public boolean isEllipsis() {
		return isEllipsis;
	}

	public void setEllipsis(boolean isEllipsis) {
		this.isEllipsis = isEllipsis;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public int getLabelMinWidth() {
		return labelMinWidth;
	}

	public void setLabelMinWidth(int labelMinWidth) {
		this.labelMinWidth = labelMinWidth;
	}

	public FormComp() {
		super();
	}

	public FormComp(String id) {
		super(id);
	}

	public int getEleWidth() {
		if (this.eleWidth == 0)
			return DEFAULT_WIDTH;
		return eleWidth;
	}

	public void setEleWidth(int eleWidth) {
		this.eleWidth = eleWidth;
	}

	public List<FormElement> getElementList() {
		return elementList;
	}

	public int getElementCountWithoutHidden() {
		if (elementList == null)
			return 0;
		int count = 0;
		for (FormElement ele : elementList) {
			if (ele.isVisible())
				count++;
		}
		return count;
	}

	public FormElement getElementById(String id) {
		Iterator<FormElement> it = elementList.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.getId().equals(id))
				return ele;
		}
		return null;
	}

	public void setElementList(List<FormElement> elementList) {
		this.elementList = elementList;
		if (this.elementList != null) {
			for (FormElement formE : this.elementList) {
				formE.setParent(this);
				formE.setWidget(this.getWidget());
			}
		}
	}

	public void addElement(FormElement ele) {
		ele.setParent(this);
		ele.setWidget(this.getWidget());
		this.elementList.add(ele);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addFormElement(ele);
		}
	}

	public void removeElement(FormElement ele) {
		if (this.elementList != null && this.elementList.size() > 0) {
			this.elementList.remove(ele);
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().removeElement(ele.getId());
			}
		}
	}

	public void removeElementById(String eleId) {
		if (this.elementList != null && this.elementList.size() > 0) {
			for (int i = 0; i < elementList.size(); i++) {
				FormElement formEle = elementList.get(i);
				if (formEle.getId().equals(eleId)) {
					this.removeElement(formEle);
					break;
				}
			}
		}
	}

	public void removeAllElement() {

	}

	public Integer getColumn() {
		return column;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public String getDataset() {
		return dataset;
	}

	public List<FormElement> filterElements(String name) {
		List<FormElement> eleList = new ArrayList<FormElement>();
		Iterator<FormElement> it = elementList.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.getId().startsWith(name))
				eleList.add(ele);
		}
		return eleList;
	}

	public void setDataset(String datasetid) {
		this.dataset = datasetid;
	}
	
	public void setDataset(String datasetid, boolean needRender) {
		this.dataset = datasetid;
		if(needRender) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getRender().setDataset(datasetid);
			}
		}
	}

	public Object clone() {
		FormComp comp = (FormComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<FormElement>();
			Iterator<FormElement> it = this.elementList.iterator();
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			while (it.hasNext()) {
				comp.addElement((FormElement) it.next().clone());
			}
			RequestLifeCycleContext.get().setPhase(phase);
		}
		return comp;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if (readOnly != this.readOnly) {
			this.readOnly = readOnly;
			setCtxChanged(true);
			addCtxChangedProperty("readOnly");
		}
	}

	public int getRowHeight() {
		return rowHeight;
	}

	public void setRowHeight(int rowHeight) {
		this.rowHeight = rowHeight;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isWithForm() {
		return withForm;
	}

	public void setWithForm(boolean withForm) {
		this.withForm = withForm;
	}

	public int getRenderType() {
		return FLOW_LAYOUT;
	}

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	public String getFormRender() {
		return formRender;
	}

	public void setFormRender(String formRender) {
		this.formRender = formRender;
	}

	public void mergeProperties(LuiElement ele) {
		super.mergeProperties(ele);
		// throw new LuiRuntimeException("not implemented");
	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("表单的ID不能为空!");
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "FormComp-000000")/*表单的ID不能为空!\r\n*/);
		}
		if (this.getDataset() == null || this.getDataset().equals("")) {
			buffer.append("表单引用的数据集不能为空!");
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "FormComp-000001")/*表单引用的数据集不能为空!\r\n*/);
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());
	}

	public boolean isCtxChanged() {
		if (this.elementList.size() > 0) {
			Iterator<FormElement> it = elementList.iterator();
			while (it.hasNext()) {
				FormElement item = it.next();
				if (item.isCtxChanged()) {
					return true;
				}
			}
		}
		return super.isCtxChanged();
	}

	@Override
	public BaseContext getContext() {
		AutoFormContext ctx = new AutoFormContext();
		ctx.setId(this.getId());
		// if (checkCtxPropertyChanged("enabled"))
		// ctx.setEnabled(this.enabled);
		if (checkCtxPropertyChanged("readOnly"))
			ctx.setReadOnly(this.readOnly);
		if (this.elementList.size() > 0) {
			List<BaseContext> ctxList = new ArrayList<BaseContext>();
			Iterator<FormElement> it = elementList.iterator();
			while (it.hasNext()) {
				FormElement item = it.next();
				if (item.isCtxChanged()) {
					ctxList.add((BaseContext) item.getContext());
				}
			}
			ctx.setElementContexts(ctxList.toArray(new FormElementContext[0]));
		}

		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		AutoFormContext formCtx = (AutoFormContext) ctx;
		this.setEnabled(formCtx.isEnabled());
		this.setReadOnly(formCtx.isReadOnly());
		BaseContext[] itemContexts = formCtx.getElementContexts();
		if (itemContexts != null) {
			for (int i = 0, n = itemContexts.length; i < n; i++) {
				BaseContext itemCtx = itemContexts[i];
				for (int j = 0, m = elementList.size(); j < m; j++) {
					FormElement ele = elementList.get(j);
					// TODO 这里不需要写，检查bug
					ele.setCtxChanged(false);
					if (ele.getId().equals(itemCtx.getId())) {
						if (itemCtx instanceof FormElementContext) {
							ele.setValue(((FormElementContext) itemCtx).getValue());
							ele.setSizeLimit(((FormElementContext) itemCtx).getSizeLimit());
						}
						ele.setContext(itemCtx);
						break;
					}
				}
			}
		}
		this.setCtxChanged(false);
	}

	/**
	 * 设置可编辑状态
	 */
	public void setEnabled(boolean enabled) {
		if (enabled != this.enabled) {
			this.enabled = enabled;
			if(!enabled){
				List<FormElement> list = this.getElementList();
				for (int i = 0, n = list.size(); i < n; i++) {
					list.get(i).setEnabled(enabled);
				}
			}
		}
	}
	
	@Override
	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if(!visible){//为false时，子的全false
			if(this.elementList != null){
				for(FormElement item : elementList){
					item.setVisible(visible);
				}
			}
		}
	}

	public int idToIndex(String id) {
		int size = elementList.size();
		for (int i = 0; i < size; i++) {
			if (elementList.get(i).getId().trim().equals(id))
				return i;
		}
		return -1;
	}

	public void setCtxChanged(boolean changed) {
		super.setCtxChanged(changed);
		if (!changed) {
			Iterator<FormElement> it = this.getElementList().iterator();
			while (it.hasNext()) {
				FormElement ele = it.next();
				ele.setCtxChanged(changed);
			}
		}
	}

	/**
	 * 动态隐藏整体错误提示框
	 */
	public void hideErrorMsg() {
		this.getRender().hideError();
	}

	@Override
	public PCFormCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCFormCompRender(this));
		}
		return render;
	}

}
