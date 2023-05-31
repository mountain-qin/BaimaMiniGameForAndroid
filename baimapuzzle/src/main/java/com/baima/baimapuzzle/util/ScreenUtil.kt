package com.baima.baimapuzzle.util

import android.app.Activity
import android.util.DisplayMetrics

abstract class ScreenUtil {
    companion object {
        /**
         * 获取屏幕宽度，单位像素；
         */
        fun getScreenWidth(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }


        /**
         * 获取屏幕高度，单位像素；
         */
        fun getScreenHeight(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.heightPixels
        }
    }
}