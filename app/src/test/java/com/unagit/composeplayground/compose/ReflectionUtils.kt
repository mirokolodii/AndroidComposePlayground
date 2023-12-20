package com.unagit.composeplayground.compose

import io.github.classgraph.AnnotationInfo
import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import io.github.classgraph.MethodInfo
import io.github.classgraph.MethodInfoList
import io.github.classgraph.ScanResult
import java.lang.reflect.Method
import java.lang.reflect.Modifier

/**
 * Returns list of [Method], directly annotated with any of [annotationClasses].
 * @param annotationClasses - result will include all methods, annotated by any of annotation classes,
 * @param packageNames - scan specific packages and their sub-packages,
 * @param includePrivate - flag indicates, whether or not private methods should be included in scan result.
 */
fun getMethodsWithAnnotations(
    annotationClasses: List<Class<out Annotation>>,
    packageNames: Array<String>,
    includePrivate: Boolean,
): List<Method> = buildList {
    val scanResult = getScanResult(packageNames, includePrivate)

    annotationClasses.forEach { annotationClass ->
        scanResult.getClassesWithMethodAnnotation(annotationClass).forEach { classInfo ->
            classInfo.declaredMethodInfo
                .filterHavingAnnotation(annotationClass)
                .forEach { methodInfo ->
                    val method = methodInfo.loadClassAndGetMethod()
                    // If method is private, need to make it accessible to be able to invoke it later
                    if (Modifier.isPrivate(method.modifiers)) method.isAccessible = true
                    this@buildList.add(method)
                }
        }
    }

    scanResult.close()
}

/**
 * Scans whole codebase and returns scan results.
 * To be able to scan classes with method annotation, we need to enable:
 * - [ClassInfo]
 * - [MethodInfo]
 * - [AnnotationInfo]
 */
private fun getScanResult(packageNames: Array<String>, includePrivateMethods: Boolean): ScanResult {
    return ClassGraph()
        .acceptPackages(*packageNames)
        .enableClassInfo()
        .enableMethodInfo()
        .enableAnnotationInfo()
        .apply { if (includePrivateMethods) ignoreMethodVisibility() }
        .scan()
}

/**
 * Filters out methods, that are not directly annotated with [annotationClass].
 */
private fun MethodInfoList.filterHavingAnnotation(annotationClass: Class<out Annotation>): MethodInfoList {
    return filter { methodInfo ->
        methodInfo.hasAnnotation(annotationClass) && !methodInfo.hasAnnotationClassAnnotatedWith(annotationClass)
    }
}

/**
 * [MethodInfo.annotationInfo] includes whole annotation hierarchy (not only direct annotations).
 * This method checks, whether any of the method's annotation classes are also annotated with [annotationClass].
 * If true, it means that method is not annotated directly with [annotationClass], but it is annotated
 * with some annotation that is annotated with [annotationClass].
 *
 * Example:
 * @Preview(locale = "en")
 * @Preview(locale = "pl")
 * annotation class MultiLanPreview
 *
 *@MultiLanPreview
 * fun SomeViewPreviews() { ... }
 *
 * In above example, [MethodInfo.annotationInfo] of 'SomeViewPreviews' includes both annotations - 'Preview' and 'MultiLanPreview'.
 * Since this method is not directly annotated by 'Preview', we check annotations of each annotation class, which we can use
 * to filter the final result.
 */
private fun MethodInfo.hasAnnotationClassAnnotatedWith(annotationClass: Class<out Annotation>): Boolean {
    return this.annotationInfo.any { annotationInfo ->
        annotationInfo.classInfo.annotationInfo.directOnly().any { annotationClassAnnotationInfo ->
            annotationClassAnnotationInfo.name == annotationClass.name
        }
    }
}
