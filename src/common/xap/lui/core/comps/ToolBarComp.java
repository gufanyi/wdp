package xap.lui.core.comps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.ButtonContext;
import xap.lui.core.context.ToolbarContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.RequestLifeCycleContext;
import xap.lui.core.model.ViewPartMeta;
import xap.lui.core.render.PCToolbarCompRender;
import xap.lui.core.render.notify.RenderProxy;

@XmlRootElement(name = "ToolBar")
@XmlAccessorType(XmlAccessType.NONE)
public class ToolBarComp extends WebComp implements IContainerComp<ToolBarItem> {
	private static final long serialVersionUID = -2518084293546367343L;
	public static final String WIDGET_NAME = "toolbar";
	public static final String NOTIFY_UPDATE_TYPE = "notifyUpdateType";
	public static final String NOTIYF_UPDATE_ADDITEM = "notifyUpdateType_additem";
	public static final String WIDGET_ID = "widgetId";
	public static final String TOOLBAR_ID = "toolbarId";
	// private ToolBarTitle title;
	@XmlAttribute
	private boolean transparent = false;
	@XmlElementRefs({ @XmlElementRef(name = "ToolBarItem", type = ToolBarItem.class) })
	private List<ToolBarItem> elementList = new ArrayList<ToolBarItem>();
	private List<String> delIds;

	private PCToolbarCompRender render = null;

	public ToolBarComp() {
		super();
	}

	public ToolBarComp(String id) {
		super(id);
	}

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public ToolBarItem[] getElements() {
		return elementList.toArray(new ToolBarItem[0]);
	}

	public List<ToolBarItem> getElementList() {
		return elementList;
	}

	public void setElementList(List<ToolBarItem> elementList) {
		this.elementList = elementList;
	}

	public int getElementCountWithoutHidden() {
		if (elementList == null)
			return 0;
		int count = 0;
		for (ToolBarItem ele : elementList) {
			if (ele.isVisible())
				count++;
		}
		return count;
	}

	public ToolBarItem getElementById(String id) {
		Iterator<ToolBarItem> it = elementList.iterator();
		while (it.hasNext()) {
			ToolBarItem ele = it.next();
			if (ele.getId().equals(id))
				return ele;
		}
		return null;
	}

	public void addElement(ToolBarItem ele) {
		this.elementList.add(ele);
		ele.setToolbar(this);
		ele.setWidget(this.getWidget());
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().addToolBarItem(ele);
		}
	}

	public void deleteElement(ToolBarItem ele) {
		this.elementList.remove(ele);
		if (delIds == null)
			delIds = new ArrayList<String>();
		delIds.add(ele.getId());
		this.setCtxChanged(true);
	}

	public Object clone() {
		ToolBarComp comp = (ToolBarComp) super.clone();
		if (this.elementList != null) {
			comp.elementList = new ArrayList<ToolBarItem>();
			Iterator<ToolBarItem> it = this.elementList.iterator();
			while (it.hasNext()) {
				LifeCyclePhase phase = RequestLifeCycleContext.get().getPhase();
				// 将ajax的状态置为nullstatus
				RequestLifeCycleContext.get().setPhase(LifeCyclePhase.nullstatus);
				comp.addElement((ToolBarItem) it.next().clone());
				RequestLifeCycleContext.get().setPhase(phase);
			}
		}
		return comp;
	}

	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	@Override
	public void setWidget(ViewPartMeta widget) {
		ToolBarItem[] items = this.getElements();
		for (ToolBarItem item : items) {
			item.setWidget(widget);
		}
		super.setWidget(widget);
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	@Override
	public boolean isCtxChanged() {
		ToolBarItem[] items = getElements();
		for (int i = 0; i < items.length; i++) {
			ToolBarItem toolBarItem = items[i];
			if (toolBarItem.isCtxChanged())
				return true;
		}
		return super.isCtxChanged();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if(!enabled){
			if(this.elementList != null){
				for(ToolBarItem item : elementList){
					item.setEnabled(enabled);
				}
			}
		}
	}
	@Override
	public void setVisible(boolean visible) {
		this.isVisible = visible;
		if(!visible){
			if(this.elementList != null){
				for(ToolBarItem item : elementList){
					item.setVisible(visible);
				}
			}
		}
	}

	/**
	 * 获取所有按钮子项
	 * 
	 * @return
	 */
	private List<ToolBarItem> getButtonItemList() {
		List<ToolBarItem> buttonItemList = new ArrayList<ToolBarItem>();
		for (ToolBarItem item : elementList) {
			if (item.getType().equals(ToolBarItem.BUTTON_TYPE))
				buttonItemList.add(item);
		}
		return buttonItemList;
	}

	@Override
	public BaseContext getContext() {
		ToolbarContext ctx = new ToolbarContext();
		ctx.setId(this.getId());
		if (this.elementList.size() > 0) {
			// Button子项
			List<ToolBarItem> buttonItemList = getButtonItemList();
			List<ButtonContext> ctxList = new ArrayList<ButtonContext>();
			Iterator<ToolBarItem> it = buttonItemList.iterator();
			while (it.hasNext()) {
				ToolBarItem item = it.next();
				if (item.isCtxChanged()) {
					ctxList.add((ButtonContext) item.getContext());
				}
			}
			ctx.setButtonItemContexts(ctxList.toArray(new ButtonContext[0]));
			// TODO 其他子项
		}
		if (delIds != null && delIds.size() > 0)
			ctx.setDelIds(delIds.toArray(new String[0]));
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		ToolbarContext tbCtx = (ToolbarContext) ctx;
		// Button子项
		ButtonContext[] buttonItemContexts = tbCtx.getButtonItemContexts();
		if (buttonItemContexts != null) {
			for (int i = 0, n = buttonItemContexts.length; i < n; i++) {
				ButtonContext btCtx = buttonItemContexts[i];
				List<ToolBarItem> buttonItemList = getButtonItemList();
				for (int j = 0, m = buttonItemList.size(); j < m; j++) {
					if (buttonItemList.get(j).getId().equals(btCtx.getId())) {
						buttonItemList.get(j).setContext(btCtx);
						break;
					}
				}
			}
		}
		// TODO 其他子项
		this.setCtxChanged(false);
	}

	// @Override
	// public void notifyChange(String type, Object obj) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// if (NOTIYF_UPDATE_ADDITEM.equals(type)) {
	// map.put(NOTIFY_UPDATE_TYPE, NOTIYF_UPDATE_ADDITEM);
	// map.put(NOTIYF_UPDATE_ADDITEM, obj);
	// }
	// if (!map.isEmpty()) {
	// if (this.getWidget() != null) {
	// map.put(WIDGET_ID, this.getWidget().getId());
	// }
	// map.put(TOOLBAR_ID, this.getId());
	// super.notifyChange(UIElement.UPDATE, map);
	// }
	// }

	@Override
	public PCToolbarCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCToolbarCompRender(this));
		}
		return render;
	}

}
