package com.vaadin.clara.eclipse.editor.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * See the class documentation of {@link ComponentDefinitions}...
 */
@XmlRootElement
public class Namespace implements Comparable<Namespace>{

	public static final String PREFIX_DEFAULT = "";
	public static final String PREFIX_PARENT = "p";
	
	private String prefix;
	private String uri;
	
	public Namespace() {
	}	
	
	public Namespace(String prefix, String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}
	@XmlAttribute
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@XmlAttribute
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Namespace other = (Namespace) obj;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Namespace [prefix=" + prefix + ", url=" + uri + "]";
	}

	@Override
	public int compareTo(Namespace other) {
		return prefix.compareTo( other.prefix);
	}
	
	public String getXmlns()
	{
		return String.format("xmlns:%s=\"%s\"", getPrefix(), getUri()).replaceAll(":=", "=");
	}
}
