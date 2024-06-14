package org.fattili.luckymusic.ui.view.main.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.fattili.luckymusic.data.SongRepository
import org.fattili.luckymusic.data.SongsRepository


class PlayModelFactory(private val repository: SongRepository,private val songsRepository: SongsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayViewModel(
            repository,songsRepository
        ) as T
    }
}