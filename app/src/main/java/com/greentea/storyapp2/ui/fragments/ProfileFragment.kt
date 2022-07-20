package com.greentea.storyapp2.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.preferences.UserPreference
import com.greentea.storyapp2.databinding.FragmentProfileBinding
import com.greentea.storyapp2.ui.LoginActivity

class ProfileFragment : Fragment() {
    //BINDING
    private var _binding: FragmentProfileBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val frProfileBinding get() = _binding!!

    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return frProfileBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference(requireContext())

        val token = userPreference.getDataLogin(Constants.TOKEN)
        val id = userPreference.getDataLogin(Constants.USER_ID)

        frProfileBinding.tvToken.text = token
        frProfileBinding.tvId.text = id

        frProfileBinding.cvLogoutButton.setOnClickListener {
            userPreference.clear()
            Toast.makeText(requireContext(), "Berhasil Logout", Toast.LENGTH_SHORT).show()

            val mIntent = Intent(requireActivity(), LoginActivity::class.java)
            killActivity()
            startActivity(mIntent)
        }
    }

    private fun killActivity(){
        activity?.finish()
    }

}