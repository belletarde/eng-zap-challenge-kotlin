package br.com.zapgroup

import android.app.Application

import br.com.zapgroup.di.*
import br.com.zapgroup.di.netModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ZapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ZapApplication)
            modules(netModule, serviceModule, repositoryModule, sharedModule, mainModule)
        }
    }


}