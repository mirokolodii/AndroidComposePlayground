package com.unagit.composeplayground.compose_searchview

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchView(
    modifier: Modifier,
    items: List<ListItem>,
    onItemClick: (ListItem) -> Unit,
    onBackClick: () -> Unit,
) {
    var filteredItems: List<ListItem> by remember { mutableStateOf(items) }

    Column(modifier = modifier.fillMaxWidth()) {

        SearchBox(
            onBackClick = onBackClick,
            onSearchTextChanged = { searchText ->
                filteredItems = items.filter { item ->
                    item.getName().contains(searchText, ignoreCase = true)
                }
            }
        )

        Divider(color = Color.DarkGray)

        SearchListView(
            modifier = Modifier.padding(16.dp),
            items = filteredItems,
            onItemClick = onItemClick,
        )
    }
}

@Preview(
    widthDp = 300,
    heightDp = 400,
)
@Composable
fun SearchViewPreview() {
    val items = listOf(
        ListItem { "Item 1" },
        ListItem { "Item 3" },
        ListItem { "Item 35" },
        ListItem { "Item 321" },
    )

    var clickTest by remember { mutableStateOf("") }
    var isClickTextVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.background(color = colorResource(id = color.white)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SearchView(
            modifier = Modifier,
            items = items,
            onItemClick = {
                scope.launch {
                    clickTest = "${it.getName()} clicked"
                    isClickTextVisible = true
                    delay(2000)
                    isClickTextVisible = false
                }
            },
            onBackClick = {},
        )

        if (isClickTextVisible) {
            Text(text = clickTest)
        }
    }
}
