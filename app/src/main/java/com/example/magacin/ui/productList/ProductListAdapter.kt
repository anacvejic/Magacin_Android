package com.example.magacin.ui.productList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.magacin.enteties.Product
import com.example.magacin.databinding.ItemsProductBinding

class ProductListAdapter(private val listener: OnItemClickedListener) :
    ListAdapter<Product, ProductListAdapter.ProductListViewHolder>(DiffCallback()) {

    inner class ProductListViewHolder(val binding: ItemsProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val product = getItem(position)
                        listener.onItemClicked(product)
                    }
                }
            }
        }

        fun bind(product: Product) {
            binding.apply {
                tvNameOfProduct.text = "Naziv: ${product.productName}"
                tvCodeOfProduct.text = "Šifra: ${product.productCode}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val binding =
            ItemsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnItemClickedListener {
        fun onItemClicked(product: Product)
    }
}


class DiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) =
        oldItem.idProduct == newItem.idProduct

    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}