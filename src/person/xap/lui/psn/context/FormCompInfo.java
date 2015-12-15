package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.control.ModePhase;

/**
 * 表单配置信息类
 * @author wupeng1
 */
public class FormCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	
	public FormCompInfo(){
		super();
		
		StringPropertyInfo ds = new StringPropertyInfo();
		ds.setId("dataset");
		ds.setDsField("string_ext4");
		ds.setVisible(true);
		ds.setEditable(false);
		ds.setLabel("数据集");
		ds.setVoField("dataset");
		list.add(ds);
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setDsField("combo_ext1");
		vis.setKeys(new String[]{"是", "否"});
		vis.setValues(new String[]{"Y", "N"});
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setLabel("是否可见");
		vis.setVoField("visibles");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setKeys(new String[]{"是","否"});
		ena.setValues(new String[]{"Y", "N"});
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可编辑");
		ena.setVoField("enableds");
		list.add(ena);
		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(false);
		width.setEditable(true);
		width.setDsField("string_ext9");
		width.setLabel("宽度");
		width.setVoField("width");
		list.add(width);
		
		IntegerPropertyInfo rowht = new IntegerPropertyInfo();
		rowht.setId("rowHeight");
		rowht.setVisible(true);
		rowht.setEditable(true);
		rowht.setDsField("integer_ext4");
		rowht.setType(StringDataTypeConst.INT);
		rowht.setLabel("行高");
		rowht.setVoField("rowheight");
		list.add(rowht);
		
		IntegerPropertyInfo elewidth = new IntegerPropertyInfo();
		elewidth.setId("eleWidth");
		elewidth.setVisible(true);
		elewidth.setEditable(true);
		elewidth.setType(StringDataTypeConst.INT);
		elewidth.setDsField("integer_ext8");
		elewidth.setLabel("行宽");
		elewidth.setVoField("elewidth");
		list.add(elewidth);		
		
		IntegerPropertyInfo colcount = new IntegerPropertyInfo();
		colcount.setId("columnCount");
		colcount.setVisible(new ModePhase[]{ModePhase.eclipse, ModePhase.persona},true);
		colcount.setEditable(true);
		colcount.setDsField("integer_ext5");
		colcount.setType(StringDataTypeConst.INTEGER);
		colcount.setLabel("列数");
		colcount.setVoField("columncount");
		list.add(colcount);
		
		ComboPropertyInfo rendertype = new ComboPropertyInfo();
		rendertype.setId("renderType");
		rendertype.setVisible(true);
		rendertype.setEditable(true);		
		rendertype.setDsField("combo_ext5");
		rendertype.setKeys(new String[]{
			"固定布局居中",
			"流式布局",
			"固定提示布局",
			"自由表单",
			"固定布局右对齐",
			"只读布局"
		});
		rendertype.setValues(new String[]{"1","2","3","4","5","6"});
		
		rendertype.setType(StringDataTypeConst.INT);
		rendertype.setLabel("渲染类型");
		rendertype.setVoField("rendertype");
		list.add(rendertype);		
		
		IntegerPropertyInfo lmw = new IntegerPropertyInfo();
		lmw.setId("labelMinWidth");
		lmw.setVisible(new ModePhase[]{ModePhase.eclipse},true);
		lmw.setEditable(true);
		lmw.setType(StringDataTypeConst.INT);
		lmw.setDsField("integer_ext7");
		lmw.setLabel("最小标签宽度");
		lmw.setVoField("labelminwidth");
		list.add(lmw);
		
		StringPropertyInfo caption = new StringPropertyInfo();
		caption.setId("caption");
		caption.setVisible(true);
		caption.setEditable(true);
		caption.setDsField("string_ext5");
		caption.setLabel("标题");
		caption.setVoField("caption");
		list.add(caption);
		
		StringPropertyInfo cm = new StringPropertyInfo();
		cm.setId("contextMenu");
		cm.setVisible(false);
		cm.setEditable(true);
		cm.setDsField("string_ext6");
		cm.setLabel("弹出菜单");
		cm.setVoField("contextmenu");
		list.add(cm);
		
		StringPropertyInfo classname = new StringPropertyInfo();
		classname.setId("className");
		classname.setVisible(false);
		classname.setEditable(true);
		classname.setDsField("string_ext7");
		classname.setLabel("自定义主题");
		classname.setVoField("classname");
		list.add(classname);
		
		ComboPropertyInfo bkgrdColor = new ComboPropertyInfo();
		bkgrdColor.setId("backgroundColor");
		bkgrdColor.setKeys(getColorKeys());
		bkgrdColor.setValues(getColorValues());
		bkgrdColor.setDsField("combo_ext3");
		bkgrdColor.setType(StringDataTypeConst.STRING);
		bkgrdColor.setVisible(true);
		bkgrdColor.setEditable(true);
		bkgrdColor.setVoField("backgroundcolor");
		bkgrdColor.setLabel("表单背景");
		list.add(bkgrdColor);

//		ComboPropertyInfo labelPosition = new ComboPropertyInfo();
//		labelPosition.setId("labelPosition");
//		labelPosition.setKeys(new String[]{"左", "上"});
//		labelPosition.setValues(new String[]{"left", "top"});
//		labelPosition.setVisible(true);
//		labelPosition.setEditable(true);
//		labelPosition.setDsField("combo_ext4");
//		labelPosition.setType(StringDataTypeConst.STRING);
//		labelPosition.setDefaultValue("left");
//		labelPosition.setVoField("labelposition");
//		labelPosition.setLabel("显示值位置");
//		list.add(labelPosition);
		
		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("formelement");
		refInfo.setVisible(false);
		refInfo.setEditable(true);
		refInfo.setWidth("680");
		refInfo.setHeight("480");
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setLabel("表单项");
		refInfo.setVoField("sourcePackage");
		refInfo.setUrl("app/mockapp/fieldmgr");
		list.add(refInfo);
	}
}
