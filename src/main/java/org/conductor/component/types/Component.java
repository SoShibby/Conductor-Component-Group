package org.conductor.component.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.conductor.component.events.ErrorEventListener;
import org.conductor.database.Database;

public abstract class Component implements IComponent {
	private Database database;
	private Map<String, Object> options;
	private List<ErrorEventListener> errorEventListeners = new ArrayList<ErrorEventListener>();

	public Component(Database database, Map<String, Object> options) {
		this.database = database;
		this.options = options;
	}

	public Object getPropertyValue(String propertyName) throws Exception {
		return this.database.getValue(this.getClass().getSimpleName(), propertyName);
	}

	public boolean hasPropertyValueChanged(String propertyName, Object newPropertyValue) throws Exception {
		return !getPropertyValue(propertyName).equals(newPropertyValue);
	}

	public void updatePropertyValue(String propertyName, Object newPropertyValue) throws Exception {
		if (hasPropertyValueChanged(propertyName, newPropertyValue)) {
			this.database.setValue(this.getClass().getSimpleName(), propertyName, newPropertyValue);
		}
	}
	
	public void reportError(String errorMessage) {
		this.reportError(errorMessage, null);
	}
	
	public void reportError(String errorMessage, String stackTrace) {
		for(ErrorEventListener listener : this.errorEventListeners) {
			listener.onError(this, errorMessage, stackTrace);
		}
	}

	public Object getOptionValue(String name) {
		return this.options.get(name);
	}
	
	public void destroy() {
		
	}

	public void addErrorEventListener(ErrorEventListener listener) {
		this.errorEventListeners.add(listener);
	}
	
	public void removeErrorEventListener(ErrorEventListener listener) {
		this.errorEventListeners.remove(listener);
	}
}
