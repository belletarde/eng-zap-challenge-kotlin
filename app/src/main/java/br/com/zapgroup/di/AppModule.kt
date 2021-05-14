package br.com.zapgroup.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import br.com.zapgroup.api.ApiService
import br.com.zapgroup.api.PropertyDetailApi
import br.com.zapgroup.api.PropertyListApi
import br.com.zapgroup.api.SplashApi
import br.com.zapgroup.data.AppSharedPreferences
import br.com.zapgroup.utils.SharedValues.Companion.SHARED_NAME
import br.com.zapgroup.repository.PropertyDetailRepository
import br.com.zapgroup.repository.PropertyListRepository
import br.com.zapgroup.repository.SplashRepository
import br.com.zapgroup.viewmodel.PropertyDetailViewModel
import br.com.zapgroup.viewmodel.PropertyListViewModel
import br.com.zapgroup.viewmodel.SplashViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val mainModule = module {
    viewModel {
        SplashViewModel(get())
    }

    viewModel {
        PropertyListViewModel(get())
    }

    viewModel {
        PropertyDetailViewModel(get())
    }
}

val repositoryModule = module {
    fun provideSplashRepository(api: ApiService, shared: AppSharedPreferences): SplashRepository {
        return SplashApi(api, shared)
    }

    fun providePropertyListRepository(shared: AppSharedPreferences): PropertyListRepository {
        return PropertyListApi(shared)
    }

    fun providePropertyDetailRepository(shared: AppSharedPreferences): PropertyDetailRepository {
        return PropertyDetailApi(shared)
    }

    factory { provideSplashRepository(get(), get()) }
    factory { providePropertyListRepository(get()) }
    factory { providePropertyDetailRepository(get()) }
}

val serviceModule = module {
    fun provideUserApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    factory { provideUserApi(get()) }
}

val netModule = module {

    fun provideHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return builder.build()
    }

    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://grupozap-code-challenge.s3-website-us-east-1.amazonaws.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
    factory { provideHttpClient() }
    factory { provideRetrofit(get()) }
}

val sharedModule = module {

    fun provideShared(application: Application): SharedPreferences {
        return  application.getSharedPreferences(SHARED_NAME, MODE_PRIVATE)
    }

    fun provideAppShared(sharedPreferences: SharedPreferences): AppSharedPreferences {
        return AppSharedPreferences(sharedPreferences, sharedPreferences.edit())
    }

    single { provideShared(get()) }
    single { provideAppShared(get()) }
}