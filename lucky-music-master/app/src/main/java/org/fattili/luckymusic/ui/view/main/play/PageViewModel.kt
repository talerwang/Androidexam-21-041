package org.fattili.luckymusic.ui.view.main.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class PageViewModel : ViewModel() {
    private val mIndex =
        MutableLiveData<Int>()
    val text =
        Transformations.map<Int, String>(
            mIndex
        ) { input -> "Hello world from section: $input" }

    fun setIndex(index: Int) {
        mIndex.value = index
    }

}