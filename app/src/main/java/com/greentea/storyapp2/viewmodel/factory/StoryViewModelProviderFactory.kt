package com.greentea.storyapp2.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.viewmodel.StoryViewModel

@Suppress("UNCHECKED_CAST")
class StoryViewModelProviderFactory (
    private val storyRepository: StoryRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StoryViewModel(storyRepository) as T
        }
}