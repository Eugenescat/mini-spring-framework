package com.minis.beans;

public class PropertyValue {
  private final String name;
  private final Object value;
  private final String type;
  public PropertyValue(String type, String name, Object value) {
    this.type = type;
    this.name = name;
    this.value = value;
  }

  public String getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public Object getValue() {
    return this.value;
  }
}
