package com.pahnal.mystoryapp.utils

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.util.*

fun String.convertTimeStampToDisplay(format: String = "EEEE, dd MMM yyyy"): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    val dateTimeStamp = sdf.parse(this)
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    return formatter.format(dateTimeStamp!!)
}

fun Activity.hideKeyboard() = currentFocus?.let { view ->
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun <T> Activity.goTo(clazz: Class<T>, isFinished: Boolean = false) {
    val intent = Intent(this, clazz)
    startActivity(intent)
    if (isFinished) finish()
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ComponentActivity.goBack() = onBackPressedDispatcher.onBackPressed()

fun <V> ComponentActivity.goBackWithSingleData(nameData: String, data: V) {
    val intent = Intent()
    when (data) {
        is String -> intent.putExtra(nameData, data)
        is Boolean -> intent.putExtra(nameData, data)
        is Int -> intent.putExtra(nameData, data)
        is Parcelable -> intent.putExtra(nameData, data)
    }
    setResult(RESULT_OK, intent)
    goBack()
}