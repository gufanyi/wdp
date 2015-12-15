package xap.lui.core.format;

import xap.lui.core.vos.IFormat;




public abstract class AbstractFormat implements IFormat{

	public FormatResult format(Object obj) throws FormatException{
		if(obj == null)
			return null;
		
		Object fObj = formatArgument(obj);
		return innerFormat(fObj);
	}
	
	
	protected abstract Object formatArgument(Object obj) throws FormatException;
	
	
	protected abstract FormatResult innerFormat(Object obj) throws FormatException;

}
