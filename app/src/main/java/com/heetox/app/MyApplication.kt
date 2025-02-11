package com.heetox.app

import android.app.Application
import com.heetox.app.Repository.ProductRepository.productRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class MyApplication : Application(){

    @Inject
    lateinit var Productrepo : productRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch(Dispatchers.IO){

            Productrepo.getCategories()
            Productrepo.getMostScannedProducts()

        }


    }

}

