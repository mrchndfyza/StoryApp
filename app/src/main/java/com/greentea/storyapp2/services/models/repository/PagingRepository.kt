package com.greentea.storyapp2.services.models.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.greentea.storyapp2.services.api.StoryAPI
import com.greentea.storyapp2.services.models.StoryPagingSource
import com.greentea.storyapp2.services.models.StoryResult
import com.greentea.storyapp2.services.models.db.StoryDB

class PagingRepository (private val storyDB: StoryDB, private val storyAPI: StoryAPI){
    fun getPaging(context: Context): LiveData<PagingData<StoryResult>>{
        return Pager(
            config = PagingConfig(
                pageSize = 2
            ),
            pagingSourceFactory = {
                StoryPagingSource(storyAPI, context)
            }
        ).liveData
    }
}