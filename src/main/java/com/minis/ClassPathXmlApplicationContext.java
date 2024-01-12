package com.minis;
import com.minis.beans.BeanDefinition;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();
    public ClassPathXmlApplicationContext(String fileName) throws ClassNotFoundException {
        // fill beanDefinitions with all bean's definition info
        this.readXml(fileName);
        // fill singletons with each all bean's id and instance
        this.instanceBeans();
    }
    private void readXml(String fileName){
        SAXReader saxReader = new SAXReader();
        try {
            // find beans docs using filename
            URL xmlPath =
                    this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            // iterate beans, each const a beanDefinition and store them
            for (Element element : (List<Element>) rootElement.elements()){
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
                beanDefinitions.add(beanDefinition);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                singletons.put(beanDefinition.getId(),
                        Class.forName(beanDefinition.getClassName()).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
