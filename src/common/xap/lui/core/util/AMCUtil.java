/**
 * 
 */
package xap.lui.core.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.dataset.LuiParameter;
import xap.lui.core.listener.EventSubmitRule;
import xap.lui.core.listener.LuiEventConf;
import xap.lui.core.listener.WidgetRule;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.DataModels;
import xap.lui.core.model.ViewPartComps;
import xap.lui.core.model.ViewPartMenus;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.serializer.PersistenceUtil;
/**
 * @author chouhl
 *
 */
public class AMCUtil {
	public static Element getElementFromClass(Document doc, Object obj) {
		final String webElementTypeName = LuiElement.class.getName();
		Element rootNode = null;
		if (obj != null) {
			Class<?> clazz = obj.getClass();
			try {
				rootNode = doc.createElement(clazz.getField("TagName").get(obj).toString());
			} catch (Exception e1) {
				rootNode = doc.createElement(clazz.getSimpleName());
			}
			do {
				try {
					getElementFromClass(rootNode, clazz, obj);
				} catch (Exception e) {
					LuiLogger.error(e.getMessage(), e);
				}
				clazz = clazz.getSuperclass();
			} while (clazz != null && !webElementTypeName.equals(clazz.getName()));
		}
		return rootNode;
	}
	private static void getElementFromClass(Element ele, Class<?> clazz, Object obj) throws Exception {
		final String stringTypeName = "java.lang.String";
		String value = null;
		Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (stringTypeName.equals(field.getType().getName())) {
					field.setAccessible(true);
					value = (String) field.get(obj);
					if (value != null && value.trim().length() > 0) {
						ele.setAttribute(field.getName(), value.trim());
					}
				}
			}
		}
	}
	private static final String EVENT_TAG_EVENTS = "Events";
	private static final String EVENT_TAG_EVENT = "Event";
	private static final String EVENT_PROP_NAME = "name";
	private static final String EVENT_PROP_METHOD_NAME = "methodName";
	private static final String EVENT_PROP_CONTROLLER_CLAZZ = "controllerClazz";
	private static final String EVENT_PROP_JS_EVENT_CLAZZ = "eventType";
	private static final String EVENT_PROP_ON_SERVER = "onserver";
	private static final String EVENT_PROP_ASYNC = "async";
	private static final String EVENT_TAG_SUBMIT_RULE = "SubmitRule";
	private static final String EVENT_TAG_ACTION = "Action";
	private static final String SUBMIT_RULE_PROP_CARD_SUBMIT = "cardSubmit";
	private static final String SUBMIT_RULE_PROP_PAGEMETA = "pagemeta";
	public static void addEvents(Document doc, LuiEventConf[] eventConfs, Element parentNode) {
		if (eventConfs != null && eventConfs.length > 0) {
			Element eventsNode = doc.createElement(EVENT_TAG_EVENTS);
			parentNode.appendChild(eventsNode);
			for (LuiEventConf eventConf : eventConfs) {
				if (eventConf.getEventStatus() == LuiEventConf.DEL_STATUS) {
					continue;
				}
				Element eventNode = doc.createElement(EVENT_TAG_EVENT);
				eventsNode.appendChild(eventNode);
				if (eventConf.getName() != null && eventConf.getName().trim().length() > 0) {
					eventNode.setAttribute(EVENT_PROP_NAME, eventConf.getName().trim());
				}
				if (eventConf.getMethod() != null && eventConf.getMethod().trim().length() > 0) {
					eventNode.setAttribute(EVENT_PROP_METHOD_NAME, eventConf.getMethod().trim());
				}
				if (eventConf.getControllerClazz() != null && eventConf.getControllerClazz().trim().length() > 0) {
					eventNode.setAttribute(EVENT_PROP_CONTROLLER_CLAZZ, eventConf.getControllerClazz().trim());
				}
				if (eventConf.getEventType() != null && eventConf.getEventType().trim().length() > 0) {
					eventNode.setAttribute(EVENT_PROP_JS_EVENT_CLAZZ, eventConf.getEventType().trim());
				}
				eventNode.setAttribute(EVENT_PROP_ON_SERVER, String.valueOf(eventConf.isOnserver()));
				eventNode.setAttribute(EVENT_PROP_ASYNC, String.valueOf(eventConf.isAsync()));
				List<LuiParameter> extendParamList = eventConf.getExtendParamList();
				if (extendParamList != null && extendParamList.size() > 0) {
					PersistenceUtil.addExtendsParameters(doc, extendParamList.toArray(new LuiParameter[0]), eventNode);
				}
				EventSubmitRule submitRule = eventConf.getSubmitRule();
				if (submitRule != null) {
					Element submitRuleNode = doc.createElement(EVENT_TAG_SUBMIT_RULE);
					submitRuleNode.setAttribute(SUBMIT_RULE_PROP_CARD_SUBMIT, String.valueOf(submitRule.isCardSubmit()));
					eventNode.appendChild(submitRuleNode);
					// 父提交规则
					EventSubmitRule pSubmitRule = submitRule.getParentSubmitRule();
					if (pSubmitRule != null) {
						Element pSubmitRuleNode = doc.createElement(EVENT_TAG_SUBMIT_RULE);
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_PAGEMETA, pSubmitRule.getPagemeta());
						pSubmitRuleNode.setAttribute(SUBMIT_RULE_PROP_CARD_SUBMIT, String.valueOf(pSubmitRule.isCardSubmit()));
						submitRuleNode.appendChild(pSubmitRuleNode);
						LuiSet<WidgetRule> pWidgetRuleMap = pSubmitRule.getWidgetRules();
						if (!pWidgetRuleMap.isEmpty()) {
							PersistenceUtil.addSubmitContent(doc, pSubmitRuleNode, pWidgetRuleMap);
						}
					}
					LuiSet<WidgetRule> widgetRuleMap = submitRule.getWidgetRules();
					if (!widgetRuleMap.isEmpty()) {
						PersistenceUtil.addSubmitContent(doc, submitRuleNode, widgetRuleMap);
					}
					PersistenceUtil.addParameters(doc, submitRule.getParams(), submitRuleNode);
				}
				PersistenceUtil.addParameters(doc, eventConf.getParamList().toArray(new LuiParameter[0]), eventNode);
				Element actionNode = doc.createElement(EVENT_TAG_ACTION);
				eventNode.appendChild(actionNode);
				if (!eventConf.isOnserver()&&eventConf.getScript() != null && eventConf.getScript().trim().length() > 0) {
					actionNode.appendChild(doc.createCDATASection(eventConf.getScript().trim()));
				}
			}
		}
	}
	/**
	 * 获取View对象事件集合（包括View中各个控件）
	 * 
	 * @param viewConf
	 * @return
	 */
	public static LuiEventConf[] getAllEvents(ViewPartMeta viewConf) {
		// 事件数组集合
		LuiEventConf[] eventConfs = null;
		// Component
		ViewPartComps vc = viewConf.getViewComponents();
		// Model
		DataModels vm = viewConf.getViewModels();
		// 菜单
		ViewPartMenus vmenu = viewConf.getViewMenus();
		// 事件List集合
		List<LuiEventConf> ecList = new ArrayList<LuiEventConf>();
		// View事件集合
		LuiEventConf[] events = viewConf.getEventConfs();
		if (events != null && events.length > 0) {
			for (LuiEventConf event : events) {
				ecList.add(event);
			}
		}
		// Web元素集合
		LuiElement[] wcs = null;
		// 控件元素集合
		wcs = vc.getComps();
		// 控件元素事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// 下拉数据集 集合
		wcs = vm.getComboDatas();
		// 下拉数据集事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// 数据集 集合
		wcs = vm.getDatasets();
		// 数据集事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// 参照集合
		IRefNode[] refnodes = vm.getRefNodes();
		// 参照事件集合
		if (refnodes != null && refnodes.length > 0) {
			for (IRefNode rf : refnodes) {
				LuiElement wc = (LuiElement) rf;
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// 菜单集合
		MenubarComp[] mcs = vmenu.getMenuBars();
		// 菜单项集合
		wcs = null;
		if (mcs != null && mcs.length > 0) {
			List<LuiElement> weList = new ArrayList<LuiElement>();
			for (MenubarComp comp : mcs) {
				if (comp.getMenuList() != null) {
					List<MenuItem> miList = comp.getMenuList();
					for (MenuItem mi : miList) {
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new LuiElement[0]);
		}
		// 菜单项事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// 右键菜单集合
		ContextMenuComp[] cmcs = vmenu.getContextMenus();
		// 右键菜单项集合
		wcs = null;
		if (cmcs != null && cmcs.length > 0) {
			List<LuiElement> weList = new ArrayList<LuiElement>();
			for (ContextMenuComp comp : cmcs) {
				if (comp.getItemList() != null) {
					List<MenuItem> miList = comp.getItemList();
					for (MenuItem mi : miList) {
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new LuiElement[0]);
		}
		// 右键菜单项事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				LuiEventConf[] ecs = wc.getEventConfs();
				if (ecs != null && ecs.length > 0) {
					for (LuiEventConf ec : ecs) {
						ecList.add(ec);
					}
				}
			}
		}
		// View包含所有事件集合
		eventConfs = ecList.toArray(new LuiEventConf[0]);
		return eventConfs;
	}
	/**
	 * 移除View对象事件（包括View中各个控件）
	 * 
	 * @param viewConf
	 * @return
	 */
	public static void removeEvent(ViewPartMeta viewConf, String eventName, String methodName) {
		// Component
		ViewPartComps vc = viewConf.getViewComponents();
		// Model
		DataModels vm = viewConf.getViewModels();
		// 菜单
		ViewPartMenus vmenu = viewConf.getViewMenus();
		viewConf.removeEventConf(eventName, methodName);
		// Web元素集合
		LuiElement[] wcs = null;
		// 控件元素集合
		wcs = vc.getComps();
		// 控件元素事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
		// 下拉数据集 集合
		wcs = vm.getComboDatas();
		// 下拉数据集事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
		// 数据集 集合
		wcs = vm.getDatasets();
		// 数据集事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
		// 参照集合
		IRefNode[] refnodes = vm.getRefNodes();
		// 参照事件集合
		if (refnodes != null && refnodes.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
		// 菜单集合
		MenubarComp[] mcs = vmenu.getMenuBars();
		// 菜单项集合
		if (mcs != null && mcs.length > 0) {
			List<LuiElement> weList = new ArrayList<LuiElement>();
			for (MenubarComp comp : mcs) {
				if (comp.getMenuList() != null) {
					List<MenuItem> miList = comp.getMenuList();
					for (MenuItem mi : miList) {
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new LuiElement[0]);
		}
		// 菜单项事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
		// 右键菜单集合
		ContextMenuComp[] cmcs = vmenu.getContextMenus();
		// 右键菜单项集合
		if (cmcs != null && cmcs.length > 0) {
			List<LuiElement> weList = new ArrayList<LuiElement>();
			for (ContextMenuComp comp : cmcs) {
				if (comp.getItemList() != null) {
					List<MenuItem> miList = comp.getItemList();
					for (MenuItem mi : miList) {
						weList.addAll(getMenuItems(mi));
					}
				}
			}
			wcs = weList.toArray(new LuiElement[0]);
		}
		// 右键菜单项事件集合
		if (wcs != null && wcs.length > 0) {
			for (LuiElement wc : wcs) {
				wc.removeEventConf(eventName, methodName);
			}
		}
	}
	private static List<MenuItem> getMenuItems(MenuItem mi) {
		List<MenuItem> miList = new ArrayList<MenuItem>();
		if (mi != null) {
			miList.add(mi);
			List<MenuItem> childList = mi.getChildList();
			if (childList != null && childList.size() > 0) {
				for (MenuItem item : childList) {
					miList.add(item);
					miList.addAll(getMenuItems(item));
				}
			}
		}
		return miList;
	}
	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException {
		if (file != null && file.exists() && file.isFile()) {
			InputStreamReader isr = null;
			StringBuffer content = new StringBuffer();
			try {
				isr = new InputStreamReader(new FileInputStream(file));
				char[] chuf = new char[1024];
				int len = 0;
				while ((len = isr.read(chuf)) != -1) {
					content.append(String.copyValueOf(chuf, 0, len));
				}
			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (null != isr) {
					isr.close();
				}
			}
			return content.toString();
		}
		return null;
	}
	public static void main(String[] args) throws IOException {}
}
