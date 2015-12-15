package xap.lui.core.format;

import xap.mw.coreitf.d.FDouble;



public class NumberObject{

	private FDouble ufDouble = null;
	
	public NumberObject(Object orignObj) throws FormatException{
		ufDouble = new FDouble(orignObj.toString());
	}
	
	public FDouble getDoubleValue(){
		return ufDouble;
	}

}
