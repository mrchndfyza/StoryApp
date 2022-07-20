package com.greentea.storyapp2.services.models

data class ListStory(
    val error: Boolean,
    val listStory: List<StoryResult>,
    val message: String
)