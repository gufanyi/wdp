package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.psn.refence.MockRefDataRefController;
import xap.lui.psn.refence.MockRefPageModel;

/**
 * @author wupeng1
 * @version 6.0 2011-10-10
 * @since 1.6
 */
public class ReferenceCompInfo extends TextCompInfo {
	
	private static final long serialVersionUID = 1L;
	
	public ReferenceCompInfo(){
		super();
		
		//移除最大最小值
		removePropertyInfoById("maxValue");
		removePropertyInfoById("minValue");
		
		StringPropertyInfo editorType = new StringPropertyInfo();
		editorType.setId("editorType");
		editorType.setEditable(true);
		editorType.setVisible(false);
		editorType.setDsField("string_ext15");
		editorType.setLabel("编辑类型");
		editorType.setVoField("editortype");
		list.add(editorType);
		
		SelfDefRefPropertyInfo refcode = new SelfDefRefPropertyInfo();
		refcode.setId("refcode");
		refcode.setEditable(true);
		refcode.setVisible(true);
		refcode.setType(StringDataTypeConst.STRING);
		refcode.setHeight("700");
		refcode.setWidth("900");
		refcode.setDsField("ref_ext1");
		refcode.setLabel("引用");
		refcode.setUrl("app/mockapp/cdref?model="+MockRefPageModel.class.getName()+"&ctrl="+MockRefDataRefController.class.getName());
		refcode.setVoField("refcode");
		list.add(refcode);
		
		StringPropertyInfo parentId = new StringPropertyInfo();
		parentId.setId("");
		parentId.setEditable(true);
		parentId.setVisible(false);
		parentId.setDsField("parentid");
		parentId.setLabel("父ID");
		parentId.setVoField("parentid");
		list.add(parentId);
	}
}
