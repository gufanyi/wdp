package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ReferenceTextContext;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTextCompRender;

/**
 * 参照
 * 
 * @author zhangxya
 *
 */
@XmlRootElement(name = "Reference")
@XmlAccessorType(XmlAccessType.NONE)
public class ReferenceComp extends TextComp {
	private static final long serialVersionUID = 1531534959905591230L;
	public static final String WIDGET_NAME = "reference";
	// 引用refnode
	@XmlAttribute
	private String refcode;
	@XmlAttribute
	private String showValue = null;
	@XmlAttribute
	private String matchValues;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getMatchValues() {
		return matchValues;
	}

	public void setMatchValues(String matchValues) {
		if (this.matchValues == null && matchValues == null)
			return;
		if ((this.matchValues == null && matchValues != null) || !this.matchValues.equals(matchValues)) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setMatchValues(matchValues);
			}
		}
	}

	@XmlAttribute
	private String beforeOpenParam;

	public String getBeforeOpenParam() {
		return beforeOpenParam;
	}

	public void setBeforeOpenParam(String beforeOpenParam) {
		if (this.beforeOpenParam == null && beforeOpenParam == null)
			return;
		this.beforeOpenParam = beforeOpenParam;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			((PCTextCompRender) this.getRender()).beforeOpenParam(beforeOpenParam);
		}
	}

	public ReferenceComp() {
		setEditorType(EditorTypeConst.REFERENCE);
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	};

	public String getShowValue() {
		return showValue;
	}

	public void setShowValue(String showValue) {
		if (this.showValue == null && showValue == null)
			return;
		if ((this.showValue == null && showValue != null) || !this.showValue.equals(showValue)) {
			this.showValue = showValue;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setShowValue(showValue);
			}
		}
	}

	@Override
	public BaseContext getContext() {
		ReferenceTextContext textCtx = new ReferenceTextContext();
		textCtx.setId(getId());
		return textCtx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ReferenceTextContext textCtx = (ReferenceTextContext) ctx;
		this.setEnabled(textCtx.isEnabled());
		this.setValue(textCtx.getValue());
		this.setShowValue(textCtx.getShowValue());
		this.setReadOnly(textCtx.isReadOnly());
		this.setVisible(textCtx.isVisible());
		this.setCtxChanged(false);
	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "ReferenceComp-000000")/*参照文本框的ID不能为空!\r\n*/);
			buffer.append("参照文本框的ID不能为空!\r\n");
		}
		if (this.getText() == null || this.getText().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "ReferenceComp-000001")/*参照文本框Text不能为空!\r\n*/);
			buffer.append("参照文本框Text不能为空!\r\n");
		}
		if (this.getRefcode() == null || this.getRefcode().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "ReferenceComp-000002")/*参照文本框RefCode不能为空!\r\n*/);
			buffer.append("参照文本框RefCode不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());

	}

	@Override
	public void setValue(String value) {
		if (this.value == null && value == null)
			return;
		if ((this.value == null && value != null) || !this.value.equals(value)) {
			this.value = value;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				((PCTextCompRender) this.getRender()).setValue(value);
			}
		}
	}

}
