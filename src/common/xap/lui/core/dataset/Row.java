package xap.lui.core.dataset;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import xap.lui.core.logger.LuiLogger;
/**
 * 记录信息。在类仅存储数据信息，是DataSet的数据载体。它和 Field相互配合构成了DataSet的数据和结构信息。
 *
 */
public class Row implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 1L;
	public static final int STATE_NORMAL = 0;
	public static final int STATE_UPDATE = 1;
	public static final int STATE_ADD = 2;
	public static final int STATE_DELETED = 3;
	public static final int STATE_FALSE_DELETED = 4;
	
	private String rowId;
	private boolean isAllowMouseoverChange = true;  //是否允许鼠标悬浮改变行颜色
	private boolean isEdit = true;
	private int state;//行状态
	private Object[] content = null;
	private String parentId;
	private boolean rowChanged = true;
	/** 发生change的列索引 */
	private List<Integer> changedIndices = null;
	public Row(String rowId, int size) {
		this.rowId = rowId;
		content = new Object[size];
	}
	protected Row(String rowId) {
		this.rowId = rowId;
	}
	public boolean isAllowMouseoverChange() {
		return isAllowMouseoverChange;
	}
	public void setAllowMouseoverChange(boolean isAllowMouseoverChange) {
		if(this.isAllowMouseoverChange!=isAllowMouseoverChange) {
			this.isAllowMouseoverChange = isAllowMouseoverChange;
			this.rowChanged = true;   //不设置时，会认为row没变化，就不解析传到前台
		}
	}
	/**
	 * 获取rowId
	 * 
	 * @return
	 */
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String id) {
		this.rowId = id;
		rowChanged = true;
	}
	/**
	 * 设置parentId
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
		rowChanged = true;
	}
	/**
	 * 获取parentId
	 * 
	 * @return
	 */
	public String getParentId() {
		return this.parentId;
	}
	/**
	 * 获取当前记录的状态信息。
	 * 
	 * @return
	 */
	public int getState() {
		return this.state;
	}
	/**
	 * 设置当前记录的状态信息。
	 * 
	 * @param state
	 */
	public void setState(int state) {
		if (this.state != state) {
			this.state = state;
			rowChanged = true;
		}
	}
	/**
	 * 获取当前行有多少个元素
	 * 
	 * @return
	 */
	public int size() {
		if (content != null)
			return content.length;
		else
			return 0;
	}
	/**
	 * 添加指定个数的空列结构，用于动态增加记录结构。
	 *
	 */
	public void addColumn(int number) {
		int numbers = number + content.length;
		Object[] newContent = new Object[numbers];
		System.arraycopy(content, 0, newContent, 0, content.length);
		this.content = newContent;
		rowChanged = true;
	}
	/**
	 * 增加一个空列结构。
	 *
	 */
	public void addColumn() {
		this.addColumn(1);
	}
	/**
	 * 设置某个字段的值
	 * 
	 * @param index
	 * @param value
	 */
	public void setValue(int index, Object value) {
		if (content == null)
			return;
		Object oldValue = content[index];
		content[index] = value;
		rowChanged = true;
		if (oldValue != value)
			addChangedIndex(index);
	}
	/**
	 * 获得记录中某个字段的值对象
	 * 
	 * @param fieldName
	 * @return
	 */
	public Object getValue(int index) {
		if (content == null)
			return null;
		return content[index];
	}
	/**
	 * 设置BigDecimal的值
	 * 
	 * @param index
	 * @param value
	 */
	public void setBigDecimal(int index, BigDecimal value) {
		setValue(index, value);
	}
	/**
	 * 设置int值
	 * 
	 * @param index
	 * @param value
	 */
	public void setInt(int index, int value) {
		setValue(index, Integer.valueOf(value));
	}
	/**
	 * 设置Integer值
	 * 
	 * @param index
	 * @param value
	 */
	public void setInteger(int index, Integer value) {
		setValue(index, value);
	}
	/**
	 * 设置String 值
	 * 
	 * @param index
	 * @param value
	 */
	public void setString(int index, String value) {
		setValue(index, value);
	}
	/**
	 * 设置boolean值
	 * 
	 * @param index
	 * @param value
	 */
	public void setBoolean(int index, boolean value) {
		setValue(index, new Boolean(value));
	}
	public void setByte(int index, byte value) {
		setValue(index, Byte.valueOf(value));
	}
	public void setDate(int index, Date value) {
		setValue(index, value);
	}
	public void setDouble(int index, double value) {
		setValue(index, new Double(value));
	}
	public void setFloat(int index, float value) {
		setValue(index, new Float(value));
	}
	public void setLong(int index, long value) {
		setValue(index, Long.valueOf(value));
	}
	public void setChar(int index, char value) {
		setValue(index, Character.valueOf(value));
	}
	public void setNull(int index) {
		setValue(index, null);
	}
	public BigDecimal getBigDecimal(int index) {
		return (BigDecimal) content[index];
	}
	public int getInt(int index) {
		return ((Integer) content[index]).intValue();
	}
	public String getString(int index) {
		return (String) content[index];
	}
	public boolean getBoolean(int index) {
		return ((Boolean) content[index]).booleanValue();
	}
	public byte getByte(int index) {
		return ((Byte) content[index]).byteValue();
	}
	public Date getDate(int index) {
		return (Date) content[index];
	}
	public double getDouble(int index) {
		return ((Double) content[index]).doubleValue();
	}
	public float getFloat(int index) {
		return ((Float) content[index]).floatValue();
	}
	public long getLong(int index) {
		return ((Long) content[index]).longValue();
	}
	public char getChar(int index) {
		return ((Character) content[index]).charValue();
	}
	public boolean isNull(int index) {
		if (index < 0 || index > content.length - 1)
			throw new IllegalArgumentException("index:" + index + "越界");
		return (content[index] == null);
	}
	public void clearContent() {
		this.content = new Object[size()];
		rowChanged = true;
	}
	public Object clone() {
		try {
			Row row = (Row) super.clone();
			if (content != null)
				row.content = content.clone(); // 此内容只会出现String,Integer等类型，所以此处已经足够。
			return row;
		} catch (CloneNotSupportedException e) {
			LuiLogger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public boolean isRowChanged() {
		return rowChanged;
	}
	public void setRowChanged(boolean rowChanged) {
		this.rowChanged = rowChanged;
		if (!rowChanged)
			this.getChangedIndices().clear();
	}
	public Object[] getContent() {
		return content;
	}
	public void setContent(Object[] content) {
		this.content = content;
	}
	
	public List<Integer> getChangedIndices() {
		if (changedIndices == null) {
			changedIndices = new ArrayList<Integer>();
		}
		return changedIndices;
	}
	private void addChangedIndex(Integer index) {
		if (changedIndices == null) {
			changedIndices = new ArrayList<Integer>();
		}
		if (changedIndices.indexOf(index) == -1)
			changedIndices.add(index);
	}
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < content.length; i++) {
			buf.append(content[i]);
			buf.append(",");
		}
		return buf.toString();
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		if(this.isEdit != isEdit) {
			this.isEdit = isEdit;
			rowChanged = true;
		}
	}
	
}
