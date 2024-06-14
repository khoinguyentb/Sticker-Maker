package com.kan.dev.st051_stickermaker.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kan.dev.st051_stickermaker.base.BaseFragment
import com.kan.dev.st051_stickermaker.databinding.FragmentPackageBinding
import com.kan.dev.st051_stickermaker.viewModel.StickerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PackageFragment : BaseFragment<FragmentPackageBinding>() {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPackageBinding {
        return FragmentPackageBinding.inflate(layoutInflater)
    }

    private val viewModel : StickerViewModel by viewModels ()
    override fun initData() {

    }

    override fun initView() {
        
    }

    override fun initListener() {
        
    }

}