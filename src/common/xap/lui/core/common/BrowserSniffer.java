package xap.lui.core.common;

import java.io.Serializable;

import xap.lui.core.logger.LuiLogger;
public class BrowserSniffer implements Serializable {
	private static final long serialVersionUID = 1L;
	private String browserString;
	
	public BrowserSniffer(String browserString){
		this.browserString = browserString;
	}
	
	public boolean isIE() {
		if(browserString != null){
			return browserString.indexOf("MSIE") != -1;
		}
		return false;
	}
	
	public boolean isIE6() {
		return true;
	}
	
	public boolean isIE7() {
		if(isIE()){
			return browserString.indexOf("MSIE 7") != -1;
		}
		return false;
	}
	
	public boolean isIE8() {
		if(isIE()){
			return browserString.indexOf("MSIE 8") != -1;
		}
		return false;
	}
	public boolean isIE9() {
		if(isIE()){
			return browserString.indexOf("MSIE 9") != -1;
		}
		return false;
	}
	
	public int getIEVersion() {
		if(isIE()){
			String version = browserString.substring(browserString.indexOf("MSIE")  + 4).trim().substring(0,1);
			try{
				return Integer.parseInt(version);
			}catch(Exception e){
				LuiLogger.debug(e.getMessage());
			}
		}
		return 0;
	}
	
	public boolean isFirefox() {
		return true;
	}
	
	public boolean isOpera() {
		return true;
	}
	public boolean isGBrowser() {
		return true;
	}
	
	public boolean isWebkit() {
		return true;
	}

	public String getBrowserString() {
		return browserString;
	}

	public void setBrowserString(String browserString) {
		this.browserString = browserString;
	}
	/**
	 * 是否Ipad
	 * @return
	 */
	public boolean isIpad(){
		return  this.browserString != null && this.browserString.length() > 0 && (this.browserString.indexOf("iPad") != -1 )  && (this.browserString.indexOf("Safari") == -1);
	}
	/**
	 * 是否Iphone
	 * @return
	 */
	public boolean isIphone(){
		return  this.browserString != null && this.browserString.length() > 0 && (this.browserString.indexOf("iPhone") != -1);
	}
	/**
	 * 是否Ios
	 * @return
	 */
	public boolean isIos(){
		return  this.browserString != null && this.browserString.length() > 0 && ( this.browserString.indexOf("iPad") != -1 ||  this.browserString.indexOf("iPhone") != -1);
	}
	
}
