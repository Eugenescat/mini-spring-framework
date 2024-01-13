package com.minis.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
  protected List<String> beanNames = new ArrayList<>();
  protected Map<String, Object> singletons = new ConcurrentHashMap<>(256);

  public Object getSingleton(String beanName) {
    return this.singletons.get(beanName);
  }
  public boolean containsSingleton(String beanName) {
    return this.singletons.containsKey(beanName);
  }
  /** only allows for register one singleton once a time */
  public void registerSingleton(String beanName, Object singletonObject) {
    synchronized (this.singletons){
      this.singletons.put(beanName, singletonObject);
      this.beanNames.add(beanName);
    }
  }
  /** only allows for remove one singleton once a time */
  protected void removeSingleton(String beanName) {
    synchronized (this.singletons) {
      this.beanNames.remove(beanName);
      this.singletons.remove(beanName);
    }
  }
  public String[] getSingletonNames() {
    return (String[]) this.beanNames.toArray();
  }
}
