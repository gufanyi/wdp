package xap.lui.core.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;

import xap.lui.core.constant.EventContextConstant;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.EmptyRow;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.FieldRelations;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.ParameterSet;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.dataset.WhereOrder;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.util.JsURLEncoder;
import xap.mw.coreitf.d.FDate;
import xap.mw.coreitf.d.FDateTime;
import xap.mw.coreitf.d.FDouble;
import xap.mw.coreitf.d.FLiteralDate;
import xap.sys.jdbc.handler.ArrayListHandler;

public class Dataset2JsonSerializer implements IObject2JsonSerializer<Dataset> {

	private Dataset ds = null;

	public Dataset2JsonSerializer(Dataset dataset) {
		this.ds = dataset;
	}

	@Override
	public JSONObject serialize(Dataset dataset) {
		this.ds = dataset;
		processFieldRelations(ds); // 关联字段的信息（id和name的显示）
		JSONObject dsJsonObj = getDatasetJson(); // 数据集信息
		JSONObject pageDatasJsonObj = getPageDatasJson(); // 查询数据信息
		dsJsonObj.put("allPage", pageDatasJsonObj); // allPage 里面/* 分页信息 */和/*
													// 记录集信息 */
		return dsJsonObj;
	}

	/**
	 * 字段关联处理函数 参照_name的显示 在dataset的fieldRelation中配置
	 * 
	 * @param ds
	 */
	private void processFieldRelations(Dataset ds) {
		FieldRelations frs = ds.getFieldRelations();
		if (frs != null) {
			FieldRelation[] rels = frs.getFieldRelations();
			processFieldRelation(ds, rels);
		}
	}

	/**
	 * 字段关联处理
	 * 
	 * @param ds
	 * @param rels
	 */
	public void processFieldRelation(Dataset ds, FieldRelation[] rels) {
		if (rels == null || rels.length == 0 || ds.getCurrentPageRowCount() == 0)
			return;
		for (int i = 0; i < rels.length; i++) {
			FieldRelation rel = rels[i];
			if (rel.isNeedProcess() == false)
				continue;
			try {
				String sql = getQuerySql(ds, rel);
				List<Object[]> result = null;
				if (sql != null) {
					result = (List<Object[]>) CRUDHelper.getCRUDService().executeQuery(sql, new ArrayListHandler());
				} else {
					result = new ArrayList<Object[]>();
				}
				// 如果查出数据为空，则增加一行，使后面置空逻辑可继续运行
				if (result.size() == 0)
					result.add(null);

				Iterator<Object[]> it = result.iterator();
				WhereField whereField = rel.getWhereFieldList().get(0);
				Field wf = ds.getField(whereField.getValue());
				if (wf == null)
					continue;
				// 外键字段
				String key = wf.getId();
				// 关联字段
				MatchField[] mfs = rel.getMatchFields();
				int[] mfIndices = new int[mfs.length];
				for (int j = 0; j < mfIndices.length; j++) {
					mfIndices[j] = ds.nameToIndex(mfs[j].getWriteField());
					if (mfIndices[j] == -1) {
						throw new LuiRuntimeException("参照配置有问题:" + mfs[j].getWriteField() + "在" + ds.getId() + "中不存在");
					}
				}
				Dataset refDs = ds.getWidget().getViewModels().getDataset(rel.getRefDataset());
				if (refDs == null) {
					continue;
				}

				// 记录是否关联过的标识，以便于处理多选关联
				PageData[] pageDataArr = ds.getAllPageData();
				Row[] rows = new Row[ds.getAllRowCount()];
				int rowIndex = 0;
				for (PageData pageData : pageDataArr) {
					for (Row row : pageData.getRows()) {
						rows[rowIndex] = row;
						rowIndex++;
					}
				}
				boolean[][] notFirst = new boolean[rows.length][];
				for (int j = 0; j < notFirst.length; j++) {
					notFirst[j] = new boolean[mfIndices.length];
				}
				while (it.hasNext()) {
					Object[] values = it.next();
					int keyIndex = ds.nameToIndex(key);
					for (int j = 0; j < rows.length; j++) {
						if (rows[j] instanceof EmptyRow || rows[j] == null)
							continue;
						String keyValue = rows[j].getValue(keyIndex) == null ? null : rows[j].getValue(keyIndex).toString();
						if (keyValue != null && values != null) {
							// 将主键值分隔
							String[] keyValues = keyValue.split(",");
							for (int m = 0; m < keyValues.length; m++) {
								// 找出匹配的主键值，将对应的值字段写入
								if (values[values.length - 1] != null && (values[values.length - 1] != null && values[values.length - 1].toString().trim().equals(keyValues[m].trim()) || (values[values.length - 2] != null && values[values.length - 2].toString().trim().equals(keyValues[m].trim())))) {
									for (int k = 0; k < mfIndices.length; k++) {
										Object value = values[k];
										int mfIndex = mfIndices[k];
										Object oldValue = rows[j].getValue(mfIndex);
										// 原来的值不为空，有2种情况。一种是残留值，这种情况应该去掉。另一种是上一循环设置的，这种情况应该拼上
										// ","
										if (oldValue != null) {
											if (notFirst[j][k]) {
												if (!mfs[k].getReadField().equals(whereField.getKey())) {
													value = oldValue.toString() + "," + value;
													rows[j].setValue(mfIndex, value);
												}
											} else {
												notFirst[j][k] = true;
												rows[j].setValue(mfIndex, value);
											}
										} else {
											notFirst[j][k] = true;
											rows[j].setValue(mfIndex, value);
										}
									}
									break;
								}
							}
						}
						// 将数据置空
						else {
							for (int k = 0; k < mfIndices.length; k++) {
								Object value = null;
								int mfIndex = mfIndices[k];
								if (mfIndex != -1)
									rows[j].setValue(mfIndex, value);
							}
						}
					}
				}
				if (rel.getChildRelationList() != null) {
					processFieldRelation(ds, rel.getChildRelationList().toArray(new FieldRelation[0]));
				}
			} catch (Exception e) {
				throw new LuiRuntimeException(e.getMessage());
			}
		}
	}

	private JSONObject getDatasetJson() {
		JSONObject dsJsonObj = new JSONObject();
		dsJsonObj.put("id", ds.getId());
		dsJsonObj.put("focusIndex", ds.getFocusIndex());
		dsJsonObj.put("editable", ds.isEdit());
		dsJsonObj.put("isCleared", ds.isCleared());
		dsJsonObj.put("randomRowIndex", ds.getRandomRowIndex());
		ParameterSet resParamSet = ds.getResParameters();
		int count = resParamSet.size();
		if (count != 0) {
			dsJsonObj.put(EventContextConstant.res_parameters, resParamSet);
		}
		return dsJsonObj;
	}

	private JSONObject getPageDatasJson() {
		JSONObject jsonObj = new JSONObject();
		int pagecount = ds.getPaginationInfo().getPageCount();
		int pageSize = ds.getPaginationInfo().getPageSize();
		int recordscount = ds.getPaginationInfo().getRecordsCount();
		int pageindex = ds.getPaginationInfo().getPageIndex();
		jsonObj.put("pagecount", pagecount);
		jsonObj.put("pageSize", pageSize);
		jsonObj.put("recordcount", recordscount);
		jsonObj.put("pageindex", pageindex);
		PageData[] pageDatas = ds.getAllPageData();
		int count = pageDatas.length;
		if (count == 0) {
			return jsonObj;
		}
		List<JSONObject> list = new ArrayList<JSONObject>();
		for (int k = 0; k < count; k++) {
			PageData rdata = pageDatas[k];
			if (rdata.isPageDataChanged()) {
				JSONObject pageDataJsonObj = genOnePage(rdata);
				list.add(pageDataJsonObj);
			}
		}
		if (list.size() == 0) {
			return jsonObj;
		} else {
			jsonObj.put("pageDatas", list);
		}
		return jsonObj;
	}

	private JSONObject genOnePage(PageData rdata) {
		JSONObject pageDataJsonObj = new JSONObject();
		pageDataJsonObj.put("pageindex", rdata.getPageindex());
		pageDataJsonObj.put("changed", rdata.isPageDataSelfChanged());
		pageDataJsonObj.put("selected", rdata.getSelectIndices());
		pageDataJsonObj.put("onePage", this.genRowPage(rdata));
		return pageDataJsonObj;
	}

	private Map<String, Object> genRowPage(PageData data) {
		Map<String, Object> map = new HashMap<String, Object>();
		int size = data.getCurrentPageRowCount();
		List<JSONObject> emptyRows = new ArrayList<JSONObject>();
		List<JSONObject> entryRows = new ArrayList<JSONObject>();
		for (int i = 0; i < size; i++) {
			JSONObject rowJsonObj = new JSONObject();
			Row row = data.getRow(i);
			boolean isEmpty = (row instanceof EmptyRow);
			String state = null;
			if (!isEmpty) {
				// emptyRows.add(rowJsonObj);
				switch (row.getState()) {
				case Row.STATE_ADD:
					state = "add";
					break;
				case Row.STATE_UPDATE:
					state = "upd";
					break;
				case Row.STATE_DELETED:
					state = "del";
					break;
				default:
					state = "nrm";
					break;
				}
			}
			rowJsonObj.put("id", row.getRowId());
			rowJsonObj.put("state", state);
			rowJsonObj.put("isEdit", row.isEdit());
			rowJsonObj.put("isAllowMouseoverChange", row.isAllowMouseoverChange());
			if (StringUtils.isNotBlank(row.getParentId())) {
				rowJsonObj.put("parentid", row.getParentId());
			}
			if (isEmpty) {
				emptyRows.add(rowJsonObj);
			} else {
				rowJsonObj.put("content", genOneRecord(row));
				rowJsonObj.put(EventContextConstant.changed, row.getChangedIndices());
				entryRows.add(rowJsonObj);
			}
		}
		if (emptyRows.size() != 0) {
			map.put("emptyRows", emptyRows);
		}
		if (entryRows.size() != 0) {
			map.put("entryRows", entryRows);
		}
		try {
			Row[] rows = data.getDeleteRows();
			if (rows == null || rows.length == 0) {
				return map;
			}
			size = rows.length;
			List<String> deleteIds = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				deleteIds.add(rows[i].getRowId());
			}
			map.put("deleteRows", deleteIds);
		} catch (Exception e) {
			LuiLogger.error(e);
		}
		return map;
	}

	private List<Object> genOneRecord(Row row) {
		int fieldCount = row.size();
		List<Object> content = new ArrayList<Object>();
		Object value = null;
		for (int j = 0; j < fieldCount; j++) {
			value = row.getValue(j);
			if (value != null) {
				if (row.getContent()[j] instanceof FDate) {
					value = ((FDate) row.getContent()[j]).toString();
				} else if (row.getContent()[j] instanceof FDateTime) {
					value = ((FDateTime) row.getContent()[j]).toString();
					// value = ((FDateTime) row.getContent()[j]).getMillis();
				} else if (row.getContent()[j] instanceof FLiteralDate) {
					value = ((FLiteralDate) row.getContent()[j]).getMillis();
				} else if (row.getContent()[j] instanceof String) {
					value = ((String) row.getContent()[j]).replaceAll("<", "&lt;");
					value = ((String) row.getContent()[j]).replaceAll("&", "&amp;");
				} else if (row.getContent()[j] instanceof FDouble) {
					Field field = ds.getField(j);
					if (field.getPrecision() != null) {
						int precision = Integer.valueOf(field.getPrecision());
						FDouble doubleValue = (FDouble) row.getContent()[j];
						if (doubleValue.getPower() != -precision) {
							row.getContent()[j] = new FDouble(doubleValue.getDouble(), precision);
							value = row.getValue(j);
						}
					}
				}
				content.add(JsURLEncoder.encode(value.toString(), "UTF-8"));
			} else {
				content.add(EventContextConstant.NULL);
			}
		}
		return content;
	}

	private String getQuerySql(Dataset ds, FieldRelation rel) {
		// 获取RefMdDataset 进行sql组装
		Dataset refDs = ds.getWidget().getViewModels().getDataset(rel.getRefDataset());
		if (refDs == null) {
			return null;
		}
		if (rel.getWhereFieldList() == null || rel.getWhereFieldList().size() == 0) {
			return null;
		}
		MatchField[] mfs = rel.getMatchFields();
		// TODO
		WhereField where = rel.getWhereFieldList().get(0);
		WhereOrder whereOrder = rel.getWhereOrder();
		String tableName = getRefTableName(refDs);
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		for (int i = 0; i < mfs.length; i++) {
			Field f = refDs.getField(mfs[i].getReadField());
			if (f == null) {
				return null;
			}
			sql.append(tableName + ".");
			sql.append(f.getId());
			if (i != mfs.length - 1) {
				sql.append(",");
			}
		}
		sql.append("," + tableName + ".").append(refDs.getField(where.getKey()).getId());
		sql.append(" from ");
		sql.append(tableName + " " + tableName);
		if (whereOrder != null) {
			if (whereOrder.getBeforWhere() != null) {
				sql.append(whereOrder.getBeforWhere());
			}
		}
		sql.append(" where ").append(tableName + ".").append(refDs.getField(where.getKey()).getId()).append(" in (");
		PageData[] rowSetArr = ds.getAllPageData();
		Set<String> inSet = new HashSet<String>();
		if (rowSetArr != null) {
			for (PageData rowSet : rowSetArr) {
				Row[] rows = rowSet.getRows();
				int whereIndex = ds.nameToIndex(where.getValue());
				if (whereIndex == -1)
					return null;
				for (int i = 0; i < rows.length; i++) {
					if (rows[i] instanceof EmptyRow)
						continue;
					Object wValue = rows[i].getValue(whereIndex);
					String wValueStr = "";
					if (wValue != null) {
						wValueStr = wValue.toString();
						String[] strs = wValueStr.split(",");
						for (int j = 0; j < strs.length; j++) {
							inSet.add(strs[j]);
						}
					}
				}
			}
		}
		if (inSet.size() == 0)
			return null;
		Iterator<String> it = inSet.iterator();
		while (it.hasNext()) {
			String value = it.next();
			sql.append("'");
			sql.append(value);
			sql.append("'");
			if (it.hasNext())
				sql.append(",");
		}
		sql.append(")");
		if (whereOrder != null) {
			if (whereOrder.getAfterWhere() != null && !whereOrder.getAfterWhere().isEmpty()) {
				sql.append(" and ");
				sql.append(whereOrder.getAfterWhere());
			}
			if (whereOrder.getOrder() != null && !whereOrder.getOrder().isEmpty()) {
				sql.append(" order by ");
				sql.append(whereOrder.getOrder());
			}
		}
		return sql.toString();
	}

	private String getRefTableName(Dataset refDs) {
		return refDs.getTableName();
	}

}
