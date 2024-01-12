package com.minis.beans;

import com.minis.core.Resource;

import org.dom4j.Element;

/** has a method to load all the beanDefinitions from the input XML resource into the included factory */
public class XMLBeanDefinitionReader {
  BeanFactory beanFactory;
  public XMLBeanDefinitionReader(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  public void loadBeanDefinitions(Resource resource) {
    while (resource.hasNext()) {
      Element element = (Element) resource.next();
      String beanID = element.attributeValue("id");
      String beanClassName = element.attributeValue("class");
      BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
      this.beanFactory.registerBeanDefinition(beanDefinition);
    }
  }
}
