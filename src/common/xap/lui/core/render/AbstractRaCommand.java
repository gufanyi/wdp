package xap.lui.core.render;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.builder.UIPartMetaDftBuilder;
import xap.lui.core.command.ICommand;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.ViewElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.CompIdGenerator;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.FieldRelation;
import xap.lui.core.dataset.MDComboDataConf;
import xap.lui.core.dataset.MatchField;
import xap.lui.core.dataset.Row;
import xap.lui.core.dataset.WhereField;
import xap.lui.core.exception.ComboInputItem;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIAbsoluteLayout;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UIGridLayout;
import xap.lui.core.layout.UIGridPanel;
import xap.lui.core.layout.UIGridRowLayout;
import xap.lui.core.layout.UIGridRowPanel;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UITextField;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.parser.UIMetaParserUtil;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.core.util.UIElementFactory;

/**
 * 界面调整基类
 * 
 * @author licza
 *
 */
public abstract class AbstractRaCommand implements ICommand {
	protected RaParameter rp;

	public AbstractRaCommand(RaParameter rp) {
		super();
		this.rp = rp;
	}

	/**
	 * 通知前台js脚本进行相应的修改
	 * 
	 * @param paramObj
	 * @param ope
	 */
	public void callServer(ParamObject paramObj, String ope) {
		StringBuffer buf = new StringBuffer();
		buf.append("{widgetid:'").append(paramObj.widgetId).append("',eleid:'").append(paramObj.eleId).append("',direction:'").append(paramObj.direction).append("',subeleid:'")
				.append(paramObj.subEleId).append("',uiid:'").append(paramObj.uiId).append("',subuiid:'").append(paramObj.subuiId).append("',type:'").append(paramObj.type).append("'}");
		String script = "callParent(" + buf.toString() + ", '" + ope + "')";
		rp.getLpc().getAppContext().addExecScript(script);
	}

	public void callServerWithDelay(ParamObject paramObj, String ope, String delayTime) {
		StringBuffer buf = new StringBuffer();
		buf.append("{widgetid:'").append(paramObj.widgetId).append("',eleid:'").append(paramObj.eleId).append("',direction:'").append(paramObj.direction).append("',subeleid:'")
				.append(paramObj.subEleId).append("',uiid:'").append(paramObj.uiId).append("',subuiid:'").append(paramObj.subuiId).append("',type:'").append(paramObj.type).append("'}");
		String script = "callParent(" + buf.toString() + ", '" + ope + "')";
		rp.getLpc().getAppContext().addExecScript("window.setTimeout(function(){" + script + "},'" + delayTime + "');");
	}

	/**
	 * 设置对应的属性值
	 * 
	 * @param uiEle
	 * @param param
	 * @throws Exception
	 */
	public void setContext(Object uiEle, UpdateParameter param) throws Exception {
		if (param == null || param.getAttr() == null)
			return;
		String upCaseAttr = StringUtils.upperCase(param.getAttr().substring(0, 1)) + param.getAttr().substring(1);
		String newValue = param.getNewValue();
		String oldValue = param.getOldValue();
		Method m = null;
		if (StringDataTypeConst.bOOLEAN.equals(param.getAttrType())) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, boolean.class);
			boolean value = false;
			if (oldValue != null)
				value = oldValue.equals("Y") ? true : false;
			if (newValue != null)
				value = newValue.equals("Y") ? true : false;
			m.invoke(uiEle, value);
		} else if (StringDataTypeConst.BOOLEAN.equals(param.getAttrType())) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, Boolean.class);
			Boolean value = false;
			if (oldValue != null)
				value = oldValue.equals("Y") ? true : false;
			if (newValue != null)
				value = newValue.equals("Y") ? true : false;
			m.invoke(uiEle, value);
		} else if (StringDataTypeConst.INT.equals(param.getAttrType())) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, int.class);
			int value = 0;
			if (oldValue != null)
				value = Integer.valueOf(oldValue);
			if (newValue != null)
				value = Integer.valueOf(newValue);
			m.invoke(uiEle, value);
		} else if (StringDataTypeConst.INTEGER.equals(param.getAttrType())) {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, Integer.class);
			Integer value = null;
			if (oldValue != null)
				value = Integer.valueOf(param.getOldValue());
			if (newValue != null)
				value = Integer.valueOf(newValue);
			m.invoke(uiEle, value);
		} else {
			m = uiEle.getClass().getMethod("set" + upCaseAttr, param.getAttrType().getClass());
			m.invoke(uiEle, newValue);
		}
	}

	/**
	 * 创建uimeta
	 * 
	 * @param pm
	 * @param pageId
	 * @param widgetId
	 * @return
	 */
	protected UIPartMeta createUIMeta(PagePartMeta pm, String pageId, String widgetId) {
		ViewPartConfig wconf = pm.getViewPartConf(widgetId);
		UIPartMeta meta = null;
		LifeCyclePhase oriPhase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		if (wconf.getRefId().startsWith("..")) {
			UIMetaParserUtil parserUtil = new UIMetaParserUtil();
			parserUtil.setPagemeta(pm);
			meta = parserUtil.parseUIMeta((String) null,pm.getId(), wconf.getId());
		} else {

			UIPartMetaDftBuilder builder = new UIPartMetaDftBuilder();
			meta = builder.buildUIMeta(pm, pageId, widgetId);
		}
		RequestLifeCycleContext.get().setPhase(oriPhase);
		return meta;
	}

	/**
	 * 检查panel中是否有元素，如果有则进行提示
	 * 
	 * @param uiEle
	 */
	protected String checkPanelElement(UILayoutPanel uiEle) {
		String flag = RaCmdConst.A2P_UP;
		if (uiEle.getElement() != null) {
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			// 将ajax的状态置为nullstatus
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			ComboInputItem ori = new ComboInputItem("processRule", "规则", true);
			ComboData cd = new DataList();

			DataItem replaceRule = new DataItem(RaCmdConst.A2P_REPLACE,"替换当前元素");
			cd.addDataItem(replaceRule);

			DataItem addUpRule = new DataItem(RaCmdConst.A2P_UP,"增加到容器上方");
			cd.addDataItem(addUpRule);

			DataItem addDownRule = new DataItem(RaCmdConst.A2P_DOWN,"增加到容器下方");
			cd.addDataItem(addDownRule);

			DataItem addLeftRule = new DataItem(RaCmdConst.A2P_LEFT,"增加到元素左侧");
			cd.addDataItem(addLeftRule);

			DataItem addRightRule = new DataItem(RaCmdConst.A2P_RIGHT,"增加到元素右侧");
			cd.addDataItem(addRightRule);

			ori.setComboData(cd);
			ori.setValue(RaCmdConst.A2P_REPLACE);
			RequestLifeCycleContext.get().setPhase(phase);
			InteractionUtil.showInputDialog("processRuleDlg", "容器中已有元素,请处理", new InputItem[] { ori });

			flag = InteractionUtil.getInputDialogResult("processRuleDlg").get("processRule");
		}

		return flag;
	}

	/**
	 * 添加元素
	 * 
	 * @param parent
	 * @param child
	 */
	protected void addUIElement(UIElement parent, UIElement child) {
		if (parent == null || child == null)
			return;
		if (parent instanceof UILayout) {
			((UILayout) parent).addPanel((UILayoutPanel) child);
		} else if (parent instanceof UILayoutPanel) {
			((UILayoutPanel) parent).setElement(child);
		} else if (parent instanceof UIAbsoluteLayout) {
			((UIAbsoluteLayout) parent).addComponent((UIComponent)child);
		}
	}

	/**
	 * 判断是否为参照的子字段，参数为从前台拖放的时候传来的参数<br>
	 * 根据两个值是否相等判断是否为参照的数据集的字段
	 * 
	 * @param queryKeyValue
	 * @param pid
	 * @return boolean
	 */
	public boolean isFieldOfRef(String queryKeyValue, String pId) {
		boolean isRef = false;
		if (pId.equals(queryKeyValue)) {
			isRef = true;
		}
		return isRef;
	}

	public Field getPKField(Dataset refDs) {
		Field primaryKeyField = null;
		for (Field field : refDs.getFieldList()) {
			if (field.isPK()) {
				primaryKeyField = field;
				break;
			}
		}
		return primaryKeyField;
	}

	/**
	 * 根据参数，获取当前要操作的对象
	 * 
	 * @param rp
	 * @return
	 */
	protected UIElement getChild(RaParameter rp) {
		PagePartMeta pageMeta = rp.getPageMeta();
		String currentDropObjId = rp.getCurrentDropObj();
		String currentDropObjType = rp.getCurrentDropObjType();
		String currentDropObjType2 = rp.getCurrentDropObjType2();
		String currentDropDsId = rp.getCurrentDropDsId();
		String widget = rp.getWidgetId();
		String pId = rp.getCurrentDropPid();
		String queryKeyValue = rp.getQueryKeyValue();

		if (currentDropObjType != null && !currentDropObjType.equals("")) {
			UIElement child = null;
			if (currentDropObjType.equals(LuiPageContext.SOURCE_TYPE_WIDGT)) {// widget的特殊处理
				UIViewPart uiWidget = new UIViewPart();
				LifeCyclePhase pa = RequestLifeCycleContext.get().getPhase();
				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
				uiWidget.setId(currentDropObjId);
				//RequestLifeCycleContext.get().setPhase(pa);
				String pageId = LuiRuntimeContext.getWebContext().getPageId();
				uiWidget.setUimeta(this.createUIMeta(pageMeta, pageId, currentDropObjId));
				RequestLifeCycleContext.get().setPhase(pa);
				child = uiWidget;
			}
			// 对传过来是数据集的进行处理
			else if (currentDropObjType.equals("DATASET")) {
				// 判断是否为参照的子字段
				boolean isRef = isFieldOfRef(queryKeyValue, pId);
				if (isRef) {
					child = refDataset2FormEle(pageMeta, currentDropObjId, currentDropDsId, widget, pId);
				} else {
					// 通过dataset的id查找对应的form
					child = dataset2Form(pageMeta, currentDropObjId, currentDropDsId, widget);
				}
			} else if (currentDropObjType.equals("combo")) {

//				LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
//				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
//				try {
//					UwComboVO[] combovo = CRUDHelper.getCRUDService().queryVOs(UwComboVO.PK_COMBO + " = '" + currentDropObjId + "'", UwComboVO.class, null, null);
//					if (combovo != null && combovo.length == 1) {
//						UwComboVO combo = combovo[0];
//						String uiStr = combo.doGetUimeta();
//						String widgetStr = combo.doGetWidget();
//						InputStream winput = IOUtils.toInputStream(widgetStr, "UTF-8");
//						InputStream uiinput = IOUtils.toInputStream(uiStr);
//						ViewPartMeta wdt = ViewPartMeta.parse(winput);
//						UIPartMeta um = null;// (new UIMetaParserUtil()).parseUIMeta(uiinput, null, null);
//
//						ViewPartMeta oriWidget = pageMeta.getWidget(widget);
//						UIPartMeta oriUm = UIElementFinder.findUIMeta(rp.getUiMeta(), widget);
//						return PaComboHelper.processCombo(um, wdt, oriUm, oriWidget);
//					}
//				} catch (Exception e) {
//					LuiLogger.error(e.getMessage(), e);
//					throw new LuiRuntimeException("创建失败:" + e.getMessage());
//				} finally {
//					RequestLifeCycleContext.get().setPhase(phase);
//				}
			} else if (currentDropObjType.equals(LuiPageContext.SOURCE_TYPE_TEXT)) {
				if (currentDropObjId != null && !currentDropObjId.equals("")) {
					UIElementFactory uf = new UIElementFactory();
					UITextField text = (UITextField) uf.createUIElement(currentDropObjType, widget);
					text.setId(currentDropObjId);
					text.setType(currentDropObjType);
					child = text;
				} else {
					// 创建未定义的 text 控件
					child = dealTextField(pageMeta, currentDropObjType, currentDropObjType2, widget);
				}

			} else if (LuiPageContext.SOURCE_TYPE_FORMELE.equals(currentDropObjType)) {
				FormElement newFrmEle = null;
				String formId = null;
				WebComp[] wcs = pageMeta.getWidget(widget).getViewComponents().getComps();
				if (wcs != null && wcs.length > 0) {
					for (WebComp wc : wcs) {
						if (wc instanceof FormComp) {
							FormComp fc = (FormComp) wc;
							FormElement el = fc.getElementById(currentDropObjId);
							if (el != null) {
								newFrmEle = (FormElement) el;
								formId = fc.getId();
							}
						}
					}
				}
				if (newFrmEle != null) {
					UIFormElement newUIFormEle = new UIElementFactory().createFormElement(newFrmEle.getId(), formId, widget, newFrmEle.getEditorType());
					child = newUIFormEle;
				}
			} else {
				UIElementFactory uf = new UIElementFactory();
				child = uf.createUIElement(currentDropObjType, widget);
				if (child != null&&StringUtils.isNotBlank(currentDropObjId)) {
					if (currentDropObjId.endsWith(LuiPageContext.UNKNOWN_LAYOUT_ID)) {
						child.setId(currentDropObjType + randomT(4));
					} else {
						child.setId(currentDropObjId);
					}
				}
			}
			return child;
		} else {
			String t = randomT(4);
			UIElementFactory uf = new UIElementFactory();
			UIElement child = uf.createUIElement(currentDropObjId, widget);
			if (child == null)
				throw new LuiRuntimeException("创建UIElement失败 !");
			child.setId(currentDropObjId + t);
			return child;
		}
	}

	/**
	 * 处理控件
	 * 
	 * @param pageMeta
	 * @param currentDropObjType
	 * @param currentDropObjType2
	 * @param widget
	 * @return
	 */
	protected UIElement dealTextField(PagePartMeta pageMeta, String currentDropObjType, String currentDropObjType2, String widget) {
		LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
		RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
		UIElement child;
		UIElementFactory uf = new UIElementFactory();
		UITextField text = (UITextField) uf.createUIElement(currentDropObjType, widget);
		String t = randomT(4);
		text.setId(currentDropObjType + t);
		text.setType(currentDropObjType2);
		if (!StringUtils.isBlank(currentDropObjType2) && (EditorTypeConst.CHECKBOXGROUP.equals(currentDropObjType2) || EditorTypeConst.RADIOGROUP.equals(currentDropObjType2))) {
			text.setWidth("240");
		}
		text.setHeight("28");
		text.setValgin("Y");
		WebComp webComp = uf.createWebComponent(text);
		addComponent(pageMeta, widget, webComp);
		initOtherPageMeta(widget, webComp);
		child = text;
		RequestLifeCycleContext.get().setPhase(phase);
		return child;
	}

	/**
	 * 添加webcomponent时，需要添加到 原始的和外部的 pagemeta中
	 * 
	 * @param widget
	 * @param webComp
	 */
	protected void initOtherPageMeta(String widget, WebComp webComp) {
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		String pageId = LuiRuntimeContext.getWebContext().getPageWebSession().getPageId();
		IPaEditorService ipaService =   new PaEditorServiceImpl();
		PagePartMeta outPageMeta = ipaService.getOriPageMeta(pageId, sessionId);
		try {
			addComponent(outPageMeta, widget, (WebComp) webComp.clone());
		} catch (Exception e) {
			// //do nothing
		}
	}

	/**
	 * 将webcomponent添加到pagemeta中
	 * 
	 * @param pageMeta
	 * @param widget
	 * @param webComp
	 */
	protected void addComponent(PagePartMeta pageMeta, String widget, WebComp webComp) {
		ViewPartMeta luiWidget = pageMeta.getWidget(widget);
		if (luiWidget != null) {
			if (webComp instanceof xap.lui.core.comps.MenubarComp) {
				luiWidget.getViewMenus().addMenuBar((xap.lui.core.comps.MenubarComp) webComp);
			} else {
				luiWidget.getViewComponents().addComponent(webComp);
			}

		}
	}

	/**
	 * 用时间轴随机生成一串数字
	 * 
	 * @param length
	 * @return
	 */
	public static String randomT(int length) {
		String t = String.valueOf(System.currentTimeMillis());
		return t.substring(t.length() - length);
	}

	/**
	 * 
	 * 自由表单，dataset获取formElement
	 * 
	 * @param pageMeta
	 * @param fieldKey
	 *            数据集的字段的id
	 * @param datasetId
	 *            数据集id
	 * @param viewId
	 * @return
	 */
	protected UIElement dataset2Form(PagePartMeta pageMeta, String fieldKey, String datasetId, String widgetId) {
		String formId = datasetId + "_newFrm";
		FormComp form = (FormComp) UIElementFinder.findWebElementById(pageMeta, widgetId, formId);
		if (form == null) {
			FormComp newFrm = new FormComp();
			newFrm.setId(formId);
			newFrm.setDataset(datasetId);

			pageMeta.getWidget(widgetId).getViewComponents().addComponent(newFrm);
			form = newFrm;
		}
		// 根据dataset中的一行，查找FormComp中的FormElement
		ViewPartMeta widget = pageMeta.getWidget(widgetId);
		Dataset ds = widget.getViewModels().getDataset(datasetId);

		Field field = ds.getField(fieldKey);

		ViewElement[] allElements = setField2FormElement(widget, ds, field);
		if (allElements != null && allElements.length > 0) {
			FormElement formEle = (FormElement) allElements[0];
			// 根据生成的formEle字段，判断是form中是存在此FormElement
			FormElement formElement = form.getElementById(formEle.getId());

			if (formElement == null) {
				form.addElement(formEle);
				// 如果生成了参照，将参照加入的pagemeta中
				if (allElements.length >= 3) {
					IRefNode refNode = null;
					if (allElements[1] instanceof IRefNode) {
						refNode = (IRefNode) allElements[1];
					}
					if (refNode != null) {
						pageMeta.getWidget(widgetId).getViewModels().addRefNode(refNode);
					}
					MDComboDataConf combo = null;
					if (allElements[2] instanceof MDComboDataConf) {
						combo = (MDComboDataConf) allElements[2];
					}
					if (combo != null) {
						pageMeta.getWidget(widgetId).getViewModels().addComboData(combo);
					}
				}
			}
			UIFormElement uiFormEle = new UIElementFactory().createFormElement(formEle.getId(), form.getId(), widgetId, formEle.getEditorType());
			return uiFormEle;
		}
		return null;
	}

	/**
	 * 将参照数据集下的子项放到view中
	 * 
	 * @param pageMeta
	 * @param fieldKey
	 * @param datasetId
	 * @param refDs
	 * @param widgetId
	 * @return UIElement
	 */
	protected UIElement refDataset2FormEle(PagePartMeta pageMeta, String currentDropObjId, String currentDropDsId, String widgetId, String pId) {
		ViewPartMeta editWidget = pageMeta.getWidget(widgetId);
		Dataset editDs = editWidget.getViewModels().getDataset(currentDropDsId);
		// 目前只支持两级，即到参照的下一级字段
		if (editDs == null) {
			throw new LuiRuntimeException("请选择正确的参照对应的数据集的字段!");
		}

		// 获取参照数据集
		String pkOfRefDs = pId.substring(pId.lastIndexOf(",") + 1);
		String idOfRefDs = editDs.getFieldRelations().getFieldRelation(pkOfRefDs + "_rel").getRefDataset();

		Dataset refDs = editWidget.getViewModels().getDataset(idOfRefDs);
		Field refField = refDs.getField(currentDropObjId);
		Field primaryKeyField = getPKField(refDs);

		// 新增的field的ID:参照数据集的pk和字段的拼接
		String newFieldId = editDs.getId() + "_" + pkOfRefDs + "_" + currentDropObjId;

		String label = refField.getText();
		// 向源ds中添加字段
		Field field = new Field();
		field.setId(newFieldId);
		field.setText(label);
		field.setDataType("String");
		field.setRequire(false);
		field.setPK(false);
		if (editDs.getField(newFieldId) == null) {
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			// 将ajax的状态置为nullstatus
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			editDs.addField(field);
			RequestLifeCycleContext.get().setPhase(phase);
		}

		// 向源ds添加FieldRelation
		FieldRelation fr = new FieldRelation();
		fr.setId(newFieldId + "_rel");
		// 对应参照的ds的ID
		fr.setRefDataset(idOfRefDs);
		MatchField mf = new MatchField();
		// 选中行的字段的id
		mf.setReadField(currentDropObjId);

		mf.setWriteField(field.getId());
		MatchField[] mfs = { mf };
		fr.setMatchFields(mfs);
		// 对应参照的ds的pk
		WhereField wf = new WhereField(primaryKeyField.getId(), pkOfRefDs);
		fr.setWhereField(wf);
		editDs.getFieldRelations().addFieldRelation(fr);

		String formId = currentDropDsId + "_newFrm";
		FormComp form = (FormComp) UIElementFinder.findWebElementById(pageMeta, widgetId, formId);
		if (form == null) {
			FormComp newFrm = new FormComp();
			newFrm.setId(formId);
			newFrm.setDataset(currentDropDsId);

			pageMeta.getWidget(widgetId).getViewComponents().addComponent(newFrm);
			form = newFrm;
		}

		ViewElement[] allElements = setField2FormElement(editWidget, editDs, field);
		if (allElements != null && allElements.length > 0) {
			FormElement formEle = (FormElement) allElements[0];
			// 根据生成的formEle字段，判断是form中是存在此FormElement
			FormElement formElement = form.getElementById(formEle.getId());

			if (formElement == null) {
				form.addElement(formEle);
				// 如果生成了参照，将参照加入的pagemeta中
				if (allElements.length >= 3) {
					IRefNode refNode = null;
					if (allElements[1] instanceof IRefNode) {
						refNode = (IRefNode) allElements[1];
					}
					if (refNode != null) {
						pageMeta.getWidget(widgetId).getViewModels().addRefNode(refNode);
					}
					MDComboDataConf combo = null;
					if (allElements[2] instanceof MDComboDataConf) {
						combo = (MDComboDataConf) allElements[2];
					}
					if (combo != null) {
						pageMeta.getWidget(widgetId).getViewModels().addComboData(combo);
					}
				}
			}
			UIFormElement uiFormEle = new UIElementFactory().createFormElement(formEle.getId(), form.getId(), widgetId, formEle.getEditorType());
			return uiFormEle;
		}
		return null;
	}

	/**
	 * 将数据集中的元素对应到formelement中
	 * 
	 * @param widget
	 * @param ds
	 * @param field
	 * @return
	 */
	protected ViewElement[] setField2FormElement(ViewPartMeta widget, Dataset ds, Field field) {
		FormElement frmEle = new FormElement();
		// 如果是Field有引用的sourceId，需要生成额外的Field字段
		Dataset entityDs = LuiRuntimeContext.getWebContext().getParentPageMeta().getWidget("data").getViewModels().getDataset("entityds");
		Row row = entityDs.getSelectedRow();
		String parentExtSourceAttr = null;
		Field parentField = null;
		GenericRefNode refNode = null;
		String editorType = null;
		// 是下拉框
		MDComboDataConf combo = null;

		if (field.getExtSource() != null && field.getExtSource().equals(Field.SOURCE_ENUM)) {
			editorType = EditorTypeConst.COMBODATA;
			String comboDataFlag = CompIdGenerator.generateComboCompId(ds.getId(), field.getId());
			frmEle.setRefComboData(comboDataFlag);
			combo = (MDComboDataConf) widget.getViewModels().getComboData(comboDataFlag);
			if (combo == null) {
				combo = new MDComboDataConf();
				combo.setId(comboDataFlag);
				combo.setFullclassName(field.getExtSourceAttr());
				combo.setCaption(field.getText());
			}
		}
		// 如果本身引用的是参照字段
		else if (field.getExtSource() != null && field.getExtSource().equals(Field.SOURCE_MD)) {
			editorType = EditorTypeConst.REFERENCE;
			String refKey = field.getId();

			// String extAttr = field.getExtSourceAttr();
			// IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
			// IBusinessEntity bean = null;
			// try {
			// bean = (IBusinessEntity) qry.getBeanByFullName(extAttr);
			// } catch (MetaDataException e) {
			// LuiLogger.error(e.getMessage(), e);
			// }
			// String showName = LuiMdUtil.getMdItfAttr(bean,
			// IBDObject.class.getName(), "name");
			// String refNodeId = CompIdGenerator.generateRefCompId(ds.getId(),
			// refKey + "_" + showName);
			// IRefNode gerefNode =
			// widget.getViewModels().getRefNode(refNodeId);
			// if(gerefNode != null){
			// frmEle.setRefNode(refNodeId);
			// }
		}
		if (editorType == null)
			editorType = EditorTypeConst.getEditorTypeByString(field.getDataType());

		if (editorType.equals(EditorTypeConst.STRINGTEXT)) {
			editorType = this.showInputDialog(editorType);
		}

		frmEle.setEditorType(editorType);
		if (row != null) {
			int pIdIndex = entityDs.nameToIndex("pid");
			String parentId = (String) row.getValue(pIdIndex);
			if (parentId != null) {
				parentField = ds.getField(parentId);
				if (parentField != null) {
					// 生成扩展字段
					int idIndex = entityDs.nameToIndex("id");
					int nameIndex = entityDs.nameToIndex("name");
					String fieldId = (String) row.getValue(idIndex);
					String name = (String) row.getValue(nameIndex);
					Field extField = ds.getField(parentId + "_" + fieldId);
					if (extField == null) {
						extField = new Field();
						extField.setId(parentId + "_" + fieldId);
						extField.setField(null);
						extField.setDefaultValue(null);
						extField.setText(name);
						extField.setI18nName(name);
						extField.setDataType(StringDataTypeConst.STRING);
						ds.addField(extField);
						// 生成FieldRelation
						FieldRelation fieldRelation = new FieldRelation();
						fieldRelation.setId(parentId + "_" + fieldId + "_" + "rel");
						if (parentField != null) {
							parentExtSourceAttr = parentField.getExtSourceAttr();
							String refDsId = "$refds_" + parentExtSourceAttr.replaceAll("\\.", "_");
							fieldRelation.setRefDataset(refDsId);
							MatchField mf = new MatchField();
							mf.setReadField(fieldId);
							mf.setWriteField(extField.getId());
							fieldRelation.addMatchField(mf);
							WhereField whereField = new WhereField();
							whereField.setKey(parentId);
							whereField.setValue(parentId);
							fieldRelation.setWhereField(whereField);
							ds.getFieldRelations().addFieldRelation(fieldRelation);
							ds.addField(extField);

						}
					}
					field = extField;
					// 如果是参照，需要生成参照refnode
					refNode = generateRefNode(ds, field, parentField, editorType);
				}
			}
		}
		frmEle.setId(field.getId());
		frmEle.setField(field.getId());
		frmEle.setText(field.getText());
		frmEle.setI18nName(field.getI18nName());

		// datatype
		frmEle.setDataType(field.getDataType());
		frmEle.setPrecision(field.getPrecision());

		frmEle.setIsRequire(field.isRequire());

		if (refNode != null)
			frmEle.setRefNode(refNode.getId());
		return new ViewElement[] { frmEle, refNode, combo };
	}

	/**
	 * 如果需要生成参照，生成参照自从
	 * 
	 * @param ds
	 * @param field
	 * @param parentField
	 * @param editorType
	 * @return
	 */
	protected GenericRefNode generateRefNode(Dataset ds, Field field, Field parentField, String editorType) {
		// NCRefNode refNode = null;
		// if(editorType != null &&
		// editorType.equals(EditorTypeConst.REFERENCE)){
		// if(parentField != null){
		// if(ds instanceof MdDataset){
		// String objMeta = ((MdDataset)ds).getObjMeta();
		// IMDQueryFacade qry = MDBaseQueryFacade.getInstance();
		// try {
		// IBean bean = qry.getBeanByFullName(objMeta);
		// IAttribute attr = bean.getAttributeByName(parentField.getId());
		// if(attr.getDataType().getTypeType() == IType.REF ||
		// attr.getDataType().getTypeType() == IType.ENTITY){
		// IRefType ref = (IRefType) attr.getDataType();
		// String refCode = ref.getRefType().getDefaultRefModelName();
		// if(refCode != null){
		// ILuiRefModel model = LuiRefUtil.getRefModel(refCode);
		// if(model != null){
		// AppLuiRefGenUtil gen = new AppLuiRefGenUtil(model, null);
		// String[] refEles = gen.getRefElements();
		//
		//
		// String writeFields = attr.getName() + "," + field.getId();
		// String readFields = refEles[0] + "," + refEles[2];
		//
		// refNode = new RefNodeGenerator().createRefNode((MdDataset)ds, false,
		// attr.getName(), null, refCode, readFields, writeFields, false, null,
		// null);
		// refNode.setId(refNode.getId() + "_" + field.getId());
		// refNode.setFrom(null);
		// }
		// }
		// }
		// } catch (MetaDataException e) {
		// LuiLogger.error(e.getMessage(), e);
		// }
		// }
		// }
		// }
		// return refNode;
		return null;
	}

	/**
	 * 显示和用户交互的对话框，并提供选择类型
	 * 
	 * @param editorType
	 * @return
	 */
	protected String showInputDialog(String editorType) {
		DataList comboData = new DataList();

		DataItem item1 = new DataItem(EditorTypeConst.STRINGTEXT, "单行文本");

		DataItem item2 = new DataItem(EditorTypeConst.DATETIMETEXT, "日期时间");
		DataItem item3 = new DataItem(EditorTypeConst.DECIMALTEXT, "小数");
		DataItem item4 = new DataItem(EditorTypeConst.REFERENCE, "参照");
		DataItem item5 = new DataItem(EditorTypeConst.DATETEXT, "日期");
		DataItem item6 = new DataItem(EditorTypeConst.INTEGERTEXT, "整型");
		DataItem item7 = new DataItem(EditorTypeConst.TEXTAREA, "文本域");
		DataItem item8 = new DataItem(EditorTypeConst.RICHEDITOR, "富文本");
		comboData.addDataItem(item1);
		comboData.addDataItem(item2);
		comboData.addDataItem(item3);
		comboData.addDataItem(item4);
		comboData.addDataItem(item5);
		comboData.addDataItem(item6);
		comboData.addDataItem(item7);
		comboData.addDataItem(item8);
		ComboInputItem editor = new ComboInputItem("editorType", "类型", true);
		editor.setValue(EditorTypeConst.STRINGTEXT);
		editor.setComboData(comboData);

		InteractionUtil.showInputDialog("确认", new InputItem[] { editor });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();

		if (rs != null && !rs.get("editorType").equals("null")) {
			return rs.get("editorType");
		}
		return editorType;
	}

	/**
	 * 获取网格布局
	 * 
	 * @param rp
	 * @return
	 */
	protected UIElement getGridElement(RaParameter rp) {
		UIElement element = UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
		if (element instanceof UIGridPanel) {
			return (UIGridPanel) element;
		} else {
			UIGridLayout grid = (UIGridLayout) element;
			UIGridPanel panel = (UIGridPanel) grid.getGridCell(Integer.valueOf(rp.getRowIndex()), Integer.valueOf(rp.getColIndex()));
			return panel;
		}
	}

	/**
	 * 删除元素
	 * 
	 * @param parent
	 * @param child
	 */
	public void removeUIElement(UIElement parent, UIElement child) {
		if (parent == null || child == null)
			return;
		if (parent instanceof UILayout) {
			((UILayout) parent).removePanel((UILayoutPanel) child);
		} else if (parent instanceof UIAbsoluteLayout){
			((UIAbsoluteLayout)parent).removeComponent((UIComponent)child);
		} else {
			((UILayoutPanel) parent).removeElement(child);
		}
	}

	/**
	 * 查找网格布局的容器
	 * 
	 * @param uiMeta
	 * @param uiEle
	 * @param ids
	 * @return
	 */
	protected UIElement findGridCell(UIPartMeta uiMeta, UIElement uiEle, String[] ids) {
		UIGridLayout grid = (UIGridLayout) UIElementFinder.findElementById(uiMeta, ids[0]);
		Iterator<UILayoutPanel> it = grid.getPanelList().iterator();
		while (it.hasNext()) {
			UIGridRowPanel panel = (UIGridRowPanel) it.next();
			UIGridRowLayout layout = panel.getRow();
			Iterator<UILayoutPanel> cit = layout.getPanelList().iterator();
			while (cit.hasNext()) {
				UIGridPanel gp = (UIGridPanel) cit.next();
				if (ids[1].equals(gp.getId())) {
					uiEle = gp;
					break;
				}
			}
			if (uiEle != null)
				break;
		}
		return uiEle;
	}
}
