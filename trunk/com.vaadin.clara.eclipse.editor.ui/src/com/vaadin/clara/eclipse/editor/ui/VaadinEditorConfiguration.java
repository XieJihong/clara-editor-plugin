package com.vaadin.clara.eclipse.editor.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.sse.ui.contentassist.CompletionProposalInvocationContext;
import org.eclipse.wst.sse.ui.contentassist.ICompletionProposalComputer;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.eclipse.wst.xml.ui.internal.contentassist.XMLStructuredContentAssistProcessor;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;

import com.vaadin.clara.eclipse.editor.ui.proposals.VaadinProposal;

/**
 * A way to filter other contributed completion proposals.
 * 
 * <h1>The problem:</h1>
 * The XML Editor ({@link XMLMultiPageEditorPart}) offers completion proposals which are contributed by several 
 * <b>org.eclipse.wst.sse.ui.completionProposal </b> extensions ({@link ICompletionProposalComputer} implementations).
 * <br><br>
 * One of them creates proposals for XML-elements (and -attributes) which where found in the XML document. This leads 
 * to duplicated tag proposals.
 *
 */
@SuppressWarnings("restriction")
public class VaadinEditorConfiguration extends StructuredTextViewerConfigurationXML {


	@Override
	@SuppressWarnings("rawtypes")
	public IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) 
	{
		final IContentAssistProcessor processor = 
				new XMLStructuredContentAssistProcessor( this.getContentAssistant(), partitionType, sourceViewer) 
		{
			@Override
			protected List filterAndSortProposals(
					List proposals, IProgressMonitor monitor, CompletionProposalInvocationContext context) 
			{
				final List<ICompletionProposal> vaadinProposals = new ArrayList<ICompletionProposal>();
				
				for(Object proposal: proposals)
					if(proposal instanceof VaadinProposal)
						vaadinProposals.add( (ICompletionProposal) proposal);
				
				return Arrays.asList(vaadinProposals.toArray());
			}
		};
		
		return new IContentAssistProcessor[] {processor};
	}
}
