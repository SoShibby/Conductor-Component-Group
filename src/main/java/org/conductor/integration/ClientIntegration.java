package org.conductor.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.conductor.bootstrap.Bootstrap;
import org.conductor.component.annotations.ConductorMethod;
import org.conductor.component.events.ErrorEventListener;
import org.conductor.component.types.IComponent;
import org.conductor.database.Database;
import org.conductor.database.IPropertyChangedListener;
import org.conductor.util.ReflectionUtil;
import org.conductor.util.StringUtil;
import org.reflections.Reflections;

public class ClientIntegration implements IClientIntegration {
	private Database database;
	private Map<String, Map<String, Object>> componentsOptions = new HashMap<String, Map<String, Object>>();
	private Map<String, IComponent> components = new HashMap<String, IComponent>();
	private Method onPropertyValueUpdated;
	private Method reportError;
	private Object caller;

	public ClientIntegration(Object caller, Method onPropertyValueUpdated, Method reportError) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.database = new Database();
		this.caller = caller;
		this.onPropertyValueUpdated = onPropertyValueUpdated;
		this.reportError = reportError;
	}

	public void start(Map<String, Object> componentGroupOptions) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		this.database.addPropertyChangedListener(new IPropertyChangedListener() {
			public void onPropertyValueChanged(String componentName, String propertyName, Object propertyValue) {
				try {
					componentName = StringUtil.lowercaseFirstLetter(componentName);
					onPropertyValueUpdated.invoke(caller, componentName, propertyName, propertyValue);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		this.components = new Bootstrap().start(database, componentGroupOptions, componentsOptions);
		
		for(IComponent component : this.components.values()) {
			component.addErrorEventListener(componentErrorListener);
		}
	}

	public void setInitialPropertyValue(String componentName, String propertyName, Object propertyValue) {
		System.out.println("ClientIntegration | setInitialPropertyValue '" + componentName + "':'" + propertyName + "' -> " + propertyValue);
		componentName = StringUtil.capitalizeFirstLetter(componentName);
		this.database.setValue(componentName, propertyName, propertyValue);
	}

	public void setComponentOptions(String componentName, Map<String, Object> options) {
		componentName = StringUtil.capitalizeFirstLetter(componentName);
		this.componentsOptions.put(componentName, options);
	}

	public void setPropertyValue(String componentName, String propertyName, Object propertyValue) throws Exception {
		componentName = StringUtil.capitalizeFirstLetter(componentName);
		propertyName = StringUtil.capitalizeFirstLetter(propertyName);

		IComponent component = components.get(componentName);
		component.getClass().getMethod("set" + propertyName, propertyValue.getClass()).invoke(component, propertyValue);
	}

	public void callMethod(String componentName, String methodName, Map<String, Object> parameters) throws Exception {
		componentName = StringUtil.capitalizeFirstLetter(componentName);
		IComponent component = components.get(componentName);

		Reflections reflections = new Reflections(component);
		Set<Method> methods = reflections.getMethodsAnnotatedWith(ConductorMethod.class);

		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Parameter[] methodParameters = method.getParameters();

				// We need to sort the parameters, because the incoming parameters is in no specific order.
				Object[] sortedParameters = new Object[methodParameters.length];
				for (int i = 0; i < methodParameters.length; i++) {
					String parameterName = methodParameters[i].getName();
					if (!parameters.containsKey(parameterName)) {
						throw new Exception("Missing parameter '" + parameterName + "'.");
					}
					sortedParameters[i] = parameters.get(parameterName);
				}
				
				method.invoke(component, sortedParameters);
				return;
			}
		}

		throw new Exception("No method was found with the name '" + methodName + "', have you missed adding the annotation to the method?");
	}

	public void destroy() {
		for (IComponent component : components.values()) {
			component.destroy();
		}
	}
	
	ErrorEventListener componentErrorListener = new ErrorEventListener() {
		
		@Override
		public void onError(IComponent component, String errorMessage, String stackTrace) {
			String componentName = StringUtil.lowercaseFirstLetter(component.getClass().getSimpleName());
			
			try {
				reportError.invoke(caller, componentName, errorMessage, stackTrace);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
}
