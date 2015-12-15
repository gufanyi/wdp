package xap.lui.core.layout;

import xap.lui.core.comps.GridColumn;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCGridCompRender;

public class UIGridComp extends UIComponent {

	private static final long serialVersionUID = -881316663820028640L;

	public static final String AUTOEXPAND = "autoExpand";
	private Integer autoExpand;

	public UIGridComp() {
		this.autoExpand = UIConstant.TRUE;
	}

	public Integer getAutoExpand() {
		return autoExpand;
	}

	public void setAutoExpand(Integer autoExpand) {
		this.autoExpand = autoExpand;
	}

	/**
	 * @param ele
	 *            删除表头列
	 */
	public void removeElement(GridColumn ele) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().deleteColumn(ele);
		}

	}

	/**
	 * @param ele
	 *            添加表头列
	 */
	public void addElement(GridColumn ele) {
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addColumn(ele);
		}
	}

	public PCGridCompRender getRender() {
		return (PCGridCompRender) super.getRender();
	}

}
