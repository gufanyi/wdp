package xap.lui.psn.cmd;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartMeta;
import xap.sys.appfw.orm.model.agg.BaseAggDO;


public abstract class AbstractWidgetController {

	public abstract String getMasterDsId();

	protected String getAggVoClazz() {
		return BaseAggDO.class.getName();
	}

	
	protected String[] getDetailDsIds() {
		ViewPartMeta  widget = AppSession.current().getViewContext()
				.getView();
		if (widget.getViewModels().getDsrelations() != null) {
			DatasetRelation[] rels = widget.getViewModels().getDsrelations()
					.getDsRelations(getMasterDsId());
			if (rels != null) {
				String[] detailDsIds = new String[rels.length];
				for (int i = 0; i < rels.length; i++) {
					detailDsIds[i] = rels[i].getDetailDataset();
				}
				return detailDsIds;
			}
		}
		return null;
	}

	protected List<Dataset> getDetailDs(String[] detailDsIds){
		ViewPartMeta widget = AppSession.current().getViewContext()
		.getView();
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
		if (detailDsIds != null && detailDsIds.length > 0) {
			for (int i = 0; i < detailDsIds.length; i++) {
				Dataset ds = widget.getViewModels().getDataset(detailDsIds[i]);
				if (ds != null)
					detailDs.add(ds);
			}
		}
		return detailDs;
	}
	
	protected boolean bodyNotNull() {
		return true;
	}

	protected void doValidate(Dataset masterDs)
			throws LuiValidateException{
	}
}
