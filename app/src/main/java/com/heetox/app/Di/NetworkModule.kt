package com.heetox.app.Di

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.heetox.app.ApiInterface.AuthenticationInterface
import com.heetox.app.ApiInterface.PostInterface
import com.heetox.app.ApiInterface.ProductInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Singleton
    @Provides
    fun ProvideRetrofit() : Retrofit {

        return Retrofit.Builder()
            .baseUrl("https://product-app-backend-z83m.onrender.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    @Singleton
    @Provides
    fun ProvideAuthApi(retrofit: Retrofit) : AuthenticationInterface{

        return retrofit.create(AuthenticationInterface::class.java)

    }



    @Singleton
    @Provides
    fun ProvideProductApi(retrofit: Retrofit) : ProductInterface{

        return retrofit.create(ProductInterface::class.java)

    }


    @Singleton
    @Provides
    fun ProvidePostApi(retrofit: Retrofit) : PostInterface{

        return retrofit.create(PostInterface::class.java)

    }



    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences {

        val masterKeyAlias = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()




        return EncryptedSharedPreferences.create(
                context,
            "secure_prefs",
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }



}




fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
}



@Composable
fun checkInternetConnection(): Boolean {
    val context = LocalContext.current
    return remember { hasInternetConnection(context) }
}
