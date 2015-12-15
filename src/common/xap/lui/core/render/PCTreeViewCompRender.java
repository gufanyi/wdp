package xap.lui.core.render;
import java.util.Iterator;

import xap.lui.core.comps.RecursiveTreeLevel;
import xap.lui.core.comps.SimpleTreeLevel;
import xap.lui.core.comps.TreeLevel;
import xap.lui.core.comps.TreeViewComp;
import xap.lui.core.comps.WebTreeNode;
import xap.lui.core.event.IEventSupport;
import xap.lui.core.event.TreeNodeDragEvent;
import xap.lui.core.event.TreeNodeEvent;
import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.layout.UITreeComp;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.model.LuiPageContext;
import xap.lui.core.util.StringUtil;
/**
 * @author renxh 树形控件渲染器
 */
@SuppressWarnings("unchecked")
public class PCTreeViewCompRender extends UINormalComponentRender<UITreeComp, TreeViewComp> {
	public PCTreeViewCompRender( TreeViewComp webEle) {
		super( webEle);
	}
	
	public String createBody() {
		TreeViewComp tree = this.getWebElement();
		if (tree == null)
			throw new LuiRuntimeException("没有找到tree:" + getId());
		StringBuilder buf = new StringBuilder();
		String treeId = getVarId();
		buf.append(" var " + treeId + " = $( \"#"+getDivId()+"\" ).tree({id: \""+tree.getId()+"\",");
		buf.append(" left:0, top:0, width:\"100%\", height:\"100%\", position:\"relative\",");
		buf.append("withCheckBox : "+tree.isShowCheckBox() + ",rootOpen:" + tree.isExpand() + ",withRoot:" + tree.isShowRoot() + ", rootNode:null, className:null, attr:{flowmode:").append(isFlowMode()).append(",canEdit:").append(tree.isEdit()).append(",treeNodeTextMaxWidth:").append(tree.getTreeNodeTextMaxWidth());
		if (tree.getImgRender() == null)
			buf.append("}}).tree(\"instance\");\n");
		else
			buf.append(",imgRender:").append(tree.getImgRender()).append("}}).tree(\"instance\");\n");
		buf.append("pageUI.getViewPart('" + this.getCurrWidget().getId() + "').addComponent(" + treeId + ");\n");
		if (tree.isShowRoot()) {
			String rootCaption = translate(tree.getI18nName(), tree.getText(), tree.getLangDir());
			buf.append(treeId).append(".rootCaption=\"").append(rootCaption).append("\";\n");
		}
		boolean dragEnable = tree.isDrag();
		if (dragEnable)
			buf.append(treeId).append(".setDragEnabled(true);\n");
		// 设置复选模式
		int checkBoxModel = tree.getCheckboxType();
		if (checkBoxModel != -1)
			buf.append(treeId).append(".setCheckBoxModel(" + checkBoxModel + ");\n");
		// 以静态树优先
		if (tree.getTreeModel() != null) {
			WebTreeNode root = tree.getTreeModel().getRootNode();
			buf.append(genScriptForTreeNode(null, root));
		} else {
			TreeLevel level = tree.getTopLevel();
			if (level != null) {
				buf.append(genScriptForTreeLevel(level));
			}
		}
		
		if (tree.isVisible() == false)
			buf.append(treeId).append(".hideV();\n");
		
		TreeLevel level = tree.getTopLevel();
		if(level != null) {
			String levelId = TL_PRE + getCurrWidget().getId() + "_" + level.getId();
			if (level != null) {
				buf.append(getVarId()).append(".setTreeLevel(").append(levelId).append(");\n");
			}
			if (tree.getOpenLevel() != -1) {
				buf.append( treeId +".currTreeComp = " + getVarId() + ";\n");
				buf.append("setTimeout(\"openNodesByLevel(" + tree.getOpenLevel() + ",'" + getId() + "')\",500);\n");
			}
			if (tree.isSelectRoot() == true) {
				buf.append(treeId +"currTreeComp = " + getVarId() + ";\n");
				buf.append("setTimeout(\"TreeNode.selectRoot()\",100);\n");
			}
		}
		
		return buf.toString();
	}
	

	
	
	private String genScriptForTreeLevel(TreeLevel level) {
		StringBuilder buf = new StringBuilder();
		String levelId = TL_PRE + getCurrWidget().getId() + "_" + level.getId();
		// 在获取labelFileds的时候根据多语环境获取
		String labelFieldsStr = level.getLabelFields();
		if (labelFieldsStr.indexOf(",") != -1) {
			String[] labelFieldArray = labelFieldsStr.split(",");
			String resultLabelFields = "";
			for (int i = 0; i < labelFieldArray.length; i++) {
				String newField =labelFieldArray[i];
				if (newField != null) {
					resultLabelFields += newField + ",";
				}
			}
			if (resultLabelFields.indexOf(",") != -1) {
				labelFieldsStr = resultLabelFields.substring(0, resultLabelFields.lastIndexOf(","));
			}
		} 
		if (level.getType().equals(TreeLevel.TREE_LEVEL_TYPE_RECURSIVE)) {
			RecursiveTreeLevel rLevel = (RecursiveTreeLevel) level;
			/**
			 * js控件的构造参数,注意顺序 ----------------------------------- id, dataset,
			 * type, recursivePkField, recursivePPkField, labelFields,
			 * masterKeyField, detailKeyParameter, labelDelims, loadField
			 * ------------------------------------
			 */
			buf.append("").append(levelId).append(" = ").append("new TreeLevel(\"")
			// -----------------参数-------------
					.append(rLevel.getId()).append("\", ").append(getDatasetVarShowId(rLevel.getDataset(), getCurrWidget().getId())).append(",\"").append(rLevel.getType()).append("\",\"").append(rLevel.getRecursiveField()).append("\",\"").append(rLevel.getRecursiveParentField()).append("\",").append(StringUtil.mergeScriptArray(labelFieldsStr)).append(",\"").append(rLevel.getMasterField()).append("\",\"").append(rLevel.getDetailKeyParameter()).append("\",").append(
							StringUtil.mergeScriptArray(rLevel.getLabelDelims())).append(",\"").append(rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"")
					// ----------------------------------
					.append(");\n");
			if (rLevel.getContextMenu() != null) {
				buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
			}
		}
		// Simple Tree
		else {
			SimpleTreeLevel rLevel = (SimpleTreeLevel) level;
			buf.append("").append(levelId).append(" = ").append("new TreeLevel(\"")
			// ---------------参数---------------
					.append(rLevel.getId()).append("\", ").append(getDatasetVarShowId(rLevel.getDataset(), getCurrWidget().getId())).append(",\"").append(rLevel.getType()).append("\",null,null,")
					// 根据多语环境设置label
					.append(StringUtil.mergeScriptArray(labelFieldsStr)).append(",").append(StringUtil.mergeScriptArray(rLevel.getMasterField())).append(",").append(StringUtil.mergeScriptArray(rLevel.getDetailKeyParameter())).append(",").append(StringUtil.mergeScriptArray(rLevel.getLabelDelims())).append(",\"").append(rLevel.getLoadField() == null ? "" : rLevel.getLoadField()).append("\"")
					// ---------------------------------
					.append(");\n");
			if (rLevel.getContextMenu() != null) {
				buf.append(addContextMenu(rLevel.getContextMenu(), levelId));
			}
		}
		if (level.getChildTreeLevel() != null) {
			TreeLevel childLevel = level.getChildTreeLevel();
			if (childLevel != null) {
				buf.append(genScriptForTreeLevel(childLevel));
				String cId = TL_PRE + getCurrWidget().getId() + "_" + childLevel.getId();
				buf.append(levelId).append(".addTreeLevel(").append(cId).append(");\n");
			}
		}
		return buf.toString();
	}
	private String genScriptForTreeNode(String pId, WebTreeNode node) {
		StringBuilder buf = new StringBuilder();
		buf.append("var ").append(node.getId()).append(" = new TreeNode(").append("\"").append(node.getId()).append("\"").append(",").append("\"").append(node.getLabel()).append("\"").append(",").append("\"").append(node.getValue()).append("\"").append(",").append("{").append("open:").append(true).append(",").append("isLeaf:").append(true).append(",").append("reload:").append(false).append("}").append(",").append("null").append(",").append("null").append(");\n");
		if (pId != null)
			buf.append(pId).append(".add(").append(node.getId()).append(");\n");
		else {
			buf.append(COMP_PRE).append(getId()).append(".addRoot(").append(node.getId()).append(");\n");
		}
		if (node.getChildNodeList() != null) {
			Iterator<WebTreeNode> it = node.getChildNodeList().iterator();
			while (it.hasNext()) {
				buf.append(genScriptForTreeNode(node.getId(), it.next()));
			}
		}
		return buf.toString();
	}
	protected String getSourceType(IEventSupport ele) {
		return LuiPageContext.SOURCE_TYPE_TREE;
	}
	protected void addProxyParam(StringBuilder buf, String eventName) {
		if (TreeNodeDragEvent.ON_DRAG_START.equals(eventName)) {
			buf.append("proxy.addParam('sourceNodeRowId', treeNodeDragEvent.sourceNode.nodeData.rowId);\n");
		} else if (TreeNodeDragEvent.ON_DRAG_END.equals(eventName)) {
			buf.append("proxy.addParam('sourceNodeRowId', treeNodeDragEvent.sourceNode.nodeData.rowId);\n");
			buf.append("proxy.addParam('targetNodeRowId', treeNodeDragEvent.targetNode.nodeData.rowId);\n");
		} else if (TreeNodeEvent.ON_DBCLICK.equals(eventName) || TreeNodeEvent.ON_CLICK.equals(eventName)) {
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData == null ? null : treeNodeEvent.node.nodeData.rowId);\n");
		} else if (TreeNodeEvent.ON_CHECKED.equals(eventName)) {
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData.rowId);\n");
		} else if (TreeNodeEvent.AFTER_SEL_NODE_CHANGE.equals(eventName)) {
			buf.append("proxy.addParam('nodeRowId', treeNodeEvent.node.nodeData== null?null:treeNodeEvent.node.nodeData.rowId);\n");
			buf.append("proxy.addParam('datasetId', treeNodeEvent.node.nodeData== null?null:treeNodeEvent.node.nodeData.dataset.id);\n");
		}
	}
	public String createDesignHead() {
		if (this.isEditMode() && getDivId() != null) {
			return toResize("$(\"#" + getDivId() + "\")[0]", "editableDivResize");
		}
		return "";
	}
	@LuiPhase(phase={LifeCyclePhase.ajax})
	public void setTreeLevel(TreeLevel level) {
		StringBuilder buf = new StringBuilder();
		TreeViewComp tree = this.getWebElement();
		buf.append("var treeObj = pageUI.getViewPart('" + this.getCurrWidget().getId() + "').getComponent('" +tree.getId() + "');\n");
		if (level != null) {
			buf.append(genScriptForTreeLevel(level));
			String levelId = TL_PRE + getCurrWidget().getId() + "_" + level.getId();
			if (level != null) {
				buf.append("treeObj.setTreeLevel(").append(levelId).append(");\n");
			}
		}
		addBeforeExeScript(buf.toString());
	}
	
}
