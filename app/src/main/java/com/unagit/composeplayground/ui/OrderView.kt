package com.unagit.composeplayground.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.unagit.composeplayground.R
import com.unagit.composeplayground.color
import com.unagit.composeplayground.databinding.ViewOrderBinding
import com.unagit.composeplayground.dpToPx
import com.unagit.composeplayground.ui.Status.COMPLETE
import com.unagit.composeplayground.ui.Status.DRAFT
import com.unagit.composeplayground.ui.Status.IN_PROGRESS

class OrderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val viewBinding = ViewOrderBinding.inflate(LayoutInflater.from(context), this)

    init {
        setPadding(context.dpToPx(16))
    }

    fun init(status: Status) {
        initLabel(status)
        initButton(status)
    }

    private fun initLabel(status: Status) {
        val text: String
        @ColorRes val backgroundColorRes: Int
        @DrawableRes val iconRes: Int

        when (status) {
            DRAFT -> {
                text = "Draft"
                backgroundColorRes = R.color.draft
                iconRes = R.drawable.ic_line_circle
            }

            IN_PROGRESS -> {
                text = "In Progress"
                backgroundColorRes = R.color.in_progress
                iconRes = R.drawable.ic_arrow_circle_right
            }

            COMPLETE -> {
                text = "Complete"
                backgroundColorRes = R.color.complete
                iconRes = R.drawable.ic_check_circle
            }
        }

        viewBinding.apply {
            label.text = text
            label.backgroundTintList = ColorStateList.valueOf(context.color(backgroundColorRes))
            label.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0)
        }
    }

    private fun initButton(status: Status) = when (status) {
        DRAFT -> viewBinding.button.text = "Create"
        IN_PROGRESS -> viewBinding.button.text = "Open"
        COMPLETE -> viewBinding.button.isVisible = false
    }
}

enum class Status {
    DRAFT, IN_PROGRESS, COMPLETE
}

@Preview
@Composable
fun CustomViewPreview(
    @PreviewParameter(CustomViewPreviewParameterProvider::class) viewState: Status
) {
    AndroidView(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth(),
        factory = { context ->
            OrderView(context).apply {
                init(viewState)
            }
        },
    )
}

class CustomViewPreviewParameterProvider : PreviewParameterProvider<Status> {
    override val values: Sequence<Status> = Status.values().asSequence()
}