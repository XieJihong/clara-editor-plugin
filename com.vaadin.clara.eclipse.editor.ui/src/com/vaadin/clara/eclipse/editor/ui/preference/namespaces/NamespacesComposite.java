package com.vaadin.clara.eclipse.editor.ui.preference.namespaces;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.model.Namespace;
import com.vaadin.clara.eclipse.editor.ui.DefinitionsLoader;
import com.vaadin.clara.eclipse.editor.ui.preference.actions.AddNamespace;

public class NamespacesComposite extends Composite{

	private DefinitionsLoader definitionsLoader;
	private TableViewer tableViewer;

	public NamespacesComposite(Composite parent, DefinitionsLoader definitionsLoader) {
		super(parent, SWT.NONE);
		
		this.definitionsLoader = definitionsLoader;
		
		setLayout(new GridLayout());
		
		final Table table = new Table(this, SWT.BORDER|SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		table.setLayoutData( GridDataFactory.fillDefaults().hint(500, -1).grab(false, true).create());
		
		tableViewer = new TableViewer(table);
		
		tableViewer.setContentProvider( new ArrayContentProvider());
		
		new NamespaceColumn(tableViewer, 400, "Url") {
			@Override
			protected String getColumnText(Namespace namespace) {
				return namespace.getUri();
			}
		};
		
		new NamespaceColumn(tableViewer, 100, "Prefix") {
			@Override
			protected String getColumnText(Namespace namespace) {
				return namespace.getPrefix();
			}
		};
		
		
		final Composite buttonComposite = new Composite(parent, 0);
		buttonComposite.setLayout( new RowLayout(SWT.HORIZONTAL));
		
		
		final Button addButton = new Button(buttonComposite, 0);
		addButton.setText("Add Namespace From Jar File");
		addButton.addSelectionListener( new AddNamespace( definitionsLoader, getShell()) {
			@Override
			protected void afterAdd() {
				updateNamespaceViewer();			
			}
		});
		
		updateNamespaceViewer();
	}
	
	private void updateNamespaceViewer()
	{
		final ComponentDefinitions definitions = definitionsLoader.loadDefinitions();
		tableViewer.setInput( definitions.getNamespaces());
	}
}
