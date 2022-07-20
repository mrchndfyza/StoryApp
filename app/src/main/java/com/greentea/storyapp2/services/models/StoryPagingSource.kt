package com.greentea.storyapp2.services.models

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.greentea.storyapp2.services.api.StoryAPI
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.preferences.UserPreference

class StoryPagingSource (private val storyAPI: StoryAPI, context: Context): PagingSource<Int, StoryResult>(){

    private lateinit var userPreference: UserPreference
    private val token = getToken(context)

    private fun getToken(context: Context): String? {
        userPreference = UserPreference(context)
        return userPreference.getDataLogin(Constants.TOKEN)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResult> {
        return try {
            val page = params.key?: INITIAL_PAGE_INDEX
            val realToken = "Bearer $token"

            val responseData = storyAPI.getPagingStory(realToken, page, params.loadSize)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if(page == 1) null else page -1,
                nextKey = if(responseData.listStory.isNullOrEmpty()) null else page +1
            )
        } catch (exception: Exception){
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResult>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object{
        const val INITIAL_PAGE_INDEX = 1
    }
}