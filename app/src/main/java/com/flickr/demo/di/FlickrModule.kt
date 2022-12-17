package com.flickr.demo.di

import com.flickr.demo.data.api.FlickrAPI
import com.flickr.demo.data.repository.FlickrRepository
import com.flickr.demo.data.repository.impl.FlickrRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FlickrModule {
    private const val BASE_URL = "https://www.flickr.com/services/"

    @Singleton
    @Provides
    fun provideFlickrApiService(retrofit: Retrofit): FlickrAPI {
        return retrofit.create(FlickrAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideFlickrRepository(
        flickrAPI: FlickrAPI,
    ): FlickrRepository {
        return FlickrRepositoryImpl(
            flickrAPI,
            Dispatchers.IO
        )
    }

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }
}