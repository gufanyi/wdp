package xap.lui.core.dataset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import com.alibaba.fastjson.annotation.JSONField;
import xap.lui.core.logger.LuiLogger;
public class PageData implements Serializable, Cloneable {
	private static final long serialVersionUID = -989226679182394744L;
	private List<Row> rows = new ArrayList<Row>();
	private boolean pageDataChanged = true;
	private boolean pageDataSelfChanged = true;
	private int pageindex;
	private List<Integer> selectIndices;
	private List<Row> deleteRowList;
	public PageData(){
		
	}
	public PageData(int pageindex) {
		this.pageindex = pageindex;
	}
	/**
	 * 添加一条记录信息
	 * 
	 * @param row
	 */
	public void addRow(Row row) {
		rows.add(row);
		pageDataChanged = true;
	}
	/**
	 * 在指定位置添加记录信息
	 * 
	 * @param index
	 * @param row
	 */
	public void insertRow(int index, Row row) {
		if (index < 0 || index > rows.size())
			throw new IllegalArgumentException("index参数异常!");
		if ((this.deleteRowList != null && this.deleteRowList.contains(row)) || rows.contains(row)) {
			Row newRow = (Row) row.clone();
			newRow.setRowId(row.getRowId() + "c");
			rows.add(index, newRow);
		} else {
			rows.add(index, row);
		}
		if (null != selectIndices) {
			int size = selectIndices.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Integer id = selectIndices.get(i);
					if (id.intValue() >= index) {
						selectIndices.set(i, (id + 1));
					}
				}
			}
		}
		pageDataChanged = true;
	}
	/**
	 * 删除一条记录信息
	 * 
	 * @param row
	 */
	public void removeRow(Row row) {
		int index = getRowIndex(row);
		removeRow(index);
	}
	/**
	 * 删除指定位置的记录信息
	 * 
	 * @param rindex
	 */
	public void removeRow(int rindex) {
		Row row = rows.remove(rindex);
		if (deleteRowList == null) {
			deleteRowList = new ArrayList<Row>();
		}
		deleteRowList.add(row);
		if (null != selectIndices) {
			int index = selectIndices.indexOf(rindex);
			if (index != -1)
				selectIndices.remove(index);
			int size = selectIndices.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Integer id = selectIndices.get(i);
					if (id.intValue() > rindex) {
						selectIndices.set(i, (id - 1));
					}
				}
			}
		}
		pageDataChanged = true;
	}
	/**
	 * 删除一条记录信息,真删除或假删除
	 * 
	 * @param row
	 * @param isTrueRemove
	 */
	public void removeRow(Row row, boolean isTrueRemove) {
		if (isTrueRemove)
			removeRow(row);
		else {
			row.setState(Row.STATE_FALSE_DELETED);
		}
	}
	/**
	 * 删除一条记录信息,真删除或假删除
	 * 
	 * @param rindex
	 * @param isTrueRemove
	 */
	public void removeRow(int rindex, boolean isTrueRemove) {
		if (isTrueRemove)
			removeRow(rindex);
		else {
			Row row = rows.get(rindex);
			row.setState(Row.STATE_FALSE_DELETED);
		}
	}
	public Row getRow(int index) {
		return rows.get(index);
	}
	public Row[] getRows() {
		return rows.toArray(new Row[0]);
	}
	
	public boolean isPageDataChanged() {
		Iterator<Row> it = rows.iterator();
		while (it.hasNext()) {
			Row row=it.next();
			if(row==null){
				continue;
			}
			if (row.isRowChanged())
				return true;
		}
		if(pageDataChanged){
			return true;
		}
		return pageDataSelfChanged;
	}
	
	public void setPageDataChanged(boolean pageDataChanged) {
		this.pageDataChanged = pageDataChanged;
	}
	
	public boolean isPageDataSelfChanged() {
		return pageDataSelfChanged;
	}
	public void setPageDataSelfChanged(boolean pageDataSelfChanged) {
		this.pageDataSelfChanged = pageDataSelfChanged;
	}
	public int getPageindex() {
		return pageindex;
	}
	public void setPageindex(int pageindex) {
		this.pageindex = pageindex;
	}
	
	public Row getSelectedRow() {
		Row[] rows = getSelectedRows();
		if (rows != null && rows.length > 0)
			return rows[rows.length - 1];
		return null;
	}
	
	public Row[] getSelectedRows() {
		if (selectIndices == null || selectIndices.size() == 0)
			return null;
		Row[] selRows = new Row[selectIndices.size()];
		Row[] rows = this.getRows();
		if(rows!=null&&rows.length>0){
			for (int i = 0; i < selRows.length; i++) {
				selRows[i] = rows[selectIndices.get(i)];
			}
		}
		return selRows;
	}
	
	public Row[] getFalseDeleteRows() {
		List<Row> delRowList = new ArrayList<Row>();
		for (Row r : rows) {
			if (r.getState() == Row.STATE_FALSE_DELETED) {
				delRowList.add(r);
			}
		}
		return delRowList.toArray(new Row[0]);
	}
	@JSONField(serialize = true)
	public Integer[] getSelectIndices() {
		if (selectIndices == null)
			return null;
		return selectIndices.toArray(new Integer[0]);
	}
	public void setRowSelectIndices(Integer[] selectIndices) {
		if (this.selectIndices == null)
			this.selectIndices = new ArrayList<Integer>();
		else
			this.selectIndices.clear();
		if (selectIndices != null)
			this.selectIndices.addAll(Arrays.asList(selectIndices));
		pageDataChanged = true;
	}
	public int getRowIndex(Row row) {
		int size = rows.size();
		for (int i = 0; i < size; i++) {
			if (rows.get(i).getRowId().equals(row.getRowId()))
				return i;
		}
		return -1;
	}
	
	public Row[] getDeleteRows() {
		return deleteRowList == null ? null : deleteRowList.toArray(new Row[0]);
	}
	
	public int getCurrentPageRowCount() {
		return rows.size();
	}
	public void setRowSelectIndex(int index) {
		setRowSelectIndices(new Integer[] {
			index
		});
	}

	public void clear() {
		this.rows.clear();
		this.pageDataChanged = true;
		this.pageDataSelfChanged = true;
		this.selectIndices = null;
		this.deleteRowList = null;
	}
	public Row getRowById(String id) {
		Iterator<Row> it = this.rows.iterator();
		while (it.hasNext()) {
			Row row = it.next();
			if (row.getRowId().equals(id))
				return row;
		}
		return null;
	}
	public void moveRow(int index, int targetIndex) {
		if (index < 0 || index > this.rows.size() - 1 || targetIndex < 0 || targetIndex > this.rows.size() - 1)
			return;
		else if (index == targetIndex)
			return;
		else {
			Row row = this.rows.get(index);
			this.rows.remove(index);
			this.rows.add(targetIndex, row);
			this.setPageDataSelfChanged(true);
		}
	}
	public void swapRow(Row row1, Row row2) {
		int index1 = getRowIndex(row1), index2 = getRowIndex(row2);
		if (index1 == -1 || index2 == -1) {
			return;
		}
		rows.set(index1, row2);
		rows.set(index2, row1);
		this.setPageDataSelfChanged(true);
	}
	public Object clone() {
		try {
			PageData rs = (PageData) super.clone();
			rs.rows = new ArrayList<Row>();
			Iterator<Row> it = this.rows.iterator();
			while (it.hasNext())
				rs.rows.add((Row) it.next().clone());
			if (this.deleteRowList != null) {
				rs.deleteRowList = new ArrayList<Row>();
				it = this.deleteRowList.iterator();
				while (it.hasNext())
					rs.deleteRowList.add((Row) it.next().clone());
			}
			return rs;
		} catch (CloneNotSupportedException e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return null;
	}
}
