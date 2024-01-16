package com.quiraadev.storyapp.data.source.remote.retrofit

import com.quiraadev.storyapp.data.preference.AppPreference
import com.quiraadev.storyapp.data.source.remote.response.DetailResponse
import com.quiraadev.storyapp.data.source.remote.response.LoginResponse
import com.quiraadev.storyapp.data.source.remote.response.RegisterResponse
import com.quiraadev.storyapp.data.source.remote.response.StoryResponse
import com.quiraadev.storyapp.data.source.remote.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") authorization: String = "Bearer ${AppPreference.token}"
    ): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") authorization: String = "Bearer ${AppPreference.token}"
    ): DetailResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part?,
        @Part("lat") latitude: Float?,
        @Part("lon") longitude: Float?,
        @Header("Authorization") authorization: String = "Bearer ${AppPreference.token}"
    ) : UploadResponse
}