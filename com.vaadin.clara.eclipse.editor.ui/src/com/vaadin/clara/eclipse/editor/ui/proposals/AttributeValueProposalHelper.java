package com.vaadin.clara.eclipse.editor.ui.proposals;

import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegionList;
import org.eclipse.wst.xml.core.internal.parser.regions.AttributeEqualsRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.AttributeNameRegion;
import org.eclipse.wst.xml.core.internal.parser.regions.AttributeValueRegion;
import org.eclipse.wst.xml.ui.internal.contentassist.ContentAssistRequest;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLTemplatesCompletionProposalComputer;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute;
import com.vaadin.clara.eclipse.editor.model.Component;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;

/**
 * //TODO documentation. 
 */
@SuppressWarnings("restriction")
public class AttributeValueProposalHelper extends AbstractProposalHelper {

	public AttributeValueProposalHelper(ComponentDefinitions definitions) {
		super(definitions);
	}
	
	@Override
	public void addCompletionProposals(ContentAssistRequest contentAssistRequest) 
	{
		//+ What is the current component and the parent component type?
		final Component component 	= getDefinitions().getComponent( contentAssistRequest.getNode().getNodeName());
		final Component parent		= getDefinitions().getComponent( contentAssistRequest.getNode().getParentNode().getNodeName());
		
		if(component == null) return;
		
		//+ What is the current attribute or rather the current attribute type (because we want to know the determined values)?
		final String attributeName 		= getAttributeName(contentAssistRequest);
		AbstractAttribute attribute		= component.getAttribute(attributeName);
		
		
		//+ The attribute type will usually determined by the current component type but can
		//  also be specified by the parent component type (see the 'ChildAttribute' or 'Component' class documentation).
		if(attribute == null) 
		{
			if(parent != null)
			{
				attribute = parent.getChildAttribute(attributeName);
				
				if(attribute == null)
					return;
			}
		}
		
		
		//+ If we have determined the attribute type then we can get the attribute values...
		final List<String> values = attribute.getValues();
		
		//... and generate the proposals.
		if(!values.isEmpty())
			addCompletionProposals(contentAssistRequest, values);
	}
	
	
	/**
	 * Adds "value proposals" to the given content assistant.  
	 */
	private void addCompletionProposals(ContentAssistRequest contentAssistRequest, List<String> values)
	{
		//The match string can start with the typed '"' (that has triggered the content assistant) which we have to remove
		final String matchString = contentAssistRequest.getMatchString().replaceFirst("\"", "");
		
		final List<String> filtered = filterAndSort(matchString, values);
		
		for(String value: filtered)
		{
			String text = value;
			
			if(contentAssistRequest.getMatchString().startsWith("\""))
				//The typed '"' in the Editor belongs to the replacement region and will be overwritten by the insertion, 
				//so we add the '"' at the beginning.
				text = "\"" + text; 
			
			contentAssistRequest.addProposal( 
					new VaadinProposal(
							text, 
							contentAssistRequest.getReplacementBeginPosition(), 
							contentAssistRequest.getMatchString().length(), 
							(text + "\"").length(), 
							null, value, null, null));
		}
	}
	
	
	/**
	 * Unfortunately the {@link ContentAssistRequest} doesn't have much information about the regarding
	 * XML attribute at this point (when the <code>addAttributeValueProposals()</code> method of the 
	 * {@link XMLTemplatesCompletionProposalComputer} was called).
	 * <br><br>
	 * So we have the extract the attribute name using the text {@link ITextRegion}s of the current 
	 * {@link IStructuredDocumentRegion}. 
	 * 
	 * @return The attribute on which the "value completion proposal" processing was triggered.
	 */
	@SuppressWarnings("unused")
	private String getAttributeName(ContentAssistRequest contentAssistRequest)
	{
		final IStructuredDocumentRegion documentRegion 				= contentAssistRequest.getDocumentRegion();
		final ITextRegionList regionList 							= documentRegion.getRegions();
		
		//The current document region is split into several text regions:
		
		//+ The current region is the "value region" (where the cursor is located, containing the '"' signs)
		final AttributeValueRegion attributeValueRegion 			= (AttributeValueRegion) contentAssistRequest.getRegion();
		
		final int attributeValueRegionIndex 						= regionList.indexOf(attributeValueRegion);
		
		//+ The region before should be the "equals region" (containing the '=' sign)
		final AttributeEqualsRegion attributeEqualsRegion 			= (AttributeEqualsRegion) regionList.get( attributeValueRegionIndex - 1); //just to assure that we have a valid order of regions ('attribute', '=' and '""')
		
		//+ And before that there is the "attribute name region" (containing the attribute name characters)
		final AttributeNameRegion attributeNameRegion 				= (AttributeNameRegion) regionList.get( attributeValueRegionIndex - 2);
		
		
		//+ We return the extracted attribute name.
		final String attributeName = documentRegion.getFullText().substring( attributeNameRegion.getStart(), attributeNameRegion.getEnd());
		
		return attributeName;
	}
}
