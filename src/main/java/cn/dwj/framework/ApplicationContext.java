package cn.dwj.framework;

import cn.dwj.framework.Imlp.SimpleApplicationEventMulticaster;

import java.util.*;

import static cn.dwj.constant.Constant.*;

/**
 * @Classname ApplicationContext
 * @Description TODO 山寨版spring容器
 * @Date 2020/7/20 9:52
 * @Created by Seven
 */
public class ApplicationContext {

    // 单例池，储存单例Bean
    private HashMap<String, Object> singletonObjects = new HashMap<>();

    // 事件广播器
    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    private ApplicationEventMulticaster applicationEventMulticaster;

    // 存储所有等待Spring创建的Bean的名字
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    // 存储所有的监听器
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    /**
     * 构造器
     */
    public ApplicationContext() {
        // 扫描并注册指定路径下所有Bean（这里就是拿个名字）
        register();
        // 刷新容器
        refresh();
    }

    /**
     * 把BeanDefinition注册到容器（注册BeanDefinition不代表实例化Bean） 需要手动的注入。
     */
    public void register() {
        // 假装扫描指定路径下的Bean
        beanDefinitionNames.add(SmsEventMonitorUri);
        beanDefinitionNames.add(OrderServiceUri);
        beanDefinitionNames.add(CarEventMonitorUri);
    }

    /**
     * 刷新Spring容器
     */
    public void refresh() {

        // 初始化事件广播器
        initApplicationEventMulticaster();

        /*
         * 初始化Spring容器中业务Bean（我们写的），在本案例中，我们只关心两件事：
         * 1.对象的实例化
         * 2.对Bean的后置处理（后置处理分很多种，这里仅考虑判断是否是Listener）
         * */
        finishBeanFactoryInitialization();
    }

    /**
     * 初始化事件广播器
     * 如果用户没有提供事件广播器（比如上一篇的@Bean提供自己的广播器），那么用默认的广播器BravoSimpleApplicationEventMuiltcaster
     * 事件广播器的职责是：
     * 1.接收一个Event
     * 2.遍历所有的Listener，找到与这个Event匹配的Listener
     * 3.调用listener.onApplicationEvent()
     */
    public void initApplicationEventMulticaster() {
        // 如果容器中已经存在名为applicationEventMulticaster的广播器，则直接赋值
        if (beanDefinitionNames.contains(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
            applicationEventMulticaster = (ApplicationEventMulticaster) singletonObjects.get(APPLICATION_EVENT_MULTICASTER_BEAN_NAME);
        } else {
            // 否则设置一个默认的广播器 也就是和这个对象的名称，进行遍历。
            applicationEventMulticaster = new SimpleApplicationEventMulticaster();
        }
    }

    /**
     * 广播事件
     * @param event
     */
    public void publishEvent(ApplicationEvent event) {
        getApplicationEventMulticaster().multicastEvent(event);
    }

    /**
     * 得到广播器，广播器中有监听器
     * @return
     */
    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        return this.applicationEventMulticaster;
    }

    /**
     * 初始化业务Bean（BeanDefinition  -> Bean）
     */
    private void finishBeanFactoryInitialization() {
        for (String beanDefinitionName : beanDefinitionNames) {
            doCreateBean(beanDefinitionName);
        }
    }

    /**
     * 创建Bean实例
     * @param beanName
     */
    private void doCreateBean(String beanName) {

        Object bean = null;
        try {
            // 反射创建Bean
            bean = createBeanInstance(beanName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // populateBean(beanName, mbd, instanceWrapper); 自动装配，省略

        // 初始化
        initializeBean(bean);

        // 存入单例池
        singletonObjects.put(beanName, bean);
    }

    /**
     * 反射创建Bean实例（空实例）
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object createBeanInstance(String beanName) throws Exception {
        return Class.forName(beanName).newInstance();
    }


    /**
     * 初始化Bean
     * @param bean
     */
    public void initializeBean(Object bean) {
        // applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName); 省略

        // invokeInitMethods(beanName, wrappedBean, mbd); 调用Bean的initMethod，省略

        // 如果当前Bean实现了事件监听接口，则加入广播器
        applyBeanPostProcessorsAfterInitialization(bean);
    }

    /**
     * 后置处理（简化了，本来是后置处理器）
     * @param bean
     */
    public void applyBeanPostProcessorsAfterInitialization(Object bean) {
        // 如果当前Bean实现了监听器接口
        if (bean instanceof ApplicationListener) {
            // 加入到监听器
            addApplicationListener((ApplicationListener<?>) bean);
        }
    }

    /**
     * 把实现了BravoApplicationListener接口的bean作为listener加入到广播器的监听器集合中
     * @param listener
     */
    public void addApplicationListener(ApplicationListener<?> listener) {
        Objects.nonNull(listener);
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.addApplicationListener(listener);
        }
    }

    /**
     * 根据名称获取Bean
     * @param beanName
     * @return
     */
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }
}
