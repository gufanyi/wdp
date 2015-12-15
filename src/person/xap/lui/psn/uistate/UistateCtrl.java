package xap.lui.psn.uistate;

import java.util.List;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.PageData;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.DatasetEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.CtrlState;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewState;

public class UistateCtrl {
	public void sysWindowClosed(PageEvent event) {
		LuiRuntimeContext.getWebContext().destroyWebSession();
	}
	
	public void onDataLoad_view(DatasetEvent e){
		String stateId = getStateId();
		PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
		UIState uistate = pagePart.getUIState(stateId);
		if(uistate != null){
			 List<ViewState> viewStates = uistate.getViewStateList();
			 if(viewStates != null && viewStates.size() > 0){
				 Dataset viewDs = e.getSource();
				 for(ViewState viewState : viewStates){
					 Row row = viewDs.getEmptyRow();
					 row.setValue(viewDs.nameToIndex("viewId"), viewState.getViewId());
					 viewDs.addRow(row);
				 }
				 viewDs.setSelectedIndex(0);
			 }
		}
	}

	private String getStateId() {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String stateId = session.getOriginalParameter("stateid");
		return stateId;
	}
	//行选中前事件
	public void onTreeBeforeRowSelect(DatasetEvent e){
		Dataset viewDs = e.getSource();
		saveCtrlState(viewDs);
	}
	private void saveCtrlState(Dataset viewDs) {
		Row selRow = viewDs.getSelectedRow();
		if(selRow != null){
			String viewId = (String) selRow.getValue(viewDs.nameToIndex("viewId"));
			String stateId = getStateId();
			PagePartMeta pagePart = PaCache.getEditorPagePartMeta();
			UIState uistate = pagePart.getUIState(stateId);
			if(uistate != null){
				ViewState viewState = uistate.getViewState(viewId);
				if(viewState != null){
					List<CtrlState> ctrlStates = viewState.getCtrlStateList();
					if(ctrlStates != null){
						ctrlStates.clear();
						Dataset ctrlDs = LuiAppUtil.getCntView().getViewModels().getDataset("ctrlds");
						PageData pagedate = ctrlDs.getCurrentPageData();
						if(pagedate != null){
							Row[] rows = pagedate.getRows();
							if(rows != null && rows.length > 0){
								for(Row row : rows){
									CtrlState ctrlState = new CtrlState();
									ctrlState.setId((String)row.getValue(ctrlDs.nameToIndex("id")));
									ctrlState.setPid((String)row.getValue(ctrlDs.nameToIndex("pid")));
									String visible = (String)row.getValue(ctrlDs.nameToIndex("visible"));
									String enabled = (String)row.getValue(ctrlDs.nameToIndex("enabled"));
									ctrlState.setVisible(visible.equals("Y") ? true : false);
									ctrlState.setEnabled(enabled.equals("Y") ? true : false);
									ctrlStates.add(ctrlState);
								}
							}
						}
					}
				}
			}
		}
	}
	//行选中后事件
	public void onTreeAfterRowSelect(DatasetEvent e){
		Dataset ctrlDs = LuiAppUtil.getCntView().getViewModels().getDataset("ctrlds");
		ctrlDs.clear();
		Dataset viewDs = e.getSource();
		Row selRow = viewDs.getSelectedRow();
		if(selRow != null){
			String viewId = (String) selRow.getValue(viewDs.nameToIndex("viewId"));
			String stateId = getStateId();
			PagePartMeta pagePart = PaCache.getInstance().getEditorPagePartMeta();
			UIState uistate = pagePart.getUIState(stateId);
			if(uistate != null){
				ViewState viewState = uistate.getViewState(viewId);
				if(viewState != null){
					List<CtrlState> ctrlStates = viewState.getCtrlStateList();
					if(ctrlStates != null && ctrlStates.size() > 0){
						for(CtrlState c : ctrlStates){
							Row row = ctrlDs.getEmptyRow();
							row.setValue(ctrlDs.nameToIndex("id"), c.getId());
							row.setValue(ctrlDs.nameToIndex("pid"), c.getPid());
							row.setValue(ctrlDs.nameToIndex("visible"), c.isVisible()?"Y":"N");
							row.setValue(ctrlDs.nameToIndex("enabled"), c.isEnabled()?"Y":"N");
							ctrlDs.addRow(row);
						}
					}
				}
			}
		}
	}
	
	public void onclickConfirm(MouseEvent e){
		Dataset viewDs = LuiAppUtil.getCntView().getViewModels().getDataset("viewds");
		saveCtrlState(viewDs);
		AppSession.current().getAppContext().closeWinDialog();
	}
	
	public void onCancelClick(MouseEvent e){
		AppSession.current().getAppContext().closeWinDialog();
	}
	
}
