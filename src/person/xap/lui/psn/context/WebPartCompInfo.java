package xap.lui.psn.context;


public class WebPartCompInfo extends ControlInfo {
	private static final long serialVersionUID = 1L;
	public WebPartCompInfo(){
		super();
		
		StringPropertyInfo contentFetcher = new StringPropertyInfo();
		contentFetcher.setId("contentFetcher");
		contentFetcher.setEditable(true);
		contentFetcher.setVisible(true);
		contentFetcher.setDsField("string_ext4");
		contentFetcher.setVoField("contentfetcher");
		contentFetcher.setLabel("内容获取类");
		list.add(contentFetcher);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setEditable(true);
		width.setVisible(true);
		width.setDsField("string_ext7");
		width.setVoField("width");
		width.setLabel("宽");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setEditable(true);
		height.setVisible(true);
		height.setDsField("string_ext8");
		height.setVoField("height");
		height.setLabel("高");
		list.add(height);
		
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setEditable(true);
		className.setVisible(true);
		className.setDsField("string_ext10");
		className.setVoField("classname");
		className.setLabel("主题");
		list.add(className);
	}
}
