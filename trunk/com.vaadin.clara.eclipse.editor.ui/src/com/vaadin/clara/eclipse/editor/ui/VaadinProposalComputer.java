package com.vaadin.clara.eclipse.editor.ui;

import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLTemplatesCompletionProposalComputer;

import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.ui.proposals.AbstractProposalHelper;
import com.vaadin.clara.eclipse.editor.ui.proposals.AttributeNameProposalsHelper;
import com.vaadin.clara.eclipse.editor.ui.proposals.AttributeValueProposalHelper;
import com.vaadin.clara.eclipse.editor.ui.proposals.TagNameProposalHelper;

/**
 * Generates completion proposals for:
 * <ul>
 * 	<li>XML tag names</li>
 * 	<li>XML element attribute names</li>
 * 	<li>XML element attribute values</li>
 * </ul>
 * by the help of the different {@link AbstractProposalHelper} implementations.
 */
@SuppressWarnings("restriction")
public class VaadinProposalComputer extends XMLTemplatesCompletionProposalComputer{

	private ComponentDefinitions definitions;
	
	private final TagNameProposalHelper tagNameProposalHelper;
	private final AttributeNameProposalsHelper attributeNameProposalHelper;
	private final AttributeValueProposalHelper attributeValueProposalHelper;
	
	public VaadinProposalComputer() 
	{
		definitions 					= new DefinitionsLoader().loadDefinitions();
		
		tagNameProposalHelper 			= new TagNameProposalHelper(definitions);
		attributeNameProposalHelper 	= new AttributeNameProposalsHelper(definitions);
		attributeValueProposalHelper 	= new AttributeValueProposalHelper(definitions);
	}
	
	@Override
	protected void addTagNameProposals(
			ContentAssistRequest contentAssistRequest, int childPosition, CompletionProposalInvocationContext context) 
	{
		tagNameProposalHelper.addCompletionProposals(contentAssistRequest);
	}
	
	protected void addAttributeNameProposals(ContentAssistRequest contentAssistRequest, CompletionProposalInvocationContext context) 
	{
		attributeNameProposalHelper.addCompletionProposals(contentAssistRequest);
	};
	
	@Override
	protected void addAttributeValueProposals(
			ContentAssistRequest contentAssistRequest, CompletionProposalInvocationContext context) 
	{
		attributeValueProposalHelper.addCompletionProposals(contentAssistRequest);
	}
	
}
