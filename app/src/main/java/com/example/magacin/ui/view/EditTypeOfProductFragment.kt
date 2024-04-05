package com.example.magacin.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.magacin.enteties.TypeOfProduct
import com.example.magacin.databinding.FragmentEditTypeOfProductBinding
import com.example.magacin.ui.viewmodel.EditTypeOfProductEvent
import com.example.magacin.ui.viewmodel.EditTypeOfProductViewModel
import com.example.magacin.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditTypeOfProductFragment : Fragment() {

    private val viewModel: EditTypeOfProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditTypeOfProductBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.apply {
            etAmountedit.setText(viewModel.priceOfTypeProduct.toString())
            etPricedit.setText(viewModel.price.toString())

            etAmountedit.addTextChangedListener {
                viewModel.priceOfTypeProduct = it.toString()
            }
            etPricedit.addTextChangedListener {
                viewModel.price = it.toString()
            }

            btnEdit.setOnClickListener {
                if (etAmountedit.text.isNullOrEmpty() || etPricedit.text.isNullOrEmpty()) {
                    Log.i("VREDNOST", "${etAmountedit.text.toString()}")
                    Snackbar.make(
                        requireView(),
                        "Sva polja moraju biti popunjena!!!",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    val typeOfProduct = TypeOfProduct(
                        productId = viewModel.productId as Int,
                        price = Integer.parseInt(etPricedit.text.toString()),
                        priceOfTypeProduct = Integer.parseInt(etAmountedit.text.toString()),
                        invoiceNumber = viewModel.invoicenumber as Int,
                        created = viewModel.created as Long,
                        idProductType = viewModel.idProductType as Int
                    )
                    viewModel.updateTypeOfProduct(typeOfProduct)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.editEvent.collect { event ->
                when (event) {
                    is EditTypeOfProductEvent.ShowSuccessMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        return root
    }
}