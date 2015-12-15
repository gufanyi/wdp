/**
 * 
 */
package xap.lui.psn.context;

import java.io.Serializable;

import xap.lui.core.control.ModePhase;


/**
 * @author wupeng1
 * @version 6.0 2011-8-22
 * @since 1.6
 */
public interface IPropertyInfo extends Serializable {

	public String getDefaultValue();

	public String getType();

	public String getValue();

	public String getId();

	public String getDsField();

	public String getLabel();

	public String getVoField();

	public boolean isVisible(ModePhase mode);

	public boolean editable(ModePhase mode);

	public IPropertyChangeMonitor getChangeMonitor();

}
