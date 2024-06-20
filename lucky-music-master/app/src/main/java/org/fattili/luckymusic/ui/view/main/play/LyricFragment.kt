package org.fattili.luckymusic.ui.view.main.play

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.fattili.luckymusic.R
import java.io.BufferedReader
import java.io.InputStreamReader

class LyricFragment : Fragment() {

    private lateinit var lyricEditText: EditText
    private lateinit var addLyricButton: Button
    private lateinit var lyricTextView: TextView
    private lateinit var importButton: Button
    private lateinit var exportButton: Button
    private val lyrics = mutableListOf<String>()

    companion object {
        private const val REQUEST_CODE_IMPORT = 1
        private const val REQUEST_CODE_EXPORT = 2

        fun newInstance(): LyricFragment = LyricFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lm_play_fragment_lyric, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lyricEditText = view.findViewById(R.id.lyricEditText)
        addLyricButton = view.findViewById(R.id.addLyricButton)
        lyricTextView = view.findViewById(R.id.lyricTextView)
        importButton = view.findViewById(R.id.importButton)
        exportButton = view.findViewById(R.id.exportButton)

        addLyricButton.setOnClickListener {
            val newLyric = lyricEditText.text.toString()
            if (newLyric.isNotBlank()) {
                lyrics.add(newLyric)
                updateLyricDisplay()
                lyricEditText.text.clear()
            }
        }

        importButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/*"
            startActivityForResult(intent, REQUEST_CODE_IMPORT)
        }

        exportButton.setOnClickListener {
            // 实现导出功能
            exportLyrics()
        }
    }

    private fun updateLyricDisplay() {
        lyricTextView.text = lyrics.joinToString("\n")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                when (requestCode) {
                    REQUEST_CODE_IMPORT -> importLyrics(uri)
                    REQUEST_CODE_EXPORT -> exportLyricsToFile(uri)
                }
            }
        }
    }

    private fun importLyrics(uri: Uri) {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val importedLyrics = reader.readLines()
            lyrics.clear()
            lyrics.addAll(importedLyrics)
            updateLyricDisplay()
            Toast.makeText(requireContext(), "歌词导入成功", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "导入歌词失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun exportLyrics() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TITLE, "lyrics.txt")
        startActivityForResult(intent, REQUEST_CODE_EXPORT)
    }

    private fun exportLyricsToFile(uri: Uri) {
        try {
            val outputStream = requireActivity().contentResolver.openOutputStream(uri)
            outputStream?.bufferedWriter()?.use { writer ->
                writer.write(lyrics.joinToString("\n"))
            }
            Toast.makeText(requireContext(), "歌词导出成功", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "导出歌词失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }
}