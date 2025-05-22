package com.findy.processor;

import java.util.Set;
import java.util.regex.Pattern;
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
public class EnumNamePatternProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(EnumNamePattern.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(EnumNamePattern.class)) {
            // 1. 해당 Element가 enum인지 확인
            if (element.getKind() != ElementKind.ENUM) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "@EnumNameRegex 는 enum 타입에만 적용할 수 있습니다.",
                        element
                );
                continue;
            }

            // 2. 애너테이션 value 값 가져오기
            EnumNamePattern annotation = element.getAnnotation(EnumNamePattern.class);
            String regex = annotation.value();

            // 3. enum 상수들 검증
            for (Element enclosed : element.getEnclosedElements()) {
                if (enclosed.getKind() == ElementKind.ENUM_CONSTANT) {
                    String enumName = enclosed.getSimpleName().toString();

                    if (!Pattern.matches(regex, enumName)) {
                        processingEnv.getMessager().printMessage(
                                Diagnostic.Kind.ERROR,
                                "Enum 값 '" + enumName + "'이 지정된 패턴과 일치하지 않습니다: " + regex,
                                enclosed
                        );
                    }
                }
            }
        }
        return true;
    }
}