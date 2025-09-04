package com.example.vandrservices.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.vandrservices.R
import com.example.vandrservices.ui.MainActivity
import com.example.vandrservices.ui.form.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var usernameEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var loginBtn: Button
    private lateinit var errorText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEdit = findViewById(R.id.editTextUsername)
        passwordEdit = findViewById(R.id.editTextPassword)
        loginBtn = findViewById(R.id.buttonLogin)
        errorText = findViewById(R.id.textViewError)
        progressBar = findViewById(R.id.progressBarLogin)

        // Al iniciar, intenta login automático con primer usuario (si existe)
        autoLoginIfExists()

        loginBtn.setOnClickListener {
            errorText.visibility = View.GONE
            setLoading(true)
            val username = usernameEdit.text.toString()
            val password = passwordEdit.text.toString()
            viewModel.login(username, password)
        }

        viewModel.loginResult.observe(this) { success ->
            setLoading(false)
            if (success) {
                val token = viewModel.token.value ?: ""
                viewModel.updateFirstUserToken(token)
                goToMain()
            } else {
                errorText.text = "No se pudo autenticar usuario"
                errorText.visibility = View.VISIBLE
            }
        }
    }

    // Intenta login con el primer usuario guardado
    private fun autoLoginIfExists() {
        setLoading(true)
        lifecycleScope.launch {
            val user = viewModel.usersFlow.firstOrNull()?.firstOrNull()
            Log.d("LoginActivity", "User: $user")
            usernameEdit.setText(user?.name)
            passwordEdit.setText(user?.password)
            if (user != null) {
                val success = viewModel.loginAuto(user.name, user.password)
                setLoading(false)
                if (success) {
                    val token = viewModel.token.value ?: ""
                    viewModel.updateFirstUserToken(token)
                    Log.d("LoginActivity", "Token: $token")
                    goToMain()
                } else {
                    if (user.token != "") {
                        goToMain()
                    }
                    Log.d("LoginActivity", "Login failed")
                }
            } else {
                setLoading(false)
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        usernameEdit.isEnabled = !show
        passwordEdit.isEnabled = !show
        loginBtn.isEnabled = !show
    }
}