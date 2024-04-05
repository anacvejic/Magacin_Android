package com.example.magacin.ui.typeOfProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.magacin.enteties.TypeOfProduct
import com.example.magacin.databinding.ItemsTypeofproductBinding

class TypeOfProductAdapter: ListAdapter<TypeOfProduct, TypeOfProductAdapter.TypeOfProductViewHolder>(DiffCallback()) {

    inner class TypeOfProductViewHolder(val binding: ItemsTypeofproductBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(typeOfProduct: TypeOfProduct) {
            binding.apply {
                tvInvoiceNumber.text = "Broj fakture: ${typeOfProduct.invoiceNumber}"
                tvAmount.text = "Uneta količina: ${typeOfProduct.priceOfTypeProduct}"
                tvPrice.text = "Cena: ${typeOfProduct.price}"
                tvDate.text = "Datum: ${typeOfProduct.createdDateFormatted}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeOfProductViewHolder {
        val binding = ItemsTypeofproductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TypeOfProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeOfProductViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
}

class DiffCallback : DiffUtil.ItemCallback<TypeOfProduct>() {
    override fun areItemsTheSame(oldItem: TypeOfProduct, newItem: TypeOfProduct) =
        oldItem.idProductType == newItem.idProductType

    override fun areContentsTheSame(oldItem: TypeOfProduct, newItem: TypeOfProduct) = oldItem == newItem
}