package org.conductor.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {
	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
		final List<Method> methods = new ArrayList<Method>();
		final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(type.getDeclaredMethods()));
		
		for (final Method method : allMethods) {
			if (method.isAnnotationPresent(annotation)) {
				methods.add(method);
			}
		}
		
		return methods;
	}
}
