package com.vaadin.clara.eclipse.editor.model;

import java.util.Collection;

public class AddonNamespacePrefixStrategy {

	//TODO docu!
	protected String getPrefix(String javaPackage, Collection<Namespace> existingNamespaces)
	{
		int importNamespaceCount = 0;
		
		for(Namespace namespace: existingNamespaces)
			if(namespace.getUri().startsWith("urn:import:"))
				importNamespaceCount++;
		
		return "n" + importNamespaceCount;
	}
}
