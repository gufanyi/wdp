package xap.lui.psn.cmd;

import xap.lui.core.command.CmdInvoker;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.WindowContext;
import xap.lui.core.util.LuiClassUtil;
import xap.mw.core.data.BaseDO;

public class LuiAddOrEditMenuClickCmd extends LuiCommand{
	private Dataset ds;
	private String oper;
	private String editViewId;
	private String width;
	private String height;
	private String title;
	public static String ADD_OPER = "add_oper";
	public static String EDIT_OPER = "edit_oper";
	public static String OPERATE_STATUS = "OPERATE_STATUS";
	
	public LuiAddOrEditMenuClickCmd(String tabLayoutId,String datasetIds,String oper,String editViewIds){
		if (tabLayoutId == null)throw new LuiRuntimeException("未指定Tab布局id");
		ViewPartContext  widgetctx = getLifeCycleContext().getViewContext();
		UITabComp tabLayout = (UITabComp) widgetctx.getUIMeta().findChildById(tabLayoutId);
		if (tabLayout == null)throw new LuiRuntimeException("Tab不存在,id=" + tabLayoutId + "!");
		int tabCurrentIndex=tabLayout.getCurrentItem();
		String[] arrDsid=datasetIds.split(",");
		String[] arrViewId= editViewIds.split(",");
		this.oper = oper;
		String dsId=arrDsid[tabCurrentIndex];
		this.ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(dsId);
		this.editViewId = arrViewId[tabCurrentIndex];
	}
	
	public LuiAddOrEditMenuClickCmd(String datasetId,String oper,String editViewId){
		this.ds = AppSession.current().getViewContext().getView().getViewModels().getDataset(datasetId);
		this.oper = oper;
		this.editViewId = editViewId;
	}
	
	public LuiAddOrEditMenuClickCmd(Dataset ds,String oper,String editViewId){
		this.ds = ds;
		this.oper = oper;
		this.editViewId = editViewId;
	}
	
	public LuiAddOrEditMenuClickCmd(String oper,String editViewId){
		this.oper = oper;
		this.editViewId = editViewId;
	}
	@Override
	public void execute() {
		if(editViewId==null||editViewId.trim().length()==0)throw new LuiRuntimeException("view不能为空！");
		if(oper==null||oper.trim().length()==0)throw new LuiRuntimeException("操作状态不能为空！");
		setWidthAndHeight();
		changeTitle();
		WindowContext curWin = getCurrentWinCtx();
		if(curWin==null)return;
		if(LuiAddOrEditMenuClickCmd.EDIT_OPER.equals(oper)){
			if(this.ds==null)throw new LuiRuntimeException("数据集不能为空！");
			if(ds.getSelectedRow()==null)throw new LuiRuntimeException("无选中行！");
			String clazz = ds.getVoMeta();
			if(clazz == null)
				return;
			BaseDO vo = (BaseDO) LuiClassUtil.newInstance(clazz);
			String pkValue = ds.getSelectedRow().getString(ds.nameToIndex(vo.getPKFieldName()));
			if(pkValue==null || pkValue.trim().length()==0) throw new LuiRuntimeException("选中行主键不能为空！");
			curWin.addAppAttribute(vo.getPKFieldName(), pkValue);
		}
		curWin.addAppAttribute(LuiAddOrEditMenuClickCmd.OPERATE_STATUS, oper);
		curWin.popView(editViewId, width, height, title);
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getHeight() {
		return height;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getWidth() {
		return width;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	protected void setWidthAndHeight(){
		setWidth(DialogConstant.DEFAULT_WIDTH);
		setHeight(DialogConstant.DEFAULT_HEIGHT);
	}
	protected WindowContext getCurrentWinCtx(){
		    return AppSession.current().getAppContext()
						.getCurrentWindowContext();
		  }
	protected void changeTitle(){
		if(LuiAddOrEditMenuClickCmd.ADD_OPER.equals(oper))
			setTitle("新增");
		else if(LuiAddOrEditMenuClickCmd.EDIT_OPER.equals(oper))
			setTitle("编辑");
	}
}
