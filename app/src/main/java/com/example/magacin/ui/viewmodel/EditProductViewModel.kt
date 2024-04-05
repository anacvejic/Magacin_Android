package com.example.magacin.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TempProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    val magacinDao: MagacinDao,
    val state: SavedStateHandle
) : ViewModel() {

    private val editEventChannel = Channel<EditProductEvent>()
    val editEvent = editEventChannel.receiveAsFlow()

    val tempProduct = state.get<TempProduct>("tempProduct")

    var idProduct = state.get<Int>("price") ?: tempProduct?.idProduct ?: null
        set(value) {
            field = value
            state.set("idProduct", value)
        }
    var productName = state.get<String>("productName") ?: tempProduct?.productName ?: ""
        set(value) {
            field = value
            state.set("productName", value)
        }
    var productCode = state.get<String>("productCode") ?: tempProduct?.productCode ?: ""
        set(value) {
            field = value
            state.set("productCode", value)
        }

    fun updateProduct(product: Product) = viewModelScope.launch {
        magacinDao.updateProduct(product)
        showSuccessMessage("Uspešno ste izmenili proizvod!")
    }

    private fun showSuccessMessage(msg: String) = viewModelScope.launch {
        editEventChannel.send(EditProductEvent.ShowSuccessMessage(msg))
    }
}

sealed class EditProductEvent {
    data class ShowSuccessMessage(val msg: String) : EditProductEvent()
}