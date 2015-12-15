package xap.lui.core.mvel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * lui bill service via json
 * @author dengjt
 *
 */
public interface IEditFormularService extends Serializable{
	public Map executeFormular(HashMap valueMap, String exp, HashMap dtMap);
	public String[] matchRefPk(String value, String pageUniqueId, String widgetId, String refNodeId);
	public String[] matchSearch(String value, String pageUniqueId, String widgetId, String refNodeId);
}
