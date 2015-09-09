package org.conductor.bootstrap;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.conductor.component.types.IComponent;
import org.conductor.database.Database;
import org.conductor.integration.factories.ComponentFactory;

public class Bootstrap {
	public Map<String, IComponent> start(Database database, Map<String, Object> componentGroupOptions, Map<String, Map<String, Object>> componentsOptions) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		ComponentFactory componentFactory = new ComponentFactory();
		List<String> componentNames = componentFactory.getComponentNames();
		Map<String, IComponent> components = new HashMap<String, IComponent>();
		
		for(String componentName : componentNames) {
			Map<String, Object> componentOption = componentsOptions.get(componentName);
			IComponent component = componentFactory.createComponent(componentName, database, componentOption);
			components.put(componentName, component);
		}
		
		return components;
	}
}
