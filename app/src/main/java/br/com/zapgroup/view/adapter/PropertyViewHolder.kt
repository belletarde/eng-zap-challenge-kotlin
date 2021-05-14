package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.databinding.ListItemBinding
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.PropertyDetailHelper.Companion.fillData

class PropertyViewHolder(
    private val itemClick: ItemClick,
    private val itemBinding: ListItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    fun bind(propertyResponse: PropertyResponse){
        with(propertyResponse) {
            with(itemBinding) {
                root.setOnClickListener {
                    itemClick.onClickListener(id)
                }
                propertyImagePager.adapter = PropertyPageAdapter(propertyResponse, itemBinding.root.context, itemClick)
                nextPager.addViewPage(propertyImagePager, images.size)
                fillData(propertyResponse, itemBinding.root.context, propertyAddress, propertyType, propertyPrice, propertyDetails)
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