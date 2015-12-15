package xap.lui.core.pluginout;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.PipeOutItem;
import xap.lui.core.model.ViewPartContext;
public class PlugoutTypeDatasetSelRow implements IPlugoutType {
	@Override public Object fetchContent(PipeOutItem item, ViewPartContext viewCtx) {
		String dsId = item.getSource();
		Dataset ds = viewCtx.getView().getViewModels().getDataset(dsId);
		Row row = ds.getSelectedRow();
		TranslatedRow transRow = translate(ds, row);
		return transRow;
	}
	private TranslatedRow translate(Dataset ds, Row row) {
		TranslatedRow transRow = new TranslatedRow();
		if (row == null) {
			return transRow;
		}
		Field[] fields = ds.getFields();
		for (int i = 0; i < fields.length; i++) {
			transRow.setValue(fields[i].getId(), row.getValue(i));
		}
		return transRow;
	}
	@Override public void buildSourceWidgetRule(WidgetRule widgetRule, String source) {
		DatasetRule dsr = new DatasetRule();
		dsr.setId(source);
		dsr.setType(DatasetRule.TYPE_CURRENT_LINE);
		widgetRule.addDsRule(dsr);
	}
}
