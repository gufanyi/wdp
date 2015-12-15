package xap.lui.core.refrence;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.Window;
import xap.lui.core.cache.CacheMgr;
import xap.lui.core.cache.LUICache;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebContext;
import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.StringTextComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.ParamConstant;
import xap.lui.core.constant.WebConstant;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.event.AppRequestProcessor;
import xap.lui.core.event.KeyEvent;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.event.PageEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIButton;
import xap.lui.core.layout.UIFlowhLayout;
import xap.lui.core.layout.UIFlowhPanel;
import xap.lui.core.layout.UIFlowvLayout;
import xap.lui.core.layout.UIFlowvPanel;
import xap.lui.core.layout.UIGridComp;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.IUIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refmodel.BaseRefModel;
import xap.lui.core.refmodel.LuiRefDftCtrl;
import xap.lui.core.refmodel.LuiRefDftGen;
import xap.lui.core.refmodel.TreeGridRefModel;

/**
 * 参照布局
 */
public class AppRefDftWindow extends Window {
	private static final String REF_BOTTOM_HEIGHT = "42";
	@Override
	protected IUIPartMeta createUIMeta(PagePartMeta pm) {
		LuiWebContext ctx = getWebSesssion();
		String widgetId = ctx.getParameter("widgetId");
		if (widgetId == null || widgetId.equals(""))
			throw new LuiRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		PagePartMeta parentPm = ctx.getParentPageMeta();
		GenericRefNode refnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refnodeId);
		UIPartMeta uiMeta = new UIPartMeta();
		int refType = 0;
		if(refnode.isDefModel()){
			BaseRefModel refModel = RefSelfUtil.getDefRefModel(refnode.getRefcode());
			refType = RefSelfUtil.getRefType(refModel);
		}else{
			IRefModel refModel = RefSelfUtil.getRefModel(refnode);
			refType = RefSelfUtil.getRefType(refModel);
		}
		if (refType == IRefConst.GRID) {
			generateGridUIMeta(uiMeta, refnode);
		} else if (refType == IRefConst.TREE) {
			generateTreeUIMeta(uiMeta, refnode);
		} else if (refType == IRefConst.GRIDTREE) {
			generateGridTreeUI(uiMeta, refnode);
		}
		uiMeta.setIsReference(1);
		uiMeta.setFlowmode(false);
		return uiMeta;
	}
	/**
	 * 产生树表型ui
	 * 
	 * @param uiMeta
	 * @param refnode
	 */
	private void generateGridTreeUI(UIPartMeta uiMeta, GenericRefNode refnode) {
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIPartMeta uiMetaWidget = new UIPartMeta();
		uiMetaWidget.setFlowmode(false);
		uiMetaWidget.setId("main_um");
		uiWidget.setUimeta(uiMetaWidget);
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("flowvLayout");
		uiMetaWidget.setElement(flowvLayout);
		if (refnode.isLocate())
			generateTreeGridLocate(flowvLayout);
		UIFlowvPanel centerFlowvPanel = new UIFlowvPanel();
		flowvLayout.addPanel(centerFlowvPanel);
		UIFlowhLayout flowhLayout = new UIFlowhLayout();
		flowhLayout.setId("flowhlayout");
		centerFlowvPanel.setElement(flowhLayout);
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setId("panel1");
		flowhPanel1.setWidth("240");
		flowhPanel1.setRightBorder("#");
		flowhLayout.addPanel(flowhPanel1);
		UIFlowvLayout leftFlowv = new UIFlowvLayout();
		leftFlowv.setId("leftflowv");
		flowhPanel1.setElement(leftFlowv);
		UIFlowvPanel leftPanel1 = new UIFlowvPanel();
		leftPanel1.setId("leftflowvp1");
		leftPanel1.setHeight("14");
		leftFlowv.addPanel(leftPanel1);
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId("reftree");
		treeComp.setViewId("main");
		treeComp.setWidth("220");
		treeComp.setLeft(10);
		leftFlowv.addElementToPanel(treeComp);
		UIGridComp uigridComp = new UIGridComp();
		uigridComp.setId("refgrid");
		uigridComp.setViewId("main");
		flowhLayout.addElementToPanel(uigridComp);
		// bottom
		UIFlowvPanel flowvBottomPanel = new UIFlowvPanel();
		flowvBottomPanel.setId("bottomvp1");
		flowvBottomPanel.setHeight(REF_BOTTOM_HEIGHT);
		flowvLayout.addPanel(flowvBottomPanel);
		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		flowvBottomPanel.setElement(uiflowhLayout);
		uiflowhLayout.addElementToPanel(null);
		UIButton buttonOk = new UIButton();
		buttonOk.setId("okbt");
		buttonOk.setViewId("main");
		buttonOk.setWidth("74");
		buttonOk.setClassName("blue_button_div");
		UIButton buttonCanCel = new UIButton();
		buttonCanCel.setId("cancelbt");
		buttonCanCel.setViewId("main");
		buttonCanCel.setWidth("74");
		UIFlowhPanel flowhPanel2 = uiflowhLayout.addElementToPanel(buttonCanCel);
		flowhPanel2.setFloat("right");
		flowhPanel2.setWidth("84");
		flowhPanel2.setTopPadding("12");
		UIFlowhPanel flowhPanel3 = uiflowhLayout.addElementToPanel(buttonOk);
		flowhPanel3.setFloat("right");
		flowhPanel3.setWidth("94");
		flowhPanel3.setTopPadding("12");
	}
	/**
	 * 生成表型参照UI
	 * 
	 * @param uiMeta
	 * @param refnode
	 */
	private void generateGridUIMeta(UIPartMeta uiMeta, GenericRefNode refnode) {
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIPartMeta widgetUIMeta = new UIPartMeta();
		widgetUIMeta.setId("main");
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("borderLayout");
		uiWidget.setUimeta(widgetUIMeta);
		widgetUIMeta.setElement(flowvLayout);
		if (refnode.isLocate()) {
			generateLocatePanle(flowvLayout);
		}
		UIFlowvPanel borderPanel1 = new UIFlowvPanel();
		borderPanel1.setId("centerFlowvp1");
		flowvLayout.addPanel(borderPanel1);
		UIGridComp gridComp = new UIGridComp();
		gridComp.setId("refgrid");
		gridComp.setViewId("main");
		borderPanel1.setElement(gridComp);
		UIFlowvPanel borderPanelBottom = new UIFlowvPanel();
		borderPanelBottom.setId("bottomFlowvp3");
		borderPanelBottom.setHeight(REF_BOTTOM_HEIGHT);
		flowvLayout.addPanel(borderPanelBottom);
		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		borderPanelBottom.setElement(uiflowhLayout);
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
		UIFlowhPanel flowhPanel2 = new UIFlowhPanel();
		uiflowhLayout.addPanel(flowhPanel2);
		flowhPanel2.setAttribute("id", "flowhPanel2");
		flowhPanel2.setFloat("right");
		flowhPanel2.setWidth("84");
		flowhPanel2.setTopPadding("12");
		{
			UIFlowhPanel flowhPanel3 = new UIFlowhPanel();
			uiflowhLayout.addPanel(flowhPanel3);
			flowhPanel3.setAttribute("id", "flowhPanel3");
			flowhPanel3.setFloat("right");
			flowhPanel3.setWidth("94");
			flowhPanel3.setTopPadding("12");
			UIButton buttonOk = new UIButton();
			buttonOk.setId("okbt");
			buttonOk.setViewId("main");
			buttonOk.setWidth("74");
			buttonOk.setClassName("blue_button_div");
			flowhPanel3.setElement(buttonOk);
			UIButton buttonCanCel = new UIButton();
			buttonCanCel.setId("cancelbt");
			buttonCanCel.setViewId("main");
			buttonCanCel.setWidth("74");
			flowhPanel2.setElement(buttonCanCel);
		}
	}
	/**
	 * 产生定位和组织的过滤
	 * 
	 * @param flowvLayout
	 */
	protected void geneLoacationAndOrg(UIFlowvLayout flowvLayout, boolean isLocate) {
		UIFlowvPanel borderPanelTop = new UIFlowvPanel();
		borderPanelTop.setId("topFlowvp222");
		borderPanelTop.setHeight("45");
		borderPanelTop.setLeftPadding("20");
		borderPanelTop.setTopPadding("4");
		borderPanelTop.setBottomBorder("#");
		flowvLayout.addPanel(borderPanelTop);
		UIFlowhLayout topLayout = new UIFlowhLayout();
		topLayout.setId("topLayout111");
		borderPanelTop.setElement(topLayout);
		UIFlowhPanel borderPanelTopOrg = new UIFlowhPanel();
		borderPanelTopOrg.setId("topFlowvp2");
		borderPanelTopOrg.setWidth("220");
		borderPanelTopOrg.setLeftPadding("20");
		borderPanelTopOrg.setTopPadding("4");
		borderPanelTopOrg.setBottomPadding("8");
		// borderPanelTopOrg.setBottomBorder("#");
		topLayout.addPanel(borderPanelTopOrg);
		UITextField orgField = new UITextField();
		orgField.setId("refcomp_org");
		orgField.setWidth("220");
		orgField.setViewId("main");
		borderPanelTopOrg.setElement(orgField);
		UIFlowhPanel emptypanel = new UIFlowhPanel();
		emptypanel.setId("emptypanel");
		emptypanel.setWidth("50");
		emptypanel.setLeftPadding("60");
		emptypanel.setTopPadding("8");
		emptypanel.setBottomPadding("8");
		topLayout.addPanel(emptypanel);
		// 定位控件
		if (isLocate) {
			UIFlowhPanel locateTextpanel = new UIFlowhPanel();
			locateTextpanel.setId("locateTextpanel");
			locateTextpanel.setWidth("220");
			locateTextpanel.setLeftPadding("50");
			locateTextpanel.setTopPadding("6");
			locateTextpanel.setBottomPadding("8");
			// locateTextpanel.setBottomBorder("#");
			topLayout.addPanel(locateTextpanel);
			UITextField locateField = new UITextField();
			locateField.setId("locatetext");
			locateField.setWidth("300");
			locateField.setViewId("main");
			locateTextpanel.setElement(locateField);
		}
	}
	protected void geneGridTreeLoacationAndOrg(UIFlowvLayout flowvLayout, boolean isLocate) {
		UIFlowvPanel borderPanelTop = new UIFlowvPanel();
		borderPanelTop.setId("topFlowvp222");
		borderPanelTop.setHeight("45");
		borderPanelTop.setLeftPadding("20");
		borderPanelTop.setTopPadding("4");
		borderPanelTop.setBottomBorder("#");
		flowvLayout.addPanel(borderPanelTop);
		UIFlowhLayout topLayout = new UIFlowhLayout();
		topLayout.setId("topLayout111");
		borderPanelTop.setElement(topLayout);
		UIFlowhPanel borderPanelTopOrg = new UIFlowhPanel();
		borderPanelTopOrg.setId("topFlowvp2");
		borderPanelTopOrg.setWidth("220");
		borderPanelTopOrg.setLeftPadding("20");
		borderPanelTopOrg.setTopPadding("4");
		borderPanelTopOrg.setBottomPadding("8");
		// borderPanelTopOrg.setBottomBorder("#");
		topLayout.addPanel(borderPanelTopOrg);
		UITextField orgField = new UITextField();
		orgField.setId("refcomp_org");
		orgField.setWidth("220");
		orgField.setViewId("main");
		borderPanelTopOrg.setElement(orgField);
		UIFlowhPanel emptypanel = new UIFlowhPanel();
		emptypanel.setId("emptypanel");
		emptypanel.setWidth("50");
		emptypanel.setLeftPadding("60");
		emptypanel.setTopPadding("8");
		emptypanel.setBottomPadding("8");
		topLayout.addPanel(emptypanel);
		// 定位控件
		if (isLocate) {
			UIFlowhPanel locateTextpanel = new UIFlowhPanel();
			locateTextpanel.setId("locateTextpanel");
			locateTextpanel.setWidth("310");
			locateTextpanel.setLeftPadding("50");
			locateTextpanel.setTopPadding("6");
			locateTextpanel.setBottomPadding("8");
			// locateTextpanel.setBottomBorder("#");
			topLayout.addPanel(locateTextpanel);
			UITextField locateField = new UITextField();
			locateField.setId("locatetext");
			locateField.setWidth("300");
			locateField.setViewId("main");
			locateTextpanel.setElement(locateField);
		}
		UIButton buttonSearch = new UIButton();
		buttonSearch.setId("searchAllBt");
		buttonSearch.setViewId("main");
		buttonSearch.setWidth("74");
		buttonSearch.setClassName("blue_button_div");
		UIFlowhPanel flowhPanel3 = topLayout.addElementToPanel(buttonSearch);
		// flowhPanel3.setFloat("right");
		flowhPanel3.setWidth("94");
		flowhPanel3.setTopPadding("6");
		flowhPanel3.setLeftPadding("20");
		flowhPanel3.setTopPadding("6");
		flowhPanel3.setBottomPadding("8");
	}
	/**
	 * 生成树形参照的uimeta
	 * 
	 * @param uiMeta
	 * @param refnode
	 */
	private void generateTreeUIMeta(UIPartMeta uiMeta, GenericRefNode refnode) {
		UIViewPart uiWidget = new UIViewPart();
		uiWidget.setId("main");
		uiMeta.setElement(uiWidget);
		UIPartMeta widgetUIMeta = new UIPartMeta();
		widgetUIMeta.setId("main");
		widgetUIMeta.setFlowmode(false);
		UIFlowvLayout flowvLayout = new UIFlowvLayout();
		flowvLayout.setId("borderLayout");
		uiWidget.setUimeta(widgetUIMeta);
		widgetUIMeta.setElement(flowvLayout);
		// IRefGridModel refModel = RefSelfUtil.getRefModel(refnode);
		UITreeComp treeComp = new UITreeComp();
		treeComp.setId("reftree");
		treeComp.setViewId("main");
		UIFlowvPanel centerFlowvPanel = flowvLayout.addElementToPanel(treeComp);
		centerFlowvPanel.setLeftPadding("10");
		centerFlowvPanel.setRightPadding("10");
		centerFlowvPanel.setTopPadding("14");
		UIFlowhLayout uiflowhLayout = new UIFlowhLayout();
		uiflowhLayout.setId("flowhLayout");
		UIFlowvPanel borderPanelBottom = flowvLayout.addElementToPanel(uiflowhLayout);
		borderPanelBottom.setHeight(REF_BOTTOM_HEIGHT);
		UIFlowhPanel flowhPanel1 = new UIFlowhPanel();
		flowhPanel1.setAttribute("id", "flowhPanel1");
		uiflowhLayout.addPanel(flowhPanel1);
		UIButton buttonOk = new UIButton();
		buttonOk.setId("okbt");
		buttonOk.setViewId("main");
		buttonOk.setWidth("74");
		buttonOk.setClassName("blue_button_div");
		UIButton buttonCanCel = new UIButton();
		buttonCanCel.setId("cancelbt");
		buttonCanCel.setViewId("main");
		buttonCanCel.setWidth("74");
		UIFlowhPanel flowhPanel2 = uiflowhLayout.addElementToPanel(buttonCanCel);
		flowhPanel2.setFloat("right");
		flowhPanel2.setWidth("84");
		flowhPanel2.setTopPadding("12");
		UIFlowhPanel flowhPanel3 = uiflowhLayout.addElementToPanel(buttonOk);
		flowhPanel3.setFloat("right");
		flowhPanel3.setWidth("94");
		flowhPanel3.setTopPadding("12");
	}
	private void generateLocatePanle(UIFlowvLayout flowvLayout) {
		UIFlowvPanel locateTextpanel = new UIFlowvPanel();
		locateTextpanel.setId("locateTextpanel");
		locateTextpanel.setHeight("30");
		locateTextpanel.setLeftPadding("20");
		locateTextpanel.setTopPadding("6");
		locateTextpanel.setBottomBorder("#");
		flowvLayout.addPanel(locateTextpanel);
		UITextField locateField = new UITextField();
		locateField.setId("locatetext");
		locateField.setWidth("220");
		locateField.setViewId("main");
		locateTextpanel.setElement(locateField);
	}
	private void generateTreeGridLocate(UIFlowvLayout flowvLayout) {
		UIFlowvPanel borderPanelTop = new UIFlowvPanel();
		borderPanelTop.setId("topFlowvp222");
		borderPanelTop.setHeight("45");
		borderPanelTop.setLeftPadding("20");
		borderPanelTop.setTopPadding("6");
		borderPanelTop.setBottomBorder("#");
		borderPanelTop.setBottomPadding("8");
		flowvLayout.addPanel(borderPanelTop);
		UIFlowhLayout topLayout = new UIFlowhLayout();
		topLayout.setId("topLayout111");
		borderPanelTop.setElement(topLayout);
		UIFlowhPanel locateTextpanel = new UIFlowhPanel();
		locateTextpanel.setId("locateTextpanel");
		locateTextpanel.setWidth("320");
		locateTextpanel.setLeftPadding("50");
		locateTextpanel.setTopPadding("6");
		locateTextpanel.setBottomPadding("8");
		// locateTextpanel.setBottomBorder("#");
		topLayout.addPanel(locateTextpanel);
		UITextField locateField = new UITextField();
		locateField.setId("locatetext");
		locateField.setWidth("300");
		locateField.setViewId("main");
		locateTextpanel.setElement(locateField);
		UIButton buttonSearch = new UIButton();
		buttonSearch.setId("searchAllBt");
		buttonSearch.setViewId("main");
		buttonSearch.setWidth("74");
		buttonSearch.setClassName("blue_button_div");
		UIFlowhPanel flowhPanel3 = topLayout.addElementToPanel(buttonSearch);
		// flowhPanel3.setFloat("right");
		flowhPanel3.setWidth("94");
		flowhPanel3.setTopPadding("6");
		flowhPanel3.setLeftPadding("20");
		flowhPanel3.setTopPadding("6");
		flowhPanel3.setBottomPadding("8");
	}
	@Override
	public IUIPartMeta getUIMeta() {
		return super.getUIMeta();
	}
	public AppRefDftWindow() {
		super();
	}
	/**
	 * 初始化pageMeta
	 */
	protected PagePartMeta createPageMeta() {
		String pageMetaId = getPageModelId();
		LuiWebContext ctx = getWebSesssion();
		String widgetId = ctx.getParameter("widgetId");
		if (widgetId == null || widgetId.equals(""))
			throw new LuiRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		PagePartMeta parentPm = ctx.getParentPageMeta();
		GenericRefNode refnode = (GenericRefNode) parentPm.getWidget(widgetId).getViewModels().getRefNode(refnodeId);
		String pPageId = LuiRuntimeContext.getWebContext().getParentPageId();
		String key = WebConstant.CACHE_PAGEMETA + pPageId + refnodeId + pageMetaId + LuiRuntimeContext.getLangCode();
		PagePartMeta window = null;
		LUICache cache = CacheMgr.getStrongCache("reference_" + LuiRuntimeContext.getRootPath(), LuiRuntimeContext.getDatasource());
		if (LuiRuntimeContext.getMode().equals(WebConstant.MODE_PRODUCTION)) {
			window = (PagePartMeta) cache.get(key);
			if (window != null) {
				return (PagePartMeta) window.clone();
			}
		}
		window = createPageMeta(pageMetaId, refnode);
		cache.put(key, window);
		return (PagePartMeta) window.clone();
	}
	/**
	 * 创建pageMeta
	 * 
	 * @param pageMetaId
	 * @return
	 */
	private PagePartMeta createPageMeta(String pageMetaId, GenericRefNode refnode) {
		PagePartMeta window = new PagePartMeta();
		window.setId(pageMetaId);
		ViewPartMeta widget = new ViewPartMeta();
		widget.setId("main");
		if(refnode.isDefModel()){
			widget.setController(LuiRefDftCtrl.class.getName());
			BaseRefModel refModel = RefSelfUtil.getDefRefModel(refnode.getRefcode());
			LuiRefDftGen util = new LuiRefDftGen(refModel, refnode);
			createViewModel4PageMeta(widget, util, refnode, refModel);
			createViewComponents4PageMeta(widget, refnode, util, refModel);
			createCommand4PageMeta(widget, refnode, util, refModel);
		}
		else{
			widget.setController(AppRefDftCtrl.class.getName());
			IRefModel refModel = RefSelfUtil.getRefModel(refnode);
			AppRefDftGen util = new AppRefDftGen(refModel, refnode);
			createViewModel4PageMeta(widget, util, refnode, refModel);
			createViewComponents4PageMeta(widget, refnode, util, refModel);
			createCommand4PageMeta(widget, refnode, util, refModel);
		}
		window.setProcessorClazz(AppRequestProcessor.class.getName());
		window.addWidget(widget);
		boolean afterPageInit = false;
		if (afterPageInit)
			createPageMetaListener(window, refnode);
		return window;
	}
	/**
	 * 为PageMeta创建ViewModel
	 * 
	 * @param widget
	 * @param dsId
	 * @param util
	 * @param refCode
	 * @param refModel
	 */
	protected void createViewModel4PageMeta(ViewPartMeta widget, AppRefDftGen util, GenericRefNode refnode, IRefModel refModel) {
		Dataset[] dss = util.getDataset();
		for (int i = 0; i < dss.length; i++) {
			widget.getViewModels().addDataset((Dataset) dss[i]);
		}
		GenericRefNode[] refNodes = util.getRefNodes();
		if (refNodes != null) {
			for (int i = 0; i < refNodes.length; i++) {
				widget.getViewModels().addRefNode((GenericRefNode) refNodes[i]);
			}
		}
		DatasetRelation[] rel = util.getRelation();
		if (rel != null) {
			DatasetRelations rels = new DatasetRelations();
			for (int i = 0; i < rel.length; i++) {
				rels.addDsRelation(rel[i]);
			}
			widget.getViewModels().setDsrelations(rels);
		}
	}
	/**
	 * 为PageMeta创建控件
	 * 
	 * @param widget
	 * @param dsId
	 * @param refCode
	 * @param util
	 * @param refModel
	 */
	protected void createViewComponents4PageMeta(ViewPartMeta widget, GenericRefNode refnode, AppRefDftGen util, IRefModel refModel) {
		WebComp[] comps = util.getComponent();
		for (WebComp comp : comps) {
			widget.getViewComponents().addComponent(comp);
		}
		ButtonComp okBtn = createButtonComp("okbt", "60", "left", "确定", null, null);
		ButtonComp cancelBt = createButtonComp("cancelbt", "60", "left", "取消", null, null);
		widget.getViewComponents().addComponent(okBtn);
		widget.getViewComponents().addComponent(cancelBt);
		EventSubmitRule submitRule = new EventSubmitRule();
		
		EventSubmitRule parentSubmitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId(refnode.getWidget().getId());
		parentSubmitRule.addWidgetRule(pwr);
		DatasetRule parentDsRule = new DatasetRule();
		parentDsRule.setId(refnode.getWriteDataset());
		parentDsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		parentSubmitRule.getWidgetRules().find(refnode.getWidget().getId()).addDsRule(parentDsRule);
		
		submitRule.setParentSubmitRule(parentSubmitRule);
		int refType = RefSelfUtil.getRefType(refModel);
		// "表型参照"
		if (refType == IRefConst.TREE) {
			if (refnode.isOnlyLeaf()) {
				TreeRule treeRule = new TreeRule();
				treeRule.setId("reftree");
				treeRule.setType(TreeRule.TREE_CURRENT_PARENT_CHILDREN);
				WidgetRule wr = new WidgetRule();
				wr.setId("main");
				wr.addTreeRule(treeRule);
				submitRule.addWidgetRule(wr);
			}
		}
		LuiEventConf buttonOkEvent = new LuiEventConf();
		buttonOkEvent.setEventType(MouseEvent.class.getSimpleName());
		buttonOkEvent.setOnserver(true);
		buttonOkEvent.setSubmitRule(submitRule);
		buttonOkEvent.setEventName("onclick");
		buttonOkEvent.setMethod(IRefConst.OK_BUTTON_EVENT_METHOD_NAME);
		if (StringUtils.isNotBlank(refnode.getController())){
			buttonOkEvent.setControllerClazz(refnode.getController());
		}
		okBtn.addEventConf(buttonOkEvent);
		LuiEventConf buttonCancelEvent = new LuiEventConf();
		buttonCancelEvent.setEventType(MouseEvent.class.getSimpleName());
		buttonCancelEvent.setSubmitRule(submitRule);
		buttonCancelEvent.setEventName("onclick");
		buttonCancelEvent.setOnserver(false);
		buttonCancelEvent.setScript(IRefConst.CANCEL_BUTTON_EVENT_SCRIPT);
		if (StringUtils.isNotBlank(refnode.getController())){
			buttonCancelEvent.setControllerClazz(refnode.getController());
		}
		cancelBt.addEventConf(buttonCancelEvent);
	}
	/**
	 * 为参照创建高级操作区的按钮及Command
	 * 
	 * @param widget
	 * @param refCode
	 */
	private void createCommand4PageMeta(ViewPartMeta widget, GenericRefNode refnode, AppRefDftGen util, IRefModel refModel) {
		if (refnode.isLocate()) {
			StringTextComp locateComp = new StringTextComp();
			locateComp.setId("locatetext");
			locateComp.setText("定位");
			widget.getViewComponents().addComponent(locateComp);
			LuiEventConf enterEvent = KeyEvent.getOnEnterEvent();
			enterEvent.setEventType(KeyEvent.class.getSimpleName());
			enterEvent.setId("locatetextKeyListener");
			enterEvent.setOnserver(false);
			enterEvent.setScript("$.refscript.doFilter(pageUI.getViewPart('main').getComponent('locatetext').getValue())");
			locateComp.addEventConf(enterEvent);
		}
		if (refModel instanceof IRefTreeGridModel) {
			ButtonComp searchAllBtn = createButtonComp("searchAllBt", "60", "left", "搜索全部", null, null);
			LuiEventConf searchEvent = MouseEvent.getOnClickEvent();
			EventSubmitRule sr = new EventSubmitRule();
			WidgetRule wr = new WidgetRule();
			wr.setId("main");
			sr.addWidgetRule(wr);
			EventSubmitRule parentSubmitRule = new EventSubmitRule();
			String pwidgetId = refnode.getWidget().getId();
			WidgetRule parentwr = new WidgetRule();
			parentwr.setId(pwidgetId);
			parentSubmitRule.addWidgetRule(parentwr);
			ViewPartMeta parentWidget = LuiRuntimeContext.getWebContext().getParentPageMeta().getWidget(pwidgetId);
			if (parentWidget != null) {
				Dataset[] dss = parentWidget.getViewModels().getDatasets();
				for (int i = 0; i < dss.length; i++) {
					DatasetRule parentDsRule = new DatasetRule();
					parentDsRule.setId(dss[i].getId());
					parentDsRule.setType(DatasetRule.TYPE_CURRENT_PAGE);
					parentwr.addDsRule(parentDsRule);
				}
			}
			sr.setParentSubmitRule(parentSubmitRule);
			searchEvent.setSubmitRule(sr);
			searchEvent.setMethod("searchAllData");
			if (refnode.getController() != null)
				searchEvent.setControllerClazz(refnode.getController());
			searchAllBtn.addEventConf(searchEvent);
			widget.getViewComponents().addComponent(searchAllBtn);
		}
	}
	
	/**
	 * 为PageMeta创建ViewModel
	 * 
	 * @param widget
	 * @param dsId
	 * @param util
	 * @param refCode
	 * @param refModel
	 */
	protected void createViewModel4PageMeta(ViewPartMeta widget, LuiRefDftGen util, GenericRefNode refnode, BaseRefModel refModel) {
		Dataset[] dss = util.getDataset();
		for (int i = 0; i < dss.length; i++) {
			widget.getViewModels().addDataset((Dataset) dss[i]);
		}
		GenericRefNode[] refNodes = util.getRefNodes();
		if (refNodes != null) {
			for (int i = 0; i < refNodes.length; i++) {
				widget.getViewModels().addRefNode((GenericRefNode) refNodes[i]);
			}
		}
		DatasetRelation[] rel = util.getRelation();
		if (rel != null) {
			DatasetRelations rels = new DatasetRelations();
			for (int i = 0; i < rel.length; i++) {
				rels.addDsRelation(rel[i]);
			}
			widget.getViewModels().setDsrelations(rels);
		}
	}
	/**
	 * 为PageMeta创建控件
	 * 
	 * @param widget
	 * @param dsId
	 * @param refCode
	 * @param util
	 * @param refModel
	 */
	protected void createViewComponents4PageMeta(ViewPartMeta widget, GenericRefNode refnode, LuiRefDftGen util, BaseRefModel refModel) {
		WebComp[] comps = util.getComponent();
		for (WebComp comp : comps) {
			widget.getViewComponents().addComponent(comp);
		}
		ButtonComp okBtn = createButtonComp("okbt", "60", "left", "确定", null, null);
		ButtonComp cancelBt = createButtonComp("cancelbt", "60", "left", "取消", null, null);
		widget.getViewComponents().addComponent(okBtn);
		widget.getViewComponents().addComponent(cancelBt);
		EventSubmitRule submitRule = new EventSubmitRule();
		
		EventSubmitRule parentSubmitRule = new EventSubmitRule();
		WidgetRule pwr = new WidgetRule();
		pwr.setId(refnode.getWidget().getId());
		parentSubmitRule.addWidgetRule(pwr);
		DatasetRule parentDsRule = new DatasetRule();
		parentDsRule.setId(refnode.getWriteDataset());
		parentDsRule.setType(DatasetRule.TYPE_CURRENT_LINE);
		parentSubmitRule.getWidgetRules().find(refnode.getWidget().getId()).addDsRule(parentDsRule);
		
		submitRule.setParentSubmitRule(parentSubmitRule);
		int refType = RefSelfUtil.getRefType(refModel);
		// "表型参照"
		if (refType == IRefConst.TREE) {
			if (refnode.isOnlyLeaf()) {
				TreeRule treeRule = new TreeRule();
				treeRule.setId("reftree");
				treeRule.setType(TreeRule.TREE_CURRENT_PARENT_CHILDREN);
				WidgetRule wr = new WidgetRule();
				wr.setId("main");
				wr.addTreeRule(treeRule);
				submitRule.addWidgetRule(wr);
			}
		}
		LuiEventConf buttonOkEvent = new LuiEventConf();
		buttonOkEvent.setEventType(MouseEvent.class.getSimpleName());
		buttonOkEvent.setOnserver(true);
		buttonOkEvent.setSubmitRule(submitRule);
		buttonOkEvent.setEventName("onclick");
		buttonOkEvent.setMethod(IRefConst.OK_BUTTON_EVENT_METHOD_NAME);
		if (StringUtils.isNotBlank(refnode.getController())){
			buttonOkEvent.setControllerClazz(refnode.getController());
		}
		okBtn.addEventConf(buttonOkEvent);
		LuiEventConf buttonCancelEvent = new LuiEventConf();
		buttonCancelEvent.setEventType(MouseEvent.class.getSimpleName());
		buttonCancelEvent.setSubmitRule(submitRule);
		buttonCancelEvent.setEventName("onclick");
		buttonCancelEvent.setOnserver(false);
		buttonCancelEvent.setScript(IRefConst.CANCEL_BUTTON_EVENT_SCRIPT);
		if (StringUtils.isNotBlank(refnode.getController())){
			buttonCancelEvent.setControllerClazz(refnode.getController());
		}
		cancelBt.addEventConf(buttonCancelEvent);
	}
	/**
	 * 为参照创建高级操作区的按钮及Command
	 * 
	 * @param widget
	 * @param refCode
	 */
	private void createCommand4PageMeta(ViewPartMeta widget, GenericRefNode refnode, LuiRefDftGen util, BaseRefModel refModel) {
		if (refnode.isLocate()) {
			StringTextComp locateComp = new StringTextComp();
			locateComp.setId("locatetext");
			locateComp.setText("定位");
			widget.getViewComponents().addComponent(locateComp);
			LuiEventConf enterEvent = KeyEvent.getOnEnterEvent();
			enterEvent.setEventType(KeyEvent.class.getSimpleName());
			enterEvent.setId("locatetextKeyListener");
			enterEvent.setOnserver(false);
			enterEvent.setScript("$.refscript.doFilter(pageUI.getViewPart('main').getComponent('locatetext').getValue())");
			locateComp.addEventConf(enterEvent);
		}
		if (refModel instanceof TreeGridRefModel) {
			ButtonComp searchAllBtn = createButtonComp("searchAllBt", "60", "left", "搜索全部", null, null);
			LuiEventConf searchEvent = MouseEvent.getOnClickEvent();
			EventSubmitRule sr = new EventSubmitRule();
			WidgetRule wr = new WidgetRule();
			wr.setId("main");
			sr.addWidgetRule(wr);
			EventSubmitRule parentSubmitRule = new EventSubmitRule();
			String pwidgetId = refnode.getWidget().getId();
			WidgetRule parentwr = new WidgetRule();
			parentwr.setId(pwidgetId);
			parentSubmitRule.addWidgetRule(parentwr);
			ViewPartMeta parentWidget = LuiRuntimeContext.getWebContext().getParentPageMeta().getWidget(pwidgetId);
			if (parentWidget != null) {
				Dataset[] dss = parentWidget.getViewModels().getDatasets();
				for (int i = 0; i < dss.length; i++) {
					DatasetRule parentDsRule = new DatasetRule();
					parentDsRule.setId(dss[i].getId());
					parentDsRule.setType(DatasetRule.TYPE_CURRENT_PAGE);
					parentwr.addDsRule(parentDsRule);
				}
			}
			sr.setParentSubmitRule(parentSubmitRule);
			searchEvent.setSubmitRule(sr);
			searchEvent.setMethod("searchAllData");
			if (refnode.getController() != null)
				searchEvent.setControllerClazz(refnode.getController());
			searchAllBtn.addEventConf(searchEvent);
			widget.getViewComponents().addComponent(searchAllBtn);
		}
	}
	/**
	 * 创建当前页面的事件
	 * 
	 * @param meta
	 */
	private void createPageMetaListener(PagePartMeta meta, GenericRefNode refNode) {
		LuiEventConf pageEvent = PageEvent.getAfterPageInitEvent();
		pageEvent.setEventName("afterPageInit");
		pageEvent.setMethod("afterPageInit");
		pageEvent.setEventType(PageEvent.class.getSimpleName());
		pageEvent.setOnserver(true);
		if (refNode.getController() != null && !"".equals(refNode.getController()))
			pageEvent.setControllerClazz(refNode.getController());
		else
			pageEvent.setControllerClazz(AppRefDftCtrl.class.getName());
		meta.addEventConf(pageEvent);
	}
	/**
	 * 构造刷新按钮
	 * 
	 * @param widget
	 * @param pageType
	 */
	protected void createRefreshButton(ViewPartMeta widget) {
		ButtonComp refreshBtn = createButtonComp("refreshBtn", "50", "right", "刷新", "/wfw/frame/themes/" + "ncclassic" + "/images/reference/refresh.gif", "refbtn");
		widget.getViewComponents().addComponent(refreshBtn);
	}
	/**
	 * 创建按钮
	 * 
	 * @param id
	 * @param width
	 * @param align
	 * @param text
	 * @param command
	 * @param refImg
	 *            所引用图片的路径
	 * @return
	 */
	protected ButtonComp createButtonComp(String id, String width, String align, String text, String refImg, String className) {
		ButtonComp btn = new ButtonComp(id);
		btn.setText(text);
		if (refImg != null && !refImg.equals(""))
			btn.setRefImg(refImg);
		return btn;
	}
	@Override
	protected void initPageMetaStruct() {
		super.initPageMetaStruct();
		LuiWebContext ctx = getWebSesssion();
		String widgetId = ctx.getParameter("widgetId");
		ctx.getPageWebSession().setAttribute("widgetId", widgetId);
		if (widgetId == null || widgetId.equals(""))
			throw new LuiRuntimeException("未获取参照所属widget!");
		String refnodeId = ctx.getParameter("nodeId");
		ctx.getPageWebSession().setAttribute("refNodeId", refnodeId);
		ctx.getPageWebSession().setAttribute("owner", ctx.getParameter("owner"));
		getClientSession().setAttribute(ParamConstant.OTHER_PAGE_UNIQUE_ID, ctx.getParentPageUniqueId(), true);
		getClientSession().setAttribute(ParamConstant.OTHER_PAGE_ID, ctx.getParentPageId(), true);
		PagePartMeta winConfig = new PagePartMeta();
		winConfig.setCaption(this.getPageMeta().getCaption());
		winConfig.setId(getPageModelId());
		AppSession.current().getAppContext().getApplication().addWindow(winConfig);
	}
	@Override
	public String getEtag() {
		return super.getEtag();
	}
}
