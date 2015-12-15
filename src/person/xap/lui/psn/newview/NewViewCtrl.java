package xap.lui.psn.newview;

import org.apache.commons.lang.StringUtils;
import xap.lui.core.cache.PaCache;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.ComboBoxComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.control.ModePhase;
import xap.lui.core.event.DialogEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.TextEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.psn.designer.PaProjViewTreeController;
public class NewViewCtrl {
	public void onBeforeShow(DialogEvent dialogEvent) {
		StringTextComp classComp = (StringTextComp)getComp("classname_text");
		setDftClassName(classComp);
	}
	private void setDftClassName(StringTextComp classComp) {
		ModePhase modePahse=(ModePhase)PaCache.getInstance().get(PaCache.ModePhase);
		String nodeType=(String) LuiAppUtil.getAppAttr("nodeType");
		if(ModePhase.nodedef.equals(modePahse)){
			if("FreeBill".equalsIgnoreCase(nodeType)){
				classComp.setValue(PaCache.FreeBillCtrlClassName);
			}
			if("FreeGrid".equalsIgnoreCase(nodeType)){
				classComp.setValue(PaCache.FreeBillGrdiClassName);
			}
		}
	}
	//下拉框的valuechange事件
	public void typeComboValueChanged(TextEvent e) {
		ComboBoxComp combo = (ComboBoxComp) getComp("type_combo");
		String value = combo.getValue();
		LabelComp pathlabel = (LabelComp)getComp("label_path");
		StringTextComp classComp = (StringTextComp) getComp("classname_text");
		StringTextComp packpath = (StringTextComp) getComp("packpath_text");
		if (StringUtils.equals("selfdefine", value)) {
			pathlabel.setVisible(true);
			packpath.setVisible(true);
			classComp.setValue("");
		} else {
			pathlabel.setVisible(false);
			packpath.setVisible(false);
			setDftClassName(classComp);
		}
	}
	
	public void onOkClick(MouseEvent<ButtonComp> e){
		String pageId=(String) LuiAppUtil.getAppAttr("pageId");
		String viewId = ((StringTextComp)getComp("id_text")).getValue();
		String classname = ((StringTextComp)getComp("classname_text")).getValue();
		String packpath = ((StringTextComp)getComp("packpath_text")).getValue();
		String ctrltype = ((ComboBoxComp)getComp("type_combo")).getValue();
		if(StringUtils.isBlank(viewId))
			throw new LuiRuntimeException("ID不能为空！");
		if(StringUtils.isBlank(classname))
			throw new LuiRuntimeException("类名不能为空！");
		if(StringUtils.equals("selfdefine", ctrltype)){
			if(StringUtils.isBlank(packpath))
				throw new LuiRuntimeException("代码路径不能为空！");
			PaProjViewTreeController controller = new PaProjViewTreeController();
			controller.dealNewView(pageId, viewId, classname, packpath);
		}else{
			PaProjViewTreeController controller = new PaProjViewTreeController();
			controller.dealNewView(pageId, viewId, classname, packpath);
		}
		closepage();
	}
	
	public void onCancelClick(MouseEvent<ButtonComp> e){
		closepage();
	}
	
	private void closepage(){
		AppSession.current().getAppContext().closeWinDialog();
	}
	private WebComp getComp(String compid) {
		WebComp comp = LuiAppUtil.getCntView().getViewComponents().getComponent(compid);
		return comp;
	}
}
