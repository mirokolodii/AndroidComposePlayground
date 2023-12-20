package com.unagit.composeplayground.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composer
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.unagit.composeplayground.compose.ComposablePreviewInvocationHandler.NoParameter
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.isAccessible

/**
 * We cannot directly invoke Composable methods, obtained via Java reflection,
 * because last parameter of each such method should be [Composer] interface instance
 * (normally added by the compose compiler plugin).
 * So each Composable method is transformed to [ComposablePreview],
 * which then can be is invoked with help of Java's Dynamic [Proxy].
 */
interface ComposablePreview {
    /**
     * Package name of declaring class
     */
    val packageName: String

    @Composable
    operator fun invoke()
}

/**
 * Transforms list of [Method] into list of [ComposablePreview],
 * handling also case, when Preview Composable has parameters, annotated with [PreviewParameter].
 */
fun List<Method>.toComposablePreviews(): List<ComposablePreview> {
    val result = mutableListOf<ComposablePreview>()
    forEach { method ->
        val previewParameterAnnotation = method.findPreviewParameterAnnotation()

        // The [Preview] doesn't have a parameter annotated with [PreviewParameter],
        // create a [ComposablePreview] with no parameters
        if (previewParameterAnnotation == null) {
            result.add(method.toComposablePreview())
        } else {
            // Create a [ComposablePreview] for each parameter name and value pair
            previewParameterAnnotation.getParameters().forEach { (name, value) ->
                result.add(method.toComposablePreview(nameSuffix = name, parameter = value))
            }
        }
    }
    return result.toList()
}

/**
 * Finds the [PreviewParameter] annotation on the method's parameters.
 */
private fun Method.findPreviewParameterAnnotation(): PreviewParameter? {
    return this.parameterAnnotations
        .flatMap { it.toList() }
        .find { it is PreviewParameter } as? PreviewParameter
}

/**
 * Creates a [ComposablePreview] from a [Method].
 * Because the method is a [Composable] function that has extra parameters added by the compiler
 * ([Composer] being one of them), we can't call it directly. Instead, we rely on
 * Java dynamic proxy to handle the call using a [ComposablePreviewInvocationHandler] instance.
 */
fun Method.toComposablePreview(
    nameSuffix: String = "",
    parameter: Any? = NoParameter,
): ComposablePreview {
    val proxy = Proxy.newProxyInstance(
        ComposablePreview::class.java.classLoader,
        arrayOf(ComposablePreview::class.java),
        ComposablePreviewInvocationHandler(composableMethod = this, parameter = parameter),
    ) as ComposablePreview

    // Wrap the call to the proxy in an object so that we can override the toString method
    // to provide a more descriptive name for the test and resulting snapshot filename.
    return object : ComposablePreview by proxy {
        override val packageName: String = declaringClass.packageName

        override fun toString(): String {
            return buildList<String> {
                add(name)
                if (nameSuffix.isNotEmpty()) add(nameSuffix)
            }.joinToString(NAME_SEPARATOR)
        }
    }
}

private fun PreviewParameter.getParameters(): List<Pair<String, Any?>> {
    // Create an instance of the PreviewParameterProvider
    val provider = this@getParameters.provider.createInstanceUnsafe()

    return provider.values.mapIndexed { index, value ->
        index.toString() to value
    }.toList()
}

/**
 * Creates an instance of the [PreviewParameterProvider] using the no-args constructor
 * even if the class or constructor is private.
 */
private fun KClass<out PreviewParameterProvider<*>>.createInstanceUnsafe(): PreviewParameterProvider<*> {
    val noArgsConstructor = constructors
        .single { it.parameters.all(KParameter::isOptional) }
    noArgsConstructor.isAccessible = true
    return noArgsConstructor.call()
}

/**
 * Used to handle calls to a [composableMethod].
 * If a [parameter] is provided, it will be used as the first parameter of the call.
 */
class ComposablePreviewInvocationHandler(
    private val composableMethod: Method,
    private val parameter: Any?,
) : InvocationHandler {

    /**
     * Used to indicate that no parameter should be used when calling the [composableMethod].
     * We can't use null here as we might want to pass null as an actual parameter to a function.
     */
    object NoParameter

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        val safeArgs = args ?: emptyArray()
        val safeArgsWithParam = if (parameter != NoParameter) {
            arrayOf(parameter, *safeArgs)
        } else {
            safeArgs
        }
        return composableMethod.invoke(null, *safeArgsWithParam)
    }
}

private const val NAME_SEPARATOR = "_"
