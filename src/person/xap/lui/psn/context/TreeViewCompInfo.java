/**
 * 
 */
package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;

/**
 * @author wupeng1
 * @version 6.0 2011-8-26
 * @since 1.6
 */
public class TreeViewCompInfo extends ControlInfo {
	
	private static final long serialVersionUID = 1L;
	private static final String[] KEYS = new String[]{"是", "否"};
	private static final String[] VALUES = new String[]{"Y", "N"};
	public TreeViewCompInfo(){
		super();
		
		ComboPropertyInfo vis = new ComboPropertyInfo();
		vis.setId("visible");
		vis.setVisible(true);
		vis.setEditable(true);
		vis.setType(StringDataTypeConst.bOOLEAN);
		vis.setKeys(KEYS);
		vis.setValues(VALUES);
		vis.setDsField("combo_ext1");
		vis.setLabel("是否可见");
		vis.setVoField("visible");
		list.add(vis);
		
		ComboPropertyInfo ena = new ComboPropertyInfo();
		ena.setId("enabled");
		ena.setVisible(true);
		ena.setEditable(true);
		ena.setType(StringDataTypeConst.bOOLEAN);
		ena.setKeys(KEYS);
		ena.setValues(VALUES);
		ena.setDsField("combo_ext2");
		ena.setLabel("是否可用");
		ena.setVoField("enabled");
		list.add(ena);
		
		ComboPropertyInfo isDrag = new ComboPropertyInfo();
		isDrag.setId("drag");
		isDrag.setVisible(true);
		isDrag.setEditable(true);
		isDrag.setType(StringDataTypeConst.bOOLEAN);
		isDrag.setKeys(KEYS);
		isDrag.setValues(VALUES);
		isDrag.setDsField("combo_ext3");
		isDrag.setLabel("可拖拽");
		isDrag.setVoField("drag");
		list.add(isDrag);
		
		ComboPropertyInfo isExpand = new ComboPropertyInfo();
		isExpand.setId("expand");
		isExpand.setVisible(true);
		isExpand.setEditable(true);
		isExpand.setType(StringDataTypeConst.bOOLEAN);
		isExpand.setKeys(KEYS);
		isExpand.setValues(VALUES);
		isExpand.setDsField("combo_ext4");
		isExpand.setLabel("展开根节点");
		isExpand.setVoField("expand");
		list.add(isExpand);

		ComboPropertyInfo isSelectRoot = new ComboPropertyInfo();
		isSelectRoot.setId("selectRoot");
		isSelectRoot.setVisible(true);
		isSelectRoot.setEditable(true);
		isSelectRoot.setType(StringDataTypeConst.bOOLEAN);
		isSelectRoot.setKeys(KEYS);
		isSelectRoot.setValues(VALUES);
		isSelectRoot.setDsField("combo_ext5");
		isSelectRoot.setLabel("选中根节点");
		isSelectRoot.setVoField("selectRoot");
		list.add(isSelectRoot);
		
		ComboPropertyInfo isShowCheckBox = new ComboPropertyInfo();
		isShowCheckBox.setId("showCheckBox");
		isShowCheckBox.setVisible(true);
		isShowCheckBox.setEditable(true);
		isShowCheckBox.setType(StringDataTypeConst.bOOLEAN);
		isShowCheckBox.setKeys(KEYS);
		isShowCheckBox.setValues(VALUES);
		isShowCheckBox.setDsField("combo_ext6");
		isShowCheckBox.setLabel("有多选框");
		isShowCheckBox.setVoField("showCheckBox");
		list.add(isShowCheckBox);
		
		ComboPropertyInfo isShowRoot = new ComboPropertyInfo();
		isShowRoot.setId("showRoot");
		isShowRoot.setVisible(true);
		isShowRoot.setEditable(true);
		isShowRoot.setType(StringDataTypeConst.bOOLEAN);
		isShowRoot.setKeys(KEYS);
		isShowRoot.setValues(VALUES);
		isShowRoot.setDsField("combo_ext7");
		isShowRoot.setLabel("显示根节点");
		isShowRoot.setVoField("showRoot");
		list.add(isShowRoot);
		

		
		StringPropertyInfo width = new StringPropertyInfo();
		width.setId("width");
		width.setVisible(true);
		width.setEditable(true);
		width.setDsField("string_ext6");
		width.setLabel("宽");
		width.setVoField("width");
		list.add(width);
		
		StringPropertyInfo height = new StringPropertyInfo();
		height.setId("height");
		height.setVisible(true);
		height.setEditable(true);
		height.setDsField("string_ext7");
		height.setLabel("高");
		height.setVoField("height");
		list.add(height);
		
		StringPropertyInfo text = new StringPropertyInfo();
		text.setId("text");
		text.setVisible(true);
		text.setEditable(true);
		text.setDsField("string_ext8");
		text.setLabel("根节点显示值");
		text.setVoField("itext");
		list.add(text);
		
		StringPropertyInfo contm = new StringPropertyInfo();
		contm.setId("contextMenu");
		contm.setVisible(true);
		contm.setEditable(true);
		contm.setDsField("string_ext9");
		contm.setLabel("弹出菜单");
		contm.setVoField("contextmenu");
		list.add(contm);
		
		StringPropertyInfo langdir = new StringPropertyInfo();
		langdir.setId("langDir");
		langdir.setVisible(true);
		langdir.setEditable(true);
		langdir.setDsField("string_ext10");
		langdir.setLabel("多语目录");
		langdir.setVoField("langdir");
		list.add(langdir);
		
		StringPropertyInfo i18nname = new StringPropertyInfo();
		i18nname.setId("i18nName");
		i18nname.setVisible(true);
		i18nname.setEditable(true);
		i18nname.setDsField("string_ext11");
		i18nname.setLabel("多语资源");
		i18nname.setVoField("i18nname");
		list.add(i18nname);
		
		StringPropertyInfo cap = new StringPropertyInfo();
		cap.setId("caption");
		cap.setVisible(true);
		cap.setEditable(true);
		cap.setDsField("string_ext12");
		cap.setLabel("标题");
		cap.setVoField("caption");
		list.add(cap);
		
		
		IntegerPropertyInfo openlevel = new IntegerPropertyInfo();
		openlevel.setId("openLevel");
		openlevel.setVisible(true);
		openlevel.setEditable(true);
		openlevel.setType(StringDataTypeConst.INT);
		openlevel.setDsField("integer_ext1");
		openlevel.setLabel("展开层级");
		openlevel.setVoField("openLevel");
		list.add(openlevel);
		
		IntegerPropertyInfo checkboxType = new IntegerPropertyInfo();
		checkboxType.setId("checkboxType");
		checkboxType.setVisible(true);
		checkboxType.setEditable(true);
		checkboxType.setType(StringDataTypeConst.INT);
		checkboxType.setDsField("integer_ext2");
		checkboxType.setLabel("选择模式");
		checkboxType.setVoField("checkboxType");
		list.add(checkboxType);
		
		SelfDefRefPropertyInfo refInfo = new SelfDefRefPropertyInfo();
		refInfo.setId("treeLevel1");
		refInfo.setVisible(true);
		refInfo.setEditable(true);
		refInfo.setType(StringDataTypeConst.STRING);
		refInfo.setDsField("ref_ext1");
		refInfo.setLabel("一级树设置");
//		refInfo.setUrl("/portal/app/mockapp/cdref?model=xap.lui.psn.dsmgr.MockFieldRefPageModel&amp;ctrl=xap.lui.psn.dsmgr.MockFieldRefController&amp;writeDs=relationsds&amp;writeField=detailds,detailkey");
		refInfo.setUrl("app/mockapp/treedatasetting?writeDs=ds_middle&writeField=ref_ext1");
		refInfo.setWidth("600");
		refInfo.setHeight("500");
		refInfo.setVoField("treeLevel1");
		list.add(refInfo);
		
		SelfDefRefPropertyInfo refInfo2 = new SelfDefRefPropertyInfo();
		refInfo2.setId("treeLevel2");
		refInfo2.setVisible(true);
		refInfo2.setEditable(true);
		refInfo2.setType(StringDataTypeConst.STRING);
		refInfo2.setDsField("ref_ext2");
		refInfo2.setLabel("二级树设置");
//		refInfo.setUrl("/portal/app/mockapp/cdref?model=xap.lui.psn.dsmgr.MockFieldRefPageModel&amp;ctrl=xap.lui.psn.dsmgr.MockFieldRefController&amp;writeDs=relationsds&amp;writeField=detailds,detailkey");
		refInfo2.setUrl("app/mockapp/treedatasetting?writeDs=ds_middle&writeField=ref_ext2");
		refInfo2.setWidth("600");
		refInfo2.setHeight("500");
		refInfo2.setVoField("treeLevel2");
		list.add(refInfo2);
		
//		StringPropertyInfo contm = new StringPropertyInfo();
//		contm.setId("contextMenu");
//		contm.setVisible(true);
//		contm.setEditable(true);
//		contm.setDsField("string_ext9");
//		contm.setLabel("弹出菜单");
//		contm.setVoField("contextmenu");
//		list.add(contm);
		
//		StringPropertyInfo classname = new StringPropertyInfo();
//		classname.setId("className");
//		classname.setVisible(true);
//		classname.setEditable(true);
//		classname.setDsField("string_ext10");
//		classname.setLabel("自定义主题");
//		classname.setVoField("classname");
//		list.add(classname);
		
//		
//		ComboPropertyInfo pos = new ComboPropertyInfo();
//		pos.setId("position");
//		pos.setVisible(true);
//		pos.setEditable(true);
//		pos.setType(StringDataTypeConst.STRING);
//		pos.setKeys(new String[]{"相对的", "绝对的"});
//		pos.setValues(new String[]{"relative", "positive"});
//		pos.setDsField("combo_ext5");
//		pos.setLabel("位置");
//		pos.setVoField("positions");
//		list.add(pos);
	
//		StringPropertyInfo top = new StringPropertyInfo();
//		top.setId("top");
//		top.setVisible(false);
//		top.setEditable(true);
//		top.setDsField("string_ext4");
//		top.setLabel("顶层距");
//		top.setVoField("itop");
//		list.add(top);
//		
//		StringPropertyInfo left = new StringPropertyInfo();
//		left.setId("left");
//		left.setVisible(false);
//		left.setEditable(true);
//		left.setDsField("string_ext5");
//		left.setLabel("左边距");
//		left.setVoField("ileft");
//		list.add(left);
		
	}

}
