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
  BeanFactory beanFactory;
  public ClassPathXmlApplicationContext(String fileName) {
    // get an iterable resource from XML
    Resource resource = new ClassPathXmlResource(fileName);
    BeanFactory bf = new SimpleBeanFactory();
    XMLBeanDefinitionReader reader = new XMLBeanDefinitionReader(bf);
    // load all the beanDefinitions from the resource into beanFactory
    reader.loadBeanDefinitions(resource);
    // bind the beanFactory to the instance
    this.beanFactory = bf;
  }

  @Override
  public Object getBean(String beanName) throws BeansException {
    return this.beanFactory.getBean(beanName);
  }

  @Override
  public void registerBeanDefinition(BeanDefinition beanDefinition) {
    this.beanFactory.registerBeanDefinition(beanDefinition);
  }
}
