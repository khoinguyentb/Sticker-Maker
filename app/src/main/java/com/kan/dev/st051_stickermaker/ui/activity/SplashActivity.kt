package com.kan.dev.st051_stickermaker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.databinding.ActivitySplashBinding
import com.kan.dev.st051_stickermaker.utils.INDEX_LOG_APP
import com.kan.dev.st051_stickermaker.utils.IS_LANGUAGE
import com.kan.dev.st051_stickermaker.utils.handler
import com.kan.dev.st051_stickermaker.viewModel.StickerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
    lateinit var runnable: Runnable
    private var index = 0;
    override fun initData() {
        index = sharePre.getInt(INDEX_LOG_APP,0)
        index++
        sharePre.putInt(INDEX_LOG_APP,index)
    }

    override fun initView() {
        binding.apply {

        }
    }

    override fun initListener() {

    }

    override fun onStart() {
        super.onStart()
        runnable = Runnable{
            startActivity()
        }
        handler.postDelayed(runnable,2000)
    }
    private fun startActivity(){
        intent = if (isLanguage()){
            Intent(this, IntroActivity::class.java)
        }else {
            Intent(this, LanguageActivity::class.java)
        }
        intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun isLanguage() : Boolean{
        return sharePre.getBoolean(IS_LANGUAGE,false)
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(runnable)
    }
}