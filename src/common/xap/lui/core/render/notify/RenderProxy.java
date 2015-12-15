package xap.lui.core.render.notify;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import xap.lui.core.common.LuiRenderContext;
import xap.lui.core.model.LifeCyclePhase;
import xap.lui.core.render.ILuiRender;
import xap.lui.core.render.LuiPhase;

public class RenderProxy<T extends ILuiRender> implements InvocationHandler {

	private ILuiRender render;

	private RenderProxy(ILuiRender render) {
		this.render = render;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LifeCyclePhase phase = LuiRenderContext.current().getPhase();
		boolean flag = method.isAnnotationPresent(LuiPhase.class);
		if (flag) {
			LuiPhase phaseAnn = method.getAnnotation(LuiPhase.class);
			LifeCyclePhase[] phaseValues = phaseAnn.phase();
			for(LifeCyclePhase inner:phaseValues){
				if(inner.equals(phase)){
					Object object = method.invoke(render, args);
					return object;
				}
			}
			return null;
		}else{
			Object object = method.invoke(render, args);
			return object;
		}
	}

	/**
	 * 获取NotifyRender
	 * 
	 * @param render
	 * @return
	 */
	public static <T> T getRender(T render) {
		return render;//(T) Proxy.newProxyInstance(render.getClass().getClassLoader(), render.getClass().getInterfaces(), new RenderProxy(render));
	}

}
