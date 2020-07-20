package cn.dwj.framework;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Classname ApplicationListener
 * @Description TODO 监听器接口，监听事件抽象基类的所有子类对象  由service下的类进行继承， 即监听到动作以后会进行调用的函数，
 * @Date 2020/7/20 9:54
 * @Created by Seven
 */
public interface ApplicationListener<E extends ApplicationEvent> {

    /**
     * 当监听器监听到对应的事件发布时，会回调本方法
     * @param event
     */
    void onApplicationEvent(E event);

    /**
     * 当前监听器是否匹配本次事件
     *
     * @param event
     * @param applicationListener
     * @return
     */
    default boolean supportsEventType(ApplicationEvent event, ApplicationListener applicationListener) {
        Type[] genericInterfaces = applicationListener.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            Class eventClass = (Class) ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
            if (eventClass.equals(event.getClass())) {
                return true;
            }
        }
        return false;
    }
}
