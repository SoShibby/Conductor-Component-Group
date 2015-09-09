package org.conductor.database;

public interface IPropertyChangedListener {
	public void onPropertyValueChanged(String componentName, String propertyName, Object propertyValue);
}
