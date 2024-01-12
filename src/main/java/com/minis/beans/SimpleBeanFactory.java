package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** STORE resources, can do getBean and registerBeanDefinition */
public class SimpleBeanFactory implements BeanFactory {

  private List<BeanDefinition> beanDefinitionList = new ArrayList<>();
  private List<String> beanNameList = new ArrayList<>();
  private Map<String, Object> singletons = new HashMap<>();
  public SimpleBeanFactory() {
  }

  /** only when we need to get the bean instance, we initiate it */
  @Override
  public Object getBean(String beanName) throws BeansException {
    // try if the bean is already registered
    Object singleton = singletons.get(beanName);
    // if not registered, try to find from beanNameList, and get the corresponding defi
    if (singleton == null) {
      int i = beanNameList.indexOf(beanName);
      // not even found in the beanNameList
      if (i == -1) {
        throw new BeansException("bean not found");
      }
      // found in the beanNameList, then fetch its defi and initiate it
      else {
        BeanDefinition bd = beanDefinitionList.get(i);
        try {
          singleton = Class.forName(bd.getClassName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
          throw new RuntimeException(e);
        }
        singletons.put(bd.getId(), singleton);
      }
    }
    return singleton;
  }

  /** when register, only update beanDefinitionList and beanNameList */
  @Override
  public void registerBeanDefinition(BeanDefinition beanDefinition) {
    this.beanDefinitionList.add(beanDefinition);
    this.beanNameList.add(beanDefinition.getId());
  }
}
