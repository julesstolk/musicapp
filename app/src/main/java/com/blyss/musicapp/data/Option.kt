package com.blyss.musicapp.data

import com.blyss.musicapp.R

data class Option(
    val title: String,
    val type: OptionType,
    var currentSetting: Int = 0,
    val icon: Int = R.drawable.baseline_question_mark_24,
    val minMax: Pair<Int, Int>? = null,
    val options: List<String>? = null,
    val onChange: () -> Unit = { null }
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