package xap.lui.core.refrence;
import xap.lui.core.builder.LuiObj;
import xap.lui.core.model.ViewPartMeta;
public interface IRefNode extends Cloneable,LuiObj {
	public void setWidget(ViewPartMeta widget);
	public String getId();
	public void setId(String id);
	public Object clone();
	public void setRendered(boolean b);
}
