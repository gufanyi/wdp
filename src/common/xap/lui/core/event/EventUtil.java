/**
 * 
 */
package xap.lui.core.event;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import xap.lui.core.comps.ButtonComp;
import xap.lui.core.comps.CheckBoxComp;
import xap.lui.core.comps.CheckboxGroupComp;
import xap.lui.core.comps.ContextMenuComp;
import xap.lui.core.comps.EditorComp;
import xap.lui.core.comps.FileUploadComp;
import xap.lui.core.comps.FormComp;
import xap.lui.core.comps.FormElement;
import xap.lui.core.comps.GridComp;
import xap.lui.core.comps.IFrameComp;
import xap.lui.core.comps.ImageComp;
import xap.lui.core.comps.LabelComp;
import xap.lui.core.comps.LinkComp;
import xap.lui.core.comps.ListComp;
import xap.lui.core.comps.MenuItem;
import xap.lui.core.comps.MenubarComp;
import xap.lui.core.comps.ModalDialogComp;
import xap.lui.core.comps.RadioComp;
import xap.lui.core.comps.RadioGroupComp;
import xap.lui.core.comps.ReferenceComp;
import xap.lui.core.comps.SelfDefComp;
import xap.lui.core.comps.SelfDefElementComp;
import xap.lui.core.comps.TextAreaComp;
import xap.lui.core.comps.TextComp;
import xap.lui.core.comps.ToolBarComp;
import xap.lui.core.comps.ToolBarItem;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.dataset.Dataset;
import xap.lui.core.layout.UICardLayout;
import xap.lui.core.layout.UIShutter;
import xap.lui.core.layout.UITabComp;
import xap.lui.core.listener.JsEventDesc;
import xap.lui.core.model.Application;
import xap.lui.core.model.PagePartMeta;
import xap.lui.core.model.ViewPartMeta;
/**
 * @author chouhl
 */
public class EventUtil {
	public final static String[] MouseListeners = new String[] {
			MouseEvent.ON_MOUSE_OUT, MouseEvent.ON_MOUSE_OVER, MouseEvent.ON_DB_CLICK, MouseEvent.ON_CLICK
	};
	public final static String[] CardListeners = new String[] {
			CardEvent.BEFORE_PAGE_CHANGE, CardEvent.AFTER_PAGE_INIT, CardEvent.BEFORE_PAGE_INIT
	};
	public final static String[] ContextMenuListeners = new String[] {
			ContextMenuEvent.ON_MOUSE_OUT, ContextMenuEvent.ON_CLOSE, ContextMenuEvent.BEFORE_CLOSE, ContextMenuEvent.ON_SHOW, ContextMenuEvent.BEFORE_SHOW
	};
	public final static String[] FocusListeners = new String[] {
			FocusEvent.ON_BLUR, FocusEvent.ON_FOCUS
	};
	public final static String[] FileListeners = new String[] {
		FileUploadEvent.ON_UPLOAD
	};
	public final static String[] AutoformListeners = new String[] {
			AutoformEvent.IN_ACTIVE, AutoformEvent.GET_VALUE, AutoformEvent.ACTIVE, AutoformEvent.SET_VALUE
	};
	public final static String[] GridListeners = new String[] {
			GridEvent.ON_DATA_OUTER_DIV_CONTEXT_MENU, GridEvent.ON_LAST_CELL_ENTER
	};
	public final static String[] GridRowListeners = new String[] {
			GridRowEvent.ON_ROW_DB_CLICK, GridRowEvent.ON_ROW_SELECTED, GridRowEvent.BEFORE_ROW_SELECTED
	};
	public final static String[] GridCellListeners = new String[] {
			GridCellEvent.BEFORE_EDIT, GridCellEvent.AFTER_EDIT, GridCellEvent.CELL_EDIT, GridCellEvent.ON_CELL_CLICK, GridCellEvent.CELL_VALUE_CHANGED
	};
	public final static String[] PluginListeners = new String[] {
		"plugin"
	};
	public final static String[] ViewListeners = new String[] {
	// WidgetListener.BEFORE_INIT_DATA, WidgetListener.ON_INITIALIZING
	};
	public final static String[] DialogListeners = new String[] {
			DialogEvent.ON_CLOSE, DialogEvent.AFTER_CLOSE, DialogEvent.BEFORE_CLOSE, DialogEvent.BEFORE_SHOW, DialogEvent.ON_CANCEL, DialogEvent.ON_OK
	};
	public final static String[] LinkListeners = new String[] {
		LinkEvent.ON_CLICK
	};
	public final static String[] ListListeners = new String[] {
			ListEvent.DB_VALUE_CHANGE, ListEvent.VALUE_CHANGED
	};
	public final static String[] ContainerListeners = new String[] {
		ContainerEvent.ON_CONTAINER_CREATE
	};
	public final static String[] OutlookBarListeners = new String[] {
			OutlookBarEvent.AFTER_ITEM_INIT, OutlookBarEvent.BEFORE_ITEM_INIT, OutlookBarEvent.AFTER_CLOSE_ITEM, OutlookBarEvent.BEFORE_ACTIVED_CHANGE, OutlookBarEvent.AFTER_ACTIVED_CHANGE
	};
	public final static String[] SelfDefListeners = new String[] {
		SelfDefEvent.ON_SELF_DEF_EVENT, SelfDefEvent.ON_CREATE_EVENT
	};
	public final static String[] TabListeners = new String[] {
			TabEvent.AFTER_ITEM_INIT, TabEvent.BEFORE_ITEM_INIT, TabEvent.AFTER_CLOSE_ITEM, TabEvent.BEFORE_ACTIVED_CHANGE, TabEvent.AFTER_ACTIVED_CHANGE
	};
	public final static String[] TextListeners = new String[] {
			TextEvent.ON_SELECT, TextEvent.VALUE_CHANGED
	};
	public final static String[] KeyListeners = new String[] {
			KeyEvent.ON_KEY_UP, KeyEvent.ON_KEY_DOWN, KeyEvent.ON_ENTER
	};
	public final static String[] ReferenceTextListeners = new String[] {
		RefTextEvent.BEFORE_OPEN_REF_PAGE
	};
	public final static String[] TreeNodeListeners_1 = new String[] {
			TreeNodeEvent.ON_CLICK, TreeNodeEvent.ON_DBCLICK, TreeNodeEvent.ON_CHECKED, TreeNodeEvent.ON_NODE_LOAD, TreeNodeEvent.ON_NODE_DELETE, TreeNodeEvent.BEFORE_SEL_NODE_CHANGE, TreeNodeEvent.AFTER_SEL_NODE_CHANGE, TreeNodeEvent.ROOT_NODE_CREATED, TreeNodeEvent.NODE_CREATED, TreeNodeEvent.BEFORE_NODE_CAPTION_CHANGE
	};
	public final static String[] TreeNodeListeners_2 = new String[] {
			TreeNodeDragEvent.ON_DRAG_END, TreeNodeDragEvent.ON_DRAG_START
	};
	public final static String[] TreeRowListeners = new String[] {
		TreeRowEvent.BEFORE_NODE_CREATE
	};
	public final static String[] TreeContextMenuListeners = new String[] {
		TreeCtxMenuEvent.BEFORE_CONTEXT_MENU
	};
	public final static String[] DatasetListeners_1 = new String[] {
			DatasetEvent.ON_AFTER_PAGE_CHANGE, DatasetEvent.ON_AFTER_ROW_DELETE, DatasetEvent.ON_AFTER_ROW_SELECT, DatasetEvent.ON_AFTER_ROW_UN_SELECT, DatasetEvent.ON_BEFORE_PAGE_CHANGE, DatasetEvent.ON_BEFORE_ROW_DELETE, DatasetEvent.ON_BEFORE_ROW_INSERT, DatasetEvent.ON_BEFORE_ROW_SELECT
	};
	public final static String[] DatasetListeners_2 = new String[] {
		DatasetEvent.ON_AFTER_ROW_INSERT
	};
	public final static String[] DatasetListeners_3 = new String[] {
			DatasetEvent.ON_BEFORE_DATA_CHANGE, DatasetEvent.ON_AFTER_DATA_CHANGE
	};
	public final static String[] DatasetListeners_4 = new String[] {
		DatasetEvent.ON_DATA_LOAD
	};
	public final static String[] PageListeners = new String[] {
			PageEvent.AFTER_PAGE_INIT, PageEvent.BEFORE_ACTIVE, PageEvent.ON_CLOSING, PageEvent.ON_CLOSED
	};
	public static List<JsEventDesc> createAcceptEventDescs(IEventSupport element) {
		List<JsEventDesc> descList = null;
		EventEntity[] eventEntities = null;
		if (element instanceof MenubarComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof ButtonComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof UICardLayout) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(CardListeners, CardEvent.class, "cardEvent", CardEvent.class);
		} else if (element instanceof ContextMenuComp) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(ContextMenuListeners, ContextMenuEvent.class, "contextMenuEvent", ContextMenuEvent.class);
		} else if (element instanceof EditorComp) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
		} else if (element instanceof FileUploadComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(FileListeners, FileUploadEvent.class, "fileUploadEvent", FileUploadEvent.class);
		} else if (element instanceof FormComp) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(AutoformListeners, AutoformEvent.class, "autoformEvent", AutoformEvent.class);
			eventEntities[2] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
		} else if (element instanceof GridComp) {
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(GridListeners, GridEvent.class, "gridEvent", GridEvent.class);
			eventEntities[2] = new EventEntity(GridRowListeners, GridRowEvent.class, "gridRowEvent", GridRowEvent.class);
			eventEntities[3] = new EventEntity(GridCellListeners, GridCellEvent.class, "gridCellEvent", GridCellEvent.class);
		} else if (element instanceof IFrameComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof ImageComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof LabelComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof ViewPartMeta) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(PluginListeners, null, "", null);
			eventEntities[1] = new EventEntity(DialogListeners, DialogEvent.class, "dialogEvent", DialogEvent.class);
			eventEntities[2] = new EventEntity(ViewListeners, ViewPartEvent.class, "widgetEvent", ViewPartEvent.class);
		} else if (element instanceof LinkComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(LinkListeners, LinkEvent.class, "linkEvent", LinkEvent.class);
		} else if (element instanceof ListComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(ListListeners, ListEvent.class, "listEvent", ListEvent.class);
		} else if (element instanceof MenubarComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof MenuItem) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(ContainerListeners, ContainerEvent.class, "containerEvent", ContainerEvent.class);
		} else if (element instanceof ModalDialogComp) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(DialogListeners, DialogEvent.class, "simpleEvent", DialogEvent.class);
		} else if (element instanceof SelfDefComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(SelfDefListeners, SelfDefEvent.class, "selfDefEvent", SelfDefEvent.class);
		} else if (element instanceof UITabComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(TabListeners, TabEvent.class, "tabEvent", TabEvent.class);
		} else if (element instanceof CheckBoxComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
		} else if (element instanceof CheckboxGroupComp) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
		} else if (element instanceof RadioComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof RadioGroupComp) {
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
			eventEntities[3] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
		} else if (element instanceof ReferenceComp) {
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
			eventEntities[3] = new EventEntity(ReferenceTextListeners, RefTextEvent.class, "refTextEvent", RefTextEvent.class);
		} else if (element instanceof SelfDefElementComp) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
		} else if (element instanceof TextAreaComp) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
		} else if (element instanceof TextComp) {
			eventEntities = new EventEntity[4];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusListener.class);
			eventEntities[3] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
		} else if (element instanceof ToolBarComp) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
		} else if (element instanceof ToolBarItem) {
			eventEntities = new EventEntity[2];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(ContextMenuListeners, ContextMenuEvent.class, "contextMenuEvent", ContextMenuEvent.class);
		} else if (element instanceof TreeViewComp) {
			eventEntities = new EventEntity[5];
			eventEntities[0] = new EventEntity(MouseListeners, MouseEvent.class, "mouseEvent", MouseEvent.class);
			eventEntities[1] = new EventEntity(TreeNodeListeners_1, TreeNodeEvent.class, "treeNodeEvent", TreeNodeEvent.class);
			eventEntities[2] = new EventEntity(TreeNodeListeners_2, TreeNodeDragEvent.class, "treeNodeDragEvent", TreeNodeDragEvent.class);
			eventEntities[3] = new EventEntity(TreeRowListeners, TreeRowEvent.class, "treeRowEvent", TreeRowEvent.class);
			eventEntities[4] = new EventEntity(TreeContextMenuListeners, TreeCtxMenuEvent.class, "treeCtxMenuEvent", TreeCtxMenuEvent.class);
		} else if (element instanceof Dataset) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(DatasetListeners_1, DatasetEvent.class, "datasetEvent", DatasetEvent.class);
			eventEntities[1] = new EventEntity(DatasetListeners_2, RowInsertEvent.class, "rowInsertEvent", DatasetEvent.class);
			eventEntities[2] = new EventEntity(DatasetListeners_3, DatasetCellEvent.class, "datasetCellEvent", DatasetEvent.class);
		} else if (element instanceof PagePartMeta) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(PageListeners, PageEvent.class, "pageEvent", PageEvent.class);
		} else if (element instanceof Application) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(PageListeners, PageEvent.class, "pageEvent", PageEvent.class);
		} else if (element instanceof UIShutter) {
			eventEntities = new EventEntity[1];
			eventEntities[0] = new EventEntity(OutlookBarListeners, SimpleEvent.class, "simpleEvent", OutlookBarEvent.class);
		} else if (element instanceof FormElement) {
			eventEntities = new EventEntity[3];
			eventEntities[0] = new EventEntity(FocusListeners, FocusEvent.class, "focusEvent", FocusEvent.class);
			eventEntities[1] = new EventEntity(KeyListeners, KeyEvent.class, "keyEvent", KeyListener.class);
			eventEntities[2] = new EventEntity(TextListeners, TextEvent.class, "textEvent", TextEvent.class);
		}
		if (eventEntities != null) {
			descList = new ArrayList<JsEventDesc>();
			for (EventEntity ee : eventEntities) {
				descList.addAll(ee.getEventDescs());
			}
		}
		return descList;
	}
}
class EventEntity {
	private String[] events = null;
	private Class<?> eventClazz = null;
	private String param = null;
	private Class<?> jsEventClaszz = null;
	public EventEntity(String[] events, Class<?> eventClazz, String param, Class<?> jsEventClaszz) {
		this.events = events;
		this.eventClazz = eventClazz;
		this.param = param;
		this.jsEventClaszz = jsEventClaszz;
	}
	public List<JsEventDesc> getEventDescs() {
		List<JsEventDesc> list = new ArrayList<JsEventDesc>();
		for (String event : events) {
			JsEventDesc desc = new JsEventDesc(event, param);
			if (eventClazz != null) {
				desc.setEventClazz(eventClazz.getName());
			} else {
				desc.setEventClazz("");
			}
			if (jsEventClaszz != null) {
				desc.setJsEventClazz(jsEventClaszz.getName());
			} else {
				desc.setJsEventClazz("");
			}
			list.add(desc);
		}
		return list;
	}
}
