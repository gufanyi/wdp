package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.ImageComp;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UIImageComp;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh 图片控件渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCImageCompRender extends UINormalComponentRender<UIImageComp, ImageComp> {

	public PCImageCompRender( ImageComp webEle) {
		super( webEle);
	}


	public String createBody() {

		StringBuilder buf = new StringBuilder();
		ImageComp image = this.getWebElement();
		String id = getVarId();
		buf.append("window.").append(id).append(" = $(\"<div id='" + image.getId() + "'></div>\")");
		buf.append(".appendTo('#"+getDivId()+"')");
		buf.append(".image(\n");
		buf.append(generateBtnParam(image));
		buf.append("\n).image('instance');\n");

		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent('" + image.getId() + "'," + id + ");\n");
		return buf.toString();
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_IMAGECOMP;
	}
	
	/**
	 * 构造图片参数
	 * @param button
	 * @return
	 */
	private String generateBtnParam(ImageComp image) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("refImg1", image.getRealImage1());
		if (image.getImage2() != null) {
			paramMap.put("refImg2", image.getRealImage2());
		}
		paramMap.put("alt", image.getAlt());
		paramMap.put("disabled", !image.isEnabled());
		paramMap.put("visible", image.isVisible());
		paramMap.put("boolFloatRight", image.isFloatRight());
		paramMap.put("boolFloatLeft", image.isFloatLeft());
		paramMap.put("maxShow", image.isMaxShow());
		paramMap.put("width", this.getUiElement().getWidth());
		paramMap.put("height", this.getUiElement().getHeight());
		if (image.getImageInact() != null && !"".equals(image.getImageInact())){
			paramMap.put("inactiveImg", image.getImageInact());
		}
		return JSON.toJSONString(paramMap);
	}

}
