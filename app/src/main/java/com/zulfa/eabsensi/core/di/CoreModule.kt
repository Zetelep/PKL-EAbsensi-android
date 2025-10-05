package com.zulfa.eabsensi.core.di

import com.zulfa.eabsensi.core.data.AuthRepository
import com.zulfa.eabsensi.core.data.pref.UserPreference
import com.zulfa.eabsensi.core.data.pref.dataStore
import com.zulfa.eabsensi.core.data.remote.RemoteDataSource
import com.zulfa.eabsensi.core.data.remote.network.ApiService
import com.zulfa.eabsensi.core.data.remote.network.AuthInterceptor
import com.zulfa.eabsensi.core.domain.repository.IAuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single<OkHttpClient> {
        OkHttpClient.Builder()
            // Logging should be last, so it can see headers/body after AuthInterceptor modifies the request
            .addInterceptor(AuthInterceptor(get()))
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit =  Retrofit.Builder()
            .baseUrl("https://pkl-e-absensi-backend.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { RemoteDataSource(get()) }
    single<IAuthRepository> { AuthRepository(get(), get())}
}

val dataStoreModule = module {
    single { UserPreference.getInstance(androidContext().dataStore) }
}
