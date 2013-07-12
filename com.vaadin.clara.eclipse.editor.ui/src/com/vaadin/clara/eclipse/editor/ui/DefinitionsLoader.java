package com.vaadin.clara.eclipse.editor.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.vaadin.clara.eclipse.editor.generate.Generator;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;

public class DefinitionsLoader {

	private File xmlFile;
	
	public DefinitionsLoader() 
	{
		xmlFile = initXmlFile();
	}

	protected File initXmlFile()
	{
		final Bundle bundle 			= Activator.getDefault().getBundle();
		final IPath stateLocationPath 	= Platform.getStateLocation(bundle);
		
		final File xmlFile = stateLocationPath.append("definition.xml").toFile();
		
		try 
		{
			if(!xmlFile.exists())
				extractDefinitionFileTemplate(bundle, xmlFile);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();//TODO 
		}
		
		return xmlFile;
	}
	

	public File getXmlFile() {
		return xmlFile;
	}
	
	private void extractDefinitionFileTemplate(Bundle bundle, File file) throws IOException
	{
		new Generator().generate(file);
	}
	
	public ComponentDefinitions loadDefinitions()
	{
		try {
			return ComponentDefinitions.load(new FileInputStream(xmlFile));
		} catch (Exception e) {
			e.printStackTrace();//TODO
			return new ComponentDefinitions();
		}
	}
}
