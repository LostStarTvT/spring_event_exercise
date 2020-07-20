package cn.dwj.framework;

/**
 * @Classname ApplicationMulticaster
 * @Description TODO 事件广播器接口
 * @Date 2020/7/20 9:54
 * @Created by Seven
 */
public interface ApplicationEventMulticaster {
    /**
     * 往广播器中添加listener
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 发布事件
     * @param event
     */
    void multicastEvent(ApplicationEvent event);

}
