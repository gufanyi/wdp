package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.List;

import xap.lui.core.exception.LuiRuntimeException;


/**
 * 模态对话框配置类
 * @author dengjt
 *
 */
public class ModalDialogComp extends WebComp {
	
	private static final long serialVersionUID = 1L;
	public static final String WIDGET_NAME = "modaldialog";

	private List<WebComp> list = new ArrayList<WebComp>();
	
	private String i18nName = "Dialog";
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public void addComponent(WebComp comp){
		list.add(comp);
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}
	
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	
}
