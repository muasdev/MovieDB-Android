package com.muasdev.moviedb_android.di

import com.muasdev.moviedb_android.BuildConfig
import com.muasdev.moviedb_android.data.remote.api.MovieDbApiServices
import com.muasdev.moviedb_android.utils.interceptor.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(provideApiKeyInterceptor())
            .build()


    @Singleton
    @Provides
    fun provideRetrofitService(okHttpClient: OkHttpClient): MovieDbApiServices =
        Retrofit.Builder()
            .baseUrl(MovieDbApiServices.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(okHttpClient)
            .build()
            .create(MovieDbApiServices::class.java)

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor {
        return ApiKeyInterceptor(BuildConfig.API_KEY)
    }
}