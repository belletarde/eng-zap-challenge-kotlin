package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.databinding.DetailPillItemBinding

class PropertyPillAdapter:
    ListAdapter<String, PropertyPillsViewHolder>(
        DIFF_CALLBACK
    ){
    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>(){
            override fun areItemsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: String,
                newItem: String
            ): Boolean {
                return oldItem ==  newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyPillsViewHolder {
        return PropertyPillsViewHolder.create(
            parent
        )
    }

    override fun onBindViewHolder(holder: PropertyPillsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}



class PropertyPillsViewHolder(
    private val itemBinding: DetailPillItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor")
    fun bind(pillDesc: String){
        itemBinding.run {
            this.pillText.text = pillDesc
        }
    }

    companion object {
        fun create(parent: ViewGroup): PropertyPillsViewHolder {
            val itemBinding = DetailPillItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return PropertyPillsViewHolder(itemBinding)
        }
    }
}