package com.meli.test.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.meli.test.domain.model.Product

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onProductClick(product.id) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            width = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = product.imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Fit
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    if (product.brand.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = product.brand.uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (product.model.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(bottom = 4.dp),
                            text = product.model.uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Text(
                        text = product.priority.uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() {
    ProductItem(
        product = Product(
            id = "MLA123456",
            name = "Samsung Galaxy S24 Ultra 5G Dual SIM 512 GB titanium black 12 GB RAM",
            imageUrl = "https://http2.mlstatic.com/D_NQ_NP_2X_686120-MLA46114829749_052021-F.jpg",
            brand = "SAMSUNG",
            model = "GALAXY",
            priority = "MEDIUM"
        ),
        onProductClick = {}
    )
}