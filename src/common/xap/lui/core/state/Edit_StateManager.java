package xap.lui.core.state;

import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.model.ViewPartMeta;

/**
 * 编辑态可用判断器
 *
 */
public class Edit_StateManager extends AbstractStateManager {

	@Override
	public IStateManager.State getState(WebComp target, ViewPartMeta widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		return ds.isEdit() ? IStateManager.State.ENABLED : IStateManager.State.DISABLED;
	}
}
