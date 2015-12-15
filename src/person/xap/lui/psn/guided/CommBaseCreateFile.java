package xap.lui.psn.guided;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.cache.PaCache;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.layout.UISplitter;
import xap.lui.core.layout.UISplitterOne;
import xap.lui.core.layout.UISplitterTwo;
import xap.lui.core.layout.UIViewPart;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.PipeIn;
import xap.lui.core.model.PipeInItem;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewPartConfig;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.ViewState;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
import xap.lui.psn.designer.DesignerMainViewController;
import xap.lui.psn.designer.PaProjViewTreeController;
import xap.lui.psn.java.CreateJavaUtil;

public class CommBaseCreateFile {
	private static final String simpleQuery_PipeinId="simpleQuery_plugin";
	private PagePartMeta pagePartMeta;
	private String dsId="";
	private Map<String,ViewPartMeta> mapViewPartMeta=new HashMap<String,ViewPartMeta>();
	private Map<String,UIPartMeta> mapViewUiPartMeta=new HashMap<String,UIPartMeta>();
	public PagePartMeta getPagePartMeta() {
		return pagePartMeta;
	}
	public Map<String, ViewPartMeta> getMapViewPartMeta() {
		return mapViewPartMeta;
	}
	public Map<String, UIPartMeta> getMapViewUiPartMeta() {
		return mapViewUiPartMeta;
	}

	public CommBaseCreateFile(){}
	
	public CommBaseCreateFile(Map<String,String>map){
		String pageId=map.get("pageId");
		String viewId=map.get("viewId");
		String simpQueryNodeCode=map.get("simpQueryNodeCode");
		this.dsId=map.get("dsId");
		_new_page(pageId,simpQueryNodeCode);
		_new_view(viewId,simpQueryNodeCode);
	}
	
	
	
	//创建page.layout.xml 和 page.meta.xml
		public void _new_page(String pageId,String simpQueryNodeCode) {
			String nodePath=getNodePath(pageId);
			String javaFullClassName=	getJavaFullClassName("win");
			File nodefile = new File(nodePath);
			if (!nodefile.exists()) {
				nodefile.mkdirs();//创建目录
			}
			{
			    pagePartMeta = new PagePartMeta();
				pagePartMeta.setId(pageId);
				pagePartMeta.setController(javaFullClassName);
				addUIStates(pagePartMeta);//新建page后添加UIState状态
				IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
				String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
				ipaService.setOriPageMeta(pageId, sessionId, pagePartMeta);
			}
			{
			  UIPartMeta  pageUIPartMeta = new UIPartMeta();
				pageUIPartMeta.setId(pageId + "_um");
				IPaEditorService ipaService = new xap.lui.core.services.PaEditorServiceImpl();
				String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
				ipaService.setOriUIMeta(pageId, sessionId, pageUIPartMeta);
			}
			{
				try {
					String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
					String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
					String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
					File file = PaProjViewTreeController.createJavaFile(javaFullClassName,null);
					FileUtils.write(file, javaContent, "utf-8");
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
			if(StringUtils.isNotBlank(simpQueryNodeCode))
			{
				String nodeProContent = "nodecode="+simpQueryNodeCode;
				String nodeProPath=getNodePath(pageId)+"/node.properties";
				File file=new File(nodeProPath);
				if (!file.exists()) {
					try {
						file.createNewFile();//创建查询模版node.properties文件
					} catch (IOException e) {
						LuiLogger.error(e.getMessage(), e);
					}
				}
				try {
					FileUtils.write(file, nodeProContent, "utf-8");
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
				
			}
				
			DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
			PaCache.instance.pub("_pageId", pageId);
			Dataset projectDs=AppSession.current().getAppContext().getWindowContext("pa").getViewContext("project").getView().getViewModels().getDataset("projViewTreeDs");
			String projectName = (String) PaCache.getInstance().get("_projName");
			desinerMainViewController._add_page_as_row(nodefile, projectName + "_ui_wins", projectDs);
		}
		//新建view 和 layout 文件
		public void _new_view(String viewId,String simpQueryNodeCode) {
			String version=null;
			PaCache cache = PaCache.getInstance();
			cache.pub("_viewId", viewId);
			String pageId=(String)cache.get("_pageId");

			 String javaFullClassName = getJavaFullClassName(viewId);
			{
				ViewPartMeta viewPartMeta = new ViewPartMeta();
				viewPartMeta.setId(viewId);
				viewPartMeta.setController(javaFullClassName);
				mapViewPartMeta.put(viewId, viewPartMeta);
			
				ViewPartConfig viewPartConif = new ViewPartConfig();
				viewPartConif.setId(viewId);
				viewPartConif.setRefId(viewId);
				pagePartMeta.addViewPartConf(viewPartConif);
				if(StringUtils.equals("main",viewId)&&StringUtils.isNotBlank(simpQueryNodeCode)){
					ViewPartConfig viewPartConif1 = new ViewPartConfig();
					viewPartConif1.setId("simplequery");
					viewPartConif1.setRefId("../simplequery");
					pagePartMeta.addViewPartConf(viewPartConif1);
					pagePartMeta.addConnector(getConnector());//pagePartMeta添加连接器
					viewPartMeta.addPipeIns(getPipeIn());//viewPartMeta声明管道
				}
				pagePartMeta.addWidget(viewPartMeta);
				//新建视图后添加到UIState
				addViewStateToUIState(pagePartMeta, viewId);
			}
			{
				LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.design);
				UIPartMeta viewUIPartMeta = new UIPartMeta();
				viewUIPartMeta.setId(pageId + "_um");
				mapViewUiPartMeta.put(viewId, viewUIPartMeta);
				
				
				if(StringUtils.equals("main", viewId)){
					UIPartMeta	pageUIPartMeta = new UIPartMeta();
					if(!StringUtils.isBlank(simpQueryNodeCode)){
					
						UISplitter split=new UISplitter();
						split.setId("spliterlayout"+CommTool.getRndNum(4));
						split.setDivideSize("220");
						split.setBoundMode(1);
						
						UISplitterOne splitOne=new UISplitterOne();
						splitOne.setId("s1");
						
						UIViewPart uiViewPart = new UIViewPart();
						uiViewPart.setId(viewId);
						splitOne.setElement(uiViewPart);
						split.addPanel(splitOne);
						
						UISplitterTwo splitTwo=new UISplitterTwo();
						splitTwo.setId("s2");
						UIViewPart uiViewPart1 = new UIViewPart();
						uiViewPart1.setId("simplequery");
						splitTwo.setElement(uiViewPart1);
						split.addPanel(splitTwo);
						
						pageUIPartMeta.setElement(split);
					}else{
						UIViewPart uiViewPart = new UIViewPart();
						uiViewPart.setId(viewId);
						pageUIPartMeta.setElement(uiViewPart);
					}
					//uiViewPart.setUimeta(viewUIPartMeta);
					try {
						String xml1 = pageUIPartMeta.toXml();
						File file = this.createUIPartMetaFile(pageId, "");
						if (file.exists()) {
							FileUtils.write(file, xml1, "utf-8");
						}
					} catch (IOException e) {
						LuiLogger.error(e.getMessage(), e);
					}
					IPaEditorService ipaService = new PaEditorServiceImpl();
					String sessionId = (String) PaCache.getInstance().get("eclipse_sesionId");
					ipaService.setOriUIMeta(pageId, sessionId, pageUIPartMeta);
				}
				RequestLifeCycleContext.get().setPhase(phase);
			}
			{
				try {
					String javaClassName = PaProjViewTreeController.getJavaClassName(javaFullClassName);
					if(StringUtils.isBlank(version)){
						String javaPackageName = PaProjViewTreeController.getPackageName(javaFullClassName);
						String javaContent = CreateJavaUtil.getControllerClazz(javaPackageName, javaClassName);
						if(StringUtils.equals("main",viewId)&&StringUtils.isNotBlank(simpQueryNodeCode)){
							javaContent=getSimpleQueryConten(javaContent,  this.dsId);//控制类添加查询 代码
						}
						File file = PaProjViewTreeController.createJavaFile(javaFullClassName,null);
						FileUtils.write(file, javaContent, "utf-8");
					}
				} catch (IOException e) {
					LuiLogger.error(e.getMessage(), e);
				}
			}
			if(StringUtils.isBlank(version)){
				DesignerMainViewController desinerMainViewController = new DesignerMainViewController();
				Dataset projectDs=AppSession.current().getAppContext().getWindowContext("pa").getViewContext("project").getView().getViewModels().getDataset("projViewTreeDs");
				desinerMainViewController._add_view_as_row(pagePartMeta, pageId, projectDs);
			}
		}
		
		private String getSimpleQueryConten(String javaContent,String dsId){
			String tplName=	"SimpleQueryPluginTpl";
			String method="pluginsimpleQuery_plugin";
			LuiEventConf eventConf = new LuiEventConf();
				
			 eventConf.setEventType("Map");
			 
			 eventConf.setEventStatus(2);
			 eventConf.setModelCmd("clearList");
			 eventConf.setMethod("clearList");
			 javaContent = CreateJavaUtil.operateMethod(eventConf.getModelCmd(), javaContent, tplName, eventConf);
			 javaContent=javaContent.replace("Map", "Dataset gridDsList").replace("event", ",BaseDO[] Dos");
			 eventConf.setMethod(method);
			 eventConf.setModelCmd(method);
				LuiParameter para=new LuiParameter();
			 	para.setName("OperatorDs_Exattr");
			 	para.setValue(dsId);
			 eventConf.addExtendParam(para);
			 String allContent = CreateJavaUtil.operateMethod(eventConf.getModelCmd(), javaContent, tplName, eventConf);
			 allContent= allContent.replace("Map", "Map<Object,Object>").replace("event", "keys");
			
			return allContent;
		}
		

		
	private xap.lui.core.model.Connector getConnector(){
		xap.lui.core.model.Connector connector=new xap.lui.core.model.Connector();
		connector.setId("simpleQuery_Connector");
		connector.setPipeinId(simpleQuery_PipeinId);
		connector.setPipeoutId("qryout");
		connector.setSource("simplequery");
		connector.setTarget("main");
		Map<String,String> map=new HashMap<String, String>();
		map.put("inValue","row");
		connector.setMapping(map);
		return connector;
	}
		
	private PipeIn getPipeIn(){
		PipeIn pipeIn=new PipeIn();
		pipeIn.setId(simpleQuery_PipeinId);
		
		PipeInItem pipInItem=new PipeInItem();
		pipInItem.setId("row");
		pipeIn.addDescItem(pipInItem);
		return pipeIn;
	}
	
	//获取存储节点的路径	
	private String getNodePath(String pageId){
		PaCache cache = PaCache.getInstance();
		cache.pub("_pageId", pageId);
		String resourceFolder = (String) cache.get("_resourceFolder");
		resourceFolder = resourceFolder + "/lui/nodes/";
		String nodePath = resourceFolder + "/" + pageId;
		return nodePath;
	}
	
	//获取节点的控制类，type为 win 或者 view
	public String getJavaFullClassName(String winOrViewId)
	{
		String javaFullClassName="";
		String pageId=(String)PaCache.getInstance().get("_pageId");
		String pageIdToUp =CommTool.firstLetterToUpperCase(pageId);//将PageId的首字母大写
		//winOrViewId 如果不是win就是viewId
		if(StringUtils.equals(winOrViewId, "win")){
			javaFullClassName="xap.lui.core."+pageId+"."+pageIdToUp+"WinCtrl";
		}else{
			String viewIdToUp=CommTool.firstLetterToUpperCase(winOrViewId);
			javaFullClassName="xap.lui.core."+pageId+"."+pageIdToUp+viewIdToUp+"ViewCtrl";
		}
		return javaFullClassName;
	}
	
	
	
	//新建page后添加UIState状态,添加默认态（编辑态和浏览态）
	private void addUIStates(PagePartMeta pagePartMeta) {
		UIState uIState = new UIState();
		uIState.setId("editstate");
		uIState.setName("编辑态");
		pagePartMeta.addUIStates(uIState);
		
		UIState uiState2 = new UIState();
		uiState2.setId("viewstate");
		uiState2.setName("浏览态");
		pagePartMeta.addUIStates(uiState2);
	}
	//新建视图后添加到UIState
	private void addViewStateToUIState(PagePartMeta pagePartMeta, String viewId) {
		 List<UIState> uiStates = pagePartMeta.getuIStates();
		 if(uiStates != null && uiStates.size() > 0){
			 for(UIState uiState : uiStates){
				 ViewState viewState = new ViewState();
				 viewState.setViewId(viewId);
				 uiState.addViewState(viewState);
			 }
		 }
	}
	
	
	
	public File createPagePartMetaFile(String pageId) {
		PaCache.getInstance().pub("_pageId", pageId);
		String compMetaPath =PaCache.getEditorPageXmlPath();
		File file = new File(compMetaPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	public File createViewPartMetaFile() {
		String path=PaCache.getEditorCompMetaXmlPath();
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}
	public File createUIPartMetaFile(String pageId, String viewId) {
		PaCache cache=PaCache.getInstance();
		String orgViewId=(String)cache.get("_viewId")==null?"":(String)cache.get("_viewId");
		cache.pub("_pageId", pageId);
		cache.pub("_viewId", viewId);
		String uiMetaPath=PaCache.getEditorUIMetaXmlPath();
		cache.pub("_viewId", orgViewId);
		File file = new File(uiMetaPath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				LuiLogger.error(e.getMessage(), e);
			}
		}
		return file;
	}

	

}
