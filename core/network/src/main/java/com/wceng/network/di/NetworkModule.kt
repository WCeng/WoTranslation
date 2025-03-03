package com.wceng.network.di

import com.wceng.core.network.BuildConfig
import com.wceng.network.api.TranslateApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = BuildConfig.TRANSLATE_BASE_URL

//class RequestInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        println(request.url())
//        println(request.method())
//        request.url()
//        return chain.proceed(request)
//    }
//}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

//    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(RequestInterceptor()) // 添加自定义拦截器
//        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
//        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideTranslateApi(retrofit: Retrofit): TranslateApi {
        return retrofit.create(TranslateApi::class.java)
    }
}