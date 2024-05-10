# 前言（关于为啥有水印）

本次实验将详细说明实验二的具体步骤与实验结果，由于我使用的csdn写的md文件，故可能有水印，水印与我群里的qq一致。

---


# 一、添加 Gradle 依赖
我的gradle版本与教程不一致，故使用了不一样的添加依赖的语法：

```kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding=true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //CameraX
    val camerax_version = "1.3.0-alpha04"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:${camerax_version}")
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")
    implementation("androidx.camera:camera-video:${camerax_version}")

    implementation("androidx.camera:camera-view:${camerax_version}" )
    implementation("androidx.camera:camera-extensions:${camerax_version}")

    //accompanist处理权限依赖库
    val accompanist_version = "0.31.2-alpha"
    implementation("com.google.accompanist:accompanist-permissions:$accompanist_version")

}

```
如图：
![三大是在这里插入图片描述](https://img-blog.csdnimg.cn/direct/d7050ae1c9f84c628edee38a31a2529f.png#pic_center)

---
# 二、创建项目布局
## 1.activity_main.xml
创建完成，如图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/59720c0406804c8b9a22252e61e6691c.png#pic_center)


## 2.strings.xml

```kotlin
<resources>
    <string name="app_name">CameraXApp</string>
    <string name="take_photo">Take Photo</string>
    <string name="start_capture">Start Capture</string>
    <string name="stop_capture">Stop Capture</string>
</resources>
```


---
# 三.请求必要的权限
AndroidManifest.xml代码如下：

```kotlin
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-feature android:name="android.hardware.camera.any" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="28" />
```
MainActivity.kt代码如下：

```kotlin
override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
```
运行应用，此时弹出请求权限窗口：
![请添加图片描述](https://img-blog.csdnimg.cn/direct/ca205f8b50144a77a105e059c349e482.jpeg)
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1e67c6be19dc4c61863b097586d81b3c.jpeg)

---
# 四.相机预览

代码：

```kotlin
private fun startCamera() {
   val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

   cameraProviderFuture.addListener({
       // Used to bind the lifecycle of cameras to the lifecycle owner
       val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

       // Preview
       val preview = Preview.Builder()
          .build()
          .also {
              it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
          }

       // Select back camera as a default
       val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

       try {
           // Unbind use cases before rebinding
           cameraProvider.unbindAll()

           // Bind use cases to camera
           cameraProvider.bindToLifecycle(
               this, cameraSelector, preview)

       } catch(exc: Exception) {
           Log.e(TAG, "Use case binding failed", exc)
       }

   }, ContextCompat.getMainExecutor(this))
}
```
如图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/1ca29df043334d9a99527cc467eba27c.jpeg)

---
# 五.拍照实现：
调用拍照按钮：

```kotlin
private fun takePhoto() {
   // Get a stable reference of the modifiable image capture use case
   val imageCapture = imageCapture ?: return

   // Create time stamped name and MediaStore entry.
   val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
              .format(System.currentTimeMillis())
   val contentValues = ContentValues().apply {
       put(MediaStore.MediaColumns.DISPLAY_NAME, name)
       put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
       if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
           put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
       }
   }

   // Create output options object which contains file + metadata
   val outputOptions = ImageCapture.OutputFileOptions
           .Builder(contentResolver,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues)
           .build()

   // Set up image capture listener, which is triggered after photo has
   // been taken
   imageCapture.takePicture(
       outputOptions,
       ContextCompat.getMainExecutor(this),
       object : ImageCapture.OnImageSavedCallback {
           override fun onError(exc: ImageCaptureException) {
               Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
           }

           override fun
               onImageSaved(output: ImageCapture.OutputFileResults){
               val msg = "Photo capture succeeded: ${output.savedUri}"
               Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
               Log.d(TAG, msg)
           }
       }
   )
}
```
拍照并存储：

```kotlin
private fun startCamera() {
   val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

   cameraProviderFuture.addListener({
       // Used to bind the lifecycle of cameras to the lifecycle owner
       val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

       // Preview
       val preview = Preview.Builder()
           .build()
           .also {
                 it.setSurfaceProvider(viewFinder.surfaceProvider)
           }

       imageCapture = ImageCapture.Builder()
           .build()

       // Select back camera as a default
       val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

       try {
           // Unbind use cases before rebinding
           cameraProvider.unbindAll()

           // Bind use cases to camera
           cameraProvider.bindToLifecycle(
               this, cameraSelector, preview, imageCapture)

       } catch(exc: Exception) {
           Log.e(TAG, "Use case binding failed", exc)
       }

   }, ContextCompat.getMainExecutor(this))
}
```
拍好的效果如图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/0ed6499e75634609bb681a633ebe2c2a.jpeg)

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/406fb5be43b64f50bd5173a25fdc37d8.png)

---
# 六.拍摄视频
拍摄键的启动与终止：
```kotlin
// Implements VideoCapture use case, including start and stop capturing.
private fun captureVideo() {
   val videoCapture = this.videoCapture ?: return

   viewBinding.videoCaptureButton.isEnabled = false

   val curRecording = recording
   if (curRecording != null) {
       // Stop the current recording session.
       curRecording.stop()
       recording = null
       return
   }

   // create and start a new recording session
   val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
              .format(System.currentTimeMillis())
   val contentValues = ContentValues().apply {
       put(MediaStore.MediaColumns.DISPLAY_NAME, name)
       put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
       if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
           put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
       }
   }

   val mediaStoreOutputOptions = MediaStoreOutputOptions
       .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
       .setContentValues(contentValues)
       .build()
   recording = videoCapture.output
       .prepareRecording(this, mediaStoreOutputOptions)
       .apply {
           if (PermissionChecker.checkSelfPermission(this@MainActivity,
                   Manifest.permission.RECORD_AUDIO) ==
               PermissionChecker.PERMISSION_GRANTED)
           {
               withAudioEnabled()
           }
       }
       .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
           when(recordEvent) {
               is VideoRecordEvent.Start -> {
                   viewBinding.videoCaptureButton.apply {
                       text = getString(R.string.stop_capture)
                       isEnabled = true
                   }
               }
               is VideoRecordEvent.Finalize -> {
                   if (!recordEvent.hasError()) {
                       val msg = "Video capture succeeded: " +
                           "${recordEvent.outputResults.outputUri}"
                       Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                            .show()
                       Log.d(TAG, msg)
                   } else {
                       recording?.close()
                       recording = null
                       Log.e(TAG, "Video capture ends with error: " +
                           "${recordEvent.error}")
                   }
                   viewBinding.videoCaptureButton.apply {
                       text = getString(R.string.start_capture)
                       isEnabled = true
                   }
               }
           }
       }
}
```
将 cameraProvider.bindToLifecycle() 调用替换为以下代码：

```kotlin
// Bind use cases to camera
cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
```


效果如图：
启动：


![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7e9e4ca046834c319aea1065c4fd60ed.jpeg)
结束：


![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/0a9aa1997b64453f8f829aefa1a18b47.jpeg)
拍摄的视频：

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/dbd827eb4c3a4c72846329e19dc6764c.jpeg)
---
# 总结

以上就是我今天要讲的内容，本文介绍了实验三的详细步骤，简要写出了实验三可能遇到的问题以及基本代码，运行结果。



