package com.vaadin.clara.eclipse.editor.ui.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewFileResourceWizard;

public class NewClaraFileWizard extends BasicNewFileResourceWizard {

	@Override
	public void addPages() 
	{
		super.addPages();
		
		final WizardNewFileCreationPage page = (WizardNewFileCreationPage) getStartingPage();
		page.setTitle("Clara (Vaadin 7) XML File");
		page.setDescription("Creates a new file (the file extension '*.clara' will be added).");
		page.setFileExtension("clara");

		setInitialValue(page);
	}
	
	
	private void setInitialValue(WizardNewFileCreationPage page)
	{		
		IFile selectedFile = null;
	
		final Object selectedElement = getSelection().getFirstElement();
		
		if(selectedElement instanceof IFile)
		{
			selectedFile = (IFile) selectedElement;
		}
		else
		{
			try { selectedFile = (IFile) ((IAdaptable) selectedElement).getAdapter(IResource.class); } catch (Exception e) {}
		}
		
		if(selectedFile != null)
			page.setFileName( selectedFile.getLocation().removeFileExtension().lastSegment());
	}
	
	
	@Override
	public boolean performFinish() 
	{
		boolean success = super.performFinish();

		if (success) 
			new CopyDialog(getShell(), getStartingPage()).open();

		return success;
	}
}
