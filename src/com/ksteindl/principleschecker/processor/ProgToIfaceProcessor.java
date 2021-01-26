package com.ksteindl.principleschecker.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;

import com.ksteindl.principleschecker.annotation.ProgrammedToInterface;

@SupportedAnnotationTypes({"com.ksteindl.principleschecker.annotation.ProgrammedToInterface"})
@SupportedSourceVersion(SourceVersion.RELEASE_15)
public class ProgToIfaceProcessor extends AbstractProcessor{
	
	private Messager messager;
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		roundEnv.getElementsAnnotatedWith(ProgrammedToInterface.class)
				.stream()
				.filter(element -> element.getKind().isClass())
				.flatMap(element -> element.getEnclosedElements().stream())
				.filter(fieldELement -> fieldELement.getKind().isField())
				.filter(fieldELement -> !fieldELement.asType().getKind().isPrimitive())
				.filter(fieldELement -> !fieldELement.asType().toString().equals("java.lang.String"))
				.filter(fieldELement -> !fieldIsInterface(fieldELement))
				.forEach(fieldELement -> messager.printMessage(Kind.ERROR, "This field (" 
						+ fieldELement.asType().toString() + ") violates GOF1: "
						+ "\"programmed to interface\" principle. Use only primitive type or interface"
						, fieldELement));
		return true;
	}
	
	private boolean fieldIsInterface(Element fieldElement) {
		if (fieldElement.asType().getKind() != TypeKind.DECLARED) {
			return false;
		}
		DeclaredType declaredFieldType = (DeclaredType) fieldElement.asType();
		TypeElement fieldTypeElement = (TypeElement) declaredFieldType.asElement();
		return fieldTypeElement.getKind().isInterface();
	}


}
