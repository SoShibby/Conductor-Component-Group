package org.conductor.util;

public final class StringUtil {
	public static String capitalizeFirstLetter(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String lowercaseFirstLetter(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
}
