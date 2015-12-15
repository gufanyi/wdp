package xap.lui.core.dataset;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
* 根据解析需要由内部类提成外部类，主要描述配置文件中WhereOrder元素。
 *
 * create on 2007-3-5 下午03:31:07
 *
 * @author lkp 
  */
@XmlRootElement(name="WhereOrder")
@XmlAccessorType(XmlAccessType.NONE)
public class WhereOrder implements Serializable,Cloneable{
	
	private static final long serialVersionUID = 1L;
    @XmlAttribute
	private String beforWhere = null;
    @XmlAttribute
	private String afterWhere = null;
    @XmlAttribute
	private String order = null;
	/**
	 * 缺省构造
	 *
	 */
	public WhereOrder() {
	}

	/**
	 * 带参构造
	 * @param where
	 * @param order
	 */
	public WhereOrder(String beforWhere,String afterWhere, String order) {
		this.beforWhere=beforWhere;
		this.afterWhere=afterWhere;
		this.order=order;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getBeforWhere() {
		return beforWhere;
	}

	public void setBeforWhere(String beforWhere) {
		this.beforWhere = beforWhere;
	}

	public String getAfterWhere() {
		return afterWhere;
	}

	public void setAfterWhere(String afterWhere) {
		this.afterWhere = afterWhere;
	}
	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}

