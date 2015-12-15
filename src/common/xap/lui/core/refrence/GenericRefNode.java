package xap.lui.core.refrence;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import xap.lui.core.comps.LuiElement;
import xap.lui.core.exception.LuiPluginException;
import xap.lui.core.exception.LuiRuntimeException;

@XmlRootElement(name="GenericRefNode")
@XmlAccessorType(XmlAccessType.NONE)
public class GenericRefNode extends BaseRefNode implements Cloneable {
	private static final long serialVersionUID = 3122374275495533964L;
	@XmlAttribute(name="isMultiple")
	private boolean isMultiple;// 是否允许多选
    @XmlAttribute(name="controller")
	private String controller;// 数据加载的listener
	@XmlAttribute(name="readDataset")
    private String readDataset;// 读数据集
	@XmlAttribute(name="writeDataset")
	private String writeDataset;// 写数据集合的字段
	@XmlAttribute
	private String readFields;// 读数据集的字段
	@XmlAttribute
	private String writeFields;// 写数据集的字段
	@XmlAttribute                 //---------------------------------
	private String delegator;// 主要用来处理OK事件的处理，默認走xap.lui.core.refrence.AppRefDftOkCmd
	@XmlAttribute
	private String refcode;// 这个用来获取refmodel
	@XmlAttribute(name="isQuickInput")
	private boolean isQuickInput = false;// // 允许手动输入
	@XmlAttribute
	private boolean isFilterNames = false;  //--------------------------
	@XmlAttribute(name="isOnlyLeaf")
	private boolean isOnlyLeaf = false;//只有叶子节点才能选中
	@XmlAttribute(name="winModel")
	private String winModel;//自定义UI模型 默认走 xap.lui.core.refrence.AppRefDftWindow
	@XmlAttribute
	private String pageMeta;
	@XmlAttribute
	private String filterSql;//过滤条件
	@XmlAttribute
	private String propertyGridId;

	public boolean isQuickInput() {
		return isQuickInput;
	}
	public void setQuickInput(boolean allowInput) {
		this.isQuickInput = allowInput;
	}
	public String getReadDataset() {
		return readDataset;
	}
	public void setReadDataset(String readDs) {
		this.readDataset = readDs;
	}
	public String getPagemeta() {
		return pageMeta;
	}
	public void setPagemeta(String pageMeta) {
		this.pageMeta = pageMeta;
	}
	public void mergeProperties(LuiElement ele) {
		throw new LuiRuntimeException("not implemented");
	}
	public boolean isMultiple() {
		return isMultiple;
	}
	public void setMultiple(boolean isMultiple) {
		this.isMultiple = isMultiple;
	}
	public boolean isOnlyLeaf() {
		return isOnlyLeaf;
	}
	public void setIsOnlyLeaf(boolean isOnlyLeaf) {
		this.isOnlyLeaf = isOnlyLeaf;
	}
	public String getWinModel() {
		return winModel;
	}
	public void setWinModel(String winModel) {
		this.winModel = winModel;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
		//this.notifyChange("updateDataListener", this);
		//this.getRender().u
	}
	public String getDelegator() {
		return delegator;
	}
	public void setDelegator(String delegator) {
		this.delegator = delegator;
	}
	public boolean isFilterNames() {
		return isFilterNames;
	}
	public void setFilterNames(boolean isFilterNames) {
		this.isFilterNames = isFilterNames;
	}
	public String getRefcode() {
		return refcode;
	}
	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}
	public String getWriteDataset() {
		return writeDataset;
	}
	public void setWriteDataset(String name) {
		this.writeDataset = name;
	}
	public Object clone() {
		return super.clone();
	}
	public String getReadFields() {
		return readFields;
	}
	public void setReadFields(String readFields) {
		this.readFields = readFields;
	}
	public String getWriteFields() {
		return writeFields;
	}
	public void setWriteFields(String writeFields) {
		this.writeFields = writeFields;
	}
	public void validate() {
		StringBuffer buffer = new StringBuffer();
		if (this.getId() == null || this.getId().equals("")) {
			buffer.append("参照的ID不能为空!");
		}
		if (buffer.length() > 0)
			throw new LuiPluginException(buffer.toString());
	}
	public String getFilterSql() {
		return filterSql;
	}
	public void setFilterSql(String filterSql) {
		this.filterSql = filterSql;
	}
	public String getPropertyGridId() {
		return propertyGridId;
	}
	public void setPropertyGridId(String propertyGridId) {
		this.propertyGridId = propertyGridId;
	}
	
	
}
