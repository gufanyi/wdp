package xap.lui.core.adapter;

/**
 * 基本类型定义。
 * 创建日期：(2002-3-1 9:13:27)
 * @author：林大翰
 * @nopublish
 */
public interface IType {
	public static final int STRING=0;//-->相当于自定义变量的String
	public static final int INT=1;//-->相当于自定义变量的Integer
	public static final int BIGINTEGER=2;//-->相当于自定义变量的Integer
	public static final int BOOLEAN=3;//-->相当于自定义变量的Boolean
	public static final int BIGDECIMAL=4;//-->相当于自定义变量的Decimal
	public static final int FDOUBLE=5;//-->相当于自定义变量的Decimal
	public static final int DOUBLE=6;//-->相当于自定义变量的Double
	public static final int INT_plus=7;//-->相当于自定义变量的int
	public static final int BOOLEAN_plus=8;//-->相当于自定义变量的boolean
	public static final int DOUBLE_plus=9;//-->相当于自定义变量的double
	//增加的
	public static final int SQLVALUE=10;//--->SQLValue
	public static final int VOID=-1;
	public static final int CI=11;//CI结构
	
	
    public final static int Primitive = 0;

	
	public final static int TYPE_String = 1;

	
	public final static int TYPE_Double = 2;

	
	public final static int TYPE_Boolean = 3;

	
	public final static int TYPE_Integer = 4;

	
	public final static int TYPE_Date = 5;

	
	public final static int TYPE_INT = 6;

	
	public final static int TYPE_BOOL = 7;

	
	public final static int TYPE_CHAR = 8;

	
	public final static int TYPE_BYTE = 9;

	
	public final static int TYPE_DOUBLE = 10;

	public final static int TYPE_FLOAT = 11;

	public final static int TYPE_SHORT = 12;

	public final static int TYPE_LONG = 13;

	public final static int TYPE_CALENDAR = 14;

	
	public final static int TYPE_MEMO = 30;
	public final static int TYPE_FDouble = 31;

	public final static int TYPE_FBoolean = 32;

	public final static int TYPE_FDate = 33;

	public final static int TYPE_FDateTime = 34;

	public final static int TYPE_FNumberFormat = 35;

	public final static int TYPE_FTime = 36;

	public final static int TYPE_FDate_BEGIN = 37;

	public final static int TYPE_FDate_END = 38;

	public final static int TYPE_FDATE_LITERAL = 39;

	public final static int TYPE_BIGDECIMAL = 40;

	
	public final static int TYPE_FID = 51;

	
	public final static int TYPE_FMoney = 52;

	public final static int TYPE_BLOB = 53;

	public final static int TYPE_CLOB = 54;

	public final static int TYPE_IMAGE = 55;

	
	public final static int TYPE_CUSTOM = 56;

	
	public final static int TYPE_FREE = 59;

	
	public final static int TYPE_UICUSTOM = 57;

	public final static int Primitive_End = 100;

	
	public final static int ENTITY = 201;

	
	public final static int VALUEOBJECT = 202;

	
	public final static int ENUM = 203;

	
	public final static int REF = 204;

	
	public final static int COLLECTION = 205;

	
	public final static int BIZINTERFACE = 206;

	
	public final static int CUSTOMENTITY = 207;

	
	public final static int DEFCLASS = 209;

	
	public final static int MULTILANGUAGE = 58;

	
	public final static int STYLE_SINGLE = 300;

	
	public final static int STYLE_ARRAY = 301;

	
	public final static int STYLE_LIST = 302;

	
	public final static int STYLE_VECTOR = 303;

	
	public final static int STYLE_SET = 304;

	
	public final static int STYLE_REF = 305;
}
