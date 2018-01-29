package my.myname.formaters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.format.AnnotationFormatterFactory;

import org.springframework.format.Parser;
import org.springframework.format.Printer;


import my.myname.anotations.AnotationFormater;


/**
 * The Class TestAnotationFormatter.
 * TestAnotationFormatter is AnnotationFormatterFactory implementation for replacing {@link Character} in {@link String} fields
 */
public class TestAnotationFormatter implements AnnotationFormatterFactory<AnotationFormater>{

	/* (non-Javadoc)
	 * @see org.springframework.format.AnnotationFormatterFactory#getFieldTypes()
	 */
	@Override
	public Set<Class<?>> getFieldTypes() {
		 Set<Class<?>> fieldTypes = new HashSet<>(1, 1);
	     fieldTypes.add(String.class);//return type
		return fieldTypes;
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.AnnotationFormatterFactory#getPrinter(java.lang.annotation.Annotation, java.lang.Class)
	 */
	@Override
	public Printer<?> getPrinter(AnotationFormater annotation, Class<?> fieldType) {
		//TODO AOP LOG
		FormaterForAnotation formater = new FormaterForAnotation();
		formater.setNewChar(annotation.newChar());
		formater.setOldChar(annotation.oldChar());
		return formater;
	}

	/* (non-Javadoc)
	 * @see org.springframework.format.AnnotationFormatterFactory#getParser(java.lang.annotation.Annotation, java.lang.Class)
	 */
	@Override
	public Parser<?> getParser(AnotationFormater annotation, Class<?> fieldType) {
		//TODO AOP LOG
		FormaterForAnotation formater = new FormaterForAnotation();
		formater.setNewChar(annotation.newChar());
		formater.setOldChar(annotation.oldChar());
		return formater;
	}




}
