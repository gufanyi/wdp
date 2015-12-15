package xap.lui.core.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import xap.lui.core.builder.SubmitRuleMergeUtil;
import xap.lui.core.command.LuiCommand;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.pluginout.IPlugoutType;
import xap.lui.core.pluginout.PlugoutTypeFactory;


public class LuiPlugoutCmd extends LuiCommand {
	private String plugoutId;
	private String widgetId;
	private String sourceWindowId;
//	private String sourceWindowId = getLifeCycleContext().getWindowContext().getId();
	private Map<String, Object> paramMap;
	private boolean isBeforeExec = false;
	
	/**
	 * plugout调用
	 * 
	 */
	public LuiPlugoutCmd(String widgetId, String plugoutId){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
	}
	
	public LuiPlugoutCmd(String widgetId, String plugoutId, boolean isBeforeExec){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.isBeforeExec = isBeforeExec;
	}
	
	/**
	 * plugout调用
	 * 
	 */
	public LuiPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
	}
	
	public LuiPlugoutCmd(String widgetId, String plugoutId, Map<String, Object> paramMap, boolean isBeforeExec){
		this.widgetId = widgetId;
		this.plugoutId = plugoutId;
		this.paramMap = paramMap;
		this.isBeforeExec = isBeforeExec;
	}
	
	@Override
	public void execute() {
		//WindowContext windowctx = getLifeCycleContext().getWindowContext();
		//windowctx.addPlug(widgetId + "_" + plugoutId, this.paramMap);
		
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		getLifeCycleContext().getAppContext().addPlug(windowctx.getId() + "_" + widgetId + "_" + plugoutId, this.paramMap);
	
		
		StringBuffer scriptBuf = new StringBuffer();
//		genPlugoutScript(scriptBuf);
		scriptBuf.append("$.pageutils.triggerPlugout('").append(this.widgetId).append("','").append(this.plugoutId).append("');\n");
		if (this.isBeforeExec)
			getLifeCycleContext().getAppContext().addBeforeExecScript(scriptBuf.toString());
		else
			getLifeCycleContext().getAppContext().addExecScript(scriptBuf.toString());
	}

	/**废弃
	 * 
	 * 生成前台plugout脚本
	 * @param scriptBuf
	 */
	@Deprecated
	private void genPlugoutScript(StringBuffer scriptBuf) {
		List<Connector> connList = getConnector();
		if (connList.size() == 0)
			return;
		
		//设置提交规则
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		PipeOut plugoutDesc = windowctx.getPagePartMeta().getWidget(widgetId).getPipeOut(plugoutId);
		EventSubmitRule sr = new EventSubmitRule();
		
		//plugout对应提交规则
		WidgetRule sourceWr = new WidgetRule();				
		sourceWr.setId(widgetId);
		List<PipeOutItem> descDescList = plugoutDesc.getItemList();
		for (PipeOutItem item : descDescList){
			String type = item.getType();
//			type = type.split("\\.")[1];
			String source = item.getSource();
			IPlugoutType plugoutType = PlugoutTypeFactory.getPlugoutType(type);
			if (plugoutType != null)
				plugoutType.buildSourceWidgetRule(sourceWr, source);
		}
		sr.addWidgetRule(sourceWr);

		PlugEventAdjuster plugEventAdjuster = new PlugEventAdjuster();
		//plugin对应提交规则
		for (Connector conn : connList){
			String targetWindowId = conn.getTargetWindow();
			String targetWidgetId = conn.getTarget();
			String pluginId = conn.getPipeinId();
			
			PagePartMeta pageMeta = null;
			//同window内的conn
			if (targetWindowId == null || targetWindowId.equals(this.sourceWindowId)){
				pageMeta = getLifeCycleContext().getWindowContext().getPagePartMeta();
				ViewPartMeta targetWidget = pageMeta.getWidget(targetWidgetId);
				EventSubmitRule targetsubmitRule = plugEventAdjuster.getTargetWidgetSubmitRule(targetWidget, pluginId);
				if (targetsubmitRule != null){
//					sr.addWidgetRule(targetsubmitRule.getWidgetRule(targetWidgetId));
					SubmitRuleMergeUtil.mergeSubmitRule(sr, targetsubmitRule);
				}
			}
			//跨window的conn
			//TODO  目前跨window时，只支持 子window 到  父window的plug调用，即：targetWindow为sourceWindow的父
			else{
//				WindowContext targetWinCtx = getLifeCycleContext().getApplicationContext().getWindowContext(targetWindowId);
//				if (targetWinCtx == null)
//					continue;
//				pageMeta = targetWinCtx.getWindow();
				pageMeta = LuiRuntimeContext.getWebContext().getCrossPageMeta(targetWindowId);
				if (pageMeta == null)
					continue;
				ViewPartMeta targetWidget = pageMeta.getWidget(targetWidgetId);
				EventSubmitRule targetsubmitRule = plugEventAdjuster.getTargetWidgetSubmitRule(targetWidget, pluginId);
				if (targetsubmitRule == null){
					targetsubmitRule = new EventSubmitRule();
					WidgetRule wr = new WidgetRule();
					wr.setId(targetWidgetId);
					targetsubmitRule.addWidgetRule(wr);
				}
				if (sr.getParentSubmitRule() == null)
					sr.setParentSubmitRule(targetsubmitRule);
				else
					SubmitRuleMergeUtil.mergeSubmitRule(sr.getParentSubmitRule(), targetsubmitRule);
			}
		}
		
		//增加参数
		sr.addParam(new Parameter(AppSession.PLUGOUT_SIGN,"1"));
//		sr.addParam(new Parameter(AppLifeCycleContext.PLUGOUT_SOURCE_WINDOW,sourceWindowId));
		sr.addParam(new Parameter(AppSession.PLUGOUT_SOURCE,widgetId));
		sr.addParam(new Parameter(AppSession.PLUGOUT_ID,plugoutId));
		sr.addParam(new Parameter(AppSession.PLUGOUT_FROMSERVER,"1"));
		sr.addParam(new Parameter(LuiPageContext.WIDGET_ID,widgetId));

		//生成前台脚本
		scriptBuf.append(plugEventAdjuster.generateSubmitRuleScript(sr));
//		
//		{"key":"value", "key":"value"}
//		key : "key"
		StringBuffer paramBuff = new StringBuffer();
		paramBuff.append("{");
//		if (this.paramMap != null){
//			Iterator<String> it =  this.paramMap.keySet().iterator();
//			while (it.hasNext()){
//				String key = it.next();
//				Object value = this.paramMap.get(key);
//				paramBuff.append(key).append(":").append(value).append(",");
//			}
//		}
		
		windowctx.addPlug(widgetId + "_" + plugoutId, this.paramMap);
		
		String values = paramBuff.toString();
		if (values.endsWith(","))
			values = values.substring(0, values.length() - 1);
		values += "}";
		
//		paramBuff.append("}'");
		
		scriptBuf.append("$.pageutils.triggerPlugout(submitRule," + values + ");");
	}

	/**废弃
	 * 获取plug连接
	 * 
	 * @return
	 */
	@Deprecated
	private List<Connector> getConnector() {
		WindowContext windowctx = getLifeCycleContext().getWindowContext();
		List<Connector> connList = new ArrayList<Connector>();
		//取window内的conn
		Connector[] conns = windowctx.getPagePartMeta().getConnectors();
		for (int i = 0 ; i < conns.length; i ++){
			if (conns[i].getSource().equals(this.widgetId) && conns[i].getPipeoutId().equals(this.plugoutId)){
				connList.add(conns[i]); 
			}
		}
		//sourceWindowId 为null或者为当前window的id，只从当前window内取conn
//		if (this.sourceWindowId == null || this.sourceWindowId.equals(getLifeCycleContext().getWindowContext().getId()))
//			return connList;
		//取app上的conn
		List<Connector> appConnList = getLifeCycleContext().getAppContext().getApplication().getConnectorList();
		if (appConnList == null)
			return connList;
		for (Connector c : appConnList){
			if (c.getSourceWindow().equals(this.sourceWindowId) && c.getSource().equals(this.widgetId) && c.getPipeoutId().equals(this.plugoutId)){
				connList.add(c);
			}
		}
		return connList;
	}

}
