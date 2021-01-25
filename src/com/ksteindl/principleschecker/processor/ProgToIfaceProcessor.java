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
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

import com.ksteindl.principleschecker.annotation.ProgrammedToInterface;

@SupportedAnnotationTypes({"com.ksteindl.principleschecker.annotation.ProgrammedToInterface"})
@SupportedSourceVersion(SourceVersion.RELEASE_15)
public class ProgToIfaceProcessor extends AbstractProcessor{
	
	private Messager messager;
	private Elements elementUtils;
	
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
		this.elementUtils = processingEnv.getElementUtils();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ProgrammedToInterface.class);
		for (Element element : elements) {
			if (element.getKind().isClass()) {
				messager.printMessage(Kind.ERROR, "GOF1 - Programmed to Interface violated", element);
			}
		}	
		return true;
	}

}
