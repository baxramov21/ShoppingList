package com.sheikh.shoppinglist.presentation.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.presentation.adapter.ShopItemsAdapter
import com.sheikh.shoppinglist.presentation.screens.interfaces.OnEditingFinishedListener
import com.sheikh.shoppinglist.presentation.screens.interfaces.OperationsWithItems
import com.sheikh.shoppinglist.presentation.view_model.MainViewModel

class MainActivity : AppCompatActivity(), OperationsWithItems, OnEditingFinishedListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopItemsAdapter: ShopItemsAdapter
    private var shopItemFragmentContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemFragmentContainer = findViewById(R.id.shopItemFragmentContainer)
        setUpRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shoppingList.observe(this) {
            shopItemsAdapter.submitList(it)
        }
    }

    private fun isOrientationMode(): Boolean {
       return shopItemFragmentContainer != null
    }

    fun newItem(view: View) {
        addItem(isOrientationMode())
    }

    private fun setUpRecyclerView() {
        val rvShopList: RecyclerView = findViewById(R.id.recyclerViewItems)
        with(rvShopList) {
            shopItemsAdapter = ShopItemsAdapter()
            adapter = shopItemsAdapter

            with(ShopItemsAdapter) {
                recycledViewPool.setMaxRecycledViews(
                    itemEnabled,
                    enabledPoolCount
                )
                recycledViewPool.setMaxRecycledViews(
                    itemDisabled,
                    disabledPoolCount
                )
            }
        }
        setupOnClickListener()
        setUpOnLongClickListener()
        setupSwipeListener(rvShopList)
    }

    private fun startFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shopItemFragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopItemsAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupOnClickListener() {
        shopItemsAdapter.onShopIteClickListener = {
          editItem(isOrientationMode(), it.ID)
        }
    }

    private fun setUpOnLongClickListener() {
        shopItemsAdapter.onShopItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }

    override fun addItem(landscapeOrientation: Boolean) {
        if (landscapeOrientation) {
            startFragment(DetailScreenFragment.newInstanceAddItem())
        } else {
            startActivity(DetailActivity.newIntentAddItem(this))
        }
    }

    override fun editItem(landscapeOrientation: Boolean, item_ID: Int) {
        if (landscapeOrientation) {
            startFragment(DetailScreenFragment.newInstanceEditItem(item_ID))
        } else {
            val intent = DetailActivity.newIntentEditItem(this, item_ID)
            startActivity(intent)
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this, "Succes", Toast.LENGTH_SHORT).show()
    }
}