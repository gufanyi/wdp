package xap.lui.core.format;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xap.lui.core.vos.IElement;




public abstract class AbstractSplitFormat extends AbstractFormat{
	
	protected List<IElement> elements = new ArrayList<IElement>();
	
	@Override
	protected FormatResult innerFormat(Object obj) throws FormatException{
		doSplit();
		
		StringBuffer result = new StringBuffer();
		
		for(IElement element : elements){
			String elementValue = element.getValue(obj);
			if(elementValue != null)
				result.append(elementValue);
		}
				
		return new FormatResult(result.toString());
	}
	
	
	protected abstract String getExpress();
	
	
	protected abstract String[] getSeperators();
	
	
	protected abstract String[] getReplaceds();
	
	protected void doSplit(){
		String express = getExpress();
		
		if(this.elements == null || this.elements.size() == 0)
			this.elements = doQuotation(express, getSeperators(), getReplaceds(), 0);
	}
	
	
	protected List<IElement> doQuotation(String express, String[] seperators, String[] replaced, int curSeperator){
		if(express.length() == 0)
			return null;
		
		List<IElement> elements = new ArrayList<IElement>();
		
		Pattern pattern  = Pattern.compile("\".*?\"");
		Matcher matcher = pattern.matcher(express);
		
		int fromIndex = 0;
		while(matcher.find()){
			int i = matcher.start();
			int j = matcher.end();
			if(i != j){
				if(fromIndex < i){
					List<IElement> childElements = doSeperator(express.substring(fromIndex, i), seperators, replaced, curSeperator);
					if(childElements != null && childElements.size() > 0)
						elements.addAll(childElements);
				}
				
				elements.add(new StringElement(express.substring(i + 1, j-1)));
				
				fromIndex = j;
			}
		}
		
		if(fromIndex < express.length()){
			List<IElement> childElements = doSeperator(express.substring(fromIndex, express.length()), seperators, replaced, curSeperator);
			if(childElements != null && childElements.size() > 0)
				elements.addAll(childElements);
		}
		
		return elements;
	}
	
	
	protected List<IElement> doSeperator(String express, String[] seperators, String[] replaced, int curSeperator){
		if(curSeperator >= seperators.length){
			List<IElement> elements = new ArrayList<IElement>();
			elements.add(getVarElement(express));
			return elements;
		}
		
		if(express.length() == 0)
			return null;
		
		List<IElement> elements = new ArrayList<IElement>();
			
		Pattern pattern  = Pattern.compile(seperators[curSeperator]);
		Matcher matcher = pattern.matcher(express);
		
		int fromIndex = 0;
		while(matcher.find()){
			int i = matcher.start();
			int j = matcher.end();
			if(i != j){
				if(fromIndex < i){
					List<IElement> childElements = doSeperator(express.substring(fromIndex, i), seperators, replaced, curSeperator + 1);
					if(childElements != null && childElements.size() > 0)
						elements.addAll(childElements);
				}
				
				if(replaced[curSeperator] != null){
					elements.add(new StringElement(replaced[curSeperator]));
				}
				else{
					elements.add(new StringElement(express.substring(i, j)));
				}
				
				fromIndex = j;
			}
		}
		
		if(fromIndex < express.length()){
			List<IElement> childElements = doSeperator(express.substring(fromIndex, express.length()), seperators, replaced, curSeperator + 1);
			if(childElements != null && childElements.size() > 0)
				elements.addAll(childElements);
		}
		
		return elements;
	}
	
	
	protected abstract IElement getVarElement(String express);

}
