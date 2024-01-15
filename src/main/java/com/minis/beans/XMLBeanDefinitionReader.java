package com.minis.beans;

import com.minis.core.Resource;

import org.dom4j.Element;

import java.util.ArrayList;
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
      List<String> refs = new ArrayList<>();
      for (Element e : propertyElementList) {
        String pType = e.attributeValue("type");
        String pName = e.attributeValue("name");
        String pValue = e.attributeValue("value");
        // handle ref property.
        // both regular value and ref are contained in pV, using isRef to distinguish
        String pRef = e.attributeValue("ref");
        String pV = "";
        boolean isRef = false;
        if (pValue != null && !pValue.isEmpty()) {
          pV = pValue;
        } else if (pRef != null && !pRef.isEmpty()) {
          isRef = true;
          pV = pRef;
          refs.add(pRef);
        }
        PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
      }
      beanDefinition.setPropertyValues(PVS);

      // handle constructor argument values. the same way as property values
      List<Element> constructorElementsList = element.elements("constructor-arg");
      ArgumentValues AVS = new ArgumentValues();
      for (Element e : constructorElementsList) {
        String aType = e.attributeValue("type");
        String aName = e.attributeValue("name");
        String aValue = e.attributeValue("value");
        AVS.addArgumentValue(new ArgumentValue(aType, aName, aValue));
      }
      beanDefinition.setConstructorArgumentValues(AVS);

      // refArray
      String[] refArray = refs.toArray(new String[0]);
      beanDefinition.setDependsOn(refArray);

      this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
      System.out.println("successfully registered " + beanID + " beanDefinition");
    }
  }
}
