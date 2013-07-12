package com.vaadin.clara.eclipse.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.Attribute;
import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.ChildAttribute;

/**
 * See the class documentation of {@link ComponentDefinitions}...
 */
@XmlRootElement
public class Component implements Comparable<Component>{

	public static final int INFINITE_CHILD_COUNT = -1;
	private String name;
	private String namespacePrefix = "";
	private List<Attribute> attributes 		= new ArrayList<Attribute>();
	private List<ChildAttribute> childAttributes = new ArrayList<ChildAttribute>();
	private int childCount;


	public Component() {
	}
	
	public Component(Attribute...attributes) {
		this();
		getAttributes().addAll( Arrays.asList( attributes));
	}
	
	
	@XmlElementRef(type=Attribute.class)
	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	@XmlElementRef(type=ChildAttribute.class)
	public List<ChildAttribute> getChildAttributes() {
		return childAttributes;
	}

	public void setChildAttributes(List<ChildAttribute> childAttributes) {
		this.childAttributes = childAttributes;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public int getChildCount() {
		return childCount;
	}

	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
	
	@XmlAttribute
	public String getNamespacePrefix() {
		return namespacePrefix;
	}

	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}
	
	public String getNameWithNamespacePrefix()
	{
		if(getNamespacePrefix().isEmpty())
			return getName();
		else
			return getNamespacePrefix() + ":" + getName();
	}
	
	public Attribute getAttribute(String attributeName)
	{
		return (Attribute) getAttribute(attributeName, getAttributes());
	}
	
	public ChildAttribute getChildAttribute(String attributeName)
	{
		return (ChildAttribute) getAttribute(attributeName, getChildAttributes());
	}
	
	public Collection<Attribute> findAttributeMatches(final String matchString, Collection<String> permittedNamespacePrefixes)
	{
		final List<Attribute> filtered = new ArrayList<Attribute>();
		
		for(Attribute attribute: getAttributes())
			if(attribute.getName().toLowerCase().startsWith( matchString.toLowerCase()))
				if(permittedNamespacePrefixes.contains( attribute.getNamespacePrefix()))
					filtered.add(attribute);
		
		return filtered;
	}
	
	public Collection<ChildAttribute> findChildAttributeMatches(final String matchString, Collection<String> permittedNamespacePrefixes)
	{
		final List<ChildAttribute> filtered = new ArrayList<ChildAttribute>();

		for (ChildAttribute attribute : getChildAttributes())
			if (attribute.getName().toLowerCase().startsWith( matchString.toLowerCase()))
				if(permittedNamespacePrefixes.contains( attribute.getNamespacePrefix()))
					filtered.add(attribute);

		return filtered;
	}
	
	private AbstractAttribute getAttribute(String attributeName, List<? extends AbstractAttribute> attributes)
	{
		for(AbstractAttribute attribute: attributes)
			if(attribute.getNameWithNamespacePrefix().equals( attributeName))
				return attribute;
		
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((namespacePrefix == null) ? 0 : namespacePrefix.hashCode());
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
		Component other = (Component) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (namespacePrefix == null) {
			if (other.namespacePrefix != null)
				return false;
		} else if (!namespacePrefix.equals(other.namespacePrefix))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Component [name=" + name + "]";
	}

	@Override
	public int compareTo(Component o) {
		return getName().compareTo( o.getName());
	}
}
