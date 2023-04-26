package com.example.booksriver.di

import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.example.booksriver.data.K
import com.example.booksriver.network.AuthInterceptor
import com.example.booksriver.network.api.AuthService
import com.example.booksriver.network.api.BooksriverService
import com.example.booksriver.session.SessionManager
import com.example.booksriver.session.SharedPreferencesFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRetrofit(
        authInterceptor: AuthInterceptor,
        @ApplicationContext appContext: Context
    ): Retrofit {
        val okHttpClientBuilder: OkHttpClient.Builder =
            OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(
                    ChuckerInterceptor.Builder(appContext)
                        .collector(
                            ChuckerCollector(
                                context = appContext,
                                showNotification = true,
                                retentionPeriod = RetentionManager.Period.ONE_HOUR
                            )
                        )
                        .build()
                )
                .addInterceptor(authInterceptor)

        return Retrofit.Builder()
            .baseUrl(K.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
    }

    @Provides
    fun provideBooksriverService(retrofit: Retrofit): BooksriverService =
        retrofit.create(BooksriverService::class.java)


    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)


    @Singleton
    @Provides
    fun provideSessionManager(application: Application): SessionManager {
        return SessionManager(SharedPreferencesFactory.sessionPreferences(application))
    }
}
