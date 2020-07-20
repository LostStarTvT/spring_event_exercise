package cn.dwj.Test;

import cn.dwj.framework.ApplicationContext;
import cn.dwj.monitor.CarEventMonitor;
import cn.dwj.monitor.SmsEventMonitor;
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
    public static SmsEventMonitor smsEventMonitor;
    public static OrderService orderService;
    public static CarEventMonitor carEventMonitor;

    @Before
    public void initApplicationContext() {
        applicationContext = new ApplicationContext();
        smsEventMonitor =  (SmsEventMonitor) applicationContext.getBean(SmsEventMonitorUri);
        orderService = (OrderService) applicationContext.getBean(OrderServiceUri);
        carEventMonitor = (CarEventMonitor) applicationContext.getBean(CarEventMonitorUri);
    }

    @Test
    public void testSpringEvent() {
        // 用户下单（试着点进去，把orderService发布的事件改为OtherEvent）
        orderService.order();
    }
}
