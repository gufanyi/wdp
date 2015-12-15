package xap.lui.psn.cmd;

import xap.lui.core.command.LuiCommand;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.dao.PtBaseDAO;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.WindowContext;
import xap.lui.core.serializer.Dataset2SuperVOSerializer;
import xap.lui.core.serializer.SuperVO2DatasetSerializer;
import xap.mw.core.data.BaseDO;
import xap.mw.core.data.BizException;
import xap.mw.core.data.Context;
import xap.mw.core.data.DOStatus;
import xap.mw.coreitf.d.FDateTime;

public class LuiCardSaveDOCmd extends LuiCommand{
	private Dataset ds;
	private String dsId;
	private String cardLayoutId;
	private ToolBarComp toolBarComp;
	
	public LuiCardSaveDOCmd(String dsId, String cardLayoutId) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.ds = getCurrentWinCtx().getCurrentViewContext().getView().getViewModels().getDataset(dsId);
	}
	public LuiCardSaveDOCmd(String dsId, String cardLayoutId, ToolBarComp toolBarComp) {
		this.dsId = dsId;
		this.cardLayoutId = cardLayoutId;
		this.ds = getCurrentWinCtx().getCurrentViewContext().getView().getViewModels().getDataset(dsId);
		this.toolBarComp=toolBarComp;
	}

	@Override
	public void execute(){
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		UICardLayout cardLayout = (UICardLayout) widgetctx.getUIMeta().findChildById(this.cardLayoutId);
		if(cardLayout == null)
			throw new LuiRuntimeException("卡片没指定！");
		if(ds==null)
			throw new LuiRuntimeException("数据集不能为空！");
		Row seleRow = ds.getSelectedRow();
		if(seleRow != null){
			BaseDO[] update = new Dataset2SuperVOSerializer<BaseDO>().serialize(ds, seleRow);
			setDefValue(update[0]);
			update[0] = saveVo(update[0]);
			seleRow.setState(Row.STATE_NORMAL);
			saveafter(update[0],seleRow);
		}
		cardLayout.setCurrentItem("0");
		ds.setEdit(false);
		ds.setSelectedIndex(0);
		
		if(this.toolBarComp!=null)
			new	ToolBarItemStatusCtrl(this.toolBarComp,"save","card");
	}

	protected void setDefValue(BaseDO basedo){
		if(basedo.getPkVal() == null) {
				basedo.setAttrVal("Createdby", Context.get().getUserId());
				basedo.setAttrVal("Createdtime", new FDateTime());
		}else{
				basedo.setAttrVal("Modifiedby", Context.get().getUserId());
				basedo.setAttrVal("Modifiedtime", new FDateTime());
		}
	};
	
	protected BaseDO  saveVo(BaseDO vo){
		PtBaseDAO dao = PtBaseDAO.getIns();
		BaseDO baseDO = null;
		try {
			if(vo.getPkVal() == null) {
				vo.setStatus(DOStatus.NEW);
				baseDO = dao.insertVODO(vo);
			}else {
				vo.setStatus(DOStatus.UPDATED);
				baseDO = dao.updateVODO(vo);
			}
			return baseDO;
		} catch (BizException e) {
			throw new LuiRuntimeException(e.getMessage());
		}
	}
	
	private void saveafter(BaseDO rules,Row row){
		new SuperVO2DatasetSerializer().vo2DataSet(rules, ds, row);
	}
	
	protected WindowContext getCurrentWinCtx(){
	    return AppSession.current().getAppContext()
					.getCurrentWindowContext();
	}
	public Dataset getDs() {
		return ds;
	}
	public void setDs(Dataset ds) {
		this.ds = ds;
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
}
