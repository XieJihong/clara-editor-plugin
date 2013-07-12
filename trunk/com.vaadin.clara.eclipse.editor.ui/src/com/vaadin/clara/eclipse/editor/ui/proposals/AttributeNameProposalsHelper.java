package com.vaadin.clara.eclipse.editor.ui.proposals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute;
import com.vaadin.clara.eclipse.editor.model.Component;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.model.Namespace;
import com.vaadin.clara.eclipse.editor.ui.proposals.util.ProposalInfo;

/**
 * //TODO documentation. 
 */
@SuppressWarnings("restriction")
public class AttributeNameProposalsHelper extends AbstractProposalHelper{

	
	public AttributeNameProposalsHelper(ComponentDefinitions definitions) {
		super(definitions);
	}

	@Override
	public void addCompletionProposals(ContentAssistRequest contentAssistRequest) 
	{
		final Component component 	= getDefinitions().getComponent( contentAssistRequest.getNode().getNodeName());
		final Component parent		= getDefinitions().getComponent( contentAssistRequest.getNode().getParentNode().getNodeName());
		
		if(component == null)
			return;
		
		final List<String> existingAttributes = getExistingAttributes( (Element) contentAssistRequest.getNode());
		
		final ProposalInfo proposalInfo = new ProposalInfo();
		
		final Document document 								= contentAssistRequest.getNode().getOwnerDocument();
		final Collection<String> permittedNamespacePrefixes 	= getPermittedNamespacePrefixes(document);
		
		final List<AbstractAttribute> attributes = 
				new ArrayList<AbstractAttribute>( 
						component.findAttributeMatches(
								contentAssistRequest.getMatchString(),
								permittedNamespacePrefixes));
		
		if(parent != null)
			attributes.addAll( 
					parent.findChildAttributeMatches(
							contentAssistRequest.getMatchString(),
							permittedNamespacePrefixes));
		
		for(AbstractAttribute attribute: attributes)
		{
			final String attributeName = attribute.getName();
			final String attributeWithNamespacePrefix = attribute.getNameWithNamespacePrefix();
			
			if(existingAttributes.contains( attributeWithNamespacePrefix))
				continue;
			
			String text = String.format("%s=\"\"", attributeWithNamespacePrefix);
			
			proposalInfo.clear();
			proposalInfo.addRow("Type:", attribute.getTargetTypeInfo());
			
			if(!attribute.getValues().isEmpty())
				proposalInfo.addRow("Values:", attribute.getValues().toString().replaceAll("(\\[|\\])", ""));
			
			if(!attribute.getAdditionalInfo().isEmpty())
				proposalInfo.addRow("Info:", attribute.getAdditionalInfo());
			
		
			contentAssistRequest.addProposal( 
					new VaadinProposal(
							text, 
							contentAssistRequest.getReplacementBeginPosition(), 
							contentAssistRequest.getMatchString().length(), 
							text.lastIndexOf("\""),
							getVaadinImage(),
							attributeName,
							null,
							proposalInfo.toString()));
		}
		
		
		
		
		//+ Add namespace proposals if we are dealing with a root element:
		if(parent == null)
		{
			//+ Take all known namespaces (that are available in the "Completion Proposal Model") ...
			final List<Namespace> allNamespaces 	= new ArrayList<Namespace>( getDefinitions().getNamespaces());
			
			//... and remove those namespaces which are already declared in the root XML element: 
			final NamedNodeMap namedNodeMap 		= contentAssistRequest.getNode().getAttributes();
			
			for(int i=0; i<namedNodeMap.getLength(); i++)
			{
				final String attribute = namedNodeMap.item(i).getNodeName();
				
				if(attribute.startsWith("xmlns"))
				{
					final String namespacePrefix 	= attribute.replaceAll("xmlns:?", "");
					final String namespaceUri		= namedNodeMap.item(i).getNodeValue();
					
					allNamespaces.remove( new Namespace( namespacePrefix, namespaceUri));
				}
			}
			
			
			//+ Create "xmlns" attribute proposals for all (remaining) missing namespaces:
			for(Namespace missingNamespace: allNamespaces)
			{
				final String displayText = 
						String.format(" xmlns \"%s\"", missingNamespace.getUri().replaceAll(".*:", ""));
				
				final String replacementText = missingNamespace.getXmlns(); 
				
				contentAssistRequest.addProposal( 
						new VaadinProposal(
								replacementText, 
								contentAssistRequest.getReplacementBeginPosition(), 
								contentAssistRequest.getMatchString().length(), 
								replacementText.length(),
								getVaadinImage(),
								displayText,
								null,
								null));
			}
		}
	}
	
	private List<String> getExistingAttributes(Element element)
	{
		final List<String> existingAttributes = new ArrayList<String>();
		
		final NamedNodeMap namedNodeMap = element.getAttributes();
		
		for (int i = 0; i < namedNodeMap.getLength(); i++) 
			existingAttributes.add( namedNodeMap.item(i).getNodeName());
		
		return existingAttributes;
	}
}
