package xap.lui.core.render;
import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.comps.IDataBinding;
import xap.lui.core.comps.WebComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.dataset.Field;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UIMenubarComp;
import xap.lui.core.logger.LuiLogger;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.services.IPaEditorService;
import xap.lui.core.services.PaEditorServiceImpl;
/**
 * @author renxh 控件渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public abstract class UIComponentRender<T extends UIComponent, K extends WebComp> extends ShapeRender<T, K> {
	public static final String BEFORE_SCRIPT = "beforeScript";
	public static final String AFTER_SCRIPT = "afterScript";
	public static final String BEFORE_LOGIC_SCRIPT = "beforeLogicScript";
	// 组件所用的外部css，此css根据组件的实现不同，不一定能被接受。
	private String styleClass = "";
	public UIComponentRender(K webEle) {
		super(webEle);
	}
	/**
	 * 获得当前控件所绑定Dataset
	 * 
	 * @return
	 */
	protected Dataset getDataset() {
		WebComp comp = this.getWebElement();
		if (!(comp instanceof IDataBinding))
			throw new LuiRuntimeException("the component is not type of IDataBinding:" + getId());
		Dataset ds = getDatasetById(((IDataBinding) comp).getDataset(), comp);
		if (ds == null)
			throw new LuiRuntimeException("can not find dataset by assigned id:" + ((IDataBinding) comp).getDataset());
		return ds;
	}
	/**
	 * 2011-8-2 下午07:21:28 renxh des：根据id获得绑定的Dataset
	 * 
	 * @param id
	 * @return
	 */
	protected Dataset getDatasetById(String id) {
		ViewPartMeta widget = getCurrWidget();
		return widget.getViewModels().getDataset(id);
	}
	protected Dataset getDatasetById(String id, WebComp comp) {
		ViewPartMeta widget = comp.getWidget();
		if (widget == null) {
			widget = getCurrWidget();
		}
		return widget.getViewModels().getDataset(id);
	}
	/**
	 * 2011-8-2 下午07:21:44 renxh des：进行多语翻译,如果不能翻译,返回原defaultI18nName
	 * 
	 * @param i18nName
	 * @param fieldId
	 * @param defaultI18nName
	 * @param langDir
	 * @return
	 */
	protected String getFieldI18nName(String i18nName, String fieldId, String defaultI18nName, String langDir) {
		if (i18nName != null && !i18nName.equals("")) {
			if (i18nName.equals("$NULL$"))
				return "";
			return translate(i18nName, defaultI18nName == null ? i18nName : defaultI18nName, langDir);
		}
		Dataset ds = getDataset();
		if (ds == null)
			return defaultI18nName;
		if (fieldId != null) {
			int fldIndex = ds.nameToIndex(fieldId);
			if (fldIndex == -1) {
				LuiLogger.error("can not find the field:" + fieldId + ",dataset:" + ds.getId());
				return defaultI18nName;
			}
			Field field = ds.getField(fldIndex);
			i18nName = field.getI18nName();
			String text = field.getText();
			String defaultValue = text == null ? i18nName : text;
			if (i18nName == null || i18nName.equals(""))
				return defaultI18nName == null ? defaultValue : defaultI18nName;
			else {
				return translate(i18nName, defaultI18nName == null ? defaultValue : defaultI18nName, langDir);
			}
		} else
			return defaultI18nName;
	}
	public String getStyleClass() {
		return styleClass;
	}
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	public void setBeforeScript(String beforeScript) {
		setAttribute(BEFORE_SCRIPT, beforeScript);
	}
	public void setAfterScript(String afterScript) {
		setAttribute(AFTER_SCRIPT, afterScript);
	}
	public void setBeforeLogicScript(String beforeLogicScript) {
		setAttribute(BEFORE_LOGIC_SCRIPT, beforeLogicScript);
	}
	public boolean isMenu(UIComponent comp) {
		if (comp instanceof UIMenubarComp) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 删除控件，删除原始的和当前的
	 * 
	 * @param widgetId
	 * @param compId
	 * @param isMenu
	 */
	public void removeComponent(String widgetId, String compId, boolean isMenu) {
		String sessionId = LuiRuntimeContext.getWebContext().getRequest().getSession().getId();
		String pageId = LuiRuntimeContext.getWebContext().getPageWebSession().getPageId();
		IPaEditorService service =  new PaEditorServiceImpl();
		PagePartMeta oriPageMeta = (PagePartMeta) service.getOriPageMeta(pageId, sessionId);
		if(oriPageMeta!=null)	{
			return;
		}
		PagePartMeta currPageMeta = LuiRuntimeContext.getWebContext().getPageMeta();
		
		if (isMenu) {
//			if (widgetId == null) {
//				oriPageMeta.getViewMenus().removeMenuBar(compId);
//			} else {
//				ViewPartMeta widget = oriPageMeta.getWidget(widgetId);
//				if (widget != null) {
//					widget.getViewMenus().removeMenuBar(compId);
//				}
//			}
		} else {
//			if (oriPageMeta == null) {
//				oriPageMeta = this.getPageMeta();
//			}
//			ViewPartMeta widget = oriPageMeta.getWidget(widgetId);
//			if (widget != null) {
//				widget.getViewComponents().removeComponent(compId);
//			}
			ViewPartMeta widget = currPageMeta.getWidget(widgetId);
			if (widget != null) {
				widget.getViewComponents().removeComponent(compId);
			}
		}
	}

}
