package xap.lui.core.comps;

import xap.lui.core.dataset.ComboData;

/**
 * List控件后台配置类
 *
 */
public class ListComp extends WebComp implements IDataBinding {

	private static final long serialVersionUID = -5601349522686730685L;
	private String dataset;
	private ComboData comboData;
	// 要删除HTML代码项的位置
	private int delOptionItemHtmlIndex;
	// 要删除项的位置
	private int delOptionItemIndex;
	// 要删除项的位置集合
	private int[] delOptionItemIndexs;
	
	public String getDataset() {
		return dataset;
	}


	public void setDataset(String dataset) {
		this.dataset = dataset;
		setCtxChanged(true);
	}


	public ComboData getComboData() {
		return comboData;
	}


	public void setComboData(ComboData comboData) {
		this.comboData = comboData;
		setCtxChanged(true);
	}


	public int getDelOptionItemHtmlIndex() {
		return delOptionItemHtmlIndex;
	}


	public void setDelOptionItemHtmlIndex(int delOptionItemHtmlIndex) {
		this.delOptionItemHtmlIndex = delOptionItemHtmlIndex;
		setCtxChanged(true);
	}


	public int getDelOptionItemIndex() {
		return delOptionItemIndex;
	}


	public void setDelOptionItemIndex(int delOptionItemIndex) {
		this.delOptionItemIndex = delOptionItemIndex;
		setCtxChanged(true);
	}


	public int[] getDelOptionItemIndexs() {
		return delOptionItemIndexs;
	}


	public void setDelOptionItemIndexs(int[] delOptionItemIndexs) {
		this.delOptionItemIndexs = delOptionItemIndexs;
		setCtxChanged(true);
	}

}
