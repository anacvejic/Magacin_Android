package com.example.magacin.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.TypeOfProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTypeOfProductViewModel @Inject constructor(
    val magacinDao: MagacinDao,
    val state: SavedStateHandle
) : ViewModel() {

    private val editEventChannel = Channel<EditTypeOfProductEvent>()
    val editEvent = editEventChannel.receiveAsFlow()

    val typeofproduct = state.get<TypeOfProduct>("typeOfProduct")

    var productId = state.get<Int>("productId") ?: typeofproduct?.productId ?: ""
        set(value) {
            field = value
            state.set("productId", value)
        }
    var price = state.get<String>("price") ?: typeofproduct?.price ?: ""
        set(value) {
            field = value
            state.set("price", value)
        }
    var priceOfTypeProduct = state.get<String>("amount") ?: typeofproduct?.priceOfTypeProduct ?: ""
        set(value) {
            field = value
            state.set("amount", value)
        }
    var invoicenumber = state.get<Int>("invoiceNumber") ?: typeofproduct?.invoiceNumber ?: null
        set(value) {
            field = value
            state.set("invoiceNumber", value)
        }
    var created = state.get<Long>("created") ?: typeofproduct?.created ?: null
        set(value) {
            field = value
            state.set("created", value)
        }
    var idProductType = state.get<Int>("idProductType") ?: typeofproduct?.idProductType ?: null
        set(value) {
            field = value
            state.set("idProductType", value)
        }

    fun updateTypeOfProduct(typeOfProduct: TypeOfProduct) = viewModelScope.launch {
        magacinDao.updateTypeOfProduct(typeOfProduct)
        showSuccessMessage("Uspešno ste izmenili proizvod!")
    }

    private fun showSuccessMessage(msg: String) = viewModelScope.launch {
        editEventChannel.send(EditTypeOfProductEvent.ShowSuccessMessage(msg))
    }
}

sealed class EditTypeOfProductEvent {
    data class ShowSuccessMessage(val msg: String) : EditTypeOfProductEvent()
}