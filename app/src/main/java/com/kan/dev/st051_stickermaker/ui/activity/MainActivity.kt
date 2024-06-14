package com.kan.dev.st051_stickermaker.ui.activity

import androidx.fragment.app.Fragment
import com.kan.dev.st051_stickermaker.R
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.databinding.ActivityMainBinding
import com.kan.dev.st051_stickermaker.ui.fragment.HomeFragment
import com.kan.dev.st051_stickermaker.ui.fragment.PackageFragment
import com.kan.dev.st051_stickermaker.ui.fragment.SettingFragment
import com.kan.dev.st051_stickermaker.ui.fragment.StickerFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun setViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initData() {
        
    }

    override fun initView() {
        replaceFragment(
            HomeFragment(),
            "Calculator",
            R.drawable.home_selector,
            R.drawable.sticker,
            R.drawable.pakage,
            R.drawable.setting
        )

    }

    override fun initListener() {
        binding.apply {
            Home.setOnClickListener { v ->
                replaceFragment(
                    HomeFragment(),
                    "Home",
                    R.drawable.home_selector,
                    R.drawable.sticker,
                    R.drawable.pakage,
                    R.drawable.setting
                )
            }
            Sticker.setOnClickListener { v ->
                replaceFragment(
                    StickerFragment(),
                    "Sticker",
                    R.drawable.home,
                    R.drawable.sticker_selector,
                    R.drawable.pakage,
                    R.drawable.setting
                )
            }
            Package.setOnClickListener { v ->
                replaceFragment(
                    PackageFragment(),
                    "Settings",
                    R.drawable.home,
                    R.drawable.sticker,
                    R.drawable.pakage_selector,
                    R.drawable.setting
                )
            }
            Setting.setOnClickListener { v ->
                replaceFragment(
                    SettingFragment(),
                    "About",
                    R.drawable.home,
                    R.drawable.sticker,
                    R.drawable.pakage,
                    R.drawable.setting_selector
                )
            }
        }

    }

    fun replaceFragment(
        fragment: Fragment?,
        tag: String?,
        img1: Int,
        img2: Int,
        img3: Int,
        img4: Int
    ) {
        val transaction = supportFragmentManager.beginTransaction()
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        binding.icHome.setImageResource(img1)
        binding.icSticker.setImageResource(img2)
        binding.icPackage.setImageResource(img3)
        binding.icSetting.setImageResource(img4)
        if (existingFragment != null) {
            val fragments = supportFragmentManager.fragments
            if (fragments != null) {
                for (fragmentInStack in fragments) {
                    transaction.hide(fragmentInStack!!)
                }
            }
            transaction.show(existingFragment)
        } else {
            transaction.add(R.id.Frame, fragment!!, tag)
        }
        transaction.addToBackStack(tag)
        transaction.commit()
    }

}