package xap.lui.psn.cmd;

import java.io.Serializable;


public interface FromWhereSQLCmd extends Serializable {

	
	public static final String DEFAULT_KEY = ".";
	
	
	public static final String DEFAULT_ATTRPATH = DEFAULT_KEY;
	
	
	public String getFrom();

	
	public String getWhere();

	
	public String getTableAliasByAttrpath(String attrpath);
}
