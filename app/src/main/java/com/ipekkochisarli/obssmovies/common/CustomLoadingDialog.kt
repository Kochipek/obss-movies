package com.ipekkochisarli.obssmovies.common

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import androidx.core.graphics.drawable.toDrawable
import com.ipekkochisarli.obssmovies.R

class CustomLoadingDialog(
    context: Context,
) : Dialog(context) {
    init {
        initDialog()
    }

    private fun initDialog() {
        val params = window?.attributes

        params?.gravity = Gravity.CENTER

        window?.attributes = params

        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)

        window?.setBackgroundDrawable(
            android.graphics.Color.TRANSPARENT
                .toDrawable(),
        )

        setContentView(R.layout.loading_layout)
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            show()
        } else {
            dismiss()
        }
    }
}
