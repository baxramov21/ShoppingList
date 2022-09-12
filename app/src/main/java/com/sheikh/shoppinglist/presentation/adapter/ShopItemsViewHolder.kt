package com.sheikh.shoppinglist.presentation.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sheikh.shoppinglist.R

class ShopItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvItemName: TextView = itemView.findViewById(R.id.textViewItemName)
    val tvItemCount: TextView = itemView.findViewById(R.id.textViewItemCount)
}