package xap.lui.core.echar;

public class Toolbox {
	@FieldMeta(desc="显示策略")
	private boolean show=false;
	@FieldMeta(desc="一级层叠控制")
	private int zlevel=0;
	@FieldMeta(desc="二级层叠控制")
	private int z=6;
	@TypeEnum(types={"horizontal","vertical"})
	@FieldMeta(desc="布局方式，默认为水平布局")
	private String orient="horizontal";
	@TypeEnum(types={"center","left", "right" })
	@FieldMeta(desc="水平安放位置，默认为全图居中")
	private String x="left";
	@TypeEnum(types={"top", "bottom" , "center" })
	@FieldMeta(desc="垂直安放位置，默认为全图顶端")
	private String y="top";
	@FieldMeta(desc="工具箱背景颜色，默认透明")
	private String backgroundColor="rgba(0,0,0,0)";
	@FieldMeta(desc="工具箱边框颜色")
	private String borderColor="#ccc";
	@FieldMeta(desc="工具箱边框线宽，单位px")
	private int borderWidth=0;
	@FieldMeta(desc="工具箱内边距，单位px，默认各方向内边距为5")
	private int padding=5;
	@FieldMeta(desc="各个item之间的间隔，单位px，默认为10")
	private int itemGap=10;
	@FieldMeta(desc="工具箱icon大小")
	private int itemSize=16;
	@FieldMeta(desc="工具箱icon颜色序列，循环使用")
	private String[] color={"#1e90ff","#22bb22","#4b0082","#d2691e"};
    @FieldMeta(desc="禁用颜色定义")
	private String disableColor="#ddd";
    @FieldMeta(desc="生效颜色定义")
    private String effectiveColor="red";
    @FieldMeta(desc="是否显示工具箱文字提示")
	private boolean showTitle=false;
    @FieldMeta(desc="工具箱提示文字样式")
    private TextStyle textStyle=null;
}
