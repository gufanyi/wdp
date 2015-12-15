package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.layout.UICanvas;

/**
 * 背景容器信息
 * @author liujmc
 */
public class CanvasInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;
	
	public CanvasInfo(){
		super();
		
		removePropertyInfoById("className");
		
		ComboPropertyInfo className = new ComboPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setKeys(new String[]{"无","左","右","全","中"});
		className.setValues(new String[]{UICanvas.NULLCANVAS, UICanvas.LEFTCANVAS, UICanvas.RIGHTCANVAS, UICanvas.FULLCANVAS, UICanvas.CENTERCANVAS});
		className.setType(StringDataTypeConst.STRING);
		className.setDsField("combo_ext1");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		
		StringPropertyInfo title = new StringPropertyInfo();
		title.setId("title");
		title.setVisible(true);
		title.setEditable(true);
		title.setDsField("string_ext3");
		title.setLabel("标题");
		title.setVoField("title");
		list.add(title);
		
		StringPropertyInfo i18n = new StringPropertyInfo();
		i18n.setId("i18nName");
		i18n.setVisible(false);
		i18n.setEditable(true);
		i18n.setDsField("string_ext4");
		i18n.setLabel("多语显示值");
		i18n.setVoField("i18nname");
		list.add(i18n);
		
		/*		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext25");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		*/

	}
 
}
