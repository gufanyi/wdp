package xap.lui.core.format;

import java.awt.Color;
import java.util.regex.Matcher;

import xap.mw.coreitf.d.FDouble;




public class NumberFormat extends AbstractFormat{

	protected NumberFormatMeta formatMeta = null;
	
	NumberFormat(){
		
	}
	
	public NumberFormat(NumberFormatMeta formatMeta){
		this.formatMeta = formatMeta;
	}
	
	@Override
	protected Object formatArgument(Object obj) throws FormatException {
		return new NumberObject(obj);
	}

	@Override
	protected FormatResult innerFormat(Object obj)throws FormatException {
		NumberObject fObj = (NumberObject) obj;
		
		FDouble dValue = fObj.getDoubleValue();
		String strValue = null;
		
		String express = null;
		double tmpValue = dValue.doubleValue();
		if(tmpValue > 0){
			express = formatMeta.getPositiveFormat();
			strValue = dValue.toString();
		}
		else if(tmpValue < 0){
			express = formatMeta.getNegativeFormat();
			strValue = dValue.toString().substring(1);
		}
		else{
			express = formatMeta.getPositiveFormat();
			strValue = dValue.toString();
		}
		
		int seperatorIndex = strValue.indexOf(".");
		StringBuffer str = new StringBuffer(strValue);
		setTheSeperator(str, seperatorIndex);
		setTheMark(str, seperatorIndex);
		Color color = null;
		
		if(dValue.doubleValue() < 0 && formatMeta.isNegRed()){
			color = Color.RED;
		}
		return new FormatResult(express.replaceAll("n", Matcher.quoteReplacement(str.toString())), color);
	}
	
	private void setTheMark(StringBuffer str, int seperatorIndex){
		
		if(!formatMeta.isMarkEnable())
			return;
		
		if(seperatorIndex <= 0)
			seperatorIndex = str.length();
			
		char first = str.charAt(0);
		
		int endIndex = 0;
		if(first == '-'){
			endIndex = 1;
		}
		
		char[] mark = formatMeta.getMarkSymbol().toCharArray();
		
		int index = seperatorIndex - 3;
		while(index > endIndex){
			str.insert(index, mark);
			
			index = index - 3;
		}
	}
	
	private void setTheSeperator(StringBuffer str, int seperatorIndex){
		if(seperatorIndex > 0)
			str.setCharAt(seperatorIndex, formatMeta.getPointSymbol().toCharArray()[0]);
	}
	
}
