package com.vaadin.clara.eclipse.editor.ui.preference.actions;

import java.io.File;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class RestoreDefinitionsFile extends SelectionAdapter{
	
	private final File definitionsFile;
	private final Shell shell;

	public RestoreDefinitionsFile(File definitionsFile, Shell shell) {
		this.definitionsFile = definitionsFile;
		this.shell = shell;
	}
	
	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		if(!MessageDialog.openConfirm(shell, "Restore", 
				"Do you really want to restore the \"Component Proposal Model\"? \n\n" +
				"(All added namespaces and component definitions will be removed)"))
			return;
		
		
		boolean deleted = false;
		
		try { deleted = definitionsFile.delete(); } catch (Exception e) { e.printStackTrace(); }
		
		if(!deleted)
		{
			MessageDialog.openWarning(
					shell, 
					"", 
					"The \"Component Proposal Model\" file can not be deleted and restored.");
			
			return;
		}
		
		PlatformUI.getWorkbench().restart();
	}
}
