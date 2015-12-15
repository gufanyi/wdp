package xap.lui.core.render;

import java.util.HashMap;
import java.util.Map;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.WebComp;
import xap.lui.core.constant.EditorTypeConst;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UIComponent;
import xap.lui.core.layout.UITextField;
import xap.lui.core.model.LuiPageContext;

import com.alibaba.fastjson.JSON;

/**
 * @author renxh Radio控件渲染器
 */
public class PCRadioCompRender extends PCTextCompRender {
	
	public PCRadioCompRender(RadioComp webEle) {
		super( webEle);
	}
	
	
	@Override
	public String createBody() {
		WebComp component = this.getWebElement();
		UIComponent uiComp = this.getUiElement();
		if (!(component instanceof RadioComp))
			throw new LuiRuntimeException("标签配置出错，"+this.getId()+"不是RadioComp类型！");

		RadioComp radio = (RadioComp) component;
		StringBuilder buf = new StringBuilder();
		buf.append("window.").append(this.getVarId());
		buf.append("= $(\"<div id='"+ this.getId() +"'></div>\").radio(\n");
		buf.append(generateParam(radio,uiComp));
		buf.append(").appendTo($('#"+ getDivId() +"')).radio('instance');\n");
		
		buf.append("var widget = pageUI.getViewPart('" + this.getCurrWidget().getId() + "');\n");
		buf.append("widget.addComponent('"+this.getId()+"'," + this.getVarId() + ");\n");
		buf.append(this.getVarId() + ".viewpart = widget;\n");
		return buf.toString();
	}
	
	
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TEXT;
	}
	
	@Override
	public String getRenderType(LuiElement ele) {
		return EditorTypeConst.RADIOCOMP;
	}
	
	public String getType(){
		return "radiotext";
	}
	
	
	/**
	 * 构造参数
	 * @param radio
	 * @return
	 */
	private String generateParam(RadioComp radio ,UIComponent uiComp) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("group", radio.getGroup());
		paramMap.put("value", radio.getValue());
		paramMap.put("text", radio.getText());
		paramMap.put("checked", radio.isChecked());
		paramMap.put("position", "relative");
		paramMap.put("className", uiComp.getClassName());
		if(uiComp.getAttribute(UITextField.IMG_SRC) != null){
			paramMap.put("imgsrc", uiComp.getAttribute(UITextField.IMG_SRC));
		}
		return JSON.toJSONString(paramMap);
	}

}
