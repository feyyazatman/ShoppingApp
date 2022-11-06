package com.feyyazatman.shoppingapp.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.feyyazatman.shoppingapp.R
import com.feyyazatman.shoppingapp.main.MainActivity
import com.feyyazatman.shoppingapp.ui.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val onBoarding : SharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)
        val isFirstTime = onBoarding.getBoolean("firstTime", true)


        supportActionBar?.hide()
        activityScope.launch {
            delay(2000)

            if (isFirstTime) {
                val editor = onBoarding.edit()
                editor.putBoolean("firstTime", false)
                editor.apply()

                val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}