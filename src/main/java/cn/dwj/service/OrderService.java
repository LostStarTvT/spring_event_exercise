package cn.dwj.service;

import cn.dwj.Test.DemoTest;
import cn.dwj.event.OrderSuccessEvent;
import cn.dwj.event.OtherEvent;

/**
 * @Classname OrderService
 * @Description TODO   在进行下订单的时候，会进行发布事件，然后便能够对应的检测器便会被触发。
 * @Date 2020/7/20 9:56
 * @Created by Seven
 */
public class OrderService {
    /**
     * 用户下单
     */
    public void order() {
        // 下单成功
        System.out.println("下单成功...");

        // 发短信通知用户（粗暴异步）
        new Thread(() -> {
            // 发布一个事件 ，然后监听器就会被触发。
            // 发布OrderSuccessEvent事件
            DemoTest.applicationContext.publishEvent(new OrderSuccessEvent(this));
            // 发布OtherEvent事件， 然后对应的监听器就会被触发。
            DemoTest.applicationContext.publishEvent(new OtherEvent(this));

        }).start();

        System.out.println("main线程结束");

        try {
            Thread.sleep(1000L * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
