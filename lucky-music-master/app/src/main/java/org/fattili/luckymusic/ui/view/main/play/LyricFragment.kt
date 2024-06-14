package org.fattili.luckymusic.ui.view.main.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class LyricFragment : Fragment() {

    private lateinit var lyricEditText: EditText
    private lateinit var addLyricButton: Button
    private lateinit var lyricTextView: TextView
    private val lyrics = mutableListOf<String>() // 存储歌词的列表

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(org.fattili.luckymusic.R.layout.lm_play_fragment_lyric, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lyricEditText = view.findViewById(org.fattili.luckymusic.R.id.lyricEditText)
        addLyricButton = view.findViewById(org.fattili.luckymusic.R.id.addLyricButton)
        lyricTextView = view.findViewById(org.fattili.luckymusic.R.id.lyricTextView)

        addLyricButton.setOnClickListener {
            val newLyric = lyricEditText.text.toString()
            if (newLyric.isNotBlank()) {
                lyrics.add(newLyric) // 将新歌词添加到列表中
                updateLyricDisplay() // 更新显示
                lyricEditText.text.clear()  // 清空输入框
            }
        }
    }

    private fun updateLyricDisplay() {
        // 将所有歌词合并为一个字符串并显示在TextView中
        lyricTextView.text = lyrics.joinToString("\n")
    }

    companion object {
        fun newInstance(): LyricFragment = LyricFragment()
    }
}