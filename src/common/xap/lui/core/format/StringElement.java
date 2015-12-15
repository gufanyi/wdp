package xap.lui.core.format;

import xap.lui.core.vos.IElement;



public class StringElement implements IElement{
	
	private String value = null;

	public StringElement(String value){
		this.value = value;
	}
	
	@Override
	public String getValue(Object obj) {
		return value;
	}

}
