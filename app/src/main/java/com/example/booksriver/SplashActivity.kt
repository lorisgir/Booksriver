package com.example.booksriver

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.booksriver.session.SessionManager
import com.example.booksriver.ui.presentation.PresentationScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val intent = intent
        var bookId: Int? = null
        if (intent.data != null) {
            try {
                bookId = intent.data.toString().split("/").last().toInt()
            } catch (_: NumberFormatException) {
                //continue
            }
        } else if (intent.extras?.containsKey("bookId") == true) {
            bookId = intent.extras!!.getInt("bookId", 0)
        }


        if (sessionManager.getToken() != null) {
            lifecycleScope.launchWhenCreated {
                onNavigateToMainActivity(true, bookId)
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            setContent {
                PresentationScreen(onNavigateToMainActivity = {
                    onNavigateToMainActivity(
                        false,
                        bookId
                    )
                })
            }
        }
    }

    private fun onNavigateToMainActivity(isLogged: Boolean, bookId: Int? = null) {
        val b = Bundle()
        b.putBoolean("isUserLoggedIn", isLogged)
        if (bookId != null) {
            b.putInt("bookId", bookId)
        }
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        intent.putExtras(b)
        startActivity(intent)
        finish()
    }
}

