package xap.lui.core.render;

import java.util.List;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UICardPanel;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 卡片布局的panel渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCCardPanelRender<T extends UICardPanel, K extends LuiElement> extends UILayoutPanelRender<T, K> {
	public PCCardPanelRender(T uiEle, PCCardLayoutRender parentRender) {
		super(uiEle);
		this.parentRender = parentRender;
		int cardCount = parentRender.getCardCount();
		cardCount++;
		parentRender.setCardCount(cardCount);
	}

	public String createHead() {
		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();
		// 获取记录当前子Item的Index
		Integer itemIndex = (Integer) getCurrentItemIndex();
		StringBuilder buf = new StringBuilder();
		buf.append("window.$").append(parentRender.getId()).append("_item").append(itemIndex).append(" = function(){\n");
		// 将已有的脚本暂存在临时变量中
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		if (dsScript == null) {
			dsScript = new StringBuilder();
			this.setContextAttribute(DS_SCRIPT, dsScript);
		}
		this.setContextAttribute("$card_" + id + "$tmpScript", dsScript.toString());
		dsScript.delete(0, dsScript.length());
		return buf.toString();
	}

	public String createTail() {
		StringBuilder tmpBuf = new StringBuilder();
		StringBuilder dsScript = (StringBuilder) this.getContextAttribute(DS_SCRIPT);
		dsScript.append(PcFormRenderUtil.getAllFormDsScript(this.viewId));// 自由表单的数据集的设置
		String tmpScript = (String) this.getContextAttribute("$card_" + id + "$tmpScript");
		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();
		UICardLayout card = parentRender.getUiElement();
		// 如果 不是当前显示项目
		if (!this.id.equals(card.getCurrentItem())) {
			// 将dsScript中的内容写入页面，并恢复原来的脚本
			tmpBuf.append(dsScript.toString());
			dsScript.delete(0, dsScript.length());
			if (tmpScript != null)
				dsScript.append(tmpScript);
		} else {
			if (tmpScript != null)
				dsScript.insert(0, tmpScript);
		}
		this.removeContextAttribute("$card_" + id + "$tmpScript");
		tmpBuf.append("\n};\n");
		return tmpBuf.toString();
	}

	/**
	 * 2011-8-2 下午08:09:28 renxh des：获得当前的条目的index
	 * 
	 * @return
	 */
	private int getCurrentItemIndex() {
		UICardPanel cardPanel = this.getUiElement();
		PCCardLayoutRender parentRender = (PCCardLayoutRender) this.getParentRender();
		UICardLayout cardLayout = (UICardLayout) parentRender.getUiElement();
		List<UILayoutPanel> panelList = cardLayout.getPanelList();
		return panelList.indexOf(cardPanel);
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_CARDPANEL;
	}

	@Override
	public String placeSelf() {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(getNewDivId()).append(" = $('<div>').attr('id','").append(getNewDivId()).append("').css({\n");
		buf.append("'width': '100%',\n");
		buf.append("'height':'100%',\n");
		// buf.append(getNewDivId()).append(".style.position = 'absolute';\n");
		buf.append("'position':'relative'});\n");
		if (this.isEditMode()) {
			buf.append(this.placeDesign());
			buf.append(getNewDivId()).append(".append(" + getDivId() + ");\n");
		}
		return buf.toString();
	}
}
