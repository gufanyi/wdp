package xap.lui.core.plugins;

import xap.lui.core.comps.WebComp;
import xap.lui.core.context.BaseContext;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.render.UINormalComponentRender;

public class GaugeCompProvider implements ILuiPaltformExtProvier {

	@Override
	public Class<? extends WebComp> getWebCompClazz() {
		return GaugeChartComp.class;
	}

	@Override
	public Class<? extends UIComponent> getUICompClazz() {
		return UIGaugeComp.class;
	}

	@Override
	public Class<? extends UINormalComponentRender> getRenderClazz() {
		return PCGaugeCompRender.class;
	}

	@Override
	public String getCompTypeName() {
		return "GaugeChart";
	}

	@Override
	public String getLayoutTagName() {
		// TODO Auto-generated method stub
		return "GaugeChart";
	}

	@Override
	public Class<? extends BaseContext> getContextClazz() {
		return null;
	}

	@Override
	public String[] getResoucesName() {
		return null;
	}

	@Override
	public String getImgIcon() {
		return null;
	}

	@Override
	public String getText() {
		return "仪表盘";
	}

	@Override
	public String getSourceType() {
		return "yibiaopan";
	}

}
