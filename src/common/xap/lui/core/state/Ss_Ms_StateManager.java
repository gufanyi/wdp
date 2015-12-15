package xap.lui.core.state;

import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.model.ViewPartMeta;

/**
 * 仅单选态,多选态可用判断器
 *
 */
public class Ss_Ms_StateManager extends AbstractStateManager {

	@Override
	public IStateManager.State getState(WebComp target, ViewPartMeta widget) {
		Dataset ds = getCtrlDataset(widget);
		if(ds == null)
			return IStateManager.State.ENABLED;
		Row[] rs = ds.getCurrentPageSelectedRows();
		if(rs == null)
			return IStateManager.State.DISABLED;
		return rs.length > 0 ? IStateManager.State.ENABLED : IStateManager.State.DISABLED;
	}
}
