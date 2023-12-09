package com.unagit.composeplayground.compose_searchview

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unagit.composeplayground.preview.R
import com.unagit.composeplayground.preview.R.color

@Composable
fun SearchListView(
    modifier: Modifier,
    items: List<ListItem>,
    onItemClick: (ListItem) -> Unit,
) {

    LazyColumn(
        modifier = modifier
    ) {
        itemsIndexed(items) { index, item ->
            Text(
                modifier = Modifier
                    .clickable { onItemClick(item) }
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = item.getName()
            )

            // Show divider
            if (index != items.size - 1) {
                Divider()
            }
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 200,
)
@Composable
fun SearchListViewPreview() {
    var clickedItem: String by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = color.white)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SearchListView(
            modifier = Modifier,
            items = items,
            onItemClick = {
                clickedItem = "${it.getName()} clicked"
            }
        )

        Text(text = clickedItem)
    }
}

val items = listOf(
    ListItem { "Item 1" },
    ListItem { "Item 3" },
    ListItem { "Item 35" },
    ListItem { "Item 321" },
)
