package org.conductor.component.events;

import org.conductor.component.types.IComponent;

public interface ErrorEventListener {
	public void onError(IComponent component, String errorMessage, String stackTrace);
}
