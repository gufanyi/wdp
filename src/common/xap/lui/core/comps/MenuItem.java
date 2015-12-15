package xap.lui.core.comps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.alibaba.fastjson.annotation.JSONField;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.MenuItemContext;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCContextMenuCompRender;
import xap.lui.core.render.PCMenubarCompRender;
/**
 * 菜单项
 * 
 */
@XmlRootElement(name = "MenuItem")
@XmlAccessorType(XmlAccessType.NONE)
public class MenuItem extends WebComp {
	private static final long serialVersionUID = -1068377600722925562L;
	public static final String WIDGET_NAME = "menuitem";
	public static final String WIDGET_ID = "widgetId";
	public static final String MENU_ID = "menuId";
	public static final String MENU_ITEM_ID = "menuItemId";
	public static final String PMENU_ITEM_ID = "pmenuItemId";
	public static final String NOTIFY_UPDATE_TYPE = "notifyUpdateType";
	public static final String CHANGEIMG = "changeImg";
	
	@JSONField(serialize=false)
	private PCMenubarCompRender render=null;
	@JSONField(serialize=false)
	private PCContextMenuCompRender ctxrender = null;
	
	@XmlAttribute
	private String text = "";
	private String i18nName;
	@XmlElement(name="MenuItem")
	private List<MenuItem> childList = null;
	// 鼠标悬停时的提示信息
	@XmlAttribute
	private String tip;
	@XmlAttribute
	private String tipI18nName;
	// 设置在checkbox状态下是否选中
	@XmlAttribute
	private boolean selected = false;
	// 设置此菜单是否checkbox组
	@XmlAttribute
	private boolean checkBoxGroup = false;
	// 内部标识信息
	@XmlAttribute
	private String tag = null;
	// 热键
	@XmlAttribute
	private String hotKey = null;
	// 热键显示名称
	@XmlAttribute
	private String displayHotKey = null;
	// 修饰符，默认＝CTRL
	@XmlAttribute
	private int modifiers = java.awt.Event.CTRL_MASK;
	@XmlAttribute
	private String imgIcon;//图标
	@XmlAttribute
	private String imgIconOn;//鼠标悬浮时图标
	@XmlAttribute
	private String imgIconDisable;//不可用时图标
	// 图片路径是否改变
	@XmlAttribute
	private boolean imgIconChanged = true;
	// 图片真实路径，在context中使用
	@XmlAttribute
	private String realImgIcon;
	@XmlAttribute
	private boolean imgIconOnChanged = true;//
	@XmlAttribute
	private String realImgIconOn;//鼠标悬浮时图片真实路径
	@XmlAttribute
	private boolean imgIconDisableChanged = true;//
	@XmlAttribute
	private String realImgIconDisable;//不可用时图片真实路径
	@XmlAttribute
	private String langDir;//
	
	private MenuItem parentItem;//
	// 是否是分割线
	@XmlAttribute
	private boolean sep = false;
	// 状态管理类
	@XmlAttribute
	private String stateManager = "";
	// 菜单
	private MenubarComp menu = null;
	// 右键菜单
	private ContextMenuComp ctxmenu = null;
	
	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}
	public boolean isSep() {
		return sep;
	}
	public void setSep(boolean isSep) {
		this.sep = isSep;
	}
	public MenuItem() {
		super();
	}
	public MenuItem(String id) {
		super(id);
	}
	public MenuItem getItem(String cId) {
		List<MenuItem> list = this.getChildList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(cId))
				return list.get(i);
			else{
				MenuItem ele = list.get(i);
				List<MenuItem> childs = ele.getChildList();
				if(childs != null && childs.size() > 0){
					if(ele.getItem(cId) == null)
						continue;
					return ele.getItem(cId);
				}
			}
		}
		return null;
	}
	public List<MenuItem> getChildList() {
		return childList;
	}
	/**
	 * 清除该item的所有孩子
	 */
	public void removeAllChildren() {
		if (this.childList != null)
			this.childList.clear();
	}
	public void setChildList(List<MenuItem> childList) {
		this.childList = childList;
		if (this.childList != null) {
			Iterator<MenuItem> it = this.childList.iterator();
			while (it.hasNext()) {
				MenuItem item = it.next();
				item.setParentItem(this);
			}
		}
	}
	public String getI18nName() {
		return i18nName;
	}
	public void setI18nName(String label) {
		this.i18nName = label;
	}
	public void addMenuItem(MenuItem item) {
		if (childList == null)
			childList = new ArrayList<MenuItem>();
		childList.add(item);
		item.setParentItem(this);
		item.setWidget(this.getWidget());
		item.setMenu(this.getMenu());
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			if (this.getWidget() != null && this.getMenu() != null && !isSep()){
				this.getRender().addItem(item);
			}
		}
	}
	public void addCtnMenuItem(MenuItem item) {
		if (childList == null)
			childList = new ArrayList<MenuItem>();
		childList.add(item);
		item.setParentItem(this);
		item.setWidget(this.getWidget());
		item.setCtxmenu(this.getCtxmenu());
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			if (this.getWidget() != null && this.getCtxmenu() != null && !isSep())
				this.getCtxRender().addMenuItem(item);
		}
	}
	public Object clone() {
		MenuItem item = (MenuItem) super.clone();
		item.childList = new ArrayList<MenuItem>();
		if (this.childList != null) {
			for (MenuItem child : this.childList)
				item.childList.add((MenuItem) child.clone());
		}
		return item;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isCheckBoxGroup() {
		return checkBoxGroup;
	}
	public void setCheckBoxGroup(boolean checkBoxGroup) {
		this.checkBoxGroup = checkBoxGroup;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getDisplayHotKey() {
		return displayHotKey;
	}
	public void setDisplayHotKey(String displayHotKey) {
		this.displayHotKey = displayHotKey;
	}
	public String getHotKey() {
		return hotKey;
	}
	public void setHotKey(String hotKey) {
		this.hotKey = hotKey;
	}
	public int getModifiers() {
		return modifiers;
	}
	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	public String getImgIcon() {
		return imgIcon;
	}
	public void setImgIcon(String imgIcon) {
		if (this.imgIcon != imgIcon) {
			this.imgIcon = imgIcon;
			setCtxChanged(true);
			setImgIconChanged(true);
		}
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			if (this.getWidget() != null && this.getMenu() != null && !isSep()){
				this.getRender().setItemImg(this);
			}else if (this.getWidget() != null && this.getCtxmenu() != null && !isSep()){
				this.getCtxRender().setItemImg(this);
			}
		}
	}
	public String getImgIconOn() {
		return imgIconOn;
	}
	public void setImgIconOn(String imgIconOn) {
		this.imgIconOn = imgIconOn;
	}
	public String getImgIconDisable() {
		return imgIconDisable;
	}
	public void setImgIconDisable(String imgIconDisable) {
		this.imgIconDisable = imgIconDisable;
	}
	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}
	@Override
	public String toString() {
		return getId() + ":" + getText();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		if (text != null && !text.equals(this.text)) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.i18nName = null;
			}
			this.text = text;
			if (this.getWidget() != null && this.getMenu() != null && !isSep()){
				this.getRender().setItemText(this);
			}else if (this.getWidget() != null && this.getCtxmenu() != null && !isSep()){
				this.getCtxRender().setItemText(this);
			}
		}
	}
	public String getTipI18nName() {
		return tipI18nName;
	}
	public void setTipI18nName(String tipI18nName) {
		this.tipI18nName = tipI18nName;
	}
	public MenuItem getParentItem() {
		return parentItem;
	}
	public void setParentItem(MenuItem parentItem) {
		this.parentItem = parentItem;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean visible) {
		if (this.isVisible != visible) {
			this.isVisible = visible;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				if (this.getWidget() != null && this.getMenu() != null && !isSep()){
					this.getRender().setItemVisible(this);
				}else if(this.getWidget() != null && this.getCtxmenu() != null && !isSep()){
					this.getCtxRender().setItemVisible(this);
				}
			}
		}
	}
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			if (this.getWidget() != null && this.getMenu() != null && !isSep()){
				this.getRender().setItemEnabled(this);
			}else if (this.getWidget() != null && this.getCtxmenu() != null && !isSep()){
				this.getCtxRender().setItemEnabled(this);
			}
		}
	}
	public boolean isImgIconChanged() {
		return imgIconChanged;
	}
	public void setImgIconChanged(boolean imgIconChanged) {
		this.imgIconChanged = imgIconChanged;
	}
	/**
	 * 获取图片真实路径
	 * 
	 * @return
	 */
	public String getRealImgIcon() {
		if(this.imgIcon == null)
			return null;
		if (this.imgIcon.contains("${theme}")) {
			realImgIcon = getRealImgPath(this.imgIcon, null);
		} else if(this.imgIcon.contains("/") || this.imgIcon.contains("\\")){
			realImgIcon = getRealImgPath(this.imgIcon, null);
		} else {
			String temImg = "icon/16/" + this.imgIcon;
			realImgIcon = getRealImgPath(temImg, null);
		}
		return realImgIcon;
	}
	public void setRealImgIcon(String realImgIcon) {
		this.realImgIcon = realImgIcon;
	}
	public boolean isImgIconOnChanged() {
		return imgIconOnChanged;
	}
	public void setImgIconOnChanged(boolean imgIconOnChanged) {
		this.imgIconOnChanged = imgIconOnChanged;
	}
	public String getRealImgIconOn() {
		if (isImgIconOnChanged()) {
			realImgIconOn = getRealImgPath(this.imgIconOn, null);
			setImgIconOnChanged(false);
		}
		return realImgIconOn;
	}
	public void setRealImgIconOn(String realImgIconOn) {
		this.realImgIconOn = realImgIconOn;
	}
	public boolean isImgIconDisableChanged() {
		return imgIconDisableChanged;
	}
	public void setImgIconDisableChanged(boolean imgIconDisableChanged) {
		this.imgIconDisableChanged = imgIconDisableChanged;
	}
	public String getRealImgIconDisable() {
		if (isImgIconDisableChanged()) {
			realImgIconDisable = getRealImgPath(this.imgIconDisable, null);
			setImgIconDisableChanged(false);
		}
		return realImgIconDisable;
	}
	public void setRealImgIconDisable(String realImgIconDisable) {
		this.realImgIconDisable = realImgIconDisable;
	}
	@Override
	public BaseContext getContext() {
		MenuItemContext ctx = new MenuItemContext();
		ctx.setRefImg(this.getRealImgIcon());
		ctx.setId(this.getId());
		if (this.childList != null && this.childList.size() > 0) {
			MenuItemContext[] childItemContexts = new MenuItemContext[this.childList.size()];
			for (int i = 0, n = this.childList.size(); i < n; i++) {
				childItemContexts[i] = (MenuItemContext) this.childList.get(i).getContext();
			}
			ctx.setChildItemContexts(childItemContexts);
		}
		return ctx;
	}
	public void setContext(BaseContext ctx) {
		MenuItemContext miCtx = (MenuItemContext) ctx;
		this.setEnabled(miCtx.getEnabled());
		this.setVisible(miCtx.getVisible());
		this.setRealImgIcon(miCtx.getRefImg());
		MenuItemContext[] childItemContexts = miCtx.getChildItemContexts();
		if (childItemContexts != null) {
			for (int i = 0, n = childItemContexts.length; i < n; i++) {
				MenuItemContext itemCtx = childItemContexts[i];
				if (itemCtx == null)
					continue;
				for (int j = 0, m = this.childList.size(); j < m; j++) {
					if (childList.get(j).getId().equals(itemCtx.getId())) {
						childList.get(j).setContext(itemCtx);
						break;
					}
				}
			}
		}
		this.setCtxChanged(false);
	}
	public String getStateManager() {
		return stateManager;
	}
	public void setStateManager(String stateManager) {
		this.stateManager = stateManager;
	}
	public MenubarComp getMenu() {
		return menu;
	}
	public void setMenu(MenubarComp menu) {
		this.menu = menu;
	}
	public ContextMenuComp getCtxmenu() {
		return ctxmenu;
	}
	public void setCtxmenu(ContextMenuComp ctxmenu) {
		this.ctxmenu = ctxmenu;
	}
	
	public List<String> getAllParents(){
		List<String> parentIds = new ArrayList<String>();
		MenuItem mi = this.getParentItem();
		while(mi!=null){
			parentIds.add(mi.getId());
			mi = mi.getParentItem();
		}
		return parentIds;
	}
	
	public PCMenubarCompRender getRender(){
		if(render==null){
			render=this.getMenu().getRender();
		}
		return render;
	}
	@JSONField(serialize=false)
	public PCContextMenuCompRender getCtxRender(){
		if(ctxrender==null){
			ctxrender=this.getCtxmenu().getRender();
		}
		return ctxrender;
	}

}
