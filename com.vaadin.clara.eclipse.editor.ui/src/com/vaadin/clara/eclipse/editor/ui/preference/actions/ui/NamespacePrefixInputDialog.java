package com.vaadin.clara.eclipse.editor.ui.preference.actions.ui;

import java.util.Collection;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.vaadin.clara.eclipse.editor.model.Namespace;

public class NamespacePrefixInputDialog extends InputDialog{

	private static final String message = 
			"Please enter a XML namespace prefix for the java package \"%s\".";
	
	
	public NamespacePrefixInputDialog(Shell parentShell, String javaPackage, final Collection<Namespace> existingNamespaces) 
	{
		super(parentShell, "title", String.format(message, javaPackage), null, new IInputValidator() {
			@Override
			public String isValid(String newText) {
				
				for(Namespace namespace: existingNamespaces)
					if(namespace.getPrefix().equals( newText))
						return String.format(
								"The namespace prefix \"%s\" is already assigned to the uri \"%s\".", 
								newText, namespace.getUri());
				
				return null;
			}
		});
		
		setShellStyle(SWT.APPLICATION_MODAL|SWT.TITLE);
	}

	
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) 
	{
		if(id == IDialogConstants.CANCEL_ID)
			return null;
		
		return super.createButton(parent, id, label, defaultButton);
	}
}
