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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.fattili.luckymusic.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LyricFragment : Fragment() {

    private lateinit var lyricEditText: EditText
    private lateinit var addLyricButton: Button
    private lateinit var lyricTextView: TextView
    private lateinit var importButton: Button
    private lateinit var exportButton: Button
    private lateinit var generateSongButton: Button
    private val lyrics = mutableListOf<String>()

    companion object {
        private const val REQUEST_CODE_IMPORT = 1
        private const val REQUEST_CODE_EXPORT = 2
        private const val REQUEST_CODE_SELECT_LYRICS = 3

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
        generateSongButton = view.findViewById(R.id.generateSongButton)

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
            exportLyrics()
        }

        generateSongButton.setOnClickListener {
            selectLyricFile()
        }
    }

    private fun selectLyricFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_LYRICS)
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
                    REQUEST_CODE_SELECT_LYRICS -> generateSong(uri)
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

    private fun generateSong(uri: Uri) {
        try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val lyrics = reader.readText()
            callSunoApiToGenerateSong(lyrics)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "读取歌词文件失败", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun callSunoApiToGenerateSong(lyrics: String) {
        val apiUrl = "https://cdn1.suno.ai/ab47e546-38de-4949-b362-c2019ad8e5ff.mp3"  // 示例URL，请替换为实际的API URL


        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true

                val requestBody = "{\"lyrics\": \"$lyrics\"}"
                connection.outputStream.use { os ->
                    os.write(requestBody.toByteArray())
                }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val response = connection.inputStream.bufferedReader().readText()
                    // 假设API返回的JSON中包含一个MP3文件的URL
                    val mp3Url = extractMp3UrlFromResponse(response)
                    downloadMp3(mp3Url)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "生成歌曲失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "调用API失败", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }

    private fun extractMp3UrlFromResponse(response: String): String {
        // 解析API返回的JSON，提取MP3文件的URL
        // 示例实现，请根据实际API返回格式进行调整
        return JSONObject(response).getString("mp3_url")
    }

    private fun downloadMp3(mp3Url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(mp3Url)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val mp3File = File(requireContext().filesDir, "generated_song.mp3")
                    connection.inputStream.use { input ->
                        mp3File.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "歌曲下载成功", Toast.LENGTH_SHORT).show()
                        // 可以在这里播放下载的MP3文件
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "下载歌曲失败", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "下载歌曲失败", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }
    }
}
