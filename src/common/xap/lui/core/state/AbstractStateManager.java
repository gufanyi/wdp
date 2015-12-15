package xap.lui.core.state;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.IRefDataset;
import xap.lui.core.model.ViewPartMeta;

public abstract class AbstractStateManager implements IStateManager {
	protected Dataset getCtrlDataset(ViewPartMeta widget){
		Dataset[] dss = widget.getViewModels().getDatasets();
		if(dss == null || dss.length == 0)
			return null;
		Dataset findDs = null;
		for (int i = 0; i < dss.length; i++) {
			Dataset ds = dss[i];
			if(ds instanceof IRefDataset)
				continue;
			findDs = ds;
		}
		return findDs;
	}
}
