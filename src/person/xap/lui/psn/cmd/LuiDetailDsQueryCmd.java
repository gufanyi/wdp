package xap.lui.psn.cmd;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.dataset.Dataset;
import xap.lui.core.exception.LuiRuntimeException;
import xap.mw.core.data.BaseDO;

@SuppressWarnings("rawtypes")
public class LuiDetailDsQueryCmd extends LuiDatasetAfterSelectCmd{
	private String detailDsId;
	private String where;
	private String foreignKeyName;
	private  String tableName;
	private String pkName;
	private Map paramMap;
	private Map langParamMap;
	private String oper="OR";
	
	public LuiDetailDsQueryCmd(String dsId,String detailDsId,String where,String foreignKeyName,String tableName,String pkName,Map paramMap,Map langParamMap,String oper) {
		super(dsId);
		this.detailDsId = detailDsId; 
		this.where = where;
		this.foreignKeyName = foreignKeyName;
		this.tableName = tableName;
		this.pkName = pkName;
		this.paramMap = paramMap;
		this.langParamMap = langParamMap;
		if(oper!=null&&(oper.equalsIgnoreCase("and")||oper.equalsIgnoreCase("or")))
			this.oper = oper;
	}
	
	@SuppressWarnings("unchecked")
	protected String postProcessRowSelectVO(BaseDO vo, Dataset ds) {
		if(detailDsId==null||!detailDsId.equals(ds.getId())){
			ds.setLastCondition(null);
			return null;
		}
		if(StringUtils.isBlank(foreignKeyName))throw new LuiRuntimeException("外键不能为空");
		if(StringUtils.isBlank(tableName))throw new LuiRuntimeException("表名不能为空");
		if(StringUtils.isBlank(pkName))throw new LuiRuntimeException("主键不能为空");
		StringBuffer buffer = new StringBuffer();
		buffer.append(foreignKeyName).append(" IN ");
		buffer.append("(SELECT ").append(pkName).append(" from ").append(tableName);
		if((paramMap!=null&&paramMap.size()!=0)||(langParamMap!=null&&langParamMap.size()!=0))
			buffer.append(" where ");
		if(paramMap!=null&&paramMap.size()!=0){
			Iterator<Entry<String, String>> it = paramMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				String value = getValidSqlValue(entry.getValue());
				buffer.append(entry.getKey()).append(" like '%").append(value).append("%' ");
				if(it.hasNext())buffer.append(oper).append(" ");
			}
			if(langParamMap!=null&&langParamMap.size()!=0)
				buffer.append(oper).append(" ");
		}
		if(langParamMap!=null&&langParamMap.size()!=0){
			Iterator<Entry<String, String>> it = langParamMap.entrySet().iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				buffer.append("(");
				String key = entry.getKey();
				String queryField = getQueryField(key);
				String value = getValidSqlValue(entry.getValue());
				buffer.append(queryField).append(" like '%").append(value).append("%' ");
				if(!queryField.equals(key)){
					buffer.append("OR").append(" ");
					buffer.append("( (");
					buffer.append(queryField).append(" is NULL ").append(" OR ").append(queryField).append(" = ' ' OR ").append(queryField).append(" ='~')");
					buffer.append(" AND ");
					buffer.append(entry.getKey()).append(" like '%").append(value).append("%' ");
					buffer.append(")");
				}
				buffer.append(")");
				if(it.hasNext())
					buffer.append(oper).append(" ");
			}
		}
		buffer.append(")");
		if(where!=null)buffer.append(" and ").append(where);
		if(detailDsId.equals(ds.getId()))ds.setLastCondition(buffer.toString());
		return buffer.toString();
	}
	protected String chanCurrentKey(String keyValue){
		Random rand  = new Random();
		return String.valueOf(rand.nextFloat());
	}
	private String getQueryField(String labelField){
//		LanguageVO currentLanguageVO = MultiLangContext.getInstance().getCurrentLangVO();
//		int index = currentLanguageVO.getLangseq();
//		if(index==1){
			return labelField;
//		}else
//			return labelField + index;
	}
	protected String getValidSqlValue(String value) {
		  if (null != value && !"".equals(value.trim()) && value.contains("'")) {
		    value = value.replaceAll("'", "''");
		  }
		  return value.trim();
		 }
}
