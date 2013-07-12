package com.vaadin.clara.eclipse.editor.ui.proposals.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class ProposalInfo {

	private final LinkedHashMap<String, String> infos = new LinkedHashMap<String, String>();
	
	public void addRow(String label, String info)
	{
		infos.put(label, info);
	}
	
	public void clear()
	{
		infos.clear();
	}
	
	@Override
	public String toString() 
	{
		final StringBuilder html = new StringBuilder();
		
		for(Entry<String, String> row: infos.entrySet())
			html.append( String.format("<b>%s</b>&nbsp;&nbsp;<i>%s</i><br>", row.getKey(), row.getValue()));
		
		return html.toString();
	}
}
