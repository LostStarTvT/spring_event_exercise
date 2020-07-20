package cn.dwj.framework.Imlp;

import cn.dwj.framework.ApplicationEvent;
import cn.dwj.framework.ApplicationEventMulticaster;
import cn.dwj.framework.ApplicationListener;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @Classname SimpleApplicationEventMulticaster
 * @Description TODO 默认的广播器，如果需要自定义，可以自己实现ApplicationEventMulticaster或者继承本类后重写
 * @Date 2020/7/20 9:55
 * @Created by Seven
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {

    /**
     * 监听器。广播器里收集了所有实现了ApplicationListener接口的listener。
     * 本案例中SmsService实现了该接口，也是一个监听器。
     */
    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    /**
     * 线程池接口，默认是null。
     * 如果希望事件执行变为异步的，可以在外部调用setTaskExecutor设置一个Executor进来
     */
    private Executor taskExecutor;

    public void setTaskExecutor(Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    protected Executor getTaskExecutor() {
        return this.taskExecutor;
    }

    /**
     * 添加监听器
     * @param listener
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    /**
     * 进行发布事件，通知所有的监听器。
     * 遍历所有监听器，找到当前事件匹配的监听器，并调用监听器的回调方法，并且进行调用
     * @param event 事件的类型。
     */
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        for (final ApplicationListener<?> listener : getApplicationListeners(event)) {
            Executor executor = getTaskExecutor();
            // excutor不为空，则开启异步线程完成方法回调
            if (executor != null) {
                executor.execute(() -> invokeListener(listener, event));
            }
            else {
                invokeListener(listener, event);
            }
        }
    }

    /**
     * 假设传进来的是OrderSuccessEvent，而此时容器中有两个监听器：
     * SmsService implements ApplicationListener<OrderSuccessEvent>   ====>  监听OrderSuccessEvent
     * CarService implements ApplicationListener<OtherEvent>   ====>  监听OtherEvent
     *
     * 那么，我们必须从两个监听器中筛选出匹配的监听器
     */
    public Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event) {
        Set<ApplicationListener<?>> matchedListener = new LinkedHashSet<>();
        for (ApplicationListener<?> applicationListener : this.applicationListeners) {
            if (applicationListener.supportsEventType(event, applicationListener)) {
                matchedListener.add(applicationListener);
            }
        }
        return matchedListener;
    }

    /**
     * 执行监听器回调方法 onApplicationEvent(event)
     * @param listener
     * @param event
     */
    protected void invokeListener(ApplicationListener listener, ApplicationEvent event) {
        listener.onApplicationEvent(event);
    }
}
