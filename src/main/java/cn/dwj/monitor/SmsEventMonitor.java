package cn.dwj.monitor;

import cn.dwj.event.OrderSuccessEvent;
import cn.dwj.framework.ApplicationListener;

/**
 * @Classname SmsService
 * @Description TODO  监听 OrderSuccessEvent事件，有 OrderSuccessEvent事件时 就会回调改方法
 * @Date 2020/7/20 9:57
 * @Created by Seven
 */
public class SmsEventMonitor implements ApplicationListener<OrderSuccessEvent> {
    /**
     * 当监听到OrderSuccessEvent事件后会调用的回调函数。
     * @param event
     */
    @Override
    public void onApplicationEvent(OrderSuccessEvent event) {
        // 调用方法，发送短信
        this.sendSms();
    }

    // 模拟发送短信。
    public void sendSms() {
        try {
            Thread.sleep(1000L * 3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("发送短信成功...");
    }

}
