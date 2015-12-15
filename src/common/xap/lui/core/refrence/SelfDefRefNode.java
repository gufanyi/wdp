package xap.lui.core.refrence;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.model.LifeCyclePhase;

/**
 * 自定义类型参照，表明参照页面，参照逻辑都统一由开发者控制
 * 
 * 
 */
@XmlRootElement(name="SelfRefNode")
@XmlAccessorType(XmlAccessType.NONE)
public class SelfDefRefNode extends BaseRefNode {
	private static final long serialVersionUID = 5017526449328725295L;
//	@XmlAttribute
//	private String width;
//	@XmlAttribute
//	private String height;
	@XmlAttribute
	private String writeFields;
	@XmlAttribute
	private String writeDs;
	@XmlAttribute
	private String path;
	@XmlAttribute
	private boolean isShowLine = true;
	@XmlAttribute
	private boolean multiSel=true;
//	public String getWidth() {
//		return width;
//	}
//	public void setWidth(String width) {
//		this.width = width;
//		super.setDialogWidth(width);
//	}
//	public String getHeight() {
//		return height;
//	}
//	public void setHeight(String height) {
//		this.height = height;
//		super.setDialogHeight(height);
//	}
	public SelfDefRefNode() {
		super();
	}
	public String getWriteFields() {
		return writeFields;
	}
	public void setWriteFields(String writeFields) {
		this.writeFields = writeFields;
	}
	public String getWriteDs() {
		return writeDs;
	}
	public void setWriteDs(String writeDs) {
		this.writeDs = writeDs;
	}
	public boolean isShowLine() {
		return isShowLine;
	}
	public void setShowLine(boolean isShowLine) {
		this.isShowLine = isShowLine;
	}
	 public void setWidth(String dialogWidth) {
		super.setWidth(dialogWidth);
	}
	 public void setHeight(String dialogHeight) {
		super.setHeight(dialogHeight);
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
		if(LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setRefNodePath(this);
		}
	}
	public boolean isMultiSel() {
		return multiSel;
	}
	public void setMultiSel(boolean multiSel) {
		this.multiSel = multiSel;
	}
}
