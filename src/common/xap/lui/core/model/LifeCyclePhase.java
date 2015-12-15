package xap.lui.core.model;
/**
 * 应用生命周期枚举值 
 */
public enum LifeCyclePhase {
	//dynrender,//动态代开对话框            没有用到
	render,  //用解析完的渲染阶段
	nullstatus,//page解析阶段
	ajax, //事件处理阶段
	design;
}
