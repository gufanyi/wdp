package xap.lui.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Parameter;
import xap.lui.core.exception.LuiInteractionException;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.AppSession;
import xap.lui.core.model.CtrlState;
import xap.lui.core.model.LuiAppUtil;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.UIState;
import xap.lui.core.model.ViewPartContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.ViewState;
import xap.lui.core.processor.AbstractRequestProcessor;
import xap.lui.core.serializer.AppContext2XmlSerializer;
import xap.lui.core.serializer.IObject2XmlSerializer;
import xap.lui.core.serializer.IXml2ObjectSerializer;
import xap.lui.core.serializer.Xml2AppContextSerializer;

public class AppRequestProcessor extends AbstractRequestProcessor<AppSession> {

	protected IXml2ObjectSerializer<AppSession> getRequestSerializer() {
		return new Xml2AppContextSerializer();
	}

	@Override
	protected IObject2XmlSerializer<AppSession> getResponseSerializer() {
		return new AppContext2XmlSerializer();
	}

	public AppSession processEvent() {
		AppSession ctx = getModelObject();
		AppSession.current(ctx);
		doProcessEvent(ctx);
		return ctx;
	}

	public void doProcessEvent(AppSession ctx) {
		try {
			List<Map<String, String>> gpList = ctx.getGroupParam();
			if (gpList != null) {
				int size = gpList.size();
				for (int i = 0; i < size; i++) {
					Map<String, String> paramMap = gpList.get(i);
					ctx.setParam(paramMap);
					processOneEvent(ctx);
				}
			} else
				processOneEvent(ctx);
		} catch (Exception e) {
			if (e instanceof InvocationTargetException) {
				Throwable t = getCause((InvocationTargetException) e);
				LuiLogger.error(e.getMessage(), e);
				if (t == null)
					t = e;

				else if (t instanceof LuiRuntimeException)
					throw (LuiRuntimeException) t;
				else
					throw new LuiRuntimeException(e);

			}

			if (e instanceof LuiInteractionException)
				throw (LuiInteractionException) e;
			LuiLogger.error(e.getMessage(), e);

			if (e instanceof LuiRuntimeException)
				throw (LuiRuntimeException) e;
			throw new LuiRuntimeException(e);
		}
	}

	private Throwable getCause(InvocationTargetException e) {
		if (e == null)
			return null;
		Throwable t = e.getCause();
		if (t == null)
			return e;
		if (t instanceof InvocationTargetException)
			return getCause((InvocationTargetException) t);
		else
			return t;
	}

	private void processOneEvent(AppSession ctx) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, Exception {

		String currentViewId = ctx.getParam().get(LuiPageContext.WIDGET_ID);
		ViewPartContext currentViewCtx = ctx.getWindowContext().getViewContext(currentViewId);
		ctx.getWindowContext().setCurrentViewContext(currentViewCtx);
		adjustProcessContext(ctx);

		String plugout = ctx.getParameter(AppSession.PLUGOUT_SIGN);
		String ctrlClazz = ctx.getParameter("clc");
		if (plugout != null && plugout.equals("1")) {
			new PlugEventHandler().doPlug();
			if (ctrlClazz != null) {
				try {
					eventInvoke(ctx);
				} catch (NoSuchMethodException e) {
					LuiLogger.error(e.getMessage());
				}
			}

		} else if (plugout != null && !plugout.equals("1")) {
			if (ctrlClazz != null)
				eventInvoke(ctx);
			new PlugEventHandler().doPlug();
		} else {
			eventInvoke(ctx);
		}
	}

	private LuiEventConf getCurrentEventConf(IEventSupport element, AppSession ctx) {
		LuiEventConf[] eleEvents = element.getEventConfs();
		if (eleEvents != null) {
			for (LuiEventConf eleEvent : eleEvents) {
				if (StringUtils.equals(eleEvent.getName(), ctx.getParameter("event_name"))) {
					return eleEvent;
				}
			}
		}
		return null;
	}

	// 目标状态在事件触发时生效
	private void setUIStates(LuiEventConf eventConf) {
		if (eventConf == null)
			return;
		String uistateId = eventConf.getuIStateId();
		if (StringUtils.isNotBlank(uistateId)) {
			PagePartMeta pagemeta = LuiAppUtil.getCntWindowCtx().getPagePartMeta();
			UIState uistate = pagemeta.getUIState(uistateId);
			if (uistate != null) {
				List<ViewState> viewStates = uistate.getViewStateList();
				if (viewStates != null && viewStates.size() > 0) {
					for (ViewState vs : viewStates) {
						ViewPartMeta view = pagemeta.getWidget(vs.getViewId());
						List<CtrlState> ctrlStates = vs.getCtrlStateList();
						if (ctrlStates != null && ctrlStates.size() > 0) {
							for (CtrlState cs : ctrlStates) {
								boolean visibleFlag = false;
								boolean enabledFlag = false;
 								WebComp webele = view.getViewComponents().getComponent(cs.getId());// 普通控件
								if (webele == null) {// 菜单
									webele = view.getViewMenus().getMenuBar(cs.getId());
									if (webele == null) {
										String pid = cs.getPid();
										MenubarComp menubar = view.getViewMenus().getMenuBar(pid);
										if (menubar != null){
											if(!menubar.isVisible())
												visibleFlag = true;
											if(!menubar.isEnabled())
												enabledFlag = true;
											webele = menubar.getElementById(cs.getId());
										}
									}
								}
								if (webele == null) {
									String pid = cs.getPid();
									WebComp tempcomp = view.getViewComponents().getComponent(pid);
									if(tempcomp instanceof ToolBarComp){// toolbaritem
										ToolBarComp toolbar = (ToolBarComp) tempcomp;
										if(toolbar != null){
											if(!toolbar.isVisible())
												visibleFlag = true;
											if(!toolbar.isEnabled())
												enabledFlag = true;
											webele = toolbar.getElementById(cs.getId());
										}
									}else if(tempcomp instanceof FormComp){//formelement
										FormComp form = (FormComp) tempcomp;
										if(form != null){
											if(!form.isVisible())
												visibleFlag = true;
											if(!form.isEnabled())
												enabledFlag = true;
											webele = form.getElementById(cs.getId());
										}
									}
								}
								if (webele == null)
									return;
								if(!visibleFlag)
									webele.setVisible(cs.isVisible());
								if(!enabledFlag)
									webele.setEnabled(cs.isEnabled());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param ctx
	 */
	private void adjustProcessContext(AppSession ctx) {
		String methodName = ctx.getParameter("m_n");
		if (methodName != null && methodName.equals("onDataLoad")) {
			String dsId = ctx.getParameter("source_id");
			Dataset ds = ctx.getViewContext().getView().getViewModels().getDataset(dsId);
			Parameter[] parameters = ds.getReqParameters().getParameters();
			for (int i = 0; i < parameters.length; i++) {
				String key = parameters[i].getName();
				String ctxValue = ctx.getParameter(key);
				if (ctxValue != null)
					parameters[i].setValue(ctxValue);
			}
		}
	}

	private void eventInvoke(AppSession ctx) throws Exception {
		AppEventFactory eventFactory = new AppEventFactory();
		Object eventHandler = eventFactory.getController();
		if (eventHandler != null) {
			AbstractServerEvent<?> serverEvent = eventFactory.getServerEvent();
			Method m = getMethod(eventHandler, serverEvent);
			if (m != null) {
				try {
					m.setAccessible(true);
					m.invoke(eventHandler, serverEvent);
					
					LuiEventConf eventConf = getCurrentEventConf((IEventSupport) serverEvent.getSource(), ctx);
					setUIStates(eventConf);
				} catch (Exception e) {
					e.printStackTrace();
					Throwable t = getCause((InvocationTargetException) e);
					if (t == null)
						t = e;
					if (t instanceof LuiInteractionException)
						throw (LuiInteractionException) t;
					else if (t instanceof LuiRuntimeException)
						throw (LuiRuntimeException) t;
					else
						throw new LuiRuntimeException(e);
				} finally {
				}
			}
		}
	}

	/**
	 * @param serverEvent
	 * @param eventHandler
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	protected Method getMethod(Object eventHandler, AbstractServerEvent<?> serverEvent) throws SecurityException, NoSuchMethodException {
		String methodName = AppSession.current().getParameter(AppSession.METHOD_NAME);
		if (methodName != null) {
			try {
				return eventHandler.getClass().getMethod(methodName, new Class[] { serverEvent.getClass() });
			} catch (Throwable e) {
				return null;
			}

		} else {
			return null;
		}
	}

	@Override
	protected String[] generateReturnString(Object result) {
		return super.generateReturnString(result);
	}

	protected void finallyProcess() {
		super.finallyProcess();
		AppSession.reset();
	}

}
