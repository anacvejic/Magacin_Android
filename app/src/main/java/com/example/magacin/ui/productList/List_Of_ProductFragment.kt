package com.example.magacin.ui.productList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.magacin.R
import com.example.magacin.enteties.Product
import com.example.magacin.databinding.FragmentListOfProductBinding
import com.example.magacin.ui.productList.ProductListAdapter.OnItemClickedListener
import com.example.magacin.util.exhaustive
import com.example.magacin.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class List_Of_ProductFragment : Fragment(), OnItemClickedListener {

    private val viewModel: ProductListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentListOfProductBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val productlistAdapter = ProductListAdapter(this)

        binding.apply {
            recyclerViewTypeOfProduct.apply {
                adapter = productlistAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)

                var dividerDecoration: DividerItemDecoration = DividerItemDecoration(
                    recyclerViewTypeOfProduct.context,
                    DividerItemDecoration.VERTICAL
                )
                dividerDecoration.setDrawable(
                    ContextCompat.getDrawable(
                        recyclerViewTypeOfProduct.context,
                        R.drawable.divider
                    )!!
                )
                addItemDecoration(dividerDecoration)
            }
        }
        viewModel.products.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                Snackbar.make(
                    requireView(),
                    "Lista proizvoda je prazna! Unesite proizvode!",
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                productlistAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewModel.productEvent.collect { event ->
                when (event) {
                    is ProductEvent.NavigateToADDTypeOfProduct -> {
                        val action =
                            com.example.magacin.ui.productList.List_Of_ProductFragmentDirections.actionListOfProductFragmentToAddTypeOfProductFragment(
                                event.product,
                                "Send product"
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.onQueryTextChanged {
            //update search query
            viewModel.searchQuery.value = it
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val itemAdd: MenuItem = menu.findItem(R.id.add)
        if(itemAdd != null) {
            itemAdd.isVisible = false
        }
    }

    override fun onItemClicked(product: Product) {
        viewModel.onProductSelected(product)
    }

}