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

import xap.lui.core.exception.LuiRuntimeException;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.PCGridCompRender;


/**
 * Grid多表头配置类
 * 
 */
@XmlRootElement(name="ColumnGroup")
@XmlAccessorType(XmlAccessType.NONE)
public class GridColumnGroup extends LuiElement implements IGridColumn {
	private static final long serialVersionUID = 1L;
    
	@XmlAttribute
	private String i18nName;
	@XmlAttribute
	private String langDir;
	@XmlAttribute
	private boolean isVisible = true;
	@XmlAttribute
	private String text;
	
	protected GridComp grid;
	// 存储此顶级表头下的所有headers
	@XmlElementRefs({ @XmlElementRef(name = "Column", type = GridColumn.class), @XmlElementRef(name = "ColumnGroup", type = GridColumnGroup.class)
    })	
	//private List<GridColumn> childColumnList = null;
	private List<IGridColumn> childColumnList = null;
	
	private PCGridCompRender render;
	
	public List<IGridColumn> getChildColumnList() {
		return childColumnList;
	}

	public void setChildColumnList(List<IGridColumn> childColumnList) {
		this.childColumnList = childColumnList;
		if(childColumnList != null && childColumnList.size() > 0){
			Iterator<IGridColumn> it = childColumnList.iterator();
			while(it.hasNext()){
				IGridColumn col = it.next();
				col.setGridComp(grid);
			}
		}
	}
	

	public void addColumn(IGridColumn column) {
		if(childColumnList == null)
			childColumnList = new ArrayList<IGridColumn>();
		childColumnList.add(column);
		column.setGridComp(grid);
		if(column instanceof GridColumnGroup){
			GridColumnGroup gridgroup = (GridColumnGroup) column;
			List<IGridColumn> children = gridgroup.getChildColumnList();
			if (children != null && children.size() > 0) {
				for (int j = 0; j < children.size(); j++) {
					IGridColumn colu = children.get(j);
					colu.setGridComp(grid);
				}
			}
		}
	}
	
	public IGridColumn getColumnById(String id){
		Iterator<IGridColumn> it = this.getChildColumnList().iterator();
		while (it.hasNext()) {
			IGridColumn inner = it.next();
			if (inner instanceof GridColumn) {
				GridColumn column = (GridColumn) inner;
				if (column.getId().equals(id))
					return column;
			} else if (inner instanceof GridColumnGroup) {
				GridColumnGroup gridColGroup = (GridColumnGroup) inner;
				if (gridColGroup.getId().equals(id)) {
					return gridColGroup;
				}else{
					if(gridColGroup.getColumnById(id) == null)
						continue;
					return gridColGroup.getColumnById(id);
				}
			}
		}
		return null;
	}
	public IGridColumn getColumnByField(String field){
		Iterator<IGridColumn> it = this.getChildColumnList().iterator();
		while (it.hasNext()) {
			IGridColumn inner = it.next();
			if (inner instanceof GridColumn) {
				GridColumn column = (GridColumn) inner;
				if (column.getField().equals(field))
					return column;
			} else if (inner instanceof GridColumnGroup) {
				GridColumnGroup gridColGroup = (GridColumnGroup) inner;
				return gridColGroup.getColumnByField(field);
			}
		}
		return null;
	}
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		if(this.text != text) {
			this.text = text;
			if(LifeCyclePhase.ajax.equals(getPhase()))
			    this.getRender().setColumnText(this);
		}
	}
	
	public void removeColumn(String fieldId)
	{
		if(fieldId == null)
			return;
		if(this.childColumnList != null)
		{
			for(int i = 0; i < this.childColumnList.size(); i++)
			{
				if(this.childColumnList.get(i) instanceof GridColumn)
				{
					GridColumn colum = (GridColumn)this.childColumnList.get(i);
					if(colum.getId() != null && colum.getId().equals(fieldId))
						this.childColumnList.remove(i);
				}
			}
		}
	}
	public void removeColumn(IGridColumn col) {
		childColumnList.remove(col);
		if (LifeCyclePhase.ajax.equals(getPhase())) {
			this.getRender().deleteColumn(col);
		}
	}

	public String getI18nName() {
		return i18nName;
	}

	public void setI18nName(String showName) {
		this.i18nName = showName;
	}

	public String getLangDir() {
		return langDir;
	}
	public void setLangDir(String langDir) {
		this.langDir = langDir;
	}

	public Object clone() {
		GridColumnGroup group = (GridColumnGroup) super.clone();
		if (this.childColumnList != null) {
			group.childColumnList = new ArrayList<IGridColumn>();
			Iterator<IGridColumn> it = this.childColumnList.iterator();
			while (it.hasNext()) {
				group.addColumn((IGridColumn) it.next().clone());
			}
		}
		return group;

	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}
	
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}

	public GridComp getGridComp() {
		return this.grid;
	}

	public void setGridComp(GridComp grid) {
		this.grid = grid;
		
		List<IGridColumn> columnList = this.getChildColumnList();
		for(IGridColumn column : columnList) {
			column.setGridComp(grid);
		}
	}

	public PCGridCompRender getRender() {
		if(this.render == null) {
			this.render = this.getGridComp().getRender();
		}
		return this.render;
	}
}
