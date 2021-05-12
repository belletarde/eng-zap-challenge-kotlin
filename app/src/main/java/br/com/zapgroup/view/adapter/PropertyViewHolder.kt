package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.databinding.ListItemBinding
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.loadUrl

class PropertyViewHolder(
    private val itemClick: ItemClick,
    private val itemBinding: ListItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor")
    fun bind(propertyResponse: PropertyResponse){
        with(propertyResponse) {
            itemBinding.propertyImage.loadUrl(propertyResponse.images[0])
            itemBinding.root.setOnClickListener {
                itemClick.onClickListener(id)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, itemClick: ItemClick): PropertyViewHolder {
            val itemBinding = ListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return PropertyViewHolder(
                itemClick,
                itemBinding
            )
        }
    }
}