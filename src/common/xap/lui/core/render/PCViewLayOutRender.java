package xap.lui.core.render;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.IDataBinding;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.DatasetRelation;
import xap.lui.core.dataset.DatasetRelations;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.IRefDataset;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.json.Dataset2JsonSerializer;
import xap.lui.core.layout.UIDialog;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.DatasetRule;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.FormRule;
import xap.lui.core.listener.GridRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.LuiListenerConf;
import xap.lui.core.listener.TreeRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeOut;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.BaseRefNode;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefConst;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.MasterFieldInfo;
import xap.lui.core.refrence.RefNodeRelation;
import xap.lui.core.refrence.RefNodeRelations;
import xap.lui.core.refrence.RefSelfUtil;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.tags.AppDynamicCompUtil;
import xap.lui.core.tags.DatasetMetaUtil;
import xap.lui.core.util.JsURLEncoder;
import xap.lui.core.util.StringUtil;
/**
 * @author renxh 片段渲染器
 * 
 */
@SuppressWarnings("unchecked")
public class PCViewLayOutRender extends UILayoutRender<UIViewPart, LuiElement> {
	public static final String OBS_IN = "OBS_IN";
	private boolean renderDiv = true;
	public PCViewLayOutRender(UIViewPart uiEle) {
		super(uiEle);
		// UIWidget widget = this.getUiElement();
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta luiWidget = pagePartMeta.getWidget(id);
		if (luiWidget != null && luiWidget.isDialog()) {
			if (renderDiv == false) {
				throw new LuiRuntimeException("对话框片段必须整体渲染");
			}
		}
	}
	
	
	public String place() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.placeSelf());
		
		UIViewPart widget = this.getUiElement();
		ILuiRender render = widget.getUimeta().getRender();
		UIPartMeta panel=widget.getUimeta();
		if (render != null){
			buf.append(render.place());
			buf.append(getDivId()).append(".append(").append(render.getNewDivId()).append(");\n");
		}
		if(panel instanceof UIPartMeta) {
			if(StringUtils.isNotBlank(((UIPartMeta)panel).getUiprovider()) && this.isEditMode()) {
				buf.append(getDivId()).append(".append('<H1>代码VIEW</H1>');\n");
			}
		}
		buf.append("$("+getDivId()+")").append(".attr('isViewPart','true');\n");
//		buf.append(this.generalTailHtml());
		
		return buf.toString();
	}

	public String getType(){
		return null;
	}
	
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void destroy() {
		addDynamicScript("pageUI.removeViewPart('"+this.getUiElement().getId()+"')");
	}

	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeChild( UIElement obj) {
		String divId = this.getDivId();
		StringBuilder buf = new StringBuilder();
		//UIViewPart widget = this.getUiElement();
		if (divId != null) {
			UIElement child = (UIElement)obj;
			if (child != null) {
				child.getRender().destroy();
			}
		} else {
			buf.append("alert('删除子元素失败！未获得divId')");
		}
		
		addDynamicScript(buf.toString());
	}

	@Override
	protected String getSourceType(LuiElement ele) {
		return LuiPageContext.SOURCE_TYPE_WIDGT;
	}
	
	protected String mockWidget() {
		return this.id;
	}

	@Override
	public String placeSelf() {
		if (renderDiv == false)
			return "";
		StringBuilder buf = new StringBuilder();
		UIViewPart uiwidget = getUiElement();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({");
		if (uiwidget instanceof UIDialog) {
			UIDialog dialog = (UIDialog) uiwidget;
			buf.append("'width':'" + dialog.getWidth() + "px',\n");
			buf.append("'height':'" + dialog.getHeight() + "px',\n");
		} else {
			buf.append("'width':'100%',\n");
			if (!isFlowMode())
				buf.append("'height':'100%',\n");
		}
		String position = "relative";
		if (uiwidget instanceof UIDialog)
			position = "absolute";
		buf.append("'top':'0px',\n");
		buf.append("'left':'0px',\n");
		buf.append("'position':'" + position + "'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
	
	public String createHead() {
		StringBuilder buf = new StringBuilder();
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(id);
		widget.setRendered(true);
		String script = createWidgetUI(widget);
		buf.append(script);
		if (widget.isDialog()) {
			buf.append(varId).append(".lazyInit = function(){\n");
		}
		// 初始化所有dataset.Ds数据在初始化PageModel时已经获取
		buf.append(renderDatasetScript(widget));
		// 初始化所有RefNode信息到页面中
		buf.append(renderRefNodeScript(widget));
		// 初始化所有聚合数据
		buf.append(renderComboDataScript(widget));
		// 记录Dataset客户端关系对象
		buf.append(renderDsRelations(widget));
		// 记录RefNode客户端关系对象
		buf.append(renderRefNodeRelations(widget));
		return buf.toString();
	}
	
	public String createTail() {
		StringBuilder buf = new StringBuilder();
		// UIWidget uiWidget = this.getUiElement();
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(id);
		if (widget.isDialog()) {
			String script = "};\n";
			buf.append(script);
		}
		String showId = getVarId();
		if (!LuiRuntimeContext.isEditMode()) {
			ViewPartMeta wdg = LuiRenderContext.current().getPagePartMeta().getWidget(this.id);
			String widgetEventScript = addEventSupport(wdg, getViewId(), "pageUI.getViewPart(\"" + getId() + "\")", null);
			buf.append(widgetEventScript);
			buf.append(showId).append(".onBeforeShow();\n");
		}
		if (renderDiv) {
			StringBuilder buft = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
			if (buft != null) {
				String script = buft.toString();
				buft.delete(0, buft.length());
				buf.append(script);
			}
		}
		// 加入plugout对象
		List<PipeOut> plugoutDescs = widget.getPipeOuts();
		if (plugoutDescs != null && plugoutDescs.size() > 0) {
			for (PipeOut plugout : plugoutDescs) {
				String id = plugout.getId();
				buf.append("var ").append(id).append(" = $.plugout.getObj('").append(id).append("');\n");
				EventSubmitRule sr = plugout.getSubmitRule();
				if (sr != null) {
					String submitRuleId = "sr";
					String srScript = generateSubmitRuleScript(sr, submitRuleId);
					buf.append(srScript);
					buf.append("").append(id).append(".submitRule = ").append(submitRuleId).append(";\n");
				}
				buf.append(varId).append(".addPlugOut(").append(id).append(");\n");
			}
		}
		buf.append(wrapByRequired("form", PcFormRenderUtil.getAllFormDsScript(this.id)));// 自由表单的数据集的设置
		PcFormRenderUtil.removeFormDsScript(this.id);
		UIViewPart uiWidget = getUiElement();
		if (widget.isDialog() || uiWidget instanceof UIDialog) {
			buf.append(showId).append(".$beforeInitData();\n");
		}
		return buf.toString();
	}
	private String createWidgetUI(ViewPartMeta widget) {
		StringBuilder buf = new StringBuilder();
		String showId = getVarId();
		buf.append("var ").append(showId).append(" = $.viewpart.getObj({");
		buf.append("id:'").append(widget.getId()).append("',");
		boolean visible = widget.isVisible();
		UIViewPart uiWidget = getUiElement();
		boolean dialog = false;
		if (widget.isDialog() || uiWidget instanceof UIDialog) {
			visible = false;
			dialog = true;
		}
		buf.append("visible:").append(visible).append(",");
		buf.append("dialog:").append(dialog);
		buf.append("});\n");
		buf.append("window.pageUI.addViewPart(").append(showId).append(");\n");
		buf.append(showId).append(".beforeWidgetInit();\n");
		return buf.toString();
	}
	/**
	 * 渲染当前控件所对应dataset脚本
	 * 
	 * @param widget
	 * @return
	 */
	protected String renderDatasetScript(ViewPartMeta widget) {
		StringBuilder buf = new StringBuilder();
		Dataset[] datasets = widget.getViewModels().getDatasets();
		if (datasets != null) {
			for (int i = 0; i < datasets.length; i++) {
				Dataset ds = datasets[i];
				if (ds instanceof IRefDataset)
					continue;
				ds.setRendered(true);
				String script = generateDatasetScript(widget, ds);
				buf.append(script);
				String dataScript = generateDataScript(widget, ds);
				if (dataScript != null) {
					buf.append(dataScript);
				}
			}
		}
		return buf.toString();
	}
	protected String renderRefNodeScript(ViewPartMeta widget) {
		StringBuilder buf = new StringBuilder();
		IRefNode[] refnodes = widget.getViewModels().getRefNodes();
		if (refnodes != null) {
			for (int i = 0; i < refnodes.length; i++) {
				IRefNode refNode = refnodes[i];
				refNode.setRendered(true);
				String script = generateRefNodeScript(widget, refNode);
				buf.append(script);
			}
		}
		return buf.toString();
	}
	/**
	 * 渲染ComboData到客户端
	 * 
	 * @param widget
	 * @param combodataId
	 * @return
	 */
	protected String renderComboDataScript(ViewPartMeta widget) {
		StringBuilder sb = new StringBuilder();
		ComboData[] cds = widget.getViewModels().getComboDatas();
		for (int j = 0; j < cds.length; j++) {
			ComboData cd = cds[j];
			StringBuilder buf = renderComboDataScript(widget, cd);
			sb.append(buf);
		}
		return sb.toString();
	}
	/**
	 * 渲染combodata的脚本
	 * 
	 * @param widget
	 * @param cd
	 * @return
	 */
	private StringBuilder renderComboDataScript(ViewPartMeta widget, ComboData cd) {
		cd.setRendered(true);
		StringBuilder buf = new StringBuilder();
		String id = COMBO_PRE + widget.getId() + "_" + cd.getId();
		buf.append("window.").append(id).append(" = $.datalist.getObj(\"").append(cd.getId()).append("\");\n");
		DataItem[] items = cd.getAllDataItems();
		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				// String text = translate(items[i].getI18nName(),
				// items[i].getText(), items[i].getLangDir());
				// text = text.replace("\"", "\\\\\"");
				String text = items[i].getText();
				String value = items[i].getValue();
				value = value.replace("\\", "&#92;");
				buf.append(id).append(".addItem($.dataitem.getObj(\"").append(text).append("\",\"").append(value).append("\"");
				if (items[i].getImage() != null && items[i].getImage().trim().length() > 0) {
					buf.append(",'" + items[i].getImage() + "'");
				}
				buf.append("));\n");
			}
		}
		buf.append("if(!" + varId + ")");
		buf.append("var " + varId).append("=pageUI.getViewPart('" + widget.getId() + "');\n");
		buf.append(varId).append(".addComboData(").append(id).append(");\n");
		buf.append(id).append(".viewpart = ").append(varId).append(";\n");
		return buf;
	}
	protected String renderDsRelations(ViewPartMeta widget) {
		StringBuilder scriptBuf = new StringBuilder();
		DatasetRelations dsRelations = widget.getViewModels().getDsrelations();
		if (dsRelations != null) {
			scriptBuf.append(varId + ".setDsRelations($.dsrelations.getObj());\n");
			DatasetRelation[] rels = dsRelations.getDsRelations();
			for (int i = 0; i < rels.length; i++) {
				DatasetRelation dsRelation = rels[i];
				scriptBuf.append("var ").append(DS_RELATION_PRE).append(dsRelation.getId()).append(" = $.dsrelation.getObj({id:\"").append(dsRelation.getId()).append("\",masterDataset:\"").append(dsRelation.getMasterDataset()).append("\",masterKeyField:\"").append(dsRelation.getMasterKeyField()).append("\",detailDataset:\"").append(dsRelation.getDetailDataset()).append("\",detailForeignkey:\"").append(dsRelation.getDetailForeignKey()).append("\"});\n");
				scriptBuf.append(varId + ".dsRelations.addRelation(").append(DS_RELATION_PRE).append(dsRelation.getId()).append(");\n");
			}
		}
		return scriptBuf.toString();
	}
	protected String renderRefNodeRelations(ViewPartMeta widget) {
		StringBuilder scriptBuf = new StringBuilder();
		RefNodeRelations refNodeRelations = widget.getViewModels().getRefNodeRelations();
		if (refNodeRelations != null) {
			scriptBuf.append("window.$refNodeRelations = $.refnoderelations.getObj();\n");
			scriptBuf.append("window.$refNodeRelations").append(".viewpart = ").append(varId).append(";\n");
			Map<String, RefNodeRelation> map = refNodeRelations.getRefnodeRelations();
			Set<String> keys = map.keySet();
			Iterator<String> it = keys.iterator();
			while (it.hasNext()) {
				String relId = it.next();
				RefNodeRelation rfRelation = map.get(relId);
				scriptBuf.append("window.").append(RF_RELATION_PRE).append(rfRelation.getId()).append(" = $.refnoderelation.getObj({id:\"").append(rfRelation.getId()).append("\",masterFieldInfos:\"")
				// .append(rfRelation.getMasterRefNode())
						.append(refNodeInfoToObj(rfRelation.getMasterFieldInfos())).append("\",detailRefNode:\"").append(rfRelation.getDetailRefNode()).append("\",targetDsId:\"").append(rfRelation.getTargetDs()).append("\",clearDetail:").append(rfRelation.isClearDetail()).append("});\n");
				scriptBuf.append("window.$refNodeRelations.addRelation(").append(RF_RELATION_PRE).append(rfRelation.getId()).append(");\n");
			}
			scriptBuf.append("$.viewmodel.bindRefNode2Dataset();\n");
		}
		return scriptBuf.toString();
	}
	/**
	 * 生成Dataset对应脚本文件
	 * 
	 * @param ds
	 * @return
	 */
	public String generateDatasetScript(ViewPartMeta widget, Dataset ds) {
		StringBuilder buf = new StringBuilder();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		// buf.append("var " + dsVarId)
		buf.append(dsVarId).append(" = $.dataset.getObj({");
		buf.append("id:\"").append(ds.getId()).append("\",");
		String metaStr = DatasetMetaUtil.generateMeta(ds);
		buf.append("meta:").append(metaStr).append(",");
		buf.append("lazyLoad:").append(ds.isLazyLoad()).append(",");
		buf.append("editable:").append(ds.isEdit()).append(",");
		buf.append("pageSize:").append(ds.getPageSize());
		buf.append("});\n");
		// 设置ds的操作状态
		// String operatorStatusArray = ds.getOperatorStatusArray();
		// if (operatorStatusArray != null && !operatorStatusArray.equals(""))
		// buf.append(dsVarId + ".operateStateArray=" +
		// StringUtil.mergeScriptArray(operatorStatusArray) + ";\n");
		String varId = getVarId();
		// buf.append(addEventSupport(ds, ds.getId()));
		buf.append("var ").append(varId).append("= pageUI.getViewPart('").append(getId()).append("');\n");
		buf.append(getVarId()).append(".addDataset(").append(dsVarId).append(");\n");
		buf.append(dsVarId).append(".viewpart = ").append(getVarId()).append(";\n");
		WebComp[] comps = widget.getViewComponents().getComps();
		for (int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof IDataBinding) {
				String dsId = ((IDataBinding) comps[i]).getDataset();
				if (ds.getId().equals(dsId)) {
					buf.append(dsVarId).append(".hasComp = true;\n");
					break;
				}
			}
		}
		buf.append(addEventSupport(ds, widget.getId(), getDatasetVarShowId(ds.getId(), widget.getId()), null));
		return buf.toString();
	}
	/**
	 * 想dataset中添加field的脚本 renxh
	 * 
	 * @param widget
	 * @param ds
	 * @param field
	 * @return
	 */
	private String addDatasetFieldScript(ViewPartMeta widget, Dataset ds, Field field) {
		StringBuilder buf = new StringBuilder();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		buf.append("var ").append(dsVarId).append(" = pageUI.getViewPart('" + id + "').getDataset('" + ds.getId() + "');\n");
		buf.append("if(" + dsVarId + "){");
		buf.append(dsVarId + ".addField(" + DatasetMetaUtil.generateField(field) + ");\n");
		buf.append("};\n");
		return buf.toString();
	}
	private String generateDataScript(ViewPartMeta widget, Dataset ds) {
		if (ds.isLazyLoad() == false)
			return null;
		Dataset2JsonSerializer seriali=new Dataset2JsonSerializer(ds);
		String dsXml =seriali.serialize(ds).toJSONString();
		String dsVarId = getDatasetVarShowId(ds.getId(), widget.getId());
		StringBuilder buf=new StringBuilder();
		buf.append("var json_"+dsVarId+"=decodeURIComponent('" + JsURLEncoder.encode(dsXml, "UTF-8") + "');\r\n");
		buf.append("var obj_"+dsVarId+"=eval('('+json_"+dsVarId+"+')');\r\n");
		buf.append(dsVarId + ".setData(obj_"+dsVarId+");\r\n");
		return buf.toString();
	}
	protected String generateRefNodeScript(ViewPartMeta widget, IRefNode iRefNode) {
		StringBuilder buf = new StringBuilder(1024);
		String refId = RF_PRE + widget.getId() + "_" + iRefNode.getId();
		if (iRefNode instanceof GenericRefNode) {
			GenericRefNode refNode = (GenericRefNode) iRefNode;
			// 如果relation为空,则按照fields来取
			String readFields = StringUtil.mergeScriptArray(refNode.getReadFields());
			String writeFields = StringUtil.mergeScriptArray(refNode.getWriteFields());
			buf.append("window.").append(refId).append(" = ").append("$.refnodeinfo.getObj({id:\"");
			buf.append(refNode.getId()).append("\",name:\"").append(refNode.getTitle());
			buf.append("\",pageMeta:\"").append(refNode.getPagemeta()).append("\",readDs:\"").append(refNode.getReadDataset()).append("\",writeDs:\"");
			buf.append(refNode.getWriteDataset() == null ? "" : refNode.getWriteDataset()).append("\",readFields:").append(readFields).append(",writeFields:");
			buf.append(writeFields).append(",filterSql:\"").append("") // filterSql
					.append("\",userObj:\"").append("").append("\",multiSel:").append(refNode.isMultiple()).append(",usePower:").append(false) // use
					.append(",selLeafOnly:").append(refNode.isOnlyLeaf()).append(",allowExtendValue:").append(refNode.isQuickInput());
			if (refNode.getWidth() != null && !"".equals(refNode.getWidth()))
				buf.append(",dialogWidth:").append(refNode.getWidth());
			if (refNode.getHeight() != null && !"".equals(refNode.getHeight()))
				buf.append(",dialogHeight:").append(refNode.getHeight());
			if (refNode.getController() != null && !"".equals(refNode.getController()))
				buf.append(",dataListener:").append("'" + refNode.getController() + "'");
			buf.append(",isRead:").append(refNode.isRead());
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
				buf.append(",refType:").append(reftype);
			}
			buf.append("});\n");
		} else if (iRefNode instanceof SelfDefRefNode) {
			SelfDefRefNode refNode = (SelfDefRefNode) iRefNode;
			buf.append("window.").append(refId).append(" = ").append("$.selfrefnodeinfo.getObj({").append("id:'").append(refNode.getId()).append("'").append(",text:'").append(refNode.getTitle()).append("'").append(",url:'").append(refNode.getPath()).append("'").append(",dialogWidth:'").append(refNode.getWidth() == null ? "" : refNode.getWidth()).append("'").append(",dialogHeight:'").append(refNode.getHeight() == null ? "" : refNode.getHeight()).append("'")
					.append(",isRead:").append(refNode.isRead()).append("});\n");
		}
		buf.append("var " + getVarId()).append(" = pageUI.getViewPart('" + widget.getId() + "');\n");
		buf.append(getVarId()).append(".addRefNode(").append(refId).append(");\n");
		buf.append(refId);
		buf.append(".viewpart = ").append(getVarId()).append(";\n");
		return buf.toString();
	}
	/**
	 * 将RefNodeInfo数组转换为前台JS对象
	 * 
	 * @param refNodeInfos
	 * @return
	 */
	private String refNodeInfoToObj(List<MasterFieldInfo> masterFieldInfos) {
		StringBuilder buf = new StringBuilder();
		buf.append("[");
		if (masterFieldInfos != null) {
			for (int i = 0, n = masterFieldInfos.size(); i < n; i++) {
				MasterFieldInfo masterFieldInfo = masterFieldInfos.get(i);
				buf.append("{dsId:'").append(masterFieldInfo.getDsId()).append("',fieldId:'").append(masterFieldInfo.getFieldId()).append("',filterSql:'").append(masterFieldInfo.getFilterSql()).append("',nullProcess:'").append(masterFieldInfo.getNullProcess()).append("'}");
				if (i != masterFieldInfos.size() - 1)
					buf.append(",");
			}
		}
		buf.append("]");
		return buf.toString();
	}
	protected String getSourceType(IEventSupport ele) {
		if (ele instanceof Dataset)
			return LuiPageContext.SOURCE_TYPE_DATASET;
		else if (ele instanceof ViewPartMeta)
			return LuiPageContext.SOURCE_TYPE_WIDGT;
		return null;
	}
	@Override
	protected String getExtendProxyStr(IEventSupport ele, LuiListenerConf listener, LuiEventConf jsEvent) {
		if (ele instanceof Dataset) {
			if (jsEvent.getName().equals("onDataLoad")) {
				StringBuilder buf = new StringBuilder();
				buf.append("if(dataLoadEvent.userObj != null)\n").append("proxy.setReturnArgs(dataLoadEvent.userObj);\n");
				return buf.toString();
			}
		}
		return null;
	}
	protected void addProxyParam(StringBuilder buf, String eventName) {
		super.addProxyParam(buf, eventName);
		if (eventName.equals("onAfterRowInsert")) {
			buf.append("proxy.addParam('row_insert_index', rowInsertEvent.insertedIndex);\n");
		}
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void adjustField(Dataset dataset, Field field) {
		String script = this.adjustFieldScript(dataset, field);
		this.addBeforeExeScript(wrapByRequired("dataset", script));
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRefNodePath(SelfDefRefNode refnode) {
		String path = refnode.getPath();
		String script = this.adjustRefNodePathScript(refnode, path);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setDataListener(GenericRefNode refNode) {
		String dataListener = refNode.getController();
		String script = this.setRefNodeDataListener(refNode, dataListener);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRefNodeWidth(BaseRefNode refnode) {
		String refNodeWidth = refnode.getWidth();
		String script = this.adjustRefNodeWidthScript(refnode, refNodeWidth);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setRefNodeText(String refNodeId,BaseRefNode refNode){
		String text = refNode.getTitle();
		String script = this.adjustRefName(refNode, text);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void updateRefNodeHeight(String refNodeId,BaseRefNode refNode){
		String refNodeHeight = refNode.getHeight();
		String script = this.adjustRefNodeHeightScript(refNode, refNodeHeight);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeField(Dataset dataset,Field field){
		String script = this.removeFiledScript(dataset, field);
		this.addBeforeExeScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCaption(){
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(this.viewId);
		String script = this.captionScript(widget);
		this.addDynamicScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setDataList(ComboData dataList) {
		this.comboDataChangeScript(this.getViewId(), dataList);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addDataList(ComboData dataList) {
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(this.viewId);
		String script = this.renderComboDataScript(widget, dataList).toString();
		this.addDynamicScript(script);
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addDataset(Dataset ds) {
		String script = generateDatasetScript(getCurrWidget(), ds);
		this.addBeforeExeScript(wrapByRequired("dataset", script));
	}
	
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void removeDataset(String dsId) {
		String script = "pageUI.getViewPart('" + id +"').removeDataset('"+dsId+"')";
		this.addDynamicScript(script);
	}
	

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setError(Dataset dataset,Integer rowindex, Integer feildIndex, String error) {
		String script = setDsError(dataset, rowindex, feildIndex, error);
		this.addDynamicScript(wrapByRequired("dataset", script));
	}
	
	public void notifyAddDsField(UIPartMeta uiMeta, PagePartMeta pageMeta, Object obj) {
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(id);
		if (obj instanceof Map) {
			Map<String, Object> map = (Map<String, Object>) obj;
			String dsId = (String) map.get("dsId");
			Field field = (Field) map.get("field");
			Dataset ds = widget.getViewModels().getDataset(dsId);
			String script = this.addDatasetFieldScript(widget, ds, field);
			this.addDynamicScript(script);
		}
	}
	/**
	 * 添加引用脚本
	 * 
	 * @param uiMeta
	 * @param pageMeta
	 * @param obj
	 */
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addRefNode(IRefNode refNode) {
		PagePartMeta pagePartMeta=LuiRenderContext.current().getPagePartMeta();
		ViewPartMeta widget = pagePartMeta.getWidget(id);
		String script = this.generateRefNodeScript(widget, refNode);
		this.addDynamicScript(script);
	}
	
//	@Override
//	public String create() {
//		String str = super.create();
//		// 子节点
//		UIViewPart uiWidget = this.getUiElement();
//		ViewPartMeta widget = getCurrWidget();
//		if (widget.isDialog() || uiWidget instanceof UIDialog) {
//			return wrapByRequired("modaldialog", str);
//		}
//		return str;
//	}
	

	@Override
	public String create() {
		String str = super.create();
		StringBuilder buf = new StringBuilder();
		buf.append("pageUI.hasChanged=false;\n").append(str);
		return buf.toString();
	}
	private String adjustFieldScript(Dataset ds, Field field) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = ds.getWidget();
		String varDs = "$c_" + ds.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varDs).append(" = pageUI.getDataset('" + ds.getId() + "');\n");
		} else {
			buf.append("var " + varDs).append(" = pageUI.getViewPart('" + widget.getId() + "').getDataset('" + ds.getId() + "');\n");
		}
		String varField = "$c_" + field.getId();
		buf.append("var " + varField).append(" = " + DatasetMetaUtil.generateField(field) + ";\n");
		buf.append(varDs).append(".addField(" + varField + ");\n");
		return buf.toString();
	}
	private String setDsError(Dataset ds, Integer rowindex, Integer feildIndex, String error) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = ds.getWidget();
		String varDs = "$c_" + ds.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varDs).append(" = pageUI.getDataset('" + ds.getId() + "');\n");
		} else {
			buf.append("var " + varDs).append(" = pageUI.getViewPart('" + widget.getId() + "').getDataset('" + ds.getId() + "');\n");
		}
		buf.append(varDs).append(".setError(" + rowindex + "," + feildIndex + ",'" + error + "')");
		return buf.toString();
	}
	private String adjustRefNodePathScript(BaseRefNode refnode, String path) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = refnode.getWidget();
		String varRefnode = RF_PRE + widget.getId() + "_" + refnode.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varRefnode).append(" = pageUI.getRefNode('" + refnode.getId() + "');\n");
		} else {
			buf.append("var " + varRefnode).append(" = pageUI.getViewPart('" + widget.getId() + "').getRefNode('" + refnode.getId() + "');\n");
		}
		buf.append(varRefnode).append(".setPath('" + path + "');\n");
		return buf.toString();
	}
	/**
	 * 调整自定义参照的宽度
	 * 
	 * @param refnode
	 * @param refnodeWidth
	 * @return
	 */
	private String adjustRefNodeWidthScript(BaseRefNode refnode, String refnodeWidth) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = refnode.getWidget();
		String varRefnode = RF_PRE + widget.getId() + "_" + refnode.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varRefnode).append(" = pageUI.getRefNode('" + refnode.getId() + "');\n");
		} else {
			buf.append("var " + varRefnode).append(" = pageUI.getViewPart('" + widget.getId() + "').getRefNode('" + refnode.getId() + "');\n");
		}
		buf.append(varRefnode).append(".setDialogWidth('" + refnodeWidth + "');\n");
		return buf.toString();
	}
	/**
	 * 设置datasetListener
	 * 
	 * @param refnode
	 * @param dataListener
	 * @return
	 */
	private String setRefNodeDataListener(BaseRefNode refnode, String dataListener) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = refnode.getWidget();
		String varRefnode = RF_PRE + widget.getId() + "_" + refnode.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varRefnode).append(" = pageUI.getRefNode('" + refnode.getId() + "');\n");
		} else {
			buf.append("var " + varRefnode).append(" = pageUI.getViewPart('" + widget.getId() + "').getRefNode('" + refnode.getId() + "');\n");
		}
		buf.append(varRefnode).append(".setDataListener('" + dataListener + "');\n");
		return buf.toString();
	}
	/**
	 * 调整参照的显示名称
	 * 
	 * @param refnode
	 * @param refnodeWidth
	 * @return
	 */
	private String adjustRefName(BaseRefNode refnode, String name) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = refnode.getWidget();
		String varRefnode = RF_PRE + widget.getId() + "_" + refnode.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varRefnode).append(" = pageUI.getRefNode('" + refnode.getId() + "');\n");
		} else {
			buf.append("var " + varRefnode).append(" = pageUI.getViewPart('" + widget.getId() + "').getRefNode('" + refnode.getId() + "');\n");
		}
		buf.append(varRefnode).append(".setName('" + name + "');\n");
		return buf.toString();
	}
	/**
	 * 调整自定义参照的高度
	 * 
	 * @param refnode
	 * @param refnodeHeight
	 * @return
	 */
	private String adjustRefNodeHeightScript(BaseRefNode refnode, String refnodeHeight) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = refnode.getWidget();
		String varRefnode = RF_PRE + widget.getId() + "_" + refnode.getId();
		// buf.append("pageUI.hasChanged=false;\n");
		if (widget == null) {
			buf.append("var " + varRefnode).append(" = pageUI.getRefNode('" + refnode.getId() + "');\n");
		} else {
			buf.append("var " + varRefnode).append(" = pageUI.getViewPart('" + widget.getId() + "').getRefNode('" + refnode.getId() + "');\n");
		}
		buf.append(varRefnode).append(".setDialogHight('" + refnodeHeight + "');\n");
		return buf.toString();
	}
	private String removeFiledScript(Dataset ds, Field field) {
		StringBuilder buf = new StringBuilder();
		ViewPartMeta widget = ds.getWidget();
		String varDs = "$c_" + ds.getId();
		if (widget == null) {
			buf.append("var " + varDs).append(" = pageUI.getDataset('" + ds.getId() + "');\n");
		} else {
			buf.append("var " + varDs).append(" = pageUI.getViewPart('" + widget.getId() + "').getDataset('" + ds.getId() + "');\n");
		}
		buf.append(varDs).append(".removeField('" + field.getId() + "');\n");
		return buf.toString();
	}
	private String captionScript(ViewPartMeta wg) {
		return "pageUI.getDialog('" + wg.getId() + "').titleDiv.innerHTML='" + wg.getCaption() + "'";
	}
	private void comboDataChangeScript(String widgetId, ComboData comboData) {
		if (comboData.getExtendAttributeValue(OBS_IN) != null)
			return;
		comboData.setExtendAttribute(OBS_IN, "1");
		AppContext appCtx = AppSession.current().getAppContext();
		ViewPartContext viewCtx = appCtx.getCurrentWindowContext().getViewContext(widgetId);
		AppDynamicCompUtil util = new AppDynamicCompUtil(appCtx, viewCtx);
		try {
			util.replaceComboData(comboData.getId(), widgetId, (ComboData) comboData.clone());
		} catch (Exception e) {
			// do nothing
		}
		comboData.removeExtendAttribute(OBS_IN);
	}
	/**
	 * 生成提交规则
	 * 
	 */
	public String generateSubmitRuleScript(EventSubmitRule submitRule, String submitRuleId) {
		if (submitRule != null) {
			// String ruleId = "sr_" + jsEvent.getName() + "_" +
			// listener.getId();
			String ruleId = submitRuleId;
			StringBuilder buf = new StringBuilder();
			buf.append("var ").append(ruleId).append(" = $.submitrule.getObj();\n");
			if (submitRule.getParamMap() != null && submitRule.getParamMap().size() > 0) {
				Iterator<Entry<String, Parameter>> it = submitRule.getParamMap().entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Parameter> entry = it.next();
					buf.append(ruleId).append(".addParam('").append(entry.getKey()).append("', '").append(entry.getValue().getValue()).append("');\n");
				}
			}
			if (submitRule.isCardSubmit()) {
				buf.append(ruleId).append(".setCardSubmit(true);\n");
			}
			String jsScript = generateWidgetRulesScript(submitRule, ruleId);
			buf.append(jsScript);
			EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
			if (pSubmitRule != null) {
				String pRuleId = ruleId + "_parent";
				buf.append("var " + pRuleId + " = $.submitrule.getObj();\n");
				String pJsScript = generateWidgetRulesScript(pSubmitRule, pRuleId);
				buf.append(pJsScript);
				buf.append(ruleId + ".setParentSubmitRule(" + pRuleId + ");\n");
			}
			return buf.toString();
			// buf.append(listenerShowId).append(".").append(jsEvent.getName()).append(".submitRule = (").append(ruleId).append(");\n");
		} else
			return "";
	}
	/**
	 * 创建所有Widget的提交规则
	 * 
	 * @param submitRule
	 * @param ruleId
	 * @return
	 */
	private String generateWidgetRulesScript(EventSubmitRule submitRule, String ruleId) {
		StringBuilder sb = new StringBuilder();
		LuiSet<WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
		if (widgetRuleMap != null && !widgetRuleMap.isEmpty()) {
			Iterator<WidgetRule> it = widgetRuleMap.iterator();
			while (it.hasNext()) {
				WidgetRule widgetRule = it.next();
				String wstr = generateWidgetRuleScript(widgetRule);
				sb.append(wstr);
				String widgetId = widgetRule.getId();
				sb.append(ruleId).append(".addViewPartRule('").append(widgetId).append("', wdr_").append(widgetId).append(");\n");
			}
		}
		return sb.toString();
	}
	/**
	 * 得到Widget的规则
	 * 
	 * @param widgetRule
	 * @return
	 */
	private String generateWidgetRuleScript(WidgetRule widgetRule) {
		StringBuilder buf = new StringBuilder();
		String wid = "wdr_" + widgetRule.getId();
		buf.append("var ").append(wid).append(" = $.viewpartrule.getObj('").append(widgetRule.getId()).append("');\n");
		if (widgetRule.isCardSubmit()) {
			buf.append(wid).append(".setCardSubmit(true);\n");
		}
		if (widgetRule.isTabSubmit()) {
			buf.append(wid).append(".setTabSubmit(true);\n");
		}
		if (widgetRule.isPanelSubmit()) {
			buf.append(wid).append(".setPanelSubmit(true);\n");
		}
		DatasetRule[] dsRules = widgetRule.getDatasetRules();
		if (dsRules != null) {
			for (int i = 0; i < dsRules.length; i++) {
				DatasetRule dsRule = dsRules[i];
				String id = "dsr_" + dsRule.getId();
				buf.append("var ").append(id).append(" = $.datasetrule.getObj('").append(dsRule.getId()).append("','").append(dsRule.getType()).append("');\n");
				buf.append(wid).append(".addDsRule('").append(dsRule.getId()).append("',").append(id).append(");\n");
			}
		}
		TreeRule[] treeRules = widgetRule.getTreeRules();
		if (treeRules != null) {
			for (int i = 0; i < treeRules.length; i++) {
				TreeRule treeRule = treeRules[i];
				String id = "treer_" + treeRule.getId();
				buf.append("var ").append(id).append(" = $.treerule.getObj('").append(treeRule.getId()).append("','").append(treeRule.getType()).append("');\n");
				buf.append(wid).append(".addTreeRule('").append(treeRule.getId()).append("',").append(id).append(");\n");
			}
		}
		GridRule[] gridRules = widgetRule.getGridRules();
		if (gridRules != null) {
			for (int i = 0; i < gridRules.length; i++) {
				GridRule gridRule = gridRules[i];
				String id = "gridr_" + gridRule.getId();
				buf.append("var ").append(id).append(" = $.gridrule.getObj('").append(gridRule.getId()).append("','").append(gridRule.getType()).append("');\n");
				buf.append(wid).append(".addGridRule('").append(gridRule.getId() + "',").append(id).append(");\n");
			}
		}
		FormRule[] formRules = widgetRule.getFormRules();
		if (formRules != null) {
			for (int i = 0; i < formRules.length; i++) {
				FormRule formRule = formRules[i];
				String id = "formr_" + formRule.getId();
				buf.append("var ").append(id).append(" = $.formrule.getObj('").append(formRule.getId()).append("','").append(formRule.getType()).append("');\n");
				buf.append(wid).append(".addFormRule('").append(formRule.getId() + "',").append(id).append(");\n");
			}
		}
		return buf.toString();
	}
}
