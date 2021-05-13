package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.databinding.ListItemBinding
import br.com.zapgroup.model.api.PropertyResponse

class PropertyViewHolder(
    private val itemClick: ItemClick,
    private val itemBinding: ListItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor")
    fun bind(propertyResponse: PropertyResponse){
        with(propertyResponse) {
            with(itemBinding) {
                root.setOnClickListener {
                    itemClick.onClickListener(id)
                }
                propertyImagePager.adapter = PropertyPageAdapter(propertyResponse, itemBinding.root.context, itemClick)
                propertyAddress.text = "${address.neighborhood}, ${address.city}"
                propertyDetail.text = "Quartos: $bedrooms, Banheiros: $bathrooms, Vagas: $parkingSpaces"
                propertyArea.text = "${usableAreas.toString()} m²"
                if(pricingInfos.businessType == "RENTAL") {
                    propertyType.text = "Propriedade para alugar"
                    propertyPrice.text = "R$ ${pricingInfos.rentalTotalPrice},00"
                } else {
                    propertyType.text = "Propriedade para vender"
                    propertyPrice.text = "R$ ${pricingInfos.price},00"
                }
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