package com.kan.dev.st051_stickermaker.ui.activity

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.data.data.Companion.tutorialList
import com.kan.dev.st051_stickermaker.databinding.ActivityIntroBinding
import com.kan.dev.st051_stickermaker.ui.adapter.IntroAdapter
import com.kan.dev.st051_stickermaker.utils.CHECK_PERMISSION
import com.kan.dev.st051_stickermaker.utils.LOG_APP
import com.kan.dev.st051_stickermaker.utils.handler
import com.kan.dev.st051_stickermaker.utils.isClick

class IntroActivity : BaseActivity<ActivityIntroBinding>() {
    override fun setViewBinding(): ActivityIntroBinding {
        return ActivityIntroBinding.inflate(layoutInflater)
    }
    private val adapter : IntroAdapter by lazy {
        IntroAdapter()
    }
    override fun initData() {
        adapter.setItems(tutorialList)
        binding.apply {

        }
    }

    override fun initView() {
        binding.apply {
            vpTutorial.adapter = adapter
            wormDotsIndicator.attachTo(vpTutorial)
        }
    }

    override fun initListener() {
        binding.apply {

            vpTutorial.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.apply {
                        when(position){
                            0->{
                                title.setText(R.string.title_intro1)
                                des.setText(R.string.des_intro1)
                                title.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.blue78CFCE))
                                stvNext.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.blue78CFCE))
                                root.setBackgroundResource(R.drawable.bg_intro_1)
                                wormDotsIndicator.setDotIndicatorColor(ContextCompat.getColor(this@IntroActivity,R.color.blue78CFCE))
                            }
                            1->{title.setText(R.string.title_intro2)
                                des.setText(R.string.des_intro2)
                                title.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.pinkF599B8))
                                stvNext.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.pinkF599B8))
                                root.setBackgroundResource(R.drawable.bg_intro_2)
                                wormDotsIndicator.setDotIndicatorColor(ContextCompat.getColor(this@IntroActivity,R.color.pinkF599B8))
                            }
                            2->{
                                title.setText(R.string.title_intro3)
                                des.setText(R.string.des_intro3)
                                title.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.yellow))
                                stvNext.setTextColor(ContextCompat.getColor(this@IntroActivity,R.color.yellow))
                                root.setBackgroundResource(R.drawable.bg_intro_3)
                                wormDotsIndicator.setDotIndicatorColor(ContextCompat.getColor(this@IntroActivity,R.color.yellow))
                            }
                        }
                    }
                }
            })

            stvNext.setOnClickListener{
                if (isClick) {
                    isClick = false
                    val page: Int = vpTutorial.getCurrentItem()
                    if (page == 2) {
//                        intent = if (isFirstApp()){
//                            Intent(this@IntroActivity, MainActivity::class.java)
//                        }else if (isPer()){
//                            Intent(this@IntroActivity,InteractionActivity::class.java)
//                        }else{
//                            Intent(this@IntroActivity,PermissionActivity::class.java)
//                        }
                        intent = Intent(this@IntroActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        vpTutorial.currentItem = page + 1
                    }
                    handler.postDelayed(Runnable { isClick = true }, 500)
                }
            }
        }
    }

    private fun isFirstApp() :Boolean{
        return sharePre.getBoolean(LOG_APP,false);
    }

    private fun isPer() :Boolean{
        return sharePre.getBoolean(CHECK_PERMISSION,false);
    }
}