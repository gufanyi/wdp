package xap.lui.core.exception;

public class PswInputItem extends InputItem {

	public PswInputItem(String inputId, String labelText, boolean required)
	{
		super(inputId, labelText, required);
	}
	
	@Override
	public String getInputType() {
		return PSW_TYPE;
	}

}
