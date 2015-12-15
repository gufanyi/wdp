package xap.lui.core.xml;



public class ComponetTypeMap extends CommonTypeMap {
	private static Object oTypeMap[][] = {{
			String.class, "String" },{
			Integer.class, "INTEGER" }, {
			TTVo.class, "TTVO" }, {
			TTVo[].class, "TTVO数组" }, };
	private static Object oTypeToTag[][] = { { "F1", "字段1" }, { "F2", "字段2" },};
	private static CommonClassTypeTagMap cct;
	private static ComponetTypeMap ctm = new ComponetTypeMap();
	static {
		ctm.setMapRelation(oTypeMap);
		cct = new CommonClassTypeTagMap();
		cct.setDefinClass(TTVo.class);
		cct.addFieldSetAsAttrib("F1");
		cct.setMapRelation(oTypeToTag);
		ctm.appendIClassTypeTargMap(cct);
	}

private ComponetTypeMap() {
	super();
}
public static ComponetTypeMap getShareInstance() {
	return ctm;
}
}
