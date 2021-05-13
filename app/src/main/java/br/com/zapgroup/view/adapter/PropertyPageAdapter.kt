package br.com.zapgroup.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import br.com.zapgroup.R
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.utils.loadUrl
import br.com.zapgroup.view.customView.PropertyPagerCount
import com.airbnb.lottie.LottieAnimationView

class PropertyPageAdapter(private val propertyResponse: PropertyResponse, private val context: Context, private val itemClick: ItemClick): PagerAdapter() {
    override fun getCount(): Int {
        return propertyResponse.images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.item_pager, container, false) as ViewGroup
        val pagerItemImage = layout.findViewById<ImageView>(R.id.pagerItemImage)
        val lottieLoad = layout.findViewById<LottieAnimationView>(R.id.loadListImage)
        pagerItemImage.loadUrl(propertyResponse.images[position], lottieLoad)
        layout.setOnClickListener {
            itemClick.onClickListener(propertyResponse.id)
        }
        container.addView(layout)
        return layout
    }
}