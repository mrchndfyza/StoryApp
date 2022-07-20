package com.greentea.storyapp2.services.models

data class LoginResult(
    var nama: String? = null,
    var token: String? = null,
    var userId: String? = null,
    var isLogin: Boolean = false
)