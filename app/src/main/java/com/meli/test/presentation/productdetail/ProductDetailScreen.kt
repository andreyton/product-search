package com.meli.test.presentation.productdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.meli.test.R
import com.meli.test.domain.model.ProductDetail
import com.meli.test.presentation.components.AppTopBar
import com.meli.test.presentation.components.ErrorView
import com.meli.test.presentation.components.LoadingView
import com.meli.test.presentation.components.ProductAttributes
import com.meli.test.presentation.components.ProductDescription
import com.meli.test.presentation.components.ProductFeatures
import com.meli.test.presentation.components.ProductImages
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProductDetailScreen(
    productId: String,
    onNavigateBack: () -> Unit,
    viewModel: ProductDetailViewModel = koinViewModel(parameters = { parametersOf(productId) })
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProductDetails()
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = stringResource(R.string.detalle),
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
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.padding(paddingValues)
                )
            }

            uiState.productDetail != null -> {
                ProductDetailContent(
                    productDetail = uiState.productDetail!!,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ProductDetailContent(
    productDetail: ProductDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = productDetail.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (productDetail.imageUrls.isNotEmpty()) {
            ProductImages(imageUrls = productDetail.imageUrls, productName = productDetail.name)
        }

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            if (productDetail.condition != null) {
                Text(
                    text = productDetail.condition,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (!productDetail.shortDescription.isNullOrBlank()) {
                ProductDescription(description = productDetail.shortDescription)
            }

            if (productDetail.mainFeatures.isNotEmpty()) {
                ProductFeatures(features = productDetail.mainFeatures)
            }

            if (productDetail.attributes.isNotEmpty()) {
                ProductAttributes(attributes = productDetail.attributes)
            }
        }
    }
}