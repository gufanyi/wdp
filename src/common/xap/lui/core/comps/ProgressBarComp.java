package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ProgressBarContext;


/**
 * Button 控件配置
 * @author dengjt
 *
 */
@XmlRootElement(name = "ProgressBarComp")
@XmlAccessorType(XmlAccessType.NONE)
public class ProgressBarComp extends WebComp {

	private static final long serialVersionUID = -3640014425289622883L;
	public static final String WIDGET_NAME = "progressbar";
	public static final String VALUE_ALIGN_RIGHT = "right";
	public static final String VALUE_ALIGN_LEFT = "left";
	public static final String VALUE_ALIGN_CENTER = "center";
	@XmlAttribute
	private String value;
	@XmlAttribute
	private String valueAlign = VALUE_ALIGN_RIGHT;
	
	public ProgressBarComp() {
//		this.setHeight("22");
//		this.setWidth("120");
	}
	
	public ProgressBarComp(String id) {
		super(id);
//		this.setHeight("22");
//		this.setWidth("120");
	}
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public Object clone(){
		return super.clone();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if (!value.equals(this.value)) {
			this.value = value;
			setCtxChanged(true);
		}
	}

	public String getValueAlign() {
		return valueAlign;
	}

	public void setValueAlign(String valueAlign) {
		this.valueAlign = valueAlign;
	}
	
	@Override
	public BaseContext getContext() {
		ProgressBarContext ctx = new ProgressBarContext();
		ctx.setId(this.getId());
		ctx.setValue(this.value);
		ctx.setVisible(this.isVisible());
		return ctx;
	}
	
	@Override
	public void setContext(BaseContext ctx) {
		ProgressBarContext pbCtx = (ProgressBarContext) ctx;
		this.setValue(pbCtx.getValue());
		this.setVisible(pbCtx.isVisible());
		this.setCtxChanged(false);
	}

}
