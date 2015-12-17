package xap.lui.psn.cmd;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.WindowContext;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.lui.psn.guided.SigTabPopUpOper;
import xap.mw.core.data.BaseDO;

public class LuiAddOrEditBeforeShowCmd extends LuiCommand {
	private Dataset ds;
	private Map<String, Object> addParMap = new HashMap<String, Object>();
	
	public LuiAddOrEditBeforeShowCmd(String dsId){
		this.ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(dsId);
	}
	
	public LuiAddOrEditBeforeShowCmd(Dataset ds){
		this.ds = ds;
	}
	
	@Override
	public void execute() {
		if(ds==null)throw new LuiRuntimeException("数据集不能为空！");
		ds.clear();
		WindowContext curWin = getCurrentWinCtx();
		if(curWin==null)throw new LuiRuntimeException("当前Window不存在！");
		String oper = (String) curWin.getAppAttribute(LuiAddOrEditMenuClickCmd.OPERATE_STATUS);
		if(StringUtils.isBlank(oper))
			throw new LuiRuntimeException("操作状态不能为空！");
		if(LuiAddOrEditMenuClickCmd.ADD_OPER.equals(oper)){
			setAddPar();
			Row emptyRow = ds.getEmptyRow();
			if(addParMap!=null&&addParMap.size()!=0){
				Iterator<Entry<String, Object>> it = addParMap.entrySet().iterator();
				while(it.hasNext()){
					Entry<String, Object> entry = it.next();
					emptyRow.setValue(ds.nameToIndex(entry.getKey()), entry.getValue());
				}
			}
			ds.addRow(emptyRow);
		}
		else if(LuiAddOrEditMenuClickCmd.EDIT_OPER.equals(oper)){
			String clazz = ds.getVoMeta();
			if(clazz == null)
				return;
			BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
			String pkValue = (String) curWin.getAppAttribute(vo.getPKFieldName());
			if(pkValue==null || pkValue.trim().length()==0)return;
			try {
				vo = (BaseDO) CRUDHelper.getCRUDService().getBeanById(vo.getClass(), pkValue);
			} catch (Throwable e) {
				throw new LuiRuntimeException(e);
			}
			new SuperVO2DatasetSerializer().serialize(new BaseDO[]{vo}, ds);
		}
		else throw new LuiRuntimeException("操作状态异常！");
		ds.setSelectedIndex(0);
		ds.setEdit(true);
		 ViewPartComps viewPartComps= LuiAppUtil.getCntView().getViewComponents();
		 if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_List)){//主表处于列表（非编辑）状态
			 viewPartComps.getComponent(SigTabPopUpOper.editView_btnSaveId).setVisible(true);
			 ButtonComp buttonOk= (ButtonComp)viewPartComps.getComponent(SigTabPopUpOper.editView_btnOkId);
			 if(buttonOk!=null)
				buttonOk.setVisible(false);
		}else if(StringUtils.equals((String)LuiAppUtil.getAppAttr(ToolBarItemStatusCtrl.toolBarStatus), ToolBarItemStatusCtrl.parTabStatus_Edit)){
			ButtonComp buttonOk= (ButtonComp)viewPartComps.getComponent(SigTabPopUpOper.editView_btnOkId);
			if(buttonOk!=null){
				buttonOk.setVisible(true);
				viewPartComps.getComponent(SigTabPopUpOper.editView_btnSaveId).setVisible(false);
			}
		}
			
		
	}
	public void setAddParMap(Map<String, Object> addParMap) {
		this.addParMap = addParMap;
	}
	public Map<String, Object> getAddParMap() {
		return addParMap;
	}
	protected WindowContext getCurrentWinCtx(){
	    return AppSession.current().getAppContext()
					.getCurrentWindowContext();
	  }
	protected void setAddPar(){
		return;
	}

}
