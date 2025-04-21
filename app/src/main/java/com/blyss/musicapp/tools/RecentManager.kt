package com.blyss.musicapp.tools

import com.blyss.musicapp.data.Playable

object RecentManager {

    private var recents = mutableListOf<Playable>()
    val cap = -1

    fun addRecent(playable: Playable) {
        // recents.remove(playable)

        recents.add(playable)
        if (cap != -1) {
            while (recents.size > cap) {
                recents = recents.slice(1..recents.size-1).toMutableList()
            }
        }
    }

    fun getRecents(): List<Playable> {
        val temp = recents.toMutableList()
        temp.reverse()
        return temp
    }
}