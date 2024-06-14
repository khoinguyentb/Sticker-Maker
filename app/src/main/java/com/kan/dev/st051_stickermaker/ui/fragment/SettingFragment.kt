package com.kan.dev.st051_stickermaker.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseFragment
import com.kan.dev.st051_stickermaker.data.data.Companion.settingList
import com.kan.dev.st051_stickermaker.databinding.FragmentSettingBinding
import com.kan.dev.st051_stickermaker.dialog.DialogRate
import com.kan.dev.st051_stickermaker.dialog.OnDialogDismissListener
import com.kan.dev.st051_stickermaker.interfaces.ISettingClickListener
import com.kan.dev.st051_stickermaker.ui.activity.LanguageActivity
import com.kan.dev.st051_stickermaker.ui.activity.VersionActivity
import com.kan.dev.st051_stickermaker.ui.adapter.SettingAdapter
import com.kan.dev.st051_stickermaker.utils.handler
import com.kan.dev.st051_stickermaker.utils.isClick
import com.kan.dev.st051_stickermaker.utils.openPolicy
import com.kan.dev.st051_stickermaker.utils.shareApp


class SettingFragment : BaseFragment<FragmentSettingBinding>(),ISettingClickListener,OnDialogDismissListener {
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingBinding {
        return FragmentSettingBinding.inflate(layoutInflater)
    }


    private val adapter : SettingAdapter by lazy {
        SettingAdapter(this)
    }
    private val layoutManager : RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
    }
    private lateinit var intent: Intent
    private lateinit var dialog : DialogRate
    override fun initData() {
        adapter.setItems(settingList)
        if (sharePre.isRated(requireActivity())){
            adapter.removeItem(1)
        }
    }

    override fun initView() {
        binding.apply {
            rcvSetting.adapter = adapter
            rcvSetting.layoutManager = layoutManager
        }
    }

    override fun initListener() {
        binding.apply {
            version.setOnClickListener {
                if (isClick){
                    isClick = false
                    intent = Intent(requireContext(), VersionActivity::class.java)
                    startActivity(intent)
                    handler.postDelayed({ isClick = true},500)
                }
            }
        }
    }

    override fun iSettingClick(image: Int) {
        if (isClick){
            isClick = false
            when(image){
                R.drawable.ic_lang -> {
                    intent = Intent(requireContext(), LanguageActivity::class.java)
                    startActivity(intent)
                }
                R.drawable.rate -> {
                    dialog = DialogRate(requireActivity(),this)
                    dialog.show()
                }
                R.drawable.share -> {
                    shareApp(requireActivity())
                }
                R.drawable.policy -> {
                    openPolicy(requireActivity(),"https://sites.google.com/andesgroup.app/megamod-frank-video-call")
                }
            }
            handler.postDelayed({ isClick = true},500)
        }
    }

    override fun onDialogDismiss() {
        if (sharePre.isRated(requireActivity())){
            adapter.removeItem(1)
        }
        requireActivity().window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}