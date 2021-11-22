package com.example.floclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator

class LockerFragment : Fragment() {
    lateinit var binding: FragmentLockerBinding
    val information = arrayListOf("저장한 곡", "저장한 앨범", "음악파일")
    val songs = ArrayList<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this, getJwtId(), songs)
        binding.lockerContentVp.adapter = lockerAdapter

        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) { tab, position ->
            tab.text = information[position]
        }.attach()

        binding.lockerLoginTv.setOnClickListener { startActivity(Intent(activity, LoginActivity::class.java)) }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView() {
        val jwtId = getJwtId()
        val jwtName : String = getJwtName() + " 님"

        if(jwtId == 0) {
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener { startActivity(Intent(activity, LoginActivity::class.java)) }
        }
        else {
            binding.lockerLoginTv.text = "로그아웃"
            binding.lockerLoginTv.setOnClickListener {
                logOut()
                startActivity(Intent(activity, MainActivity::class.java))
            }

            binding.lockerUsernameTv.text = jwtName
        }
    }

    private fun getJwtId() : Int {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return sharedPreference!!.getInt("jwtId", 0)
    }

    private fun getJwtName() : String? {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return sharedPreference?.getString("jwtName", "홍길동")
    }

    private fun logOut() {
        val sharedPreference = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreference!!.edit()
        editor.remove("jwtId")
        editor.remove("jwtName")
        editor.apply()
    }
}