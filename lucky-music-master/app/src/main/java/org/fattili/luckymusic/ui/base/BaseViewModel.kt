package org.fattili.luckymusic.ui.base

import androidx.lifecycle.ViewModel


abstract class BaseViewModel : ViewModel() {
    /**
     * 初始化
     */
    abstract fun init()
}