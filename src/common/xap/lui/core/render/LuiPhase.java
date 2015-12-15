package xap.lui.core.render;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import xap.lui.core.model.LifeCyclePhase;

/**
 * 指定在Action执行前执行的方法 一个Servlet中只能有一个.
 * 
 * @author licza.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LuiPhase {
	LifeCyclePhase[] phase();
}