package com.sheikh.shoppinglist.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.sheikh.shoppinglist.R
import com.sheikh.shoppinglist.presentation.view_model.MainViewModel
import com.sheikh.shoppinglist.presentation.adapter.ShopItemsAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopItemsAdapter: ShopItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shoppingList.observe(this) {
            shopItemsAdapter.submitList(it)
        }
    }

    fun newItem(view: View) {
        startActivity(DetailActivity.newIntentAddItem(this))
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
            val intent = DetailActivity.newIntentEditItem(this, it.ID)
            startActivity(intent)

        }
    }

    private fun setUpOnLongClickListener() {
        shopItemsAdapter.onShopItemLongClickListener = {
            viewModel.changeEnabledState(it)
        }
    }
}