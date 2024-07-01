package com.example.playlistmaker.app

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.example.playlistmaker.R

fun Toast.showCustomToast(message: String, activity: Activity) {
    val layout = activity.layoutInflater.inflate(
        R.layout.custom_toast,
        activity.findViewById(R.id.customToast)
    )
    val textView = layout.findViewById<TextView>(R.id.toastMessage)
    textView.text = message
    this.apply {
        setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 0)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}