package com.meli.test.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.meli.test.R
import com.meli.test.presentation.components.AppTopBar
import com.meli.test.presentation.components.ErrorView
import com.meli.test.presentation.components.LoadingView
import com.meli.test.presentation.components.ProductItem

@Composable
fun ProductListScreen(
    onNavigateToProductDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(Unit) {
        viewModel.loadProductsForQuery()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.resultados_para),
                showBackButton = true,
                onBackPressed = onNavigateBack
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                LoadingView(
                    modifier = Modifier.padding(paddingValues)
                )
            }
            uiState.error != null -> {
                ErrorView(
                    message = uiState.error ?: stringResource(R.string.ha_ocurrido_un_error),
                    onRetry = { viewModel.searchProducts() },
                    modifier = Modifier.padding(paddingValues)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(uiState.products) { product ->
                        ProductItem(
                            product = product,
                            onProductClick = onNavigateToProductDetail
                        )
                    }
                }
            }
        }
    }
}