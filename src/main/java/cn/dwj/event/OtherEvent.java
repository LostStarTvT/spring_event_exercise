package cn.dwj.event;

import cn.dwj.framework.ApplicationEvent;

/**
 * @Classname OtherEvent
 * @Description TODO
 * @Date 2020/7/20 9:57
 * @Created by Seven
 */
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
