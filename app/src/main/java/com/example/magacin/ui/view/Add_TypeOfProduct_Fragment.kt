package com.example.magacin.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.magacin.R
import com.example.magacin.enteties.TypeOfProduct
import com.example.magacin.databinding.FragmentAddTypeOfProductBinding
import com.example.magacin.ui.viewmodel.AddTypeOfProductEvent
import com.example.magacin.ui.viewmodel.Add_TypeOfProductViewModel
import com.example.magacin.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Add_TypeOfProduct_Fragment : Fragment() {

    private val viewModel: Add_TypeOfProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddTypeOfProductBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {

            if (!viewModel.productName.isNullOrEmpty() && !viewModel.productCode.isNullOrEmpty()) {
                btnNameOfProductType.setText("${viewModel.productName} ${viewModel.productCode}")
            }

            btnNameOfProductType.setOnClickListener {
                findNavController().navigate(R.id.action_add_TypeOfProduct_Fragment_to_list_Of_ProductFragment)
            }
            btnSaveNewTypeOfProduct.setOnClickListener {
                if (viewModel.productName.isNullOrEmpty() ||
                    etPriceOfProduct.text.toString().isNullOrEmpty() ||
                    etAmountOfProductType.text.toString().isNullOrEmpty()
                ) {
                    Snackbar.make(
                        requireView(),
                        "Sva polja moraju biti popunjena!!!",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    var res = viewModel.getMaxInvoiceNumber.value
                    if(res == null) res=0 else res

                     val typeOfProduct = TypeOfProduct(
                         viewModel.idPorduct as Int,
                         Integer.parseInt(etPriceOfProduct.text.toString()),
                         Integer.parseInt(etAmountOfProductType.text.toString()),
                         res!!.plus(1)
                     )
                     viewModel.createTypeOfProduct(typeOfProduct)
                     etPriceOfProduct.text.clear()
                     etAmountOfProductType.text.clear()
                     btnNameOfProductType.setText("Naziv proizvoda...")
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addTypeOfProductEvent.collect() { event ->
                when (event) {
                    is AddTypeOfProductEvent.ShowMessageDataSaved -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
        fun clear() {
            binding.apply {
                etPriceOfProduct.text.clear()
                etAmountOfProductType.text.clear()
                btnNameOfProductType.text = "Naziv proizvoda..."
            }
        }

        return root
    }

}