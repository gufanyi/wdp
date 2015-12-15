package xap.lui.core.exception;

import xap.lui.core.dataset.ComboData;

public class RadioGroupItem extends InputItem {
	private static final long serialVersionUID = -5402942241574700766L;
	private ComboData comboData;
	
	public RadioGroupItem(String inputId, String labelText, boolean required)
	{
		super(inputId, labelText, required);
	}
	
	@Override
	public String getInputType() {
		return RADIO_TYPE;
	}


	public ComboData getComboData() {
		return comboData;
	}


	public void setComboData(ComboData comboData) {
		this.comboData = comboData;
	}
}
