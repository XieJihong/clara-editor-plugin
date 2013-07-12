package com.vaadin.clara.eclipse.editor.ui.preference.namespaces;


import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;

import com.vaadin.clara.eclipse.editor.model.Namespace;

public abstract class NamespaceColumn {
	
	public NamespaceColumn(TableViewer tableViewer, int width, String columnHeader) 
	{
		final TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
		column.getColumn().setWidth(width);
		column.getColumn().setText(columnHeader);
		column.setLabelProvider( new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				final Namespace namespace = (Namespace) cell.getElement();
				cell.setText( getColumnText(namespace));
			}
		});
	}
	
	protected abstract String getColumnText(Namespace namespace);
	
}
