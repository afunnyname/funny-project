package com.example.funwithimages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.funwithimages.data.HelloWorldService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val baseUrl = "http://192.168.1.63:8080"
        val baseUrl = "http://10.0.2.2:8080"

        val httpClientBuilder =
            OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClientBuilder.build())
            .build()
            .create(HelloWorldService::class.java)

        buttonSayHello.setOnClickListener {

            retrofit.sayHello("Bob").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    textView.text = it
                }, { it.printStackTrace() })

        }

        buttonSendImage.setOnClickListener {

            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            val bitmap = Bitmap.createBitmap(imageView.drawingCache)

            val file = File(this.applicationContext.cacheDir, "image-file.png")
            file.createNewFile()

            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bitmapData = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            val fileRequestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)

            val part: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, fileRequestBody)

            retrofit.sendFile(part).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val bytes = it.byteStream().readBytes()

                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                    imageView.setImageBitmap(bitmap)

                    Log.d("hello", "image worked:$it")
                }, { Log.d("hello", "image error") })
        }
    }
}
