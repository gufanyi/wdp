package xap.lui.core.refmodel;
import java.util.List;
import java.util.Vector;

import xap.lui.core.refrence.AbstractRefListItem;
import xap.lui.core.refrence.IRefListFilter;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefTreeModel;
import xap.lui.core.refrence.RefDftListItem;
import xap.lui.core.refrence.SpellUtil;
public class LuiRefDftListFilter implements IRefListFilter {
	// 最大匹配行
	int maxRow = 20;
	public static boolean isMatch(String searchString, String targetString) {
		// +CnToSpell.getFirstSpell(targetString));
		boolean isMatch = targetString.indexOf(searchString) > -1;
		if (isMatch) {
			return isMatch;
		}
		String[] strs = SpellUtil.getFirstSpells(targetString);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].indexOf(searchString) > -1) {
				return true;
			}
		}
		return false;
	}
	// 最大匹配行
	public AbstractRefListItem[] filter(String inputValue, BaseRefModel model) {
		if (model == null) {
			return new AbstractRefListItem[0];
		}
		String[] fieldNames = model.getBlurFields().split(",");
		if (inputValue == null || inputValue.trim().length() == 0) {
			return new AbstractRefListItem[0];
		}
		String value = inputValue.toLowerCase();
		String[] values = value.split(",");
		value = values[values.length - 1];
		Vector<Object> vMatchedRecords = new Vector<Object>();
		// 重新载入数据
		List<List<Object>> data = getData(model);
		if (model instanceof IRefTreeModel) {
			IRefTreeModel treeModel = (IRefTreeModel) model;
			if (!treeModel.isNotLeafNodeSelected()) {
				data = treeModel.filterNotLeafNode(data);
			}
		}
		if (data != null) {
			int index = 0;
			int size = data.size();
			for (int i = 0; i < size; i++) {
				if (index > maxRow) {
					break;
				}
				List<Object> vRecord = (List<Object>) data.get(i);
				if (vRecord != null) {
					int codeIndex = model.getFieldIndex(model.getCodeField());
					int nameIndex = model.getFieldIndex(model.getNameField());
					for (int j = 0; j < fieldNames.length; j++) {
						int col = model.getFieldIndex(fieldNames[j]);
						if (col < 0 || col >= vRecord.size()) {
							continue;
						}
						Object obj = vRecord.get(col);
						if (obj == null)
							continue;
						String s = obj.toString().toLowerCase();
						if (LuiRefDftListFilter.isMatch(value, s)) {
							AbstractRefListItem item = new RefDftListItem(vRecord, codeIndex, nameIndex);
							vMatchedRecords.addElement(item);
							index++;
							break;
						}
					}
				}
			}
		}
		return (AbstractRefListItem[]) vMatchedRecords.toArray(new RefDftListItem[0]);
	}
	
	private List<List<Object>> getData(BaseRefModel model) {
		// Vector data = null;
		// // 在全集中匹配数据
		//model.setIncludeBlurPart(false);
		// model.setAddEnvWherePart(false);
		int pageSize = model.getPageSize();
		try {
			//model.setPageSize(3000);
			return model.getRefData(0).getData();
		} finally {
			//model.setPageSize(pageSize);
		}
		// // model.setIncludeBlurPart(true); // 恢复原状态
		// return data;
		// return null;
	}
	private List<List<Object>> getData(IRefModel model) {
		// Vector data = null;
		// // 在全集中匹配数据
		//model.setIncludeBlurPart(false);
		// model.setAddEnvWherePart(false);
		int pageSize = model.getPageSize();
		try {
			//model.setPageSize(3000);
			return model.getRefData(0).getData();
		} finally {
			//model.setPageSize(pageSize);
		}
		// // model.setIncludeBlurPart(true); // 恢复原状态
		// return data;
		// return null;
	}

	@Override
	public AbstractRefListItem[] filter(String inputValue, IRefModel model) {
		// TODO Auto-generated method stub
		return null;
	}
}
