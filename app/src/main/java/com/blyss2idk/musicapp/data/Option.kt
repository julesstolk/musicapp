package com.blyss2idk.musicapp.data

import android.util.Log

data class Option(
    val title: String,
    val type: OptionType,
    var currentSetting: Int = 0,
    val icon: Int? = null,
    val minMax: Pair<Int, Int>? = null,
    val options: List<String>? = null,
    val onChange: () -> Unit = { doNothing() }
) {
    
    init {
        when(type) {
            OptionType.TOGGLE -> assert(minMax == null && options == null)
            OptionType.BUTTON -> assert(minMax == null && options == null)
            OptionType.SLIDER -> assert(minMax != null && options == null)
            OptionType.DROPDOWN -> assert(minMax == null && options != null)
        }
    }

    fun setSetting(new: Int) {
        if (type == OptionType.SLIDER && minMax != null) {
            assert(new in minMax.first..minMax.second)
        } else if (type == OptionType.TOGGLE) {
            assert(new == 0 || new == 1)
        } else if (type == OptionType.DROPDOWN) {
            assert(new < options!!.size)
        }
        currentSetting = new
        onChange.invoke()
        if (type == OptionType.BUTTON && new == 1) {
            currentSetting = 0
        }
    }
}

fun doNothing(){
    // do nothing
    Log.w("OPTION", "doNothing() called, should not happen")
}