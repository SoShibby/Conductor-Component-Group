package org.conductor.integration;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface IClientIntegration {
	public void start(Map<String, Object> options) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException;
	public void setInitialPropertyValue(String componentName, String propertyName, Object propertyValue);
	public void setComponentOptions(String componentName, Map<String, Object> options);
	public void setPropertyValue(String componentName, String propertyName, Object propertyValue) throws Exception;
	public void callMethod(String componentName, String methodName, Map<String, Object> parameters) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, Exception;
}
