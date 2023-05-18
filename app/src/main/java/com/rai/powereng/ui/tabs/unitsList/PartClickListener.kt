package com.rai.powereng.ui.tabs.unitsList

import android.view.View

interface PartClickListener {
    fun onPartClickListener(unitNum:Int,part: Int,position: Int, view: View)

}