package com.udacity

import android.view.animation.Animation
import android.view.animation.Transformation

class DTextAnimation (lCircle: LoadingCircle): Animation(){

    private var lCircle: LoadingCircle

    init {
        this.lCircle = lCircle
    }
    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)

        lCircle.setTheBText("Download")
        lCircle.requestLayout()
    }

}