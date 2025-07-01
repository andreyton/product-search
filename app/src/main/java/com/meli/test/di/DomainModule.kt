package com.meli.test.di

import com.meli.test.domain.usecase.GetProductDetailsUseCase
import com.meli.test.domain.usecase.SearchProductsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetProductDetailsUseCase(get()) }
    factory { SearchProductsUseCase(get()) }
}