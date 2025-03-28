package com.heetox.app.Screens.ProdcutScreens


import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.heetox.app.R
import com.heetox.app.Utils.Resource
import com.heetox.app.ViewModel.ProductsVM.ProductsViewModel
import com.heetox.app.ui.theme.HeetoxDarkGreen
import com.heetox.app.ui.theme.HeetoxGreen
import com.heetox.app.ui.theme.HeetoxWhite


@Composable
fun Scanscreen(navController: NavController) {

    val productVM: ProductsViewModel = hiltViewModel()

    BackHandler {
        navController.navigate("home") {
            popUpTo("scan") { inclusive = true }
        }
    }


    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            if (!isGranted) {
                Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Request the camera permission when the composable is first launched
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            hasCameraPermission = true
        }
    }

    if (hasCameraPermission) {
        SearchProductScreen(productViewModel = productVM, authToken = "", navController = navController)
    } else {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Waiting for camera permission...", Toast.LENGTH_SHORT).show()
        }
    }
}







@Composable
fun BarcodeScanner(onBarcodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val barcodeScanner = remember { BarcodeScanning.getClient() }
val scrollState = rememberScrollState()


   Column(
        modifier = Modifier
            .fillMaxSize()
            .background(HeetoxWhite)
            .verticalScroll(scrollState)
        ,

       horizontalAlignment = Alignment.CenterHorizontally
    ) {

       Spacer(modifier = Modifier.height(100.dp))


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(painter = painterResource(id = R.drawable.heetoxlogobg), contentDescription = "",
                modifier = Modifier
                    .width(150.dp)
            )
            
            Text(
                text = "Scan Barcode",
                color = HeetoxDarkGreen,
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,

            )

            Spacer(modifier = Modifier.height(15.dp))

            Column(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color.Black)
                    .border(5.dp, HeetoxGreen, RoundedCornerShape(26.dp))
//                    .padding(8.dp)
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


       Spacer(modifier = Modifier.height(100.dp))

    }


    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        val imageAnalyzer = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                    processImageProxy(barcodeScanner, imageProxy, onBarcodeScanned)
                }
            }

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )
        } catch (e: Exception) {
            Log.e("BarcodeScanner", "Use case binding failed", e)
        }
    }
}



@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onBarcodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { rawValue ->
                        onBarcodeScanned(rawValue)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("BarcodeScanner", "Barcode scanning failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}







@Composable
fun SearchProductScreen(
    productViewModel: ProductsViewModel,
    authToken: String,
    navController: NavController
) {
    val context = LocalContext.current
    val productResponse by productViewModel.checkbarcode.collectAsState()



    var previousScannedBarcode by rememberSaveable { mutableStateOf("") }
    var scannedBarcode by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    BarcodeScanner(onBarcodeScanned = { barcode ->
        if (barcode.isNotEmpty() && barcode != scannedBarcode && !isLoading) {
            scannedBarcode = barcode
            isLoading = true

            productViewModel.checkbarcode(barcode)

        }
    })



    LaunchedEffect(scannedBarcode, productResponse) {

        when (productResponse) {

            is Resource.Error -> {
                isLoading = false
                Toast.makeText(context, "Product Not Found", Toast.LENGTH_SHORT).show()
            }


            is Resource.Loading -> {
                isLoading = true
            }


            is Resource.Success -> {
                isLoading = false

                if (scannedBarcode != previousScannedBarcode) {
                    previousScannedBarcode = scannedBarcode

                    if (productResponse.data?.product_data == true) {

                        navController.navigate("productdetails/$scannedBarcode")
                        {
                            popUpTo("scan") { inclusive = false }
                        }

                } else {

                    Toast.makeText(context, "Product Not Found", Toast.LENGTH_SHORT).show()

                }

            }

            }


            is Resource.Nothing -> {
                isLoading = false
            }


        }
    }



    LaunchedEffect(Unit) {
        scannedBarcode = ""
        previousScannedBarcode = ""
    }



}