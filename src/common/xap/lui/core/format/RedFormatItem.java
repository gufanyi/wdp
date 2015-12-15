
package xap.lui.core.format;



public class RedFormatItem extends FormatItem 
{

	
	private static final long serialVersionUID = -864477111146743896L;

	private boolean isRed = false;
	
	public RedFormatItem(){
		
	}

	public RedFormatItem(String code, String name, boolean isRed){
		super(code, name);
		this.isRed = isRed;
	}
	
	
	public boolean isRed() {
		return isRed;
	}

	
	public void setRed(boolean isRed) {
		this.isRed = isRed;
	}
	
	public boolean equals(Object o)
	{
		if(o == null || !(o instanceof RedFormatItem))
		{
			return false;
		}
		RedFormatItem r = (RedFormatItem)o;
		
		return r.getCode().equals(this.getCode())&&r.isRed()==this.isRed();
	}
	@Override
	public int hashCode() {
		return super.hashCode()+Integer.valueOf(getCode());
	}
	
}
