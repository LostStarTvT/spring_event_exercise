package cn.dwj.Test;

import cn.dwj.framework.ApplicationContext;
import cn.dwj.Listener.CarEventListener;
import cn.dwj.Listener.SmsEventListener;
import cn.dwj.service.OrderService;
import org.junit.Before;
import org.junit.Test;

import static cn.dwj.constant.Constant.*;

/**
 * @Classname DemoTest
 * @Description TODO  进行测试整个监听器的流程。
 * @Date 2020/7/20 9:56
 * @Created by Seven
 */

public class DemoTest {

    // 之所以设计成public static，是为了能在OrderService中拿到SmsService，毕竟我没实现Autowired自动注入
    public static ApplicationContext applicationContext;
    public static SmsEventListener smsEventListener;
    public static OrderService orderService;
    public static CarEventListener carEventListener;

    @Before
    public void initApplicationContext() {
        applicationContext = new ApplicationContext();
        smsEventListener =  (SmsEventListener) applicationContext.getBean(SmsEventMonitorUri);
        orderService = (OrderService) applicationContext.getBean(OrderServiceUri);
        carEventListener = (CarEventListener) applicationContext.getBean(CarEventMonitorUri);
    }

    @Test
    public void testSpringEvent() {
        // 用户下单（试着点进去，把orderService发布的事件改为OtherEvent）
        orderService.order();
    }
}
