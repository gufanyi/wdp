package xap.lui.psn.cmd;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.pluginout.TranslatedRow;


public final class LuiRowTranslator {
	
	public static Row translateRowToRow(Dataset ds, TranslatedRow transRow){
		Row row = ds.getEmptyRow();
		String[] keys = transRow.getKeys();
		for (int i = 0; i < keys.length; i++) {
			row.setValue(ds.nameToIndex(keys[i]), transRow.getValue(keys[i]));
		}
		return row;
	}
	
	public static Row translateRowToRow(Dataset ds, Row row, TranslatedRow transRow){
		String[] keys = transRow.getKeys();
		for (int i = 0; i < keys.length; i++) {
			row.setValue(ds.nameToIndex(keys[i]), transRow.getValue(keys[i]));
		}
		return row;
	}
	
	public static Row translateRowToRow(Dataset ds, Row transRow){
		return (Row) transRow.clone();
	}
}
