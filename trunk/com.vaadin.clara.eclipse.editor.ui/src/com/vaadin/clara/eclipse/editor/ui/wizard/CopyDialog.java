package com.vaadin.clara.eclipse.editor.ui.wizard;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

public class CopyDialog extends InputDialog{

	public CopyDialog(Shell parentShell, IWizardPage page) 
	{
		super(
				parentShell, "", 
				"If your UI class is located in the same package as the clara markup file, then you can create (parse) " +
				"the root component by calling the following line of code (which will be copied to the clipboard now).", 
				String.format("Clara.create( getClass().getResourceAsStream(\"%s\"))", ((WizardNewFileCreationPage) page).getFileName()), 
				null);
	}

	
	@Override
	protected Control createContents(Composite parent) 
	{
		final Control control = super.createContents(parent);
		getText().addVerifyListener( new VerifyListener() {
			
			@Override
			public void verifyText(VerifyEvent e) {
				e.doit = false;
			}
		});
		return control;
	}
	
	
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) 
	{
		if(id == IDialogConstants.CANCEL_ID)
			return null;
		
		return super.createButton(parent, id, label, defaultButton);
	}

	
	@Override
	protected void okPressed() 
	{
		getText().copy();
		super.okPressed();
	}
}
