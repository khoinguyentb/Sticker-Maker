package com.kan.dev.st051_stickermaker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kan.dev.st051_stickermaker.api.StickerApiService
import com.kan.dev.st051_stickermaker.data.model.StickerModel
import com.kan.dev.st051_stickermaker.data.model.TypeStickerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StickerViewModel @Inject constructor(
    private val apiService: StickerApiService
) : ViewModel() {

    private val _typeStickers = MutableLiveData<List<TypeStickerModel>>()
    val typeStickers: LiveData<List<TypeStickerModel>> get() = _typeStickers
    init {
        fetchTypeStickers()
    }

    private fun fetchTypeStickers() {
        viewModelScope.launch {
            val response = apiService.getSticker()
            if (response.isSuccessful) {
                val stickers = response.body()
                _typeStickers.postValue(stickers!!)

                Log.d("KanMobile",stickers.size.toString())
            } else {
                Log.e("StickerViewModel", "Failed to fetch beard stickers: ${response.code()}")
            }
        }
    }

}
