package cn.dwj.framework;

import java.util.EventObject;

/**
 * @Classname ApplicationEvent
 * @Description TODO 事件抽象基类，后面如果要自定义事件只需要继承此类即可 这个类没啥深意，source才是真正的事件源，
 * 通过BravoApplicationEvent又包装了一层而已
 * @Date 2020/7/20 9:53
 * @Created by Seven
 */
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
