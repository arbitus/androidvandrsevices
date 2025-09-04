package com.example.vandrservices

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.vandrservices.databinding.ActivitySplashBinding
import com.example.vandrservices.ui.MainActivity
import com.example.vandrservices.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logoImage)
        val animation = AnimationUtils.loadAnimation(this, R.anim.translate_up)
        logo.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            // Lanza la siguiente actividad
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000) // Espera 2 segundos
    }
}