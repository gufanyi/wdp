package xap.lui.psn.context;

import xap.lui.core.constant.StringDataTypeConst;
import xap.lui.core.layout.UISplitter;

/**
 * @author wupeng1
 * @version 6.0 2011-9-5
 * @since 1.6
 * @desc SpliterLayoutInfo:分隔容器信息
 */
public class SpliterLayoutInfo extends LayoutInfo {

	private static final long serialVersionUID = 1L;
	public SpliterLayoutInfo(){
		super();
		StringPropertyInfo divs = new StringPropertyInfo();
		divs.setId("divideSize");
		divs.setVisible(true);
		divs.setEditable(true);
		divs.setDsField("string_ext3");
		divs.setLabel("分隔尺寸");
		divs.setVoField("dividesize");
		list.add(divs);
		
		/*
		StringPropertyInfo splw = new StringPropertyInfo();
		splw.setId("spliterWidth");
		splw.setVisible(true);
		splw.setEditable(true);
		splw.setDsField("string_ext4");
		splw.setLabel("spliterWidth");
		splw.setVoField("spliterwidth");
		list.add(splw);
		
		IntegerPropertyInfo ori = new IntegerPropertyInfo();
		ori.setId("orientation");
		ori.setVisible(true);
		ori.setEditable(true);
		ori.setType(StringDataTypeConst.INTEGER);
		ori.setDsField("integer_ext1");
		ori.setLabel("orientation");
		ori.setVoField("orientation");
		list.add(ori);
 		*/
		
		ComboPropertyInfo bou = new ComboPropertyInfo();
		bou.setId("boundMode");
		bou.setKeys(new String[]{"像素","百分比"});
		bou.setValues(new String[]{UISplitter.BOUNDMODE_PX + "", UISplitter.BOUNDMODE_PERC + ""});
		bou.setDsField("combo_ext1");
		bou.setType(StringDataTypeConst.INTEGER);
		bou.setLabel("分隔单位");
		bou.setVoField("boundmode");
		bou.setVisible(true);
		bou.setEditable(true);
		list.add(bou);
		
		ComboPropertyInfo oneTouch = new ComboPropertyInfo();
		oneTouch.setId("oneTouch");
		oneTouch.setKeys(new String[]{"自动","固定"});
		oneTouch.setValues(new String[]{UISplitter.ONETOUCH_TRUE + "", UISplitter.ONETOUCH_FALSE + ""});
		oneTouch.setDsField("combo_ext2");
		oneTouch.setType(StringDataTypeConst.INTEGER);
		oneTouch.setLabel("自动隐藏");
		oneTouch.setVoField("onetouch");
		oneTouch.setVisible(true);
		oneTouch.setEditable(true);
		list.add(oneTouch);
		
		ComboPropertyInfo inverse = new ComboPropertyInfo();
		inverse.setId("inverse");
		inverse.setKeys(new String[]{"正向","反向"});
		inverse.setValues(new String[]{UISplitter.INVERSE_TRUE + "", UISplitter.INVERSE_FALSE + ""});
		inverse.setDsField("combo_ext3");
		inverse.setType(StringDataTypeConst.INTEGER);
		inverse.setLabel("计算依据");
		inverse.setVoField("inverse");
		inverse.setVisible(true);
		inverse.setEditable(true);
		list.add(inverse);
		
		/*
		IntegerPropertyInfo hidebar = new IntegerPropertyInfo();
		hidebar.setId("hideBar");
		hidebar.setVisible(true);
		hidebar.setEditable(true);
		hidebar.setType(StringDataTypeConst.INTEGER);
		hidebar.setDsField("integer_ext5");
		hidebar.setLabel("hideBar");
		hidebar.setVoField("hidebar");
		list.add(hidebar);
		*/
		
		ComboPropertyInfo hidedirection = new ComboPropertyInfo();
		hidedirection.setId("hideDirection");
		hidedirection.setKeys(new String[]{"左","右"});
		hidedirection.setValues(new String[]{UISplitter.HIDEDIRECTION_L + "", UISplitter.HIDEDIRECTION_R + ""});
		hidedirection.setDsField("combo_ext4");
		hidedirection.setType(StringDataTypeConst.INTEGER);
		hidedirection.setLabel("隐藏方向");
		hidedirection.setVoField("hidedirection");
		hidedirection.setVisible(true);
		hidedirection.setEditable(true);
		list.add(hidedirection);
		
		/*
		StringPropertyInfo parentid = new StringPropertyInfo();
		parentid.setId("");
		parentid.setVisible(false);
		parentid.setEditable(true);
		parentid.setDsField("parentid");
		parentid.setLabel("父ID");
		parentid.setVoField("parentid");
		list.add(parentid);
		*/
	}
}
