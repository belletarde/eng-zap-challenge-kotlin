package br.com.zapgroup.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.zapgroup.R
import br.com.zapgroup.databinding.PagerCountItemBinding

class PagerCountAdapter:
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedTabList: MutableList<Boolean> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PropertyCounterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is PropertyCounterViewHolder) {
            holder.bind(getItem(position))
        }
    }

    fun setListView(selectedTabList: List<Boolean>) {
        this.selectedTabList.clear()
        this.selectedTabList.addAll(selectedTabList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return selectedTabList.size
    }

    private fun getItem(position: Int): Boolean {
        return selectedTabList[position]
    }
}

class PropertyCounterViewHolder(
    private val itemBinding: PagerCountItemBinding
) : RecyclerView.ViewHolder(itemBinding.root){

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    fun bind(isActive: Boolean){
        with(itemBinding) {
            if (isActive) {
                nextPagerItemCount.setBackgroundResource(R.drawable.circle_active)
            } else {
                nextPagerItemCount.setBackgroundResource(R.drawable.circle)
            }
        }

    }

    companion object {
        fun create(parent: ViewGroup): PropertyCounterViewHolder {
            val itemBinding = PagerCountItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return PropertyCounterViewHolder(
                itemBinding
            )
        }
    }
}