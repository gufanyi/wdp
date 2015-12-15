package xap.lui.core.echar;

//标注图形炫光特效
public class Effect {
	@FieldMeta(desc="是否开启")
	private boolean show = false;
	@TypeEnum(types={"bounce","scale"})
	@FieldMeta(desc="特效类型")
	private String type = "scale";
	@FieldMeta(desc="循环动画")
	private boolean loop = true;
	@FieldMeta(desc="运动周期")
	private int period = 15;
	@FieldMeta(desc="放大倍数")
	private int scaleSize = 2;
	@FieldMeta(desc="跳动距离")
	private int bounceDistance = 10;
	@FieldMeta(desc="炫光颜色")
	private String color = null;
	@FieldMeta(desc="光影颜色")
	private String shadowColor = null;
	@FieldMeta(desc="光影模糊度")
	private int shadowBlur = 0;
}
