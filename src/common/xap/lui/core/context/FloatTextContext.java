package xap.lui.core.context;

/**
 * FloatTextComp的context
 * @author zhangxya
 *
 */
public class FloatTextContext extends TextContext {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//精度
	private String precision;

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

}
