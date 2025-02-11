package com.heetox.app.Repository.AuthenticationRepository

import android.content.SharedPreferences
import com.heetox.app.Model.Authentication.LocalStoredData
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {


    private var LocalData = MutableStateFlow<LocalStoredData?>(null)

    val accessLocalData : StateFlow<LocalStoredData?>
        get() = LocalData


    suspend fun saveToken(data : LocalStoredData) {

        val gson = Gson()
        val json = gson.toJson(data)
        sharedPreferences.edit().putString("UserData",json).apply()

        LocalData.emit(if(json != null){
            gson.fromJson(json,LocalStoredData::class.java)
        }else{
            null
        })

    }

    suspend fun getToken(){

      val gson = Gson()
        val json = sharedPreferences.getString("UserData", null)

        LocalData.emit(if(json != null){
            gson.fromJson(json,LocalStoredData::class.java)
        }else{
            null
        })

    }

   suspend fun removeToken(){

        sharedPreferences.edit().putString("UserData", null).apply()
        LocalData.emit(null)

    }

}