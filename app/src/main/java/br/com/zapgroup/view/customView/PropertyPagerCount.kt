package br.com.zapgroup.view.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import br.com.zapgroup.R
import br.com.zapgroup.databinding.PagerMapLayoutBinding
import br.com.zapgroup.view.adapter.PagerCountAdapter
import kotlin.collections.ArrayList

class PropertyPagerCount: FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)

    private var binding: PagerMapLayoutBinding
    private val pagerCountAdapter by lazy { PagerCountAdapter() }

    init {
        View.inflate(context, R.layout.pager_map_layout, this)
        binding = PagerMapLayoutBinding.inflate(LayoutInflater.from(context), this, true)

    }

    fun addViewPage(viewPager: ViewPager, listSize: Int) {
        binding.pagerCountRecycler.adapter = pagerCountAdapter
        pagerCountAdapter.setListView(getList(listSize,isFirst = true))

        viewPager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}
                override fun onPageSelected(position: Int) {
                    val list = getList(listSize)
                    list[position] = true
                    pagerCountAdapter.setListView(list)
                }
                override fun onPageScrollStateChanged(state: Int) {}
            }
        )
    }

    fun getList(listSize: Int, isFirst: Boolean = false): MutableList<Boolean> {
        val list: MutableList<Boolean> = ArrayList()
        list.add(isFirst)
        for(item: Int in 2..listSize) {
            list.add(false)
        }
        return list
    }
}