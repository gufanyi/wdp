package xap.lui.core.dataset;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 根据解析需要由内部类提成外部类，主要描述配置文件中matchfield元素。
 *
 * create on 2007-3-5 下午03:30:24
 *
 * @author lkp 
 */
@XmlRootElement(name="Field")
@XmlAccessorType(XmlAccessType.NONE)
public class MatchField implements Serializable,Cloneable{


	private static final long serialVersionUID = 1L;

	/*读取字段*/
	@XmlAttribute
	private String readField = null;

	/*写入字段*/
	@XmlAttribute
	private String writeField = null;
	@XmlAttribute
	private String ismacth;
	@XmlAttribute
	private String iscontains;
	
	public String getIsmacth() {
		return ismacth;
	}

	public void setIsmacth(String ismacth) {
		this.ismacth = ismacth;
	}

	public String getIscontains() {
		return iscontains;
	}

	public void setIscontains(String iscontains) {
		this.iscontains = iscontains;
	}

	/**
	 * 缺省构造
	 *
	 */
	public MatchField() {

	}

	/**
	 * 带参构造
	 * @param read
	 * @param write
	 */
	public MatchField(String read, String write) {

		this.readField = read;
		this.writeField = write;
	}

	/**
	 * 获取读取字段
	 * @return
	 */
	public String getReadField() {
		return readField;
	}

	/**
	 * 设置读取字段
	 * @param readField
	 */
	public void setReadField(String readField) {
		this.readField = readField;
	}

	/**
	 * 获取写入字段
	 * @return
	 */
	public String getWriteField() {
		return writeField;
	}

	/**
	 * 设置写入字段
	 * @param writeField
	 */
	public void setWriteField(String writeField) {
		this.writeField = writeField;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

