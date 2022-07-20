package com.greentea.storyapp2.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.greentea.storyapp2.databinding.ActivityDetailBinding
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory

class DetailActivity : AppCompatActivity() {
    //BINDING
    private lateinit var detailBinding: ActivityDetailBinding

    private lateinit var storyViewModel: StoryViewModel

    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        val detail = args.detail

        //GET DATA FORM ARGUMENT
        val name = detail.name
        val photo = detail.photoUrl
        val desc = detail.description

        detailBinding.tvItemName.text = name
        detailBinding.tvDescription.text = desc
        Glide.with(this)
            .load(photo)
            .into(detailBinding.circleImageView)

        detailBinding.btnBack.setOnClickListener {
            val intent = Intent(this@DetailActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}