package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.databinding.DetailPillItemBinding

class PropertyPillAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var list: MutableList<String> = ArrayList()


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PropertyPillsViewHolder) {
            holder.bind(getItem(position))
        }
    }

    fun setListView(detailList: List<String>) {
        list.clear()
        list.addAll(detailList)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): String {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PropertyPillsViewHolder.create(parent)
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