package xap.lui.core.render;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.ExtAttriSupport;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.IDataBinding;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIFormElement;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.IRefNode;

@SuppressWarnings("unchecked")
public class PCFormElementRender extends UINormalComponentRender<UIFormElement, FormElement> {

	// 元素默认行宽
	private static final int ELEMENT_WIDTH = 120;
	// 元素默认行高
	private static final int ELEMENT_HEIGHT = 20;

	private String formId = null;
	private String varFormId = null;
	private FormComp form = null;
	private PCFormCompRender formRender = null;

	public PCFormElementRender(FormElement webEle) {
		super(webEle);
		UIFormElement uiEle = this.getUiElement();
		this.formId = uiEle.getFormId();
		if (this.formId == null || this.formId.equals("")) {
			throw new LuiRuntimeException("formId 不能为 null");
		}

		this.form = webEle.getParent();
		if (this.form == null) {
			PagePartMeta pageMeta = LuiRenderContext.current().getPagePartMeta();
			if (pageMeta != null && this.getViewId() != null) {
				ViewPartMeta oWidget = pageMeta.getWidget(this.getViewId());
				if (oWidget != null) {
					this.form = (FormComp) oWidget.getViewComponents().getComponent(formId);
				}
			}
		}
		if (this.form != null) {
			if (!this.form.isRendered()) {
				this.form.setRendered(true);
				formRender = new PCFormCompRender(this.form);
				this.form.setExtendAttribute(ExtAttriSupport.DYN_ATTRIBUTE_KEY + "_formId", formRender.getVarId());
			}
			this.viewId = this.form.getWidget().getId();
		} else {
			throw new LuiRuntimeException("form 不能为 null");
		}
		varFormId = (String) this.form.getExtendAttributeValue(ExtAttriSupport.DYN_ATTRIBUTE_KEY + "_formId");
	}

	@Override
	protected String createDivId(String widget, UIElement ele) {
		if (ele == null)
			return DIV_PRE + this.id + "_ele";
		else
			return DIV_PRE + ele.getAttribute("form_id") + "_" + this.id + "_ele";
	}

	@Override
	protected String createVarId(String widget, UIElement ele) {
		if (ele == null)
			return COMP_PRE + this.id + "_ele";
		else
			return COMP_PRE + ele.getAttribute("form_id") + "_" + this.id + "_ele";
	}


	@Override
	public String place() {
		// StringBuilder buf = new StringBuilder();
		// buf.append("var ").append(getDivId()).append(" = $ce('DIV');");
		// buf.append(getDivId()).append(".style.width = '100%';");
		// buf.append(getDivId()).append(".style.height = '100%';");
		// ;
		// buf.append(getDivId()).append(".style.top = '0px';");
		// buf.append(getDivId()).append(".style.left = '0px';");
		// buf.append(getDivId()).append(".style.position = 'relative';");
		// buf.append(getDivId()).append(".style.overflow = 'hidden';");
		// buf.append(getDivId()).append(".id = '").append(getDivId()).append("';");
		// return buf.toString();
		return super.place();
	}


	@Override
	public String createBody() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(varFormId).append(" = pageUI.getViewPart('").append(this.getViewId()).append("').getComponent('").append(formId).append("');\n");
		buf.append("if(!").append(varFormId).append("){");
		buf.append(this.createForm(false));
		buf.append("}else{" + varFormId + ".renderType=4;\n};\n");
		buf.append(this.createFormElement(false));
		this.createFormElemntDs();
		return buf.toString();
	}

	// @Override
	// public String generateBodyScriptDynamic() {
	// StringBuilder buf = new StringBuilder();
	// buf.append("var ").append(varFormId).append(" = pageUI.getViewPart('").append(widget).append("').getComponent('").append(formId).append("');\n");
	// buf.append("if(!").append(varFormId).append("){");
	// buf.append(this.createForm(true));
	// buf.append("};\n");
	// buf.append(this.createFormElement(true));
	// this.createFormElemntDs();
	// return buf.toString();
	// }

	private void fillDataTypeAndEditorType(Dataset ds, FormElement ele) {
		if (ele.getField() != null) {
			if (ele.getDataType() == null || ele.getDataType().trim().equals(""))
				ele.setDataType(ds.getField(ele.getField()).getDataType());
			if (ele.getEditorType() == null || ele.getEditorType().trim().equals(""))
				ele.setEditorType(EditorTypeConst.getEditorTypeByString(ele.getDataType()));
		}
		// formRender.fillDataTypeAndEditorType(ds, ele);
	}

	private String getFieldProperty(String fieldId, String name) {
		Dataset ds = getDataset();
		Field field = ds.getField(fieldId);
		if (field == null)
			return null;
		return (String) field.getExtendAttributeValue(name);
		// return formRender.getFieldProperty(fieldId, name);
	}

	private String createFormElemntDs() {
//		StringBuffer buf = new StringBuffer();
//		buf.append("var ").append(varFormId).append(" = pageUI.getViewPart('").append(this.getViewId()).append("').getComponent('").append(form.getId()).append("');\n");
//		buf.append("if (").append(varFormId).append(" != null && ").append(varFormId).append(".dataset == null){\n");
//		
//		buf.append(varFormId).append(".setDataset(");
//		buf.append("pageUI.getViewPart(\"" + this.getViewId() + "\").getDataset" + "(\"").append(form.getDataset());
//		buf.append("\"));\n");
//		
//		buf.append("}\n");
		PcFormRenderUtil.setFromDsScript(form.getId(), varFormId, this.getViewId(), form.getDataset());
		return null;
		//return buf.toString();
	}

	/**
	 * 创建formElement元素
	 * 
	 * @param isDynamic
	 * @return
	 */
	private String createFormElement(boolean isDynamic) {
		FormElement ele = this.getWebElement();
		UIFormElement uiEle = this.getUiElement();
		StringBuilder buf = new StringBuilder();
		ViewPartMeta currWidget = ele.getParent().getWidget();
		String widget = WIDGET_PRE + currWidget.getId();

		Dataset ds = currWidget.getViewModels().getDataset(this.form.getDataset());
		fillDataTypeAndEditorType(ds, ele);// 为 ele指定类型
		String eleId = varId;
		buf.append("var ").append(widget).append(" = pageUI.getViewPart('").append(currWidget.getId()).append("');\n");
		buf.append("var " + eleId + " = " + varFormId).append(".createElement(\"").append(ele.getId()).append("\",\"").append(ele.getField()).append("\",\"");

		// String eleWidth = uiEle.getEleWidth();
		// buf.append(eleWidth).append("\",\"");
		// buf.append(uiEle.getHeight()).append("\",\"");

		// String eleWidth = ele.getWidth();//"120";
		String eleWidth = uiEle.getEleWidth();

		String eleHeight = uiEle.getHeight();// "20";
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
		buf.append(ele.getEditorType()).append("\",");
		if (ele.getEditorType().equals(EditorTypeConst.REFERENCE)) {
			referenceObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.STRINGTEXT)) {
			stringTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.IMAGECOMP)) {
			imageCompObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.FILECOMP)) {
			fileCompObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.INTEGERTEXT)) {
			integerTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.DECIMALTEXT)) {
			decimalTextObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.COMBODATA) || ele.getEditorType().equals(EditorTypeConst.RADIOGROUP) || ele.getEditorType().equals(EditorTypeConst.CHECKBOXGROUP)) {
			combOrGroupObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.LANGUAGECOMBODATA)) {
			//languageComboBoxObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.CHECKBOX)) {
			checkboxObj(ele, buf);
		} else if (ele.getEditorType().equals(EditorTypeConst.RICHEDITOR)) {
			if (this.isEditMode()) {
				String imagePath = "/wfw/frame/themes/webclassic/images/editor/richEditor.gif";
				imageCompObj(ele, buf, imagePath, imagePath);
			} else {
				richEditorObj(ele, buf);
			}

		} else {
			buf.append("{visible:").append(ele.isVisible()).append("}");
		}
		buf.append(",").append(!ele.isEnabled()).append(",").append(!ele.isEdit()).append(",");

		// 参照元素需要提前将Dataset传入
		if (ele.getEditorType().equals(EditorTypeConst.REFERENCE))
			buf.append("\"" + this.getDataset().getId() + "\",");
		else
			buf.append("null,");

		String resId = ele.getI18nName();
		String simpchn = ele.getText() == null ? resId : ele.getText();
		String label = super.getFieldI18nName(resId, ele.getField(), simpchn, ele.getLangDir());
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
			buf.append("'").append(ele.getTip()).append("',");
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

		String className = uiEle.getClassName();
		if (className != null && !className.equals("")) {
			buf.append("'").append(className).append("'\n");
		} else {
			buf.append("null");
		}
		
		boolean hasValidateEvent = hasFormula(ele,"validate_method");
		boolean hasEditorEvent = hasFormula(ele, "editor_method");
		if(hasValidateEvent || hasEditorEvent) {
			buf.append(",true");
		} else {
			buf.append(",null");
		}

		if (isDynamic) {
			buf.append("," + divId + " \n");
		} else {
			buf.append(",$('#" + divId + "') \n");
		}

		buf.append(",\"" + uiEle.getWidth() + "\");\n");

		buf.append(eleId + ".viewpart=" + widget + ";\n");

		return buf.toString();
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

	/**
	 * 根据字段结尾序号和系统允许的多语VO查询字段对应的多语提示
	 * 
	 * @param fieldId
	 *            控件绑定的字段id
	 * @param fid
	 *            循环的字段id
	 * @return 字段结尾序号对应的语言简称
	 */
//	private String matchLanguageTipWithVos(String fieldId, String fid) {
//		String returnLangTip = "";
//		LanguageVO[] enabledLanguages = MultiLangContext.getInstance().getEnableLangVOs();
//		int index = 0;
//		if (fieldId.equals(fid)) {
//			index = 1;
//		} else {
//			String endIndex = fid.substring(fieldId.length());
//			// 忽略类型转化异常
//			try {
//				index = Integer.valueOf(endIndex);
//			} catch (Exception e) {
//				// 如果结尾中包含非数字字符
//				return "";
//			}
//		}
//		for (LanguageVO langVo : enabledLanguages) {
//			int sqe = langVo.getLangseq();
//			if (sqe == index) {
//				returnLangTip = langVo.getLocallang();
//			}
//		}
//		return returnLangTip;
//	}

	private void referenceObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{");
		IRefNode refNode = getCurrWidget().getViewModels().getRefNode(ele.getRefNode());
		if (refNode == null) {
			LuiLogger.error("Form Element 类型为参照，但是没设置参照节点,Element id:" + ele.getId());
			userObj.append("refNode:null");
			ele.setEnabled(false);
		} else {
			String refId = RF_PRE + getCurrWidget().getId() + "_" + refNode.getId();
			userObj.append("refNode:").append(refId);
		}
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void stringTextObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{maxLength:");
		String maxLength = ele.getMaxLength();
		if (maxLength == null || maxLength.trim().equals(""))
			maxLength = getFieldProperty(ele.getField(), Field.MAX_LENGTH);
		if (maxLength == null || maxLength.trim().equals(""))
			maxLength = null;
		userObj.append(maxLength).append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void imageCompObj(FormElement ele, StringBuilder buf, String refImage1, String refImage2) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{url:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_URL));
		userObj.append("', width:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_WIDTH));
		userObj.append("',height:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_HEIGHT));
		userObj.append("',pkfield:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_PK_FIELD));
		if (refImage1 != null && !refImage1.equals("")) {
			userObj.append("',refImage1:'").append(refImage1);
		}
		if (refImage2 != null && !refImage2.equals("")) {
			userObj.append("',refImage2:'").append(refImage2);
		}
		userObj.append("'");
		userObj.append(",visible:").append(ele.isVisible());
		userObj.append("}");
		buf.append(userObj.toString());
	}

	private void imageCompObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{url:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_URL)).append("', width:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_WIDTH))
				.append("',height:'").append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_HEIGHT)).append("',pkfield:'")
				.append(getFieldProperty(ele.getField(), PCFormCompRender.IMG_PK_FIELD)).append("'");
		userObj.append(",visible:").append(ele.isVisible());
		userObj.append("}");
		buf.append(userObj.toString());
	}

	private void fileCompObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		// userObj.append("{visible:").append(ele.isVisible()).append(",sizeLimit:").append(ele.getSizeLimit()
		// + "}");
		userObj.append("{visible:").append(ele.isVisible()).append(",sizeLimit:").append(ele.getSizeLimit()).append(",sysid:").append(ele.getSysid()).append(",billtype:")
				.append(ele.getBilltype() + "}");

		buf.append(userObj.toString());
	}

	private void integerTextObj(FormElement ele, StringBuilder buf) {
		String maxValue = ele.getMaxValue();
		String minValue = ele.getMinValue();
		StringBuilder userObj = new StringBuilder();
		userObj.append("{");
		if (maxValue == null || "".equals(maxValue))
			maxValue = getFieldProperty(ele.getField(), Field.MAX_VALUE);
		if (minValue == null || "".equals(minValue))
			minValue = getFieldProperty(ele.getField(), Field.MIN_VALUE);
		// if(maxValue != null && !maxValue.trim().equals(""))
		userObj.append("maxValue:").append(maxValue);
		// if(minValue != null && !minValue.trim().equals("")){
		// if(maxValue != null && !maxValue.trim().equals(""))
		userObj.append(",");
		userObj.append("minValue:").append(minValue);
		// }
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void decimalTextObj(FormElement ele, StringBuilder buf) {
		String precision = ele.getPrecision();
		if (precision == null || precision.trim().equals(""))
			precision = getFieldProperty(ele.getField(), Field.PRECISION);
		if (precision == null || precision.trim().equals(""))
			precision = null;

		StringBuilder userObj = new StringBuilder();
		userObj.append("{precision:'").append(precision).append("',visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void combOrGroupObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{comboData:").append(ele.getRefComboData() == null ? "null" : (COMBO_PRE + getCurrWidget().getId() + "_" + ele.getRefComboData())).append(",imageOnly:")
				.append(ele.isImageOnly()).append(",selectOnly:").append(ele.isOnlySelect()).append(",dataDivHeight:").append(ele.getDataDivHeight() == null ? null : ele.getDataDivHeight())
				.append(",visible:").append(ele.isVisible()).append("}");

		buf.append(userObj.toString());
	}

//	private void languageComboBoxObj(FormElement ele, StringBuilder buf) {
//		StringBuilder userObj = new StringBuilder();
//		userObj.append("visible:").append(ele.isVisible());
//		// 根据字段名称过滤出相关字段，来构造初始化下拉的数据结构，fieldId为控件绑定字段的id，根据此id去查找所有的id
//		String fieldId = ele.getField();
//		List<String> idList = new ArrayList<String>();
//		List<String> textList = new ArrayList<String>();
//		// 找到字段中已经设置的值
//		FieldSet fs = this.getDataset();
//		for (Field field : fs.getFieldList()) {
//			String fid = field.getId();
//			// 如果dataset中的字段的id与element中绑定的字段的名称匹配，则加入到list
//			if (fid != null && fid.length() >= fieldId.length() && fid.startsWith(fieldId)) {
//				// String tipText = matchLanguageTip(fieldId,fid);
//				String tipText = matchLanguageTipWithVos(fieldId, fid);
//				if (!StringUtils.isBlank(tipText)) {
//					idList.add(fid);
//					textList.add(tipText);
//				}
//			}
//		}
//		JSONArray jArry = new JSONArray();
//		String currentLanguage = MultiLangContext.getInstance().getCurrentLangVO().getLocallang();
//		int currentLangIndex = 0;
//		for (int i = 0; i < idList.size(); i++) {
//			JSONObject jObj = new JSONObject();
//			jObj.put("field", idList.get(i));
//			jObj.put("caption", "");
//			jObj.put("name", idList.get(i));
//			// 应该是valuesList中的值
//			jObj.put("value", "");
//			jObj.put("langTip", textList.get(i));
//			if (currentLanguage.equals(textList.get(i))) {
//				currentLangIndex = i;
//			}
//			jArry.put(jObj);
//		}
//		String jsArray = jArry.toString();
//		// 根据构造的list生成此控件对应的userObj
//		userObj.append(",").append("langDataArray:").append(jsArray).append(",").append("imageOnly:").append(ele.isImageOnly()).append(",").append("selectOnly:").append(ele.isSelectOnly())
//				.append(",").append("dataDivHeight:").append(ele.getDataDivHeight() == null ? null : ele.getDataDivHeight()).append(",").append("currentLangCode:").append(currentLangIndex);
//		buf.append("{").append(userObj.toString()).append("}");
//	}

	private void checkboxObj(FormElement ele, StringBuilder buf) {
		StringBuilder userObj = new StringBuilder();
		userObj.append("{valuePair:");
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
			if (dataType.equals(StringDataTypeConst.BOOLEAN) || dataType.equals(StringDataTypeConst.bOOLEAN))
				userObj.append("[\"true\",\"false\"]");
			else if (dataType.equals(StringDataTypeConst.FBOOLEAN))
				userObj.append("['Y','N']");
		}
		userObj.append(",visible:").append(ele.isVisible()).append("}");
		buf.append(userObj.toString());
	}

	private void richEditorObj(FormElement ele, StringBuilder buf) {
		String toolbarType = ele.getToolbarType();
		buf.append("{");
		buf.append("visible:").append(ele.isVisible());
		if (toolbarType != null)
			buf.append(",toolbarType:'").append(toolbarType).append("'");
		buf.append("}");

		// String hideBarIndices = ele.getHideBarIndices();
		// String hideImageIndices = ele.getHideImageIndices();
		// buf.append("[");
		// if (hideBarIndices != null && !"".equals(hideBarIndices))
		// buf.append(hideBarIndices);
		// else
		// buf.append("");
		// buf.append(",");
		// if (hideImageIndices != null && !"".equals(hideImageIndices))
		// buf.append(hideImageIndices);
		// else
		// buf.append("");
		// buf.append("]");
	}

	private String createForm(boolean isDynamic) {
		FormComp form = this.form;
		StringBuilder buf = new StringBuilder();
		String widget = WIDGET_PRE + this.getViewId();
		if (isDynamic) {// 动态脚本
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getViewId() + "');\n");
			buf.append(varFormId).append(" = $('<div>').autoform({parentDiv:'").append(getDivId());
			buf.append("', id:\"").append(form.getId());
		} else {
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('" + this.getViewId() + "');\n");
			buf.append(varFormId).append(" = $('<div>').autoform({parentDiv:'").append(getDivId());
			buf.append("', id:\"").append(form.getId());
		}

		buf.append("\",renderType:4,renderHiddenEle:").append(false);
		buf.append(",rowHeight:").append(form.getRowHeight()).append(",columnCount:").append(form.getColumn()).append("}).autoform('instance');\n");
		buf.append(varFormId + ".viewpart = " + widget + ";\n");
		buf.append(widget + ".addComponent('"+form.getId()+"'," + varFormId + ");\n");
		return buf.toString();
	}

	@Override
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_FORMELE;
	}

	@Override
	public void destroy() {
		String divId = this.getDivId();
		UIFormElement uiEle = this.getUiElement();
		if (uiEle != null) {
			StringBuilder buf = new StringBuilder();
			if (divId != null) {
				buf.append("window.execDynamicScript2RemoveFormElement2('" + divId + "','" + uiEle.getViewId() + "','" + uiEle.getFormId() + "','" + uiEle.getId() + "');\n");
			} else {
				buf.append("alert('删除控件失败！未获得divId')");
			}
			addDynamicScript(buf.toString());
		}
	}

	@Override
	public String createDesignTail() {
		StringBuilder buf = new StringBuilder();
		if (this.isEditMode()) {
			if (this.getViewId() != null && LuiRuntimeContext.isWindowEditorMode()) {
				return "";
			}
			String widgetId = this.getViewId() == null ? "" : this.getViewId();
			// String uiid = this.getParentRender().getId();
			// String eleid = this.getParentRender().getId();
			String uiid = this.getUiElement() == null ? "" : (String) this.getUiElement().getId();
			String eleid = this.getWebElement().getId() == null ? "" : this.getWebElement().getId();
			String renderType = "4";
			String type = this.getRenderType(this.getWebElement());
			if (type == null)
				type = "";

			if (getDivId() == null) {
				LuiLogger.error("div id is null!" + this.getClass().getName());
				throw new LuiRuntimeException("div id is null!" + this.getClass().getName());
			} else {
				buf.append("var params = {widgetid:'" + widgetId + "',uiid:'" + uiid + "',eleid:'" + this.formId + "',type:'" + type + "',subeleid:'" + eleid + "',subuiid:'',renderType:'"
						+ renderType + "'};\n");
				buf.append("$.design.getObj({divObj:" + this.getDivId() + "[0],params:params,objType:'component'});\n");
			}
			if (this.getDivId() != null) {
				buf.append("if(" + this.getDivId() + ")");
				buf.append(this.getDivId()).append(".css('padding','0px');\n");
			}
		}
		return buf.toString();
	}

	@Override
	public ViewPartMeta getCurrWidget() {
		return this.form.getWidget();
	}
	
	/**
	 * 获得当前控件所绑定Dataset
	 * 
	 * @return
	 */
	protected Dataset getDataset() {
		WebComp comp = this.form;
		if (!(comp instanceof IDataBinding))
			throw new LuiRuntimeException("the component is not type of IDataBinding:" + getId());
		Dataset ds = getDatasetById(((IDataBinding) comp).getDataset());
		if (ds == null)
			throw new LuiRuntimeException("can not find dataset by assigned id:" + ((IDataBinding) comp).getDataset());
		return ds;
	}
	
	@Override
	public void setWidth(String width) {
		if (width == null) {
			return;
		}
		super.setWidth(width);
		String divId = getDivId();
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(divId).append(" = $('#" + divId + "');\n");
		buf.append("var currForm = pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + form.getId() + "');\n");
		buf.append("currForm.updateFormWidth('" + this.getId() + "', " + divId + ",'" + width + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setEleWidth(String width) {
		if (width == null) {
			return;
		}
		String divId = getDivId();
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(divId).append(" = $('#" + divId + "');\n");
		buf.append("var currForm = pageUI.getViewPart('" + this.getViewId() + "').getComponent('" + form.getId() + "');\n");
		buf.append("currForm.updateType4Size('" + this.getId() + "', " + divId + ",'" + width + "');\n");
		addDynamicScript(buf.toString());
	}
	
	public void setHeight(String height) {
		if (height == null) {
			return;
		}
		height = RenderHelper.formatMeasureStr(height);
		String divId = getDivId();
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(divId).append(" = $('#" + divId + "');\n");
		buf.append(divId).append("css('height' , '").append(height).append("');");
		addDynamicScript(buf.toString());
	}

}
