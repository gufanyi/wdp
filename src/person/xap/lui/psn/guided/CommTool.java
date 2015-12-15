package xap.lui.psn.guided;


public class CommTool {
	/**
	 * 返回num位随机数，mum最大为8
	 */
	public static String getRndNum(int num){
		num=num>8?8:num;
		return  Double.toString(Math.random()*9000000+1000000).substring(0, num);
	}
	/**
	 * 将传入字符串的首字母大写并返回
	 */
	public static String firstLetterToUpperCase(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1, str.length());
	}
	

}
