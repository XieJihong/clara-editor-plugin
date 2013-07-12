package com.vaadin.clara.eclipse.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.Attribute;
import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.ChildAttribute;

/**
 * See the class documentation of {@link ComponentDefinitions}...
 */
@XmlRootElement
@XmlSeeAlso({Attribute.class, ChildAttribute.class})
public abstract class AbstractAttribute {

	private String name;
	private String namespacePrefix = "";
	private List<String> values = new ArrayList<String>();
	private String targetTypeInfo = "";
	private String additionalInfo = "";

	public AbstractAttribute() {
	}
	
	public AbstractAttribute(String...values) {
		this();
		getValues().addAll( Arrays.asList( values));
	}
	
	@XmlElement(name="value")
	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getTargetTypeInfo() {
		return targetTypeInfo;
	}

	public void setTargetTypeInfo(String targetTypeInfo) {
		this.targetTypeInfo = targetTypeInfo;
	}

	@XmlAttribute
	public String getAdditionalInfo() {
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
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
	
	@Override
	public String toString() {
		return "Attribute [name=" + name + "]";
	}
	
	@XmlRootElement public static class Attribute extends AbstractAttribute {}
	@XmlRootElement public static class ChildAttribute extends AbstractAttribute {}
}
