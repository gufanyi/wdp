package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;

/**
 * UIMeta的个性化和表单设置的信息类
 */
public class UIMetaInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;
	
	public UIMetaInfo(){
		super();
		
		ComboPropertyInfo flowmode = new ComboPropertyInfo();
		flowmode.setId("flow");
		flowmode.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		flowmode.setEditable(true);
		flowmode.setType(StringDataTypeConst.BOOLEAN);
		flowmode.setKeys(new String[]{"是", "否"});
		flowmode.setValues(new String[]{"Y", "N"});
		flowmode.setDsField("combo_ext1");
		flowmode.setVoField("flow");
		flowmode.setLabel("流式布局");
		list.add(flowmode);
		
		StringPropertyInfo includejs = new StringPropertyInfo();
		includejs.setId("includejs");
		includejs.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		includejs.setEditable(true);
		includejs.setDsField("string_ext6");
		includejs.setVoField("includejs");
		includejs.setLabel("引入脚本库");
		list.add(includejs);
		
		StringPropertyInfo includecss = new StringPropertyInfo();
		includecss.setId("includecss");
		includecss.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		includecss.setEditable(true);
		includecss.setDsField("string_ext7");
		includecss.setVoField("includecss");
		includecss.setLabel("引入样式表");
		list.add(includecss);
		
		/*
		IntegerPropertyInfo chart = new IntegerPropertyInfo();
		chart.setId("chart");
		chart.setVisible(true);
		chart.setEditable(true);
		chart.setType(StringDataTypeConst.INTEGER);
		chart.setDsField("integer_ext2");
		chart.setVoField("chart");
		chart.setLabel("chart");
		list.add(chart);

		IntegerPropertyInfo jsEditor = new IntegerPropertyInfo();
		jsEditor.setId("jsEditor");
		jsEditor.setVisible(true);
		jsEditor.setEditable(true);
		jsEditor.setType(StringDataTypeConst.INTEGER);
		jsEditor.setDsField("integer_ext3");
		jsEditor.setVoField("jseditor");
		jsEditor.setLabel("jsEditor");
		list.add(jsEditor);
		
		IntegerPropertyInfo jsExcel = new IntegerPropertyInfo();
		jsExcel.setId("jsExcel");
		jsExcel.setVisible(true);
		jsExcel.setEditable(true);
		jsExcel.setType(StringDataTypeConst.INTEGER);
		jsExcel.setDsField("string_ext9");
		jsExcel.setVoField("jsexcel");
		jsExcel.setLabel("jsExcel");
		list.add(jsExcel);
		
		StringPropertyInfo generateclass = new StringPropertyInfo();
		generateclass.setId("generateClass");
		generateclass.setVisible(true);
		generateclass.setEditable(true);
		generateclass.setDsField("string_ext10");
		generateclass.setVoField("generateclass");
		generateclass.setLabel("generateclass");
		list.add(generateclass);
		
		StringPropertyInfo tabBody = new StringPropertyInfo();
		tabBody.setId("tabBody");
		tabBody.setVisible(true);
		tabBody.setEditable(true);
		tabBody.setDsField("string_ext11");
		tabBody.setVoField("tabbody");
		tabBody.setLabel("tabBody");
		list.add(tabBody);
		
		StringPropertyInfo childtype = new StringPropertyInfo();
		childtype.setId("");
		childtype.setVisible(false);
		childtype.setEditable(true);
		childtype.setDsField("childtype");
		childtype.setLabel("子类型");
		childtype.setVoField("childtype");
		list.add(childtype);
		
		StringPropertyInfo childid = new StringPropertyInfo();
		childid.setId("");
		childid.setVisible(false);
		childid.setEditable(true);
		childid.setDsField("childid");
		childid.setLabel("子ID");
		childid.setVoField("childid");
		list.add(childid);
		*/
	}
}
