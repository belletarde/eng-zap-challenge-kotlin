package br.com.zapgroup.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.com.zapgroup.data.SharedValues
import br.com.zapgroup.model.api.PropertyResponse
import br.com.zapgroup.model.api.PropertyShared
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.lang.Exception
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

fun ImageView.loadUrl(url: String?) {
    val glide = when (context) {
        is Activity -> Glide.with(context as Activity)
        else -> Glide.with(context)
    }

    glide.load(url).centerCrop().into(this)
}

fun loadSnackBar(appCompatActivity: AppCompatActivity, mesage: String = "Desculpe! Tivemos problemas para realizar ação") {
    val view: View = appCompatActivity.findViewById(android.R.id.content)
    Snackbar.make(view, mesage, Snackbar.LENGTH_LONG).show()
}