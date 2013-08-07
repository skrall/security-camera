package org.krall.security.event;

import com.google.common.eventbus.EventBus;

public class EventBusSingleton {

    private static EventBusSingleton eventBusSingleton ;

    EventBus eventBus = new EventBus();

    static {
        eventBusSingleton = new EventBusSingleton();
    }

    private EventBusSingleton() {
    }

    public static EventBusSingleton getInstance() {
        return eventBusSingleton;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
