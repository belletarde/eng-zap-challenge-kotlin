package br.com.zapgroup.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.model.api.PropertyResponse

class PropertyAdapter(private val itemClick: ItemClick):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var propertyResponseList: MutableList<PropertyResponse> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PropertyViewHolder.create(parent, itemClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PropertyViewHolder) {
            holder.bind(getItem(position))
        }
    }

    fun setListView(propertyResponseList: List<PropertyResponse>) {
        this.propertyResponseList.clear()
        this.propertyResponseList.addAll(propertyResponseList)
        notifyDataSetChanged()
    }

    fun loadMore(propertyResponseList: List<PropertyResponse>) {
        this.propertyResponseList.addAll(propertyResponseList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return propertyResponseList.size
    }

    private fun getItem(position: Int): PropertyResponse {
        return propertyResponseList[position]
    }
}