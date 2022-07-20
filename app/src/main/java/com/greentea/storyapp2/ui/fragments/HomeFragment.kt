package com.greentea.storyapp2.ui.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentea.storyapp2.R
import com.greentea.storyapp2.databinding.FragmentHomeBinding
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.ui.DetailPagingActivity
import com.greentea.storyapp2.ui.adapter.ListStoryUsersAdapter
import com.greentea.storyapp2.ui.adapter.LoadingStateAdapter
import com.greentea.storyapp2.ui.adapter.PagingListAdapter
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.utils.Resources
import com.greentea.storyapp2.viewmodel.PagingViewModel
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.ViewModelFactory
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory
import com.greentea.storyapp2.viewmodel.preferences.UserPreference

class HomeFragment : Fragment() {
    //BINDING
    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val frHomeBinding get() = _binding!!

    private val tag1 = "HomeFragment"
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var listStoryUsersAdapter: ListStoryUsersAdapter
    private lateinit var userPreference: UserPreference

    private val pagingViewModel: PagingViewModel by viewModels {
        ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return frHomeBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        configRecyclerView()

//        listStoryUsersAdapter.setOnItemClickListener {
//            val bundle = Bundle().apply { putSerializable("detail", it) }
//            findNavController().navigate(R.id.action_homeFragment_to_detailActivity, bundle)
//        }

        userPreference = UserPreference(requireContext())
        val tokenFromPreferences = userPreference.getDataLogin(Constants.TOKEN)
        val realToken = "Bearer $tokenFromPreferences"

        storyViewModel.getListStoryUsers(realToken)
//        showProgressBar()
//        observerListStoryUser()

        getData()

    }

    private fun getData() {
        val adapter = PagingListAdapter()
        frHomeBinding.rvListStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        pagingViewModel.paging.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }

        adapter.setOnItemClickListener {
            val bundle = Bundle().apply { putSerializable("detail", it) }
            findNavController().navigate(R.id.action_homeFragment_to_detailActivity, bundle)
        }
    }

    private fun observerListStoryUser() {
        storyViewModel.listStoryUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        listStoryUsersAdapter.differAsync.submitList(userResponse.listStory)
                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(tag1, "An error occurred: $message")
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun showProgressBar() {
        frHomeBinding.pbListUser.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        frHomeBinding.pbListUser.visibility = View.INVISIBLE
    }

    private fun configRecyclerView() {
        listStoryUsersAdapter = ListStoryUsersAdapter(requireContext())
        frHomeBinding.rvListStory.apply {
            adapter = listStoryUsersAdapter
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(activity, 2)
                } else {
                    LinearLayoutManager(activity)
                }
        }
    }

}