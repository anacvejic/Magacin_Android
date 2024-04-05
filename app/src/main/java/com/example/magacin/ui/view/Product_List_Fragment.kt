package com.example.magacin.ui.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magacin.R
import com.example.magacin.enteties.Product
import com.example.magacin.enteties.TempProduct
import com.example.magacin.databinding.FragmentProductListBinding
import com.example.magacin.ui.viewmodel.ProductEvent
import com.example.magacin.ui.viewmodel.ProductViewModel
import com.example.magacin.ui.adapter.ProductWithAmountAdapter
import com.example.magacin.util.exhaustive
import com.example.magacin.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.snackbar.Snackbar

@AndroidEntryPoint
class Product_List_Fragment : Fragment(), ProductWithAmountAdapter.OnItemClickedListener {

    private lateinit var searchView: SearchView
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var tempProduct: TempProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val productAdapter = ProductWithAmountAdapter(this)

        binding.apply {
            recyclerViewProductwithamount.apply {
                adapter = productAdapter
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
                    tempProduct = productAdapter.currentList[viewHolder.adapterPosition]

                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            val product = Product(tempProduct.productName, tempProduct.productCode, tempProduct.idProduct)
                            viewModel.onProductSwipe(product)
                            recyclerViewProductwithamount.adapter!!.notifyDataSetChanged()
                            Snackbar.make(
                                requireView(),
                                "Proizvod je obrisan!",
                                Snackbar.LENGTH_LONG
                            )
                                .setAction("Odustani") {
                                    viewModel.onUndoDeleteClick(product)
                                }.show()
                        }

                        ItemTouchHelper.RIGHT -> {
                            val alert = AlertDialog.Builder(requireContext())
                            val editName = EditText(requireContext())
                            val editCode = EditText(requireContext())
                            alert.setTitle("Izmeni proizvod ${tempProduct.productName}")
                            alert.setCancelable(true)
                            alert.setNegativeButton(
                                "Odustani",
                                DialogInterface.OnClickListener { dialog, which ->
                                    recyclerViewProductwithamount.adapter!!.notifyDataSetChanged()
                                })
                            alert.setPositiveButton(
                                "Izmeni",
                                DialogInterface.OnClickListener { dialog, which ->
                                    val action =
                                        com.example.magacin.ui.product.Product_List_FragmentDirections.actionProductListFragmentToEditProductFragment(
                                            tempProduct,
                                            "parametri"
                                        )
                                    findNavController().navigate(action)
                                })
                            alert.show()
                        }
                    }
                }
            }).attachToRecyclerView(recyclerViewProductwithamount)
        }

        viewModel.product.observe(viewLifecycleOwner) {
            productAdapter.submitList(it)
            if (it.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Nemate nijedan proizvod!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.productEvent.collect { event ->
                when (event) {
                    is ProductEvent.NavigateToTypeOfProduct -> {
                        val action =
                            com.example.magacin.ui.product.Product_List_FragmentDirections.actionProductListFragmentToTypeOfProductFragment(
                                event.tempProduct,
                                "Send type pf product"
                            )
                        findNavController().navigate(action)
                    }

                    else -> {}
                }.exhaustive
            }
        }
        setHasOptionsMenu(true)

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView

        searchView.onQueryTextChanged {
            //update search query
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_product -> {
                findNavController().navigate(R.id.action_product_List_Fragment_to_add_Product_Fragment)
                true
            }

            R.id.add_type_ofProduct -> {
                findNavController().navigate(R.id.action_product_List_Fragment_to_add_TypeOfProduct_Fragment)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClicked(tempProduct: TempProduct) {
        viewModel.onTypeOfProductSelected(tempProduct)
    }
}