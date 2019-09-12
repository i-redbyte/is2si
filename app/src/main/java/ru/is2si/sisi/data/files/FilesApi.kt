package ru.is2si.sisi.data.files

import io.reactivex.Completable
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FilesApi {
    @Multipart
    @POST("Api/upload/")
    fun uploadFile(
            @Part("filesDescript")
            type: String,
            @Part("pinkod")
            pin: String,
            @Part("NameCommand")
            teamName: String,
            @Part file: MultipartBody.Part
    ): Completable
}