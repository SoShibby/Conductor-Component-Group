package org.conductor.integration.factories;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.conductor.component.annotations.ConductorComponent;
import org.conductor.component.types.IComponent;
import org.conductor.database.Database;
import org.reflections.Reflections;

public class ComponentFactory {
	private Set<Class<?>> components;
	private static String componentsPackage = "org.conductor.components";

	public ComponentFactory() {
		Reflections reflections = new Reflections(componentsPackage);
		components = reflections.getTypesAnnotatedWith(ConductorComponent.class);
	}
	
	public List<String> getComponentNames() {
		List<String> names = new ArrayList<String>();

		for (Class<?> component : components) {
			names.add(component.getSimpleName());
		}

		return names;
	}

	public IComponent createComponent(String componentName, Database database, Map<String, Object> options) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		for (Class<?> component : components) {
			if (component.getSimpleName().equals(componentName)) {
				Constructor constructor = component.getConstructor(Database.class, Map.class);
				return (IComponent) constructor.newInstance(database, options);
			}
		}

		return null;
	}
}
