package com.minis.beans;

public interface BeanDefinitionRegistry {
  void registerBeanDefinition(BeanDefinition bd) throws BeansException;
  void removeBeanDefinition(String name);
  BeanDefinition getBeanDefinition(String name);
  boolean containsBeanDefinition(String name);
}
