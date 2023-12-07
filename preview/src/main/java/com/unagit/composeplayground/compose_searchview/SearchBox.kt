package com.unagit.composeplayground.compose_searchview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.unagit.composeplayground.preview.R
import com.unagit.composeplayground.preview.R.color


@Composable
fun SearchBox(
    onBackClick: () -> Unit,
    onSearchTextChanged: (String) -> Unit,
) {
    var searchText: String by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Back icon
        Icon(
            modifier = Modifier.clickable { onBackClick() },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            tint = colorResource(id = R.color.onSurface)
        )

        BasicTextField(
            // Show hint, when 'searchText' is empty
            decorationBox = { innerTextField ->
                if (searchText.isBlank()) {
                    Text(
                        text = stringResource(id = R.string.search_placeholder),
                        style = TextStyle(
                            color = colorResource(id = R.color.onSurface)
                        )
                    )
                }
                innerTextField()
            },
            modifier = Modifier.weight(1f),
            value = searchText,
            singleLine = true,
            onValueChange = { value ->
                searchText = value
                onSearchTextChanged(value)
            },
        )

        AnimatedVisibility(visible = searchText.isNotBlank()) {
            // Clear text icon
            Icon(
                modifier = Modifier.clickable {
                    searchText = ""
                    onSearchTextChanged("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "",
                tint = colorResource(id = R.color.onSurface),
            )
        }
    }
}

@Preview
@Composable
fun SearchBoxPreview() {
    Box(modifier = Modifier.background(color = colorResource(id = color.white))) {
        SearchBox(
            onBackClick = {},
            onSearchTextChanged = {},
        )
    }
}
