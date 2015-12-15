
package xap.lui.core.format;

import java.awt.Color;



public class FormatUtils 
{
	public static String colorToHtmlString(Color c)
	{
		String color = "#" + Integer.toHexString(c.getRGB()).substring(2);
		return color;
	}
	
	public static String toColorfulString(FormatResult result)
	{
		if(result==null)
		{
			return "";
		}
		if(result.getColor()==null)
		{
			return result.getValue();
		}
		String color = colorToHtmlString(result.getColor());
		return "<font color=\""+color +"\">"+ result.getValue()+"</font>";
	}
}
