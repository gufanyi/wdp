package xap.lui.core.comps;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ComboBoxContext;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.render.PCComboCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * 下拉框类型编辑控件
 *
 */
@XmlRootElement(name = "ComboBox")
@XmlAccessorType(XmlAccessType.NONE)
public class ComboBoxComp extends TextComp {
	private static final long serialVersionUID = 2847503923257565737L;
	public static final String WIDGET_NAME = "combo";
	@XmlAttribute
	private String refComboData;
	// 是否为仅显示图片
	@XmlAttribute
	private boolean imageOnly;
	@XmlAttribute(name="isOnlySelect")
	private boolean isOnlySelect = true;
	// 数据区的高度,可以不指定该参数
	@XmlAttribute
	private String dataDivHeight = null;
	// 是否允许存在下拉数据之外的值
	@XmlAttribute(name="isAllowExtendValue")
	private boolean isAllowExtendValue = false;
	// 是否替换为不可见图片显示
	//@XmlAttribute
	//private boolean showMark = false;
	//引用参照
	@XmlAttribute
	private String refData = null;
	// 可见下拉项数量
	@XmlAttribute
	private int visibleOptionsNum = 10;
	
	//是否可多选
	@XmlAttribute
	private Boolean multiple = false;
	
	//多选分隔符
	@XmlAttribute
	private String multiSplitChar = ",";
	
	@JSONField(serialize=false)
	private PCComboCompRender  render=null;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	
	public int getVisibleOptionsNum() {
		return visibleOptionsNum;
	}
	public void setVisibleOptionsNum(int visibleOptionsNum) {
		this.visibleOptionsNum = visibleOptionsNum;
	}
	public ComboBoxComp() {
		super();
		setEditorType(EditorTypeConst.COMBODATA);
	}
	public ComboBoxComp(String id) {
		super(id);
		setEditorType(EditorTypeConst.COMBODATA);
	}

	public boolean isImageOnly() {
		return imageOnly;
	}
	public void setImageOnly(boolean imageOnly) {
		this.imageOnly = imageOnly;
	}
	public boolean isOnlySelect() {
		return isOnlySelect;
	}
	public void setOnlySelect(boolean isOnlySelect) {
		this.isOnlySelect = isOnlySelect;
	}
	public String getDataDivHeight() {
		return dataDivHeight;
	}
	public void setDataDivHeight(String dataDivHeight) {
		this.dataDivHeight = dataDivHeight;
	}
	public String getRefComboData() {
		return refComboData;
	}
	public void setRefComboData(String refComboData) {
		this.refComboData = refComboData;
	}
	public ComboData getComboData() {
		ComboData cb = getWidget().getViewModels().getComboData(this.getRefComboData());
		return cb;
	}
	public boolean isAllowExtendValue() {
		return isAllowExtendValue;
	}
	public void setAllowExtendValue(boolean isAllowExtendValue) {
		this.isAllowExtendValue = isAllowExtendValue;
	}
	
	@Override
	public BaseContext getContext() {
		ComboBoxContext comboCtx = new ComboBoxContext();
		comboCtx.setId(this.getId());
//		comboCtx.setEnabled(isEnabled());
//		comboCtx.setValue(getValue());
//		comboCtx.setVisible(this.isVisible());
		return comboCtx;
	}
	
	public Boolean getMultiple() {
		return multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public String getMultiSplitChar() {
		return multiSplitChar;
	}

	public void setMultiSplitChar(String multiSplitChar) {
		this.multiSplitChar = multiSplitChar;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ComboBoxContext comboCtx = (ComboBoxContext)ctx;
		this.enabled = comboCtx.isEnabled();
		this.value = comboCtx.getValue();
		this.isVisible = comboCtx.isVisible();
		setCtxChanged(false);
	}
	public String getRefData() {
		return refData;
	}
	public void setRefData(String refData) {
		this.refData = refData;
	}

	@Override
	public PCComboCompRender getRender() {
		if(render==null){
			render = RenderProxy.getRender(new PCComboCompRender(this));
		}
		return render;
	}
	
	
}
