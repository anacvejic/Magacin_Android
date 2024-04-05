package com.example.magacin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.magacin.enteties.TempProduct
import com.example.magacin.databinding.ItemProductithamountBinding

class ProductWithAmountAdapter(private val listener: OnItemClickedListener) :
    ListAdapter<TempProduct, ProductWithAmountAdapter.ProductWithAmountListViewHolder>(DiffCallback()) {

    inner class ProductWithAmountListViewHolder(val binding: ItemProductithamountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val tempProduct = getItem(position)
                        listener.onItemClicked(tempProduct)
                    }
                }
            }
        }

        fun bind(tempProduct: TempProduct) {
            binding.apply {
                tvNameOfProduct.text = "Naziv: ${tempProduct.productName}"
                tvCodeOfProduct.text = "Šifra: ${tempProduct.productCode}"
                tvProductAmount.text = "Količina: ${tempProduct.sumOfAmount}"
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductWithAmountListViewHolder {
        val binding =
            ItemProductithamountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductWithAmountListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductWithAmountListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnItemClickedListener {
        fun onItemClicked(tempProduct: TempProduct)
    }
}

class DiffCallback : DiffUtil.ItemCallback<TempProduct>() {
    override fun areItemsTheSame(oldItem: TempProduct, newItem: TempProduct) =
        oldItem.idProduct == newItem.idProduct

    override fun areContentsTheSame(oldItem: TempProduct, newItem: TempProduct) = oldItem == newItem
}