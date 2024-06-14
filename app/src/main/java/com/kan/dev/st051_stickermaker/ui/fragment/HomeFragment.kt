package com.kan.dev.st051_stickermaker.ui.fragment

import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseFragment
import com.kan.dev.st051_stickermaker.data.data.Companion.homeList
import com.kan.dev.st051_stickermaker.databinding.FragmentHomeBinding
import com.kan.dev.st051_stickermaker.interfaces.IHomeClickListener
import com.kan.dev.st051_stickermaker.ui.activity.PhotoActivity
import com.kan.dev.st051_stickermaker.ui.activity.SelectorTextStickerActivity
import com.kan.dev.st051_stickermaker.ui.adapter.HomeAdapter


class HomeFragment : BaseFragment<FragmentHomeBinding>(), IHomeClickListener {
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    private val adapter: HomeAdapter by lazy {
        HomeAdapter(requireActivity(),this)
    }
    private lateinit var intent : Intent
    private val layoutManager: RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }

    override fun initData() {
        adapter.setItems(homeList)
    }

    override fun initView() {
        binding.apply {
            stvSticker.setStroke(
                2.5f,
                ContextCompat.getColor(requireContext(), R.color.strock),
                Paint.Join.ROUND,
                5f
            )
            rcvHome.adapter = adapter
            rcvHome.layoutManager = layoutManager
        }
    }

    override fun initListener() {

    }

    override fun isClickListener(position: Int) {
        when(position){
            0 -> {
                intent = Intent(requireContext(),PhotoActivity::class.java)
            }
            1 -> {
                intent = Intent(requireContext(),SelectorTextStickerActivity::class.java)
            }
            2 -> {
                intent = Intent(requireContext(),PhotoActivity::class.java)
            }
            3 -> {
                intent = Intent(requireContext(),PhotoActivity::class.java).putExtra("Video",true)
            }
        }
        startActivity(intent)
    }
}