package xap.lui.psn.cmd;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.CRUDHelper;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.WindowContext;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.mw.core.data.BaseDO;
import xap.mw.core.data.DOStatus;
import xap.sys.jdbc.pk.PKBaseAlgorithm;

public class LuiSaveCmd extends LuiCommand{
	private Dataset ds;
	private boolean isEditView=false;
	private String parentViewId;
	private String parentDsId;
	
	public LuiSaveCmd(Dataset ds) {
		this.ds = ds;
	}
	public LuiSaveCmd(String dsId) {
		this.ds = getCurrentWinCtx().getCurrentViewContext().getView().getViewModels().getDataset(dsId);
	}
	public LuiSaveCmd(boolean isEditView,String parentViewId,String parentDsId) {
		this.isEditView=isEditView;
		this.parentViewId=parentViewId;
		this.parentDsId=parentDsId;
		this.ds = getCurrentWinCtx().getCurrentViewContext().getView().getViewModels().getDataset(parentDsId);
	}
	@Override
	public void execute() {
		if(ds==null)
			throw new LuiRuntimeException("数据集不能为空！");
		Row[] updateRows= ds.getStatusRows(Row.STATE_UPDATE);
		if(updateRows.length!=0){
			BaseDO[] updates = new Dataset2SuperVOSerializer<BaseDO>().serialize(ds, updateRows);
			for(int i=0;i<updates.length;i++){
				BaseDO rul = updates[i];
				rul.setStatus(DOStatus.UPDATED);
			}
			updates = updateVo(updates);;
			for(int i=0;i<updates.length;i++){
				updateRows[i].setState(Row.STATE_NORMAL);
			}
			saveafter(updates,updateRows);
		}
		Row[] newRows=ds.getStatusRows(Row.STATE_ADD);
		if(newRows.length!=0){
			BaseDO[] news = new Dataset2SuperVOSerializer<BaseDO>().serialize(ds, newRows);
			news = saveVo(news);
			for(int i=0;i<news.length;i++){
				newRows[i].setState(Row.STATE_NORMAL);
			}
			saveafter(news,newRows);
		}
	}

	protected BaseDO[]  saveVo(BaseDO[] vos){
		try {
			BaseDO[] newVos = new BaseDO[vos.length];
			for(int i=0; i<vos.length; i++){
				BaseDO vo = vos[i];
				BaseDO newDo = getNewDO(vo);
				CRUDHelper.getCRUDService().saveBean(newDo);
				newVos[i] = newDo;
			}
			return newVos;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	
	protected BaseDO[] updateVo(BaseDO[] vos){
		try {
			BaseDO[] newVos = new BaseDO[vos.length];
			for(int i=0; i<vos.length; i++){
				BaseDO vo = vos[i];
				BaseDO newDo = getNewDO(vo);
				CRUDHelper.getCRUDService().saveBean(newDo);
				newVos[i] = newDo;
			}
			return newVos;
		} catch (Throwable e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	
	private BaseDO getNewDO(BaseDO vo) {
		if(StringUtils.isBlank(vo.getPkVal())){//若主键没值，则生成一个值
			String newpk = genId(vo);
			vo.setPkVal(newpk);
			String[] attrs = vo.getAttrNames();
			if(includeDs(attrs,vo)){
				vo.setAttrVal("Ds", "0");//若有Ds字段，且值为空，则设值
			}
		}
		return vo;
	}
	private boolean includeDs(String[] attrs, BaseDO vo) {
		for(String att : attrs){
			if(StringUtils.equals("Ds", att) && (null == vo.getAttrVal("Ds")))
				return true;
		}
		return false;
	}
	public static long OID_BASE_INITIAL_VAL = 19000000000000l;
	public String genId(BaseDO vo){
		String newOid = "";
		newOid = PKBaseAlgorithm.getInstance(
			String.valueOf(OID_BASE_INITIAL_VAL)).nextPKBase();
	    return newOid;
	}
	
	
	private void saveafter(BaseDO[] rules,Row[] row){
		if(isEditView){
			if (StringUtils.isBlank(parentViewId))
				throw new LuiRuntimeException("父页面窗口ID不能为空");
			if (StringUtils.isBlank(parentDsId))
				throw new LuiRuntimeException("父页面主数据集ID不能为空");
			ViewPartContext parentViewCtx = getLifeCycleContext().getWindowContext().getViewContext(parentViewId);
			if (parentViewCtx == null)
				throw new LuiRuntimeException("父窗口不能为空");
			ViewPartMeta parentWidget = parentViewCtx.getView();
			Dataset parentDs = parentWidget.getViewModels().getDataset(parentDsId);
			if (parentDs == null)
				throw new LuiRuntimeException("父窗口主数据集不能为空");
			Row emptRow=parentDs.getEmptyRow();
			new SuperVO2DatasetSerializer().update(rules, parentDs,new Row[]{emptRow});
			parentDs.addRow(emptRow);
			parentDs.setEdit(false);
			getLifeCycleContext().getViewContext().getView().getPagemeta().setHasChanged(false);
			getLifeCycleContext().getAppContext().getCurrentWindowContext().closeView(AppSession.current().getViewContext().getId());
			
		}else{
			row=new SuperVO2DatasetSerializer().vo2DataSet(rules, ds, row);
			ds.setEdit(false);
		}
	}
	
	public Dataset getDs() {
		return ds;
	}
	public void setDs(Dataset ds) {
		this.ds = ds;
	}

	protected WindowContext getCurrentWinCtx(){
	    return AppSession.current().getAppContext()
					.getCurrentWindowContext();
	}
}
