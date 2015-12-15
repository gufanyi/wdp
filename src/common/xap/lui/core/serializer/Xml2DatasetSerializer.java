package xap.lui.core.serializer;

import static xap.lui.core.constant.IntDataTypeConst.BigDecimal_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Byte_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Character_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Date_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Double_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FBoolean_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDate_begin;
import static xap.lui.core.constant.IntDataTypeConst.FDate_end;
import static xap.lui.core.constant.IntDataTypeConst.FLiteralDate;
import static xap.lui.core.constant.IntDataTypeConst.Float_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Integer_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.Long_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.String_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.boolean_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.byte_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.char_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.double_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.float_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.int_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.long_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDouble_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDate_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FDateTime_TYPE;
import static xap.lui.core.constant.IntDataTypeConst.FTime_TYPE;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

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
import xap.lui.core.util.XmlUtilPatch;
import xap.mw.coreitf.d.FBoolean;
import xap.mw.coreitf.d.FDate;
import xap.mw.coreitf.d.FDateTime;
import xap.mw.coreitf.d.FDouble;
import xap.mw.coreitf.d.FLiteralDate;
import xap.mw.coreitf.d.FTime;

public class Xml2DatasetSerializer implements IXml2ObjectSerializer<Dataset[]> {

	public static final String PAGEMETA_KEY = "pagemeta";
	
	public Dataset[] serialize(String xml, Map<String, Object> paramMap) {
		try {
			ArrayList<Dataset> dss = new ArrayList<Dataset>();
			Document doc = XmlUtilPatch.getDocumentBuilder().parse(new InputSource(new StringReader(xml)));
			NodeList datasetList = doc.getElementsByTagName("dataset");
			if (datasetList != null) 
			{
				Map<Dataset, Element> dsEleMap = new HashMap<Dataset, Element>();
				for (int i = 0; i < datasetList.getLength(); i++) 
				{
					Element dsEle = (Element) datasetList.item(i);
					String dsId = dsEle.getAttribute("id");
					String[] ids = dsId.split("\\."); 
					String widgetId = ids[0];
					String datasetId = ids[1];
					
					PagePartMeta pagemeta = null;
					if(paramMap != null && paramMap.get(PAGEMETA_KEY) != null){
						pagemeta = (PagePartMeta) paramMap.get(PAGEMETA_KEY);
					}
					else
						pagemeta = LuiRuntimeContext.getWebContext().getPageMeta();
					Dataset ds = pagemeta.getWidget(widgetId).getViewModels().getDataset(datasetId);
					
					this.processDataset(ds, dsEle);
					dsEleMap.put(ds, dsEle);

					dss.add(ds);
				}
				
				fillDataset(dsEleMap);
			}
			Dataset[] dsArr = dss.toArray(new Dataset[0]);
			for (int i = 0; i < dsArr.length; i++) {
				dsArr[i].setCtxChanged(false);
				PageData[] rds = dsArr[i].getAllPageData();
				if(rds != null){
					for (int k = 0; k < rds.length; k++) {
						rds[k].setPageDataSelfChanged(false);
					}
				}
			}
			return dsArr;
		} 
		catch (Exception e) {
			throw new LuiRuntimeException("序列化Dataset时出错");
		} 
		
	}
	
	protected void fillDataset(Map<Dataset, Element> dsEleMap) {
		// 此步中已经将请求参数和records记录设入了ds中
		Iterator<Entry<Dataset, Element>> it = dsEleMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Dataset, Element> entry = it.next();
			Element dsEle = entry.getValue();
			Dataset ds = entry.getKey();
			NodeList rowsets = dsEle.getElementsByTagName("rowsets");
			if(rowsets != null && rowsets.getLength() > 0){
				NodeList rowsetsList = ((Element)rowsets.item(0)).getElementsByTagName("rowset");
				processRowsets(rowsetsList, ds);
			}
			//processRecords(recordsList, entry.getKey());
			String randomRowIndex = dsEle.getAttribute("randomRowIndex");
			ds.setRandomRowIndex(Integer.parseInt(randomRowIndex));
		}
	}
	
	protected void processRowsets(NodeList rowsetsList, Dataset ds) {
		int size = rowsetsList.getLength();
		for(int i = 0; i < size; i ++){
			Element rowset = (Element) rowsetsList.item(i);
			String pageindex = rowset.getAttribute("pageindex");
			String pagesize = rowset.getAttribute("pagesize");
			String recordcount = rowset.getAttribute("recordcount");
			PaginationInfo pInfo = new PaginationInfo();
			pInfo.setPageSize(Integer.parseInt(pagesize));
			pInfo.setRecordsCount(Integer.parseInt(recordcount));
			pInfo.setPageIndex(Integer.parseInt(pageindex));
			ds.setPaginationInfo(pInfo);
			
			NodeList recordsList = rowset.getElementsByTagName(EventContextConstant.records);
			
			processRecords(recordsList,ds);
//			rowSet.setRowSetChanged(false);
		}
		
		// TODO Auto-generated method stub
	}

	private void processRecords(NodeList recordsList,Dataset ds)
	{
		if (recordsList != null && recordsList.getLength() != 0) {
			int rsize = recordsList.getLength();
			for (int m = 0; m < rsize; m++) {
				Element recordsNode = (Element) recordsList.item(m);
				
				String pageindex = recordsNode.getAttribute("pageindex");
				PageData pageData = new PageData(Integer.parseInt(pageindex));
				
				ds.addPageData(pageData.getPageindex(), pageData);
				NodeList recordList = recordsNode.getChildNodes();
				int size = recordList.getLength();
				for (int i = 0; i < size; i++) {
					Node node = (Node) recordList.item(i);
					if(node instanceof Text)
						continue;
					Element recordEle = (Element) node;
					if(recordEle.getNodeName().equals("selected")){
						String selected = recordEle.getFirstChild() == null? null : recordEle.getFirstChild().getNodeValue();
						if(selected != null && !selected.equals("")){
							String[] sels = selected.split(",");
							Integer[] selectedIndices = new Integer[sels.length];
							for (int k = 0; k < selectedIndices.length; k++) {
								selectedIndices[k] = Integer.valueOf(sels[k]);
							}
							pageData.setRowSelectIndices(selectedIndices);
						}
						continue;
					}
					if(recordEle.getNodeName().equals("focus")){
						String focusIndex = recordEle.getFirstChild() == null? null : recordEle.getFirstChild().getNodeValue();
						if(focusIndex != null && !focusIndex.equals("")){
							ds.setFocusIndex(Integer.valueOf(focusIndex));
						}
						continue;
					}
	//				if(recordEle instanceof)
					String rowId = recordEle.getAttribute("id");
					String parentId = recordEle.getAttribute("parentid");
	
					if(recordEle.getNodeName().equals(EventContextConstant.erecord)){
						Row emptyRow = new EmptyRow(rowId);
						pageData.addRow(emptyRow);
						emptyRow.setRowChanged(false);
					}
					else{
						Row row = ds.getEmptyRow();
						pageData.addRow(row);
						row.setRowId(rowId);
						
						if (!isNull(parentId))
							row.setParentId(parentId);
		
						NodeList stateRecords = recordEle.getChildNodes();
						int j = 0;
						while(stateRecords.item(j) instanceof Text){
							j ++;
						}
						
						Element stateEle = (Element) stateRecords.item(j);
						String stateName = stateEle.getNodeName();
						row.setState(getState(stateName));
		
		//				dataSet.getRowSet().addRow(row);
						if(stateEle.getFirstChild() != null){
							String recordValue = stateEle.getFirstChild().getNodeValue();
							// 处理ajax请求的记录内容并设置Row对象的各个字段值
			                processXmlToContentRow(ds,recordValue, row);
						}
		                row.setRowChanged(false);
					}
				}
				
				pageData.setPageDataSelfChanged(false);
				pageData.setPageDataChanged(false);
			}
		}
	}
	
	/**
	 * 根据前台ajax发送的记录请求内容，设置Row对象的各个字段值。
	 * 即将<upd>,,,,,,</upd>中逗号分割的部分根据其类型进行DataSet中
	 * 某个Row的设置。
	 * 
	 * @param ds
	 * @param content
	 * @param row
	 */
	public void processXmlToContentRow(Dataset ds,String content,Row row)
	{
		//将""值也转换为数组元素 dengjt
		String[] fieldValue = content.split(",", -1);
		try {
			int fieldCount = ds.getFieldCount();
			for(int i = 0;i < fieldCount && i < fieldValue.length; i++)
			{
				String value = fieldValue[i];
		       	Field currentField = ds.getField(i);
		       	String dataTypeStr = currentField.getDataType();
		       	int dataType = DataTypeTranslator.translateString2Int(dataTypeStr);
		       	 //恢复空值
		       	if(value.equals(EventContextConstant.NULL))
		       		value = null;
		       	else
		       		value = URLDecoder.decode(value, "UTF-8");
		       	processRowContent(currentField, dataType, value, row,i);	 
			}
		}
		catch(UnsupportedEncodingException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	
	protected void processDataset(Dataset ds, Element dsEle) {
		ds.clear();
		ds.setCleared(false);
		String editable = dsEle.getAttribute("editable");
		ds.setEdit(Boolean.parseBoolean(editable));
		
		NodeList reqParamList = dsEle.getElementsByTagName(EventContextConstant.req_parameters);
		if (reqParamList != null && reqParamList.getLength() != 0) {
			Node reqParamNode = reqParamList.item(0);
			NodeList paramList = reqParamNode.getChildNodes();
			for (int i = 0; i < paramList.getLength(); i++) {

				Element pElement = (Element) paramList.item(i);
				String name = pElement.getAttribute("name");
				String value = "";
				if(pElement.getFirstChild() != null)
					value = pElement.getFirstChild().getNodeValue();

				Parameter param = new Parameter(name, value);
				ds.getReqParameters().addParameter(param);
			}
		}
		//if (pagecount != null && !pagecount.trim().equals(""))
			//ds.getPaginationInfo().setPageCount(Integer.parseInt(pagecount));
	}
	
	public static void processRowContent(Field field, int dataType,String content,Row row,int index)
	{
		// 后台框架级校验暂时屏蔽,前台负责了校验,如果需要校验则在自己的handler代码中进行!!!
		/*进行规则校验*/
		//if(row.getState() != Row.STATE_DELETED)
			//ruleCheck(field, content, index);
		
		if(content == null){
			row.setNull(index);
			return ;
		}
		String con = content.toLowerCase();
		
		try{
			switch(dataType){
			
			   case Integer_TYPE:
				   row.setInteger(index, content.equals("") ? null: Integer.parseInt(content));
				   break;
			   case FDate_begin:
				   row.setValue(index, content.equals("")? null: new FDate(Long.parseLong(content)));
				   break;
			   case FLiteralDate:
				   row.setValue(index, content.equals("")? null: new FLiteralDate(Long.parseLong(content)));
				   break;
			   case FDate_end:
				   row.setValue(index, content.equals("")? null: new FDate(Long.parseLong(content)));
				   break;
			   case int_TYPE:  
				   row.setInt(index, content.equals("")? 0: Integer.parseInt(content));
			       break;
			   case String_TYPE:
				   String newContent = null;
				   newContent = content.replaceAll("&lt;", "<");
				   newContent = newContent.replaceAll("&amp;", "&");
				   row.setString(index, newContent);
				   break;
			   case boolean_TYPE:
				   con = con.toLowerCase();
				   boolean value = con.equals("true")?true:(con.equals("1")?true:(con.equals("y")?true:false));
			       row.setBoolean(index, value);
			       break;
			   case FBoolean_TYPE:
				   con = con.toLowerCase();
				   value = con.equals("true")?true:(con.equals("1")?true:(con.equals("y")?true:false));
			     //  row.setFBoolean(index, value);
				   row.setValue(index, new FBoolean(value));
			       break;
			   case double_TYPE:
			   case Double_TYPE:
				   row.setDouble(index, content.equals("")? 0 : Double.parseDouble(content));
				   break;
			   case FDouble_TYPE:
				   row.setValue(index, content.equals("")? null : new FDouble(content));
				   break;
			   case float_TYPE :
			   case Float_TYPE :
				   row.setFloat(index, content.equals("")? 0 : Float.parseFloat(content));
				   break;
			   case byte_TYPE:
			   case Byte_TYPE:
				   row.setFloat(index, content.equals("")? 0 : Byte.parseByte(content));
				   break;
			   case Date_TYPE:
				   if(content.length() == 10)
					   row.setDate(index, java.sql.Date.valueOf(content));//--此处可能有问题，待改正.
				   break;
			   case BigDecimal_TYPE:
				   if(content!=null && !"".equals(content))
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
//				   if (content.matches("[0-9]+"))
				   if(!"".equals(content))
					   row.setValue(index, content.equals("")? null: new FDate(Long.parseLong(content)));
//				   else
//					   row.setValue(index, content.equals("")? null: new FDate(content));
				   break;
			   case FDateTime_TYPE:
				   if(content != null && !content.equals("")){
//					   if (content.matches("[0-9]+"))
					   row.setValue(index, new FDateTime(Long.parseLong(content)));
//					   else{
//						   if(content.length() == 10)
//							   content += " 00:00:00";
//						   row.setValue(index, new FDateTime(content));
//					   }
				   }
				   break;
			   case FTime_TYPE:
				   row.setValue(index, FTime.getValidUFTimeString(content));
				   break;
			   default:
				   row.setValue(index, content);			   
			}
		}
		catch(Exception e)
		{
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
	
	private static boolean isEmpty(String content)
	{
		if(content == null || content.equals(""))
			return true;
		return false;
	}
	
	/**
	 * 获取行状态
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
		//假删除的行，在序列化回来时将状态置为删除标志
		else if (stateName.trim().equals("fdel"))
			return Row.STATE_FALSE_DELETED;
		else
			return Row.STATE_NORMAL;
	}
}
