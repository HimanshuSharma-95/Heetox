package com.heetox.app.Repository.AuthenticationRepository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.heetox.app.Model.Authentication.LocalStoredData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    private val _tokenFlow = MutableStateFlow<String?>(getToken()?.Token)
    val tokenFlow = _tokenFlow.asStateFlow()

    fun saveToken(data : LocalStoredData):LocalStoredData?{

        val gson = Gson()
        val json = gson.toJson(data)
        sharedPreferences.edit().putString("UserData",json).apply()

        return if(json != null){
            val savedData = gson.fromJson(json,LocalStoredData::class.java)
            _tokenFlow.value = savedData?.Token
            savedData
        }else{
            null
        }

    }

     fun getToken():LocalStoredData?{

      val gson = Gson()
        val json = sharedPreferences.getString("UserData", null)

        return if(json != null){
            gson.fromJson(json,LocalStoredData::class.java)
        }else{
             null
        }

    }

    fun removeToken(){
        sharedPreferences.edit().putString("UserData", null).apply()
        _tokenFlow.value = ""
    }

}