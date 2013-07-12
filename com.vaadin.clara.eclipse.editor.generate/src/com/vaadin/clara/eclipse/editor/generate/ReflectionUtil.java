package com.vaadin.clara.eclipse.editor.generate;

import java.lang.reflect.Method;

public class ReflectionUtil {

	public static boolean checkParameterType(Method method)
	{
		if(method.getParameterTypes().length != 1)
			return false;
		
		final Class<?> type = method.getParameterTypes()[0];
		
		return checkParameterType(type);
	}
	
	public static boolean checkParameterType(Class<?> type)
	{
		if(type.isPrimitive())
			return true;
		
		if(type.isEnum())
			return true;
		
		if(isWrapperType(type))
			return true;
		
		if(String.class.equals(type))
			return true;
		
		return false;
	}
	
	private static boolean isWrapperType(Class<?> type)
	{
		return 
				Boolean.class.isAssignableFrom(type) ||
				Byte.class.isAssignableFrom(type) ||
				Character.class.isAssignableFrom(type) ||
				Double.class.isAssignableFrom(type) ||
				Float.class.isAssignableFrom(type) ||
				Integer.class.isAssignableFrom(type) ||
				Long.class.isAssignableFrom(type) ||
				Short.class.isAssignableFrom(type);
	}
}
