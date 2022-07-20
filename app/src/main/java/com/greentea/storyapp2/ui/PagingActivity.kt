package com.greentea.storyapp2.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentea.storyapp2.databinding.ActivityPagingBinding
import com.greentea.storyapp2.ui.adapter.LoadingStateAdapter
import com.greentea.storyapp2.ui.adapter.PagingListAdapter
import com.greentea.storyapp2.viewmodel.PagingViewModel
import com.greentea.storyapp2.viewmodel.ViewModelFactory

class PagingActivity : AppCompatActivity() {
    //BINDING
    private lateinit var pagingBinding: ActivityPagingBinding

    private val pagingViewModel: PagingViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagingBinding = ActivityPagingBinding.inflate(layoutInflater)
        setContentView(pagingBinding.root)

        pagingBinding.rvPagingList.layoutManager = LinearLayoutManager(this)

        getData()

    }

    private fun getData() {
        val adapter = PagingListAdapter()
        pagingBinding.rvPagingList.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        pagingViewModel.paging.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickListener {
            val intent = Intent(this, DetailPagingActivity::class.java)
            intent.putExtra("detail", it)
            startActivity(intent)
            finish()
        }
    }
}