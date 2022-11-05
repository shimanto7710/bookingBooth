
package com.example.bookingbooth.utils

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

fun Any.getCanonicalName(fragment: Fragment): String {
    return fragment.javaClass.canonicalName
}

fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
    val smoothScroller = object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference(): Int = snapMode
        override fun getHorizontalSnapPreference(): Int = snapMode
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun Window.disableUserInteraction() = this.setFlags(
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

fun Window.enableUserInteraction() = this.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

fun Any.convertJsonToString(): String {
    return Gson().toJson(this)
}

/***
 * Converts a json string to the corresponding object
 */
fun String.jsonStringToObject(type: Any): Any? {
    return try {
        Gson().fromJson(this, type::class.java)
    }catch (exception: JsonSyntaxException){
        null
    }
}

fun List<String>.convertListToString(): String {
    var message = ""
    this.forEach {
        message = if(message.isEmpty()) {
            it
        } else {
            "$message,$it"
        }
    }
    return message
}

fun String.isNumber(): Boolean {
    return if (this.isEmpty()) false else this.all { Character.isDigit(it) }
}

fun String.isExpressionOperator(): Boolean {
    val operatorList = arrayOf("+", "-", "*", "/", "&&", "||", "==", "(", ")", "{", "}", "[", "]")
    return this in operatorList
}

fun String.capitalizeFirst(): String {
    return (this.substring(0, 1).uppercase(Locale.getDefault())
            + this.substring(1).lowercase(Locale.getDefault()))
}


fun String.capitalizeWord(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
/***
 * Converts a date string of a given format to epoch
 */
fun String.dateStringToEpoch(format: String?): Long {
    val df = SimpleDateFormat(format, Locale.ROOT)
    try {
        val date = df.parse(this)
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0L
}

fun String.dateStringToEpoch(format: String?, addDays: Int): Long {
    val df = SimpleDateFormat(format, Locale.ROOT)
    try {
        val date = df.parse(this)
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, addDays)
        return c.time.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return 0L
}


fun Long.epochToDateString(formatString: String?): String? {
    val sdf = SimpleDateFormat(formatString, Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}

fun Long.epochToCalendar(): Calendar {
    val calendar: Calendar = Calendar.getInstance()
    calendar.time = Date(this)
    return calendar
}

/**
* return current data in string format
* */
fun currentDataInString(): String {
    return System.currentTimeMillis().epochToDateString("yyyy/MM/dd HH:mm:ss a")!!
}

fun currentTzDataInString(): String {
    return System.currentTimeMillis().epochToDateString("yyyy-MM-dd")!!+"T05:53:55.101Z"
}

fun String.tzToDate():String {
    return try {
        this.substring(0, 10)
    }catch (ex: Exception) {
        ""
    }
}

fun String.attributeToValue(neededKey: String) : String{
    val attributeValue = (this.drop(1).dropLast(1)).split(",")
    for (item in attributeValue) {
        val items = item.split(":")
        if (items.size==2) {
            val key  = items[0].drop(1).dropLast(1)
            val value  = items[1].drop(1).dropLast(1)
                .replace("\\","",true)
                .replace("\"","",true)

            if(key.contains(neededKey)) {
                return value
            }
        }
    }
    return ""
}

fun View.safeClick(listener: View.OnClickListener, blockInMillis: Long = 2000) {
    var lastClickTime: Long = 0
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        listener.onClick(this)
    }
}

fun String.tzToMillisecond(dateFormat: String = "yyyy-MM-dd HH:mm", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Long {
    var dateStr = this.replace("T", " ")
    dateStr = dateStr.replace("Z", " ")
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(dateStr).time
}

fun String.convertStringToDateObject(dateFormat: String = "yyyy-MM-dd HH:mm"): Long {
    try{
        val replaceTZ = this.replace("T", " ").replace("Z", "")
        return replaceTZ.toDate(dateFormat).time
    }catch (ex: Exception) {

    }
    return 0L
}

fun String.toDate(
    dateFormat: String = "yyyy-MM-dd HH:mm",
    timeZone: TimeZone = TimeZone.getTimeZone("UTC")
): Date {
    try{
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }catch (ex: Exception) {

    }
    return Calendar.getInstance().time
}

fun TextView.setAppearance(context: Context, res: Int) {
    if (Build.VERSION.SDK_INT < 23) {
        setTextAppearance(context, res)
    } else {
        setTextAppearance(res)
    }
}

fun String.convertEnglishToBanglaDigits(): String{
    val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    if (this == null) return ""
    val builder = StringBuilder()
    try {
        for (i in 0 until this.length) {
            if (Character.isDigit(this[i])) {
                if (this[i].toInt() - 48 <= 9) {
                    builder.append(banglaDigits[this[i].toInt() - 48])
                } else {
                    builder.append(this[i])
                }
            } else {
                builder.append(this[i])
            }
        }
    } catch (e: java.lang.Exception) {
        return ""
    }
    return builder.toString()
}

