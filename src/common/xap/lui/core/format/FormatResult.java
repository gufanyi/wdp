package xap.lui.core.format;

import java.awt.Color;



public class FormatResult {

	
	private String value = null;
	
	
	private Color color = Color.BLACK;
	
	public FormatResult(){
		
	}
	
	public FormatResult(String value){
		this.value = value;
	}
	
	public FormatResult(String value, Color color){
		this.value = value;
		this.color = color;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
