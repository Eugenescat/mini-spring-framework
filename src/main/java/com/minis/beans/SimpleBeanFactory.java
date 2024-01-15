package com.minis.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** STORE beans, can do getBean and registerBeanDefinition */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

  private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
  protected List<String> beanNamesList = new ArrayList<>();
  private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);
  public SimpleBeanFactory() {
  }
  /** create all the beans as bundle */
  public void refresh() {
    for (String beanName : beanNamesList) {
      try {
        getBean(beanName);
      } catch (ClassNotFoundException | BeansException e) {
        //throw new RuntimeException(e);
        e.printStackTrace();
      }
    }
  }

  // implement functions of BeanFactory
  /** instantiate the bean to get it, use the given beanName to search for the beanDefinition */
  public Object getBean(String beanName) throws BeansException, ClassNotFoundException {
    // try if the bean is already registered, use methods extends from DefaultSingletonBeanRegistry
    Object singleton = this.getSingleton(beanName);
    // if not registered, try to find in cached hollow beans (earlySingletonObjects)
    if (singleton == null) {
      System.out.println(beanName + " not created as singleton");
      singleton = this.earlySingletonObjects.get(beanName);
      // if even no earlySingletonObjects, get beanDefinition and create it
      if (singleton == null) {
        System.out.println(beanName + " not cached as earlySingleton, handle creating it");
        BeanDefinition bd = beanDefinitionMap.get(beanName);
        singleton = createBean(bd);
        this.registerBean(beanName, singleton);
        System.out.println(beanName + " singleton registered");
        if (bd.getInitMethodName() != null) {
          // ToDo: init
        }
        // if not even found in the beanDefinitionMap, throw exception
        if (bd == null) {
          throw new BeansException("bean not found");
        }
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
        //throw new BeansException("bean instantiated error");
        e.printStackTrace();
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

  /** based on the given beanDefinition, create a bean */
  public Object createBean(BeanDefinition bd) throws BeansException, ClassNotFoundException {
    System.out.println("handle caching " + bd.getId() + " " + bd.getClassName());
    // create the hollow bean object, handling constructor arguments， cache in the earlySingletons
    Object obj = preCreateBean(bd);
    this.earlySingletonObjects.put(bd.getId(), obj);
    System.out.println(bd.getId() + " bean cached. " + bd.getClassName() + " : " + obj.toString());

    System.out.println("handle properties for bean : " + bd.getId());
    // handle properties
    handleProperties(bd, obj);
    System.out.println("finished properties for bean : " + bd.getId());

    return obj;
  }

  /** create a hollow bean and cache it, without handling properties */
  private Object preCreateBean(BeanDefinition bd) throws BeansException {
    Class<?> beanCls = null;
    Object obj = null;
    Constructor<?> beanCons = null;

    try {
      // obtain class from this beanDefinition
      beanCls = Class.forName(bd.getClassName());

      // obtain argument values from this beanDefinition
      ArgumentValues argumentValues = bd.getConstructorArgumentValues();

      // if this beanDefinition has argument values
      if (!argumentValues.isEmpty()) {
        int n = argumentValues.getArgumentCount();
        Class<?>[] paramTypes = new Class<?>[n];
        Object[] paramValues = new Object[n];

        // store argument values into the two arrays: type array, value array
        for (int i = 0; i < argumentValues.getArgumentCount(); i++){
          ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
          if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
            paramTypes[i] = String.class;
            paramValues[i] = argumentValue.getValue();
          } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
            paramTypes[i] = Integer.class;
            paramValues[i] = Integer.valueOf((String)argumentValue.getValue());
          } else if ("int".equals(argumentValue.getType())) {
            paramTypes[i] = int.class;
            paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
          } else { // default as string
            paramTypes[i] = String.class;
            paramValues[i] = argumentValue.getValue();
          }

        }
        // use the argument values to create an instance
        try {
          beanCons = beanCls.getConstructor(paramTypes);
          // feed the constructor an array of parameters
          obj = beanCons.newInstance(paramValues);
        } catch (InvocationTargetException | NoSuchMethodException e) {
          e.printStackTrace();;
        }
      }
      // if this beanDefinition has no argument values, directly create objects using obtained class
      else {
        obj = beanCls.newInstance();
      }
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
    //  throw new BeansException("Error create bean handling constructor arguments");
      e.printStackTrace();
    }

    return obj;
  }

  /** add properties to the cached hollow object bean */
  public void handleProperties(BeanDefinition bd, Object obj) throws BeansException, ClassNotFoundException {
    PropertyValues propertyValues = bd.getPropertyValues();
    Class<?> beanCls = Class.forName(bd.getClassName());

    if (!propertyValues.isEmpty()) {
      for (int i = 0; i < propertyValues.size(); i++) {
        PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
        String pName = propertyValue.getName();
        String pType = propertyValue.getType();
        Object pValue = propertyValue.getValue(); // value may be value or refName
        boolean isRef = propertyValue.getIsRef();
        Class<?>[] paramTypes = new Class<?>[1]; // Java not allow for create a class with unknown type, thus use array[1]
        Object[] paramValues = new Object[1];

        // for regular property
        if (!isRef) {
          // store its Type
          if ("String".equals(pType) || "java.lang.String".equals(pType)) {
            paramTypes[0] = String.class;
          } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
            paramTypes[0] = Integer.class;
          } else if ("int".equals(pType)) {
            paramTypes[0] = int.class;
          } else {
            paramTypes[0] = String.class;
          }
          paramValues[0] = pValue; // store Value
        }

        // for ref property
        else {
          System.out.println(bd.getId() + " isRef to: " + propertyValue.getType() + " : " + pValue);
          try {
            paramTypes[0] = Class.forName(pType); // store Type
          } catch (ClassNotFoundException e) {
            //throw new BeansException("class not found");
            e.printStackTrace();
          }
          try {
            // instantiate the bean and store it (pValue is the beanName in ref property)
            paramValues[0] = getBean((String)pValue);
          } catch (BeansException e) {
            //throw new BeansException("class not found");
            e.printStackTrace();
          }
        }

        // feels like: set + "A" + "ge" = setAge
        String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

        // reflection
        // A Method provides information about, and access to, a single method on a class/interface.
        Method method = null;

        // The parameterTypes parameter is an array of Class objects that identify
        // the method's formal parameter types, in declared order.
        try {
          method = beanCls.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
          //throw new BeansException("method not found");
          e.printStackTrace();
        }
        try {
          // feels like: obj.method(paramValues) e.g. sarah.setAge(10)
          method.invoke(obj, paramValues);
        } catch (InvocationTargetException | IllegalAccessException e) {
          //throw new RuntimeException(e);
          e.printStackTrace();
        }
      }
    }
  }
}
