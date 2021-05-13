package br.com.zapgroup.view.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import br.com.zapgroup.R
import br.com.zapgroup.databinding.DetailPillLayoutBinding
import br.com.zapgroup.view.adapter.PropertyPillAdapter

class PropertyPillView: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    private var binding: DetailPillLayoutBinding
    private val propertyPillAdapter by lazy { PropertyPillAdapter() }

    init {
        View.inflate(context, R.layout.detail_pill_layout, this)
        binding = DetailPillLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    }

    fun addItems(details: List<String>) {
        binding.pillRecycler.adapter = propertyPillAdapter
        propertyPillAdapter.submitList(details)
    }
}