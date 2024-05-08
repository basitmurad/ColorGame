package com.example.colorgame

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        lottieAnimationView.playAnimation()

        lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                // Animation started
            }

            override fun onAnimationEnd(animation: Animator) {
                // Animation ended, navigate to the next activity
                // For example: startActivity(Intent(this@SplashActivity, NextActivity::class.java))
            }

            override fun onAnimationCancel(animation: Animator) {
                // Animation cancelled
            }

            override fun onAnimationRepeat(animation: Animator) {
                // Animation repeated
            }
        })


        android.os.Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 2000)
    }
}