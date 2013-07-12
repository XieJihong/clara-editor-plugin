package com.vaadin.clara.eclipse.editor.generate;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.vaadin.ui.AbstractComponent;

/**
 * This is the {@link List} of all <b>Vaadin</b> UI component classes.
 */
@SuppressWarnings("serial")
public class ComponentClasses extends ArrayList<Class<?>> {

	public ComponentClasses(InputStream jarFileInputStream, ClassLoader classLoader) 
	{
		final ZipInputStream zipInputStream = 
				new ZipInputStream( jarFileInputStream);
		
		final List<Class<?>> foundClasses = new ArrayList<Class<?>>();
		
		ZipEntry zipEntry = null;
		
		while(true)
		{
			try 
			{
				zipEntry = zipInputStream.getNextEntry();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			if(zipEntry == null)
				break;
			
//			if(!zipEntry.getName().startsWith("com/vaadin/ui")) 	continue;
			if(!zipEntry.getName().endsWith(".class"))				continue;
			if(zipEntry.getName().contains("$")) 					continue;
			if(zipEntry.isDirectory()) 								continue;
			
			final String className = zipEntry.getName().replace(".class", "").replace("/", ".");
			
			final Class<?> _class;
			
			try 
			{
				_class = classLoader.loadClass(className);
			} 
			catch (Throwable e) 
			{
				continue;
			}
			
//			if(!_class.getPackage().getName().equals("com.vaadin.ui"))	continue;
			if(!AbstractComponent.class.isAssignableFrom(_class)) 		continue;
			if(Modifier.isAbstract( _class.getModifiers())) 			continue;
			if(Modifier.isInterface( _class.getModifiers())) 			continue;
			
			foundClasses.add(_class);
		}
		
		try {
			zipInputStream.close();
		} catch (Exception e) {}
		
		Collections.sort(foundClasses, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> class1, Class<?> class2) {
				return class1.getSimpleName().compareTo( class2.getSimpleName());
			}
		});
		
		
		for(Class<?> foundClass: foundClasses)
			add( foundClass);
	}
}
