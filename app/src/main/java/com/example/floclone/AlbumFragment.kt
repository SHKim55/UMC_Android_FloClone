package com.example.floclone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.floclone.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment () {

    lateinit var binding: FragmentAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var sign = 0

        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        binding.btnAlbumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        /* Toast 메세지 기능 추후 적용
        binding.albumBackIv.setOnClickListener {
            Toast.makeText(activity, "라일락", Toast.LENGTH_SHORT).show()
        }
        */

        binding.toggleFavorMixIv.setOnClickListener {
            if(sign != 0) {
                setToggle(true)
                sign = 0
            }
            else {
                setToggle(false)
                sign = 1
            }
        }
        return binding.root
    }

    fun setToggle(isOn : Boolean) {
        if(isOn)
            binding.toggleFavorMixIv.setBackgroundResource(R.drawable.btn_toggle_off)
        else
            binding.toggleFavorMixIv.setBackgroundResource(R.drawable.btn_toggle_on)
    }
}