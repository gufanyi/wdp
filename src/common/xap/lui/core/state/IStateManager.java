package xap.lui.core.state;

import xap.lui.core.comps.WebComp;
import xap.lui.core.model.ViewPartMeta;

/**
 * 状态管理接口 
 *
 */
public interface IStateManager {
	/*
	 * 状态值
	 */
	public enum State{
		ENABLED,
		DISABLED,
		HIDDEN, 
		VISIBLE,
		ENABLED_VISIBLE,
		DISABLED_VISIBLE
	}
	public State getState(WebComp target, ViewPartMeta view);
}
