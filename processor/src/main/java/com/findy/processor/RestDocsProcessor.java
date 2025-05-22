package com.findy.processor;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;

@AutoService(Processor.class)
public class RestDocsProcessor extends AbstractProcessor {

    private static final String RESTDOCS_ANNOTATION        = "com.findy.processor.RestDocs";
    private static final String RESTDOCS_IGNORE_ANNOTATION = "com.findy.processor.RestDocsIgnore";

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("*");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            if (element.getKind() == ElementKind.ENUM) {
                enumProcess(element);
            }

        }
        return true;
    }

    private void enumProcess(Element element) {
        String enumName = element.getSimpleName().toString();

        boolean hasRestDocs = element
                .getAnnotationMirrors()
                .stream()
                .anyMatch(annotation -> RESTDOCS_ANNOTATION.equals(annotation.getAnnotationType().toString()));

        boolean hasRestDocsIgnore = element
                .getAnnotationMirrors()
                .stream()
                .anyMatch(annotation -> RESTDOCS_IGNORE_ANNOTATION.equals(annotation.getAnnotationType().toString()));

        if (!hasRestDocs && !hasRestDocsIgnore) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Enum '" + enumName + "'에 '@RestDocs' 혹은 '@RestDocsIgnore' 어노테이션이 누락되었습니다.",
                    element
            );
        }

        if (hasRestDocsIgnore) {
            return;
        }

        for (Element enclosed : element.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.ENUM_CONSTANT) {
                boolean hasFieldRestdocs = enclosed
                        .getAnnotationMirrors()
                        .stream()
                        .anyMatch(annotation -> RESTDOCS_ANNOTATION.equals(annotation.getAnnotationType().toString()));

                if (!hasFieldRestdocs) {
                    processingEnv.getMessager().printMessage(
                            Diagnostic.Kind.ERROR,
                            "Enum 상수 '" + enclosed.getSimpleName() + "'에 @RestDocs 어노테이션이 누락되었습니다.",
                            enclosed
                    );
                }
            }
        }
    }

}