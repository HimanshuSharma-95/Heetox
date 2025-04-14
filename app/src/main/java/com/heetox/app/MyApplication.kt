package com.heetox.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication : Application(){

//    @Inject
//    lateinit var Productrepo : productRepository

//    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

//        applicationScope.launch(Dispatchers.IO){
//
//            Productrepo.getCategories()
//            Productrepo.getMostScannedProducts()
//
//        }


    }

}

