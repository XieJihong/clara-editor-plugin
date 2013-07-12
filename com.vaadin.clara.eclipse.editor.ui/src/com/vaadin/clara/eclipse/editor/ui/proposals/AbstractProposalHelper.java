package com.vaadin.clara.eclipse.editor.ui.proposals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.model.Namespace;
import com.vaadin.clara.eclipse.editor.ui.Activator;

/**
 * //TODO documentation. 
 */
@SuppressWarnings("restriction")
public abstract class AbstractProposalHelper {

	private final ComponentDefinitions definitions;
	private final Image vaadinImage;
	
	public AbstractProposalHelper(ComponentDefinitions definitions) {
		this.definitions = definitions;
		this.vaadinImage = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/logo.png").createImage();
	}

	public abstract void addCompletionProposals(ContentAssistRequest contentAssistRequest);
	
	protected List<String> filterAndSort(final String matchString, Collection<String> strings)
	{
		final List<String> filtered = new ArrayList<String>();
		
		for(String string: strings)
			if(string.toLowerCase().startsWith( matchString.toLowerCase()))
				filtered.add(string);
		
		Collections.sort(filtered);
		
		return filtered;
	}
	
	protected ComponentDefinitions getDefinitions() {
		return definitions;
	}
	
	protected Image getVaadinImage() {
		return vaadinImage;
	}
	
	
	/**
	 * @param document The XML editor content.
	 * @return The collection of namespace prefixes defined in the root element of 
	 * the given XML <b><i>document</i></b>.
	 */
	protected Collection<String> getPermittedNamespacePrefixes(Document document)
	{
		//+ If there is no root element or no namespace definitions in the root element...
		final Node rootElement = document.getFirstChild();
		
		if(rootElement == null || rootElement.getAttributes() == null)
			//... the return the default Vaadin namespace prefix:
			return Arrays.asList( Namespace.PREFIX_DEFAULT);
		
		
		//+ Otherwise return the defined namespace prefixes:
		final Set<String> permittedNamespacePrefixes = new HashSet<String>();
		
		final NamedNodeMap rootElementAttributes = rootElement.getAttributes();
		
		for (int i = 0; i < rootElementAttributes.getLength(); i++) 
		{
			final String attribute = rootElementAttributes.item(i).getNodeName();
			
			if(attribute.startsWith("xmlns"))
			{
				final String namespacePrefix = attribute.replaceAll("xmlns:?", "");
				
				permittedNamespacePrefixes.add( namespacePrefix);
			}
		}
		
		return permittedNamespacePrefixes;
	}
}
