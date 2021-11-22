package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding : ActivitySignUpBinding
    private var passwordHidden : Boolean = true
    private var passwordCheckHidden : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initOnClickListener()
    }

    private fun initOnClickListener() {
        binding.signUpSignUpBtn.setOnClickListener {
            signUp()
            finish()
        }

        binding.signUpHidePasswordIv.setOnClickListener { hidePassword() }
        binding.signUpHidePasswordCheckIv.setOnClickListener { hidePasswordCheck() }
    }

    private fun getUser() : User {
        val email : String = binding.signUpIdEt.text.toString() + "@" + binding.signUpDirectInputEt.text.toString()
        val password : String = binding.signUpPasswordEt.text.toString()
        val name : String = binding.signUpNameEt.text.toString()

        return User(email, password, name)
    }

    private fun signUp() {
        if(binding.signUpIdEt.text.toString().isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.UserDao().insert(getUser())

        val users = userDB.UserDao().getUsers()
        Log.d("Users", users.toString())
    }

    private fun hidePassword() {
        if(passwordHidden) {
            binding.signUpHidePasswordIv.setImageResource(R.drawable.btn_input_password_off)
            binding.signUpPasswordEt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordHidden = false
        } else {
            binding.signUpHidePasswordIv.setImageResource(R.drawable.btn_input_password)
            binding.signUpPasswordEt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordHidden = true
        }
    }

    private fun hidePasswordCheck() {
        if(passwordCheckHidden) {
            binding.signUpHidePasswordCheckIv.setImageResource(R.drawable.btn_input_password_off)
            binding.signUpPasswordCheckEt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordCheckHidden = false
        } else {
            binding.signUpHidePasswordCheckIv.setImageResource(R.drawable.btn_input_password)
            binding.signUpPasswordCheckEt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordCheckHidden = true
        }
    }
}