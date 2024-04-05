package com.example.magacin.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.magacin.enteties.Product
import com.example.magacin.databinding.FragmentEditProductBinding
import com.example.magacin.ui.viewmodel.EditProductEvent
import com.example.magacin.ui.viewmodel.EditProductViewModel
import com.example.magacin.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProductFragment : Fragment() {

    private val viewModel: EditProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentEditProductBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.apply {
            etNamedit.setText(viewModel.productName)
            etCodeedit.setText(viewModel.productCode)

            etNamedit.addTextChangedListener {
                viewModel.productName = it.toString()
            }
            etCodeedit.addTextChangedListener {
                viewModel.productCode = it.toString()
            }

            btnEdit.setOnClickListener {
                if (etNamedit.text.isNullOrEmpty() || etCodeedit.text.isNullOrEmpty()) {
                    Snackbar.make(
                        requireView(),
                        "Sva polja moraju biti popunjena!!!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }else{
                    val product = Product(viewModel.productName, viewModel.productCode,
                        viewModel.idProduct!!)
                    viewModel.updateProduct(product)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.editEvent.collect{ event ->
                when (event) {
                    is EditProductEvent.ShowSuccessMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
        return view
    }

}