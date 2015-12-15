package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ComboBoxContext;
import xap.lui.core.context.FloatTextContext;
import xap.lui.core.context.FormElementContext;
import xap.lui.core.context.ReferenceTextContext;
import xap.lui.core.context.TextContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCFormElementRender;
import xap.lui.core.render.notify.RenderProxy;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 表单元素
 * 
 */
@XmlRootElement(name = "Element")
@XmlAccessorType(XmlAccessType.NONE)
public final class FormElement extends WebComp {
	private static final long serialVersionUID = -633638779207135081L;
	public static final String SELF_DEF_FUNC = "SELF_DEF_FUNC";
	// LABEL居左，靠左对齐
	public static final String LABELPOS_LEFT_l = "left";
	// LABEL居左，靠右对齐
	public static final String LABELPOS_LEFT_r = "right";
	// LABEL居右
	public static final String LABELPOS_RIGHT = "right";
	// LABEL居输入框顶部
	public static final String LABELPOS_TOP = "top";

	public static final String WIDGET_NAME = "formelement";

	private FormComp parent = null;
	@XmlAttribute
	private Integer rowSpan = Integer.valueOf(1);
	@XmlAttribute
	private Integer colSpan = Integer.valueOf(1);
	// private Integer inputWidth = new Integer(100); //element元素的宽度
	@XmlAttribute
	private String i18nName;

	// 提示多语
	@XmlAttribute
	private String tipI18nName;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getTipI18nName() {
		return tipI18nName;
	}

	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}

	@XmlAttribute
	private String text;
	// 真实值，Reference中使用
	@XmlAttribute
	private String value;
	// 参照使用的显示值
	@XmlAttribute
	private String showValue;

	// 显示在每个ele之后的说明文字
	@XmlAttribute
	private String description;
	@XmlAttribute
	private String langDir;
	// label字体颜色
	@XmlAttribute
	private String labelColor = null;
	@XmlAttribute
	private String labelPos = LABELPOS_LEFT_r;
	@XmlAttribute
	private String editorType;// 编辑器类型
	@XmlAttribute
	private String refNode;// 只有编辑器类型为“Ref”时才有用
	@XmlAttribute
	private String relationField;// 只有编辑器类型为“Ref”时才有用
	@XmlAttribute
	private String dataType;// 数据类型
	@XmlAttribute
	private String field;// 用于帮定数据dataset的具体字段
	@XmlAttribute
	private String refComboData; // 用于存取聚合字段易用
	@XmlAttribute
	private int index; // ComboBox中的选中项索引
	@XmlAttribute
	private String dataDivHeight; // 下拉框控件数据区高度
	@XmlAttribute
	private String defaultValue; // 默认值
	// 对应前台的readOnly属性,enabled代表了前台的是否激活
	@XmlAttribute(name = "isEdit")
	private boolean isEdit = true;
	@XmlAttribute
	private boolean imageOnly = false;
	// 字符串输入框的最大字节长度(目前只用于StringText)
	@XmlAttribute
	private String maxLength;
	// 下拉框使用
	@XmlAttribute(name = "isOnlySelect")
	private boolean isOnlySelect = true;
	// 是否必输项
	// private boolean required = false;
	@XmlAttribute
	private boolean nextLine = false;

	/* 最大值最小值设置,仅用于IntegerTextComp类型 */
	@XmlAttribute
	private String maxValue;
	@XmlAttribute
	private String minValue;
	@XmlAttribute
	private String showTip;

	private String editFormular;
	
	private String validateFormula;
	
	

	public String getValidateFormula() {
		
		if (editFormular == null) {
			LuiEventConf[] confs = this.getEventConfs();
			if (confs != null && confs.length != 0) {
				for (int i = 0; i < confs.length; i++) {
					LuiEventConf inner = confs[i];
					String methodName = inner.getMethod();
					if ("validate_method".equalsIgnoreCase(methodName)) {
						String script = inner.getScript();
						editFormular=script;
					}
				}
			}
		}
		return validateFormula;
	}



	public String getEditFormular() {
		if (editFormular == null) {
			LuiEventConf[] confs = this.getEventConfs();
			if (confs != null && confs.length != 0) {
				for (int i = 0; i < confs.length; i++) {
					LuiEventConf inner = confs[i];
					String methodName = inner.getMethod();
					if ("editor_method".equalsIgnoreCase(methodName)) {
						String script = inner.getScript();
						editFormular=script;
					}
				}
			}
		}
		return editFormular;
	}

	// public void setEditFormular(String editFormular) {
	// this.editFormular = editFormular;
	// }

	/* 精度 */
	@XmlAttribute
	private String precision;

	/* 富文本框工具栏类型 ：简单：Custom ，全部:Full */
	@XmlAttribute
	private String toolbarType = "Custom";

	// 针对于高级编辑器(需要隐藏的操作条:以js数组形式传入,例[0,1])
	// private String hideBarIndices;
	// 针对高级编辑器(需要隐藏的操作条中的图片,以js数组的形式传入,例[[0,1],[],[2]])
	// private String hideImageIndices;

	// 是否聚焦
	@XmlAttribute
	private boolean focus = false;

	// 默认显示值（提示用，不是真实值）
	@XmlAttribute
	private String tip = null;

	// 自定义类型包含控件ID
	@XmlAttribute
	private String bindId;
	@XmlAttribute(name = "isRequire")
	private boolean isRequire = false;
	@XmlAttribute
	private boolean attachNext = false;

	// 输入辅助提示信息（和错误提示相互替代）
	@XmlAttribute
	private String inputAssistant = "";

	// protected String height = null;
	// 文件上传类型的element，加上文件大小限制
	@XmlAttribute
	private String sizeLimit;

	// FileComp用
	@XmlAttribute
	private String sysid;
	// FileComp用
	@XmlAttribute
	private String billtype;
	@XmlAttribute
	private String matchValues;
	@XmlAttribute
	private String ext1;
	@XmlAttribute
	private String ext2;
	@XmlAttribute
	private String ext3;

	/**
	 * 打开页面前的参数传递
	 */
	private String beforeOpenParam;
	@JSONField(serialize = false)
	private PCFormElementRender render;

	public String getBeforeOpenParam() {
		return beforeOpenParam;
	}

	public void setBeforeOpenParam(String beforeOpenParam) {
		if (this.beforeOpenParam == null && beforeOpenParam == null)
			return;
		this.beforeOpenParam = beforeOpenParam;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getParent().getRender().setBeforeOpenParam(this);
		}
	}

	public String getMatchValues() {
		return matchValues;
	}

	public void setMatchValues(String matchValues) {
		if (this.matchValues == null && matchValues == null)
			return;
		this.matchValues = matchValues;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getParent().getRender().setMatchValues(this);
		}
	}

	public String getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(String sizeLimit) {
		if (this.sizeLimit != sizeLimit) {
			this.sizeLimit = sizeLimit;
			setCtxChanged(true);
		}
	}

	public String getSysid() {
		return sysid;
	}

	public void setSysid(String sysid) {
		this.sysid = sysid;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getInputAssistant() {
		return inputAssistant;
	}

	public void setInputAssistant(String inputAssistant) {
		this.inputAssistant = inputAssistant;
	}

	public boolean isAttachNext() {
		return attachNext;
	}

	public void setAttachNext(boolean attachNext) {
		this.attachNext = attachNext;
	}

	public boolean isRequire() {
		return isRequire;
	}

	public void setIsRequire(boolean isRequire) {
		this.isRequire = isRequire;
	}

	public static final int DEFAULT_HEIGHT = 24;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FormElement() {
		super();
	}

	public FormElement(String id) {
		super(id);
	}

	public Integer getColSpan() {
		return colSpan;
	}

	public void setColSpan(Integer colSpan) {
		this.colSpan = colSpan;
	}

	public Integer getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(Integer rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getLabel() {
		return text;
	}

	public void setLabel(String label) {
		if (this.text == null && label == null)
			return;
		this.text = label;
		this.i18nName = null;
		this.langDir = null;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setLabel(this);
			}
		}
	}

	public String getRefNode() {
		return refNode;
	}

	public void setRefNode(String refNode) {
		this.refNode = refNode;
	}

	public String getRelationField() {
		return relationField;
	}

	public void setRelationField(String relationField) {
		this.relationField = relationField;
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String type) {
		this.editorType = type;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object clone() {
		return super.clone();
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		if (isEdit != this.isEdit) {
			this.isEdit = isEdit;
			if (parent != null) {
				if (LifeCyclePhase.ajax.equals(getPhase())) {
					this.getParent().getRender().setIsEdit(this);
				}
			}
		}
	}

	public String getRefComboData() {
		return refComboData;
	}

	public void setRefComboData(String refComboData) {
		this.refComboData = refComboData;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isImageOnly() {
		return imageOnly;
	}

	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		if (this.maxLength == null && maxLength == null)
			return;
		this.maxLength = maxLength;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setMaxLength(this);
			}
		}
	}

	public boolean isOnlySelect() {
		return isOnlySelect;
	}

	public void setOnlySelect(boolean isOnlySelect) {
		this.isOnlySelect = isOnlySelect;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		if (this.maxValue == null && maxValue == null)
			return;
		this.maxValue = maxValue;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setMaxValue(this);
			}
		}
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		if (this.minValue == null && minValue == null)
			return;
		this.minValue = minValue;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setMinValue(this);
			}
		}
	}

	public String getPrecision() {
		return precision;
	}

	/**
	 * @modified by zuopf 2012.09.19
	 * @param precision
	 */
	public void setPrecision(String precision) {
		if (precision != null && !precision.equals(this.precision)) {
			this.precision = precision;
			if (parent != null) {
				if (LifeCyclePhase.ajax.equals(getPhase())) {
					this.getParent().getRender().setPrecision(this);
				}
			}
		}
	}

	public boolean isRequired() {
		return !isRequire;
	}

	public void setRequired(boolean required) {
		this.isRequire = required;
		//setIsRequire(!required);
		setCtxChanged(true);
	}

	public boolean isNextLine() {
		return nextLine;
	}

	public void setNextLine(boolean nextLine) {
		this.nextLine = nextLine;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public String getDataDivHeight() {
		return dataDivHeight;
	}

	public void setDataDivHeight(String dataDivHeight) {
		this.dataDivHeight = dataDivHeight;
	}

	public String getLabelColor() {
		return labelColor;
	}

	public void setLabelColor(String labelColor) {
		if (this.labelColor == null && labelColor == null)
			return;
		this.labelColor = labelColor;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setLabelColor(this);
			}
		}
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text != null && !text.equals(this.text)) {
			this.text = text;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.i18nName = null;
				this.langDir = null;
			}
			setCtxChanged(true);
		}
	}

	public boolean isFocus() {
		return focus;
	}

	/**
	 * @modified by zuopf 2012.09.19
	 */
	public void setFocus(boolean focus) {
		if (focus == true) {
			this.focus = focus;
			if (parent != null) {
				if (LifeCyclePhase.ajax.equals(getPhase())) {
					this.getParent().getRender().setFocus(this);
				}
			}
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (this.value == null && value == null)
			return;
		if ((this.value == null && value != null) || !this.value.equals(value)) {
			this.value = value;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setValue(this);
			}
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		if (index != this.index) {
			this.index = index;
			setCtxChanged(true);
		}
	}

	/**
	 * @modified by zuopf 2012.09.18
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setEnable(this);
			}
		}
	}

	/**
	 * @add by zuopf 2012.09.19
	 */
	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if (parent != null) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.getParent().getRender().setVisible(this);
			}
		}
	}

	public void setParent(FormComp parent) {
		this.parent = parent;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
		setCtxChanged(true);
	}

	public String getShowTip() {
		return showTip;
	}

	public void setShowTip(String showTip) {
		this.showTip = showTip;
	}

	@Override
	public BaseContext getContext() {
		FormElementContext ctx = new FormElementContext();
		ctx.setId(this.getId());
		ctx.setIndex(this.getIndex());
		ctx.setVisible(this.isVisible());
		if (this.getSizeLimit() != null)
			ctx.setSizeLimit(this.getSizeLimit());
		ctx.setTip(this.getTip());
		ctx.setRequired(this.isRequired());
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		FormElementContext feleCtx = (FormElementContext) ctx;
		if (ctx instanceof ReferenceTextContext) { // 参照输入框
			ReferenceTextContext eleCtx = (ReferenceTextContext) ctx;
			this.setShowValue(eleCtx.getShowValue());
			this.setValue(eleCtx.getValue());
			// //针对参照设置matchValues
			// this.setMatchValues(eleCtx.getMatchValues());
		} else if (ctx instanceof TextContext) { // 普通输入框
			TextContext eleCtx = (TextContext) ctx;
			this.setValue(eleCtx.getValue());
		} else if (ctx instanceof ComboBoxContext) { // 下拉输入框
			// ComboBoxContext eleCtx = (ComboBoxContext) ctx;
		} else if (ctx instanceof FloatTextContext) {// 数值型
			FloatTextContext eleCtx = (FloatTextContext) ctx;
			this.setPrecision(eleCtx.getPrecision());
		} else { // RadioGroup、CheckboxGroup等
			// BaseContext eleCtx = (BaseContext) ctx;
		}
		this.setVisible(feleCtx.isVisible());
		this.setLabelColor(feleCtx.getLabelColor());
		this.setLabel(feleCtx.getLabel());
		if (feleCtx.getSizeLimit() != null)
			this.setSizeLimit(feleCtx.getSizeLimit());
		this.setTip(feleCtx.getTip());
		this.setRequired(feleCtx.isRequired());
		this.setCtxChanged(false);
	}

	public FormComp getParent() {
		return parent;
	}

	public String getBindId() {
		return bindId;
	}

	public void setBindId(String bindId) {
		this.bindId = bindId;
	}

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		if (this.showValue == null && showValue == null)
			return;
		if ((this.showValue == null && showValue != null) || !this.showValue.equals(showValue)) {
			this.showValue = showValue;
			this.getParent().getRender().setShowValue(this);
		}
	}

	public static String getLABELPOS_LEFT_l() {
		return LABELPOS_LEFT_l;
	}

	public String getLabelPos() {
		return labelPos;
	}

	public void setLabelPos(String labelPos) {
		this.labelPos = labelPos;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getParent().getRender().setLabelPos(this);
		}
	}

	public String getToolbarType() {
		return toolbarType;
	}

	public void setToolbarType(String toolbarType) {
		this.toolbarType = toolbarType;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	@Override
	public PCFormElementRender getRender() {
		if (this.render == null) {
			this.render = RenderProxy.getRender(new PCFormElementRender(this));
		}
		return this.render;
	}

}
