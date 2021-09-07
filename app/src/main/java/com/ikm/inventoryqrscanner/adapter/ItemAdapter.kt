package com.ikm.inventoryqrscanner.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ikm.inventoryqrscanner.databinding.AdapterItemBinding
import com.ikm.inventoryqrscanner.model.Product
import com.ikm.inventoryqrscanner.util.timestampToString

class ItemAdapter (
    var items : ArrayList<Product>,
    var listener: AdapterListener?
    ): RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    class ViewHolder(val binding: AdapterItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = items[position]

        holder.binding.product.text = items.product
        holder.binding.number.text = items.number.toString()
        holder.binding.location.text = items.location
        holder.binding.date.text = timestampToString(items.expDate)

        holder.binding.container.setOnClickListener{
            listener?.onClick(items)
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Product>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface AdapterListener {
        fun onClick(items: Product)
    }
}

