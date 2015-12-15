package xap.lui.core.comps;
/**
 * GridColumn标识接口
 *
 */
public interface IGridColumn extends Cloneable{
	public Object clone();
	public void setGridComp(GridComp grid);
	public GridComp getGridComp();
	public boolean isVisible();
	public void setVisible(boolean visible);
	public String getId();
	public String getText();
}
