package xap.lui.core.device;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IDataBinding;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.EmptyRow;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.PageData;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.util.Validator;
import xap.lui.core.util.Validator.ValidatorType;
/**
 * 默认验证实现
 * 
 * @author guoweic
 * 
 */
public class DefaultDataValidator implements IDataValidator {
	private void validate(Dataset ds) {
		UIPartMeta uimeta = AppSession.current().getViewContext().getUIMeta();
		ViewPartMeta widget = AppSession.current().getViewContext().getView();
		ViewPartComps vc = widget.getViewComponents();
		if (vc != null) {
			WebComp[] wcs = vc.getComps();
			if (wcs != null && wcs.length > 0) {
				// 筛选Form和Gird，排序Form在前，Grid在后
				List<WebComp> comps = new ArrayList<WebComp>();
				for (WebComp wc : wcs) {
					if (uimeta.findChildById(wc.getId()) == null) {// 当前控件未被使用
						continue;
					}
					if (wc instanceof FormComp && ds.getId().equals(((FormComp) wc).getDataset())) {
						comps.add(0, wc);
					} else if (wc instanceof GridComp && ds.getId().equals(((GridComp) wc).getDataset())) {
						comps.add(wc);
					}
				}
				for (WebComp wc : comps) {
					if (wc instanceof FormComp) {
						validate(ds, (FormComp) wc);
					} else if (wc instanceof GridComp) {
						validate(ds, (GridComp) wc);
					}
				}
			}
		}
	}
	/**
	 * 验证片段
	 */
	public void validate(Dataset ds, ViewPartMeta widget) {
		// 如果传入的片段为空 验证数据集
		if (widget == null || widget.getId() == null) {
			validate(ds);
		} else {
			// 否则以form和grid中设置的为准
			WebComp[] comps = widget.getViewComponents().getComps();
			if (comps != null && comps.length > 0) {
				for (WebComp comp : comps) {
					if (comp instanceof IDataBinding) {
						IDataBinding dbcomp = (IDataBinding) comp;
						if (ds.getId().equals(dbcomp.getDataset())) {
							validate(ds, dbcomp);
						}
					}
				}
			}
		}
		showValidateInOldStyle(ds);
	}
	/**
	 * 验证数据绑定组件如form grid
	 * 
	 * @param ds
	 * @param compment
	 */
	private void validate(Dataset ds, IDataBinding compment) {
		if (compment instanceof FormComp) {
			validate(ds, (FormComp) compment);
		} else if (compment instanceof GridComp) {
			validate(ds, (GridComp) compment);
		}
	}
	/**
	 * Form验证
	 * 
	 * @param ds
	 * @param formcomp
	 */
	public void validate(Dataset ds, FormComp fc) {
		// 整合FormElement中的校验规则到Field中.
		if (fc != null) {
			// 隐藏整体错误提示框
			fc.hideErrorMsg();
			List<FormElement> elements = fc.getElementList();
			if (elements != null && elements.size() > 0) {
				Field field = null;
				for (FormElement fe : elements) {
					field = ds.getField(fe.getField());
					if (field != null && field.isRequire()) {// field存在并且允许为空,则取formelement的非空校验规则.
						field.setRequire(fe.isRequire());
					}
				}
			}
		}
		Map<String, Object> validatorMap = validator(ds);
		if (validatorMap == null) {
			return;
		}
		validatorMap = dealValidator(validatorMap, ds);
		// 所有错误信息 key-fieldid value-errorMsg
		Map<String, String> error_fields_map = (Map<String, String>) validatorMap.get("error_fields_map");
		if (error_fields_map != null && !error_fields_map.isEmpty()) {
			UIPartMeta uimeta = AppSession.current().getViewContext().getUIMeta();
			ViewPartMeta widget = AppSession.current().getViewContext().getView();
			ViewPartComps vc = widget.getViewComponents();
			if (fc != null && uimeta.findChildById(fc.getId()) != null && ds.getId().equals((fc.getDataset()))) {
				// 错误类型序号
				int index = 1;
				StringBuffer allErrorMsg = new StringBuffer(Validator.getValidatorMsg(ValidatorType.saveFailure.ordinal()));
				// 平台默认验证-错误信息分类
				Map<Integer, List<Field>> type_fields_map = (Map<Integer, List<Field>>) validatorMap.get("type_fields_map");
				if (type_fields_map != null && !type_fields_map.isEmpty()) {
					List<Field> list = null;
					ValidatorType[] types = ValidatorType.values();
					for (ValidatorType type : types) {
						list = type_fields_map.get(type.ordinal());
						if (list != null && list.size() > 0) {
							index = getFieldErrorMsgByErrorType(allErrorMsg, list, fc, index, Validator.getValidatorMsg(ValidatorType.followingMsg.ordinal()) + Validator.getValidatorMsg(type.ordinal()) + ":");
						}
					}
				}
				// 自定义公式验证-错误信息分类
				Map<String, List<Field>> formular_fields_map = (Map<String, List<Field>>) validatorMap.get("formular_fields_map");
				if (formular_fields_map != null && !formular_fields_map.isEmpty()) {
					List<Field> list = null;
					String key = null;
					Iterator<String> keys = formular_fields_map.keySet().iterator();
					while (keys.hasNext()) {
						key = keys.next();
						list = formular_fields_map.get(key);
						if (list != null && list.size() > 0) {
							index = getFieldErrorMsgByErrorType(allErrorMsg, list, fc, index, key + "：");
						}
					}
				}
				if (index > 1) {
					LuiValidateException exception = new LuiValidateException(allErrorMsg.toString());
					exception.setViewId(widget.getId());
					exception.addComponentId(fc.getId());
					exception.setElementMap(error_fields_map);
					throw exception;
				}
			}
		}
	}
	private int getFieldErrorMsgByErrorType(StringBuffer allErrorMsg, List<Field> list, FormComp fc, int index, String titleError) {
		List<FormElement> fes = fc.getElementList();
		if (fes != null && fes.size() > 0) {
			boolean isNeedToShowError = false;
			FormElement[] formElements = new FormElement[fes.size()];
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Field f = list.get(i);
				int fieldIndex = fc.idToIndex(f.getId());
				if (fieldIndex != -1 && fc.getElementById(f.getId()).isVisible()) {
					formElements[fieldIndex] = fes.get(fieldIndex);
					isNeedToShowError = true;
				}
			}
			if (isNeedToShowError) {
				allErrorMsg.append(((index > 1) ? " " : "") + index + ")");
				index++;
				allErrorMsg.append(titleError);
				boolean temp = false;
				for (int i = 0; i < formElements.length; i++) {
					if (formElements[i] != null) {
						String text = formElements[i].getText();
						allErrorMsg.append(((temp) ? "，" : "") + "“" + text + "”");
						temp = true;
					}
				}
			}
		}
		return index;
	}
	/**
	 * 表格数据验证
	 * 
	 * @param ds
	 * @param gridcomp
	 */
	public void validate(Dataset ds, GridComp gc) {
		if (gc != null) {
			// 隐藏整体错误提示框
			gc.hideErrorMsg();
			List<IGridColumn> columns = gc.getColumnList();
			if (columns != null && columns.size() > 0) {
				Field field = null;
				for (IGridColumn colum : columns) {
					if (colum instanceof GridColumn) {
						field = ds.getField(((GridColumn) colum).getField());
						if (field != null && field.isRequire()) {// field存在并且允许为空,则取gridColumn的非空校验规则.
							field.setRequire(((GridColumn) colum).isRequire());
						}
					}
				}
			}
		}
		Map<String, Object> validatorMap = validator(ds);
		if (validatorMap == null) {
			return;
		}
		validatorMap = dealValidator(validatorMap, ds);
		// 所有错误信息 key-fieldid value-errorMsg
		Map<String, String> error_fields_map = (Map<String, String>) validatorMap.get("error_fields_map");
		if (error_fields_map != null && !error_fields_map.isEmpty()) {
			UIPartMeta uimeta = AppSession.current().getViewContext().getUIMeta();
			ViewPartMeta widget = AppSession.current().getViewContext().getView();
			ViewPartComps vc = widget.getViewComponents();
			if (gc != null && uimeta.findChildById(gc.getId()) != null && ds.getId().equals((gc.getDataset()))) {
				// 错误标题序号
				int index = 1;
				StringBuffer allErrorMsg = new StringBuffer(Validator.getValidatorMsg(ValidatorType.saveFailure.ordinal()));
				// 平台默认验证-错误信息分类
				Map<Integer, List<Field>> type_fields_map = (Map<Integer, List<Field>>) validatorMap.get("type_fields_map");
				if (type_fields_map != null && !type_fields_map.isEmpty()) {
					List<Field> list = null;
					ValidatorType[] types = ValidatorType.values();
					for (ValidatorType type : types) {
						list = type_fields_map.get(type.ordinal());
						if (list != null && list.size() > 0) {
							index = getFieldErrorMsgByErrorType(allErrorMsg, list, gc, index, Validator.getValidatorMsg(ValidatorType.followingMsg.ordinal()) + Validator.getValidatorMsg(type.ordinal()) + "：");
						}
					}
				}
				// 自定义公式验证-错误信息分类
				Map<String, List<Field>> formular_fields_map = (Map<String, List<Field>>) validatorMap.get("formular_fields_map");
				if (formular_fields_map != null && !formular_fields_map.isEmpty()) {
					List<Field> list = null;
					String key = null;
					Iterator<String> keys = formular_fields_map.keySet().iterator();
					while (keys.hasNext()) {
						key = keys.next();
						list = formular_fields_map.get(key);
						if (list != null && list.size() > 0) {
							index = getFieldErrorMsgByErrorType(allErrorMsg, list, gc, index, key + "：");
						}
					}
				}
				if (index > 1) {
					LuiValidateException exception = new LuiValidateException(allErrorMsg.toString());
					exception.setViewId(widget.getId());
					exception.addComponentId(gc.getId());
					exception.setElementMap(error_fields_map);
					throw exception;
				}
			}
		}
	}
	private int getFieldErrorMsgByErrorType(StringBuffer allErrorMsg, List<Field> list, GridComp gc, int index, String titleError) {
		List<IGridColumn> fes = gc.getAllColumnList();
		if (fes != null && fes.size() > 0) {
			boolean isNeedToShowError = false;
			IGridColumn[] gridColumns = new IGridColumn[fes.size()];
			int size = list.size();
			for (int i = 0; i < size; i++) {
				Field f = list.get(i);
				IGridColumn col = gc.getColumnByField(f.getField());
				if (col != null && col.isVisible()) {
					for (int k = 0; k < fes.size(); k++) {
						if (col.getId().equals(fes.get(k).getId())) {
							gridColumns[k] = col;
							break;
						}
					}
					isNeedToShowError = true;
				}
			}
			if (isNeedToShowError) {
				allErrorMsg.append(((index > 1) ? " " : "") + index + ")");
				index++;
				allErrorMsg.append(titleError);
				boolean temp = false;
				for (int i = 0; i < gridColumns.length; i++) {
					if (gridColumns[i] != null) {
						String text = null;
						if (gridColumns[i] instanceof GridColumn) {
							text = ((GridColumn) gridColumns[i]).getText();
						} else if (gridColumns[i] instanceof GridColumnGroup) {
							text = ((GridColumnGroup) gridColumns[i]).getText();
						}
						allErrorMsg.append(((temp) ? "，" : "") + "“" + text + "”");
						temp = true;
					}
				}
			}
		}
		return index;
	}
	/**
	 * 使用弹出窗口显示验证信息
	 * 
	 * @param ds
	 */
	public void showValidateInOldStyle(Dataset ds) {
		Map<String, Object> validatorMap = validator(ds);
		if (validatorMap == null) {
			return;
		}
		validatorMap = dealValidator(validatorMap, ds);
		// 所有错误信息 key-fieldid value-errorMsg
		Map<String, String> error_fields_map = (Map<String, String>) validatorMap.get("error_fields_map");
		if (error_fields_map != null && !error_fields_map.isEmpty()) {
			// 错误类型序号
			int index = 1;
			StringBuffer allErrorMsg = new StringBuffer(Validator.getValidatorMsg(ValidatorType.saveFailure.ordinal()));
			// 分类错误信息
			Map<Integer, List<Field>> type_fields_map = (Map<Integer, List<Field>>) validatorMap.get("type_fields_map");
			if (type_fields_map != null && !type_fields_map.isEmpty()) {
				List<Field> list = null;
				ValidatorType[] types = ValidatorType.values();
				for (ValidatorType type : types) {
					list = type_fields_map.get(type.ordinal());
					if (list != null && list.size() > 0) {
						index = getFieldErrorMsgByErrorType(allErrorMsg, list, index, Validator.getValidatorMsg(ValidatorType.followingMsg.ordinal()) + Validator.getValidatorMsg(type.ordinal()) + ":");
					}
				}
			}
			// 自定义验证信息
			Map<String, List<Field>> formular_fields_map = (Map<String, List<Field>>) validatorMap.get("formular_fields_map");
			if (formular_fields_map != null && !formular_fields_map.isEmpty()) {
				List<Field> list = null;
				String key = null;
				Iterator<String> keys = formular_fields_map.keySet().iterator();
				while (keys.hasNext()) {
					key = keys.next();
					list = formular_fields_map.get(key);
					if (list != null && list.size() > 0) {
						index = getFieldErrorMsgByErrorType(allErrorMsg, list, index, key + "：");
					}
				}
			}
			if (index > 1) {
				throw new LuiRuntimeException(ds.getCaption() + allErrorMsg.toString());
			}
		}
	}
	private int getFieldErrorMsgByErrorType(StringBuffer allErrorMsg, List<Field> list, int index, String titleError) {
		allErrorMsg.append(((index > 1) ? " " : "") + index + ")");
		index++;
		allErrorMsg.append(titleError);
		int size = list.size();
		boolean temp = false;
		for (int i = 0; i < size; i++) {
			Field f = list.get(i);
			allErrorMsg.append(((temp) ? "，" : "") + "“" + f.getText() + "”");
			temp = true;
		}
		return index;
	}
	/**
	 * 验证
	 * 
	 * @param ds
	 * @return
	 */
	private Map<String, Object> validator(Dataset ds) {
		Map<String, Object> validatorMap = new HashMap<String, Object>();
		PageData rd = ds.getCurrentPageData();
		if (rd == null) {
			return null;
		}
		Row[] rows = getValidatorRows(ds);
		if (rows == null || rows.length == 0) {
			return null;
		}
		int count = ds.getFieldCount();
		if (count == 0) {
			return null;
		}
		// formElement错误提示信息类型 key-fieldId value-errorType
		Map<String, Integer> errorTypeMap = new HashMap<String, Integer>();
		// formElement错误提示信息 key-fieldId value-errorMsg
		Map<String, String> formularMsgMap = new HashMap<String, String>();
		// 保存时校验验证公式
		HashMap<String, String> valueMap = new HashMap<String, String>();
		HashMap<String, String> dataTypeMap = new HashMap<String, String>();
		//DefaultEditFormularService editFormular = new DefaultEditFormularService();
		// 保存多语主语种字段
		Map<String, Field> multiMap = new HashMap<String, Field>();
		/*LanguageVO[] lanVos = MultiLangContext.getInstance().getEnableLangVOs();
		if (lanVos != null && lanVos.length > 0) {
			for (int i = 0; i < count; i++) {
				if (fs.getField(i).getExtendAttribute(Field.MDFIELD_MULTILANG) != null && IType.MULTILANGUAGE == (Integer) fs.getField(i).getExtendAttribute(Field.MDFIELD_MULTILANG).getValue()) {
					// 当前字段是多语字段并且是主语种字段
					multiMap.put(fs.getField(i).getId(), fs.getField(i));
					for (LanguageVO vo : lanVos) {
						multiMap.put(fs.getField(i).getId() + vo.getLangseq(), fs.getField(fs.getField(i).getId() + vo.getLangseq()));
					}
				}
			}
		}*/
		for (int j = 0; j < rows.length; j++) {
			Row row = rows[j];
			int rowIndex = ds.getRowIndex(row);
			if (row instanceof EmptyRow) {
				continue;
			}
			for (int k = 0; k < count; k++) {
				Field field = ds.getField(k);
				String fieldId = field.getId();
				boolean isCurrLangField = false;
//				if (field.getExtendAttribute(Field.MDFIELD_MULTILANG) != null && IType.MULTILANGUAGE == (Integer) field.getExtendAttribute(Field.MDFIELD_MULTILANG).getValue()) {
//					// 当前字段是多语字段并且是主语种字段
//				} else if (multiMap.containsKey(fieldId) && fieldId.endsWith(String.valueOf(MultiLangContext.getInstance().getCurrentLangSeq()))) {
//					// 当前字段是多语字段并且是当前语种字段
//					isCurrLangField = true;
//				} else if (multiMap.containsKey(fieldId)) {
//					// 当前字段是多语字段并且不是当前语种字段,不进行校验.
//					continue;
//				}
				String value = null;
				if (row.getValue(k) != null) {
					value = String.valueOf(row.getValue(k));
					value = value.trim();
				}
//				if (isCurrLangField) {
//					fieldId = fieldId.substring(0, fieldId.lastIndexOf(String.valueOf(MultiLangContext.getInstance().getCurrentLangSeq())));
//				}
				if (!field.isPK()) {
					if (field.getSourceField() != null) {
						if (errorTypeMap.get(field.getSourceField() + "_" + rowIndex) != null) {
							errorTypeMap.put(fieldId + "_" + rowIndex, errorTypeMap.get(field.getSourceField() + "_" + rowIndex));
						} else {
							field = ds.getField(field.getSourceField());
							int index = ds.fieldToIndexById(field.getId());
							value = row.getValue(index) instanceof String ? (String) row.getValue(index) : null;
							int type = Validator.validtor(ds.getField(fieldId), value);
							if (type != -1) {
								errorTypeMap.put(fieldId + "_" + rowIndex, type);
							}
						}
					} else {
						int type = Validator.validtor(ds.getField(fieldId), value);
						if (type != -1) {
							errorTypeMap.put(fieldId + "_" + rowIndex, type);
						}
					}
				}
				if (errorTypeMap.get(fieldId + "_" + rowIndex) != null) {// 平台校验未通过,不进行下面的校验.
					continue;
				}
				valueMap.put(field.getId(), value);
				dataTypeMap.put(field.getId(), field.getDataType());
				if (field.getValidateFormula() != null) {
					Map<String, String> map =null;// editFormular.executeFormular(valueMap, field.getValidateFormula(), dataTypeMap);
					if (map != null) {
						String forumValue = (String) map.get("formular_value");
						if (forumValue != null && !forumValue.equals("$NULL$")) {
							LuiValidateException.putFormElementToMap(formularMsgMap, fieldId + "_" + rowIndex, forumValue);
						}
					}
				}
			}
		}
		validatorMap.put("errorTypeMap", errorTypeMap);
		validatorMap.put("formularMsgMap", formularMsgMap);
		return validatorMap;
	}
	/**
	 * 处理验证结果
	 * 
	 * @param validatorMap
	 * @param ds
	 * @return
	 */
	private Map<String, Object> dealValidator(Map<String, Object> validatorMap, Dataset ds) {
		// field错误提示信息类型(平台默认验证) key-fieldId_rowIndex, value-errorType
		Map<String, Integer> errorTypeMap = (HashMap<String, Integer>) validatorMap.get("errorTypeMap");
		// field错误提示信息(自定义验证公式) key-fieldId_rowIndex, value-errorMsg
		Map<String, String> formularMsgMap = (HashMap<String, String>) validatorMap.get("formularMsgMap");
		if (!errorTypeMap.isEmpty() || !formularMsgMap.isEmpty()) {
			// field错误提示信息 key-fieldid_rowIndex, value-errorMsg
			Map<String, String> error_fields_map = new HashMap<String, String>();
			// 错误信息分类包含的field集合
			Map<Integer, List<Field>> type_fields_map = null;
			if (!errorTypeMap.isEmpty()) {
				type_fields_map = new HashMap<Integer, List<Field>>();
				List<Field> list = null;
				String key = null;
				String fieldId = null;
				Iterator<String> keys = errorTypeMap.keySet().iterator();
				while (keys.hasNext()) {
					// key-field_rowIndex
					key = keys.next();
					fieldId = key.substring(0, key.lastIndexOf("_"));
					// 当前错误信息分类包含的field集合
					list = type_fields_map.get(errorTypeMap.get(key));
					if (list == null) {
						list = new ArrayList<Field>();
					}
					boolean temp = true;
					if (ds.getField(fieldId).getSourceField() != null) {// 当前field包含sourcefield，list中移除sourcefield。
						for (Field f : list) {
							if (f.getId().equals(ds.getField(fieldId).getSourceField())) {
								list.remove(f);
								break;
							}
						}
					} else {// 当前field不包含sourcefield，判断当前field是否是list中某个field的sourcefield。
						for (Field f : list) {
							if (ds.getField(fieldId).getId().equals(f.getSourceField())) {
								temp = false;
								break;
							}
						}
					}
					if (temp) {
						list.add(ds.getField(fieldId));
						type_fields_map.put(errorTypeMap.get(key), list);
					}
					error_fields_map.put(key, Validator.getValidatorMsg(errorTypeMap.get(key)));
				}
			}
			// 自定义错误信息分类包含的field集合
			Map<String, List<Field>> formular_fields_map = null;
			if (!formularMsgMap.isEmpty()) {
				formular_fields_map = new HashMap<String, List<Field>>();
				List<Field> list = null;
				String key = null;
				String fieldId = null;
				Iterator<String> keys = formularMsgMap.keySet().iterator();
				while (keys.hasNext()) {
					key = keys.next();
					fieldId = key.substring(0, key.lastIndexOf("_"));
					list = formular_fields_map.get(formularMsgMap.get(key));
					if (list == null) {
						list = new ArrayList<Field>();
					}
					boolean temp = true;
					if (ds.getField(fieldId).getSourceField() != null) {// 当前field包含sourcefield，list中移除sourcefield。
						for (Field f : list) {
							if (f.getId().equals(ds.getField(fieldId).getSourceField())) {
								list.remove(f);
								break;
							}
						}
					} else {// 当前field不包含sourcefield，判断当前field是否是list中某个field的sourcefield。
						for (Field f : list) {
							if (ds.getField(fieldId).getId().equals(f.getSourceField())) {
								temp = false;
								break;
							}
						}
					}
					if (temp) {
						list.add(ds.getField(fieldId));
						formular_fields_map.put(formularMsgMap.get(key), list);
					}
				}
				error_fields_map.putAll(formularMsgMap);
			}
			// 平台默认验证-错误信息分类
			validatorMap.put("type_fields_map", type_fields_map);
			// 自定义公式验证-错误信息分类
			validatorMap.put("formular_fields_map", formular_fields_map);
			// field错误信息集合
			validatorMap.put("error_fields_map", error_fields_map);
		}
		return validatorMap;
	}
	/**
	 * 获取要校验的数据行Row集合
	 * 
	 * @param ds
	 * @return
	 */
	public Row[] getValidatorRows(Dataset ds) {
		if (ds.getAllChangedRows().length > 0) {
			return ds.getAllChangedRows();
		} else {
			return ds.getAllSelectedRows();
		}
	}
}
