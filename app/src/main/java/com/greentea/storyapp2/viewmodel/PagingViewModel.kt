package com.greentea.storyapp2.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.greentea.storyapp2.di.Injection
import com.greentea.storyapp2.services.models.StoryResult
import com.greentea.storyapp2.services.models.repository.PagingRepository

class PagingViewModel (pagingRepository: PagingRepository, context: Context): ViewModel(){
    val paging: LiveData<PagingData<StoryResult>> =
        pagingRepository.getPaging(context).cachedIn(viewModelScope)
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val context: Context): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PagingViewModel::class.java)){
            return PagingViewModel(Injection.provideRepository(context), context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}