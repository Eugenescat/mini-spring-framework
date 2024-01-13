package com.minis.context;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.BeansException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XMLBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/** integrate the functions of make-resource + read-and-load-into-factory
 * has methods of getBean and registerBeanDefinition, as a BeanFactory */
public class ClassPathXmlApplicationContext implements BeanFactory {
  SimpleBeanFactory simpleBeanFactory;
  public ClassPathXmlApplicationContext(String fileName) {
    // get an iterable resource from XML
    Resource resource = new ClassPathXmlResource(fileName);
    SimpleBeanFactory bf = new SimpleBeanFactory();
    XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader(bf);
    // load all the beanDefinitions from the resource into beanFactory
    // inside a XMLBeanDefinitionReader
    reader.loadBeanDefinitions(resource);
    // bind the beanFactory to the instance
    this.simpleBeanFactory = bf;
  }

  public Object getBean(String beanName) throws BeansException {
    return this.simpleBeanFactory.getBean(beanName);
  }
  public boolean containsBean(String beanName){
    return this.simpleBeanFactory.containsBean(beanName);
  }
  public void registerBean(String beanName, Object obj) {
    this.simpleBeanFactory.registerBean(beanName, obj);
  }
  public void registerBeanDefinition(BeanDefinition beanDefinition) {
    this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
  }
}
