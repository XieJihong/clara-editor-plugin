package com.vaadin.clara.eclipse.editor.ui.preference.actions;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.FileStoreEditorInput;

public class OpenDefintionsFile extends SelectionAdapter{

	private final File definitionsFile;
	private final Shell shell;

	public OpenDefintionsFile(File definitionsFile, Shell shell) {
		this.definitionsFile = definitionsFile;
		this.shell = shell;
	}

	@Override
	public void widgetSelected(SelectionEvent event) 
	{
		final IFileStore fileStore 				= 
				EFS.getLocalFileSystem().getStore( new Path( definitionsFile.getAbsolutePath()));
		final FileStoreEditorInput editorInput 	= new FileStoreEditorInput(fileStore);
		
		try 
		{
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor( 
					editorInput, "org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		shell.close();
	}
}
