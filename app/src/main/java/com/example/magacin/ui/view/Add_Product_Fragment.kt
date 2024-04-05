package com.example.magacin.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.magacin.databinding.FragmentAddProduct2Binding
import com.example.magacin.ui.viewmodel.AddProductViewModel
import com.example.magacin.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Add_Product_Fragment : Fragment() {

    private val viewModel: AddProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAddProduct2Binding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            btnSaveNewProduct.setOnClickListener {
                viewModel.onSaveData(
                    etNameOfProduct.text.toString(),
                    etCodeOfProduct.text.toString()
                )
                etNameOfProduct.text.clear()
                etCodeOfProduct.text.clear()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addProductEvent.collect { event ->
                when (event) {
                    is AddProductViewModel.AddProductEvent.ShowInputInvalidMessage -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }

                    is AddProductViewModel.AddProductEvent.ShowMessageDataSaved -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }

        return root
    }
}

