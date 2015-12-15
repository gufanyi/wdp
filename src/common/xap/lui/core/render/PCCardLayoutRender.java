package xap.lui.core.render;

import xap.lui.core.comps.LuiElement;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UIElement;
import xap.lui.core.layout.UILayout;
import xap.lui.core.layout.UILayoutPanel;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;

/**
 * @author renxh 卡片布局渲染器
 * @param <T>
 * @param <K>
 */
@SuppressWarnings("unchecked")
public class PCCardLayoutRender extends UILayoutRender<UICardLayout, LuiElement> {

	// 卡片布局ID基础字符串
	protected static final String CARD_ID_BASE = "card_layout_";

	public PCCardLayoutRender(UICardLayout uiEle) {
		super(uiEle);
		this.setCardIndex(Integer.parseInt(uiEle.getCurrentItem()));

	}

	// 初始时显示的页的顺序
	private int cardIndex = 0;

	// 用来完成CardPanel的计数
	private int cardCount = 0;


	public String createHead() {
		return toResize("$(\"#"+getDivId()+"\")[0]", "cardResize");
	}



	public String createTail() {
		StringBuffer buf = new StringBuffer();
		buf.append("var ").append(getVarId()).append(" = $(\"<div>\").cardLayout({\n");
		buf.append("id:'").append(getId()).append("',\n");
		buf.append("cardDiv:").append("$('#"+getDivId()+"')[0]").append(",\n");
		buf.append("index:").append(cardIndex).append("\n");
		buf.append("}).cardLayout('instance');\n");
		
		if (this.getViewId() != null) {
			String widget = WIDGET_PRE + this.getCurrWidget().getId();
			buf.append("var ").append(widget).append(" = pageUI.getViewPart('").append(this.getCurrWidget().getId()).append("');\n");
			buf.append(widget + ".addCard(" + getVarId() + ");\n");
		} else
			buf.append("pageUI.addCard(" + getVarId() + ");\n");
		buf.append(addEventSupport(this.getUiElement(), getViewId(), getVarId(), null));
		return buf.toString();
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public void setCardIndex(int cardIndex) {
		this.cardIndex = cardIndex;
	}

	public int getCardCount() {
		return cardCount;
	}

	public void setCardCount(int cardCount) {
		this.cardCount = cardCount;
	}

	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_CARDLAYOUT;
	}

	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setCardPage(int pageIndex) {
		StringBuffer buf = new StringBuffer();		
		if (this.getViewId() != null) {
			buf.append("var currForm = pageUI.getViewPart('").append(this.viewId).append("');\n");
			buf.append("var ").append(getVarId()).append(" = currForm.getCard('"+getId()+"');\n");
		} else{
			buf.append("var ").append(getVarId()).append(" = pageUI.getCard('" + getId() + "');\n");
		}
		
		buf.append("if("+getVarId()+"){\n");
		buf.append(getVarId()).append(".setPage("+pageIndex+");\n");
		buf.append("};\n");		
		
		buf.append("$(window).triggerHandler('resize');\n");		
		addBeforeExeScript(buf.toString());
	}

	
	@Override
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void addChild(UIElement obj) {
		UILayout layout = getUiElement();
		int size = layout.getPanelList().size();
		UILayoutPanel targetPanel = (UILayoutPanel) obj;
		int index = 0;
		for (int i = 0; i < size; i++) {
			UILayoutPanel panel = layout.getPanelList().get(i);
			if(panel.getId().equals(targetPanel.getId())){
				index = i;
				break;
			}
		}
		ILuiRender render =targetPanel.getRender();
		StringBuffer buf = new StringBuffer();
		String html = render.place();
		buf.append(html);
		
		buf.append("var div = $('#" + divId + "');\n");
		if (index == (size - 1)){
			buf.append("div.append(" + render.getNewDivId() + ");\n");
		} else {
			buf.append( render.getNewDivId()).append(".insertBefore(div.children(':eq(").append(index).append(")'));\n");
//			buf.append("div.insertBefore(" + render.getNewDivId() + ", div.children[" + (index) + "]);\n");
		}
		buf.append(render.create());
		buf.append("$(window).triggerHandler('resize');\n");
		buf.append(getVarId()).append(".refreshPages("+index+");\n");
		addDynamicScript(buf.toString());
	}	
}
