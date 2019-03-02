package com.example.funwithimages.data

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by tokgozmusa on 20.01.2019.
 */
interface HelloWorldService {
    @POST("/hello")
    fun sayHello(@Query("name") name: String): Single<String>

    @Multipart
    @POST("/send-file")
    fun sendFile(@Part file: MultipartBody.Part): Single<ResponseBody>
}