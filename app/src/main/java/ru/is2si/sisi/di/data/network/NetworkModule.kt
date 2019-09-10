package ru.is2si.sisi.di.data.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.is2si.sisi.base.network.BaseUrlInterceptor
import ru.is2si.sisi.base.network.NetworkSettings
import ru.is2si.sisi.data.auth.AuthApi
import ru.is2si.sisi.data.auth.ServerUrlHolder
import ru.is2si.sisi.data.finish.FinishApi
import ru.is2si.sisi.data.network.Network
import ru.is2si.sisi.data.points.PointApi
import ru.is2si.sisi.data.result.ResultApi
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
internal class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
            networkSettings: NetworkSettings,
            serverUrlHolder: ServerUrlHolder
    ): OkHttpClient {
        val logInterceptor = if (networkSettings.logRequests)
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        else
            null
        return OkHttpClient.Builder()
                .addInterceptor(BaseUrlInterceptor(serverUrlHolder))
                .apply { logInterceptor?.let { addNetworkInterceptor(it) } }
                .connectTimeout(networkSettings.connectionTimeout, TimeUnit.SECONDS)
                .readTimeout(networkSettings.readTimeout, TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideRetrofit(
            networkSettings: NetworkSettings,
            okHttpClient: OkHttpClient,
            gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(networkSettings.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Singleton
    @Provides
    fun provideServerUrlHolder(
            sharedPreferences: SharedPreferences,
            networkSettings: NetworkSettings
    ): ServerUrlHolder = ServerUrlHolder(sharedPreferences, networkSettings.baseUrl)

    @Singleton
    @Provides
    fun provideNetworkettings(): NetworkSettings = NetworkSettingsFactory.create()

    @Singleton
    @Provides
    fun provideNetwork(context: Context, gson: Gson): Network = Network(context, gson)

    // TODO: Red_byte 2019-08-02 extract to ApiModule
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideResultApi(retrofit: Retrofit): ResultApi = retrofit.create(ResultApi::class.java)

    @Provides
    @Singleton
    fun providePointApi(retrofit: Retrofit): PointApi = retrofit.create(PointApi::class.java)

    @Provides
    @Singleton
    fun provideFinishApi(retrofit: Retrofit): FinishApi = retrofit.create(FinishApi::class.java)

}