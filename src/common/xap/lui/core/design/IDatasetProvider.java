package xap.lui.core.design;

import java.util.List;
import java.util.Map;

import xap.lui.core.dataset.MdDataset;
import xap.lui.core.model.ViewPartMeta;


/**
 * 获取关于元数据的信息接口
 * @author zhangxya
 *
 */

public interface IDatasetProvider {

	
	/**
	 * 填充基于元数据的数据集
	 * @param mdds
	 * @return
	 */
	public MdDataset getMdDataset(MdDataset mdds,boolean needRefCode);
	
	public List  getRefMdDatasetList(MdDataset ds)  ;
}
