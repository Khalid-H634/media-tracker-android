package edu.metrostate.ics342.mediatracker.ui.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import edu.metrostate.ics342.mediatracker.R
import edu.metrostate.ics342.mediatracker.data.FakeMediaRepository
import edu.metrostate.ics342.mediatracker.data.model.Media
import androidx.compose.foundation.shape.RoundedCornerShape
@Composable
fun SearchResultsScreen(
    initialQuery: String,
    onBack: () -> Unit,
    onMediaClick: (Int) -> Unit
) {
    var searchBarQuery by remember { mutableStateOf(initialQuery) }
    val results = remember { mutableStateListOf<Media>() }
    var selectedType by remember { mutableStateOf("") }

    LaunchedEffect(initialQuery) {
        results.clear()
        results.addAll(FakeMediaRepository.mediaList)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back)
                )
            }
            OutlinedTextField(
                value = searchBarQuery,
                onValueChange = { searchBarQuery = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    val query = searchBarQuery.lowercase()
                    results.clear()
                    results.addAll(
                        FakeMediaRepository.mediaList
                            .filter { it.title.lowercase().contains(query) }
                    )
                })
            )
        }

        MediaTypeFilterChips(
            selectedType = selectedType,
            onTypeSelect = { type ->
                selectedType = type
                results.clear()
                results.addAll(
                    FakeMediaRepository.mediaList
                        .filter { type.isEmpty() || it.mediaType == type }
                )
            },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = stringResource(R.string.search_results_count, results.size),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(results, key = { it.id }) { media ->
                MediaResultCard(
                    media = media,
                    onClick = { onMediaClick(media.id) }
                )
            }
        }
    }
}