package org.conductor.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
	private Map<String, Map<String, Object>> database = new HashMap<String, Map<String, Object>>();
	private List<IPropertyChangedListener> eventListeners = new ArrayList<IPropertyChangedListener>();
	
	public void setValue(String componentName, String propertyName, Object value){
		if(!database.containsKey(componentName)) {
			database.put(componentName, new HashMap<String, Object>());
		}
		
		database.get(componentName).put(propertyName, value);
		
		for(IPropertyChangedListener eventListener : eventListeners) {
			eventListener.onPropertyValueChanged(componentName, propertyName, value);
		}
	}
	
	public Object getValue(String componentName, String propertyName) throws Exception{
		if(!database.containsKey(componentName)) {
			throw new Exception("No value exist in the database for the component: '" + componentName + "' and property name: " + propertyName);
		}
		
		return database.get(componentName).get(propertyName);
	}
	
	public void addPropertyChangedListener(IPropertyChangedListener listener) {
		eventListeners.add(listener);
	}
	
	public void removePropertyChangedListener(IPropertyChangedListener listener) {
		eventListeners.remove(listener);
	}
}
