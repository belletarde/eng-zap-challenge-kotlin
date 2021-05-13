package br.com.zapgroup.view.customView

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import br.com.zapgroup.R

class TabButton(buttonContext: Context, attrs: AttributeSet? = null): AppCompatButton(buttonContext, attrs) {

    fun setBtnNotClicked() {
        this.setBackgroundResource(R.drawable.default_list_tab_bg)
    }

    fun setBtnClicked() {
        this.setBackgroundResource(R.drawable.seleceted_list_tab_bg)
    }
}