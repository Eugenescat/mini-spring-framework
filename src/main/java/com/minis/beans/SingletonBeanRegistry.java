package com.minis.beans;

/** store singleton beans */
public interface SingletonBeanRegistry {
  Object getSingleton(String beanName);
  boolean containsSingleton(String beanName);
  void registerSingleton(String beanName, Object singletonObject);
  /** get all the singleton names */
  String[] getSingletonNames();
}
