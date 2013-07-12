package com.vaadin.clara.eclipse.editor.ui.preference.actions;

import java.io.File;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.vaadin.clara.eclipse.editor.generate.Generator;
import com.vaadin.clara.eclipse.editor.model.AddonNamespacePrefixStrategy;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.model.Namespace;
import com.vaadin.clara.eclipse.editor.ui.DefinitionsLoader;
import com.vaadin.clara.eclipse.editor.ui.preference.actions.ui.NamespacePrefixInputDialog;

public abstract class AddNamespace extends SelectionAdapter {

	private final File definitionsFile;
	private final ComponentDefinitions definitions;
	private final Shell shell;

	
	public AddNamespace(DefinitionsLoader definitionsLoader, Shell shell) 
	{
		this.definitionsFile = definitionsLoader.getXmlFile();
		this.definitions = definitionsLoader.loadDefinitions();
		this.shell = shell;
	}


	@Override
	public void widgetSelected(SelectionEvent e) 
	{
		final FileDialog fileDialog = new FileDialog( shell);
		fileDialog.setFilterExtensions(new String[] {"*.jar"});
		
		final String filePath = fileDialog.open();
		
		if(filePath == null)
			return;
		
		definitions.setNamespacePrefixStrategy( new AddonNamespacePrefixStrategy() {
			@Override
			protected String getPrefix(String javaPackage, Collection<Namespace> existingNamespaces) 
			{
				final NamespacePrefixInputDialog inputDialog = 
						new NamespacePrefixInputDialog(shell, javaPackage, existingNamespaces);
				
				inputDialog.open();
				
				return inputDialog.getValue();
			}
		});
		
		Generator generator = new Generator(definitions);
		
		generator.generate(definitionsFile, new File(filePath));
		
		afterAdd();
		
		if(MessageDialog.openQuestion(shell, "", 
			"Eclipse has to be restarted for the changes to take effect.\n\n" +
			"Do you want to restart Eclipse?"))
			PlatformUI.getWorkbench().restart();
	}
	
	protected abstract void afterAdd();
}
