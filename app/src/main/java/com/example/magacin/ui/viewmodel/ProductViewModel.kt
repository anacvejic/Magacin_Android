package com.example.magacin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TempProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val magacinDao: MagacinDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val productEventChanel = Channel<ProductEvent>()
    val productEvent = productEventChanel.receiveAsFlow()

    private val productWithAmountFlow = searchQuery.flatMapLatest {
        magacinDao.getProductWithAmount(it)
    }

    val product = productWithAmountFlow.asLiveData()

    fun onTypeOfProductSelected(tempProduct: TempProduct) = viewModelScope.launch {
        productEventChanel.send(ProductEvent.NavigateToTypeOfProduct(tempProduct))
    }
    fun onProductSwipe(product: Product) = viewModelScope.launch {
        val pr = Product(product.productName, product.productCode, product.idProduct)
        magacinDao.deleteProduct(product)
        productEventChanel.send(ProductEvent.ShowUndoDeleteTaskMassage(product))
    }

    fun onUndoDeleteClick(product: Product) = viewModelScope.launch {
        magacinDao.insertProduct(product)
    }
}

sealed class ProductEvent {
    data class NavigateToTypeOfProduct(val tempProduct: TempProduct) : ProductEvent()
    data class ShowUndoDeleteTaskMassage(val product: Product) : ProductEvent()
}
