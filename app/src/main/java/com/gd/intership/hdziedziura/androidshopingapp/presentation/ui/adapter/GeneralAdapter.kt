package com.gd.intership.hdziedziura.androidshopingapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gd.intership.hdziedziura.androidshopingapp.BR
import com.gd.intership.hdziedziura.androidshopingapp.common.RecyclerViewItem
import kotlinx.coroutines.cancel

class GeneralAdapter : RecyclerView.Adapter<GeneralViewHolder>() {
    private var items = mutableListOf<RecyclerViewItem>()
    private val viewTypesToViewLayout = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralViewHolder {
        val binding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            viewTypesToViewLayout[viewType] ?: 0,
            parent,
            false
        )
        return GeneralViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GeneralViewHolder, position: Int) {
        items[position].apply {
            this.itemViewModel.position = position
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        if (!viewTypesToViewLayout.containsKey(item.viewType)) {
            viewTypesToViewLayout[item.viewType] = item.layoutId
        }
        return item.viewType
    }

    fun update(newItems: List<RecyclerViewItem>) {
        val diffResult = DiffUtil.calculateDiff(
            DiffUtilCallback(
                items, newItems
            )
        )
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        items.forEach {
            it.itemViewModel.itemsScope.coroutineContext.cancel()
        }
    }
}

class GeneralViewHolder(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    lateinit var item: RecyclerViewItem

    fun bind(recyclerViewItem: RecyclerViewItem) {
        item = recyclerViewItem
        binding.setVariable(BR.itemVM, recyclerViewItem.itemViewModel)
    }
}

class DiffUtilCallback(
    val old: List<RecyclerViewItem>,
    val new: List<RecyclerViewItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return oldItem.layoutId == newItem.layoutId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        return oldItem.itemViewModel.item == newItem.itemViewModel.item
    }
}
