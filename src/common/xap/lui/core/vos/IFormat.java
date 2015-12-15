package xap.lui.core.vos;

import xap.lui.core.format.FormatException;
import xap.lui.core.format.FormatResult;




public interface IFormat {

	
	public FormatResult format(Object obj) throws FormatException;
	
}
