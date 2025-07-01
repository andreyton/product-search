package com.meli.test

import android.app.Application
import com.meli.test.di.dataModule
import com.meli.test.di.domainModule
import com.meli.test.di.networkModule
import com.meli.test.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MeliApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MeliApplication)
            modules(
                listOf(
                    dataModule,
                    networkModule,
                    domainModule,
                    presentationModule
                )
            )
        }
    }
}