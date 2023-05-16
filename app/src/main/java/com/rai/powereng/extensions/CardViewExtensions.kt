package com.rai.powereng.extensions

import android.widget.TextView
import androidx.cardview.widget.CardView


fun CardView.getAllTextViews(): List<TextView> {
    val allTextViews = mutableListOf<TextView>()
    for (i in 0 until this.childCount) {
        val childView = this.getChildAt(i)
        if (childView is TextView) {
            allTextViews.add(childView)
        }
    }
    return allTextViews
}

