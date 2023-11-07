package com.unagit.composeplayground.ui

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.unagit.composeplayground.databinding.ViewCustomBinding
import com.unagit.composeplayground.ui.ViewState.LONG
import com.unagit.composeplayground.ui.ViewState.SHORT

class CustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val viewBinding = ViewCustomBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
    }

    fun init(viewState: ViewState) {
        when (viewState) {
            LONG -> viewBinding.button.isVisible = true
            SHORT -> viewBinding.button.isVisible = false
        }
    }

    private fun onClickAction() {}
}

enum class ViewState {
    LONG, SHORT
}

@Preview
@Composable
fun CustomViewPreview(
    @PreviewParameter(CustomViewPreviewParameterProvider::class) viewState: ViewState
) {
    AndroidView(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        factory = { context ->
            CustomView(context).apply {
                init(viewState)
            }
        },
    )
}

class CustomViewPreviewParameterProvider : PreviewParameterProvider<ViewState> {
    override val values: Sequence<ViewState> = ViewState.values().asSequence()


}