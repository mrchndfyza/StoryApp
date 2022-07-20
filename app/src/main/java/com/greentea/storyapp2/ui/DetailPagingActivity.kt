package com.greentea.storyapp2.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.greentea.storyapp2.databinding.ActivityDetailPagingBinding
import com.greentea.storyapp2.services.models.StoryResult
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory

class DetailPagingActivity : AppCompatActivity() {
    //BINDING
    private lateinit var detailPagingBinding: ActivityDetailPagingBinding

    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailPagingBinding = ActivityDetailPagingBinding.inflate(layoutInflater)
        setContentView(detailPagingBinding.root)

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        val detail: StoryResult = intent.getSerializableExtra("detail") as StoryResult

        //GET DATA FORM ARGUMENT
        val name = detail.name
        val photo = detail.photoUrl
        val desc = detail.description

        detailPagingBinding.tvItemName.text = name
        detailPagingBinding.tvDescription.text = desc
        Glide.with(this)
            .load(photo)
            .into(detailPagingBinding.circleImageView)

        detailPagingBinding.btnBack.setOnClickListener {
            val intent = Intent(this@DetailPagingActivity, PagingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}