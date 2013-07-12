package com.vaadin.clara.eclipse.editor.ui.proposals;

import java.util.Collection;
import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.TagNameRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.TagOpenRegion;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.vaadin.clara.eclipse.editor.model.Component;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;

/**
 * //TODO documentation. 
 */
@SuppressWarnings("restriction")
public class TagNameProposalHelper extends AbstractProposalHelper{

	public TagNameProposalHelper(ComponentDefinitions definitions) {
		super(definitions);
	}
	
	@Override
	public void addCompletionProposals(final ContentAssistRequest contentAssistRequest) 
	{
		if(!canCreateComponent(contentAssistRequest))
			return;
		
		final Document document 								= contentAssistRequest.getNode().getOwnerDocument();
		final Collection<String> permittedNamespacePrefixes 	= getPermittedNamespacePrefixes(document);
		
		boolean isRootElement = contentAssistRequest.getParent() instanceof Document;
		
		final List<Component> matchingComponents = 
				getDefinitions().findMatches(
						contentAssistRequest.getMatchString(),
						permittedNamespacePrefixes);
		
		for(Component component: matchingComponents)
		{
			//+ Create the XML tag string to insert.
			final String tag = component.getName();
			
			String xml;
			
			if(component.getChildCount() == 0)
				xml = String.format("<%s/>", tag);
			else
				xml = String.format("<%s></%s>", tag, tag);
			
			if(!component.getNamespacePrefix().isEmpty())
				xml = xml.replaceFirst("<", String.format("<%s:", component.getNamespacePrefix()));
			
			if(isRootElement)
				xml = xml.replaceFirst(">", String.format(" %s>", getDefinitions().getDefaultXmlnsString()));
			
			//+ The typed "<" in the XML editor (which triggered the activation of the content assistant) 
			//  is not part of the replacement region, so let's remove the first "<" from the beginning
			//  of the XML tag, otherwise we get a "<<" in the XML editor.
			final ITextRegion textRegion = contentAssistRequest.getRegion();
			
			if(textRegion instanceof TagOpenRegion || textRegion instanceof TagNameRegion)
				xml = xml.replaceFirst("<", "");
			
			contentAssistRequest.addProposal( 
					new VaadinProposal(
							xml, 
							contentAssistRequest.getReplacementBeginPosition(), 
							contentAssistRequest.getMatchString().length(), 
							xml.indexOf(">") + 1, getVaadinImage(), tag, null, null));
		}
	}
	
	
	
	/**
	 * Is it permitted to add more child elements to the parent element?
	 */
	private boolean canCreateComponent(ContentAssistRequest contentAssistRequest)
	{
		final Node parent = contentAssistRequest.getParent();
		
		//+ How many children are allowed in the parent node?
		final int allowedChildCount;
		
		if(parent instanceof Document)
		{
			allowedChildCount = 1;
		}
		else
		{
			final Component parentComponent = getDefinitions().getComponent( parent.getNodeName());
			
			if(parentComponent == null)
				allowedChildCount = 1;
			else
				allowedChildCount = parentComponent.getChildCount();
		}
		
		//+ How many children are already existing in the parent node?
		final int currentChildCount = getChildElementCount(parent);
		
		
		//+ So is the insertion of an new component permitted?
		if(allowedChildCount == Component.INFINITE_CHILD_COUNT)
			return true;
		
		return currentChildCount < allowedChildCount;
	}
	
	
	
	private int getChildElementCount(Node node)
	{
		int count = 0;
		NodeList nodeList = node.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++)
		{
			Node child = nodeList.item(i);
			
			if(child.getNodeType() == Node.ELEMENT_NODE)
				count++;
		}
		return count;
	}
}
