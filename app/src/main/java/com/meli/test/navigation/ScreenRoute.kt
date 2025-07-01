package com.meli.test.navigation

sealed class ScreenRoute(val route: String) {
    object Search : ScreenRoute("search")
    
    object ProductList : ScreenRoute("product_list")
    
    object ProductDetail : ScreenRoute("product_detail/{productId}") {
        fun createRoute(productId: String): String {
            return "product_detail/$productId"
        }
    }
}