package xap.lui.core.refrence;
import java.util.List;
import java.util.Vector;
public class RefDftListFilter implements IRefListFilter {
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
//	@Override
//	public AbstractRefListItem[] filter(String inputValue, IRefNode refNode, Vector vec) {
//		if (refNode == null)
//			return null;
//		GenericRefNode refnode = (GenericRefNode) refNode;
//		if (inputValue == null || inputValue.trim().length() == 0) {
//			return new AbstractRefListItem[0];
//		}
//		String value = inputValue.toLowerCase();
//		String[] values = value.split(",");
//		value = values[values.length - 1];
//		Vector<Object> vMatchedRecords = new Vector<Object>();
//		// 重新载入数据
//		List<List<Object>> data = getData(vec, refnode);
//		if (data != null) {
//			int index = 0;
//			int size = data.size();
//			for (int i = 0; i < size; i++) {
//				if (index > maxRow) {
//					break;
//				}
//				List<Object> vRecord = (List<Object>) data.get(i);
//				if (vRecord != null) {
//					Object obj = vRecord.get(0);
//					String s = obj == null ? "" : obj.toString().toLowerCase();
//					if (RefDftListFilter.isMatch(value, s)) {
//						AbstractRefListItem item = new RefDftListItem(vRecord, 0, 1);
//						vMatchedRecords.addElement(item);
//						index++;
//					}
//				}
//			}
//		}
//		return (AbstractRefListItem[]) vMatchedRecords.toArray(new RefDftListItem[0]);
//	}
//	private List<List<Object>> getData(Vector vec, GenericRefNode refnode) {
//		List<List<Object>> res = new ArrayList<List<Object>>();
//		if (vec != null && vec.size() > 0) {
//			for (int index = 0; index < vec.size(); index++) {
//				List<Object> list = new ArrayList<Object>();
//				Vector v = (Vector) vec.get(index);
//				if (v.size() >= 2) {
//					list.add(0, v.get(0));
//					list.add(1, v.get(1));
//				}
//				res.add(list);
//			}
//		}
//		return res;
//	}
	// 最大匹配行
	@Override
	public AbstractRefListItem[] filter(String inputValue, IRefModel model) {
		if (model == null) {
			return new AbstractRefListItem[0];
		}
		String[] fieldNames = model.getBlurFields();
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
					int codeIndex = model.getFieldIndex(model.getRefCodeField());
					int nameIndex = model.getFieldIndex(model.getRefNameField());
					for (int j = 0; j < fieldNames.length; j++) {
						int col = model.getFieldIndex(fieldNames[j]);
						if (col < 0 || col >= vRecord.size()) {
							continue;
						}
						Object obj = vRecord.get(col);
						if (obj == null)
							continue;
						String s = obj.toString().toLowerCase();
						if (RefDftListFilter.isMatch(value, s)) {
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
}
