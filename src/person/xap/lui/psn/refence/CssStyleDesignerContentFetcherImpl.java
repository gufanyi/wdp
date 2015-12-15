package xap.lui.psn.refence;

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import xap.lui.core.common.LuiRuntimeContext;
import xap.lui.core.common.LuiWebSession;
import xap.lui.core.comps.IWebPartContentFetcher;
import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;

@SuppressWarnings("restriction")
public class CssStyleDesignerContentFetcherImpl implements IWebPartContentFetcher {

	/**
	 * 获取页面内容
	 */
	@Override
	public String fetchHtml(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String paramValue = "";
		String refValue = session.getOriginalParameter("param");
		if (refValue != null && !StringUtils.isBlank(refValue) && !"null".equals(refValue)) {
			paramValue = refValue;
			paramValue = URLEncoder.encode(paramValue);
		}
		return "\"<iframe id='csseditor' style='width:540px;height:320px;overflow:auto;border:1px solid #EEEEEE;' src='/portal/lui/nodes/pa/csseditor.html'></iframe>\"";
	}

	/**
	 * 生成页面的执行脚本
	 */
	@Override
	public String fetchBodyScript(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		LuiWebSession session = LuiRuntimeContext.getWebContext().getPageWebSession();
		String refValue = session.getOriginalParameter("param");
		StringBuffer scriptBuffer = new StringBuffer();
		if (refValue != null && !StringUtils.isBlank(refValue) && !"null".equals(refValue)) {
			// 设置初始值等可在此实现
			scriptBuffer.append("var initValue = '" + refValue.trim() + "';");
			scriptBuffer.append("var editorWin = document.getElementById('csseditor').contentWindow;\n");
			scriptBuffer.append("var valueArray = initValue.trim().split(';');");
			scriptBuffer.append("for(var i=0;i<valueArray.length;i++){" + "    var cssAttr = valueArray[i];" + "    var attrArray = cssAttr.trim().split(':');"
					+ "    if(attrArray[1]!=null && typeof(attrArray[1])!='undefined'){" + "        editorWin.setTargetStyle(attrArray[0].trim(),attrArray[1].trim());" + "	 }" + "}");
		}
		return scriptBuffer.toString();
	}

}
