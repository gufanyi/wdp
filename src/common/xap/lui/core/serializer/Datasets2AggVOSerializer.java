package xap.lui.core.serializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.constant.ExtAttrConstants;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;
import xap.mw.coreitf.i.IAggDesc;
import xap.mw.coreitf.i.IBaseDO;
import xap.mw.coreitf.i.IDODesc;
import xap.sys.appfw.orm.model.agg.BaseAggDO;


public class Datasets2AggVOSerializer {
	/**
	 * 将数据集组装成聚合VO
	 * @param masterDs
	 * @param detailDss
	 * @param aggVoClazz
	 * @return
	 */
	public BaseAggDO serialize(Dataset masterDs, Dataset[] detailDss, String aggVoClazz) 
	{
		if(masterDs != null)
		{
			BaseAggDO aggVo = (BaseAggDO) LuiClassUtil.newInstance(aggVoClazz);
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] pvos = ser.serialize(masterDs, masterDs.getSelectedRow());
			if(pvos == null || pvos.length == 0)
				return null;
			aggVo.setParentDO(pvos[0]);
			if(aggVo instanceof BaseAggDO){
				if(detailDss != null && detailDss.length > 0)
				{
					for(int i = 0; i < detailDss.length; i++)
					{
						Dataset detailDs = detailDss[i];
						BaseDO[] vos = ser.serialize(detailDs);
						if(vos==null||vos.length==0)continue;//2015年12月1日 10:17:00 李宝亮修改，原来这里是break；如果第一个detailDs为空，后边的Detail不为空，则后边的DetailDs无法序列号
						String tableId = null;
						Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
						if(tabcode != null)
							tableId = tabcode.toString();
						else{
							Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
							if(parentField != null)
								tableId = parentField.toString();
						}
						if(tableId == null)
							tableId = detailDs.getId();
						
						((BaseAggDO)aggVo).setChildrenDO(vos);
//						.setTableVO(tableId, vos);
					}
				}
				return aggVo;
			}
			else{
				if(detailDss != null && detailDss.length > 0)
				{
					Dataset detailDs = detailDss[0];
					BaseDO[] vos = ser.serialize(detailDs);
					aggVo.setChildrenDO(vos);
				}
				return aggVo;
			}
		}
		return null;
	}
	
	
	/**
	 * 根据masterDs、masterDS的选中行得到序列化后的aggvo
	 * @param masterDs
	 * @param detailDss
	 * @param aggVoClazz
	 * @return
	 */
	
	public BaseAggDO serialize(Dataset masterDs, Row masterRow, Dataset[] detailDss, String aggVoClazz) 
	{
		if(masterDs != null)
		{
			BaseAggDO aggVo = (BaseAggDO) LuiClassUtil.newInstance(aggVoClazz);
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] pvos = ser.serialize(masterDs, masterRow);
			aggVo.setParentDO(pvos[0]);
			if(aggVo instanceof BaseAggDO){
				if(detailDss != null && detailDss.length > 0)
				{
					for(int i = 0; i < detailDss.length; i++)
					{
						Dataset detailDs = detailDss[i];
						BaseDO[] vos = ser.serialize(detailDs);
						String tableId = null;
						Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
						if(tabcode != null)
							tableId = tabcode.toString();
						else{
							Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
							if(parentField != null)
								tableId = parentField.toString();
						}
						if(tableId == null)
							tableId = detailDs.getId();
						((BaseAggDO)aggVo).setChildrenDO(vos);
//						setTableVO(tableId, vos);
					}
				}
				return aggVo;
			}
			else{
				if(detailDss != null && detailDss.length > 0)
				{
					Dataset detailDs = detailDss[0];
					BaseDO[] vos = ser.serialize(detailDs);
					aggVo.setChildrenDO(vos);
				}
				return aggVo;
			}
		}
		return null;
	}
	
	
	/**
	 * 将数据集组装成聚合VO
	 * 
	 * @param masterDs
	 * @param aggVoClazz
	 * @return
	 */
	public BaseAggDO[] serialize(Dataset masterDs,String aggVoClazz) 
	{
		if(masterDs != null)
		{
			List<BaseAggDO> aggVos = new ArrayList<BaseAggDO>();
			Dataset2SuperVOSerializer ser = new Dataset2SuperVOSerializer();
			BaseDO[] pvos = ser.serialize(masterDs, masterDs.getChangedRows());
			for (int i = 0; i< pvos.length; i++){
				BaseAggDO aggVo = (BaseAggDO) LuiClassUtil.newInstance(aggVoClazz);				
				aggVo.setParentDO(pvos[i]);
				aggVos.add(aggVo);
			}
			return aggVos.toArray(new BaseAggDO[0]);
		}
		return null;
	}
	
	/**
	 * 用aggVo中的数据更新ds中的数据
	 * @param aggVO
	 * @param masterDs
	 * @param detailDss
	 */
	public void update(BaseAggDO aggVO, Dataset masterDs, Dataset[] detailDss) {
		SuperVO2DatasetSerializer svds=new SuperVO2DatasetSerializer();
		Row mselectrow= masterDs.getSelectedRow();
		if(mselectrow!=null)
			svds.update(new BaseDO[]{(BaseDO) aggVO.getParentDO()}, masterDs,new Row[]{mselectrow});
		if(detailDss != null && detailDss.length > 0){
			if(aggVO instanceof BaseAggDO){
				List<IBaseDO> childDos= getAllAggDoChildDos(aggVO);
				for(int i=0;i<detailDss.length;i++){
					Dataset detailDs=detailDss[i];
					String tableName= detailDs.getTableName();
					List<BaseDO> listBaseDo=new ArrayList<BaseDO>();
					for(IBaseDO childDo:childDos){
						if(StringUtils.equals(childDo.getTableName(), tableName)){
//							Row[] dselectrows= detailDs.getChangedRows();
							listBaseDo.add((BaseDO)childDo);
							//svds.update(new BaseDO[]{(BaseDO)childDo}, detailDs,dselectrows);
						}
					}
					detailDs.clear();
					BaseDO[] baseDOs=(BaseDO[])listBaseDo.toArray(new BaseDO[listBaseDo.size()]);
					svds.serialize(baseDOs, detailDs);
				}
				
			}
		}
	}
	public static List<IBaseDO> getAllAggDoChildDos(BaseAggDO aggVo){
		IAggDesc billMeta = aggVo.getAggDesc();
		List<IBaseDO> dos=new ArrayList< IBaseDO>();
		IDODesc[] doDesc=	billMeta.getChildren();
		for(IDODesc inner:doDesc){
			IBaseDO[] tmp=aggVo.getChildren(inner);
			if(tmp!=null)
				dos.addAll(Arrays.asList(tmp));
		}
		return dos;
	}
	
	private Dataset getDataset(Dataset[] dss, String id){
		for (int i = 0; i < dss.length; i++) {
			String tableId = null;
			Dataset detailDs = dss[i];
			Object tabcode = detailDs.getExtendAttributeValue(ExtAttrConstants.TAB_CODE);
			if(tabcode != null)
				tableId = tabcode.toString();
			else{
				Object parentField = detailDs.getExtendAttributeValue(ExtAttrConstants.PARENT_FIELD);
				if(parentField != null)
					tableId = parentField.toString();
			}if(tableId == null)
				tableId = detailDs.getId();
			if(tableId.equals(id))
				return dss[i];
		}
		return null;
	}
}
