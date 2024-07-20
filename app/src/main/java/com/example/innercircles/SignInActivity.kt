package com.example.innercircles

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.innercircles.api.data.SignInRequest
import com.example.innercircles.api.data.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.innercircles.api.RetrofitClient.apiService
import com.example.innercircles.api.data.SignUpRequest
import com.example.innercircles.api.data.SignUpResponse
import com.example.innercircles.utils.PreferencesHelper

class SignInActivity : AppCompatActivity() {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignIn: Button
    private lateinit var tvErrorMessage: TextView
    private lateinit var btnShowSignUp: Button
    private lateinit var llSignUpForm: LinearLayout
    private lateinit var etEmail: EditText
    private lateinit var etDisplayName: EditText
    private lateinit var etSignUpPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var preferencesHelper: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnSignIn = findViewById(R.id.btn_sign_in)
        tvErrorMessage = findViewById(R.id.tv_error_message)
        btnShowSignUp = findViewById(R.id.btn_show_sign_up)
        llSignUpForm = findViewById(R.id.ll_sign_up_form)
        etEmail = findViewById(R.id.et_email)
        etDisplayName = findViewById(R.id.et_display_name)
        etSignUpPassword = findViewById(R.id.et_sign_up_password)
        etConfirmPassword = findViewById(R.id.et_confirm_password)
        btnSignUp = findViewById(R.id.btn_sign_up)


        preferencesHelper = PreferencesHelper(this)

        // Check if user is already signed in
        if (preferencesHelper.getUserId() != null) {
            navigateToMainActivity()
            finish() // Close the sign-in activity
        }

        btnSignIn.setOnClickListener {
            signIn()
        }

        btnShowSignUp.setOnClickListener {
            llSignUpForm.visibility = View.VISIBLE
        }

        btnSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signIn() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        val request = SignInRequest(username, password)

        val call = apiService.authenticateUser(request)
        call.enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                if (response.isSuccessful) {
                    val userId = response.body()?.data?.id
                    if (userId != null) {
                        preferencesHelper.saveUserIdSecurely(userId)
                        saveUserId(userId)
                        Toast.makeText(this@SignInActivity, "Sign in successful!", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                        finish()
                    }
                } else {
                    showErrorMessage("Username or password is incorrect")
                    Toast.makeText(this@SignInActivity, "Sign in failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Log.e("com.example.innercircles.SignInActivity", "onFailure: ", t)
                Toast.makeText(this@SignInActivity, "An error occurred!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun signUp() {
        val email = etEmail.text.toString()
        val displayName = etDisplayName.text.toString()
        val password = etSignUpPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        if (password != confirmPassword) {
            showErrorMessage("Passwords do not match")
            return
        }

        val request = SignUpRequest(email, displayName, password, confirmPassword)

        val call = apiService.signUp(request)
        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val userId = response.body()?.data?.id
                    if (userId != null) {
                        preferencesHelper.saveUserIdSecurely(userId)
                        saveUserId(userId)
                        Toast.makeText(this@SignInActivity, "Sign in successful!", Toast.LENGTH_SHORT).show()
                        navigateToMainActivity()
                        finish()
                    }
                } else {
                    // Response was not successful
                    showErrorMessage("Sign up failed")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("SignInActivity", "onFailure: ", t)
                showErrorMessage("An error occurred!")
            }
        })
    }

    private fun saveUserId(userId: String) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", userId)
        editor.apply()
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


    private fun showErrorMessage(message: String) {
        tvErrorMessage.text = message
        tvErrorMessage.visibility = View.VISIBLE
    }
}