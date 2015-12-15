package xap.lui.psn.context;


/**
 * @author wupeng1
 * @version 6.0 2011-9-7
 * @since 1.6
 * @desc ShutterInfo:百叶窗布局信息
 */
public class ShutterInfo extends LayoutInfo {

	private static final long serialVersionUID = 1L;
	public ShutterInfo(){
		super();
		IntegerPropertyInfo curr = new IntegerPropertyInfo();
		curr.setId("currentItem");
		curr.setVisible(true);
		curr.setEditable(true);
		curr.setDsField("string_ext3");
		curr.setLabel("当前项");
		curr.setVoField("currentitem");
		list.add(curr);
		
		/*
		StringPropertyInfo className = new StringPropertyInfo();
		className.setId("className");
		className.setVisible(true);
		className.setEditable(true);
		className.setDsField("string_ext4");
		className.setLabel("自定义主题");
		className.setVoField("classname");
		list.add(className);
		*/
		
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
		
	}
	 
}
