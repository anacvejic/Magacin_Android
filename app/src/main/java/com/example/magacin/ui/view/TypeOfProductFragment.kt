package com.example.magacin.ui.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magacin.enteties.TypeOfProduct
import com.example.magacin.databinding.FragmentTypeOfProductBinding
import com.example.magacin.ui.typeOfProduct.TypeOfProductAdapter
import com.example.magacin.ui.viewmodel.TypeOfProductEvent
import com.example.magacin.ui.viewmodel.TypeOfProductViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TypeOfProductFragment : Fragment() {

    private val viewModel: TypeOfProductViewModel by viewModels()
    private lateinit var typeOfProduct: TypeOfProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTypeOfProductBinding.inflate(inflater, container, false)
        val typeOfProductAdapter = TypeOfProductAdapter()

        binding.apply {
            recyclerViewTypeOfProduct.apply {
                adapter = typeOfProductAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object :
                ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    source: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    typeOfProduct = typeOfProductAdapter.currentList[viewHolder.adapterPosition]

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            viewModel.onTypeOfProductSwipe(typeOfProduct)
                            recyclerViewTypeOfProduct.adapter!!.notifyDataSetChanged()
                            Snackbar.make(
                                requireView(),
                                "Proizvod je obrisan!",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Odustani") {
                                    viewModel.onUndoDeleteClick(typeOfProduct)
                                }.show()
                        }

                        ItemTouchHelper.RIGHT -> {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Izmeni proizvod")
                            builder.setCancelable(true)
                            builder.setNegativeButton(
                                "Odustani",
                                DialogInterface.OnClickListener { dialog, which ->
                                    recyclerViewTypeOfProduct.adapter!!.notifyDataSetChanged()
                                })
                            builder.setPositiveButton(
                                "Izmeni",
                                DialogInterface.OnClickListener { dialog, which ->
                                    val action =
                                        com.example.magacin.ui.typeOfProduct.TypeOfProductFragmentDirections.actionTypeOfProductFragmentToEditTypeOfProductFragment2(
                                            typeOfProduct,
                                            "Parametri"
                                        )
                                    findNavController().navigate(action)
                                })
                            builder.show()
                        }
                    }
                }
            }).attachToRecyclerView(recyclerViewTypeOfProduct)
        }

        viewModel.getTypeOfProducts(viewModel.idProduct as Int).observe(viewLifecycleOwner) { it ->
            if (it.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Nemate nijedan proizvod ove vrste!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            typeOfProductAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.typeOfProductEvent.collect() { event ->
                when (event) {
                    is TypeOfProductEvent.ShowUndoDeleteTaskMassage -> {
                        Snackbar.make(requireView(), "Proizvod je obrisan!", Snackbar.LENGTH_LONG)
                            .setAction("Odustani") {
                                viewModel.onUndoDeleteClick(typeOfProduct)
                                binding.recyclerViewTypeOfProduct.adapter!!.notifyDataSetChanged()
                            }.show()
                    }
                }
            }
        }


        setHasOptionsMenu(true)
        return binding.root
    }
}
