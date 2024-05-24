# 前言（关于为啥有水印）

本次实验将详细说明实验二的具体步骤与实验结果，由于我使用的csdn写的md文件，故可能有水印，水印与我群里的qq一致。

---
# 一、编译start项目
由于版本jdk过高，会提示版本不匹配，这里我改成了jdk1.8
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/183707841c48462cb400485804be1014.png)
然后会提示sdk版本不匹配，这里我改成了29.0

![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/e438e525ddf34b2c92a83a6437e34de6.png)
可以编译了：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/f866894b0d55437d8b005663105a1dfc.png)
编译完成：![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/06f7f2f4d197431593fef1946828a13d.png)



# 二、检查代码中的TODO项
已配置好
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/5aec9596e7d04265a8c0ef7e8a7bf3a6.png)


---
# 三、添加代码重新运行APP
修改后的代码如下：

```kotlin
 private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {

        // TODO 1: Add class variable TensorFlow Lite Model
        // Initializing the flowerModel by lazy so that it runs in the same thread when the process
        private val flowerModel = FlowerModel.newInstance(ctx)
        // method is called.

        // TODO 6. Optional GPU acceleration


        override fun analyze(imageProxy: ImageProxy) {

            val items = mutableListOf<Recognition>()

            // TODO 2: Convert Image to Bitmap then to TensorImage
            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
            // TODO 3: Process the image using the trained model, sort and pick out the top results
            val outputs = flowerModel.process(tfImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score } // Sort with highest confidence first
                }.take(MAX_RESULT_DISPLAY) // take the top results
            // TODO 4: Converting the top probability items into a list of recognitions
            for (output in outputs) {
                items.add(Recognition(output.label, output.score))
            }
            // START - Placeholder code at the start of the codelab. Comment this block of code out.
//            for (i in 0..MAX_RESULT_DISPLAY-1){
//                items.add(Recognition("Fake label $i", Random.nextFloat()))
//            }
            // END - Placeholder code at the start of the codelab. Comment this block of code out.

            // Return the result
            listener(items.toList())

            // Close the image,this tells CameraX to feed the next image to the analyzer
            imageProxy.close()
        }
```
运行结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/direct/04c4309aa5474f7b942c6b7d649cb49c.jpeg)

# 总结
以上就是我今天要讲的内容，本文介绍了实验四的详细步骤，简要写出了实验四可能遇到的问题以及基本代码，运行结果。

