package cn.dwj.event;

import cn.dwj.framework.ApplicationEvent;

/**
 * @Classname OrderSuccessEvent
 * @Description TODO
 * @Date 2020/7/20 9:57
 * @Created by Seven
 */
public class OrderSuccessEvent  extends ApplicationEvent {
    public OrderSuccessEvent(Object source) {
        super(source);
    }
}
