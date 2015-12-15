package xap.lui.core.pluginout;

import java.util.ArrayList;
import java.util.List;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.ViewPartContext;


public class PlugoutTypeDatasetAllRow implements IPlugoutType {

	@Override
	public void buildSourceWidgetRule(WidgetRule widgetRule, String source) {
		DatasetRule dsr = new DatasetRule();
		dsr.setId(source);
		dsr.setType(DatasetRule.TYPE_ALL_LINE);
		widgetRule.addDsRule(dsr);
	}

	@Override
	public Object fetchContent(PipeOutItem item, ViewPartContext viewCtx) {
		String dsId = item.getSource();
		Dataset ds = viewCtx.getView().getViewModels().getDataset(dsId);
		if(ds.getCurrentPageData() == null)
			return null;
		Row[] rows = ds.getCurrentPageData().getRows();
		TranslatedRows transRows = translate(ds, rows);
		return transRows;
	}

	private TranslatedRows translate(Dataset ds, Row[] rows) {
		TranslatedRows transRows = new TranslatedRows();
		Field[] fields = ds.getFields();
		for (int i = 0; i < fields.length; i++) {
			transRows.setValue(fields[i].getId(), getValueList(rows, i));
		}
		return transRows;
	}

	private List<Object> getValueList(Row[] rows, int index) {
		List<Object> valueList = new ArrayList<Object>();
		for (int i = 0; i< rows.length; i++){
			valueList.add(rows[i].getValue(index));
		}
		return valueList;
	}
	
}
