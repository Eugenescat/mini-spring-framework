package com.minis.beans;

/** store beans */
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
    boolean containsBean(String beanName);
    void registerBean(String beanName, Object obj);
    boolean isSingleton(String name);
    boolean isPrototype(String name);
    Class<?> getType(String name);
}
