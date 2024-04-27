
# 前言

本次实验将详细说明实验二的具体步骤与实验结果。



---





# 一、使用步骤
## 1.新建一个新的项目
如图，我的Android studio是阿尔法版本，故只能选择如图所示的模板         
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/a605f2ce025f4f1386fded89c6d3431a.png#pic_center)创建完成后，等待gradle的构建：                 
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/baa6430029364c648b9df4837f4014b3.png)                    
## 2.更改fragment_first.xml的界面                      
构建完成，此时可以看到模块与老师的不同：                       
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/a9790a2ebb75419dbde10c2476ce4122.png)                   
观察得知：与老师的图片相比，新模块是多一层父级的                       
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/17422288c21b4ca79dfd3005f10e4b95.png)                
删除NestedScrollView的父层，改为ConstraintLayout即可：                   

                                             

>代码如下：

```c
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screenBackground"

    tools:context=".FirstFragment">
    ...
    </androidx.constraintlayout.widget.ConstraintLayout>

```

然后增加按键ID并且修改文本内容，更改按键与text的约束，如图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/388f175875a042d1b708d4df26ab6098.png)
如果出现按键对不齐的情况，将加粗部分的BOTTOM改为TOP或相反即可，例如：app:layout_constraintTop_to**Bottom**Of="@id/textview_first"


然后可以背景颜色的库如以下代码：
>代码如下：
>

```
<color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
    <color name="screenBackground">#2196F3</color>
    <color name="buttonBackground">#BBDEFB</color>
    <color name="colorPrimaryDark">#3700B3</color>
    <color name="screenBackground2">#26C6DA</color>
```
fragment_first.xml的整体代码如下：

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screenBackground"

    tools:context=".FirstFragment">


    <Button
        android:id="@+id/random_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="24dp"

        android:background="@color/buttonBackground"
        android:text="random"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        tools:ignore="TextContrastCheck" />

    <TextView
        android:id="@+id/textview_first"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="sans-serif-condensed"
        android:text="0"
        android:textColor="@color/white"
        android:textSize="72sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.3" />

    <Button
        android:id="@+id/count_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:background="@color/buttonBackground"
        android:text="count"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/random_button"
        app:layout_constraintStart_toEndOf="@id/toast_button"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        tools:ignore="TextContrastCheck" />

    <Button
        android:id="@+id/toast_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:background="@color/buttonBackground"
        android:text="toast"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        app:layout_constraintVertical_bias="0.501"
        tools:ignore="TextContrastCheck"
        />
</androidx.constraintlayout.widget.ConstraintLayout>
```

## 3.fragment_second.xml的页面编写                   
整体效果与约束如下：                          
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/7723d482e18d4f57b5411261d332a668.png)                            




代码如下：

```c
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/screenBackground"

    tools:context=".FirstFragment">


    <Button
        android:id="@+id/random_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="24dp"

        android:background="@color/buttonBackground"
        android:text="random"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        tools:ignore="TextContrastCheck" />

    <TextView
        android:id="@+id/textview_first"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:fontFamily="sans-serif-condensed"
        android:text="0"
        android:textColor="@color/white"
        android:textSize="72sp"
        android:textStyle="bold"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.3" />

    <Button
        android:id="@+id/count_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"

        android:background="@color/buttonBackground"
        android:text="count"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/random_button"
        app:layout_constraintStart_toEndOf="@id/toast_button"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        tools:ignore="TextContrastCheck" />

    <Button
        android:id="@+id/toast_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:background="@color/buttonBackground"
        android:text="toast"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@id/textview_first"
        app:layout_constraintVertical_bias="0.501"
        tools:ignore="TextContrastCheck"
        />
</androidx.constraintlayout.widget.ConstraintLayout>
```
颜色库如第一页面所示


---
## 4.SafeArg的安装
阿尔法版的SafeArg也有所不同，经过查找资料，我找出了合适的代码：
build.gradle.kts（Project）：

```
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false


}
buildscript {
    repositories {
        google()
    }
    dependencies {
        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
```
build.gradle.kts（Module）：

```
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.aexam"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aexam"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}
```
---
## 5.kt文件中功能的实现
FirstFragment.kt的功能实现如下：
//实现Count按键功能的函数
```
private fun countMe(view: View) {
    // Get the text view
    val showCountTextView = view.findViewById<TextView>(R.id.textview_first)

    // Get the value of the text view.
    val countString = showCountTextView.text.toString()

    // Convert value to a number and increment it
    var count = countString.toInt()
    count++

    // Display the new value in the text view.
    showCountTextView.text = count.toString()
}
```
//实现Toast提示功能与Random跳转页面与传递数据实现随机的功能代码
```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        // find the toast_button by its ID and set a click listener
        view.findViewById<Button>(R.id.toast_button).setOnClickListener {
            // create a Toast with some text, to appear for a short time
            val myToast = Toast.makeText(context, "Hello Toast!", Toast.LENGTH_LONG)
            // show the Toast
            myToast.show()
        }

        view.findViewById<Button>(R.id.count_button).setOnClickListener {
            countMe(view)
        }
        view.findViewById<Button>(R.id.random_button).setOnClickListener {
            val showCountTextView = view.findViewById<TextView>(R.id.textview_first)
            val currentCount = showCountTextView.text.toString().toInt()
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(currentCount)
            findNavController().navigate(action)

        }



    }
```
SecondFragment.kt的功能实现如下：

```
val args: SecondFragmentArgs by navArgs()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        val count = args.myArg
        val countText = getString(R.string.random_heading, count)
        view.findViewById<TextView>(R.id.textview_header).text = countText
        val random = java.util.Random()
        var randomNumber = 0
        if (count > 0) {
            randomNumber = random.nextInt(count + 1)
        }
        view.findViewById<TextView>(R.id.textview_random).text = randomNumber.toString()

    }
```

---
## 6.运行结果
由于我的硬盘空间不足，无法安装虚拟机，故用手机运行，结果如下：
应用信息：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/831f38b0ab9a48718a1ec1d21575c54c.jpeg)

首页：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/57b10ec67ee3413b943292a53c8b7846.jpeg)
TOAST：![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/775a3975321347399064b2b972edc2ed.jpeg)


第二页：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/90f55b5da7a243708f587daccb94fb76.jpeg)

# 总结
以上就是今天要讲的内容，本文详细介绍了实验二的过程，并且解读了实验二遇到的问题与解决方法，展示了实验二的结果

