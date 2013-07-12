package com.vaadin.clara.eclipse.editor.ui.proposals;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;

import com.vaadin.clara.eclipse.editor.ui.VaadinEditorConfiguration;


/**
 * This is our own custom {@link ICompletionProposal} implementation.
 * The main purpose is to distinguish the <b>Vaadin</b> proposals 
 * from other contributed completion proposals in the XML editor.
 * 
 * @see VaadinEditorConfiguration
 *
 */
@SuppressWarnings("restriction")
public class VaadinProposal extends CustomCompletionProposal{

	public VaadinProposal(
			String replacementString, 
			int replacementOffset,
			int replacementLength, 
			int cursorPosition, 
			Image image,
			String displayString, 
			IContextInformation contextInformation,
			String additionalProposalInfo) 
	{
		super(
				replacementString, 
				replacementOffset, 
				replacementLength, 
				cursorPosition,
				image, 
				displayString, 
				contextInformation, 
				additionalProposalInfo,
				1);
	}
}
