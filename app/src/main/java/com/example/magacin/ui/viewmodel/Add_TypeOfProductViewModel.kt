package com.example.magacin.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TypeOfProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class Add_TypeOfProductViewModel @Inject constructor(
    private val magacinDao: MagacinDao,
    private val state: SavedStateHandle
) : ViewModel() {

    val product = state.get<Product>("product")
    var result: Int = 0

    private val addTypeOfProductEventChannel = Channel<AddTypeOfProductEvent>()
    val addTypeOfProductEvent = addTypeOfProductEventChannel.receiveAsFlow()

    var idPorduct = state.get<Int>("idProduct") ?: product?.idProduct ?: ""
        set(value) {
            field = value
            state["idProduct"] = value
        }

    var productName = state.get<String>("productName") ?: product?.productName ?: ""
        set(value) {
            field = value
            state["productName"] = value
        }

    var productCode = state.get<String>("productCode") ?: product?.productCode ?: ""
        set(value) {
            field = value
            state["productCode"] = value
        }
    val getMaxInvoiceNumber: LiveData<Int> = magacinDao.getMaxInvoiceNUmber()

    fun createTypeOfProduct(typeOfProduct: TypeOfProduct) = viewModelScope.launch {
        magacinDao.insertProductType(typeOfProduct)
        showMessageDataSaved("Uneli ste novi tip proizvoda!")
    }

    private fun showMessageDataSaved(text: String) = viewModelScope.launch {
        addTypeOfProductEventChannel.send(AddTypeOfProductEvent.ShowMessageDataSaved(text))
    }
}

sealed class AddTypeOfProductEvent {
    data class ShowMessageDataSaved(val message: String) : AddTypeOfProductEvent()
}