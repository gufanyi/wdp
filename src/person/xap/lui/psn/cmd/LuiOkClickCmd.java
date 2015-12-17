package xap.lui.psn.cmd;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.device.DefaultDataValidator;
import xap.lui.core.device.IDataValidator;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.LuiValidateException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.WindowContext;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;

public class LuiOkClickCmd extends LuiCommand{
	private Dataset ds;
	private String parentViewId ;
	private String parentDsId;
	
	public LuiOkClickCmd(String dsId,String parentViewId,String parentDsId){
		this.ds = getCurrentWinCtx().getCurrentViewContext().getView().getViewModels().getDataset(dsId);
		this.parentViewId = parentViewId;
		this.parentDsId = parentDsId;
	}	
	
	public LuiOkClickCmd(Dataset ds,String parentViewId,String parentDsId){
		this.ds = ds;
		this.parentViewId = parentViewId;
		this.parentDsId = parentDsId;
	}
	
	@Override
	public void execute() {
		if(ds==null)throw new LuiRuntimeException("数据集不能为空！");
		Row curRow = ds.getSelectedRow();
		if(curRow==null)return;
		ArrayList<Dataset> detailDs = new ArrayList<Dataset>();
	//	doValidate(ds, detailDs);
		BaseDO curVo = new Dataset2SuperVOSerializer<BaseDO>().serialize(ds, curRow)[0];
		WindowContext curWin = getCurrentWinCtx();
		if(curWin==null)throw new LuiRuntimeException("当前Window不能为空");
		ViewPartContext parentViewContext = curWin.getViewContext(parentViewId);
		if(parentViewContext==null)
			throw new LuiRuntimeException("父窗口不能为空");
		Dataset parentDs = parentViewContext.getView().getViewModels().getDataset(parentDsId);
		if(parentDs==null)
			throw new LuiRuntimeException("父窗口Ds不能为空");
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
		String pk = curRow.getString(ds.nameToIndex(vo.getPKFieldName()));
//		LfwBeanUtil<BaseDO> bu = new LfwBeanUtil<BaseDO>();
		if(StringUtils.isNotBlank(pk)){
			Row selRow = parentDs.getSelectedRow();
			if(selRow==null)throw new LuiRuntimeException("父窗口无选中行");
			modifyVoBeforeUpdate(curVo);
			updateVo(curVo);
//			bu.setUIID(curVo,selRow.getRowId());
		}
		else{
			modifyVoBeforeSave(curVo);
			saveVo(curVo);
//			bu.setUIID(curVo,null);
		}
		new SuperVO2DatasetSerializer().update(new BaseDO[]{curVo},parentDs,new Row[]{curRow});
		AppSession.current().getViewContext().getView().getPagemeta().setHasChanged(false);
		getCurrentWinCtx().closeView(getCurrentWinCtx().getCurrentViewContext().getId());
	}
	protected void saveVo(BaseDO vo){
		try {
			CRUDHelper.getCRUDService().saveBean(vo);
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected void updateVo(BaseDO vo){
		try {
			CRUDHelper.getCRUDService().saveBean(vo);
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected WindowContext getCurrentWinCtx(){
	    return AppSession.current().getAppContext()
					.getCurrentWindowContext();
	  }
	protected void modifyVoBeforeSave(BaseDO vo){
		return;
	}
	protected void modifyVoBeforeUpdate(BaseDO vo){
		return;
	}
	protected void doValidate(Dataset masterDs, List<Dataset> detailDs) throws LuiValidateException {
		ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewCtx.getView();
		IDataValidator validator = getValidator();
		validator.validate(masterDs, widget);
		if (detailDs != null) {
			int size = detailDs.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Dataset ds = detailDs.get(i);
					validator.validate(ds, widget);
				}
			}
		}
	}
	protected IDataValidator getValidator() {
		return new DefaultDataValidator();
	}
}
