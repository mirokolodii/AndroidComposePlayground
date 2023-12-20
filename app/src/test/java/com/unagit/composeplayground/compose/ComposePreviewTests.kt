package com.unagit.composeplayground.compose

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

private const val BASE_PACKAGE_NAME = "com.unagit.composeplayground"

// If you have custom Compose Preview annotations defined in the project,
// you can include them here as well
private val previewAnnotations = listOf(
    Preview::class.java,
)

class ComposePreviewTests {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
    )

    @Test
    fun snapshot() {
        // Generate list of all Compose Previews in project
        generateComposePreviews(includePrivate = true).forEach { preview ->

            paparazzi.snapshot(name = preview.toString()) {
                // Wrap Composable in [CompositionLocalProvider], so that it is rendered in preview mode
                // @see https://github.com/cashapp/paparazzi#localinspectionmode
                CompositionLocalProvider(LocalInspectionMode provides true) {
                    preview()
                }
            }
        }
    }

    //
    private fun generateComposePreviews(includePrivate: Boolean): List<ComposablePreview> {
        return getMethodsWithAnnotations(
            annotationClasses = previewAnnotations,
            packageNames = arrayOf(BASE_PACKAGE_NAME),
            includePrivate = includePrivate
        ).toComposablePreviews()
    }
}