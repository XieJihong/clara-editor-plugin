package com.vaadin.clara.eclipse.editor.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.Attribute;
import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.ChildAttribute;

/**
 * This is root element class of the <b>Vaadin</b> "Completion Proposal Model".
 * <p></p>
 * The informations from this model will be used to generate the completion
 * proposals for...
 * <ul>
 *  <li>XML elements</li>
 *  <li>XML attributes</li>
 *  <li>XML attribute values</li>
 * </ul>
 * ... in the XML editor.
 * <p></p>
 * <h1>What do we have?</h1>
 * <ul>
 * 	<li>We have {@link Component}s:
 *   <ul>
 *    <li>They represent a <b>Vaadin</b> UI component class. </li>
 *    <li>And they represent a XML Element in the <b>Clara</b> mark-up (XML) file. </li>
 *    <li>A {@link Component} has an element name (which is the class name of the <b>Vaadin</b> UI component class).</li>
 *    <li>A {@link Component} also {@link Component#getChildCount() determines} how many child elements/components it is allowed to contain.
 *      <ul>
 *        <li>That is because in <b>Vaadin</b> for example:
 *          <ul>
 *            <li> a <b>VerticalSplittPanel</b> can have 2 components ...</li>
 *            <li> whereas a <b>VerticalLayout</b> can have an infinite number of children.</li>
 *          </ul>
 *        </li>
 *      </ul>
 *      <i>(So we will show no completion proposals in the Editor for child XML elements if the allowed number of child elements has already been reached.)</i>
 *    </li>
 *   </ul>
 *  </li>
 *  <li>{@link Component}s also determine which {@link Attribute}s they contain:
 *    <ul>
 *      <li>An attribute represents a property (or setter) of the <b>Vaadin</b> UI component class.</li>
 *      <li>And an attribute represents an XML element attribute in the <b>Clara</b> XML file.</li>
 *      <li>The attribute name is usually equivalent to the property name of the <b>Vaadin</b> UI component class.</li>
 *      <li>An attribute gives some additional informations which can be displayed in the auto-completion pop-up for the XML attribute
 *      (like e.g. the {@link AbstractAttribute#getTargetTypeInfo() target type} of the property).</li>
 *      <li>A attribute determines a list of values (Strings) that the XML attribute in the <b>Clara</b> file can have 
 *      (<i>Thus we can generate XML attribute value proposals for e.g. <code>boolean</code> and 
 *      <code>enumeration</code> values.</i>):</li>
 *      <li>A special form of attribute is the {@link ChildAttribute}:
 *        <ul>
 *          <li>TODO</li> documentation!
 *        </ul>
 *      </li>
 *    </ul>
 *  </li>
 *  <li> {@link Component}s and {@link Attribute}s can have different name spaces.
 *    <ul>
 *      <li>We can register several {@link Namespace}s and add the name space prefix to the
 *      desired {@link Attribute} or {@link Component}.</li>
 *      <li>For more information take a look at the examples and the documentation of the <b>Clara Vaadin Addon</b> project.
 *      But basically we can say that currently:
 *        <ul>
 *          <li> {@link Component}s can get a name space in order to determine 
 *          the java package of the associated  <b>Vaadin</b> UI component class ...<br>
 *          <i>(This is necessary in order to use <b>Add-On</b> components in the <b>Clara</b> XML, which 
 *          are located in a different package as the default <b>Vaadin</b> components)</i>
 *          </li>
 *          <li> ... and some {@link Attribute}s get name spaces to indicate and realize 
 *          a special handling during the mark-up parsing process.</li>
 *        </ul>
 *      </li>
 *    </ul>
 *  </li>
 * </ul>
 * This model can be serialized to XML and loaded from XML (via {@link JAXB}). 
 *
 */
@XmlRootElement(name="definitions")
public class ComponentDefinitions {

	private Set<Component> components = new TreeSet<Component>();
	private Set<Namespace> namespaces = new TreeSet<Namespace>();
	
	@XmlTransient
	private AddonNamespacePrefixStrategy namespacePrefixStrategy = new AddonNamespacePrefixStrategy();

	public ComponentDefinitions() {
	}
	
	public ComponentDefinitions(Component...components) {
		this();
		getComponents().addAll( Arrays.asList( components));
	}
	
	public ComponentDefinitions setNamespacePrefixStrategy( AddonNamespacePrefixStrategy namespacePrefixStrategy) 
	{
		this.namespacePrefixStrategy = namespacePrefixStrategy; return this;
	}
	
	@XmlElementRef(type=Component.class)
	public Set<Component> getComponents() {
		return components;
	}

	public void setComponents(Set<Component> components) {
		this.components = components;
	}
	
	@XmlElementRef(type=Namespace.class)
	public Set<Namespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(Set<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	public Component getComponent(String componentName)
	{
		for(Component component: getComponents())
			if(component.getNameWithNamespacePrefix().equals( componentName))
				return component;
		
		return null;
	}
	
	public List<Component> findMatches(String matchString, Collection<String> permittedNamespacePrefixes)
	{
		final List<Component> matches = new ArrayList<Component>();
		
		for(Component component: getComponents())
			if(component.getName().toLowerCase().startsWith( matchString.toLowerCase()))
				if(permittedNamespacePrefixes.contains( component.getNamespacePrefix()))
					matches.add( component);
		
		return matches;
	}
	
	
	public String getDefaultXmlnsString()
	{
		final StringBuilder namespaceString = new StringBuilder();
		
		final List<String> defaultNamespacePrefixes = 
				Arrays.asList(Namespace.PREFIX_DEFAULT, Namespace.PREFIX_PARENT);
		
		for(Namespace namespace: getNamespaces())
		{
			if(!defaultNamespacePrefixes.contains( namespace.getPrefix()))
				continue;
			
			namespaceString.append( "xmlns");
			
			if(!namespace.getPrefix().isEmpty())
				namespaceString.append(":").append( namespace.getPrefix());
			
			namespaceString.append( "=\"").append(namespace.getUri()).append("\" ");
		}
		
		return namespaceString.toString().replaceAll(" $", "");
	}
	

	public Namespace getOrCreateImportNamespace(String javaPackage)
	{
		final String uri = "urn:import:" + javaPackage;
		
		for(Namespace namespace: getNamespaces())
			if(namespace.getUri().equals( uri))
				return namespace;
		
		final Namespace newImportNamespace = new Namespace();
		
		if(javaPackage.equals("com.vaadin.ui"))
		{
			newImportNamespace.setPrefix( Namespace.PREFIX_DEFAULT);
		}
		else
		{
			final String prefix = namespacePrefixStrategy.getPrefix(javaPackage, getNamespaces());
			
			newImportNamespace.setPrefix(prefix);
		}
		
		newImportNamespace.setUri(uri);
		
		getNamespaces().add(newImportNamespace);
		
		return newImportNamespace;
	}
	
	
	
	public static ComponentDefinitions load(InputStream inputStream)
	{
		try 
		{
			return JAXB.unmarshal(inputStream, ComponentDefinitions.class);
		} 
		finally
		{
			try { inputStream.close(); } catch (Exception e) {}
		}
	}
}
