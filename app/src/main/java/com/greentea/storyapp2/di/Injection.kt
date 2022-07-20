package com.greentea.storyapp2.di

import android.content.Context
import com.greentea.storyapp2.services.models.db.StoryDB
import com.greentea.storyapp2.services.models.repository.PagingRepository
import com.greentea.storyapp2.services.retrofit.RetrofitInstance

object Injection {
    fun provideRepository(context: Context): PagingRepository{
        val database = StoryDB.getDatabase(context)
        val storyApi = RetrofitInstance.API_OBJECT
        return PagingRepository(database, storyApi)
    }
}