package com.meli.test.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.meli.test.presentation.productdetail.ProductDetailScreen
import com.meli.test.presentation.search.ProductListScreen
import com.meli.test.presentation.search.ProductViewModel
import com.meli.test.presentation.search.SearchScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavGraph(navController: NavHostController) {

    // Unique instance
    val productViewModel: ProductViewModel = koinViewModel()
    
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Search.route
    ) {
        composable(route = ScreenRoute.Search.route) {
            SearchScreen(
                onNavigateToProductList = {
                    navController.navigate(ScreenRoute.ProductList.route)
                },
                viewModel = productViewModel
            )
        }
        
        composable(route = ScreenRoute.ProductList.route) { backStackEntry ->
            ProductListScreen(
                viewModel = productViewModel,
                onNavigateToProductDetail = { productId ->
                    navController.navigate(ScreenRoute.ProductDetail.createRoute(productId))
                },
                onNavigateBack = {
                    productViewModel.clearResults()
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = ScreenRoute.ProductDetail.route,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}