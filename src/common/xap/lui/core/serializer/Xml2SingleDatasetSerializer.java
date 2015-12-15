package xap.lui.core.serializer;

import java.util.Map;

import xap.lui.core.dataset.Dataset;

public class Xml2SingleDatasetSerializer implements IXml2ObjectSerializer<Dataset> {

	public Dataset serialize(String xml, Map<String, Object> paramMap) {
		Dataset[] dss = new Xml2DatasetSerializer().serialize(xml, paramMap);
		return dss[0];
	}

}
