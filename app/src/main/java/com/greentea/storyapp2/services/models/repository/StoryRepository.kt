package com.greentea.storyapp2.services.models.repository

import com.greentea.storyapp2.services.retrofit.RetrofitInstance
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository() {
    suspend fun registerUser(name: String, email: String, password: String) =
        RetrofitInstance.API_OBJECT.registerUser(name, email, password)

    suspend fun loginUser(email: String, password: String) =
        RetrofitInstance.API_OBJECT.loginUser(email, password)

    suspend fun addStoryUser(
        mySuperSecretKey: String,
        file: MultipartBody.Part,
        description: RequestBody) =
        RetrofitInstance.API_OBJECT.addStoryUser(mySuperSecretKey, file, description)

    suspend fun getListStoryUser(mySuperSecretKey: String) =
        RetrofitInstance.API_OBJECT.getListStoryUser(mySuperSecretKey)

    suspend fun getListViaMap(mySuperSecretKey: String) =
        RetrofitInstance.API_OBJECT.getListViaMap(mySuperSecretKey)

}