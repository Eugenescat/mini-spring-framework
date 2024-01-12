package com.minis.beans;

public interface BeanFactory {
    /* get a bean */
    Object getBean(String beanName) throws BeansException;
    /* add a beanDefinition to the beanDefinition list */
    void registerBeanDefinition(BeanDefinition beanDefinition);
}
