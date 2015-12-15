package xap.lui.core.comps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

import xap.lui.core.context.BaseContext;
import xap.lui.core.context.TreeViewContext;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCTextCompRender;
import xap.lui.core.render.PCTreeViewCompRender;
import xap.lui.core.render.notify.RenderProxy;

/**
 * Tree控件描述类
 * 
 * @author dengjt
 * 
 */
@XmlRootElement(name = "Tree")
@XmlAccessorType(XmlAccessType.NONE)
public class TreeViewComp extends WebComp implements IDataBinding, IDetachable {

	private static final long serialVersionUID = 820546587109276281L;
	public static final String WIDGET_NAME = "tree";
	public static final String ADD_TREELEVEL = "addTreeLevel";

	private WebTreeModel treeModel;
	// @XmlElement(name="RecursiveTreeLevel")
	@XmlElementRefs({ @XmlElementRef(name = "RecursiveTreeLevel", type = RecursiveTreeLevel.class) })
	private TreeLevel topLevel = null;
	// 是否可以拖拽
	@XmlAttribute(name = "isDrag")
	private boolean isDrag;
	// 是否展开根节点
	@XmlAttribute(name = "isExpand")
	private boolean isExpand = true;
	// 默认展开级别
	@XmlAttribute
	private int openLevel = -1;
	// 树根节点默认显示值
	@XmlAttribute
	private String text = null;
	@XmlAttribute
	private String i18nName;// 多语资源
	@XmlAttribute(name = "isShowRoot")
	private boolean isShowRoot = true;
	// 是否是CheckBoxTree
	@XmlAttribute(name = "isShowCheckBox")
	private boolean isShowCheckBox = false;
	// 复选策略 0:只设置自己 1:设置自己和子 2:设置自己和子和父
	@XmlAttribute(name = "checkboxType")
	private int checkboxType = 0;
	@XmlAttribute(name = "isEdit")
	private boolean isEdit = false;
	@XmlAttribute
	private String imgRender = null;
	@XmlAttribute
	private String langDir;// 多语目录

	// 显示名称
	@XmlAttribute
	private String caption;

	// 选中根节点
	@XmlAttribute(name = "isSelectRoot")
	private boolean isSelectRoot = false;
	// 树节点文字显示最大宽度,-1不限制.
	@XmlAttribute
	private int treeNodeTextMaxWidth = -1;

	private PCTreeViewCompRender render = null;

	@Override
	public String getWidgetName() {
		return WIDGET_NAME;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public TreeViewComp() {
		super();
	}

	public TreeViewComp(String id) {
		super(id);
	}

	public TreeViewComp(WebTreeModel mode) {

	}

	public WebTreeModel getTreeModel() {
		return treeModel;
	}

	public void setTreeModel(WebTreeModel model) {
		this.treeModel = model;
	}

	public String getDataset() {
		if (topLevel == null)
			return null;
		return topLevel.getDataset();
	}

	public TreeLevel getTopLevel() {
		return topLevel;
	}

	public void setTopLevel(TreeLevel topLevel) {
		this.topLevel = topLevel;
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().setTreeLevel(topLevel);
		}

	}

	public boolean isDrag() {
		return isDrag;
	}

	public void setDrag(boolean isDrag) {
		this.isDrag = isDrag;
	}

	public Object clone() {
		TreeViewComp comp = (TreeViewComp) super.clone();
		if (this.topLevel != null)
			comp.setTopLevel((TreeLevel) this.topLevel.clone());
		if (this.treeModel != null)
			comp.setTreeModel((WebTreeModel) this.treeModel.clone());
		return comp;
	}

	public int getOpenLevel() {
		return openLevel;
	}

	public void setOpenLevel(int openLevel) {
		this.openLevel = openLevel;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	public void mergeProperties(LuiElement ele) {
		super.mergeProperties(ele);
	}

	public String getLangDir() {
		return langDir;
	}

	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String name) {
		i18nName = name;
	}

	public boolean isShowRoot() {
		return isShowRoot;
	}

	public void setShowRoot(boolean withRoot) {
		this.isShowRoot = withRoot;
	}

	@Override
	public BaseContext getContext() {
		TreeViewContext ctx = new TreeViewContext();
		ctx.setEnabled(this.enabled);
		ctx.setText(this.text);
		return ctx;
	}

	@Override
	public void setContext(BaseContext ctx) {
		TreeViewContext treectx = (TreeViewContext) ctx;
		this.setEnabled(treectx.isEnabled());
		this.setShowRoot(treectx.isWithRoot());
		this.setTreeModel(treectx.getTreeModel());
		this.setText(treectx.getText());
		this.setCtxChanged(false);
	}
	
	@Override
	public void setVisible(boolean visible) {
		if (visible != this.isVisible) {
			this.isVisible = visible;
			if (LifeCyclePhase.ajax.equals(getPhase())) {
			     this.getRender().setVisible(visible);
			}
		}
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (text == null)
			return;
		if (!text.equals(this.text)) {
			if (LifeCyclePhase.ajax.equals(getPhase())) {
				this.i18nName = null;
			}
			this.text = text;
			this.setCtxChanged(true);
		}
	}

	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			// buffer.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("bc",
			// "TreeViewComp-000000")/*树的ID不能为空!\r\n*/);
			buffer.append("树的ID不能为空!\r\n");
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());
	}

	public boolean isShowCheckBox() {
		return isShowCheckBox;
	}

	public void setShowCheckBox(boolean isShowCheckBox) {
		this.isShowCheckBox = isShowCheckBox;
	}

	public int getCheckboxType() {
		return checkboxType;
	}

	public void setCheckboxType(int checkboxType) {
		this.checkboxType = checkboxType;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getImgRender() {
		return imgRender;
	}

	public void setImgRender(String imgRender) {
		this.imgRender = imgRender;
	}

	@Override
	public void detach() {
		treeModel = null;
	}

	public boolean isSelectRoot() {
		return isSelectRoot;
	}

	public void setSelectRoot(boolean isSelectRoot) {
		this.isSelectRoot = isSelectRoot;
	}

	public int getTreeNodeTextMaxWidth() {
		return treeNodeTextMaxWidth;
	}

	public void setTreeNodeTextMaxWidth(int treeNodeTextMaxWidth) {
		this.treeNodeTextMaxWidth = treeNodeTextMaxWidth;
	}

	@Override
	public PCTreeViewCompRender getRender() {
		if (render == null) {
			render = RenderProxy.getRender(new PCTreeViewCompRender(this));
		}
		return render;
	}

}
