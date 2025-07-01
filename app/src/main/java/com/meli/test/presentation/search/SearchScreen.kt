package com.meli.test.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.meli.test.R
import com.meli.test.presentation.components.AppTopBar
import com.meli.test.presentation.components.ErrorView
import com.meli.test.presentation.components.LoadingView

@Composable
fun SearchScreen(
    onNavigateToProductList: () -> Unit,
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.products) {
        if (uiState.hasSearched && uiState.products.isNotEmpty()) {
            onNavigateToProductList()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = stringResource(R.string.meli_box))
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.buscar_productos)) },
                singleLine = true,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.searchProducts()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    focusManager.clearFocus()
                    viewModel.searchProducts()
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                !uiState.hasSearched -> {
                    Text(
                        text = stringResource(R.string.mensaje_mercado_libre),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    )
                }
                uiState.isLoading -> {
                    LoadingView()
                }
                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: stringResource(R.string.ha_ocurrido_un_error),
                        onRetry = { viewModel.searchProducts() },
                    )
                }
                uiState.products.isEmpty() -> {
                    ErrorView(
                        title = stringResource(R.string.alerta),
                        message = stringResource(R.string.no_se_encontraron_productos),
                        onRetry = { viewModel.searchProducts() },
                    )
                }
            }
        }
    }
}