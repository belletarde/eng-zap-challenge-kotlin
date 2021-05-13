package br.com.zapgroup.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.model.api.PropertyShared
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.lang.NumberFormatException

fun String?.toPropertyResponse(): PropertyShared? {
    val gson = Gson()
    return gson.fromJson(this, PropertyShared::class.java) ?: null
}

fun PropertyShared.objectToString(): String {
    return Gson().toJson(this)
}

fun String?.isNotNumber(): Boolean {
    try {
        this?.toDouble()
        return false
    } catch (exception: NumberFormatException) {
        return true
    }
}

fun List<PropertyResponse>.pagination(page: Int, limit: Int = 20): List<PropertyResponse> {
    val endIndex = (limit * page)
    val startIndex = (limit * (page - 1))

    if (this.size / limit < page) {
        return this.subList(startIndex, this.lastIndex + 1)
    }
    return this.subList(startIndex, endIndex)
}

fun ImageView.loadUrl(url: String?, loader: LottieAnimationView? = null) {
    val glide = when (context) {
        is Activity -> Glide.with(context as Activity)
        else -> Glide.with(context)
    }

    glide.load(url).centerCrop().listener(object: RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            return true
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            loader?.visibility = GONE
            return false
        }

    }
    ).into(this)
}

fun loadSnackBar(appCompatActivity: AppCompatActivity, mesage: String = "Desculpe! Tivemos problemas para realizar ação") {
    val view: View = appCompatActivity.findViewById(android.R.id.content)
    Snackbar.make(view, mesage, Snackbar.LENGTH_LONG).show()
}