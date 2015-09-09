package org.conductor.component.types;

import java.util.Map;

import org.conductor.component.events.ErrorEventListener;

public interface IComponent {
	public Object getPropertyValue(String propertyName) throws Exception;
	public void updatePropertyValue(String propertyName, Object propertyValue) throws Exception;
	public Object getOptionValue(String name);
	public void destroy();
	public void addErrorEventListener(ErrorEventListener listener);	
	public void removeErrorEventListener(ErrorEventListener listener);	
}
