package com.greentea.storyapp2.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greentea.storyapp2.services.models.APIResponse
import com.greentea.storyapp2.services.models.ListStory
import com.greentea.storyapp2.services.models.LoginResponse
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.utils.Resources
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    val apiUsers: MutableLiveData<Response<APIResponse>> = MutableLiveData()
    val loginUsers: MutableLiveData<Response<LoginResponse>> = MutableLiveData()
    val addStoryUsers: MutableLiveData<Response<APIResponse>> = MutableLiveData()
    val listStoryUser: MutableLiveData<Resources<ListStory>> = MutableLiveData()
    val listMapUser: MutableLiveData<Response<ListStory>> = MutableLiveData()

    fun getRegisterUsersResponse(name: String, email: String, password: String) =
        viewModelScope.launch {
            val response = storyRepository.registerUser(name, email, password)
            apiUsers.value = response
        }

    fun getLoginUsersResponse(email: String, password: String) =
        viewModelScope.launch {
            val response = storyRepository.loginUser(email, password)
            loginUsers.value = response
        }

    fun getAddStoryUserResponse(
        mySuperSecretKey: String,
        file: MultipartBody.Part,
        description: RequestBody
    ) =
        viewModelScope.launch {
            val response = storyRepository.addStoryUser(mySuperSecretKey, file, description)
            addStoryUsers.value = response
        }

    fun getListStoryUsers(mySuperSecretKey: String) =
        viewModelScope.launch {
            listStoryUser.postValue(Resources.Loading())
            val response = storyRepository.getListStoryUser(mySuperSecretKey)
            listStoryUser.postValue(handleListStoryResponse(response))
        }

    fun getListViaMap(mySuperSecretKey: String) =
        viewModelScope.launch {
            val response = storyRepository.getListViaMap(mySuperSecretKey)
            listMapUser.value = response
        }

    private fun handleListStoryResponse(
        response: Response<ListStory>
    ): Resources<ListStory> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
}