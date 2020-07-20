# Spring事件监听器
模拟spring事件监听器demo，通过模拟生成订单来进行事件监听，当用户成功下订单时，便会触发监听器进行发送成功短信等其他操作。

# 快速开始

运行testSpringEvent()方法。运行结果：

```shell
下单成功...
main线程结束
发送短信成功...
监听到OtherEvent，CarService执行了
```

# 项目结构

通过定义一个盗版的Spring容器来进行模拟Spring中的事件监听器逻辑，通过扩展增加event包下的类便能定义自定通知模型。monitor包中作为事件的监听器，有事件触发时便会调用其中的方法。

用户需要定义两个东西

- 需要监听的事件(event包下)
- 事件监听器(monitor包下)

### 事件

其中所有的自定义的事件都需要继承ApplicationEvent类。

```java
public class ApplicationEvent extends EventObject {
    /**
     * Constructs a prototypical Event.  定义一个事件。
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
```

子类进行继承，

```java
public class OtherEvent  extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public OtherEvent(Object source) {
        super(source);
    }
}
```

### 事件监听器

事件监听器接口如下:其中广播器是通过遍历事件监听器来进行发布通知

```java
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
```

具体的实现类

```java
public class CarEventListener implements ApplicationListener<OtherEvent> {
    // 监听器触发时候 回调本方法
    @Override
    public void onApplicationEvent(OtherEvent event) {
        System.out.println("监听到OtherEvent，CarService执行了");
    }
}
```

## 广播器

另外还有一个比较重要就是ApplicationEventMulticaster,即事件的发布器，实现了增加监听器和发布事件的功能，这个需要由容器提供。

```java
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
```

其中广播器相当于将事件Event和事件监听器Listener联系到一起，他主要做了两件事，第一，使用缓存HashMap将监听器存储，然后提供发布事件接口，发布事件接口具体为通过遍历调用缓存将对应的Event发布出去。

```java
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {

    /**
     * 监听器。广播器里收集了所有实现了ApplicationListener接口的listener。
     * 本案例中SmsService实现了该接口，也是一个监听器。
     */
    public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

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
```

以上完成整个监听器的设置，其实也是基于观察者模式进行实现，只不过这里面使用了HashMap进行存储监听器，而观察者使用List来进行存储。