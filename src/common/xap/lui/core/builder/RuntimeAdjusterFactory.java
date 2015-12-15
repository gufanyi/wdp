package xap.lui.core.builder;

import xap.lui.core.event.AppTypeRuntimeAdjuster;
import xap.lui.core.util.ClassUtil;

public class RuntimeAdjusterFactory {

	public static IRuntimeAdjuster getRuntimeAdjuster() {
		IRuntimeAdjuster adjuster = (IRuntimeAdjuster) ClassUtil.newInstance(AppTypeRuntimeAdjuster.class.getName());
		return adjuster;
	}

}
