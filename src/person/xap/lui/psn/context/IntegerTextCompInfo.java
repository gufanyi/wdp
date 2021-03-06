package xap.lui.psn.context;


public class IntegerTextCompInfo extends TextCompInfo {

	private static final long serialVersionUID = 1L;
	public IntegerTextCompInfo(){
		super();

		StringPropertyInfo precisions = new StringPropertyInfo();
		precisions.setId("precision");
		precisions.setEditable(true);
		precisions.setVisible(true);
		precisions.setDsField("string_ext18");
		precisions.setLabel("精度");
		precisions.setVoField("precisions");
		list.add(precisions);
	}
}
