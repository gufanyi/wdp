package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.TextContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTextCompRender;
import xap.lui.core.render.UIRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * Text控件基类
 * 
 * @author dengjt
 *
 */
@XmlRootElement(name = "Text")
@XmlAccessorType(XmlAccessType.NONE)
public class TextComp extends WebComp {

	private static final long serialVersionUID = 4555267873292876642L;
	public static final String WIDGET_NAME = "textfield";
	@XmlAttribute
	protected String value = "";
	@XmlAttribute
	private boolean readOnly = false;
	@XmlAttribute
	private String editorType = EditorTypeConst.STRINGTEXT;
	@XmlAttribute
	private String maxValue;
	@XmlAttribute
	private String minValue;// IntegerTextComp中用到
	@XmlAttribute
	private String precision;// FloatTextComp中用到。
	@XmlAttribute
	private String sizeLimit;

	// 是否聚焦
	private String i18nName;
	@XmlAttribute
	private String langDir;
	@XmlAttribute
	private String text;

	// 标签属性
	@XmlAttribute
	private boolean focus = false;
	@XmlAttribute(name = "align")
	private String align = "left";
	@XmlAttribute
	private int textWidth = 0;
	// 是否替换为不可见图片显示
	@XmlAttribute
	private boolean showMark = false;

	// 默认显示值（提示用，不是真实值）
	@XmlAttribute
	private String tip = null;

	// 显示样式
	@XmlAttribute
	private String mask;
	@XmlAttribute(name = "isRequired")
	private boolean isRequired = false;
	// 是否显示Label
	@XmlAttribute(name = "isShowlabel")
	private boolean isShowlabel = true;

	private PCTextCompRender render = null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public boolean isShowlabel() {
		return isShowlabel;
	}

	public void setShowlabel(boolean showlabel) {
		if (this.isShowlabel != showlabel) {
			this.isShowlabel = showlabel;
			// this.getRender().
			// this.getRender().show
			// UpdatePair pair = new UpdatePair("showlabelChanged", this);
			// this.notifyChange(UIElement.UPDATE, pair);
		}

	}

	public boolean isNullable() {
		return isRequired;
	}

	public void setNullable(boolean nullable) {
		this.isRequired = nullable;
	}

	public String getSizeLimit() {
		return sizeLimit;
	}

	public void setSizeLimit(String sizeLimit) {
		this.sizeLimit = sizeLimit;
	}

	public boolean isShowMark() {
		return showMark;
	}

	public void setShowMark(boolean showMark) {
		this.showMark = showMark;
	}

	public TextComp() {
		super();
	}

	public TextComp(String id) {
		super(id);
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public int getTextWidth() {
		return textWidth;
	}

	public void setTextWidth(int textWidth) {
		this.textWidth = textWidth;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.i18nName = null;
		}
		if (this.text != text && text != null) {
			this.text = text;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setText(text);
			}
		}
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		if (this.readOnly != readOnly) {
			this.readOnly = readOnly;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setReadOnly(readOnly);
			}
		}
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (value != null && !value.equals(this.value)) {
			this.value = value;
			// 日期时间类型，要从时间值转到long
			if (!value.equals("")) {
				if (this.editorType.equals(EditorTypeConst.DATETEXT)) {
				} else if (this.editorType.equals(EditorTypeConst.DATETIMETEXT)) {
				}
			}
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setValue(value);
			}
		}
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxv) {
		if ((maxv != null && (this.maxValue == null || !this.maxValue.equals(maxv)))) {
			this.maxValue = maxv;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setMaxValue(maxv);
			}
		}
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		if ((minValue != null && (this.minValue == null || !this.minValue.equals(minValue)))) {
			this.minValue = minValue;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setMinValue(minValue);
			}
		}
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		if (precision != null && !precision.equals(this.precision)) {
			this.precision = precision;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setPrecision(precision);
			}
		}
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String type) {
		this.editorType = type;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean isFocus() {
		return focus;
	}

	public void setFocus(boolean focus) {
		if (focus) {
			this.focus = focus;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setFocus();
			}
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCTextCompRender) this.getRender()).setEnable(enabled);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible != this.isVisible) {
			this.isVisible = visible;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setVisible(visible);
			}
		}
	}

	@Override
	public BaseContext getContext() {
		TextContext textCtx = new TextContext();
		this.getContext(textCtx);
		if (this.getEditorType().equals(EditorTypeConst.INTEGERTEXT)) {
			textCtx.setMaxValue(this.getMaxValue());
			textCtx.setMinValue(this.getMinValue());
		}
		return textCtx;
	}

	// 供继承类使用
	public void getContext(TextContext textCtx) {
	}

	@Override
	public void setContext(BaseContext ctx) {
		TextContext textCtx = (TextContext) ctx;
		String value = textCtx.getValue();
		// 日期时间类型，要从long值转到时间
		if (value != null && !value.equals("")) {
			if (this.editorType.equals(EditorTypeConst.DATETEXT)) {
				// FDate date = new FDate(Long.parseLong(value));
				// value = date.toString();
			} else if (this.editorType.equals(EditorTypeConst.DATETIMETEXT)) {
				// FDateTime dateTime = new FDateTime(Long.parseLong(value));
				// value = dateTime.toString();
			}
		}
		this.setValue(value);
		this.setReadOnly(textCtx.isReadOnly());
		this.setEnabled(textCtx.isEnabled());
		this.setVisible(textCtx.isVisible());
		String maxValue = textCtx.getMaxValue();
		if (maxValue != null && !"".equals(maxValue)) {
			if (this.getEditorType() == EditorTypeConst.INTEGERTEXT) {
				this.setMaxValue(textCtx.getMaxValue());
			}
		}
		String minValue = textCtx.getMinValue();
		if (minValue != null && !"".equals(minValue)) {
			if (this.getEditorType() == EditorTypeConst.INTEGERTEXT) {
				this.setMinValue(textCtx.getMinValue());
			}
		}

		this.setCtxChanged(false);
	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("文本框的ID不能为空!");
		}
		if (buffer.length() > 0)
			throw new LuiRuntimeException(buffer.toString());
	}


	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}
	//清空值
	public void clearValue(){
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCTextCompRender) this.getRender()).clearValue();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public UIRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCTextCompRender(this));
		}
		return render;
	}

}
