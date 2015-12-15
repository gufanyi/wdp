package xap.lui.core.json;
import static xap.lui.core.constant.IntDataTypeConst.BigDecimal_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Byte_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Character_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Date_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Double_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Float_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Integer_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Long_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.String_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FBoolean_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDateTime_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDate_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDate_begin;
import static xap.lui.core.constant.IntDataTypeConst.FDate_end;
import static xap.lui.core.constant.IntDataTypeConst.FDouble_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FLiteralDate;
import static xap.lui.core.constant.IntDataTypeConst.FTime_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.boolean_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Boolean_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.byte_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.char_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.double_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.float_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.int_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.long_TYPE;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.constant.DataTypeTranslator;
import xap.lui.core.constant.EventContextConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.EmptyRow;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.PaginationInfo;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.PagePartMeta;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDate;
import xap.mw.coreitf.d.FDateTime;
import xap.mw.coreitf.d.FDouble;
import xap.mw.coreitf.d.FLiteralDate;
import xap.mw.coreitf.d.FTime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public class Json2DatasetDesirializer implements IJson2ObjectDeserialize<Dataset> {
	public static final String PAGEMETA_KEY = "pagemeta";
	@Override
	public Dataset derialize(String obj) {
		return null;
	}
	public Dataset[] serialize(JSONArray datasetJsonObjs, Map<String, Object> paramMap) {
		if(datasetJsonObjs==null)return null;
		ArrayList<Dataset> dss = new ArrayList<Dataset>();
		Map<String, Dataset> dsEleMap = new HashMap<String, Dataset>();
		for (int i = 0; i < datasetJsonObjs.size(); i++) {
			JSONObject datasetJsonObj = datasetJsonObjs.getJSONObject(i);
			{
				String dsId = datasetJsonObj.getString("id");
				String widgetId = datasetJsonObj.getString("widgetId");
				PagePartMeta pagemeta = null;
				if (paramMap != null && paramMap.get(PAGEMETA_KEY) != null) {
					pagemeta = (PagePartMeta) paramMap.get(PAGEMETA_KEY);
				} else
					pagemeta = LuiRuntimeContext.getWebContext().getPageMeta();
				Dataset dataset = pagemeta.getWidget(widgetId).getViewModels().getDataset(dsId);
				this.processDataset(dataset, datasetJsonObj);
				dss.add(dataset);
				dsEleMap.put(dataset.getId(), dataset);
			}
		}
		//DataSet数据集的填充
		fillDataset(datasetJsonObjs, dsEleMap);
		try {
			Dataset[] dsArr = dss.toArray(new Dataset[0]);
			for (int i = 0; i < dsArr.length; i++) {
				dsArr[i].setCtxChanged(false);
				PageData[] rds = dsArr[i].getAllPageData();
				if (rds != null) {
					for (int k = 0; k < rds.length; k++) {
						rds[k].setPageDataSelfChanged(false);
					}
				}
			}
			return dsArr;
		} catch (Exception e) {
			throw new LuiRuntimeException("序列化Dataset时出错");
		}
	}
	protected void fillDataset(JSONArray datasetJsonObjs, Map<String, Dataset> dataSets) {
		for (int i = 0; i < datasetJsonObjs.size(); i++) {
			JSONObject jsonObj = datasetJsonObjs.getJSONObject(i);
			String dsId = jsonObj.getString("id");
			Dataset ds = dataSets.get(dsId);
			if (ds == null) {
				continue;
			}
			String randomRowIndex = jsonObj.getString("randomRowIndex");
			ds.setRandomRowIndex(Integer.parseInt(randomRowIndex));
			JSONObject allPageJsons = jsonObj.getJSONObject("allPage");
			processRowsets(allPageJsons, ds);
		}
	}
	protected void processRowsets(JSONObject rowsetObject, Dataset ds) {
		if (rowsetObject == null) {
			return;
		}
		JSONObject rowset = rowsetObject;
		String pageindex = rowset.getString("pageindex");
		String pagesize = rowset.getString("pageSize");
		String recordcount = rowset.getString("recordcount");
		PaginationInfo pInfo = new PaginationInfo();
		pInfo.setPageSize(Integer.parseInt(pagesize));
		pInfo.setRecordsCount(Integer.parseInt(recordcount));
		pInfo.setPageIndex(Integer.parseInt(pageindex));
		ds.setPaginationInfo(pInfo);
		JSONArray recordsList = rowset.getJSONArray("pageDatas");
		processRecords(recordsList, ds);
	}
	
	private void processRecords(JSONArray recordsList,Dataset ds) {
		if (recordsList != null) {
			int rsize = recordsList.size();
			for (int m = 0; m < rsize; m++) {
				JSONObject recordsNode = recordsList.getJSONObject(m);
				String pageindex = recordsNode.getString("pageindex");
				PageData pageData = new PageData(Integer.parseInt(pageindex));
				ds.addPageData(pageData.getPageindex(), pageData);
				JSONArray selected = recordsNode.getJSONArray("selected");
				if (selected != null) {
					Integer[] selectedIndices = new Integer[selected.size()];
					for (int k = 0; k < selectedIndices.length; k++) {
						selectedIndices[k] = Integer.valueOf(selected.getIntValue(k));
					}
					pageData.setRowSelectIndices(selectedIndices);
					// continue;
				}
				String focusIndex = recordsNode.getString("focusRowIndex");
				if (focusIndex != null && !focusIndex.equals("")) {
					ds.setFocusIndex(Integer.valueOf(focusIndex));
					// continue;
				}
				JSONObject node = recordsNode.getJSONObject("onePage");
				if(node == null) return;
				JSONArray rowJsons = node.getJSONArray("rows");
				if (rowJsons != null) {
					for (int k = 0; k < rowJsons.size(); k++) {
						JSONObject rowJson = rowJsons.getJSONObject(k);
						String rowId = rowJson.getString("id");
						String parentId = rowJson.getString("parentid");
						boolean isEntry=rowJson.getBoolean("isEntry");
						if(!isEntry){
							Row emptyRow = new EmptyRow(rowId);
							pageData.addRow(emptyRow);
							emptyRow.setRowChanged(false);
						}else{
							Row row = ds.getEmptyRow();
							pageData.addRow(row);
							row.setRowId(rowId);
							if (!isNull(parentId))
								row.setParentId(parentId);
							String stateName = rowJson.getString("state");
							row.setState(getState(stateName));
							JSONArray jsonArray = rowJson.getJSONArray("content");
							processXmlToContentRow(ds, jsonArray, row);
							row.setRowChanged(false);
						}
					}
				}
				pageData.setPageDataChanged(false);
				pageData.setPageDataSelfChanged(false);
				
			}
		}
	}
	/**
	 * 根据前台ajax发送的记录请求内容，设置Row对象的各个字段值。
	 * 即将<upd>,,,,,,</upd>中逗号分割的部分根据其类型进行DataSet中 某个Row的设置。
	 * 
	 * @param ds
	 * @param content
	 * @param row
	 */
	public void processXmlToContentRow(Dataset ds, JSONArray content, Row row) {
		// 将""值也转换为数组元素 dengjt
		///String[] fieldValue = content.toJSONString().split(",", -1);
		try {
			int fieldCount = ds.getFieldCount();
			
			for (int i = 0; i < fieldCount && i < content.size(); i++) {
				String value = content.getString(i);
				//String value=jsonObj.toString();
				Field currentField = ds.getField(i);
				String dataTypeStr = currentField.getDataType();
				int dataType = DataTypeTranslator.translateString2Int(dataTypeStr);
				// 恢复空值
				if (value.equals(EventContextConstant.NULL))
					value = null;
				else
					value = URLDecoder.decode(value, "UTF-8");
				processRowContent(currentField, dataType, value, row, i);
			}
		} catch (UnsupportedEncodingException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected void processDataset(Dataset ds, JSONObject datasetJsonObj) {
		ds.clear();
		ds.setCleared(false);
		String editable = datasetJsonObj.getString("editable");
		ds.setEdit(Boolean.parseBoolean(editable));
		JSONObject reqParams = datasetJsonObj.getJSONObject("reqparas");
		if (reqParams != null) {
			Set<String> keySet = reqParams.keySet();
			for (Iterator<String> iter = keySet.iterator(); iter.hasNext();) {
				String key = iter.next();
				Parameter param = new Parameter(key, reqParams.getString(key));
				ds.getReqParameters().addParameter(param);
			}
		}
	}
	public static void processRowContent(Field field, int dataType, String content, Row row, int index) {
		if (content == null) {
			row.setNull(index);
			return;
		}
		String con = content.toLowerCase();
		try {
			switch (dataType) {
			case Integer_TYPE:
				row.setInteger(index, content.equals("") ? null : Integer.parseInt(content));
				break;
			case FDate_begin:
				row.setValue(index, content.equals("") ? null : new FDate(Long.parseLong(content)));
				break;
			case FLiteralDate:
				row.setValue(index, content.equals("") ? null : new FLiteralDate(Long.parseLong(content)));
				break;
			case FDate_end:
				row.setValue(index, content.equals("") ? null : new FDate(Long.parseLong(content)));
				break;
			case int_TYPE:
				row.setInt(index, content.equals("") ? 0 : Integer.parseInt(content));
				break;
			case String_TYPE:
				String newContent = null;
				newContent = content.replaceAll("&lt;", "<");
				newContent = newContent.replaceAll("&amp;", "&");
				row.setString(index, newContent);
				break;
			case Boolean_TYPE:
			case boolean_TYPE:
				con = con.toLowerCase();
				boolean value = con.equals("true") ? true : (con.equals("1") ? true : (con.equals("y") ? true : false));
				row.setBoolean(index, value);
				break;
			case FBoolean_TYPE:
				con = con.toLowerCase();
				value = con.equals("true") ? true : (con.equals("1") ? true : (con.equals("y") ? true : false));
				row.setValue(index, new FBoolean(value));
				break;
			case double_TYPE:
			case Double_TYPE:
				row.setDouble(index, content.equals("") ? 0 : Double.parseDouble(content));
				break;
			case FDouble_TYPE:
				row.setValue(index, content.equals("") ? null : new FDouble(content));
				break;
			case float_TYPE:
			case Float_TYPE:
				row.setFloat(index, content.equals("") ? 0 : Float.parseFloat(content));
				break;
			case byte_TYPE:
			case Byte_TYPE:
				row.setFloat(index, content.equals("") ? 0 : Byte.parseByte(content));
				break;
			case Date_TYPE:
				if (content.length() == 10)
					row.setDate(index, java.sql.Date.valueOf(content));// --此处可能有问题，待改正.
				break;
			case BigDecimal_TYPE:
				if (content != null && !"".equals(content))
					row.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble(content)));
				else
					row.setBigDecimal(index, null);
				break;
			case long_TYPE:
			case Long_TYPE:
				row.setLong(index, Long.parseLong(content));
				break;
			case char_TYPE:
			case Character_TYPE:
				row.setChar(index, content.charAt(0));
				break;
			case FDate_TYPE:
				if (!"".equals(content))
					row.setValue(index, content.equals("") ? null : new FDate(Long.parseLong(content)));
				break;
			case FDateTime_TYPE:
				if (content != null && !content.equals("")) {
					try{
						row.setValue(index, new FDateTime(Long.parseLong(content)));
					}catch(Throwable e){
						row.setValue(index, new FDateTime(content));
					}
					
				}
				break;
			case FTime_TYPE:
				row.setValue(index, FTime.getValidUFTimeString(content));
				break;
			default:
				row.setValue(index, content);
			}
		} catch (Exception e) {
			String value = field.getId();
			throw new LuiRuntimeException(e.getMessage(), "字段：" + value + " 类型错误!");
		}
	}
	private static boolean isNull(String s) {
		if (s == null || s.trim().equals(""))
			return true;
		else
			return false;
	}
	// private static boolean isEmpty(String content) {
	// if (content == null || content.equals(""))
	// return true;
	// return false;
	// }
	/**
	 * 获取行状态
	 * 
	 * @param stateName
	 * @return
	 */
	private static int getState(String stateName) {
		if (stateName.trim().equals("add"))
			return Row.STATE_ADD;
		else if (stateName.trim().equals("upd"))
			return Row.STATE_UPDATE;
		else if (stateName.trim().equals("del"))
			return Row.STATE_DELETED;
		// 假删除的行，在序列化回来时将状态置为删除标志
		else if (stateName.trim().equals("fdel"))
			return Row.STATE_FALSE_DELETED;
		else
			return Row.STATE_NORMAL;
	}
}
