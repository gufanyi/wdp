package xap.lui.core.render;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIDialog;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UIFormComp;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefConst;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefSelfUtil;
import xap.lui.core.refrence.SelfDefRefNode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
@SuppressWarnings("unchecked")
public class PCFormCompRender extends UIContainerComponentRender<UIFormComp, FormComp> {
	public static final String LABEL_POSITION = "label_position";
	public static final String IMG_TABLE_NAME = "tn";
	public static final String IMAGE_BEAN_ID = "beanid";
	public static final String IMAGE_ONSQL = "onsql";
	public static final String IMG_DATA_FIELD = "df";
	public static final String IMG_PK_FIELD = "pf";
	public static final String IMG_PK_VALUE = "pv";
	public static final String IMG_URL = "IMG_URL";
	public static final String IMG_HEIGHT = "IMG_HEIGHT";
	public static final String IMG_WIDTH = "IMG_WIDTH";
	public PCFormCompRender( FormComp webEle) {
		super(webEle);
	}
	// 元素默认行宽
	private static final int ELEMENT_WIDTH = 120;
	// 元素默认行高
	private static final int ELEMENT_HEIGHT = 20;
	public String placeSelf() {
		FormComp form = this.getWebElement();
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getDivId()).append(" = $('<div>').attr('id','").append(getDivId()).append("');\n");
		buf.append(super.placeSelf());
		buf.append(getDivId()).append(".css('overflow-y','auto');\n");
		// 子元素的渲染
		Dataset ds = getDataset();
		List<FormElement> list = form.getElementList();
		Iterator<FormElement> it = list.iterator();
		while (it.hasNext()) {
			FormElement ele = it.next();
			if (ele.isVisible() == false)
				continue;
			fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型
		}
		return buf.toString();
	}
	
	
	
	private String generateParam() {
		FormComp form = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("parentDiv", getDivId());
		param.put("id", getId());
		param.put("renderType", form.getRenderType());
		param.put("renderHiddenEle", false);
		param.put("rowHeight", form.getRowHeight());
		param.put("columnCount", form.getColumn());
		if (form != null) {
			param.put("eleWidth", form.getEleWidth());
			param.put("labelMinWidth", form.getLabelMinWidth());
			param.put("formRender", form.getFormRender());
			param.put("ellipsis", form.isEllipsis());
		}
		if (uiComp != null) {
			if (((UIFormComp)uiComp).getLabelPosition() != null) {
				param.put("labelPosition", ((UIFormComp)uiComp).getLabelPosition());
			}
		}
		return JSON.toJSONString(param);
	}
	
	public String createHead() {
		FormComp form = this.getWebElement();
		// boolean hasWebPart = false;
		UIComponent uiComp = this.getUiElement();
		StringBuilder buf = new StringBuilder();
		String formId = getVarId();
		String widget = WIDGET_PRE + this.getCurrWidget().getId();
		buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");
		buf.append("var ").append(formId).append(" = $('<div>').autoform(\n");
		buf.append(generateParam());
		buf.append("\n).autoform('instance');\n");
		
		Dataset ds = getDataset();
		List<FormElement> list = form.getElementList();
		Iterator<FormElement> it = list.iterator();
		// 当前form所属widget
		buf.append(formId + ".viewpart = " + widget + ";\n");
		// form自定义元素在产生form控件时自动添加到pageContext中,供jsp页面取用
		List<String> selDefEleIds = new ArrayList<String>(10);
		while (it.hasNext()) {
			FormElement element = it.next();
			genOneFormElement(null, null, form, uiComp, buf, widget, ds, element, selDefEleIds);
		}
		this.setContextAttribute("selDefEleIds", selDefEleIds);
		// getJspContext().setAttribute("selDefEleIds", selDefEleIds);
		buf.append(widget + ".addComponent('"+getId()+"'," + getVarId() + ");\n");
//		buf.append("if ($.browsersupport.IS_IE) try{ " + getVarId() + ".pLayout.paint(true);}catch(e){}\n");
		return buf.toString();
	}
	protected void genOneFormElement(String scriptWidgetId, String scirptFormId,  FormComp form, UIComponent uiComp, StringBuilder buf, String widget, Dataset ds, FormElement ele, List<String> selDefEleIds) {
		// if (ele.isVisible() == false) //
		// 渲染所有element,在AutoFormComp.prototype.createElement中用visible来判断是否需要隐藏.
		// continue;
		fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型
		if (ele.getEditorType() != null && ele.getEditorType().equals(EditorTypeConst.SELFDEF))
			selDefEleIds.add(ele.getId());
		String eleId = COMP_PRE + ele.getId() + "_ele";
		buf.append("var " + eleId + " = " + (scirptFormId == null ? getVarId() : scirptFormId)).append(".createElement(\"").append(ele.getId()).append("\",\"").append(ele.getField()).append("\",\"");
		UIFormElement uiEle = (UIFormElement)UIElementFinder.findElementById(LuiRenderContext.current().getUiPartMeta(), ele.getId());
		String eleWidth = uiEle==null?"":uiEle.getEleWidth();// "120";
		String eleHeight = uiEle==null?"":uiEle.getHeight();// "20";
		if (eleWidth == null || eleWidth.equals("") || eleWidth.equals("0")) {
			int formEleWidth = form.getEleWidth();
			if (formEleWidth <= 0)
				eleWidth = ELEMENT_WIDTH + "";
			else
				eleWidth = form.getEleWidth() + "";
		}
		if (eleHeight == null || eleHeight.equals("") || eleHeight.equals("0")) {
			int formEleHeight = form.getRowHeight();
			if (formEleHeight <= 0)
				eleHeight = ELEMENT_HEIGHT + "";
			else
				eleHeight = form.getRowHeight() + "";
		}
		buf.append(eleWidth).append("\",\"");
		buf.append(eleHeight).append("\",\"");
		buf.append(ele.getRowSpan()).append("\",\"");
		buf.append(ele.getColSpan()).append("\",\"");
		String editorType = ele.getEditorType();
		buf.append(editorType).append("\"");
		StringBuilder userObj = new StringBuilder();
		/*** 公共属性 ***/
		userObj.append("visible:").append(ele.isVisible());
		/*************/
		if (editorType.equals(EditorTypeConst.REFERENCE)) {
			userObj.append(",");
			// RefNode refNode = (RefNode)
			// getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
			IRefNode refNode = getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
			if (refNode == null) {
				userObj.append("refNode:null");
				ele.setEnabled(false);
			} else {
				// 取参照类型
				if (refNode instanceof GenericRefNode) {
					GenericRefNode ncRefNode = (GenericRefNode) refNode;
					IRefModel refModel = RefSelfUtil.getRefModel(ncRefNode);
					int refType = RefSelfUtil.getRefType(refModel);
					String reftype = "0";
					if (refType == IRefConst.GRID)
						reftype = "2";
					else if (refType == IRefConst.TREE)
						reftype = "1";
					else if (refType == IRefConst.GRIDTREE)
						reftype = "3";
					userObj.append("refType:").append(reftype).append(",");
					String refHeight = ncRefNode.getHeight();
					if (refHeight != null && !refHeight.equals(""))
						userObj.append("refHeight:").append(refHeight).append(",");
					String refWidth = ncRefNode.getWidth();
					if (refWidth != null && !refWidth.equals(""))
						userObj.append("refWidth:").append(refWidth).append(",");
				} else if (refNode instanceof SelfDefRefNode) {
					SelfDefRefNode selfRef = (SelfDefRefNode) refNode;
					String refHeight = selfRef.getHeight();
					if (refHeight != null && !refHeight.equals(""))
						userObj.append("refHeight:").append(refHeight).append(",");
					String refWidth = selfRef.getWidth();
					if (refWidth != null && !refWidth.equals(""))
						userObj.append("refWidth:").append(refWidth).append(",");
					userObj.append("isShowLine:").append(selfRef.isShowLine()).append(",");
				}
				String refId = RF_PRE + getCurrWidget().getId() + "_" + refNode.getId();
				userObj.append("refNode:").append(refId);
			}
		} else if (editorType.equals(EditorTypeConst.STRINGTEXT)) {
			String maxLength = ele.getMaxLength();
			if (maxLength == null || maxLength.trim().equals(""))
				maxLength = getFieldProperty(ele.getField(), Field.MAX_LENGTH);
			if (maxLength == null || maxLength.trim().equals(""))
				maxLength = null;
			userObj.append(",").append("maxLength:").append(maxLength);
		} else if (editorType.equals(EditorTypeConst.FILECOMP)) {
			userObj.append(",").append("sizeLimit:").append(ele.getSizeLimit());
			if (ele.getSysid() != null) {
				userObj.append(",").append("sysid:").append("'").append(ele.getSysid()).append("'");
			} else {
				userObj.append(",").append("sysid:null");
			}
			if (ele.getBilltype() != null) {
				userObj.append(",").append("billtype:").append("'").append(ele.getBilltype()).append("'");
			} else {
				userObj.append(",").append("billtype:null");
			}
		} else if (editorType.equals(EditorTypeConst.IMAGECOMP)) {
			userObj.append(",").append("url:").append("'").append(getFieldProperty(ele.getField(), IMG_URL)).append("'");
			userObj.append(",").append("width:").append("'").append(getFieldProperty(ele.getField(), IMG_WIDTH)).append("'");
			userObj.append(",").append("height:").append("'").append(getFieldProperty(ele.getField(), IMG_HEIGHT)).append("'");
			userObj.append(",").append("pkfield:").append("'").append(getFieldProperty(ele.getField(), IMG_PK_FIELD)).append("'");
		} else if (editorType.equals(EditorTypeConst.INTEGERTEXT)) {
			String maxValue = ele.getMaxValue();
			String minValue = ele.getMinValue();
			if (maxValue == null || "".equals(maxValue))
				maxValue = getFieldProperty(ele.getField(), Field.MAX_VALUE);
			if (minValue == null || "".equals(minValue))
				minValue = getFieldProperty(ele.getField(), Field.MIN_VALUE);
			userObj.append(",").append("maxValue:").append(maxValue);
			userObj.append(",").append("minValue:").append(minValue);
		} else if (editorType.equals(EditorTypeConst.DECIMALTEXT)) {
			String precision = ele.getPrecision();
			if (precision == null || precision.trim().equals(""))
				precision = getFieldProperty(ele.getField(), Field.PRECISION);
			if (precision == null || precision.trim().equals(""))
				precision = null;
			if (precision != null) {
				userObj.append(",").append("precision:").append("'").append(precision).append("'");
			} else {
				userObj.append(",").append("precision:null");
			}
		} else if (editorType.equals(EditorTypeConst.COMBODATA) || editorType.equals(EditorTypeConst.RADIOGROUP) || editorType.equals(EditorTypeConst.CHECKBOXGROUP) || editorType.equals(EditorTypeConst.LIST)) {
			userObj.append(",").append("comboData:").append(ele.getRefComboData() == null ? "null" : (COMBO_PRE + getCurrWidget().getId() + "_" + ele.getRefComboData()));
			userObj.append(",").append("imageOnly:").append(ele.isImageOnly());
			userObj.append(",").append("selectOnly:").append(ele.isOnlySelect());
			userObj.append(",").append("dataDivHeight:").append(ele.getDataDivHeight() == null ? null : ele.getDataDivHeight());
		} else if (editorType.equals(EditorTypeConst.LANGUAGECOMBODATA)) {
			// 根据字段名称过滤出相关字段，来构造初始化下拉的数据结构，fieldId为控件绑定字段的id，根据此id去查找所有的id
			String fieldId = ele.getField();
			List<String> idList = new ArrayList<String>();
			List<String> textList = new ArrayList<String>();
			// 找到字段中已经设置的值
			for (Field field : ds.getFieldList()) {
				String fid = field.getId();
				// 如果dataset中的字段的id与element中绑定的字段的名称匹配，则加入到list
				if (fid != null && fid.length() >= fieldId.length() && fid.startsWith(fieldId)) {
					// String tipText = matchLanguageTip(fieldId,fid);
//					String tipText = matchLanguageTipWithVos(fieldId, fid);
//					if (!StringUtils.isBlank(tipText)) {
//						idList.add(fid);
//						textList.add(tipText);
//					}
				}
			}
			JSONArray jArry = new JSONArray();
			int currentLangIndex = 0;
			for (int i = 0; i < idList.size(); i++) {
				JSONObject jObj = new JSONObject();
				jObj.put("field", idList.get(i));
				jObj.put("caption", "");
				jObj.put("name", idList.get(i));
				// 应该是valuesList中的值
				jObj.put("value", "");
				jObj.put("langTip", textList.get(i));
				jArry.add(jObj);
			}
			String jsArray = jArry.toString();
			// 根据构造的list生成此控件对应的userObj
			userObj.append(",").append("langDataArray:").append(jsArray).append(",").append("imageOnly:").append(ele.isImageOnly()).append(",").append("selectOnly:").append(ele.isOnlySelect()).append(",").append("dataDivHeight:").append(ele.getDataDivHeight() == null ? null : ele.getDataDivHeight()).append(",").append("currentLangCode:").append(currentLangIndex);
		} else if (editorType.equals(EditorTypeConst.CHECKBOX)) {
			userObj.append(",").append("valuePair:");
			// 设置comboData的情况
			DataList data = (DataList) getCurrWidget().getViewModels().getComboData(ele.getRefComboData());
			if (data != null) {
				DataItem[] items = data.getAllDataItems();
				if (items == null || items.length != 2)
					throw new LuiRuntimeException("The Combodata is not suitable for ele:" + ele.getId());
				userObj.append("[\"").append(items[0].getValue()).append("\",\"").append(items[1].getValue()).append("\"]");
			} else {
				// 没有设置comboData的情况
				String dataType = ele.getDataType();
				if (StringDataTypeConst.BOOLEAN.equals(dataType) || StringDataTypeConst.bOOLEAN.equals(dataType))
					userObj.append("[\"true\",\"false\"]");
				else if (StringDataTypeConst.FBOOLEAN.equals(dataType))
					userObj.append("['Y','N']");
			}
		} else if (editorType.equals(EditorTypeConst.RICHEDITOR)) {
			String toolbarType = ele.getToolbarType();
			if (toolbarType != null) {
				userObj.append(",").append("toolbarType:").append("'").append(toolbarType).append("'");
			}
		}
		buf.append(",").append("{").append(userObj.toString()).append("}");
		buf.append(",").append(!ele.isEnabled()).append(",").append(!ele.isEdit()).append(",");
		// 参照元素需要提前将Dataset传入
		if (editorType.equals(EditorTypeConst.REFERENCE))
			buf.append("\"" + this.getDataset().getId() + "\",");
		else
			buf.append("null,");
		String resId = ele.getI18nName();
		String simpchn = ele.getText() == null ? resId : ele.getText();
		String label = getFieldI18nName(resId, ele.getField(), simpchn, ele.getLangDir());
		label = label == null ? "" : label;
		buf.append("\"").append(label).append("\",");
		// labelColor
		if (ele.getLabelColor() != null)
			buf.append("'").append(ele.getLabelColor()).append("',");
		else
			buf.append("null,");
		//labelPos
		if(ele.getLabelPos() !=null) {
			buf.append("'").append(ele.getLabelPos()).append("',");
		} else {
			buf.append("null,");
		}
		if (ele.isNextLine())
			buf.append("true,");
		else
			buf.append("false,");
		buf.append(ele.isRequired()).append(",");
		if (ele.getTip() != null && !"".equals(ele.getTip())) {
			String tip = getFieldI18nName(ele.getTipI18nName(), ele.getField(), ele.getTip(), ele.getLangDir());
			buf.append("'").append(tip).append("',");
		} else {
			buf.append("null,");
		}
		if (ele.getInputAssistant() != null && !"".equals(ele.getInputAssistant())) {
			buf.append("'").append(ele.getInputAssistant()).append("',");
		} else {
			buf.append("null,");
		}
		if (ele.getShowTip() != null && !"".equals(ele.getShowTip())) {
			buf.append("'").append(ele.getShowTip()).append("',");
		} else
			buf.append("null,");
		if (ele.getDescription() != null && !"".equals(ele.getDescription())) {
			buf.append("'").append(ele.getDescription()).append("',");
		} else {
			buf.append("null,");
		}
		buf.append("'").append(ele.isAttachNext()).append("',");
		String className = uiComp.getClassName();
		if (className != null && !className.equals("")) {
			buf.append("'").append(className).append("',");
		} else
			buf.append("null,");
		boolean hasValidateEvent = hasFormula(ele,"validate_method");
		boolean hasEditorEvent = hasFormula(ele, "editor_method");
		if(hasValidateEvent || hasEditorEvent) {
			buf.append("true");
		} else {
			buf.append("null");
		}
		buf.append("); \n");
		buf.append(eleId + ".viewpart=" + (scriptWidgetId == null ? widget : scriptWidgetId) + ";\n");
		// 自定义控件
		if (editorType.equals(EditorTypeConst.SELFDEF)) {
			String bindId = ele.getBindId();
			if (bindId == null || bindId.equals("")) {
				String ext = (String) ele.getExtendAttributeValue(FormElement.SELF_DEF_FUNC);
				if (ext != null) {
					buf.append(eleId).append(".setContent(").append(ext).append("());\n");
				}
			} else {
				// 渲染控件 renxh此处需要研究处理
				WebComp comp = getCurrWidget().getViewComponents().getComponent(bindId);
				comp.setId(bindId);
				ILuiRender compRender =comp.getRender();
				if (compRender instanceof UIComponentRender) {
					try {
						if (!(compRender instanceof PCSelfDefCompRender))
							buf.append(eleId).append(".Div_gen.id='").append(compRender.getDivId()).append("';\n");
							buf.append(compRender.create());
						if (compRender instanceof PCSelfDefCompRender)
							buf.append(eleId).append(".setContent(pageUI.getViewPart('").append(this.viewId).append("').getComponent('").append(((UIComponentRender<UIComponent, WebComp>) compRender).getId()).append("'));\n");
					} catch (Exception e) {
						throw new LuiRuntimeException(e.getMessage());
					}
				} else
					buf.append(eleId).append(".setContent(pageUI.getViewPart('").append(this.viewId).append("').getComponent('").append(((UIComponentRender<UIComponent, WebComp>) compRender).getId()).append("'));\n");
			}
		}
		buf.append(this.addEventSupport(ele, this.viewId, null, form.getId()));
	}
	
	private boolean hasFormula(WebComp comp, String method) {
		List<LuiEventConf> eventList = comp.getEventConfList();
		if(CollectionUtils.isNotEmpty(eventList)) {
			for(LuiEventConf event : eventList) {
				if(StringUtils.equals(event.getMethod(), method)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String createTail() {
		return generalTailScript(true);
	}
	private String generalTailScript(boolean isDynamic) {
		FormComp form = this.getWebElement();
		StringBuilder buf1 = new StringBuilder();
		// form tag结束时移除"selDefEleIds"属性
		this.removeContextAttribute("selDefEleIds");
		// getJspContext().removeAttribute("selDefEleIds");
		if (form.getRenderType() == 1 || form.getRenderType() == 3) {
			buf1.append(generateTailScriptForType1(form));
		} else
			buf1.append(generateTailScriptForType2(form));
		StringBuilder buf = new StringBuilder();
		// buf.append("\n" +COMP_PRE)
		// .append(getId())
		buf.append("\npageUI.getViewPart('" + this.getCurrWidget().getId() + "').getComponent('" + getId() + "').setDataset(");
		buf.append("pageUI.getViewPart(\"" + getCurrWidget().getId() + "\").getDataset" + "(\"").append(form.getDataset());
		buf.append("\"));\n");
		if (!form.isEnabled()) {
			buf.append("\npageUI.getViewPart('" + this.getCurrWidget().getId() + "').getComponent('" + getId() + "').setEditable(false);\n");
			// buf.append(getVarId()).append(".setEditable(false);\n");
		}
		String script = buf.toString();
		ViewPartMeta widget = getCurrWidget();
		UIPartMeta uiPartMeta=LuiRenderContext.current().getUiPartMeta();
		UIDialog dialog = (UIDialog) uiPartMeta.getDialog(widget.getId());
		// if (isDynamic)
		if (isDynamic || dialog != null || widget.isDialog())
			buf1.append(script);
		else {
			// StringBuilder dsScript = (StringBuilder)
			// getJspContext().getAttribute(PageModelTag.DS_SCRIPT); 被下面的代码替换
			StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
			if (dsScript == null) {
				dsScript = new StringBuilder();
				this.setContextAttribute(DS_SCRIPT, dsScript);
			}
			if (script != null)
				dsScript.append(script);
		}
		return buf1.toString();
	}
	public String generalTailScriptDynamic() {
		//String oper = AppLifeCycleContext.current().getParameter("oper");
		// if("add".equals(oper)){
		// StringBuilder url = new
		// StringBuilder(LuiRuntimeEnvironment.getRootPath());
		// ///portal/app/mockapp/fieldmgr?sourceEleId=formcomp4773&sourceWinId=fc&sourceViewId=main&
		// url.append("/app/mockapp/fieldmgr?sourceType=formcomp");
		// url.append("&sourceEleId=").append(this.getId());
		// url.append("&sourceViewId=").append(this.getViewPart());
		// url.append("&sourceWinId=").append(LuiRuntimeEnvironment.getWebContext().getPageId());
		// AppLifeCycleContext.current().getApplicationContext().popOuterWindow(url.toString(),
		// "Editor", "750", "450", ApplicationContext.TYPE_DIALOG);
		//			
		// }
		return generalTailScript(true);// 没有变动，所以调用原始的方法 renxh
	}
	private String generateTailScriptForType2(FormComp form) {
		return "";
	}
	private String generateTailScriptForType1(FormComp form) {
		return "";
	}
	/**
	 * 根据dataset中的数据类型填充FormElement的dataType，并且获得对应的EditorType
	 * 
	 * @param ds
	 * @param ele
	 * @return
	 */
	protected void fillDataTypeAndEditorType(Dataset ds, FormElement ele) {
		if (ele.getField() != null) {
			String eleDt = ele.getDataType();
			if (eleDt == null || eleDt.equals("")) {
				Field f = ds.getField(ele.getField());
				if (f == null) {
					ele.setDataType(StringDataTypeConst.STRING);
				} else
					ele.setDataType(ds.getField(ele.getField()).getDataType());
			}
			String editorType = ele.getEditorType();
			if (editorType == null || editorType.trim().equals(""))
				ele.setEditorType(EditorTypeConst.getEditorTypeByString(ele.getDataType()));
		}
	}
	/**
	 * 2011-8-2 下午08:18:06 renxh des：根据字段的id和属性名称获得属性值
	 * 
	 * @param fieldId
	 * @param name
	 * @return
	 */
	protected String getFieldProperty(String fieldId, String name) {
		Dataset ds = getDataset();
		Field field = ds.getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
	}
	protected String getSourceType(IEventSupport ele) {
		if(ele instanceof FormElement) {
			return LuiPageContext.SOURCE_FORMELEMENT;
		}
		return LuiPageContext.SOURCE_TYPE_FORMCOMP;
	}
	@Override
	protected Dataset getDataset() {
		return super.getDataset();
	}
	// 删除form中的元素
	public void removeElement(Object obj) {
		if (obj instanceof FormElement) { // 表格列属性
			FormElement element = (FormElement) obj;
			FormComp form = element.getParent();
			StringBuilder buf = new StringBuilder();
			String widget = form.getWidget() != null ? form.getWidget().getId() : this.getViewId();
			buf.append("window.execDynamicScript2RemoveFormElement('" + widget + "','" + form.getId() + "','" + element.getField() + "');");
			form.removeElement(element);
			addDynamicScript(buf.toString());
		}
	}
	@Override
	protected String getFieldI18nName(String name, String fieldId, String defaultI18nName, String langDir) {
		return super.getFieldI18nName(name, fieldId, defaultI18nName, langDir);
	}
	@Override
	protected String setWidgetToComponent() {
		return "pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + this.getId() + "').viewpart = " + WIDGET_PRE + this.getViewId() + "\n";
	}
	private String generateAttrArr(FormComp form, UIComponent uiComp) {
		StringBuilder buf = new StringBuilder();
		buf.append("{");
		if (form != null) {
			buf.append("'eleWidth':'").append(form.getEleWidth()).append("',");
			buf.append("'labelMinWidth':'").append(form.getLabelMinWidth()).append("',");
			buf.append("'formRender':").append(form.getFormRender()).append(",");
			buf.append("'ellipsis':").append(form.isEllipsis());
		}
		if (uiComp != null) {
			if (((UIFormComp)uiComp).getLabelPosition() != null) {
				if (buf.length() > 1)
					buf.append(",");
				buf.append("'labelPosition':'").append(((UIFormComp)uiComp).getLabelPosition()).append("'");
			}
		}
		buf.append("}");
		return buf.toString();
	}
	
	/**
	 * ******************************************************************************************************************
	 */
	public void removeElement(String elementId) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".removeElementById('" + elementId + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setMatchValues(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMatchValues('").append(ele.getMatchValues() + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setBeforeOpenParam(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').beforeOpenParam('").append(ele.getBeforeOpenParam() + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setValue(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setValue('").append(ele.getValue() + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setShowValue(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setShowValue('").append(ele.getShowValue() + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setLabel(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleLabel('").append(ele.getId()).append("','" + StringUtils.defaultIfEmpty(ele.getLabel(), "")).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setLabelPos(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".changeEleLabelPos('").append(ele.getId()).append("','" + StringUtils.defaultIfEmpty(ele.getLabelPos(), "")).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setLabelColor(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleLabelColor('").append(ele.getId()).append("','" + ele.getLabelColor()).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setMaxLength(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + " && currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMaxSize) {\n");
		buf.append("  currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMaxSize('").append(ele.getMaxValue() + "');\n");
		buf.append("}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setMaxValue(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + " && currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMaxValue) {\n");
		buf.append("  currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMaxValue('").append(ele.getMaxValue() + "');\n");
		buf.append("}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setMinValue(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + " && currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMinValue) {\n");
		buf.append("  currForm_" + this.getId() + ".getElement('").append(ele.getId()).append("').setMinValue('").append(ele.getMinValue() + "');\n");
		buf.append("}\n");
		addDynamicScript(buf.toString());
	}
	
	public void setEnable(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleEnabled('").append(ele.getId()).append("'," + ele.isEnabled()).append(");\n");
		addDynamicScript(buf.toString());
	}
	
	public void setVisible(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleVisible('").append(ele.getId()).append("'," + ele.isVisible()).append(");\n");
		addDynamicScript(buf.toString());
	}
	
	public void setFocus(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleFocus('").append(ele.getId()).append("'," + ele.isFocus()).append(");\n");
		addDynamicScript(buf.toString());
	}
	
	public void setPrecision(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setElePrecision('").append(ele.getId()).append("','" + ele.getPrecision()).append("');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setIsEdit(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		buf.append("if (currForm_" + this.getId() + ") currForm_" + this.getId() + ".setEleEditable('").append(ele.getId()).append("'," + ele.isEdit()).append(");\n");
		addDynamicScript(buf.toString());
	}
	
	public void hideError() {
		// 隐藏整体错误提示框
		addDynamicScript("$.autoFormComp.hideErrorMsg('" + this.viewId + "', '" + this.getId() + "');\n");
	}
	
	public void addFormElement(FormElement ele) {
		StringBuilder buf = new StringBuilder();
		buf.append("var currWidget_" + this.getViewId() + " = pageUI.getViewPart('" + this.viewId + "');\n");
		buf.append("var currForm_" + this.getId() + " = pageUI.getViewPart('" + this.viewId + "').getComponent('" + this.getId() + "');\n");
		this.genOneFormElement("currWidget_" + this.getViewId(), "currForm_" + this.getId(),  (FormComp) this.getWebElement(), (UIComponent) this.getUiElement(), buf, this.getViewId(), this.getDataset(), ele, null);
		buf.append("try{ currForm_" + this.getId() + ".pLayout.paint(true);}catch(e){}\n");
		addDynamicScript(buf.toString());
	}
	@Override
	public void removeChild(UIElement obj) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addChild(UIElement obj) {
		// TODO Auto-generated method stub
		
	}



	public void setDataset(String datasetid) {
		StringBuilder buf = new StringBuilder();
		buf.append("\npageUI.getViewPart('" + this.getCurrWidget().getId() + "').getComponent('" + getId() + "').setDataset(");
		buf.append("pageUI.getViewPart(\"" + getCurrWidget().getId() + "\").getDataset" + "(\"").append(datasetid);
		buf.append("\"));\n");
		addDynamicScript(buf.toString());
	}



}
