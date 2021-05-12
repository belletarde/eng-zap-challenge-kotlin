package br.com.zapgroup.utils

import android.app.Activity
import android.widget.ImageView
import br.com.zapgroup.data.SharedValues
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.model.api.PropertyShared
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.lang.Exception
import java.lang.NumberFormatException

fun String?.toPropertyResponse(): PropertyShared {
    val gson = Gson()
    return gson.fromJson(this, PropertyShared::class.java) ?: throw Exception()
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

fun ImageView.loadUrl(url: String?) {
    val glide = when (context) {
        is Activity -> Glide.with(context as Activity)
        else -> Glide.with(context)
    }

    glide.load(url).centerCrop().into(this)
}