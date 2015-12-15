package xap.lui.core.refrence;
import java.awt.Color;
public interface IRefConst {
	public final static int DEFAULT = -1; // 默认类型
	public final static int GRID = 0; // GRID
	public final static int TREE = 1; // TREE
	public final static int GRIDTREE = 2; // GRIDTREE
	public final static int COMBOBOX = 3; // 下拉列表
	public static int CHARTYPE = 0;
	public static int NUMBERTYPE = 0;
	public static String DISTINCT = "distinct";
	public static String QUERY = "query";
	public final static String GROUPCORP = "0001";
	public final static String MACROCURRENTPK_CORP = "$PK_CORP$";
	public final static String MACROCURRENTPK_USER = "$PK_USER$";
	//gird每页条数
    public static final int GRID_PAGESIZE = 12;
	// 当前主体账簿
	public final static String MACROCURRENTPK_GLORGBOOK = "$PK_GLORGBOOK$";
	// 参照panel背景色
	public final static Color REFBACKGROUND = Color.WHITE;
	// 参照表格行标号背景色
	public final static Color REFTABLEROWHEADER = new Color(0xf4, 0xfe, 0xff);
	// 参照按钮有焦点背景色
	public final static Color REFBUTTONFOCUS = new Color(0xb0, 0xbd, 0xcf);
	// 参照按钮有焦点边框背景色
	public final static Color REFBUTTONFOCUSBORDER = new Color(0x58, 0x6a, 0x84);
	// 参照按钮按下背景色
	public final static Color REFBUTTONPRESS = new Color(0xdb, 0xe2, 0xeb);
	// 参照按钮按下边框背景色
	public final static Color REFBUTTONPRESSBORDER = new Color(0x58, 0x6a, 0x84);
	// 参照工具条背景色
	// public final static Color REFTOOLPANELBACKGROUND = new
	// Color(230,230,230);
	public final static String REFINFOVOCACHEKEY = "RefInfoVOsCache";
	// 参照输入框中要特殊处理的字段。总账对此字段有特殊解释;
	// 总账系统的凭证中,辅助核算允许录入空值,但是在进行账簿查询时,辅助项查询条件录入框应区分两种情况:
	// 1)录入null:表示对辅助项值为空值的数据进行查询统计,
	// 2)不设置任何查询条件值(即查询条件录入框为空):表示查询所有辅助项的数据.
	public final static String NULL = "null";
	public final static int TREEEXPANDLEVEL = 6;
	public final static String CODINGRULETOKEN = "/";
	public final static int COMMONDATASIZE = 200;
	public final static String MULTIINPUTTOKEN = ",";
	public final static String REFNODENAME_TEXTFIELD = "文本框";
	public final static String REFNODENAME_NULLSTR = "";
	public final static String REFNODENAME_CALENDAR = "日历";
	// 10位长度日历，不带时间
	public final static String REFNODENAME_LITERALCALENDAR = "日期";
	public final static String REFNODENAME_CALCULATOR = "计算器";
	public final static String REFNODENAME_COLOR = "颜色";
	public final static String REFNODENAME_FILE = "文件";
	public final static String REFNODENAME_REFCUSTOMIZE = "参照定制";
	public final static String REFNODENAME_DATETIME = "日期时间";
	public static final String LARGE_NODE_NAME = "客户档案:客商档案:供应商档案:人员:物料";
	public static final String DATAPOWEROPERATION_CODE = "default";
	public static final String MASTER_DS_ID = "masterDs";
	public static final String RIGHT_DS_ID = "rightGridDs";
	public static final String REFORG = "refcomp_org";
	public static final String WIDGET_ID = "main";
	public static final String REF_BOTTOM_HEIGHT = "45";
	public static final String EVENT_ID = "Listener";
	public static final String OK_BUTTON_ID = "okbt";
	public static final String CANCEL_BUTTON_ID = "cancelbt";
	public static final String LOCATE_TEXT_COMP_ID = "locatetext";
	public static final String OK_BUTTON_EVENT_METHOD_NAME = "refOkDelegator";
	public static final String CANCEL_BUTTON_EVENT_SCRIPT = "parent.$.pageutils.hideDialog();";
	public static final String LOCATE_TEXT_COMP_EVENT_SCRIPT = "$.refscript.doFilter(pageUI.getViewPart('main').getComponent('locatetext').getValue());";
	
}
