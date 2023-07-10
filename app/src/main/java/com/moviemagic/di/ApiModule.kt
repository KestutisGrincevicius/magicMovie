package com.moviemagic.di

import android.content.Context
import com.moviemagic.AppService
import com.moviemagic.data.api.interceptor.HeaderInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val API_SERVICE_TIMEOUT = 60L

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideClientApi(
        @Named("apiUrl") apiUrl: String,
        okHttpClientBuilder: OkHttpClient.Builder
    ): AppService {
        okHttpClientBuilder.connectTimeout(API_SERVICE_TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(API_SERVICE_TIMEOUT, TimeUnit.SECONDS)
        val okHttpClient = okHttpClientBuilder.build()
        val retrofit = createRetrofit(okHttpClient, apiUrl)
        return retrofit.create(AppService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpBuilder(
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient.Builder {
        val okBuilder = OkHttpClient.Builder()
        okBuilder.addInterceptor(headerInterceptor)

        val loggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
        okBuilder.addInterceptor(loggingInterceptor)

        return okBuilder
    }

    @Provides
    @Singleton
    @Named("apiUrl")
    fun provideApiUrl(@ApplicationContext context: Context) = "https://moviesdatabase.p.rapidapi.com/"

    private fun createRetrofit(okHttpClient: OkHttpClient, apiUrl: String): Retrofit {
        val retrofitBuilder = Retrofit.Builder()
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create())
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        retrofitBuilder.client(okHttpClient)
        retrofitBuilder.baseUrl(apiUrl)
        return retrofitBuilder.build()
    }

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

}