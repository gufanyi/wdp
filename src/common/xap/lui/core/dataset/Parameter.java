package xap.lui.core.dataset;

import xap.lui.core.dataset.LuiParameter;


/**
 * 参数信息对象
 */
public class Parameter extends LuiParameter{
	
	private static final long serialVersionUID = 1L;
	
	/*参数值*/
	private String value = null;
	
	/**
	 * 参数构造方法
	 * @param name
	 * @param value
	 */
	public Parameter(String name, String value){
		this(name, value, null);
	}
	
	public Parameter(String name, String value, String desc){
		super(name, desc);
		this.value = value;
	}
	
	
	/**
	 * 参数缺省构造方法
	 *
	 */
	public Parameter(){
		
	}
	
	/**
	 * 获取参数值
	 * @return
	 */
	public String getValue(){
		
		return this.value;
	}
	
	/**
	 * 设置参数值
	 * @param value
	 */
	public void setValue(String value){
		
		this.value = value;
	}
	
	
	/**
	 * 实现相等判断
	 */
	public boolean equals(Object obj) {
		Boolean bResult = false;
		if(obj==null){
			bResult = false;
		}else{
			if(!(obj instanceof Parameter))
				bResult =  false;
			if(obj == this)
				bResult =  true;
			if(((Parameter)obj).getName().trim().equals(this.getName()))
				bResult =  true;
		}
		return bResult;
	} 
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	/**
	 * 实现克隆方法
	 */
	public Object clone(){
		return super.clone();
	}


}

