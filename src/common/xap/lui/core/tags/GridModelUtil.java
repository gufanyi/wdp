package xap.lui.core.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.GridColumn;
import xap.lui.core.comps.GridColumnGroup;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.GridTreeLevel;
import xap.lui.core.comps.IGridColumn;
import xap.lui.core.comps.PropertyGridComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.RenderTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.render.BaseLuiDirectiveModel;
import xap.lui.core.render.UIRender;
import xap.lui.core.util.StringUtil;
import xap.lui.core.xml.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class GridModelUtil {

	public static final String GRID_MODEL_TMP = "grid_model_tmp";

	public static String generateGridModel(Dataset ds, GridComp grid, ViewPartMeta widget) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("var " + GRID_MODEL_TMP + " = $.gridcompmodel.getObj();\n");
		if(grid instanceof PropertyGridComp) {
			GridColumn nameCol = new GridColumn();
			nameCol.setId("propName");
			nameCol.setField("text");
			nameCol.setText(((PropertyGridComp) grid).getPropNameText());
			nameCol.setRenderType(RenderTypeConst.DefaultRender);
			nameCol.setDataType(StringDataTypeConst.STRING);
			nameCol.setEdit(false);
			nameCol.setFitWidth(true);
			buf.append(generateGridColumn(nameCol, ds, widget, GRID_MODEL_TMP));
			
			GridColumn valueCol = new GridColumn();
			valueCol.setId("propValue");
			valueCol.setField("value");
			valueCol.setText(((PropertyGridComp) grid).getPropValueText());
			valueCol.setRenderType(RenderTypeConst.DefaultRender);
			valueCol.setDataType(StringDataTypeConst.STRING);
			valueCol.setFitWidth(true);
			buf.append(generateGridColumn(valueCol, ds, widget, GRID_MODEL_TMP));
		} else {
			List<IGridColumn> headers = grid.getColumnList();
			if (headers == null)
				return buf.toString();
			Iterator<IGridColumn> it = headers.iterator();
			while (it.hasNext()) {
				IGridColumn header = (IGridColumn) it.next();
				buf.append(generateGridColumn(header, ds, widget, GRID_MODEL_TMP));
			}
		}

		GridTreeLevel topLevel = grid.getTopLevel();
		if(grid instanceof PropertyGridComp) {
			topLevel = new GridTreeLevel();
			topLevel.setId("propgridTreeLevel_");
			topLevel.setDataset(ds.getId());
			topLevel.setLabelFields("text");
			topLevel.setRecursiveField("id");
			topLevel.setRecursiveParentField("pid");
		}
		if (topLevel != null) {
			String levelId = UIRender.TL_PRE + widget.getId() + "_" + topLevel.getId();
			String levelScript = genScriptForTreeLevel(levelId, topLevel, widget.getId());
			buf.append(levelScript);
			buf.append(GRID_MODEL_TMP + ".setTreeLevel(").append(levelId).append(");\n");
		}

		buf.append(GRID_MODEL_TMP + ".setDataSet(pageUI.getViewPart('" + widget.getId()).append("').getDataset('" + ds.getId() + "'));\n");
		return buf.toString();
	}

	public static String generateGridColumn(IGridColumn header, Dataset ds, ViewPartMeta widget, String modelName) {
		StringBuffer buf = new StringBuffer();
		// 单表头
		if (header instanceof GridColumn) {
			GridColumn curHeader = (GridColumn) header;
			fillDataTypeAndEditorType(ds, curHeader);

			String text = curHeader.getText();
			// if(curHeader.getField() != null)
			String label = text;
			// if (label != null)
			// label = label.replaceAll("'", "\\\\\\\\'");
			// 得到数据类型
			String dataType = curHeader.getDataType();
			if (dataType == null) {
				throw new LuiRuntimeException("GridCompHeader must set DataType!");
			}
			// 得到编辑器类型
			String editorType = curHeader.getEditorType();
			// // 如果数据类型是"FBOOLEAN"并且没有设置编辑类型,则编辑类型设置为"CHECKBOX"
			// if(editorType == null || "".equals(editorType))
			// {
			// if(curHeader.getDataType().equals(StringDataTypeConst.FBOOLEAN))
			// editorType = EditorTypeConst.CHECKBOX;
			// else
			// editorType = EditorTypeConst.STRINGTEXT;
			// }
			// 得到渲染器
			String renderType = curHeader.getRenderType();
			// 是否运行态
			boolean isRunMode = false;
			if (LuiRuntimeContext.getWebContext().getOriginalParameter("eclipse") == null) {
				isRunMode = !LuiRuntimeContext.isEditMode();
			}
			//if (!isRunMode) {
				//renderType = null;
			//}
			// // 对于下拉框类型,如果renderType没有设置,需要根据editorType来填充renderType
			// if((renderType == null || "".equals(renderType)) &&
			// editorType.equals(EditorTypeConst.COMBODATA))
			// {
			// renderType = RenderTypeConst.ComboRender;
			// }
			// 根据数据类型设置textAlign
			String textAlign = curHeader.getAlign();
			if (textAlign == null) {
				if (dataType.equals(StringDataTypeConst.bOOLEAN) || dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.FBOOLEAN))
					textAlign = "center";
				else if (dataType.equals(StringDataTypeConst.Decimal) || dataType.equals(StringDataTypeConst.FDOUBLE) || dataType.equals(StringDataTypeConst.DATE)
						|| dataType.equals(StringDataTypeConst.INTEGER))
					textAlign = "right";
				else
					textAlign = "left";
			}

			int headerWidth = curHeader.getWidth();
			if (headerWidth == -1)
				headerWidth = GridColumn.DEFAULT_WIDTH;
			/**
			 * 对应的参数,sumColRenderFunc为合计单元格的js渲染方法，在include.js中定义，
			 * sumColRenderFunc暂未考虑多表头的情况 keyName, showName, width, dataType,
			 * sortable, isHidden, olumEditable, defaultValue, columBgColor,
			 * textAlign, textColor, isFixedHeader, renderType, editorType,
			 * topHeader, groupHeader, isGroupHeader, isSumCol, isAutoExpand,
			 * isShowCheckBox, sumColRenderFunc
			 * ,,,,,,,,,,:null})
			 */
			buf.append(	"var " + curHeader.getId() + " = $('<div>').gridheader({keyName : '");
					
					buf.append((curHeader.getField() != null ? curHeader.getField() : curHeader.getId()) + "',showName:\"" + label + "\",width:'" + headerWidth + "',dataType:'"
							+ dataType + "',sortable:").append(curHeader.isSort() + ",isHidden:" + (!curHeader.isVisible()) + ",columEditable:" + curHeader.isEdit() + ",defaultValue:'',columBgColor:'").append(
					(curHeader.getColumBgColor() == null ? "" : curHeader.getColumBgColor()) + "',textAlign:'").append(textAlign + "',textColor:'").append(
					(curHeader.getTextColor() == null ? "" : curHeader.getTextColor()) + "',isFixedHeader:" + curHeader.isFixed() + ",renderType:" + renderType + ",editorType:'" + editorType + "',topHeader:null,groupHeader:null,isGroupHeader:" + "null,"
							+"isSumCol:"+ curHeader.isSumCol() + ",isAutoExpand:" + curHeader.isFitWidth() + ",isShowCheckBox:" + curHeader.isShowCheckBox() + ",sumColRenderFunc:"
							+ (curHeader.getSumColRenderFunc() == null ? "null" : curHeader.getSumColRenderFunc()) + "}).gridheader(\"instance\");\n");

			buf.append(curHeader.getId()).append(".id = '").append(curHeader.getId()).append("';\n");

			buf.append(modelName + ".addHeader(" + curHeader.getId() + ");\n");

			if (curHeader.getExtendAttribute("showState") != null) {
				buf.append(curHeader.getId()).append(".setShowState('").append(curHeader.getExtendAttribute("showState")).append("');\n");
			}

			// 设置是否必输项
			if (curHeader.isRequired()) {
				buf.append(curHeader.getId()).append(".setRequired(true);\n");
			}
			// 如果是组合数据类型，生成相应的脚本
			if (curHeader.getEditorType() != null && (curHeader.getEditorType().equals(EditorTypeConst.COMBODATA) || EditorTypeConst.MULTICOMBOBOX.equals(curHeader.getEditorType())))
				generateComboDataScript(widget, buf, curHeader);
			// 多语输入类型
			if (curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.LANGUAGECOMBODATA))
				generateLanguageComboDataScript(widget, buf, curHeader, ds);
			// 如果是参照类型，生成相应的脚本
			else if (curHeader.getEditorType() != null && curHeader.getEditorType().contains(EditorTypeConst.REFERENCE))
				generateRefereceScript(widget, buf, curHeader);
			// 如果是选择框类型，生成相应的脚本
			else if (curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.CHECKBOX))
				generateCheckBoxScript(widget, buf, curHeader);
			// 如果是整型类型，生成相应的脚本
			else if (curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.INTEGERTEXT))
				generateIntegerTextScript(ds, buf, curHeader);
			// 如果是浮点类型，生成相应的脚本
			else if (curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.DECIMALTEXT))
				generateDecimalTextScript(ds, buf, curHeader);
			// 如果是字符类型,生成相应的脚本
			else if (curHeader.getEditorType() != null && curHeader.getEditorType().equals(EditorTypeConst.STRINGTEXT))
				generateStringTextScript(buf, curHeader);
		}
		// 多表头
		else if (header instanceof GridColumnGroup) {
			GridColumnGroup curHeader = (GridColumnGroup) header;
			makeHeaders(widget, buf, curHeader, curHeader, ds, modelName);
		}
		return buf.toString();
	}


	private static String genScriptForTreeLevel(String levelShowId, GridTreeLevel level, String widgetId) {
		StringBuffer buf = new StringBuffer();
		String levelId = levelShowId;
		GridTreeLevel rLevel = level;
		buf.append("var ").append(levelId).append(" = ").append("$.gridtreelevel.getObj(\"").append(rLevel.getId()).append("\",\"").append(rLevel.getRecursiveField()).append("\",\"").append(
				rLevel.getRecursiveParentField()).append("\",").append(StringUtil.mergeScriptArray(rLevel.getLabelFields())).append(",\"").append(
				rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"");
		buf.append(");\n");
		// if (rLevel.getContextMenu() != null) {
		// buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
		// }

		return buf.toString();
	}

	private static void generateCheckBoxScript(ViewPartMeta widget, StringBuffer buf, GridColumn curHeader) {
		DataList data = (DataList) widget.getViewModels().getComboData(curHeader.getRefComboData());
		if (data != null) {
			DataItem[] items = data.getAllDataItems();
			if (items == null || items.length != 2) {
				throw new LuiRuntimeException("The Combodata is not suitable for header:" + curHeader.getId());
			}
			buf.append(curHeader.getId()).append(".setValuePair([\"").append(items[0].getValue()).append("\",\"").append(items[1].getValue()).append("\"]").append(");\n");
		} else {
			if (curHeader.getDataType().equals(StringDataTypeConst.FBOOLEAN)) {
				buf.append(curHeader.getId()).append(".setValuePair([\"Y\",\"N\"]);\n");
			} else if (curHeader.getDataType().equals(StringDataTypeConst.bOOLEAN) || curHeader.getDataType().equals(StringDataTypeConst.BOOLEAN)) {
				buf.append(curHeader.getId()).append(".setValuePair([\"true\",\"false\"]);\n");
			}
		}
	}

	private static void generateRefereceScript(ViewPartMeta widget, StringBuffer buf, GridColumn curHeader) {
		IRefNode refNode = widget.getViewModels().getRefNode(curHeader.getRefNode());
		if (refNode != null) {
			String refId = BaseLuiDirectiveModel.RF_PRE + widget.getId() + "_" + refNode.getId();
			buf.append(curHeader.getId()).append(".setNodeInfo(").append(refId).append(");\n");
		}

	}

	/**
	 * 根据dataset中的数据类型填充GridColumn的dataType，并且获得对应的EditorType
	 * 
	 * @param ds
	 * @param col
	 * @return
	 */
	private static void fillDataTypeAndEditorType(Dataset ds, GridColumn col) {
		if (col.getDataType() == null || col.getDataType().trim().equals("")) {
			Field field = ds.getField(col.getId());
			if (field != null)
				col.setDataType(field.getDataType());
			else
				col.setDataType(StringDataTypeConst.STRING);
		}
		if (col.getEditorType() == null || col.getEditorType().trim().equals(""))
			col.setEditorType(EditorTypeConst.getEditorTypeByString(col.getDataType()));
		if (col.getRenderType() == null || col.getRenderType().trim().equals(""))
			col.setRenderType(RenderTypeConst.getRenderTypeByString(col.getDataType()));
	}

	/**
	 * 为组合数据类型生成相关数据脚本
	 * 
	 * @param buf
	 * @param curHeader
	 */
	private static void generateComboDataScript(ViewPartMeta widget, StringBuffer buf, GridColumn curHeader) {
		buf.append("var comboData = pageUI.getViewPart('").append(widget.getId()).append("').getComboData('").append(curHeader.getRefComboData()).append("');\n");
		buf.append(curHeader.getId()).append(".setHeaderComboBoxComboData(comboData);\n");
	}

	/**
	 * 设置多语下拉项obj
	 * 
	 * @param widget
	 * @param buf
	 * @param curHeader
	 * @param ds
	 */
	private static void generateLanguageComboDataScript(ViewPartMeta widget, StringBuffer buf, GridColumn curHeader, Dataset ds) {
		String maxLength = curHeader.getMaxLength();
		if (maxLength != null && !"".equals(maxLength)) {
			buf.append(curHeader.getId()).append(".setMaxLength(").append(maxLength).append(");\n");
		}

		// 根据字段名称过滤出相关字段，来构造初始化下拉的数据结构，fieldId为控件绑定字段的id，根据此id去查找所有的id
		String fieldId = curHeader.getField();
		List<String> idList = new ArrayList<String>();
		List<String> textList = new ArrayList<String>();

		// 找到字段中已经设置的值
		for (Field field : ds.getFieldList()) {
			String fid = field.getId();
			// 如果dataset中的字段的id与element中绑定的字段的名称匹配，则加入到list
			if (fid != null && fid.length() >= fieldId.length() && fid.startsWith(fieldId)) {
				String tipText = matchLanguageTipWithVos(fieldId, fid);
				if (!StringUtils.isBlank(tipText)) {
					idList.add(fid);
					textList.add(tipText);
				}
			}
		}

		JSONArray jArry = new JSONArray();
		for (int i = 0; i < idList.size(); i++) {
			JSONObject jObj = new JSONObject();
			jObj.put("field", idList.get(i));
			jObj.put("caption", "");
			jObj.put("name", idList.get(i));
			// 应该是valuesList中的值,此时不设置值
			jObj.put("value", "");
			jObj.put("langTip", textList.get(i));
			jArry.add(jObj);
		}
		String jsArray = jArry.toString();

		buf.append("var languageCombos = " + jsArray + ";\n");
		buf.append(curHeader.getId()).append(".setHeaderLanguageComboBoxs(languageCombos);\n");
	}

	/**
	 * 根据字段结尾序号和系统允许的多语VO查询字段对应的多语提示
	 * 
	 * @param fieldId
	 *            控件绑定的字段id
	 * @param fid
	 *            循环的字段id
	 * @return 字段结尾序号对应的语言简称
	 */
	private static String matchLanguageTipWithVos(String fieldId, String fid) {
		String returnLangTip = "";
		return returnLangTip;
	}

	private static void generateStringTextScript(StringBuffer buf, GridColumn curHeader) {
		String maxLength = curHeader.getMaxLength();
		if (maxLength != null && !"".equals(maxLength)) {
			buf.append(curHeader.getId()).append(".setMaxLength(").append(maxLength).append(");\n");
		}
	}

	private static void generateDecimalTextScript(Dataset ds, StringBuffer buf, GridColumn curHeader) {
		String precision = curHeader.getPrecision();
		if (precision == null || "".equals(precision))
			precision = ds.getField(curHeader.getField()).getPrecision();
		if (precision != null && !"".equals(precision)) {
			buf.append(curHeader.getId()).append(".setPrecision(").append(precision).append(");\n");
		}
		String maxFloatValue = curHeader.getMaxValue();
		String minFloatValue = curHeader.getMinValue();
		if (maxFloatValue == null || "".equals(maxFloatValue))
			maxFloatValue = getFieldProperty(ds, curHeader.getField(), Field.MAX_VALUE);
		if (minFloatValue == null || "".equals(minFloatValue))
			minFloatValue = getFieldProperty(ds, curHeader.getField(), Field.MIN_VALUE);
		if (minFloatValue != null && !"".equals(minFloatValue)) {
			buf.append(curHeader.getId()).append(".setFloatMinValue(").append(minFloatValue).append(");\n");
		}

		if (maxFloatValue != null && !"".equals(maxFloatValue)) {
			buf.append(curHeader.getId()).append(".setFloatMaxValue(").append(maxFloatValue).append(");\n");
		}
	}

	private static void generateIntegerTextScript(Dataset ds, StringBuffer buf, GridColumn curHeader) {
		// 首先获取控件的设置属性
		String maxValue = curHeader.getMaxValue();
		String minValue = curHeader.getMinValue();
		// 控件属性没有设置则获取相应的ds field的设置
		if (maxValue == null || "".equals(maxValue))
			maxValue = getFieldProperty(ds, curHeader.getField(), Field.MAX_VALUE);
		if (minValue == null || "".equals(minValue))
			minValue = getFieldProperty(ds, curHeader.getField(), Field.MIN_VALUE);

		if (minValue != null && !"".equals(minValue)) {
			buf.append(curHeader.getId()).append(".setIntegerMinValue(").append(minValue).append(");\n");
		}
		if (maxValue != null && !"".equals(maxValue)) {
			buf.append(curHeader.getId()).append(".setIntegerMaxValue(").append(maxValue).append(");\n");
		}
	}

	private static String getFieldProperty(Dataset ds, String fieldId, String name) {
		Field field = ds.getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}

	/**
	 * 递归构建多表头
	 * 
	 * @param buf
	 * @param header
	 * @param topHeader
	 *            此多表头的最顶层header
	 */
	private static void makeHeaders(ViewPartMeta widget, StringBuffer buf, IGridColumn header, GridColumnGroup topHeader, Dataset ds, String modelName) {
		if (header instanceof GridColumnGroup) {
			GridColumnGroup parentHeader = (GridColumnGroup) header;
			if (parentHeader == topHeader) {
				// 注意此处的visible要传入!visible
				String resId = parentHeader.getI18nName();
				String text = parentHeader.getText();
				String label = text;
				// if (label != null)
				// label = label.replaceAll("'", "\\\\\\\\'");

				buf.append(
						"var " + parentHeader.getId() + "= $('<div>').gridheader({keyName:'" + parentHeader.getId() + "',showName:\"" + label + "\",isHidden:" + (!parentHeader.isVisible())
								+ ",isGroupHeader: true}).gridheader('instance');\n").append(modelName + ".addHeader(" + parentHeader.getId() + ");\n");
				buf.append(parentHeader.getId()).append(".id = '").append(parentHeader.getId()).append("';\n");
				if (parentHeader.getExtendAttribute("showState") != null) {
					buf.append(parentHeader.getId()).append(".setShowState('").append(parentHeader.getExtendAttribute("showState")).append("');\n");
				}
			}

			// 得到此组的所有孩子
			List<IGridColumn> list = parentHeader.getChildColumnList();
			// 记录孩子中是"组"的那个,为继续递归做准备
			IGridColumn groupHeader = null;
			if (list != null && list.size() > 0) {
				Iterator<IGridColumn> it = list.iterator();
				while (it.hasNext()) {
					IGridColumn curHeader = it.next();
					if (curHeader instanceof GridColumnGroup) {
						GridColumnGroup tempHeader = (GridColumnGroup) curHeader;
						// 注意此处的visible要传入!visible
						String resId = tempHeader.getI18nName();
						String text = tempHeader.getText();
						String label = text;
						// if (label != null)
						// label = label.replaceAll("'", "\\\\\\\\'");
						/**
						 * keyName, showName, width, dataType, sortable,
						 * isHidden, olumEditable, defaultValue, columBgColor,
						 * textAlign, textColor, isFixedHeader, renderType,
						 * editorType, topHeader, groupHeader, isGroupHeader,
						 * isSumCol, isAutoExpand, isShowCheckBox,
						 * sumColRenderFunc
						 */
						buf.append("var " + tempHeader.getId() + "= $('<div>').gridheader({keyName:'" + tempHeader.getId() + "',showName:\"" + label + "\", isHidden:" + (!tempHeader.isVisible())
								+ ", topHeader:" + topHeader.getId() + ", groupHeader:" + parentHeader.getId() + ",isGroupHeader:true}).gridheader('instance');\n");
						groupHeader = tempHeader;
						if (tempHeader.getExtendAttribute("showState") != null) {
							buf.append(tempHeader.getId()).append(".setShowState('").append(tempHeader.getExtendAttribute("showState")).append("');\n");
						}
					} else if (curHeader instanceof GridColumn) {
						groupHeader = null;
						GridColumn realHeader = (GridColumn) curHeader;
						fillDataTypeAndEditorType(ds, realHeader);
						// 得到数据类型
						String dataType = realHeader.getDataType();

						// 得到编辑器类型
						String editorType = realHeader.getEditorType();
						// 如果数据类型是"FBOOLEAN"并且没有设置编辑类型,则编辑类型设置为"CHECKBOX"
						if (editorType == null || "".equals(editorType)) {
							if (realHeader.getDataType().equals(StringDataTypeConst.FBOOLEAN))
								editorType = EditorTypeConst.CHECKBOX;
							else
								editorType = "";
						}
						// 得到渲染器
						String renderType = realHeader.getRenderType();
						// 对于下拉框类型,如果renderType没有设置,需要根据editorType来填充renderType
						if ((renderType == null || "".equals(renderType)) && editorType.equals(EditorTypeConst.COMBODATA)) {
							renderType = RenderTypeConst.ComboRender;
						}
						// 是否运行态
						boolean isRunMode = false;
						if (LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParameter("eclipse") == null) {
							isRunMode = !LuiRuntimeContext.isEditMode();
						}
						if (!isRunMode) {
							renderType = null;
						}

						String resId = realHeader.getI18nName();
						String text = realHeader.getText();
						String label = text;
						// if (label != null)
						// label = label.replaceAll("'", "\\\\\\\\'");
						int headerWidth = realHeader.getWidth();
						if (headerWidth == -1)
							headerWidth = GridColumn.DEFAULT_WIDTH;

						String textAlign = realHeader.getAlign();
						if (textAlign == null) {
							if (dataType.equals(StringDataTypeConst.bOOLEAN) || dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.FBOOLEAN))
								textAlign = "center";
							else if (dataType.equals(StringDataTypeConst.Decimal) || dataType.equals(StringDataTypeConst.FDOUBLE) || dataType.equals(StringDataTypeConst.DATE)
									|| dataType.equals(StringDataTypeConst.INTEGER))
								textAlign = "right";
							else
								textAlign = "left";
						}

						/**
						 * keyName, showName, width, dataType, sortable,
						 * isHidden, olumEditable, defaultValue, columBgColor,
						 * textAlign, textColor, isFixedHeader, renderType,
						 * editorType, topHeader, groupHeader, isGroupHeader,
						 * isSumCol, isAutoExpand, isShowCheckBox,
						 * sumColRenderFunc
						 */
						buf.append(
								"var " + realHeader.getId() + " = $('<div>').gridheader({keyName:'" + (realHeader.getField() != null ? realHeader.getField() : realHeader.getId()) + "', showName:\"" + label + "\", width:'"
										+ headerWidth + "', dataType:'" + dataType + "', sortable:").append(
								realHeader.isSort() + ", isHidden:" + (!realHeader.isVisible()) + ", columEditable:" + realHeader.isEdit() + ", textAlign:'" + textAlign + "', isFixedHeader:" + realHeader.isFixed() + ", renderType:"
										+ renderType + ", editorType:'" + editorType + "', topHeader:" + topHeader.getId() + ", groupHeader:" + parentHeader.getId() + ",isGroupHeader:true, isSumCol:" + realHeader.isSumCol() + ", isAutoExpand:" + realHeader.isFitWidth()
										+ ", isShowCheckBox:" + realHeader.isShowCheckBox() + ", sumColRenderFunc:" + (realHeader.getSumColRenderFunc() == null ? "null" : realHeader.getSumColRenderFunc()) + "}).gridheader('instance');\n");

						if (realHeader.getExtendAttribute("showState") != null) {
							buf.append(realHeader.getId()).append(".setShowState('").append(realHeader.getExtendAttribute("showState")).append("');\n");
						}

						// 如果是组合数据类型，生成相应的脚本
						if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.COMBODATA))
							generateComboDataScript(widget, buf, realHeader);
						// 如果是参照类型，生成相应的脚本
						else if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.REFERENCE))
							generateRefereceScript(widget, buf, realHeader);
						// 如果是选择框类型，生成相应的脚本
						else if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.CHECKBOX))
							generateCheckBoxScript(widget, buf, realHeader);
						// 如果是整型类型，生成相应的脚本
						else if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.INTEGERTEXT))
							generateIntegerTextScript(ds, buf, realHeader);
						// 如果是浮点类型，生成相应的脚本
						else if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.DECIMALTEXT))
							generateDecimalTextScript(ds, buf, realHeader);
						else if (realHeader.getEditorType() != null && realHeader.getEditorType().equals(EditorTypeConst.STRINGTEXT))
							generateStringTextScript(buf, realHeader);
					}
					// 递归处理仍为GridColumnGroup类型的组header
					if (groupHeader != null)
						makeHeaders(widget, buf, groupHeader, topHeader, ds, modelName);
				}
			}
		}
	}

	// protected static String getFieldI18nName(Dataset ds, String i18nName,
	// String fieldId, String defaultI18nName, String langDir)
	// {
	// if(i18nName != null && !i18nName.equals("")){
	// if(i18nName.equals("$NULL$"))
	// return "";
	// String value = LanguageUtil.translate(i18nName, defaultI18nName ==
	// null?i18nName:defaultI18nName, langDir);
	// return value == null ? defaultI18nName : value;
	// // return translate(i18nName, defaultI18nName ==
	// null?i18nName:defaultI18nName, langDir);
	// }
	// // if(ds == null)
	// // return defaultI18nName;
	// //
	// // if(fieldId != null){
	// // int fldIndex = ds.nameToIndex(fieldId);
	// // if(fldIndex == -1){
	// // String queryNode = null;
	// // try{
	// // queryNode = (String) AppLifeCycleContext.current()
	// .getApplicationContext().getAppAttribute(
	// // AppControlPlugin.NODECODE);
	// // }
	// // catch(Throwable e){}
	// // queryNode = queryNode == null?"":queryNode;
	// // LuiLogger.error("node: " + queryNode + ",can not find the field:" +
	// fieldId + ",dataset:" + ds.getId());
	// // return defaultI18nName;
	// // }
	// // Field field = ds.getField(fldIndex);
	// // i18nName = field.getI18nName();
	// // String text = field.getText();
	// // String defaultValue = text == null? i18nName : text;
	// // if(i18nName == null || i18nName.equals(""))
	// // return defaultI18nName == null?defaultValue:defaultI18nName;
	// // else{
	// // String value = translate(i18nName, defaultI18nName ==
	// null?defaultValue:defaultI18nName, langDir);
	// // return value == null ? defaultI18nName ==
	// null?defaultValue:defaultI18nName : value;
	// //// return translate(i18nName, defaultI18nName ==
	// null?defaultValue:defaultI18nName, langDir);
	// // }
	// // }
	// else return defaultI18nName;
	// }

}
