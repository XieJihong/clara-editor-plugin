package com.vaadin.clara.eclipse.editor.generate;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.Attribute;
import com.vaadin.clara.eclipse.editor.model.AbstractAttribute.ChildAttribute;
import com.vaadin.clara.eclipse.editor.model.Component;
import com.vaadin.clara.eclipse.editor.model.ComponentDefinitions;
import com.vaadin.clara.eclipse.editor.model.Namespace;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSingleComponentContainer;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;

public class PoroposalModelCreator {

	private final Collection<Class<?>> componentClasses;
	private HashMap<String, Namespace> namespaceMap;
	private ComponentDefinitions definitions;
	
	public PoroposalModelCreator(Collection<Class<?>> componentClasses, ComponentDefinitions existingDefinitions) 
	{
		this.componentClasses = componentClasses;
		definitions = existingDefinitions == null ? new ComponentDefinitions() : existingDefinitions;
	}


	public ComponentDefinitions getComponentDefinitions()
	{
		final Namespace parentNamespace		= new Namespace(Namespace.PREFIX_PARENT, "urn:vaadin:parent");
		final Namespace tabSheetNamespace	= new Namespace("t", "urn:vaadin:tabsheet");
		
		definitions.getNamespaces().addAll( Arrays.asList( parentNamespace, tabSheetNamespace));
		
		
		namespaceMap = new HashMap<String, Namespace>();
		
		for(Class<?> componentClass: componentClasses)
		{
			final String javaPackage = componentClass.getPackage().getName();
			
			final Namespace namespace = 
					definitions.getOrCreateImportNamespace( javaPackage);
			
			namespaceMap.put(javaPackage, namespace);
		}
		
		
		for(Class<?> componentClass: componentClasses)
		{
			final Component component = new Component();
			component.setName( componentClass.getSimpleName());
			
			if(AbstractSingleComponentContainer.class.isAssignableFrom(componentClass))
				component.setChildCount(1);
			else if(AbstractSplitPanel.class.isAssignableFrom(componentClass))
				component.setChildCount(2);
			else if(AbstractComponentContainer.class.isAssignableFrom(componentClass))
				component.setChildCount(Component.INFINITE_CHILD_COUNT);
			else
				component.setChildCount(0);
			
			
			
			final String javaPackage = componentClass.getPackage().getName();
			
			component.setNamespacePrefix( 
					namespaceMap.get( javaPackage).getPrefix());

			
			
			definitions.getComponents().add(component);
			
			final List<Method> sortedMethods = new ArrayList<Method>( Arrays.asList( componentClass.getMethods()));
			
			Collections.sort( sortedMethods, new Comparator<Method>() {
				public int compare(Method m1, Method m2) { return m1.getName().compareTo( m2.getName()); }
			});
			
			for(Method method: sortedMethods)
			{
				if(!method.getName().startsWith("set"))
					continue;
				
				if(method.getParameterTypes().length > 1)
					continue;
				
				if(!ReflectionUtil.checkParameterType( method))
					continue;
				
				final String attributeName = getAttributeName(method);
				
				final Class<?> attributeType = (method.getParameterTypes().length == 1) ? method.getParameterTypes()[0] : Void.class;
				
				
				final Attribute attribute = new Attribute();
				attribute.setName(attributeName);
				attribute.setTargetTypeInfo( attributeType.getSimpleName());
				
				if(boolean.class.equals(attributeType) || Boolean.class.equals( attributeType))
				{
					attribute.getValues().addAll( Arrays.asList( "true", "false"));
				}
				if(attributeType.isEnum())
				{
					final List<String> enumConstants = new ArrayList<String>();
					
					for(Field field: attributeType.getDeclaredFields())
					{
						if(field.isEnumConstant())
							enumConstants.add( field.getName());
					}
					
					attribute.getValues().addAll( enumConstants);
				}
				
				
				component.getAttributes().add(attribute);
			}
			
			if(AbstractOrderedLayout.class.isAssignableFrom(componentClass) || GridLayout.class.isAssignableFrom(componentClass))
			{
				final ChildAttribute componentAlignementAttribute = new ChildAttribute();
				componentAlignementAttribute.setName("componentAlignment");
				componentAlignementAttribute.setNamespacePrefix( parentNamespace.getPrefix());
				componentAlignementAttribute.setTargetTypeInfo( Alignment.class.getSimpleName());
				componentAlignementAttribute.setValues( getAlignementValues());
				
				component.getChildAttributes().add( componentAlignementAttribute);
				
				
				final ChildAttribute expandRationAttribute = new ChildAttribute();
				expandRationAttribute.setName("expandRatio");
				expandRationAttribute.setNamespacePrefix( parentNamespace.getPrefix());
				expandRationAttribute.setTargetTypeInfo( float.class.getSimpleName());
				
				component.getChildAttributes().add( expandRationAttribute);
			}
			
			if(AbstractSelect.class.isAssignableFrom(componentClass))
			{
				final Attribute attribute = new Attribute();
				attribute.setName("itemCaptionPropertyId");
				attribute.setTargetTypeInfo("Object");
				
				component.getAttributes().add(attribute);
			}
			if(TabSheet.class.isAssignableFrom(componentClass))
			{
				final ChildAttribute tabCaptionAttribute = new ChildAttribute();
				tabCaptionAttribute.setName("tabCaption");
				tabCaptionAttribute.setNamespacePrefix( tabSheetNamespace.getPrefix());
				tabCaptionAttribute.setTargetTypeInfo( String.class.getSimpleName());
				tabCaptionAttribute.setAdditionalInfo("... this doesn't work with Clara 1.0.0");
				
				component.getChildAttributes().add( tabCaptionAttribute);
			}
		}
		
		return definitions;
	}
	
	private static List<String> getAlignementValues()
	{
		final List<String> alignementValues = new ArrayList<String>();
		
		for(Field field: Alignment.class.getDeclaredFields())
			if(Modifier.isStatic(field.getModifiers()) && Modifier.isFinal( field.getModifiers()) && Alignment.class.isAssignableFrom( field.getType()))
				alignementValues.add(field.getName());
		
		return alignementValues;
	}
	
	private static String getAttributeName(Method method)
	{
		final String methodName = method.getName();
		final String attributeName = 
				methodName
				.replaceFirst("set", "")
				.replaceFirst(".", String.valueOf( methodName.charAt( "set".length())).toLowerCase());
		
		return attributeName;
	}
}
