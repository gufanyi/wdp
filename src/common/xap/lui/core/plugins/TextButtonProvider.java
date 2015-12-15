package xap.lui.core.plugins;

import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.render.UINormalComponentRender;

public class TextButtonProvider implements ILuiPaltformExtProvier {

	@Override
	public Class<? extends WebComp> getWebCompClazz() {
		return TextButtonComp.class;
	}

	@Override
	public Class<? extends UIComponent> getUICompClazz() {
		return UITextButton.class;
	}

	@Override
	public Class<? extends UINormalComponentRender> getRenderClazz() {
		return PCTextButtonCompRender.class;
	}

	@Override
	public String getCompTypeName() {
		return "TextButton";
	}


	@Override
	public String getLayoutTagName() {
		return "TextButton";
	}

	@Override
	public Class<? extends BaseContext> getContextClazz() {
		// TODO Auto-generated method stub
		return TextButtonContext.class;
	}

	@Override
	public String[] getResoucesName() {
		return new String[]{"xap/lui/core/plugins/TextButtonComp.js"};
	}

	@Override
	public String getImgIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return "文本按钮";
	}

	@Override
	public String getSourceType() {
		return "textbutton";
	}

}
