package com.baima.bgmsound

import android.content.Context
import android.widget.Toast

abstract class ToastUtil {
    private constructor(){}


    companion object {
        private var toast: Toast? = null


        /**
         * 下一个吐丝可以把上一个吐丝覆盖；
         */
        fun showToast(context: Context, text: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
            if (toast != null) {
                toast?.cancel()
                toast = null
            }

            toast = Toast.makeText(context, text, duration)
            toast?.show()
        }
    }
}