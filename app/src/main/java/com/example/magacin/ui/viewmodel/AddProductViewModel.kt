package com.example.magacin.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val magacinDao: MagacinDao,
    private val state: SavedStateHandle
): ViewModel() {

    private val addProductEventChannel = Channel<AddProductEvent>()
    val addProductEvent = addProductEventChannel.receiveAsFlow()


    fun onSaveData(productName: String, productCode: String){
        if (productName.isBlank() || productCode.isBlank()) {
            //Show invalid input message
            showInvalidInputMessage("Polje ime i šifra ne mogu biti prazna!")
        }else{
            val newProduct = Product(productName = productName, productCode = productCode)
            createProduct(newProduct)
       }
    }

    private fun createProduct(product: Product) = viewModelScope.launch {
        magacinDao.insertProduct(product)
        //Show message
        showMessageDataSaved("Uneli ste novi proizvod!")
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addProductEventChannel.send(AddProductEvent.ShowInputInvalidMessage(text))
    }

    private fun showMessageDataSaved(text: String) = viewModelScope.launch {
        addProductEventChannel.send(AddProductEvent.ShowMessageDataSaved(text))
    }

    sealed class AddProductEvent{

        data class ShowInputInvalidMessage(val message: String): AddProductEvent()
        data class ShowMessageDataSaved(val message: String): AddProductEvent()
    }
}