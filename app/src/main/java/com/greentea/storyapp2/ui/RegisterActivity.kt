package com.greentea.storyapp2.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.greentea.storyapp2.databinding.ActivityRegisterBinding
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding

    private lateinit var storyViewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        setupView()
        callingComponent()

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        registerBinding.cvDaftarButton.setOnClickListener {
            registerBinding.pbLoading.visibility = View.VISIBLE
            daftarForm()
        }

        registerBinding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun daftarForm() {
        val name = registerBinding.edtName.text.toString()
        val email = registerBinding.edtEmail.text.toString()
        val password = registerBinding.edtPassword.text.toString()

        storyViewModel.getRegisterUsersResponse(name, email, password)
        observeRegisterUsers()
    }

    private fun observeRegisterUsers() {
        storyViewModel.apiUsers.observe(this) { response ->
            if (response.isSuccessful) {
                if (response.body()?.error == false) {
                    Toast.makeText(this, "Pendaftaran Berhasil, harap Login", Toast.LENGTH_LONG)
                        .show()
                    registerBinding.pbLoading.visibility = View.INVISIBLE
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, response.body()?.message, Toast.LENGTH_LONG).show()
                    registerBinding.pbLoading.visibility = View.INVISIBLE
                }

            } else {
                Toast.makeText(this, "Pendaftaran gagal, email sudah terdaftar", Toast.LENGTH_LONG)
                    .show()
                registerBinding.pbLoading.visibility = View.INVISIBLE
                val intent = Intent(this@RegisterActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun callingComponent(){
        //calling edit text component (name)
        registerBinding.edtName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnabled()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        //calling edit text component (email)
        registerBinding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnabled()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        //calling edit text component (password)
        registerBinding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnabled()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setLoginButtonEnabled() {
        val resultName = registerBinding.edtName.text
        val resultEmail = registerBinding.edtEmail.text
        val resultPassword = registerBinding.edtPassword.text

        registerBinding.cvDaftarButton.isEnabled =
            (resultName != null && resultName.toString().isNotEmpty()) &&
                    resultEmail != null && resultEmail.toString().isNotEmpty() &&
                    (resultPassword != null && resultPassword.toString().isNotEmpty())
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}