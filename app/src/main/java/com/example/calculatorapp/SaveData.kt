package com.example.calculatorapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SaveData (
    private val context: Context
        ){
     private fun saveDataToFile(
        data: String,
        fileName: String,

    ){
        val file = File(context.filesDir,fileName)
        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(data.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun readDataFromFile(
        fileName: String
    )
    : String {
        val file = File(context.filesDir, fileName)
        val stringBuilder = StringBuilder()
        try {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                stringBuilder.append(String(buffer, 0, bytesRead))
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
    fun saveListToFile(fileName: String, list: List<String>) {
        val data = list.joinToString("\n").trim()
        saveDataToFile(data, fileName)
    }
    fun loadListFromFile(fileName: String, context: Context): MutableList<String> {
        val data = readDataFromFile(fileName)
        return if (data.isNotBlank()) {
            data.split("\n").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    fun saveSettingsData(key: String, value: String) {
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
        }
    fun getSettingsData(key: String, defaultValue: String): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }
    }
