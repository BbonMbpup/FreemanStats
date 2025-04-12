package com.example.freemanstats.animation

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ZoomOutPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val scale = if (position < 0) position + 1f else Math.abs(1f - position)
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = scale
    }
}