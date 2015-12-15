package xap.lui.core.render;

import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UIElementFinder;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.layout.UITabRightPanel;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.util.UIElementFactory;


/**
 * FormComp动态添加字段命令
 * 
 * @author liujmc
 * @since uapweb6.12
 */
public class AddTabRightCmd extends AbstractRaCommand {

	public AddTabRightCmd(RaParameter rp) {
		super(rp);
	}

	@Override
	public void execute() {
		UITabComp tabComp = (UITabComp) UIElementFinder.findElementById(rp.getUiMeta(), rp.getUiId());
		if(tabComp.getRightPanel()==null) {
			UIElementFactory uf = new UIElementFactory();
			UITabRightPanel tabRightSpace = (UITabRightPanel)uf.createUIElement(LuiPageContext.SOURCE_TYPE_TABSPACE, rp.getWidgetId());
			String t = randomT(4);
			tabRightSpace.setId(LuiPageContext.SOURCE_TYPE_TABSPACE+1+t);
			tabComp.setRightPanel(tabRightSpace);
		}
	}
}
