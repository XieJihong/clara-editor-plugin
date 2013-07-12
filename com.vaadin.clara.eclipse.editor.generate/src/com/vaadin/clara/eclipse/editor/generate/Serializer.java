package com.vaadin.clara.eclipse.editor.generate;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;

public class Serializer {

	public static void save(ComponentDefinitions definitions, File xmlFile)
	{
		FileOutputStream fileOutputStream = null;
		
		try
		{
			fileOutputStream =  new FileOutputStream(xmlFile);
			
			final JAXBContext context = JAXBContext.newInstance(ComponentDefinitions.class);
			
			final Marshaller marshaller = context.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "\n<!-- Generated Proposal Model -->");
			marshaller.marshal(definitions, fileOutputStream);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try { fileOutputStream.close(); } catch (Exception e) {}
		}
	}
}
