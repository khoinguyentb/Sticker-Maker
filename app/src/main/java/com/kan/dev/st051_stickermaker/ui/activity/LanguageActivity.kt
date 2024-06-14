package com.kan.dev.st051_stickermaker.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kan.dev.st051_stickermaker.base.BaseActivity
import com.kan.dev.st051_stickermaker.data.data.Companion.languageList
import com.kan.dev.st051_stickermaker.data.model.LanguageModel
import com.kan.dev.st051_stickermaker.databinding.ActivityLanguageBinding
import com.kan.dev.st051_stickermaker.interfaces.ILanguageClick
import com.kan.dev.st051_stickermaker.ui.adapter.LanguageAdapter
import com.kan.dev.st051_stickermaker.utils.IS_LANGUAGE
import com.kan.dev.st051_stickermaker.utils.handler
import com.kan.dev.st051_stickermaker.utils.isClick


class LanguageActivity : BaseActivity<ActivityLanguageBinding>(),ILanguageClick {
    override fun setViewBinding(): ActivityLanguageBinding {
        return ActivityLanguageBinding.inflate(layoutInflater)
    }

    private lateinit var code : String
    private val adapter : LanguageAdapter by lazy {
        LanguageAdapter(this)
    }
    private val layoutManager : RecyclerView.LayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    override fun initData() {
        initListLanguage()
        code = getCodeLang()
        binding.apply {
            adapter.setItems(languageList)
            adapter.setCheck(code)
            rcvLanguge.layoutManager = layoutManager
            rcvLanguge.adapter = adapter
        }
    }

    override fun initView() {
        
    }

    override fun initListener() {
        binding.apply {
            Done.setOnClickListener {
                if (isClick){
                    isClick = false
                    setLang(code)
                    if (isLanguage()){
                        intent = Intent(this@LanguageActivity, MainActivity::class.java)
                    }else{
                        isFirstLang()
                        intent = Intent(this@LanguageActivity, IntroActivity::class.java)
                    }
                    startActivity(intent)
                    finishAffinity()
                    handler.postDelayed({isClick = true},500)
                }
            }
        }
    }

    fun initListLanguage(): List<LanguageModel>{
        val codeLang = getCodeLang()
        val listLanguage = languageList
        for (i in 0 until listLanguage.size){
            if (listLanguage[i].codeLang == codeLang) {
                val selectedLanguage = listLanguage[i]
                listLanguage.removeAt(i)
                listLanguage.add(0, selectedLanguage)
                break
            }
        }
        return listLanguage
    }
    fun getCodeLang() : String{
        return systemUtils.getPreLanguage()
    }
    fun setLang(code : String){
        systemUtils.setPreLanguage(code)
    }

    fun isLanguage():Boolean{
        return sharePre.getBoolean(IS_LANGUAGE,false)
    }
    fun isFirstLang(){
        sharePre.putBoolean(IS_LANGUAGE,true)
    }

    override fun onClickItem(code: String) {
        this.code = code
    }
}