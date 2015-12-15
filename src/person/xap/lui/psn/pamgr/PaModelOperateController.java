package xap.lui.psn.pamgr;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xap.lui.core.cache.PaCache;
import xap.lui.core.common.InteractionUtil;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.control.IWindowCtrl;
import xap.lui.core.control.ModePhase;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.DataItem;
import xap.lui.core.dataset.DataList;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.dataset.MDComboDataConf;
import xap.lui.core.dataset.Row;
import xap.lui.core.event.MouseEvent;
import xap.lui.core.exception.ComboInputItem;
import xap.lui.core.exception.InputItem;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.exception.StringInputItem;
import xap.lui.core.model.AppContext;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.SelfDefRefNode;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.command.PaHelper;
/**
 * 对模型操作的后台控制类、包含模型（数据集、参照、下拉数据集）的增删改查
 * 
 * @author wupeng1
 *
 */
@SuppressWarnings({ "unused" })
public class PaModelOperateController implements IWindowCtrl {
	public static final String TYPE_COMBODATA = "ComboData";
	public static final String TYPE_DATASET = "DATASET";
	public static final String TYPE_REFNODE = "RefNode";
	public static final String DATASET_NAME = "entityds";
	public static final String TYPE_COMBO = "combo";
	public static final String TYPE_REFMODEL = "RefModel";
	private String params_uuid = null;
	private String params_id = null;
	private String params_pid = null;
	private String params_type = null;
	private ModePhase modePhase = null;
	private ViewPartMeta sourceView = null;
	private PagePartMeta sourceWin = null;
	/**
	 * 初始化获取参数信息
	 */
	public void initParams() {
		// 获取源window和view
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		IPaEditorService pes = new PaEditorServiceImpl();
		PaCache cache = PaCache.getInstance();
		String sessionId = (String) cache.get("eclipse_sesionId");
		String viewId = (String) cache.get("_viewId");
		String pageId = (String) cache.get("_pageId");
		modePhase = LuiRuntimeContext.getModePhase();
		PagePartMeta pagemeta = null;
		ViewPartMeta widget = null;
		if (modePhase == null) {
			throw new LuiRuntimeException("设计器没有设置正确的状态!");
		}
		if (modePhase.equals(ModePhase.nodedef) || modePhase.equals(ModePhase.persona)) {
			String sourceWinId = PaHelper.getCurrentEditWindowId();
			pagemeta = pes.getOriPageMeta(sourceWinId, sessionId);
			if (pagemeta == null)
				return;
			widget = pagemeta.getWidget(viewId);
		}
		if (modePhase.equals(ModePhase.eclipse)) {
			pagemeta = pes.getOriPageMeta(pageId, sessionId);
			if (pagemeta == null)
				return;
			widget = pagemeta.getWidget(viewId);
		}
		// 获取当前菜单所在window和view以及对应的参数
		ViewPartMeta currView = AppSession.current().getViewContext().getView();
		PagePartMeta currWin = AppSession.current().getWindowContext().getPagePartMeta();
		Dataset ds = currView.getViewModels().getDataset(DATASET_NAME);
		Row row = ds.getSelectedRow();
		if (row == null) {
			throw new LuiRuntimeException("请选择数据集！");
		}
		int uuidIndex = ds.nameToIndex("uuid");
		int idIndex = ds.nameToIndex("id");
		int typeIndex = ds.nameToIndex("type");
		int pIdIndex = ds.nameToIndex("pid");
		this.sourceWin = pagemeta;
		this.sourceView = widget;
		this.params_uuid = (String) row.getValue(uuidIndex);
		this.params_id = (String) row.getValue(idIndex);
		this.params_pid = (String) row.getValue(pIdIndex);
		this.params_type = (String) row.getValue(typeIndex);
	}
	// 新建模型的后台处理类
	public void addModel(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		// 新建下拉数据集
		if (TYPE_COMBODATA.equals(this.params_type) && this.params_pid == null) {
			// 输入下拉数据集名称
			InputItem coderow = new StringInputItem("comboDataCode", "下拉数据编码：", true);
			InputItem namerow = new StringInputItem("comboDataName", "下拉数据名称：", true);
			coderow.setValue("");
			namerow.setValue("");
			InteractionUtil.showInputDialog("请输入下拉数据集名称和编码：", new InputItem[] { coderow, namerow });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String comboDataCode = rs.get("comboDataCode");
			String comboDataName = rs.get("comboDataName");
			// 正则表达式校验
			if (!validateInputString(comboDataCode))
				throw new LuiRuntimeException("请使用规范的数据集编码重新创建!");
			ComboData comboData = new DataList();
			comboData.setId(comboDataCode);
			comboData.setCaption(comboDataName);
			boolean isExist = this.sourceView.getViewModels().getComboData(comboDataCode) != null ? true : false;
			if (isExist) {
				throw new LuiRuntimeException("新建的下拉数据集编码存在，请重新创建！");
			}
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			this.sourceView.getViewModels().addComboData(comboData);
			RequestLifeCycleContext.get().setPhase(phase);
			// 触发前台通知eclipse进行状态控制
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
			AppSession.current().getAppContext()
					.addExecScript("document.getElementById('iframe_tmp').contentWindow.reNewModel('" + comboDataCode + "','" + this.params_type + "','" + this.sourceView.getId() + "');");
			addTreeNode(comboDataCode, comboDataName, TYPE_COMBODATA);
		} else if (TYPE_REFNODE.equals(this.params_type) && this.params_pid == null) {
			InputItem refId = new StringInputItem("refNodeId", "参照ID：", true);
			InputItem refName = new StringInputItem("refName", "参照名称：", true);
			refName.setValue("");
			refId.setValue("");
			ComboInputItem refType = new ComboInputItem("refType", "参照类型：", true);
			ComboData cd = new DataList();
			DataItem item1 = new DataItem("NcRefNode", "通用参照");
			DataItem item2 = new DataItem("SelfRefNode", "自定义参照");
			cd.addDataItem(item1);
			cd.addDataItem(item2);
			refType.setComboData(cd);
			refType.setValue("NcRefNode");
			InteractionUtil.showInputDialog("请输入参照信息", new InputItem[] { refId, refName, refType });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String refNodeId = rs.get("refNodeId");
			String reftype = rs.get("refType");
			String refLabel = rs.get("refName");
			// 正则表达式校验
			if (!validateInputString(refNodeId))
				throw new LuiRuntimeException("请使用规范的参照ID重新创建!");
			IRefNode refNode = null;
			if ("NcRefNode".equals(reftype)) {
				refNode = new GenericRefNode();
			} else if ("SelfRefNode".equals(reftype)) {
				refNode = new SelfDefRefNode();
			}
			refNode.setId(refNodeId);
			boolean isExist = this.sourceView.getViewModels().getRefNode(refNodeId) != null ? true : false;
			if (isExist) {
				throw new LuiRuntimeException("新建的参照数据集编码存在，请重新创建！");
			}
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			this.sourceView.getViewModels().addRefNode(refNode);
			RequestLifeCycleContext.get().setPhase(phase);
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
			addTreeNode(refNodeId, refLabel, TYPE_REFNODE);
		} else if (TYPE_DATASET.equals(this.params_type) && this.params_pid == null) {
			InputItem row = new StringInputItem("code", "数据集编码：", true);
			InputItem nameRow = new StringInputItem("caption", "数据集名称：", true);
			row.setValue("");
			nameRow.setValue("");
			InteractionUtil.showInputDialog("请输入数据集信息", new InputItem[] { row, nameRow });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String dsId = rs.get("code");
			String caption = rs.get("caption");
			// 正则表达式校验
			if (!validateInputString(dsId))
				throw new LuiRuntimeException("请使用规范的数据集编码重新创建!");
			boolean isExist = this.sourceView.getViewModels().getDataset(dsId) != null ? true : false;
			if (isExist) {
				throw new LuiRuntimeException("新建的数据集编码存在，请重新创建！");
			}
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			Dataset ds = new Dataset(dsId);
			ds.setCaption(caption);
			ds.setEdit(false);
			ds.setLazyLoad(true);
			ds.setWidget(this.sourceView);
			this.sourceView.getViewModels().addDataset(ds);
			RequestLifeCycleContext.get().setPhase(phase);
			// 触发前台通知eclipse进行状态控制
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
			addTreeNode(dsId, caption, TYPE_DATASET);
			AppContext ctx = AppSession.current().getAppContext();
			ctx.addExecScript("var obj ={widgetid :'" + this.sourceView.getId() + "', type:'addDataset', uiid:'" + dsId + "'};");
			ctx.addExecScript("document.getElementById('iframe_tmp').contentWindow.datasetOper(obj,'addModel');");
		} else if (TYPE_COMBO.equals(this.params_type) && this.params_pid == null) {
			String url = LuiRuntimeContext.getRootPath() + "/core/file.jsp?pageId=file&isquick=false&closeDialog=true&uploadurl=/portal/pt/portalcombo/importCombo;jsessionid="
					+ LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
			AppSession.current().getAppContext().popOuterWindow(url, "模板上传", "502", "390");
		}
	}
	public void addModelByEdit(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		// 新建下拉数据集
		if (TYPE_COMBODATA.equals(this.params_type) && this.params_pid == null) {
			// AppSession.current().getAppContext().navgateTo("newcombocfg",
			// "新建枚举", "340", "400");
		} else if (TYPE_REFNODE.equals(this.params_type) && this.params_pid == null) {} else if (TYPE_DATASET.equals(this.params_type) && this.params_pid == null) {
			addds(this.sourceWin.getId(), this.sourceView.getId());
		} else if (TYPE_COMBO.equals(this.params_type) && this.params_pid == null) {}
	}
	public void addds(String srcWin, String srcView) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("dsId", "");
		paramMap.put(LuiRuntimeContext.DESIGNWINID, srcWin);
		paramMap.put("sourceView", srcView);
		// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
		paramMap.put("pi", UUID.randomUUID().toString());
		paramMap.put("datasetType", "source"); // 元数据
		paramMap.put("new_edit", "new");
		AppSession.current().getAppContext().navgateTo("dscfg", "新建元数据数据集", "850", "500", paramMap);
	}
	// 新建枚举
	public void addCombo(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		pubAddCombo(this.sourceWin.getId(), this.sourceView.getId());
	}
	public void pubAddCombo(String srcWin, String srcView) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("comboId", this.params_id);
		paramMap.put(LuiRuntimeContext.DESIGNWINID, srcWin);
		paramMap.put("sourceView", srcView);
		// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
		paramMap.put("pi", UUID.randomUUID().toString());
		AppSession.current().getAppContext().navgateTo("combodatacfg", "新建枚举", "340", "400", paramMap);
	}
	// 新建参照
	public void addRefNode(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		pubAddRef(this.sourceWin.getId(), this.sourceView.getId());
	}
	//新建表格参照模型
	public void addGridRef(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		pubAddGridRef(this.sourceWin.getId(), this.sourceView.getId(), "gridrefmodel", "新建表格参照模型", "560", "560", "grid");
	}
	//新建树参照模型
	public void addTreeRef(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		pubAddGridRef(this.sourceWin.getId(), this.sourceView.getId(), "gridrefmodel", "新建树参照模型", "560", "560", "tree");
	}
	//新建树表参照模型
	public void addTreeGridRef(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		pubAddGridRef(this.sourceWin.getId(), this.sourceView.getId(), "treegridmodel", "新建树表参照模型", "560", "500", "treegrid");
	}
	
	private void pubAddGridRef(String srcWin, String srcView, String nodeId, String title, String width, String height, String reftype) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(LuiRuntimeContext.DESIGNWINID, srcWin);
		paramMap.put("sourceView", srcView);
		paramMap.put("reftype", reftype);
		paramMap.put("pi", UUID.randomUUID().toString());
		AppSession.current().getAppContext().navgateTo(nodeId, title, width, height, paramMap);
	}
	public void pubAddRef(String srcWin, String srcView) {
		Map<String, String> paramMap = new HashMap<String, String>();
		// paramMap.put("refId", this.params_id);
		paramMap.put(LuiRuntimeContext.DESIGNWINID, srcWin);
		paramMap.put("sourceView", srcView);
		// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
		paramMap.put("pi", UUID.randomUUID().toString());
		AppSession.current().getAppContext().navgateTo("refcfg", "编辑数据集", "662", "405", paramMap);
	}
	public void addNormalDatasetByEdit(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		addNormalDs(this.sourceWin.getId(), this.sourceView.getId());
	}
	public void addNormalDs(String srcWin, String srcView) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("dsId", "");
		paramMap.put(LuiRuntimeContext.DESIGNWINID, srcWin);
		paramMap.put("sourceView", srcView);
		// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
		paramMap.put("pi", UUID.randomUUID().toString());
		paramMap.put("datasetType", "normal"); // 普通数据集
		AppSession.current().getAppContext().navgateTo("dscfg", "新建普通数据集", "850", "500", paramMap);
	}
	/**
	 * 编辑模型的后台处理类
	 * 
	 * @param e
	 */
	public void editModel(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		// 下拉数据集添加
		if (TYPE_COMBODATA.equals(this.params_type) && this.params_pid != null) {
			ComboData comboData = this.sourceView.getViewModels().getComboData(this.params_id);
			if (comboData == null) {
				throw new LuiRuntimeException("没有找到对应的下拉数据集!");
			}
			if (comboData instanceof MDComboDataConf) {
				throw new LuiRuntimeException("从元数据中引入的枚举不允许编辑!");
			}
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("comboId", this.params_id);
			paramMap.put(LuiRuntimeContext.DESIGNWINID, this.sourceWin.getId());
			paramMap.put("sourceView", this.sourceView.getId());
			// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
			paramMap.put("pi", UUID.randomUUID().toString());
			AppSession.current().getAppContext().navgateTo("combodatacfg", "编辑数据集", "340", "400", paramMap);
			// 弹出window
			// String url = LuiRuntimeContext.getRootPath() +
			// "/app/mockapp/combodatacfg?comboId=" + this.params_id + "&" +
			// PaHelper.SOURCE_WINDOW + "=" + this.sourceWin.getId() +
			// "&sourceView=" + this.sourceView.getId();
			// AppSession.current().getAppContext().popOuterWindow(url,
			// "下拉数据集编辑", "500", "405", AppContext.TYPE_DIALOG, false);
		}
		// 参照
		else if (TYPE_REFNODE.equals(this.params_type) && this.params_pid != null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("refId", this.params_id);
			paramMap.put(LuiRuntimeContext.DESIGNWINID, this.sourceWin.getId());
			paramMap.put("sourceView", this.sourceView.getId());
			// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
			paramMap.put("pi", UUID.randomUUID().toString());
			AppSession.current().getAppContext().navgateTo("refcfg", "编辑数据集", "662", "405", paramMap);
			// String url = LuiRuntimeContext.getRootPath() +
			// "/app/mockapp/refcfg?refId=" + this.params_id + "&" +
			// PaHelper.SOURCE_WINDOW + "=" + this.sourceWin.getId() +
			// "&sourceView=" + this.sourceView.getId();
			// AppSession.current().getAppContext().popOuterWindow(url, "编辑参照",
			// "662", "405", AppContext.TYPE_DIALOG, true);
		}
		// 数据集编辑：弹出对话框判断是不是自由表单态，如果是，则添加extendAttibute数据加载类的编辑项
		else if (TYPE_DATASET.equals(this.params_type) && this.params_pid != null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("dsId", this.params_id);
			paramMap.put(LuiRuntimeContext.DESIGNWINID, this.sourceWin.getId());
			paramMap.put("sourceView", this.sourceView.getId());
			// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
			paramMap.put("pi", UUID.randomUUID().toString());
			paramMap.put("new_edit", "edit");
			AppSession.current().getAppContext().navgateTo("dscfg", "编辑数据集", "850", "500", paramMap);
		} else if (TYPE_COMBO.equals(this.params_type) && this.params_pid != null) {
			// String url = LuiRuntimeContext.getRootPath() +
			// "/app/mockapp/combocfg?dsId=" + this.params_id + "&" +
			// PaHelper.SOURCE_WINDOW + "=" + this.sourceWin.getId() +
			// "&sourceView=" + this.sourceView.getId();
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("dsId", this.params_id);
			paramMap.put(LuiRuntimeContext.DESIGNWINID, this.sourceWin.getId());
			paramMap.put("sourceView", this.sourceView.getId());
			// 当在app中打开其他page的时候 如果没有发生改变不需要传入。page属性发生变化的时候需要（pi）属性
			paramMap.put("pi", UUID.randomUUID().toString());
			AppSession.current().getAppContext().navgateTo("combocfg", "编辑数据集", "498", "309", paramMap);
			// AppSession.current().getAppContext().popOuterWindow(url, "编辑组合",
			// "498", "309", AppContext.TYPE_DIALOG, true);
		}
		//参照模型
		else if (TYPE_REFMODEL.equals(this.params_type) && this.params_pid != null){
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("modelId", this.params_id);
			paramMap.put(LuiRuntimeContext.DESIGNWINID, this.sourceWin.getId());
			paramMap.put("sourceView", this.sourceView.getId());
			paramMap.put("pi", UUID.randomUUID().toString());
			paramMap.put("neworedit", "edit");
			if(this.params_uuid.endsWith("_grid")){
				paramMap.put("reftype", "grid");
				AppSession.current().getAppContext().navgateTo("gridrefmodel", "编辑表格参照模型", "560", "560", paramMap);
			}else if(this.params_uuid.endsWith("_tree")){
				paramMap.put("reftype", "tree");
				AppSession.current().getAppContext().navgateTo("gridrefmodel", "编辑树参照模型", "560", "560", paramMap);
			}else{//树表
				AppSession.current().getAppContext().navgateTo("treegridmodel", "编辑树表参照模型", "560", "500", paramMap);
			}
		}
		//AppSession.current().getAppContext().addExecScript("setEditorState();");
		LuiRuntimeContext.getWebContext().getPageWebSession().getOriginalParamMap();
	}
	public void copyModel(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		if (TYPE_DATASET.equals(this.params_type) && this.params_pid != null) {
			ViewPartMeta widget = this.sourceView;
			Dataset editDs = widget.getViewModels().getDataset(this.params_id);
			InputItem dsNameItem = new StringInputItem("dsName", "数据集名称：", true);
			InputItem dsCodeItem = new StringInputItem("dsCode", "数据集编码：", true);
			dsNameItem.setValue("");
			dsCodeItem.setValue("");
			InteractionUtil.showInputDialog("请输入下拉数据集名称和编码", new InputItem[] { dsNameItem, dsCodeItem });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String dsName = rs.get("dsName");
			String dsCode = rs.get("dsCode");
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			Dataset newDs = (Dataset) editDs.clone();
			newDs.setId(dsCode);
			newDs.setCaption(dsName);
			widget.getViewModels().addDataset(newDs);
			RequestLifeCycleContext.get().setPhase(phase);
			setNewDs2TreeDs(dsCode, dsName, newDs);
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
			/*
			 * AppLifeCycleContext.current().getApplicationContext().
			 * addExecScript ("var obj = {id : '" + this.params_id +
			 * "', type : '" + this.params_type + "', currentDropDsId : '"+
			 * nickName +"'}; \n"); AppLifeCycleContext
			 * .current().getApplicationContext().addExecScript (
			 * "operateModel(obj, 'copyModel'); \n");
			 */
		} else if (TYPE_COMBODATA.equals(this.params_type) && this.params_pid != null) {
			ViewPartMeta widget = this.sourceView;
			ComboData comboData = widget.getViewModels().getComboData(this.params_id);
			InputItem row = new StringInputItem("comboDataName", "下拉数据集名称：", true);
			InputItem coderow = new StringInputItem("comboDataCode", "下拉数据集编码：", true);
			row.setValue("");
			coderow.setValue("");
			InteractionUtil.showInputDialog("请输入下拉数据集名称和编码：", new InputItem[] { row, coderow });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String comboDataName = rs.get("comboDataName");
			String comboDataCode = rs.get("comboDataCode");
			if (comboDataName != null && comboDataName.equals(this.params_id)) {
				throw new LuiRuntimeException("复制名称不能重复！");
			}
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			ComboData newComboData = (ComboData) comboData.clone();
			newComboData.setId(comboDataCode);
			newComboData.setCaption(comboDataName);
			widget.getViewModels().addComboData(newComboData);
			RequestLifeCycleContext.get().setPhase(phase);
			Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("entityds");
			Row newTreeRNode = ds.getEmptyRow();
			newTreeRNode.setValue(ds.nameToIndex("id"), comboDataCode);
			newTreeRNode.setValue(ds.nameToIndex("pid"), this.params_pid);
			newTreeRNode.setValue(ds.nameToIndex("uuid"), comboDataCode);
			newTreeRNode.setValue(ds.nameToIndex("type"), TYPE_COMBODATA);
			newTreeRNode.setValue(ds.nameToIndex("name"), comboDataName);
			newTreeRNode.setValue(ds.nameToIndex("isdrag"), "0");
			ds.addRow(newTreeRNode);
			//AppSession.current().getAppContext().addExecScript("setEditorState();");
		} else if (TYPE_REFNODE.equals(this.params_type) && this.params_pid != null) {
			ViewPartMeta widget = this.sourceView;
			IRefNode refNode = widget.getViewModels().getRefNode(this.params_id);
			InputItem refId = new StringInputItem("refNodeId", "请输入参照ID：", true);
			InputItem refName = new StringInputItem("refName", "请输入参照名称：", true);
			refName.setValue("");
			refId.setValue("");
			ComboInputItem refType = new ComboInputItem("refType", "请选择参照类型：", true);
			InteractionUtil.showInputDialog("请输入参照信息", new InputItem[] { refId, refName });
			Map<String, String> rs = InteractionUtil.getInputDialogResult();
			String refNodeId = rs.get("refNodeId");
			String refLabel = rs.get("refName");
			if (refLabel != null && refLabel.equals(this.params_id)) {
				throw new LuiRuntimeException("复制名称不能重复！");
			}
			LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
			RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
			IRefNode newRefNode = (IRefNode) refNode.clone();
			newRefNode.setId(refNodeId);
			widget.getViewModels().addRefNode(newRefNode);
			RequestLifeCycleContext.get().setPhase(phase);
			Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("entityds");
			Row row = ds.getEmptyRow();
			row.setValue(ds.nameToIndex("id"), refNodeId);
			row.setValue(ds.nameToIndex("pid"), this.params_pid);
			row.setValue(ds.nameToIndex("uuid"), refNodeId);
			row.setValue(ds.nameToIndex("type"), TYPE_REFNODE);
			row.setValue(ds.nameToIndex("name"), refLabel);
			row.setValue(ds.nameToIndex("isdrag"), "0");
			ds.addRow(row);
		}
	}
	public void deleteModel(MouseEvent<MenuItem> e) {
		initParams();
		if (this.sourceView == null)
			return;
		ViewPartMeta widget = this.sourceView;
		if (TYPE_DATASET.equals(this.params_type) && this.params_pid != null) {
			if (InteractionUtil.showConfirmDialog("确认", "确认删除吗？")) {
				Dataset editDs = widget.getViewModels().getDataset(this.params_id);
				widget.getViewModels().removeDataset(this.params_id);
				removeDsFromTree(this.params_id);
				//AppSession.current().getAppContext().addExecScript("setEditorState();");
			}
		} else if (TYPE_COMBODATA.equals(this.params_type) && this.params_pid != null) {
			if (InteractionUtil.showConfirmDialog("确认", "确认删除吗？")) {
				widget.getViewModels().removeComboData(this.params_id);
				removeDsFromTree(this.params_id);
				//AppSession.current().getAppContext().addExecScript("setEditorState();");
			}
		} else if (TYPE_REFNODE.equals(this.params_type) && this.params_pid != null) {
			if (InteractionUtil.showConfirmDialog("确认", "确认删除吗？")) {
				widget.getViewModels().removeRefNode(this.params_id);
				removeDsFromTree(this.params_id);
				//AppSession.current().getAppContext().addExecScript("setEditorState();");
			}
		} 
	}
	/**
	 * 删除树节点
	 * 
	 * @param params_id
	 */
	private void removeDsFromTree(String params_id2) {
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("entityds");
		Row[] rows = ds.getCurrentPageSelectedRows();
		if (rows != null && rows.length > 0) {
			for (int i = 0; i < rows.length; i++) {
				Row row = rows[i];
				ds.removeRow(row);
			}
		}
	}
	private void setNewDs2TreeDs(String value, String name, Dataset newDs) {
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("entityds");
		Row row = addTreeNode(value, name, TYPE_DATASET);
		Field[] fss = newDs.getFields();
		if (fss != null) {
			for (int j = 0; j < fss.length; j++) {
				Field f = fss[j];
				String sourceField = f.getSourceField();
				// 过滤被带出字段
				if (sourceField != null && !sourceField.equals(""))
					continue;
				row = ds.getEmptyRow();
				row.setValue(ds.nameToIndex("uuid"), ds.getId() + "," + f.getExtSourceAttr() + "," + f.getId());
				row.setValue(ds.nameToIndex("pid"), value);
				row.setValue(ds.nameToIndex("type"), f.getDataType());
				row.setValue(ds.nameToIndex("id"), f.getId());
				row.setValue(ds.nameToIndex("name"), f.getText());
				row.setValue(ds.nameToIndex("dsid"), ds.getId());
				String extSource = f.getExtSource();
				if (extSource != null && extSource.equals(Field.SOURCE_MD))
					row.setValue(ds.nameToIndex("source"), "1");
				if (!(row == null || row.size() == 0))
					ds.addRow(row);
			}
		}
	}
	public static Map<String, String> showInputDialog(String value) {
		InputItem row = new StringInputItem("nickName", "请重新命名：", true);
		row.setValue(value);
		InteractionUtil.showInputDialog("请重命名：", new InputItem[] { row });
		Map<String, String> rs = InteractionUtil.getInputDialogResult();
		return rs;
	}
	/**
	 * 添加树节点
	 * 
	 * @param value
	 * @param type
	 * @return Row
	 */
	public Row addTreeNode(String value, String name, String type) {
		Dataset ds = AppSession.current().getViewContext().getView().getViewModels().getDataset("entityds");
		Row row = ds.getEmptyRow();
		row.setValue(ds.nameToIndex("id"), value);
		row.setValue(ds.nameToIndex("name"), name);
		if (type.equals(TYPE_COMBODATA) || type.equals(TYPE_REFNODE) || type.equals(TYPE_DATASET)) {
			row.setValue(ds.nameToIndex("pid"), this.params_uuid);
		} else {
			row.setValue(ds.nameToIndex("pid"), this.params_pid);
		}
		row.setValue(ds.nameToIndex("uuid"), value);
		row.setValue(ds.nameToIndex("type"), type);
		row.setValue(ds.nameToIndex("isdrag"), "0");
		ds.addRow(row);
		return row;
	}
	public boolean validateInputString(String inputStr) {
		String regex = "^[a-zA-Z0-9_]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
}
