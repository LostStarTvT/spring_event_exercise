package cn.dwj.Listener;

import cn.dwj.event.OtherEvent;
import cn.dwj.framework.ApplicationListener;

/**
 * @Classname CarEventListener
 * @Description TODO   监听 OtherEvent事件，有 OtherEvent事件时 就会回调改方法
 * @Date 2020/7/20 9:56
 * @Created by Seven
 */
public class CarEventListener implements ApplicationListener<OtherEvent> {
    @Override
    public void onApplicationEvent(OtherEvent event) {
        System.out.println("监听到OtherEvent，CarService执行了");
    }
}
