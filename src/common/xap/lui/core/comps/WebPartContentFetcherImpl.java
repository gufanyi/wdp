package xap.lui.core.comps;

import xap.lui.core.layout.UIPartMeta;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.model.PagePartMeta;

public class WebPartContentFetcherImpl implements IWebPartContentFetcher {

	@Override
	public String fetchHtml(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		return "<div>Html Body ...</div>";
	}

	@Override
	public String fetchBodyScript(UIPartMeta um, PagePartMeta pm, ViewPartMeta view) {
		return "";
	}

}
