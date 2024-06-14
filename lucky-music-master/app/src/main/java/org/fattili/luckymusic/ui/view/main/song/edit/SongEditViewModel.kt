package org.fattili.luckymusic.ui.view.main.song.edit

import android.text.Editable
import android.text.TextWatcher
import org.fattili.luckymusic.data.SongRepository
import org.fattili.luckymusic.data.model.play.Song
import org.fattili.luckymusic.ui.base.BaseViewModel
import org.fattili.luckymusic.util.Logger


class SongEditViewModel(private val repository: SongRepository) : BaseViewModel() {

    var songId = 0L

    var song: Song? = null

    override fun init() {
        song = getSong(songId)
        Logger.d("init:${songId}")
    }

    fun getTitle(): String {
        return song?.title ?: ""
    }

    fun save() {
        song?.let { repository.edit(it) }
    }


    fun getSong(songId: Long): Song? {
        return repository.getSong(songId)
    }
}