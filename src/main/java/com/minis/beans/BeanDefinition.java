package com.minis.beans;

public class BeanDefinition {
    // a single instance is reused across all requests.
    String SCOPE_SINGLETON = "singleton";
    // a "prototype" scope means a new instance of a bean is created
    // each time it is requested from the container.
    String SCOPE_PROTOTYPE = "prototype";
    private String scope = SCOPE_SINGLETON;

    // body : id and className
    private String id;
    private String className;

    /* volatile indicates its value can be changed by different threads during program execution.
    The volatile keyword ensures that the value of beanClass is always read from the main memory,
    not just from a thread's local cache, providing a level of thread safety. */
    private volatile Object beanClass;

    // where or not such bean would use lazy initiate
    private boolean lazyInit = false;
    // after a Bean is constructed and instantiated, framework call the initialization method or not
    private String initMethodName;

    // dependency between beans
    private String[] dependsOn;

    private ArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;

    public BeanDefinition(String id, String className){
        this.id = id;
        this.className = className;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getClassName() {return className;}
    public void setClassName(String className) {this.className = className;}

    /** if it returns false, implying beanClass might be holding something else
     * (like a class name as a String, or it might be null).
     * This method is a way to check the state of beanClass without directly accessing it,
     * adhering to encapsulation principles.*/
    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass(){

        return (Class<?>) this.beanClass;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return this.scope;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    public String getInitMethodName() {
        return this.initMethodName;
    }

    public void setDependsOn(String... dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String[] getDependsOn() {
        return this.dependsOn;
    }

    public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues =
                (constructorArgumentValues != null ? constructorArgumentValues : new ArgumentValues());
    }

    public ArgumentValues getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }

    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgumentValues.isEmpty();
    }
    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = (propertyValues != null ? propertyValues : new PropertyValues());
    }

    public PropertyValues getPropertyValues() {
        return this.propertyValues;
    }

}
