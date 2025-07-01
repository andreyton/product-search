package com.meli.test.di

import com.meli.test.presentation.productdetail.ProductDetailViewModel
import com.meli.test.presentation.search.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { ProductViewModel(get()) }
    viewModel { parameters -> ProductDetailViewModel(get(), parameters.get()) }
}