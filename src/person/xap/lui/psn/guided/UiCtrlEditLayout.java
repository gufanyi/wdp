package xap.lui.psn.guided;

import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIFormComp;

/**
 * 控件弹出窗口编辑的布局
 */
public class UiCtrlEditLayout {
	private UIFlowvLayout layoutMain;
	private UIFormComp formComp;
//	private UIFlowhPanel panelhOkButton;
	private UIFlowhLayout layoutOkSaveBtn;
	public UIFlowvLayout getLayoutMain() {
		return layoutMain;
	}

	public UIFormComp getFormComp() {
		return formComp;
	}

//	public UIFlowhPanel getPanelhOkButton(){
//		return panelhOkButton;
//	}

	
	public 	UiCtrlEditLayout(){
		layoutMain = new UIFlowvLayout();
	    layoutMain.setId("flowvlayout"+CommTool.getRndNum(4));
		     
	   	UIFlowvPanel panelvTop=new UIFlowvPanel();
	    panelvTop.setId("panelv"+CommTool.getRndNum(5));
	    
	    formComp=new UIFormComp();
	    panelvTop.setElement(formComp);
	    layoutMain.addPanel(panelvTop);
		     
	    UIFlowvPanel  panelvBottom=new UIFlowvPanel();
	    panelvBottom.setId("panelv"+CommTool.getRndNum(5));
	    panelvBottom.setHeight("40");
	    layoutMain.addPanel(panelvBottom);
	    
	    UIFlowhLayout layoutBottom=new UIFlowhLayout();
	    layoutBottom.setId("flowhlayout"+CommTool.getRndNum(4));
	    panelvBottom.setElement(layoutBottom);
	    
	    UIFlowhPanel panelhLeft=new UIFlowhPanel();
	    panelhLeft.setId("panelh"+CommTool.getRndNum(5));
	    layoutBottom.addPanel(panelhLeft);
	    
	    UIFlowhPanel panelhOKSaveButton=new UIFlowhPanel();
	    panelhOKSaveButton.setId("panelh"+CommTool.getRndNum(5));
	    panelhOKSaveButton.setWidth("80");
	    panelhOKSaveButton.setTopPadding("8");
	    layoutBottom.addPanel(panelhOKSaveButton);
	    
	    layoutOkSaveBtn=new UIFlowhLayout();
	    layoutOkSaveBtn.setId("flowhlayout"+CommTool.getRndNum(4));
	    panelhOKSaveButton.setElement(layoutOkSaveBtn);
	    
	    UIFlowhPanel panelhSaveButton=new UIFlowhPanel();
	    panelhSaveButton.setId("panelh"+CommTool.getRndNum(5));
	    panelhSaveButton.setWidth("1");
	    layoutOkSaveBtn.addPanel(panelhSaveButton);
	    
	    UIButton buttonSave=new UIButton();
	    buttonSave.setId(SigTabPopUpOper.editView_btnSaveId);	    
	    buttonSave.setClassName("blue_button_div");
	    panelhSaveButton.setElement(buttonSave);
	    
	    
	  
	    
	    UIFlowhPanel panelhCancelButton=new UIFlowhPanel();
	    panelhCancelButton.setId("panelh"+CommTool.getRndNum(5));
	    panelhCancelButton.setWidth("80");
	    panelhCancelButton.setTopPadding("8");
	    layoutBottom.addPanel(panelhCancelButton);
	    
	    UIButton buttonCancel=new UIButton();
	    buttonCancel.setId(SigTabPopUpOper.editView_btnCancelId);	    
	    panelhCancelButton.setElement(buttonCancel);
	    
	    
	}
	
	public UIButton getOkButton(){
		UIFlowhPanel panelhOkButton=new UIFlowhPanel();
	    panelhOkButton.setId("panelh"+CommTool.getRndNum(5));
	    this.layoutOkSaveBtn.addPanel(panelhOkButton);
		
		UIButton buttonOK=new UIButton();
	    buttonOK.setId(SigTabPopUpOper.editView_btnOkId);	    
	    buttonOK.setClassName("blue_button_div");
	    
	    panelhOkButton.setElement(buttonOK);
	    return buttonOK;
	  
	}
}
