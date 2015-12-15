package xap.lui.core.comps;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

public interface IWebPartContentFetcher {
	/**
	 * 根据分组获取页面元素
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchHtml(UIPartMeta um, PagePartMeta pm, ViewPartMeta view);
	/**
	 * 获取bodyScript脚本
	 * @param um
	 * @param pm
	 * @param view
	 * @param hroup
	 * @return
	 */
	public String fetchBodyScript(UIPartMeta um, PagePartMeta pm, ViewPartMeta view);
}
