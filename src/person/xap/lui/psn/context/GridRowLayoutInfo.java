package xap.lui.psn.context;


public class GridRowLayoutInfo extends LayoutInfo {
	private static final long serialVersionUID = 1L;

	public GridRowLayoutInfo(){
		super();
		StringPropertyInfo rowHeight = new StringPropertyInfo();
		rowHeight.setId("rowHeight");
		rowHeight.setVisible(true);
		rowHeight.setEditable(true);
		rowHeight.setDsField("string_ext4");
		rowHeight.setLabel("行高");
		rowHeight.setVoField("rowheight");
		list.add(rowHeight);
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父Id");
		parentid.setVoField("parentid");
		list.add(parentid);
	}
}
