package com.example.magacin.ui.productList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.magacin.data.MagacinDao
import com.example.magacin.enteties.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val magacinDao: MagacinDao
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val productFlow = searchQuery.flatMapLatest {
        magacinDao.getProducts(it)
    }

    val products = productFlow.asLiveData()

    private val productEventChanel = Channel<ProductEvent>()
    val productEvent = productEventChanel.receiveAsFlow()

    fun onProductSelected(product: Product) = viewModelScope.launch {
        productEventChanel.send(ProductEvent.NavigateToADDTypeOfProduct(product))
    }

}

sealed class ProductEvent {
    data class NavigateToADDTypeOfProduct(val product: Product) : ProductEvent()
}
