package com.vaadin.clara.eclipse.editor.ui.preference;

import java.io.File;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.vaadin.clara.eclipse.editor.ui.DefinitionsLoader;
import com.vaadin.clara.eclipse.editor.ui.preference.actions.OpenDefintionsFile;
import com.vaadin.clara.eclipse.editor.ui.preference.actions.RestoreDefinitionsFile;
import com.vaadin.clara.eclipse.editor.ui.preference.namespaces.NamespacesComposite;

public class ClaraPreferencePage extends PreferencePage implements IWorkbenchPreferencePage{

	@Override
	protected Control createContents(Composite parent) 
	{
		// The preference page composite:
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout( new GridLayout());
		
		// The definitions:
		final DefinitionsLoader definitionsLoader = new DefinitionsLoader();
		final File definitionsFile 	= definitionsLoader.getXmlFile();
		
		
		// "Open Definitions File"
		final Link openFileLink = new Link(composite, SWT.NONE);
		openFileLink.setText(
				"Here you can <a>open</a> the \"Component Proposal Model\" file in the Editor.");
		openFileLink.addSelectionListener( new OpenDefintionsFile(definitionsFile, getShell()));
		spacer(composite);
		
		
		
		// "Restore Definitions File"
		final Link resetFileLink = new Link(composite, SWT.NONE);
		resetFileLink.setText(
				"And here you can <a>restore</a> the original \"Component Proposal Model\" file, \n" +
				"if you want to reset your modifications to the XML file (Eclipse will be restarted).");
		resetFileLink.addSelectionListener( new RestoreDefinitionsFile(definitionsFile, getShell()));
		spacer(composite);
		
		
		// The definitions file path:
		new Label(composite, SWT.NONE).setText("The location of the \"Component Proposal Model\" file:");
		final Text locationText = new Text(composite, SWT.NONE);
		locationText.setText( definitionsFile.getAbsolutePath());
		locationText.setEditable(false);
		spacer(composite);
		
		
		// "Add Add-On Namespaces"
		final NamespacesComposite namespacesComposite = 
				new NamespacesComposite( composite, definitionsLoader); 
		namespacesComposite.setLayoutData( GridDataFactory.fillDefaults().grab(false, true).create());
		
		new Label(composite, SWT.NONE).setText("(Currently this works only with single jar file addons, that don't have external dependencies)");
		
		
		return composite;
	}
	
	private void spacer(Composite parent)
	{
		new Label(parent, SWT.NONE);
	}
	
	@Override
	public void init(IWorkbench workbench) {}
}
