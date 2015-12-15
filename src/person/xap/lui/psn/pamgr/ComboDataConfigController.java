package xap.lui.psn.pamgr;

import java.io.Serializable;
import java.util.UUID;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.xml.StringUtils;


/**
 * 下拉控件数据配置主控制类
 * 
 * @author liujmc
 * @date 2012-07-04
 */
@SuppressWarnings("rawtypes")
public class ComboDataConfigController implements IWindowCtrl, Serializable {
	private static final long serialVersionUID = 7532916478964732880L;
	public static final String DS_NAME_COMBOCFG = "combocfg";
	public static final String WIDGET_ID_COMBOCFG = "main";

	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}

	public void onDataLoad(DatasetEvent DatasetEvent) {
		ComboData comboData = getOriginalComboData();
		if(comboData != null){
			StringTextComp str_id = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("str_id");
			str_id.setValue(comboData.getId());
			str_id.setEnabled(false);
			StringTextComp str_caption = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("str_caption");
			str_caption.setValue(comboData.getCaption());
			
			Dataset ds = DatasetEvent.getSource();
			//根据下拉数据集的数据初始化表格的dataset
			DataItem[] items = comboData.getAllDataItems();
			if (items != null) {
				for (DataItem combItem : items) {
					Row row = ds.getEmptyRow();
					row.setValue(ds.nameToIndex("text"), combItem.getText());
					row.setValue(ds.nameToIndex("value"), combItem.getValue());
					ds.addRow(row);
				}
			}
			ds.setSelectedIndex(0);
		}
	}

	/**
	 * 添加菜单项的点击事件处理
	 * 
	 * @param mouseEvent
	 */
	public void onAddClick(MouseEvent mouseEvent) {
		Dataset ds = getCurrentDs();
//		// 校验空数据
//		int count = ds.getCurrentIndexPageData(0).getCurrentPageRowCount();
//		for (int i = 0; i < count; i++) {
//			Row row = ds.getCurrentIndexPageData(0).getRow(i);
//			String key = String.valueOf(row.getValue(ds.nameToIndex("key")));
//			String value = String.valueOf(row.getValue(ds.nameToIndex("value")));
//			if (value == null || "null".equals(value) || key == null || "null".equals(key) || "".equals(key) || "".equals(value)) {
//				throw new LuiRuntimeException("不允许输入空行!");
//			}
//		}

		ViewPartMeta widget = AppSession.current().getAppContext().getCurrentWindowContext().getPagePartMeta().getWidget(WIDGET_ID_COMBOCFG);
		GridComp grid = (GridComp) widget.getViewComponents().getComponent("cfggrid");
		grid.setEdit(true);
		Row row = ds.getEmptyRow();
		row.setRowId(UUID.randomUUID().toString());
		ds.addRow(row);
		//选中新添加的行
		ds.setRowSelectIndex(ds.getRowIndex(row));
	}

	/**
	 * 删除菜单项点击事件处理
	 * 
	 * @param mouseEvent
	 */
	public void onDeleteClick(MouseEvent mouseEvent) {
		Dataset ds = getCurrentDs();
		Row selectRow = ds.getSelectedRow();
		if (selectRow == null) {
			throw new LuiRuntimeException("请选选择您要删除的行!");
		} else {
			ds.removeRow(selectRow);
		}
	}

	/**
	 * 点击保存菜单
	 * 
	 * @param mouseEvent
	 */
	public void onSaveClick(MouseEvent mouseEvent) {
		Dataset ds = getCurrentDs();
		int rowCount = ds.getCurrentIndexPageData(0).getCurrentPageRowCount();
		ComboData comboData = getOriginalComboData();
		StringTextComp str_caption = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("str_caption");
		String combcap = str_caption.getValue();
		LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		if(comboData == null){
			comboData = new DataList();
			
			StringTextComp str_id = (StringTextComp) LuiAppUtil.getCntView().getViewComponents().getComponent("str_id");
			String combid = str_id.getValue();
			if(StringUtils.isBlank(combid))
				throw new LuiRuntimeException("请输入id！");
			comboData.setId(combid);
			comboData.setCaption(combcap);
			ViewPartMeta view = PaCache.getEditorViewPartMeta();
			DataModels viewmodels = view.getViewModels();
			viewmodels.addComboData(comboData);
			
		}else{
			comboData.setCaption(combcap);
		}
		
		comboData.removeAllDataItems();
		RequestLifeCycleContext.get().setPhase(oriPhase);

		for (int i = 0; i < rowCount; i++) {
			Row row = ds.getCurrentIndexPageData(0).getRow(i);
			DataItem item = new DataItem();
			//校验
			String key = String.valueOf(row.getValue(ds.nameToIndex("text")));
			String value = String.valueOf(row.getValue(ds.nameToIndex("value")));
//			String image = String.valueOf(row.getValue(ds.nameToIndex("image")));
//			String langDir = String.valueOf(row.getValue(ds.nameToIndex("langDir")));
//			String i18nName = String.valueOf(row.getValue(ds.nameToIndex("i18nName")));
//			if (value == null || "null".equals(value) || key == null || "null".equals(key) || "".equals(key) || "".equals(value)) {
//				ds.setRowSelectIndex(i);
//				throw new LuiRuntimeException("不允许输入空值!");
//			}
			item.setText(key);
			item.setValue(value);
//			item.setLangDir((langDir == null || "null".equals(langDir))==true?"":langDir);
//			item.setI18nName((image == null || "null".equals(image))==true?"":image);
//			item.setI18nName((i18nName == null || "null".equals(i18nName))==true?"":i18nName);
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			comboData.addDataItem(item);
			RequestLifeCycleContext.get().setPhase(phase);
		}
		Dataset entityDateset = LuiAppUtil.getCntAppCtx().getWindowContext("pa").getViewContext("data").getView().getViewModels().getDataset("entityds");
		PaEntityDsListener.setNewComboDateInfo(entityDateset, comboData);
		AppSession.current().getAppContext().addExecScript("parent.document.getElementById('iframe_tmp').contentWindow.reNewModel('"+ comboData.getId() +"','"+ PaModelOperateController.TYPE_COMBODATA +"','"+ comboData.getWidget().getId() +"');"); 
	}

	//获取当前的Dataset
	public Dataset getCurrentDs() {
		ViewPartMeta widget = AppSession.current().getAppContext().getCurrentWindowContext().getPagePartMeta().getWidget(WIDGET_ID_COMBOCFG);
		Dataset ds = widget.getViewModels().getDataset(DS_NAME_COMBOCFG);
		return ds;
	}

	//获取下拉数据集
	@SuppressWarnings("restriction")
	public ComboData getOriginalComboData() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String comboId = session.getOriginalParameter("comboId");
		String viewId = session.getOriginalParamMap().get("sourceView");
		PagePartMeta pagemeta = PaCache.getEditorPagePartMeta();
		ViewPartMeta widget = pagemeta.getWidget(viewId);
		ComboData comboData = null;
		if(comboId != null)
			comboData = widget.getViewModels().getComboData(comboId);
		return comboData;
	}

	public void onOKClick(MouseEvent mouseEvent) {
		onSaveClick(mouseEvent);
		//关闭并提示
		//InteractionUtil.showMessageDialog("修改下拉数据集成功!");
		AppSession.current().getAppContext().closeWinDialog();		
	}
	//取消
	public void cancel_onclick(MouseEvent e) {
		AppSession.current().getAppContext().closeWinDialog();
	}
}
