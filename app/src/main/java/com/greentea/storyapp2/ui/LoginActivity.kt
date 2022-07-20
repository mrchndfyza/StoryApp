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
import com.greentea.storyapp2.databinding.ActivityLoginBinding
import com.greentea.storyapp2.services.models.repository.StoryRepository
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.StoryViewModel
import com.greentea.storyapp2.viewmodel.factory.StoryViewModelProviderFactory
import com.greentea.storyapp2.viewmodel.preferences.UserPreference

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userPreference: UserPreference

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        setupView()
        callingComponent()

        //CREATE CONNECTION ->
        val storyViewModelFactory = StoryViewModelProviderFactory(StoryRepository())
        storyViewModel = ViewModelProvider(
            this, storyViewModelFactory
        )[StoryViewModel::class.java]

        userPreference = UserPreference(this)

        loginBinding.cvLoginButton.setOnClickListener {
            loginBinding.pbLoading.visibility = View.VISIBLE
            loginForm()
        }

        loginBinding.tvDaftarLink.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.killActivity()
    }

    override fun onStart() {
        super.onStart()
        if(userPreference.getSessionLogin(Constants.IS_LOGIN)){
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            killActivity()
        }
    }

    private fun loginForm() {
        val email = loginBinding.edtEmail.text.toString()
        val password = loginBinding.edtPassword.text.toString()

        storyViewModel.getLoginUsersResponse(email, password)
        observeLoginUsers()
    }

    private fun observeLoginUsers() {
        storyViewModel.loginUsers.observe(this) { response ->
            if(response.isSuccessful){
                if(response.body()?.error == false){
                    val responseNama = response.body()?.loginResult?.nama.toString()
                    val responseToken = response.body()?.loginResult?.token.toString()
                    val responseUserId = response.body()?.loginResult?.userId.toString()

                    saveLoginSession(
                        responseNama,
                        responseToken,
                        responseUserId
                    )

                    Toast.makeText(
                        this,
                        "Status : ${response.body()?.message}, Berhasil login",
                        Toast.LENGTH_LONG)
                        .show()
                    loginBinding.pbLoading.visibility = View.INVISIBLE

                    //INTENT TO HOME DIRECTLY
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                    killActivity()
                }
                else{
                    Toast.makeText(
                        this,
                        response.body()?.message,
                        Toast.LENGTH_LONG)
                        .show()
                    loginBinding.pbLoading.visibility = View.INVISIBLE
                }
            } else{
                Toast.makeText(this, "Login gagal, password/email salah", Toast.LENGTH_LONG).show()
                loginBinding.pbLoading.visibility = View.INVISIBLE
                val intent = Intent(this@LoginActivity, LoginActivity::class.java)
                startActivity(intent)
                killActivity()
            }
        }
    }

    private fun killActivity() {
        finish()
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

    private fun callingComponent(){
        //calling edit text component (email)
        loginBinding.edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnabled()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        //calling edit text component (password)
        loginBinding.edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setLoginButtonEnabled()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    private fun setLoginButtonEnabled(){
        val resultEmail = loginBinding.edtEmail.text
        val resultPassword = loginBinding.edtPassword.text

        loginBinding.cvLoginButton.isEnabled =
            (resultEmail != null && resultEmail.toString().isNotEmpty()) &&
                    (resultPassword != null && resultPassword.toString().isNotEmpty())
    }

    private fun saveLoginSession(name: String, token: String, userId: String){
        userPreference.putDataLogin(Constants.NAME, name)
        userPreference.putDataLogin(Constants.TOKEN, token)
        userPreference.putDataLogin(Constants.USER_ID, userId)
        userPreference.putSessionLogin(Constants.IS_LOGIN, true)
    }
}