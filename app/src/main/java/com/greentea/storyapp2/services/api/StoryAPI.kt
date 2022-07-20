package com.greentea.storyapp2.services.api

import com.greentea.storyapp2.services.models.APIResponse
import com.greentea.storyapp2.services.models.ListStory
import com.greentea.storyapp2.services.models.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryAPI {

    //LOGIN USER
    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    //REGISTER USER
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<APIResponse>

    //ADD STORY USER
    @Multipart
    @POST("stories")
    suspend fun addStoryUser(
        @Header("Authorization") mySuperSecretKey: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<APIResponse>

    //GET LIST STORY USER
    @GET("stories")
    suspend fun getListStoryUser(
        @Header("Authorization") mySuperSecretKey: String
    ): Response<ListStory>

    //GET LIST STORY VIA MAP
    @GET("stories?location=1")
    suspend fun getListViaMap(
        @Header("Authorization") mySuperSecretKey: String
    ): Response<ListStory>

    //GET LIST STORY WITH PAGING
    @GET("stories")
    suspend fun getPagingStory(
        @Header("Authorization") mySuperSecretKey: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): ListStory
}