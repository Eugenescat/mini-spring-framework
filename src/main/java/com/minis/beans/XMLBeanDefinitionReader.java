package com.minis.beans;

import com.minis.core.Resource;

import org.dom4j.Element;

import java.util.List;

/** has a method to load all the beanDefinitions from the input XML resource into the included factory */
public class XMLBeanDefinitionReader {
  SimpleBeanFactory simpleBeanFactory;
  public XMLBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
    this.simpleBeanFactory = simpleBeanFactory;
  }

  public void loadBeanDefinitions(Resource resource) throws BeansException {
    while (resource.hasNext()) {
      Element element = (Element) resource.next();
      String beanID = element.attributeValue("id");
      String beanClassName = element.attributeValue("class");
      BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

      // handle property. type+name+value of each property, add together to property values
      List<Element> propertyElementList = element.elements("property");
      PropertyValues PVS = new PropertyValues();
      for (Element e : propertyElementList) {
        String pType = e.attributeValue("type");
        String pName = e.attributeValue("name");
        String pValue = e.attributeValue("value");
        PVS.addPropertyValue(new PropertyValue(pType, pName, pValue));
      }
      beanDefinition.setPropertyValues(PVS);

      // handle constructor argument values. the same as property values
      List<Element> constructorElements = element.elements("constructor-arg");
      ArgumentValues AVS = new ArgumentValues();
      for (Element e : constructorElements) {
        String aType = e.attributeValue("type");
        String aName = e.attributeValue("name");
        String aValue = e.attributeValue("value");
        AVS.addArgumentValue(new ArgumentValue(aType, aName, aValue));
      }
      beanDefinition.setConstructorArgumentValues(AVS);

      this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
    }
  }
}
