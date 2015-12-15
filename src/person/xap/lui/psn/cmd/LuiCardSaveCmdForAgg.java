package xap.lui.psn.cmd;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.dao.PtBaseDAO;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.WindowContext;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;
import xap.sys.jdbc.facade.DAException;


public class LuiCardSaveCmdForAgg extends LuiCommand {
	private String dsId;
	private String cardLayoutId;
	private String navDatasetId;

	public LuiCardSaveCmdForAgg(String dsId, String cardLayoutId, String navDatasetId) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.navDatasetId = navDatasetId;
	}
	
	public void execute() {
		ViewPartContext viewCtx = getLifeCycleContext().getViewContext();
		ViewPartMeta widget = viewCtx.getView();
		UICardLayout cardLayout = (UICardLayout) viewCtx.getUIMeta().findChildById(this.cardLayoutId);
		if(this.cardLayoutId == null)
			throw new LuiRuntimeException("未指定卡片id!");
		if(cardLayout == null)
			throw new LuiRuntimeException("卡片不存在,id=" + cardLayoutId + "!");
		
		if (this.dsId == null)
			throw new LuiRuntimeException("未指定主数据集id!");
		Dataset ds = widget.getViewModels().getDataset(dsId);
		if(ds==null)throw new LuiRuntimeException("数据集不能为空！");
		
		Dataset navDs = widget.getViewModels().getDataset(navDatasetId);
		if(navDs==null)
			throw new LuiRuntimeException("导航Ds不能为空");
		Row curRow = ds.getSelectedRow();
		if(curRow==null)return;
		
		BaseDO curVo = new Dataset2SuperVOSerializer<BaseDO>().serialize(ds, curRow)[0];
		String clazz = ds.getVoMeta();
		if(clazz == null)
			return;
		BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
		String pk = curRow.getString(ds.nameToIndex(vo.getPKFieldName()));
		if(StringUtils.isNotBlank(pk)){
			Row selRow = navDs.getSelectedRow();
			if(selRow==null)throw new LuiRuntimeException("导航Ds无选中行");
			modifyVoBeforeUpdate(curVo);
			updateVo(curVo);
		}
		else{
			modifyVoBeforeSave(curVo);
			saveVo(curVo);
		}
		new SuperVO2DatasetSerializer().update(new BaseDO[]{curVo},navDs,new Row[]{curRow});
		AppSession.current().getViewContext().getView().getPagemeta().setHasChanged(false);
		getCurrentWinCtx().closeView(getCurrentWinCtx().getCurrentViewContext().getId());
		cardLayout.setCurrentItem("0");
		ds.setEdit(false);
	}
	
	
	protected void saveVo(BaseDO vo){
		try {
			PtBaseDAO.getIns().insertVO(vo);
		} catch (DAException e) {
			
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	protected void updateVo(BaseDO vo){
		try {
			PtBaseDAO.getIns().updateVO(vo);
		} catch (DAException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}

	protected WindowContext getCurrentWinCtx() {
		return AppSession.current().getAppContext().getCurrentWindowContext();
	}
	protected void modifyVoBeforeUpdate(BaseDO vo){
		return;
	}
	protected void modifyVoBeforeSave(BaseDO vo){
		return;
	}

	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getCardLayoutId() {
		return cardLayoutId;
	}
	public void setCardLayoutId(String cardLayoutId) {
		this.cardLayoutId = cardLayoutId;
	}
	public String getNavDatasetId() {
		return navDatasetId;
	}
	public void setNavDatasetId(String navDatasetId) {
		this.navDatasetId = navDatasetId;
	}
}
