package xap.lui.core.render;

import java.util.ArrayList;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.model.LuiPageContext;

public class Add2AbsoluteLayoutCmd extends AbstractRaCommand {

	UIAbsoluteLayout uiEle;
	
	public Add2AbsoluteLayoutCmd(RaParameter rp,UIElement uiEle){
		super(rp);
		this.uiEle=(UIAbsoluteLayout)uiEle;
	}
	@Override
	public void execute() {
		UIElement child = getChild(rp);
		if (UIElementFinder.findElementById(rp.getUiMeta(), child.getId()) != null) {
			throw new LuiRuntimeException("当前控件" + child.getId() + "在View中已存在！");
		}
		if (uiEle == null) {
			throw new LuiRuntimeException("请选择正确的容器！");
		}
		if(!rp.getOffsetX().equals(RaParameter.NAN))
			((UIComponent)child).setLeft(Integer.parseInt(rp.getOffsetX()));
		if(!rp.getOffsetY().equals(RaParameter.NAN))
			((UIComponent)child).setTop(Integer.parseInt(rp.getOffsetY()));
		if(uiEle.getComponentList() == null){
			uiEle.setComponentList(new ArrayList<UIComponent>());
		}
//		if(uiEle instanceof UIPartMeta) {
//			if(StringUtils.isNotBlank(((UIPartMeta)uiEle).getUiprovider())) {
//				throw new LuiRuntimeException("当前容器已含有代码VIEW！");
//			}
//		}
		// ---------------------------原直接添加逻辑----------------------
		String uiid = rp.getUiId();
		rp.setUiId(uiid);
		// 对grid的特殊处理
		//TODO:此处会影响表头自动扩展，原来不知何故特殊处理，现将其注释， by lxl 2015.9.9
//		if (child instanceof UIGridComp) {
//			((UIGridComp) child).setAutoExpand(UIConstant.FALSE);
//		}
		// 后台添加
		this.addUIElement(uiEle, child);
		// 构造callServer参数对象
		ParamObject paramObj = new ParamObject();
		paramObj.widgetId = rp.getWidgetId();
		paramObj.uiId = child.getId();
		if (child instanceof UIComponent) {
			paramObj.eleId = child.getId();
		}
		// 针对UIFormElement进行处理
		if (child instanceof UIFormElement) {
			String formId = ((UIFormElement) child).getFormId();
			// 如果是在form中的复杂控件
			if (formId != null) {
				paramObj.eleId = formId;
				paramObj.subEleId = child.getId();
				paramObj.type = LuiPageContext.SOURCE_TYPE_FORMELE;
			} else {
				paramObj.type = LuiPageContext.SOURCE_FORMELEMENT;
			}
		} else {
			if (rp.getCurrentDropObjType2() == null)
				paramObj.type = rp.getCurrentDropObjType() == null ? rp.getCurrentDropObj() : rp.getCurrentDropObjType();
			else
				paramObj.type = rp.getCurrentDropObjType2();
		}
		
	}
//	/**
//	 * 具有用户规则的添加：上下、左右
//	 */
//	private void processCmdWithDirection(String processRule, UIElement child, UILayoutPanel uiEle) {
//		// -----------------------------------1:添加到容器的上下---------------------------------------
//		if (RaCmdConst.A2P_DOWN.equals(processRule) || RaCmdConst.A2P_UP.equals(processRule)) {
//			// 查找纵向容器
//			UIFlowvPanel vPanel = findVPanel(uiEle);
//			if (vPanel != null) {
//				UIFlowvLayout vLayout = (UIFlowvLayout) vPanel.getLayout();
//				UIElementFactory uf = new UIElementFactory();
//				// 创建panel
//				UILayoutPanel newUiEle = (UILayoutPanel) uf.createUIElement(LuiPageContext.SOURCE_TYPE_FLOWVPANEL, rp.getWidgetId());
//				String t = randomT(4);
//				newUiEle.setId(LuiPageContext.SOURCE_TYPE_FLOWVPANEL + "_" + t);
//				newUiEle.setLayout(vLayout);
//				// 判断是否是最后一个panel
//				List<UILayoutPanel> panelList = vLayout.getPanelList();
//				if (panelList.size() == panelList.indexOf(vPanel) + 1 && RaCmdConst.A2P_DOWN.equals(processRule)) {
//					// 添加到最后
//					vLayout.addPanel(newUiEle);
//				} else {
//					vLayout.addPanel(newUiEle, vPanel, RaCmdConst.A2P_DOWN.equals(processRule));
//				}
//				// 调用AddPanelCmd完成vPanel的增加
//				RaParameter param = new RaParameter(AppSession.current());
//				param.setWidgetId(rp.getWidgetId());
//				param.setCurrentDropObjType2("isPanel");
//				param.setType(LuiPageContext.SOURCE_TYPE_FLOWVPANEL);
//				param.setUiId(newUiEle.getId());
//				param.setPageMeta(rp.getPageMeta());
//				param.setUiMeta(rp.getUiMeta());
//				ParamObject paramObj = new ParamObject();
//				paramObj.widgetId = rp.getWidgetId();
//				paramObj.uiId = child.getId();
//				paramObj.type = rp.getCurrentDropObjType() == null ? rp.getCurrentDropObj() : rp.getCurrentDropObjType();
//				callServer(paramObj, RaParameter.ADD);
//				// 调用cmd添加panel
//				CmdInvoker.invoke(new AddPanelCmd(param, newUiEle));
//				rp.setUiId(newUiEle.getId());
//				// 将目标添加到新建的panel中，模拟正常的Add2PanelCmd
//				this.addUIElement(newUiEle, child);
//			}
//			// 如果为空，则增加纵向布局
//			else {
//				// 重布局
//				redoLayoutWithPanels(processRule, child);
//			}
//		}
//		// -----------------------------------2:添加到元素的左右--------------------------------
//		else if (RaCmdConst.A2P_LEFT.equals(processRule) || RaCmdConst.A2P_RIGHT.equals(processRule)) {
//			// 重布局
//			redoLayoutWithPanels(processRule, child);
//		}
//	}
//	// 重布局
//	private void redoLayoutWithPanels(String processRule, UIElement child) {
//		UIElement copyEle = (UIElement) uiEle.getElement().doClone();
//		WebComp comp = rp.getPageMeta().getWidget(rp.getWidgetId()).getViewComponents().getComponent(uiEle.getElement().getId());
//		WebComp copyComp = null;
//		if (comp != null)
//			copyComp = (WebComp) comp.clone();
//		// 删除原Element
//		rp.setUiId(uiEle.getId());
//		rp.setSubuiId(uiEle.getElement().getId());
//		CmdInvoker.invoke(new DeleteCmd(rp));
//		if (copyComp != null)
//			rp.getPageMeta().getWidget(rp.getWidgetId()).getViewComponents().addComponent(copyComp);
//		String widgetId = rp.getWidgetId();
//		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
//		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//		// 创建新的flowlayout
//		UILayout flowLayout = null;
//		UILayoutPanel panel1 = null;
//		UILayoutPanel panel2 = null;
//		// 创建新的flowlayout
//		if (RaCmdConst.A2P_UP.equals(processRule) || RaCmdConst.A2P_DOWN.equals(processRule)) {
//			flowLayout = new UIFlowvLayout();
//			flowLayout.setId("flowvlayout_" + random.nextInt(10000));
//			flowLayout.setViewId(widgetId);
//			// 创建两个纵向容器并添加新老元素
//			panel1 = new UIFlowvPanel();
//			((UIFlowvPanel) panel1).setHeight("50%");
//			panel2 = new UIFlowvPanel();
//		} else {
//			flowLayout = new UIFlowhLayout();
//			flowLayout.setId("flowhlayout_" + random.nextInt(10000));
//			flowLayout.setViewId(widgetId);
//			// 创建两个横向容器并添加新老元素
//			panel1 = new UIFlowhPanel();
//			((UIFlowhPanel) panel1).setWidth("50%");
//			panel2 = new UIFlowhPanel();
//		}
//		panel1.setId("flowpanel1_" + random.nextInt(999999));
//		panel2.setId("flowpanel2_" + random.nextInt(999999));
//		panel1.setViewId(widgetId);
//		panel1.setElement(copyEle);
//		panel2.setViewId(widgetId);
//		panel2.setElement(child);
//		// 控制左右
//		if (RaCmdConst.A2P_LEFT.equals(processRule) || RaCmdConst.A2P_UP.equals(processRule)) {
//			flowLayout.addPanel(panel2);
//			flowLayout.addPanel(panel1);
//		} else {
//			flowLayout.addPanel(panel1);
//			flowLayout.addPanel(panel2);
//		}
//		RequestLifeCycleContext.get().setPhase(phase);
//		this.addUIElement(uiEle, flowLayout);
//		// 刷新布局
//		refreshLayout(flowLayout, child, copyEle);
//	}
//	/**
//	 * 查找纵向容器
//	 * 
//	 * @param uiEle
//	 * @return UIFlowvPanel
//	 */
//	private UIFlowvPanel findVPanel(UILayoutPanel uiEle) {
//		// 查找纵向容器
//		UIFlowvPanel vPanel = null;
//		if (uiEle instanceof UIFlowvPanel) {
//			vPanel = (UIFlowvPanel) uiEle;
//		} else {
//			// 如果不是纵向布局，寻找外层的纵向容器
//			vPanel = findOuterVPanel(rp.getUiMeta(), uiEle);
//		}
//		return vPanel;
//	}
//	/**
//	 * 寻找外层的纵向容器
//	 * 
//	 * @param um
//	 * @param uiPanel
//	 * @return UIFlowvPanel
//	 */
//	private UIFlowvPanel findOuterVPanel(UIPartMeta um, UIElement uiPanel) {
//		if (uiPanel.getId() != null && uiPanel.getId().endsWith("_um"))
//			return null;
//		UIElement parentEle = UIElementFinder.findParent(rp.getUiMeta(), uiPanel);
//		if (parentEle instanceof UIFlowvPanel) {
//			return (UIFlowvPanel) parentEle;
//		} else {
//			return findOuterVPanel(um, parentEle);
//		}
//	}
//	private void refreshLayout(UILayout newLayout, UIElement child, UIElement copyEle) {
//		// 构造callServer参数对象,刷新树
//		ParamObject paramObj4Layout = new ParamObject();
//		paramObj4Layout.widgetId = rp.getWidgetId();
//		paramObj4Layout.uiId = newLayout.getId();
//		callServer(paramObj4Layout, RaParameter.ADD);
//		ParamObject preParamObj = new ParamObject();
//		preParamObj.widgetId = rp.getWidgetId();
//		preParamObj.uiId = copyEle.getId();
//		callServerWithDelay(preParamObj, RaParameter.ADD, "100");
//		ParamObject paramObj = new ParamObject();
//		paramObj.widgetId = rp.getWidgetId();
//		paramObj.uiId = child.getId();
//		callServerWithDelay(paramObj, RaParameter.ADD, "200");
//	}

}
