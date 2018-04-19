package top.wboost.common.utils.web.interfaces.context;

import org.springframework.context.event.ContextRefreshedEvent;

public interface EzApplicationListener {

	/**
	 * WEB APPLICATION 初始化完成回调函数
	 * 
	 * @param event
	 */
	public void onWebApplicationEvent(ContextRefreshedEvent event);

	/**
	 * ROOT APPLICATION 初始化完成回调函数
	 * 
	 * @param event
	 */
	public void onRootApplicationEvent(ContextRefreshedEvent event);

	/**
	 * spring-boot 初始化完成回调函数
	 * 
	 * @param event
	 */
	public void onBootApplicationEvent(ContextRefreshedEvent event);

	/**
	 * 若为spring-boot项目,是否执行普通web工程监听器
	 * 
	 * @param event
	 * @return
	 */
	default public boolean doWebAndRootApplicationListener(ContextRefreshedEvent event) {
		return true;
	}

}
