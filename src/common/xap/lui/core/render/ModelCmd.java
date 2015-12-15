package xap.lui.core.render;
import xap.lui.core.builder.LuiSet;
import xap.lui.core.cache.PaCache;
import xap.lui.core.dataset.ComboData;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.xml.StringUtils;
/**
 * 模型操作 dataset,refnode,datalist
 * 
 * @author li_xiaolei
 *
 */
public class ModelCmd extends AbstractRaCommand {
	public ModelCmd(RaParameter rp) {
		super(rp);
	}
	@Override
	public void execute() {
//		String type = rp.getType();
//		if(StringUtils.equals("addDataset", type)){
			Dataset ds = PaCache.getEditorViewPartMeta().getViewModels().getDataset(rp.getUiId());
			rp.getPageMeta().getWidget(rp.getWidgetId()).getViewModels().addDataset(ds);
			LuiSet<IRefNode> refnodes = rp.getPageMeta().getWidget(rp.getWidgetId()).getViewModels().getRefNodeList();
			for (IRefNode inner : refnodes) {
				rp.getPageMeta().getWidget(rp.getWidgetId()).getViewModels().addRefNode(inner);
			}
			LuiSet<ComboData> combodatas = PaCache.getEditorViewPartMeta().getViewModels().getComboDataList();
			for (ComboData inner: combodatas) {
				rp.getPageMeta().getWidget(rp.getWidgetId()).getViewModels().addComboData(inner);
			}
//		}else if(StringUtils.equals("addCombo", type)){
//			LuiSet<ComboData> combodatas = PaCache.getEditorViewPartMeta().getViewModels().getComboDataList();
//			for (ComboData inner: combodatas) {
//				rp.getPageMeta().getWidget(rp.getWidgetId()).getViewModels().addComboData(inner);
//			}
//		}
		
	}
}
