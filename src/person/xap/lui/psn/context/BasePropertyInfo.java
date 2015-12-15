/**
 * 
 */
package xap.lui.psn.context;

import java.util.LinkedHashMap;
import java.util.Map;

import xap.lui.core.control.ModePhase;


/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public abstract class BasePropertyInfo implements IPropertyInfo{
	private static final long serialVersionUID = -7273347104924710780L;
	private String defaultValue;
	private String id;
	private String value;
	private String dsField;
	private String label;
	private Map<ModePhase,Boolean> visible = new LinkedHashMap<ModePhase, Boolean>();
	private Map<ModePhase,Boolean> editable = new LinkedHashMap<ModePhase, Boolean>();
	private String type;
	public static ModePhase[] all = new ModePhase[]{ModePhase.eclipse, ModePhase.nodedef, ModePhase.persona};
	IPropertyChangeMonitor pcm;
	private String voField;
	
	@Override
	public String getDefaultValue() {
		return defaultValue;
	}
	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getValue() {
		return value;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDsField() {
		return dsField;
	}

	public void setDsField(String dsField) {
		this.dsField = dsField;
	}

	public boolean isVisible(ModePhase mode) {
		Boolean vsb = this.visible.get(mode);
		return vsb != null ? vsb.booleanValue() : false;
	}

	public void setVisible(ModePhase mode,boolean visible) {
		this.visible.put(mode, Boolean.valueOf(visible));
	}
	public void setVisible(ModePhase[] modes,boolean visible) {
		for(ModePhase mode : modes)
			this.visible.put(mode, Boolean.valueOf(visible));
	}
	public void setVisible(boolean visible) {
		setVisible(all, visible);
	}
	public String getLabel() {
		return label == null ? id : label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVoField() {
		return voField;
	}

	public void setVoField(String voField) {
		this.voField = voField;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean editable(ModePhase mode) {
		Boolean vsb = this.editable.get(mode);
		return vsb != null ? vsb.booleanValue() : true;
	}

	public void setEditable(  ModePhase mode, boolean editable) {
		this.editable.put(mode, Boolean.valueOf(editable));
	}
	public void setEditable(  boolean editable) {
		setEditable(all, editable);
	}
	
	
	public void setEditable(  ModePhase[] modes, boolean editable) {
		for(ModePhase mode : modes)
			this.editable.put(mode, Boolean.valueOf(editable));
	}
	@Override
	public IPropertyChangeMonitor getChangeMonitor() {
		return pcm;
	}
	public void setChangeMonitor(IPropertyChangeMonitor pcm) {
		this.pcm = pcm;
	}
}
