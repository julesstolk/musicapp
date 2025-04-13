package com.blyss2idk.musicapp.data

data class Option(
    val title: String,
    val type: OptionType,
    var currentSetting: Int = 0,
    val icon: Int? = null,
    val minMax: Pair<Int, Int>? = null,
    val options: List<String>? = null
) {
    init {
        when(type) {
            OptionType.TOGGLE -> assert(minMax == null && options == null)
            OptionType.SLIDER -> assert(minMax != null && options == null)
            OptionType.DROPDOWN -> assert(minMax == null && options != null)
        }
    }
}