package com.kan.dev.st051_stickermaker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kan.dev.st051_stickermaker.data.model.StickerPackageModel
import com.kan.dev.st051_stickermaker.repository.StickerPackageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackageViewModel @Inject constructor(
    private val repository: StickerPackageRepository
) :ViewModel() {

    private val _getAll = MutableLiveData<List<StickerPackageModel>>()
    val getAll: LiveData<List<StickerPackageModel>> = _getAll

    private val _getListPackage = MutableLiveData<List<String>>()
    val getListPackage: LiveData<List<String>> = _getListPackage
    init {
        getAll()
        getListPackage
    }
    private fun getAll() {
        viewModelScope.launch {
            repository.getAll().collect {
                _getAll.value = it
            }
        }
    }

    fun getListPackage() {
        viewModelScope.launch {
            repository.getListPackageName().collect {
                _getListPackage.value = it
            }
        }
    }
    fun getBmi(id: Int) {
        viewModelScope.launch {
            repository.getItem(id)
        }
    }

    fun insert(stickerPackageModel: StickerPackageModel) {
        viewModelScope.launch {
            repository.insert(stickerPackageModel)
        }
    }

    fun update(stickerPackageModel: StickerPackageModel) {
        viewModelScope.launch {
            repository.update(stickerPackageModel)
        }
    }

    fun delete(stickerPackageModel: StickerPackageModel) {
        viewModelScope.launch {
            repository.delete(stickerPackageModel)
        }
    }

}