package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.floclone.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    private var passwordHidden : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignUpTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginLoginTv.setOnClickListener() { logIn() }
        binding.loginHidePasswordIv.setOnClickListener { hidePassword() }
    }

    private fun logIn() {
        if(binding.loginIdEt.text.toString().isEmpty() || binding.loginDirectInputEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(binding.loginPasswordEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email : String = binding.loginIdEt.text.toString() + "@" + binding.loginDirectInputEt.text.toString()
        val password : String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.UserDao().getUser(email, password)

        user?.let {     //null이 아닐 때 실행
            Log.d("LOGIN USER", "userID : ${user.id}, $user")

            // SharedPreference에 로그인 정보 저장
            saveJwt(user.id, user.name)
            startMainActivity()
            return
        }

        Toast.makeText(this, "회원 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun saveJwt(jwtId : Int, jwtName : String) {
        val sharedPreference = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = sharedPreference.edit()

        editor.putInt("jwtId", jwtId)
        editor.putString("jwtName", jwtName)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun hidePassword() {
        if (passwordHidden) {
            binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password_off)
            binding.loginPasswordEt.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordHidden = false
        } else {
            binding.loginHidePasswordIv.setImageResource(R.drawable.btn_input_password)
            binding.loginPasswordEt.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD  // 작동하지 않음
            passwordHidden = true
        }
    }
}