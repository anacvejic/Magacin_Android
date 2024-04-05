package com.example.magacin.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.TempProduct
import com.example.magacin.enteties.TypeOfProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeOfProductViewModel @Inject constructor(
    private val magacinDao: MagacinDao,
    private val state: SavedStateHandle
) : ViewModel() {
    val tempProduct = state.get<TempProduct>("typeOfProduct")

    private val typeOfProductEventChannel = Channel<TypeOfProductEvent>()

    val typeOfProductEvent = typeOfProductEventChannel.receiveAsFlow()

    var idProduct = state.get<Int>("idProduct") ?: tempProduct?.idProduct ?: ""
        set(value) {
            field = value
            state["idProduct"] = value
        }

    fun getTypeOfProducts(id: Int) = magacinDao.getTypeOfProducts(id).asLiveData()

    fun onTypeOfProductSwipe(typeOfProduct: TypeOfProduct) = viewModelScope.launch {
        magacinDao.deleteTypeOfProduct(typeOfProduct)
        typeOfProductEventChannel.send(TypeOfProductEvent.ShowUndoDeleteTaskMassage(typeOfProduct))
    }

    fun onUndoDeleteClick(typeOfProduct: TypeOfProduct) = viewModelScope.launch {
        magacinDao.insertProductType(typeOfProduct)
    }
}

sealed class TypeOfProductEvent{
    data class ShowUndoDeleteTaskMassage(val typeOfProduct: TypeOfProduct) : TypeOfProductEvent()
}


