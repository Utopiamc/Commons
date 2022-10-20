package de.utopiamc.commons.utils;

import de.utopiamc.commons.validate.Validator;
import lombok.experimental.UtilityClass;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.HashSet;
import java.util.Set;

@UtilityClass
public class AnnotationUtil {

    public static boolean isAnnotationPresent(AnnotatedElement element, Class<? extends Annotation> annotation) {
        return getAnnotation(element, annotation) != null;
    }

    public static <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationType) {
        return getAnnotation(element, annotationType, new HashSet<>());
    }

    public static Set<Annotation> getAnnotations(AnnotatedElement element) {
        return getAnnotations(element, new HashSet<>());
    }

    private static Set<Annotation> getAnnotations(AnnotatedElement element, Set<Annotation> scannedAnnotations) {
        Validator.requireNonNull(element, "Element should not be null!");

        if (element instanceof Annotation) {
            scannedAnnotations.add((Annotation) element);
        }

        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            if (isInLavaLangAnnotationPackage(annotation))
                continue;

            scannedAnnotations.add(annotation);

            getAnnotations(annotation.annotationType(), scannedAnnotations);
        }

        if (element instanceof Class) {
            Class<?> cls = (Class<?>) element;

            for (Class<?> anInterface : cls.getInterfaces())
                getAnnotations(anInterface, scannedAnnotations);

            Class<?> superclass = cls.getSuperclass();
            if (superclass != null)
                getAnnotations(superclass, scannedAnnotations);
        }

        return scannedAnnotations;
    }

    public static <A extends Annotation> Set<Annotation> getAnnotationsWithAnnotation(AnnotatedElement element, Class<A> annotationType) {
        Set<Annotation> annotations = new HashSet<>();

        Annotation[] presentAnnotations = element.getAnnotations();
        for (Annotation annotation : presentAnnotations) {
            if (isInLavaLangAnnotationPackage(annotation))
                continue;

            if (isAnnotationPresent(annotation.annotationType(), annotationType))
                annotations.add(annotation);
        }

        return annotations;
    }

    @SuppressWarnings("unchecked")
    private static <A extends Annotation> A getAnnotation(AnnotatedElement element, Class<A> annotationType, Set<Annotation> visited) {
        Annotation[] annotations = element.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationType))
                return (A) annotation;
        }
        for (Annotation annotation : annotations) {
            if (!isInLavaLangAnnotationPackage(annotation) && visited.add(annotation)) {
                A parentedAnnotation = getAnnotation(annotation.annotationType(), annotationType, visited);
                if (parentedAnnotation != null)
                    return parentedAnnotation;
            }
        }

        if (Class.class.isAssignableFrom(element.getClass())) {
            Class<?> clazz = (Class<?>) element;

            for (Class<?> anInterface : clazz.getInterfaces()) {
                A parentedAnnotation = getAnnotation(anInterface, annotationType, visited);
                if (parentedAnnotation != null)
                    return parentedAnnotation;
            }

            Class<?> superclass = clazz.getSuperclass();
            if (superclass == null || Object.class == superclass) {
                return null;
            }
            return getAnnotation(superclass, annotationType, visited);
        }
        return null;
    }

    private static boolean isInLavaLangAnnotationPackage(Annotation annotation) {
        return annotation.annotationType().getPackage().getName().startsWith("java.lang.annotation");
    }

}
