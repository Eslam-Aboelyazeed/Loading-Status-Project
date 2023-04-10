package com.udacity

import android.view.animation.Animation
import android.view.animation.Transformation

class TheAnimation(lCircle: LoadingCircle, nSweepAngle: Float, nWidth: Float): Animation(){

    private var lCircle: LoadingCircle

    private var oSweepAngle: Float

    private var nSweepAngle: Float

    private var oWidth: Float

    private var nWidth: Float


    init {

        this.oSweepAngle = lCircle.getTheAngle()
        this.nSweepAngle = nSweepAngle
        this.oWidth = lCircle.getTheWidth()
        this.nWidth = nWidth
        this.lCircle = lCircle
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val ang = oSweepAngle +((nSweepAngle - oSweepAngle) * interpolatedTime)
        val wid = oWidth + ((nWidth - oWidth) * interpolatedTime)


        lCircle.setTheBText("Loading")
        lCircle.setTheAngle(ang)
        lCircle.setTheWidth(wid)
        lCircle.requestLayout()

    }

}