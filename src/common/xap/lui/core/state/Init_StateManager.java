package xap.lui.core.state;

import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.ViewPartMeta;

/**
 * 仅初始态可用判断器
 *
 */
public class Init_StateManager extends AbstractStateManager {

	@Override
	/**
	 * 如果有选中行，或者处于编辑态，则按钮不可用
	 */
	public IStateManager.State getState(WebComp target, ViewPartMeta widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		Row r = ds.getSelectedRow();
		if(r == null)
			return IStateManager.State.ENABLED;
		return ds.isEdit() ? IStateManager.State.DISABLED : IStateManager.State.ENABLED;
	}

}
