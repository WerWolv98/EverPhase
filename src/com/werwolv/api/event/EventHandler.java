package com.werwolv.api.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class EventHandler {

    private static Stack<Event> eventStack = new Stack<>();
    private static List<Class<?>> eventHandlers = new ArrayList<>();

    public static void registerEventHandler(Class<?> eventHandlerClazz) {
        eventHandlers.add(eventHandlerClazz);
    }

    public static void postEvent(Event event) {
        eventStack.push(event);
    }

    public void processEvents() {

        for (Iterator<Event> iterator = eventStack.iterator(); iterator.hasNext(); ) {
            Event currEvent = iterator.next();

            for (Class eventHandler : eventHandlers)
                runAllAnnotatedWith(SubscribeEvent.class, eventHandler, currEvent);

            iterator.remove();
        }
    }

    private void runAllAnnotatedWith(Class<? extends Annotation> annotation, Class<?> eventListener, Event event) {
        Method[] methods = eventListener.getMethods();

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation))
                try {
                    if (method.getParameterTypes()[0].isInstance(event))
                        method.invoke(eventListener.newInstance(), event);
                } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
        }
    }
}