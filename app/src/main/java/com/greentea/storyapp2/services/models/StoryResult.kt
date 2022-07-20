package com.greentea.storyapp2.services.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "story")
data class StoryResult(
    @PrimaryKey
    val id: String,

    val createdAt: String,
    val description: String,
    val name: String,
    val photoUrl: String,
    val lat: String,
    val lon: String
) : Serializable