package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** STORE beans, can do getBean and registerBeanDefinition */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

  private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
  protected List<String> beanNamesList = new ArrayList<>();
  public SimpleBeanFactory() {
  }

  // implement functions of BeanFactory
  /** in lazy model, when we need to get the bean instance, we initiate it */
  public Object getBean(String beanName) throws BeansException {
    // try if the bean is already registered, use methods extends from DefaultSingletonBeanRegistry
    Object singleton = this.getSingleton(beanName);
    // if not registered, try to find from beanDefinitionMap and create it
    if (singleton == null) {
      BeanDefinition bd = beanDefinitionMap.get(beanName);
      singleton = createBean(bd);
      this.registerBean(beanName, singleton);
      if (bd.getInitMethodName() != null) {
        // ToDo: init
      }
      // if not even found in the beanDefinitionMap, throw exception
      if (bd == null) {
        throw new BeansException("bean not found");
      }
    }
    return singleton;
  }
  public boolean containsBean(String name) {
    return this.containsSingleton(name);
  }
  public void registerBean(String beanName, Object obj) {
    this.registerSingleton(beanName, obj);
  }

  // implements functions of BeanDefinitionRegistry
  /** if lazy, only register to definitionMap, else additionally call getBean(name) */
  public void registerBeanDefinition(BeanDefinition beanDefinition) throws BeansException {
    String beanName = beanDefinition.getId();
    this.beanDefinitionMap.put(beanName, beanDefinition);
    this.beanNamesList.add(beanName);
    if (!beanDefinition.isLazyInit()) {
      try {
        // use beanFactory part to instantiate the bean immediately if it's not meant to be lazy.
        getBean(beanName);
      } catch (Exception e) {
        throw new BeansException("bean instantiated error");
      }
    }
  }
  public void removeBeanDefinition(String beanName) {
    this.beanDefinitionMap.remove(beanName);
    this.beanNamesList.remove(beanName);
    /* remove an instance from the singleton cache,
    this method is inherited from DefaultSingletonBeanRegistry */
    this.removeSingleton(beanName);
  }
  public BeanDefinition getBeanDefinition(String beanName) {
    return this.beanDefinitionMap.get(beanName);
  }
  public boolean containsBeanDefinition(String beanName) {
    return this.beanDefinitionMap.containsKey(beanName);
  }

 // misc
  public boolean isSingleton(String beanName) {
    return beanDefinitionMap.get(beanName).isSingleton();
  }
  public boolean isPrototype(String beanName) {
    return beanDefinitionMap.get(beanName).isPrototype();
  }
  /** for future check the value what we stored is Class beanDefinition */
  public Class<?> getType(String beanName) {
    return this.beanDefinitionMap.get(beanName).getClass();
  }

  /** through the given beanDefinition, create a bean */
  public Object createBean(BeanDefinition bd) {
    Object singleton = null;
    try {
      singleton = Class.forName(bd.getClassName()).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    // use methods extends from DefaultSingletonBeanRegistry
    this.registerSingleton(bd.getId(), singleton);
    return singleton;
  }
}
