package r2.llc.sch.di


import android.util.Log
import com.mocklets.pluto.PlutoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import r2.llc.sch.BuildConfig
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@[Module InstallIn(SingletonComponent::class)]
object NetworkModule {

    @[Provides Singleton]
    fun provideChuckerCollector(): PlutoInterceptor = PlutoInterceptor()

    @[Provides Singleton]
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor {
        Log.d("NETWORK", it)
    }.apply {
        setLevel(if (BuildConfig.DEBUG) BODY else NONE)
    }

    @[Provides Singleton]
    fun provideJson(): Json = Json {
        isLenient = true
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @[Provides Singleton]
    fun provideClient(
        plutoInterceptor: PlutoInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient().newBuilder()
        .retryOnConnectionFailure(false)
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(plutoInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @[Provides Singleton]
    fun getSocketIO(client: OkHttpClient): Socket {

        val option = IO.Options().apply {
            this.callFactory = client
            this.webSocketFactory = client
            transports = arrayOf("polling", "websocket")
        }

        return IO.socket("https://chat-zxc-socketio.herokuapp.com", option)
    }
}