package com.unagit.composeplayground.ui

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.unagit.composeplayground.R
import com.unagit.composeplayground.R.string
import com.unagit.composeplayground.color
import com.unagit.composeplayground.databinding.ViewOrderBinding
import com.unagit.composeplayground.dpToPx
import com.unagit.composeplayground.drawable
import com.unagit.composeplayground.ui.Status.COMPLETE
import com.unagit.composeplayground.ui.Status.DRAFT
import com.unagit.composeplayground.ui.Status.IN_PROGRESS

class OrderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val binding = ViewOrderBinding.inflate(LayoutInflater.from(context), this)

    init {
        setPadding(context.dpToPx(16))
        background = context.drawable(R.drawable.background_info_view)
    }

    fun init(status: Status) {
        initItems()
        initLabel(status)
        initButton(status)
    }

    private fun initItems() {
        val item = "Dimension" to "34x12x8"
        val textView = TextView(context).apply {
            id = generateViewId()
            text = context.getString(string.item_description, item.first, item.second)
            setTextColor(context.color(R.color.primary))
            setTextAppearance(R.style.TextNormal)
        }
        addView(textView)
        setConstraints(view = textView, topViewId = binding.title.id)
    }

    private fun initLabel(status: Status) {
        @StringRes val text: Int
        @ColorRes val backgroundColorRes: Int
        @DrawableRes val iconRes: Int

        when (status) {
            DRAFT -> {
                text = string.draft
                backgroundColorRes = R.color.draft
                iconRes = R.drawable.ic_line_circle
            }

            IN_PROGRESS -> {
                text = string.in_progress
                backgroundColorRes = R.color.in_progress
                iconRes = R.drawable.ic_arrow_circle_right
            }

            COMPLETE -> {
                text = string.complete
                backgroundColorRes = R.color.complete
                iconRes = R.drawable.ic_check_circle
            }
        }

        binding.apply {
            label.text = context.getString(text)
            label.backgroundTintList = ColorStateList.valueOf(context.color(backgroundColorRes))
            label.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0)
        }
    }

    private fun initButton(status: Status) = when (status) {
        DRAFT -> binding.button.text = context.getString(string.create)
        IN_PROGRESS -> binding.button.text = context.getString(string.open)
        COMPLETE -> binding.button.isVisible = false
    }

    private fun setConstraints(view: View, topViewId: Int) {
        val constraintSet = ConstraintSet().apply {
            clone(this@OrderView)
            connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(view.id, ConstraintSet.TOP, topViewId, ConstraintSet.BOTTOM, context.dpToPx(8))
        }
        constraintSet.applyTo(this)
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
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
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