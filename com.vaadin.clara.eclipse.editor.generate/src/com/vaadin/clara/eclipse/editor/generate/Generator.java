package com.vaadin.clara.eclipse.editor.generate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;


public class Generator {
	
	private final ComponentDefinitions existingDefinitions;
	
	public Generator(ComponentDefinitions existingDefinitions) 
	{
		this.existingDefinitions = existingDefinitions;
	}
	
	public Generator() {
		this(null);
	}
	
	public void generate(File xmlFile, File... addOnJarFiles)
	{
		//+ Initialize class loader for add-on jar files:
		final List<URL> urls = new ArrayList<URL>();
		
		for(File jarFile: addOnJarFiles)
		{
			try {
				urls.add( jarFile.toURI().toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		
		URLClassLoader classLoader = new URLClassLoader(
				urls.toArray( new URL[ urls.size()]), 
				getClass().getClassLoader());
		
		
		
		//+ Collect the jar file input-streams to search for class files.
		final List<InputStream> jarFileInputStreams = new ArrayList<InputStream>();
		
		for(File jarFile: addOnJarFiles)
		{
			try 
			{
				final FileInputStream fileInputStream = new FileInputStream(jarFile);
				jarFileInputStreams.add(fileInputStream);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();//TODO
			}
		}
		
		jarFileInputStreams.add( getClass().getResourceAsStream("vaadin-server-7.0.5.jar"));
		
		
		
		//+ Find all load-able Vaadin component classes: 
		List<Class<?>> componentClasses = new ArrayList<Class<?>>();
		
		for(InputStream jarFileInputStream: jarFileInputStreams)
			componentClasses.addAll( new ComponentClasses( jarFileInputStream, classLoader));
		
		
		//+ Create the "completion proposal model" from the found component classes: 
		final ComponentDefinitions definitions = 
				new PoroposalModelCreator(componentClasses, existingDefinitions).getComponentDefinitions();
		

		//+ Unload the classes and the class-loader (in order to close the opened jar-files): 
		componentClasses 	= null;
		classLoader 		= null;
		System.gc();
//		In Java 7: classLoader.close();
		
		
		//+ Save the "completion proposal model" as XML file:
		Serializer.save(definitions, xmlFile);
	}
}
