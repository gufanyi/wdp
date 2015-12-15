package xap.lui.core.comps;
import org.apache.commons.lang.StringUtils;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.refrence.GenericRefNode;
import xap.lui.core.refrence.IRefModel;
import xap.lui.core.refrence.IRefNode;
import xap.lui.core.refrence.RefSelfUtil;
public class RefHelper {
	/**
	 * 根据key填充参照对应的显示值
	 * 
	 * @param widget
	 * @param ref
	 */
	public static void fetchRefShowValue(ViewPartMeta widget, ReferenceComp ref) {
		String value = ref.getValue();
		if (value == null || value.equals("")) {
			ref.setShowValue(null);
			return;
		}
		String showValue = ref.getShowValue();
		if (StringUtils.isNotBlank(showValue)) {
			return;
		}
		String refCode = ref.getRefcode();
		if (refCode != null) {
			IRefNode refNo = (IRefNode) widget.getViewModels().getRefNode(refCode);
			if (refNo instanceof GenericRefNode) {
				GenericRefNode refnode = (GenericRefNode) widget.getViewModels().getRefNode(refCode);
				IRefModel refmodel = RefSelfUtil.getRefModel(refnode);
				showValue = refmodel.value2ShowValue(value);
				ref.setShowValue(showValue);
			}
		}
	}
}
